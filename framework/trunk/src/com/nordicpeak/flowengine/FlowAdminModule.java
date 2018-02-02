package com.nordicpeak.flowengine;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import javax.sql.rowset.serial.SerialBlob;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadBase;
import org.apache.commons.fileupload.FileUploadBase.FileSizeLimitExceededException;
import org.apache.commons.fileupload.FileUploadBase.SizeLimitExceededException;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.io.FilenameUtils;
import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import se.unlogic.fileuploadutils.MultipartRequest;
import se.unlogic.hierarchy.core.annotations.CheckboxSettingDescriptor;
import se.unlogic.hierarchy.core.annotations.EnumDropDownSettingDescriptor;
import se.unlogic.hierarchy.core.annotations.GroupMultiListSettingDescriptor;
import se.unlogic.hierarchy.core.annotations.InstanceManagerDependency;
import se.unlogic.hierarchy.core.annotations.ModuleSetting;
import se.unlogic.hierarchy.core.annotations.TextFieldSettingDescriptor;
import se.unlogic.hierarchy.core.annotations.UserMultiListSettingDescriptor;
import se.unlogic.hierarchy.core.annotations.WebPublic;
import se.unlogic.hierarchy.core.annotations.XSLVariable;
import se.unlogic.hierarchy.core.beans.Breadcrumb;
import se.unlogic.hierarchy.core.beans.Group;
import se.unlogic.hierarchy.core.beans.SimpleBundleDescriptor;
import se.unlogic.hierarchy.core.beans.SimpleForegroundModuleResponse;
import se.unlogic.hierarchy.core.beans.SimpleMenuItemDescriptor;
import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.hierarchy.core.comparators.PriorityComparator;
import se.unlogic.hierarchy.core.enums.CRUDAction;
import se.unlogic.hierarchy.core.enums.EventSource;
import se.unlogic.hierarchy.core.enums.EventTarget;
import se.unlogic.hierarchy.core.enums.MenuItemType;
import se.unlogic.hierarchy.core.enums.SystemStatus;
import se.unlogic.hierarchy.core.enums.URLType;
import se.unlogic.hierarchy.core.events.CRUDEvent;
import se.unlogic.hierarchy.core.exceptions.AccessDeniedException;
import se.unlogic.hierarchy.core.exceptions.ModuleConfigurationException;
import se.unlogic.hierarchy.core.exceptions.URINotFoundException;
import se.unlogic.hierarchy.core.handlers.GroupHandler;
import se.unlogic.hierarchy.core.handlers.UserHandler;
import se.unlogic.hierarchy.core.interfaces.AccessInterface;
import se.unlogic.hierarchy.core.interfaces.ForegroundModuleResponse;
import se.unlogic.hierarchy.core.interfaces.SectionInterface;
import se.unlogic.hierarchy.core.interfaces.ViewFragment;
import se.unlogic.hierarchy.core.interfaces.events.EventHandler;
import se.unlogic.hierarchy.core.interfaces.events.EventListener;
import se.unlogic.hierarchy.core.interfaces.listeners.SystemStartupListener;
import se.unlogic.hierarchy.core.interfaces.menu.BundleDescriptor;
import se.unlogic.hierarchy.core.interfaces.menu.MenuItemDescriptor;
import se.unlogic.hierarchy.core.interfaces.modules.descriptors.ForegroundModuleDescriptor;
import se.unlogic.hierarchy.core.interfaces.settings.SettingHandler;
import se.unlogic.hierarchy.core.utils.AccessUtils;
import se.unlogic.hierarchy.core.utils.AdvancedCRUDCallback;
import se.unlogic.hierarchy.core.utils.FCKUtils;
import se.unlogic.hierarchy.core.utils.GenericCRUD;
import se.unlogic.hierarchy.core.utils.ViewFragmentUtils;
import se.unlogic.hierarchy.core.utils.crud.MultipartLimitProvider;
import se.unlogic.hierarchy.core.utils.crud.MultipartRequestFilter;
import se.unlogic.hierarchy.core.utils.crud.TransactionRequestFilter;
import se.unlogic.hierarchy.core.utils.extensionlinks.ExtensionLink;
import se.unlogic.hierarchy.core.utils.extensionlinks.ExtensionLinkProvider;
import se.unlogic.hierarchy.core.utils.extensionlinks.ExtensionLinkUtils;
import se.unlogic.hierarchy.core.utils.usergrouplist.UserGroupListConnector;
import se.unlogic.hierarchy.core.validationerrors.FileSizeLimitExceededValidationError;
import se.unlogic.hierarchy.core.validationerrors.InvalidFileExtensionValidationError;
import se.unlogic.hierarchy.core.validationerrors.RequestSizeLimitExceededValidationError;
import se.unlogic.hierarchy.core.validationerrors.UnableToParseFileValidationError;
import se.unlogic.openhierarchy.foregroundmodules.siteprofile.interfaces.SiteProfile;
import se.unlogic.openhierarchy.foregroundmodules.siteprofile.interfaces.SiteProfileHandler;
import se.unlogic.standardutils.base64.Base64;
import se.unlogic.standardutils.collections.CollectionUtils;
import se.unlogic.standardutils.dao.AdvancedAnnotatedDAOWrapper;
import se.unlogic.standardutils.dao.AnnotatedDAO;
import se.unlogic.standardutils.dao.AnnotatedDAOWrapper;
import se.unlogic.standardutils.dao.HighLevelQuery;
import se.unlogic.standardutils.dao.LowLevelQuery;
import se.unlogic.standardutils.dao.MySQLRowLimiter;
import se.unlogic.standardutils.dao.OrderByCriteria;
import se.unlogic.standardutils.dao.QueryOperators;
import se.unlogic.standardutils.dao.QueryParameterFactory;
import se.unlogic.standardutils.dao.RelationQuery;
import se.unlogic.standardutils.dao.TransactionHandler;
import se.unlogic.standardutils.dao.querys.ObjectQuery;
import se.unlogic.standardutils.enums.Order;
import se.unlogic.standardutils.image.ImageUtils;
import se.unlogic.standardutils.io.BinarySizes;
import se.unlogic.standardutils.io.CloseUtils;
import se.unlogic.standardutils.io.FileUtils;
import se.unlogic.standardutils.numbers.NumberUtils;
import se.unlogic.standardutils.populators.IntegerPopulator;
import se.unlogic.standardutils.populators.PositiveStringIntegerPopulator;
import se.unlogic.standardutils.populators.StringURLPopulator;
import se.unlogic.standardutils.serialization.SerializationUtils;
import se.unlogic.standardutils.streams.StreamUtils;
import se.unlogic.standardutils.string.StringUtils;
import se.unlogic.standardutils.time.TimeUtils;
import se.unlogic.standardutils.validation.PositiveStringIntegerValidator;
import se.unlogic.standardutils.validation.ValidationError;
import se.unlogic.standardutils.validation.ValidationErrorType;
import se.unlogic.standardutils.validation.ValidationException;
import se.unlogic.standardutils.xml.XMLGeneratorDocument;
import se.unlogic.standardutils.xml.XMLParser;
import se.unlogic.standardutils.xml.XMLUtils;
import se.unlogic.webutils.http.HTTPUtils;
import se.unlogic.webutils.http.RequestUtils;
import se.unlogic.webutils.http.SessionUtils;
import se.unlogic.webutils.http.URIParser;
import se.unlogic.webutils.http.enums.ContentDisposition;
import se.unlogic.webutils.url.URLRewriter;
import se.unlogic.webutils.validation.ValidationUtils;

import com.nordicpeak.flowengine.accesscontrollers.AdminUserFlowInstanceAccessController;
import com.nordicpeak.flowengine.beans.Category;
import com.nordicpeak.flowengine.beans.DefaultStatusMapping;
import com.nordicpeak.flowengine.beans.EvaluatorDescriptor;
import com.nordicpeak.flowengine.beans.ExtensionView;
import com.nordicpeak.flowengine.beans.Flow;
import com.nordicpeak.flowengine.beans.FlowAction;
import com.nordicpeak.flowengine.beans.FlowFamily;
import com.nordicpeak.flowengine.beans.FlowFamilyEvent;
import com.nordicpeak.flowengine.beans.FlowForm;
import com.nordicpeak.flowengine.beans.FlowInstance;
import com.nordicpeak.flowengine.beans.FlowInstanceEvent;
import com.nordicpeak.flowengine.beans.FlowType;
import com.nordicpeak.flowengine.beans.QueryDescriptor;
import com.nordicpeak.flowengine.beans.StandardStatus;
import com.nordicpeak.flowengine.beans.Status;
import com.nordicpeak.flowengine.beans.Step;
import com.nordicpeak.flowengine.comparators.FlowVersionComparator;
import com.nordicpeak.flowengine.comparators.QueryDescriptorSortIndexComparator;
import com.nordicpeak.flowengine.comparators.StepSortIndexComparator;
import com.nordicpeak.flowengine.cruds.CategoryCRUD;
import com.nordicpeak.flowengine.cruds.EvaluatorDescriptorCRUD;
import com.nordicpeak.flowengine.cruds.FlowCRUD;
import com.nordicpeak.flowengine.cruds.FlowFamilyCRUD;
import com.nordicpeak.flowengine.cruds.FlowFormCRUD;
import com.nordicpeak.flowengine.cruds.FlowTypeCRUD;
import com.nordicpeak.flowengine.cruds.QueryDescriptorCRUD;
import com.nordicpeak.flowengine.cruds.StandardStatusCRUD;
import com.nordicpeak.flowengine.cruds.StatusCRUD;
import com.nordicpeak.flowengine.cruds.StepCRUD;
import com.nordicpeak.flowengine.enums.EventType;
import com.nordicpeak.flowengine.enums.ShowMode;
import com.nordicpeak.flowengine.enums.StatisticsMode;
import com.nordicpeak.flowengine.exceptions.FlowEngineException;
import com.nordicpeak.flowengine.exceptions.evaluation.EvaluationException;
import com.nordicpeak.flowengine.exceptions.evaluationprovider.EvaluationProviderException;
import com.nordicpeak.flowengine.exceptions.flow.FlowDefaultStatusNotFound;
import com.nordicpeak.flowengine.exceptions.flow.FlowNoLongerAvailableException;
import com.nordicpeak.flowengine.exceptions.flow.FlowNotAvailiableInRequestedFormat;
import com.nordicpeak.flowengine.exceptions.flowinstance.InvalidFlowInstanceStepException;
import com.nordicpeak.flowengine.exceptions.flowinstance.MissingQueryInstanceDescriptor;
import com.nordicpeak.flowengine.exceptions.flowinstancemanager.DuplicateFlowInstanceManagerIDException;
import com.nordicpeak.flowengine.exceptions.flowinstancemanager.FlowInstanceManagerClosedException;
import com.nordicpeak.flowengine.exceptions.queryinstance.QueryRequestException;
import com.nordicpeak.flowengine.exceptions.queryinstance.UnableToGetQueryInstanceShowHTMLException;
import com.nordicpeak.flowengine.exceptions.queryinstance.UnableToResetQueryInstanceException;
import com.nordicpeak.flowengine.exceptions.queryinstance.UnableToSaveQueryInstanceException;
import com.nordicpeak.flowengine.exceptions.queryprovider.QueryNotFoundInQueryProviderException;
import com.nordicpeak.flowengine.exceptions.queryprovider.QueryProviderErrorException;
import com.nordicpeak.flowengine.exceptions.queryprovider.QueryProviderException;
import com.nordicpeak.flowengine.exceptions.queryprovider.QueryProviderNotFoundException;
import com.nordicpeak.flowengine.interfaces.EvaluationHandler;
import com.nordicpeak.flowengine.interfaces.FlowAdminExtensionViewProvider;
import com.nordicpeak.flowengine.interfaces.FlowAdminShowFlowExtensionLinkProvider;
import com.nordicpeak.flowengine.interfaces.FlowBrowserExtensionViewProvider;
import com.nordicpeak.flowengine.interfaces.FlowFamilyEventHandler;
import com.nordicpeak.flowengine.interfaces.FlowInstanceAccessController;
import com.nordicpeak.flowengine.interfaces.FlowNotificationHandler;
import com.nordicpeak.flowengine.interfaces.FlowProcessCallback;
import com.nordicpeak.flowengine.interfaces.Icon;
import com.nordicpeak.flowengine.interfaces.ImmutableFlow;
import com.nordicpeak.flowengine.interfaces.ImmutableFlowInstance;
import com.nordicpeak.flowengine.interfaces.ImmutableStatus;
import com.nordicpeak.flowengine.interfaces.PDFRequestFilter;
import com.nordicpeak.flowengine.interfaces.Query;
import com.nordicpeak.flowengine.listeners.EvaluatorDescriptorElementableListener;
import com.nordicpeak.flowengine.listeners.FlowFormExportElementableListener;
import com.nordicpeak.flowengine.listeners.QueryDescriptorElementableListener;
import com.nordicpeak.flowengine.managers.FlowInstanceManager;
import com.nordicpeak.flowengine.managers.ImmutableFlowInstanceManager;
import com.nordicpeak.flowengine.managers.MutableFlowInstanceManager;
import com.nordicpeak.flowengine.managers.MutableFlowInstanceManager.FlowInstanceManagerRegistery;
import com.nordicpeak.flowengine.managers.UserGroupListFlowManagersConnector;
import com.nordicpeak.flowengine.utils.TextTagReplacer;
import com.nordicpeak.flowengine.validationerrors.EvaluatorImportValidationError;
import com.nordicpeak.flowengine.validationerrors.EvaluatorTypeNotFoundValidationError;
import com.nordicpeak.flowengine.validationerrors.NoQueryDescriptorSortindexValidationError;
import com.nordicpeak.flowengine.validationerrors.NoStepSortindexValidationError;
import com.nordicpeak.flowengine.validationerrors.QueryExportValidationError;
import com.nordicpeak.flowengine.validationerrors.QueryImportValidationError;
import com.nordicpeak.flowengine.validationerrors.QueryTypeNotAllowedInFlowTypeValidationError;
import com.nordicpeak.flowengine.validationerrors.QueryTypeNotFoundValidationError;

public class FlowAdminModule extends BaseFlowBrowserModule implements EventListener<CRUDEvent<?>>, AdvancedCRUDCallback<User>, AccessInterface, FlowProcessCallback, FlowFamilyEventHandler, MultipartLimitProvider, SystemStartupListener {

	public static final ValidationError FLOW_HAS_NO_CONTENT_VALIDATION_ERROR = new ValidationError("FlowHasNoContent");
	public static final ValidationError FLOW_HAS_NO_STEPS_AND_SKIP_OVERVIEW_IS_SET_VALIDATION_ERROR = new ValidationError("FlowHasNoStepsAndOverviewSkipIsSet");
	public static final ValidationError MAY_NOT_REMOVE_FLOW_FORM_IF_NO_STEPS_VALIDATION_ERROR = new ValidationError("MayNotRemoveFlowFormIfNoSteps");
	public static final ValidationError MAY_NOT_ADD_FLOW_FORM_IF_SKIP_OVERVIEW_IS_SET_VALIDATION_ERROR = new ValidationError("MayNotAddFlowFormIfOverviewSkipIsSet");
	public static final ValidationError MAY_NOT_SET_SKIP_OVERVIEW_IF_FLOW_FORM_IS_SET_VALIDATION_ERROR = new ValidationError("MayNotSetOverviewIfFlowFormIsSet");
	public static final ValidationError NO_MANAGERS_VALIDATION_ERROR = new ValidationError("NoManagersSet");

	@SuppressWarnings("rawtypes")
	private static final Class[] EVENT_LISTENER_CLASSES = new Class[] { FlowFamily.class, FlowType.class, Flow.class, Category.class, Step.class, QueryDescriptor.class, EvaluatorDescriptor.class, Status.class, FlowInstance.class, FlowForm.class };

	protected static final RelationQuery ADD_NEW_FLOW_AND_FAMILY_RELATION_QUERY = new RelationQuery(Flow.FLOW_FORMS_RELATION, Flow.STATUSES_RELATION, Flow.DEFAULT_FLOW_STATE_MAPPINGS_RELATION, Flow.STEPS_RELATION, Flow.FLOW_FAMILY_RELATION, Step.QUERY_DESCRIPTORS_RELATION, QueryDescriptor.EVALUATOR_DESCRIPTORS_RELATION, Flow.CHECKS_RELATION, Flow.TAGS_RELATION);
	protected static final RelationQuery ADD_NEW_FLOW_VERSION_RELATION_QUERY = new RelationQuery(Flow.FLOW_FORMS_RELATION, Flow.STATUSES_RELATION, Flow.DEFAULT_FLOW_STATE_MAPPINGS_RELATION, Flow.STEPS_RELATION, Step.QUERY_DESCRIPTORS_RELATION, QueryDescriptor.EVALUATOR_DESCRIPTORS_RELATION, Flow.CHECKS_RELATION, Flow.TAGS_RELATION);

	protected static final List<Field> LIST_FLOWS_IGNORED_FIELDS = Arrays.asList(FlowType.ALLOWED_ADMIN_GROUPS_RELATION, FlowType.ALLOWED_QUERIES_RELATION, FlowType.ALLOWED_ADMIN_USERS_RELATION, FlowType.CATEGORIES_RELATION, Flow.STATUSES_RELATION, Flow.DEFAULT_FLOW_STATE_MAPPINGS_RELATION, Flow.STEPS_RELATION);

	private static final StepSortIndexComparator STEP_COMPARATOR = new StepSortIndexComparator();
	private static final QueryDescriptorSortIndexComparator QUERY_DESCRIPTOR_COMPARATOR = new QueryDescriptorSortIndexComparator();
	private static final FlowVersionComparator FLOW_VERSION_COMPARATOR = new FlowVersionComparator();

	private final AdminUserFlowInstanceAccessController updateAccessController = new AdminUserFlowInstanceAccessController(this, true);
	private final AdminUserFlowInstanceAccessController previewAccessController = new AdminUserFlowInstanceAccessController(this, false);

	@XSLVariable(prefix = "java.")
	private String flowNameCopySuffix = " (copy)";

	@XSLVariable(prefix = "java.")
	private String fileMissing = "File is missing";

	@XSLVariable(prefix = "java.")
	private String eventCopyFlowMessage = "eventCopyFlowMessage";

	@XSLVariable(prefix = "java.")
	private String eventUpdateIconMessage = "eventUpdateIconMessage";

	@XSLVariable(prefix = "java.")
	private String eventUpdateNotificationsMessage = "eventUpdateNotificationsMessage";

	@XSLVariable(prefix = "java.")
	private String eventSortFlowMessage = "eventSortFlowMessage";

	@XSLVariable(prefix = "java.")
	private String eventImportFlowMessage = "eventImportFlowMessage";

	@XSLVariable(prefix = "java.")
	private String eventImportQueriesMessage = "eventImportQueriesMessage";

	@XSLVariable(prefix = "java.")
	private String eventFlowFamilyUpdatedMessage = "eventFlowFamilyUpdatedMessage";

	@XSLVariable(prefix = "java.")
	private String eventFlowAddedMessage = "eventFlowAddedMessage";

	@XSLVariable(prefix = "java.")
	private String eventFlowUpdatedMessage = "eventFlowUpdatedMessage";

	@XSLVariable(prefix = "java.")
	private String eventFlowDeletedMessage = "eventFlowDeletedMessage";

	@XSLVariable(prefix = "java.")
	private String eventStepAddedMessage = "eventStepAddedMessage";

	@XSLVariable(prefix = "java.")
	private String eventStepUpdatedMessage = "eventStepUpdatedMessage";

	@XSLVariable(prefix = "java.")
	private String eventStepDeletedMessage = "eventStepDeletedMessage";

	@XSLVariable(prefix = "java.")
	private String eventQueryAddedMessage = "eventQueryAddedMessage";

	@XSLVariable(prefix = "java.")
	private String eventQueryUpdatedMessage = "eventQueryUpdatedMessage";

	@XSLVariable(prefix = "java.")
	private String eventQueryDeletedMessage = "eventQueryDeletedMessage";

	@XSLVariable(prefix = "java.")
	private String eventEvaluatorAddedMessage = "eventEvaluatorAddedMessage";

	@XSLVariable(prefix = "java.")
	private String eventEvaluatorUpdatedMessage = "eventEvaluatorUpdatedMessage";

	@XSLVariable(prefix = "java.")
	private String eventEvaluatorDeletedMessage = "eventEvaluatorDeletedMessage";

	@XSLVariable(prefix = "java.")
	private String eventStatusAddedMessage = "eventStatusAddedMessage";

	@XSLVariable(prefix = "java.")
	private String eventStatusUpdatedMessage = "eventStatusUpdatedMessage";

	@XSLVariable(prefix = "java.")
	private String eventStatusDeletedMessage = "eventStatusDeletedMessage";

	@XSLVariable(prefix = "java.")
	private String eventFlowFormAddedMessage = "eventFlowFormAddedMessage";

	@XSLVariable(prefix = "java.")
	private String eventFlowFormUpdatedMessage = "eventFlowFormUpdatedMessage";

	@XSLVariable(prefix = "java.")
	private String eventFlowFormDeletedMessage = "eventFlowFormDeletedMessage";

	@XSLVariable(prefix = "java.")
	private String eventChangeFlowType = "eventChangeFlowType";

	@XSLVariable(prefix = "java.")
	private String eventStatusSortMessage = "eventStatusSortMessage";
	
	@XSLVariable(prefix = "java.")
	private String eventFunctionConfigured = "eventFunctionConfigured";

	@XSLVariable(prefix = "java.")
	private String bundleListFlows= "List flows";
	
	@XSLVariable(prefix = "java.")
	private String bundleAddFlow = "Add flow";

	@XSLVariable(prefix = "java.")
	private String bundleImportFlow = "Import flow";

	@XSLVariable(prefix = "java.")
	private String bundleStandardStatuses = "Administrate standard statuses";

	@XSLVariable(prefix = "java.")
	private String bundleFlowtypes = "Administrate flow types";

	@ModuleSetting(allowsNull = true)
	@GroupMultiListSettingDescriptor(name = "Admin groups", description = "Groups allowed to administrate global parts of this module such as standard statuses")
	protected List<Integer> adminGroupIDs;

