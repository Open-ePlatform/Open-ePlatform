package com.nordicpeak.flowengine;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import se.unlogic.hierarchy.core.annotations.CheckboxSettingDescriptor;
import se.unlogic.hierarchy.core.annotations.GroupMultiListSettingDescriptor;
import se.unlogic.hierarchy.core.annotations.InstanceManagerDependency;
import se.unlogic.hierarchy.core.annotations.ModuleSetting;
import se.unlogic.hierarchy.core.annotations.TextFieldSettingDescriptor;
import se.unlogic.hierarchy.core.annotations.WebPublic;
import se.unlogic.hierarchy.core.annotations.XSLVariable;
import se.unlogic.hierarchy.core.beans.LinkTag;
import se.unlogic.hierarchy.core.beans.ScriptTag;
import se.unlogic.hierarchy.core.beans.SimpleForegroundModuleResponse;
import se.unlogic.hierarchy.core.beans.SimpleViewFragment;
import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.hierarchy.core.enums.ResponseType;
import se.unlogic.hierarchy.core.exceptions.AccessDeniedException;
import se.unlogic.hierarchy.core.exceptions.URINotFoundException;
import se.unlogic.hierarchy.core.interfaces.AccessInterface;
import se.unlogic.hierarchy.core.interfaces.ForegroundModuleResponse;
import se.unlogic.hierarchy.core.interfaces.SectionInterface;
import se.unlogic.hierarchy.core.interfaces.ViewFragment;
import se.unlogic.hierarchy.core.interfaces.modules.descriptors.ForegroundModuleDescriptor;
import se.unlogic.hierarchy.core.utils.AccessUtils;
import se.unlogic.hierarchy.core.utils.ModuleViewFragmentTransformer;
import se.unlogic.hierarchy.core.utils.ViewFragmentModule;
import se.unlogic.hierarchy.core.utils.usergrouplist.UserGroupListConnector;
import se.unlogic.hierarchy.foregroundmodules.AnnotatedForegroundModule;
import se.unlogic.hierarchy.foregroundmodules.staticcontent.StaticContentModule;
import se.unlogic.standardutils.collections.CollectionUtils;
import se.unlogic.standardutils.dao.AnnotatedDAO;
import se.unlogic.standardutils.dao.HighLevelQuery;
import se.unlogic.standardutils.dao.QueryParameterFactory;
import se.unlogic.standardutils.dao.TransactionHandler;
import se.unlogic.standardutils.numbers.NumberUtils;
import se.unlogic.standardutils.validation.NonNegativeStringIntegerValidator;
import se.unlogic.standardutils.validation.ValidationError;
import se.unlogic.standardutils.validation.ValidationErrorType;
import se.unlogic.standardutils.xml.XMLUtils;
import se.unlogic.webutils.http.RequestUtils;
import se.unlogic.webutils.http.URIParser;

import com.nordicpeak.flowengine.beans.APIAccessSetting;
import com.nordicpeak.flowengine.beans.Flow;
import com.nordicpeak.flowengine.beans.FlowAdminExtensionShowView;
import com.nordicpeak.flowengine.beans.FlowFamily;
import com.nordicpeak.flowengine.dao.FlowEngineDAOFactory;
import com.nordicpeak.flowengine.interfaces.FlowAdminFragmentExtensionViewProvider;

public class APIAccessModule extends AnnotatedForegroundModule implements FlowAdminFragmentExtensionViewProvider, ViewFragmentModule<ForegroundModuleDescriptor>, AccessInterface {

	@XSLVariable(prefix = "java.")
	private String extensionViewTitle = "API access functions";

	@XSLVariable(prefix = "java.")
	private String settingsUpdated = "API access settings updated";

	@XSLVariable(prefix = "java.")
	private String settingsRemoved = "Inactivated API access";

	@ModuleSetting
	@GroupMultiListSettingDescriptor(name = "Allowed groups", description = "Groups from which Users allowed to access APIs can be chosen from.", required = true)
	protected List<Integer> allowedGroupIDs;

