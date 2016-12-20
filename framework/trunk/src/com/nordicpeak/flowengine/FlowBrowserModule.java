package com.nordicpeak.flowengine;

import it.sauronsoftware.cron4j.Scheduler;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import se.unlogic.hierarchy.core.annotations.CheckboxSettingDescriptor;
import se.unlogic.hierarchy.core.annotations.InstanceManagerDependency;
import se.unlogic.hierarchy.core.annotations.ModuleSetting;
import se.unlogic.hierarchy.core.annotations.TextAreaSettingDescriptor;
import se.unlogic.hierarchy.core.annotations.TextFieldSettingDescriptor;
import se.unlogic.hierarchy.core.annotations.WebPublic;
import se.unlogic.hierarchy.core.annotations.XSLVariable;
import se.unlogic.hierarchy.core.beans.Breadcrumb;
import se.unlogic.hierarchy.core.beans.SettingDescriptor;
import se.unlogic.hierarchy.core.beans.SimpleForegroundModuleResponse;
import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.hierarchy.core.beans.ValueDescriptor;
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
import se.unlogic.hierarchy.core.utils.AccessUtils;
import se.unlogic.hierarchy.core.utils.FCKUtils;
import se.unlogic.hierarchy.core.utils.ModuleUtils;
import se.unlogic.openhierarchy.foregroundmodules.siteprofile.interfaces.SiteProfile;
import se.unlogic.standardutils.collections.CollectionUtils;
import se.unlogic.standardutils.collections.MethodComparator;
import se.unlogic.standardutils.collections.NameComparator;
import se.unlogic.standardutils.dao.HighLevelQuery;
import se.unlogic.standardutils.dao.QueryParameterFactory;
import se.unlogic.standardutils.dao.querys.ArrayListQuery;
import se.unlogic.standardutils.enums.Order;
import se.unlogic.standardutils.numbers.NumberUtils;
import se.unlogic.standardutils.populators.IntegerPopulator;
import se.unlogic.standardutils.string.StringUtils;
import se.unlogic.standardutils.threads.ReflectedRunnable;
import se.unlogic.standardutils.time.TimeUtils;
import se.unlogic.standardutils.validation.PositiveStringIntegerValidator;
import se.unlogic.standardutils.validation.ValidationError;
import se.unlogic.standardutils.xml.XMLUtils;
import se.unlogic.webutils.http.HTTPUtils;
import se.unlogic.webutils.http.RequestUtils;
import se.unlogic.webutils.http.SessionUtils;
import se.unlogic.webutils.http.URIParser;
import se.unlogic.webutils.url.URLRewriter;

import com.nordicpeak.flowengine.accesscontrollers.SessionAccessController;
import com.nordicpeak.flowengine.accesscontrollers.UserFlowInstanceAccessController;
import com.nordicpeak.flowengine.beans.ExternalFlowRedirect;
import com.nordicpeak.flowengine.beans.Flow;
import com.nordicpeak.flowengine.beans.FlowFamily;
import com.nordicpeak.flowengine.beans.FlowForm;
import com.nordicpeak.flowengine.beans.FlowInstance;
import com.nordicpeak.flowengine.beans.FlowInstanceEvent;
import com.nordicpeak.flowengine.beans.FlowType;
import com.nordicpeak.flowengine.beans.SigningParty;
import com.nordicpeak.flowengine.beans.Status;
import com.nordicpeak.flowengine.enums.EventType;
import com.nordicpeak.flowengine.enums.QueryState;
import com.nordicpeak.flowengine.enums.ShowMode;
import com.nordicpeak.flowengine.events.FlowBrowserCacheEvent;
import com.nordicpeak.flowengine.exceptions.FlowEngineException;
import com.nordicpeak.flowengine.exceptions.evaluation.EvaluationException;
import com.nordicpeak.flowengine.exceptions.evaluationprovider.EvaluationProviderException;
import com.nordicpeak.flowengine.exceptions.flow.FlowDefaultStatusNotFound;
import com.nordicpeak.flowengine.exceptions.flow.FlowDisabledException;
import com.nordicpeak.flowengine.exceptions.flow.FlowLimitExceededException;
import com.nordicpeak.flowengine.exceptions.flow.FlowNoLongerAvailableException;
import com.nordicpeak.flowengine.exceptions.flow.FlowNotAvailiableInRequestedFormat;
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
import com.nordicpeak.flowengine.exceptions.queryinstance.UnableToSaveQueryInstanceException;
import com.nordicpeak.flowengine.exceptions.queryprovider.QueryProviderException;
import com.nordicpeak.flowengine.interfaces.FlowInstanceAccessController;
import com.nordicpeak.flowengine.interfaces.FlowProcessCallback;
import com.nordicpeak.flowengine.interfaces.ImmutableFlowInstance;
import com.nordicpeak.flowengine.interfaces.ImmutableFlowInstanceEvent;
import com.nordicpeak.flowengine.interfaces.MultiSigningHandler;
import com.nordicpeak.flowengine.interfaces.MultiSigningQuery;
import com.nordicpeak.flowengine.interfaces.OperatingStatus;
import com.nordicpeak.flowengine.interfaces.PDFProvider;
import com.nordicpeak.flowengine.interfaces.PaymentProvider;
import com.nordicpeak.flowengine.interfaces.SigningProvider;
import com.nordicpeak.flowengine.managers.FlowInstanceManager;
import com.nordicpeak.flowengine.managers.MutableFlowInstanceManager;
import com.nordicpeak.flowengine.search.FlowIndexer;
import com.nordicpeak.flowengine.utils.SigningUtils;
import com.nordicpeak.flowengine.utils.TextTagReplacer;

public class FlowBrowserModule extends BaseFlowBrowserModule implements FlowProcessCallback, FlowInstanceAccessController, EventListener<CRUDEvent<?>>, Runnable {

	public static final UserFlowInstanceAccessController PREVIEW_ACCESS_CONTROLLER = new UserFlowInstanceAccessController(false, false);

	private static final Comparator<FlowFamily> FAMILY_COMPARATOR = new MethodComparator<FlowFamily>(FlowFamily.class, "getFlowInstanceCount", Order.DESC);

