package com.nordicpeak.flowengine.flowapprovalmodule;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URLDecoder;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import se.unlogic.cron4jutils.CronStringValidator;
import se.unlogic.emailutils.framework.EmailUtils;
import se.unlogic.emailutils.framework.SimpleEmail;
import se.unlogic.hierarchy.core.annotations.CheckboxSettingDescriptor;
import se.unlogic.hierarchy.core.annotations.EventListener;
import se.unlogic.hierarchy.core.annotations.HTMLEditorSettingDescriptor;
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
import se.unlogic.hierarchy.core.enums.CRUDAction;
import se.unlogic.hierarchy.core.enums.EventSource;
import se.unlogic.hierarchy.core.enums.EventTarget;
import se.unlogic.hierarchy.core.enums.ResponseType;
import se.unlogic.hierarchy.core.events.CRUDEvent;
import se.unlogic.hierarchy.core.exceptions.URINotFoundException;
import se.unlogic.hierarchy.core.handlers.GroupHandler;
import se.unlogic.hierarchy.core.handlers.UserHandler;
import se.unlogic.hierarchy.core.interfaces.ForegroundModuleResponse;
import se.unlogic.hierarchy.core.interfaces.SectionInterface;
import se.unlogic.hierarchy.core.interfaces.ViewFragment;
import se.unlogic.hierarchy.core.interfaces.attributes.AttributeHandler;
import se.unlogic.hierarchy.core.interfaces.modules.descriptors.ForegroundModuleDescriptor;
import se.unlogic.hierarchy.core.utils.AccessUtils;
import se.unlogic.hierarchy.core.utils.AttributeTagUtils;
import se.unlogic.hierarchy.core.utils.CRUDCallback;
import se.unlogic.hierarchy.core.utils.HierarchyAnnotatedDAOFactory;
import se.unlogic.hierarchy.core.utils.ModuleViewFragmentTransformer;
import se.unlogic.hierarchy.core.utils.UserUtils;
import se.unlogic.hierarchy.core.utils.ViewFragmentModule;
import se.unlogic.hierarchy.core.utils.usergrouplist.UserGroupListConnector;
import se.unlogic.hierarchy.foregroundmodules.AnnotatedForegroundModule;
import se.unlogic.hierarchy.foregroundmodules.staticcontent.StaticContentModule;
import se.unlogic.standardutils.arrays.ArrayUtils;
import se.unlogic.standardutils.collections.CollectionUtils;
import se.unlogic.standardutils.dao.AdvancedAnnotatedDAOWrapper;
import se.unlogic.standardutils.dao.AnnotatedDAO;
import se.unlogic.standardutils.dao.HighLevelQuery;
import se.unlogic.standardutils.dao.LowLevelQuery;
import se.unlogic.standardutils.dao.QueryParameterFactory;
import se.unlogic.standardutils.dao.TransactionHandler;
import se.unlogic.standardutils.db.tableversionhandler.TableVersionHandler;
import se.unlogic.standardutils.db.tableversionhandler.UpgradeResult;
import se.unlogic.standardutils.db.tableversionhandler.XMLDBScriptProvider;
import se.unlogic.standardutils.json.JsonArray;
import se.unlogic.standardutils.json.JsonObject;
import se.unlogic.standardutils.json.JsonUtils;
import se.unlogic.standardutils.numbers.NumberUtils;
import se.unlogic.standardutils.object.ObjectUtils;
import se.unlogic.standardutils.string.AnnotatedBeanTagSourceFactory;
import se.unlogic.standardutils.string.SingleTagSource;
import se.unlogic.standardutils.string.StringUtils;
import se.unlogic.standardutils.string.TagReplacer;
import se.unlogic.standardutils.string.TagSource;
import se.unlogic.standardutils.time.TimeUtils;
import se.unlogic.standardutils.validation.NonNegativeStringIntegerValidator;
import se.unlogic.standardutils.validation.ValidationError;
import se.unlogic.standardutils.xml.XMLUtils;
import se.unlogic.webutils.http.HTTPUtils;
import se.unlogic.webutils.http.RequestUtils;
import se.unlogic.webutils.http.URIParser;

import com.nordicpeak.flowengine.FlowAdminModule;
import com.nordicpeak.flowengine.beans.Flow;
import com.nordicpeak.flowengine.beans.FlowAdminExtensionShowView;
import com.nordicpeak.flowengine.beans.FlowFamily;
import com.nordicpeak.flowengine.beans.FlowInstance;
import com.nordicpeak.flowengine.beans.FlowInstanceEvent;
import com.nordicpeak.flowengine.beans.Status;
import com.nordicpeak.flowengine.enums.EventType;
import com.nordicpeak.flowengine.events.StatusChangedByManagerEvent;
import com.nordicpeak.flowengine.events.SubmitEvent;
import com.nordicpeak.flowengine.flowapprovalmodule.beans.FlowApprovalActivity;
import com.nordicpeak.flowengine.flowapprovalmodule.beans.FlowApprovalActivityGroup;
import com.nordicpeak.flowengine.flowapprovalmodule.beans.FlowApprovalActivityProgress;
import com.nordicpeak.flowengine.flowapprovalmodule.beans.FlowApprovalActivityResponsibleUser;
import com.nordicpeak.flowengine.flowapprovalmodule.cruds.FlowApprovalActivityCRUD;
import com.nordicpeak.flowengine.flowapprovalmodule.cruds.FlowApprovalActivityGroupCRUD;
import com.nordicpeak.flowengine.flowapprovalmodule.validationerrors.ActivityGroupInvalidStatus;
import com.nordicpeak.flowengine.interfaces.FlowAdminFragmentExtensionViewProvider;
import com.nordicpeak.flowengine.interfaces.ImmutableFlowInstance;
import com.nordicpeak.flowengine.managers.FlowInstanceManager;
import com.nordicpeak.flowengine.notifications.StandardFlowNotificationHandler;

import it.sauronsoftware.cron4j.Scheduler;

public class FlowApprovalAdminModule extends AnnotatedForegroundModule implements FlowAdminFragmentExtensionViewProvider, ViewFragmentModule<ForegroundModuleDescriptor>, CRUDCallback<User>, Runnable {

	private static final AnnotatedBeanTagSourceFactory<FlowApprovalActivityGroup> ACTIVITY_GROUP_TAG_SOURCE_FACTORY = new AnnotatedBeanTagSourceFactory<FlowApprovalActivityGroup>(FlowApprovalActivityGroup.class, "$activityGroup.");
	private static final AnnotatedBeanTagSourceFactory<User> MANAGER_TAG_SOURCE_FACTORY = new AnnotatedBeanTagSourceFactory<User>(User.class, "$manager.");
	private static final AnnotatedBeanTagSourceFactory<FlowInstance> FLOW_INSTANCE_TAG_SOURCE_FACTORY = new AnnotatedBeanTagSourceFactory<FlowInstance>(FlowInstance.class, "$flowInstance.");
	private static final AnnotatedBeanTagSourceFactory<Flow> FLOW_TAG_SOURCE_FACTORY = new AnnotatedBeanTagSourceFactory<Flow>(Flow.class, "$flow.");

