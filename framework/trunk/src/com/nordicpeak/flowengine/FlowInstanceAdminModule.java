package com.nordicpeak.flowengine;

import java.io.IOException;
import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import se.unlogic.hierarchy.core.annotations.CheckboxSettingDescriptor;
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
import se.unlogic.hierarchy.core.interfaces.EventListener;
import se.unlogic.hierarchy.core.interfaces.ForegroundModuleDescriptor;
import se.unlogic.hierarchy.core.interfaces.ForegroundModuleResponse;
import se.unlogic.hierarchy.core.interfaces.SectionInterface;
import se.unlogic.hierarchy.core.interfaces.SystemStartupListener;
import se.unlogic.hierarchy.core.interfaces.ViewFragment;
import se.unlogic.hierarchy.core.utils.ViewFragmentUtils;
import se.unlogic.hierarchy.core.utils.extensionlinks.ExtensionLink;
import se.unlogic.hierarchy.core.utils.extensionlinks.ExtensionLinkProvider;
import se.unlogic.hierarchy.core.utils.extensionlinks.ExtensionLinkUtils;
import se.unlogic.openhierarchy.foregroundmodules.siteprofile.interfaces.SiteProfile;
import se.unlogic.standardutils.collections.CollectionUtils;
import se.unlogic.standardutils.dao.HighLevelQuery;
import se.unlogic.standardutils.dao.LowLevelQuery;
import se.unlogic.standardutils.dao.QueryParameterFactory;
import se.unlogic.standardutils.dao.RelationQuery;
import se.unlogic.standardutils.dao.TransactionHandler;
import se.unlogic.standardutils.dao.querys.ArrayListQuery;
import se.unlogic.standardutils.date.DateUtils;
import se.unlogic.standardutils.io.BinarySizeFormater;
import se.unlogic.standardutils.io.BinarySizes;
import se.unlogic.standardutils.numbers.NumberUtils;
import se.unlogic.standardutils.populators.IntegerPopulator;
import se.unlogic.standardutils.string.StringUtils;
import se.unlogic.standardutils.time.TimeUtils;
import se.unlogic.standardutils.validation.PositiveStringIntegerValidator;
import se.unlogic.standardutils.validation.ValidationError;
import se.unlogic.standardutils.validation.ValidationException;
import se.unlogic.standardutils.xml.XMLUtils;
import se.unlogic.webutils.http.RequestUtils;
import se.unlogic.webutils.http.URIParser;

import com.nordicpeak.flowengine.accesscontrollers.ManagerFlowInstanceAccessController;
import com.nordicpeak.flowengine.beans.ExternalMessage;
import com.nordicpeak.flowengine.beans.ExternalMessageAttachment;
import com.nordicpeak.flowengine.beans.Flow;
import com.nordicpeak.flowengine.beans.FlowFamily;
import com.nordicpeak.flowengine.beans.FlowInstance;
import com.nordicpeak.flowengine.beans.FlowInstanceEvent;
import com.nordicpeak.flowengine.beans.InternalMessage;
import com.nordicpeak.flowengine.beans.InternalMessageAttachment;
import com.nordicpeak.flowengine.beans.QueryDescriptor;
import com.nordicpeak.flowengine.beans.RequestMetadata;
import com.nordicpeak.flowengine.beans.Status;
import com.nordicpeak.flowengine.beans.Step;
import com.nordicpeak.flowengine.beans.UserBookmark;
import com.nordicpeak.flowengine.cruds.ExternalMessageCRUD;
import com.nordicpeak.flowengine.cruds.InternalMessageCRUD;
import com.nordicpeak.flowengine.enums.ContentType;
import com.nordicpeak.flowengine.enums.EventType;
import com.nordicpeak.flowengine.enums.Priority;
import com.nordicpeak.flowengine.enums.SenderType;
import com.nordicpeak.flowengine.enums.ShowMode;
import com.nordicpeak.flowengine.events.ExternalMessageAddedEvent;
import com.nordicpeak.flowengine.events.ManagersChangedEvent;
import com.nordicpeak.flowengine.events.StatusChangedByManagerEvent;
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
import com.nordicpeak.flowengine.exceptions.queryinstance.UnableToGetQueryInstanceShowHTMLException;
import com.nordicpeak.flowengine.exceptions.queryinstance.UnableToResetQueryInstanceException;
import com.nordicpeak.flowengine.exceptions.queryprovider.QueryProviderException;
import com.nordicpeak.flowengine.interfaces.AdminFlowInstanceProvider;
import com.nordicpeak.flowengine.interfaces.FlowInstanceAccessController;
import com.nordicpeak.flowengine.interfaces.FlowInstanceOverviewExtensionProvider;
import com.nordicpeak.flowengine.interfaces.FlowProcessCallback;
import com.nordicpeak.flowengine.interfaces.ImmutableFlowInstance;
import com.nordicpeak.flowengine.interfaces.ImmutableFlowInstanceEvent;
import com.nordicpeak.flowengine.interfaces.MessageCRUDCallback;
import com.nordicpeak.flowengine.interfaces.PDFProvider;
import com.nordicpeak.flowengine.interfaces.XMLProvider;
import com.nordicpeak.flowengine.managers.FlowInstanceManager;
import com.nordicpeak.flowengine.managers.MutableFlowInstanceManager;
import com.nordicpeak.flowengine.search.FlowInstanceIndexer;
import com.nordicpeak.flowengine.validationerrors.UnauthorizedManagerUserValidationError;

public class FlowInstanceAdminModule extends BaseFlowBrowserModule implements FlowProcessCallback, SystemStartupListener, EventListener<CRUDEvent<?>>, MessageCRUDCallback, Runnable{

	protected static final Field[] FLOW_INSTANCE_OVERVIEW_RELATIONS = { FlowInstance.OWNERS_RELATION, FlowInstance.INTERNAL_MESSAGES_RELATION, InternalMessage.ATTACHMENTS_RELATION, FlowInstance.EXTERNAL_MESSAGES_RELATION, ExternalMessage.ATTACHMENTS_RELATION, FlowInstance.FLOW_RELATION, Flow.FLOW_FAMILY_RELATION, FlowFamily.MANAGER_GROUPS_RELATION, FlowFamily.MANAGER_USERS_RELATION, FlowInstance.FLOW_STATE_RELATION, FlowInstance.EVENTS_RELATION, FlowInstance.ATTRIBUTES_RELATION, FlowInstanceEvent.ATTRIBUTES_RELATION, FlowInstance.MANAGERS_RELATION};