	@ModuleSetting(allowsNull = true)
	@UserMultiListSettingDescriptor(name = "Admin users", description = "Users allowed to administrate global parts of this module such as standard statuses")
	protected List<Integer> adminUserIDs;

	@ModuleSetting(allowsNull = true)
	@GroupMultiListSettingDescriptor(name = "Publisher groups", description = "Groups allowed to change enabled and publish settings for flows")
	protected List<Integer> publisherGroupIDs;
	
	@ModuleSetting(allowsNull = true)
	@GroupMultiListSettingDescriptor(name = "Manager groups", description = "Groups with users that are allowed to be set as managers")
	protected List<Integer> managerGroupIDs;

	@ModuleSetting
	@CheckboxSettingDescriptor(name = "Require managers", description = "Controls if it's required to have managers set when publishing a flow")
	protected boolean requireManagers = false;

	@ModuleSetting
	@TextFieldSettingDescriptor(name = "Max flow icon width", description = "Max allowed flow icon width.")
	private int maxFlowIconWidth = 100;

	@ModuleSetting
	@TextFieldSettingDescriptor(name = "Max flow icon height", description = "Max allowed flow icon height.")
	private int maxFlowIconHeight = 100;

	@ModuleSetting(allowsNull = true)
	@TextFieldSettingDescriptor(name = "CKEditor connector module alias", description = "The full alias of the CKEditor connector module (relative from the contextpath). Leave empty if you do not want to activate file manager for CKEditor")
	protected String ckConnectorModuleAlias;

	@ModuleSetting(allowsNull = true)
	@TextFieldSettingDescriptor(name = "Editor CSS", description = "Path to the desired CSS stylesheet for CKEditor (relative from the contextpath)", required = false)
	protected String cssPath;

	@ModuleSetting(id = "pdfFormFilestore")
	@TextFieldSettingDescriptor(id = "pdfFormFilestore", name = "Flow PDF form filestore", description = "Directory where attached PDF forms are stored", required = true)
	protected String flowFormFilestore;

	@ModuleSetting
	@TextFieldSettingDescriptor(name = "Max PDF form file size", description = "Maxmium file size in megabytes allowed", required = true, formatValidator = PositiveStringIntegerValidator.class)
	protected Integer maxPDFFormFileSize = 15;

	@ModuleSetting(id = "allowSkipOverviewForPDFForms")
	@CheckboxSettingDescriptor(name = "Allow skip overview for PDF forms", description = "If false always shows the overview if a PDF form is available. Requires special button on step pages if set.")
	protected boolean allowSkipOverviewForFlowForms = false;

	@ModuleSetting
	@CheckboxSettingDescriptor(name = "Open external flows in new window", description = "Controls whether to open external flows in new window or not")
	protected boolean openExternalFlowsInNewWindow = true;

	@ModuleSetting
	@CheckboxSettingDescriptor(name = "Use categories", description = "Controls whether to show columns for categories")
	protected boolean useCategories = false;

	@ModuleSetting
	@TextFieldSettingDescriptor(name = "Recent flow family event count", description = "Amount of flow family events to show on showFlow", required = true, formatValidator = PositiveStringIntegerValidator.class)
	protected Integer recentFlowFamilyEventCount = 5;

	@ModuleSetting
	@CheckboxSettingDescriptor(name = "Require tags", description = "Controls if tags should be required when adding and updating flows")
	private boolean requireTags;

	@ModuleSetting
	@EnumDropDownSettingDescriptor(name = "Default statistics mode", description = "Controls the default statistics mode when adding new tags.")
	private StatisticsMode defaultStatisticsMode = StatisticsMode.INTERNAL;

	@ModuleSetting
	@CheckboxSettingDescriptor(name = "Use flowtype icon upload", description = "Controls whether flowtype icon upload should be used for flowtypes")
	protected boolean useFlowTypeIconUpload = false;

	@ModuleSetting
	@CheckboxSettingDescriptor(name = "Use bundle instead of menuitem", description = "Controls whether bundle should be generated instead of menuitem")
	protected boolean useBundle = false;

	@ModuleSetting
	@TextFieldSettingDescriptor(name = "Max flowtype icon width", description = "Max allowed flowtype icon width.")
	private int maxFlowTypeIconWidth = 100;

	@ModuleSetting
	@TextFieldSettingDescriptor(name = "Max flowtype icon height", description = "Max allowed flowtype icon height.")
	private int maxFlowTypeIconHeight = 100;
	
	@XSLVariable(prefix = "java.")
	@ModuleSetting(allowsNull = true)
	@TextFieldSettingDescriptor(name = "Default login help link name", description = "Name of the login help link.")
	private String defaultLoginHelpLinkName;
	
	@ModuleSetting(allowsNull = true)
	@TextFieldSettingDescriptor(name = "Default login help url", description = "URL to redirect the user to for login help.", formatValidator = StringURLPopulator.class)
	private String defaultLoginHelpLinkURL;

	@InstanceManagerDependency(required = true)
	protected SiteProfileHandler siteProfileHandler;

	@InstanceManagerDependency
	protected FlowNotificationHandler notificationHandler;

	private FlowFamilyCRUD flowFamilyCRUD;
	private FlowCRUD flowCRUD;
	private StepCRUD stepCRUD;
	private QueryDescriptorCRUD queryDescriptorCRUD;
	private EvaluatorDescriptorCRUD evaluatorDescriptorCRUD;
	private StatusCRUD statusCRUD;
	private StandardStatusCRUD standardStatusCRUD;
	private FlowTypeCRUD flowTypeCRUD;
	private CategoryCRUD categoryCRUD;
	private FlowFormCRUD flowFormCRUD;

	protected QueryParameterFactory<FlowFamily, Integer> flowFamiliyIDParamFactory;

	protected QueryParameterFactory<Flow, FlowFamily> flowFlowFamilyParamFactory;
	protected QueryParameterFactory<Flow, Integer> flowVersionParamFactory;

	protected QueryParameterFactory<FlowFamilyEvent, FlowFamily> flowFamilyEventFlowFamilyParamFactory;

	protected QueryParameterFactory<FlowInstance, Flow> flowInstanceFlowParamFactory;
	protected QueryParameterFactory<FlowInstance, Status> flowInstanceStatusParamFactory;
	protected QueryParameterFactory<FlowInstance, Timestamp> flowInstanceFirstSubmittedParamFactory;

	protected QueryParameterFactory<FlowAction, Boolean> flowActionRequiredParamFactory;
	protected QueryParameterFactory<FlowAction, String> flowActionIDParamFactory;

	protected QueryParameterFactory<QueryDescriptor, String> queryDescriptorQueryTypeIDParamFactory;
	protected QueryParameterFactory<EvaluatorDescriptor, String> evaluatorDescriptorEvaluatorTypeIDParamFactory;

	protected OrderByCriteria<Flow> flowVersionOrderByCriteria;

	private LinkedHashMap<Integer, FlowType> flowTypeCacheMap;
	private LinkedHashMap<Integer, Flow> flowCacheMap;
	private HashMap<Integer, FlowFamily> flowFamilyCacheMap;

	protected UserGroupListConnector userGroupListConnector;
	protected UserGroupListConnector unrestrictedUserGroupListConnector;
	protected UserGroupListFlowManagersConnector userGroupListFlowManagersConnector;

	protected CopyOnWriteArrayList<PDFRequestFilter> pdfRequestFilters = new CopyOnWriteArrayList<PDFRequestFilter>();

	protected CopyOnWriteArrayList<FlowAdminExtensionViewProvider> extensionViewProviders = new CopyOnWriteArrayList<FlowAdminExtensionViewProvider>();

	protected CopyOnWriteArrayList<ExtensionLinkProvider> flowListExtensionLinkProviders = new CopyOnWriteArrayList<ExtensionLinkProvider>();
	protected CopyOnWriteArrayList<FlowAdminShowFlowExtensionLinkProvider> flowShowExtensionLinkProviders = new CopyOnWriteArrayList<FlowAdminShowFlowExtensionLinkProvider>();
	
	protected CopyOnWriteArrayList<FlowBrowserExtensionViewProvider> flowBrowserExtensionViewProviders = new CopyOnWriteArrayList<FlowBrowserExtensionViewProvider>();

	@Override
	public void init(ForegroundModuleDescriptor moduleDescriptor, SectionInterface sectionInterface, DataSource dataSource) throws Exception {

		super.init(moduleDescriptor, sectionInterface, dataSource);

		cacheFlows();
		cacheFlowTypes();

		eventHandler.addEventListener(CRUDEvent.class, this, EVENT_LISTENER_CLASSES);
		
		userGroupListConnector = new UserGroupListConnector(systemInterface);
		userGroupListConnector.setUserGroupFilter(managerGroupIDs);
		
		unrestrictedUserGroupListConnector = new UserGroupListConnector(systemInterface);
		
		userGroupListFlowManagersConnector = new UserGroupListFlowManagersConnector(systemInterface, this);
		
		if (!systemInterface.getInstanceHandler().addInstance(FlowAdminModule.class, this)) {

			throw new RuntimeException("Unable to register module in global instance handler using key " + FlowAdminModule.class.getSimpleName() + ", another instance is already registered using this key.");
		}
		
		if (systemInterface.getSystemStatus() == SystemStatus.STARTED) {
			systemStarted();
		} else if (systemInterface.getSystemStatus() == SystemStatus.STARTING) {
			systemInterface.addSystemStartupListener(this);
		}
	}

	@Override
	public void unload() throws Exception {

		eventHandler.removeEventListener(CRUDEvent.class, this, EVENT_LISTENER_CLASSES);

		systemInterface.getInstanceHandler().removeInstance(FlowAdminModule.class, this);

		extensionViewProviders.clear();
		flowBrowserExtensionViewProviders.clear();

		flowListExtensionLinkProviders.clear();

		flowShowExtensionLinkProviders.clear();

		super.unload();
	}

	@Override
	protected void createDAOs(DataSource dataSource) throws Exception {

		super.createDAOs(dataSource);

		flowFamiliyIDParamFactory = daoFactory.getFlowFamilyDAO().getParamFactory("flowFamilyID", Integer.class);

		flowFlowFamilyParamFactory = daoFactory.getFlowDAO().getParamFactory("flowFamily", FlowFamily.class);
		flowVersionParamFactory = daoFactory.getFlowDAO().getParamFactory("version", Integer.class);

		flowInstanceFlowParamFactory = daoFactory.getFlowInstanceDAO().getParamFactory("flow", Flow.class);
		flowInstanceStatusParamFactory = daoFactory.getFlowInstanceDAO().getParamFactory("status", Status.class);
		flowInstanceFirstSubmittedParamFactory = daoFactory.getFlowInstanceDAO().getParamFactory("firstSubmitted", Timestamp.class);

		flowActionRequiredParamFactory = daoFactory.getFlowActionDAO().getParamFactory("required", boolean.class);
		flowActionIDParamFactory = daoFactory.getFlowActionDAO().getParamFactory("actionID", String.class);

		queryDescriptorQueryTypeIDParamFactory = daoFactory.getQueryDescriptorDAO().getParamFactory("queryTypeID", String.class);
		evaluatorDescriptorEvaluatorTypeIDParamFactory = daoFactory.getEvaluatorDescriptorDAO().getParamFactory("evaluatorTypeID", String.class);

		AnnotatedDAOWrapper<FlowFamily, Integer> flowFamilyDAOWrapper = daoFactory.getFlowFamilyDAO().getWrapper("flowFamilyID", Integer.class);
		flowFamilyDAOWrapper.addRelations(FlowFamily.MANAGER_GROUPS_RELATION, FlowFamily.MANAGER_USERS_RELATION);
		flowFamilyDAOWrapper.setUseRelationsOnGet(true);
		flowFamilyDAOWrapper.setUseRelationsOnUpdate(true);
		this.flowFamilyCRUD = new FlowFamilyCRUD(flowFamilyDAOWrapper, this);

		AdvancedAnnotatedDAOWrapper<Flow, Integer> flowDAOWrapper = daoFactory.getFlowDAO().getAdvancedWrapper("flowID", Integer.class);
		flowDAOWrapper.getAddQuery().addRelations(Flow.FLOW_FAMILY_RELATION, Flow.STATUSES_RELATION, Status.DEFAULT_STATUS_MAPPINGS_RELATION, Flow.TAGS_RELATION, Flow.CHECKS_RELATION, FlowFamily.ALIASES_RELATION);
		flowDAOWrapper.getUpdateQuery().addRelations(Flow.FLOW_FAMILY_RELATION, Flow.TAGS_RELATION, Flow.CHECKS_RELATION, FlowFamily.ALIASES_RELATION);

		this.flowCRUD = new FlowCRUD(flowDAOWrapper, this);

		AnnotatedDAOWrapper<QueryDescriptor, Integer> queryDescriptorDAOWrapper = daoFactory.getQueryDescriptorDAO().getWrapper(Integer.class);
		queryDescriptorDAOWrapper.addRelations(QueryDescriptor.STEP_RELATION, Step.FLOW_RELATION, Flow.FLOW_TYPE_RELATION, Flow.CATEGORY_RELATION, FlowType.ALLOWED_ADMIN_GROUPS_RELATION, FlowType.ALLOWED_ADMIN_USERS_RELATION, QueryDescriptor.EVALUATOR_DESCRIPTORS_RELATION);
		queryDescriptorDAOWrapper.setUseRelationsOnGet(true);

		this.queryDescriptorCRUD = new QueryDescriptorCRUD(queryDescriptorDAOWrapper, this);

		AnnotatedDAOWrapper<EvaluatorDescriptor, Integer> evaluatorDescriptorDAOWrapper = daoFactory.getEvaluatorDescriptorDAO().getWrapper(Integer.class);
		evaluatorDescriptorDAOWrapper.addRelations(EvaluatorDescriptor.QUERY_DESCRIPTOR_RELATION, QueryDescriptor.STEP_RELATION, Step.FLOW_RELATION, Flow.FLOW_TYPE_RELATION, Flow.CATEGORY_RELATION, FlowType.ALLOWED_ADMIN_GROUPS_RELATION, FlowType.ALLOWED_ADMIN_USERS_RELATION);
		evaluatorDescriptorDAOWrapper.setUseRelationsOnGet(true);

		this.evaluatorDescriptorCRUD = new EvaluatorDescriptorCRUD(evaluatorDescriptorDAOWrapper, this);

		AnnotatedDAOWrapper<Step, Integer> stepDAOWrapper = daoFactory.getStepDAO().getWrapper(Integer.class);
		stepDAOWrapper.addRelations(Step.FLOW_RELATION, Flow.FLOW_TYPE_RELATION, Flow.CATEGORY_RELATION, FlowType.ALLOWED_ADMIN_GROUPS_RELATION, FlowType.ALLOWED_ADMIN_USERS_RELATION, Step.QUERY_DESCRIPTORS_RELATION, QueryDescriptor.EVALUATOR_DESCRIPTORS_RELATION);
		stepDAOWrapper.setUseRelationsOnGet(true);

		stepCRUD = new StepCRUD(stepDAOWrapper, this);
		stepCRUD.addRequestFilter(new TransactionRequestFilter(dataSource));

		AdvancedAnnotatedDAOWrapper<Status, Integer> statusDAOWrapper = daoFactory.getStatusDAO().getAdvancedWrapper("statusID", Integer.class);
		statusDAOWrapper.getGetQuery().addRelations(Status.DEFAULT_STATUS_MAPPINGS_RELATION, Status.FLOW_RELATION, Flow.FLOW_TYPE_RELATION, FlowType.ALLOWED_ADMIN_GROUPS_RELATION, FlowType.ALLOWED_ADMIN_USERS_RELATION, Status.MANAGER_GROUPS_RELATION, Status.MANAGER_USERS_RELATION);
		statusDAOWrapper.getAddQuery().addRelations(Status.MANAGER_GROUPS_RELATION, Status.MANAGER_USERS_RELATION);
		statusDAOWrapper.getUpdateQuery().addRelations(Status.MANAGER_GROUPS_RELATION, Status.MANAGER_USERS_RELATION);

		statusCRUD = new StatusCRUD(statusDAOWrapper, this);

		AnnotatedDAOWrapper<StandardStatus, Integer> standardStatusDAOWrapper = daoFactory.getStandardStatusDAO().getWrapper("statusID", Integer.class);
		standardStatusDAOWrapper.addRelations(StandardStatus.DEFAULT_STANDARD_STATUS_MAPPINGS_RELATION);
		standardStatusDAOWrapper.setUseRelationsOnGet(true);

		standardStatusCRUD = new StandardStatusCRUD(standardStatusDAOWrapper, this);

		AnnotatedDAOWrapper<FlowType, Integer> flowTypeDAOWrapper = daoFactory.getFlowTypeDAO().getWrapper("flowTypeID", Integer.class);
		flowTypeDAOWrapper.addRelations(FlowType.ALLOWED_ADMIN_GROUPS_RELATION, FlowType.ALLOWED_QUERIES_RELATION, FlowType.ALLOWED_ADMIN_USERS_RELATION, FlowType.ALLOWED_GROUPS_RELATION, FlowType.ALLOWED_USERS_RELATION);
		flowTypeDAOWrapper.setUseRelationsOnAdd(true);
		flowTypeDAOWrapper.setUseRelationsOnUpdate(true);

		this.flowTypeCRUD = new FlowTypeCRUD(flowTypeDAOWrapper, this);
		this.flowTypeCRUD.addRequestFilter(new MultipartRequestFilter(this));

		AnnotatedDAOWrapper<Category, Integer> categoryDAOWrapper = daoFactory.getCategoryDAO().getWrapper("categoryID", Integer.class);
		categoryDAOWrapper.addRelations(Category.FLOW_TYPE_RELATION);
		categoryDAOWrapper.setUseRelationsOnGet(true);

		categoryCRUD = new CategoryCRUD(categoryDAOWrapper, this);

		AnnotatedDAOWrapper<FlowForm, Integer> flowFormDAOWrapper = daoFactory.getFlowFormDAO().getWrapper("flowFormID", Integer.class);
		flowFormDAOWrapper.addRelations(FlowForm.FLOW_RELATION, Flow.FLOW_TYPE_RELATION, FlowType.ALLOWED_ADMIN_GROUPS_RELATION, FlowType.ALLOWED_ADMIN_USERS_RELATION);
		flowFormDAOWrapper.setUseRelationsOnGet(true);

		flowFormCRUD = new FlowFormCRUD(flowFormDAOWrapper, this);
		flowFormCRUD.addRequestFilter(new MultipartRequestFilter(this));

		flowVersionOrderByCriteria = daoFactory.getFlowDAO().getOrderByCriteria("version", Order.DESC);

		flowFamilyEventFlowFamilyParamFactory = daoFactory.getFlowFamilyEventDAO().getParamFactory("flowFamily", FlowFamily.class);
	}

	@Override
	protected void moduleConfigured() throws Exception {

		super.moduleConfigured();

		if (!FileUtils.directoryExists(flowFormFilestore)) {

			log.error("Module " + this.moduleDescriptor + " has no/invalid PDF form filestore set, check modulesettings");
		}
		
		if(this.userGroupListConnector != null){
			
			this.userGroupListConnector.setUserGroupFilter(managerGroupIDs);
		}
	}

	protected synchronized void cacheFlowTypes() throws SQLException {

		long startTime = System.currentTimeMillis();

		List<FlowType> flowTypes = daoFactory.getFlowTypeDAO().getAll(new HighLevelQuery<FlowType>(FlowType.ALLOWED_ADMIN_GROUPS_RELATION, FlowType.ALLOWED_ADMIN_USERS_RELATION, FlowType.ALLOWED_QUERIES_RELATION, FlowType.CATEGORIES_RELATION, FlowType.ALLOWED_GROUPS_RELATION, FlowType.ALLOWED_USERS_RELATION));

		if (flowTypes == null) {

			flowTypeCacheMap = new LinkedHashMap<Integer, FlowType>(0);

		} else {

			LinkedHashMap<Integer, FlowType> tempFlowTypeMap = new LinkedHashMap<Integer, FlowType>(flowTypes.size());

			for (FlowType flowType : flowTypes) {

				tempFlowTypeMap.put(flowType.getFlowTypeID(), flowType);
			}

			flowTypeCacheMap = tempFlowTypeMap;
		}

		log.info("Cached " + CollectionUtils.getSize(flowTypes) + " flow types in " + TimeUtils.millisecondsToString(System.currentTimeMillis() - startTime) + " ms");
	}

	public synchronized void cacheFlows() throws SQLException {

		TransactionHandler transactionHandler = null;

		try {

			transactionHandler = new TransactionHandler(dataSource);

			long startTime = System.currentTimeMillis();

			HighLevelQuery<Flow> query = new HighLevelQuery<Flow>(Flow.FLOW_FORMS_RELATION, Flow.FLOW_TYPE_RELATION, FlowType.CATEGORIES_RELATION, Flow.CATEGORY_RELATION, Flow.STEPS_RELATION, Flow.STATUSES_RELATION, Step.QUERY_DESCRIPTORS_RELATION, QueryDescriptor.EVALUATOR_DESCRIPTORS_RELATION, Flow.DEFAULT_FLOW_STATE_MAPPINGS_RELATION, Flow.FLOW_FAMILY_RELATION, FlowFamily.MANAGER_USERS_RELATION, FlowFamily.MANAGER_GROUPS_RELATION, DefaultStatusMapping.FLOW_STATE_RELATION, FlowType.ALLOWED_ADMIN_GROUPS_RELATION, FlowType.ALLOWED_ADMIN_USERS_RELATION, FlowType.ALLOWED_QUERIES_RELATION, Flow.TAGS_RELATION, Flow.CHECKS_RELATION, FlowFamily.ALIASES_RELATION);

			List<Flow> flows = daoFactory.getFlowDAO().getAll(query, transactionHandler);

			if (flows == null) {

				flowCacheMap = new LinkedHashMap<Integer, Flow>(0);
				flowFamilyCacheMap = new HashMap<Integer, FlowFamily>();

			} else {

				LinkedHashMap<Integer, Flow> tempFlowCacheMap = new LinkedHashMap<Integer, Flow>(flows.size());
				HashMap<Integer, FlowFamily> tempFlowFamilyMap = new HashMap<Integer, FlowFamily>();

				for (Flow flow : flows) {

					flow.setFlowInstanceCount(getFlowInstanceCount(flow, transactionHandler));
					flow.setFlowSubmittedInstanceCount(getFlowSubmittedInstanceCount(flow, transactionHandler));
					flow.setLatestVersion(isLatestVersion(flow, transactionHandler));

					setStatusFlowInstanceCount(flow, transactionHandler);

					tempFlowCacheMap.put(flow.getFlowID(), flow);
					tempFlowFamilyMap.put(flow.getFlowFamily().getFlowFamilyID(), flow.getFlowFamily());
				}

				flowCacheMap = tempFlowCacheMap;
				flowFamilyCacheMap = tempFlowFamilyMap;
			}

			log.info("Cached " + flowCacheMap.size() + " flows from " + flowFamilyCacheMap.size() + " flow families in " + TimeUtils.millisecondsToString(System.currentTimeMillis() - startTime) + " ms");

		} finally {

			TransactionHandler.autoClose(transactionHandler);
		}
	}

