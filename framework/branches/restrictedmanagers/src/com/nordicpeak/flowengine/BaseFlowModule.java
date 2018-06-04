package com.nordicpeak.flowengine;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URL;
import java.net.URLEncoder;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import javax.xml.transform.TransformerException;

import org.apache.commons.fileupload.FileUploadBase.FileSizeLimitExceededException;
import org.apache.commons.fileupload.FileUploadBase.SizeLimitExceededException;
import org.apache.commons.fileupload.FileUploadException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.nordicpeak.flowengine.beans.DefaultInstanceMetadata;
import com.nordicpeak.flowengine.beans.DefaultStatusMapping;
import com.nordicpeak.flowengine.beans.Flow;
import com.nordicpeak.flowengine.beans.FlowFamily;
import com.nordicpeak.flowengine.beans.FlowInstance;
import com.nordicpeak.flowengine.beans.FlowInstanceEvent;
import com.nordicpeak.flowengine.beans.FlowType;
import com.nordicpeak.flowengine.beans.QueryDescriptor;
import com.nordicpeak.flowengine.beans.QueryInstanceDescriptor;
import com.nordicpeak.flowengine.beans.QueryResponse;
import com.nordicpeak.flowengine.beans.RequestMetadata;
import com.nordicpeak.flowengine.beans.Status;
import com.nordicpeak.flowengine.beans.Step;
import com.nordicpeak.flowengine.beans.SubmitCheckFailedResponse;
import com.nordicpeak.flowengine.dao.FlowEngineDAOFactory;
import com.nordicpeak.flowengine.enums.ContentType;
import com.nordicpeak.flowengine.enums.EventType;
import com.nordicpeak.flowengine.enums.FlowAction;
import com.nordicpeak.flowengine.enums.FlowDirection;
import com.nordicpeak.flowengine.enums.QueryState;
import com.nordicpeak.flowengine.enums.ShowMode;
import com.nordicpeak.flowengine.events.MultiSigningInitiatedEvent;
import com.nordicpeak.flowengine.events.SubmitEvent;
import com.nordicpeak.flowengine.exceptions.FlowEngineException;
import com.nordicpeak.flowengine.exceptions.evaluation.EvaluationException;
import com.nordicpeak.flowengine.exceptions.evaluationprovider.EvaluationProviderErrorException;
import com.nordicpeak.flowengine.exceptions.evaluationprovider.EvaluationProviderException;
import com.nordicpeak.flowengine.exceptions.evaluationprovider.EvaluationProviderNotFoundException;
import com.nordicpeak.flowengine.exceptions.evaluationprovider.EvaluatorNotFoundInEvaluationProviderException;
import com.nordicpeak.flowengine.exceptions.flow.FlowDefaultStatusNotFound;
import com.nordicpeak.flowengine.exceptions.flow.FlowDisabledException;
import com.nordicpeak.flowengine.exceptions.flow.FlowException;
import com.nordicpeak.flowengine.exceptions.flow.FlowLimitExceededException;
import com.nordicpeak.flowengine.exceptions.flow.FlowNoLongerAvailableException;
import com.nordicpeak.flowengine.exceptions.flow.FlowNotAvailiableInRequestedFormat;
import com.nordicpeak.flowengine.exceptions.flow.FlowNotPublishedException;
import com.nordicpeak.flowengine.exceptions.flowinstance.FlowInstanceNoLongerAvailableException;
import com.nordicpeak.flowengine.exceptions.flowinstance.InvalidFlowInstanceStepException;
import com.nordicpeak.flowengine.exceptions.flowinstance.MissingQueryInstanceDescriptor;
import com.nordicpeak.flowengine.exceptions.flowinstancemanager.DuplicateFlowInstanceManagerIDException;
import com.nordicpeak.flowengine.exceptions.flowinstancemanager.FlowInstanceManagerClosedException;
import com.nordicpeak.flowengine.exceptions.flowinstancemanager.QueryInstanceNotFoundInFlowInstanceManagerException;
import com.nordicpeak.flowengine.exceptions.queryinstance.QueryModificationException;
import com.nordicpeak.flowengine.exceptions.queryinstance.QueryRequestException;
import com.nordicpeak.flowengine.exceptions.queryinstance.QueryRequestsNotSupported;
import com.nordicpeak.flowengine.exceptions.queryinstance.SubmitCheckException;
import com.nordicpeak.flowengine.exceptions.queryinstance.UnableToGetQueryInstanceFormHTMLException;
import com.nordicpeak.flowengine.exceptions.queryinstance.UnableToGetQueryInstanceShowHTMLException;
import com.nordicpeak.flowengine.exceptions.queryinstance.UnableToPopulateQueryInstanceException;
import com.nordicpeak.flowengine.exceptions.queryinstance.UnableToResetQueryInstanceException;
import com.nordicpeak.flowengine.exceptions.queryinstance.UnableToSaveQueryInstanceException;
import com.nordicpeak.flowengine.exceptions.queryprovider.QueryInstanceNotFoundInQueryProviderException;
import com.nordicpeak.flowengine.exceptions.queryprovider.QueryProviderErrorException;
import com.nordicpeak.flowengine.exceptions.queryprovider.QueryProviderException;
import com.nordicpeak.flowengine.exceptions.queryprovider.QueryProviderNotFoundException;
import com.nordicpeak.flowengine.interfaces.EvaluationHandler;
import com.nordicpeak.flowengine.interfaces.FlowEngineInterface;
import com.nordicpeak.flowengine.interfaces.FlowInstanceAccessController;
import com.nordicpeak.flowengine.interfaces.FlowPaymentProvider;
import com.nordicpeak.flowengine.interfaces.FlowProcessCallback;
import com.nordicpeak.flowengine.interfaces.FlowSubmitSurveyProvider;
import com.nordicpeak.flowengine.interfaces.Icon;
import com.nordicpeak.flowengine.interfaces.ImmutableFlow;
import com.nordicpeak.flowengine.interfaces.ImmutableFlowFamily;
import com.nordicpeak.flowengine.interfaces.ImmutableFlowInstance;
import com.nordicpeak.flowengine.interfaces.ImmutableFlowInstanceEvent;
import com.nordicpeak.flowengine.interfaces.ImmutableQueryInstance;
import com.nordicpeak.flowengine.interfaces.ImmutableStatus;
import com.nordicpeak.flowengine.interfaces.InstanceMetadata;
import com.nordicpeak.flowengine.interfaces.InvoiceLine;
import com.nordicpeak.flowengine.interfaces.MultiSigningHandler;
import com.nordicpeak.flowengine.interfaces.OperatingStatus;
import com.nordicpeak.flowengine.interfaces.PDFProvider;
import com.nordicpeak.flowengine.interfaces.PaymentQuery;
import com.nordicpeak.flowengine.interfaces.QueryHandler;
import com.nordicpeak.flowengine.interfaces.QueryRequestProcessor;
import com.nordicpeak.flowengine.interfaces.SigningCallback;
import com.nordicpeak.flowengine.interfaces.SigningProvider;
import com.nordicpeak.flowengine.interfaces.XMLProvider;
import com.nordicpeak.flowengine.managers.FlowInstanceManager;
import com.nordicpeak.flowengine.managers.ImmutableFlowInstanceManager;
import com.nordicpeak.flowengine.managers.ManagerResponse;
import com.nordicpeak.flowengine.managers.MutableFlowInstanceManager;
import com.nordicpeak.flowengine.managers.MutableFlowInstanceManager.FlowInstanceManagerRegistery;
import com.nordicpeak.flowengine.utils.AttributeTagUtils;
import com.nordicpeak.flowengine.utils.FlowInstanceEventGenerator;
import com.nordicpeak.flowengine.utils.FlowInstanceUtils;
import com.nordicpeak.flowengine.utils.MultiSignUtils;
import com.nordicpeak.flowengine.utils.SigningUtils;
import com.nordicpeak.flowengine.validationerrors.FileUploadValidationError;

import se.unlogic.fileuploadutils.MultipartRequest;
import se.unlogic.hierarchy.core.annotations.CheckboxSettingDescriptor;
import se.unlogic.hierarchy.core.annotations.InstanceManagerDependency;
import se.unlogic.hierarchy.core.annotations.ModuleSetting;
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
import se.unlogic.hierarchy.core.interfaces.ForegroundModuleResponse;
import se.unlogic.hierarchy.core.interfaces.SystemInterface;
import se.unlogic.hierarchy.core.interfaces.ViewFragment;
import se.unlogic.hierarchy.core.interfaces.events.EventHandler;
import se.unlogic.hierarchy.core.utils.AccessUtils;
import se.unlogic.hierarchy.core.utils.ViewFragmentUtils;
import se.unlogic.hierarchy.foregroundmodules.AnnotatedForegroundModule;
import se.unlogic.openhierarchy.foregroundmodules.siteprofile.interfaces.SiteProfile;
import se.unlogic.openhierarchy.foregroundmodules.siteprofile.interfaces.SiteProfileHandler;
import se.unlogic.standardutils.collections.CollectionUtils;
import se.unlogic.standardutils.collections.ReverseListIterator;
import se.unlogic.standardutils.dao.HighLevelQuery;
import se.unlogic.standardutils.dao.LowLevelQuery;
import se.unlogic.standardutils.dao.QueryParameterFactory;
import se.unlogic.standardutils.date.DateUtils;
import se.unlogic.standardutils.io.BinarySizes;
import se.unlogic.standardutils.io.FileUtils;
import se.unlogic.standardutils.json.JsonObject;
import se.unlogic.standardutils.numbers.NumberUtils;
import se.unlogic.standardutils.object.ObjectUtils;
import se.unlogic.standardutils.string.AnnotatedBeanTagSourceFactory;
import se.unlogic.standardutils.string.TagReplacer;
import se.unlogic.standardutils.time.MillisecondTimeUnits;
import se.unlogic.standardutils.time.TimeUtils;
import se.unlogic.standardutils.validation.PositiveStringIntegerValidator;
import se.unlogic.standardutils.validation.ValidationError;
import se.unlogic.standardutils.xml.XMLUtils;
import se.unlogic.webutils.http.HTTPUtils;
import se.unlogic.webutils.http.RequestUtils;
import se.unlogic.webutils.http.SessionUtils;
import se.unlogic.webutils.http.URIParser;
import se.unlogic.webutils.http.enums.ContentDisposition;

public abstract class BaseFlowModule extends AnnotatedForegroundModule implements FlowEngineInterface {

	public static final Field[] FLOW_RELATIONS = { Flow.FLOW_TYPE_RELATION, Flow.FLOW_FAMILY_RELATION, Flow.STEPS_RELATION, Flow.STATUSES_RELATION, Step.QUERY_DESCRIPTORS_RELATION, QueryDescriptor.EVALUATOR_DESCRIPTORS_RELATION, Flow.DEFAULT_FLOW_STATE_MAPPINGS_RELATION, DefaultStatusMapping.FLOW_STATE_RELATION, FlowType.ALLOWED_GROUPS_RELATION, FlowType.ALLOWED_USERS_RELATION, FlowFamily.MANAGER_USERS_RELATION, FlowFamily.MANAGER_GROUPS_RELATION };
	public static final Field[] FLOW_INSTANCE_RELATIONS = { FlowInstance.OWNERS_RELATION, FlowInstance.EVENTS_RELATION, FlowInstanceEvent.ATTRIBUTES_RELATION, FlowInstance.FLOW_RELATION, FlowInstance.STATUS_RELATION, Flow.FLOW_TYPE_RELATION, Flow.FLOW_FAMILY_RELATION, Flow.STEPS_RELATION, Flow.STATUSES_RELATION, FlowType.ALLOWED_ADMIN_GROUPS_RELATION, FlowType.ALLOWED_ADMIN_USERS_RELATION, FlowFamily.MANAGER_GROUPS_RELATION, FlowFamily.MANAGER_USERS_RELATION, Step.QUERY_DESCRIPTORS_RELATION, QueryDescriptor.EVALUATOR_DESCRIPTORS_RELATION, Flow.DEFAULT_FLOW_STATE_MAPPINGS_RELATION, DefaultStatusMapping.FLOW_STATE_RELATION, QueryDescriptor.QUERY_INSTANCE_DESCRIPTORS_RELATION, FlowInstance.ATTRIBUTES_RELATION, FlowInstance.MANAGERS_RELATION, FlowInstance.MANAGER_GROUPS_RELATION };
	public static final Field[] FLOW_INSTANCE_SAVED_MUTABLE_ACCESS_CHECK_RELATIONS = { FlowInstance.OWNERS_RELATION, FlowInstance.FLOW_RELATION, FlowInstance.STATUS_RELATION, Flow.FLOW_TYPE_RELATION, Flow.FLOW_FAMILY_RELATION, FlowFamily.MANAGER_GROUPS_RELATION, FlowFamily.MANAGER_USERS_RELATION, FlowInstance.MANAGERS_RELATION, FlowInstance.MANAGER_GROUPS_RELATION };

	public static final ValidationError PREVIEW_NOT_ENABLED_VALIDATION_ERROR = new ValidationError("PreviewNotEnabledForCurrentFlow");
	public static final ValidationError PREVIEW_ONLY_WHEN_FULLY_POPULATED_VALIDATION_ERROR = new ValidationError("PreviewOnlyAvailableWhenFlowFullyPopulated");
	public static final ValidationError SUBMIT_ONLY_WHEN_FULLY_POPULATED_VALIDATION_ERROR = new ValidationError("SubmitOnlyAvailableWhenFlowFullyPopulated");

	public static final ValidationError UNABLE_TO_POPULATE_QUERY_INSTANCE_VALIDATION_ERROR = new ValidationError("UnableToPopulateQueryInstance");
	public static final ValidationError UNABLE_TO_RESET_QUERY_INSTANCE_VALIDATION_ERROR = new ValidationError("UnableToResetQueryInstance");
	public static final ValidationError UNABLE_TO_SAVE_QUERY_INSTANCE_VALIDATION_ERROR = new ValidationError("UnableToSaveQueryInstance");

