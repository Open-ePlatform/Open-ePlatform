package com.nordicpeak.flowengine;

import java.io.IOException;
import java.lang.reflect.Field;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
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

import se.unlogic.hierarchy.core.annotations.InstanceManagerDependency;
import se.unlogic.hierarchy.core.annotations.ModuleSetting;
import se.unlogic.hierarchy.core.annotations.TextAreaSettingDescriptor;
import se.unlogic.hierarchy.core.annotations.TextFieldSettingDescriptor;
import se.unlogic.hierarchy.core.annotations.WebPublic;
import se.unlogic.hierarchy.core.beans.Breadcrumb;
import se.unlogic.hierarchy.core.beans.SimpleForegroundModuleResponse;
import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.hierarchy.core.enums.CRUDAction;
import se.unlogic.hierarchy.core.enums.EventTarget;
import se.unlogic.hierarchy.core.events.CRUDEvent;
import se.unlogic.hierarchy.core.exceptions.AccessDeniedException;
import se.unlogic.hierarchy.core.exceptions.ModuleConfigurationException;
import se.unlogic.hierarchy.core.exceptions.URINotFoundException;
import se.unlogic.hierarchy.core.interfaces.ForegroundModuleDescriptor;
import se.unlogic.hierarchy.core.interfaces.ForegroundModuleResponse;
import se.unlogic.hierarchy.core.interfaces.SectionInterface;
import se.unlogic.hierarchy.core.interfaces.ViewFragment;
import se.unlogic.hierarchy.core.utils.ViewFragmentUtils;
import se.unlogic.hierarchy.core.utils.extensionlinks.ExtensionLink;
import se.unlogic.openhierarchy.foregroundmodules.siteprofile.interfaces.SiteProfile;
import se.unlogic.standardutils.collections.CollectionUtils;
import se.unlogic.standardutils.dao.HighLevelQuery;
import se.unlogic.standardutils.dao.LowLevelQuery;
import se.unlogic.standardutils.dao.QueryOperators;
import se.unlogic.standardutils.dao.QueryParameterFactory;
import se.unlogic.standardutils.dao.TransactionHandler;
import se.unlogic.standardutils.dao.querys.ArrayListQuery;
import se.unlogic.standardutils.date.DateUtils;
import se.unlogic.standardutils.io.BinarySizeFormater;
import se.unlogic.standardutils.io.BinarySizes;
import se.unlogic.standardutils.numbers.NumberUtils;
import se.unlogic.standardutils.populators.IntegerPopulator;
import se.unlogic.standardutils.string.StringUtils;
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
import com.nordicpeak.flowengine.beans.FlowInstanceEvent;
import com.nordicpeak.flowengine.beans.FlowType;
import com.nordicpeak.flowengine.beans.QueryDescriptor;
import com.nordicpeak.flowengine.beans.QueryInstanceDescriptor;
import com.nordicpeak.flowengine.beans.Status;
import com.nordicpeak.flowengine.beans.Step;
import com.nordicpeak.flowengine.beans.UserFlowInstanceBrowserProcessCallback;
import com.nordicpeak.flowengine.cruds.ExternalMessageCRUD;
import com.nordicpeak.flowengine.enums.ContentType;
import com.nordicpeak.flowengine.enums.EventType;
import com.nordicpeak.flowengine.enums.SenderType;
import com.nordicpeak.flowengine.enums.ShowMode;
import com.nordicpeak.flowengine.events.ExternalMessageAddedEvent;
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
import com.nordicpeak.flowengine.exceptions.queryinstance.UnableToSaveQueryInstanceException;
import com.nordicpeak.flowengine.exceptions.queryprovider.QueryProviderException;
import com.nordicpeak.flowengine.interfaces.FlowInstanceAccessController;
import com.nordicpeak.flowengine.interfaces.FlowInstanceOverviewExtensionProvider;
import com.nordicpeak.flowengine.interfaces.FlowProcessCallback;
import com.nordicpeak.flowengine.interfaces.ImmutableFlowInstance;
import com.nordicpeak.flowengine.interfaces.ImmutableFlowInstanceEvent;
import com.nordicpeak.flowengine.interfaces.MessageCRUDCallback;
import com.nordicpeak.flowengine.interfaces.MultiSigningHandler;
import com.nordicpeak.flowengine.interfaces.MultiSigningQueryProvider;
import com.nordicpeak.flowengine.interfaces.PDFProvider;
import com.nordicpeak.flowengine.interfaces.PaymentProvider;
import com.nordicpeak.flowengine.interfaces.SigningProvider;
import com.nordicpeak.flowengine.interfaces.XMLProvider;
import com.nordicpeak.flowengine.managers.FlowInstanceManager;
import com.nordicpeak.flowengine.managers.MutableFlowInstanceManager;
import com.nordicpeak.flowengine.managers.MutableFlowInstanceManager.FlowInstanceManagerRegistery;

