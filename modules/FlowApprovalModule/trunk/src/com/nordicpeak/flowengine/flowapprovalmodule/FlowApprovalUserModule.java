package com.nordicpeak.flowengine.flowapprovalmodule;

import java.io.IOException;
import java.lang.reflect.Field;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import se.unlogic.hierarchy.core.annotations.InstanceManagerDependency;
import se.unlogic.hierarchy.core.annotations.ModuleSetting;
import se.unlogic.hierarchy.core.annotations.TextFieldSettingDescriptor;
import se.unlogic.hierarchy.core.annotations.WebPublic;
import se.unlogic.hierarchy.core.annotations.XSLVariable;
import se.unlogic.hierarchy.core.beans.SimpleForegroundModuleResponse;
import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.hierarchy.core.enums.CRUDAction;
import se.unlogic.hierarchy.core.enums.EventTarget;
import se.unlogic.hierarchy.core.events.CRUDEvent;
import se.unlogic.hierarchy.core.exceptions.AccessDeniedException;
import se.unlogic.hierarchy.core.exceptions.URINotFoundException;
import se.unlogic.hierarchy.core.interfaces.AccessInterface;
import se.unlogic.hierarchy.core.interfaces.ForegroundModuleResponse;
import se.unlogic.hierarchy.core.interfaces.SectionInterface;
import se.unlogic.hierarchy.core.interfaces.modules.descriptors.ForegroundModuleDescriptor;
import se.unlogic.hierarchy.core.utils.AccessUtils;
import se.unlogic.hierarchy.core.utils.AttributeTagUtils;
import se.unlogic.hierarchy.core.utils.HierarchyAnnotatedDAOFactory;
import se.unlogic.hierarchy.core.utils.UserUtils;
import se.unlogic.hierarchy.core.utils.extensionlinks.ExtensionLink;
import se.unlogic.hierarchy.foregroundmodules.rest.AnnotatedRESTModule;
import se.unlogic.hierarchy.foregroundmodules.rest.RESTMethod;
import se.unlogic.hierarchy.foregroundmodules.rest.URIParam;
import se.unlogic.standardutils.dao.AdvancedAnnotatedDAOWrapper;
import se.unlogic.standardutils.dao.AnnotatedDAO;
import se.unlogic.standardutils.dao.HighLevelQuery;
import se.unlogic.standardutils.dao.QueryParameterFactory;
import se.unlogic.standardutils.dao.querys.ArrayListQuery;
import se.unlogic.standardutils.db.tableversionhandler.TableVersionHandler;
import se.unlogic.standardutils.db.tableversionhandler.UpgradeResult;
import se.unlogic.standardutils.db.tableversionhandler.XMLDBScriptProvider;
import se.unlogic.standardutils.populators.IntegerPopulator;
import se.unlogic.standardutils.populators.StringPopulator;
import se.unlogic.standardutils.string.StringUtils;
import se.unlogic.standardutils.time.TimeUtils;
import se.unlogic.standardutils.validation.ValidationError;
import se.unlogic.standardutils.xml.XMLUtils;
import se.unlogic.webutils.http.RequestUtils;
import se.unlogic.webutils.http.URIParser;
import se.unlogic.webutils.validation.ValidationUtils;

import com.nordicpeak.flowengine.FlowAdminModule;
import com.nordicpeak.flowengine.FlowInstanceAdminModule;
import com.nordicpeak.flowengine.UserFlowInstanceMenuModule;
import com.nordicpeak.flowengine.beans.ExternalMessage;
import com.nordicpeak.flowengine.beans.FlowInstance;
import com.nordicpeak.flowengine.beans.FlowInstanceEvent;
import com.nordicpeak.flowengine.enums.EventType;
import com.nordicpeak.flowengine.enums.SenderType;
import com.nordicpeak.flowengine.events.ExternalMessageAddedEvent;
import com.nordicpeak.flowengine.flowapprovalmodule.beans.FlowApprovalActivity;
import com.nordicpeak.flowengine.flowapprovalmodule.beans.FlowApprovalActivityGroup;
import com.nordicpeak.flowengine.flowapprovalmodule.beans.FlowApprovalActivityProgress;
import com.nordicpeak.flowengine.interfaces.UserMenuProvider;
import com.nordicpeak.flowengine.utils.ExternalMessageUtils;

public class FlowApprovalUserModule extends AnnotatedRESTModule implements UserMenuProvider {

	@XSLVariable(prefix = "java.")
	private String userMenuTabTitle = "My organizations";