	@ModuleSetting
	@TextFieldSettingDescriptor(name = "Priority", description = "The priority of this extension provider compared to other providers. A low value means a higher priority. Valid values are 0 - " + Integer.MAX_VALUE + ".", required = true, formatValidator = NonNegativeStringIntegerValidator.class)
	protected int priority = 100;

	@ModuleSetting
	@CheckboxSettingDescriptor(name = "Enable fragment XML debug", description = "Enables debugging of fragment XML")
	private boolean debugFragmentXML;

	private AnnotatedDAO<APIAccessSetting> settingsDAO;

	protected QueryParameterFactory<APIAccessSetting, Integer> settingsFlowFamilyIDParamFactory;
	protected QueryParameterFactory<APIAccessSetting, Integer> settingsUserIDParamFactory;

	@InstanceManagerDependency(required = true)
	private StaticContentModule staticContentModule;

	private FlowAdminModule flowAdminModule;
	
	protected UserGroupListConnector userGroupListConnector;

	private ModuleViewFragmentTransformer<ForegroundModuleDescriptor> viewFragmentTransformer;

	@Override
	public void init(ForegroundModuleDescriptor moduleDescriptor, SectionInterface sectionInterface, DataSource dataSource) throws Exception {

		viewFragmentTransformer = new ModuleViewFragmentTransformer<ForegroundModuleDescriptor>(sectionInterface.getForegroundModuleXSLTCache(), this, sectionInterface.getSystemInterface().getEncoding());
		
		super.init(moduleDescriptor, sectionInterface, dataSource);
		
		userGroupListConnector = new UserGroupListConnector(systemInterface);
		userGroupListConnector.setUserGroupFilter(allowedGroupIDs);

		if (!systemInterface.getInstanceHandler().addInstance(APIAccessModule.class, this)) {

			throw new RuntimeException("Unable to register module in global instance handler using key " + APIAccessModule.class.getSimpleName() + ", another instance is already registered using this key.");
		}
	}

	@Override
	protected void createDAOs(DataSource dataSource) throws Exception {

		super.createDAOs(dataSource);

		FlowEngineDAOFactory daoFactory = new FlowEngineDAOFactory(dataSource, systemInterface.getUserHandler(), systemInterface.getGroupHandler());

		settingsDAO = daoFactory.getApiAccessSettingDAO();

		settingsFlowFamilyIDParamFactory = settingsDAO.getParamFactory("flowFamilyID", Integer.class);
		settingsUserIDParamFactory = settingsDAO.getParamFactory("userID", Integer.class);
	}

	@Override
	protected void moduleConfigured() throws Exception {

		viewFragmentTransformer.setDebugXML(debugFragmentXML);
		viewFragmentTransformer.modifyScriptsAndLinks(true, null);
		
		if (userGroupListConnector != null) {
			userGroupListConnector.setUserGroupFilter(allowedGroupIDs);
		}
	}
	
	@InstanceManagerDependency(required = true)
	public void setFlowAdminModule(FlowAdminModule flowAdminModule) {

		if (flowAdminModule == null && this.flowAdminModule != null) {

			this.flowAdminModule.removeFragmentExtensionViewProvider(this);
		}

		this.flowAdminModule = flowAdminModule;

		if (this.flowAdminModule != null) {

			this.flowAdminModule.addFragmentExtensionViewProvider(this);
		}
	}

	@Override
	public void unload() throws Exception {

		systemInterface.getInstanceHandler().removeInstance(APIAccessModule.class, this);
		
		if (flowAdminModule != null) {

			setFlowAdminModule(null);
		}

		super.unload();
	}