public class UserFlowInstanceModule extends BaseFlowBrowserModule implements MessageCRUDCallback{

	protected static final Field [] FLOW_INSTANCE_OVERVIEW_RELATIONS = { FlowInstance.OWNERS_RELATION, FlowInstance.EXTERNAL_MESSAGES_RELATION, ExternalMessage.ATTACHMENTS_RELATION, FlowInstance.FLOW_RELATION, FlowInstance.FLOW_STATE_RELATION, FlowInstance.EVENTS_RELATION, FlowInstanceEvent.ATTRIBUTES_RELATION, FlowInstance.MANAGERS_RELATION, Flow.FLOW_FAMILY_RELATION, FlowInstance.ATTRIBUTES_RELATION};
	protected static final Field [] LIST_EXCLUDED_FIELDS = { FlowInstance.POSTER_FIELD, FlowInstance.EDITOR_FIELD, Flow.ICON_FILE_NAME_FIELD, Flow.DESCRIPTION_SHORT_FIELD, Flow.DESCRIPTION_LONG_FIELD, Flow.SUBMITTED_MESSAGE_FIELD, Flow.HIDE_INTERNAL_MESSAGES_FIELD, Flow.HIDE_FROM_OVERVIEW_FIELD , Flow.HIDE_MANAGER_DETAILS_FIELD , Flow.HAS_PDF_FIELD , Flow.HIDE_SUBMIT_STEP_TEXT_FIELD , Flow.SHOW_SUBMIT_SURVEY_FIELD , Flow.REQUIRES_SIGNING_FIELD , Flow.REQUIRE_AUTHENTICATION_FIELD , Flow.USE_PREVIEW_FIELD , Flow.PUBLISH_DATE_FIELD , FlowInstanceEvent.POSTER_FIELD};
	
	public static final String SESSION_ACCESS_CONTROLLER_TAG = UserFlowInstanceModule.class.getName();

	public static final String SUBMIT_COMPLETION_ACTION_ID = UserFlowInstanceModule.class.getName() + ".submitcompletion";

	public static final UserFlowInstanceAccessController UPDATE_ACCESS_CONTROLLER = new UserFlowInstanceAccessController(true, false);
	public static final UserFlowInstanceAccessController DELETE_ACCESS_CONTROLLER = new UserFlowInstanceAccessController(false, true);
	public static final UserFlowInstanceAccessController PREVIEW_ACCESS_CONTROLLER = new UserFlowInstanceAccessController(false, false);

	@ModuleSetting(allowsNull = true)
	@TextFieldSettingDescriptor(name="CKEditor connector module alias", description="The full alias of the CKEditor connector module (relative from the contextpath). Leave empty if you do not want to activate file manager for CKEditor")
	protected String ckConnectorModuleAlias;
	
	@ModuleSetting
	@TextFieldSettingDescriptor(name = "Max file size", description = "Maxmium allowed file size in megabytes", required = true, formatValidator = PositiveStringIntegerValidator.class)
	protected Integer maxFileSize = 50;
	
