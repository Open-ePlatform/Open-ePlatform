package com.nordicpeak.flowengine;

import java.io.IOException;
import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.naming.OperationNotSupportedException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import se.unlogic.hierarchy.core.annotations.CheckboxSettingDescriptor;
import se.unlogic.hierarchy.core.annotations.EnumDropDownSettingDescriptor;
import se.unlogic.hierarchy.core.annotations.InstanceManagerDependency;
import se.unlogic.hierarchy.core.annotations.ModuleSetting;
import se.unlogic.hierarchy.core.annotations.TextFieldSettingDescriptor;
import se.unlogic.hierarchy.core.annotations.WebPublic;
import se.unlogic.hierarchy.core.annotations.XSLVariable;
import se.unlogic.hierarchy.core.beans.Group;
import se.unlogic.hierarchy.core.beans.SimpleForegroundModuleResponse;
import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.hierarchy.core.enums.CRUDAction;
import se.unlogic.hierarchy.core.enums.EventSource;
import se.unlogic.hierarchy.core.enums.EventTarget;
import se.unlogic.hierarchy.core.events.CRUDEvent;
import se.unlogic.hierarchy.core.exceptions.AccessDeniedException;
import se.unlogic.hierarchy.core.exceptions.ModuleConfigurationException;
import se.unlogic.hierarchy.core.exceptions.URINotFoundException;
import se.unlogic.hierarchy.core.interfaces.ForegroundModuleResponse;
import se.unlogic.hierarchy.core.interfaces.SectionInterface;
import se.unlogic.hierarchy.core.interfaces.ViewFragment;
import se.unlogic.hierarchy.core.interfaces.attributes.AttributeHandler;
import se.unlogic.hierarchy.core.interfaces.events.EventListener;
import se.unlogic.hierarchy.core.interfaces.listeners.ServerStartupListener;
import se.unlogic.hierarchy.core.interfaces.modules.descriptors.ForegroundModuleDescriptor;
import se.unlogic.hierarchy.core.interfaces.modules.descriptors.ModuleDescriptor;
import se.unlogic.hierarchy.core.utils.AccessUtils;
import se.unlogic.hierarchy.core.utils.ViewFragmentUtils;
import se.unlogic.hierarchy.core.utils.crud.FragmentLinkScriptFilter;
import se.unlogic.hierarchy.core.utils.extensionlinks.ExtensionLink;
import se.unlogic.hierarchy.core.utils.extensionlinks.ExtensionLinkProvider;
import se.unlogic.hierarchy.core.utils.extensionlinks.ExtensionLinkUtils;
import se.unlogic.hierarchy.foregroundmodules.usersessionadmin.UserNameComparator;
import se.unlogic.openhierarchy.foregroundmodules.siteprofile.interfaces.SiteProfile;
import se.unlogic.standardutils.collections.CollectionUtils;
import se.unlogic.standardutils.dao.HighLevelQuery;
import se.unlogic.standardutils.dao.LowLevelQuery;
import se.unlogic.standardutils.dao.QueryParameterFactory;
import se.unlogic.standardutils.dao.RelationQuery;
import se.unlogic.standardutils.dao.TransactionHandler;
import se.unlogic.standardutils.dao.querys.ArrayListQuery;
import se.unlogic.standardutils.date.DateUtils;
import se.unlogic.standardutils.enums.Order;
import se.unlogic.standardutils.io.BinarySizeFormater;
import se.unlogic.standardutils.io.BinarySizes;
import se.unlogic.standardutils.json.JsonArray;
import se.unlogic.standardutils.json.JsonObject;
import se.unlogic.standardutils.json.JsonUtils;
import se.unlogic.standardutils.numbers.NumberUtils;
import se.unlogic.standardutils.object.ObjectUtils;
import se.unlogic.standardutils.populators.IntegerPopulator;
import se.unlogic.standardutils.string.StringUtils;
import se.unlogic.standardutils.time.TimeUtils;
import se.unlogic.standardutils.validation.PositiveStringIntegerValidator;
import se.unlogic.standardutils.validation.ValidationError;
import se.unlogic.standardutils.validation.ValidationException;
import se.unlogic.standardutils.xml.XMLGeneratorDocument;
import se.unlogic.standardutils.xml.XMLUtils;
import se.unlogic.webutils.http.HTTPUtils;
import se.unlogic.webutils.http.RequestUtils;
import se.unlogic.webutils.http.URIParser;

import com.nordicpeak.flowengine.accesscontrollers.ManagerFlowInstanceAccessController;
import com.nordicpeak.flowengine.beans.AutoManagerAssignmentRule;
import com.nordicpeak.flowengine.beans.AutoManagerAssignmentStatusRule;
import com.nordicpeak.flowengine.beans.ExternalMessage;
import com.nordicpeak.flowengine.beans.ExternalMessageAttachment;
import com.nordicpeak.flowengine.beans.Flow;
import com.nordicpeak.flowengine.beans.FlowFamily;
import com.nordicpeak.flowengine.beans.FlowInstance;
import com.nordicpeak.flowengine.beans.FlowInstanceAttribute;
import com.nordicpeak.flowengine.beans.FlowInstanceEvent;
import com.nordicpeak.flowengine.beans.InternalMessage;
import com.nordicpeak.flowengine.beans.InternalMessageAttachment;
import com.nordicpeak.flowengine.beans.QueryDescriptor;
import com.nordicpeak.flowengine.beans.RequestMetadata;
import com.nordicpeak.flowengine.beans.SimpleSigningRequest;
import com.nordicpeak.flowengine.beans.Status;
import com.nordicpeak.flowengine.beans.Step;
import com.nordicpeak.flowengine.beans.UserBookmark;
import com.nordicpeak.flowengine.comparators.GroupNameComparator;
import com.nordicpeak.flowengine.cruds.ExternalMessageCRUD;
import com.nordicpeak.flowengine.cruds.InternalMessageCRUD;
import com.nordicpeak.flowengine.enums.ContentType;
import com.nordicpeak.flowengine.enums.EventType;
import com.nordicpeak.flowengine.enums.Priority;
import com.nordicpeak.flowengine.enums.SenderType;
import com.nordicpeak.flowengine.enums.ShowMode;
import com.nordicpeak.flowengine.events.DeletedByManagerEvent;
import com.nordicpeak.flowengine.events.ExternalMessageAddedEvent;
import com.nordicpeak.flowengine.events.ManagerMentionedEvent;
import com.nordicpeak.flowengine.events.ManagersChangedEvent;
import com.nordicpeak.flowengine.events.StatusChangedByManagerEvent;
import com.nordicpeak.flowengine.events.SubmitEvent;
import com.nordicpeak.flowengine.exceptions.FlowEngineException;
import com.nordicpeak.flowengine.exceptions.evaluation.EvaluationException;
import com.nordicpeak.flowengine.exceptions.evaluationprovider.EvaluationProviderException;
import com.nordicpeak.flowengine.exceptions.flow.FlowDefaultStatusNotFound;
import com.nordicpeak.flowengine.exceptions.flow.FlowDisabledException;
import com.nordicpeak.flowengine.exceptions.flow.FlowNoLongerAvailableException;
import com.nordicpeak.flowengine.exceptions.flow.FlowNotPublishedException;
import com.nordicpeak.flowengine.exceptions.flowinstance.FlowInstanceNoLongerAvailableException;
import com.nordicpeak.flowengine.exceptions.flowinstance.InvalidFlowInstanceStepException;
import com.nordicpeak.flowengine.exceptions.flowinstance.MissingQueryInstanceDescriptor;
import com.nordicpeak.flowengine.exceptions.flowinstancemanager.DuplicateFlowInstanceManagerIDException;
import com.nordicpeak.flowengine.exceptions.flowinstancemanager.FlowInstanceManagerClosedException;
import com.nordicpeak.flowengine.exceptions.queryinstance.QueryInstanceHTMLException;
import com.nordicpeak.flowengine.exceptions.queryinstance.QueryRequestException;
import com.nordicpeak.flowengine.exceptions.queryinstance.SubmitCheckException;
import com.nordicpeak.flowengine.exceptions.queryinstance.UnableToGetQueryInstanceShowHTMLException;
import com.nordicpeak.flowengine.exceptions.queryinstance.UnableToResetQueryInstanceException;
import com.nordicpeak.flowengine.exceptions.queryprovider.QueryProviderException;
import com.nordicpeak.flowengine.interfaces.FlowInstanceAccessController;
import com.nordicpeak.flowengine.interfaces.FlowInstanceOverviewExtensionProvider;
import com.nordicpeak.flowengine.interfaces.FlowProcessCallback;
import com.nordicpeak.flowengine.interfaces.GenericSigningProvider;
import com.nordicpeak.flowengine.interfaces.GenericSigningRequest;
import com.nordicpeak.flowengine.interfaces.Icon;
import com.nordicpeak.flowengine.interfaces.ImmutableFlowFamily;
import com.nordicpeak.flowengine.interfaces.ImmutableFlowInstance;
import com.nordicpeak.flowengine.interfaces.ImmutableFlowInstanceEvent;
import com.nordicpeak.flowengine.interfaces.MessageCRUDCallback;
import com.nordicpeak.flowengine.interfaces.PDFProvider;
import com.nordicpeak.flowengine.interfaces.SigningResponse;
import com.nordicpeak.flowengine.interfaces.XMLProvider;
import com.nordicpeak.flowengine.internalnotifications.beans.NotificationMetadata;
import com.nordicpeak.flowengine.internalnotifications.interfaces.Notification;
import com.nordicpeak.flowengine.internalnotifications.interfaces.NotificationHandler;
import com.nordicpeak.flowengine.internalnotifications.interfaces.NotificationSource;
import com.nordicpeak.flowengine.listeners.FlowInstanceExternalMessageElementableListener;
import com.nordicpeak.flowengine.listeners.FlowStatusManagerAccessElementableListener;
import com.nordicpeak.flowengine.managers.FlowInstanceManager;
import com.nordicpeak.flowengine.managers.MutableFlowInstanceManager;
import com.nordicpeak.flowengine.managers.UserGroupListFlowManagersConnector;
import com.nordicpeak.flowengine.search.FlowInstanceIndexer;
import com.nordicpeak.flowengine.utils.ExternalMessageUtils;
import com.nordicpeak.flowengine.utils.FlowFamilyUtils;
import com.nordicpeak.flowengine.utils.FlowIconUtils;
import com.nordicpeak.flowengine.utils.FlowInstanceUtils;
import com.nordicpeak.flowengine.utils.MentionedUserTagUtils;

public class FlowInstanceAdminModule extends BaseFlowBrowserModule implements FlowProcessCallback, ServerStartupListener, EventListener<CRUDEvent<?>>, MessageCRUDCallback, Runnable, NotificationSource {

	protected static final Field[] FLOW_INSTANCE_OVERVIEW_RELATIONS = { FlowInstance.OWNERS_RELATION, FlowInstance.INTERNAL_MESSAGES_RELATION, InternalMessage.ATTACHMENTS_RELATION, FlowInstance.EXTERNAL_MESSAGES_RELATION, ExternalMessage.ATTACHMENTS_RELATION, FlowInstance.FLOW_RELATION, Flow.OVERVIEW_ATTRIBUTES_RELATION, Flow.FLOW_FAMILY_RELATION, FlowFamily.MANAGER_GROUPS_RELATION, FlowFamily.MANAGER_USERS_RELATION, FlowFamily.EXTERNAL_MESSAGE_TEMPLATES_RELATION, FlowInstance.STATUS_RELATION, FlowInstance.EVENTS_RELATION, FlowInstance.ATTRIBUTES_RELATION, FlowInstanceEvent.ATTRIBUTES_RELATION, FlowInstance.MANAGERS_RELATION, FlowInstance.MANAGER_GROUPS_RELATION };

	protected static final Field[] UPDATE_STATUS_RELATIONS = { FlowInstance.ATTRIBUTES_RELATION, FlowInstance.FLOW_RELATION, Flow.FLOW_FAMILY_RELATION, FlowFamily.MANAGER_GROUPS_RELATION, FlowFamily.MANAGER_USERS_RELATION, FlowFamily.EXTERNAL_MESSAGE_TEMPLATES_RELATION, FlowInstance.STATUS_RELATION, Flow.STATUSES_RELATION, FlowInstance.OWNERS_RELATION, Status.MANAGER_USERS_RELATION, Status.MANAGER_GROUPS_RELATION, FlowInstance.MANAGERS_RELATION, FlowInstance.MANAGER_GROUPS_RELATION };
	protected static final Field[] UPDATE_MANAGER_RELATIONS = { FlowInstance.ATTRIBUTES_RELATION, FlowInstance.FLOW_RELATION, Flow.FLOW_FAMILY_RELATION, FlowFamily.MANAGER_GROUPS_RELATION, FlowFamily.MANAGER_USERS_RELATION, FlowInstance.STATUS_RELATION, FlowInstance.OWNERS_RELATION, FlowInstance.MANAGERS_RELATION, FlowInstance.MANAGER_GROUPS_RELATION };

	@SuppressWarnings("rawtypes")
	private static final Class[] EVENT_LISTENER_CLASSES = new Class[] { FlowFamily.class, Flow.class, FlowInstance.class, InternalMessage.class, ExternalMessage.class };