	@ModuleSetting
	@TextFieldSettingDescriptor(name = "User menu item slot", description = "User menu item slot")
	protected String userMenuExtensionLinkSlot = "30";

	private AnnotatedDAO<FlowApprovalActivity> activityDAO;
	private AnnotatedDAO<FlowApprovalActivityProgress> activityProgressDAO;

	private AdvancedAnnotatedDAOWrapper<FlowApprovalActivityProgress, Integer> activityProgressDAOWrapper;

	private QueryParameterFactory<FlowApprovalActivityProgress, FlowApprovalActivity> activityProgressActivityParamFactory;
	private QueryParameterFactory<FlowApprovalActivityProgress, Timestamp> activityProgressCompletedParamFactory;

	@InstanceManagerDependency(required = true)
	protected FlowAdminModule flowAdminModule;

	@InstanceManagerDependency(required = true)
	protected FlowApprovalAdminModule approvalAdminModule;

	protected UserFlowInstanceMenuModule userFlowInstanceMenuModule;

	protected ExtensionLink userMenuLink;

	@Override
	public void init(ForegroundModuleDescriptor moduleDescriptor, SectionInterface sectionInterface, DataSource dataSource) throws Exception {

		super.init(moduleDescriptor, sectionInterface, dataSource);
		
		if (!systemInterface.getInstanceHandler().addInstance(FlowApprovalUserModule.class, this)) {
			
			throw new RuntimeException("Unable to register module in global instance handler using key " + FlowApprovalUserModule.class.getSimpleName() + ", another instance is already registered using this key.");
		}
	}

	@Override
	protected void createDAOs(DataSource dataSource) throws Exception {

		super.createDAOs(dataSource);

		//Automatic table version handling
		UpgradeResult upgradeResult = TableVersionHandler.upgradeDBTables(dataSource, FlowApprovalAdminModule.class.getName(), new XMLDBScriptProvider(FlowApprovalAdminModule.class.getResourceAsStream("DB script.xml")));

		if (upgradeResult.isUpgrade()) {

			log.info(upgradeResult.toString());
		}

		HierarchyAnnotatedDAOFactory daoFactory = new HierarchyAnnotatedDAOFactory(dataSource, systemInterface.getUserHandler(), systemInterface.getGroupHandler(), true, true, false);

		activityDAO = daoFactory.getDAO(FlowApprovalActivity.class);
		activityProgressDAO = daoFactory.getDAO(FlowApprovalActivityProgress.class);

		activityProgressDAOWrapper = activityProgressDAO.getAdvancedWrapper(Integer.class);
		activityProgressDAOWrapper.getGetQuery().addRelations(FlowApprovalActivityProgress.ACTIVITY_RELATION, FlowApprovalActivity.ACTIVITY_GROUP_RELATION, FlowApprovalActivity.USERS_RELATION, FlowApprovalActivity.GROUPS_RELATION);

		activityProgressActivityParamFactory = activityProgressDAO.getParamFactory("activity", FlowApprovalActivity.class);
		activityProgressCompletedParamFactory = activityProgressDAO.getParamFactory("completed", Timestamp.class);
	}

	@InstanceManagerDependency
	public void setUserFlowInstanceMenuModule(UserFlowInstanceMenuModule userFlowInstanceMenuModule) {

		if (userFlowInstanceMenuModule == null && this.userFlowInstanceMenuModule != null) {

			this.userFlowInstanceMenuModule.removeUserMenuProvider(this);
		}

		this.userFlowInstanceMenuModule = userFlowInstanceMenuModule;

		if (this.userFlowInstanceMenuModule != null) {

			this.userFlowInstanceMenuModule.addUserMenuProvider(this);
		}
	}

	@Override
	public void unload() throws Exception {

		if (userFlowInstanceMenuModule != null) {

			setUserFlowInstanceMenuModule(null);
		}
		
		systemInterface.getInstanceHandler().removeInstance(FlowApprovalUserModule.class, this);

		super.unload();
	}

	@Override
	protected void moduleConfigured() throws Exception {

		super.moduleConfigured();

		this.userMenuLink = new ExtensionLink(userMenuTabTitle, getFullAlias(), "b", userMenuExtensionLinkSlot);

		if (userFlowInstanceMenuModule != null) {

			userFlowInstanceMenuModule.sortProviders();
		}
	}