	protected static final Field[] UPDATE_STATUS_RELATIONS = { FlowInstance.FLOW_RELATION, Flow.FLOW_FAMILY_RELATION, FlowFamily.MANAGER_GROUPS_RELATION, FlowFamily.MANAGER_USERS_RELATION, FlowInstance.FLOW_STATE_RELATION, Flow.STATUSES_RELATION };
	protected static final Field[] UPDATE_MANAGER_RELATIONS = { FlowInstance.FLOW_RELATION, Flow.FLOW_FAMILY_RELATION, FlowFamily.MANAGER_GROUPS_RELATION, FlowFamily.MANAGER_USERS_RELATION, FlowInstance.FLOW_STATE_RELATION, FlowInstance.MANAGERS_RELATION };

	@SuppressWarnings("rawtypes")
	private static final Class[] EVENT_LISTENER_CLASSES = new Class[] { FlowFamily.class, Flow.class, FlowInstance.class, InternalMessage.class, ExternalMessage.class};

	protected static final String FLOW_MANAGER_SQL = "SELECT flowID FROM flowengine_flows WHERE enabled = true AND flowFamilyID IN (SELECT ff.flowFamilyID FROM flowengine_flow_families ff LEFT JOIN flowengine_flow_family_manager_users ffu on ff.flowFamilyID = ffu.flowFamilyID LEFT JOIN flowengine_flow_family_manager_groups ffg on ff.flowFamilyID = ffg.flowFamilyID WHERE ffu.userID = ?";
	protected static final String FLOW_INSTANCE_BOOKMARKS_SQL = "SELECT ffi.* FROM flowengine_flow_instances ffi LEFT JOIN flowengine_flow_instance_bookmarks ffib ON ffi.flowInstanceID = ffib.flowInstanceID WHERE ffib.userID = ? AND ffi.flowID IN (";
	protected static final String ACTIVE_FLOWS = "SELECT ffi.* FROM flowengine_flow_instances ffi LEFT JOIN flowengine_flow_statuses ffs ON ffi.statusID = ffs.statusID WHERE ffi.firstSubmitted IS NOT NULL AND ffi.flowID IN ($flowIDs) AND ffs.contentType NOT IN ('" + ContentType.NEW + "', '" + ContentType.ARCHIVED + "') ORDER BY lastStatusChange DESC";

	public static final ManagerFlowInstanceAccessController UPDATE_ACCESS_CONTROLLER = new ManagerFlowInstanceAccessController(true, false);
	public static final ManagerFlowInstanceAccessController DELETE_ACCESS_CONTROLLER = new ManagerFlowInstanceAccessController(false, true);
	public static final ManagerFlowInstanceAccessController GENERAL_ACCESS_CONTROLLER = new ManagerFlowInstanceAccessController(false, false);

	public static final ValidationError STATUS_NOT_FOUND_VALIDATION_ERROR = new ValidationError("StatusNotFoundValidationError");
	public static final ValidationError INVALID_STATUS_VALIDATION_ERROR = new ValidationError("InvalidStatusValidationError");
	public static final ValidationError FLOW_INSTANCE_PREVIEW_VALIDATION_ERROR = new ValidationError("FlowInstancePreviewError");
	public static final ValidationError FLOW_INSTANCE_MANAGER_CLOSED_VALIDATION_ERROR = new ValidationError("FlowInstanceManagerClosedError");

	public static final ValidationError ONE_OR_MORE_SELECTED_MANAGER_USERS_NOT_FOUND_VALIDATION_ERROR = new ValidationError("OneOrMoreSelectedManagerUsersNotFoundError");

	protected static final RequestMetadata MANAGER_REQUEST_METADATA = new RequestMetadata(true);
	
	@XSLVariable(prefix = "java.")
	private String noManagersSelected = "No managers selected";

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
	@CheckboxSettingDescriptor(name="Enable site profile support", description="Controls if site profile support is enabled")
	protected boolean enableSiteProfileSupport;

	@ModuleSetting
	@CheckboxSettingDescriptor(name="Enable logging of flow instance indexing", description="Enables logging of indexing of flow instances")
	protected boolean logFlowInstanceIndexing;
	
	@ModuleSetting(allowsNull = true)
	@TextFieldSettingDescriptor(name="CKEditor connector module alias", description="The full alias of the CKEditor connector module (relative from the contextpath). Leave empty if you do not want to activate file manager for CKEditor")
	protected String ckConnectorModuleAlias;
	
	@ModuleSetting
	@TextFieldSettingDescriptor(name = "Max file size", description = "Maxmium allowed file size in megabytes", required = true, formatValidator = PositiveStringIntegerValidator.class)
	protected Integer maxFileSize = 50;
	
	@InstanceManagerDependency
	protected PDFProvider pdfProvider;

	@InstanceManagerDependency
	protected XMLProvider xmlProvider;

	protected CopyOnWriteArrayList<ExtensionLinkProvider> overviewExtensionLinkProviders = new CopyOnWriteArrayList<ExtensionLinkProvider>();
	protected CopyOnWriteArrayList<FlowInstanceOverviewExtensionProvider> tabExtensionProviders = new CopyOnWriteArrayList<FlowInstanceOverviewExtensionProvider>();
	protected CopyOnWriteArrayList<AdminFlowInstanceProvider> adminFlowInstanceProviders = new CopyOnWriteArrayList<AdminFlowInstanceProvider>();
	
	protected ExternalMessageCRUD externalMessageCRUD;
	protected InternalMessageCRUD internalMessageCRUD;

	private FlowInstanceIndexer flowInstanceIndexer;

	private QueryParameterFactory<Status, Integer> statusIDParamFactory;
	private QueryParameterFactory<Status, Flow> statusFlowParamFactory;
	private QueryParameterFactory<UserBookmark, FlowInstance> bookmarkFlowInstanceParamFactory;
	private QueryParameterFactory<UserBookmark, User> bookmarkUserParamFactory;

