package com.nordicpeak.flowengine.sharing;

import java.io.IOException;
import java.lang.reflect.Field;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import javax.sql.rowset.serial.SerialBlob;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import se.unlogic.hierarchy.core.annotations.DropDownSettingDescriptor;
import se.unlogic.hierarchy.core.annotations.GroupMultiListSettingDescriptor;
import se.unlogic.hierarchy.core.annotations.HTMLEditorSettingDescriptor;
import se.unlogic.hierarchy.core.annotations.ModuleSetting;
import se.unlogic.hierarchy.core.annotations.TextFieldSettingDescriptor;
import se.unlogic.hierarchy.core.annotations.UserMultiListSettingDescriptor;
import se.unlogic.hierarchy.core.annotations.WebPublic;
import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.hierarchy.core.interfaces.AccessInterface;
import se.unlogic.hierarchy.core.interfaces.ForegroundModuleDescriptor;
import se.unlogic.hierarchy.core.interfaces.ForegroundModuleResponse;
import se.unlogic.hierarchy.core.interfaces.SectionInterface;
import se.unlogic.hierarchy.core.utils.CRUDCallback;
import se.unlogic.hierarchy.core.utils.HierarchyAnnotatedDAOFactory;
import se.unlogic.hierarchy.foregroundmodules.rest.AnnotatedRESTModule;
import se.unlogic.hierarchy.foregroundmodules.rest.RESTMethod;
import se.unlogic.hierarchy.foregroundmodules.rest.URIParam;
import se.unlogic.hierarchy.foregroundmodules.rest.responsehandlers.XMLResponseHandler;
import se.unlogic.standardutils.arrays.ArrayUtils;
import se.unlogic.standardutils.base64.Base64;
import se.unlogic.standardutils.collections.CollectionUtils;
import se.unlogic.standardutils.dao.AnnotatedDAO;
import se.unlogic.standardutils.dao.AnnotatedDAOWrapper;
import se.unlogic.standardutils.dao.HighLevelQuery;
import se.unlogic.standardutils.dao.LowLevelQuery;
import se.unlogic.standardutils.dao.QueryParameterFactory;
import se.unlogic.standardutils.db.tableversionhandler.TableVersionHandler;
import se.unlogic.standardutils.db.tableversionhandler.UpgradeResult;
import se.unlogic.standardutils.db.tableversionhandler.XMLDBScriptProvider;
import se.unlogic.standardutils.hash.HashAlgorithms;
import se.unlogic.standardutils.hash.HashUtils;
import se.unlogic.standardutils.validation.ValidationError;
import se.unlogic.standardutils.validation.ValidationErrorType;
import se.unlogic.standardutils.validation.ValidationException;
import se.unlogic.standardutils.xml.XMLUtils;
import se.unlogic.webutils.http.BeanRequestPopulator;
import se.unlogic.webutils.http.HTTPUtils;
import se.unlogic.webutils.http.RequestUtils;
import se.unlogic.webutils.http.URIParser;
import se.unlogic.webutils.http.enums.ContentDisposition;
import se.unlogic.webutils.populators.annotated.AnnotatedRequestPopulator;

import com.nordicpeak.flowengine.sharing.beans.Repository;
import com.nordicpeak.flowengine.sharing.beans.SharedFlow;
import com.nordicpeak.flowengine.sharing.beans.Source;
import com.nordicpeak.flowengine.sharing.beans.SourceUser;
import com.nordicpeak.flowengine.sharing.cruds.SourceCRUD;

public class FlowRepositoryModule extends AnnotatedRESTModule implements CRUDCallback<User>, AccessInterface {

	private static BeanRequestPopulator<SharedFlow> SHARED_FLOW_POPULATOR = new AnnotatedRequestPopulator<SharedFlow>(SharedFlow.class);

	@ModuleSetting
	@HTMLEditorSettingDescriptor(name = "Repository description", description = "Description shown for this repository")
	private String repositoryDescription;

