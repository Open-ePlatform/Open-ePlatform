package com.nordicpeak.flowengine;

import java.io.IOException;
import java.lang.reflect.Field;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import se.unlogic.hierarchy.core.annotations.CheckboxSettingDescriptor;
import se.unlogic.hierarchy.core.annotations.EnumDropDownSettingDescriptor;
import se.unlogic.hierarchy.core.annotations.InstanceManagerDependency;
import se.unlogic.hierarchy.core.annotations.ModuleSetting;
import se.unlogic.hierarchy.core.annotations.TextAreaSettingDescriptor;
import se.unlogic.hierarchy.core.annotations.TextFieldSettingDescriptor;
import se.unlogic.hierarchy.core.annotations.WebPublic;
import se.unlogic.hierarchy.core.annotations.XSLVariable;
import se.unlogic.hierarchy.core.beans.Breadcrumb;
import se.unlogic.hierarchy.core.beans.SimpleForegroundModuleResponse;
import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.hierarchy.core.enums.CRUDAction;
import se.unlogic.hierarchy.core.enums.EventSource;
import se.unlogic.hierarchy.core.enums.EventTarget;
import se.unlogic.hierarchy.core.events.CRUDEvent;
import se.unlogic.hierarchy.core.exceptions.AccessDeniedException;
import se.unlogic.hierarchy.core.exceptions.ModuleConfigurationException;
import se.unlogic.hierarchy.core.exceptions.URINotFoundException;
import se.unlogic.hierarchy.core.interfaces.AccessInterface;
import se.unlogic.hierarchy.core.interfaces.ForegroundModuleResponse;
import se.unlogic.hierarchy.core.interfaces.SectionInterface;
import se.unlogic.hierarchy.core.interfaces.ViewFragment;
import se.unlogic.hierarchy.core.interfaces.modules.descriptors.ForegroundModuleDescriptor;
import se.unlogic.hierarchy.core.interfaces.modules.descriptors.ModuleDescriptor;
import se.unlogic.hierarchy.core.utils.ViewFragmentUtils;
import se.unlogic.hierarchy.core.utils.extensionlinks.ExtensionLink;
import se.unlogic.hierarchy.foregroundmodules.userproviders.SimpleUser;
import se.unlogic.openhierarchy.foregroundmodules.siteprofile.interfaces.SiteProfile;
import se.unlogic.standardutils.collections.CollectionUtils;
import se.unlogic.standardutils.dao.HighLevelQuery;
import se.unlogic.standardutils.dao.QueryOperators;
import se.unlogic.standardutils.dao.QueryParameterFactory;
import se.unlogic.standardutils.dao.TransactionHandler;
import se.unlogic.standardutils.dao.querys.ArrayListQuery;
import se.unlogic.standardutils.date.DateUtils;
import se.unlogic.standardutils.enums.Order;
import se.unlogic.standardutils.io.BinarySizeFormater;
import se.unlogic.standardutils.io.BinarySizes;
import se.unlogic.standardutils.numbers.NumberUtils;
import se.unlogic.standardutils.populators.IntegerPopulator;
import se.unlogic.standardutils.validation.NonNegativeStringIntegerValidator;
import se.unlogic.standardutils.validation.PositiveStringIntegerValidator;
import se.unlogic.standardutils.validation.ValidationError;
import se.unlogic.standardutils.xml.XMLGeneratorDocument;
import se.unlogic.standardutils.xml.XMLUtils;
import se.unlogic.webutils.http.RequestUtils;
import se.unlogic.webutils.http.URIParser;

import com.nordicpeak.flowengine.accesscontrollers.SessionAccessController;
import com.nordicpeak.flowengine.accesscontrollers.UserFlowInstanceAccessController;
import com.nordicpeak.flowengine.beans.ExternalMessage;
import com.nordicpeak.flowengine.beans.ExternalMessageAttachment;
import com.nordicpeak.flowengine.beans.Flow;
import com.nordicpeak.flowengine.beans.FlowInstance;
import com.nordicpeak.flowengine.beans.FlowInstanceAttribute;
import com.nordicpeak.flowengine.beans.FlowInstanceEvent;
import com.nordicpeak.flowengine.beans.FlowType;
import com.nordicpeak.flowengine.beans.QueryDescriptor;
import com.nordicpeak.flowengine.beans.QueryInstanceDescriptor;
import com.nordicpeak.flowengine.beans.Status;
import com.nordicpeak.flowengine.beans.Step;
import com.nordicpeak.flowengine.beans.UserFlowInstanceBrowserProcessCallback;
import com.nordicpeak.flowengine.comparators.FlowInstanceAddedComparator;
import com.nordicpeak.flowengine.cruds.ExternalMessageCRUD;
import com.nordicpeak.flowengine.enums.ContentType;
import com.nordicpeak.flowengine.enums.EventType;
import com.nordicpeak.flowengine.enums.SenderType;
import com.nordicpeak.flowengine.enums.ShowMode;
import com.nordicpeak.flowengine.events.ExternalMessageAddedEvent;
import com.nordicpeak.flowengine.events.OwnersChangedEvent;
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
import com.nordicpeak.flowengine.exceptions.queryinstance.SubmitCheckException;
import com.nordicpeak.flowengine.exceptions.queryinstance.UnableToGetQueryInstanceShowHTMLException;
import com.nordicpeak.flowengine.exceptions.queryinstance.UnableToResetQueryInstanceException;
import com.nordicpeak.flowengine.exceptions.queryinstance.UnableToSaveQueryInstanceException;
import com.nordicpeak.flowengine.exceptions.queryprovider.QueryProviderException;
import com.nordicpeak.flowengine.interfaces.FlowInstanceAccessController;
import com.nordicpeak.flowengine.interfaces.FlowInstanceFilter;
import com.nordicpeak.flowengine.interfaces.FlowInstanceOverviewExtensionProvider;
import com.nordicpeak.flowengine.interfaces.FlowPaymentProvider;
import com.nordicpeak.flowengine.interfaces.FlowProcessCallback;
import com.nordicpeak.flowengine.interfaces.Icon;
import com.nordicpeak.flowengine.interfaces.ImmutableFlowInstance;
import com.nordicpeak.flowengine.interfaces.ImmutableFlowInstanceEvent;
import com.nordicpeak.flowengine.interfaces.ListFlowInstancesExtensionLinkProvider;
import com.nordicpeak.flowengine.interfaces.ListFlowInstancesViewFragmentExtensionProvider;
import com.nordicpeak.flowengine.interfaces.MessageCRUDCallback;
import com.nordicpeak.flowengine.interfaces.MultiSigningHandler;
import com.nordicpeak.flowengine.interfaces.PDFProvider;
import com.nordicpeak.flowengine.interfaces.SigningProvider;
import com.nordicpeak.flowengine.interfaces.UserFlowInstanceProvider;
import com.nordicpeak.flowengine.interfaces.UserMenuProvider;
import com.nordicpeak.flowengine.interfaces.XMLProvider;
import com.nordicpeak.flowengine.managers.FlowInstanceManager;
import com.nordicpeak.flowengine.managers.MutableFlowInstanceManager;
import com.nordicpeak.flowengine.managers.MutableFlowInstanceManager.FlowInstanceManagerRegistery;
import com.nordicpeak.flowengine.notifications.beans.NotificationMetadata;
import com.nordicpeak.flowengine.notifications.interfaces.Notification;
import com.nordicpeak.flowengine.notifications.interfaces.NotificationHandler;
import com.nordicpeak.flowengine.notifications.interfaces.NotificationSource;
import com.nordicpeak.flowengine.utils.ExternalMessageUtils;
import com.nordicpeak.flowengine.utils.FlowIconUtils;