	public static final ValidationError FLOW_INSTANCE_NOT_FOUND_VALIDATION_ERROR = new ValidationError("RequestedFlowInstanceNotFound");
	public static final ValidationError INVALID_LINK_VALIDATION_ERROR = new ValidationError("InvalidLinkRequested");
	public static final ValidationError FLOW_DISABLED_VALIDATION_ERROR = new ValidationError("FlowDisabled");
	public static final ValidationError FLOW_NOT_AVAILIABLE_IN_REQUESTED_FORMAT_VALIDATION_ERROR = new ValidationError("FlowNotAvailiableInRequestedFormat");

	public static final ValidationError FLOW_INSTANCE_PREVIEW_VALIDATION_ERROR = new ValidationError("FlowInstancePreviewError");
	public static final ValidationError UNABLE_TO_LOAD_FLOW_INSTANCE_VALIDATION_ERROR = new ValidationError("FlowInstanceLoadError");

	public static final ValidationError SIGNING_PROVIDER_NOT_FOUND_VALIDATION_ERROR = new ValidationError("SigningProviderNotFoundError");
	public static final ValidationError PAYMENT_PROVIDER_NOT_FOUND_VALIDATION_ERROR = new ValidationError("PaymentProviderNotFoundError");
	public static final ValidationError MULTI_SIGNING_PROVIDER_NOT_FOUND_VALIDATION_ERROR = new ValidationError("MultiSigningProviderNotFoundError");
	
	private static final String SIGN_FLOW_MODIFICATION_COUNT_INSTANCE_MANAGER_ATTRIBUTE = "flowinstance.sign.modificationcount";
	private static final String PAYMENT_FLOW_MODIFICATION_COUNT_INSTANCE_MANAGER_ATTRIBUTE = "flowinstance.payment.modificationcount";
	public static final String SIGNING_CHAIN_ID_FLOW_INSTANCE_EVENT_ATTRIBUTE = "signingChainID";

	protected static final URL DEFAULT_FLOW_ICON = BaseFlowModule.class.getResource("staticcontent/pics/flow_default.png");
	
	private static final AnnotatedBeanTagSourceFactory<FlowInstance> FLOWINSTANCE_TAG_SOURCE_FACTORY = new AnnotatedBeanTagSourceFactory<FlowInstance>(FlowInstance.class, "$flowInstance.");
	
	@ModuleSetting(allowsNull = true)
	@TextFieldSettingDescriptor(name = "Temp dir", description = "Directory for temporary files. Should be on the same filesystem as the file store for best performance. If not set system default temp directory will be used")
	protected String tempDir;

	@ModuleSetting
	@TextFieldSettingDescriptor(name = "Max request size", description = "Maxmium allowed request size in megabytes", required = true, formatValidator = PositiveStringIntegerValidator.class)
	protected Integer maxRequestSize = 1000;

	@ModuleSetting
	@TextFieldSettingDescriptor(name = "RAM threshold", description = "Maximum size of files in KB to be buffered in RAM during file uploads. Files exceeding the threshold are written to disk instead.", required = true, formatValidator = PositiveStringIntegerValidator.class)
	protected Integer ramThreshold = 500;

	@ModuleSetting
	@CheckboxSettingDescriptor(name = "Allow creation of old published versions", description = "Controls if creation of old published versions should be allowed.")
	protected boolean allowOldPublishedVersions = false;

	@InstanceManagerDependency(required = true)
	protected QueryHandler queryHandler;

	@InstanceManagerDependency(required = true)
	protected EvaluationHandler evaluationHandler;

	@InstanceManagerDependency
	protected SiteProfileHandler profileHandler;

	@InstanceManagerDependency
	protected OperatingMessageModule operatingMessageModule;

	protected EventHandler eventHandler;

	protected FlowEngineDAOFactory daoFactory;

	protected QueryParameterFactory<Flow, Integer> flowIDParamFactory;
	protected QueryParameterFactory<FlowType, Integer> flowTypeIDParamFactory;
	protected QueryParameterFactory<FlowInstance, Integer> flowInstanceIDParamFactory;
	protected QueryParameterFactory<FlowInstanceEvent, Integer> eventIDParamFactory;
	protected QueryParameterFactory<FlowInstanceEvent, FlowInstance> eventFlowInstanceParamFactory;
	protected QueryParameterFactory<QueryInstanceDescriptor, Integer> queryInstanceDescriptorFlowInstanceIDParamFactory;

	protected FlowInstanceEventGenerator flowInstanceEventGenerator;
	
	@Override
	protected void createDAOs(DataSource dataSource) throws Exception {

		super.createDAOs(dataSource);

		this.daoFactory = new FlowEngineDAOFactory(dataSource, systemInterface.getUserHandler(), systemInterface.getGroupHandler());
		flowIDParamFactory = daoFactory.getFlowDAO().getParamFactory("flowID", Integer.class);
		flowTypeIDParamFactory = daoFactory.getFlowTypeDAO().getParamFactory("flowTypeID", Integer.class);
		flowInstanceIDParamFactory = daoFactory.getFlowInstanceDAO().getParamFactory("flowInstanceID", Integer.class);
		eventIDParamFactory = daoFactory.getFlowInstanceEventDAO().getParamFactory("eventID", Integer.class);
		eventFlowInstanceParamFactory = daoFactory.getFlowInstanceEventDAO().getParamFactory("flowInstance", FlowInstance.class);
		queryInstanceDescriptorFlowInstanceIDParamFactory = daoFactory.getQueryInstanceDescriptorDAO().getParamFactory("flowInstanceID", Integer.class);

		flowInstanceEventGenerator = new FlowInstanceEventGenerator(daoFactory);
		
		eventHandler = systemInterface.getEventHandler();
	}
	
	protected MutableFlowInstanceManager getSavedMutableFlowInstanceManager(int flowID, int flowInstanceID, FlowInstanceAccessController callback, HttpSession session, User user, URIParser uriParser, HttpServletRequest req, boolean loadFromDBIfNeeded, boolean checkPublishDate, boolean checkEnabled, RequestMetadata requestMetadata) throws FlowNoLongerAvailableException, SQLException, FlowInstanceNoLongerAvailableException, AccessDeniedException, FlowNotPublishedException, FlowDisabledException, DuplicateFlowInstanceManagerIDException, MissingQueryInstanceDescriptor, QueryProviderNotFoundException, InvalidFlowInstanceStepException, QueryProviderErrorException, QueryInstanceNotFoundInQueryProviderException, FlowDisabledException, EvaluationProviderNotFoundException, EvaluationProviderErrorException, EvaluatorNotFoundInEvaluationProviderException, EvaluationException, UnableToResetQueryInstanceException {

		if (session == null) {

			throw new RuntimeException("Session cannot be null");
		}

		synchronized (session) {

			//TODO check if the status has changed since this instance was opened!

			// Check if the user already has an instance of this flow open in his session
			MutableFlowInstanceManager instanceManager = getMutableFlowInstanceManagerFromSession(flowID, flowInstanceID, session);

			if (instanceManager != null) {

				checkFlow(instanceManager, session, checkPublishDate, checkEnabled, requestMetadata.isManager());

				FlowInstance dbFlowInstance;

				// Check if the flow instance still exists in DB
				if ((dbFlowInstance = getFlowInstanceForSavedMutableAccessCheck(instanceManager.getFlowInstanceID())) == null) {

					this.removeMutableFlowInstanceManagerFromSession(instanceManager, session);

					throw new FlowInstanceNoLongerAvailableException(instanceManager.getFlowInstance());
				}

				// Check the status of flow instance if it is still user mutable etc...
				try {
					callback.checkFlowInstanceAccess(dbFlowInstance, user);

				} catch (AccessDeniedException e) {

					this.removeMutableFlowInstanceManagerFromSession(instanceManager, session);
					throw e;
				}

				if (dbFlowInstance.getUpdated() != null && (instanceManager.getFlowInstance().getUpdated() == null || instanceManager.getFlowInstance().getUpdated().before(dbFlowInstance.getUpdated()))) {

					instanceManager.setConcurrentModificationLock(true);
				}
				
				if (dbFlowInstance.getLastStatusChange() != null && (instanceManager.getFlowInstance().getLastStatusChange() == null || instanceManager.getFlowInstance().getLastStatusChange().before(dbFlowInstance.getLastStatusChange()))) {

					instanceManager.setConcurrentModificationLock(true);
				}

				if (log.isDebugEnabled()) {

					log.debug("Found flow instance " + instanceManager.getFlowInstance() + " in session of user " + user);
				}

				return instanceManager;

			} else if (!loadFromDBIfNeeded) {

				return null;
			}

			// User does not have the requested flow instance open, get flow instance from DB and create a new instance manager
			FlowInstance flowInstance = getFlowInstance(flowInstanceID);

			if (flowInstance == null) {

				return null;
			}

			callback.checkFlowInstanceAccess(flowInstance, user);

			if (checkEnabled && (!flowInstance.getFlow().isEnabled() || isOperatingStatusDisabled(flowInstance.getFlow(), requestMetadata.isManager()))) {

				throw new FlowDisabledException(flowInstance.getFlow());
			}

			if (checkPublishDate && !flowInstance.getFlow().isPublished()) {

				throw new FlowNotPublishedException(flowInstance.getFlow());
			}

			log.info("Opening copy of flow instance " + flowInstance + " for user " + user);

			InstanceMetadata instanceMetadata = new DefaultInstanceMetadata(getSiteProfile(flowInstance));

			instanceManager = new MutableFlowInstanceManager(flowInstance, queryHandler, evaluationHandler, getNewInstanceManagerID(user), req, user, instanceMetadata, requestMetadata, getAbsoluteFileURL(uriParser, flowInstance.getFlow()));

			addMutableFlowInstanceManagerToSession(flowID, flowInstanceID, instanceManager, session);

			return instanceManager;
		}
	}

	protected MutableFlowInstanceManager getUnsavedMutableFlowInstanceManager(int flowID, FlowInstanceAccessController callback, HttpSession session, User user, User poster, SiteProfile profile, URIParser uriParser, HttpServletRequest req, boolean createInstanceIfNeeded, boolean checkPublishDate, boolean checkEnabled, boolean checkFlowTypeAccess, RequestMetadata requestMetadata) throws FlowNoLongerAvailableException, SQLException, AccessDeniedException, FlowNotPublishedException, FlowDisabledException, DuplicateFlowInstanceManagerIDException, QueryProviderNotFoundException, QueryProviderErrorException, QueryInstanceNotFoundInQueryProviderException, FlowDisabledException, EvaluationProviderNotFoundException, EvaluationProviderErrorException, EvaluatorNotFoundInEvaluationProviderException, FlowLimitExceededException, FlowNotAvailiableInRequestedFormat, EvaluationException, UnableToResetQueryInstanceException {
		
		if (session == null) {

			throw new RuntimeException("Session cannot be null");
		}

		synchronized (session) {

			// Check if the user already has an instance of this flow open in
			// his session
			MutableFlowInstanceManager instanceManager = (MutableFlowInstanceManager) session.getAttribute(Constants.FLOW_INSTANCE_SESSION_PREFIX + flowID + ":" + null);

			if (instanceManager != null) {

				checkFlow(instanceManager, session, checkPublishDate, checkEnabled, requestMetadata.isManager());

				log.debug("Found existing instance of flow " + instanceManager.getFlowInstance().getFlow() + " in session of user " + user);

				return instanceManager;

			} else if (!createInstanceIfNeeded) {

				return null;
			}

			// User has no instance open of the requested flow, get flow from DB
			// and create a new instance manager
			Flow flow = getFlow(flowID);

			if (flow == null) {

				return null;

			} else if (!flow.isInternal() || flow.getSteps() == null) {

				throw new FlowNotAvailiableInRequestedFormat(flowID);
			}

			if(checkFlowTypeAccess && !AccessUtils.checkAccess(user, flow.getFlowType().getUserAccessInterface())){

				throw new AccessDeniedException("Access to flow " + flow + " denied");
			}

			checkFlowLimit(user, flow);

			if (profile == null) {
				
				profile = getCurrentSiteProfile(req, poster, uriParser, flow.getFlowFamily());
			}
			
			callback.checkNewFlowInstanceAccess(flow, user, profile);

			if (checkEnabled && (!flow.isEnabled() || isOperatingStatusDisabled(flow, requestMetadata.isManager()))) {

				throw new FlowDisabledException(flow);
			}

			if (checkPublishDate && !flow.isPublished()) {

				throw new FlowNotPublishedException(flow);
			}

			if (checkPublishDate && !allowOldPublishedVersions && !isLatestPublishedVersion(flow)) {

				throw new FlowNotPublishedException(flow);
			}

			log.info("Creating new instance of flow " + flow + " for user " + user);
			
			InstanceMetadata instanceMetadata = new DefaultInstanceMetadata(profile);

			instanceManager = new MutableFlowInstanceManager(flow, queryHandler, evaluationHandler, getNewInstanceManagerID(user), req, user, poster, instanceMetadata, requestMetadata, getAbsoluteFileURL(uriParser, flow));

			session.setAttribute(Constants.FLOW_INSTANCE_SESSION_PREFIX + flowID + ":" + null, instanceManager);

			return instanceManager;
		}
	}

	protected abstract String getAbsoluteFileURL(URIParser uriParser, Object object);


	protected void checkFlowLimit(User user, Flow flow) throws FlowLimitExceededException, SQLException {

	}