	@XSLVariable(prefix = "java.")
	private String adminExtensionViewTitle = "Flow approval settings";

	@XSLVariable(prefix = "java.")
	private String eventActivityGroupAdded;

	@XSLVariable(prefix = "java.")
	private String eventActivityGroupUpdated;

	@XSLVariable(prefix = "java.")
	private String eventActivityGroupDeleted;
	
	@XSLVariable(prefix = "java.")
	private String eventActivityGroupsSorted;

	@XSLVariable(prefix = "java.")
	private String eventActivityAdded;

	@XSLVariable(prefix = "java.")
	private String eventActivityUpdated;

	@XSLVariable(prefix = "java.")
	private String eventActivityDeleted;

	@XSLVariable(prefix = "java.")
	private String eventActivityGroupStarted;

	@XSLVariable(prefix = "java.")
	private String eventActivityGroupCompleted;

	@XSLVariable(prefix = "java.")
	private String eventActivityGroupApproved;

	@XSLVariable(prefix = "java.")
	private String eventActivityGroupDenied;

	@ModuleSetting
	@TextFieldSettingDescriptor(name = "Priority", description = "The priority of this extension provider compared to other providers. A low value means a higher priority. Valid values are 0 - " + Integer.MAX_VALUE + ".", required = true, formatValidator = NonNegativeStringIntegerValidator.class)
	protected int priority = 0;

	@ModuleSetting
	@CheckboxSettingDescriptor(name = "Enable fragment XML debug", description = "Enables debugging of fragment XML")
	private boolean debugFragmentXML;

	@ModuleSetting
	@TextFieldSettingDescriptor(name = "User approval module URL", description = "The full URL of the user approval module", required = true)
	protected String userApprovalModuleAlias = null;

	@ModuleSetting
	@TextFieldSettingDescriptor(name = "Activity group started email subject", description = "The subject of emails sent to the users when an activity group is started for them", required = true)
	@XSLVariable(prefix = "java.")
	private String activityGroupStartedEmailSubject;

	@ModuleSetting
	@HTMLEditorSettingDescriptor(name = "Activity group started email message", description = "The message of emails sent to the users when an activity group is started for them", required = true)
	@XSLVariable(prefix = "java.")
	private String activityGroupStartedEmailMessage;
	
	@ModuleSetting
	@TextFieldSettingDescriptor(name = "Activity reminder email subject prefix", description = "The subject prefix of emails sent to remind users of an activity", required = true)
	@XSLVariable(prefix = "java.")
	private String reminderEmailPrefix;
	
	@ModuleSetting
	@TextFieldSettingDescriptor(name = "Check for expiring managers interval", description = "How often this module should check for expiring flow managers (specified in crontab format)", required = true, formatValidator = CronStringValidator.class)
	private String managersUpdateInterval = "0 0 * * *";

	private AnnotatedDAO<FlowApprovalActivity> activityDAO;
	private AnnotatedDAO<FlowApprovalActivityGroup> activityGroupDAO;
	private AnnotatedDAO<FlowApprovalActivityProgress> activityProgressDAO;

	private AdvancedAnnotatedDAOWrapper<FlowApprovalActivity, Integer> activityDAOWrapper;
	private AdvancedAnnotatedDAOWrapper<FlowApprovalActivityGroup, Integer> activityGroupDAOWrapper;

	private QueryParameterFactory<FlowApprovalActivityProgress, Integer> activityProgressFlowInstanceIDParamFactory;
	private QueryParameterFactory<FlowApprovalActivityProgress, FlowApprovalActivity> activityProgressActivityParamFactory;
	private QueryParameterFactory<FlowApprovalActivityGroup, Integer> activityGroupFlowFamilyIDParamFactory;
	private QueryParameterFactory<FlowApprovalActivityGroup, String> activityGroupStartStatusParamFactory;

	@InstanceManagerDependency(required = true)
	private StaticContentModule staticContentModule;

	protected StandardFlowNotificationHandler notificationHandler;

	private FlowAdminModule flowAdminModule;

	private ModuleViewFragmentTransformer<ForegroundModuleDescriptor> viewFragmentTransformer;
	private UserGroupListConnector userGroupListConnector;

	private FlowApprovalActivityCRUD activityCRUD;
	private FlowApprovalActivityGroupCRUD activityGroupCRUD;
	
	private Scheduler scheduler;
	private String updateManagersScheduleID;

	@Override
	public void init(ForegroundModuleDescriptor moduleDescriptor, SectionInterface sectionInterface, DataSource dataSource) throws Exception {

		viewFragmentTransformer = new ModuleViewFragmentTransformer<ForegroundModuleDescriptor>(sectionInterface.getForegroundModuleXSLTCache(), this, sectionInterface.getSystemInterface().getEncoding());

		super.init(moduleDescriptor, sectionInterface, dataSource);
		
		userGroupListConnector = new UserGroupListConnector(systemInterface);

		if (!systemInterface.getInstanceHandler().addInstance(FlowApprovalAdminModule.class, this)) {

			throw new RuntimeException("Unable to register module in global instance handler using key " + FlowApprovalAdminModule.class.getSimpleName() + ", another instance is already registered using this key.");
		}
		
		initScheduler();
	}

	@Override
	protected void createDAOs(DataSource dataSource) throws Exception {

		super.createDAOs(dataSource);

		//Automatic table version handling
		UpgradeResult upgradeResult = TableVersionHandler.upgradeDBTables(dataSource, FlowApprovalAdminModule.class.getName(), new XMLDBScriptProvider(FlowApprovalAdminModule.class.getResourceAsStream("DB script.xml")));

		if (upgradeResult.isUpgrade()) {

			log.info(upgradeResult.toString());
		}

		HierarchyAnnotatedDAOFactory daoFactory = new HierarchyAnnotatedDAOFactory(dataSource, systemInterface.getUserHandler(), systemInterface.getGroupHandler(), false, true, false);

		activityDAO = daoFactory.getDAO(FlowApprovalActivity.class);
		activityGroupDAO = daoFactory.getDAO(FlowApprovalActivityGroup.class);
		activityProgressDAO = daoFactory.getDAO(FlowApprovalActivityProgress.class);

		activityDAOWrapper = activityDAO.getAdvancedWrapper(Integer.class);
		activityDAOWrapper.getAddQuery().addRelations(FlowApprovalActivity.USERS_RELATION, FlowApprovalActivity.GROUPS_RELATION);
		activityDAOWrapper.getUpdateQuery().addRelations(FlowApprovalActivity.USERS_RELATION, FlowApprovalActivity.GROUPS_RELATION);
		activityDAOWrapper.getGetQuery().addRelations(FlowApprovalActivity.ACTIVITY_GROUP_RELATION, FlowApprovalActivity.USERS_RELATION, FlowApprovalActivity.GROUPS_RELATION);

		activityGroupDAOWrapper = activityGroupDAO.getAdvancedWrapper(Integer.class);
		activityGroupDAOWrapper.getGetQuery().addRelations(FlowApprovalActivityGroup.ACTIVITIES_RELATION, FlowApprovalActivity.USERS_RELATION, FlowApprovalActivity.GROUPS_RELATION);

		activityProgressFlowInstanceIDParamFactory = activityProgressDAO.getParamFactory("flowInstanceID", Integer.class);
		activityProgressActivityParamFactory = activityProgressDAO.getParamFactory("activity", FlowApprovalActivity.class);
		activityGroupFlowFamilyIDParamFactory = activityGroupDAO.getParamFactory("flowFamilyID", Integer.class);
		activityGroupStartStatusParamFactory = activityGroupDAO.getParamFactory("startStatus", String.class);

		activityCRUD = new FlowApprovalActivityCRUD(activityDAOWrapper, this);
		activityGroupCRUD = new FlowApprovalActivityGroupCRUD(activityGroupDAOWrapper, this);
	}
	