	public static final String SAVE_ACTION_ID = FlowBrowserModule.class.getName() + ".save";
	public static final String SUBMIT_ACTION_ID = FlowBrowserModule.class.getName() + ".submit";
	public static final String PAYMENT_ACTION_ID = FlowBrowserModule.class.getName() + ".pay";
	public static final String MULTI_SIGNING_ACTION_ID = FlowBrowserModule.class.getName() + ".multisigning";

	public static final String SESSION_ACCESS_CONTROLLER_TAG = FlowBrowserModule.class.getName();

	@ModuleSetting
	@CheckboxSettingDescriptor(name = "Show all flowtypes", description = "List all flowtypes in this module")
	protected boolean listAllFlowTypes = false;

	@ModuleSetting
	protected List<Integer> flowTypeIDs;

	@ModuleSetting(allowsNull = true)
	@TextAreaSettingDescriptor(name = "Recommended tags (one search tag per line)", description = "Recommended tag listed between flow search form (one search tag per line)", required = false)
	private List<String> recommendedTags;

	@XSLVariable(prefix = "java.")
	@ModuleSetting
	@TextFieldSettingDescriptor(name = "Search hints", description = "Search hints used as placeholder for search input field", required = true)
	private String searchHints;

	@ModuleSetting
	@TextFieldSettingDescriptor(name = "Max search hits", description = "Maximum number of hits to get from index when searching", formatValidator = PositiveStringIntegerValidator.class, required = true)
	protected int maxHitCount = 10;

	@ModuleSetting(allowsNull = true)
	@TextFieldSettingDescriptor(name = "User favourite module alias", description = "Full alias of the user favourite module", required = false)
	protected String userFavouriteModuleAlias;

	@ModuleSetting
	@TextFieldSettingDescriptor(name = "Intervall size", description = "Controls how any hours back in time that the popular statistics should be based on")
	private int popularInterval = 72;

	@ModuleSetting
	@TextFieldSettingDescriptor(name = "Flow count", description = "Controls how many popular flows this module should display for each flow type")
	private int popularFlowCount = 5;

	@ModuleSetting
	@CheckboxSettingDescriptor(name = "Open external flows in new window", description = "Controls whether to open external flows in new window or not")
	protected boolean openExternalFlowsInNewWindow = true;

	@ModuleSetting
	@CheckboxSettingDescriptor(name = "Show related flows", description = "Controls whether to show related flows in flowoverview")
	protected boolean showRelatedFlows = true;

	@ModuleSetting
	@CheckboxSettingDescriptor(name = "Use category filter", description = "Controls whether to use category filter when listing flows")
	protected boolean useCategoryFilter = false;

	@ModuleSetting(allowsNull = true)
	@TextFieldSettingDescriptor(name = "CKEditor connector module alias", description = "The full alias of the CKEditor connector module (relative from the contextpath). Leave empty if you do not want to activate file manager for CKEditor")
	protected String ckConnectorModuleAlias;

	@ModuleSetting
	@CheckboxSettingDescriptor(name = "Register in instance handler", description = "Controls whether to register this module in instance handler or not")
	protected boolean registerInInstanceHandler = true;

	@ModuleSetting
	@CheckboxSettingDescriptor(name = "Save last search", description = "Saves the last search in session")
	private boolean saveSearchInSession = false;
	
	@ModuleSetting
	@CheckboxSettingDescriptor(name = "Enable direct search", description = "Enable searching via request parameter on main page")
	private boolean enableDirectSearch = false;
	
	@InstanceManagerDependency
	protected PDFProvider pdfProvider;

	@InstanceManagerDependency
	protected SigningProvider signingProvider;

	@InstanceManagerDependency
	protected MultiSigningHandler multiSigningHandler;

	@InstanceManagerDependency
	protected PaymentProvider paymentProvider;

	@InstanceManagerDependency
	protected FlowAdminModule flowAdminModule;

	private QueryParameterFactory<FlowType, Integer> flowTypeIDParamFactory;

	private List<FlowType> flowTypes;
	private HashMap<Integer, Flow> flowMap;
	protected LinkedHashMap<Integer, Flow> latestPublishedFlowVersionsMap;

	protected final ReentrantReadWriteLock rwl = new ReentrantReadWriteLock();
	protected final Lock r = rwl.readLock();
	protected final Lock w = rwl.writeLock();

	private Scheduler scheduler;

	private FlowIndexer flowIndexer;

	@Override
	public void init(ForegroundModuleDescriptor moduleDescriptor, SectionInterface sectionInterface, DataSource dataSource) throws Exception {

		super.init(moduleDescriptor, sectionInterface, dataSource);

		cacheFlows();

		this.eventHandler.addEventListener(FlowType.class, CRUDEvent.class, this);
		this.eventHandler.addEventListener(Flow.class, CRUDEvent.class, this);

		scheduler = new Scheduler();
		scheduler.schedule("0 0 * * *", this);
		scheduler.schedule("0 * * * *", new ReflectedRunnable(this, "calculatePopularFlows"));
		scheduler.start();

		if (registerInInstanceHandler) {

			if (!systemInterface.getInstanceHandler().addInstance(FlowBrowserModule.class, this)) {

				throw new RuntimeException("Unable to register module in global instance handler using key " + FlowBrowserModule.class.getSimpleName() + ", another instance is already registered using this key.");
			}

		}
	}

	@Override
	public void update(ForegroundModuleDescriptor descriptor, DataSource dataSource) throws Exception {

		super.update(descriptor, dataSource);

		cacheFlows();
	}

	@Override
	public void unload() throws Exception {

		super.unload();

		try {
			scheduler.stop();
		} catch (IllegalStateException e) {
			log.error("Error stopping scheduler", e);
		}

		this.eventHandler.removeEventListener(FlowType.class, CRUDEvent.class, this);
		this.eventHandler.removeEventListener(Flow.class, CRUDEvent.class, this);

		systemInterface.getInstanceHandler().removeInstance(FlowBrowserModule.class, this);
	}

	@Override
	protected void createDAOs(DataSource dataSource) throws Exception {

		super.createDAOs(dataSource);

		flowTypeIDParamFactory = daoFactory.getFlowTypeDAO().getParamFactory("flowTypeID", Integer.class);
	}