	protected void checkFlow(MutableFlowInstanceManager instanceManager, HttpSession session, boolean checkPublishDate, boolean checkEnabled, boolean manager) throws FlowNoLongerAvailableException, SQLException, FlowDisabledException, FlowNotPublishedException {

		Flow flow;

		// Check if the flow still exists in DB!
		if ((flow = getBareFlow(instanceManager.getFlowID())) == null) {

			this.removeMutableFlowInstanceManagerFromSession(instanceManager, session);

			throw new FlowNoLongerAvailableException(instanceManager.getFlowInstance().getFlow());
		}

		if (checkEnabled && (!flow.isEnabled() || isOperatingStatusDisabled(instanceManager.getFlowInstance().getFlow(), manager))) {

			this.removeMutableFlowInstanceManagerFromSession(instanceManager, session);

			throw new FlowDisabledException(flow);
		}

		if (checkPublishDate && !flow.isPublished()) {

			this.removeMutableFlowInstanceManagerFromSession(instanceManager, session);

			throw new FlowNotPublishedException(instanceManager.getFlowInstance().getFlow());
		}
	}

	public boolean isOperatingStatusDisabled(ImmutableFlow flow, boolean manager) {

		if (operatingMessageModule != null) {

			OperatingStatus operatingStatus = operatingMessageModule.getOperatingStatus(flow.getFlowFamily().getFlowFamilyID(), manager);

			if (operatingStatus != null && operatingStatus.isDisabled()) {

				return true;
			}
		}

		return false;
	}

	public ImmutableFlowInstanceManager getImmutableFlowInstanceManager(int flowInstanceID, FlowInstanceAccessController accessController, User user, boolean checkEnabled, HttpServletRequest req, URIParser uriParser, boolean manager) throws AccessDeniedException, SQLException, FlowDisabledException, DuplicateFlowInstanceManagerIDException, MissingQueryInstanceDescriptor, QueryProviderNotFoundException, InvalidFlowInstanceStepException, QueryProviderErrorException, QueryInstanceNotFoundInQueryProviderException {

		FlowInstance flowInstance = getFlowInstance(flowInstanceID);

		if (flowInstance == null) {

			return null;
		}

		if(accessController != null){
			
			accessController.checkFlowInstanceAccess(flowInstance, user);
		}

		if (checkEnabled && (!flowInstance.getFlow().isEnabled() || isOperatingStatusDisabled(flowInstance.getFlow(), manager))) {

			throw new FlowDisabledException(flowInstance.getFlow());
		}

		return new ImmutableFlowInstanceManager(flowInstance, queryHandler, req, new DefaultInstanceMetadata(getSiteProfile(flowInstance)), getAbsoluteFileURL(uriParser, flowInstance.getFlow()));
	}

	public ImmutableFlowInstanceManager getImmutableFlowInstanceManager(int flowInstanceID) throws SQLException, MissingQueryInstanceDescriptor, QueryProviderNotFoundException, InvalidFlowInstanceStepException, QueryProviderErrorException, QueryInstanceNotFoundInQueryProviderException {

		FlowInstance flowInstance = getFlowInstance(flowInstanceID);

		if (flowInstance == null) {

			return null;
		}

		return new ImmutableFlowInstanceManager(flowInstance, queryHandler, null, new DefaultInstanceMetadata(getSiteProfile(flowInstance)), null);
	}
	
	public static void addMutableFlowInstanceManagerToSession(int flowID, Integer flowInstanceID, MutableFlowInstanceManager instanceManager, HttpSession session) {

		session.setAttribute(Constants.FLOW_INSTANCE_SESSION_PREFIX + flowID + ":" + flowInstanceID, instanceManager);
	}

	public static MutableFlowInstanceManager getMutableFlowInstanceManagerFromSession(int flowID, Integer flowInstanceID, HttpSession session) {

		return (MutableFlowInstanceManager) session.getAttribute(Constants.FLOW_INSTANCE_SESSION_PREFIX + flowID + ":" + flowInstanceID);
	}

	public void removeMutableFlowInstanceManagerFromSession(MutableFlowInstanceManager instanceManager, HttpSession session) {

		removeFlowInstanceManagerFromSession(instanceManager.getFlowID(), instanceManager.getFlowInstanceID(), session);

		if (!instanceManager.isClosed()) {

			instanceManager.close(this.getQueryHandler());
		}
	}

	public static void removeFlowInstanceManagerFromSession(int flowID, Integer flowInstanceID, HttpSession session) {

		if (session == null) {

			return;
		}

		SessionUtils.removeAttribute(Constants.FLOW_INSTANCE_SESSION_PREFIX + flowID + ":" + flowInstanceID, session);
	}

	public ForegroundModuleResponse processFlowRequest(MutableFlowInstanceManager instanceManager, FlowProcessCallback callback, FlowInstanceAccessController accessController, HttpServletRequest req, HttpServletResponse res, User user, User poster, URIParser uriParser, boolean enableSaving, RequestMetadata requestMetadata) throws UnableToGetQueryInstanceFormHTMLException, SQLException, IOException, UnableToGetQueryInstanceShowHTMLException, ModuleConfigurationException, FlowInstanceManagerClosedException, FlowDefaultStatusNotFound, EvaluationException, SubmitCheckException {

		MultipartRequest multipartRequest = null;

		try {
			if (!(req instanceof MultipartRequest) && MultipartRequest.isMultipartRequest(req)) {

				log.debug("Parsing multipart request from user " + user + " for flow instance " + instanceManager.getFlowInstance());
				multipartRequest = new MultipartRequest(this.ramThreshold * BinarySizes.KiloByte, this.maxRequestSize * BinarySizes.MegaByte, tempDir, req);
				req = multipartRequest;
			}

			FlowAction flowAction = parseAction(req);

			synchronized (instanceManager) {

				ManagerResponse managerResponse = null;

				if (req.getMethod().equals("POST") && req.getParameter("nopost") == null) {

					if (flowAction == null) {

						Integer queryID = NumberUtils.toInt(req.getParameter("queryID"));

						if (queryID != null) {

							log.info("User " + user + " populating single query with ID " + queryID + " in flow instance " + instanceManager);

							String response = null;

							try {
								response = instanceManager.populateQueryInCurrentStep(req, user, poster, queryID, queryHandler, evaluationHandler, getMutableQueryRequestBaseURL(req, instanceManager), requestMetadata, getSiteProfile(instanceManager));
							} catch (QueryModificationException e) {
								log.error("Error populating queryID " + queryID + " in flow instance " + instanceManager, e);
							}

							if (response != null) {

								res.setHeader("AjaxPostValid", "true");

							} else {

								response = new JsonObject().toJson();

							}

							HTTPUtils.sendReponse(response, "application/json;charset=" + systemInterface.getEncoding(), systemInterface.getEncoding(), res);

							return null;
						}
					}

					FlowDirection flowDirection = parseFlowDirection(req, flowAction);

					managerResponse = instanceManager.populateCurrentStep(req, user, poster, flowDirection, queryHandler, evaluationHandler, getMutableQueryRequestBaseURL(req, instanceManager), requestMetadata, getSiteProfile(instanceManager));

					if (managerResponse.hasValidationErrors()) {

						// Show form for current step
						log.info("Validation errors detected in POST from user " + user + " for flow instance " + instanceManager);
						return showCurrentStepForm(instanceManager, callback, req, res, user, poster, uriParser, managerResponse, null, flowAction, requestMetadata);
					}
				}

				if (instanceManager.isConcurrentModificationLocked()) {

					if (flowAction == FlowAction.SAVE_AND_CLOSE || flowAction == FlowAction.SAVE_AND_SUBMIT || flowAction == FlowAction.SAVE) {

						flowAction = null;

					} else if (flowAction == FlowAction.SAVE_AND_PREVIEW) {

						flowAction = FlowAction.PREVIEW;
					}
				}

				if (flowAction == null) {

					Integer stepID = NumberUtils.toInt(req.getParameter("step"));

					if (stepID != null) {

						if (instanceManager.setStep(stepID)) {

							log.info("User " + user + " changing step in flow instance " + instanceManager + " to step " + instanceManager.getCurrentStep());

						} else {

							log.info("Invalid step change requested by user " + user + " for flow instance " + instanceManager + " which is in step " + instanceManager.getCurrentStep());
						}
					}

					// Show form for current step
					return showCurrentStepForm(instanceManager, callback, req, res, user, poster, uriParser, managerResponse, null, null, requestMetadata);

				} else if (flowAction == FlowAction.CLOSE_AND_REOPEN) {

					log.info("User " + user + " closing and reopening flow instance " + instanceManager.getFlowInstance());

					try {
						instanceManager.close(queryHandler);
					} catch (Exception e) {

						log.error("Error closing flow instance " + instanceManager.getFlowInstance() + " in session of user " + user, e);
					}

					removeMutableFlowInstanceManagerFromSession(instanceManager, req.getSession());

					reOpenFlowInstance(instanceManager.getFlowID(), instanceManager.getFlowInstanceID(), req, res, user, uriParser);
					return null;

				} else if (flowAction == FlowAction.SAVE) {

					if (user != null) {

						boolean previouslySaved = instanceManager.isPreviouslySaved();

						save(instanceManager, user, poster, req, callback.getSaveActionID(), EventType.UPDATED, null);

						// Check if we need to redirect to new url
						if (!previouslySaved && enableSaving) {

							res.sendRedirect(uriParser.getRequestURL() + "/" + instanceManager.getFlowInstanceID() + "?saved=1");
							return null;
						}
					}

					return showCurrentStepForm(instanceManager, callback, req, res, user, poster, uriParser, managerResponse, null, flowAction, requestMetadata);

				} else if (flowAction == FlowAction.PREVIEW || flowAction == FlowAction.SAVE_AND_PREVIEW) {

					if (flowAction == FlowAction.SAVE_AND_PREVIEW && user != null) {

						boolean previouslySaved = instanceManager.isPreviouslySaved();

						save(instanceManager, user, poster, req, callback.getSaveActionID(), EventType.UPDATED, null);

						// Check if we need to redirect to new url
						if (!previouslySaved) {

							if (enableSaving) {

								res.sendRedirect(uriParser.getRequestURL() + "/" + instanceManager.getFlowInstanceID() + "?preview=1");

							} else {

								res.sendRedirect(uriParser.getRequestURL() + "?preview=1");
							}

							return null;
						}
					}

					// Check if preview is enabled for the flow
					if (!instanceManager.getFlowInstance().getFlow().usesPreview()) {

						// Show form for current step
						log.info("Preview denied for user " + user + " requesting flow instance " + instanceManager.getFlowInstance() + ", flow does NOT have preview enabled");
						return showCurrentStepForm(instanceManager, callback, req, res, user, poster, uriParser, managerResponse, PREVIEW_NOT_ENABLED_VALIDATION_ERROR, flowAction, requestMetadata);

					} else if (!instanceManager.isFullyPopulated()) {

						log.info("Preview denied for user " + user + " requesting flow instance " + instanceManager.getFlowInstance() + ", instance is NOT fully populated");
						return showCurrentStepForm(instanceManager, callback, req, res, user, poster, uriParser, managerResponse, PREVIEW_ONLY_WHEN_FULLY_POPULATED_VALIDATION_ERROR, flowAction, requestMetadata);
					}

					return showPreview(req, user, poster, uriParser, instanceManager, callback, flowAction, getBaseUpdateURL(req, uriParser, user, instanceManager.getFlowInstance(), accessController), null, requestMetadata);

				} else if (flowAction == FlowAction.SAVE_AND_SUBMIT) {

					// Check if instance is fully populated, save and then
					// display submitted message
					if (!instanceManager.isFullyPopulated()) {

						log.info("Save & submit denied for user " + user + " requesting flow instance " + instanceManager.getFlowInstance() + ", instance is NOT fully populated");
						return showCurrentStepForm(instanceManager, callback, req, res, user, poster, uriParser, managerResponse, SUBMIT_ONLY_WHEN_FULLY_POPULATED_VALIDATION_ERROR, flowAction, requestMetadata);
					}
					
					SubmitCheckFailedResponse submitCheckResponse = instanceManager.checkValidForSubmit(poster, queryHandler, getBaseUpdateURL(req, uriParser, user, instanceManager.getFlowInstance(), accessController), requestMetadata);
					
					if (submitCheckResponse != null) {
						
						log.info("Save & submit denied for user " + user + " requesting flow instance " + instanceManager.getFlowInstance() + ", instance is NOT valid for submit due to validation error in query " + submitCheckResponse.getQueryInstance().getQueryInstanceDescriptor());
						res.sendRedirect(submitCheckResponse.getRedirectURL());
						return null;
					}

					if (enableSaving && instanceManager.getFlowInstance().getFlow().requiresSigning()) {

						SigningProvider signingProvider = getSigningProvider();

						if (signingProvider == null) {

							if (instanceManager.getFlowInstance().getFlow().usesPreview()) {

								return showPreview(req, user, poster, uriParser, instanceManager, callback, flowAction, getBaseUpdateURL(req, uriParser, user, instanceManager.getFlowInstance(), accessController), SIGNING_PROVIDER_NOT_FOUND_VALIDATION_ERROR, requestMetadata);

							} else {

								return showCurrentStepForm(instanceManager, callback, req, res, user, poster, uriParser, managerResponse, SIGNING_PROVIDER_NOT_FOUND_VALIDATION_ERROR, flowAction, requestMetadata);
							}
						}

						boolean modifiedSinceLastSignRequest = false;
						
						if (instanceManager.hasUnsavedChanges() && user != null) {
							
							log.info("User " + user + " saving and preparing to sign flow instance " + instanceManager.getFlowInstance());
							
							boolean previouslySaved = instanceManager.isPreviouslySaved();
							
							save(instanceManager, user, poster, req, callback.getSaveActionID(), EventType.UPDATED, null);
							
							if (!previouslySaved) {
								
								res.sendRedirect(getSaveAndSubmitURL(instanceManager, req));
								return null;
							}
							
						} else if (!instanceManager.getSessionAttributeHandler().isSet(SIGN_FLOW_MODIFICATION_COUNT_INSTANCE_MANAGER_ATTRIBUTE)) {
							
							log.info("User " + user + " preparing to sign flow instance " + instanceManager.getFlowInstance());
							
							instanceManager.getSessionAttributeHandler().setAttribute(SIGN_FLOW_MODIFICATION_COUNT_INSTANCE_MANAGER_ATTRIBUTE, instanceManager.getChangesCounter());
							
						} else if (instanceManager.getChangesCounter() != instanceManager.getSessionAttributeHandler().getInt(SIGN_FLOW_MODIFICATION_COUNT_INSTANCE_MANAGER_ATTRIBUTE)) {
							
							log.info("User " + user + " modified unsaved flow instance and is re-preparing to sign flow instance " + instanceManager.getFlowInstance());
							
							instanceManager.getSessionAttributeHandler().setAttribute(SIGN_FLOW_MODIFICATION_COUNT_INSTANCE_MANAGER_ATTRIBUTE, instanceManager.getChangesCounter());
							modifiedSinceLastSignRequest = true;
						}

						try {

							SigningCallback signingCallback;

							SiteProfile instanceProfile = getSiteProfile(instanceManager);
							
							if (MultiSignUtils.requiresMultiSigning(instanceManager)) {

								signingCallback = getSigningCallback(instanceManager, poster, null, callback.getMultiSigningActionID(), instanceProfile, false);

							} else if (requiresPayment(instanceManager)) {

								signingCallback = getSigningCallback(instanceManager, poster, null, callback.getPaymentActionID(), instanceProfile, false);

							} else {

								signingCallback = getSigningCallback(instanceManager, poster, EventType.SUBMITTED, callback.getSubmitActionID(), instanceProfile, true);
							}

							ViewFragment viewFragment = signingProvider.sign(req, res, user, instanceManager, signingCallback, modifiedSinceLastSignRequest);

							if (res.isCommitted()) {

								return null;

							} else if (viewFragment == null) {

								log.warn("Signing provider returned no view fragment and not committed not direct response for signing of flow instance " + instanceManager + " by user " + user);

								redirectToSignError(req, res, uriParser, instanceManager);

								return null;
							}

							return showSigningForm(instanceManager, req, res, user, uriParser, viewFragment);

						} catch (Exception e) {

							log.error("Error ivoking signing provider " + signingProvider + " for flow instance " + instanceManager + " requested by user " + user, e);

							redirectToSignError(req, res, uriParser, instanceManager);

							return null;
						}

					} else if (enableSaving && requiresPayment(instanceManager)) {

						FlowPaymentProvider paymentProvider = getFlowPaymentProvider();

						if (paymentProvider == null) {

							if (instanceManager.getFlowInstance().getFlow().usesPreview()) {

								return showPreview(req, user, poster, uriParser, instanceManager, callback, flowAction, getBaseUpdateURL(req, uriParser, user, instanceManager.getFlowInstance(), accessController), PAYMENT_PROVIDER_NOT_FOUND_VALIDATION_ERROR, requestMetadata);

							} else {

								return showCurrentStepForm(instanceManager, callback, req, res, user, poster, uriParser, managerResponse, PAYMENT_PROVIDER_NOT_FOUND_VALIDATION_ERROR, flowAction, requestMetadata);
							}
						}

						boolean previouslySaved = instanceManager.isPreviouslySaved();

						if (instanceManager.hasUnsavedChanges()) {

							log.info("User " + user + " saving and preparing to pay flow instance " + instanceManager.getFlowInstance());

							save(instanceManager, user, poster, req, callback.getSaveActionID(), EventType.UPDATED, null);
							
							savedFlowInstanceForPayment(instanceManager, user, req);

						} else {

							log.info("User " + user + " preparing to pay flow instance " + instanceManager.getFlowInstance());
						}

						if (!previouslySaved) {

							res.sendRedirect(getSaveAndSubmitURL(instanceManager, req));

							return null;
						}
						
						try {
							
							SiteProfile instanceProfile = getSiteProfile(instanceManager);

							ViewFragment viewFragment = paymentProvider.pay(req, res, user, uriParser, instanceManager, new BaseFlowModuleInlinePaymentCallback(this, poster, instanceProfile, callback.getSubmitActionID()));

							if (res.isCommitted()) {

								return null;

							} else if (viewFragment == null) {

								log.warn("Payment provider returned no view fragment and not committed not direct response for pay of flow instance " + instanceManager + " by user " + user);

								redirectToPaymentError(multipartRequest, res, uriParser, instanceManager);

								return null;
							}

							return showInlinePaymentForm(instanceManager, req, res, user, uriParser, viewFragment);

						} catch (Exception e) {

							log.error("Error invoking payment provider " + paymentProvider + " for flow instance " + instanceManager + " requested by user " + user, e);

							redirectToPaymentError(multipartRequest, res, uriParser, instanceManager);

							return null;
						}
					}

					log.info("User " + user + " saving and submitting flow instance " + instanceManager.getFlowInstance());

					FlowInstanceEvent event = save(instanceManager, user, poster, req, callback.getSubmitActionID(), EventType.SUBMITTED, null);

					if (enableSaving) {

						sendSubmitEvent(instanceManager, event, callback.getSubmitActionID(), getSiteProfile(instanceManager), false);
					}

					removeFlowInstanceManagerFromSession(instanceManager.getFlowID(), instanceManager.getFlowInstanceID(), req.getSession(false));

					closeSubmittedFlowInstanceManager(instanceManager, req);

					redirectToSubmitMethod(instanceManager, req, res);

					return null;

				} else if (flowAction == FlowAction.SAVE_AND_CLOSE && user != null) {

					log.info("User " + user + " saving and closing flow instance " + instanceManager.getFlowInstance());

					FlowInstanceEvent event = save(instanceManager, user, poster, req, callback.getSaveActionID(), EventType.UPDATED, null);

					removeMutableFlowInstanceManagerFromSession(instanceManager, req.getSession(false));

					flowInstanceSavedAndClosed(instanceManager, req, res, user, event);

					return null;

				} else {

					throw new RuntimeException("Unhandled " + FlowAction.class.getSimpleName() + " enum value " + flowAction);
				}
			}

		} catch (FileUploadException e) {

			if (!(e instanceof FileSizeLimitExceededException) && !(e instanceof SizeLimitExceededException)) {

				log.warn("Unable to parse request for flow instance " + instanceManager + " from user " + user, e);
			}

			return showCurrentStepForm(instanceManager, callback, req, res, user, poster, uriParser, null, new FileUploadValidationError(this.maxRequestSize), null, requestMetadata);

		} catch (UnableToPopulateQueryInstanceException e) {

			log.error("Error populating flow instance " + instanceManager + " from user " + user, e);

			return showCurrentStepForm(instanceManager, callback, req, res, user, poster, uriParser, null, UNABLE_TO_POPULATE_QUERY_INSTANCE_VALIDATION_ERROR, null, requestMetadata);

		} catch (UnableToResetQueryInstanceException e) {

			log.error("Error populating flow instance " + instanceManager + " from user " + user, e);

			return showCurrentStepForm(instanceManager, callback, req, res, user, poster, uriParser, null, UNABLE_TO_RESET_QUERY_INSTANCE_VALIDATION_ERROR, null, requestMetadata);

		} catch (UnableToSaveQueryInstanceException e) {

			log.error("Unable to save flow instance " + instanceManager + " from user " + user, e);

			return showCurrentStepForm(instanceManager, callback, req, res, user, poster, uriParser, null, UNABLE_TO_SAVE_QUERY_INSTANCE_VALIDATION_ERROR, null, requestMetadata);

		} finally {

			if (multipartRequest != null) {

				multipartRequest.deleteFiles();
			}
		}
	}