	private void setStatusFlowInstanceCount(Flow flow, TransactionHandler transactionHandler) throws SQLException {

		if (flow.getStatuses() != null) {

			for (Status status : flow.getStatuses()) {

				status.setFlowInstanceCount(getFlowInstanceCount(status, transactionHandler));
				status.setFlowSubmittedInstanceCount(getFlowSubmittedInstanceCount(status, transactionHandler));
			}
		}
	}

	private Boolean isLatestVersion(Flow flow, TransactionHandler transactionHandler) throws SQLException {

		HighLevelQuery<Flow> query = new HighLevelQuery<Flow>();

		query.addParameter(flowFlowFamilyParamFactory.getParameter(flow.getFlowFamily()));
		query.addParameter(flowVersionParamFactory.getParameter(flow.getVersion(), QueryOperators.BIGGER_THAN));

		return !daoFactory.getFlowDAO().getBoolean(query, transactionHandler);
	}

	public Integer getFlowInstanceCount(Flow flow, TransactionHandler transactionHandler) throws SQLException {

		HighLevelQuery<FlowInstance> query = new HighLevelQuery<FlowInstance>();

		query.addParameter(flowInstanceFlowParamFactory.getParameter(flow));

		return daoFactory.getFlowInstanceDAO().getCount(query, transactionHandler);
	}

	public Integer getFlowInstanceCount(Flow flow) throws SQLException {

		HighLevelQuery<FlowInstance> query = new HighLevelQuery<FlowInstance>();

		query.addParameter(flowInstanceFlowParamFactory.getParameter(flow));

		return daoFactory.getFlowInstanceDAO().getCount(query);
	}

	public Integer getFlowInstanceCount(Status status) throws SQLException {

		HighLevelQuery<FlowInstance> query = new HighLevelQuery<FlowInstance>();

		query.addParameter(flowInstanceStatusParamFactory.getParameter(status));

		return daoFactory.getFlowInstanceDAO().getCount(query);
	}

	public Integer getFlowInstanceCount(Status status, TransactionHandler transactionHandler) throws SQLException {

		HighLevelQuery<FlowInstance> query = new HighLevelQuery<FlowInstance>();

		query.addParameter(flowInstanceStatusParamFactory.getParameter(status));

		return daoFactory.getFlowInstanceDAO().getCount(query, transactionHandler);
	}

	public Integer getFlowSubmittedInstanceCount(Flow flow, TransactionHandler transactionHandler) throws SQLException {

		HighLevelQuery<FlowInstance> query = new HighLevelQuery<FlowInstance>();

		query.addParameter(flowInstanceFlowParamFactory.getParameter(flow));
		query.addParameter(flowInstanceFirstSubmittedParamFactory.getIsNotNullParameter());

		return daoFactory.getFlowInstanceDAO().getCount(query, transactionHandler);
	}

	public Integer getFlowSubmittedInstanceCount(Status status, TransactionHandler transactionHandler) throws SQLException {

		HighLevelQuery<FlowInstance> query = new HighLevelQuery<FlowInstance>();

		query.addParameter(flowInstanceStatusParamFactory.getParameter(status));
		query.addParameter(flowInstanceFirstSubmittedParamFactory.getIsNotNullParameter());

		return daoFactory.getFlowInstanceDAO().getCount(query, transactionHandler);
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

		log.info("User " + user + " listing flows");

		Document doc = this.createDocument(req, uriParser, user);

		Element listFlowsElement = doc.createElement("ListFlows");

		doc.getDocumentElement().appendChild(listFlowsElement);

		if (hasFlowTypeAccess(user)) {

			XMLGeneratorDocument generatorDocument = new XMLGeneratorDocument(doc);
			generatorDocument.setIgnoredFields(LIST_FLOWS_IGNORED_FIELDS);

			listFlowsElement.appendChild(doc.createElement("AddAccess"));

			Collection<Flow> flows = this.flowCacheMap.values();

			if (!AccessUtils.checkAccess(user, this)) {

				//Check access and append flows
				for (Flow flow : flows) {

					if (AccessUtils.checkAccess(user, flow.getFlowType().getAdminAccessInterface()) && flow.isLatestVersion()) {

						Element flowElement = flow.toXML(generatorDocument);

						appendFamilyInformation(doc, flowElement, flow, flows);

						listFlowsElement.appendChild(flowElement);
					}
				}

			} else {

				for (Flow flow : flows) {

					if (flow.isLatestVersion()) {
						Element flowElement = flow.toXML(generatorDocument);

						appendFamilyInformation(doc, flowElement, flow, flows);

						listFlowsElement.appendChild(flowElement);
					}
				}
			}
		}

		if (AccessUtils.checkAccess(user, this)) {
			XMLUtils.appendNewElement(doc, listFlowsElement, "AdminAccess");
		}

		if (validationErrors != null) {

			XMLUtils.append(doc, listFlowsElement, validationErrors);
		}

		ExtensionLinkUtils.appendExtensionLinks(this.flowListExtensionLinkProviders, user, req, doc, listFlowsElement);

		return new SimpleForegroundModuleResponse(doc, this.getDefaultBreadcrumb());
	}

	public void appendFamilyInformation(Document doc, Element flowElement, Flow lastestFlow, Collection<Flow> flows) {

		int versions = 0;
		int instances = 0;
		int submittedInstances = 0;
		boolean published = false;
		boolean external = false;

		FlowFamily flowFamily = lastestFlow.getFlowFamily();

		for (Flow flow : flows) {

			if (flow.getFlowFamily().equals(flowFamily)) {

				versions++;
				instances += flow.getFlowInstanceCount();
				submittedInstances += flow.getFlowSubmittedInstanceCount();

				if (!published && flow.isEnabled() && flow.isPublished()) {

					published = true;
				}

				if (!flow.isInternal()) {

					external = true;
				}
			}
		}

		XMLUtils.appendNewElement(doc, flowElement, "VersionCount", versions);
		XMLUtils.appendNewElement(doc, flowElement, "InstanceCount", instances);
		XMLUtils.appendNewElement(doc, flowElement, "SubmittedInstanceCount", submittedInstances);

		if (external) {
			XMLUtils.appendNewElement(doc, flowElement, "HasExternalVersions");
		}

		if (published) {

			XMLUtils.appendNewElement(doc, flowElement, "HasPublishedVersion");
		}
	}

	public boolean hasFlowTypeAccess(User user) {

		if (AccessUtils.checkAccess(user, this) && !this.flowTypeCacheMap.isEmpty()) {

			return true;
		}

		//Check if the user has access to any flow type
		for (FlowType flowType : this.flowTypeCacheMap.values()) {

			if (AccessUtils.checkAccess(user, flowType.getAdminAccessInterface())) {

				return true;
			}
		}

		return false;
	}