	@ModuleSetting
	@HTMLEditorSettingDescriptor(name = "Repository share form description", description = "Description show when uploading flows to this repository")
	private String repositoryUploadDescription;
	
	@ModuleSetting(allowsNull = true)
	@GroupMultiListSettingDescriptor(name = "Admin groups", description = "Groups allowed to administrate sources")
	protected List<Integer> adminGroupIDs;

	@ModuleSetting(allowsNull = true)
	@UserMultiListSettingDescriptor(name = "Admin users", description = "Users allowed to administrate sources")
	protected List<Integer> adminUserIDs;
	
	@ModuleSetting
	@TextFieldSettingDescriptor(name="Realm", description="The name of this realm", required = true)
	private String realm = "Protected";
	
	@ModuleSetting
	@DropDownSettingDescriptor(name="Password algorithm",description="The algorithm used for password hashing",required=true,values={"MD5", "SHA-256", "SHA-384", "SHA-512"},valueDescriptions={"MD5", "SHA-256", "SHA-384", "SHA-512"})
	protected String passwordAlgorithm = HashAlgorithms.SHA256;

	private AnnotatedDAO<Source> sourceDAO;
	private AnnotatedDAO<Repository> repositoryDAO;
	private AnnotatedDAO<SharedFlow> sharedFlowDAO;

//	private QueryParameterFactory<SharedFlow, Integer> sharedFlowIDParamFactory;
	private QueryParameterFactory<SharedFlow, Integer> sharedFlowFlowIDParamFactory;
	private QueryParameterFactory<SharedFlow, Integer> sharedFlowFamilyIDParamFactory;
	private QueryParameterFactory<SharedFlow, Source> sharedFlowSourceParamFactory;

	private SourceCRUD sourceCRUD;
	private Repository repository;

	@Override
	public void init(ForegroundModuleDescriptor descriptor, SectionInterface sectionInterface, DataSource dataSource) throws Exception {

		super.init(descriptor, sectionInterface, dataSource);

		addResponseHandler(new XMLResponseHandler(systemInterface.getEncoding()));
	}

	@Override
	protected void createDAOs(DataSource dataSource) throws Exception {

		//Automatic table version handling
		UpgradeResult upgradeResult = TableVersionHandler.upgradeDBTables(dataSource, FlowRepositoryModule.class.getName(), new XMLDBScriptProvider(this.getClass().getResourceAsStream("DB script.xml")));

		if (upgradeResult.isUpgrade()) {

			log.info(upgradeResult.toString());
		}

		HierarchyAnnotatedDAOFactory daoFactory = new HierarchyAnnotatedDAOFactory(dataSource, systemInterface.getUserHandler(), systemInterface.getGroupHandler(), false, false, false);

		sourceDAO = daoFactory.getDAO(Source.class);
		repositoryDAO = daoFactory.getDAO(Repository.class);
		sharedFlowDAO = daoFactory.getDAO(SharedFlow.class);

		sourceCRUD = new SourceCRUD(sourceDAO.getWrapper(Integer.class), this);
		
//		sharedFlowIDParamFactory = sharedFlowDAO.getParamFactory("sharedFlowID", Integer.class);
		sharedFlowFlowIDParamFactory = sharedFlowDAO.getParamFactory("flowID", Integer.class);
		sharedFlowSourceParamFactory = sharedFlowDAO.getParamFactory("source", Source.class);
		sharedFlowFamilyIDParamFactory = sharedFlowDAO.getParamFactory("flowFamilyID", Integer.class);

		AnnotatedDAOWrapper<Repository, Integer> repositoryDAOWrapper = repositoryDAO.getWrapper(Integer.class);

		if ((repository = repositoryDAOWrapper.get(getRepositoryID())) == null) {

			repository = new Repository();
			repository.setRepositoryID(getRepositoryID());

			log.info("Creating repository " + repository);
			repositoryDAO.add(repository);
		}
	}

	public String getRepositoryName() {

		return moduleDescriptor.getName();
	}