	@InstanceManagerDependency(required = true)
	public void setFlowAdminModule(FlowAdminModule flowAdminModule) {

		if (this.flowAdminModule != null) {

			this.flowAdminModule.removeFragmentExtensionViewProvider(this);
		}

		this.flowAdminModule = flowAdminModule;

		if (flowAdminModule != null) {

			flowAdminModule.addFragmentExtensionViewProvider(this);
		}
	}
	
	@Override
	public void update(ForegroundModuleDescriptor descriptor, DataSource dataSource) throws Exception {
		
		super.update(descriptor, dataSource);
		
		scheduler.reschedule(updateManagersScheduleID, managersUpdateInterval);
	}

	@Override
	protected void moduleConfigured() throws Exception {

		viewFragmentTransformer.setDebugXML(debugFragmentXML);
		viewFragmentTransformer.modifyScriptsAndLinks(true, null);
	}
	
	protected synchronized void initScheduler() {
		
		if (scheduler != null) {
			
			log.warn("Invalid state, scheduler already running!");
			stopScheduler();
		}
		
		scheduler = new Scheduler();
		updateManagersScheduleID = scheduler.schedule(managersUpdateInterval, this);
		
		scheduler.start();
	}
	
	protected synchronized void stopScheduler() {
		
		try {
			if (scheduler != null) {
				
				scheduler.stop();
				scheduler = null;
			}
			
		} catch (IllegalStateException e) {
			log.error("Error stopping scheduler", e);
		}
	}

	@Override
	public void unload() throws Exception {

		stopScheduler();
		
		systemInterface.getInstanceHandler().removeInstance(FlowApprovalAdminModule.class, this);

		super.unload();
	}

	public ForegroundModuleResponse list(String extensionRequestURL, Flow flow, HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser, List<ValidationError> validationErrors) throws SQLException {

		Document doc = createDocument(req, uriParser, user);

		Element showViewElement = doc.createElement("List");
		doc.getDocumentElement().appendChild(showViewElement);

		showViewElement.appendChild(flow.toXML(doc));
		XMLUtils.appendNewElement(doc, showViewElement, "extensionRequestURL", extensionRequestURL);
		XMLUtils.appendNewElement(doc, showViewElement, "Title", getExtensionViewTitle());

		List<FlowApprovalActivityGroup> activityGroups = getActivityGroups(flow.getFlowFamily().getFlowFamilyID());

		XMLUtils.append(doc, showViewElement, "ActivityGroups", activityGroups);
		XMLUtils.append(doc, showViewElement, "ValidationErrors", validationErrors);

		return new SimpleForegroundModuleResponse(doc, getDefaultBreadcrumb());
	}

	@Override
	public FlowAdminExtensionShowView getShowView(String extensionRequestURL, Flow flow, HttpServletRequest req, User user, URIParser uriParser) throws TransformerConfigurationException, TransformerException, SQLException {

		Document doc = createDocument(req, uriParser, user);

		Element showViewElement = doc.createElement("FlowOverviewExtension");
		doc.getDocumentElement().appendChild(showViewElement);

		showViewElement.appendChild(flow.toXML(doc));
		XMLUtils.appendNewElement(doc, showViewElement, "extensionRequestURL", extensionRequestURL);

		List<FlowApprovalActivityGroup> activityGroups = getActivityGroups(flow.getFlowFamily().getFlowFamilyID(), FlowApprovalActivityGroup.ACTIVITIES_RELATION);

		XMLUtils.append(doc, showViewElement, "ActivityGroups", activityGroups);

		List<ValidationError> validationErrors = null;

		boolean enabled = false;
		
		if (activityGroups != null) {

			enabled = true;
			
			// Invalid status
			for (FlowApprovalActivityGroup activityGroup : activityGroups) {

				boolean startStatusFound = false;
				boolean completeStatusFound = false;
				boolean denyStatusFound = !activityGroup.isUseApproveDeny();

				for (Status status : flow.getStatuses()) {

					if (!startStatusFound && status.getName().equalsIgnoreCase(activityGroup.getStartStatus())) {
						startStatusFound = true;
					}

					if (!completeStatusFound && status.getName().equalsIgnoreCase(activityGroup.getCompleteStatus())) {
						completeStatusFound = true;
					}

					if (!denyStatusFound && status.getName().equalsIgnoreCase(activityGroup.getDenyStatus())) {
						denyStatusFound = true;
					}

					if (startStatusFound && completeStatusFound && denyStatusFound) {
						break;
					}
				}

				if (!startStatusFound) {
					validationErrors = CollectionUtils.addAndInstantiateIfNeeded(validationErrors, new ActivityGroupInvalidStatus(activityGroup.getName(), activityGroup.getStartStatus()));
				}

				if (!completeStatusFound) {
					validationErrors = CollectionUtils.addAndInstantiateIfNeeded(validationErrors, new ActivityGroupInvalidStatus(activityGroup.getName(), activityGroup.getCompleteStatus()));
				}

				if (!denyStatusFound) {
					validationErrors = CollectionUtils.addAndInstantiateIfNeeded(validationErrors, new ActivityGroupInvalidStatus(activityGroup.getName(), activityGroup.getDenyStatus()));
				}
			}

			// Conflicting statuses
			HashMap<String, String> startToCompleteStatusMapping = new HashMap<>();

			for (FlowApprovalActivityGroup activityGroup : activityGroups) {

				String existingCompleteStatus = startToCompleteStatusMapping.get(activityGroup.getStartStatus().toLowerCase());

				if (existingCompleteStatus != null) {

					if (!activityGroup.getCompleteStatus().toLowerCase().equals(existingCompleteStatus)) {

						validationErrors = CollectionUtils.addAndInstantiateIfNeeded(validationErrors, new ValidationError("MultipleCompletionStatusesForSameStartStatus"));
						break;
					}

				} else {

					startToCompleteStatusMapping.put(activityGroup.getStartStatus().toLowerCase(), activityGroup.getCompleteStatus().toLowerCase());
				}
			}

			HashMap<String, String> startToDenyStatusMapping = new HashMap<>();

			for (FlowApprovalActivityGroup activityGroup : activityGroups) {

				if (activityGroup.isUseApproveDeny()) {

					String existingDenyStatus = startToDenyStatusMapping.get(activityGroup.getStartStatus().toLowerCase());

					if (existingDenyStatus != null) {

						if (!activityGroup.getDenyStatus().toLowerCase().equals(existingDenyStatus)) {

							validationErrors = CollectionUtils.addAndInstantiateIfNeeded(validationErrors, new ValidationError("MultipleDenyStatusesForSameStartStatus"));
							break;
						}

					} else {

						startToDenyStatusMapping.put(activityGroup.getStartStatus().toLowerCase(), activityGroup.getDenyStatus().toLowerCase());
					}
				}
			}
			
		}

		if (validationErrors != null) {

			XMLUtils.append(doc, showViewElement, "ValidationErrors", validationErrors);
		}

		return new FlowAdminExtensionShowView(viewFragmentTransformer.createViewFragment(doc, true), enabled);
	}