	@ModuleSetting(allowsNull = true)
	@TextAreaSettingDescriptor(name = "Excluded flow types", description = "Flow instances from these flow types will be excluded", formatValidator = NonNegativeStringIntegerValidator.class)
	protected List<Integer> excludedFlowTypes;
	protected HashSet<Integer> excludedFlowTypesHashSet = null;
	
	private QueryParameterFactory<FlowInstanceEvent, FlowInstance> flowInstanceEventFlowInstanceParamFactory;
	private QueryParameterFactory<FlowInstanceEvent, User> flowInstanceEventPosterParamFactory;
	private QueryParameterFactory<FlowInstanceEvent, Timestamp> flowInstanceEventAddedParamFactory;

	@InstanceManagerDependency
	protected PDFProvider pdfProvider;

	@InstanceManagerDependency
	protected SigningProvider signingProvider;

	@InstanceManagerDependency
	protected MultiSigningHandler multiSigningHandler;

	@InstanceManagerDependency
	protected PaymentProvider paymentProvider;

	@InstanceManagerDependency
	protected XMLProvider xmlProvider;

	private FlowProcessCallback defaultFlowProcessCallback;

	private FlowProcessCallback completeFlowProcessCallback;

	private ExternalMessageCRUD externalMessageCRUD;
	
	protected CopyOnWriteArrayList<FlowInstanceOverviewExtensionProvider> tabExtensionProviders = new CopyOnWriteArrayList<FlowInstanceOverviewExtensionProvider>();
	protected Locale systemLocale;

	@Override
	protected void createDAOs(DataSource dataSource) throws Exception {

		super.createDAOs(dataSource);

		flowInstanceEventFlowInstanceParamFactory = daoFactory.getFlowInstanceEventDAO().getParamFactory("flowInstance", FlowInstance.class);
		flowInstanceEventPosterParamFactory = daoFactory.getFlowInstanceEventDAO().getParamFactory("poster", User.class);
		flowInstanceEventAddedParamFactory = daoFactory.getFlowInstanceEventDAO().getParamFactory("added", Timestamp.class);

		defaultFlowProcessCallback = new UserFlowInstanceBrowserProcessCallback(FlowBrowserModule.SAVE_ACTION_ID, FlowBrowserModule.SUBMIT_ACTION_ID, FlowBrowserModule.MULTI_SIGNING_ACTION_ID, FlowBrowserModule.PAYMENT_ACTION_ID, this);
		completeFlowProcessCallback = new UserFlowInstanceBrowserProcessCallback(null, SUBMIT_COMPLETION_ACTION_ID, FlowBrowserModule.MULTI_SIGNING_ACTION_ID, null, this);

		externalMessageCRUD = new ExternalMessageCRUD(daoFactory.getExternalMessageDAO(), daoFactory.getExternalMessageAttachmentDAO(), this, false);
	}

	@Override
	public void init(ForegroundModuleDescriptor moduleDescriptor, SectionInterface sectionInterface, DataSource dataSource) throws Exception {

		super.init(moduleDescriptor, sectionInterface, dataSource);

		if(!systemInterface.getInstanceHandler().addInstance(UserFlowInstanceModule.class, this)){

			throw new RuntimeException("Unable to register module in global instance handler using key " + UserFlowInstanceModule.class.getSimpleName() + ", another instance is already registered using this key.");
		}
		
		systemLocale = new Locale(systemInterface.getDefaultLanguage().getLanguageCode());
	}
	
	@Override
	protected void moduleConfigured() throws Exception {
		
		super.moduleConfigured();
		
		excludedFlowTypesHashSet = null;
		
		if (excludedFlowTypes != null) {
			
			excludedFlowTypesHashSet = new HashSet<Integer>(excludedFlowTypes);
		}
	}

	@Override
	public void unload() throws Exception {

		systemInterface.getInstanceHandler().removeInstance(UserFlowInstanceModule.class, this);
		
		tabExtensionProviders.clear();
		
		super.unload();
	}