	public Integer getRepositoryID() {

		return moduleDescriptor.getModuleID();
	}
	
	public String getHashedPassword(String password) {

		if (password == null) {
			return null;
		}

		return HashUtils.hash(password, passwordAlgorithm, "UTF-8");
	}

	protected Source getSource(String username, String password, Field... extraRelations) throws SQLException {

		LowLevelQuery<Source> query = new LowLevelQuery<Source>("SELECT * FROM " + sourceDAO.getTableName() + " WHERE repositoryID = ? AND username = ? AND password = ?;");

		query.addParameter(repository.getRepositoryID());
		query.addParameter(username);
		query.addParameter(getHashedPassword(password));

		if (!ArrayUtils.isEmpty(extraRelations)) {

			query.addRelations(extraRelations);
		}

		return sourceDAO.get(query);
	}
	
	public Source getSource(String username, Field... extraRelations) throws SQLException {

		LowLevelQuery<Source> query = new LowLevelQuery<Source>("SELECT * FROM " + sourceDAO.getTableName() + " WHERE repositoryID = ? AND username = ?;");

		query.addParameter(repository.getRepositoryID());
		query.addParameter(username);

		if (!ArrayUtils.isEmpty(extraRelations)) {

			query.addRelations(extraRelations);
		}

		return sourceDAO.get(query);
	}
	
	protected Source getSource(User user) {

		if (user instanceof SourceUser) {

			SourceUser sourceUser = (SourceUser) user;
			return sourceUser.getSource();
		}

		return null;
	}

	protected SharedFlow getSharedFlow(Integer sharedFlowID, Field... extraRelations) throws SQLException {
		
		LowLevelQuery<SharedFlow> query = new LowLevelQuery<SharedFlow>("SELECT * FROM " + sharedFlowDAO.getTableName() + " "
										+ "WHERE sharedFlowID = ? AND sourceID IN (SELECT sourceID FROM " + sourceDAO.getTableName() + " WHERE repositoryID = ?);");

		query.addParameter(sharedFlowID);
		query.addParameter(repository.getRepositoryID());

		if (!ArrayUtils.isEmpty(extraRelations)) {

			query.addRelations(extraRelations);
		}

		return sharedFlowDAO.get(query);
	}

	protected List<SharedFlow> getLatestSharedFlows(Field... extraRelations) throws SQLException {

		LowLevelQuery<SharedFlow> query = new LowLevelQuery<SharedFlow>("SELECT s.* FROM " + sharedFlowDAO.getTableName() + " s "
										+ "INNER JOIN (SELECT flowFamilyID, sourceID, MAX(version) as version FROM " + sharedFlowDAO.getTableName() + " "
										+ "GROUP BY sourceID, flowFamilyID) j " 
										+ "ON s.sourceID = j.sourceID AND s.flowFamilyID = j.flowFamilyID AND s.version = j.version "
										+ "INNER JOIN (SELECT sourceID FROM " + sourceDAO.getTableName() + " "
										+ "WHERE repositoryID = ?) x " 
										+ "ON s.sourceID = x.sourceID "
										+ "ORDER BY name;");

		query.addParameter(getRepositoryID());
		query.addRelation(SharedFlow.SOURCE_FIELD);

		if (!ArrayUtils.isEmpty(extraRelations)) {

			query.addRelations(extraRelations);
		}

		List<SharedFlow> flows = sharedFlowDAO.getAll(query);

		if (!CollectionUtils.isEmpty(flows)) {

			for (SharedFlow flow : flows) {

				flow.setFamilySize(getSharedFlowFamilySize(flow.getSource(), flow.getFlowFamilyID()));
			}
		}

		return flows;
	}

	protected Integer getSharedFlowFamilySize(Source source, Integer flowFamilyID) throws SQLException {

		HighLevelQuery<SharedFlow> query = new HighLevelQuery<SharedFlow>();

		query.addParameter(sharedFlowSourceParamFactory.getParameter(source));
		query.addParameter(sharedFlowFamilyIDParamFactory.getParameter(flowFamilyID));

		return sharedFlowDAO.getCount(query);
	}