public class UserFlowInstanceModule extends BaseFlowBrowserModule implements MessageCRUDCallback, NotificationSource, UserMenuProvider {

	protected static final Field[] FLOW_INSTANCE_OVERVIEW_RELATIONS = { FlowInstance.OWNERS_RELATION, FlowInstance.EXTERNAL_MESSAGES_RELATION, ExternalMessage.ATTACHMENTS_RELATION, FlowInstance.FLOW_RELATION, FlowInstance.STATUS_RELATION, FlowInstance.EVENTS_RELATION, FlowInstanceEvent.ATTRIBUTES_RELATION, FlowInstance.MANAGERS_RELATION, FlowInstance.MANAGER_GROUPS_RELATION, Flow.FLOW_FAMILY_RELATION, FlowInstance.ATTRIBUTES_RELATION };

	public static final Field[] LIST_EXCLUDED_FIELDS = { FlowInstance.POSTER_FIELD, FlowInstance.EDITOR_FIELD, Flow.ICON_FILE_NAME_FIELD, Flow.DESCRIPTION_SHORT_FIELD, Flow.DESCRIPTION_LONG_FIELD, Flow.SUBMITTED_MESSAGE_FIELD, Flow.HIDE_EXTERNAL_MESSAGES_FIELD, Flow.HIDE_EXTERNAL_MESSAGE_ATTACHMENTS_FIELD, Flow.HIDE_INTERNAL_MESSAGES_FIELD, Flow.HIDE_FROM_OVERVIEW_FIELD, Flow.HIDE_MANAGER_DETAILS_FIELD, Flow.FLOW_FORMS_FIELD, Flow.HIDE_SUBMIT_STEP_TEXT_FIELD, Flow.SHOW_SUBMIT_SURVEY_FIELD, Flow.REQUIRES_SIGNING_FIELD, Flow.REQUIRE_AUTHENTICATION_FIELD, Flow.USE_PREVIEW_FIELD, Flow.PUBLISH_DATE_FIELD, FlowInstanceEvent.POSTER_FIELD };

	public static final String SESSION_ACCESS_CONTROLLER_TAG = UserFlowInstanceModule.class.getName();

	public static final String SUBMIT_COMPLETION_ACTION_ID = UserFlowInstanceModule.class.getName() + ".submitcompletion";

	public static final UserFlowInstanceAccessController UPDATE_ACCESS_CONTROLLER = new UserFlowInstanceAccessController(true, false);
	public static final UserFlowInstanceAccessController DELETE_ACCESS_CONTROLLER = new UserFlowInstanceAccessController(false, true);
	public static final UserFlowInstanceAccessController PREVIEW_ACCESS_CONTROLLER = new UserFlowInstanceAccessController(false, false);

	private static final FlowInstanceAddedComparator FLOW_INSTANCE_ADDED_COMPARATOR = new FlowInstanceAddedComparator();

	@XSLVariable(prefix = "java.")
	private String notificationExternalMessage = "Message";

	@XSLVariable(prefix = "i18n.", name = "PostedByManager")
	private String notificationPostedByManager = "Manager";

	@XSLVariable(prefix = "i18n.", name = "StatusUpdatedEvent")
	private String eventStatusUpdated = "Status updated";

	@XSLVariable(prefix = "java.")
	private String userMenuTabTitle = "My errands";

	@ModuleSetting(allowsNull = true)
	@TextFieldSettingDescriptor(name = "CKEditor connector module alias", description = "The full alias of the CKEditor connector module (relative from the contextpath). Leave empty if you do not want to activate file manager for CKEditor")
	protected String ckConnectorModuleAlias;

	@ModuleSetting
	@TextFieldSettingDescriptor(name = "Max file size", description = "Maxmium allowed file size in megabytes", required = true, formatValidator = PositiveStringIntegerValidator.class)
	protected Integer maxFileSize = 50;

	@ModuleSetting
	@CheckboxSettingDescriptor(name = "Enable site profile support", description = "Controls if site profile support is enabled")
	protected boolean enableSiteProfileSupport;

	@ModuleSetting
	@CheckboxSettingDescriptor(name = "Enable site profile redirect support", description = "Controls if site profile redirect support is enabled")
	protected boolean enableSiteProfileRedirectSupport;

	@ModuleSetting
	@CheckboxSettingDescriptor(name = "Enable external ID support", description = "Controls if external ID is displayed")
	protected boolean enableExternalID;
	
	@ModuleSetting
	@CheckboxSettingDescriptor(name = "Enable the description column", description = "Controls if description column is visible")
	protected boolean enableDescriptionColumn = true;

	@ModuleSetting
	@CheckboxSettingDescriptor(name = "Hide manager email address in flow instance overview", description = "Controls if manager email address is shown in flow instance overview")
	protected boolean hideManagerEmailInOverview;
	
	@ModuleSetting
	@CheckboxSettingDescriptor(name = "Show new flow instance events in list", description = "Controls if new since last login events are shown in the list view")
	protected boolean showNewEventsInList = false;
	
	@ModuleSetting(allowsNull = true)
	@TextAreaSettingDescriptor(name = "Excluded flow types", description = "Flow instances from these flow types will be excluded", formatValidator = NonNegativeStringIntegerValidator.class)
	protected List<Integer> excludedFlowTypes;

	@ModuleSetting
	@TextFieldSettingDescriptor(name = "User menu item slot", description = "User menu item slot")
	protected String userMenuExtensionLinkSlot = "10";
	
	@ModuleSetting
	@EnumDropDownSettingDescriptor(name="Flow instance event sort order", description="The order of flow instance events when displayed in this module", required=true)
	protected Order flowInstanceEventSortOrder = Order.ASC;

	@InstanceManagerDependency
	protected PDFProvider pdfProvider;

	@InstanceManagerDependency
	protected SigningProvider signingProvider;

	@InstanceManagerDependency
	protected MultiSigningHandler multiSigningHandler;

	@InstanceManagerDependency
	protected FlowPaymentProvider paymentProvider;

	@InstanceManagerDependency
	protected XMLProvider xmlProvider;

	protected HashSet<Integer> excludedFlowTypesHashSet = null;

	private QueryParameterFactory<FlowInstanceEvent, FlowInstance> flowInstanceEventFlowInstanceParamFactory;
	private QueryParameterFactory<FlowInstanceEvent, User> flowInstanceEventPosterParamFactory;
	private QueryParameterFactory<FlowInstanceEvent, Timestamp> flowInstanceEventAddedParamFactory;
	private QueryParameterFactory<FlowInstanceAttribute, String> attributeNameParamFactory;

	protected UserFlowInstanceMenuModule userFlowInstanceMenuModule;

	protected NotificationHandler notificationHandler;

	private FlowProcessCallback defaultFlowProcessCallback;

	private FlowProcessCallback completeFlowProcessCallback;

	private ExternalMessageCRUD externalMessageCRUD;