	public ForegroundModuleResponse updateSettings(String extensionRequestURL, Flow flow, HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws URINotFoundException, SQLException, IOException {

		FlowFamily flowFamily = flow.getFlowFamily();
		Integer flowFamilyID = flowFamily.getFlowFamilyID();

		List<ValidationError> validationErrors = null;

		if (req.getMethod().equalsIgnoreCase("POST")) {

			validationErrors = new ArrayList<ValidationError>();

			String[] userIDStrings = req.getParameterValues("users");
			List<User> users = null;

			if (userIDStrings != null) {

				List<Integer> userIDs = new ArrayList<>(userIDStrings.length);

				for (String userIDString : userIDStrings) {

					Integer userID = NumberUtils.toInt(userIDString);

					if (userID != null) {

						userIDs.add(userID);

					} else {

						validationErrors.add(new ValidationError("users", ValidationErrorType.InvalidFormat));
					}
				}

				if (userIDs.size() > 0) {

					users = systemInterface.getUserHandler().getUsers(userIDs, true, false);

					if (CollectionUtils.getSize(users) != userIDs.size()) {

						validationErrors.add(new ValidationError("users", ValidationErrorType.InvalidFormat));
						
					} else {
						
						for (User apiUser : users) {
							
							if (!AccessUtils.checkAccess(apiUser, this)) {
								
								validationErrors.add(new ValidationError("users", ValidationErrorType.InvalidFormat));
							}
						}
					}
				}
			}

			// Store settings
			if (validationErrors.isEmpty()) {

				if (users == null) {

					HighLevelQuery<APIAccessSetting> deleteQuery = new HighLevelQuery<APIAccessSetting>();
					deleteQuery.addParameter(settingsFlowFamilyIDParamFactory.getParameter(flowFamilyID));

					if (settingsDAO.delete(deleteQuery) > 0) {

						flowAdminModule.addFlowFamilyEvent(settingsRemoved, flowAdminModule.getLatestFlowVersion(flowFamily), user);
					}

					res.sendRedirect(uriParser.getFullContextPath() + flowAdminModule.getFullAlias() + "/showflow/" + flowAdminModule.getLatestFlowVersion(flowFamily).getFlowID() + "#culling");
					return null;

				} else {

					TransactionHandler transaction = null;

					try {

						transaction = new TransactionHandler(dataSource);

						HighLevelQuery<APIAccessSetting> deleteQuery = new HighLevelQuery<APIAccessSetting>();
						deleteQuery.addParameter(settingsFlowFamilyIDParamFactory.getParameter(flowFamilyID));
						settingsDAO.delete(deleteQuery, transaction);

						// Archived
						for (User allowedAPIUser : users) {

							log.info("User " + user + " added user " + allowedAPIUser + " as an allowed API user for for flow family " + flowFamily);

							settingsDAO.add(new APIAccessSetting(flowFamilyID, allowedAPIUser.getUserID()), transaction, null);
						}

						transaction.commit();

						flowAdminModule.addFlowFamilyEvent(settingsUpdated, flowAdminModule.getLatestFlowVersion(flowFamily), user);

					} finally {
						TransactionHandler.autoClose(transaction);
					}

					res.sendRedirect(uriParser.getFullContextPath() + flowAdminModule.getFullAlias() + "/showflow/" + flowAdminModule.getLatestFlowVersion(flowFamily).getFlowID() + "#culling");
					return null;
				}
			}
		}

		List<APIAccessSetting> settings = getSettings(flowFamilyID);

		log.info("User " + user + " viewing API access settings for flow family " + flowFamily);

		Document doc = createDocument(req, uriParser, user);
		Element updateElement = doc.createElement("Update");
		doc.getFirstChild().appendChild(updateElement);
		
		XMLUtils.append(doc, updateElement, flowFamily);
		XMLUtils.append(doc, updateElement, "APIUsers", getSettingUsers(settings));
		
		XMLUtils.appendNewElement(doc, updateElement, "VersionName", flow.getName());
		XMLUtils.appendNewElement(doc, updateElement, "extensionRequestURL", extensionRequestURL);

		if (!CollectionUtils.isEmpty(validationErrors)) {

			updateElement.appendChild(RequestUtils.getRequestParameters(req, doc));
			XMLUtils.append(doc, updateElement, "ValidationErrors", validationErrors);
		}

		return new SimpleForegroundModuleResponse(doc);
	}

	private List<APIAccessSetting> getSettings(Integer flowFamilyID) throws SQLException {

		HighLevelQuery<APIAccessSetting> query = new HighLevelQuery<APIAccessSetting>();
		query.addParameter(settingsFlowFamilyIDParamFactory.getParameter(flowFamilyID));

		return settingsDAO.getAll(query);
	}
	
	private List<User> getSettingUsers(List<APIAccessSetting> settings) {
		
		if (CollectionUtils.getSize(settings) > 0) {
			
			List<Integer> userIDs = new ArrayList<>(settings.size());

			for (APIAccessSetting setting : settings) {
				
				userIDs.add(setting.getUserID());
			}
			
			return systemInterface.getUserHandler().getUsers(userIDs, false, false);
		}
		
		return null;
	}

	@Override
	public FlowAdminExtensionShowView getShowView(String extensionRequestURL, Flow flow, HttpServletRequest req, User user, URIParser uriParser) throws TransformerConfigurationException, TransformerException, SQLException {

		if (!AccessUtils.checkRecursiveModuleAccess(user, moduleDescriptor, systemInterface)) {
			return null;
		}

		Document doc = createDocument(req, uriParser, user);

		Element showViewElement = doc.createElement("Show");
		doc.getDocumentElement().appendChild(showViewElement);

		XMLUtils.append(doc, showViewElement, flow);

		List<APIAccessSetting> settings = getSettings(flow.getFlowFamily().getFlowFamilyID());
		List<User> users = getSettingUsers(settings);

		XMLUtils.append(doc, showViewElement, "APIUsers", users);

		XMLUtils.appendNewElement(doc, showViewElement, "extensionRequestURL", extensionRequestURL);

		return new FlowAdminExtensionShowView(viewFragmentTransformer.createViewFragment(doc, true), settings != null);
	}

	@Override
	public ViewFragment processRequest(String extensionRequestURL, Flow flow, HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Exception {

		if (!AccessUtils.checkAccess(user, this) && !AccessUtils.checkRecursiveModuleAccess(user, moduleDescriptor, systemInterface)) {
			throw new AccessDeniedException("User is not allowed to remove flow instances");
		}

		String method = uriParser.get(4);

		if ("update".equals(method)) {

			return getViewFragmentResponse(updateSettings(extensionRequestURL, flow, req, res, user, uriParser));

		} else if ("users".equals(method)) {

			userGroupListConnector.getUsers(req, res, user, uriParser);
			return null;

		} else if ("toflow".equals(method)) {

			return null;
		}

		throw new URINotFoundException(uriParser);
	}

	private ViewFragment getViewFragmentResponse(ForegroundModuleResponse foregroundModuleResponse) throws TransformerConfigurationException, TransformerException {

		if (foregroundModuleResponse != null) {

			if (foregroundModuleResponse.getResponseType() == ResponseType.XML_FOR_SEPARATE_TRANSFORMATION) {

				return viewFragmentTransformer.createViewFragment(foregroundModuleResponse.getDocument());

			} else {

				log.warn("Scripts and links have not been modified for FlowAdminFragmentExtensionViewProviderProcessRequest");
				return new SimpleViewFragment(foregroundModuleResponse.getHtml(), debugFragmentXML ? foregroundModuleResponse.getDocument() : null, foregroundModuleResponse.getScripts(), foregroundModuleResponse.getLinks());
			}
		}

		return null;
	}

	public Document createDocument(HttpServletRequest req, URIParser uriParser, User user) {

		Document doc = XMLUtils.createDomDocument();
		Element document = doc.createElement("Document");
		document.appendChild(RequestUtils.getRequestInfoAsXML(doc, req, uriParser));
		document.appendChild(this.moduleDescriptor.toXML(doc));

		XMLUtils.appendNewElement(doc, document, "ModuleURI", req.getContextPath() + getFullAlias());
		XMLUtils.appendNewElement(doc, document, "StaticContentURL", staticContentModule.getModuleContentURL(moduleDescriptor));

		doc.appendChild(document);

		return doc;
	}
	
	@WebPublic(alias = "users")
	public ForegroundModuleResponse getUsers(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Throwable {

		return userGroupListConnector.getUsers(req, res, user, uriParser);
	}
	
	public boolean hasAccess(Integer familyID, Integer userID) throws SQLException {
	
		HighLevelQuery<APIAccessSetting> query = new HighLevelQuery<APIAccessSetting>();
		query.addParameter(settingsFlowFamilyIDParamFactory.getParameter(familyID));
		query.addParameter(settingsUserIDParamFactory.getParameter(userID));

		return settingsDAO.get(query) != null;
	}
	
	public boolean hasAccess(FlowFamily family, User user) throws SQLException {
		
		return hasAccess(family.getFlowFamilyID(), user.getUserID());
	}
	
	public boolean hasAccess(Flow flow, User user) throws SQLException {
		
		FlowFamily flowFamily = flow.getFlowFamily();
		
		if (flowFamily == null) {
			
			Flow cachedFlow = flowAdminModule.getCachedFlow(flow.getFlowID());
			
			if (cachedFlow != null) {
				flowFamily = cachedFlow.getFlowFamily();
			}
		}
		
		if (flowFamily != null) {
			return hasAccess(flowFamily, user);
		}
		
		log.warn("Unable to determine API access to flow " + flow + " for user " + user + ", flow family not found");
		return false;
	}
	
	public void accessCheck(Integer flowFamilyID, User user) throws SQLException, AccessDeniedException {
	
		if (!hasAccess(flowFamilyID, user.getUserID())) {
			
			log.warn("User " + user + " does not have API access to flow family with ID " + flowFamilyID);
			throw new AccessDeniedException("User is not allowed to access APIs for flow family with ID " + flowFamilyID);
		}
	}
	
	public void accessCheck(FlowFamily family, User user) throws SQLException, AccessDeniedException {
	
		if (!hasAccess(family, user)) {
			
			log.warn("User " + user + " does not have API access to flow family " + family);
			throw new AccessDeniedException("User is not allowed to access APIs for flow family " + family);
		}
	}
	
	public void accessCheck(Flow flow, User user) throws SQLException, AccessDeniedException {
	
		FlowFamily flowFamily = flow.getFlowFamily();
		
		if (flowFamily == null) {
			
			Flow cachedFlow = flowAdminModule.getCachedFlow(flow.getFlowID());
			
			if (cachedFlow != null) {
				flowFamily = cachedFlow.getFlowFamily();
			}
		}
		
		if (flowFamily != null) {
			accessCheck(flowFamily, user);
		}
		
		log.warn("Unable to determine API access to flow " + flow + " for user " + user + ", flow family not found");
		throw new AccessDeniedException("Unable to determine API access to flow " + flow + " for user " + user + ", flow family not found");
	}
	
	@Override
	public int getPriority() {
		return priority;
	}

	@Override
	public ForegroundModuleDescriptor getModuleDescriptor() {
		return moduleDescriptor;
	}

	@Override
	public List<LinkTag> getLinkTags() {
		return links;
	}

	@Override
	public List<ScriptTag> getScriptTags() {
		return scripts;
	}

	@Override
	public String getExtensionViewTitle() {
		return extensionViewTitle;
	}

	@Override
	public String getExtensionViewLinkName() {
		return "apiaccess";
	}

	@Override
	public int getModuleID() {
		return moduleDescriptor.getModuleID();
	}

	@Override
	public boolean allowsAdminAccess() {
		return false;
	}

	@Override
	public boolean allowsUserAccess() {
		return false;
	}

	@Override
	public boolean allowsAnonymousAccess() {
		return false;
	}

	@Override
	public Collection<Integer> getAllowedGroupIDs() {
		return allowedGroupIDs;
	}

	@Override
	public Collection<Integer> getAllowedUserIDs() {
		return null;
	}

}