	//@formatter:off
	
	protected static final String FLOW_MANAGER_SQL = "SELECT flowID FROM flowengine_flows WHERE enabled = true AND flowFamilyID IN (SELECT ff.flowFamilyID FROM flowengine_flow_families ff LEFT JOIN flowengine_flow_family_manager_users ffu on ff.flowFamilyID = ffu.flowFamilyID LEFT JOIN flowengine_flow_family_manager_groups ffg on ff.flowFamilyID = ffg.flowFamilyID WHERE ffu.userID = ? AND (ffu.validFromDate IS NULL OR ffu.validFromDate <= ?)";
	protected static final String FLOW_INSTANCE_BOOKMARKS_SQL = "SELECT ffi.* FROM flowengine_flow_instances ffi"
	                                                          + " INNER JOIN flowengine_flow_instance_bookmarks ffib ON ffi.flowInstanceID = ffib.flowInstanceID"
	                                                          + " WHERE ffib.userID = ? AND ffi.flowID IN (";
	protected static final String ACTIVE_FLOWS = "SELECT ffi.* FROM flowengine_flow_instances ffi"
	                                           + " INNER JOIN flowengine_flow_statuses ffs ON ffi.statusID = ffs.statusID"
	                                           + " WHERE ffi.firstSubmitted IS NOT NULL AND ffi.flowID IN ($flowIDs) AND ffs.contentType NOT IN ('" + ContentType.NEW + "', '" + ContentType.ARCHIVED + "')"
	                                           + " ORDER BY lastStatusChange DESC";
	
	//@formatter:on

	public static final ManagerFlowInstanceAccessController UPDATE_ACCESS_CONTROLLER = new ManagerFlowInstanceAccessController(true, false, false, false);
	public static final ManagerFlowInstanceAccessController UPDATE_MANAGERS_ACCESS_CONTROLLER = new ManagerFlowInstanceAccessController(false, false, false, true);
	public static final ManagerFlowInstanceAccessController DELETE_ACCESS_CONTROLLER = new ManagerFlowInstanceAccessController(false, true, false, false);
	public static final ManagerFlowInstanceAccessController GENERAL_ACCESS_CONTROLLER = new ManagerFlowInstanceAccessController(false, false, false, false);
	public static final ManagerFlowInstanceAccessController GENERAL_FULL_ACCESS_CONTROLLER = new ManagerFlowInstanceAccessController(false, false, true, false);

	public static final ValidationError STATUS_NOT_FOUND_VALIDATION_ERROR = new ValidationError("StatusNotFoundValidationError");
	public static final ValidationError INVALID_STATUS_VALIDATION_ERROR = new ValidationError("InvalidStatusValidationError");
	public static final ValidationError FLOW_INSTANCE_PREVIEW_VALIDATION_ERROR = new ValidationError("FlowInstancePreviewError");
	public static final ValidationError FLOW_INSTANCE_MANAGER_CLOSED_VALIDATION_ERROR = new ValidationError("FlowInstanceManagerClosedError");

	public static final ValidationError ONE_OR_MORE_SELECTED_MANAGER_USERS_NOT_FOUND_VALIDATION_ERROR = new ValidationError("OneOrMoreSelectedManagerUsersNotFoundError");
	public static final ValidationError ONE_OR_MORE_SELECTED_MANAGER_GROUPS_NOT_FOUND_VALIDATION_ERROR = new ValidationError("OneOrMoreSelectedManagerGroupsNotFoundError");

	protected static final RequestMetadata MANAGER_REQUEST_METADATA = new RequestMetadata(true);

	@XSLVariable(prefix = "java.")
	protected String noManagersSelected = "No managers selected";

	@XSLVariable(prefix = "java.")
	private String notificationNewManager = "Assigned as manager";

	@XSLVariable(prefix = "java.")
	private String notificationCompletion = "Completion";

	@XSLVariable(prefix = "java.")
	private String notificationExternalMessage = "Message";

	@XSLVariable(prefix = "java.")
	private String mentionedInFlowInstance = "Mentioned in message";

	@XSLVariable(prefix = "java.")
	private String managerSignedDetailsText = "Signed by manager";

	@ModuleSetting
	@TextFieldSettingDescriptor(name = "High priority lapsed managing time", description = "The precent of the managing time of the current status that has to have elapsed for an instance to be classified as high priority", required = true, formatValidator = PositiveStringIntegerValidator.class)
	protected int highPriorityThreshold = 90;

	@ModuleSetting
	@TextFieldSettingDescriptor(name = "Medium priority lapsed managing time", description = "The precent of the managing time of the current status that has to have elapsed for an instance to be classified as medium priority", required = true, formatValidator = PositiveStringIntegerValidator.class)
	protected int mediumPriorityThreshold = 60;

	@ModuleSetting
	@TextFieldSettingDescriptor(name = "Max search hits", description = "Maximum number of hits to get from index when searching", formatValidator = PositiveStringIntegerValidator.class, required = true)
	protected int maxHitCount = 20;

	@ModuleSetting
	@CheckboxSettingDescriptor(name = "Enable site profile support", description = "Controls if site profile support is enabled")
	protected boolean enableSiteProfileSupport;

	@ModuleSetting
	@CheckboxSettingDescriptor(name = "Enable external ID support", description = "Controls if external ID is displayed")
	protected boolean enableExternalID;

	@ModuleSetting
	@CheckboxSettingDescriptor(name = "Enable the description column", description = "Controls if description column is visible")
	protected boolean enableDescriptionColumn = true;

	@ModuleSetting
	@CheckboxSettingDescriptor(name = "Enable logging of flow instance indexing", description = "Enables logging of indexing of flow instances")
	protected boolean logFlowInstanceIndexing;

	@ModuleSetting(allowsNull = true)
	@TextFieldSettingDescriptor(name = "CKEditor connector module alias", description = "The full alias of the CKEditor connector module (relative from the contextpath). Leave empty if you do not want to activate file manager for CKEditor")
	protected String ckConnectorModuleAlias;

	@ModuleSetting
	@TextFieldSettingDescriptor(name = "Max file size", description = "Maxmium allowed file size in megabytes", required = true, formatValidator = PositiveStringIntegerValidator.class)
	protected Integer maxFileSize = 50;

	@ModuleSetting
	@EnumDropDownSettingDescriptor(name = "Flow instance event sort order", description = "The order of flow instance events when displayed in this module", required = true)
	protected Order flowInstanceEventSortOrder = Order.ASC;

	@InstanceManagerDependency
	protected PDFProvider pdfProvider;

	@InstanceManagerDependency
	protected XMLProvider xmlProvider;

	@InstanceManagerDependency
	protected GenericSigningProvider genericSigningProvider;
	
	protected FlowAdminModule flowAdminModule;

	protected NotificationHandler notificationHandler;

	protected CopyOnWriteArrayList<ExtensionLinkProvider> listOverviewExtensionLinkProviders = new CopyOnWriteArrayList<ExtensionLinkProvider>();
	protected CopyOnWriteArrayList<FlowInstanceOverviewExtensionProvider> flowInstanceOverviewTabExtensionProviders = new CopyOnWriteArrayList<FlowInstanceOverviewExtensionProvider>();

	protected ExternalMessageCRUD externalMessageCRUD;
	protected InternalMessageCRUD internalMessageCRUD;

	protected FlowInstanceIndexer flowInstanceIndexer;

	private QueryParameterFactory<Status, Integer> statusIDParamFactory;
	private QueryParameterFactory<Status, Flow> statusFlowParamFactory;
	private QueryParameterFactory<UserBookmark, FlowInstance> bookmarkFlowInstanceParamFactory;
	private QueryParameterFactory<UserBookmark, User> bookmarkUserParamFactory;
	private QueryParameterFactory<FlowInstanceAttribute, String> attributeNameParamFactory;

	protected List<String> selectedAttributes;
	
	private UserGroupListFlowManagersConnector userGroupListFlowManagersConnector;

	@Override
	public void init(ForegroundModuleDescriptor moduleDescriptor, SectionInterface sectionInterface, DataSource dataSource) throws Exception {

		super.init(moduleDescriptor, sectionInterface, dataSource);

		eventHandler.addEventListener(CRUDEvent.class, this, EVENT_LISTENER_CLASSES);
		
		registerInstance(moduleDescriptor, sectionInterface, dataSource);
	}
	
	protected void registerInstance(ForegroundModuleDescriptor moduleDescriptor, SectionInterface sectionInterface, DataSource dataSource) throws Exception {

		if (!systemInterface.getInstanceHandler().addInstance(FlowInstanceAdminModule.class, this)) {
			
			throw new RuntimeException("Unable to register module in global instance handler using key " + FlowInstanceAdminModule.class.getSimpleName() + ", another instance is already registered using this key.");
		}
	}

	@Override
	public void unload() throws Exception {

		systemInterface.getInstanceHandler().removeInstance(FlowInstanceAdminModule.class, this);

		eventHandler.removeEventListener(CRUDEvent.class, this, EVENT_LISTENER_CLASSES);

		if (notificationHandler != null) {

			notificationHandler.removeNotificationSource(this);
		}

		flowInstanceIndexer.close();

		listOverviewExtensionLinkProviders.clear();
		flowInstanceOverviewTabExtensionProviders.clear();

		super.unload();
	}

	@Override
	protected void createDAOs(DataSource dataSource) throws Exception {

		super.createDAOs(dataSource);

		externalMessageCRUD = new ExternalMessageCRUD(daoFactory.getExternalMessageDAO(), daoFactory.getExternalMessageAttachmentDAO(), this, true);
		internalMessageCRUD = new InternalMessageCRUD(daoFactory.getInternalMessageDAO(), daoFactory.getInternalMessageAttachmentDAO(), this, true);

		statusIDParamFactory = daoFactory.getStatusDAO().getParamFactory("statusID", Integer.class);
		statusFlowParamFactory = daoFactory.getStatusDAO().getParamFactory("flow", Flow.class);
		bookmarkFlowInstanceParamFactory = daoFactory.getUserBookmarkDAO().getParamFactory("flowInstance", FlowInstance.class);
		bookmarkUserParamFactory = daoFactory.getUserBookmarkDAO().getParamFactory("user", User.class);
		attributeNameParamFactory = daoFactory.getFlowInstanceAttributeDAO().getParamFactory("name", String.class);
	}

	@Override
	protected void moduleConfigured() throws Exception {

		FlowInstanceIndexer oldIndexer = flowInstanceIndexer;

		this.flowInstanceIndexer = createIndexer();
		this.flowInstanceIndexer.setLogIndexing(this.logFlowInstanceIndexing);

		systemInterface.addServerStartupListener(this);

		if (oldIndexer != null) {

			oldIndexer.close();
		}

		this.selectedAttributes = getSelectedAttributes();
	}

	protected List<String> getSelectedAttributes() {

		if (enableExternalID) {

			ArrayList<String> attributes = new ArrayList<String>(1);

			if (enableExternalID) {

				attributes.add(Constants.FLOW_INSTANCE_EXTERNAL_ID_ATTRIBUTE);
			}

			return attributes;
		}

		return null;
	}
	
	@InstanceManagerDependency
	public void setFlowAdminModule(FlowAdminModule flowAdminModule) {
		
		this.flowAdminModule = flowAdminModule;
		
		if (flowAdminModule != null) {
			
			userGroupListFlowManagersConnector = new UserGroupListFlowManagersConnector(systemInterface, flowAdminModule);
			
		} else {
			
			userGroupListFlowManagersConnector = null;
		}
		
	}

	protected FlowInstanceIndexer createIndexer() throws Exception {

		return new FlowInstanceIndexer(daoFactory, maxHitCount, systemInterface);
	}