	protected CopyOnWriteArrayList<FlowInstanceOverviewExtensionProvider> tabExtensionProviders = new CopyOnWriteArrayList<FlowInstanceOverviewExtensionProvider>();
	protected CopyOnWriteArrayList<ListFlowInstancesExtensionLinkProvider> listExtensionLinkProviders = new CopyOnWriteArrayList<ListFlowInstancesExtensionLinkProvider>();
	protected CopyOnWriteArrayList<ListFlowInstancesViewFragmentExtensionProvider> listViewFragmentExtensionProviders = new CopyOnWriteArrayList<ListFlowInstancesViewFragmentExtensionProvider>();

	protected CopyOnWriteArrayList<UserFlowInstanceProvider> userFlowInstanceProviders = new CopyOnWriteArrayList<UserFlowInstanceProvider>();
	
	protected CopyOnWriteArrayList<FlowInstanceFilter> flowInstanceFilters = new CopyOnWriteArrayList<FlowInstanceFilter>();

	protected Locale systemLocale;

	protected ExtensionLink userMenuLink;

	protected List<String> selectedAttributes;
	
	@Override
	protected void createDAOs(DataSource dataSource) throws Exception {

		super.createDAOs(dataSource);

		flowInstanceEventFlowInstanceParamFactory = daoFactory.getFlowInstanceEventDAO().getParamFactory("flowInstance", FlowInstance.class);
		flowInstanceEventPosterParamFactory = daoFactory.getFlowInstanceEventDAO().getParamFactory("poster", User.class);
		flowInstanceEventAddedParamFactory = daoFactory.getFlowInstanceEventDAO().getParamFactory("added", Timestamp.class);
		attributeNameParamFactory = daoFactory.getFlowInstanceAttributeDAO().getParamFactory("name", String.class);

		defaultFlowProcessCallback = new UserFlowInstanceBrowserProcessCallback(FlowBrowserModule.SAVE_ACTION_ID, FlowBrowserModule.SUBMIT_ACTION_ID, FlowBrowserModule.MULTI_SIGNING_ACTION_ID, FlowBrowserModule.PAYMENT_ACTION_ID, this);
		completeFlowProcessCallback = new UserFlowInstanceBrowserProcessCallback(null, SUBMIT_COMPLETION_ACTION_ID, FlowBrowserModule.MULTI_SIGNING_ACTION_ID, null, this);

		externalMessageCRUD = new ExternalMessageCRUD(daoFactory.getExternalMessageDAO(), daoFactory.getExternalMessageAttachmentDAO(), this, false);
	}

	@Override
	public void init(ForegroundModuleDescriptor moduleDescriptor, SectionInterface sectionInterface, DataSource dataSource) throws Exception {

		super.init(moduleDescriptor, sectionInterface, dataSource);

		if (!systemInterface.getInstanceHandler().addInstance(UserFlowInstanceModule.class, this)) {

			throw new RuntimeException("Unable to register module in global instance handler using key " + UserFlowInstanceModule.class.getSimpleName() + ", another instance is already registered using this key.");
		}

		systemLocale = new Locale(systemInterface.getDefaultLanguage().getLanguageCode());
	}

	@Override
	protected void moduleConfigured() throws Exception {

		super.moduleConfigured();

		this.userMenuLink = new ExtensionLink(userMenuTabTitle, getFullAlias(), "\uE60E", userMenuExtensionLinkSlot);

		excludedFlowTypesHashSet = null;

		if (excludedFlowTypes != null) {

			excludedFlowTypesHashSet = new HashSet<Integer>(excludedFlowTypes);
		}

		if (userFlowInstanceMenuModule != null) {

			userFlowInstanceMenuModule.sortProviders();
		}
		
		if(enableExternalID){
			
			ArrayList<String> attributes = new ArrayList<String>(2);
			
			if(enableExternalID){
				
				attributes.add(Constants.FLOW_INSTANCE_EXTERNAL_ID_ATTRIBUTE);
			}
			
			this.selectedAttributes = attributes;
			
		}else{
			
			this.selectedAttributes = null;
		}
	}