	@Override
	public void init(ForegroundModuleDescriptor moduleDescriptor, SectionInterface sectionInterface, DataSource dataSource) throws Exception {

		super.init(moduleDescriptor, sectionInterface, dataSource);
		
		systemInterface.getInstanceHandler().addInstance(FlowInstanceAdminModule.class, this);
		
	}

	@Override
	public void unload() throws Exception {

		systemInterface.getInstanceHandler().removeInstance(FlowInstanceAdminModule.class, this);
		
		eventHandler.removeEventListener(CRUDEvent.class, this, EVENT_LISTENER_CLASSES);

		flowInstanceIndexer.close();
		
		overviewExtensionLinkProviders.clear();
		tabExtensionProviders.clear();
		adminFlowInstanceProviders.clear();

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

		eventHandler.addEventListener(CRUDEvent.class, this, EVENT_LISTENER_CLASSES);
	}

	@Override
	protected void moduleConfigured() throws Exception {

		FlowInstanceIndexer oldIndexer = flowInstanceIndexer;

		this.flowInstanceIndexer = new FlowInstanceIndexer(daoFactory, maxHitCount, systemInterface);
		this.flowInstanceIndexer.setLogIndexing(this.logFlowInstanceIndexing);
		
		systemInterface.addStartupListener(this);
		
		if(oldIndexer != null){

			oldIndexer.close();
		}
	}
	