	@Override
	public ForegroundModuleResponse defaultMethod(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Throwable {

		return listPending(req, res, user, uriParser, null);
	}

	public ForegroundModuleResponse listPending(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser, List<ValidationError> validationErrors) throws Throwable {

		if (user == null) {
			throw new AccessDeniedException("User needs to be logged in");
		}

		log.info("User " + user + " listing pending activities");

		Document doc = createDocument(req, uriParser, user);

		Element listPending = XMLUtils.appendNewElement(doc, doc.getDocumentElement(), "ListPendingActivities");

		List<FlowApprovalActivity> accessibleActivities = getAccessibleActivities(user);

		List<FlowApprovalActivityProgress> pendingActivities = getPendingActivities(accessibleActivities, FlowApprovalActivityProgress.ACTIVITY_RELATION, FlowApprovalActivity.ACTIVITY_GROUP_RELATION);

		appendActivities(pendingActivities, doc, listPending);

		if (validationErrors != null) {

			listPending.appendChild(RequestUtils.getRequestParameters(req, doc));
			XMLUtils.append(doc, listPending, "ValidationErrors", validationErrors);
		}

		return new SimpleForegroundModuleResponse(doc, moduleDescriptor.getName(), getDefaultBreadcrumb());
	}

	@WebPublic(requireLogin = true, toLowerCase = true)
	public ForegroundModuleResponse listCompleted(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Throwable {

		log.info("User " + user + " listing completed activities");

		Document doc = createDocument(req, uriParser, user);

		Element listCompleted = XMLUtils.appendNewElement(doc, doc.getDocumentElement(), "ListCompletedActivities");

		List<FlowApprovalActivity> accessibleActivities = getAccessibleActivities(user);

		List<FlowApprovalActivityProgress> completedActivities = getCompletedActivities(accessibleActivities, FlowApprovalActivityProgress.ACTIVITY_RELATION, FlowApprovalActivity.ACTIVITY_GROUP_RELATION);

		appendActivities(completedActivities, doc, listCompleted);

		return new SimpleForegroundModuleResponse(doc, moduleDescriptor.getName(), getDefaultBreadcrumb());
	}

	private void appendActivities(List<FlowApprovalActivityProgress> activities, Document doc, Element listElement) throws SQLException {

		// flowInstanceID, activityGroup, activityProgress
		Map<Integer, Map<FlowApprovalActivityGroup, List<FlowApprovalActivityProgress>>> mapping = new HashMap<>();

		if (activities != null) {
			for (FlowApprovalActivityProgress activityProgress : activities) {

				Map<FlowApprovalActivityGroup, List<FlowApprovalActivityProgress>> activityGroupToProgressMap = mapping.get(activityProgress.getFlowInstanceID());

				if (activityGroupToProgressMap == null) {

					activityGroupToProgressMap = new HashMap<>();
					mapping.put(activityProgress.getFlowInstanceID(), activityGroupToProgressMap);
				}

				List<FlowApprovalActivityProgress> progressList = activityGroupToProgressMap.get(activityProgress.getActivity().getActivityGroup());

				if (progressList == null) {

					progressList = new ArrayList<>();
					activityGroupToProgressMap.put(activityProgress.getActivity().getActivityGroup(), progressList);
				}

				progressList.add(activityProgress);
			}
		}

		Element flowInstancesElement = XMLUtils.appendNewElement(doc, listElement, "FlowInstances");

		for (Entry<Integer, Map<FlowApprovalActivityGroup, List<FlowApprovalActivityProgress>>> entry : mapping.entrySet()) {

			Integer flowInstanceID = entry.getKey();

			FlowInstance flowInstance = flowAdminModule.getFlowInstance(flowInstanceID);

			if (flowInstance != null) {

				Element flowInstanceElement = flowInstance.toXML(doc);

				Map<FlowApprovalActivityGroup, List<FlowApprovalActivityProgress>> activityGroupToProgressMap = entry.getValue();

				Element activityGroupsElement = XMLUtils.appendNewElement(doc, flowInstanceElement, "ActivityGroups");

				for (Entry<FlowApprovalActivityGroup, List<FlowApprovalActivityProgress>> entry2 : activityGroupToProgressMap.entrySet()) {

					FlowApprovalActivityGroup activityGroup = entry2.getKey();

					Element activityGroupElement = activityGroup.toXML(doc);

					if (activityGroup.getUserDescriptionTemplate() != null) {

						XMLUtils.appendNewElement(doc, activityGroupElement, "UserDescription", AttributeTagUtils.replaceTags(activityGroup.getUserDescriptionTemplate(), flowInstance.getAttributeHandler()));
					}

					List<FlowApprovalActivityProgress> activityProgresses = entry2.getValue();

					XMLUtils.append(doc, activityGroupElement, "Progresses", activityProgresses);

					activityGroupsElement.appendChild(activityGroupElement);
				}

				flowInstancesElement.appendChild(flowInstanceElement);
			}
		}
	}

	private List<FlowApprovalActivity> getAccessibleActivities(User user) throws SQLException {

		ArrayList<Integer> userGroupIDs = UserUtils.getUserGroupIDs(user, true);

		// @formatter:off
		ArrayListQuery<Integer> query = new ArrayListQuery<>(dataSource,
				"SELECT DISTINCT a.activityID FROM " + activityDAO.getTableName() + " a"
				+" LEFT OUTER JOIN flowapproval_activity_users u ON u.activityID = a.activityID "
				+" LEFT OUTER JOIN flowapproval_activity_groups g ON g.activityID = a.activityID "
				+" WHERE u.userID = " + user.getUserID() + " OR g.groupID IN (" + (userGroupIDs != null ? StringUtils.toCommaSeparatedString(userGroupIDs) : null) + ")"
				, IntegerPopulator.getPopulator()
		);
		// @formatter:on

		List<Integer> activityIDs = query.executeQuery();

		if (activityIDs == null) {
			return null;
		}

		List<FlowApprovalActivity> activites = new ArrayList<>(activityIDs.size());

		for (Integer activityID : activityIDs) {

			FlowApprovalActivity activity = new FlowApprovalActivity();
			activity.setActivityID(activityID);

			activites.add(activity);
		}

		return activites;
	}

	private List<FlowApprovalActivityProgress> getPendingActivities(List<FlowApprovalActivity> accessibleActivities, Field... relations) throws SQLException {

		if (accessibleActivities == null) {
			return null;
		}

		HighLevelQuery<FlowApprovalActivityProgress> query = new HighLevelQuery<FlowApprovalActivityProgress>();

		query.addParameter(activityProgressActivityParamFactory.getWhereInParameter(accessibleActivities));
		query.addParameter(activityProgressCompletedParamFactory.getIsNullParameter());

		if (relations != null) {

			query.addRelations(relations);
			query.addCachedRelation(FlowApprovalActivity.ACTIVITY_GROUP_RELATION);
		}

		return activityProgressDAO.getAll(query);
	}

	private List<FlowApprovalActivityProgress> getCompletedActivities(List<FlowApprovalActivity> accessibleActivities, Field... relations) throws SQLException {

		if (accessibleActivities == null) {
			return null;
		}

		HighLevelQuery<FlowApprovalActivityProgress> query = new HighLevelQuery<FlowApprovalActivityProgress>();

		query.addParameter(activityProgressActivityParamFactory.getWhereInParameter(accessibleActivities));
		query.addParameter(activityProgressCompletedParamFactory.getIsNotNullParameter());

		if (relations != null) {

			query.addRelations(relations);
			query.addCachedRelation(FlowApprovalActivity.ACTIVITY_GROUP_RELATION);
		}

		return activityProgressDAO.getAll(query);
	}

	@RESTMethod(alias = "show/{activityProgressID}", method = { "get", "post" }, requireLogin = true)
	public ForegroundModuleResponse showActivity(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser, @URIParam(name = "activityProgressID") Integer activityProgressID) throws SQLException, URINotFoundException, IOException, AccessDeniedException {

		FlowApprovalActivityProgress activityProgress = activityProgressDAOWrapper.get(activityProgressID);

		if (activityProgress == null) {
			throw new URINotFoundException(uriParser);
		}

		FlowApprovalActivity activity = activityProgress.getActivity();
		FlowApprovalActivityGroup activityGroup = activity.getActivityGroup();

		FlowInstance flowInstance = flowAdminModule.getFlowInstance(activityProgress.getFlowInstanceID());
		
		if (!AccessUtils.checkAccess(user, activity)) {
			
			try {
				FlowInstanceAdminModule.GENERAL_ACCESS_CONTROLLER.checkManagerAccess(flowInstance, user);
				
			} catch (AccessDeniedException e) {
				
				throw new AccessDeniedException("User does not have access to activity " + activity + " nor is a manager for " + flowInstance);
			}
		}

		List<ValidationError> validationErrors = null;

		if (req.getMethod().equalsIgnoreCase("POST") && activityProgress.getCompleted() == null) {

			validationErrors = new ArrayList<>();

			boolean completed = false;

			if (activityGroup.isUseApproveDeny()) {

				if ("approved".equals(req.getParameter("completed"))) {

					log.info("User " + user + " approved activity progress " + activityProgress);

					activityProgress.setDenied(false);
					completed = true;

				} else if ("denied".equals(req.getParameter("completed"))) {

					log.info("User " + user + " denied activity progress " + activityProgress);

					activityProgress.setDenied(true);
					completed = true;
				}

			} else {

				if ("true".equals(req.getParameter("completed"))) {

					log.info("User " + user + " completing activity progress " + activityProgress);

					completed = true;
				}
			}

			String comment = ValidationUtils.validateParameter("comment", req, false, 0, 65535, StringPopulator.getPopulator(), validationErrors);

			if (validationErrors.isEmpty() && (completed || !StringUtils.compare(comment, activityProgress.getComment()))) {

				activityProgress.setComment(comment);

				if (completed) {

					activityProgress.setCompleted(TimeUtils.getCurrentTimestamp());
					activityProgress.setCompletingUser(user);
				}

				activityProgressDAOWrapper.update(activityProgress);

				if (completed) {

					if (activityGroup.isAppendCommentsToExternalMessages() && flowInstance.isExternalMessagesEnabled() && !StringUtils.isEmpty(comment) && flowInstance.getOwners() != null) {

						log.info("Copying comment to external messages for flowinstance " + flowInstance);

						ExternalMessage externalMessage = new ExternalMessage();
						externalMessage.setFlowInstance(flowInstance);
						externalMessage.setPoster(user);
						externalMessage.setMessage(activity.getName() + ":\r" + comment);
						externalMessage.setAdded(TimeUtils.getCurrentTimestamp());
						externalMessage.setAttachments(null);
						externalMessage.setPostedByManager(true);

						flowAdminModule.getDAOFactory().getExternalMessageDAO().add(externalMessage);

						FlowInstanceEvent flowInstanceEvent = flowAdminModule.getFlowInstanceEventGenerator().addFlowInstanceEvent(flowInstance, EventType.MANAGER_MESSAGE_SENT, null, user, null, ExternalMessageUtils.getFlowInstanceEventAttributes(externalMessage));

						systemInterface.getEventHandler().sendEvent(FlowInstance.class, new ExternalMessageAddedEvent(flowInstance, flowInstanceEvent, flowAdminModule.getSiteProfile(flowInstance), externalMessage, SenderType.MANAGER), EventTarget.ALL);
						systemInterface.getEventHandler().sendEvent(ExternalMessage.class, new CRUDEvent<ExternalMessage>(CRUDAction.ADD, externalMessage), EventTarget.ALL);
					}

					approvalAdminModule.checkApprovalCompletion(activityProgress.getActivity().getActivityGroup(), flowInstance);

					redirectToDefaultMethod(req, res);
					return null;
				}
			}
		}

		log.info("User " + user + " requested activity progress " + activityProgress);

		if (activity.getDescription() != null) {
			activity.setDescription(AttributeTagUtils.replaceTags(activity.getDescription(), flowInstance.getAttributeHandler()));
		}

		Document doc = createDocument(req, uriParser, user);

		Element showActivity = XMLUtils.appendNewElement(doc, doc.getDocumentElement(), "ShowActivity");

		showActivity.appendChild(activityProgress.toXML(doc));
		showActivity.appendChild(flowInstance.toXML(doc));
		
		if (activityGroup.getUserDescriptionTemplate() != null) {
			
			XMLUtils.appendNewElement(doc, showActivity, "UserDescription", AttributeTagUtils.replaceTags(activityGroup.getUserDescriptionTemplate(), flowInstance.getAttributeHandler()));
		}

		if (validationErrors != null) {

			showActivity.appendChild(RequestUtils.getRequestParameters(req, doc));
			XMLUtils.append(doc, showActivity, "ValidationErrors", validationErrors);
		}

		return new SimpleForegroundModuleResponse(doc, moduleDescriptor.getName(), getDefaultBreadcrumb());
	}

	public Document createDocument(HttpServletRequest req, URIParser uriParser, User user) {

		Document doc = XMLUtils.createDomDocument();
		Element document = doc.createElement("Document");
		document.appendChild(RequestUtils.getRequestInfoAsXML(doc, req, uriParser));
		document.appendChild(this.moduleDescriptor.toXML(doc));
		doc.appendChild(document);
		return doc;
	}

	@Override
	public AccessInterface getAccessInterface() {
		return moduleDescriptor;
	}

	@Override
	public String getUserMenuAlias() {
		return getFullAlias();
	}

	@Override
	public ExtensionLink getUserMenuExtensionLink() {
		return userMenuLink;
	}

	@WebPublic(alias = "icon")
	public ForegroundModuleResponse getFlowIcon(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws SQLException, URINotFoundException {

		return flowAdminModule.getFlowIcon(req, res, user, uriParser);
	}

}