	@Override
	public void unload() throws Exception {

		systemInterface.getInstanceHandler().removeInstance(UserFlowInstanceModule.class, this);

		if (notificationHandler != null) {

			notificationHandler.removeNotificationSource(this);
		}

		if (userFlowInstanceMenuModule != null) {

			setUserFlowInstanceMenuModule(null);
		}

		tabExtensionProviders.clear();
		listExtensionLinkProviders.clear();
		listViewFragmentExtensionProviders.clear();
		flowInstanceFilters.clear();
		
		super.unload();
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
	public ForegroundModuleResponse defaultMethod(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Exception, Throwable {

		return this.list(req, res, user, uriParser, (List<ValidationError>) null);
	}

	public ForegroundModuleResponse list(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser, ValidationError validationError) throws ModuleConfigurationException, SQLException {

		return list(req, res, user, uriParser, CollectionUtils.getGenericSingletonList(validationError));
	}

	@Override
	public ForegroundModuleResponse list(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser, List<ValidationError> validationErrors) throws ModuleConfigurationException, SQLException {

		log.info("User " + user + " listing its flow instances");

		Document doc = this.createDocument(req, uriParser, user);

		XMLGeneratorDocument genDoc = new XMLGeneratorDocument(doc);
		genDoc.addIgnoredFields(LIST_EXCLUDED_FIELDS);

		Element listFlowInstancesElement = doc.createElement("ListFlowInstances");

		doc.getDocumentElement().appendChild(listFlowInstancesElement);

		if(enableDescriptionColumn){
			
			XMLUtils.appendNewElement(doc, listFlowInstancesElement, "ShowDescriptionColumn");
		}
		
		if(enableExternalID){
			
			XMLUtils.appendNewElement(doc, listFlowInstancesElement, "ShowExternalID");
		}
		
		List<FlowInstance> flowInstances = getFlowInstances(user, true, excludedFlowTypes != null);

		if (!userFlowInstanceProviders.isEmpty()) {

			int flowInstanceCount = CollectionUtils.getSize(flowInstances);

			for (UserFlowInstanceProvider userFlowInstanceProvider : userFlowInstanceProviders) {

				try {
					flowInstances = CollectionUtils.addAndInstantiateIfNeeded(flowInstances, userFlowInstanceProvider.getUserFlowInstances(user));

				} catch (RuntimeException e) {

					log.error("Error getting flow instances from provider " + userFlowInstanceProvider, e);
				}
			}

			if (flowInstances != null && CollectionUtils.getSize(flowInstances) != flowInstanceCount) {

				Collections.sort(flowInstances, FLOW_INSTANCE_ADDED_COMPARATOR);
			}
		}

		if (!CollectionUtils.isEmpty(flowInstances)) {

			Element savedFlowInstancesElement = XMLUtils.appendNewElement(doc, listFlowInstancesElement, "SavedFlowInstances");
			Element submittedFlowInstancesElement = XMLUtils.appendNewElement(doc, listFlowInstancesElement, "SubmittedFlowInstances");
			Element archivedFlowInstancesElement = XMLUtils.appendNewElement(doc, listFlowInstancesElement, "ArchivedFlowInstances");

			for (FlowInstance flowInstance : flowInstances) {

				if (excludedFlowTypesHashSet != null && excludedFlowTypesHashSet.contains(flowInstance.getFlow().getFlowType().getFlowTypeID())) {
					continue;
				}
				
				if (flowInstance.getFlow().isHideFromUser() && flowInstance.getFirstSubmitted() != null) {
					continue;
				}

				if (!evaluateFlowInstanceFilters(flowInstance)) {
					
					continue;
				}

				Status status = flowInstance.getStatus();

				Element flowInstanceElement = flowInstance.toXML(genDoc);

				if (flowInstance.getFlow().isEnabled()) {

					for (ListFlowInstancesExtensionLinkProvider listExtensionLinkProvider : listExtensionLinkProviders) {

						try {
							ExtensionLink flowInstanceExtensionLink = listExtensionLinkProvider.getListFlowInstancesExtensionLink(flowInstance, req, uriParser, user);

							if (flowInstanceExtensionLink != null) {

								flowInstanceElement.appendChild(flowInstanceExtensionLink.toXML(doc));
							}

						} catch (Exception e) {
							log.error("Error appending extension link for flow instance " + flowInstance + " from FlowInstanceListExtensionProvider " + listExtensionLinkProvider + " for user " + user, e);
						}
					}
				}

				if (status.getContentType() == ContentType.NEW || status.getContentType() == ContentType.WAITING_FOR_MULTISIGN || status.getContentType() == ContentType.WAITING_FOR_PAYMENT) {

					savedFlowInstancesElement.appendChild(flowInstanceElement);

				} else if (status.getContentType() == ContentType.SUBMITTED || status.getContentType() == ContentType.IN_PROGRESS || status.getContentType() == ContentType.WAITING_FOR_COMPLETION) {

					if (showNewEventsInList) {
						
						List<FlowInstanceEvent> events = getNewFlowInstanceEvents(flowInstance, user);
	
						if (events != null) {
	
							for (FlowInstanceEvent event : events) {
								event.setShortDate(DateUtils.getDateWithMonthString(event.getAdded(), systemLocale));
							}
	
							XMLUtils.append(genDoc, flowInstanceElement, "newEvents", events);
						}
					}
					
					submittedFlowInstancesElement.appendChild(flowInstanceElement);

				} else if (status.getContentType() == ContentType.ARCHIVED) {

					archivedFlowInstancesElement.appendChild(flowInstanceElement);

				} else {

					log.warn("The status of flow instance " + flowInstance + " has an unknown content type of " + status.getContentType());
				}

			}

			req.getSession().removeAttribute("LastFlowInstanceEventUpdate");
		}

		if (validationErrors != null) {

			XMLUtils.append(doc, listFlowInstancesElement, validationErrors);
		}

		if (enableSiteProfileSupport && this.profileHandler != null) {

			XMLUtils.append(doc, listFlowInstancesElement, "SiteProfiles", this.profileHandler.getProfiles());
		}

		List<ViewFragment> viewFragments = null;

		if (!listViewFragmentExtensionProviders.isEmpty()) {

			viewFragments = new ArrayList<ViewFragment>(listViewFragmentExtensionProviders.size());

			for (ListFlowInstancesViewFragmentExtensionProvider listViewFragmentExtensionProvider : listViewFragmentExtensionProviders) {

				try {
					ViewFragment viewFragment = listViewFragmentExtensionProvider.getListFlowInstancesViewFragment(flowInstances, enableSiteProfileSupport ? profileHandler : null, req, uriParser, user);

					if (viewFragment != null) {

						listFlowInstancesElement.appendChild(viewFragment.toXML(doc));
						viewFragments.add(viewFragment);
					}

				} catch (Exception e) {
					log.error("Error appending view fragment from ListFlowInstancesViewFragmentExtensionProvider " + listViewFragmentExtensionProvider, e);
				}
			}
		}

		SimpleForegroundModuleResponse moduleResponse = new SimpleForegroundModuleResponse(doc, moduleDescriptor.getName(), this.getDefaultBreadcrumb());

		if (!CollectionUtils.isEmpty(viewFragments)) {
			for (ViewFragment viewFragment : viewFragments) {

				ViewFragmentUtils.appendLinksAndScripts(moduleResponse, viewFragment);
			}
		}

		return moduleResponse;
	}

	private boolean evaluateFlowInstanceFilters(FlowInstance flowInstance) {

		if (CollectionUtils.isEmpty(flowInstanceFilters)) {

			return true;
		}

		for (FlowInstanceFilter filter : flowInstanceFilters) {

			try {

				if (!filter.include(flowInstance)) {

					return false;
				}

			} catch (Throwable t) {

				log.error("Error running evaluateFlowInstance on filter " + filter, t);
			}
		}

		return true;
	}

	@WebPublic(alias = "overview")
	public ForegroundModuleResponse showFlowInstanceOverview(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws ModuleConfigurationException, SQLException, AccessDeniedException, IOException {

		FlowInstance flowInstance;

		if (uriParser.size() == 4 && NumberUtils.isInt(uriParser.get(3)) && (flowInstance = getFlowInstance(Integer.valueOf(uriParser.get(3)), CollectionUtils.getList(ExternalMessageAttachment.DATA_FIELD), FLOW_INSTANCE_OVERVIEW_RELATIONS)) != null) {

			PREVIEW_ACCESS_CONTROLLER.checkFlowInstanceAccess(flowInstance, user);

			SiteProfile instanceProfile = getSiteProfile(flowInstance);

			if (enableSiteProfileRedirectSupport) {

				SiteProfile currentSiteProfile = getCurrentSiteProfile(req, user, uriParser, flowInstance.getFlow().getFlowFamily());

				if (profileRedirect(currentSiteProfile, flowInstance, req, res, uriParser)) {

					return null;
				}
			}

			if (!flowInstance.getFlow().isEnabled() || isOperatingStatusDisabled(flowInstance, false)) {

				return list(req, res, user, uriParser, FLOW_DISABLED_VALIDATION_ERROR);
			}

			log.info("User " + user + " viewing overview of flow instance " + flowInstance);

			Document doc = this.createDocument(req, uriParser, user);

			Element showFlowInstanceOverviewElement = doc.createElement("ShowFlowInstanceOverview");
			doc.getDocumentElement().appendChild(showFlowInstanceOverviewElement);

			XMLUtils.appendNewElement(doc, showFlowInstanceOverviewElement, "FormatedMaxFileSize", BinarySizeFormater.getFormatedSize(maxFileSize * BinarySizes.MegaByte));

			if(enableDescriptionColumn){
				
				XMLUtils.appendNewElement(doc, showFlowInstanceOverviewElement, "ShowDescriptionColumn");
			}
			
			if(enableExternalID){
				
				XMLUtils.appendNewElement(doc, showFlowInstanceOverviewElement, "ShowExternalID");
			}
			
			if(hideManagerEmailInOverview) {
				
				XMLUtils.appendNewElement(doc, showFlowInstanceOverviewElement, "HideManagerEmailInOverview");
			}
			
			if (req.getMethod().equalsIgnoreCase("POST")) {
				
				if (!flowInstance.getFlow().isHideExternalMessages()) {
					
					//TODO append message or request parameters
					ExternalMessage externalMessage = externalMessageCRUD.add(req, res, uriParser, user, doc, showFlowInstanceOverviewElement, flowInstance, false);
					
					if (externalMessage != null) {
						
						FlowInstanceEvent flowInstanceEvent = flowInstanceEventGenerator.addFlowInstanceEvent(flowInstance, EventType.CUSTOMER_MESSAGE_SENT, null, user, null, ExternalMessageUtils.getFlowInstanceEventAttributes(externalMessage));
						
						systemInterface.getEventHandler().sendEvent(FlowInstance.class, new ExternalMessageAddedEvent(flowInstance, flowInstanceEvent, instanceProfile, externalMessage, SenderType.USER), EventTarget.ALL);
						
						res.sendRedirect(req.getContextPath() + uriParser.getFormattedURI() + "#messages");
						
						return null;
					}
				}
			}

			req.setAttribute(UserFlowInstanceMenuModule.REQUEST_DISABLE_MENU, true);
			
			if(flowInstance.getEvents() != null && flowInstanceEventSortOrder == Order.DESC){
				
				Collections.reverse(flowInstance.getEvents());
			}

			appendFlowInstanceOverviewElement(doc, showFlowInstanceOverviewElement, flowInstance);

			if (enableSiteProfileSupport && flowInstance.getProfileID() != null && this.profileHandler != null) {

				XMLUtils.append(doc, showFlowInstanceOverviewElement, profileHandler.getProfile(flowInstance.getProfileID()));
			}

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
	public ForegroundModuleResponse showFlowInstanceMessages(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws ModuleConfigurationException, SQLException, AccessDeniedException, IOException {

		if (uriParser.size() == 4 && NumberUtils.isInt(uriParser.get(2)) && NumberUtils.isInt(uriParser.get(3))) {

			redirectToMethod(req, res, "/overview/" + uriParser.get(2) + "/" + uriParser.get(3) + "#messages");

			return null;
		}

		return list(req, res, user, uriParser, FLOW_INSTANCE_NOT_FOUND_VALIDATION_ERROR);
	}

	protected void appendFlowInstanceOverviewElement(Document doc, Element showFlowInstanceOverviewElement, FlowInstance flowInstance) {

		showFlowInstanceOverviewElement.appendChild(flowInstance.toXML(doc));

	}

	protected boolean profileRedirect(SiteProfile profile, FlowInstance flowInstance, HttpServletRequest req, HttpServletResponse res, URIParser uriParser) throws IOException {

		if (flowInstance.getProfileID() != null && this.profileHandler != null) {

			if (profile == null || !profile.getProfileID().equals(flowInstance.getProfileID())) {

				SiteProfile targetProfile = this.profileHandler.getProfile(flowInstance.getProfileID());

				if (targetProfile != null && targetProfile.getDomains() != null) {

					res.sendRedirect(req.getScheme() + "://" + targetProfile.getDomains().get(0) + req.getContextPath() + uriParser.getFormattedURI());

					return true;
				}
			}
		}
		return false;
	}

	protected List<ViewFragment> appendOverviewData(Document doc, Element showFlowInstanceOverviewElement, FlowInstance flowInstance, HttpServletRequest req, User user, URIParser uriParser) throws SQLException {

		if (tabExtensionProviders.isEmpty()) {
			return null;
		}

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

	@WebPublic(alias = "flowinstance")
	public ForegroundModuleResponse processFlowRequest(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws URINotFoundException, AccessDeniedException, ModuleConfigurationException, SQLException, IOException, FlowDefaultStatusNotFound, EvaluationException, SubmitCheckException {

		Integer flowID = null;
		Integer flowInstanceID = null;
		MutableFlowInstanceManager instanceManager;

		try {
			if (uriParser.size() == 4 && (flowID = NumberUtils.toInt(uriParser.get(2))) != null && (flowInstanceID = NumberUtils.toInt(uriParser.get(3))) != null) {

				if (enableSiteProfileRedirectSupport) {

					if (flowInstanceID != null) {

						FlowInstance flowInstance = this.getFlowInstance(flowInstanceID, null, (Field) null);

						if (flowInstance != null && profileHandler != null) {

							SiteProfile profile = profileHandler.getCurrentProfile(user, req, uriParser);

							if (profileRedirect(profile, flowInstance, req, res, uriParser)) {

								return null;
							}
						}
					}
				}

				//Get saved instance from DB or session
				instanceManager = getSavedMutableFlowInstanceManager(flowID, flowInstanceID, UPDATE_ACCESS_CONTROLLER, req.getSession(true), user, uriParser, req, true, false, true, DEFAULT_REQUEST_METADATA);

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

			req.setAttribute(UserFlowInstanceMenuModule.REQUEST_DISABLE_MENU, true);

			if (instanceManager.getFlowInstance().getFirstSubmitted() != null) {

				return processFlowRequest(instanceManager, completeFlowProcessCallback, UPDATE_ACCESS_CONTROLLER, req, res, user, user, uriParser, true, DEFAULT_REQUEST_METADATA);
			}

			return processFlowRequest(instanceManager, defaultFlowProcessCallback, UPDATE_ACCESS_CONTROLLER, req, res, user, user, uriParser, true, DEFAULT_REQUEST_METADATA);

		} catch (FlowInstanceManagerClosedException e) {

			log.info("User " + user + " requested flow instance manager for flow instance " + e.getFlowInstance() + " which has already been closed. Removing flow instance manager from session.");

			removeMutableFlowInstanceManagerFromSession(instanceManager, req.getSession(false));

			redirectToMethod(req, res, "/flowinstance/" + flowID + "/" + flowInstanceID);

			return null;

		} catch (QueryInstanceHTMLException e) {

			return processFlowRequestException(instanceManager, req, res, user, user, uriParser, e);

		} catch (RuntimeException e) {

			return processFlowRequestException(instanceManager, req, res, user, user, uriParser, e);
		}
	}

	@WebPublic(alias = "delete")
	public ForegroundModuleResponse deleteFlowInstance(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws URINotFoundException, SQLException, AccessDeniedException, IOException {

		Integer flowInstanceID = null;

		if (uriParser.size() == 3 && (flowInstanceID = NumberUtils.toInt(uriParser.get(2))) != null && deleteFlowInstance(flowInstanceID, DELETE_ACCESS_CONTROLLER, user) != null) {

			this.redirectToDefaultMethod(req, res);

			return null;
		}

		throw new URINotFoundException(uriParser);
	}

	@WebPublic(alias = "externalattachment")
	public ForegroundModuleResponse getExternalMessageAttachment(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws ModuleConfigurationException, SQLException, AccessDeniedException, IOException, FlowDefaultStatusNotFound, EvaluationException, URINotFoundException {

		return externalMessageCRUD.getRequestedMessageAttachment(req, res, user, uriParser, PREVIEW_ACCESS_CONTROLLER);
	}

	@WebPublic(alias = "mquery")
	public ForegroundModuleResponse processMutableQueryRequest(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws ModuleConfigurationException, SQLException, AccessDeniedException, IOException, FlowDefaultStatusNotFound, EvaluationException, URINotFoundException, QueryRequestException, QueryProviderException, EvaluationProviderException, InvalidFlowInstanceStepException, MissingQueryInstanceDescriptor, DuplicateFlowInstanceManagerIDException, UnableToResetQueryInstanceException {

		return processMutableQueryRequest(req, res, user, user, uriParser, UPDATE_ACCESS_CONTROLLER, true, true, false, DEFAULT_REQUEST_METADATA);
	}

	@WebPublic(alias = "iquery")
	public ForegroundModuleResponse processImmutableQueryRequest(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws ModuleConfigurationException, SQLException, AccessDeniedException, IOException, FlowDefaultStatusNotFound, EvaluationException, URINotFoundException, QueryRequestException, QueryProviderException, EvaluationProviderException, InvalidFlowInstanceStepException, MissingQueryInstanceDescriptor, DuplicateFlowInstanceManagerIDException {

		return processImmutableQueryRequest(req, res, user, uriParser, PREVIEW_ACCESS_CONTROLLER, true, false);
	}

	@Override
	public Breadcrumb getFlowBreadcrumb(ImmutableFlowInstance flowInstance) {

		return new Breadcrumb(this, flowInstance.getFlow().getName(), "/flowinstance/" + flowInstance.getFlow().getFlowID() + "/" + flowInstance.getFlowInstanceID());
	}

	public List<FlowInstance> getFlowInstances(User user, boolean getEvents, boolean getFlowTypes) throws SQLException {

		ArrayListQuery<Integer> flowInstanceIDQuery = new ArrayListQuery<Integer>(daoFactory.getFlowInstanceDAO().getDataSource(), "SELECT flowInstanceID FROM flowengine_flow_instance_owners WHERE userID = ?", IntegerPopulator.getPopulator());
		flowInstanceIDQuery.setInt(1, user.getUserID());

		List<Integer> flowInstanceIDs = flowInstanceIDQuery.executeQuery();

		if (flowInstanceIDs == null) {

			return null;
		}

		HighLevelQuery<FlowInstance> query = new HighLevelQuery<FlowInstance>();

		addListRelations(query);

		query.addExcludedFields(LIST_EXCLUDED_FIELDS);

		if (getEvents) {

			query.addRelation(FlowInstance.EVENTS_RELATION);
		}

		if (getFlowTypes) {

			query.addRelation(Flow.FLOW_TYPE_RELATION);
		}

		query.addParameter(flowInstanceIDParamFactory.getWhereInParameter(flowInstanceIDs));

		return daoFactory.getFlowInstanceDAO().getAll(query);
	}

	protected void addListRelations(HighLevelQuery<FlowInstance> query) {

		if (selectedAttributes != null) {

			query.addRelations(FlowInstance.FLOW_RELATION, FlowInstance.STATUS_RELATION, FlowInstance.ATTRIBUTES_RELATION);

			query.addRelationParameter(FlowInstanceAttribute.class, attributeNameParamFactory.getWhereInParameter(selectedAttributes));

		} else {

			query.addRelations(FlowInstance.FLOW_RELATION, FlowInstance.STATUS_RELATION);
		}
	}

	public List<FlowInstanceEvent> getNewFlowInstanceEvents(FlowInstance flowInstance, User user) throws SQLException {

		HighLevelQuery<FlowInstanceEvent> query = new HighLevelQuery<FlowInstanceEvent>();

		query.addParameter(flowInstanceEventFlowInstanceParamFactory.getParameter(flowInstance));
		query.addParameter(flowInstanceEventPosterParamFactory.getParameter(user, QueryOperators.NOT_EQUALS));

		if (user.getLastLogin() != null) {
			query.addParameter(flowInstanceEventAddedParamFactory.getParameter(user.getLastLogin(), QueryOperators.BIGGER_THAN));
		}

		return daoFactory.getFlowInstanceEventDAO().getAll(query);
	}

	@WebPublic(alias = "submitted")
	public ForegroundModuleResponse showSubmittedMessage(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws FlowInstanceManagerClosedException, UnableToGetQueryInstanceShowHTMLException, AccessDeniedException, ModuleConfigurationException, SQLException, URINotFoundException {

		try {
			return super.showImmutableFlowInstance(req, res, user, uriParser, new SessionAccessController(req.getSession(), SESSION_ACCESS_CONTROLLER_TAG), this.defaultFlowProcessCallback, ShowMode.SUBMIT, DEFAULT_REQUEST_METADATA);

		} catch (AccessDeniedException e) {

			throw new URINotFoundException(uriParser);
		}
	}

	@WebPublic(alias = "multisign")
	public ForegroundModuleResponse showMultiSignMessage(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws FlowInstanceManagerClosedException, UnableToGetQueryInstanceShowHTMLException, AccessDeniedException, ModuleConfigurationException, SQLException, URINotFoundException {

		return super.showMultiSignMessage(req, res, user, uriParser, PREVIEW_ACCESS_CONTROLLER, this.defaultFlowProcessCallback, false);
	}

	@WebPublic(alias = "pay")
	public ForegroundModuleResponse showPaymentForm(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws FlowInstanceManagerClosedException, UnableToGetQueryInstanceShowHTMLException, AccessDeniedException, ModuleConfigurationException, SQLException, URINotFoundException, IOException {

		return super.showPaymentForm(req, res, user, uriParser, PREVIEW_ACCESS_CONTROLLER, this.defaultFlowProcessCallback, false);
	}

	@Override
	protected void redirectToSubmitMethod(MutableFlowInstanceManager flowInstanceManager, HttpServletRequest req, HttpServletResponse res) throws IOException {

		HttpSession session = req.getSession();

		if (session != null) {

			try {
				SessionAccessController.setSessionAttribute(flowInstanceManager.getFlowInstanceID(), session, SESSION_ACCESS_CONTROLLER_TAG);
			} catch (IllegalStateException e) {}
		}

		redirectToMethod(req, res, "/submitted/" + flowInstanceManager.getFlowInstanceID());
	}

	@Override
	protected void flowInstanceSavedAndClosed(FlowInstanceManager flowInstanceManager, HttpServletRequest req, HttpServletResponse res, User user, FlowInstanceEvent event) throws IOException {

		redirectToMethod(req, res, "/overview/" + flowInstanceManager.getFlowID() + "/" + flowInstanceManager.getFlowInstanceID());

	}

	@WebPublic(alias = "preview")
	public ForegroundModuleResponse showPreview(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws FlowInstanceManagerClosedException, UnableToGetQueryInstanceShowHTMLException, AccessDeniedException, ModuleConfigurationException, SQLException, IOException {

		if (enableSiteProfileRedirectSupport) {

			Integer flowInstanceID = NumberUtils.toInt(uriParser.get(2));

			if (flowInstanceID != null) {

				FlowInstance flowInstance = this.getFlowInstance(flowInstanceID, null, (Field) null);

				if (flowInstance != null && profileHandler != null) {

					SiteProfile profile = profileHandler.getCurrentProfile(user, req, uriParser);

					if (profileRedirect(profile, flowInstance, req, res, uriParser)) {

						return null;
					}
				}
			}
		}

		req.setAttribute(UserFlowInstanceMenuModule.REQUEST_DISABLE_MENU, true);

		return super.showImmutableFlowInstance(req, res, user, uriParser, PREVIEW_ACCESS_CONTROLLER, defaultFlowProcessCallback, ShowMode.PREVIEW, DEFAULT_REQUEST_METADATA);
	}

	@Override
	protected Breadcrumb getFlowInstancePreviewBreadcrumb(ImmutableFlowInstance flowInstance, HttpServletRequest req, URIParser uriParser) {

		return new Breadcrumb(this, flowInstance.getFlow().getName(), "/preview/" + flowInstance.getFlowInstanceID());
	}

	@Override
	protected Breadcrumb getFlowInstanceSubmitBreadcrumb(ImmutableFlowInstance flowInstance, HttpServletRequest req, URIParser uriParser) {

		return new Breadcrumb(this, flowInstance.getFlow().getName(), "/submit/" + flowInstance.getFlowInstanceID());
	}

	@WebPublic(alias = "pdf")
	public ForegroundModuleResponse getEventPDF(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws URINotFoundException, SQLException, IOException, AccessDeniedException, ModuleConfigurationException {

		try {
			sendEventPDF(req, res, user, uriParser, PREVIEW_ACCESS_CONTROLLER, pdfProvider, false);

		} catch (FlowDisabledException e) {

			return list(req, res, user, uriParser, FLOW_DISABLED_VALIDATION_ERROR);
		}

		return null;
	}

	@WebPublic(alias = "xml")
	public ForegroundModuleResponse getEventXML(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws URINotFoundException, SQLException, IOException, AccessDeniedException, ModuleConfigurationException {

		try {
			sendEventXML(req, res, user, uriParser, PREVIEW_ACCESS_CONTROLLER, xmlProvider, false);

		} catch (FlowDisabledException e) {

			return list(req, res, user, uriParser, FLOW_DISABLED_VALIDATION_ERROR);
		}

		return null;
	}

	@WebPublic(alias = "signature")
	public ForegroundModuleResponse getEventSignature(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws URINotFoundException, SQLException, IOException, AccessDeniedException, ModuleConfigurationException {

		try {
			sendEventSignature(req, res, user, uriParser, PREVIEW_ACCESS_CONTROLLER, false);

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
	protected SigningProvider getSigningProvider() {

		return signingProvider;
	}

	@Override
	protected MultiSigningHandler getMultiSigningHandler() {

		return multiSigningHandler;
	}

	@Override
	protected FlowPaymentProvider getFlowPaymentProvider() {

		return paymentProvider;
	}

	@Override
	public String getSignFailURL(MutableFlowInstanceManager instanceManager, HttpServletRequest req) {

		String preview = instanceManager.getFlowInstance().getFlow().usesPreview() ? "&preview=1" : "";

		return RequestUtils.getFullContextPathURL(req) + this.getFullAlias() + "/flowinstance/" + instanceManager.getFlowID() + "/" + instanceManager.getFlowInstanceID() + "?signprovidererror=1" + preview;
	}
	
	@Override
	public String getStandalonePaymentURL(FlowInstanceManager instanceManager, HttpServletRequest req) {

		return RequestUtils.getFullContextPathURL(req) + this.getFullAlias() + "/pay/" + instanceManager.getFlowInstanceID();
	}

	@Override
	public String getPaymentFailURL(MutableFlowInstanceManager instanceManager, HttpServletRequest req) {

		String preview = instanceManager.getFlowInstance().getFlow().usesPreview() ? "&preview=1" : "";

		return RequestUtils.getFullContextPathURL(req) + this.getFullAlias() + "/flowinstance/" + instanceManager.getFlowID() + "/" + instanceManager.getFlowInstanceID() + "?paymentprovidererror=1" + preview;
	}

	@Override
	public String getSaveAndSubmitURL(MutableFlowInstanceManager instanceManager, HttpServletRequest req) {

		return RequestUtils.getFullContextPathURL(req) + this.getFullAlias() + "/flowinstance/" + instanceManager.getFlowID() + "/" + instanceManager.getFlowInstanceID() + "?save-submit=1&nopost=1";
	}

	@Override
	public void signingComplete(MutableFlowInstanceManager instanceManager, FlowInstanceEvent event, HttpServletRequest req, SiteProfile siteProfile, String actionID) throws FlowInstanceManagerClosedException, UnableToSaveQueryInstanceException, FlowDefaultStatusNotFound, SQLException {

		super.signingComplete(instanceManager, event, req, siteProfile, actionID);

		SessionAccessController.setSessionAttribute(instanceManager.getFlowInstanceID(), req.getSession(), SESSION_ACCESS_CONTROLLER_TAG);
	}

	@Override
	public String getPaymentSuccessURL(FlowInstanceManager instanceManager, HttpServletRequest req) {

		SessionAccessController.setSessionAttribute(instanceManager.getFlowInstanceID(), req.getSession(), SESSION_ACCESS_CONTROLLER_TAG);

		return super.getPaymentSuccessURL(instanceManager, req);
	}

	@Override
	protected String getEventPDFLink(FlowInstanceManager instanceManager, ImmutableFlowInstanceEvent event, HttpServletRequest req, User user) {

		if (event.getAttributeHandler().getPrimitiveBoolean("pdf")) {

			return this.getModuleURI(req) + "/pdf/" + instanceManager.getFlowInstanceID() + "/" + event.getEventID();
		}

		return null;
	}

	@Override
	public String getAbsoluteFileURL(URIParser uriParser, Object bean) {

		if (ckConnectorModuleAlias != null) {

			return uriParser.getContextPath() + ckConnectorModuleAlias;
		}

		return null;
	}

	public boolean addFlowInstanceOverviewExtensionProvider(FlowInstanceOverviewExtensionProvider provider) {

		return tabExtensionProviders.add(provider);
	}

	public boolean removeFlowInstanceOverviewExtensionProvider(FlowInstanceOverviewExtensionProvider provider) {

		return tabExtensionProviders.remove(provider);
	}

	public boolean addListFlowInstancesExtensionLinkProvider(ListFlowInstancesExtensionLinkProvider provider) {

		return listExtensionLinkProviders.add(provider);
	}

	public boolean removeListFlowInstancesExtensionLinkProvider(ListFlowInstancesExtensionLinkProvider provider) {

		return listExtensionLinkProviders.remove(provider);
	}
	
	public boolean addListFlowInstancesViewFragmentExtensionProvider(ListFlowInstancesViewFragmentExtensionProvider provider) {

		return listViewFragmentExtensionProviders.add(provider);
	}

	public boolean removeListFlowInstancesViewFragmentExtensionProvider(ListFlowInstancesViewFragmentExtensionProvider provider) {

		return listViewFragmentExtensionProviders.remove(provider);
	}

	/**
	 * @param flowInstanceID
	 * @param accessController
	 * @param user
	 * @return The deleted flow instance or null if the flow instance could not be found.
	 * @throws URINotFoundException
	 * @throws SQLException
	 * @throws AccessDeniedException
	 * @throws IOException
	 */
	public FlowInstance deleteFlowInstance(Integer flowInstanceID, FlowInstanceAccessController accessController, User user) throws SQLException, AccessDeniedException {

		HighLevelQuery<FlowInstance> query = new HighLevelQuery<FlowInstance>();

		query.addRelations(FlowInstance.OWNERS_RELATION, FlowInstance.STATUS_RELATION, FlowInstance.FLOW_RELATION, Flow.STEPS_RELATION, Flow.FLOW_TYPE_RELATION, FlowType.ALLOWED_ADMIN_GROUPS_RELATION, FlowType.ALLOWED_ADMIN_USERS_RELATION, Step.QUERY_DESCRIPTORS_RELATION, QueryDescriptor.QUERY_INSTANCE_DESCRIPTORS_RELATION);
		query.addParameter(flowInstanceIDParamFactory.getParameter(flowInstanceID));
		query.addRelationParameter(QueryInstanceDescriptor.class, queryInstanceDescriptorFlowInstanceIDParamFactory.getParameter(flowInstanceID));

		FlowInstance flowInstance = daoFactory.getFlowInstanceDAO().get(query);

		if (flowInstance == null) {

			return null;
		}

		accessController.checkFlowInstanceAccess(flowInstance, user);

		log.info("User " + user + " deleting flow instance " + flowInstance);

		TransactionHandler transactionHandler = null;

		try {
			transactionHandler = daoFactory.getFlowInstanceDAO().createTransaction();

			daoFactory.getFlowInstanceDAO().delete(flowInstance, transactionHandler);

			if (flowInstance.getFlow().getSteps() != null) {

				for (Step step : flowInstance.getFlow().getSteps()) {

					if (step.getQueryDescriptors() != null) {

						for (QueryDescriptor queryDescriptor : step.getQueryDescriptors()) {

							if (queryDescriptor.getQueryInstanceDescriptors() != null) {

								QueryInstanceDescriptor instanceDescriptor = queryDescriptor.getQueryInstanceDescriptors().get(0);

								instanceDescriptor.setQueryDescriptor(queryDescriptor);

								try {
									queryHandler.deleteQueryInstance(queryDescriptor.getQueryInstanceDescriptors().get(0), transactionHandler);

								} catch (Exception e) {

									log.error("Error deleting query instance " + queryDescriptor.getQueryInstanceDescriptors().get(0), e);
								}
							}
						}
					}
				}
			}

			transactionHandler.commit();

		} finally {

			TransactionHandler.autoClose(transactionHandler);
		}

		daoFactory.getFlowInstanceDAO().delete(flowInstance);

		int closedCount = FlowInstanceManagerRegistery.getInstance().closeInstances(flowInstanceID, queryHandler);

		if (closedCount > 0) {
			log.info("Closed " + closedCount + " flow instance managers for flow instance " + flowInstance);
		}

		this.eventHandler.sendEvent(FlowInstance.class, new CRUDEvent<FlowInstance>(CRUDAction.DELETE, flowInstance), EventTarget.ALL);

		return flowInstance;
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

		if (uriParser.size() == 5 && NumberUtils.isInt(uriParser.get(3)) && (flowInstance = getFlowInstance(Integer.valueOf(uriParser.get(3)), CollectionUtils.getList(ExternalMessageAttachment.DATA_FIELD), FLOW_INSTANCE_OVERVIEW_RELATIONS)) != null) {

			PREVIEW_ACCESS_CONTROLLER.checkFlowInstanceAccess(flowInstance, user);

			if (!flowInstance.getFlow().isEnabled() || isOperatingStatusDisabled(flowInstance, false)) {

				return list(req, res, user, uriParser, FLOW_DISABLED_VALIDATION_ERROR);
			}

			FlowInstanceOverviewExtensionProvider extensionProvider = getOverviewExtensionProvider(uriParser.get(4));

			if (extensionProvider != null) {

				return extensionProvider.processOverviewExtensionRequest(flowInstance, req, res, uriParser, user);
			}
		}

		throw new URINotFoundException(uriParser);
	}

	public String getOverviewExtensionRequestMethodAlias(FlowInstance flowInstance, String providerID) {

		return getFullAlias() + "/" + "overviewextensionrequest" + "/" + flowInstance.getFlow().getFlowID() + "/" + flowInstance.getFlowInstanceID() + "/" + providerID;
	}

	public boolean addFlowInstanceProvider(UserFlowInstanceProvider flowInstanceProvider) {

		return userFlowInstanceProviders.add(flowInstanceProvider);
	}

	public boolean removeFlowInstanceProvider(UserFlowInstanceProvider flowInstanceProvider) {

		return userFlowInstanceProviders.remove(flowInstanceProvider);
	}

	public boolean addFlowInstanceListFilter(FlowInstanceFilter flowInstanceFilter) {

		return flowInstanceFilters.add(flowInstanceFilter);
	}

	public boolean removeFlowInstanceListFilter(FlowInstanceFilter flowInstanceFilter) {

		return flowInstanceFilters.remove(flowInstanceFilter);
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

		if (source == EventSource.LOCAL && event.getSenderType().equals(SenderType.MANAGER) && notificationHandler != null && !event.getFlowInstance().getFlow().isHideFromUser()) {

			try {
				notificationHandler.sendNotificationToFlowInstanceOwners(this, event.getFlowInstance().getFlowInstanceID(), notificationExternalMessage, event.getEvent().getPoster(), "message", null);

			} catch (SQLException e) {

				log.error("Error sending notifications for " + event.getExternalMessage() + " to owners of " + event.getFlowInstance(), e);
			}
		}
	}

	@se.unlogic.hierarchy.core.annotations.EventListener(channel = FlowInstance.class)
	public void processEvent(StatusChangedByManagerEvent event, EventSource source) {

		log.debug("Received status changed by manager event regarding " + event.getFlowInstance());

		if (source == EventSource.LOCAL && notificationHandler != null && !event.getFlowInstance().getFlow().isHideFromUser()) {

			try {
				notificationHandler.sendNotificationToFlowInstanceOwners(this, event.getFlowInstance().getFlowInstanceID(), eventStatusUpdated + ": " + event.getFlowInstance().getStatus().getName(), event.getUser(), "changedStatus", null);

			} catch (SQLException e) {

				log.error("Error sending notifications for " + event.getEvent() + " to owners of " + event.getFlowInstance(), e);
			}
		}
	}
	
	//TODO add notification for SubmitEvent if multi signing was used
	
	@se.unlogic.hierarchy.core.annotations.EventListener(channel = FlowInstance.class)
	public void processEvent(OwnersChangedEvent event, EventSource source) {

		log.debug("Received owners changed event regarding " + event.getFlowInstance());

		if (source == EventSource.LOCAL && notificationHandler != null) {

			if (!CollectionUtils.isEmpty(event.getPreviousOwners())) {
				for (User owner : event.getPreviousOwners()) {

					if (event.getFlowInstance().getOwners() == null || !event.getFlowInstance().getOwners().contains(owner)) {

						try {
							notificationHandler.deleteNotifications(moduleDescriptor.getModuleID(), event.getFlowInstance().getFlowInstanceID(), owner, null);

						} catch (SQLException e) {

							log.error("Error deleting notificatios for old owner " + owner + " of " + event.getFlowInstance(), e);
						}
					}
				}
			}
		}
	}

	@Override
	public NotificationMetadata getNotificationMetadata(Notification notification, FlowInstance flowInstance, String fullContextPath) throws Exception {

		NotificationMetadata metadata = new NotificationMetadata();
		metadata.setShowURL(fullContextPath + getFullAlias() + "/overview/" + flowInstance.getFlow().getFlowID() + "/" + notification.getFlowInstanceID());

		String type = notification.getNotificationType();

		if ("message".equals(type)) {

			metadata.setUrl(fullContextPath + getFullAlias() + "/overview/" + flowInstance.getFlow().getFlowID() + "/" + notification.getFlowInstanceID() + "#messages");

		} else {

			metadata.setUrl(fullContextPath + getFullAlias() + "/overview/" + flowInstance.getFlow().getFlowID() + "/" + notification.getFlowInstanceID());
		}

		if (!flowInstance.getFlow().hidesManagerDetails()) {

			if(notification.getExternalNotificationID() != null){
				
				metadata.setPoster(systemInterface.getUserHandler().getUser(notification.getExternalNotificationID(), false, true));
			}

		} else {

			SimpleUser hiddenManager = new SimpleUser();
			hiddenManager.setFirstname(notificationPostedByManager);

			metadata.setPoster(hiddenManager);
		}

		return metadata;
	}

	@Override
	public ModuleDescriptor getModuleDescriptor() {

		return moduleDescriptor;
	}

	@Override
	protected Icon getFlowTypeIcon(Integer flowTypeID) throws SQLException {

		return getBareFlowType(flowTypeID);
	}

	@Override
	protected Icon getFlowIcon(Integer flowID) throws SQLException {

		return FlowIconUtils.getFlowIcon(getBareFlow(flowID));
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
}