	private SharedFlow getSharedFlow(Source source, Integer flowFamiliyID, Integer flowID) throws SQLException {

		HighLevelQuery<SharedFlow> query = new HighLevelQuery<SharedFlow>();

		query.addParameter(sharedFlowSourceParamFactory.getParameter(source));
		query.addParameter(sharedFlowFamilyIDParamFactory.getParameter(flowFamiliyID));
		query.addParameter(sharedFlowFlowIDParamFactory.getParameter(flowID));

		return sharedFlowDAO.get(query);
	}

	protected List<SharedFlow> getSharedFlowsByFamily(Integer sourceID, Integer flowFamilyID, Field... extraRelations) throws SQLException {

		LowLevelQuery<SharedFlow> query = new LowLevelQuery<SharedFlow>("SELECT s.* FROM " + sharedFlowDAO.getTableName() + " s "
										+ "WHERE sourceID = ? AND flowFamilyID = ? "
										+ "ORDER BY version DESC;");

		query.addParameter(sourceID);
		query.addParameter(flowFamilyID);

		if (!ArrayUtils.isEmpty(extraRelations)) {

			query.addRelations(extraRelations);
		}

		return sharedFlowDAO.getAll(query);
	}

	@RESTMethod(alias = "info", method = "get")
	public Document getInfo(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Throwable {

		log.info("User " + user + " requesting info");

		Document doc = createDocument();
		Element documentElement = (Element) doc.getFirstChild();

		XMLUtils.appendNewElement(doc, documentElement, "Name", getRepositoryName());
		XMLUtils.appendNewElement(doc, documentElement, "Description", repositoryDescription);
		XMLUtils.appendNewElement(doc, documentElement, "UploadDescription", repositoryUploadDescription);

		Source source = getSource(user);
		
		if (source != null && source.hasUploadAccess()) {
			
			XMLUtils.appendNewElement(doc, documentElement, "UploadAccess");
		}

		return doc;
	}

	@RESTMethod(alias = "flows", method = "get")
	public Document getFlows(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Throwable {

		log.info("User " + user + " requesting shared flows");

		Document doc = createDocument();

		Element sharedFlowsElement = XMLUtils.appendNewElement(doc, (Element) doc.getFirstChild(), "SharedFlows");

		XMLUtils.append(doc, sharedFlowsElement, getLatestSharedFlows());

		return doc;
	}

	@RESTMethod(alias = "flow/{sharedFlowID}", method = "get")
	public Document getFlow(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser, @URIParam(name = "sharedFlowID") Integer sharedFlowID) throws Throwable {

		Document doc = createDocument();
		SharedFlow sharedFlow = getSharedFlow(sharedFlowID, SharedFlow.SOURCE_FIELD);

		if (sharedFlow != null) {

			log.info("User " + user + " requesting shared flow " + sharedFlow);

			XMLUtils.append(doc, (Element) doc.getFirstChild(), sharedFlow);
			return doc;
		}

		setStatus(doc, "NotFound");
		return doc;
	}

	@RESTMethod(alias = "family/{sourceID}/{flowFamilyID}", method = "get")
	public Document getFlowFamily(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser, @URIParam(name = "sourceID") Integer sourceID, @URIParam(name = "flowFamilyID") Integer flowFamilyID) throws Throwable {

		Document doc = createDocument();
		List<SharedFlow> sharedFlows = getSharedFlowsByFamily(sourceID, flowFamilyID, SharedFlow.SOURCE_FIELD);

		if (sharedFlows != null) {

			log.info("User " + user + " requesting shared flows with family ID " + flowFamilyID);

			setStatus(doc, "OK");
			XMLUtils.append(doc, (Element) doc.getFirstChild(), "SharedFlows", sharedFlows);

			Source source = getSource(user);

			if (source != null && source.getSourceID().equals(sharedFlows.get(0).getSource().getSourceID())) {

				XMLUtils.appendNewElement(doc, (Element) doc.getFirstChild(), "DeleteAccess");
				
				if (source.hasUploadAccess()) {

					XMLUtils.appendNewElement(doc, (Element) doc.getFirstChild(), "UploadAccess");
				}
			}

			return doc;
		}

		setStatus(doc, "NotFound");
		return doc;
	}

	@RESTMethod(alias = "download/{sharedFlowID}", method = "get")
	public Document downloadFlow(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser, @URIParam(name = "sharedFlowID") Integer sharedFlowID) throws Throwable {

		SharedFlow sharedFlow = getSharedFlow(sharedFlowID, SharedFlow.XML_BLOB_FIELD);

		if (sharedFlow != null) {

			log.info("User " + user + " downloading shared flow " + sharedFlow);

			HTTPUtils.sendBlob(sharedFlow.getFlowXML(), sharedFlow.getName() + ".oeflow", req, res, ContentDisposition.INLINE);
			return null;
		}

		res.setStatus(HttpServletResponse.SC_NOT_FOUND);
		Document doc = createDocument();
		setStatus(doc, "NotFound");
		return doc;
	}

	@RESTMethod(alias = "add", method = "post")
	public Document addFlow(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Throwable {

		List<ValidationError> validationErrors = new ArrayList<ValidationError>();
		Document doc = createDocument();
		Source source = getSource(user);

		if (source == null || !source.hasUploadAccess()) {

			validationErrors.add(new ValidationError("AccessDenied"));
			
			log.warn("Access denied for request from " + req.getRemoteAddr());
		}

		if (!req.getMethod().equalsIgnoreCase("POST")) {

			validationErrors.add(new ValidationError("MethodNotAllowed"));
			
			log.warn("Invalid method used in request from " + req.getRemoteAddr());
		}

		SharedFlow sharedFlow = null;
		
		if (validationErrors.isEmpty()) {
			
			try {
				sharedFlow = SHARED_FLOW_POPULATOR.populate(req);
				
			} catch (ValidationException e) {
				
				validationErrors.addAll(e.getErrors());
			}
			
			if (validationErrors.isEmpty()) {

				String flowXML = req.getParameter("flowXML");

				if (flowXML == null || flowXML.length() == 0) {
					validationErrors.add(new ValidationError("flowXML", ValidationErrorType.RequiredField));
				}

				if (validationErrors.isEmpty()) {

					sharedFlow.setSource(source);
					sharedFlow.setAdded(new Date(System.currentTimeMillis()));
					sharedFlow.setFlowXML(new SerialBlob(flowXML.getBytes()));

					SharedFlow existingFlow = getSharedFlow(sharedFlow.getSource(), sharedFlow.getFlowFamilyID(), sharedFlow.getFlowID());
					
					if (existingFlow != null) {

						sharedFlowDAO.delete(existingFlow);
					}
					
					log.info("User " + user + " adding shared flow " + sharedFlow + " from " + source);

					sharedFlowDAO.add(sharedFlow);

					XMLUtils.append(doc, (Element) doc.getFirstChild(), sharedFlow);
					return doc;
				}
			}			
		}
	

		XMLUtils.append(doc, (Element) doc.getFirstChild(), "ValidationErrors", validationErrors);
		return doc;
	}

	@RESTMethod(alias = "delete/{sharedFlowID}", method = "get")
	public Document deleteFlow(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser, @URIParam(name = "sharedFlowID") Integer sharedFlowID) throws Throwable {

		Document doc = createDocument();
		SharedFlow sharedFlow = getSharedFlow(sharedFlowID, SharedFlow.SOURCE_FIELD);

		if (sharedFlow != null) {

			if (!sharedFlow.getSource().equals(getSource(user))) {

				setStatus(doc, "AccessDenied");
				return doc;
			}

			log.info("User " + user + " deleting shared flow " + sharedFlow);

			sharedFlowDAO.delete(sharedFlow);

			sharedFlow.setFamilySize(getSharedFlowFamilySize(sharedFlow.getSource(), sharedFlow.getFlowFamilyID()));

			setStatus(doc, "Deleted");
			XMLUtils.append(doc, (Element) doc.getFirstChild(), sharedFlow);

			return doc;
		}

		setStatus(doc, "NotFound");
		return doc;
	}

	public Document createDocument() {

		Document doc = XMLUtils.createDomDocument();
		Element documentElement = doc.createElement("Response");

		doc.appendChild(documentElement);
		return doc;
	}

	private void setStatus(Document doc, String status) {

		XMLUtils.appendNewElement(doc, (Element) doc.getFirstChild(), "Status", status);
	}
	
	@Override
	public ForegroundModuleResponse defaultMethod(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Throwable {

		return sourceCRUD.list(req, res, user, uriParser, null);
	}

	@WebPublic(toLowerCase = true)
	public ForegroundModuleResponse listSources(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Exception {

		return sourceCRUD.list(req, res, user, uriParser, null);
	}

	@WebPublic(toLowerCase = true)
	public ForegroundModuleResponse addSource(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Exception {

		return sourceCRUD.add(req, res, user, uriParser);
	}

	@WebPublic(toLowerCase = true)
	public ForegroundModuleResponse showSource(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Exception {

		return sourceCRUD.show(req, res, user, uriParser);
	}

	@WebPublic(toLowerCase = true)
	public ForegroundModuleResponse deleteSource(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Exception {

		return sourceCRUD.delete(req, res, user, uriParser);
	}

	@WebPublic(toLowerCase = true)
	public ForegroundModuleResponse updateSource(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Exception {

		return sourceCRUD.update(req, res, user, uriParser);
	}

	@Override
	public Document createDocument(HttpServletRequest req, URIParser uriParser, User user) {

		Document doc = XMLUtils.createDomDocument();
		Element documentElement = doc.createElement("Document");
		documentElement.appendChild(RequestUtils.getRequestInfoAsXML(doc, req, uriParser));
		documentElement.appendChild(this.moduleDescriptor.toXML(doc));
		
		doc.appendChild(documentElement);
		return doc;
	}

	@Override
	public String getTitlePrefix() {

		return getRepositoryName();
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

		return adminGroupIDs;
	}

	@Override
	public Collection<Integer> getAllowedUserIDs() {

		return adminUserIDs;
	}

	public Repository getRepository() {

		return repository;
	}
	
	@Override
	protected ForegroundModuleResponse processForegroundRequest(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Throwable {

		String authHeader = req.getHeader("Authorization");

		if (authHeader != null) {

			StringTokenizer st = new StringTokenizer(authHeader);

			if (st.hasMoreTokens()) {

				String basic = st.nextToken();

				if (basic.equalsIgnoreCase("Basic")) {

					String decodedCredentials = new String(Base64.decode(st.nextToken()), "UTF-8");

					int index = decodedCredentials.indexOf(":");

					if (index != -1) {

						String username = decodedCredentials.substring(0, index).trim();
						String password = decodedCredentials.substring(index + 1).trim();

						Source source = getSource(username, password);

						if (source == null) {

							log.warn("Failed login attempt using username " + username + " from address " + req.getRemoteHost());

							unauthorized(res, "Bad credentials");
							return null;

						} else {

							SourceUser requestedUser = new SourceUser(source);

							return super.processForegroundRequest(req, res, requestedUser, uriParser);
						}

					} else {

						unauthorized(res, "Invalid authentication token");
						return null;
					}
				}
			}

		} else if (user != null) {

			return super.processForegroundRequest(req, res, user, uriParser);
		}

		unauthorized(res, "Unauthorized");
		return null;
	}
	
	protected void unauthorized(HttpServletResponse response, String message) throws IOException {

		response.setHeader("WWW-Authenticate", "Basic realm=\"" + realm + "\"");
		response.sendError(401, message);
	}
}