	@Override
	public ForegroundModuleResponse processRequest(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Throwable {

		if (!listAllFlowTypes && flowTypeIDs == null) {

			throw new ModuleConfigurationException("No flowTypeIDs set in module settings for this module ");
		}

		//		if (flowTypes == null) {
		//
		//			throw new ModuleConfigurationException("The configured flow types was not found in the database");
		//		}

		return super.processRequest(req, res, user, uriParser);
	}

	@Override
	public ForegroundModuleResponse defaultMethod(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Exception, Throwable {

		return list(req, res, user, uriParser, (List<ValidationError>) null);
	}

	public ForegroundModuleResponse list(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser, ValidationError validationError) throws ModuleConfigurationException, SQLException {

		return list(req, res, user, uriParser, CollectionUtils.getGenericSingletonList(validationError));
	}

	@Override
	public ForegroundModuleResponse list(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser, List<ValidationError> validationErrors) throws ModuleConfigurationException, SQLException {

		r.lock();

		try {
			log.info("User " + user + " listing flows");

			Document doc = this.createDocument(req, uriParser, user);
			
			Element showFlowTypesElement = doc.createElement("ShowFlowTypes");

			doc.getDocumentElement().appendChild(showFlowTypesElement);

			String lastSearch = null;
			
			if(enableDirectSearch){
				
				lastSearch = req.getParameter("q");
			}
			
			if(lastSearch == null && saveSearchInSession){
				
				lastSearch = (String)SessionUtils.getAttribute("lastsearch", req);
			}

			if(lastSearch != null){
				
				XMLUtils.appendNewElement(doc, showFlowTypesElement, "lastSearch", lastSearch);
			}			
			
			if(flowTypes != null){

				for(FlowType flowType : flowTypes){

					if(AccessUtils.checkAccess(user, flowType.getUserAccessInterface())){

						showFlowTypesElement.appendChild(flowType.toXML(doc));
					}
				}
			}

			SiteProfile siteProfile = getCurrentSiteProfile(req, user, uriParser, null);

			if (latestPublishedFlowVersionsMap != null) {

				if(siteProfile != null){

					for (Flow flow : latestPublishedFlowVersionsMap.values()) {

						if(!flow.isHideFromOverview() && AccessUtils.checkAccess(user, flow.getFlowType().getUserAccessInterface())){

							showFlowTypesElement.appendChild(flow.toXML(doc, siteProfile, getAbsoluteFileURL(uriParser, flow), req));
						}
					}

				}else{

					for (Flow flow : latestPublishedFlowVersionsMap.values()) {

						if(!flow.isHideFromOverview() && AccessUtils.checkAccess(user, flow.getFlowType().getUserAccessInterface())){

							showFlowTypesElement.appendChild(flow.toXML(doc));
						}
					}
				}
			}

			if (user != null) {
				XMLUtils.appendNewElement(doc, showFlowTypesElement, "loggedIn");
				XMLUtils.append(doc, showFlowTypesElement, daoFactory.getUserFavouriteDAO().getAll(user, req.getSession(), latestPublishedFlowVersionsMap));
			}

			XMLUtils.append(doc, showFlowTypesElement, "recommendedTags", "Tag", recommendedTags);
			XMLUtils.appendNewElement(doc, showFlowTypesElement, "searchHints", searchHints);
			XMLUtils.appendNewElement(doc, showFlowTypesElement, "popularFlowCount", popularFlowCount);
			XMLUtils.appendNewElement(doc, showFlowTypesElement, "userFavouriteModuleAlias", userFavouriteModuleAlias);
			XMLUtils.appendNewElement(doc, showFlowTypesElement, "openExternalFlowsInNewWindow", openExternalFlowsInNewWindow);

			if (useCategoryFilter) {
				XMLUtils.appendNewElement(doc, showFlowTypesElement, "useCategoryFilter", true);
			}

			appendAdditionalListFlowsXML(doc, showFlowTypesElement);

			if (validationErrors != null) {

				XMLUtils.append(doc, showFlowTypesElement, validationErrors);
			}

			return new SimpleForegroundModuleResponse(doc, moduleDescriptor.getName(), this.getDefaultBreadcrumb());

		} finally {

			r.unlock();
		}
	}

	@WebPublic(alias = "flowoverview")
	public ForegroundModuleResponse showFlowOverview(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws ModuleConfigurationException, SQLException, AccessDeniedException, IOException, FlowDefaultStatusNotFound, EvaluationException {

		Flow flow;

		if (uriParser.size() == 3 && NumberUtils.isInt(uriParser.get(2)) && (flow = flowMap.get(Integer.valueOf(uriParser.get(2)))) != null) {

			checkFlowAccess(user, flow);

			if (skipOverview(flow, req, res, "flow")) {

				return null;
			}

			return showFlowOverview(flow, req, res, user, uriParser);
		}

		return list(req, res, user, uriParser, FLOW_NOT_FOUND_VALIDATION_ERROR);

	}

	@WebPublic(alias = "overview")
	public ForegroundModuleResponse showLatestPublishedFlowOverview(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws ModuleConfigurationException, SQLException, IOException, AccessDeniedException {

		Flow flow;

		if (uriParser.size() == 3 && NumberUtils.isInt(uriParser.get(2)) && (flow = getLatestPublishedFlowVersion(Integer.valueOf(uriParser.get(2)))) != null) {

			checkFlowAccess(user, flow);

			if (skipOverview(flow, req, res, "flow")) {

				return null;
			}

			return showFlowOverview(flow, req, res, user, uriParser);
		}

		return list(req, res, user, uriParser, FLOW_NOT_FOUND_VALIDATION_ERROR);
	}

	protected void checkFlowAccess(User user, Flow flow) throws AccessDeniedException {

		if(!AccessUtils.checkAccess(user, flow.getFlowType().getUserAccessInterface())){

			throw new AccessDeniedException("Access to flow " + flow + " denied");
		}
	}

	@WebPublic(alias = "external")
	public ForegroundModuleResponse checkExternalLogin(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws ModuleConfigurationException, SQLException, IOException, AccessDeniedException {

		Flow flow;

		if (uriParser.size() == 3 && NumberUtils.isInt(uriParser.get(2)) && (flow = flowMap.get(Integer.valueOf(uriParser.get(2)))) != null) {

			if (flow.requiresAuthentication() && user == null) {

				throw new AccessDeniedException("External flow requires login");
			}

			daoFactory.getExternalFlowRedirectDAO().add(new ExternalFlowRedirect(flow.getFlowID(), TimeUtils.getCurrentTimestamp()));

			res.sendRedirect(flow.getExternalLink());
			return null;
		}

		return list(req, res, user, uriParser, FLOW_NOT_FOUND_VALIDATION_ERROR);
	}

	private ForegroundModuleResponse showFlowOverview(Flow flow, HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws ModuleConfigurationException, SQLException {

		if (!flow.isPublished() || !flow.isEnabled()) {

			log.info("User " + user + " requested flow " + flow + " which is no longer available.");

			return list(req, res, user, uriParser, FLOW_NO_LONGER_AVAILABLE_VALIDATION_ERROR);

		}

		Document doc = this.createDocument(req, uriParser, user);

		Element showFlowOverviewElement = doc.createElement("ShowFlowOverview");

		doc.getDocumentElement().appendChild(showFlowOverviewElement);

		showFlowOverviewElement.appendChild(flow.toXML(doc, getCurrentSiteProfile(req, user, uriParser, flow.getFlowFamily()), getAbsoluteFileURL(uriParser, flow), req));

		XMLUtils.append(doc, showFlowOverviewElement, "FlowTypeFlows", getLatestPublishedFlowVersions());

		if (user != null) {
			XMLUtils.appendNewElement(doc, showFlowOverviewElement, "loggedIn");
			XMLUtils.append(doc, showFlowOverviewElement, daoFactory.getUserFavouriteDAO().getAll(user, req.getSession(), latestPublishedFlowVersionsMap));
		}

		XMLUtils.appendNewElement(doc, showFlowOverviewElement, "userFavouriteModuleAlias", userFavouriteModuleAlias);
		XMLUtils.appendNewElement(doc, showFlowOverviewElement, "openExternalFlowsInNewWindow", openExternalFlowsInNewWindow);
		XMLUtils.appendNewElement(doc, showFlowOverviewElement, "showRelatedFlows", showRelatedFlows);

		if (operatingMessageModule != null) {

			OperatingStatus operatingStatus = operatingMessageModule.getOperatingStatus(flow.getFlowFamily().getFlowFamilyID(), false);

			if (operatingStatus != null) {
				showFlowOverviewElement.appendChild(operatingStatus.toXML(doc));
			}

		}

		return new SimpleForegroundModuleResponse(doc, flow.getName(), this.getDefaultBreadcrumb());

	}

	@WebPublic(alias = "flow")
	public ForegroundModuleResponse processFlowRequest(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws ModuleConfigurationException, SQLException, AccessDeniedException, IOException, FlowDefaultStatusNotFound, EvaluationException {

		Integer flowID = null;
		Integer flowInstanceID = null;
		MutableFlowInstanceManager instanceManager;

		try {
			if (uriParser.size() == 3 && (flowID = NumberUtils.toInt(uriParser.get(2))) != null) {

				//Create new instance or get instance from session
				instanceManager = getUnsavedMutableFlowInstanceManager(flowID, this, req.getSession(true), user, uriParser, req, true, true, true, true, DEFAULT_REQUEST_METADATA);

				if (instanceManager == null) {

					log.info("User " + user + " requested non-existing flow with ID " + flowID + ", listing flows");
					return list(req, res, user, uriParser, FLOW_NOT_FOUND_VALIDATION_ERROR);
				}

			} else if (uriParser.size() == 4 && (flowID = NumberUtils.toInt(uriParser.get(2))) != null && (flowInstanceID = NumberUtils.toInt(uriParser.get(3))) != null) {

				//Get saved instance from DB or session
				instanceManager = getSavedMutableFlowInstanceManager(flowID, flowInstanceID, this, req.getSession(true), user, uriParser, req, false, true, true, DEFAULT_REQUEST_METADATA);

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

		} catch (FlowNotAvailiableInRequestedFormat e) {

			log.info("User " + user + " requested flow " + flowID + " which is not availiable in the requested format.");
			return list(req, res, user, uriParser, FLOW_NOT_AVAILIABLE_IN_REQUESTED_FORMAT_VALIDATION_ERROR);

		} catch (FlowInstanceNoLongerAvailableException e) {

			log.info("User " + user + " requested flow instance " + e.getFlowInstance() + " which is no longer available.");
			return list(req, res, user, uriParser, FLOW_INSTANCE_NO_LONGER_AVAILABLE_VALIDATION_ERROR);

		} catch (FlowLimitExceededException e) {

			log.info("User " + user + " has reached the flow instance limit for flow " + e.getFlow());

			return handleFlowLimitExceededException(req, res, user, uriParser, e);

		} catch (FlowEngineException e) {

			log.error("Unable to get flow instance manager for flowID " + flowID + " and flowInstanceID " + flowInstanceID + " requested by user " + user, e);
			return list(req, res, user, uriParser, ERROR_GETTING_FLOW_INSTANCE_MANAGER_VALIDATION_ERROR);
		}

		try {
			return processFlowRequest(instanceManager, this, this, req, res, user, uriParser, true, DEFAULT_REQUEST_METADATA);

		} catch (FlowInstanceManagerClosedException e) {

			log.info("User " + user + " requested flow instance manager for flow instance " + e.getFlowInstance() + " which has already been closed. Removing flow instance manager from session.");

			removeMutableFlowInstanceManagerFromSession(instanceManager, req.getSession(false));

			if (flowInstanceID != null) {

				redirectToMethod(req, res, "/flow/" + flowID + "/" + flowInstanceID);

			} else {

				redirectToMethod(req, res, "/flow/" + flowID);
			}

			return null;

		} catch (QueryInstanceHTMLException e) {

			return processFlowRequestException(instanceManager, req, res, user, uriParser, e);

		} catch (RuntimeException e) {

			return processFlowRequestException(instanceManager, req, res, user, uriParser, e);
		}
	}

	@WebPublic(alias = "search")
	public ForegroundModuleResponse search(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws ModuleConfigurationException, SQLException, AccessDeniedException, IOException, FlowDefaultStatusNotFound, EvaluationException {

		FlowIndexer flowIndexer = this.flowIndexer;

		if (flowIndexer != null) {

			this.flowIndexer.search(req, res, user);

		} else {

			FlowIndexer.sendEmptyResponse(res);
		}

		return null;
	}

	@WebPublic(alias = "resetlastsearch")
	public ForegroundModuleResponse resetLastSearch(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws ModuleConfigurationException, SQLException, AccessDeniedException, IOException, FlowDefaultStatusNotFound, EvaluationException {

		FlowIndexer flowIndexer = this.flowIndexer;

		if (flowIndexer != null) {

			this.flowIndexer.resetLastSearch(req, user);

		}
		
		HTTPUtils.sendReponse("", "text/plain", res);
		
		return null;
	}
	
	
	
	@WebPublic(alias = "mquery")
	public ForegroundModuleResponse processMutableQueryRequest(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws ModuleConfigurationException, SQLException, AccessDeniedException, IOException, FlowDefaultStatusNotFound, EvaluationException, URINotFoundException, QueryRequestException, QueryProviderException, EvaluationProviderException, InvalidFlowInstanceStepException, MissingQueryInstanceDescriptor, DuplicateFlowInstanceManagerIDException, UnableToResetQueryInstanceException {

		return processMutableQueryRequest(req, res, user, uriParser, this, true, true, true, DEFAULT_REQUEST_METADATA);
	}

	@WebPublic(alias = "iquery")
	public ForegroundModuleResponse processImmutableQueryRequest(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws ModuleConfigurationException, SQLException, AccessDeniedException, IOException, FlowDefaultStatusNotFound, EvaluationException, URINotFoundException, QueryRequestException, QueryProviderException, EvaluationProviderException, InvalidFlowInstanceStepException, MissingQueryInstanceDescriptor, DuplicateFlowInstanceManagerIDException {

		return processImmutableQueryRequest(req, res, user, uriParser, new SessionAccessController(req.getSession(), SESSION_ACCESS_CONTROLLER_TAG), true, false);
	}

	@Override
	public void checkNewFlowInstanceAccess(Flow flow, User user) throws AccessDeniedException {

		if (!listAllFlowTypes && !this.flowTypeIDs.contains(flow.getFlowType().getFlowTypeID())) {

			throw new AccessDeniedException("Access denied to flow " + flow + " belonging to flow type " + flow.getFlowType());

		} else if (flow.requiresAuthentication() && user == null) {

			throw new AccessDeniedException("Flow " + flow + " requires autentication");
		}
	}

	@Override
	public void checkFlowInstanceAccess(ImmutableFlowInstance flowInstance, User user) throws AccessDeniedException {

		if (!listAllFlowTypes && !this.flowTypeIDs.contains(flowInstance.getFlow().getFlowType().getFlowTypeID())) {

			throw new AccessDeniedException("Access to flow instance " + flowInstance + " belonging to flow type " + flowInstance.getFlow().getFlowType() + " is not allowed via this module");

		} else if (flowInstance.getOwners() == null || user == null || !flowInstance.getOwners().contains(user)) {

			throw new AccessDeniedException("Access denied to flow instance " + flowInstance + ", the current user is not owner of the requested instance.");

		} else if (!flowInstance.getStatus().isUserMutable()) {

			//TODO throw better exception here
			throw new AccessDeniedException("Access denied to flow instance " + flowInstance + ", the requested instance is not in a user mutable state.");
		}
	}

	public void cacheFlows() throws SQLException {

		w.lock();

		try {

			if (!listAllFlowTypes && CollectionUtils.isEmpty(flowTypeIDs)) {

				log.warn("No flowTypeIDs set, unable to cache flows.");

				this.flowTypes = null;
				this.flowMap = null;
				this.latestPublishedFlowVersionsMap = null;
				this.flowIndexer = null;

				return;
			}

			HighLevelQuery<FlowType> query = new HighLevelQuery<FlowType>(FlowType.FLOWS_RELATION, FlowType.CATEGORIES_RELATION, Flow.CATEGORY_RELATION, Flow.FLOW_FAMILY_RELATION, Flow.TAGS_RELATION, Flow.CHECKS_RELATION, Flow.STEPS_RELATION, FlowFamily.ALIASES_RELATION, FlowType.ALLOWED_GROUPS_RELATION, FlowType.ALLOWED_USERS_RELATION, Flow.FLOW_FORMS_RELATION);

			if (listAllFlowTypes) {

				log.info("Caching flows for all flowTypes");

			} else {

				log.info("Caching flows for flowTypeIDs " + StringUtils.toCommaSeparatedString(flowTypeIDs));

				query.addParameter(flowTypeIDParamFactory.getWhereInParameter(flowTypeIDs));

			}

			List<FlowType> flowTypes = daoFactory.getFlowTypeDAO().getAll(query);

			if (flowTypes == null) {

				log.warn("The configured flowTypeIDs were not found in the database.");

				this.flowTypes = null;
				this.flowMap = null;
				this.latestPublishedFlowVersionsMap = null;
				this.flowIndexer = null;

			} else {

				HashMap<Integer, Flow> flowMap = new HashMap<Integer, Flow>();

				for (FlowType flowType : flowTypes) {

					if (flowType.getFlows() != null) {

						for (Flow flow : flowType.getFlows()) {

							flow.setHasTextTags(TextTagReplacer.hasTextTags(flow));
							flow.setHasFileURLs(FCKUtils.hasAbsoluteFileUrls(flow));
							flow.setHasRelativeURLs(URLRewriter.hasAbsoluteLinkUrls(flow));

							flow.setFlowType(flowType);
							flowMap.put(flow.getFlowID(), flow);
						}

						flowType.setFlows(null);
					}
				}

				this.flowTypes = flowTypes;

				if (!flowMap.isEmpty()) {

					this.flowMap = flowMap;
					this.latestPublishedFlowVersionsMap = getLatestPublishedFlowVersionsMap(flowMap.values());
					this.calculatePopularFlows();

					createFlowIndexer();

				} else {

					this.flowMap = null;
					this.latestPublishedFlowVersionsMap = null;
					this.flowIndexer = null;
					this.flowIndexer = null;
				}

			}

			systemInterface.getEventHandler().sendEvent(FlowBrowserModule.class, new FlowBrowserCacheEvent(), EventTarget.LOCAL);

		} finally {

			w.unlock();
		}
	}

	private void createFlowIndexer() {

		try {
			if (this.latestPublishedFlowVersionsMap == null) {

				this.flowIndexer = null;

			} else {

				this.flowIndexer = new FlowIndexer(latestPublishedFlowVersionsMap.values(), maxHitCount);
			}

		} catch (IOException e) {

			log.error("Error indexing flows, searching disabled", e);

			this.flowIndexer = null;
		}
	}

	private void calculatePopularFlows() {

		log.info("Calculating popular flows...");

		w.lock();

		try {

			if (this.flowTypes == null || this.latestPublishedFlowVersionsMap == null) {

				return;
			}

			List<FlowFamily> popularFamilies = new ArrayList<FlowFamily>(latestPublishedFlowVersionsMap.size());

			for (FlowType flowType : this.flowTypes) {

				List<FlowFamily> flowTypePopularFamilies = getPopularFamilies(flowType);

				if (popularFamilies != null && flowTypePopularFamilies != null) {

					popularFamilies.addAll(flowTypePopularFamilies);
				}
			}

			for (Flow flow : this.latestPublishedFlowVersionsMap.values()) {

				flow.setPopular(popularFamilies.contains(flow.getFlowFamily()));
			}

		} catch (SQLException e) {

			log.error("Error calculating popular flow", e);

		} finally {

			w.unlock();
		}

	}

	private List<FlowFamily> getPopularFamilies(FlowType flowType) throws SQLException {

		//Get ID of all families for this flow type with at least one published flow
		List<Integer> familyIDs = new ArrayListQuery<Integer>(dataSource, "SELECT DISTINCT flowFamilyID FROM flowengine_flows WHERE flowTypeID = " + flowType.getFlowTypeID() + " AND publishDate <= CURDATE() AND (unPublishDate IS NULL OR unPublishDate > CURDATE());", IntegerPopulator.getPopulator()).executeQuery();

		if (familyIDs != null) {

			ArrayList<FlowFamily> flowFamilies = new ArrayList<FlowFamily>(familyIDs.size());

			PopularFlowFamiliesModule.getFlowFamilyPopularity(dataSource, familyIDs, flowFamilies, popularInterval);

			if (flowFamilies.size() > this.popularFlowCount) {

				Collections.sort(flowFamilies, FAMILY_COMPARATOR);

				return flowFamilies.subList(0, popularFlowCount);
			}

			return flowFamilies;
		}

		return null;
	}

	private LinkedHashMap<Integer, Flow> getLatestPublishedFlowVersionsMap(Collection<Flow> flows) {

		HashMap<Integer, Flow> latestPublishedFlowVersionsMap = new HashMap<Integer, Flow>();

		for (Flow flow : flows) {

			if (!flow.isPublished()) {

				continue;
			}

			Flow mapFlow = latestPublishedFlowVersionsMap.get(flow.getFlowFamily().getFlowFamilyID());

			if (mapFlow == null || mapFlow.getVersion() < flow.getVersion()) {

				latestPublishedFlowVersionsMap.put(flow.getFlowFamily().getFlowFamilyID(), flow);
			}
		}

		if (latestPublishedFlowVersionsMap.isEmpty()) {

			return null;
		}

		ArrayList<Flow> flowList = new ArrayList<Flow>(latestPublishedFlowVersionsMap.values());

		Collections.sort(flowList, NameComparator.getInstance());

		LinkedHashMap<Integer, Flow> flowMap = new LinkedHashMap<Integer, Flow>();

		for (Flow flow : flowList) {

			flowMap.put(flow.getFlowFamily().getFlowFamilyID(), flow);

		}

		return flowMap;

	}

	protected ForegroundModuleResponse handleFlowLimitExceededException(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser, FlowLimitExceededException e) throws IOException {

		return null;
	}

	@Override
	public void processEvent(CRUDEvent<?> event, EventSource source) {

		try {
			log.info("Received crud event regarding " + event.getAction() + " of " + event.getBeans().size() + " beans with " + event.getBeanClass());
			cacheFlows();
		} catch (SQLException e) {
			log.error("Error caching flows", e);
		}
	}

	public Collection<Flow> getLatestPublishedFlowVersions() {

		if (latestPublishedFlowVersionsMap == null) {

			return null;
		}

		return latestPublishedFlowVersionsMap.values();
	}

	public Flow getLatestPublishedFlowVersion(Integer flowFamilyID) {

		if (latestPublishedFlowVersionsMap == null) {

			return null;
		}

		return latestPublishedFlowVersionsMap.get(flowFamilyID);
	}

	public LinkedHashMap<Integer, Flow> getLatestPublishedFlowVersionMap() {

		return latestPublishedFlowVersionsMap;
	}

	@Override
	protected Flow getBareFlow(Integer flowID) throws SQLException {

		r.lock();

		try {

			if (flowMap != null) {

				return flowMap.get(flowID);
			}

			return null;

		} finally {

			r.unlock();
		}
	}

	@Override
	public void run() {

		r.lock();

		try {

			log.info("Refreshing list of latest published flow versions...");

			if (flowMap != null) {

				this.latestPublishedFlowVersionsMap = getLatestPublishedFlowVersionsMap(flowMap.values());

				createFlowIndexer();
				
				systemInterface.getEventHandler().sendEvent(FlowBrowserModule.class, new FlowBrowserCacheEvent(), EventTarget.LOCAL);
			}

		} finally {

			r.unlock();
		}
	}

	@Override
	public List<SettingDescriptor> getSettings() {

		ArrayList<SettingDescriptor> settingDescriptors = new ArrayList<SettingDescriptor>();

		try {

			List<FlowType> flowTypes = daoFactory.getFlowTypeDAO().getAll();

			List<ValueDescriptor> valueDescriptors = new ArrayList<ValueDescriptor>();

			if (flowTypes != null) {

				for (FlowType flowType : flowTypes) {

					valueDescriptors.add(new ValueDescriptor(flowType.getName(), flowType.getFlowTypeID()));

				}

			}

			settingDescriptors.add(SettingDescriptor.createMultiListSetting("flowTypeIDs", "Flow types", "Flow types to show in this module", false, null, valueDescriptors));

		} catch (SQLException e) {

			log.error("Unable to create setting descriptor for flowTypes", e);

		}

		ModuleUtils.addSettings(settingDescriptors, super.getSettings());

		return settingDescriptors;

	}

	@Override
	public boolean isMutable(ImmutableFlowInstance flowInstance, User user) {

		if (flowInstance.getStatus() == null || flowInstance.getStatus().isUserMutable()) {

			return true;
		}

		return false;
	}

	@Override
	public String getSubmitActionID() {

		return SUBMIT_ACTION_ID;
	}

	@Override
	public String getSaveActionID() {

		return SAVE_ACTION_ID;
	}

	@Override
	public String getPaymentActionID() {

		return PAYMENT_ACTION_ID;
	}

	@Override
	public String getMultiSigningActionID() {

		return MULTI_SIGNING_ACTION_ID;
	}

	public String getUserFavouriteModuleAlias() {

		return userFavouriteModuleAlias;
	}

	@Override
	public void appendFormData(Document doc, Element baseElement, MutableFlowInstanceManager instanceManager, HttpServletRequest req, User user) {

	}

	@Override
	public void appendShowFlowInstanceData(Document doc, Element baseElement, FlowInstanceManager instanceManager, HttpServletRequest req, User user) {

	}

	@WebPublic(alias = "pdf")
	public ForegroundModuleResponse getEventPDF(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws URINotFoundException, SQLException, IOException, AccessDeniedException, ModuleConfigurationException {

		try {
			sendEventPDF(req, res, user, uriParser, new SessionAccessController(req.getSession(), SESSION_ACCESS_CONTROLLER_TAG), pdfProvider, false);

		} catch (FlowDisabledException e) {

			return list(req, res, user, uriParser, FLOW_DISABLED_VALIDATION_ERROR);
		}

		return null;
	}

	@WebPublic(alias = "submitted")
	public ForegroundModuleResponse showSubmittedMessage(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws FlowInstanceManagerClosedException, UnableToGetQueryInstanceShowHTMLException, AccessDeniedException, ModuleConfigurationException, SQLException, URINotFoundException {

		try {
			return super.showImmutableFlowInstance(req, res, user, uriParser, new SessionAccessController(req.getSession(), SESSION_ACCESS_CONTROLLER_TAG), this, ShowMode.SUBMIT, DEFAULT_REQUEST_METADATA);

		} catch (AccessDeniedException e) {

			throw new URINotFoundException(uriParser);
		}
	}

	@WebPublic(alias = "multisign")
	public ForegroundModuleResponse showMultiSignMessage(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws FlowInstanceManagerClosedException, UnableToGetQueryInstanceShowHTMLException, AccessDeniedException, ModuleConfigurationException, SQLException, URINotFoundException {

		return super.showMultiSignMessage(req, res, user, uriParser, new SessionAccessController(req.getSession(), SESSION_ACCESS_CONTROLLER_TAG), this, false);
	}

	@WebPublic(alias = "pay")
	public ForegroundModuleResponse showPaymentForm(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws FlowInstanceManagerClosedException, UnableToGetQueryInstanceShowHTMLException, AccessDeniedException, ModuleConfigurationException, SQLException, URINotFoundException {

		return super.showPaymentForm(req, res, user, uriParser, new SessionAccessController(req.getSession(), SESSION_ACCESS_CONTROLLER_TAG), this, false);
	}

	@Override
	protected void redirectToSubmitMethod(MutableFlowInstanceManager flowInstanceManager, HttpServletRequest req, HttpServletResponse res) throws IOException {

		SessionAccessController.setSessionAttribute(flowInstanceManager.getFlowInstanceID(), req.getSession(), SESSION_ACCESS_CONTROLLER_TAG);

		redirectToMethod(req, res, "/submitted/" + flowInstanceManager.getFlowInstanceID());
	}

	@Override
	protected void onFlowInstanceClosedRedirect(FlowInstanceManager flowInstanceManager, HttpServletRequest req, HttpServletResponse res) throws IOException {

		redirectToDefaultMethod(req, res);

	}

	@Override
	protected Breadcrumb getFlowInstanceSubmitBreadcrumb(ImmutableFlowInstance flowInstance, HttpServletRequest req, URIParser uriParser) {

		//TODO add prefix
		return new Breadcrumb(this, flowInstance.getFlow().getName(), "/submitted/" + flowInstance.getFlowInstanceID());
	}

	@Override
	protected String getBaseUpdateURL(HttpServletRequest req, URIParser uriParser, User user, ImmutableFlowInstance flowInstance, FlowInstanceAccessController accessController) {

		if (!accessController.isMutable(flowInstance, user)) {

			return null;
		}

		return req.getContextPath() + uriParser.getFormattedURI();
	}

	@Override
	protected SigningProvider getSigningProvider() {

		return signingProvider;
	}

	@Override
	protected PaymentProvider getPaymentProvider() {

		return paymentProvider;
	}

	@Override
	public String getPaymentFailURL(MutableFlowInstanceManager instanceManager, HttpServletRequest req) {

		return RequestUtils.getFullContextPathURL(req) + this.getFullAlias() + "/flow/" + instanceManager.getFlowID() + "/" + instanceManager.getFlowInstanceID() + "?preview=1&paymentprovidererror=1";
	}

	@Override
	public String getSignFailURL(MutableFlowInstanceManager instanceManager, HttpServletRequest req) {

		return RequestUtils.getFullContextPathURL(req) + this.getFullAlias() + "/flow/" + instanceManager.getFlowID() + "/" + instanceManager.getFlowInstanceID() + "?preview=1&signprovidererror=1";
	}

	@Override
	public String getSaveAndSubmitURL(MutableFlowInstanceManager instanceManager, HttpServletRequest req) {

		if(instanceManager.getFlowInstanceID() != null){
			
			return RequestUtils.getFullContextPathURL(req) + this.getFullAlias() + "/flow/" + instanceManager.getFlowID() + "/" + instanceManager.getFlowInstanceID() + "?save-submit=1&nopost=1";
			
		} else {
			
			return RequestUtils.getFullContextPathURL(req) + this.getFullAlias() + "/flow/" + instanceManager.getFlowID() + "?save-submit=1&nopost=1";
		}
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
	protected void reOpenFlowInstance(Integer flowID, Integer flowInstanceID, HttpServletRequest req, User user, URIParser uriParser) {

		if(flowInstanceID != null){

			try {
				getSavedMutableFlowInstanceManager(flowID, flowInstanceID, this, req.getSession(true), user, uriParser, req, true, true, true, DEFAULT_REQUEST_METADATA);

			} catch (Exception e) {

				log.error("Error reopening flow instance with ID " + flowInstanceID + " for user " + user, e);
			}
		}
	}

	@Override
	protected MultiSigningHandler getMultiSigningProvider() {

		return multiSigningHandler;
	}

	public void multiSigningComplete(FlowInstanceManager instanceManager, SiteProfile siteProfile, String signingChainID) {

		boolean requiresPayment = requiresPayment(instanceManager);

		EventType eventType;
		String actionID;
		
		Map<String,String> eventAttributes = new HashMap<String, String>();
		eventAttributes.put(BaseFlowModule.SIGNING_CHAIN_ID_FLOW_INSTANCE_EVENT_ATTRIBUTE, signingChainID);
		
		if (requiresPayment) {
			
			actionID = FlowBrowserModule.PAYMENT_ACTION_ID;
			eventType = EventType.STATUS_UPDATED;
			
		} else if (instanceManager.getFlowInstance().getFirstSubmitted() != null) {
			
			actionID = UserFlowInstanceModule.SUBMIT_COMPLETION_ACTION_ID;
			eventType = EventType.SUBMITTED;
			
		} else {
			
			actionID = FlowBrowserModule.SUBMIT_ACTION_ID;
			eventType = EventType.SUBMITTED;
		}

		Status nextStatus = (Status) instanceManager.getFlowInstance().getFlow().getDefaultState(actionID);

		if (nextStatus == null) {

			log.error("Unable to find status for actionID " + actionID + " for flow instance " + instanceManager + ", flow instance will be left with wrong status.");
			return;
		}

		try {
			FlowInstance flowInstance = (FlowInstance) instanceManager.getFlowInstance();

			Timestamp currentTimestamp = TimeUtils.getCurrentTimestamp();

			flowInstance.setStatus(nextStatus);
			flowInstance.setLastStatusChange(currentTimestamp);

			if (flowInstance.getFirstSubmitted() == null && !requiresPayment) {

				flowInstance.setFirstSubmitted(currentTimestamp);
			}
			
			List<MultiSigningQuery> multiSigningQueries = instanceManager.getQueries(MultiSigningQuery.class);

			if (multiSigningQueries != null) {

				for (MultiSigningQuery multiSigningQuery : multiSigningQueries) {

					if (multiSigningQuery.getQueryInstanceDescriptor().getQueryState() != QueryState.HIDDEN && !CollectionUtils.isEmpty(multiSigningQuery.getSigningParties())) {

						for (SigningParty signingParty : multiSigningQuery.getSigningParties()) {
							
							if (signingParty.isAddAsOwner()) {
							
								User signer = null;
								
								if (!StringUtils.isEmpty(signingParty.getSocialSecurityNumber())) {
								
									signer = systemInterface.getUserHandler().getUserByAttribute("citizenIdentifier", signingParty.getSocialSecurityNumber(), false, true);
								}
								
								if (signer == null && !StringUtils.isEmpty(signingParty.getEmail())) {
									
									signer = systemInterface.getUserHandler().getUserByEmail(signingParty.getEmail(), false, true);
								}
								
								if (signer != null) {
									
									if (flowInstance.getOwners() == null || !flowInstance.getOwners().contains(signer)) {
										
										if (flowInstance.getOwners() == null) {
											flowInstance.setOwners(new ArrayList<User>());
										}
										
										flowInstance.getOwners().add(signer);
									}
									
								} else {
									
									log.error("User for signing party " + signingParty + " not found");
								}
							}
						}
					}
				}
			}
			
			HighLevelQuery<FlowInstance> updateQuery = new HighLevelQuery<FlowInstance>(FlowInstance.OWNERS_RELATION);
			daoFactory.getFlowInstanceDAO().update(flowInstance, updateQuery);
			
			ImmutableFlowInstanceEvent posterSignEvent = SigningUtils.getLastPosterSignEvents(flowInstance);

			FlowInstanceEvent event = addFlowInstanceEvent(flowInstance, eventType, null, posterSignEvent.getPoster(), currentTimestamp, eventAttributes);

			eventHandler.sendEvent(FlowInstance.class, new CRUDEvent<FlowInstance>(CRUDAction.UPDATE, flowInstance), EventTarget.ALL);

			if (!requiresPayment) {

				sendSubmitEvent(instanceManager, event, actionID, siteProfile, true);
			}

		} catch (SQLException e) {

			log.error("Error changing status and adding event for flow instance " + instanceManager + ", flow instance will be left with wrong status.", e);
		}
	}

	@Override
	public int getPriority() {

		return 0;
	}

	@Override
	public String getAbsoluteFileURL(URIParser uriParser, Object object) {

		if (ckConnectorModuleAlias != null) {

			return uriParser.getContextPath() + ckConnectorModuleAlias;
		}

		return null;
	}

	@WebPublic(toLowerCase = true)
	public ForegroundModuleResponse getFlowForm(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Throwable {

		if (flowAdminModule == null) {

			throw new URINotFoundException(uriParser);
		}

		Integer flowFormID;
		Integer flowID;
		Flow flow;

		if (uriParser.size() == 4 && (flowID = uriParser.getInt(2)) != null && (flowFormID = uriParser.getInt(3)) != null && (flow = flowMap.get(flowID)) != null) {

			if (!flow.isPublished() || !flow.isEnabled()) {

				log.info("User " + user + " requested flow " + flow + " form PDF which is no longer available.");

				return list(req, res, user, uriParser, FLOW_NO_LONGER_AVAILABLE_VALIDATION_ERROR);

			}

			try {
				for (FlowForm flowForm : flow.getFlowForms()) {
					
					if (flowForm.getFlowFormID().equals(flowFormID)) {
						
						flowForm.setFlow(flow);
						
						return flowAdminModule.sendFlowForm(flowForm, req, res, user, uriParser, getCurrentSiteProfile(req, user, uriParser, flow.getFlowFamily()), false);
					}
				}

			} catch (FlowNotAvailiableInRequestedFormat e) {

				log.warn("User " + user + " requested PDF form for flow " + flow + " which has no PDF form available.");
			}
		}

		throw new URINotFoundException(uriParser);
	}

	public boolean getOpenExternalFlowsInNewWindow() {

		return openExternalFlowsInNewWindow;
	}

	protected void appendAdditionalListFlowsXML(Document doc, Element element) {

	}


	public FlowIndexer getFlowIndexer() {

		return flowIndexer;
	}

	public String getSearchHints() {

		return searchHints;
	}

}