	@WebPublic(toLowerCase = true)
	public ForegroundModuleResponse showFlow(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Exception, Throwable {

		String validationError = req.getParameter("error");

		if (!StringUtils.isEmpty(validationError)) {

			if (validationError.equals(MAY_NOT_REMOVE_FLOW_FORM_IF_NO_STEPS_VALIDATION_ERROR.getMessageKey())) {

				return flowCRUD.show(req, res, user, uriParser, Collections.singletonList(MAY_NOT_REMOVE_FLOW_FORM_IF_NO_STEPS_VALIDATION_ERROR));

			} else if (validationError.equals(MAY_NOT_ADD_FLOW_FORM_IF_SKIP_OVERVIEW_IS_SET_VALIDATION_ERROR.getMessageKey())) {

				return flowCRUD.show(req, res, user, uriParser, Collections.singletonList(MAY_NOT_ADD_FLOW_FORM_IF_SKIP_OVERVIEW_IS_SET_VALIDATION_ERROR));
			}
		}

		return flowCRUD.show(req, res, user, uriParser);
	}

	@WebPublic(toLowerCase = true)
	public ForegroundModuleResponse addFlow(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Exception, Throwable {

		return flowCRUD.add(req, res, user, uriParser);
	}

	@WebPublic(toLowerCase = true)
	public ForegroundModuleResponse updateFlow(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Exception, Throwable {

		return flowCRUD.update(req, res, user, uriParser);
	}

	@WebPublic(toLowerCase = true)
	public ForegroundModuleResponse deleteFlow(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Exception, Throwable {

		return flowCRUD.delete(req, res, user, uriParser);
	}

	@WebPublic(toLowerCase = true)
	public synchronized ForegroundModuleResponse deleteFlowFamily(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Exception, Throwable {

		FlowFamily flowFamily;

		if (uriParser.size() != 3 || !NumberUtils.isInt(uriParser.get(2)) || (flowFamily = flowFamilyCacheMap.get(NumberUtils.toInt(uriParser.get(2)))) == null) {

			return list(req, res, user, uriParser, new ValidationError("RequestedFlowFamilyNotFound"));
		}

		List<Flow> flows = getFlowVersions(flowFamily);

		for (Flow flow : flows) {

			if ((flow.isPublished() && flow.isEnabled()) || flow.getFlowInstanceCount() > 0) {

				return list(req, res, user, uriParser, new ValidationError("FlowFamilyCannotBeDeleted"));
			}
		}

		log.info("User " + user + " deleting flow family " + flowFamily.getFlowFamilyID() + " with flows " + flows);

		TransactionHandler transactionHandler = null;

		try {
			transactionHandler = daoFactory.getTransactionHandler();

			for (Flow flow : flows) {

				if (flow.getSteps() != null) {

					for (Step step : flow.getSteps()) {

						if (step.getQueryDescriptors() != null) {

							for (QueryDescriptor queryDescriptor : step.getQueryDescriptors()) {

								if (queryDescriptor.getEvaluatorDescriptors() != null) {

									for (EvaluatorDescriptor evaluatorDescriptor : queryDescriptor.getEvaluatorDescriptors()) {

										getEvaluationHandler().deleteEvaluator(evaluatorDescriptor, transactionHandler);
									}
								}

								getQueryHandler().deleteQuery(queryDescriptor, transactionHandler);
							}
						}
					}
				}
			}

			daoFactory.getFlowFamilyDAO().delete(flowFamily);

			transactionHandler.commit();

		} finally {

			TransactionHandler.autoClose(transactionHandler);
		}

		if (!flows.isEmpty()) {

			eventHandler.sendEvent(Flow.class, new CRUDEvent<Flow>(Flow.class, CRUDAction.DELETE, flows), EventTarget.ALL);
		}

		eventHandler.sendEvent(FlowFamily.class, new CRUDEvent<FlowFamily>(CRUDAction.DELETE, flowFamily), EventTarget.ALL);

		redirectToDefaultMethod(req, res);

		return null;
	}

	@WebPublic(toLowerCase = true)
	public synchronized ForegroundModuleResponse copyFlow(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Exception, Throwable {

		Integer flowID = NumberUtils.toInt(req.getParameter("flowID"));

		Flow flow;

		if (flowID == null || (flow = this.getCachedFlow(flowID)) == null) {

			return list(req, res, user, uriParser, new ValidationError("FlowNotFound"));
		}

		Flow flowCopy = SerializationUtils.cloneSerializable(flow);

		flowCopy.setFlowID(null);
		flowCopy.setEnabled(false);
		flowCopy.setIcon(flow.getIcon());
		flowCopy.setPublishDate(null);
		flowCopy.setUnPublishDate(null);

		boolean newFamily = req.getParameter("new_family") != null;

		if (newFamily) {

			log.info("User " + user + " creating new flow based on flow " + flow);

			FlowFamily flowFamily = new FlowFamily();
			flowFamily.setVersionCount(1);
			flowFamily.setStatisticsMode(flow.getFlowFamily().getStatisticsMode());
			flowCopy.setFlowFamily(flowFamily);
			flowCopy.setVersion(1);
			flowCopy.setName(flow.getName() + flowNameCopySuffix);

		} else {

			log.info("User " + user + " creating new flow version based on flow " + flow);
		}

		if (flowCopy.getDefaultFlowStateMappings() != null) {

			outer: for (DefaultStatusMapping defaultStatusMapping : flowCopy.getDefaultFlowStateMappings()) {

				for (Status status : flowCopy.getStatuses()) {

					if (status.equals(defaultStatusMapping.getStatus())) {

						defaultStatusMapping.setStatus(status);
						defaultStatusMapping.getStatus().setFlow(flowCopy);
						continue outer;
					}
				}

				throw new RuntimeException("Unable to find status " + defaultStatusMapping.getStatus() + " in statuses of flow " + flowCopy);
			}
		}

		Map<Integer, ImmutableStatus> statusConversionMap;

		if (flowCopy.getStatuses() != null) {

			statusConversionMap = new HashMap<Integer, ImmutableStatus>(flowCopy.getStatuses().size());

			for (Status status : flowCopy.getStatuses()) {

				statusConversionMap.put(status.getStatusID(), status);
				status.setStatusID(null);
			}

		} else {

			statusConversionMap = null;
		}

		if (flowCopy.getSteps() != null) {

			for (Step step : flowCopy.getSteps()) {

				step.setStepID(null);

				if (step.getQueryDescriptors() != null) {

					for (QueryDescriptor queryDescriptor : step.getQueryDescriptors()) {

						queryDescriptor.setQueryID(null);

						if (queryDescriptor.getEvaluatorDescriptors() != null) {

							for (EvaluatorDescriptor evaluatorDescriptor : queryDescriptor.getEvaluatorDescriptors()) {

								evaluatorDescriptor.setEvaluatorID(null);
							}
						}
					}
				}
			}
		}

		if (!CollectionUtils.isEmpty(flowCopy.getFlowForms())) {

			for (FlowForm flowForm : flowCopy.getFlowForms()) {

				flowForm.setFlowFormID(null);
			}
		}

		TransactionHandler transactionHandler = null;

		try {
			transactionHandler = daoFactory.getFlowDAO().createTransaction();

			if (newFamily) {

				daoFactory.getFlowDAO().add(flowCopy, transactionHandler, ADD_NEW_FLOW_AND_FAMILY_RELATION_QUERY);

			} else {

				Integer version = getNextVersion(flow.getFlowFamily().getFlowFamilyID(), transactionHandler);

				if (version == null) {

					throw new RuntimeException("Flow family " + flow.getFlowFamily() + " not found in database.");
				}

				flowCopy.getFlowFamily().setVersionCount(version);
				flowCopy.setVersion(version);

				daoFactory.getFlowFamilyDAO().update(flowCopy.getFlowFamily(), transactionHandler, null);
				daoFactory.getFlowDAO().add(flowCopy, transactionHandler, ADD_NEW_FLOW_VERSION_RELATION_QUERY);
			}

			if (flow.getSteps() != null) {

				int stepIndex = 0;

				while (stepIndex < flowCopy.getSteps().size()) {

					Step step = flow.getSteps().get(stepIndex);

					if (step.getQueryDescriptors() != null) {

						int queryIndex = 0;

						while (queryIndex < step.getQueryDescriptors().size()) {

							QueryDescriptor queryDescriptor = step.getQueryDescriptors().get(queryIndex);

							queryHandler.copyQuery(queryDescriptor, flowCopy.getSteps().get(stepIndex).getQueryDescriptors().get(queryIndex), transactionHandler);

							if (queryDescriptor.getEvaluatorDescriptors() != null) {

								Query sourceQuery = queryHandler.getQuery(queryDescriptor, transactionHandler);
								Query copyQuery = queryHandler.getQuery(flowCopy.getSteps().get(stepIndex).getQueryDescriptors().get(queryIndex), transactionHandler);

								int evaluatorIndex = 0;

								while (evaluatorIndex < queryDescriptor.getEvaluatorDescriptors().size()) {

									EvaluatorDescriptor sourceEvaluatorDescriptor = queryDescriptor.getEvaluatorDescriptors().get(evaluatorIndex);
									EvaluatorDescriptor copyEvaluatorDescriptor = flowCopy.getSteps().get(stepIndex).getQueryDescriptors().get(queryIndex).getEvaluatorDescriptors().get(evaluatorIndex);

									if (sourceEvaluatorDescriptor.getTargetQueryIDs() != null) {

										copyEvaluatorDescriptor.getTargetQueryIDs().clear();

										for (Integer queryID : sourceEvaluatorDescriptor.getTargetQueryIDs()) {

											int nestedStepIndex = stepIndex;

											nestedStepLoop: while (nestedStepIndex < flow.getSteps().size()) {

												Step nestedStep = flow.getSteps().get(nestedStepIndex);

												if (nestedStep.getQueryDescriptors() != null) {

													int nestedQueryIndex = 0;

													while (nestedQueryIndex < nestedStep.getQueryDescriptors().size()) {

														if (nestedStep.getQueryDescriptors().get(nestedQueryIndex).getQueryID().equals(queryID)) {

															copyEvaluatorDescriptor.getTargetQueryIDs().add(flowCopy.getSteps().get(nestedStepIndex).getQueryDescriptors().get(nestedQueryIndex).getQueryID());

															break nestedStepLoop;
														}

														nestedQueryIndex++;
													}
												}

												nestedStepIndex++;
											}
										}
									}

									daoFactory.getEvaluatorDescriptorDAO().update(copyEvaluatorDescriptor, transactionHandler, null);

									evaluationHandler.copyEvaluator(sourceEvaluatorDescriptor, copyEvaluatorDescriptor, sourceQuery, copyQuery, statusConversionMap, transactionHandler);

									evaluatorIndex++;
								}
							}

							queryIndex++;
						}
					}

					stepIndex++;
				}
			}

			if (!CollectionUtils.isEmpty(flow.getFlowForms())) {

				for (int i = 0; i < flow.getFlowForms().size(); i++) {

					FlowForm flowForm = flow.getFlowForms().get(i);
					FlowForm flowFormCopy = flowCopy.getFlowForms().get(i);

					if (StringUtils.isEmpty(flowForm.getExternalURL())) {

						File file = new File(getFlowFormFilePath(flowForm));

						if (!file.exists()) {

							log.error("Unable to find form file for flow form " + flowForm + " at " + file);

						} else {

							File fileCopy = new File(getFlowFormFilePath(flowFormCopy));
							FileUtils.copyFile(file, fileCopy);
						}
					}
				}
			}

			try {
				// Commit
				transactionHandler.commit();

			} catch (SQLException e) {

				// Cleanup
				if (!CollectionUtils.isEmpty(flowCopy.getFlowForms())) {

					for (FlowForm flowForm : flowCopy.getFlowForms()) {

						deleteFlowFormFile(flowForm);
					}
				}

				throw e;
			}

		} finally {

			TransactionHandler.autoClose(transactionHandler);
		}

		eventHandler.sendEvent(Flow.class, new CRUDEvent<Flow>(CRUDAction.ADD, flowCopy), EventTarget.ALL);

		addFlowFamilyEvent(eventCopyFlowMessage + " " + flow.getVersion() + " \"" + flow.getName() + "\"", flowCopy, user);

		redirectToMethod(req, res, "/showflow/" + flowCopy.getFlowID());

		return null;
	}

	@WebPublic(toLowerCase = true)
	public ForegroundModuleResponse updateIcon(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws ModuleConfigurationException, SQLException, AccessDeniedException, IOException {

		Flow flow = flowCRUD.getRequestedBean(req, null, user, uriParser, GenericCRUD.UPDATE);

		if (flow == null) {

			return list(req, res, user, uriParser, new ValidationError("UpdateFailedFlowNotFound"));

		} else if (!AccessUtils.checkAccess(user, flow.getFlowType().getAdminAccessInterface())) {

			throw new AccessDeniedException("User does not have access to flow type " + flow.getFlowType());
		}

		ValidationError validationError = null;

		if (req.getMethod().equalsIgnoreCase("POST")) {

			MultipartRequest multipartRequest = null;

			try {
				multipartRequest = new MultipartRequest(this.ramThreshold * BinarySizes.KiloByte, this.maxRequestSize * BinarySizes.MegaByte, tempDir, req);

				if (multipartRequest.getParameter("clearicon") != null) {

					log.info("User " + user + " restoring default icon for flow " + flow);

					flow.setIcon(null);
					flow.setIconFileName(null);
					flow.setIconLastModified(TimeUtils.getCurrentTimestamp());

					this.daoFactory.getFlowDAO().update(flow);

					eventHandler.sendEvent(Flow.class, new CRUDEvent<Flow>(CRUDAction.UPDATE, flow), EventTarget.ALL);

					redirectToMethod(multipartRequest, res, "/showflow/" + flow.getFlowID());

					return null;

				} else if (multipartRequest.getFileCount() > 0 && !StringUtils.isEmpty(multipartRequest.getFile(0).getName())) {

					FileItem file = multipartRequest.getFile(0);

					String lowerCasefileName = file.getName().toLowerCase();

					if (!(lowerCasefileName.endsWith(".png") || lowerCasefileName.endsWith(".jpg") || lowerCasefileName.endsWith(".gif") || lowerCasefileName.endsWith(".bmp"))) {

						validationError = new ValidationError("InvalidIconFileFormat");

					} else {

						try {
							BufferedImage image = ImageUtils.getImage(file.get());

							if (image == null) {

								validationError = new ValidationError("UnableToParseIcon");

							} else {

								image = ImageUtils.cropAsSquare(image);

								String filename = FilenameUtils.getName(file.getName());

								if (image.getWidth() > maxFlowIconWidth || image.getHeight() > maxFlowIconHeight) {

									image = ImageUtils.scaleImage(image, maxFlowIconHeight, maxFlowIconWidth, Image.SCALE_SMOOTH, BufferedImage.TYPE_INT_ARGB);

									ByteArrayOutputStream iconStream = new ByteArrayOutputStream();

									ImageIO.write(image, "png", iconStream);

									flow.setIcon(new SerialBlob(iconStream.toByteArray()));
									flow.setIconFileName(FileUtils.replaceFileExtension(FilenameUtils.getName(filename), "png"));

								} else {

									flow.setIcon(new SerialBlob(file.get()));
									flow.setIconFileName(FilenameUtils.getName(filename));
								}

								flow.setIconLastModified(TimeUtils.getCurrentTimestamp());

								log.info("User " + user + " updating icon for flow " + flow);

								this.daoFactory.getFlowDAO().update(flow);

								eventHandler.sendEvent(Flow.class, new CRUDEvent<Flow>(CRUDAction.UPDATE, flow), EventTarget.ALL);

								addFlowFamilyEvent(eventUpdateIconMessage + " \"" + filename + "\"", flow, user);

								redirectToMethod(multipartRequest, res, "/showflow/" + flow.getFlowID());

								return null;
							}

						} catch (IOException e) {

							validationError = new ValidationError("UnableToParseIcon");
						}
					}

				} else {

					log.info("User " + user + " submitted update icon form for flow " + flow + " without making any changes.");

					redirectToMethod(multipartRequest, res, "/showflow/" + flow.getFlowID());

					return null;
				}

			} catch (FileUploadBase.SizeLimitExceededException e) {

				validationError = new RequestSizeLimitExceededValidationError(e.getActualSize(), maxRequestSize * BinarySizes.MegaByte);

			} catch (FileUploadException e) {

				validationError = new ValidationError("UnableToParseRequest");

			} finally {

				if (multipartRequest != null) {
					multipartRequest.deleteFiles();
				}
			}
		}

		log.info("User " + user + " requesting update icon form for flow " + flow);

		Document doc = createDocument(req, uriParser, user);

		Element updateFlowIconElement = doc.createElement("UpdateFlowIcon");
		doc.getDocumentElement().appendChild(updateFlowIconElement);

		updateFlowIconElement.appendChild(flow.toXML(doc));

		if (validationError != null) {

			updateFlowIconElement.appendChild(validationError.toXML(doc));
		}

		return new SimpleForegroundModuleResponse(doc);
	}

	@WebPublic(toLowerCase = true)
	public ForegroundModuleResponse sortFlow(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws ModuleConfigurationException, SQLException, AccessDeniedException, IOException {

		Flow flow = flowCRUD.getRequestedBean(req, null, user, uriParser, GenericCRUD.UPDATE);

		if (flow == null) {

			return list(req, res, user, uriParser, new ValidationError("FlowNotFound"));

		} else if (!AccessUtils.checkAccess(user, flow.getFlowType().getAdminAccessInterface())) {

			throw new AccessDeniedException("User does not have access to flow type " + flow.getFlowType());

		} else if (flow.getSteps() == null) {

			log.info("User " + user + " requested sort steps and queries form for flow " + flow + " which has no steps.");

			redirectToMethod(req, res, "/showflow/" + flow.getFlowID());

			return null;
		}

		ValidationError validationError = null;

		if (req.getMethod().equalsIgnoreCase("POST")) {

			validationError = parseSortPost(flow, req, res, user, uriParser);

			if (validationError == null) {

				return null;
			}
		}

		log.info("User " + user + " requesting sorting form for flow " + flow);

		Document doc = createDocument(req, uriParser, user);

		Element sortFlowElement = doc.createElement("SortFlow");
		doc.getDocumentElement().appendChild(sortFlowElement);

		sortFlowElement.appendChild(flow.toXML(doc));

		if (validationError != null) {

			sortFlowElement.appendChild(validationError.toXML(doc));
		}

		return new SimpleForegroundModuleResponse(doc);
	}

	@WebPublic(toLowerCase = true)
	public ForegroundModuleResponse sortStatuses(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws ModuleConfigurationException, SQLException, AccessDeniedException, IOException {

		Flow flow = flowCRUD.getRequestedBean(req, null, user, uriParser, GenericCRUD.UPDATE);

		if (flow == null) {

			return list(req, res, user, uriParser, new ValidationError("FlowNotFound"));

		} else if (!AccessUtils.checkAccess(user, flow.getFlowType().getAdminAccessInterface())) {

			throw new AccessDeniedException("User does not have access to flow type " + flow.getFlowType());

		} else if (flow.getStatuses() == null) {

			log.info("User " + user + " requested sort statuses form for flow " + flow + " which has no statuses.");

			redirectToMethod(req, res, "/showflow/" + flow.getFlowID());

			return null;
		}

		if (req.getMethod().equalsIgnoreCase("POST")) {

			for (Status status : flow.getStatuses()) {

				String sortIndex = req.getParameter("sortorder_" + status.getStatusID());

				if (NumberUtils.isInt(sortIndex)) {

					status.setSortIndex(NumberUtils.toInt(sortIndex));
				}

				status.setFlow(flow);
			}

			daoFactory.getStatusDAO().update(flow.getStatuses(), null);

			addFlowFamilyEvent(eventStatusSortMessage, flow, user);

			getEventHandler().sendEvent(Status.class, new CRUDEvent<Status>(Status.class, CRUDAction.UPDATE, flow.getStatuses()), EventTarget.ALL);

			redirectToMethod(req, res, "/showflow/" + flow.getFlowID() + "#statuses");

			return null;
		}

		log.info("User " + user + " requesting sort status form for flow " + flow);

		Document doc = createDocument(req, uriParser, user);

		Element sortStatusesElement = doc.createElement("SortStatuses");
		doc.getDocumentElement().appendChild(sortStatusesElement);

		sortStatusesElement.appendChild(flow.toXML(doc));

		return new SimpleForegroundModuleResponse(doc);
	}

	@WebPublic(toLowerCase = true)
	public ForegroundModuleResponse updateNotifications(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Exception {

		Flow flow = flowCRUD.getRequestedBean(req, null, user, uriParser, GenericCRUD.SHOW);

		if (flow == null) {

			return list(req, res, user, uriParser, new ValidationError("FlowNotFound"));

		} else if (!AccessUtils.checkAccess(user, flow.getFlowType().getAdminAccessInterface())) {

			throw new AccessDeniedException("User does not have access to flow type " + flow.getFlowType());

		} else if (notificationHandler == null) {

			log.info("User " + user + " requested notifications settings for flow " + flow + " but no flow notification handler is available");

			redirectToMethod(req, res, "/showflow/" + flow.getFlowID());

			return null;
		}

		ValidationException validationException = null;

		if (req.getMethod().equalsIgnoreCase("POST")) {

			try {
				notificationHandler.updateSettings(flow, req, user, uriParser);

				log.info("User " + user + " updated notification settings for flow " + flow);

				addFlowFamilyEvent(eventUpdateNotificationsMessage, flow, user);

				redirectToMethod(req, res, "/showflow/" + flow.getFlowID() + "#notifications");

				return null;

			} catch (ValidationException e) {

				validationException = e;
			}

		}

		log.info("User " + user + " notification settings for flow " + flow);

		Document doc = createDocument(req, uriParser, user);

		Element notificationSettingsElement = doc.createElement("UpdateNotifications");
		doc.getDocumentElement().appendChild(notificationSettingsElement);

		notificationSettingsElement.appendChild(flow.toXML(doc));

		ViewFragment viewFragment = notificationHandler.getUpdateSettingsView(flow, req, user, uriParser, validationException);

		notificationSettingsElement.appendChild(viewFragment.toXML(doc));

		return ViewFragmentUtils.appendLinksAndScripts(new SimpleForegroundModuleResponse(doc), viewFragment);
	}

	@WebPublic(toLowerCase = true)
	public ForegroundModuleResponse generateXSD(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws ModuleConfigurationException, SQLException, AccessDeniedException, IOException, SAXException, ParserConfigurationException, TransformerFactoryConfigurationError, TransformerException, XPathExpressionException, QueryProviderNotFoundException, QueryNotFoundInQueryProviderException, QueryProviderErrorException {

		Flow flow = flowCRUD.getRequestedBean(req, null, user, uriParser, GenericCRUD.SHOW);

		if (flow == null) {

			return list(req, res, user, uriParser, new ValidationError("FlowNotFound"));

		} else if (!AccessUtils.checkAccess(user, flow.getFlowType().getAdminAccessInterface())) {

			throw new AccessDeniedException("User does not have access to flow type " + flow.getFlowType());
		}

		log.info("User " + user + " requesting XSD for flow " + flow);

		Document doc = XMLUtils.parseXML(FlowAdminModule.class.getResourceAsStream("xsd/base-schema.xsd"), false, false);

		XPath xPath = XPathFactory.newInstance().newXPath();

		Element flowSequenceElement = (Element) xPath.evaluate("complexType[@name='Flow']/sequence", doc.getDocumentElement(), XPathConstants.NODE);

		setFixedXSDAttribute("element[@name='FamilyID']", flow.getFlowFamily().getFlowFamilyID(), flowSequenceElement, xPath);
		setFixedXSDAttribute("element[@name='FlowID']", flow.getFlowID(), flowSequenceElement, xPath);
		setFixedXSDAttribute("element[@name='Version']", flow.getVersion(), flowSequenceElement, xPath);

		if (flow.getStatuses() != null) {

			Element statusIDElement = (Element) xPath.evaluate("complexType[@name='Status']/sequence/element[@name='ID']", doc.getDocumentElement(), XPathConstants.NODE);

			Element simpleTypeElement = doc.createElementNS("http://www.w3.org/2001/XMLSchema", "xs:simpleType");
			statusIDElement.appendChild(simpleTypeElement);

			Element restrictionElement = doc.createElementNS("http://www.w3.org/2001/XMLSchema", "xs:restriction");
			simpleTypeElement.appendChild(restrictionElement);

			restrictionElement.setAttribute("base", "xs:positiveInteger");

			for (Status status : flow.getStatuses()) {

				Comment comment = doc.createComment("ID for status: " + status.getName());
				restrictionElement.appendChild(comment);

				Element enumerationElement = doc.createElementNS("http://www.w3.org/2001/XMLSchema", "xs:enumeration");
				restrictionElement.appendChild(enumerationElement);

				enumerationElement.setAttribute("value", status.getStatusID().toString());
			}
		}

		if (flow.getSteps() != null) {

			Element valuesSequenceElement = (Element) xPath.evaluate("complexType[@name='Values']/sequence", doc.getDocumentElement(), XPathConstants.NODE);

			for (Step step : flow.getSteps()) {

				if (step.getQueryDescriptors() != null) {

					for (QueryDescriptor queryDescriptor : step.getQueryDescriptors()) {

						if (!queryDescriptor.isExported()) {

							continue;
						}

						Query query = queryHandler.getQuery(queryDescriptor);

						Element queryElement = doc.createElementNS("http://www.w3.org/2001/XMLSchema", "xs:element");
						queryElement.setAttribute("name", queryDescriptor.getXSDElementName());
						queryElement.setAttribute("type", query.getXSDTypeName());
						queryElement.setAttribute("minOccurs", "0");
						queryElement.setAttribute("maxOccurs", "1");
						valuesSequenceElement.appendChild(queryElement);

						query.toXSD(doc);
					}
				}
			}
		}

		res.setHeader("Content-Disposition", "attachment; filename=\"schema-" + flow.getFlowID() + ".xsd\"");
		res.setHeader("Cache-Control", "no-store, no-cache, must-revalidate, proxy-revalidate");
		res.setContentType("text/xml");

		XMLUtils.writeXML(doc, res.getOutputStream(), true, systemInterface.getEncoding());

		return null;
	}

	private void setFixedXSDAttribute(String targetElementPath, Object value, Element sourceElement, XPath xPath) throws XPathExpressionException {

		Element targetElement = (Element) xPath.evaluate(targetElementPath, sourceElement, XPathConstants.NODE);

		targetElement.setAttribute("fixed", value.toString());
	}

	private ValidationError parseSortPost(Flow flow, HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws SQLException, IOException {

		List<QueryDescriptor> queryDescriptors = new ArrayList<QueryDescriptor>();

		//Set sort index for each step
		for (Step step : flow.getSteps()) {

			Integer sortIndex = NumberUtils.toInt(req.getParameter("step" + step.getStepID()));

			if (sortIndex == null) {

				return new NoStepSortindexValidationError(step);
			}

			step.setSortIndex(sortIndex);

			if (step.getQueryDescriptors() != null) {

				for (QueryDescriptor queryDescriptor : step.getQueryDescriptors()) {

					sortIndex = NumberUtils.toInt(req.getParameter("query" + queryDescriptor.getQueryID()));

					if (sortIndex == null) {

						return new NoQueryDescriptorSortindexValidationError(queryDescriptor);
					}

					queryDescriptor.setSortIndex(sortIndex);

					queryDescriptors.add(queryDescriptor);
				}

				step.getQueryDescriptors().clear();

			} else {

				step.setQueryDescriptors(new ArrayList<QueryDescriptor>());
			}
		}

		Collections.sort(flow.getSteps(), STEP_COMPARATOR);
		Collections.sort(queryDescriptors, QUERY_DESCRIPTOR_COMPARATOR);

		List<QueryDescriptor> queryDescriptorsCopy = new ArrayList<QueryDescriptor>(queryDescriptors);

		Iterator<QueryDescriptor> queryIterator = queryDescriptors.iterator();

		outer: while (queryIterator.hasNext()) {

			QueryDescriptor queryDescriptor = queryIterator.next();

			int stepIndex = flow.getSteps().size() - 1;

			while (stepIndex >= 0) {

				Step step = flow.getSteps().get(stepIndex);

				if (queryDescriptor.getSortIndex() > step.getSortIndex()) {

					step.getQueryDescriptors().add(queryDescriptor);
					queryIterator.remove();
					continue outer;
				}

				stepIndex--;
			}
		}

		if (!queryDescriptors.isEmpty()) {

			return new ValidationError("UnableToFindStepsForAllQueries");
		}

		int stepSortIndex = 0;

		List<Integer> addedQueries = new ArrayList<Integer>();

		for (Step step : flow.getSteps()) {

			step.setSortIndex(stepSortIndex);
			step.setFlow(flow);

			stepSortIndex++;

			int querySortIndex = 0;

			for (QueryDescriptor queryDescriptor : step.getQueryDescriptors()) {

				queryDescriptor.setSortIndex(querySortIndex);

				if (queryDescriptor.getEvaluatorDescriptors() != null) {

					for (EvaluatorDescriptor evaluatorDescriptor : queryDescriptor.getEvaluatorDescriptors()) {

						if (evaluatorDescriptor.getTargetQueryIDs() != null) {

							for (Integer queryID : evaluatorDescriptor.getTargetQueryIDs()) {

								if (addedQueries.contains(queryID)) {

									return new ValidationError("InvalidQuerySortIndex");

								}

							}

						}

					}

				}

				addedQueries.add(queryDescriptor.getQueryID());

				querySortIndex++;
			}
		}

		log.info("User " + user + " updating sorting of flow " + flow);

		HighLevelQuery<Step> query = new HighLevelQuery<Step>();

		query.addRelations(Step.QUERY_DESCRIPTORS_RELATION, QueryDescriptor.EVALUATOR_DESCRIPTORS_RELATION);

		daoFactory.getStepDAO().update(flow.getSteps(), query);

		eventHandler.sendEvent(Step.class, new CRUDEvent<Step>(Step.class, CRUDAction.UPDATE, flow.getSteps()), EventTarget.ALL);

		if (!queryDescriptorsCopy.isEmpty()) {

			eventHandler.sendEvent(QueryDescriptor.class, new CRUDEvent<QueryDescriptor>(QueryDescriptor.class, CRUDAction.UPDATE, queryDescriptorsCopy), EventTarget.ALL);
		}

		addFlowFamilyEvent(eventSortFlowMessage, flow, user);

		redirectToMethod(req, res, "/showflow/" + flow.getFlowID() + "#steps");

		return null;
	}

	@WebPublic(toLowerCase = true)
	public ForegroundModuleResponse addStep(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Exception, Throwable {

		return stepCRUD.add(req, res, user, uriParser);
	}

	@WebPublic(toLowerCase = true)
	public ForegroundModuleResponse updateStep(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Exception, Throwable {

		return stepCRUD.update(req, res, user, uriParser);
	}

	@WebPublic(toLowerCase = true)
	public ForegroundModuleResponse deleteStep(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Exception, Throwable {

		return stepCRUD.delete(req, res, user, uriParser);
	}

	@WebPublic(toLowerCase = true)
	public ForegroundModuleResponse addStatus(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Exception, Throwable {

		return statusCRUD.add(req, res, user, uriParser);
	}

	@WebPublic(toLowerCase = true)
	public ForegroundModuleResponse updateStatus(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Exception, Throwable {

		return statusCRUD.update(req, res, user, uriParser);
	}

	@WebPublic(toLowerCase = true)
	public ForegroundModuleResponse deleteStatus(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Exception, Throwable {

		return statusCRUD.delete(req, res, user, uriParser);
	}

	@WebPublic(toLowerCase = true)
	public ForegroundModuleResponse addFlowForm(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Exception, Throwable {
		
		Flow flow = getRequestedFlow(req, user, uriParser);
		
		if (flow == null) {
			
			throw new URINotFoundException(uriParser);
		}
		
		flowFormCRUD.checkFlowTypeAccess(user, flow.getFlowType());
		
		req.setAttribute("flow", flow);
		
		return flowFormCRUD.add(req, res, user, uriParser);
	}

	@WebPublic(toLowerCase = true)
	public ForegroundModuleResponse updateFlowForm(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Exception, Throwable {

		return flowFormCRUD.update(req, res, user, uriParser);
	}

	@WebPublic(toLowerCase = true)
	public ForegroundModuleResponse deleteFlowForm(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Exception, Throwable {

		return flowFormCRUD.delete(req, res, user, uriParser);
	}

	@WebPublic(alias = "standardstatuses")
	public ForegroundModuleResponse listStandardStatuses(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Exception, Throwable {

		return standardStatusCRUD.list(req, res, user, uriParser, null);
	}

	@WebPublic(toLowerCase = true)
	public ForegroundModuleResponse addStandardStatus(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Exception, Throwable {

		return standardStatusCRUD.add(req, res, user, uriParser);
	}

	@WebPublic(toLowerCase = true)
	public ForegroundModuleResponse updateStandardStatus(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Exception, Throwable {

		return standardStatusCRUD.update(req, res, user, uriParser);
	}

	@WebPublic(toLowerCase = true)
	public ForegroundModuleResponse deleteStandardStatus(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Exception, Throwable {

		return standardStatusCRUD.delete(req, res, user, uriParser);
	}

	@WebPublic(toLowerCase = true)
	public ForegroundModuleResponse addQuery(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Exception, Throwable {

		return queryDescriptorCRUD.add(req, res, user, uriParser);
	}

	@WebPublic(toLowerCase = true)
	public ForegroundModuleResponse updateQuery(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Exception, Throwable {

		return queryDescriptorCRUD.update(req, res, user, uriParser);
	}

	@WebPublic(toLowerCase = true)
	public ForegroundModuleResponse deleteQuery(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Exception, Throwable {

		return queryDescriptorCRUD.delete(req, res, user, uriParser);
	}

	@WebPublic(toLowerCase = true)
	public ForegroundModuleResponse addEvaluator(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Exception, Throwable {

		return evaluatorDescriptorCRUD.add(req, res, user, uriParser);
	}

	@WebPublic(toLowerCase = true)
	public ForegroundModuleResponse updateEvaluator(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Exception, Throwable {

		return evaluatorDescriptorCRUD.update(req, res, user, uriParser);
	}

	@WebPublic(toLowerCase = true)
	public ForegroundModuleResponse deleteEvaluator(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Exception, Throwable {

		return evaluatorDescriptorCRUD.delete(req, res, user, uriParser);
	}

	@WebPublic(alias = "overview")
	public ForegroundModuleResponse showFlowOverview(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws ModuleConfigurationException, SQLException, AccessDeniedException, IOException {

		Integer flowID;
		Flow flow;

		if (uriParser.size() == 3 && (flowID = uriParser.getInt(2)) != null && (flow = flowCRUD.getBean(flowID, FlowCRUD.SHOW)) != null) {

			flowCRUD.checkAccess(user, flow);

			if (skipOverview(flow, req, res, "testflow")) {

				return null;
			}

			Document doc = this.createDocument(req, uriParser, user);

			Element showFlowOverviewElement = doc.createElement("ShowFlowOverview");

			doc.getDocumentElement().appendChild(showFlowOverviewElement);

			flow.setHasTextTags(TextTagReplacer.hasTextTags(flow));
			flow.getFlowFamily().setHasTextTags(TextTagReplacer.hasTextTags(flow.getFlowFamily()));
			flow.setHasFileURLs(FCKUtils.hasAbsoluteFileUrls(flow));
			flow.setHasRelativeURLs(URLRewriter.hasAbsoluteLinkUrls(flow));

			showFlowOverviewElement.appendChild(flow.toXML(doc, this.getCurrentSiteProfile(req, user, uriParser, flow.getFlowFamily()), getAbsoluteFileURL(uriParser, flow), req));

			if (!flow.isInternal()) {
				XMLUtils.appendNewElement(doc, showFlowOverviewElement, "openExternalFlowsInNewWindow", openExternalFlowsInNewWindow);
			}
			
			List<ExtensionView> extensionViews = null;
			
			if (!CollectionUtils.isEmpty(flowBrowserExtensionViewProviders)) {

				extensionViews = new ArrayList<ExtensionView>(flowBrowserExtensionViewProviders.size());

				for (FlowBrowserExtensionViewProvider extensionProvider : flowBrowserExtensionViewProviders) {

					try {
						ExtensionView extension = extensionProvider.getFlowOverviewExtensionView(flow, req, user, uriParser);

						if (extension != null) {

							extensionViews.add(extension);
						}

					} catch (Exception e) {

						log.error("Error getting extension view from provider " + extensionProvider, e);
					}
				}
				
				XMLUtils.append(doc, showFlowOverviewElement, "ExtensionViews", extensionViews);
			}
			
			SimpleForegroundModuleResponse response = new SimpleForegroundModuleResponse(doc, flow.getName(), this.getDefaultBreadcrumb());
			
			if (!CollectionUtils.isEmpty(extensionViews)) {
				
				for (ExtensionView extensionView : extensionViews) {
					
					if (!CollectionUtils.isEmpty(extensionView.getViewFragment().getLinks())) {
						response.addLinks(extensionView.getViewFragment().getLinks());
					}
					
					if (!CollectionUtils.isEmpty(extensionView.getViewFragment().getScripts())) {
						
						response.addScripts(extensionView.getViewFragment().getScripts());
					}
				}
			}
			
			return response;
		}

		return list(req, res, user, uriParser, FLOW_NOT_FOUND_VALIDATION_ERROR);
	}

	@WebPublic(toLowerCase = true)
	public ForegroundModuleResponse testFlow(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Exception, Throwable {

		Integer flowID = null;
		MutableFlowInstanceManager instanceManager;

		try {
			if (uriParser.size() == 3 && (flowID = NumberUtils.toInt(uriParser.get(2))) != null && flowCacheMap.get(flowID) != null) {

				//Create new instance or get instance from session
				instanceManager = getUnsavedMutableFlowInstanceManager(flowID, updateAccessController, req.getSession(true), user, user, uriParser, req, true, false, false, false, DEFAULT_REQUEST_METADATA);

				if (instanceManager == null) {

					log.info("User " + user + " requested non-existing flow with ID " + flowID + ", listing flows");
					return list(req, res, user, uriParser, FLOW_NOT_FOUND_VALIDATION_ERROR);
				}

			} else {

				log.info("User " + user + " requested invalid URL, listing flows");
				return list(req, res, user, uriParser, INVALID_LINK_VALIDATION_ERROR);
			}

		} catch (FlowNoLongerAvailableException e) {

			log.info("User " + user + " requested flow " + e.getFlow() + " which is no longer available.");
			return list(req, res, user, uriParser, FLOW_NO_LONGER_AVAILABLE_VALIDATION_ERROR);

		} catch (FlowNotAvailiableInRequestedFormat e) {

			log.info("User " + user + " requested flow " + flowID + " which is not availiable in the requested format.");
			return list(req, res, user, uriParser, FLOW_NOT_AVAILIABLE_IN_REQUESTED_FORMAT_VALIDATION_ERROR);

		} catch (FlowEngineException e) {

			log.error("Unable to get flow instance manager for flowID " + flowID + " requested by user " + user, e);
			return list(req, res, user, uriParser, ERROR_GETTING_FLOW_INSTANCE_MANAGER_VALIDATION_ERROR);
		}

		try {
			return processFlowRequest(instanceManager, this, updateAccessController, req, res, user, user, uriParser, false, DEFAULT_REQUEST_METADATA);

		} catch (FlowInstanceManagerClosedException e) {

			log.info("User " + user + " requested flow instance manager for flow instance " + e.getFlowInstance() + " which has already been closed. Removing flow instance manager from session.");

			removeMutableFlowInstanceManagerFromSession(instanceManager, req.getSession(false));

			redirectToMethod(req, res, "/testflow/" + flowID);

			return null;
		}
	}

	@WebPublic(alias = "flowtypes")
	public ForegroundModuleResponse listFlowTypes(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Exception, Throwable {

		return flowTypeCRUD.list(req, res, user, uriParser, null);
	}

	@WebPublic(alias = "flowtype")
	public ForegroundModuleResponse showFlowType(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Exception, Throwable {

		return flowTypeCRUD.show(req, res, user, uriParser, null);
	}

	@WebPublic(toLowerCase = true)
	public ForegroundModuleResponse addFlowType(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Exception, Throwable {

		return flowTypeCRUD.add(req, res, user, uriParser);
	}

	@WebPublic(toLowerCase = true)
	public ForegroundModuleResponse updateFlowType(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Exception, Throwable {

		return flowTypeCRUD.update(req, res, user, uriParser);
	}

	@WebPublic(toLowerCase = true)
	public ForegroundModuleResponse deleteFlowType(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Exception, Throwable {

		return flowTypeCRUD.delete(req, res, user, uriParser);
	}

	@WebPublic(toLowerCase = true)
	public ForegroundModuleResponse addCategory(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Exception, Throwable {

		FlowType flowType = flowTypeCRUD.getRequestedBean(req, res, user, uriParser, null);

		if (flowType == null) {

			return flowTypeCRUD.list(req, res, user, uriParser, Collections.singletonList(new ValidationError("AddCategoryFailedFlowTypeNotFound")));

		}

		req.setAttribute("flowType", flowType);

		return categoryCRUD.add(req, res, user, uriParser);
	}

	@WebPublic(toLowerCase = true)
	public ForegroundModuleResponse updateCategory(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Exception, Throwable {

		return categoryCRUD.update(req, res, user, uriParser);
	}

	@WebPublic(toLowerCase = true)
	public ForegroundModuleResponse deleteCategory(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Exception, Throwable {

		return categoryCRUD.delete(req, res, user, uriParser);
	}

	@WebPublic(alias = "updatemanagers")
	public ForegroundModuleResponse updateFlowFamilyManagers(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Exception, Throwable {

		return flowFamilyCRUD.update(req, res, user, uriParser);
	}

	@WebPublic(alias = "mquery")
	public ForegroundModuleResponse processMutableQueryRequest(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws ModuleConfigurationException, SQLException, AccessDeniedException, IOException, FlowDefaultStatusNotFound, EvaluationException, URINotFoundException, QueryRequestException, QueryProviderException, EvaluationProviderException, InvalidFlowInstanceStepException, MissingQueryInstanceDescriptor, DuplicateFlowInstanceManagerIDException, UnableToResetQueryInstanceException {

		return processMutableQueryRequest(req, res, user, user, uriParser, updateAccessController, false, false, false, DEFAULT_REQUEST_METADATA);
	}

	@SuppressWarnings("unchecked")
	@Override
	public synchronized void processEvent(CRUDEvent<?> event, EventSource source) {

		try {
			log.debug("Received crud event regarding " + event.getAction() + " of " + event.getBeans().size() + " beans with " + event.getBeanClass());

			//Increment flow instance count for the given flow if the event is as an add or delete of a flow instance
			if (FlowInstance.class.isAssignableFrom(event.getBeanClass())) {

				for (FlowInstance flowInstance : (List<FlowInstance>) event.getBeans()) {

					Flow flow;

					//Check if the given flow is found in the cache else reload the whole cache
					if (flowInstance.getFlow() == null || (flow = flowCacheMap.get(flowInstance.getFlow().getFlowID())) == null) {

						cacheFlows();
						return;

					} else if (event.getAction() == CRUDAction.ADD) {

						flow.setFlowInstanceCount(flow.getFlowInstanceCount() + 1);

						if (flowInstance.getFirstSubmitted() != null) {

							flow.setFlowSubmittedInstanceCount(flow.getFlowSubmittedInstanceCount() + 1);
						}

						Status status = getCachedStatus(flow, flowInstance.getStatus());

						if (status != null) {

							status.setFlowInstanceCount(status.getFlowInstanceCount() + 1);

							if (flowInstance.getFirstSubmitted() != null) {

								status.setFlowSubmittedInstanceCount(status.getFlowSubmittedInstanceCount() + 1);
							}

							continue;

						} else {

							cacheFlows();
							return;
						}

					} else if (event.getAction() == CRUDAction.DELETE) {

						flow.setFlowInstanceCount(flow.getFlowInstanceCount() - 1);

						if (flowInstance.getFirstSubmitted() != null) {

							flow.setFlowSubmittedInstanceCount(flow.getFlowSubmittedInstanceCount() - 1);
						}

						Status status = getCachedStatus(flow, flowInstance.getStatus());

						if (status != null) {

							status.setFlowInstanceCount(status.getFlowInstanceCount() - 1);

							if (flowInstance.getFirstSubmitted() != null) {

								status.setFlowSubmittedInstanceCount(status.getFlowSubmittedInstanceCount() - 1);
							}

							continue;

						} else {

							cacheFlows();
							return;
						}

					}

					//Update operation, reload flow submitted instance count, reload flow instance count for each status from DB
					TransactionHandler transactionHandler = null;

					try {
						transactionHandler = new TransactionHandler(dataSource);

						flow.setFlowSubmittedInstanceCount(getFlowSubmittedInstanceCount(flow, transactionHandler));

						if (flow.getStatuses() != null) {

							for (Status status : flow.getStatuses()) {

								status.setFlowInstanceCount(getFlowInstanceCount(status, transactionHandler));
								status.setFlowSubmittedInstanceCount(getFlowSubmittedInstanceCount(status, transactionHandler));
							}
						}

					} finally {

						TransactionHandler.autoClose(transactionHandler);
					}
				}

				return;

			} else if (FlowType.class.isAssignableFrom(event.getBeanClass()) || Category.class.isAssignableFrom(event.getBeanClass())) {

				cacheFlowTypes();

			} else if (Flow.class.isAssignableFrom(event.getBeanClass()) && (event.getAction() != CRUDAction.ADD)) {

				for (Flow flow : (List<Flow>) event.getBeans()) {

					closeInstanceManagers(flow);
				}

				if (event.getAction() == CRUDAction.DELETE) {

					for (Flow flow : (List<Flow>) event.getBeans()) {

						if (!CollectionUtils.isEmpty(flow.getFlowForms())) {

							for (FlowForm flowForm : flow.getFlowForms()) {

								deleteFlowFormFile(flowForm);
							}
						}
					}
				}

			} else if (Step.class.isAssignableFrom(event.getBeanClass())) {

				for (Step step : (List<Step>) event.getBeans()) {

					closeInstanceManagers(step.getFlow());
				}

			} else if (QueryDescriptor.class.isAssignableFrom(event.getBeanClass())) {

				for (QueryDescriptor queryDescriptor : (List<QueryDescriptor>) event.getBeans()) {

					if (queryDescriptor.getStep() == null || queryDescriptor.getStep().getFlow() == null) {

						log.error("Received CRUD event regarding query descriptor " + queryDescriptor + " without a flow set.");

						continue;
					}

					closeInstanceManagers(queryDescriptor.getStep().getFlow());
				}

			} else if (EvaluatorDescriptor.class.isAssignableFrom(event.getBeanClass())) {

				for (EvaluatorDescriptor evaluatorDescriptor : (List<EvaluatorDescriptor>) event.getBeans()) {

					closeInstanceManagers(evaluatorDescriptor.getQueryDescriptor().getStep().getFlow());
				}

			} else if (FlowForm.class.isAssignableFrom(event.getBeanClass())) {

				cacheFlows();

				//TODO only re-cache affected flows
				//				for (FlowForm flowForm : (List<FlowForm>) event.getBeans()) {
				//
				//					Integer flowID = flowForm.getFlow().getFlowID();
				//
				//				}
			}

			cacheFlows();

		} catch (SQLException e) {
			log.error("Error reloading cache", e);
		}
	}

	private Status getCachedStatus(Flow flow, Status status) {

		if (status == null || flow.getStatuses() == null) {

			return null;
		}

		for (Status cachedStatus : flow.getStatuses()) {

			if (cachedStatus.equals(status)) {

				return cachedStatus;
			}
		}

		return null;
	}
	
	@WebPublic(toLowerCase = true)
	public ForegroundModuleResponse reCacheFlow(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws SQLException, URINotFoundException, IOException {
		
		Integer flowID;
		Flow flow;
		
		if (user != null && user.isAdmin() && uriParser.size() == 3 && (flowID = uriParser.getInt(2)) != null && (flow = getFlow(flowID)) != null) {
			
			systemInterface.getEventHandler().sendEvent(Flow.class, new CRUDEvent<Flow>(CRUDAction.UPDATE, flow), EventTarget.ALL);
			
			redirectToMethod(req, res, "/showflow/" + flow.getFlowID());
			return null;
		}
		
		throw new URINotFoundException(uriParser);
	}

	private void closeInstanceManagers(Flow flow) {

		int closedCount = FlowInstanceManagerRegistery.getInstance().closeInstances(flow, queryHandler);

		if (closedCount > 0) {
			log.info("Closed " + closedCount + " flow instance managers handling instances of flow " + flow);
		}
	}

	@Override
	protected Flow getBareFlow(Integer flowID) throws SQLException {

		return flowCacheMap.get(flowID);
	}

	@Override
	public String getTitlePrefix() {

		return this.moduleDescriptor.getName();
	}

	public Flow getCachedFlow(Integer flowID) {

		return flowCacheMap.get(flowID);
	}

	@Override
	public int getRamThreshold() {

		return ramThreshold;
	}

	@Override
	public long getMaxRequestSize() {

		return maxRequestSize;
	}

	public Collection<FlowType> getCachedFlowTypes() {

		return this.flowTypeCacheMap.values();
	}

	public FlowType getCachedFlowType(Integer flowTypeID) {

		return flowTypeCacheMap.get(flowTypeID);
	}

	public EventHandler getEventHandler() {

		return eventHandler;
	}

	public Flow getRequestedFlow(HttpServletRequest req, User user, URIParser uriParser) throws AccessDeniedException, SQLException {

		return flowCRUD.getRequestedBean(req, null, user, uriParser, GenericCRUD.SHOW);
	}

	public QueryDescriptor getQueryDescriptor(int queryID) throws AccessDeniedException, SQLException {

		return queryDescriptorCRUD.getBean(queryID);
	}

	public EvaluatorDescriptor getEvaluatorDescriptor(int evaluatorID) throws AccessDeniedException, SQLException {

		return evaluatorDescriptorCRUD.getBean(evaluatorID);
	}

	public String getFlowQueryRedirectURL(HttpServletRequest req, int flowID) {

		return req.getContextPath() + this.getFullAlias() + "/showflow/" + flowID + "#steps";
	}

	public void checkFlowStructureManipulationAccess(User user, Flow flow) throws AccessDeniedException, SQLException {

		if (!flow.isInternal()) {

			throw new AccessDeniedException("Requested flow is external and cannot be structure manipulated");

		} else if (!AccessUtils.checkAccess(user, this) && !AccessUtils.checkAccess(user, flow.getFlowType().getAdminAccessInterface())) {

			throw new AccessDeniedException("User does not have access to flow type " + flow.getFlowType());

		} else if (flow.isPublished() && flow.isEnabled()) {

			throw new AccessDeniedException("Changes to queries in flow " + flow + " is not allowed since the flow is published");

		} else if ((flow.getFlowInstanceCount() != null && flow.getFlowInstanceCount() > 0) || (flow.getFlowInstanceCount() == null && getFlowInstanceCount(flow) > 0)) {

			throw new AccessDeniedException("Changes to queries in flow " + flow + " is not allowed since the flow has one or more flow instances connected to it.");
		}
	}

	public DataSource getDataSource() {

		return dataSource;
	}

	@Override
	public String getAbsoluteFileURL(URIParser uriParser, Object bean) {

		if (ckConnectorModuleAlias != null) {

			return uriParser.getContextPath() + ckConnectorModuleAlias;
		}

		return null;
	}

	@Override
	public EvaluationHandler getEvaluationHandler() {

		return evaluationHandler;
	}

	public QueryDescriptor getRequestedQueryDescriptor(HttpServletRequest req, User user, URIParser uriParser) throws AccessDeniedException, SQLException {

		return queryDescriptorCRUD.getRequestedBean(req, null, user, uriParser, GenericCRUD.SHOW);
	}

	public ImmutableFlow getFlow(int flowID) {

		return flowCacheMap.get(flowID);
	}

	public FlowFamily getFlowFamily(int flowFamilyID) {

		return flowFamilyCacheMap.get(flowFamilyID);
	}

	public List<Flow> getFlowVersions(FlowFamily flowFamily) {

		List<Flow> flows = new ArrayList<Flow>(flowFamily.getVersionCount());

		for (Flow flow : flowCacheMap.values()) {

			if (flow.getFlowFamily().getFlowFamilyID().equals(flowFamily.getFlowFamilyID())) {

				flows.add(flow);
			}
		}

		if (!flows.isEmpty()) {

			Collections.sort(flows, FLOW_VERSION_COMPARATOR);
		}

		return flows;
	}

	public List<Flow> getFlows(int flowTypeID) {

		List<Flow> flows = new ArrayList<Flow>();

		for (Flow flow : flowCacheMap.values()) {

			if (flow.getFlowType().getFlowTypeID() == flowTypeID) {

				flows.add(flow);
			}
		}

		return flows;
	}

	public List<FlowFamily> getFlowFamilies(int flowTypeID) {

		HashSet<FlowFamily> flowFamilies = new HashSet<FlowFamily>(this.flowFamilyCacheMap.size());

		List<Flow> flows = getFlows(flowTypeID);

		for (Flow flow : flows) {

			flowFamilies.add(flow.getFlowFamily());
		}

		return new ArrayList<FlowFamily>(flowFamilies);
	}

	public Flow getLatestFlowVersion(FlowFamily flowFamily) {

		List<Flow> flows = getFlowVersions(flowFamily);

		return flows.get(flows.size() - 1);
	}

	public boolean hasFlows(FlowFamily flowFamily, TransactionHandler transactionHandler) throws SQLException {

		HighLevelQuery<Flow> query = new HighLevelQuery<Flow>();

		query.addParameter(flowFlowFamilyParamFactory.getParameter(flowFamily));

		return daoFactory.getFlowDAO().getBoolean(query, transactionHandler);
	}

	public List<FlowAction> getFlowActions(boolean getOptionalActions) throws SQLException {

		HighLevelQuery<FlowAction> query = new HighLevelQuery<FlowAction>();

		if (!getOptionalActions) {

			query.addParameter(flowActionRequiredParamFactory.getParameter(true));

		}

		return daoFactory.getFlowActionDAO().getAll(query);
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

	public Collection<Integer> getPublisherGroupIDs() {

		return publisherGroupIDs;
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
	protected FlowInstanceEvent save(MutableFlowInstanceManager instanceManager, User user, User poster, HttpServletRequest req, String actionID, EventType eventType, Map<String,String> eventAttributes) throws FlowInstanceManagerClosedException, UnableToSaveQueryInstanceException, SQLException, FlowDefaultStatusNotFound {

		return null;
	}

	@Override
	protected Flow getFlow(Integer flowID) throws SQLException {

		return SerializationUtils.cloneSerializable(getCachedFlow(flowID));
	}

	public Collection<Flow> getCachedFlows() {

		return this.flowCacheMap.values();
	}

	public Collection<FlowFamily> getCachedFlowFamilies() {

		return this.flowFamilyCacheMap.values();
	}

	@Override
	public Breadcrumb getFlowBreadcrumb(ImmutableFlowInstance flowInstance) {

		return new Breadcrumb(this, flowInstance.getFlow().getName(), "/testflow/" + flowInstance.getFlow().getFlowID());
	}

	public FlowTypeCRUD getFlowTypeCRUD() {

		return flowTypeCRUD;
	}

	public UserHandler getUserHandler() {

		return systemInterface.getUserHandler();
	}

	@Override
	public void appendFormData(Document doc, Element baseElement, MutableFlowInstanceManager instanceManager, HttpServletRequest req, User user) {

	}

	@Override
	public void appendShowFlowInstanceData(Document doc, Element baseElement, FlowInstanceManager instanceManager, HttpServletRequest req, User user) {

	}

	public GroupHandler getGroupHandler() {

		return systemInterface.getGroupHandler();
	}

	public FlowType getFlowType(Integer flowTypeID) {

		return flowTypeCacheMap.get(flowTypeID);
	}

	@Override
	protected void redirectToSubmitMethod(MutableFlowInstanceManager flowInstance, HttpServletRequest req, HttpServletResponse res) throws IOException {

		redirectToMethod(req, res, "/submitted/" + flowInstance.getFlowID());
	}

	@Override
	protected void flowInstanceSavedAndClosed(FlowInstanceManager flowInstanceManager, HttpServletRequest req, HttpServletResponse res, User user, FlowInstanceEvent event) throws IOException {

		redirectToMethod(req, res, "/showflow/" + flowInstanceManager.getFlowID());

	}

	@Override
	protected void closeSubmittedFlowInstanceManager(MutableFlowInstanceManager instanceManager, HttpServletRequest req) {

		SessionUtils.setAttribute(this.getClass().getName() + "-flow-" + instanceManager.getFlowID(), instanceManager, req);
	}

	@WebPublic(alias = "submitted")
	public ForegroundModuleResponse showSubmittedMessage(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws FlowInstanceManagerClosedException, UnableToGetQueryInstanceShowHTMLException, AccessDeniedException, ModuleConfigurationException, SQLException {

		Integer flowID;

		if (uriParser.size() == 3 && (flowID = uriParser.getInt(2)) != null) {

			MutableFlowInstanceManager instanceManager = (MutableFlowInstanceManager) SessionUtils.getAttribute(this.getClass().getName() + "-flow-" + flowID, req);

			if (instanceManager != null) {

				FlowInstance flowInstance = (FlowInstance) instanceManager.getFlowInstance();

				Timestamp now = TimeUtils.getCurrentTimestamp();

				flowInstance.setFlowInstanceID(12345);
				flowInstance.setPoster(user);
				flowInstance.setOwners(Collections.singletonList(user));
				flowInstance.setAdded(now);

				if (flowInstance.getEvents() == null) {

					flowInstance.setEvents(new ArrayList<FlowInstanceEvent>(1));
				}

				FlowInstanceEvent submittedEvent = new FlowInstanceEvent();

				submittedEvent.setAdded(now);
				submittedEvent.setPoster(user);
				submittedEvent.setEventType(EventType.SUBMITTED);

				flowInstance.getEvents().add(submittedEvent);

				try {
					ForegroundModuleResponse moduleResponse = showFlowInstance(req, res, user, user, uriParser, instanceManager, previewAccessController, this, "FlowInstanceManagerSubmitted", null, ShowMode.SUBMIT, DEFAULT_REQUEST_METADATA);

					instanceManager.close(queryHandler);

					return moduleResponse;

				} finally {

					SessionUtils.removeAttribute(this.getClass().getName() + "-flow-" + flowID, req);
				}

			}
		}

		log.info("User " + user + " requested invalid URL, listing flows");
		return list(req, res, user, uriParser, INVALID_LINK_VALIDATION_ERROR);
	}

	@Override
	protected String getBaseUpdateURL(HttpServletRequest req, URIParser uriParser, User user, ImmutableFlowInstance flowInstance, FlowInstanceAccessController accessController) {

		if (!accessController.isMutable(flowInstance, user)) {

			return null;
		}

		return req.getContextPath() + uriParser.getFormattedURI();
	}

	@Override
	public String getSignFailURL(MutableFlowInstanceManager instanceManager, HttpServletRequest req) {

		throw new UnsupportedOperationException();
	}
	
	@Override
	public String getStandalonePaymentURL(ImmutableFlowInstanceManager instanceManager, HttpServletRequest req) {

		throw new UnsupportedOperationException();
	}

	@Override
	public String getPaymentFailURL(MutableFlowInstanceManager instanceManager, HttpServletRequest req) {

		throw new UnsupportedOperationException();
	}

	@Override
	public String getSignSuccessURL(MutableFlowInstanceManager instanceManager, HttpServletRequest req) {

		throw new UnsupportedOperationException();
	}

	@Override
	public String getSaveAndSubmitURL(MutableFlowInstanceManager instanceManager, HttpServletRequest req) {

		throw new UnsupportedOperationException();
	}

	public SettingHandler getSiteProfileSettingHandler(User user, HttpServletRequest req, URIParser uriParser) {

		if (siteProfileHandler != null) {

			return siteProfileHandler.getCurrentSettingHandler(user, req, uriParser);
		}

		return null;

	}

	public boolean changeQueryTypeID(String oldQueryTypeID, String newQueryTypeID) throws SQLException {

		TransactionHandler transactionHandler = null;

		try {
			transactionHandler = daoFactory.getTransactionHandler();

			AnnotatedDAO<QueryDescriptor> queryDescriptorDAO = daoFactory.getQueryDescriptorDAO();

			//First check that the new query type ID is not taken already
			HighLevelQuery<QueryDescriptor> newIdCheckQuery = new HighLevelQuery<QueryDescriptor>();

			newIdCheckQuery.addParameter(queryDescriptorQueryTypeIDParamFactory.getParameter(newQueryTypeID));

			Integer newMatchCount = queryDescriptorDAO.getCount(newIdCheckQuery, transactionHandler);

			if (newMatchCount != null && newMatchCount > 0) {

				log.error("Refusing to change queryTypeID from " + oldQueryTypeID + " to " + newQueryTypeID + " since there already exists " + newMatchCount + " query descriptors of this type in DB");

				return false;
			}

			//Check if there are any query descriptors using the old ID
			HighLevelQuery<QueryDescriptor> oldIdCheckQuery = new HighLevelQuery<QueryDescriptor>();

			oldIdCheckQuery.addParameter(queryDescriptorQueryTypeIDParamFactory.getParameter(oldQueryTypeID));

			Integer oldMatchCount = queryDescriptorDAO.getCount(oldIdCheckQuery, transactionHandler);

			if (oldMatchCount == null || oldMatchCount == 0) {

				return true;
			}

			log.info("Changing queryTypeID for " + oldMatchCount + " query descriptors from " + oldQueryTypeID + " to " + newQueryTypeID);

			LowLevelQuery<QueryDescriptor> descriptorsUpdateQuery = new LowLevelQuery<QueryDescriptor>("UPDATE " + queryDescriptorDAO.getTableName() + " SET " + queryDescriptorQueryTypeIDParamFactory.getColumnName() + " = ? WHERE " + queryDescriptorQueryTypeIDParamFactory.getColumnName() + " = ?");

			descriptorsUpdateQuery.addParameter(newQueryTypeID);
			descriptorsUpdateQuery.addParameter(oldQueryTypeID);

			queryDescriptorDAO.update(descriptorsUpdateQuery, transactionHandler);

			LowLevelQuery<QueryDescriptor> flowTypesUpdateQuery = new LowLevelQuery<QueryDescriptor>("UPDATE flowengine_flow_type_allowed_queries SET queryTypeID = ? WHERE queryTypeID = ?");

			flowTypesUpdateQuery.addParameter(newQueryTypeID);
			flowTypesUpdateQuery.addParameter(oldQueryTypeID);

			queryDescriptorDAO.update(flowTypesUpdateQuery, transactionHandler);

			transactionHandler.commit();

			cacheFlows();
			cacheFlowTypes();

			return true;

		} finally {

			TransactionHandler.autoClose(transactionHandler);
		}
	}

	public int getQueryCount(String queryTypeID) throws SQLException {

		AnnotatedDAO<QueryDescriptor> queryDescriptorDAO = daoFactory.getQueryDescriptorDAO();

		HighLevelQuery<QueryDescriptor> query = new HighLevelQuery<QueryDescriptor>();

		query.addParameter(queryDescriptorQueryTypeIDParamFactory.getParameter(queryTypeID));

		return NumberUtils.toPrimitiveInt(queryDescriptorDAO.getCount(query));
	}

	public boolean changeEvaluatorTypeID(String oldEvaluatorTypeID, String newEvaluatorTypeID) throws SQLException {

		TransactionHandler transactionHandler = null;

		try {
			transactionHandler = daoFactory.getTransactionHandler();

			AnnotatedDAO<EvaluatorDescriptor> evaluatorDescriptorDAO = daoFactory.getEvaluatorDescriptorDAO();

			//First check that the new evaluator type ID is not taken already
			HighLevelQuery<EvaluatorDescriptor> newIdCheckQuery = new HighLevelQuery<EvaluatorDescriptor>();

			newIdCheckQuery.addParameter(evaluatorDescriptorEvaluatorTypeIDParamFactory.getParameter(newEvaluatorTypeID));

			Integer newMatchCount = evaluatorDescriptorDAO.getCount(newIdCheckQuery, transactionHandler);

			if (newMatchCount != null && newMatchCount > 0) {

				log.error("Refusing to change evaluatorTypeID from " + oldEvaluatorTypeID + " to " + newEvaluatorTypeID + " since there already exists " + newMatchCount + " evaluator descriptors of this type in DB");

				return false;
			}

			//Check if there are any evaluator descriptors using the old ID
			HighLevelQuery<EvaluatorDescriptor> oldIdCheckQuery = new HighLevelQuery<EvaluatorDescriptor>();

			oldIdCheckQuery.addParameter(evaluatorDescriptorEvaluatorTypeIDParamFactory.getParameter(oldEvaluatorTypeID));

			Integer oldMatchCount = evaluatorDescriptorDAO.getCount(oldIdCheckQuery, transactionHandler);

			if (oldMatchCount == null || oldMatchCount == 0) {

				return true;
			}

			log.info("Changing evaluatorTypeID for " + oldMatchCount + " evaluator descriptors from " + oldEvaluatorTypeID + " to " + newEvaluatorTypeID);

			LowLevelQuery<EvaluatorDescriptor> descriptorsUpdateQuery = new LowLevelQuery<EvaluatorDescriptor>("UPDATE " + evaluatorDescriptorDAO.getTableName() + " SET " + evaluatorDescriptorEvaluatorTypeIDParamFactory.getColumnName() + " = ? WHERE " + evaluatorDescriptorEvaluatorTypeIDParamFactory.getColumnName() + " = ?");

			descriptorsUpdateQuery.addParameter(newEvaluatorTypeID);
			descriptorsUpdateQuery.addParameter(oldEvaluatorTypeID);

			evaluatorDescriptorDAO.update(descriptorsUpdateQuery, transactionHandler);

			transactionHandler.commit();

			cacheFlows();
			cacheFlowTypes();

			return true;

		} finally {

			TransactionHandler.autoClose(transactionHandler);
		}
	}

	public int getEvaluatorCount(String evaluatorTypeID) throws SQLException {

		AnnotatedDAO<EvaluatorDescriptor> evaluatorDescriptorDAO = daoFactory.getEvaluatorDescriptorDAO();

		HighLevelQuery<EvaluatorDescriptor> query = new HighLevelQuery<EvaluatorDescriptor>();

		query.addParameter(evaluatorDescriptorEvaluatorTypeIDParamFactory.getParameter(evaluatorTypeID));

		return NumberUtils.toPrimitiveInt(evaluatorDescriptorDAO.getCount(query));
	}

	@WebPublic(toLowerCase = true)
	public ForegroundModuleResponse exportFlow(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws TransformerFactoryConfigurationError, Exception {

		Flow flow = flowCRUD.getRequestedBean(req, null, user, uriParser, GenericCRUD.SHOW);

		if (flow == null) {

			return list(req, res, user, uriParser, new ValidationError("FlowNotFound"));

		} else if (!AccessUtils.checkAccess(user, flow.getFlowType().getAdminAccessInterface())) {

			throw new AccessDeniedException("User does not have access to flow type " + flow.getFlowType());

		}

		log.info("User " + user + " exporting flow " + flow);

		List<ValidationError> validationErrors = new ArrayList<ValidationError>();

		Document doc = getExportFlowDocument(flow, validationErrors);

		if (!validationErrors.isEmpty()) {

			return flowCRUD.showBean(flow, req, res, user, uriParser, validationErrors);
		}

		res.setHeader("Content-Disposition", "attachment; filename=\"" + flow.getName() + ".oeflow\"");
		res.setHeader("Cache-Control", "no-store, no-cache, must-revalidate, proxy-revalidate");
		res.setContentType("text/xml");

		try {
			XMLUtils.writeXML(doc, res.getOutputStream(), true, systemInterface.getEncoding());
		} catch (TransformerException e) {
			
			log.info("Error sending exported flow " + flow + " to user " + user + ", " + e);
		}

		return null;
	}

	public Document getExportFlowDocument(Flow flow, List<ValidationError> validationErrors) throws IOException, SQLException {

		Document doc = XMLUtils.createDomDocument();

		XMLGeneratorDocument xmlGeneratorDocument = new XMLGeneratorDocument(doc);

		//TODO exclude flowtype
		//TODO exclude config url
		
		xmlGeneratorDocument.addElementableListener(QueryDescriptor.class, new QueryDescriptorElementableListener(queryHandler, validationErrors));
		xmlGeneratorDocument.addElementableListener(EvaluatorDescriptor.class, new EvaluatorDescriptorElementableListener(evaluationHandler, validationErrors));
		xmlGeneratorDocument.addElementableListener(FlowForm.class, new FlowFormExportElementableListener(this, validationErrors));

		Element flowNode = flow.toXML(xmlGeneratorDocument);
		doc.appendChild(flowNode);

		if (flow.getIcon() != null) {

			XMLUtils.appendNewElement(doc, doc.getDocumentElement(), "icon", Base64.encodeBytes(StreamUtils.toByteArray(flow.getIcon().getBinaryStream())));
		}

		return doc;
	}

	@WebPublic(toLowerCase = true, alias = "importversion")
	public ForegroundModuleResponse importFlowIntoExistingFamily(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws TransformerFactoryConfigurationError, Exception {

		Flow flow = flowCRUD.getRequestedBean(req, null, user, uriParser, GenericCRUD.SHOW);

		if (flow == null) {

			return list(req, res, user, uriParser, new ValidationError("FlowNotFound"));

		} else if (!AccessUtils.checkAccess(user, flow.getFlowType().getAdminAccessInterface())) {

			throw new AccessDeniedException("User does not have access to flow type " + flow.getFlowType());
		}

		return importFlow(flow.getFlowType(), flow, req, res, user, uriParser);
	}

	@WebPublic(toLowerCase = true, alias = "importflow")
	public ForegroundModuleResponse importFlowIntoNewFamily(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws TransformerFactoryConfigurationError, Exception {

		FlowType flowType = this.flowTypeCRUD.getRequestedBean(req, res, user, uriParser, FlowCRUD.SHOW);

		if (flowType == null) {

			log.info("User " + user + " listing flow type import targets");

			Document doc = createDocument(req, uriParser, user);
			Element selectImportTargetFamily = doc.createElement("SelectImportTargetType");
			doc.getDocumentElement().appendChild(selectImportTargetFamily);

			appendUserFlowTypes(doc, selectImportTargetFamily, user);

			return new SimpleForegroundModuleResponse(doc);

		} else if (!AccessUtils.checkAccess(user, flowType.getAdminAccessInterface())) {

			throw new AccessDeniedException("User does not have access to flow type " + flowType);
		}

		return importFlow(flowType, null, req, res, user, uriParser);
	}

	public synchronized ForegroundModuleResponse importFlow(FlowType flowType, Flow relatedFlow, HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws InstantiationException, IllegalAccessException, SQLException, IOException {

		ValidationException validationException = null;

		if (user.getSession().getAttribute("FlowImportFile") != null) {

			log.info("User " + user + " importing flow...");

			try {
				String filename = (String) user.getSession().getAttribute("FlowImportFileName");
				byte[] fileData = (byte[]) user.getSession().getAttribute("FlowImportFile");

				user.getSession().setAttribute("FlowImportFileName", null);
				user.getSession().setAttribute("FlowImportFile", null);

				if (filename != null && !filename.endsWith(".oeflow")) {

					throw new ValidationException(new InvalidFileExtensionValidationError(filename, "oeflow"));
				}

				validationException = importFlow(new ByteArrayInputStream(fileData), filename, flowType, relatedFlow, req, res, user, uriParser);

			} catch (ValidationException e) {

				validationException = e;

			} finally {

				if (validationException != null) {

					log.info("Import of flow by user " + user + " failed due to validation error(s) " + validationException);
				}
			}
		}

		if (req.getMethod().equalsIgnoreCase("POST") && MultipartRequest.isMultipartRequest(req)) {

			log.info("User " + user + " importing flow...");

			MultipartRequest multipartRequest = null;

			try {
				multipartRequest = new MultipartRequest(ramThreshold * BinarySizes.KiloByte, maxRequestSize * BinarySizes.MegaByte, req);
				req = multipartRequest;

				if (multipartRequest.getFileCount() == 0 || (multipartRequest.getFileCount() == 1 && multipartRequest.getFile(0).getName().equals(""))) {

					throw new ValidationException(new ValidationError("NoAttachedFile"));
				}

				FileItem fileItem = multipartRequest.getFile(0);

				if (!fileItem.getName().endsWith(".oeflow")) {

					throw new ValidationException(new InvalidFileExtensionValidationError(FilenameUtils.getName(fileItem.getName()), "oeflow"));
				}

				InputStream inputStream = null;

				try {
					inputStream = fileItem.getInputStream();

					validationException = importFlow(inputStream, FilenameUtils.getName(fileItem.getName()), flowType, relatedFlow, multipartRequest, res, user, uriParser);

				} finally {

					CloseUtils.close(inputStream);
				}

			} catch (ValidationException e) {

				validationException = e;

			} catch (SizeLimitExceededException e) {

				validationException = new ValidationException(new RequestSizeLimitExceededValidationError(e.getActualSize(), e.getPermittedSize()));

			} catch (FileSizeLimitExceededException e) {

				validationException = new ValidationException(new FileSizeLimitExceededValidationError(e.getFileName(), e.getActualSize(), e.getPermittedSize()));

			} catch (FileUploadException e) {

				validationException = new ValidationException(new ValidationError("UnableToParseRequest"));

			} finally {

				if (validationException != null) {

					log.info("Import of flow by user " + user + " failed due to validation error(s) " + validationException);
				}

				if (multipartRequest != null) {

					multipartRequest.deleteFiles();
				}
			}
		}

		if (relatedFlow != null) {

			log.info("User " + user + " requested flow version import form for flow familiy " + relatedFlow.getFlowFamily());

		} else {

			log.info("User " + user + " listing flow import form");
		}

		Document doc = this.createDocument(req, uriParser, user);
		Element importFlowElement = doc.createElement("ImportFlow");
		doc.getFirstChild().appendChild(importFlowElement);

		importFlowElement.appendChild(flowType.toXML(doc));
		XMLUtils.append(doc, importFlowElement, relatedFlow);

		if (validationException != null) {
			importFlowElement.appendChild(validationException.toXML(doc));
			importFlowElement.appendChild(RequestUtils.getRequestParameters(req, doc, "categoryID"));
		}

		return new SimpleForegroundModuleResponse(doc, moduleDescriptor.getName(), this.getDefaultBreadcrumb());
	}

	private synchronized ValidationException importFlow(InputStream inputStream, String filename, FlowType flowType, Flow relatedFlow, HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws InstantiationException, IllegalAccessException, SQLException, IOException {

		ValidationException validationException = null;

		try {
			Document doc = null;

			try {
				doc = XMLUtils.parseXML(inputStream, false, false);

			} catch (Exception e) {

				log.info("Unable to parse file " + filename, e);

				throw new ValidationException(new UnableToParseFileValidationError(filename));
			}

			Element docElement = doc.getDocumentElement();

			if (!docElement.getTagName().equals("Flow")) {

				log.info("Error parsing file " + filename + ", unable to find flow element");

				throw new ValidationException(new UnableToParseFileValidationError(filename));
			}

			Flow flow = Flow.class.newInstance();

			XMLParser xmlParser = new XMLParser(docElement);

			flow.populate(xmlParser);

			flow.setFlowType(flowType);

			if (relatedFlow == null) {

				flow.setVersion(1);

				FlowFamily flowFamily = new FlowFamily();
				flowFamily.setVersionCount(1);

				flow.setFlowFamily(flowFamily);

			} else {

				FlowFamily flowFamily = SerializationUtils.cloneSerializable(relatedFlow.getFlowFamily());
				flow.setFlowFamily(flowFamily);
			}

			Integer categoryID = NumberUtils.toInt(req.getParameter("categoryID"));

			if (categoryID != null && flowType.getCategories() != null) {

				for (Category category : flowType.getCategories()) {

					if (category.getCategoryID().equals(categoryID)) {

						flow.setCategory(category);
						break;
					}

				}
			}

			//Create translation map for query ID's in order to able to update target queries field of evaluators later on
			HashMap<EvaluatorDescriptor, List<QueryDescriptor>> evaluatorTargetQueriesMap = new HashMap<EvaluatorDescriptor, List<QueryDescriptor>>();

			if (flow.getSteps() != null) {

				List<ValidationError> validationErrors = new ArrayList<ValidationError>();

				for (Step step : flow.getSteps()) {

					if (step.getQueryDescriptors() != null) {

						for (QueryDescriptor queryDescriptor : step.getQueryDescriptors()) {

							//Check if a query provider for this query type is available
							if (queryHandler.getQueryProvider(queryDescriptor.getQueryTypeID()) == null) {

								log.info("Unable to find query provider for query type " + queryDescriptor.getQueryTypeID() + " used by query " + queryDescriptor);

								validationErrors.add(new QueryTypeNotFoundValidationError(queryDescriptor));

							} else {

								//Check if this query type is allowed for the select flowtype
								if (flowType.getAllowedQueryTypes() == null || !flowType.getAllowedQueryTypes().contains(queryDescriptor.getQueryTypeID())) {

									validationErrors.add(new QueryTypeNotAllowedInFlowTypeValidationError(queryDescriptor, flowType));
								}
							}

							if (queryDescriptor.getEvaluatorDescriptors() != null) {

								for (EvaluatorDescriptor evaluatorDescriptor : queryDescriptor.getEvaluatorDescriptors()) {

									//Check if a evaluation provider for this evaluator type is available
									if (evaluationHandler.getEvaluationProvider(evaluatorDescriptor.getEvaluatorTypeID()) == null) {

										log.info("Unable to find evulation provider for evaluator type " + evaluatorDescriptor.getEvaluatorTypeID() + " used by evaluator " + evaluatorDescriptor);

										validationErrors.add(new EvaluatorTypeNotFoundValidationError(evaluatorDescriptor));
									}

									if (evaluatorDescriptor.getTargetQueryIDs() != null) {

										List<QueryDescriptor> targetQueries = getTargetQueries(evaluatorDescriptor.getTargetQueryIDs(), flow.getSteps());

										if (targetQueries != null) {

											evaluatorTargetQueriesMap.put(evaluatorDescriptor, targetQueries);
										}

										evaluatorDescriptor.setTargetQueryIDs(null);
									}
								}
							}
						}
					}
				}

				if (!validationErrors.isEmpty()) {

					throw new ValidationException(validationErrors);
				}
			}

			//Set correct status references on default flow statuses and check actionID's
			if (flow.getStatuses() != null && flow.getDefaultFlowStateMappings() != null) {

				Iterator<DefaultStatusMapping> iterator = flow.getDefaultFlowStateMappings().iterator();

				mappingLoop: while (iterator.hasNext()) {

					DefaultStatusMapping statusMapping = iterator.next();

					//If the action for this status mapping does not exist in this installation skip this mapping
					if (!actionExists(statusMapping.getActionID())) {

						log.info("Removing default status mapping for action ID " + statusMapping.getActionID() + " from imported flow since it's supported in this installation.");
						iterator.remove();
						continue;
					}

					if (statusMapping.getStatus() == null) {

						log.info("Removing default status mapping for action ID " + statusMapping.getActionID() + " since it has no status set.");
						iterator.remove();
						continue;
					}

					Integer statusID = statusMapping.getStatus().getStatusID();

					for (Status status : flow.getStatuses()) {

						if (status.getStatusID().equals(statusID)) {

							statusMapping.setStatus(status);

							continue mappingLoop;
						}
					}

					//No matching status found in flow status list, skip this mapping
					log.info("Removing default status mapping for action ID " + statusMapping.getActionID() + " since no matching status could be found.");
					iterator.remove();
				}
			}

			//Clear query descriptor ID's
			if (flow.getSteps() != null) {

				for (Step step : flow.getSteps()) {

					if (step.getQueryDescriptors() != null) {

						for (QueryDescriptor queryDescriptor : step.getQueryDescriptors()) {

							queryDescriptor.setQueryID(null);
						}
					}
				}
			}

			Map<Integer, ImmutableStatus> statusConversionMap;

			//Clear status ID's
			if (flow.getStatuses() != null) {

				statusConversionMap = new HashMap<Integer, ImmutableStatus>(flow.getStatuses().size());

				for (Status status : flow.getStatuses()) {

					statusConversionMap.put(status.getStatusID(), status);
					status.setStatusID(null);
				}

			} else {

				statusConversionMap = null;
			}

			//Create transaction
			TransactionHandler transactionHandler = null;

			try {
				transactionHandler = daoFactory.getTransactionHandler();

				//Add flow to database
				if (relatedFlow == null) {

					daoFactory.getFlowDAO().add(flow, transactionHandler, ADD_NEW_FLOW_AND_FAMILY_RELATION_QUERY);

				} else {

					Integer version = getNextVersion(flow.getFlowFamily().getFlowFamilyID(), transactionHandler);

					if (version == null) {

						throw new RuntimeException("Flow family " + flow.getFlowFamily() + " not found in database.");
					}

					flow.setVersion(version);
					flow.getFlowFamily().setVersionCount(version);

					daoFactory.getFlowFamilyDAO().update(flow.getFlowFamily(), transactionHandler, null);
					daoFactory.getFlowDAO().add(flow, transactionHandler, ADD_NEW_FLOW_VERSION_RELATION_QUERY);
				}

				//Set target query ID's on evaluator descriptors
				for (Entry<EvaluatorDescriptor, List<QueryDescriptor>> entry : evaluatorTargetQueriesMap.entrySet()) {

					List<Integer> targetQueryIDs = new ArrayList<Integer>(entry.getValue().size());

					for (QueryDescriptor queryDescriptor : entry.getValue()) {

						targetQueryIDs.add(queryDescriptor.getQueryID());
					}

					entry.getKey().setTargetQueryIDs(targetQueryIDs);

					daoFactory.getEvaluatorDescriptorDAO().update(entry.getKey(), transactionHandler, null);
				}

				HashMap<QueryDescriptor, Query> importedQueryMap = new HashMap<QueryDescriptor, Query>();

				//Import queries using QueryHandler
				if (flow.getSteps() != null) {

					for (Step step : flow.getSteps()) {

						if (step.getQueryDescriptors() != null) {

							for (QueryDescriptor queryDescriptor : step.getQueryDescriptors()) {

								try {
									queryDescriptor.setStep(null);
									importedQueryMap.put(queryDescriptor, queryHandler.importQuery(queryDescriptor, transactionHandler));

								} catch (Exception e) {

									log.error("Error importing query " + queryDescriptor + " of type " + queryDescriptor.getQueryTypeID() + " into flow " + flow + " uploaded by user " + user, e);

									throw new ValidationException(new QueryImportValidationError(queryDescriptor));
								}
							}
						}
					}
				}

				//Import evaluator using EvaluationHandler
				if (flow.getSteps() != null) {

					for (Step step : flow.getSteps()) {

						if (step.getQueryDescriptors() != null) {

							for (QueryDescriptor queryDescriptor : step.getQueryDescriptors()) {

								if (queryDescriptor.getEvaluatorDescriptors() != null) {

									for (EvaluatorDescriptor evaluatorDescriptor : queryDescriptor.getEvaluatorDescriptors()) {

										try {
											evaluatorDescriptor.setQueryDescriptor(null);
											evaluationHandler.importEvaluator(evaluatorDescriptor, transactionHandler, importedQueryMap.get(queryDescriptor), statusConversionMap);

										} catch (Exception e) {

											log.error("Error importing evaluator " + evaluatorDescriptor + " of type " + evaluatorDescriptor.getEvaluatorTypeID() + " into flow " + flow + " uploaded by user " + user, e);

											throw new ValidationException(new EvaluatorImportValidationError(evaluatorDescriptor));
										}
									}
								}
							}
						}
					}
				}

				if (!CollectionUtils.isEmpty(flow.getFlowForms())) {

					if (flow.getFlowForms().size() == 1) {

						String oldFlowForm = xmlParser.getString("/Flow/PDF");

						// Handle old flow form format
						if (!StringUtils.isEmpty(oldFlowForm)) {

							byte[] pdfFileContents = Base64.decode(xmlParser.getString("/Flow/PDF"));

							if (pdfFileContents != null) {

								FileUtils.writeFile(getFlowFormFilePath(flow.getFlowForms().get(0)), pdfFileContents);
							}
						}
					}

					for (FlowForm flowForm : flow.getFlowForms()) {

						if (flowForm.getImportFileContents() != null) {

							FileUtils.writeFile(new File(getFlowFormFilePath(flowForm)), flowForm.getImportFileContents());
						}
					}
				}

				try {
					// Commit
					transactionHandler.commit();

				} catch (SQLException e) {

					// Cleanup
					if (!CollectionUtils.isEmpty(flow.getFlowForms())) {

						for (FlowForm flowForm : flow.getFlowForms()) {

							deleteFlowFormFile(flowForm);
						}
					}

					throw e;
				}

				log.info("User " + user + " succefully imported flow " + flow);

				eventHandler.sendEvent(Flow.class, new CRUDEvent<Flow>(CRUDAction.ADD, flow), EventTarget.ALL);

				addFlowFamilyEvent(eventImportFlowMessage + " \"" + flow.getName() + "\"", flow, user);

				redirectToMethod(req, res, "/showflow/" + flow.getFlowID());

			} finally {

				TransactionHandler.autoClose(transactionHandler);
			}

		} catch (ValidationException e) {

			validationException = e;

		} finally {

			if (validationException != null) {

				log.info("Import of flow by user " + user + " failed due to validation error(s) " + validationException);
			}
		}

		return validationException;
	}

	private Integer getNextVersion(Integer flowFamilyID, TransactionHandler transactionHandler) throws SQLException {

		HighLevelQuery<FlowFamily> query = new HighLevelQuery<FlowFamily>();

		query.addParameter(flowFamiliyIDParamFactory.getParameter(flowFamilyID));

		FlowFamily flowFamily = daoFactory.getFlowFamilyDAO().get(query, transactionHandler);

		if (flowFamily != null) {

			return flowFamily.getVersionCount() + 1;
		}

		return null;
	}

	@WebPublic(toLowerCase = true, alias = "importqueries")
	public ForegroundModuleResponse importQueries(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws TransformerFactoryConfigurationError, Exception {

		Flow flow = flowCRUD.getRequestedBean(req, null, user, uriParser, GenericCRUD.SHOW);

		if (flow == null) {

			return list(req, res, user, uriParser, new ValidationError("FlowNotFound"));

		} else if (!AccessUtils.checkAccess(user, flow.getFlowType().getAdminAccessInterface())) {

			throw new AccessDeniedException("User does not have access to flow type " + flow.getFlowType());
		}

		ValidationException validationException = null;

		if (req.getMethod().equalsIgnoreCase("POST") && MultipartRequest.isMultipartRequest(req)) {

			log.info("User " + user + " importing queries into flow " + flow);

			MultipartRequest multipartRequest = null;

			try {
				multipartRequest = new MultipartRequest(ramThreshold * BinarySizes.KiloByte, maxRequestSize * BinarySizes.MegaByte, req);
				req = multipartRequest;

				Integer stepID = NumberUtils.toInt(req.getParameter("stepID"));

				if (stepID == null) {

					throw new ValidationException(new ValidationError("stepID", ValidationErrorType.RequiredField));
				}

				Step step = flow.getStep(stepID);

				if (step == null) {

					throw new ValidationException(new ValidationError("SelectedStepNotFound"));
				}

				if (multipartRequest.getFileCount() == 0 || (multipartRequest.getFileCount() == 1 && multipartRequest.getFile(0).getName().equals(""))) {

					throw new ValidationException(new ValidationError("NoAttachedFile"));
				}

				Integer sortindex = getCurrentMaxSortIndex(step);

				List<ValidationError> validationErrors = new ArrayList<ValidationError>(multipartRequest.getFileCount());
				List<QueryDescriptor> queryDescriptors = new ArrayList<QueryDescriptor>(multipartRequest.getFileCount());

				for (FileItem fileItem : multipartRequest.getFiles()) {

					if (fileItem.getName().equals("")) {

						continue;
					}

					if (!fileItem.getName().endsWith(".oequery")) {

						validationErrors.add(new InvalidFileExtensionValidationError(FilenameUtils.getName(fileItem.getName()), "oequery"));
						continue;
					}

					InputStream inputStream = null;

					Document doc = null;

					try {
						inputStream = fileItem.getInputStream();

						doc = XMLUtils.parseXML(inputStream, false, false);

					} catch (Exception e) {

						log.info("Unable to parse file " + FilenameUtils.getName(fileItem.getName()), e);

						validationErrors.add(new UnableToParseFileValidationError(FilenameUtils.getName(fileItem.getName())));
						continue;

					} finally {

						CloseUtils.close(inputStream);
					}

					Element docElement = doc.getDocumentElement();

					if (!docElement.getTagName().equals("QueryDescriptor")) {

						log.info("Error parsing file " + fileItem.getName() + ", unable to find flow element");

						validationErrors.add(new UnableToParseFileValidationError(fileItem.getName()));
						continue;
					}

					QueryDescriptor queryDescriptor = new QueryDescriptor();

					XMLParser xmlParser = new XMLParser(docElement);

					try {
						queryDescriptor.populate(xmlParser);
						queryDescriptor.setQueryID(null);
						queryDescriptor.setSortIndex(++sortindex);

					} catch (ValidationException e) {

						validationErrors.addAll(e.getErrors());
						continue;
					}

					//Check if a query provider for this query type is available
					if (queryHandler.getQueryProvider(queryDescriptor.getQueryTypeID()) == null) {

						log.info("Unable to find query provider for query type " + queryDescriptor.getQueryTypeID() + " used by query " + queryDescriptor);

						validationErrors.add(new QueryTypeNotFoundValidationError(queryDescriptor));
						continue;

					} else {

						//Check if this query type is allowed for the select flowtype
						if (flow.getFlowType().getAllowedQueryTypes() == null || !flow.getFlowType().getAllowedQueryTypes().contains(queryDescriptor.getQueryTypeID())) {

							validationErrors.add(new QueryTypeNotAllowedInFlowTypeValidationError(queryDescriptor, flow.getFlowType()));
							continue;
						}
					}

					queryDescriptor.setStep(step);
					queryDescriptors.add(queryDescriptor);
				}

				if (!validationErrors.isEmpty()) {

					throw new ValidationException(validationErrors);

				} else if (queryDescriptors.isEmpty()) {

					throw new ValidationException(new ValidationError("NoAttachedFile"));
				}

				//Create transaction
				TransactionHandler transactionHandler = null;

				try {
					transactionHandler = daoFactory.getTransactionHandler();

					this.daoFactory.getQueryDescriptorDAO().addAll(queryDescriptors, transactionHandler, null);

					for (QueryDescriptor queryDescriptor : queryDescriptors) {

						try {
							queryDescriptor.setStep(null);
							queryHandler.importQuery(queryDescriptor, transactionHandler);

						} catch (Exception e) {

							log.error("Error importing query " + queryDescriptor + " of type " + queryDescriptor.getQueryTypeID() + " into flow " + flow + " uploaded by user " + user, e);

							throw new ValidationException(new QueryImportValidationError(queryDescriptor));
						}
					}

					//Commit
					transactionHandler.commit();

					log.info("User " + user + " succefully imported queries " + StringUtils.toCommaSeparatedString(queryDescriptors) + " into flow " + flow);

					eventHandler.sendEvent(Flow.class, new CRUDEvent<Flow>(CRUDAction.UPDATE, flow), EventTarget.ALL);

					addFlowFamilyEvent(eventImportQueriesMessage + " " + StringUtils.toCommaSeparatedString(queryDescriptors), flow, user);

					redirectToMethod(req, res, "/showflow/" + flow.getFlowID() + "#steps");

				} finally {

					TransactionHandler.autoClose(transactionHandler);
				}

			} catch (ValidationException e) {

				validationException = e;

			} catch (SizeLimitExceededException e) {

				validationException = new ValidationException(new RequestSizeLimitExceededValidationError(e.getActualSize(), e.getPermittedSize()));

			} catch (FileSizeLimitExceededException e) {

				validationException = new ValidationException(new FileSizeLimitExceededValidationError(e.getFileName(), e.getActualSize(), e.getPermittedSize()));

			} catch (FileUploadException e) {

				validationException = new ValidationException(new ValidationError("UnableToParseRequest"));

			} finally {

				if (validationException != null) {

					log.info("Import of queries by user " + user + " into flow " + flow + " failed due to validation error(s) " + validationException);
				}

				if (multipartRequest != null) {

					multipartRequest.deleteFiles();
				}
			}
		}

		log.info("User " + user + " requested import queries form for flow " + flow);

		Document doc = this.createDocument(req, uriParser, user);
		Element importQueriesElement = doc.createElement("ImportQueries");
		doc.getFirstChild().appendChild(importQueriesElement);

		importQueriesElement.appendChild(flow.toXML(doc));

		if (validationException != null) {
			importQueriesElement.appendChild(validationException.toXML(doc));
		}

		return new SimpleForegroundModuleResponse(doc, moduleDescriptor.getName(), this.getDefaultBreadcrumb());
	}

	public int getCurrentMaxSortIndex(Step step) throws SQLException {

		ObjectQuery<Integer> query = new ObjectQuery<Integer>(dataSource, "SELECT MAX(sortIndex) FROM " + daoFactory.getQueryDescriptorDAO().getTableName() + " WHERE stepID = ?", IntegerPopulator.getPopulator());

		query.setInt(1, step.getStepID());

		Integer sortIndex = query.executeQuery();

		if (sortIndex == null) {

			return 0;
		}

		return sortIndex;
	}

	@WebPublic(toLowerCase = true)
	public ForegroundModuleResponse exportQuery(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws TransformerFactoryConfigurationError, Exception {

		QueryDescriptor queryDescriptor = queryDescriptorCRUD.getRequestedBean(req, res, user, uriParser, GenericCRUD.UPDATE);

		if (queryDescriptor == null) {

			return list(req, res, user, uriParser, new ValidationError("ExportFailedQueryDescriptorNotFound"));

		}

		Flow flow = queryDescriptor.getStep().getFlow();

		if (!AccessUtils.checkAccess(user, flow.getFlowType().getAdminAccessInterface())) {

			throw new AccessDeniedException("User does not have access to flow type " + queryDescriptor.getStep().getFlow().getFlowType());

		}

		log.info("User " + user + " exporting query " + queryDescriptor + " from flow " + flow);

		queryDescriptor.setStep(null);

		if (queryDescriptor.getEvaluatorDescriptors() != null) {

			queryDescriptor.setEvaluatorDescriptors(null);
		}

		Document doc = XMLUtils.createDomDocument();

		Element queryDescriptorElement = queryDescriptor.toXML(doc);

		try {
			Query query = queryHandler.getQuery(queryDescriptor);

			queryDescriptorElement.appendChild(query.toXML(doc));

		} catch (Exception e) {

			log.error("Error exporting query " + queryDescriptor, e);

			return flowCRUD.showBean(flowCacheMap.get(flow.getFlowID()), req, res, user, uriParser, new QueryExportValidationError(queryDescriptor));
		}

		doc.appendChild(queryDescriptorElement);

		res.setHeader("Content-Disposition", "attachment; filename=\"" + queryDescriptor.getName() + ".oequery\"");
		res.setHeader("Cache-Control", "no-store, no-cache, must-revalidate, proxy-revalidate");
		res.setContentType("text/xml");

		XMLUtils.writeXML(doc, res.getOutputStream(), true, systemInterface.getEncoding());

		return null;
	}

	private boolean actionExists(String actionID) throws SQLException {

		HighLevelQuery<FlowAction> query = new HighLevelQuery<FlowAction>();

		query.addParameter(flowActionIDParamFactory.getParameter(actionID));

		return daoFactory.getFlowActionDAO().getBoolean(query);
	}

	private List<QueryDescriptor> getTargetQueries(List<Integer> targetQueryIDs, List<Step> steps) {

		List<QueryDescriptor> targetQueries = null;

		for (Step step : steps) {

			if (step.getQueryDescriptors() != null) {

				for (QueryDescriptor queryDescriptor : step.getQueryDescriptors()) {

					if (targetQueryIDs.contains(queryDescriptor.getQueryID())) {

						targetQueries = CollectionUtils.addAndInstantiateIfNeeded(targetQueries, queryDescriptor);
					}
				}
			}
		}

		return targetQueries;
	}

	public void appendUserFlowTypes(Document doc, Element targetElement, User user) {

		Element flowTypesElement = doc.createElement("FlowTypes");
		targetElement.appendChild(flowTypesElement);

		for (FlowType flowType : getCachedFlowTypes()) {

			if (AccessUtils.checkAccess(user, flowType.getAdminAccessInterface())) {

				flowTypesElement.appendChild(flowType.toXML(doc));
			}
		}
	}

	@WebPublic(alias = "users")
	public ForegroundModuleResponse getUsers(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Throwable {

		return userGroupListConnector.getUsers(req, res, user, uriParser);
	}

	@WebPublic(alias = "groups")
	public ForegroundModuleResponse getGroups(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Throwable {

		return userGroupListConnector.getGroups(req, res, user, uriParser);
	}

	@WebPublic(alias = "allusers")
	public ForegroundModuleResponse getAllUsers(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Throwable {

		return unrestrictedUserGroupListConnector.getUsers(req, res, user, uriParser);
	}

	@WebPublic(alias = "allgroups")
	public ForegroundModuleResponse getAllGroups(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Throwable {

		return unrestrictedUserGroupListConnector.getGroups(req, res, user, uriParser);
	}
	
	@WebPublic(alias = "managerusers")
	public ForegroundModuleResponse getManagerUsers(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Throwable {

		return userGroupListFlowManagersConnector.getUsers(req, res, user, uriParser);
	}
	
	public FlowNotificationHandler getNotificationHandler() {

		return notificationHandler;
	}

	@Override
	public int getPriority() {

		return 0;
	}

	public String getCkConnectorModuleAlias() {

		return ckConnectorModuleAlias;
	}

	public String getCssPath() {

		return cssPath;
	}

	@WebPublic(toLowerCase = true)
	public ForegroundModuleResponse getFlowForm(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Throwable {

		Integer flowFormID;
		Integer flowID;
		Flow flow;

		if (uriParser.size() >= 3 && (flowID = uriParser.getInt(2)) != null && (flow = flowCRUD.getBean(flowID, FlowCRUD.SHOW)) != null) {

			flowCRUD.checkAccess(user, flow);

			if (CollectionUtils.isEmpty(flow.getFlowForms())) {

				log.info("User " + user + " requested flow " + flow.getFlowID() + " which is not availiable in the requested format.");

				throw new FlowNotAvailiableInRequestedFormat(flow.getFlowID());
			}

			boolean raw = req.getParameter("raw") != null;

			if (uriParser.size() == 3) {

				FlowForm flowForm = flow.getFlowForms().get(0);
				flowForm.setFlow(flow);

				return sendFlowForm(flowForm, req, res, user, uriParser, getCurrentSiteProfile(req, user, uriParser, flow.getFlowFamily()), raw);

			} else if (uriParser.size() == 4 && (flowFormID = uriParser.getInt(3)) != null) {

				for (FlowForm flowForm : flow.getFlowForms()) {

					if (flowForm.getFlowFormID().equals(flowFormID)) {

						flowForm.setFlow(flow);

						return sendFlowForm(flowForm, req, res, user, uriParser, getCurrentSiteProfile(req, user, uriParser, flow.getFlowFamily()), raw);
					}
				}
			}
		}

		throw new URINotFoundException(uriParser);
	}

	public ForegroundModuleResponse sendFlowForm(FlowForm flowForm, HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser, SiteProfile siteProfile, boolean unmodified) throws IOException, ModuleConfigurationException, URINotFoundException {

		if (!StringUtils.isEmpty(flowForm.getExternalURL())) {

			res.sendRedirect(flowForm.getExternalURL());
			return null;
		}

		if (flowFormFilestore == null) {

			throw new ModuleConfigurationException("Form filestore not set");
		}

		log.info("User " + user + " downloading form for flow form " + flowForm);

		File file = new File(getFlowFormFilePath(flowForm));

		if (!file.exists()) {

			log.warn("Unable to find PDF file for flowForm " + flowForm + " at " + file);

			throw new URINotFoundException(uriParser);
		}

		File sentFile = file;

		if (!unmodified) {

			for (PDFRequestFilter filter : pdfRequestFilters) {

				try {
					sentFile = filter.processPDFRequest(sentFile, siteProfile);

				} catch (Throwable t) {

					log.error("Error running PDF request filter " + filter, t);
				}
			}
		}

		try {
			HTTPUtils.sendFile(sentFile, flowForm.getName() + ".pdf", req, res, ContentDisposition.ATTACHMENT);

		} catch (IOException e) {

			log.info("Error sending file " + sentFile + " to user " + user + ", " + e);
		}

		// Remove temporary file
		if (file != sentFile) {

			FileUtils.deleteFile(sentFile);
		}

		return null;
	}

	public String getFlowFormFilestore() {

		return flowFormFilestore;
	}

	public String getFlowFormFilePath(FlowForm form) {

		return getFlowFormFilestore() + java.io.File.separator + form.getFlowFormID() + ".pdf";
	}

	public boolean deleteFlowFormFile(FlowForm form) {

		return FileUtils.deleteFile(getFlowFormFilePath(form));
	}

	public boolean allowSkipOverviewForFlowForms() {

		return allowSkipOverviewForFlowForms;
	}

	public FlowFamily getFlowFamilyByAlias(String alias) {

		for (FlowFamily flowFamily : flowFamilyCacheMap.values()) {

			if (!CollectionUtils.isEmpty(flowFamily.getAliases()) && flowFamily.getAliases().contains(alias)) {

				return flowFamily;
			}
		}

		return null;
	}

	public boolean checkAliasForSystemCollision(String alias) {

		return systemInterface.getRootSection().getForegroundModuleCache().getEntry(alias) != null || systemInterface.getRootSection().getSectionCache().getEntry(alias) != null;
	}

	public boolean hasPublishAccess(User user) {

		if (CollectionUtils.isEmpty(publisherGroupIDs)) {

			return true;
		}

		if (!CollectionUtils.isEmpty(user.getGroups())) {

			for (Group group : user.getGroups()) {

				if (group.isEnabled() && publisherGroupIDs.contains(group.getGroupID())) {
					return true;
				}
			}
		}

		return false;
	}

	public void addPDFRequestFilter(PDFRequestFilter filter) {

		if (filter == null) {
			throw new NullPointerException();
		}

		pdfRequestFilters.add(filter);
	}

	public void removePDFRequestFilter(PDFRequestFilter filter) {

		pdfRequestFilters.remove(filter);
	}

	public synchronized void addExtensionViewProvider(FlowAdminExtensionViewProvider flowAdminExtensionProvider) {

		if (!extensionViewProviders.contains(flowAdminExtensionProvider)) {

			log.info("Extension view provider " + flowAdminExtensionProvider + " added");
			
			List<FlowAdminExtensionViewProvider> tempProviders = new ArrayList<FlowAdminExtensionViewProvider>(extensionViewProviders);

			tempProviders.add(flowAdminExtensionProvider);

			Collections.sort(tempProviders, PriorityComparator.ASC_COMPARATOR);

			extensionViewProviders = new CopyOnWriteArrayList<FlowAdminExtensionViewProvider>(tempProviders);
		}

	}

	public synchronized void removeExtensionViewProvider(FlowAdminExtensionViewProvider flowAdminExtensionProvider) {

		extensionViewProviders.remove(flowAdminExtensionProvider);

		log.info("Extension view provider " + flowAdminExtensionProvider + " removed");
	}

	public List<FlowAdminExtensionViewProvider> getExtensionViewProviders() {

		return extensionViewProviders;
	}

	public void addFlowListExtensionLinkProvider(ExtensionLinkProvider e) {

		if (!flowListExtensionLinkProviders.contains(e)) {

			flowListExtensionLinkProviders.add(e);

			log.info("List flow extension link provider " + e + " added");

			if(systemInterface.getSystemStatus() == SystemStatus.STARTED) {
				
				sectionInterface.getMenuCache().moduleUpdated(moduleDescriptor, this);
			}
		
		}
	}

	public void removeFlowListExtensionLinkProvider(ExtensionLinkProvider e) {

		flowListExtensionLinkProviders.remove(e);

		log.info("List flow extension link provider " + e + " removed");
	
		if(systemInterface.getSystemStatus() == SystemStatus.STARTED) {
			
			sectionInterface.getMenuCache().moduleUpdated(moduleDescriptor, this);
		}
	}

	public void addFlowShowExtensionLinkProvider(FlowAdminShowFlowExtensionLinkProvider e) {

		if (!flowShowExtensionLinkProviders.contains(e)) {

			flowShowExtensionLinkProviders.add(e);

			log.info("Show flow extension link provider " + e + " added");
		}
	}

	public void removeFlowShowExtensionLinkProvider(FlowAdminShowFlowExtensionLinkProvider e) {

		flowShowExtensionLinkProviders.remove(e);

		log.info("Show flow extension link provider " + e + " removed");
	}

	public List<FlowAdminShowFlowExtensionLinkProvider> getFlowShowExtensionLinkProviders() {

		return flowShowExtensionLinkProviders;
	}

	public List<FlowFamilyEvent> getRecentFlowFamilyEvents(FlowFamily flowFamily) throws SQLException {

		if (flowFamily == null) {
			throw new NullPointerException();
		}

		HighLevelQuery<FlowFamilyEvent> query = new HighLevelQuery<FlowFamilyEvent>();

		query.addParameter(flowFamilyEventFlowFamilyParamFactory.getParameter(flowFamily));

		query.setRowLimiter(new MySQLRowLimiter(recentFlowFamilyEventCount));

		return daoFactory.getFlowFamilyEventDAO().getAll(query);
	}

	public List<FlowFamilyEvent> getFlowFamilyEvents(FlowFamily flowFamily) throws SQLException {

		if (flowFamily == null) {
			throw new NullPointerException();
		}

		HighLevelQuery<FlowFamilyEvent> query = new HighLevelQuery<FlowFamilyEvent>();

		query.addParameter(flowFamilyEventFlowFamilyParamFactory.getParameter(flowFamily));

		return daoFactory.getFlowFamilyEventDAO().getAll(query);
	}

	@WebPublic(toLowerCase = true)
	public ForegroundModuleResponse showFlowFamilyEvents(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Exception {

		Integer flowFamilyID;
		FlowFamily flowFamily;

		if (uriParser.size() >= 3 && PositiveStringIntegerPopulator.getPopulator().validateFormat(uriParser.get(2)) && (flowFamilyID = PositiveStringIntegerPopulator.getPopulator().getValue(uriParser.get(2))) != null && (flowFamily = flowFamilyCacheMap.get(flowFamilyID)) != null) {

			Flow flow;

			if (uriParser.size() >= 4) {

				Integer flowID;

				if (!(PositiveStringIntegerPopulator.getPopulator().validateFormat(uriParser.get(3)) && (flowID = PositiveStringIntegerPopulator.getPopulator().getValue(uriParser.get(3))) != null && (flow = flowCacheMap.get(flowID)) != null)) {

					throw new URINotFoundException(uriParser);
				}

			} else {

				flow = getLatestFlowVersion(flowFamily);
			}

			if (!AccessUtils.checkAccess(user, flow.getFlowType().getAdminAccessInterface()) && !AccessUtils.checkAccess(user, this)) {

				throw new AccessDeniedException("User does not have access to flow type " + flow.getFlowType());
			}

			Document doc = createDocument(req, uriParser, user);
			Element showFlowFamilyEventsElement = doc.createElement("ShowFlowFamilyEvents");
			doc.getDocumentElement().appendChild(showFlowFamilyEventsElement);

			showFlowFamilyEventsElement.appendChild(flow.toXML(doc));
			XMLUtils.append(doc, showFlowFamilyEventsElement, "FlowFamilyEvents", getFlowFamilyEvents(flowFamily));

			return new SimpleForegroundModuleResponse(doc, getTitlePrefix(), getDefaultBreadcrumb(), new Breadcrumb(this, flow.getName(), "/showFlow/" + flow.getFlowID()));
		}

		throw new URINotFoundException(uriParser);
	}

	@Override
	public void addFlowFamilyEvent(String message, ImmutableFlow flow, User user) {

		if (flow.getFlowFamily() != null) {

			addFlowFamilyEvent(message, flowFamilyCacheMap.get(flow.getFlowFamily().getFlowFamilyID()), flow.getVersion(), user);

		} else {

			addFlowFamilyEvent(message, flowCacheMap.get(flow.getFlowID()).getFlowFamily(), flow.getVersion(), user);
		}
	}

	@Override
	public void addFlowFamilyEvent(String message, FlowFamily flowFamily, User user) {

		addFlowFamilyEvent(message, flowFamily, null, user);
	}

	private void addFlowFamilyEvent(String message, FlowFamily flowFamily, Integer flowVersion, User user) {

		message = StringUtils.substring(message, 255, "...");

		FlowFamilyEvent event = new FlowFamilyEvent(flowFamily, flowVersion, new Timestamp(System.currentTimeMillis()), user, message);

		try {
			daoFactory.getFlowFamilyEventDAO().add(event);

		} catch (SQLException e) {
			log.error("Error adding new flow family event " + event, e);
		}
	}

	public String getEventFlowFamilyUpdatedMessage() {

		return eventFlowFamilyUpdatedMessage;
	}

	public String getEventFlowAddedMessage() {

		return eventFlowAddedMessage;
	}

	public String getEventFlowUpdatedMessage() {

		return eventFlowUpdatedMessage;
	}

	public String getEventFlowDeletedMessage() {

		return eventFlowDeletedMessage;
	}

	public String getEventStepAddedMessage() {

		return eventStepAddedMessage;
	}

	public String getEventStepUpdatedMessage() {

		return eventStepUpdatedMessage;
	}

	public String getEventStepDeletedMessage() {

		return eventStepDeletedMessage;
	}

	public String getEventQueryAddedMessage() {

		return eventQueryAddedMessage;
	}

	public String getEventQueryUpdatedMessage() {

		return eventQueryUpdatedMessage;
	}

	public String getEventQueryDeletedMessage() {

		return eventQueryDeletedMessage;
	}

	public String getEventEvaluatorAddedMessage() {

		return eventEvaluatorAddedMessage;
	}

	public String getEventEvaluatorUpdatedMessage() {

		return eventEvaluatorUpdatedMessage;
	}

	public String getEventEvaluatorDeletedMessage() {

		return eventEvaluatorDeletedMessage;
	}

	public String getEventStatusAddedMessage() {

		return eventStatusAddedMessage;
	}

	public String getEventStatusUpdatedMessage() {

		return eventStatusUpdatedMessage;
	}

	public String getEventStatusDeletedMessage() {

		return eventStatusDeletedMessage;
	}

	public boolean getUseCategories() {

		return useCategories;
	}

	@Override
	public Document createDocument(HttpServletRequest req, URIParser uriParser, User user) {

		Document doc = super.createDocument(req, uriParser, user);

		if (useCategories) {
			XMLUtils.appendNewElement(doc, (Element) doc.getFirstChild(), "UseCategories");
		}

		return doc;
	}

	public boolean requiresTags() {

		return requireTags;
	}

	public StatisticsMode getDefaultStatisticsMode() {

		return defaultStatisticsMode;
	}

	public boolean useFlowTypeIconUpload() {

		return useFlowTypeIconUpload;
	}

	public int getMaxFlowTypeIconWidth() {

		return maxFlowTypeIconWidth;
	}

	public int getMaxFlowTypeIconHeight() {

		return maxFlowTypeIconHeight;
	}

	public SiteProfileHandler getSiteProfileHandler() {

		return siteProfileHandler;
	}

	@WebPublic(toLowerCase = true)
	public ForegroundModuleResponse changeFlowType(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws ModuleConfigurationException, SQLException, AccessDeniedException, IOException {

		Flow requestedFlow = flowCRUD.getRequestedBean(req, null, user, uriParser, GenericCRUD.UPDATE);

		if (requestedFlow == null) {

			return list(req, res, user, uriParser, new ValidationError("UpdateFailedFlowNotFound"));

		} else if (!AccessUtils.checkAccess(user, requestedFlow.getFlowType().getAdminAccessInterface())) {

			throw new AccessDeniedException("User does not have access to flow type " + requestedFlow.getFlowType());
		}

		List<ValidationError> validationErrors = null;

		if (req.getMethod().equalsIgnoreCase("POST")) {

			validationErrors = new ArrayList<ValidationError>();

			Integer flowTypeID = ValidationUtils.validateParameter("flowTypeID", req, true, IntegerPopulator.getPopulator(), validationErrors);
			FlowType flowType = null;

			if (flowTypeID != null) {

				flowType = flowTypeCRUD.getBean(flowTypeID, "UPDATE", null);

				if (flowType == null || !AccessUtils.checkAccess(user, flowType.getAdminAccessInterface())) {

					validationErrors.add(new ValidationError("flowTypeID", ValidationErrorType.InvalidFormat));
				}
			}

			if (validationErrors.isEmpty()) {

				log.info("User " + user + " changing flow type for " + requestedFlow + " from " + requestedFlow.getFlowType() + " to " + flowType);

				TransactionHandler transactionHandler = null;

				try {
					transactionHandler = daoFactory.getFlowDAO().createTransaction();

					HighLevelQuery<Flow> getQuery = new HighLevelQuery<Flow>(Flow.FLOW_TYPE_RELATION);
					getQuery.addParameter(flowFlowFamilyParamFactory.getParameter(requestedFlow.getFlowFamily()));

					List<Flow> flows = daoFactory.getFlowDAO().getAll(getQuery);

					for (Flow flow : flows) {

						flow.setFlowType(flowType);
					}

					HighLevelQuery<Flow> updateQuery = new HighLevelQuery<Flow>(Flow.FLOW_TYPE_RELATION);
					updateQuery.addExcludedFields(Flow.FLOW_FAMILY_RELATION, Flow.CATEGORY_RELATION);
					updateQuery.disableAutoRelations(true);

					daoFactory.getFlowDAO().update(flows, transactionHandler, updateQuery);

					transactionHandler.commit();

					eventHandler.sendEvent(Flow.class, new CRUDEvent<Flow>(Flow.class, CRUDAction.UPDATE, flows), EventTarget.ALL);

					addFlowFamilyEvent(eventChangeFlowType + " " + flowType.getName(), requestedFlow, user);

					redirectToMethod(req, res, "/showflow/" + requestedFlow.getFlowID());

					return null;

				} finally {
					TransactionHandler.autoClose(transactionHandler);
				}
			}
		}

		log.info("User " + user + " requesting change flow type form for flow " + requestedFlow);

		Document doc = createDocument(req, uriParser, user);

		Element changeFlowTypeElement = doc.createElement("ChangeFlowType");
		doc.getDocumentElement().appendChild(changeFlowTypeElement);

		changeFlowTypeElement.appendChild(requestedFlow.toXML(doc));

		List<FlowType> allowedFlowTypes = new ArrayList<FlowType>();

		for (FlowType flowType : getCachedFlowTypes()) {

			if (AccessUtils.checkAccess(user, flowType.getAdminAccessInterface())) {

				allowedFlowTypes.add(flowType);
			}
		}

		XMLUtils.append(doc, changeFlowTypeElement, "AllowedFlowTypes", allowedFlowTypes);

		if (validationErrors != null) {

			XMLUtils.append(doc, changeFlowTypeElement, validationErrors);
		}

		return new SimpleForegroundModuleResponse(doc);
	}

	public String getFileMissing() {

		return fileMissing;
	}

	public String getEventFlowFormAddedMessage() {

		return eventFlowFormAddedMessage;
	}

	public String getEventFlowFormUpdatedMessage() {

		return eventFlowFormUpdatedMessage;
	}

	public String getEventFlowFormDeletedMessage() {

		return eventFlowFormDeletedMessage;
	}

	public Integer getMaxPDFFormFileSize() {

		return maxPDFFormFileSize;
	}

	@Override
	public Icon getFlowTypeIcon(Integer flowTypeID) {

		return flowTypeCacheMap.get(flowTypeID);
	}

	@Override
	public Icon getFlowIcon(Integer flowID) {

		Flow flow = flowCacheMap.get(flowID);
		
		if(flow == null) {
			
			return null;			
		}
		
		FlowType flowType = flowTypeCacheMap.get(flow.getFlowType().getFlowTypeID());
		
		if(flowType.useIconOnAllFlows()) {
			
			return flowType;
		}
		
		return flow;
	}

	public boolean isRequireManagers() {

		return requireManagers;
	}

	@Override
	public List<? extends MenuItemDescriptor> getVisibleMenuItems() {

		if (useBundle) {
			return null;
		}

		return super.getVisibleMenuItems();
	}

	@Override
	public List<? extends BundleDescriptor> getVisibleBundles() {

		if (useBundle) {
			
			SimpleBundleDescriptor bundle = new SimpleBundleDescriptor();
			bundle.setName(moduleDescriptor.getName());
			bundle.setUniqueID(moduleDescriptor.getModuleID().toString());
			bundle.setDescription(moduleDescriptor.getDescription());
			bundle.setItemType(MenuItemType.SECTION);
			bundle.setUrl(getFullAlias());
			bundle.setUrlType(URLType.RELATIVE_FROM_CONTEXTPATH);
			bundle.setAccess(moduleDescriptor);

			ArrayList<MenuItemDescriptor> menuItemDescriptors = new ArrayList<MenuItemDescriptor>();

			menuItemDescriptors.add(getMenuItemDescriptor(bundleListFlows, "", moduleDescriptor));
			menuItemDescriptors.add(getMenuItemDescriptor(bundleAddFlow, "/addflow", moduleDescriptor));
			menuItemDescriptors.add(getMenuItemDescriptor(bundleImportFlow, "/importflow", moduleDescriptor));
			menuItemDescriptors.add(getMenuItemDescriptor(bundleStandardStatuses, "/standardstatuses", this));
			menuItemDescriptors.add(getMenuItemDescriptor(bundleFlowtypes, "/flowtypes", this));

			if (!CollectionUtils.isEmpty(flowListExtensionLinkProviders)) {

				for (ExtensionLinkProvider linkProvider : flowListExtensionLinkProviders) {

					ExtensionLink link;

					try {

						link = linkProvider.getExtensionLink(null);

					} catch (Exception e) {

						log.error("Error getting extension link from provider " + linkProvider, e);

						continue;
					}

					if (link != null) {

						menuItemDescriptors.add(getMenuItemDescriptor(link, (linkProvider.getAccessInterface() != null ? linkProvider.getAccessInterface() : moduleDescriptor)));
					}
				}
			}

			bundle.setMenuItemDescriptors(menuItemDescriptors);

			return Arrays.asList(bundle);
		}

		return super.getVisibleBundles();
	}
	
	private MenuItemDescriptor getMenuItemDescriptor(String name, String alias, AccessInterface accessInterface) {

		SimpleMenuItemDescriptor menuItemDescriptor = new SimpleMenuItemDescriptor();
		menuItemDescriptor.setName(name);
		menuItemDescriptor.setDescription(name);
		menuItemDescriptor.setItemType(MenuItemType.MENUITEM);
		menuItemDescriptor.setUrl(getFullAlias() + alias);
		menuItemDescriptor.setUrlType(URLType.RELATIVE_FROM_CONTEXTPATH);
		menuItemDescriptor.setAccess(accessInterface);
		menuItemDescriptor.setUniqueID(moduleDescriptor.getModuleID().toString());

		return menuItemDescriptor;
	}

	private MenuItemDescriptor getMenuItemDescriptor(ExtensionLink extensionLink, AccessInterface accessInterface) {

		SimpleMenuItemDescriptor menuItemDescriptor = new SimpleMenuItemDescriptor();
		menuItemDescriptor.setName(extensionLink.getName());
		menuItemDescriptor.setDescription(extensionLink.getName());
		menuItemDescriptor.setItemType(MenuItemType.MENUITEM);
		menuItemDescriptor.setUrl(extensionLink.getUrl());
		menuItemDescriptor.setUrlType(URLType.FULL);
		menuItemDescriptor.setAccess(accessInterface);
		menuItemDescriptor.setUniqueID(moduleDescriptor.getModuleID().toString());

		return menuItemDescriptor;
	}

	@Override
	public void systemStarted() throws Exception {
		
		sectionInterface.getMenuCache().moduleUpdated(moduleDescriptor, this);
	}
	
	@WebPublic(toLowerCase = true)
	public ForegroundModuleResponse unPublishFlowFamily(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws ModuleConfigurationException, SQLException, AccessDeniedException, IOException {

		if (!hasPublishAccess(user)) {
			throw new AccessDeniedException("User does not have publish access");
		}
		
		FlowFamily flowFamily = flowFamilyCRUD.getRequestedBean(req, null, user, uriParser, GenericCRUD.UPDATE);

		if (flowFamily == null) {

			return list(req, res, user, uriParser, new ValidationError("UpdateFailedFlowFamilyNotFound"));
		}
		
		List<Flow> flows = getFlowVersions(flowFamily);

		if (!AccessUtils.checkAccess(user, flows.get(0).getFlowType().getAdminAccessInterface())) {
			
			throw new AccessDeniedException("User does not have access to flow type " + flows.get(0).getFlowType());
		}
		
		TransactionHandler transactionHandler = null;
		
		try {
			transactionHandler = daoFactory.getFlowDAO().createTransaction();
			
			List<Flow> unpublishedFlows = new ArrayList<Flow>(flows.size());
			
			for (Flow flow : flows) {
				
				if (flow.isPublished()) {
					
					log.info("User " + user + " unpublishing " + flow);
					
					flow.setPublishDate(null);
					
					HighLevelQuery<Flow> updateQuery = new HighLevelQuery<Flow>();
					updateQuery.disableAutoRelations(true);
					
					daoFactory.getFlowDAO().update(flows, transactionHandler, updateQuery);
					
					unpublishedFlows.add(flow);
				}
			}
			
			if (!CollectionUtils.isEmpty(unpublishedFlows)) {
				
				transactionHandler.commit();
				
				for (Flow flow : unpublishedFlows) {
					addFlowFamilyEvent(getEventFlowUpdatedMessage(), flow, user);
				}
				
				eventHandler.sendEvent(Flow.class, new CRUDEvent<Flow>(Flow.class, CRUDAction.UPDATE, unpublishedFlows), EventTarget.ALL);
			}
			
		} finally {
			TransactionHandler.autoClose(transactionHandler);
		}
		
		redirectToMethod(req, res, "/showflow/" + uriParser.get(3) + "#versions");
		return null;
	}
	
	public synchronized void addFlowBrowserExtensionViewProvider(FlowBrowserExtensionViewProvider flowAdminExtensionProvider) {
		
		if (!flowBrowserExtensionViewProviders.contains(flowAdminExtensionProvider)) {
			
			log.info("Flow browser extension view provider " + flowAdminExtensionProvider + " added");
			
			List<FlowBrowserExtensionViewProvider> tempProviders = new ArrayList<FlowBrowserExtensionViewProvider>(flowBrowserExtensionViewProviders);
			
			tempProviders.add(flowAdminExtensionProvider);
			
			Collections.sort(tempProviders, PriorityComparator.ASC_COMPARATOR);
			
			flowBrowserExtensionViewProviders = new CopyOnWriteArrayList<FlowBrowserExtensionViewProvider>(tempProviders);
		}
		
	}
	
	public synchronized void removeFlowBrowserExtensionViewProvider(FlowBrowserExtensionViewProvider flowAdminExtensionProvider) {
		
		flowBrowserExtensionViewProviders.remove(flowAdminExtensionProvider);
		
		log.info("Flow browser extension view provider " + flowAdminExtensionProvider + " removed");
	}

	
	public String getEventFunctionConfigured() {
	
		return eventFunctionConfigured;
	}

}