	@Override
	public ViewFragment processRequest(String extensionRequestURL, Flow flow, HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Exception {

		String method = uriParser.get(4);

		req.setAttribute("extensionRequestURL", extensionRequestURL);
		req.setAttribute("flow", flow);

		if ("showactivitygroup".equals(method)) {

			return getViewFragmentResponse(activityGroupCRUD.show(req, res, user, uriParser));

		} else if ("addactivitygroup".equals(method)) {

			return getViewFragmentResponse(activityGroupCRUD.add(req, res, user, uriParser));

		} else if ("updateactivitygroup".equals(method)) {

			return getViewFragmentResponse(activityGroupCRUD.update(req, res, user, uriParser));
			
		} else if ("sortactivitygroups".equals(method)) {

			return sortActivityGroups(extensionRequestURL, flow, req, res, user, uriParser);

		} else if ("deleteactivitygroup".equals(method)) {

			return getViewFragmentResponse(activityGroupCRUD.delete(req, res, user, uriParser));

		} else if ("showactivity".equals(method)) {

			return getViewFragmentResponse(activityCRUD.show(req, res, user, uriParser));

		} else if ("addactivity".equals(method)) {

			return getViewFragmentResponse(activityCRUD.add(req, res, user, uriParser));

		} else if ("updateactivity".equals(method)) {

			return getViewFragmentResponse(activityCRUD.update(req, res, user, uriParser));

		} else if ("deleteactivity".equals(method)) {

			return getViewFragmentResponse(activityCRUD.delete(req, res, user, uriParser));

		} else if ("users".equals(method)) {

			userGroupListConnector.getUsers(req, res, user, uriParser);
			return null;

		} else if ("groups".equals(method)) {

			userGroupListConnector.getGroups(req, res, user, uriParser);
			return null;

		} else if ("statuses".equals(method)) {

			searchStatuses(flow, req, res, user, uriParser);
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

	private void searchStatuses(Flow flow, HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws IOException {

		String search = req.getParameter("q");

		if (StringUtils.isEmpty(search)) {

			sendEmptyJSONResponse(res);
			return;
		}

		if (!systemInterface.getEncoding().equalsIgnoreCase("UTF-8")) {
			search = URLDecoder.decode(search, "UTF-8");
		}

		Set<String> statusNames = new HashSet<String>();

		String searchLower = search.toLowerCase();

		if(flow.getStatuses() != null) {
		
			for (Status status : flow.getStatuses()) {
	
				if (ArrayUtils.contains(FlowApprovalActivityGroupCRUD.INVALID_STATUS_TYPES, status.getContentType())) {
					continue;
				}
				
				if (status.getName().toLowerCase().contains(searchLower)) {
					statusNames.add(status.getName());
				}
			}
		
		}

		log.info("User " + user + " searching for statuses using query " + search + " in flow " + flow + ", found " + CollectionUtils.getSize(statusNames) + " hits");

		if (statusNames.isEmpty()) {

			sendEmptyJSONResponse(res);
			return;
		}

		JsonArray jsonArray = new JsonArray();

		for (String status : statusNames) {

			jsonArray.addNode(status);
		}

		sendJSONResponse(jsonArray, res);
		return;
	}

	private static void sendEmptyJSONResponse(HttpServletResponse res) throws IOException {

		JsonObject jsonObject = new JsonObject(1);
		jsonObject.putField("hitCount", "0");
		HTTPUtils.sendReponse(jsonObject.toJson(), JsonUtils.getContentType(), res);
	}

	private void sendJSONResponse(JsonArray jsonArray, HttpServletResponse res) throws IOException {

		JsonObject jsonObject = new JsonObject(2);
		jsonObject.putField("hitCount", Integer.toString(jsonArray.size()));
		jsonObject.putField("hits", jsonArray);
		HTTPUtils.sendReponse(jsonObject.toJson(), JsonUtils.getContentType(), res);
	}
	
	private ViewFragment sortActivityGroups(String extensionRequestURL, Flow flow, HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws SQLException, TransformerConfigurationException, TransformerException  {

		List<FlowApprovalActivityGroup> activityGroups = getActivityGroups(flow.getFlowFamily().getFlowFamilyID(), FlowApprovalActivityGroup.ACTIVITIES_RELATION);

		if (activityGroups == null) {
			return null;
		}

		if (req.getMethod().equalsIgnoreCase("POST")) {

			for (FlowApprovalActivityGroup activityGroup : activityGroups) {

				String sortIndex = req.getParameter("sortorder_" + activityGroup.getActivityGroupID());

				if (NumberUtils.isInt(sortIndex)) {

					activityGroup.setSortIndex(NumberUtils.toInt(sortIndex));
				}
			}

			activityGroupDAO.update(activityGroups, null);

			flowAdminModule.addFlowFamilyEvent(eventActivityGroupsSorted, flow.getFlowFamily(), user);

			return null;
		}

		log.info("User " + user + " requesting sort activity groups form for flow " + flow);

		Document doc = createDocument(req, uriParser, user);

		Element sortActivityGroupsElement = doc.createElement("SortActivityGroups");
		doc.getDocumentElement().appendChild(sortActivityGroupsElement);

		XMLUtils.append(doc, sortActivityGroupsElement, "ActivityGroups", activityGroups);

		return viewFragmentTransformer.createViewFragment(doc);
	}

	public void checkApprovalCompletion(FlowApprovalActivityGroup modifiedActivityGroup, FlowInstance flowInstance) throws SQLException {

		List<FlowApprovalActivityGroup> activityGroups = getActivityGroups(flowInstance, FlowApprovalActivityGroup.ACTIVITIES_RELATION, FlowApprovalActivity.ACTIVITY_PROGRESSES_RELATION);

		// Add history event if modifiedActivityGroup was completed
		for (FlowApprovalActivityGroup activityGroup : activityGroups) {

			if (activityGroup.getActivityGroupID().equals(modifiedActivityGroup.getActivityGroupID())) {

				boolean anyStarted = false;
				boolean noPending = true;
				boolean denied = false;

				for (FlowApprovalActivity activity : activityGroup.getActivities()) {

					if (activity.getActivityProgresses() == null) { // Activity not started
						continue;
					}

					anyStarted = true;

					FlowApprovalActivityProgress activityProgress = activity.getActivityProgresses().get(0);

					if (activityProgress.getCompleted() == null) {

						noPending = false;
						break;

					}

					if (activityGroup.isUseApproveDeny() && activityProgress.isDenied()) {

						denied = true;
					}
				}

				if (anyStarted && noPending) {

					if (activityGroup.isUseApproveDeny()) {

						if (denied) {

							log.info("Completed denied activity group " + activityGroup + " for " + flowInstance);
							flowAdminModule.getFlowInstanceEventGenerator().addFlowInstanceEvent(flowInstance, EventType.OTHER_EVENT, eventActivityGroupDenied + " " + activityGroup.getName(), null);

						} else {

							log.info("Completed approved activity group " + activityGroup + " for " + flowInstance);
							flowAdminModule.getFlowInstanceEventGenerator().addFlowInstanceEvent(flowInstance, EventType.OTHER_EVENT, eventActivityGroupApproved + " " + activityGroup.getName(), null);
						}

					} else {

						log.info("Completed activity group " + activityGroup + " for " + flowInstance);
						flowAdminModule.getFlowInstanceEventGenerator().addFlowInstanceEvent(flowInstance, EventType.OTHER_EVENT, eventActivityGroupCompleted + " " + activityGroup.getName(), null);
					}
				}

				break;
			}
		}

		Status currentStatus = flowInstance.getStatus();

		List<FlowApprovalActivityGroup> activityGroupsForCurrentStatus = new ArrayList<>();

		for (FlowApprovalActivityGroup activityGroup : activityGroups) {

			if (currentStatus.getName().equalsIgnoreCase(activityGroup.getStartStatus())) {

				activityGroupsForCurrentStatus.add(activityGroup);
			}
		}

		if (!activityGroupsForCurrentStatus.isEmpty()) {

			boolean anyCompleted = false;
			boolean noPending = true;
			boolean denied = false;

			outer: for (FlowApprovalActivityGroup activityGroup : activityGroupsForCurrentStatus) {

				if (activityGroup.getActivities() != null) {

					boolean anyStarted = false;

					for (FlowApprovalActivity activity : activityGroup.getActivities()) {

						if (activity.getActivityProgresses() == null) { // Activity not started
							continue;
						}

						anyStarted = true;

						FlowApprovalActivityProgress activityProgress = activity.getActivityProgresses().get(0);

						if (activityProgress.getCompleted() == null) {

							noPending = false;
							break outer;

						}

						anyCompleted = true;

						if (activityGroup.isUseApproveDeny() && activityProgress.isDenied()) {

							denied = true;
						}
					}

					if (!anyStarted) {

						activityGroup.setActivities(null);
					}
				}
			}

			if (anyCompleted && noPending) {

				String newStatusName = null;

				if (denied) {

					for (FlowApprovalActivityGroup activityGroup : activityGroupsForCurrentStatus) {

						if (activityGroup.isUseApproveDeny()) {

							newStatusName = activityGroup.getDenyStatus();
							break;
						}
					}

					log.info("All activities completed but denied for " + flowInstance + " and status " + currentStatus + ", new status " + newStatusName);

				} else {

					newStatusName = activityGroupsForCurrentStatus.get(0).getCompleteStatus();

					log.info("All activities completed successfully for " + flowInstance + " and status " + currentStatus + ", new status " + newStatusName);
				}

				Status newStatus = null;

				for (Status status : flowInstance.getFlow().getStatuses()) {

					if (status.getName().equalsIgnoreCase(newStatusName)) {
						newStatus = status;
						break;
					}
				}

				if (newStatus == null) {

					if (denied) {

						log.error("Unable to find denied status \"" + newStatusName + "\" for " + flowInstance);

					} else {

						log.error("Unable to find complete status \"" + newStatusName + "\" for " + flowInstance);
					}

				} else {

					flowInstance.setStatus(newStatus);
					flowInstance.setLastStatusChange(TimeUtils.getCurrentTimestamp());

					flowAdminModule.getDAOFactory().getFlowInstanceDAO().update(flowInstance);

					StringBuilder activityGroupNames = new StringBuilder();

					for (FlowApprovalActivityGroup activityGroup : activityGroupsForCurrentStatus) {

						if (activityGroup.getActivities() != null) {

							if (activityGroupNames.length() > 0) {
								activityGroupNames.append(", ");
							}

							activityGroupNames.append(activityGroup.getName());
						}
					}

					FlowInstanceEvent flowInstanceEvent = flowAdminModule.getFlowInstanceEventGenerator().addFlowInstanceEvent(flowInstance, EventType.STATUS_UPDATED, eventActivityGroupCompleted + " " + activityGroupNames.toString(), null);

					systemInterface.getEventHandler().sendEvent(FlowInstance.class, new CRUDEvent<FlowInstance>(CRUDAction.UPDATE, flowInstance), EventTarget.ALL);
					systemInterface.getEventHandler().sendEvent(FlowInstance.class, new StatusChangedByManagerEvent(flowInstance, flowInstanceEvent, flowAdminModule.getSiteProfile(flowInstance), currentStatus, null), EventTarget.ALL);
				}
			}
		}
	}

	private void triggerActivityGroups(ImmutableFlowInstance flowInstance, Status newStatus) throws SQLException {

		List<FlowApprovalActivityGroup> activityGroups = getActivityGroups(flowInstance.getFlow().getFlowFamily().getFlowFamilyID(), newStatus.getName(), FlowApprovalActivityGroup.ACTIVITIES_RELATION, FlowApprovalActivity.USERS_RELATION, FlowApprovalActivity.GROUPS_RELATION);

		if (activityGroups != null) {

			Timestamp now = TimeUtils.getCurrentTimestamp();

			StringBuilder activityGroupNames = new StringBuilder();

			for (FlowApprovalActivityGroup activityGroup : activityGroups) {

				if (activityGroup.getActivities() != null) {

					log.debug("Starting activity group " + activityGroup + " for " + flowInstance);

					TransactionHandler transactionHandler = activityProgressDAO.createTransaction();

					try {

						Map<FlowApprovalActivity, FlowApprovalActivityProgress> createdActivities = new HashMap<>(activityGroup.getActivities().size());

						for (FlowApprovalActivity activity : activityGroup.getActivities()) {

							FlowApprovalActivityProgress progress = getActivityProgress(activity, flowInstance);

							if (progress == null) {

								boolean createProgress = true;

								if (activity.getAttributeName() != null && activity.getAttributeValues() != null) {

									boolean match = false;

									AttributeHandler attributeHandler = flowInstance.getAttributeHandler();

									if (attributeHandler != null) {

										String value = attributeHandler.getString(activity.getAttributeName());

										if (value != null) {

											match = activity.getAttributeValues().contains(value);

											if (!match && value.contains(",")) {

												String[] values = value.split(", ?");

												for (String splitValue : values) {

													match = activity.getAttributeValues().contains(splitValue);

													if (match) {
														break;
													}
												}
											}
										}
									}

									createProgress = match != activity.isInverted();
								}

								if (createProgress) {

									progress = new FlowApprovalActivityProgress();
									progress.setFlowInstanceID(flowInstance.getFlowInstanceID());
									progress.setActivity(activity);
									progress.setAdded(now);

									if (activity.getResponsibleUserAttributeNames() != null) {

										List<User> responsibleUsers = getResponsibleUsersFromAttribute(activity, flowInstance);

										if (responsibleUsers != null) {

											progress.setResponsibleAttributedUsers(responsibleUsers);
										}
									}

									activityProgressDAO.add(progress, transactionHandler, null);

									createdActivities.put(activity, progress);
								}
							}
						}

						if (!createdActivities.isEmpty()) {

							transactionHandler.commit();

							if (activityGroup.isSendActivityGroupStartedEmail()) {

								sendActivityGroupStartedNotifications(createdActivities, activityGroup, flowInstance, false);
							}

							if (activityGroupNames.length() > 0) {
								activityGroupNames.append(", ");
							}

							activityGroupNames.append(activityGroup.getName());
						}

					} finally {
						TransactionHandler.autoClose(transactionHandler);
					}
				}
			}

			if (activityGroupNames.length() > 0) {

				log.info("Started activity groups " + activityGroupNames.toString() + " for " + flowInstance);

				flowAdminModule.getFlowInstanceEventGenerator().addFlowInstanceEvent(flowInstance, EventType.OTHER_EVENT, eventActivityGroupStarted + " " + activityGroupNames.toString(), null);
			}
		}
	}
	
	public void sendActivityGroupStartedNotifications(Map<FlowApprovalActivity, FlowApprovalActivityProgress> createdActivities, FlowApprovalActivityGroup activityGroup, ImmutableFlowInstance flowInstance, boolean reminder) throws SQLException {

		String subject = ObjectUtils.getFirstNotNull(activityGroup.getActivityGroupStartedEmailSubject(), activityGroupStartedEmailSubject);
		String message = ObjectUtils.getFirstNotNull(activityGroup.getActivityGroupStartedEmailMessage(), activityGroupStartedEmailMessage);

		if (subject == null || message == null) {

			log.warn("no subject or message");
			return;
		}

		List<TagSource> sharedTagSources = new ArrayList<TagSource>(4);

		sharedTagSources.add(ACTIVITY_GROUP_TAG_SOURCE_FACTORY.getTagSource(activityGroup));
		sharedTagSources.add(FLOW_INSTANCE_TAG_SOURCE_FACTORY.getTagSource((FlowInstance) flowInstance));
		sharedTagSources.add(FLOW_TAG_SOURCE_FACTORY.getTagSource((Flow) flowInstance.getFlow()));
		
		sharedTagSources.add(new SingleTagSource("$myActivitiesURL", userApprovalModuleAlias));

		HashSet<User> managers = new HashSet<>();
		HashSet<String> globalRecipients = new HashSet<>();

		for (FlowApprovalActivity activity : createdActivities.keySet()) {

			boolean useFallbackUsers = true;

			if (activity.getResponsibleUserAttributeNames() != null) {

				List<User> users = getResponsibleUsersFromAttribute(activity, flowInstance);

				if (users != null) {

					useFallbackUsers = false;
					managers.addAll(users);
				}
			}

			if (activity.getResponsibleUsers() != null) {

				for (FlowApprovalActivityResponsibleUser responsibleUser : activity.getResponsibleUsers()) {

					if (!responsibleUser.isFallback() || useFallbackUsers) {

						managers.add(responsibleUser.getUser());
					}
				}
			}

			if (activity.getResponsibleGroups() != null) {

				List<User> groupUsers = null;

				for (int groupID : UserUtils.getGroupIDs(activity.getResponsibleGroups())) {

					groupUsers = CollectionUtils.addAndInstantiateIfNeeded(groupUsers, systemInterface.getUserHandler().getUsersByGroup(groupID, true, true));
				}

				if (groupUsers != null) {

					managers.addAll(groupUsers);
				}
			}
			
			if (activity.getGlobalEmailAddress() != null) {
				
				globalRecipients.add(activity.getGlobalEmailAddress());
			}
		}

		log.info("Sending emails for started " + activityGroup + " to " + managers.size() + " managers and " + globalRecipients.size() + " global recipients");

		StringBuilder activitiesStringBuilder = new StringBuilder();

		for (User manager : managers) {

			if (!StringUtils.isEmpty(manager.getEmail())) {

				activitiesStringBuilder.setLength(0);

				for (Entry<FlowApprovalActivity, FlowApprovalActivityProgress> entry : createdActivities.entrySet()) {
					FlowApprovalActivity activity = entry.getKey();
					FlowApprovalActivityProgress activityProgress = entry.getValue();

					if (AccessUtils.checkAccess(manager, activityProgress)) {

						if (activitiesStringBuilder.length() > 0) {
							activitiesStringBuilder.append("<br/>");
						}

						activitiesStringBuilder.append(activity.getName());
					}
				}

				TagReplacer tagReplacer = new TagReplacer();
				
				tagReplacer.addTagSources(sharedTagSources);
				tagReplacer.addTagSource(MANAGER_TAG_SOURCE_FACTORY.getTagSource(manager));
				tagReplacer.addTagSource(new SingleTagSource("$activities", activitiesStringBuilder.toString()));

				SimpleEmail email = new SimpleEmail(systemInterface.getEncoding());

				try {
					email.addRecipient(manager.getEmail());
					email.setMessageContentType(SimpleEmail.HTML);
					email.setSenderName(notificationHandler.getEmailSenderName(null));
					email.setSenderAddress(notificationHandler.getEmailSenderAddress(null));
					email.setSubject(tagReplacer.replace(subject));
					email.setMessage(EmailUtils.addMessageBody(replaceTags(message, tagReplacer, flowInstance)));
					
					if (reminder) {
						email.setSubject(reminderEmailPrefix + email.getSubject());
					}

					systemInterface.getEmailHandler().send(email);

				} catch (Exception e) {

					log.info("Error generating/sending email " + email, e);
				}
			}
		}

		if (!globalRecipients.isEmpty()) {

			sharedTagSources.add(new SingleTagSource("$manager.firstname", ""));
			sharedTagSources.add(new SingleTagSource("$manager.lastname", ""));

			for (String emailAdress : globalRecipients) {

				activitiesStringBuilder.setLength(0);

				for (FlowApprovalActivity activity : createdActivities.keySet()) {

					if (emailAdress.equals(activity.getGlobalEmailAddress())) {

						if (activitiesStringBuilder.length() > 0) {
							activitiesStringBuilder.append("<br/>");
						}

						activitiesStringBuilder.append(activity.getName());
					}
				}
				
				TagReplacer tagReplacer = new TagReplacer(sharedTagSources);

				tagReplacer.addTagSource(new SingleTagSource("$activities", activitiesStringBuilder.toString()));

				SimpleEmail email = new SimpleEmail(systemInterface.getEncoding());

				try {
					email.addRecipient(emailAdress);
					email.setMessageContentType(SimpleEmail.HTML);
					email.setSenderName(notificationHandler.getEmailSenderName(null));
					email.setSenderAddress(notificationHandler.getEmailSenderAddress(null));
					email.setSubject(tagReplacer.replace(subject));
					email.setMessage(EmailUtils.addMessageBody(replaceTags(message, tagReplacer, flowInstance)));
					
					if (reminder) {
						email.setSubject(reminderEmailPrefix + email.getSubject());
					}

					systemInterface.getEmailHandler().send(email);

				} catch (Exception e) {

					log.info("Error generating/sending email " + email, e);
				}
			}
		}
	}

	@EventListener(channel = FlowInstanceManager.class)
	public void processSubmitEvent(SubmitEvent event, EventSource eventSource) {

		try {
			triggerActivityGroups(event.getFlowInstanceManager().getFlowInstance(), event.getFlowInstanceManager().getFlowState());

		} catch (Exception e) {
			log.error("Error processing SubmitEvent " + event, e);
		}
	}

	@EventListener(channel = FlowInstance.class)
	public void processStatusChangedEvent(StatusChangedByManagerEvent event, EventSource eventSource) {

		try {
			FlowInstance flowInstance = event.getFlowInstance();

			triggerActivityGroups(flowInstance, flowInstance.getStatus());

		} catch (Exception e) {
			log.error("Error processing StatusChangedByManagerEvent " + event, e);
		}
	}

	@EventListener(channel = FlowFamily.class)
	public void processFlowFamilyEvent(CRUDEvent<FlowFamily> event, EventSource source) throws SQLException {

		if (event.getAction() == CRUDAction.DELETE) {

			for (FlowFamily flowFamily : event.getBeans()) {

				log.info("Deleting approval settings for " + flowFamily);

				HighLevelQuery<FlowApprovalActivityGroup> query = new HighLevelQuery<FlowApprovalActivityGroup>();
				query.addParameter(activityGroupFlowFamilyIDParamFactory.getParameter(flowFamily.getFlowFamilyID()));

				activityGroupDAO.delete(query);
			}
		}
	}

	@EventListener(channel = FlowInstance.class)
	public void processFlowInstanceEvent(CRUDEvent<FlowInstance> event, EventSource source) throws SQLException {

		if (event.getAction() == CRUDAction.DELETE) {

			for (FlowInstance flowInstance : event.getBeans()) {

				log.info("Deleting approval progress for " + flowInstance);

				HighLevelQuery<FlowApprovalActivityProgress> query = new HighLevelQuery<FlowApprovalActivityProgress>();
				query.addParameter(activityProgressFlowInstanceIDParamFactory.getParameter(flowInstance.getFlowInstanceID()));

				activityProgressDAO.delete(query);
			}
			
		} else if (event.getAction() == CRUDAction.UPDATE) {
			
			//TODO start new activites
			//TODO mark old activites as aborted, then check for group completed
			
			for (FlowInstance flowInstance : event.getBeans()) {

				List<FlowApprovalActivityGroup> activityGroups = getActivityGroups(flowInstance.getFlow().getFlowFamily().getFlowFamilyID(), flowInstance.getStatus().getName(), FlowApprovalActivityGroup.ACTIVITIES_RELATION, FlowApprovalActivity.USERS_RELATION, FlowApprovalActivity.GROUPS_RELATION);

				if (activityGroups != null) {

					for (FlowApprovalActivityGroup activityGroup : activityGroups) {

						if (activityGroup.getActivities() != null) {

							TransactionHandler transactionHandler = activityProgressDAO.createTransaction();

							try {
								Map<FlowApprovalActivity, FlowApprovalActivityProgress> updatedActivities = new HashMap<>(activityGroup.getActivities().size());

								for (FlowApprovalActivity activity : activityGroup.getActivities()) {

									if (activity.getResponsibleUserAttributeNames() != null) {

										boolean activeActivity = true;

										if (activity.getAttributeName() != null && activity.getAttributeValues() != null) {

											boolean match = false;

											AttributeHandler attributeHandler = flowInstance.getAttributeHandler();

											if (attributeHandler != null) {

												String value = attributeHandler.getString(activity.getAttributeName());

												if (value != null) {

													match = activity.getAttributeValues().contains(value);

													if (!match && value.contains(",")) {

														String[] values = value.split(", ?");

														for (String splitValue : values) {

															match = activity.getAttributeValues().contains(splitValue);

															if (match) {
																break;
															}
														}
													}
												}
											}

											activeActivity = match != activity.isInverted();
										}

										if (activeActivity) {

											FlowApprovalActivityProgress progress = getActivityProgress(activity, flowInstance);

											if (progress != null) {

												List<User> responsibleUsers = getResponsibleUsersFromAttribute(activity, flowInstance);

												if ((responsibleUsers == null && progress.getResponsibleAttributedUsers() != null) || (responsibleUsers != null && !responsibleUsers.equals(progress.getResponsibleAttributedUsers()))) {

													log.info("Updating responsible user for " + progress + " from " + (progress.getResponsibleAttributedUsers() != null ? StringUtils.toCommaSeparatedString(progress.getResponsibleAttributedUsers()) : "null") + " to " + (responsibleUsers != null ? StringUtils.toCommaSeparatedString(responsibleUsers) : "null"));
													progress.setResponsibleAttributedUsers(responsibleUsers);

													progress.setActivity(activity);
													activityProgressDAO.update(progress, transactionHandler, null);

													updatedActivities.put(activity, progress);
												}
											}
										}
									}
								}

								if (!updatedActivities.isEmpty()) {

									transactionHandler.commit();

									if (activityGroup.isSendActivityGroupStartedEmail()) {

										sendActivityGroupStartedNotifications(updatedActivities, activityGroup, flowInstance, false);
									}
								}

							} finally {
								TransactionHandler.autoClose(transactionHandler);
							}
						}
					}
				}
			}
		}
	}
	
	private String replaceTags(String template, TagReplacer tagReplacer, ImmutableFlowInstance flowInstance) {

		return AttributeTagUtils.replaceTags(tagReplacer.replace(template), flowInstance.getAttributeHandler());
	}
	
	private List<User> getResponsibleUsersFromAttribute(FlowApprovalActivity activity, ImmutableFlowInstance flowInstance) {

		List<User> users = null;
		
		for (String attributeName : activity.getResponsibleUserAttributeNames()) {
			
			String username = flowInstance.getAttributeHandler().getString(attributeName);
			
			if (username != null) {
				
				User user = systemInterface.getUserHandler().getUserByUsername(username, false, true);
				
				if (user != null) {
					users = CollectionUtils.addAndInstantiateIfNeeded(users, user);
				}
			}
		}

		return users;
	}

	public FlowApprovalActivityGroup getActivityGroup(Integer activityGroupID) throws SQLException {

		return activityGroupDAOWrapper.get(activityGroupID);
	}

	private List<FlowApprovalActivityGroup> getActivityGroups(Integer flowFamilyID, Field... relations) throws SQLException {

		HighLevelQuery<FlowApprovalActivityGroup> query = new HighLevelQuery<FlowApprovalActivityGroup>();

		query.addParameter(activityGroupFlowFamilyIDParamFactory.getParameter(flowFamilyID));

		if (relations != null) {
			query.addRelations(relations);
		}

		return activityGroupDAO.getAll(query);
	}
	
	private List<FlowApprovalActivityGroup> getActivityGroups(Integer flowFamilyID, String startStatusName, Field... relations) throws SQLException {

		HighLevelQuery<FlowApprovalActivityGroup> query = new HighLevelQuery<FlowApprovalActivityGroup>();

		query.addParameter(activityGroupFlowFamilyIDParamFactory.getParameter(flowFamilyID));
		query.addParameter(activityGroupStartStatusParamFactory.getParameter(startStatusName));

		if (relations != null) {
			query.addRelations(relations);
		}

		return activityGroupDAO.getAll(query);
	}

	private List<FlowApprovalActivityGroup> getActivityGroups(ImmutableFlowInstance flowInstance, Field... relations) throws SQLException {

		HighLevelQuery<FlowApprovalActivityGroup> query = new HighLevelQuery<FlowApprovalActivityGroup>();

		query.addParameter(activityGroupFlowFamilyIDParamFactory.getParameter(flowInstance.getFlow().getFlowFamily().getFlowFamilyID()));
		query.addRelationParameter(FlowApprovalActivityProgress.class, activityProgressFlowInstanceIDParamFactory.getParameter(flowInstance.getFlowInstanceID()));

		if (relations != null) {
			query.addRelations(relations);
		}

		return activityGroupDAO.getAll(query);
	}

	private FlowApprovalActivityProgress getActivityProgress(FlowApprovalActivity activity, ImmutableFlowInstance flowInstance, Field... relations) throws SQLException {

		HighLevelQuery<FlowApprovalActivityProgress> query = new HighLevelQuery<FlowApprovalActivityProgress>();

		query.addParameter(activityProgressActivityParamFactory.getParameter(activity));
		query.addParameter(activityProgressFlowInstanceIDParamFactory.getParameter(flowInstance.getFlowInstanceID()));

		if (relations != null) {
			query.addRelations(relations);
		}

		return activityProgressDAO.get(query);
	}

	@Override
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
	
	@Override
	public String getTitlePrefix() {
		return adminExtensionViewTitle;
	}

	@Override
	public String getExtensionViewTitle() {

		return adminExtensionViewTitle;
	}

	@Override
	public String getExtensionViewLinkName() {

		return "flowapprovalsettings";
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
	public int getModuleID() {

		return moduleDescriptor.getModuleID();
	}

	@Override
	public List<LinkTag> getLinkTags() {

		return links;
	}

	@Override
	public List<ScriptTag> getScriptTags() {

		return scripts;
	}

	public void addFlowFamilyEvent(String message, FlowFamily flowFamily, User user) {

		flowAdminModule.addFlowFamilyEvent(message, flowFamily, user);
	}

	public String getEventActivityGroupAddedMessage() {
		return eventActivityGroupAdded;
	}

	public String getEventActivityGroupUpdatedMessage() {
		return eventActivityGroupUpdated;
	}

	public String getEventActivityGroupDeletedMessage() {
		return eventActivityGroupDeleted;
	}

	public String getEventActivityAddedMessage() {
		return eventActivityAdded;
	}

	public String getEventActivityUpdatedMessage() {
		return eventActivityUpdated;
	}

	public String getEventActivityDeletedMessage() {
		return eventActivityDeleted;
	}

	public UserHandler getUserHandler() {
		return systemInterface.getUserHandler();
	}

	public GroupHandler getGroupHandler() {
		return systemInterface.getGroupHandler();
	}

	@Override
	public void run() {

		try {
			log.info("Sending reminders for flow approval activities..");

			// @formatter:off
			LowLevelQuery<FlowApprovalActivityProgress> query = new LowLevelQuery<>(
					"SELECT DISTINCT ap.activityProgressID as dummy, ap.* FROM " + activityProgressDAO.getTableName() + " ap"
					+" INNER JOIN " + activityDAO.getTableName() + " a ON ap.activityID = a.activityID"
					+" INNER JOIN " + activityGroupDAO.getTableName() + " ag ON ag.activityGroupID = a.activityGroupID AND ag.reminderAfterXDays IS NOT NULL"
					+" WHERE ap.completed IS NULL AND ap.automaticReminderSent = 0 AND DATEDIFF(NOW(), ap.added) >= ag.reminderAfterXDays"
			);
			// @formatter:on

			query.addRelations(FlowApprovalActivityProgress.ACTIVITY_RELATION, FlowApprovalActivity.ACTIVITY_GROUP_RELATION);
			query.addCachedRelations(FlowApprovalActivityProgress.ACTIVITY_RELATION, FlowApprovalActivity.ACTIVITY_GROUP_RELATION);

			List<FlowApprovalActivityProgress> activityProgresses = activityProgressDAO.getAll(query);

			if (activityProgresses != null) {

				Map<Integer, Map<FlowApprovalActivity, FlowApprovalActivityProgress>> flowInstanceMap = new HashMap<>();

				for (FlowApprovalActivityProgress activityProgress : activityProgresses) {

					Map<FlowApprovalActivity, FlowApprovalActivityProgress> activityMap = flowInstanceMap.get(activityProgress.getFlowInstanceID());

					if (activityMap == null) {

						activityMap = new HashMap<>();
						flowInstanceMap.put(activityProgress.getFlowInstanceID(), activityMap);
					}

					activityMap.put(activityProgress.getActivity(), activityProgress);

					activityProgress.setAutomaticReminderSent(true);
				}

				for (Entry<Integer, Map<FlowApprovalActivity, FlowApprovalActivityProgress>> entry : flowInstanceMap.entrySet()) {

					FlowInstance flowInstance = flowAdminModule.getFlowInstance(entry.getKey());

					if (flowInstance != null) {

						FlowApprovalActivity activity = entry.getValue().keySet().iterator().next();

						sendActivityGroupStartedNotifications(entry.getValue(), activity.getActivityGroup(), flowInstance, true);
					}
				}
				
				activityProgressDAO.update(activityProgresses, null);
			}

		} catch (Throwable t) {
			log.error("Error sending reminders for flow approval activities", t);
		}
	}
	
	@WebPublic(requireLogin = true)
	public ForegroundModuleResponse sendReminders(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws URINotFoundException {
		
		if (user.isAdmin()) {
			
			run();
			return new SimpleForegroundModuleResponse("See log", getDefaultBreadcrumb());
		}
		
		throw new URINotFoundException(uriParser);
	}

	@InstanceManagerDependency(required = true)
	public void setNotificationHandler(StandardFlowNotificationHandler notificationHandler) {
		
		if(notificationHandler != null && (this.userApprovalModuleAlias == null || this.userApprovalModuleAlias.equals("not set"))) {
			
			String baseURL = notificationHandler.getUserFlowInstanceModuleAlias(null);
			
			this.userApprovalModuleAlias = StringUtils.substringBefore(baseURL, StringUtils.substringAfterLast(baseURL, "/")) + "flowapproval";
		}
		
		this.notificationHandler = notificationHandler;
	}
}