	@Override
	public ForegroundModuleResponse defaultMethod(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Exception, Throwable {

		return this.list(req, res, user, uriParser, (List<ValidationError>)null);
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
		
		List<FlowInstance> flowInstances = getFlowInstances(user, true, excludedFlowTypes != null);
		
		if (!CollectionUtils.isEmpty(flowInstances)) {
			
			Element savedFlowInstancesElement = XMLUtils.appendNewElement(doc, listFlowInstancesElement, "SavedFlowInstances");
			Element submittedFlowInstancesElement = XMLUtils.appendNewElement(doc, listFlowInstancesElement, "SubmittedFlowInstances");
			Element archivedFlowInstancesElement = XMLUtils.appendNewElement(doc, listFlowInstancesElement, "ArchivedFlowInstances");
			
			for (FlowInstance flowInstance : flowInstances) {
				
				if (excludedFlowTypesHashSet != null && excludedFlowTypesHashSet.contains(flowInstance.getFlow().getFlowType().getFlowTypeID())) {
					continue;
				}
				
				Status status = flowInstance.getStatus();
				
				if ((status.getContentType() == ContentType.NEW || status.getContentType() == ContentType.WAITING_FOR_MULTISIGN || status.getContentType() == ContentType.WAITING_FOR_PAYMENT)) {
					
					savedFlowInstancesElement.appendChild(flowInstance.toXML(genDoc));
					
				} else if (status.getContentType() == ContentType.SUBMITTED || status.getContentType() == ContentType.IN_PROGRESS || status.getContentType() == ContentType.WAITING_FOR_COMPLETION) {
					
					Element flowInstanceElement = flowInstance.toXML(genDoc);
					
					List<FlowInstanceEvent> events = getNewFlowInstanceEvents(flowInstance, user);
					
					if (events != null) {
						
						for (FlowInstanceEvent event : events) {
							event.setShortDate(DateUtils.dateAndShortMonthToString(event.getAdded(), systemLocale));
						}
						
						XMLUtils.append(genDoc, flowInstanceElement, "newEvents", events);
					}
					
					submittedFlowInstancesElement.appendChild(flowInstanceElement);
					
				} else if (status.getContentType() == ContentType.ARCHIVED) {
					
					archivedFlowInstancesElement.appendChild(flowInstance.toXML(genDoc));
					
				} else {
					
					log.warn("The status of flow instance " + flowInstance + " has an unknown content type of " + status.getContentType());
				}
				
			}
			
			req.getSession().removeAttribute("LastFlowInstanceEventUpdate");
		}
		
		List<MultiSigningQueryProvider> multiSingningQueryProviders = queryHandler.getAssignableQueryProviders(MultiSigningQueryProvider.class);
		
		if (!CollectionUtils.isEmpty(multiSingningQueryProviders)) {
			
			String citizenIdentifier = user.getAttributeHandler().getString("citizenIdentifier");
			
			if (!StringUtils.isEmpty(citizenIdentifier)) {
				
				List<Integer> queryInstanceIDs = new ArrayList<Integer>();
				
				for (MultiSigningQueryProvider queryProvider : multiSingningQueryProviders) {
					
					List<Integer> queryProviderInstanceIDs = queryProvider.getQueryInstanceIDs(citizenIdentifier);
					
					if (queryProviderInstanceIDs != null) {
						
						queryInstanceIDs.addAll(queryProviderInstanceIDs);
					}
				}
				
				if (!CollectionUtils.isEmpty(queryInstanceIDs)) {
					
					ArrayListQuery<Integer> query = new ArrayListQuery<Integer>(daoFactory.getQueryInstanceDescriptorDAO().getDataSource(), "SELECT DISTINCT flowInstanceID FROM " + daoFactory.getQueryInstanceDescriptorDAO().getTableName() + " WHERE queryInstanceID IN (? " + StringUtils.repeatString(",?", queryInstanceIDs.size() - 1) + ")", IntegerPopulator.getPopulator());
					
					for (int i = 0; i < queryInstanceIDs.size(); i++) {
						
						query.setInt(i + 1, queryInstanceIDs.get(i));
					}
					
					ArrayList<Integer> flowInstanceIDs = query.executeQuery();
					
					if (flowInstanceIDs != null) {
						
						Element waitingMultiSignFlowInstancesElement = XMLUtils.appendNewElement(doc, listFlowInstancesElement, "WaitingMultiSignFlowInstances");
						
						List<FlowInstance> multiSignFlowInstances = getMultiSignFlowInstances(flowInstanceIDs);
						
						if (multiSignFlowInstances != null) {
							
							for (FlowInstance flowInstance : multiSignFlowInstances) {
								
								Element flowInstanceElement = (Element) waitingMultiSignFlowInstancesElement.appendChild(flowInstance.toXML(genDoc));
								
								XMLUtils.appendNewElement(doc, flowInstanceElement, "MultiSignURL", multiSigningHandler.getSigningURL(flowInstance, null));
							}
						}
					}
				}
			}
		}
		
		if (validationErrors != null) {
			
			XMLUtils.append(doc, listFlowInstancesElement, validationErrors);
		}
		
		return new SimpleForegroundModuleResponse(doc, moduleDescriptor.getName(), this.getDefaultBreadcrumb());
	}

	@WebPublic(alias = "overview")
	public ForegroundModuleResponse showFlowInstanceOverview(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws ModuleConfigurationException, SQLException, AccessDeniedException, IOException  {

		FlowInstance flowInstance;

		if (uriParser.size() == 4 && NumberUtils.isInt(uriParser.get(3)) && (flowInstance = getFlowInstance(Integer.valueOf(uriParser.get(3)), CollectionUtils.getList(ExternalMessageAttachment.DATA_FIELD), FLOW_INSTANCE_OVERVIEW_RELATIONS)) != null) {

			PREVIEW_ACCESS_CONTROLLER.checkFlowInstanceAccess(flowInstance, user);

			if (!flowInstance.getFlow().isEnabled() || isOperatingStatusDisabled(flowInstance.getFlow(), true)) {

				return list(req, res, user, uriParser, FLOW_DISABLED_VALIDATION_ERROR);
			}
			
			log.info("User " + user + " viewing overview of flow instance " + flowInstance);
			
			Document doc = this.createDocument(req, uriParser, user);

			Element showFlowInstanceOverviewElement = doc.createElement("ShowFlowInstanceOverview");
			doc.getDocumentElement().appendChild(showFlowInstanceOverviewElement);

			XMLUtils.appendNewElement(doc, showFlowInstanceOverviewElement, "FormatedMaxFileSize", BinarySizeFormater.getFormatedSize(maxFileSize * BinarySizes.MegaByte));
			
			if(req.getMethod().equalsIgnoreCase("POST")) {

				//TODO append message or request parameters
				ExternalMessage externalMessage = externalMessageCRUD.add(req, res, uriParser, user, doc, showFlowInstanceOverviewElement, flowInstance, false);

				if(externalMessage != null) {

					FlowInstanceEvent flowInstanceEvent = this.addFlowInstanceEvent(flowInstance, EventType.CUSTOMER_MESSAGE_SENT, null, user);

					systemInterface.getEventHandler().sendEvent(FlowInstance.class, new ExternalMessageAddedEvent(flowInstance, flowInstanceEvent, getCurrentSiteProfile(req, user, uriParser, flowInstance.getFlow().getFlowFamily()), externalMessage, SenderType.USER), EventTarget.ALL);

					res.sendRedirect(req.getContextPath() + uriParser.getFormattedURI() + "#messages");

					return null;

				}

			}

			showFlowInstanceOverviewElement.appendChild(flowInstance.toXML(doc));

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
				log.error("Error appending tab from FlowInstanceOverviewExtensionProvider " + tabContentsElement, e);
			}
		}

		return viewFragments;
	}

	@WebPublic(alias = "flowinstance")
	public ForegroundModuleResponse processFlowRequest(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws URINotFoundException, AccessDeniedException, ModuleConfigurationException, SQLException, IOException, FlowDefaultStatusNotFound, EvaluationException {

		Integer flowID = null;
		Integer flowInstanceID = null;
		MutableFlowInstanceManager instanceManager;

		try{
			if(uriParser.size() == 4 && (flowID = NumberUtils.toInt(uriParser.get(2))) != null &&  (flowInstanceID = NumberUtils.toInt(uriParser.get(3))) != null){

				//Get saved instance from DB or session
				instanceManager = getSavedMutableFlowInstanceManager(flowID, flowInstanceID, UPDATE_ACCESS_CONTROLLER, req.getSession(true), user, uriParser, req, true, false, true, DEFAULT_REQUEST_METADATA);

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

		} catch (FlowInstanceNoLongerAvailableException e) {

			log.info("User " + user + " requested flow instance " + e.getFlowInstance() + " which is no longer available.");
			return list(req, res, user, uriParser, FLOW_INSTANCE_NO_LONGER_AVAILABLE_VALIDATION_ERROR);

		} catch (FlowEngineException e) {

			log.error("Unable to get flow instance manager for flowID " + flowID + " and flowInstanceID " + flowInstanceID + " requested by user " + user,e);
			return list(req, res, user, uriParser, ERROR_GETTING_FLOW_INSTANCE_MANAGER_VALIDATION_ERROR);
		}

		try {

			if (instanceManager.getFlowInstance().getStatus().getContentType().equals(ContentType.WAITING_FOR_COMPLETION)) {
				
				return processFlowRequest(instanceManager, completeFlowProcessCallback, UPDATE_ACCESS_CONTROLLER, req, res, user, uriParser, true, DEFAULT_REQUEST_METADATA);
			}

			return processFlowRequest(instanceManager, defaultFlowProcessCallback, UPDATE_ACCESS_CONTROLLER, req, res, user, uriParser, true, DEFAULT_REQUEST_METADATA);

		} catch (FlowInstanceManagerClosedException e) {

			log.info("User " + user + " requested flow instance manager for flow instance " + e.getFlowInstance() + " which has already been closed. Removing flow instance manager from session.");

			removeMutableFlowInstanceManagerFromSession(instanceManager, req.getSession(false));

			redirectToMethod(req, res, "/flowinstance/" + flowID + "/" + flowInstanceID);

			return null;

		} catch (QueryInstanceHTMLException e) {

			return processFlowRequestException(instanceManager, req, res, user, uriParser, e);

		}catch (RuntimeException e){

			return processFlowRequestException(instanceManager, req, res, user, uriParser, e);
		}
	}

	@WebPublic(alias = "delete")
	public ForegroundModuleResponse deleteFlowInstance(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws URINotFoundException, SQLException, AccessDeniedException, IOException {

		Integer flowInstanceID = null;

		if(uriParser.size() == 3 && (flowInstanceID = NumberUtils.toInt(uriParser.get(2))) != null &&  deleteFlowInstance(flowInstanceID, DELETE_ACCESS_CONTROLLER, user) != null) {

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
	public ForegroundModuleResponse processMutableQueryRequest(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws ModuleConfigurationException, SQLException, AccessDeniedException, IOException, FlowDefaultStatusNotFound, EvaluationException, URINotFoundException, QueryRequestException, QueryProviderException, EvaluationProviderException, InvalidFlowInstanceStepException, MissingQueryInstanceDescriptor, DuplicateFlowInstanceManagerIDException {

		return processMutableQueryRequest(req, res, user, uriParser, UPDATE_ACCESS_CONTROLLER, true, true, false, DEFAULT_REQUEST_METADATA);
	}

	@WebPublic(alias = "iquery")
	public ForegroundModuleResponse processImmutableQueryRequest(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws ModuleConfigurationException, SQLException, AccessDeniedException, IOException, FlowDefaultStatusNotFound, EvaluationException, URINotFoundException, QueryRequestException, QueryProviderException, EvaluationProviderException, InvalidFlowInstanceStepException, MissingQueryInstanceDescriptor, DuplicateFlowInstanceManagerIDException {

		return processImmutableQueryRequest(req, res, user, uriParser, PREVIEW_ACCESS_CONTROLLER, true, false);
	}

	@Override
	public Breadcrumb getFlowBreadcrumb(ImmutableFlowInstance flowInstance) {

		return new Breadcrumb(this, flowInstance.getFlow().getName(), "/flowinstance/" + flowInstance.getFlow().getFlowID() + "/" + flowInstance.getFlowInstanceID());
	}

	protected List<FlowInstance> getFlowInstances(User user, boolean getEvents, boolean getFlowTypes) throws SQLException {
		
		ArrayListQuery<Integer> flowInstanceIDQuery = new ArrayListQuery<Integer>(daoFactory.getFlowInstanceDAO().getDataSource(), "SELECT flowInstanceID FROM flowengine_flow_instance_owners WHERE userID = ?", IntegerPopulator.getPopulator());
		flowInstanceIDQuery.setInt(1, user.getUserID());
		
		List<Integer> flowInstanceIDs = flowInstanceIDQuery.executeQuery();
		
		if (flowInstanceIDs == null) {
			
			return null;
		}
		
		HighLevelQuery<FlowInstance> query = new HighLevelQuery<FlowInstance>();
		
		query.addRelations(FlowInstance.FLOW_RELATION, FlowInstance.FLOW_STATE_RELATION);
		
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
	
	protected List<FlowInstance> getMultiSignFlowInstances(List<Integer> flowInstanceIDs) throws SQLException {
		
		LowLevelQuery<FlowInstance> query = new LowLevelQuery<FlowInstance>("SELECT i.* FROM " + daoFactory.getFlowInstanceDAO().getTableName()
				+ " i INNER JOIN (SELECT statusID FROM " + daoFactory.getStatusDAO().getTableName() + " WHERE contentType = '" + ContentType.WAITING_FOR_MULTISIGN + "') s ON i.statusID = s.statusID"
				+ " WHERE i.flowInstanceID IN (? " + StringUtils.repeatString(",?", flowInstanceIDs.size() - 1) + ")");
		
		query.addRelations(FlowInstance.FLOW_RELATION, FlowInstance.FLOW_STATE_RELATION, Flow.FLOW_TYPE_RELATION);
		
		query.addExcludedFields(LIST_EXCLUDED_FIELDS);
		
		query.addParameters(flowInstanceIDs);
		
		return daoFactory.getFlowInstanceDAO().getAll(query);
	}

	protected List<FlowInstanceEvent> getNewFlowInstanceEvents(FlowInstance flowInstance, User user) throws SQLException {

		HighLevelQuery<FlowInstanceEvent> query = new HighLevelQuery<FlowInstanceEvent>();

		query.addParameter(flowInstanceEventFlowInstanceParamFactory.getParameter(flowInstance));
		query.addParameter(flowInstanceEventPosterParamFactory.getParameter(user, QueryOperators.NOT_EQUALS));

		if(user.getLastLogin() != null){
			query.addParameter(flowInstanceEventAddedParamFactory.getParameter(user.getLastLogin(), QueryOperators.BIGGER_THAN));
		}


		return daoFactory.getFlowInstanceEventDAO().getAll(query);

	}

	@WebPublic(alias = "submitted")
	public ForegroundModuleResponse showSubmittedMessage(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws FlowInstanceManagerClosedException, UnableToGetQueryInstanceShowHTMLException, AccessDeniedException, ModuleConfigurationException, SQLException, URINotFoundException{

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
	public ForegroundModuleResponse showPaymentForm(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws FlowInstanceManagerClosedException, UnableToGetQueryInstanceShowHTMLException, AccessDeniedException, ModuleConfigurationException, SQLException, URINotFoundException {

		return super.showPaymentForm(req, res, user, uriParser, PREVIEW_ACCESS_CONTROLLER, this.defaultFlowProcessCallback, false);
	}

	@Override
	protected void redirectToSubmitMethod(MutableFlowInstanceManager flowInstanceManager, HttpServletRequest req, HttpServletResponse res) throws IOException {

		HttpSession session = req.getSession();

		if(session != null){

			try {
				SessionAccessController.setSessionAttribute(flowInstanceManager.getFlowInstanceID(), session, SESSION_ACCESS_CONTROLLER_TAG);
			} catch (IllegalStateException e) {}
		}

		redirectToMethod(req, res, "/submitted/" + flowInstanceManager.getFlowInstanceID());
	}

	@Override
	protected void onFlowInstanceClosedRedirect(FlowInstanceManager flowInstanceManager, HttpServletRequest req, HttpServletResponse res) throws IOException {

		redirectToMethod(req, res, "/overview/" + flowInstanceManager.getFlowID() + "/" + flowInstanceManager.getFlowInstanceID());

	}

	@WebPublic(alias = "preview")
	public ForegroundModuleResponse showPreview(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws FlowInstanceManagerClosedException, UnableToGetQueryInstanceShowHTMLException, AccessDeniedException, ModuleConfigurationException, SQLException{

		return super.showImmutableFlowInstance(req, res, user, uriParser, PREVIEW_ACCESS_CONTROLLER, defaultFlowProcessCallback, ShowMode.PREVIEW, DEFAULT_REQUEST_METADATA);
	}

	@Override
	protected Breadcrumb getFlowInstancePreviewBreadcrumb(ImmutableFlowInstance flowInstance, HttpServletRequest req, URIParser uriParser) {

		//TODO add prefix
		return new Breadcrumb(this, flowInstance.getFlow().getName(), "/preview/" + flowInstance.getFlowInstanceID());
	}

	@Override
	protected Breadcrumb getFlowInstanceSubmitBreadcrumb(ImmutableFlowInstance flowInstance, HttpServletRequest req, URIParser uriParser) {

		//TODO add prefix
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
	protected MultiSigningHandler getMultiSigningProvider() {

		return multiSigningHandler;
	}

	@Override
	protected PaymentProvider getPaymentProvider() {

		return paymentProvider;
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

		if(event.getAttributeHandler().getPrimitiveBoolean("pdf")){

			return this.getModuleURI(req) + "/pdf/" + instanceManager.getFlowInstanceID() + "/" + event.getEventID();
		}

		return null;
	}

	@Override
	public boolean requiresPayment(FlowInstanceManager instanceManager) {

		if(instanceManager.getFlowState().getContentType() == ContentType.WAITING_FOR_COMPLETION) {

			return false;
		}

		return super.requiresPayment(instanceManager);
	}
	
	@Override
	public String getAbsoluteFileURL(URIParser uriParser, Object bean) {

		if(ckConnectorModuleAlias != null){
			
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

		query.addRelations(FlowInstance.FLOW_STATE_RELATION, FlowInstance.FLOW_RELATION, Flow.STEPS_RELATION, Flow.FLOW_TYPE_RELATION, FlowType.ALLOWED_ADMIN_GROUPS_RELATION, FlowType.ALLOWED_ADMIN_USERS_RELATION, Step.QUERY_DESCRIPTORS_RELATION, QueryDescriptor.QUERY_INSTANCE_DESCRIPTORS_RELATION);
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
			
			if(providerID.equals(tabExtensionProvider.getOverviewExtensionProviderID())){
				
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

			if (!flowInstance.getFlow().isEnabled() || isOperatingStatusDisabled(flowInstance.getFlow(), true)) {

				return list(req, res, user, uriParser, FLOW_DISABLED_VALIDATION_ERROR);
			}

			FlowInstanceOverviewExtensionProvider extensionProvider = getOverviewExtensionProvider(uriParser.get(4));
			
			if(extensionProvider != null){
				
				return extensionProvider.processOverviewExtensionRequest(flowInstance, req, res, uriParser, user);
			}
		}

		throw new URINotFoundException(uriParser);
	}
	
	public String getOverviewExtensionRequestMethodAlias(FlowInstance flowInstance, String providerID){
		return getFullAlias() + "/" + "overviewextensionrequest" + "/" + flowInstance.getFlow().getFlowID() + "/" + flowInstance.getFlowInstanceID() + "/" + providerID;
	}
	
}