	protected void savedFlowInstanceForPayment(MutableFlowInstanceManager instanceManager, User user, HttpServletRequest req) {
		
	}

	protected void reOpenFlowInstance(Integer flowID, Integer flowInstanceID, HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws IOException {

		res.sendRedirect(uriParser.getRequestURL());
	}

	protected void closeSubmittedFlowInstanceManager(MutableFlowInstanceManager instanceManager, HttpServletRequest req) {

		instanceManager.close(queryHandler);
	}

	protected SigningCallback getSigningCallback(MutableFlowInstanceManager instanceManager, User poster, EventType eventType, String actionID, SiteProfile siteProfile, boolean addSubmitEvent) {

		return new BaseFlowModuleSigningCallback(this, poster, actionID, eventType, siteProfile, addSubmitEvent);
	}

	protected void redirectToSignError(HttpServletRequest req, HttpServletResponse res, URIParser uriParser, MutableFlowInstanceManager instanceManager) throws IOException {

		String preview = instanceManager.getFlowInstance().getFlow().usesPreview() ? "&preview=1" : "";
		
		if (instanceManager.getFlowInstanceID() != null) {

			res.sendRedirect(uriParser.getRequestURL() + "/" + instanceManager.getFlowInstanceID() + "?signprovidererror=1" + preview);

		} else {

			res.sendRedirect(uriParser.getRequestURL() + "?signprovidererror=1" + preview);
		}
	}

	protected void redirectToPaymentError(HttpServletRequest req, HttpServletResponse res, URIParser uriParser, MutableFlowInstanceManager instanceManager) throws IOException {

		String preview = instanceManager.getFlowInstance().getFlow().usesPreview() ? "&preview=1" : "";
		
		if (instanceManager.getFlowInstanceID() != null) {

			res.sendRedirect(uriParser.getRequestURL() + "/" + instanceManager.getFlowInstanceID() + "?paymentprovidererror=1" + preview);

		} else {

			res.sendRedirect(uriParser.getRequestURL() + "?paymentprovidererror=1" + preview);
		}
	}

	protected SigningProvider getSigningProvider() {

		return null;
	}

	protected FlowPaymentProvider getFlowPaymentProvider() {

		return null;
	}

	protected MultiSigningHandler getMultiSigningHandler() {

		return null;
	}

	protected abstract void redirectToSubmitMethod(MutableFlowInstanceManager instanceManager, HttpServletRequest req, HttpServletResponse res) throws IOException;

	protected abstract void flowInstanceSavedAndClosed(FlowInstanceManager instanceManager, HttpServletRequest req, HttpServletResponse res, User user, FlowInstanceEvent event) throws IOException;

	public SiteProfile getCurrentSiteProfile(HttpServletRequest req, User user, URIParser uriParser, ImmutableFlowFamily flowFamily) {

		if (profileHandler != null) {

			SiteProfile profile = profileHandler.getCurrentProfile(user, req, uriParser);

			if (profile != null && flowFamily != null) {

				SiteProfile subProfile = profileHandler.getMatchingSubProfile(profile.getProfileID(), "flowfamily-" + flowFamily.getFlowFamilyID());

				if (subProfile != null) {

					return subProfile;
				}
			}

			return profile;
		}

		return null;
	}
	
	public SiteProfile getSiteProfile(FlowInstanceManager instanceManager) {
		
		return getSiteProfile(instanceManager.getFlowInstance());
	}
	
	public SiteProfile getSiteProfile(ImmutableFlowInstance flowInstance) {

		if (profileHandler != null) {

			SiteProfile profile = profileHandler.getProfile(flowInstance.getProfileID());

			if (profile != null && flowInstance.getFlow().getFlowFamily() != null) {

				SiteProfile subProfile = profileHandler.getMatchingSubProfile(profile.getProfileID(), "flowfamily-" + flowInstance.getFlow().getFlowFamily().getFlowFamilyID());

				if (subProfile != null) {

					return subProfile;
				}
			}

			return profile;
		}

		return null;
	}

	protected void processQueryRequest(FlowInstanceManager instanceManager, int queryID, HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws QueryInstanceNotFoundInFlowInstanceManagerException, QueryRequestsNotSupported, QueryRequestException {

		ImmutableQueryInstance queryInstance;

		QueryRequestProcessor queryRequestProcessor;

		synchronized (instanceManager) {

			queryInstance = instanceManager.getQueryInstance(queryID);

			if (queryInstance == null) {

				//TODO change hierarchy for this exception
				throw new QueryInstanceNotFoundInFlowInstanceManagerException(queryID, instanceManager.getFlowInstance());
			}

			try {
				queryRequestProcessor = queryInstance.getQueryRequestProcessor(req, user, queryHandler);

			} catch (Exception e) {

				throw new QueryRequestException(queryInstance.getQueryInstanceDescriptor(), e);
			}

			if (queryRequestProcessor == null) {

				throw new QueryRequestsNotSupported(queryInstance.getQueryInstanceDescriptor());
			}
		}

		try {
			queryRequestProcessor.processRequest(req, res, user, uriParser);

		} catch (Exception e) {

			throw new QueryRequestException(queryInstance.getQueryInstanceDescriptor(), e);

		} finally {

			if (queryRequestProcessor != null) {

				try {
					queryRequestProcessor.close();

				} catch (Exception e) {

					log.error("Error closing query request processor from query " + queryInstance.getQueryInstanceDescriptor() + " in flow instance " + instanceManager.getFlowInstance() + " requested by user " + user, e);
				}
			}
		}
	}

	protected abstract String getBaseUpdateURL(HttpServletRequest req, URIParser uriParser, User user, ImmutableFlowInstance flowInstance, FlowInstanceAccessController accessController);

	protected String getMutableQueryRequestBaseURL(HttpServletRequest req, MutableFlowInstanceManager instanceManager) {

		String baseURL = req.getContextPath() + this.getFullAlias() + "/mquery/" + instanceManager.getFlowID();

		if (instanceManager.isPreviouslySaved()) {

			baseURL += "/" + instanceManager.getFlowInstanceID() + "/q/";

		} else {

			baseURL += "/q/";
		}

		return baseURL;
	}

	protected String getImmutableQueryRequestBaseURL(HttpServletRequest req, FlowInstanceManager instanceManager) {

		return req.getContextPath() + this.getFullAlias() + "/iquery/" + instanceManager.getFlowID() + "/" + instanceManager.getFlowInstanceID() + "/q/";
	}
	
	protected FlowInstanceEvent save(MutableFlowInstanceManager instanceManager, User user, User poster, HttpServletRequest req, String actionID, EventType eventType, Map<String,String> eventAttributes) throws FlowInstanceManagerClosedException, UnableToSaveQueryInstanceException, SQLException, FlowDefaultStatusNotFound {

		HttpSession session = null;

		if (req != null) {
			session = req.getSession(false);
		}

		boolean wasPreviouslySaved = instanceManager.isPreviouslySaved();

		log.info("User " + user + " saving flow instance " + instanceManager.getFlowInstance());

		Timestamp previousStatusChange = instanceManager.getFlowInstance().getLastStatusChange();
		
		setFlowStatus(instanceManager, actionID);

		Timestamp saveTimestamp = null;
		
		if (ObjectUtils.compare(previousStatusChange, instanceManager.getFlowInstance().getLastStatusChange())) {
			
			saveTimestamp = TimeUtils.getCurrentTimestamp(false);
			
		} else {
			
			saveTimestamp = instanceManager.getFlowInstance().getLastStatusChange();
		}
		
		Timestamp savedTimestamp = instanceManager.saveInstance(this, user, poster, eventType, saveTimestamp);

		CRUDAction crudAction;

		if (!wasPreviouslySaved) {

			rebindFlowInstance(session, instanceManager);

			crudAction = CRUDAction.ADD;

		} else {

			crudAction = CRUDAction.UPDATE;
		}

		FlowInstanceEvent event = null;

		if (!instanceManager.getFlowInstance().getStatus().getContentType().equals(ContentType.NEW)) {

			event = flowInstanceEventGenerator.addFlowInstanceEvent(instanceManager.getFlowInstance(), eventType, null, user, savedTimestamp, eventAttributes);
		}

		eventHandler.sendEvent(FlowInstance.class, new CRUDEvent<FlowInstance>(crudAction, (FlowInstance) instanceManager.getFlowInstance()), EventTarget.ALL);

		return event;
	}

	protected void setFlowStatus(MutableFlowInstanceManager instanceManager, String actionID) throws FlowDefaultStatusNotFound {

		if (actionID != null) {
			
			if (instanceManager.getFlowInstance().getAttributeHandler() != null && instanceManager.getFlowInstance().getAttributeHandler().isSet(actionID + ".override")) {
				
				Integer statusID = instanceManager.getFlowInstance().getAttributeHandler().getInt(actionID + ".override");
				
				if (statusID != null) {
					
					if (instanceManager.getFlowInstance().getFlow().getStatuses() != null) {
						
						Status overrideStatus = null;
						
						for (ImmutableStatus status : instanceManager.getFlowInstance().getFlow().getStatuses()) {
							
							if (status.getStatusID().equals(statusID)) {
								
								overrideStatus = (Status) status;
								break;
							}
						}
						
						if (overrideStatus != null) {
							
							log.info("Using flow status override for actionID " + actionID + " to flow status " + overrideStatus);
							instanceManager.setFlowState(overrideStatus);
							return;
						}
						
					} else {
						
						log.error("Found no statuses for flow " + instanceManager.getFlowInstance().getFlow() + ". You need to add the relation Flow.Statuses somewhere.");
					}
				}
			}

			instanceManager.setFlowState((Status) instanceManager.getFlowInstance().getFlow().getDefaultState(actionID));

			if (instanceManager.getFlowState() == null) {

				throw new FlowDefaultStatusNotFound(actionID, instanceManager.getFlowInstance().getFlow());
			}

		}
	}

	protected void rebindFlowInstance(HttpSession session, MutableFlowInstanceManager instanceManager) {

		if (session == null) {

			return;
		}

		FlowInstanceManagerRegistery registery = FlowInstanceManagerRegistery.getInstance();

		try {
			registery.addNonSessionBoundInstance(instanceManager);
			SessionUtils.removeAttribute(Constants.FLOW_INSTANCE_SESSION_PREFIX + instanceManager.getFlowID() + ":" + null, session);
			SessionUtils.setAttribute(Constants.FLOW_INSTANCE_SESSION_PREFIX + instanceManager.getFlowID() + ":" + instanceManager.getFlowInstanceID(), instanceManager, session);
		} finally {
			registery.removeNonSessionBoundInstance(instanceManager);
		}
	}

	protected FlowDirection parseFlowDirection(HttpServletRequest req, FlowAction flowAction) {

		if (flowAction == null) {

			if (req.getParameter("forward") != null) {

				return FlowDirection.FORWARD;

			} else if (req.getParameter("back") != null) {

				return FlowDirection.BACKWARD;
			}

		}else if(flowAction == FlowAction.SAVE || flowAction == FlowAction.SAVE_AND_CLOSE){

			return FlowDirection.STAY_AND_POPULATE_PARTIALLY;
		}

		return FlowDirection.STAY_AND_POPULATE_FULLY;
	}

	protected FlowAction parseAction(HttpServletRequest req) {

		if (req.getParameter("save") != null) {

			return FlowAction.SAVE;

		} else if (req.getParameter("save-submit") != null) {

			return FlowAction.SAVE_AND_SUBMIT;

		} else if (req.getParameter("preview") != null) {

			return FlowAction.PREVIEW;

		} else if (req.getParameter("save-preview") != null) {

			return FlowAction.SAVE_AND_PREVIEW;

		} else if (req.getParameter("save-close") != null) {

			return FlowAction.SAVE_AND_CLOSE;

		} else if (req.getParameter("close-reopen") != null) {

			return FlowAction.CLOSE_AND_REOPEN;
		}

		return null;
	}

	protected ForegroundModuleResponse showCurrentStepForm(MutableFlowInstanceManager instanceManager, FlowProcessCallback callback, HttpServletRequest req, HttpServletResponse res, User user, User poster, URIParser uriParser, ManagerResponse managerResponse, ValidationError validationError, FlowAction lastFlowAction, RequestMetadata requestMetadata) throws FlowInstanceManagerClosedException, UnableToGetQueryInstanceFormHTMLException {

		log.info("User " + user + " requested form for step " + (instanceManager.getCurrentStepIndex() + 1) + ". " + instanceManager.getCurrentStep() + " in flow instance " + instanceManager.getFlowInstance());

		if (managerResponse == null) {

			managerResponse = instanceManager.getCurrentStepFormHTML(queryHandler, req, user, poster, getMutableQueryRequestBaseURL(req, instanceManager), requestMetadata);
		}

		Document doc = createDocument(req, uriParser, user);
		Element flowInstanceManagerFormElement = doc.createElement("FlowInstanceManagerForm");
		doc.getDocumentElement().appendChild(flowInstanceManagerFormElement);

		flowInstanceManagerFormElement.appendChild(instanceManager.getFlowInstance().toXML(doc));
		flowInstanceManagerFormElement.appendChild(managerResponse.toXML(doc));

		if (lastFlowAction == null && req.getParameter("saved") != null) {
			lastFlowAction = FlowAction.SAVE;
		}

		XMLUtils.appendNewElement(doc, flowInstanceManagerFormElement, "lastFlowAction", lastFlowAction);

		if (validationError != null) {

			flowInstanceManagerFormElement.appendChild(validationError.toXML(doc));
		}

		if (poster != null) {
			XMLUtils.appendNewElement(doc, flowInstanceManagerFormElement, "loggedIn");
		}

		callback.appendFormData(doc, flowInstanceManagerFormElement, instanceManager, req, user);

		SimpleForegroundModuleResponse moduleResponse = new SimpleForegroundModuleResponse(doc, instanceManager.getFlowInstance().getFlow().getName(), this.getDefaultBreadcrumb(), this.getFlowBreadcrumb(instanceManager.getFlowInstance()));

		appendLinksAndScripts(moduleResponse, managerResponse);

		return moduleResponse;
	}

	protected ForegroundModuleResponse showPreview(HttpServletRequest req, User user, User poster, URIParser uriParser, MutableFlowInstanceManager instanceManager, FlowProcessCallback callback, FlowAction lastFlowAction, String baseUpdateURL, ValidationError validationError, RequestMetadata requestMetadata) throws UnableToGetQueryInstanceShowHTMLException, FlowInstanceManagerClosedException {

		log.info("User " + user + " requested preview of flow instance " + instanceManager.getFlowInstance());
		
		req.setAttribute("BaseFlowModule.preview", "preview");

		List<ManagerResponse> managerResponses = instanceManager.getFullShowHTML(req, user, poster, this, true, baseUpdateURL, getMutableQueryRequestBaseURL(req, instanceManager), requestMetadata);

		Document doc = createDocument(req, uriParser, user);
		Element flowInstanceManagerPreviewElement = doc.createElement("FlowInstanceManagerPreview");
		doc.getDocumentElement().appendChild(flowInstanceManagerPreviewElement);

		flowInstanceManagerPreviewElement.appendChild(instanceManager.getFlowInstance().toXML(doc));

		XMLUtils.append(doc, flowInstanceManagerPreviewElement, "ManagerResponses", managerResponses);

		XMLUtils.appendNewElement(doc, flowInstanceManagerPreviewElement, "lastFlowAction", lastFlowAction);

		if (poster != null) {
			XMLUtils.appendNewElement(doc, flowInstanceManagerPreviewElement, "loggedIn");
		}
		
		if (requiresPayment(instanceManager)) {
			
			XMLUtils.appendNewElement(doc, flowInstanceManagerPreviewElement, "PaymentRequired");
		}

		if (validationError != null) {

			flowInstanceManagerPreviewElement.appendChild(validationError.toXML(doc));
		}

		callback.appendFormData(doc, flowInstanceManagerPreviewElement, instanceManager, req, user);

		// TODO breadcrumbs
		SimpleForegroundModuleResponse moduleResponse = new SimpleForegroundModuleResponse(doc, instanceManager.getFlowInstance().getFlow().getName());

		appendLinksAndScripts(moduleResponse, managerResponses);
		
		return moduleResponse;
	}

	protected ForegroundModuleResponse showSigningForm(MutableFlowInstanceManager instanceManager, HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser, ViewFragment viewFragment) throws FlowInstanceManagerClosedException, UnableToGetQueryInstanceFormHTMLException {

		log.info("User " + user + " requested sign form flow instance " + instanceManager.getFlowInstance());

		Document doc = createDocument(req, uriParser, user);
		Element signFormElement = doc.createElement("SigningForm");
		doc.getDocumentElement().appendChild(signFormElement);

		signFormElement.appendChild(instanceManager.getFlowInstance().toXML(doc));
		signFormElement.appendChild(viewFragment.toXML(doc));

		//TODO fix add breadcrumbs
		SimpleForegroundModuleResponse moduleResponse = new SimpleForegroundModuleResponse(doc, instanceManager.getFlowInstance().getFlow().getName(), this.getDefaultBreadcrumb());

		ViewFragmentUtils.appendLinksAndScripts(moduleResponse, viewFragment);

		return moduleResponse;
	}

	protected ForegroundModuleResponse showInlinePaymentForm(MutableFlowInstanceManager instanceManager, HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser, ViewFragment viewFragment) throws FlowInstanceManagerClosedException, UnableToGetQueryInstanceFormHTMLException {

		log.info("User " + user + " requested payment form flow instance " + instanceManager.getFlowInstance());

		Document doc = createDocument(req, uriParser, user);
		Element paymentFormElement = doc.createElement("InlinePaymentForm");
		doc.getDocumentElement().appendChild(paymentFormElement);

		paymentFormElement.appendChild(instanceManager.getFlowInstance().toXML(doc));
		paymentFormElement.appendChild(viewFragment.toXML(doc));

		//TODO fix add breadcrumbs
		SimpleForegroundModuleResponse moduleResponse = new SimpleForegroundModuleResponse(doc, instanceManager.getFlowInstance().getFlow().getName(), this.getDefaultBreadcrumb());

		ViewFragmentUtils.appendLinksAndScripts(moduleResponse, viewFragment);

		return moduleResponse;
	}

	protected ForegroundModuleResponse showImmutableFlowInstance(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser, FlowInstanceAccessController accessController, FlowProcessCallback callback, ShowMode showMode, RequestMetadata requestMetadata) throws UnableToGetQueryInstanceShowHTMLException, FlowInstanceManagerClosedException, AccessDeniedException, SQLException, ModuleConfigurationException {

		Integer flowInstanceID = null;
		ImmutableFlowInstanceManager instanceManager;

		try {
			if (uriParser.size() == 3 && (flowInstanceID = uriParser.getInt(2)) != null) {

				//Get saved instance from DB or session
				instanceManager = getImmutableFlowInstanceManager(flowInstanceID, accessController, user, true, req, uriParser, requestMetadata.isManager());

				if (instanceManager == null) {

					log.info("User " + user + " requested non-existing flow instance with ID " + flowInstanceID + ", listing flows");
					return callback.list(req, res, user, uriParser, Collections.singletonList(FLOW_INSTANCE_NOT_FOUND_VALIDATION_ERROR));
				}

			} else {

				log.info("User " + user + " requested invalid URL, listing flows");
				return callback.list(req, res, user, uriParser, Collections.singletonList(INVALID_LINK_VALIDATION_ERROR));
			}

		} catch (FlowDisabledException e) {

			log.info("User " + user + " requested flow " + e.getFlow() + " which is not enabled.");
			return callback.list(req, res, user, uriParser, Collections.singletonList(FLOW_DISABLED_VALIDATION_ERROR));

		} catch (FlowEngineException e) {

			log.info("Error generating preview of flowInstanceID " + flowInstanceID + " for user " + user, e);
			return callback.list(req, res, user, uriParser, Collections.singletonList(FLOW_INSTANCE_PREVIEW_VALIDATION_ERROR));
		}

		String elementName;

		if (showMode == ShowMode.SUBMIT) {

			elementName = "FlowInstanceManagerSubmitted";
			
			req.setAttribute("BaseFlowModule.flowFamilyID", instanceManager.getFlowInstance().getFlow().getFlowFamily().getFlowFamilyID());
			req.setAttribute("BaseFlowModule.submitted", "submitted");

		} else {

			elementName = "ImmutableFlowInstanceManagerPreview";
		}

		Breadcrumb breadcrumb;

		if (showMode == ShowMode.SUBMIT) {

			breadcrumb = this.getFlowInstanceSubmitBreadcrumb(instanceManager.getFlowInstance(), req, uriParser);

		} else {

			breadcrumb = this.getFlowInstancePreviewBreadcrumb(instanceManager.getFlowInstance(), req, uriParser);
		}

		return showFlowInstance(req, res, user, null, uriParser, instanceManager, accessController, callback, elementName, breadcrumb, showMode, requestMetadata);
	}

	protected ForegroundModuleResponse showFlowInstance(HttpServletRequest req, HttpServletResponse res, User user, User poster, URIParser uriParser, FlowInstanceManager instanceManager, FlowInstanceAccessController accessController, FlowProcessCallback callback, String elementName, Breadcrumb breadcrumb, ShowMode showMode, RequestMetadata requestMetadata) throws UnableToGetQueryInstanceShowHTMLException, FlowInstanceManagerClosedException, AccessDeniedException, SQLException, ModuleConfigurationException {

		log.info("User " + user + " requested preview of flow instance " + instanceManager.getFlowInstance());

		String baseUpdateURL = getBaseUpdateURL(req, uriParser, user, instanceManager.getFlowInstance(), accessController);

		List<ManagerResponse> managerResponses = instanceManager.getFullShowHTML(req, user, poster, this, true, baseUpdateURL, getImmutableQueryRequestBaseURL(req, instanceManager), requestMetadata);

		Document doc = createDocument(req, uriParser, user);
		Element flowInstanceManagerElement = doc.createElement(elementName);
		doc.getDocumentElement().appendChild(flowInstanceManagerElement);

		Flow flow = (Flow) instanceManager.getFlowInstance().getFlow();
		
		if(flow.getSubmittedMessage() != null) {
		
			flow.setSubmittedMessage(AttributeTagUtils.replaceTags(flow.getSubmittedMessage(), instanceManager.getFlowInstance().getAttributeHandler()));
			
			TagReplacer tagReplacer = new TagReplacer();
			tagReplacer.addTagSource(FLOWINSTANCE_TAG_SOURCE_FACTORY.getTagSource((FlowInstance)instanceManager.getFlowInstance()));
			flow.setSubmittedMessage(tagReplacer.replace(flow.getSubmittedMessage()));
		}
		
		flowInstanceManagerElement.appendChild(instanceManager.getFlowInstance().toXML(doc));

		XMLUtils.append(doc, flowInstanceManagerElement, "ManagerResponses", managerResponses);

		if (instanceManager.getFlowInstance().getEvents() != null) {

			for (ImmutableFlowInstanceEvent event : new ReverseListIterator<ImmutableFlowInstanceEvent>(instanceManager.getFlowInstance().getEvents())) {

				if (event.getEventType() == EventType.SUBMITTED || event.getEventType() == EventType.SIGNED) {

					String pdfLink = getEventPDFLink(instanceManager, event, req, user);

					if (pdfLink != null) {

						XMLUtils.appendNewCDATAElement(doc, flowInstanceManagerElement, "PDFLink", pdfLink);

						break;

					}
				}
			}
			
			ImmutableFlowInstanceEvent event = FlowInstanceUtils.getLatestSubmitEvent(instanceManager.getFlowInstance());
			
			if(event != null) {
			
				String alternateSubmitMessageHeader = event.getAttributeHandler().getString(Constants.FLOW_INSTANCE_ALTERNATE_SUBMIT_MESSAGE_HEADER);
				String alternateSubmitMessageText = event.getAttributeHandler().getString(Constants.FLOW_INSTANCE_ALTERNATE_SUBMIT_MESSAGE_TEXT);
				
				if(alternateSubmitMessageHeader != null) {
					
					XMLUtils.appendNewElement(doc, flowInstanceManagerElement, "AlternateSubmitMessageHeader", alternateSubmitMessageHeader);
				}
				
				if(alternateSubmitMessageText != null) {
				
					XMLUtils.appendNewElement(doc, flowInstanceManagerElement, "AlternateSubmitMessageText", alternateSubmitMessageText);
				}
			
			}
		}

		if (callback != null) {
			callback.appendShowFlowInstanceData(doc, flowInstanceManagerElement, instanceManager, req, user);
		}

		SimpleForegroundModuleResponse moduleResponse = new SimpleForegroundModuleResponse(doc, instanceManager.getFlowInstance().getFlow().getName(), this.getDefaultBreadcrumb());

		if (instanceManager.getFlowInstance().getFlow().showsSubmitSurvey() && showMode == ShowMode.SUBMIT) {

			FlowSubmitSurveyProvider instance = systemInterface.getInstanceHandler().getInstance(FlowSubmitSurveyProvider.class);

			if (instance != null) {

				try {

					ViewFragment viewFragment = instance.getSurveyFormFragment(req, user, instanceManager);

					if (viewFragment != null) {

						ViewFragmentUtils.appendLinksAndScripts(moduleResponse, viewFragment);

						XMLUtils.appendNewElement(doc, flowInstanceManagerElement, "SubmitSurveyHTML", viewFragment.getHTML());
					}

				} catch (TransformerException e) {

					log.error("Unable to get view fragment for flow submit survey", e);
				}
			}

		}

		if (breadcrumb != null) {

			moduleResponse.addBreadcrumbLast(breadcrumb);
		}

		appendLinksAndScripts(moduleResponse, managerResponses);

		return moduleResponse;
	}

	protected Breadcrumb getFlowInstancePreviewBreadcrumb(ImmutableFlowInstance flowInstance, HttpServletRequest req, URIParser uriParser) {

		return null;
	}

	protected Breadcrumb getFlowInstanceSubmitBreadcrumb(ImmutableFlowInstance flowInstance, HttpServletRequest req, URIParser uriParser) {

		return null;
	}

	protected String getEventPDFLink(FlowInstanceManager instanceManager, ImmutableFlowInstanceEvent event, HttpServletRequest req, User user) {

		return null;
	}

	protected void appendLinksAndScripts(SimpleForegroundModuleResponse moduleResponse, List<ManagerResponse> managerResponses) {

		for (ManagerResponse managerResponse : managerResponses) {

			appendLinksAndScripts(moduleResponse, managerResponse);
		}
	}

	protected void appendLinksAndScripts(SimpleForegroundModuleResponse moduleResponse, ManagerResponse managerResponse) {

		if (managerResponse.getQueryResponses() != null) {

			for (QueryResponse queryResponse : managerResponse.getQueryResponses()) {

				if (queryResponse.getLinks() != null) {

					moduleResponse.addLinks(queryResponse.getLinks());
				}

				if (queryResponse.getScripts() != null) {

					moduleResponse.addScripts(queryResponse.getScripts());
				}
			}
		}
	}

	protected void sendSubmitEvent(FlowInstanceManager instanceManager, FlowInstanceEvent event, String actionID, SiteProfile siteProfile, boolean forcePDF) {

		eventHandler.sendEvent(FlowInstanceManager.class, new SubmitEvent(instanceManager, event, moduleDescriptor, actionID, siteProfile, forcePDF), EventTarget.ALL);
	}

	public Document createDocument(HttpServletRequest req, URIParser uriParser, User user) {

		Document doc = XMLUtils.createDomDocument();
		Element documentElement = doc.createElement("Document");
		documentElement.appendChild(RequestUtils.getRequestInfoAsXML(doc, req, uriParser));
		documentElement.appendChild(this.moduleDescriptor.toXML(doc));
		
		if (user != null) {
			
			Element userElement = (Element) documentElement.appendChild(user.toXML(doc));
			
			if (user.getAttributeHandler() != null) {
				
				userElement.appendChild(user.getAttributeHandler().toXML(doc));
			}
		}

		doc.appendChild(documentElement);
		return doc;
	}

	protected Flow getFlow(Integer flowID) throws SQLException {

		HighLevelQuery<Flow> query = new HighLevelQuery<Flow>(FLOW_RELATIONS);

		query.addParameter(flowIDParamFactory.getParameter(flowID));

		return daoFactory.getFlowDAO().get(query);
	}

	protected boolean isLatestPublishedVersion(Flow flow) throws SQLException {

		LowLevelQuery<Flow> query = new LowLevelQuery<Flow>("SELECT * FROM flowengine_flows WHERE flowFamilyID = ? AND enabled = 1 AND publishDate <= ? AND (unPublishDate IS NULL OR unPublishDate >= ?) AND version > ?");

		Date currentDate = DateUtils.getCurrentSQLDate(false);

		query.addParameters(flow.getFlowFamily().getFlowFamilyID(), currentDate, currentDate, flow.getVersion());

		return !daoFactory.getFlowDAO().getBoolean(query);
	}

	public FlowInstance getFlowInstance(int flowInstanceID) throws SQLException {

		HighLevelQuery<FlowInstance> query = new HighLevelQuery<FlowInstance>(FLOW_INSTANCE_RELATIONS);

		query.addParameter(flowInstanceIDParamFactory.getParameter(flowInstanceID));

		query.addRelationParameter(QueryInstanceDescriptor.class, queryInstanceDescriptorFlowInstanceIDParamFactory.getParameter(flowInstanceID));

		return daoFactory.getFlowInstanceDAO().get(query);
	}

	public FlowInstance getFlowInstance(int flowInstanceID, List<Field> excludedFields, Field... relations) throws SQLException {

		HighLevelQuery<FlowInstance> query = new HighLevelQuery<FlowInstance>(relations);

		if (excludedFields != null) {
			query.addExcludedFields(excludedFields);
		}

		query.addParameter(flowInstanceIDParamFactory.getParameter(flowInstanceID));

		return daoFactory.getFlowInstanceDAO().get(query);
	}
	
	protected FlowInstance getFlowInstanceForSavedMutableAccessCheck(int flowInstanceID) throws SQLException {

		HighLevelQuery<FlowInstance> query = new HighLevelQuery<FlowInstance>(FLOW_INSTANCE_SAVED_MUTABLE_ACCESS_CHECK_RELATIONS);

		query.addParameter(flowInstanceIDParamFactory.getParameter(flowInstanceID));

		return daoFactory.getFlowInstanceDAO().get(query);
	}

	protected static String getNewInstanceManagerID(User user) {

		if (user != null && user.getUserID() != null) {

			return "userid-" + user.getUserID() + "-uuid-" + UUID.randomUUID();
		}

		return "anonymous-uuid-" + UUID.randomUUID();
	}

	@Override
	public QueryHandler getQueryHandler() {

		return queryHandler;
	}

	@Override
	public SystemInterface getSystemInterface() {

		return systemInterface;
	}

	@Override
	public FlowEngineDAOFactory getDAOFactory() {

		return daoFactory;
	}

	public ForegroundModuleResponse processMutableQueryRequest(HttpServletRequest req, HttpServletResponse res, User user, User poster, URIParser uriParser, FlowInstanceAccessController accessController, boolean checkPublishDate, boolean checkEnabled, boolean checkFlowTypeAccess, RequestMetadata requestMetadata) throws ModuleConfigurationException, SQLException, AccessDeniedException, IOException, FlowDefaultStatusNotFound, EvaluationException, URINotFoundException, QueryRequestException, QueryProviderException, EvaluationProviderException, InvalidFlowInstanceStepException, MissingQueryInstanceDescriptor, DuplicateFlowInstanceManagerIDException, UnableToResetQueryInstanceException {

		Integer flowID = null;

		if (uriParser.size() < 5 || (flowID = NumberUtils.toInt(uriParser.get(2))) == null) {

			throw new URINotFoundException(uriParser);
		}

		Integer queryID = null;
		MutableFlowInstanceManager instanceManager = null;

		try {
			if (uriParser.get(3).equals("q") && NumberUtils.isInt(uriParser.get(4))) {

				queryID = NumberUtils.toInt(uriParser.get(4));

				//Get instance from session
				instanceManager = getUnsavedMutableFlowInstanceManager(flowID, accessController, req.getSession(true), user, poster, null, uriParser, req, false, checkEnabled, checkPublishDate, checkFlowTypeAccess, requestMetadata);

			} else if (uriParser.size() > 5 && NumberUtils.isInt(uriParser.get(3)) && uriParser.get(4).equals("q") && NumberUtils.isInt(uriParser.get(5))) {

				Integer flowInstanceID = NumberUtils.toInt(uriParser.get(3));
				queryID = NumberUtils.toInt(uriParser.get(5));

				//Get saved instance from DB or session
				instanceManager = getSavedMutableFlowInstanceManager(flowID, flowInstanceID, accessController, req.getSession(true), user, uriParser, req, true, checkEnabled, checkPublishDate, requestMetadata);

			}

			if (instanceManager == null || queryID == null) {

				throw new URINotFoundException(uriParser);
			}

			processQueryRequest(instanceManager, queryID, req, res, user, uriParser);

		} catch (QueryInstanceNotFoundInFlowInstanceManagerException e) {

			throw new URINotFoundException(uriParser);

		} catch (FlowException e) {

			throw new URINotFoundException(uriParser);

		} catch (FlowNotAvailiableInRequestedFormat e) {

			throw new URINotFoundException(uriParser);

		} catch (FlowInstanceNoLongerAvailableException e) {

			throw new URINotFoundException(uriParser);

		} catch (QueryRequestsNotSupported e) {

			throw new URINotFoundException(uriParser);

		} catch (QueryRequestException e) {

			log.error("Error processing query request for query " + e.getQueryInstanceDescriptor() + " in flow instance " + instanceManager.getFlowInstance() + " requested by user " + user, e);

			throw e;
		}

		return null;
	}

	public ForegroundModuleResponse processImmutableQueryRequest(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser, FlowInstanceAccessController accessController, boolean checkEnabled, boolean manager) throws ModuleConfigurationException, SQLException, AccessDeniedException, IOException, FlowDefaultStatusNotFound, EvaluationException, URINotFoundException, QueryRequestException, QueryProviderException, EvaluationProviderException, InvalidFlowInstanceStepException, MissingQueryInstanceDescriptor, DuplicateFlowInstanceManagerIDException {

		Integer flowInstanceID = null;
		Integer queryID = null;

		if (uriParser.size() < 6 || (flowInstanceID = NumberUtils.toInt(uriParser.get(3))) == null || !uriParser.get(4).equals("q") || (queryID = NumberUtils.toInt(uriParser.get(5))) == null) {

			throw new URINotFoundException(uriParser);
		}

		ImmutableFlowInstanceManager instanceManager = null;

		try {
			instanceManager = this.getImmutableFlowInstanceManager(flowInstanceID, accessController, user, checkEnabled, req, uriParser, manager);

			if (instanceManager == null || queryID == null) {

				throw new URINotFoundException(uriParser);
			}

			processQueryRequest(instanceManager, queryID, req, res, user, uriParser);

		} catch (QueryInstanceNotFoundInFlowInstanceManagerException e) {

			throw new URINotFoundException(uriParser);

		} catch (FlowException e) {

			throw new URINotFoundException(uriParser);

		} catch (QueryRequestsNotSupported e) {

			throw new URINotFoundException(uriParser);

		} catch (QueryRequestException e) {

			log.error("Error processing query request for query " + e.getQueryInstanceDescriptor() + " in flow instance " + instanceManager.getFlowInstance() + " requested by user " + user, e);

			throw e;
		}

		return null;
	}

	@WebPublic(alias = "flowtypeicon")
	public ForegroundModuleResponse getFlowTypeIcon(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws URINotFoundException, AccessDeniedException, ModuleConfigurationException, SQLException, IOException {

		Integer flowTypeID = null;

		if (uriParser.size() == 3 && (flowTypeID = NumberUtils.toInt(uriParser.get(2))) != null) {

			Icon icon = getFlowTypeIcon(flowTypeID);

			if (icon != null && icon.getIconBlob() != null) {

				try {

					HTTPUtils.sendBlob(icon.getIconBlob(), icon.getIconFilename(), icon.getIconLastModified(), req, res, ContentDisposition.INLINE);

				} catch (RuntimeException e) {

					log.debug("Caught exception " + e + " while sending image " + icon.getIconFilename() + " to " + user);

				} catch (IOException e) {

					log.debug("Caught exception " + e + " while sending image " + icon.getIconFilename() + " to " + user);

				}

				return null;
			}
		}

		throw new URINotFoundException(uriParser);

	}

	protected abstract Icon getFlowTypeIcon(Integer flowTypeID) throws SQLException;

	@WebPublic(alias = "icon")
	public ForegroundModuleResponse getFlowIcon(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws SQLException, URINotFoundException {

		Integer flowID = null;

		if (uriParser.size() == 3 && (flowID = NumberUtils.toInt(uriParser.get(2))) != null) {

			Icon icon = getFlowIcon(flowID);

			if (icon != null && icon.getIconBlob() != null) {

				try {
					res.setDateHeader("Expires", System.currentTimeMillis() + MillisecondTimeUnits.HOUR);
					
					HTTPUtils.sendBlob(icon.getIconBlob(), icon.getIconFilename(), icon.getIconLastModified(), req, res, ContentDisposition.INLINE);

				} catch (RuntimeException e) {

					log.debug("Caught exception " + e + " while sending image " + icon.getIconFilename() + " to " + user);

				} catch (IOException e) {

					log.debug("Caught exception " + e + " while sending image " + icon.getIconFilename() + " to " + user);
				}
			
			}else{
			
				try {
					res.setDateHeader("Expires", System.currentTimeMillis() + MillisecondTimeUnits.HOUR);
					
					HTTPUtils.sendFile(DEFAULT_FLOW_ICON, DEFAULT_FLOW_ICON.getFile(), req, res, ContentDisposition.INLINE);

				} catch (RuntimeException e) {

					log.debug("Caught exception " + e + " while sending default image " + icon.getIconFilename() + " to " + user);

				} catch (IOException e) {

					log.debug("Caught exception " + e + " while sending default image " + icon.getIconFilename() + " to " + user);
				}
			}
		
			return null;
		}

		throw new URINotFoundException(uriParser);
	}

	protected abstract Icon getFlowIcon(Integer flowID) throws SQLException;

	public ForegroundModuleResponse showMultiSignMessage(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser, FlowInstanceAccessController accessController, FlowProcessCallback callback, boolean mananger) throws FlowInstanceManagerClosedException, UnableToGetQueryInstanceShowHTMLException, AccessDeniedException, ModuleConfigurationException, SQLException, URINotFoundException {

		Integer flowInstanceID = null;
		ImmutableFlowInstanceManager instanceManager;

		try {

			if (uriParser.size() == 3 && (flowInstanceID = uriParser.getInt(2)) != null) {

				//Get saved instance from DB or session
				instanceManager = getImmutableFlowInstanceManager(flowInstanceID, accessController, user, true, req, uriParser, mananger);

				if (instanceManager == null) {

					log.info("User " + user + " requested non-existing flow instance with ID " + flowInstanceID + ", listing flows");
					return callback.list(req, res, user, uriParser, Collections.singletonList(FLOW_INSTANCE_NOT_FOUND_VALIDATION_ERROR));
				}

			} else {

				log.info("User " + user + " requested invalid URL, listing flows");
				return callback.list(req, res, user, uriParser, Collections.singletonList(INVALID_LINK_VALIDATION_ERROR));
			}

		} catch (FlowDisabledException e) {

			log.info("User " + user + " requested flow " + e.getFlow() + " which is not enabled.");
			return callback.list(req, res, user, uriParser, Collections.singletonList(FLOW_DISABLED_VALIDATION_ERROR));

		} catch (FlowEngineException e) {

			log.info("Error generating preview of flowInstanceID " + flowInstanceID + " for user " + user, e);
			return callback.list(req, res, user, uriParser, Collections.singletonList(UNABLE_TO_LOAD_FLOW_INSTANCE_VALIDATION_ERROR));
		}

		if (instanceManager.getFlowState().getContentType() != ContentType.WAITING_FOR_MULTISIGN) {

			//TODO show correct view
			
			log.warn("User " + user + " attempted to view multi sign message for flow instance " + instanceManager.getFlowInstance() + " not in WAITING_FOR_MULTISIGN state");
			throw new URINotFoundException(uriParser);
		}

		MultiSigningHandler multiSigningHandler = getMultiSigningHandler();

		if (multiSigningHandler == null) {

			return callback.list(req, res, user, uriParser, Collections.singletonList(MULTI_SIGNING_PROVIDER_NOT_FOUND_VALIDATION_ERROR));
		}

		ViewFragment viewFragment;

		try {
			viewFragment = multiSigningHandler.getSigningStatus(req, user, uriParser, instanceManager);
		} catch (Exception e) {
			viewFragment = null;
			log.error("Error getting view fragment from multi signing provider " + multiSigningHandler, e);
		}

		if (viewFragment == null) {

			log.warn("No viewfragement returned from multi signing provider " + multiSigningHandler);
		}

		log.info("User " + user + " requested multi signing status for flow instance " + instanceManager.getFlowInstance());

		Document doc = createDocument(req, uriParser, user);
		Element multiSigningStatusElement = doc.createElement("MultiSigningStatusForm");
		doc.getDocumentElement().appendChild(multiSigningStatusElement);

		multiSigningStatusElement.appendChild(instanceManager.getFlowInstance().toXML(doc));

		SimpleForegroundModuleResponse moduleResponse = new SimpleForegroundModuleResponse(doc, this.getDefaultBreadcrumb());

		if (viewFragment != null) {

			multiSigningStatusElement.appendChild(viewFragment.toXML(doc));
			ViewFragmentUtils.appendLinksAndScripts(moduleResponse, viewFragment);
		}

		//TODO fix add breadcrumbs

		return moduleResponse;

	}

	//TODO show inline payment form?

	public ForegroundModuleResponse showPaymentForm(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser, FlowInstanceAccessController accessController, FlowProcessCallback callback, boolean manager) throws ModuleConfigurationException, SQLException, AccessDeniedException, URINotFoundException {

		Integer flowInstanceID = null;
		ImmutableFlowInstanceManager instanceManager;

		try {

			if (uriParser.size() == 3 && (flowInstanceID = uriParser.getInt(2)) != null) {

				//Get saved instance from DB or session
				instanceManager = getImmutableFlowInstanceManager(flowInstanceID, accessController, user, true, req, uriParser, manager);

				if (instanceManager == null) {

					log.info("User " + user + " requested non-existing flow instance with ID " + flowInstanceID + ", listing flows");
					return callback.list(req, res, user, uriParser, Collections.singletonList(FLOW_INSTANCE_NOT_FOUND_VALIDATION_ERROR));
				}

			} else {

				log.info("User " + user + " requested invalid URL, listing flows");
				return callback.list(req, res, user, uriParser, Collections.singletonList(INVALID_LINK_VALIDATION_ERROR));
			}

		} catch (FlowDisabledException e) {

			log.info("User " + user + " requested flow " + e.getFlow() + " which is not enabled.");
			return callback.list(req, res, user, uriParser, Collections.singletonList(FLOW_DISABLED_VALIDATION_ERROR));

		} catch (FlowEngineException e) {

			log.info("Error generating preview of flowInstanceID " + flowInstanceID + " for user " + user, e);
			return callback.list(req, res, user, uriParser, Collections.singletonList(UNABLE_TO_LOAD_FLOW_INSTANCE_VALIDATION_ERROR));
		}

		if (instanceManager.getFlowState().getContentType() != ContentType.WAITING_FOR_PAYMENT) {

			//TODO show correct view
			throw new URINotFoundException(uriParser);
		}

		FlowPaymentProvider paymentProvider = getFlowPaymentProvider();

		if (paymentProvider == null) {

			return callback.list(req, res, user, uriParser, Collections.singletonList(PAYMENT_PROVIDER_NOT_FOUND_VALIDATION_ERROR));
		}
		
		//TODO Submit check
		
		
		SiteProfile instanceProfile = getSiteProfile(instanceManager);
		ViewFragment viewFragment;

		try {

			viewFragment = paymentProvider.pay(req, res, user, uriParser, instanceManager, new BaseFlowModuleStandalonePaymentCallback(this, instanceProfile, callback.getSubmitActionID()));

		} catch (Exception e) {

			viewFragment = null;
			log.error("Error getting view fragment from payment provider " + paymentProvider, e);

		}

		if (res.isCommitted()) {

			return null;

		} else if (viewFragment == null) {

			log.warn("No view fragment returned from payment provider " + paymentProvider);
		}

		log.info("User " + user + " requested payment form for flow instance " + instanceManager.getFlowInstance());

		Document doc = createDocument(req, uriParser, user);
		Element paymentFormElement = doc.createElement("StandalonePaymentForm");
		doc.getDocumentElement().appendChild(paymentFormElement);

		paymentFormElement.appendChild(instanceManager.getFlowInstance().toXML(doc));

		//TODO fix add breadcrumbs
		SimpleForegroundModuleResponse moduleResponse = new SimpleForegroundModuleResponse(doc, this.getDefaultBreadcrumb());

		if (viewFragment != null) {

			paymentFormElement.appendChild(viewFragment.toXML(doc));
			ViewFragmentUtils.appendLinksAndScripts(moduleResponse, viewFragment);
		}

		return moduleResponse;
	}

	protected Flow getBareFlow(Integer flowID) throws SQLException {

		HighLevelQuery<Flow> query = new HighLevelQuery<Flow>(Flow.FLOW_TYPE_RELATION);

		query.addParameter(flowIDParamFactory.getParameter(flowID));

		return daoFactory.getFlowDAO().get(query);
	}

	protected FlowType getBareFlowType(Integer flowTypeID) throws SQLException {

		HighLevelQuery<FlowType> query = new HighLevelQuery<FlowType>();

		query.addParameter(flowTypeIDParamFactory.getParameter(flowTypeID));

		return daoFactory.getFlowTypeDAO().get(query);
	}

	@Override
	public EvaluationHandler getEvaluationHandler() {

		return evaluationHandler;
	}

	public Breadcrumb getFlowBreadcrumb(ImmutableFlowInstance flowInstance) {

		return new Breadcrumb(this, flowInstance.getFlow().getName(), "/flow/" + flowInstance.getFlow().getFlowID());
	}

	public String getTempDir() {

		return tempDir;
	}

	public long getMaxRequestSize() {

		return maxRequestSize;
	}

	public int getRamThreshold() {

		return ramThreshold;
	}

	public void sendEventPDF(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser, FlowInstanceAccessController accessController, PDFProvider pdfProvider, boolean manager) throws URINotFoundException, SQLException, IOException, AccessDeniedException, FlowDisabledException {

		Integer flowInstanceID;
		Integer eventID;

		if (uriParser.size() != 4 || (flowInstanceID = uriParser.getInt(2)) == null || (eventID = uriParser.getInt(3)) == null || pdfProvider == null) {

			throw new URINotFoundException(uriParser);
		}

		FlowInstance flowInstance = getFlowInstance(flowInstanceID);

		if (flowInstance == null) {

			throw new URINotFoundException(uriParser);
		}

		accessController.checkFlowInstanceAccess(flowInstance, user);

		if (!flowInstance.getFlow().isEnabled() || isOperatingStatusDisabled(flowInstance.getFlow(), manager)) {

			throw new FlowDisabledException(flowInstance.getFlow());
		}

		File pdfFile = pdfProvider.getPDF(flowInstanceID, eventID);

		if (pdfFile == null) {

			throw new URINotFoundException(uriParser);
		}

		try {
			log.info("Sending PDF for flow instance " + flowInstance + ", event " + eventID + " to user " + user);
			HTTPUtils.sendFile(pdfFile, flowInstance.getFlow().getName() + " - " + flowInstance.getFlowInstanceID() + ".pdf", req, res, ContentDisposition.ATTACHMENT);
		} catch (Exception e) {
			log.info("Error sending PDF for flow instance " + flowInstance + ", event " + eventID + " to user " + user + ", " + e);
		}
	}

	public void sendEventXML(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser, FlowInstanceAccessController accessController, XMLProvider xmlProvider, boolean manager) throws URINotFoundException, SQLException, IOException, AccessDeniedException, FlowDisabledException {

		Integer flowInstanceID;
		Integer eventID;

		if (uriParser.size() != 4 || (flowInstanceID = uriParser.getInt(2)) == null || (eventID = uriParser.getInt(3)) == null || xmlProvider == null) {

			throw new URINotFoundException(uriParser);
		}

		FlowInstance flowInstance = getFlowInstance(flowInstanceID);

		if (flowInstance == null) {

			throw new URINotFoundException(uriParser);
		}

		accessController.checkFlowInstanceAccess(flowInstance, user);

		if (!flowInstance.getFlow().isEnabled() || isOperatingStatusDisabled(flowInstance.getFlow(), manager)) {

			throw new FlowDisabledException(flowInstance.getFlow());
		}

		File pdfFile = xmlProvider.getXML(flowInstanceID, eventID);

		if (pdfFile == null) {

			throw new URINotFoundException(uriParser);
		}

		try {
			log.info("Sending export XML for flow instance " + flowInstance + ", event " + eventID + " to user " + user);
			HTTPUtils.sendFile(pdfFile, flowInstance.getFlow().getName() + " - " + flowInstance.getFlowInstanceID() + ".xml", req, res, ContentDisposition.ATTACHMENT);
		} catch (Exception e) {
			log.info("Error sending export XML for flow instance " + flowInstance + ", event " + eventID + " to user " + user + ", " + e);
		}
	}

	public void sendEventSignature(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser, FlowInstanceAccessController accessController, boolean manager) throws URINotFoundException, SQLException, IOException, AccessDeniedException, FlowDisabledException {

		Integer flowInstanceID;
		Integer eventID;

		if (uriParser.size() != 4 || (flowInstanceID = uriParser.getInt(2)) == null || (eventID = uriParser.getInt(3)) == null) {

			throw new URINotFoundException(uriParser);
		}

		FlowInstance flowInstance = getFlowInstance(flowInstanceID);

		if (flowInstance == null) {

			throw new URINotFoundException(uriParser);
		}

		accessController.checkFlowInstanceAccess(flowInstance, user);

		if (!flowInstance.getFlow().isEnabled() || isOperatingStatusDisabled(flowInstance.getFlow(), manager)) {

			throw new FlowDisabledException(flowInstance.getFlow());
		}

		if(flowInstance.getEvents() != null){

			for(FlowInstanceEvent event : flowInstance.getEvents()){

				if(event.getEventID().equals(eventID)){

					String eventSignature = event.getAttributeHandler().getString("signingData");

					if(eventSignature != null){

						log.info("Sending signature for flow instance " + flowInstance + ", event " + eventID + " to user " + user);

						//Follows RFC6266 filename encoding
						String filename = flowInstance.getFlow().getName() + " - " + flowInstance.getFlowInstanceID() + " - signature - " + eventID + ".txt";

						res.setHeader("Content-Disposition", ContentDisposition.ATTACHMENT + "; filename=\"" + FileUtils.toValidHttpFilename(filename) + "\"; filename*=UTF-8''" + URLEncoder.encode(filename, "UTF-8").replace("+", "%20"));
						res.setContentType("text/plain");

						res.getWriter().write(eventSignature);
						res.getWriter().close();
						return;
					}

					break;
				}
			}
		}

		throw new URINotFoundException(uriParser);
	}

	public FlowInstanceEvent createSigningEvent(MutableFlowInstanceManager instanceManager, User user, EventType eventType, String actionID) throws FlowInstanceManagerClosedException, UnableToSaveQueryInstanceException, SQLException {

		return flowInstanceEventGenerator.addFlowInstanceEvent(instanceManager.getFlowInstance(), eventType, null, user);
	}

	public void signingComplete(MutableFlowInstanceManager instanceManager, FlowInstanceEvent event, HttpServletRequest req, SiteProfile siteProfile, String actionID) throws FlowInstanceManagerClosedException, UnableToSaveQueryInstanceException, FlowDefaultStatusNotFound, SQLException {

		instanceManager.getSessionAttributeHandler().removeAttribute(SIGN_FLOW_MODIFICATION_COUNT_INSTANCE_MANAGER_ATTRIBUTE);
		
		if (!MultiSignUtils.requiresMultiSigning(instanceManager) && !requiresPayment(instanceManager)) {
			
			sendSubmitEvent(instanceManager, event, actionID, siteProfile, true);
			
		} else if (MultiSignUtils.requiresMultiSigning(instanceManager)) {
			
			systemInterface.getEventHandler().sendEvent(FlowInstance.class, new MultiSigningInitiatedEvent(instanceManager, event), EventTarget.ALL);
		}

		systemInterface.getEventHandler().sendEvent(FlowInstance.class, new CRUDEvent<FlowInstance>(CRUDAction.UPDATE, (FlowInstance) instanceManager.getFlowInstance()), EventTarget.ALL);

		removeMutableFlowInstanceManagerFromSession(instanceManager, req.getSession());
	}
	
	protected Map<String, String> getPaymentCompleteSubmitEventAttributes(FlowInstanceManager instanceManager) throws SQLException {
		
		Map<String, String> submitEventAttributes = null;
		
		List<ImmutableFlowInstanceEvent> signEvents = SigningUtils.getLastestSignEvents(getFlowInstanceEvents((FlowInstance) instanceManager.getFlowInstance()), true);
		
		if (!CollectionUtils.isEmpty(signEvents)) {
			
			submitEventAttributes = new HashMap<String, String>();
			submitEventAttributes.put(BaseFlowModule.SIGNING_CHAIN_ID_FLOW_INSTANCE_EVENT_ATTRIBUTE, signEvents.get(signEvents.size() - 1).getAttributeHandler().getString(BaseFlowModule.SIGNING_CHAIN_ID_FLOW_INSTANCE_EVENT_ATTRIBUTE));
		}
		
		return submitEventAttributes;
	}

	public void standalonePaymentComplete(ImmutableFlowInstanceManager instanceManager, HttpServletRequest req, User user, SiteProfile siteProfile, String actionID, boolean addPaymentEvent, String eventDetails, Map<String, String> eventAttributes) throws FlowInstanceManagerClosedException, UnableToSaveQueryInstanceException, FlowDefaultStatusNotFound, SQLException {

		instanceManager.getSessionAttributeHandler().removeAttribute(PAYMENT_FLOW_MODIFICATION_COUNT_INSTANCE_MANAGER_ATTRIBUTE);
		
		FlowInstance flowInstance = (FlowInstance) instanceManager.getFlowInstance();

		if (addPaymentEvent) {
		
			flowInstanceEventGenerator.addFlowInstanceEvent(flowInstance, EventType.PAYED, eventDetails, user, null, eventAttributes);
		}

		Status nextStatus = flowInstance.getFlow().getDefaultState(actionID);

		if (nextStatus == null) {

			throw new FlowDefaultStatusNotFound(actionID, instanceManager.getFlowInstance().getFlow());
		}

		Timestamp currentTimestamp = TimeUtils.getCurrentTimestamp();

		flowInstance.setStatus(nextStatus);
		flowInstance.setLastStatusChange(currentTimestamp);

		if (flowInstance.getFirstSubmitted() == null) {

			flowInstance.setFirstSubmitted(currentTimestamp);
		}

		this.daoFactory.getFlowInstanceDAO().update(flowInstance);
		
		FlowInstanceEvent event = flowInstanceEventGenerator.addFlowInstanceEvent(instanceManager.getFlowInstance(), EventType.SUBMITTED, null, user, null, getPaymentCompleteSubmitEventAttributes(instanceManager));

		sendSubmitEvent(instanceManager, event, actionID, siteProfile, true);

		systemInterface.getEventHandler().sendEvent(FlowInstance.class, new CRUDEvent<FlowInstance>(CRUDAction.UPDATE, (FlowInstance) instanceManager.getFlowInstance()), EventTarget.ALL);

	}

	public void inlinePaymentComplete(MutableFlowInstanceManager instanceManager, HttpServletRequest req, User user, User poster, SiteProfile siteProfile, String actionID, boolean addPaymentEvent, String eventDetails, Map<String, String> eventAttributes) throws FlowInstanceManagerClosedException, UnableToSaveQueryInstanceException, FlowDefaultStatusNotFound, SQLException {

		instanceManager.getSessionAttributeHandler().removeAttribute(PAYMENT_FLOW_MODIFICATION_COUNT_INSTANCE_MANAGER_ATTRIBUTE);
		
		if (addPaymentEvent) {
			
			flowInstanceEventGenerator.addFlowInstanceEvent(instanceManager.getFlowInstance(), EventType.PAYED, eventDetails, user, null, eventAttributes);
		}

		FlowInstanceEvent event = save(instanceManager, user, poster, req, actionID, EventType.SUBMITTED, getPaymentCompleteSubmitEventAttributes(instanceManager));

		sendSubmitEvent(instanceManager, event, actionID, siteProfile, true);

		systemInterface.getEventHandler().sendEvent(FlowInstance.class, new CRUDEvent<FlowInstance>(CRUDAction.UPDATE, (FlowInstance) instanceManager.getFlowInstance()), EventTarget.ALL);

	}

	public void abortSigning(MutableFlowInstanceManager instanceManager) {
		
		instanceManager.getSessionAttributeHandler().removeAttribute(SIGN_FLOW_MODIFICATION_COUNT_INSTANCE_MANAGER_ATTRIBUTE);
	}

	public abstract String getStandalonePaymentURL(ImmutableFlowInstanceManager instanceManager, HttpServletRequest req);
	
	public abstract String getPaymentFailURL(MutableFlowInstanceManager instanceManager, HttpServletRequest req);

	public abstract String getSignFailURL(MutableFlowInstanceManager instanceManager, HttpServletRequest req);

	public String getSignSuccessURL(MutableFlowInstanceManager instanceManager, HttpServletRequest req) {

		if (MultiSignUtils.requiresMultiSigning(instanceManager)) {

			return RequestUtils.getFullContextPathURL(req) + this.getFullAlias() + "/multisign/" + instanceManager.getFlowInstanceID();

		} else if (requiresPayment(instanceManager)) {

			return RequestUtils.getFullContextPathURL(req) + this.getFullAlias() + "/pay/" + instanceManager.getFlowInstanceID();
		}

		return RequestUtils.getFullContextPathURL(req) + this.getFullAlias() + "/submitted/" + instanceManager.getFlowInstanceID();
	}

	public abstract String getSaveAndSubmitURL(MutableFlowInstanceManager instanceManager, HttpServletRequest req);

	public String getPaymentSuccessURL(FlowInstanceManager instanceManager, HttpServletRequest req) {

		return RequestUtils.getFullContextPathURL(req) + this.getFullAlias() + "/submitted/" + instanceManager.getFlowInstanceID();
	}

	public boolean requiresPayment(FlowInstanceManager instanceManager) {
		
		if (!instanceManager.getFlowInstance().getFlow().isPaymentSupportEnabled() || instanceManager.getFlowInstance().getFirstSubmitted() != null) {
			
			return false;
		}
		
		List<PaymentQuery> paymentQueries = instanceManager.getQueries(PaymentQuery.class);
		
		if (paymentQueries != null) {
			
			int amount = 0;
			
			for (PaymentQuery paymentQuery : paymentQueries) {
				
				if (paymentQuery.getQueryInstanceDescriptor().getQueryState() != QueryState.HIDDEN && !CollectionUtils.isEmpty(paymentQuery.getInvoiceLines())) {
					
					for (InvoiceLine invoiceLine : paymentQuery.getInvoiceLines()) {
						
						amount += invoiceLine.getQuanitity() * invoiceLine.getUnitPrice();
						
					}
					
				}
				
			}
			
			if (amount > 0) {
				
				return true;
			}
			
		}
		
		return false;
	}

	public boolean submitSurveyEnabled() {

		return systemInterface.getInstanceHandler().getInstance(FlowSubmitSurveyProvider.class) != null;
	}
	
	public List<? extends ImmutableFlowInstanceEvent> getFlowInstanceEvents(FlowInstance flowInstance) throws SQLException {
		
		HighLevelQuery<FlowInstanceEvent> query = new HighLevelQuery<FlowInstanceEvent>(FlowInstanceEvent.ATTRIBUTES_RELATION);
		query.addParameter(eventFlowInstanceParamFactory.getParameter(flowInstance));
		
		return daoFactory.getFlowInstanceEventDAO().getAll(query);
	}
	
	public FlowInstanceEvent getFlowInstanceEvent(int eventID, Field... relations) throws SQLException {
		
		HighLevelQuery<FlowInstanceEvent> query = new HighLevelQuery<FlowInstanceEvent>();
		
		if(relations != null){
			
			query.addRelations(relations);
		}
		
		query.addParameter(eventIDParamFactory.getParameter(eventID));
		
		return daoFactory.getFlowInstanceEventDAO().get(query);
	}

	
	public FlowInstanceEventGenerator getFlowInstanceEventGenerator() {
	
		return flowInstanceEventGenerator;
	}
}