	@Override
	public ForegroundModuleResponse defaultMethod(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Exception {

		return this.list(req, res, user, uriParser, (List<ValidationError>) null);
	}

	public ForegroundModuleResponse list(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser, ValidationError validationError) throws SQLException {

		return list(req, res, user, uriParser, CollectionUtils.getGenericSingletonList(validationError));
	}

	@Override
	public ForegroundModuleResponse list(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser, List<ValidationError> validationErrors) throws SQLException {

		log.info("User " + user + " listing flow instances");

		SiteProfile profile = null;

		if (profileHandler != null) {

			profile = profileHandler.getCurrentProfile(user, req, uriParser);
		}

		List<Integer> flowIDs = getUserFlowIDs(user, profile);

		List<FlowInstance> bookmarkedFlows = null;
		List<FlowInstance> activeFlowInstances = null;

		if (flowIDs != null) {

			bookmarkedFlows = getFlowInstanceBookmarks(user, profile, flowIDs);
			activeFlowInstances = getActiveFlowInstances(user, profile, flowIDs);

			filterRestrictedFlowInstances(bookmarkedFlows, user);
			filterRestrictedFlowInstances(activeFlowInstances, user);
		}

		if (CollectionUtils.isEmpty(activeFlowInstances) && CollectionUtils.isEmpty(bookmarkedFlows)) {

			Document doc = createDocument(req, uriParser, user);

			Element overviewElement = doc.createElement("OverviewElement");
			doc.getDocumentElement().appendChild(overviewElement);

			XMLUtils.append(doc, overviewElement, validationErrors);

			ExtensionLinkUtils.appendExtensionLinks(this.listOverviewExtensionLinkProviders, user, req, doc, overviewElement);

			return new SimpleForegroundModuleResponse(doc, this.getDefaultBreadcrumb());
		}

		Document doc = createDocument(req, uriParser, user);

		Element overviewElement = doc.createElement("OverviewElement");
		doc.getDocumentElement().appendChild(overviewElement);

		XMLUtils.append(doc, overviewElement, validationErrors);
		XMLUtils.append(doc, overviewElement, "BookmarkedInstances", bookmarkedFlows);

		if (activeFlowInstances != null) {

			Element prioritizedInstances = doc.createElement("PrioritizedInstances");
			overviewElement.appendChild(prioritizedInstances);

			Element userAssignedInstances = doc.createElement("UserAssignedInstances");
			overviewElement.appendChild(userAssignedInstances);

			Element activeInstances = doc.createElement("ActiveInstances");
			overviewElement.appendChild(activeInstances);

			Element unassignedInstances = doc.createElement("UnassignedInstances");
			overviewElement.appendChild(unassignedInstances);

			for (FlowInstance instance : activeFlowInstances) {

				if (instance.getManagers() == null) {

					unassignedInstances.appendChild(instance.toXML(doc));

				} else if (instance.getManagers().contains(user)) {

					userAssignedInstances.appendChild(instance.toXML(doc));

				} else {

					activeInstances.appendChild(instance.toXML(doc));
				}

				if (instance.getStatus().getManagingTime() != null) {

					int daysLapsed = DateUtils.getWorkingDays(instance.getLastStatusChange(), new Date());

					float percent = (daysLapsed / instance.getStatus().getManagingTime()) * 100;

					if (percent >= highPriorityThreshold) {

						instance.setPriority(Priority.HIGH);
						prioritizedInstances.appendChild(instance.toXML(doc));

					} else if (percent >= mediumPriorityThreshold) {

						instance.setPriority(Priority.MEDIUM);
						prioritizedInstances.appendChild(instance.toXML(doc));

					}

				}
			}
		}

		if (enableSiteProfileSupport && this.profileHandler != null) {

			XMLUtils.append(doc, overviewElement, "SiteProfiles", this.profileHandler.getProfiles());
		}

		if (enableDescriptionColumn) {

			XMLUtils.appendNewElement(doc, overviewElement, "ShowDescriptionColumn");
		}

		if (enableExternalID) {

			XMLUtils.appendNewElement(doc, overviewElement, "ShowExternalID");
		}

		ExtensionLinkUtils.appendExtensionLinks(this.listOverviewExtensionLinkProviders, user, req, doc, overviewElement);

		return new SimpleForegroundModuleResponse(doc, moduleDescriptor.getName(), this.getDefaultBreadcrumb());
	}

	@WebPublic(alias = "overview")
	public ForegroundModuleResponse showFlowInstanceOverview(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws ModuleConfigurationException, SQLException, AccessDeniedException, IOException, URINotFoundException {

		FlowInstance flowInstance;

		if (uriParser.size() == 3 && NumberUtils.isInt(uriParser.get(2)) && (flowInstance = getFlowInstance(Integer.valueOf(uriParser.get(2)), CollectionUtils.getList(ExternalMessageAttachment.DATA_FIELD, InternalMessageAttachment.DATA_FIELD), getFlowInstanceOverviewRelations())) != null && !flowInstance.getStatus().getContentType().equals(ContentType.NEW)) {

			getGeneralAccessController().checkFlowInstanceAccess(flowInstance, user);

			if (!flowInstance.getFlow().isEnabled() || isOperatingStatusDisabled(flowInstance, true)) {

				return list(req, res, user, uriParser, FLOW_DISABLED_VALIDATION_ERROR);
			}

			log.info("User " + user + " viewing overview of flow instance " + flowInstance);

			Document doc = this.createDocument(req, uriParser, user);

			Element showFlowInstanceOverviewElement = doc.createElement("ShowFlowInstanceOverview");
			doc.getDocumentElement().appendChild(showFlowInstanceOverviewElement);

			XMLUtils.appendNewElement(doc, showFlowInstanceOverviewElement, "FormattedMaxFileSize", BinarySizeFormater.getFormatedSize(maxFileSize * BinarySizes.MegaByte));

			if (req.getMethod().equalsIgnoreCase("POST")) {

				if (req.getParameter("externalmessage") != null && flowInstance.isNewExternalMessagesAllowed()) {

					ExternalMessage externalMessage = externalMessageCRUD.add(req, res, uriParser, user, doc, showFlowInstanceOverviewElement, flowInstance, true);

					if (externalMessage != null) {

						FlowInstanceEvent flowInstanceEvent = flowInstanceEventGenerator.addFlowInstanceEvent(flowInstance, EventType.MANAGER_MESSAGE_SENT, null, user, null, ExternalMessageUtils.getFlowInstanceEventAttributes(externalMessage));

						systemInterface.getEventHandler().sendEvent(FlowInstance.class, new ExternalMessageAddedEvent(flowInstance, flowInstanceEvent, getSiteProfile(flowInstance), externalMessage, SenderType.MANAGER), EventTarget.ALL);

						systemInterface.getEventHandler().sendEvent(ExternalMessage.class, new CRUDEvent<ExternalMessage>(CRUDAction.ADD, externalMessage), EventTarget.ALL);

						res.sendRedirect(req.getContextPath() + uriParser.getFormattedURI() + "#messages");

						return null;
					}

				} else if (req.getParameter("internalmessage") != null) {

					//TODO append message or request parameters
					InternalMessage internalMessage = internalMessageCRUD.add(req, res, uriParser, user, doc, showFlowInstanceOverviewElement, flowInstance);

					if (internalMessage != null) {

						Set<Integer> mentionedUserIDs = MentionedUserTagUtils.getMentionedUserIDs(internalMessage.getMessage());

						if (mentionedUserIDs != null) {

							Collection<User> availableManagers = getAllowedManagers(flowInstance);

							if (availableManagers != null) {

								List<User> mentionedUsers = new ArrayList<User>(mentionedUserIDs.size());

								for (Integer userID : mentionedUserIDs) {

									User mentionedUser = systemInterface.getUserHandler().getUser(userID, false, false);

									if (mentionedUser != null && availableManagers.contains(mentionedUser)) {

										try {

											notificationHandler.addNotification(flowInstance.getFlowInstanceID(), mentionedUser.getUserID(), moduleDescriptor.getModuleID(), "mention", user.getUserID(), mentionedInFlowInstance, null);

										} catch (SQLException e) {

											log.error("Error sending notification to mentioned user " + user + " of " + flowInstance, e);
										}

										mentionedUsers.add(mentionedUser);
									}
								}

								eventHandler.sendEvent(FlowInstance.class, new ManagerMentionedEvent(flowInstance, mentionedUsers, user), EventTarget.ALL);
							}
						}

						res.sendRedirect(req.getContextPath() + uriParser.getFormattedURI() + "#notes");

						systemInterface.getEventHandler().sendEvent(InternalMessage.class, new CRUDEvent<InternalMessage>(CRUDAction.ADD, internalMessage), EventTarget.ALL);

						return null;
					}
				}
			}

			if (flowInstanceEventSortOrder == Order.DESC && flowInstance.getEvents() != null) {

				Collections.reverse(flowInstance.getEvents());
			}

			appendFlowInstanceOverviewElement(doc, showFlowInstanceOverviewElement, flowInstance, req, res, user, uriParser);

			if (user != null) {
				showFlowInstanceOverviewElement.appendChild(user.toXML(doc));
			}

			if (enableSiteProfileSupport && flowInstance.getProfileID() != null && this.profileHandler != null) {

				XMLUtils.append(doc, showFlowInstanceOverviewElement, profileHandler.getProfile(flowInstance.getProfileID()));
			}

			if (enableDescriptionColumn) {

				XMLUtils.appendNewElement(doc, showFlowInstanceOverviewElement, "ShowDescriptionColumn");
			}

			if (enableExternalID) {

				XMLUtils.appendNewElement(doc, showFlowInstanceOverviewElement, "ShowExternalID");
			}

			if (!FlowInstanceUtils.isOwnersContactable(flowInstance)) {

				XMLUtils.appendNewElement(doc, showFlowInstanceOverviewElement, "NoContactWay");
			}
			
			try {

				getUpdateManagersAccessController().checkFlowInstanceAccess(flowInstance, user);

			} catch (AccessDeniedException e) {
				
				XMLUtils.appendNewElement(doc, showFlowInstanceOverviewElement, "HideUpdateManagers");
			}

			appendBookmark(doc, showFlowInstanceOverviewElement, flowInstance, req, user);

			List<ViewFragment> viewFragments = appendOverviewData(doc, showFlowInstanceOverviewElement, flowInstance, req, user, uriParser);

			SimpleForegroundModuleResponse moduleResponse = new SimpleForegroundModuleResponse(doc, flowInstance.getFlow().getName(), this.getDefaultBreadcrumb());

			if (!CollectionUtils.isEmpty(viewFragments)) {
				for (ViewFragment viewFragment : viewFragments) {

					ViewFragmentUtils.appendLinksAndScripts(moduleResponse, viewFragment);
				}
			}

			return moduleResponse;
		}

		return list(req, res, user, uriParser, FLOW_INSTANCE_NOT_FOUND_VALIDATION_ERROR);
	}

	@WebPublic(alias = "messages")
	public ForegroundModuleResponse showFlowInstanceMessages(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws ModuleConfigurationException, SQLException, AccessDeniedException, IOException, URINotFoundException {

		if (uriParser.size() == 3 && NumberUtils.isInt(uriParser.get(2))) {

			redirectToMethod(req, res, "/overview/" + uriParser.get(2) + "#messages");

			return null;
		}

		throw new URINotFoundException(uriParser);
	}

	protected Element appendFlowInstanceOverviewElement(Document doc, Element showFlowInstanceOverviewElement, FlowInstance flowInstance, HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) {

		XMLGeneratorDocument genDoc = new XMLGeneratorDocument(doc);
		genDoc.addElementableListener(FlowInstance.class, new FlowInstanceExternalMessageElementableListener());
		
		Element showFlowInstanceElement = flowInstance.toXML(genDoc);
		showFlowInstanceOverviewElement.appendChild(showFlowInstanceElement);

		return showFlowInstanceElement;
	}

	protected Field[] getFlowInstanceOverviewRelations() {

		return FLOW_INSTANCE_OVERVIEW_RELATIONS;
	}

	protected List<ViewFragment> appendOverviewData(Document doc, Element showFlowInstanceOverviewElement, FlowInstance flowInstance, HttpServletRequest req, User user, URIParser uriParser) throws SQLException {

		Element tabHeadersElement = XMLUtils.appendNewElement(doc, showFlowInstanceOverviewElement, "TabHeaders");
		Element tabContentsElement = XMLUtils.appendNewElement(doc, showFlowInstanceOverviewElement, "TabContents");

		List<ViewFragment> viewFragments = new ArrayList<ViewFragment>();

		for (FlowInstanceOverviewExtensionProvider tabExtensionProvider : flowInstanceOverviewTabExtensionProviders) {

			try {
				ExtensionLink headerExtensionLink = tabExtensionProvider.getOverviewTabHeaderExtensionLink(flowInstance, req, uriParser, user);

				if (headerExtensionLink != null) {

					ViewFragment contentsFragment = tabExtensionProvider.getOverviewTabContentsViewFragment(flowInstance, req, uriParser, user);

					if (contentsFragment != null) {

						tabHeadersElement.appendChild(headerExtensionLink.toXML(doc));

						tabContentsElement.appendChild(contentsFragment.toXML(doc));
						viewFragments.add(contentsFragment);
					}
				}

			} catch (Exception e) {
				log.error("Error appending tab from FlowInstanceOverviewExtensionProvider " + tabExtensionProvider, e);
			}
		}

		return viewFragments;
	}

	@WebPublic(alias = "status")
	public ForegroundModuleResponse updateStatus(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws ModuleConfigurationException, SQLException, AccessDeniedException, IOException {

		FlowInstance flowInstance;

		if (uriParser.size() == 3 && NumberUtils.isInt(uriParser.get(2)) && (flowInstance = getFlowInstance(Integer.valueOf(uriParser.get(2)), null, getUpdateStatusRelations())) != null && !flowInstance.getStatus().getContentType().equals(ContentType.NEW)) {

			getGeneralAccessController().checkFlowInstanceAccess(flowInstance, user);

			if (!flowInstance.getFlow().isEnabled() || isOperatingStatusDisabled(flowInstance, true)) {

				return list(req, res, user, uriParser, FLOW_DISABLED_VALIDATION_ERROR);
			}

			log.info("User " + user + " requesting update status form for instance " + flowInstance);

			Document doc = this.createDocument(req, uriParser, user);

			Element updateInstanceStatusElement = doc.createElement("UpdateInstanceStatus");

			doc.getDocumentElement().appendChild(updateInstanceStatusElement);

			if (req.getMethod().equalsIgnoreCase("POST")) {

				Integer statusID = NumberUtils.toInt(req.getParameter("statusID"));

				Status status;

				if (statusID == null || (status = getStatus(flowInstance.getFlow(), statusID)) == null) {

					updateInstanceStatusElement.appendChild(STATUS_NOT_FOUND_VALIDATION_ERROR.toXML(doc));

				} else if (status.getContentType() == ContentType.NEW || status.getContentType() == ContentType.WAITING_FOR_MULTISIGN || status.getContentType() == ContentType.WAITING_FOR_PAYMENT || (status.isUseAccessCheck() && !AccessUtils.checkAccess(user, status))) {

					updateInstanceStatusElement.appendChild(INVALID_STATUS_VALIDATION_ERROR.toXML(doc));

				} else {

					Status previousStatus = flowInstance.getStatus();

					if (!previousStatus.equals(status)) {

						if (status.isRequireSigning()) {

							redirectToMethod(req, res, "/signstatus/" + flowInstance.getFlowInstanceID() + "/" + status.getStatusID());
							return null;
						}

						log.info("User " + user + " changing status of instance " + flowInstance + " from " + previousStatus + " to " + status);

						flowInstance.setStatus(status);
						flowInstance.setLastStatusChange(TimeUtils.getCurrentTimestamp());
						this.daoFactory.getFlowInstanceDAO().update(flowInstance);

						FlowInstanceEvent flowInstanceEvent = flowInstanceEventGenerator.addFlowInstanceEvent(flowInstance, EventType.STATUS_UPDATED, null, user);

						eventHandler.sendEvent(FlowInstance.class, new CRUDEvent<FlowInstance>(CRUDAction.UPDATE, flowInstance), EventTarget.ALL);
						eventHandler.sendEvent(FlowInstance.class, new StatusChangedByManagerEvent(flowInstance, flowInstanceEvent, getSiteProfile(flowInstance), previousStatus, user), EventTarget.ALL);
					}

					redirectToMethod(req, res, "/overview/" + flowInstance.getFlowInstanceID());

					return null;
				}
			}

			try {

				getUpdateManagersAccessController().checkFlowInstanceAccess(flowInstance, user);

			} catch (AccessDeniedException e) {
				
				XMLUtils.appendNewElement(doc, updateInstanceStatusElement, "HideUpdateManagers");
			}

			XMLGeneratorDocument genDoc = new XMLGeneratorDocument(doc);
			genDoc.addElementableListener(Status.class, new FlowStatusManagerAccessElementableListener(user));

			updateInstanceStatusElement.appendChild(flowInstance.toXML(genDoc));

			XMLUtils.append(doc, updateInstanceStatusElement, user);

			appendBookmark(doc, updateInstanceStatusElement, flowInstance, req, user);

			return new SimpleForegroundModuleResponse(doc, flowInstance.getFlow().getName(), this.getDefaultBreadcrumb());
		}

		return list(req, res, user, uriParser, FLOW_INSTANCE_NOT_FOUND_VALIDATION_ERROR);
	}

	@WebPublic(alias = "signstatus")
	public ForegroundModuleResponse updateStatusSigning(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Exception {

		FlowInstance flowInstance;

		if (uriParser.size() == 4 && NumberUtils.isInt(uriParser.get(2)) && (flowInstance = getFlowInstance(Integer.valueOf(uriParser.get(2)), null, getUpdateStatusRelations())) != null && !flowInstance.getStatus().getContentType().equals(ContentType.NEW)) {

			Status status;

			if (NumberUtils.isInt(uriParser.get(3)) && (status = getStatus(flowInstance.getFlow(), Integer.valueOf(uriParser.get(3)))) != null && status.isRequireSigning()) {

				final Status previousStatus = flowInstance.getStatus();

				getGeneralAccessController().checkFlowInstanceAccess(flowInstance, user);

				if (!flowInstance.getFlow().isEnabled() || isOperatingStatusDisabled(flowInstance, true)) {

					return list(req, res, user, uriParser, FLOW_DISABLED_VALIDATION_ERROR);
				}

				if (status.getContentType() == ContentType.NEW || status.getContentType() == ContentType.WAITING_FOR_MULTISIGN || status.getContentType() == ContentType.WAITING_FOR_PAYMENT || (status.isUseAccessCheck() && !AccessUtils.checkAccess(user, status))) {

					redirectToMethod(req, res, "/status/" + flowInstance.getFlowInstanceID());
					return null;
				}

				log.info("User " + user + " preparing singing for changing status of instance " + flowInstance + " from " + previousStatus + " to " + status);

				return showUpdateStatusSignForm(req, res, user, uriParser, flowInstance, status, null);

			} else {

				redirectToMethod(req, res, "/status/" + flowInstance.getFlowInstanceID());
				return null;
			}
		}

		return list(req, res, user, uriParser, FLOW_INSTANCE_NOT_FOUND_VALIDATION_ERROR);
	}

	private ForegroundModuleResponse showUpdateStatusSignForm(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser, FlowInstance flowInstance, Status status, List<ValidationError> validationErrors) throws Exception {

		if (genericSigningProvider == null) {
			throw new ModuleConfigurationException("genericSigningProvider is null");
		}

		Document doc = createDocument(req, uriParser, user);

		Element signingFormElement = XMLUtils.appendNewElement(doc, doc.getDocumentElement(), "UpdateStatusSigning");

		ViewFragment viewFragment = genericSigningProvider.showSignForm(req, res, user, getUpdateStatusSigningRequest(flowInstance, status, uriParser), validationErrors);

		signingFormElement.appendChild(flowInstance.toXML(doc));
		signingFormElement.appendChild(status.toXML(doc));
		signingFormElement.appendChild(viewFragment.toXML(doc));
		FragmentLinkScriptFilter.addViewFragment(viewFragment, req);

		SimpleForegroundModuleResponse response = new SimpleForegroundModuleResponse(doc, moduleDescriptor.getName(), getDefaultBreadcrumb());
		ViewFragmentUtils.appendLinksAndScripts(response, viewFragment);

		return response;
	}

	private GenericSigningRequest getUpdateStatusSigningRequest(FlowInstance flowInstance, Status status, URIParser uriParser) {

		//TODO i18n!
		String description = "Byte av status på ärende " + flowInstance.getFlowInstanceID() + " till " + status.getName();

		//TODO checksum of latest available PDF
		String dataToSign = "Change status of flow instance " + flowInstance.getFlowInstanceID() + " to " + status.getStatusID();
		String signingFormURL = uriParser.getFullContextPath() + getFullAlias() + "/signstatus/" + flowInstance.getFlowInstanceID() + "/" + status.getStatusID();
		String processSigningURL = uriParser.getFullContextPath() + getFullAlias() + "/processsignstatus/" + flowInstance.getFlowInstanceID() + "/" + status.getStatusID() + "?signreq=1";

		return new SimpleSigningRequest(description, dataToSign, signingFormURL, processSigningURL);
	}

	@WebPublic(alias = "processsignstatus")
	public ForegroundModuleResponse processUpdateStatusSigning(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Exception {

		FlowInstance flowInstance;

		if (uriParser.size() == 4 && NumberUtils.isInt(uriParser.get(2)) && (flowInstance = getFlowInstance(Integer.valueOf(uriParser.get(2)), null, getUpdateStatusRelations())) != null && !flowInstance.getStatus().getContentType().equals(ContentType.NEW)) {

			String statusID = uriParser.get(3);

			Status status;

			if (NumberUtils.isInt(statusID) && (status = getStatus(flowInstance.getFlow(), Integer.valueOf(statusID))) != null && status.isRequireSigning()) {

				final Status previousStatus = flowInstance.getStatus();

				getGeneralAccessController().checkFlowInstanceAccess(flowInstance, user);

				if (!flowInstance.getFlow().isEnabled() || isOperatingStatusDisabled(flowInstance, true)) {

					return list(req, res, user, uriParser, FLOW_DISABLED_VALIDATION_ERROR);
				}

				if (status.getContentType() == ContentType.NEW || status.getContentType() == ContentType.WAITING_FOR_MULTISIGN || status.getContentType() == ContentType.WAITING_FOR_PAYMENT || (status.isUseAccessCheck() && !AccessUtils.checkAccess(user, status))) {

					redirectToMethod(req, res, "/status/" + flowInstance.getFlowInstanceID());
					return null;
				}

				log.info("User " + user + " processing singing for changing status of instance " + flowInstance + " from " + previousStatus + " to " + status);

				List<ValidationError> validationErrors = null;

				try {
					if (genericSigningProvider == null) {
						throw new ModuleConfigurationException("genericSigningProvider is null");
					}

					SigningResponse signingResponse = genericSigningProvider.processSigning(req, res, user, getUpdateStatusSigningRequest(flowInstance, status, uriParser));

					if (res.isCommitted()) {
						return null;
					}

					if (signingResponse != null) {

						log.info("User " + user + " signed changing status of instance " + flowInstance + " from " + previousStatus + " to " + status);

						flowInstance.setStatus(status);
						flowInstance.setLastStatusChange(TimeUtils.getCurrentTimestamp());
						this.daoFactory.getFlowInstanceDAO().update(flowInstance);

						FlowInstanceEvent flowInstanceEvent = flowInstanceEventGenerator.addFlowInstanceEvent(flowInstance, EventType.STATUS_UPDATED, managerSignedDetailsText, user, null, signingResponse.getSigningAttributes());

						eventHandler.sendEvent(FlowInstance.class, new CRUDEvent<FlowInstance>(CRUDAction.UPDATE, flowInstance), EventTarget.ALL);

						eventHandler.sendEvent(FlowInstance.class, new StatusChangedByManagerEvent(flowInstance, flowInstanceEvent, getSiteProfile(flowInstance), previousStatus, user), EventTarget.ALL);

						redirectToMethod(req, res, "/overview/" + flowInstance.getFlowInstanceID());
						return null;

					} else {

						log.warn("Signing failed");
					}

				} catch (ValidationException e) {

					validationErrors = new ArrayList<ValidationError>();
					validationErrors.addAll(e.getErrors());
				}

				return showUpdateStatusSignForm(req, res, user, uriParser, flowInstance, status, validationErrors);

			} else {

				redirectToMethod(req, res, "/status/" + flowInstance.getFlowInstanceID());
				return null;
			}
		}

		return list(req, res, user, uriParser, FLOW_INSTANCE_NOT_FOUND_VALIDATION_ERROR);
	}

	protected Field[] getUpdateStatusRelations() {

		return UPDATE_STATUS_RELATIONS;
	}

	private Status getStatus(Flow flow, Integer statusID) throws SQLException {

		HighLevelQuery<Status> query = new HighLevelQuery<Status>(Status.MANAGER_USERS_RELATION, Status.MANAGER_GROUPS_RELATION);

		query.addParameter(statusIDParamFactory.getParameter(statusID));
		query.addParameter(statusFlowParamFactory.getParameter(flow));

		return daoFactory.getStatusDAO().get(query);
	}

	@WebPublic(alias = "managers")
	public ForegroundModuleResponse updateManagers(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws ModuleConfigurationException, SQLException, AccessDeniedException, IOException {

		FlowInstance flowInstance;

		if (uriParser.size() == 3 && NumberUtils.isInt(uriParser.get(2)) && (flowInstance = getFlowInstance(Integer.valueOf(uriParser.get(2)), null, getUpdateManagerRelations())) != null && !flowInstance.getStatus().getContentType().equals(ContentType.NEW)) {

			getUpdateManagersAccessController().checkFlowInstanceAccess(flowInstance, user);
			
			if (!flowInstance.getFlow().isEnabled() || isOperatingStatusDisabled(flowInstance, true)) {

				return list(req, res, user, uriParser, FLOW_DISABLED_VALIDATION_ERROR);
			}

			log.info("User " + user + " requesting update managers form for instance " + flowInstance);

			Document doc = this.createDocument(req, uriParser, user);

			Element updateInstanceManagersElement = doc.createElement("UpdateInstanceManagers");

			doc.getDocumentElement().appendChild(updateInstanceManagersElement);

			List<User> allowedManagers = getAllowedManagers(flowInstance);
			List<Group> allowedManagerGroups = getAllowedManagerGroups(flowInstance);

			if (req.getMethod().equalsIgnoreCase("POST")) {

				List<ValidationError> validationErrors = new ArrayList<ValidationError>();

				List<Integer> userIDs = NumberUtils.toInt(req.getParameterValues("userID"));
				List<Integer> groupIDs = NumberUtils.toInt(req.getParameterValues("groupID"));

				String detailString;

				List<User> previousManagers = flowInstance.getManagers();
				List<Group> previousManagerGroups = flowInstance.getManagerGroups();

				if (userIDs != null || groupIDs != null) {

					flowInstance.setManagers(FlowFamilyUtils.filterSelectedManagerUsers(allowedManagers, userIDs, validationErrors));
					flowInstance.setManagerGroups(FlowFamilyUtils.filterSelectedManagerGroups(allowedManagerGroups, groupIDs, validationErrors));

					detailString = FlowInstanceUtils.getManagersString(flowInstance.getManagers(), flowInstance.getManagerGroups());

				} else {

					detailString = noManagersSelected;
					flowInstance.setManagers(null);
					flowInstance.setManagerGroups(null);
				}

				if (validationErrors.isEmpty()) {

					if (!CollectionUtils.equals(previousManagers, flowInstance.getManagers()) || !CollectionUtils.equals(previousManagerGroups, flowInstance.getManagerGroups())) {

						log.info("User " + user + " setting managers of instance " + flowInstance + " to " + detailString);

						RelationQuery relationQuery = new RelationQuery(FlowInstance.MANAGERS_RELATION, FlowInstance.MANAGER_GROUPS_RELATION);
						daoFactory.getFlowInstanceDAO().update(flowInstance, relationQuery);

						FlowInstanceEvent flowInstanceEvent = flowInstanceEventGenerator.addFlowInstanceEvent(flowInstance, EventType.MANAGERS_UPDATED, detailString, user);

						eventHandler.sendEvent(FlowInstance.class, new CRUDEvent<FlowInstance>(CRUDAction.UPDATE, flowInstance), EventTarget.ALL);
						eventHandler.sendEvent(FlowInstance.class, new ManagersChangedEvent(flowInstance, flowInstanceEvent, getSiteProfile(flowInstance), previousManagers, previousManagerGroups, user), EventTarget.ALL);
					}

					try {
						getGeneralAccessController().checkFlowInstanceAccess(flowInstance, user);

						redirectToMethod(req, res, "/overview/" + flowInstance.getFlowInstanceID());

					} catch (AccessDeniedException e) {

						redirectToDefaultMethod(req, res);
					}

					return null;

				} else {

					XMLUtils.append(doc, updateInstanceManagersElement, validationErrors);
				}
			}

			if (flowInstance.getManagers() != null) {

				Collections.sort(flowInstance.getManagers(), UserNameComparator.getInstance());
			}

			if (flowInstance.getManagerGroups() != null) {

				Collections.sort(flowInstance.getManagerGroups(), GroupNameComparator.getInstance());
			}

			updateInstanceManagersElement.appendChild(flowInstance.toXML(doc));

			XMLUtils.append(doc, updateInstanceManagersElement, user);
			XMLUtils.appendNewElement(doc, updateInstanceManagersElement, "AvailableManagersCount", CollectionUtils.getSize(allowedManagers));
			XMLUtils.appendNewElement(doc, updateInstanceManagersElement, "AvailableManagerGroupsCount", CollectionUtils.getSize(allowedManagerGroups));

			appendBookmark(doc, updateInstanceManagersElement, flowInstance, req, user);

			return new SimpleForegroundModuleResponse(doc, flowInstance.getFlow().getName(), this.getDefaultBreadcrumb());
		}

		return list(req, res, user, uriParser, FLOW_INSTANCE_NOT_FOUND_VALIDATION_ERROR);
	}
	
	@WebPublic(alias = "getmanagers")
	public ForegroundModuleResponse getManagers(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Throwable {

		return userGroupListFlowManagersConnector.getUsers(req, res, user, uriParser);
	}
	
	@WebPublic(alias = "getmanagergroups")
	public ForegroundModuleResponse getManagerGroups(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Throwable {
		
		return userGroupListFlowManagersConnector.getGroups(req, res, user, uriParser);
	}
	
	protected Field[] getUpdateManagerRelations() {

		return UPDATE_MANAGER_RELATIONS;
	}

	@WebPublic(alias = "delete")
	public ForegroundModuleResponse deleteFlowInstance(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws ModuleConfigurationException, SQLException, AccessDeniedException, IOException {

		FlowInstance flowInstance;

		if (uriParser.size() == 3 && uriParser.getInt(2) != null && (flowInstance = getFlowInstance(uriParser.getInt(2))) != null && !flowInstance.getStatus().getContentType().equals(ContentType.NEW)) {

			checkDeleteAccess(flowInstance, user);

			log.info("User " + user + " deleting flow instance " + flowInstance);

			TransactionHandler transactionHandler = null;

			try {
				transactionHandler = new TransactionHandler(dataSource);

				for (Step step : flowInstance.getFlow().getSteps()) {

					if (step.getQueryDescriptors() != null) {

						for (QueryDescriptor queryDescriptor : step.getQueryDescriptors()) {

							if (queryDescriptor.getQueryInstanceDescriptors() != null) {

								try {
									queryDescriptor.getQueryInstanceDescriptors().get(0).setQueryDescriptor(queryDescriptor);
									this.queryHandler.deleteQueryInstance(queryDescriptor.getQueryInstanceDescriptors().get(0), transactionHandler);

								} catch (QueryProviderException e) {

									log.error("Unable to delete " + queryDescriptor + " belonging to flow instance " + flowInstance, e);
								}
							}
						}
					}
				}

				daoFactory.getFlowInstanceDAO().delete(flowInstance, transactionHandler);

				transactionHandler.commit();

			} finally {

				TransactionHandler.autoClose(transactionHandler);
			}

			eventHandler.sendEvent(FlowInstance.class, new CRUDEvent<FlowInstance>(CRUDAction.DELETE, flowInstance), EventTarget.ALL);

			eventHandler.sendEvent(FlowInstance.class, new DeletedByManagerEvent(flowInstance, user), EventTarget.ALL);

			redirectToDefaultMethod(req, res);

			return null;
		}

		return list(req, res, user, uriParser, FLOW_INSTANCE_NOT_FOUND_VALIDATION_ERROR);
	}

	protected void checkDeleteAccess(FlowInstance flowInstance, User user) throws AccessDeniedException {

		getDeleteAccessController().checkFlowInstanceAccess(flowInstance, user);
	}

	@WebPublic(alias = "bookmark")
	public ForegroundModuleResponse toggleBookmark(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws ModuleConfigurationException, SQLException, AccessDeniedException, IOException {

		Field[] relations = { FlowInstance.FLOW_RELATION, Flow.FLOW_FAMILY_RELATION, FlowFamily.MANAGER_GROUPS_RELATION, FlowFamily.MANAGER_USERS_RELATION, FlowInstance.STATUS_RELATION };

		FlowInstance flowInstance;

		if (uriParser.size() == 3 && NumberUtils.isInt(uriParser.get(2)) && (flowInstance = getFlowInstance(Integer.valueOf(uriParser.get(2)), null, relations)) != null && !flowInstance.getStatus().getContentType().equals(ContentType.NEW)) {

			getGeneralAccessController().checkFlowInstanceAccess(flowInstance, user);

			if (!flowInstance.getFlow().isEnabled() || isOperatingStatusDisabled(flowInstance, true)) {

				return list(req, res, user, uriParser, FLOW_DISABLED_VALIDATION_ERROR);
			}

			UserBookmark bookmark = getBookmark(user, flowInstance);

			if (bookmark == null) {

				log.info("User " + user + " adding bookmark for instance " + flowInstance);

				bookmark = new UserBookmark();
				bookmark.setFlowInstance(flowInstance);
				bookmark.setUser(user);
				daoFactory.getUserBookmarkDAO().add(bookmark);

			} else {

				log.info("User " + user + " removing bookmark for instance " + flowInstance);
				daoFactory.getUserBookmarkDAO().delete(bookmark);
				bookmark = null;
			}

			res.setContentType("text/html");
			res.setCharacterEncoding(systemInterface.getEncoding());
			res.getWriter().write(bookmark != null ? "1" : "0");
			res.getWriter().flush();

			return null;
		}

		return list(req, res, user, uriParser, FLOW_INSTANCE_NOT_FOUND_VALIDATION_ERROR);
	}

	@WebPublic(alias = "mquery")
	public ForegroundModuleResponse processMutableQueryRequest(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws ModuleConfigurationException, SQLException, AccessDeniedException, IOException, FlowDefaultStatusNotFound, EvaluationException, URINotFoundException, QueryRequestException, QueryProviderException, EvaluationProviderException, InvalidFlowInstanceStepException, MissingQueryInstanceDescriptor, DuplicateFlowInstanceManagerIDException, UnableToResetQueryInstanceException {

		return processMutableQueryRequest(req, res, user, null, uriParser, getUpdateAccessController(), true, true, false, MANAGER_REQUEST_METADATA);
	}

	@WebPublic(alias = "iquery")
	public ForegroundModuleResponse processImmutableQueryRequest(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws ModuleConfigurationException, SQLException, AccessDeniedException, IOException, FlowDefaultStatusNotFound, EvaluationException, URINotFoundException, QueryRequestException, QueryProviderException, EvaluationProviderException, InvalidFlowInstanceStepException, MissingQueryInstanceDescriptor, DuplicateFlowInstanceManagerIDException {

		return processImmutableQueryRequest(req, res, user, uriParser, getGeneralAccessController(), true, true);
	}

	private UserBookmark getBookmark(User user, FlowInstance flowInstance) throws SQLException {

		HighLevelQuery<UserBookmark> query = new HighLevelQuery<UserBookmark>(UserBookmark.FLOW_INSTANCE_RELATION);

		query.addParameter(bookmarkFlowInstanceParamFactory.getParameter(flowInstance));
		query.addParameter(bookmarkUserParamFactory.getParameter(user));

		return daoFactory.getUserBookmarkDAO().get(query);
	}

	protected List<User> getAllowedManagers(FlowInstance flowInstance) {

		return FlowFamilyUtils.getAllowedManagerUsers(flowInstance.getFlow().getFlowFamily(), systemInterface.getUserHandler());
	}

	protected List<Group> getAllowedManagerGroups(FlowInstance flowInstance) {

		return FlowFamilyUtils.getAllowedManagerGroups(flowInstance.getFlow().getFlowFamily(), systemInterface.getGroupHandler());
	}

	@WebPublic(alias = "getmentionusers")
	public ForegroundModuleResponse getMentionUsers(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws AccessDeniedException, SQLException, IOException, URINotFoundException {

		FlowInstance flowInstance;

		if (uriParser.size() == 3 && NumberUtils.isInt(uriParser.get(2)) && (flowInstance = getFlowInstance(Integer.valueOf(uriParser.get(2)), null, getUpdateManagerRelations())) != null && !flowInstance.getStatus().getContentType().equals(ContentType.NEW)) {

			getGeneralAccessController().checkFlowInstanceAccess(flowInstance, user);

			Collection<User> availableManagers = getAllowedManagers(flowInstance);

			JsonArray jsonArray = new JsonArray();

			if (availableManagers == null) {

				HTTPUtils.sendReponse(jsonArray.toJson(), JsonUtils.getContentType(), res);
				return null;
			}

			log.info("User " + user + " searching for mention users");

			for (User currentUser : availableManagers) {

				JsonObject instance = new JsonObject(4);
				instance.putField("ID", currentUser.getUserID().toString());
				instance.putField("Name", currentUser.getFirstname() + " " + currentUser.getLastname());

				if (currentUser.getUsername() != null) {
					instance.putField("Username", currentUser.getUsername());
				} else {
					instance.putField("Username", currentUser.getFirstname().toLowerCase() + "_" + currentUser.getLastname().toLowerCase());
				}

				jsonArray.addNode(instance);
			}

			HTTPUtils.sendReponse(jsonArray.toJson(), JsonUtils.getContentType(), res);

			return null;
		}

		throw new URINotFoundException(uriParser);

	}

	@WebPublic(alias = "preview")
	public ForegroundModuleResponse processPreviewRequest(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws URINotFoundException, AccessDeniedException, ModuleConfigurationException, SQLException, IOException, UnableToGetQueryInstanceShowHTMLException, FlowInstanceManagerClosedException, OperationNotSupportedException {

		return this.showImmutableFlowInstance(req, res, user, uriParser, getGeneralAccessController(), this, ShowMode.PREVIEW, MANAGER_REQUEST_METADATA);
	}

	@WebPublic(alias = "flowinstance")
	public ForegroundModuleResponse processFlowRequest(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws URINotFoundException, AccessDeniedException, ModuleConfigurationException, SQLException, IOException, FlowDefaultStatusNotFound, EvaluationException, SubmitCheckException {

		Integer flowID = null;
		Integer flowInstanceID = null;
		MutableFlowInstanceManager instanceManager;

		try {
			if (uriParser.size() == 4 && (flowID = NumberUtils.toInt(uriParser.get(2))) != null && (flowInstanceID = NumberUtils.toInt(uriParser.get(3))) != null) {

				// Get saved instance from DB or session
				instanceManager = getSavedMutableFlowInstanceManager(flowID, flowInstanceID, getUpdateAccessController(), req.getSession(true), user, uriParser, req, true, false, true, MANAGER_REQUEST_METADATA);

				if (instanceManager == null) {

					log.info("User " + user + " requested non-existing flow instance with ID " + flowInstanceID + " and flow ID " + flowID + ", listing flows");
					return list(req, res, user, uriParser, FLOW_INSTANCE_NOT_FOUND_VALIDATION_ERROR);
				}

			} else {

				log.info("User " + user + " requested invalid URL, listing flows");
				return list(req, res, user, uriParser, INVALID_LINK_VALIDATION_ERROR);
			}

		} catch (FlowNoLongerAvailableException e) {

			log.info("User " + user + " requested flow " + e.getFlow() + " which is no longer available.");
			return list(req, res, user, uriParser, FLOW_NO_LONGER_AVAILABLE_VALIDATION_ERROR);

		} catch (FlowNotPublishedException e) {

			log.info("User " + user + " requested flow " + e.getFlow() + " which is no longer published.");
			return list(req, res, user, uriParser, FLOW_NO_LONGER_PUBLISHED_VALIDATION_ERROR);

		} catch (FlowDisabledException e) {

			log.info("User " + user + " requested flow " + e.getFlow() + " which is not enabled.");
			return list(req, res, user, uriParser, FLOW_DISABLED_VALIDATION_ERROR);

		} catch (FlowInstanceNoLongerAvailableException e) {

			log.info("User " + user + " requested flow instance " + e.getFlowInstance() + " which is no longer available.");
			return list(req, res, user, uriParser, FLOW_INSTANCE_NO_LONGER_AVAILABLE_VALIDATION_ERROR);

		} catch (FlowEngineException e) {

			log.error("Unable to get flow instance manager for flowID " + flowID + " and flowInstanceID " + flowInstanceID + " requested by user " + user, e);
			return list(req, res, user, uriParser, ERROR_GETTING_FLOW_INSTANCE_MANAGER_VALIDATION_ERROR);
		}

		try {

			return processFlowRequest(instanceManager, this, getUpdateAccessController(), req, res, user, null, uriParser, true, MANAGER_REQUEST_METADATA);

		} catch (FlowInstanceManagerClosedException e) {

			log.info("User " + user + " requested flow instance manager for flow instance " + e.getFlowInstance() + " which has already been closed. Removing flow instance manager from session.");

			removeMutableFlowInstanceManagerFromSession(instanceManager, req.getSession(false));

			return list(req, res, user, uriParser, FLOW_INSTANCE_MANAGER_CLOSED_VALIDATION_ERROR);

		} catch (QueryInstanceHTMLException e) {

			return processFlowRequestException(instanceManager, req, res, user, null, uriParser, e);

		} catch (RuntimeException e) {

			return processFlowRequestException(instanceManager, req, res, user, null, uriParser, e);
		}
	}

	@WebPublic(alias = "search")
	public ForegroundModuleResponse search(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws ModuleConfigurationException, SQLException, AccessDeniedException, IOException, FlowDefaultStatusNotFound, EvaluationException {

		flowInstanceIndexer.search(req, res, user, true);

		return null;
	}

	public void search(HttpServletRequest req, HttpServletResponse res, User user, boolean checkAccess) throws IOException {

		flowInstanceIndexer.search(req, res, user, checkAccess);
	}

	@WebPublic(alias = "externalattachment")
	public ForegroundModuleResponse getExternalMessageAttachment(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws ModuleConfigurationException, SQLException, AccessDeniedException, IOException, FlowDefaultStatusNotFound, EvaluationException, URINotFoundException {

		return externalMessageCRUD.getRequestedMessageAttachment(req, res, user, uriParser, getGeneralAccessController());
	}

	@WebPublic(alias = "internalattachment")
	public ForegroundModuleResponse getInternalMessageAttachment(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws ModuleConfigurationException, SQLException, AccessDeniedException, IOException, FlowDefaultStatusNotFound, EvaluationException, URINotFoundException {

		return internalMessageCRUD.getRequestedMessageAttachment(req, res, user, uriParser, getGeneralAccessController());
	}

	public List<Integer> getUserFlowIDs(User user, SiteProfile profile) throws SQLException {

		String sql;

		if (!CollectionUtils.isEmpty(user.getGroups())) {

			sql = FLOW_MANAGER_SQL + " OR ffg.groupID IN (?" + StringUtils.repeatString(", ?", user.getGroups().size() - 1) + "))";

		} else {

			sql = FLOW_MANAGER_SQL + ")";
		}

		ArrayListQuery<Integer> query = new ArrayListQuery<Integer>(dataSource, sql, IntegerPopulator.getPopulator());

		query.setInt(1, user.getUserID());
		query.setDate(2, DateUtils.setTimeToMidnight(new java.sql.Date(System.currentTimeMillis())));

		if (!CollectionUtils.isEmpty(user.getGroups())) {

			int paramPosition = 3;

			for (Group group : user.getGroups()) {

				query.setInt(paramPosition++, group.getGroupID());
			}
		}

		return query.executeQuery();
	}

	protected List<FlowInstance> getFlowInstanceBookmarks(User user, SiteProfile profile, List<Integer> flowIDs) throws SQLException {

		String sql = FLOW_INSTANCE_BOOKMARKS_SQL + StringUtils.toCommaSeparatedString(flowIDs) + ") ORDER BY lastStatusChange DESC";

		LowLevelQuery<FlowInstance> query = new LowLevelQuery<FlowInstance>(sql);

		query.addParameter(user.getUserID());

		query.addRelations(FlowInstance.STATUS_RELATION, FlowInstance.MANAGERS_RELATION, FlowInstance.MANAGER_GROUPS_RELATION, FlowInstance.EVENTS_RELATION, FlowInstance.FLOW_RELATION, Flow.FLOW_FAMILY_RELATION, FlowFamily.MANAGER_USERS_RELATION, FlowFamily.MANAGER_GROUPS_RELATION);
		query.addCachedRelations(FlowInstance.STATUS_RELATION, FlowInstance.MANAGERS_RELATION, FlowInstance.MANAGER_GROUPS_RELATION, FlowInstance.FLOW_RELATION, Flow.FLOW_FAMILY_RELATION);

		addListRelations(query);

		return this.daoFactory.getFlowInstanceDAO().getAll(query);
	}

	public List<FlowInstance> getActiveFlowInstances(User user, SiteProfile profile, List<Integer> flowIDs) throws SQLException {

		String sql = getActiveFlowInstancesSQL(user, profile, flowIDs, ACTIVE_FLOWS);

		LowLevelQuery<FlowInstance> query = new LowLevelQuery<FlowInstance>(sql);

		query.addRelations(FlowInstance.STATUS_RELATION, FlowInstance.MANAGERS_RELATION, FlowInstance.MANAGER_GROUPS_RELATION, FlowInstance.EVENTS_RELATION, FlowInstance.FLOW_RELATION, Flow.FLOW_FAMILY_RELATION, FlowFamily.MANAGER_USERS_RELATION, FlowFamily.MANAGER_GROUPS_RELATION);
		query.addCachedRelations(FlowInstance.STATUS_RELATION, FlowInstance.MANAGERS_RELATION, FlowInstance.MANAGER_GROUPS_RELATION, FlowInstance.FLOW_RELATION, Flow.FLOW_FAMILY_RELATION);

		addListRelations(query);

		return this.daoFactory.getFlowInstanceDAO().getAll(query);
	}

	protected void addListRelations(RelationQuery query) {

		if (selectedAttributes != null) {

			query.addRelations(FlowInstance.ATTRIBUTES_RELATION);

			query.addRelationParameter(FlowInstanceAttribute.class, attributeNameParamFactory.getWhereInParameter(selectedAttributes));
		}
	}

	public String getActiveFlowInstancesSQL(User user, SiteProfile profile, List<Integer> flowIDs, String template) {

		return template.replace("$flowIDs", StringUtils.toCommaSeparatedString(flowIDs));
	}

	@Override
	public String getSubmitActionID() {

		return null;
	}

	@Override
	public String getSaveActionID() {

		return null;
	}

	@Override
	public String getPaymentActionID() {

		return null;
	}

	@Override
	public String getMultiSigningActionID() {

		return null;
	}

	@Override
	public void appendFormData(Document doc, Element baseElement, MutableFlowInstanceManager instanceManager, HttpServletRequest req, User user) {

		appendBookmark(doc, baseElement, (FlowInstance) instanceManager.getFlowInstance(), req, user);
		
		try {

			getUpdateManagersAccessController().checkFlowInstanceAccess(instanceManager.getFlowInstance(), user);

		} catch (AccessDeniedException e) {
			
			XMLUtils.appendNewElement(doc, baseElement, "HideUpdateManagers");
		}
	}

	@Override
	public void appendShowFlowInstanceData(Document doc, Element baseElement, FlowInstanceManager instanceManager, HttpServletRequest req, User user) {

		appendBookmark(doc, baseElement, (FlowInstance) instanceManager.getFlowInstance(), req, user);

		try {

			getUpdateManagersAccessController().checkFlowInstanceAccess(instanceManager.getFlowInstance(), user);

		} catch (AccessDeniedException e) {
			
			XMLUtils.appendNewElement(doc, baseElement, "HideUpdateManagers");
		}
	}

	public void appendBookmark(Document doc, Element baseElement, FlowInstance flowInstance, HttpServletRequest req, User user) {

		try {
			if (getBookmark(user, flowInstance) != null) {

				XMLUtils.appendNewElement(doc, baseElement, "Bookmarked");
			}

		} catch (SQLException e) {

			log.error("Error getting ookmark for user " + user + " and flow instance " + flowInstance, e);
		}
	}

	@Override
	public void serverStarted() {

		Thread indexThreading = new Thread(this, "Flow instance indexer starter for " + moduleDescriptor);

		indexThreading.start();
	}

	@Override
	public void run() {

		try {
			this.flowInstanceIndexer.cacheFlowInstances();
			
		} catch (Throwable t) {
			log.error("Error caching flow instances in indexer", t);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void processEvent(CRUDEvent<?> event, EventSource source) {

		log.debug("Received crud event regarding " + event.getAction() + " of " + event.getBeans().size() + " beans with " + event.getBeanClass());

		if (FlowFamily.class.isAssignableFrom(event.getBeanClass())) {

			switch (event.getAction()) {

				case ADD:
					flowInstanceIndexer.addFlowFamilies((List<FlowFamily>) event.getBeans());
					break;

				case UPDATE:
					flowInstanceIndexer.updateFlowFamilies((List<FlowFamily>) event.getBeans());
					break;

				case DELETE:
					flowInstanceIndexer.deleteFlowFamilies((List<FlowFamily>) event.getBeans());
					break;
			}

		} else if (Flow.class.isAssignableFrom(event.getBeanClass())) {

			switch (event.getAction()) {

				case ADD:
					flowInstanceIndexer.addFlows((List<Flow>) event.getBeans());
					break;

				case UPDATE:
					flowInstanceIndexer.updateFlows((List<Flow>) event.getBeans());
					break;

				case DELETE:
					flowInstanceIndexer.deleteFlows((List<Flow>) event.getBeans());
					break;
			}

		} else if (FlowInstance.class.isAssignableFrom(event.getBeanClass())) {

			switch (event.getAction()) {

				case ADD:
					flowInstanceIndexer.addFlowInstances((List<FlowInstance>) event.getBeans());
					break;

				case UPDATE:
					flowInstanceIndexer.updateFlowInstances((List<FlowInstance>) event.getBeans());
					break;

				case DELETE:
					flowInstanceIndexer.deleteFlowInstances((List<FlowInstance>) event.getBeans());
					break;
			}

		} else if (InternalMessage.class.isAssignableFrom(event.getBeanClass())) {

			List<FlowInstance> flowInstances = new ArrayList<FlowInstance>(event.getBeans().size());

			for (Object object : event.getBeans()) {

				InternalMessage internalMessage = (InternalMessage) object;

				if (internalMessage.getFlowInstance() != null) {

					flowInstances.add(internalMessage.getFlowInstance());

				} else {

					log.warn("Internal message " + internalMessage + " has no flow instance set, unable to update search index");
				}
			}

			if (!flowInstances.isEmpty()) {

				flowInstanceIndexer.updateFlowInstances(flowInstances);
			}

		} else if (ExternalMessage.class.isAssignableFrom(event.getBeanClass())) {

			List<FlowInstance> flowInstances = new ArrayList<FlowInstance>(event.getBeans().size());

			for (Object object : event.getBeans()) {

				ExternalMessage externalMessage = (ExternalMessage) object;

				if (externalMessage.getFlowInstance() != null) {

					flowInstances.add(externalMessage.getFlowInstance());

				} else {

					log.warn("External message " + externalMessage + " has no flow instance set, unable to update search index");
				}
			}

			if (!flowInstances.isEmpty()) {

				flowInstanceIndexer.updateFlowInstances(flowInstances);
			}

		} else {

			log.warn("Received CRUD event for unsupported class " + event.getBeanClass());
		}
	}

	@Override
	protected void redirectToSubmitMethod(MutableFlowInstanceManager instanceManager, HttpServletRequest req, HttpServletResponse res) throws IOException {

		redirectToMethod(req, res, "/submitted/" + instanceManager.getFlowInstanceID());
	}

	@Override
	protected void flowInstanceSavedAndClosed(FlowInstanceManager instanceManager, HttpServletRequest req, HttpServletResponse res, User user, FlowInstanceEvent event) throws IOException {

		if (this.pdfProvider != null) {

			try {
				Map<String, String> extraElements = new HashMap<String, String>();
				extraElements.put("EditedByManager", "true");

				pdfProvider.createTemporaryPDF(instanceManager, getSiteProfile(instanceManager), user, extraElements, event);

				pdfProvider.saveTemporaryPDF(instanceManager, event);

				xmlProvider.generateXML(instanceManager.getFlowInstance(), instanceManager, event, event.getAdded());

			} catch (Exception e) {

				log.error("Error creating PDF for event " + event + " belonging to flow instance " + instanceManager.getFlowInstance() + " saved and close by " + user, e);
			}
		}

		redirectToMethod(req, res, "/overview/" + instanceManager.getFlowInstanceID());
	}

	@WebPublic(alias = "submitted")
	public ForegroundModuleResponse showSubmittedMessage(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws FlowInstanceManagerClosedException, UnableToGetQueryInstanceShowHTMLException, AccessDeniedException, ModuleConfigurationException, SQLException {

		return super.showImmutableFlowInstance(req, res, user, uriParser, getGeneralAccessController(), this, ShowMode.SUBMIT, MANAGER_REQUEST_METADATA);
	}

	@WebPublic(alias = "pdf")
	public ForegroundModuleResponse getEventPDF(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws URINotFoundException, SQLException, IOException, AccessDeniedException {

		try {
			sendEventPDF(req, res, user, uriParser, getGeneralAccessController(), pdfProvider, true);

		} catch (FlowDisabledException e) {

			return list(req, res, user, uriParser, FLOW_DISABLED_VALIDATION_ERROR);
		}

		return null;
	}

	@WebPublic(alias = "xml")
	public ForegroundModuleResponse getEventXML(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws URINotFoundException, SQLException, IOException, AccessDeniedException {

		try {
			sendEventXML(req, res, user, uriParser, getGeneralAccessController(), xmlProvider, true);

		} catch (FlowDisabledException e) {

			return list(req, res, user, uriParser, FLOW_DISABLED_VALIDATION_ERROR);
		}

		return null;
	}

	@Override
	protected String getBaseUpdateURL(HttpServletRequest req, URIParser uriParser, User user, ImmutableFlowInstance flowInstance, FlowInstanceAccessController accessController) {

		if (!accessController.isMutable(flowInstance, user)) {

			return null;
		}

		return getModuleURI(req) + "/flowinstance/" + flowInstance.getFlow().getFlowID() + "/" + flowInstance.getFlowInstanceID();
	}

	@Override
	protected String getEventPDFLink(FlowInstanceManager instanceManager, ImmutableFlowInstanceEvent event, HttpServletRequest req, User user) {

		if (event.getAttributeHandler().getPrimitiveBoolean("pdf")) {

			return this.getModuleURI(req) + "/pdf/" + instanceManager.getFlowInstanceID() + "/" + event.getEventID();
		}

		return null;
	}

	@Override
	public String getSignFailURL(MutableFlowInstanceManager instanceManager, HttpServletRequest req) {

		throw new UnsupportedOperationException();
	}

	@Override
	public String getPaymentFailURL(MutableFlowInstanceManager instanceManager, HttpServletRequest req) {

		throw new UnsupportedOperationException();
	}

	@Override
	public String getStandalonePaymentURL(FlowInstanceManager instanceManager, HttpServletRequest req) {

		throw new UnsupportedOperationException();
	}

	@Override
	public String getSaveAndSubmitURL(MutableFlowInstanceManager instanceManager, HttpServletRequest req) {

		return RequestUtils.getFullContextPathURL(req) + this.getFullAlias() + "/flowinstance/" + instanceManager.getFlowID() + "/" + instanceManager.getFlowInstanceID() + "?save-submit=1";
	}

	@Override
	public int getPriority() {

		return 0;
	}

	@Override
	public String getAbsoluteFileURL(URIParser uriParser, Object bean) {

		if (ckConnectorModuleAlias != null) {

			return uriParser.getContextPath() + ckConnectorModuleAlias;
		}

		return null;
	}

	public boolean addOverviewExtensionLinkProvider(ExtensionLinkProvider e) {

		return listOverviewExtensionLinkProviders.add(e);
	}

	public boolean removeOverviewExtensionLinkProvider(ExtensionLinkProvider e) {

		return listOverviewExtensionLinkProviders.remove(e);
	}

	public boolean addFlowInstanceOverviewExtensionProvider(FlowInstanceOverviewExtensionProvider provider) {

		return flowInstanceOverviewTabExtensionProviders.add(provider);
	}

	public boolean removeFlowInstanceOverviewExtensionProvider(FlowInstanceOverviewExtensionProvider provider) {

		return flowInstanceOverviewTabExtensionProviders.remove(provider);
	}

	@Override
	public long getMaxFileSize() {

		return maxFileSize;
	}

	private FlowInstanceOverviewExtensionProvider getOverviewExtensionProvider(String providerID) {

		for (FlowInstanceOverviewExtensionProvider tabExtensionProvider : flowInstanceOverviewTabExtensionProviders) {

			if (providerID.equals(tabExtensionProvider.getOverviewExtensionProviderID())) {

				return tabExtensionProvider;
			}
		}

		return null;
	}

	@WebPublic(alias = "overviewextensionrequest")
	public ForegroundModuleResponse processOverviewExtensionRequest(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Exception {

		FlowInstance flowInstance;

		if (uriParser.size() >= 4 && NumberUtils.isInt(uriParser.get(2)) && (flowInstance = getFlowInstance(Integer.valueOf(uriParser.get(2)), CollectionUtils.getList(ExternalMessageAttachment.DATA_FIELD, InternalMessageAttachment.DATA_FIELD), getFlowInstanceOverviewRelations())) != null && !flowInstance.getStatus().getContentType().equals(ContentType.NEW)) {

			getGeneralAccessController().checkFlowInstanceAccess(flowInstance, user);

			if (!flowInstance.getFlow().isEnabled() || isOperatingStatusDisabled(flowInstance, true)) {

				return list(req, res, user, uriParser, FLOW_DISABLED_VALIDATION_ERROR);
			}

			FlowInstanceOverviewExtensionProvider extensionProvider = getOverviewExtensionProvider(uriParser.get(3));

			if (extensionProvider != null) {

				return extensionProvider.processOverviewExtensionRequest(flowInstance, req, res, uriParser, user);
			}
		}

		throw new URINotFoundException(uriParser);
	}

	public String getOverviewExtensionRequestMethodAlias(FlowInstance flowInstance, String providerID) {

		return getFullAlias() + "/" + "overviewextensionrequest" + "/" + flowInstance.getFlowInstanceID() + "/" + providerID;
	}

	protected FlowInstanceAccessController getUpdateAccessController() {

		return UPDATE_ACCESS_CONTROLLER;
	}
	
	protected FlowInstanceAccessController getUpdateManagersAccessController() {

		return UPDATE_MANAGERS_ACCESS_CONTROLLER;
	}

	protected FlowInstanceAccessController getDeleteAccessController() {

		return DELETE_ACCESS_CONTROLLER;
	}

	protected FlowInstanceAccessController getGeneralAccessController() {

		return GENERAL_ACCESS_CONTROLLER;
	}

	protected FlowInstanceAccessController getGeneralFullAccessController() {

		return GENERAL_FULL_ACCESS_CONTROLLER;
	}

	@WebPublic(alias = "signature")
	public ForegroundModuleResponse getEventSignature(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws URINotFoundException, SQLException, IOException, AccessDeniedException, ModuleConfigurationException {

		try {
			sendEventSignature(req, res, user, uriParser, getGeneralAccessController(), true);

		} catch (FlowDisabledException e) {

			return list(req, res, user, uriParser, FLOW_DISABLED_VALIDATION_ERROR);
		}

		return null;
	}

	@InstanceManagerDependency
	public void setNotificationHandlerModule(NotificationHandler notificationHandler) {

		if (notificationHandler != null) {

			notificationHandler.addNotificationSource(this);

		} else {

			this.notificationHandler.removeNotificationSource(this);
		}

		this.notificationHandler = notificationHandler;
	}

	@se.unlogic.hierarchy.core.annotations.EventListener(channel = FlowInstance.class)
	public void processEvent(ExternalMessageAddedEvent event, EventSource source) {

		log.debug("Received external message event regarding " + event.getFlowInstance());

		if (source == EventSource.LOCAL && event.getSenderType().equals(SenderType.USER) && notificationHandler != null) {

			try {
				notificationHandler.sendNotificationToFlowInstanceManagers(this, event.getFlowInstance().getFlowInstanceID(), notificationExternalMessage, event.getEvent().getPoster(), "message", null);

			} catch (SQLException e) {

				log.error("Error sending notifications for " + event.getExternalMessage() + " to managers of " + event.getFlowInstance(), e);
			}
		}
	}

	@se.unlogic.hierarchy.core.annotations.EventListener(channel = FlowInstance.class)
	public void processEvent(ManagersChangedEvent event, EventSource source) {

		log.debug("Received managers changed event regarding " + event.getFlowInstance());

		if (source == EventSource.LOCAL && notificationHandler != null) {

			if (!CollectionUtils.isEmpty(event.getFlowInstance().getManagers())) {
				for (User manager : event.getFlowInstance().getManagers()) {

					if (!manager.equals(event.getUser()) && (event.getPreviousManagers() == null || !event.getPreviousManagers().contains(manager))) {

						try {
							Integer userID = null;

							if (event.getUser() != null) {
								userID = event.getUser().getUserID();
							}

							notificationHandler.addNotification(event.getFlowInstance().getFlowInstanceID(), manager.getUserID(), moduleDescriptor.getModuleID(), "newManager", userID, notificationNewManager, null);

						} catch (SQLException e) {

							log.error("Error sending notification to new manager " + manager + " of " + event.getFlowInstance(), e);
						}
					}
				}
			}

			if (!CollectionUtils.isEmpty(event.getPreviousManagers())) {
				for (User manager : event.getPreviousManagers()) {

					if (event.getFlowInstance().getManagers() == null || !event.getFlowInstance().getManagers().contains(manager)) {

						try {
							notificationHandler.deleteNotifications(moduleDescriptor.getModuleID(), event.getFlowInstance().getFlowInstanceID(), manager, "newManager");

						} catch (SQLException e) {

							log.error("Error deleting notificatios for old manager " + manager + " of " + event.getFlowInstance(), e);
						}
					}
				}
			}
		}
	}

	@se.unlogic.hierarchy.core.annotations.EventListener(channel = FlowInstanceManager.class)
	public void processEvent(SubmitEvent event, EventSource source) {

		log.debug("Received submit event regarding " + event.getFlowInstanceManager());

		if (source == EventSource.LOCAL) {

			try {
				FlowInstance flowInstance = getFlowInstance(event.getFlowInstanceManager().getFlowInstanceID(), null, FlowInstance.STATUS_RELATION, FlowInstance.MANAGERS_RELATION, FlowInstance.MANAGER_GROUPS_RELATION, FlowInstance.EVENTS_RELATION, FlowInstanceEvent.POSTER_FIELD, FlowInstance.FLOW_RELATION, Flow.FLOW_FAMILY_RELATION, FlowFamily.MANAGER_USERS_RELATION, FlowFamily.MANAGER_GROUPS_RELATION, FlowFamily.AUTO_MANAGER_ASSIGNMENT_ALWAYS_USERS_RELATION, FlowFamily.AUTO_MANAGER_ASSIGNMENT_ALWAYS_GROUPS_RELATION, FlowFamily.AUTO_MANAGER_ASSIGNMENT_NO_MATCH_USERS_RELATION, FlowFamily.AUTO_MANAGER_ASSIGNMENT_NO_MATCH_GROUPS_RELATION, FlowFamily.AUTO_MANAGER_ASSIGNMENT_RULES_RELATION);
				ImmutableFlowFamily flowFamily = flowInstance.getFlow().getFlowFamily();

				boolean isPreviouslySubmitted = false;

				if (!CollectionUtils.isEmpty(flowInstance.getEvents())) {

					for (ImmutableFlowInstanceEvent flowInstanceEvent : flowInstance.getEvents()) {

						if (flowInstanceEvent.getEventType() == EventType.SUBMITTED && !flowInstanceEvent.equals(event.getEvent())) {

							isPreviouslySubmitted = true;
							break;
						}
					}
				}

				if (isPreviouslySubmitted) {

					if (notificationHandler != null && !CollectionUtils.isEmpty(flowInstance.getManagers())) {

						for (User manager : flowInstance.getManagers()) {

							try {
								notificationHandler.addNotification(flowInstance.getFlowInstanceID(), manager.getUserID(), moduleDescriptor.getModuleID(), "completion", event.getEvent().getPoster().getUserID(), notificationCompletion, null);

							} catch (SQLException e) {

								log.error("Error sending completion notification to manager " + manager + " for " + flowInstance, e);
							}
						}
					}

				} else {

					if (!ObjectUtils.isNull(flowFamily.getAutoManagerAssignmentAlwaysUserIDs(), flowFamily.getAutoManagerAssignmentAlwaysGroupIDs(), flowFamily.getAutoManagerAssignmentNoMatchUserIDs(), flowFamily.getAutoManagerAssignmentNoMatchGroupIDs(), flowFamily.getAutoManagerAssignmentRules())) {

						List<User> allowedManagers = FlowFamilyUtils.getAllowedManagerUsers(flowFamily, systemInterface.getUserHandler());
						List<Group> allowedManagerGroups = FlowFamilyUtils.getAllowedManagerGroups(flowFamily, systemInterface.getGroupHandler());

						List<User> newManagers = null;
						List<Group> newManagerGroups = null;

						if (flowFamily.getAutoManagerAssignmentAlwaysUserIDs() != null) {

							newManagers = CollectionUtils.addAndInstantiateIfNeeded(newManagers, FlowFamilyUtils.filterSelectedManagerUsers(allowedManagers, flowFamily.getAutoManagerAssignmentAlwaysUserIDs(), null));
						}

						if (flowFamily.getAutoManagerAssignmentAlwaysGroupIDs() != null) {

							newManagerGroups = CollectionUtils.addAndInstantiateIfNeeded(newManagerGroups, FlowFamilyUtils.filterSelectedManagerGroups(allowedManagerGroups, flowFamily.getAutoManagerAssignmentAlwaysGroupIDs(), null));
						}

						boolean ruleMatched = false;

						if (flowFamily.getAutoManagerAssignmentRules() != null) {

							AttributeHandler attributeHandler = event.getFlowInstanceManager().getFlowInstance().getAttributeHandler();

							if (attributeHandler != null) {

								for (AutoManagerAssignmentRule rule : flowFamily.getAutoManagerAssignmentRules()) {

									if (rule.getAttributeName() != null && rule.getValues() != null) {

										String value = attributeHandler.getString(rule.getAttributeName());

										if (value != null) {

											boolean match = rule.getValues().contains(value);

											if (!match && value.contains(",")) {

												String[] values = value.split(", ?");

												for (String splitValue : values) {

													match = rule.getValues().contains(splitValue);

													if (match) {
														break;
													}
												}
											}

											if (match != rule.isInverted()) {

												List<User> ruleManagers = null;
												List<Group> ruleManagerGroups = null;

												if (rule.getUserIDs() != null) {

													ruleManagers = FlowFamilyUtils.filterSelectedManagerUsers(allowedManagers, rule.getUserIDs(), null);
												}

												if (rule.getGroupIDs() != null) {

													ruleManagerGroups = FlowFamilyUtils.filterSelectedManagerGroups(allowedManagerGroups, rule.getGroupIDs(), null);
												}

												if (ruleManagers != null || ruleManagerGroups != null) {

													ruleMatched = true;

													newManagers = CollectionUtils.addAndInstantiateIfNeeded(newManagers, ruleManagers);
													newManagerGroups = CollectionUtils.addAndInstantiateIfNeeded(newManagerGroups, ruleManagerGroups);
												}
											}
										}
									}
								}
							}
						}

						if (!ruleMatched) {

							if (flowFamily.getAutoManagerAssignmentNoMatchUserIDs() != null) {

								newManagers = CollectionUtils.addAndInstantiateIfNeeded(newManagers, FlowFamilyUtils.filterSelectedManagerUsers(allowedManagers, flowFamily.getAutoManagerAssignmentNoMatchUserIDs(), null));
							}

							if (flowFamily.getAutoManagerAssignmentNoMatchGroupIDs() != null) {

								newManagerGroups = CollectionUtils.addAndInstantiateIfNeeded(newManagerGroups, FlowFamilyUtils.filterSelectedManagerGroups(allowedManagerGroups, flowFamily.getAutoManagerAssignmentNoMatchGroupIDs(), null));
							}
						}

						if (!ObjectUtils.isNull(newManagers, newManagerGroups)) {

							List<User> previousManagers = flowInstance.getManagers();
							List<Group> previousManagerGroups = flowInstance.getManagerGroups();

							Set<User> managers = new HashSet<User>();
							Set<Group> managerGroups = new HashSet<Group>();

							if (flowInstance.getManagers() != null) {
								managers.addAll(flowInstance.getManagers());
							}

							if (flowInstance.getManagerGroups() != null) {
								managerGroups.addAll(flowInstance.getManagerGroups());
							}

							if (newManagers != null) {
								managers.addAll(newManagers);
							}

							if (newManagerGroups != null) {
								managerGroups.addAll(newManagerGroups);
							}

							flowInstance.setManagers(new ArrayList<User>(managers));
							flowInstance.setManagerGroups(new ArrayList<Group>(managerGroups));

							Collections.sort(flowInstance.getManagers(), UserNameComparator.getInstance());
							Collections.sort(flowInstance.getManagerGroups(), GroupNameComparator.getInstance());

							String detailString = FlowInstanceUtils.getManagersString(flowInstance.getManagers(), flowInstance.getManagerGroups());

							log.info("Automatically setting managers of instance " + flowInstance + " to " + detailString);

							RelationQuery updateQuery = new RelationQuery(FlowInstance.MANAGERS_RELATION, FlowInstance.MANAGER_GROUPS_RELATION);
							updateQuery.addExcludedFields(FlowInstance.STATUS_RELATION, FlowInstance.FLOW_RELATION);
							daoFactory.getFlowInstanceDAO().update(flowInstance, updateQuery);

							FlowInstanceEvent flowInstanceEvent = flowInstanceEventGenerator.addFlowInstanceEvent(flowInstance, EventType.MANAGERS_UPDATED, detailString, null);

							eventHandler.sendEvent(FlowInstance.class, new CRUDEvent<FlowInstance>(CRUDAction.UPDATE, flowInstance), EventTarget.ALL);
							eventHandler.sendEvent(FlowInstance.class, new ManagersChangedEvent(flowInstance, flowInstanceEvent, getSiteProfile(flowInstance), previousManagers, previousManagerGroups, null), EventTarget.ALL);
						}
					}
				}

			} catch (SQLException e) {

				log.error("Error getting flow instance with managers and events for " + event.getFlowInstanceManager(), e);
			}
		}
	}

	@se.unlogic.hierarchy.core.annotations.EventListener(channel = FlowInstance.class)
	public void processEvent(StatusChangedByManagerEvent event, EventSource source) {

		log.debug("Received status changed by manager event regarding " + event.getFlowInstance());

		if (source == EventSource.LOCAL) {

			FlowInstance flowInstance;
			try {

				flowInstance = getFlowInstance(event.getFlowInstance().getFlowInstanceID(), null, FlowInstance.MANAGERS_RELATION, FlowInstance.MANAGER_GROUPS_RELATION, FlowInstance.STATUS_RELATION, FlowInstance.FLOW_RELATION, Flow.FLOW_FAMILY_RELATION, FlowFamily.MANAGER_USERS_RELATION, FlowFamily.MANAGER_GROUPS_RELATION, FlowFamily.AUTO_MANAGER_ASSIGNMENT_STATUS_RULES_RELATION);

			} catch (SQLException e) {

				log.error("Error getting flow instance with managers and events for " + event, e);
				return;
			}
			
			ImmutableFlowFamily flowFamily = flowInstance.getFlow().getFlowFamily();

			if (!CollectionUtils.isEmpty(flowFamily.getAutoManagerAssignmentStatusRules())) {

				AutoManagerAssignmentStatusRule matchingRule = null;
				
				for (AutoManagerAssignmentStatusRule rule : flowFamily.getAutoManagerAssignmentStatusRules()) {

					if (flowInstance.getStatus().getName().equalsIgnoreCase(rule.getStatusName())) {
					
						matchingRule = rule;
						break;
					}
				}
				
				if (matchingRule != null) {
					
					try {
						
						if (!matchingRule.isAddManagers() && !matchingRule.isRemovePreviousManagers()) {
							
							return;
						}
						
						List<User> previousManagers = flowInstance.getManagers();
						List<Group> previousManagerGroups = flowInstance.getManagerGroups();
						
						if (matchingRule.isAddManagers()) {
							
							List<User> allowedManagers = FlowFamilyUtils.getAllowedManagerUsers(flowFamily, systemInterface.getUserHandler());
							List<Group> allowedManagerGroups = FlowFamilyUtils.getAllowedManagerGroups(flowFamily, systemInterface.getGroupHandler());
							
							Set<User> managers = new HashSet<User>();
							Set<Group> managerGroups = new HashSet<Group>();

							if (!matchingRule.isRemovePreviousManagers()) {
								
								CollectionUtils.add(managers, previousManagers);
								CollectionUtils.add(managerGroups, previousManagerGroups);
							}
							
							List<User> newManagers = FlowFamilyUtils.filterSelectedManagerUsers(allowedManagers, matchingRule.getUserIDs(), null);
							List<Group> newManagerGroups = FlowFamilyUtils.filterSelectedManagerGroups(allowedManagerGroups, matchingRule.getGroupIDs(), null);
							
							CollectionUtils.add(managers, newManagers);
							CollectionUtils.add(managerGroups, newManagerGroups);
							
							flowInstance.setManagers(new ArrayList<User>(managers));
							flowInstance.setManagerGroups(new ArrayList<Group>(managerGroups));
							
							Collections.sort(flowInstance.getManagers(), UserNameComparator.getInstance());
							Collections.sort(flowInstance.getManagerGroups(), GroupNameComparator.getInstance());
							
						} else if (matchingRule.isRemovePreviousManagers()) {
							
							if (ObjectUtils.isNull(previousManagers, previousManagerGroups)) {
								
								return;
							}
							
							flowInstance.setManagers(null);
							flowInstance.setManagerGroups(null);
						}
						
						if (CollectionUtils.equals(previousManagers, flowInstance.getManagers()) && CollectionUtils.equals(previousManagerGroups, flowInstance.getManagerGroups())) {
							
							return;
						}
						
						String detailString = null;
						
						if (CollectionUtils.isEmpty(flowInstance.getManagers()) && CollectionUtils.isEmpty(flowInstance.getManagerGroups())) {
							
							log.info("Removing managers of instance " + flowInstance);
							
						} else {
							
							detailString = FlowInstanceUtils.getManagersString(flowInstance.getManagers(), flowInstance.getManagerGroups());
							
							log.info("Automatically setting managers of instance " + flowInstance + " to " + detailString);
						}
						
						RelationQuery updateQuery = new RelationQuery(FlowInstance.MANAGERS_RELATION, FlowInstance.MANAGER_GROUPS_RELATION);
						updateQuery.addExcludedFields(FlowInstance.STATUS_RELATION, FlowInstance.FLOW_RELATION);
						daoFactory.getFlowInstanceDAO().update(flowInstance, updateQuery);
						
						FlowInstanceEvent flowInstanceEvent = flowInstanceEventGenerator.addFlowInstanceEvent(flowInstance, EventType.MANAGERS_UPDATED, detailString, null);
						
						eventHandler.sendEvent(FlowInstance.class, new CRUDEvent<FlowInstance>(CRUDAction.UPDATE, flowInstance), EventTarget.ALL);
						
						ManagersChangedEvent managersChangedEvent = new ManagersChangedEvent(flowInstance, flowInstanceEvent, getSiteProfile(flowInstance), previousManagers, previousManagerGroups, null);
						
						if (matchingRule.isSendNotification() && matchingRule.getEmailRecipients() != null) {
							
							managersChangedEvent.setAdditionalGlobalEmailRecipients(matchingRule.getEmailRecipients());
						}
						
						eventHandler.sendEvent(FlowInstance.class, managersChangedEvent, EventTarget.ALL);
						
					} catch (SQLException e) {
						
						log.error("Error updating flow instance with new managers for " + matchingRule, e);
						
					}
				}
			}
		}
	}

	@Override
	public NotificationMetadata getNotificationMetadata(Notification notification, FlowInstance flowInstance, String fullContextPath) throws Exception {

		String type = notification.getNotificationType();
		NotificationMetadata extra = new NotificationMetadata();
		extra.setShowURL(fullContextPath + getFullAlias() + "/overview/" + notification.getFlowInstanceID());

		if (notification.getExternalNotificationID() != null) {
			extra.setPoster(systemInterface.getUserHandler().getUser(notification.getExternalNotificationID(), false, true));
		}

		if (type.equals("message")) {

			extra.setUrl(fullContextPath + getFullAlias() + "/overview/" + notification.getFlowInstanceID() + "#messages");

		} else if (type.equals("mention")) {

			extra.setUrl(fullContextPath + getFullAlias() + "/overview/" + notification.getFlowInstanceID() + "#notes");

		} else {

			extra.setUrl(fullContextPath + getFullAlias() + "/overview/" + notification.getFlowInstanceID());
		}

		return extra;
	}

	@Override
	public ModuleDescriptor getModuleDescriptor() {

		return moduleDescriptor;
	}

	@Override
	protected Icon getFlowTypeIcon(Integer flowTypeID) throws SQLException {

		return this.getBareFlowType(flowTypeID);
	}

	@Override
	protected Icon getFlowIcon(Integer flowID) throws SQLException {

		return FlowIconUtils.getFlowIcon(getBareFlow(flowID));
	}

	public String getNoManagersSelectedMessage() {

		return noManagersSelected;
	}

	public String getFlowInstancePreviewURL(Integer flowInstanceID) {

		return this.getFullAlias() + "/preview/" + flowInstanceID;
	}

	protected void filterRestrictedFlowInstances(List<FlowInstance> flowInstances, User user) {

		if (flowInstances != null) {

			Iterator<FlowInstance> it = flowInstances.iterator();

			while (it.hasNext()) {

				FlowInstance flowInstance = it.next();

				if (flowInstance.getFlow().getFlowFamily().checkManagerRestrictedAccess(user) && !flowInstance.isManager(user)) {
					it.remove();
				}
			}
		}
	}

	public int getHighPriorityThreshold() {

		return highPriorityThreshold;
	}

	public int getMediumPriorityThreshold() {

		return mediumPriorityThreshold;
	}

	public boolean isEnableSiteProfileSupport() {

		return enableSiteProfileSupport;
	}

	public boolean isEnableDescriptionColumn() {

		return enableDescriptionColumn;
	}
}