	@Override
	public ForegroundModuleResponse defaultMethod(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Exception {

		return this.list(req, res, user, uriParser, (List<ValidationError>)null);
	}

	public ForegroundModuleResponse list(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser, ValidationError validationError) throws SQLException {

		return list(req, res, user, uriParser, CollectionUtils.getGenericSingletonList(validationError));
	}

	@Override
	public ForegroundModuleResponse list(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser, List<ValidationError> validationErrors) throws SQLException {

		log.info("User " + user + " listing flow instances");

		SiteProfile profile = null;
		
		if(profileHandler != null) {
			
			profile = profileHandler.getCurrentProfile(user, req, uriParser);
		}		
		
		List<Integer> flowIDs = getUserFlowIDs(user, profile);

		List<FlowInstance> bookmarkedFlows = null;
		List<FlowInstance> activeFlowInstances = null;
		
		if(flowIDs != null){
			
			bookmarkedFlows = getFlowInstanceBookmarks(user, profile, flowIDs);
			activeFlowInstances = getActiveFlowInstances(user, profile, flowIDs);
		}
		
		if(!adminFlowInstanceProviders.isEmpty()){
			
			int activeFlowInstanceCount = CollectionUtils.getSize(activeFlowInstances);
			
			for(AdminFlowInstanceProvider adminFlowInstanceProvider : adminFlowInstanceProviders){
				
				try{
					activeFlowInstances = CollectionUtils.addAndInstantiateIfNeeded(activeFlowInstances, adminFlowInstanceProvider.getActiveFlowInstances(user));
					
				}catch(RuntimeException e){
					
					log.error("Error getting flow instances from provider " + adminFlowInstanceProvider, e);
				}
			}
			
			if(activeFlowInstances != null && CollectionUtils.getSize(activeFlowInstances) != activeFlowInstanceCount){
				
				//TODO sort
			}
		}
		
		if(CollectionUtils.isEmpty(activeFlowInstances) && bookmarkedFlows == null){
			
			Document doc = createDocument(req, uriParser, user);

			Element overviewElement = doc.createElement("OverviewElement");
			doc.getDocumentElement().appendChild(overviewElement);

			XMLUtils.append(doc, overviewElement, validationErrors);
			
			ExtensionLinkUtils.appendExtensionLinks(this.overviewExtensionLinkProviders, user, req, doc, overviewElement);
			
			return new SimpleForegroundModuleResponse(doc, this.getDefaultBreadcrumb());
		}
		
		Document doc = createDocument(req, uriParser, user);

		Element overviewElement = doc.createElement("OverviewElement");
		doc.getDocumentElement().appendChild(overviewElement);

		XMLUtils.append(doc, overviewElement, validationErrors);
		XMLUtils.append(doc, overviewElement, "BookmarkedInstances", bookmarkedFlows);

		if(activeFlowInstances != null){

			Element prioritizedInstances = doc.createElement("PrioritizedInstances");
			overviewElement.appendChild(prioritizedInstances);

			Element userAssignedInstances = doc.createElement("UserAssignedInstances");
			overviewElement.appendChild(userAssignedInstances);

			Element activeInstances = doc.createElement("ActiveInstances");
			overviewElement.appendChild(activeInstances);

			Element unassignedInstances = doc.createElement("UnassignedInstances");
			overviewElement.appendChild(unassignedInstances);

			for(FlowInstance instance : activeFlowInstances){

				if(instance.getManagers() == null){

					unassignedInstances.appendChild(instance.toXML(doc));

				}else if(instance.getManagers().contains(user)){

					userAssignedInstances.appendChild(instance.toXML(doc));

				}else{

					activeInstances.appendChild(instance.toXML(doc));
				}

				if(instance.getStatus().getManagingTime() != null){

					int daysLapsed = DateUtils.getWorkingDays(instance.getLastStatusChange(), new Date());

					float percent = (daysLapsed / instance.getStatus().getManagingTime()) * 100;

					if(percent >= highPriorityThreshold){

						instance.setPriority(Priority.HIGH);
						prioritizedInstances.appendChild(instance.toXML(doc));

					}else if(percent >= mediumPriorityThreshold){

						instance.setPriority(Priority.MEDIUM);
						prioritizedInstances.appendChild(instance.toXML(doc));

					}

				}
			}
		}

		if(enableSiteProfileSupport && this.profileHandler != null){

			XMLUtils.append(doc, overviewElement, "SiteProfiles", this.profileHandler.getProfiles());
		}
		
		ExtensionLinkUtils.appendExtensionLinks(this.overviewExtensionLinkProviders, user, req, doc, overviewElement);

		return new SimpleForegroundModuleResponse(doc, moduleDescriptor.getName(), this.getDefaultBreadcrumb());
	}
	
	@WebPublic(alias = "overview")
	public ForegroundModuleResponse showFlowInstanceOverview(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws ModuleConfigurationException, SQLException, AccessDeniedException, IOException, URINotFoundException {

		FlowInstance flowInstance;
		
		if(uriParser.size() == 3 && NumberUtils.isInt(uriParser.get(2)) && (flowInstance = getFlowInstance(Integer.valueOf(uriParser.get(2)), CollectionUtils.getList(ExternalMessageAttachment.DATA_FIELD, InternalMessageAttachment.DATA_FIELD), getFlowInstanceOverviewRelations())) != null && !flowInstance.getStatus().getContentType().equals(ContentType.NEW)){

			getGeneralAccessController().checkFlowInstanceAccess(flowInstance, user);
			
			if (!flowInstance.getFlow().isEnabled() || isOperatingStatusDisabled(flowInstance.getFlow(), true)) {

				return list(req, res, user, uriParser, FLOW_DISABLED_VALIDATION_ERROR);
			}

			log.info("User " + user + " viewing overview of flow instance " + flowInstance);
			
			Document doc = this.createDocument(req, uriParser, user);

			Element showFlowInstanceOverviewElement = doc.createElement("ShowFlowInstanceOverview");
			doc.getDocumentElement().appendChild(showFlowInstanceOverviewElement);
			
			XMLUtils.appendNewElement(doc, showFlowInstanceOverviewElement, "FormatedMaxFileSize", BinarySizeFormater.getFormatedSize(maxFileSize * BinarySizes.MegaByte));

			if(req.getMethod().equalsIgnoreCase("POST")){

				if(req.getParameter("externalmessage") != null){

					if(flowInstance.getOwners() != null){
						
						//TODO append message or request parameters
						ExternalMessage externalMessage = externalMessageCRUD.add(req, res, uriParser, user, doc, showFlowInstanceOverviewElement, flowInstance, true);

						if(externalMessage != null){

							//TODO check bug with wrong poster
							
							FlowInstanceEvent flowInstanceEvent = this.addFlowInstanceEvent(flowInstance, EventType.MANAGER_MESSAGE_SENT, null, user);

							systemInterface.getEventHandler().sendEvent(FlowInstance.class, new ExternalMessageAddedEvent(flowInstance, flowInstanceEvent, getCurrentSiteProfile(req, user, uriParser, flowInstance.getFlow().getFlowFamily()), externalMessage, SenderType.MANAGER), EventTarget.ALL);

							systemInterface.getEventHandler().sendEvent(ExternalMessage.class, new CRUDEvent<ExternalMessage>(CRUDAction.ADD, externalMessage), EventTarget.ALL);
							
							res.sendRedirect(req.getContextPath() + uriParser.getFormattedURI() + "#messages");

							return null;

						}
						
					}else{
						
						log.warn("User " + user + " tried to add external message for flow instance " + flowInstance + " which has no poster set.");
					}

				}else if(req.getParameter("internalmessage") != null){

					//TODO append message or request parameters
					InternalMessage internalMessage = internalMessageCRUD.add(req, res, uriParser, user, doc, showFlowInstanceOverviewElement, flowInstance);

					if(internalMessage != null){

						res.sendRedirect(req.getContextPath() + uriParser.getFormattedURI() + "#notes");
						
						systemInterface.getEventHandler().sendEvent(InternalMessage.class, new CRUDEvent<InternalMessage>(CRUDAction.ADD, internalMessage), EventTarget.ALL);

						return null;

					}
				}
			}

			appendShowFlowInstanceOverviewFlowInstanceElement(doc, showFlowInstanceOverviewElement, flowInstance);

			if(user != null){
				showFlowInstanceOverviewElement.appendChild(user.toXML(doc));
			}

			if(enableSiteProfileSupport && flowInstance.getProfileID() != null && this.profileHandler != null){

				XMLUtils.append(doc, showFlowInstanceOverviewElement, profileHandler.getProfile(flowInstance.getProfileID()));
			}

			appendBookmark(doc, showFlowInstanceOverviewElement, flowInstance, req, user);
			
			List<ViewFragment> viewFragments = appendOverviewData(doc, showFlowInstanceOverviewElement, flowInstance, req, user, uriParser);

			SimpleForegroundModuleResponse moduleResponse = new SimpleForegroundModuleResponse(doc, flowInstance.getFlow().getName(), this.getDefaultBreadcrumb());
			
			if(!CollectionUtils.isEmpty(viewFragments)){
				for(ViewFragment viewFragment : viewFragments){
					
					ViewFragmentUtils.appendLinksAndScripts(moduleResponse, viewFragment);
				}
			}
			
			return moduleResponse;
		}

		return list(req, res, user, uriParser, FLOW_INSTANCE_NOT_FOUND_VALIDATION_ERROR);
	}
	
	protected Element appendShowFlowInstanceOverviewFlowInstanceElement(Document doc, Element showFlowInstanceOverviewElement, FlowInstance flowInstance) {

		Element showFlowInstanceElement = flowInstance.toXML(doc);
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

		for (FlowInstanceOverviewExtensionProvider tabExtensionProvider : tabExtensionProviders) {

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

		if(uriParser.size() == 3 && NumberUtils.isInt(uriParser.get(2)) && (flowInstance = getFlowInstance(Integer.valueOf(uriParser.get(2)), null, getUpdateStatusRelations())) != null && !flowInstance.getStatus().getContentType().equals(ContentType.NEW)){

			getGeneralAccessController().checkFlowInstanceAccess(flowInstance, user);

			if (!flowInstance.getFlow().isEnabled() || isOperatingStatusDisabled(flowInstance.getFlow(), true)) {

				return list(req, res, user, uriParser, FLOW_DISABLED_VALIDATION_ERROR);
			}
			
			log.info("User " + user + " requesting update status form for instance " + flowInstance);
			
			Document doc = this.createDocument(req, uriParser, user);

			Element updateInstanceStatusElement = doc.createElement("UpdateInstanceStatus");

			doc.getDocumentElement().appendChild(updateInstanceStatusElement);

			if(req.getMethod().equalsIgnoreCase("POST")){

				Integer statusID = NumberUtils.toInt(req.getParameter("statusID"));

				Status status;

				if(statusID == null || (status = getStatus(flowInstance.getFlow(), statusID)) == null){

					updateInstanceStatusElement.appendChild(STATUS_NOT_FOUND_VALIDATION_ERROR.toXML(doc));

				}else if(status.getContentType() == ContentType.NEW){

					updateInstanceStatusElement.appendChild(INVALID_STATUS_VALIDATION_ERROR.toXML(doc));

				}else{

					Status previousStatus = flowInstance.getStatus();

					if(!previousStatus.equals(status)){

						log.info("User " + user + " changing status of instance " + flowInstance + " from " + previousStatus + " to " + status);

						flowInstance.setStatus(status);
						flowInstance.setLastStatusChange(TimeUtils.getCurrentTimestamp());
						this.daoFactory.getFlowInstanceDAO().update(flowInstance);

						FlowInstanceEvent flowInstanceEvent = addFlowInstanceEvent(flowInstance, EventType.STATUS_UPDATED, null, user);

						eventHandler.sendEvent(FlowInstance.class, new CRUDEvent<FlowInstance>(CRUDAction.UPDATE, flowInstance), EventTarget.ALL);

						eventHandler.sendEvent(FlowInstance.class, new StatusChangedByManagerEvent(flowInstance, flowInstanceEvent, getCurrentSiteProfile(req, user, uriParser, flowInstance.getFlow().getFlowFamily()), previousStatus, user), EventTarget.ALL);
					}

					redirectToMethod(req, res, "/overview/" + flowInstance.getFlowInstanceID());

					return null;
				}
			}

			updateInstanceStatusElement.appendChild(flowInstance.toXML(doc));

			XMLUtils.append(doc, updateInstanceStatusElement, user);

			appendBookmark(doc, updateInstanceStatusElement, flowInstance, req, user);

			return new SimpleForegroundModuleResponse(doc, flowInstance.getFlow().getName(), this.getDefaultBreadcrumb());
		}

		return list(req, res, user, uriParser, FLOW_INSTANCE_NOT_FOUND_VALIDATION_ERROR);
	}

	protected Field[] getUpdateStatusRelations() {

		return UPDATE_STATUS_RELATIONS;
	}

	private Status getStatus(Flow flow, Integer statusID) throws SQLException {

		HighLevelQuery<Status> query = new HighLevelQuery<Status>();

		query.addParameter(statusIDParamFactory.getParameter(statusID));
		query.addParameter(statusFlowParamFactory.getParameter(flow));

		return daoFactory.getStatusDAO().get(query);
	}

	@WebPublic(alias = "managers")
	public ForegroundModuleResponse updateManagers(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws ModuleConfigurationException, SQLException, AccessDeniedException, IOException {

		FlowInstance flowInstance;

		if(uriParser.size() == 3 && NumberUtils.isInt(uriParser.get(2)) && (flowInstance = getFlowInstance(Integer.valueOf(uriParser.get(2)), null, getUpdateManagerRelations())) != null && !flowInstance.getStatus().getContentType().equals(ContentType.NEW)){

			getGeneralAccessController().checkFlowInstanceAccess(flowInstance, user);
			
			if (!flowInstance.getFlow().isEnabled() || isOperatingStatusDisabled(flowInstance.getFlow(), true)) {

				return list(req, res, user, uriParser, FLOW_DISABLED_VALIDATION_ERROR);
			}
			
			log.info("User " + user + " requesting update managers form for instance " + flowInstance);

			Document doc = this.createDocument(req, uriParser, user);

			Element updateInstanceManagersElement = doc.createElement("UpdateInstanceManagers");

			doc.getDocumentElement().appendChild(updateInstanceManagersElement);

			List<User> availableManagers = getAvailableManagers(flowInstance);

			if(req.getMethod().equalsIgnoreCase("POST")){

				List<Integer> userIDs = NumberUtils.toInt(req.getParameterValues("userID"));

				String detailString;

				try{

					List<User> previousManagers = flowInstance.getManagers();

					if(userIDs != null){

						List<User> users = systemInterface.getUserHandler().getUsers(userIDs, false, false);

						if(CollectionUtils.getSize(users) < userIDs.size()){

							throw new ValidationException(ONE_OR_MORE_SELECTED_MANAGER_USERS_NOT_FOUND_VALIDATION_ERROR);
						}

						StringBuilder stringBuilder = new StringBuilder();

						for(User selectedManager : users){

							if(!availableManagers.contains(selectedManager)){

								throw new ValidationException(new UnauthorizedManagerUserValidationError(selectedManager));
							}

							if(stringBuilder.length() > 0){

								stringBuilder.append(", ");
							}

							stringBuilder.append(selectedManager.getFirstname());
							stringBuilder.append(" ");
							stringBuilder.append(selectedManager.getLastname());

						}

						detailString = stringBuilder.toString();
						flowInstance.setManagers(users);

					}else{

						detailString = noManagersSelected;
						flowInstance.setManagers(null);
					}

					if(!CollectionUtils.equals(previousManagers, flowInstance.getManagers())){

						log.info("User " + user + " setting managers of instance " + flowInstance + " to " + flowInstance.getManagers());

						RelationQuery relationQuery = new RelationQuery(FlowInstance.MANAGERS_RELATION);

						this.daoFactory.getFlowInstanceDAO().update(flowInstance, relationQuery);

						FlowInstanceEvent flowInstanceEvent = addFlowInstanceEvent(flowInstance, EventType.MANAGERS_UPDATED, detailString, user);

						eventHandler.sendEvent(FlowInstance.class, new CRUDEvent<FlowInstance>(CRUDAction.UPDATE, flowInstance), EventTarget.ALL);

						eventHandler.sendEvent(FlowInstance.class, new ManagersChangedEvent(flowInstance, flowInstanceEvent, getCurrentSiteProfile(req, user, uriParser, flowInstance.getFlow().getFlowFamily()), previousManagers, user), EventTarget.ALL);
					}

					redirectToMethod(req, res, "/overview/" + flowInstance.getFlowInstanceID());

					return null;

				}catch(ValidationException e){

					XMLUtils.append(doc, updateInstanceManagersElement, e.getErrors());
				}
			}

			updateInstanceManagersElement.appendChild(flowInstance.toXML(doc));

			XMLUtils.append(doc, updateInstanceManagersElement, user);

			XMLUtils.append(doc, updateInstanceManagersElement, "AvailableManagers", availableManagers);

			appendBookmark(doc, updateInstanceManagersElement, flowInstance, req, user);

			return new SimpleForegroundModuleResponse(doc, flowInstance.getFlow().getName(), this.getDefaultBreadcrumb());
		}

		return list(req, res, user, uriParser, FLOW_INSTANCE_NOT_FOUND_VALIDATION_ERROR);
	}

	protected Field[] getUpdateManagerRelations() {

		return UPDATE_MANAGER_RELATIONS;
	}

	@WebPublic(alias = "delete")
	public ForegroundModuleResponse deleteFlowInstance(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws ModuleConfigurationException, SQLException, AccessDeniedException, IOException {

		FlowInstance flowInstance;

		if(uriParser.size() == 3 && uriParser.getInt(2) != null && (flowInstance = getFlowInstance(uriParser.getInt(2))) != null && !flowInstance.getStatus().getContentType().equals(ContentType.NEW)){

			checkDeleteAccess(flowInstance, user);

			log.info("User " + user + " deleting flow instance " + flowInstance);

			TransactionHandler transactionHandler = null;

			try{
				transactionHandler = new TransactionHandler(dataSource);

				for(Step step : flowInstance.getFlow().getSteps()){

					if(step.getQueryDescriptors() != null){

						for(QueryDescriptor queryDescriptor : step.getQueryDescriptors()){

							if(queryDescriptor.getQueryInstanceDescriptors() != null){

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

			}finally{

				TransactionHandler.autoClose(transactionHandler);
			}

			eventHandler.sendEvent(FlowInstance.class, new CRUDEvent<FlowInstance>(CRUDAction.DELETE, flowInstance), EventTarget.ALL);

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

		Field[] relations = { FlowInstance.FLOW_RELATION, Flow.FLOW_FAMILY_RELATION, FlowFamily.MANAGER_GROUPS_RELATION, FlowFamily.MANAGER_USERS_RELATION, FlowInstance.FLOW_STATE_RELATION};

		FlowInstance flowInstance;

		if(uriParser.size() == 3 && NumberUtils.isInt(uriParser.get(2)) && (flowInstance = getFlowInstance(Integer.valueOf(uriParser.get(2)), null, relations)) != null && !flowInstance.getStatus().getContentType().equals(ContentType.NEW)){

			getGeneralAccessController().checkFlowInstanceAccess(flowInstance, user);

			if (!flowInstance.getFlow().isEnabled() || isOperatingStatusDisabled(flowInstance.getFlow(), true)) {

				return list(req, res, user, uriParser, FLOW_DISABLED_VALIDATION_ERROR);
			}
			
			UserBookmark bookmark = getBookmark(user, flowInstance);

			if(bookmark == null){

				log.info("User " + user + " adding bookmark for instance " + flowInstance);

				bookmark = new UserBookmark();
				bookmark.setFlowInstance(flowInstance);
				bookmark.setUser(user);
				daoFactory.getUserBookmarkDAO().add(bookmark);

			}else{

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

		return processMutableQueryRequest(req, res, user, uriParser, getUpdateAccessController(), true, true, false, MANAGER_REQUEST_METADATA);
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

	private List<User> getAvailableManagers(FlowInstance flowInstance) {

		List<User> availableManagers = null;

		if(flowInstance.getFlow().getFlowFamily().getAllowedUserIDs() != null){

			availableManagers = systemInterface.getUserHandler().getUsers(flowInstance.getFlow().getFlowFamily().getAllowedUserIDs(), false, false);
		}

		if(flowInstance.getFlow().getFlowFamily().getAllowedGroupIDs() != null){

			List<User> groupUsers = systemInterface.getUserHandler().getUsersByGroups(flowInstance.getFlow().getFlowFamily().getAllowedGroupIDs(), false);

			if(groupUsers != null){

				if(availableManagers == null){

					availableManagers = new ArrayList<User>(groupUsers.size());

				}

				for(User groupUser : groupUsers){

					if(!availableManagers.contains(groupUser)){
						availableManagers.add(groupUser);
					}

				}

			}
		}

		return availableManagers;
	}

	@WebPublic(alias = "preview")
	public ForegroundModuleResponse processPreviewRequest(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws URINotFoundException, AccessDeniedException, ModuleConfigurationException, SQLException, IOException, UnableToGetQueryInstanceShowHTMLException, FlowInstanceManagerClosedException {
		
		return this.showImmutableFlowInstance(req, res, user, uriParser, getGeneralAccessController(), this, ShowMode.PREVIEW, MANAGER_REQUEST_METADATA);
	}

	@WebPublic(alias = "flowinstance")
	public ForegroundModuleResponse processFlowRequest(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws URINotFoundException, AccessDeniedException, ModuleConfigurationException, SQLException, IOException, FlowDefaultStatusNotFound, EvaluationException {

		Integer flowID = null;
		Integer flowInstanceID = null;
		MutableFlowInstanceManager instanceManager;

		try{
			if(uriParser.size() == 4 && (flowID = NumberUtils.toInt(uriParser.get(2))) != null && (flowInstanceID = NumberUtils.toInt(uriParser.get(3))) != null){

				// Get saved instance from DB or session
				instanceManager = getSavedMutableFlowInstanceManager(flowID, flowInstanceID, getUpdateAccessController(), req.getSession(true), user, uriParser, req, true, false, true, MANAGER_REQUEST_METADATA);

				if(instanceManager == null){

					log.info("User " + user + " requested non-existing flow instance with ID " + flowInstanceID + " and flow ID " + flowID + ", listing flows");
					return list(req, res, user, uriParser, FLOW_INSTANCE_NOT_FOUND_VALIDATION_ERROR);
				}

			}else{

				log.info("User " + user + " requested invalid URL, listing flows");
				return list(req, res, user, uriParser, INVALID_LINK_VALIDATION_ERROR);
			}

		}catch(FlowNoLongerAvailableException e){

			log.info("User " + user + " requested flow " + e.getFlow() + " which is no longer available.");
			return list(req, res, user, uriParser, FLOW_NO_LONGER_AVAILABLE_VALIDATION_ERROR);

		}catch(FlowNotPublishedException e){

			log.info("User " + user + " requested flow " + e.getFlow() + " which is no longer published.");
			return list(req, res, user, uriParser, FLOW_NO_LONGER_PUBLISHED_VALIDATION_ERROR);

		}catch(FlowDisabledException e){

			log.info("User " + user + " requested flow " + e.getFlow() + " which is not enabled.");
			return list(req, res, user, uriParser, FLOW_DISABLED_VALIDATION_ERROR);

		}catch(FlowInstanceNoLongerAvailableException e){

			log.info("User " + user + " requested flow instance " + e.getFlowInstance() + " which is no longer available.");
			return list(req, res, user, uriParser, FLOW_INSTANCE_NO_LONGER_AVAILABLE_VALIDATION_ERROR);

		}catch(FlowEngineException e){

			log.error("Unable to get flow instance manager for flowID " + flowID + " and flowInstanceID " + flowInstanceID + " requested by user " + user, e);
			return list(req, res, user, uriParser, ERROR_GETTING_FLOW_INSTANCE_MANAGER_VALIDATION_ERROR);
		}

		try{

			return processFlowRequest(instanceManager, this, getUpdateAccessController(), req, res, user, uriParser, true, MANAGER_REQUEST_METADATA);

		}catch(FlowInstanceManagerClosedException e){

			log.info("User " + user + " requested flow instance manager for flow instance " + e.getFlowInstance() + " which has already been closed. Removing flow instance manager from session.");

			removeMutableFlowInstanceManagerFromSession(instanceManager, req.getSession(false));

			return list(req, res, user, uriParser, FLOW_INSTANCE_MANAGER_CLOSED_VALIDATION_ERROR);

		}catch(QueryInstanceHTMLException e){

			return processFlowRequestException(instanceManager, req, res, user, uriParser, e);

		}catch(RuntimeException e){

			return processFlowRequestException(instanceManager, req, res, user, uriParser, e);
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

		if(!CollectionUtils.isEmpty(user.getGroups())){

			sql = FLOW_MANAGER_SQL + " OR ffg.groupID IN (?" + StringUtils.repeatString(", ?", user.getGroups().size() - 1) + "))";

		}else{

			sql = FLOW_MANAGER_SQL + ")";
		}

		ArrayListQuery<Integer> query = new ArrayListQuery<Integer>(dataSource, sql, IntegerPopulator.getPopulator());

		query.setInt(1, user.getUserID());

		if(!CollectionUtils.isEmpty(user.getGroups())){

			int paramPosition = 2;

			for(Group group : user.getGroups()){

				query.setInt(paramPosition++, group.getGroupID());
			}
		}

		return query.executeQuery();
	}

	protected List<FlowInstance> getFlowInstanceBookmarks(User user, SiteProfile profile, List<Integer> flowIDs) throws SQLException {

		String sql = FLOW_INSTANCE_BOOKMARKS_SQL + StringUtils.toCommaSeparatedString(flowIDs) + ") ORDER BY lastStatusChange DESC";

		LowLevelQuery<FlowInstance> query = new LowLevelQuery<FlowInstance>(sql);

		query.addParameter(user.getUserID());

		query.addRelations(FlowInstance.FLOW_RELATION, FlowInstance.FLOW_STATE_RELATION, FlowInstance.MANAGERS_RELATION, FlowInstance.EVENTS_RELATION);

		return this.daoFactory.getFlowInstanceDAO().getAll(query);
	}

	public List<FlowInstance> getActiveFlowInstances(User user, SiteProfile profile, List<Integer> flowIDs) throws SQLException {

		String sql = getActiveFlowInstancesSQL(user, profile, flowIDs, ACTIVE_FLOWS);

		LowLevelQuery<FlowInstance> query = new LowLevelQuery<FlowInstance>(sql);

		query.addRelations(FlowInstance.FLOW_RELATION, FlowInstance.FLOW_STATE_RELATION, FlowInstance.MANAGERS_RELATION, FlowInstance.EVENTS_RELATION);

		return this.daoFactory.getFlowInstanceDAO().getAll(query);
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

		appendBookmark(doc, baseElement, (FlowInstance)instanceManager.getFlowInstance(), req, user);
	}

	@Override
	public void appendShowFlowInstanceData(Document doc, Element baseElement, FlowInstanceManager instanceManager, HttpServletRequest req, User user){

		appendBookmark(doc, baseElement, (FlowInstance)instanceManager.getFlowInstance(), req, user);
	}

	public void appendBookmark(Document doc, Element baseElement, FlowInstance flowInstance, HttpServletRequest req, User user) {

		try{
			if(getBookmark(user, flowInstance) != null){

				XMLUtils.appendNewElement(doc, baseElement, "Bookmarked");
			}

		}catch(SQLException e){

			log.error("Error getting ookmark for user " + user + " and flow instance " + flowInstance, e);
		}
	}

	@Override
	public void systemStarted() {

		Thread indexThreading = new Thread(this, "Flow instance indexer for " + moduleDescriptor);
		
		indexThreading.start();
	}

	public void run(){
		
		this.flowInstanceIndexer.cacheFlowInstances();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void processEvent(CRUDEvent<?> event, EventSource source) {

		log.debug("Received crud event regarding " + event.getAction() + " of " + event.getBeans().size() + " beans with " + event.getBeanClass());

		if(FlowFamily.class.isAssignableFrom(event.getBeanClass())){

			switch(event.getAction()){

				case ADD:
					flowInstanceIndexer.addFlowFamilies((List<FlowFamily>)event.getBeans());
					break;

				case UPDATE:
					flowInstanceIndexer.updateFlowFamilies((List<FlowFamily>)event.getBeans());
					break;

				case DELETE:
					flowInstanceIndexer.deleteFlowFamilies((List<FlowFamily>)event.getBeans());
					break;
			}

		}else if(Flow.class.isAssignableFrom(event.getBeanClass())){

			switch(event.getAction()){

				case ADD:
					flowInstanceIndexer.addFlows((List<Flow>)event.getBeans());
					break;

				case UPDATE:
					flowInstanceIndexer.updateFlows((List<Flow>)event.getBeans());
					break;

				case DELETE:
					flowInstanceIndexer.deleteFlows((List<Flow>)event.getBeans());
					break;
			}

		}else if(FlowInstance.class.isAssignableFrom(event.getBeanClass())){

			switch(event.getAction()){

				case ADD:
					flowInstanceIndexer.addFlowInstances((List<FlowInstance>)event.getBeans());
					break;

				case UPDATE:
					flowInstanceIndexer.updateFlowInstances((List<FlowInstance>)event.getBeans());
					break;

				case DELETE:
					flowInstanceIndexer.deleteFlowInstances((List<FlowInstance>)event.getBeans());
					break;
			}

			
		}else if(InternalMessage.class.isAssignableFrom(event.getBeanClass())){

			List<FlowInstance> flowInstances = new ArrayList<FlowInstance>(event.getBeans().size());
			
			for(Object object : event.getBeans()){
				
				InternalMessage internalMessage = (InternalMessage)object;
				
				if(internalMessage.getFlowInstance() != null){
					
					flowInstances.add(internalMessage.getFlowInstance());
					
				}else{
					
					log.warn("Internal message " + internalMessage + " has no flow instance set, unable to update search index");
				}
			}
		
			if(!flowInstances.isEmpty()){
				
				flowInstanceIndexer.updateFlowInstances(flowInstances);
			}
			
		}else if(ExternalMessage.class.isAssignableFrom(event.getBeanClass())){

			List<FlowInstance> flowInstances = new ArrayList<FlowInstance>(event.getBeans().size());
			
			for(Object object : event.getBeans()){
				
				ExternalMessage externalMessage = (ExternalMessage)object;
				
				if(externalMessage.getFlowInstance() != null){
					
					flowInstances.add(externalMessage.getFlowInstance());
					
				}else{
					
					log.warn("External message " + externalMessage + " has no flow instance set, unable to update search index");
				}
			}
		
			if(!flowInstances.isEmpty()){
				
				flowInstanceIndexer.updateFlowInstances(flowInstances);
			}
			
		}else{

			log.warn("Received CRUD event for unsupported class " + event.getBeanClass());
		}
	}

	@Override
	protected void redirectToSubmitMethod(MutableFlowInstanceManager instanceManager, HttpServletRequest req, HttpServletResponse res) throws IOException {

		redirectToMethod(req, res, "/submitted/" + instanceManager.getFlowInstanceID());
	}

	@Override
	protected void onFlowInstanceClosedRedirect(FlowInstanceManager instanceManager, HttpServletRequest req, HttpServletResponse res) throws IOException {

		redirectToMethod(req, res, "/overview/" + instanceManager.getFlowInstanceID());
	}

	@WebPublic(alias = "submitted")
	public ForegroundModuleResponse showSubmittedMessage(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws FlowInstanceManagerClosedException, UnableToGetQueryInstanceShowHTMLException, AccessDeniedException, ModuleConfigurationException, SQLException{

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

		if(event.getAttributeHandler().getPrimitiveBoolean("pdf")){

			return this.getModuleURI(req) + "/pdf/" + instanceManager.getFlowInstanceID() + "/" + event.getEventID();
		}

		return null;
	}

	@Override
	public String getSignFailURL(MutableFlowInstanceManager instanceManager, HttpServletRequest req) {

		return RequestUtils.getFullContextPathURL(req) + this.getFullAlias() + "/flowinstance/" + instanceManager.getFlowID() + "/" + instanceManager.getFlowInstanceID() + "?preview=1&signprovidererror=1";
	}

	@Override
	public String getPaymentFailURL(MutableFlowInstanceManager instanceManager, HttpServletRequest req) {

		return RequestUtils.getFullContextPathURL(req) + this.getFullAlias() + "/flowinstance/" + instanceManager.getFlowID() + "/" + instanceManager.getFlowInstanceID() + "?preview=1&paymentprovidererror=1";
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

		if(ckConnectorModuleAlias != null){
			
			return uriParser.getContextPath() + ckConnectorModuleAlias;
		}
		
		return null;
	}

	public boolean addOverviewExtensionLinkProvider(ExtensionLinkProvider e) {

		return overviewExtensionLinkProviders.add(e);
	}

	public boolean removeOverviewExtensionLinkProvider(ExtensionLinkProvider e) {

		return overviewExtensionLinkProviders.remove(e);
	}
	
	public boolean addFlowInstanceOverviewExtensionProvider(FlowInstanceOverviewExtensionProvider provider) {

		return tabExtensionProviders.add(provider);
	}

	public boolean removeFlowInstanceOverviewExtensionProvider(FlowInstanceOverviewExtensionProvider provider) {

		return tabExtensionProviders.remove(provider);
	}

	public boolean addFlowInstanceProvider(AdminFlowInstanceProvider flowInstanceProvider) {

		return adminFlowInstanceProviders.add(flowInstanceProvider);
	}

	public boolean removeFlowInstanceProvider(AdminFlowInstanceProvider flowInstanceProvider) {

		return adminFlowInstanceProviders.remove(flowInstanceProvider);
	}

	@Override
	public long getMaxFileSize() {

		return maxFileSize;
	}
	
	private FlowInstanceOverviewExtensionProvider getOverviewExtensionProvider(String providerID) {
		
		for (FlowInstanceOverviewExtensionProvider tabExtensionProvider : tabExtensionProviders) {
			
			if (providerID.equals(tabExtensionProvider.getOverviewExtensionProviderID())) {
				
				return tabExtensionProvider;
			}
		}
		
		return null;
	}
	
	@WebPublic(alias = "overviewextensionrequest")
	public ForegroundModuleResponse processOverviewExtensionRequest(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Exception {
		
		FlowInstance flowInstance;
		
		if (uriParser.size() == 4 && NumberUtils.isInt(uriParser.get(2)) && (flowInstance = getFlowInstance(Integer.valueOf(uriParser.get(2)), CollectionUtils.getList(ExternalMessageAttachment.DATA_FIELD, InternalMessageAttachment.DATA_FIELD), getFlowInstanceOverviewRelations())) != null && !flowInstance.getStatus().getContentType().equals(ContentType.NEW)) {
			
			getGeneralAccessController().checkFlowInstanceAccess(flowInstance, user);
			
			if (!flowInstance.getFlow().isEnabled() || isOperatingStatusDisabled(flowInstance.getFlow(), true)) {
				
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
	
	protected FlowInstanceAccessController getUpdateAccessController(){
		
		return UPDATE_ACCESS_CONTROLLER;
	}
	
	protected FlowInstanceAccessController getDeleteAccessController(){
		return DELETE_ACCESS_CONTROLLER;
	}
	
	protected FlowInstanceAccessController getGeneralAccessController(){
		return GENERAL_ACCESS_CONTROLLER;
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
}
