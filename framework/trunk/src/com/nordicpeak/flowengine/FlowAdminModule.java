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
import java.util.ListIterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import javax.sql.rowset.serial.SerialBlob;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;

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

import se.unlogic.cron4jutils.CronStringValidator;
import se.unlogic.emailutils.populators.EmailPopulator;
import se.unlogic.fileuploadutils.MultipartRequest;
import se.unlogic.hierarchy.core.annotations.CheckboxSettingDescriptor;
import se.unlogic.hierarchy.core.annotations.EnumDropDownSettingDescriptor;
import se.unlogic.hierarchy.core.annotations.EventListener;
import se.unlogic.hierarchy.core.annotations.GroupMultiListSettingDescriptor;
import se.unlogic.hierarchy.core.annotations.InstanceManagerDependency;
import se.unlogic.hierarchy.core.annotations.ModuleSetting;
import se.unlogic.hierarchy.core.annotations.TextAreaSettingDescriptor;
import se.unlogic.hierarchy.core.annotations.TextFieldSettingDescriptor;
import se.unlogic.hierarchy.core.annotations.UserMultiListSettingDescriptor;
import se.unlogic.hierarchy.core.annotations.WebPublic;
import se.unlogic.hierarchy.core.annotations.XSLVariable;
import se.unlogic.hierarchy.core.beans.Breadcrumb;
import se.unlogic.hierarchy.core.beans.Group;
import se.unlogic.hierarchy.core.beans.LinkTag;
import se.unlogic.hierarchy.core.beans.ScriptTag;
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
import se.unlogic.hierarchy.core.interfaces.listeners.SystemStartupListener;
import se.unlogic.hierarchy.core.interfaces.menu.BundleDescriptor;
import se.unlogic.hierarchy.core.interfaces.menu.MenuItemDescriptor;
import se.unlogic.hierarchy.core.interfaces.modules.Module;
import se.unlogic.hierarchy.core.interfaces.modules.descriptors.ForegroundModuleDescriptor;
import se.unlogic.hierarchy.core.interfaces.settings.SettingHandler;
import se.unlogic.hierarchy.core.utils.AccessUtils;
import se.unlogic.hierarchy.core.utils.AdvancedCRUDCallback;
import se.unlogic.hierarchy.core.utils.FCKUtils;
import se.unlogic.hierarchy.core.utils.GenericCRUD;
import se.unlogic.hierarchy.core.utils.HierarchyAnnotatedDAOFactory;
import se.unlogic.hierarchy.core.utils.ModuleViewFragmentTransformer;
import se.unlogic.hierarchy.core.utils.UserUtils;
import se.unlogic.hierarchy.core.utils.ViewFragmentModule;
import se.unlogic.hierarchy.core.utils.ViewFragmentUtils;
import se.unlogic.hierarchy.core.utils.crud.MultipartLimitProvider;
import se.unlogic.hierarchy.core.utils.crud.MultipartRequestFilter;
import se.unlogic.hierarchy.core.utils.crud.TransactionRequestFilter;
import se.unlogic.hierarchy.core.utils.extensionlinks.ExtensionLink;
import se.unlogic.hierarchy.core.utils.extensionlinks.ExtensionLinkProvider;
import se.unlogic.hierarchy.core.utils.extensionlinks.ExtensionLinkUtils;
import se.unlogic.hierarchy.core.utils.usergroupadminextensions.GroupAdminExtensionProvider;
import se.unlogic.hierarchy.core.utils.usergroupadminextensions.UserAdminExtensionProvider;
import se.unlogic.hierarchy.core.utils.usergroupadminextensions.UserGroupAdminExtensionHandler;
import se.unlogic.hierarchy.core.utils.usergrouplist.UserGroupListConnector;
import se.unlogic.hierarchy.core.validationerrors.FileSizeLimitExceededValidationError;
import se.unlogic.hierarchy.core.validationerrors.InvalidFileExtensionValidationError;
import se.unlogic.hierarchy.core.validationerrors.RequestSizeLimitExceededValidationError;
import se.unlogic.hierarchy.core.validationerrors.UnableToParseFileValidationError;
import se.unlogic.hierarchy.foregroundmodules.staticcontent.StaticContentModule;
import se.unlogic.openhierarchy.foregroundmodules.siteprofile.interfaces.SiteProfile;
import se.unlogic.openhierarchy.foregroundmodules.siteprofile.interfaces.SiteProfileHandler;
import se.unlogic.standardutils.annotations.RequiredIfSet;
import se.unlogic.standardutils.annotations.SplitOnLineBreak;
import se.unlogic.standardutils.base64.Base64;
import se.unlogic.standardutils.bool.BooleanUtils;
import se.unlogic.standardutils.collections.CaseInsensitiveNameComparator;
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
import se.unlogic.standardutils.fileattachments.FileAttachmentHandler;
import se.unlogic.standardutils.image.ImageUtils;
import se.unlogic.standardutils.io.BinarySizes;
import se.unlogic.standardutils.io.CloseUtils;
import se.unlogic.standardutils.io.FileUtils;
import se.unlogic.standardutils.json.JsonArray;
import se.unlogic.standardutils.json.JsonObject;
import se.unlogic.standardutils.json.JsonUtils;
import se.unlogic.standardutils.numbers.NumberUtils;
import se.unlogic.standardutils.populators.IntegerPopulator;
import se.unlogic.standardutils.populators.PositiveStringIntegerPopulator;
import se.unlogic.standardutils.populators.StringPopulator;
import se.unlogic.standardutils.populators.StringURLPopulator;
import se.unlogic.standardutils.serialization.SerializationUtils;
import se.unlogic.standardutils.streams.StreamUtils;
import se.unlogic.standardutils.string.StringUtils;
import se.unlogic.standardutils.time.TimeUtils;
import se.unlogic.standardutils.validation.PositiveStringIntegerValidator;
import se.unlogic.standardutils.validation.ValidationError;
import se.unlogic.standardutils.validation.ValidationErrorType;
import se.unlogic.standardutils.validation.ValidationException;
import se.unlogic.standardutils.xml.PooledXPathFactory;
import se.unlogic.standardutils.xml.XMLGeneratorDocument;
import se.unlogic.standardutils.xml.XMLParser;
import se.unlogic.standardutils.xml.XMLPopulationUtils;
import se.unlogic.standardutils.xml.XMLUtils;
import se.unlogic.webutils.http.HTTPUtils;
import se.unlogic.webutils.http.RequestUtils;
import se.unlogic.webutils.http.SessionUtils;
import se.unlogic.webutils.http.URIParser;
import se.unlogic.webutils.http.enums.ContentDisposition;
import se.unlogic.webutils.url.URLRewriter;
import se.unlogic.webutils.validation.ValidationUtils;

import com.nordicpeak.flowengine.accesscontrollers.AdminUserFlowInstanceAccessController;
import com.nordicpeak.flowengine.beans.AutoManagerAssignmentRule;
import com.nordicpeak.flowengine.beans.AutoManagerAssignmentStatusRule;
import com.nordicpeak.flowengine.beans.Category;
import com.nordicpeak.flowengine.beans.DefaultInstanceMetadata;
import com.nordicpeak.flowengine.beans.DefaultStandardStatusMapping;
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
import com.nordicpeak.flowengine.beans.MessageTemplate;
import com.nordicpeak.flowengine.beans.QueryDescriptor;
import com.nordicpeak.flowengine.beans.RequestMetadata;
import com.nordicpeak.flowengine.beans.StandardStatus;
import com.nordicpeak.flowengine.beans.StandardStatusGroup;
import com.nordicpeak.flowengine.beans.Status;
import com.nordicpeak.flowengine.beans.Step;
import com.nordicpeak.flowengine.cache.FlowCache;
import com.nordicpeak.flowengine.comparators.FlowAdminExtensionViewProviderComparator;
import com.nordicpeak.flowengine.comparators.FlowVersionComparator;
import com.nordicpeak.flowengine.comparators.QueryDescriptorSortIndexComparator;
import com.nordicpeak.flowengine.comparators.StepSortIndexComparator;
import com.nordicpeak.flowengine.cruds.CategoryCRUD;
import com.nordicpeak.flowengine.cruds.EvaluatorDescriptorCRUD;
import com.nordicpeak.flowengine.cruds.FlowCRUD;
import com.nordicpeak.flowengine.cruds.FlowFamilyCRUD;
import com.nordicpeak.flowengine.cruds.FlowFormCRUD;
import com.nordicpeak.flowengine.cruds.FlowTypeCRUD;
import com.nordicpeak.flowengine.cruds.MessageTemplateCRUD;
import com.nordicpeak.flowengine.cruds.QueryDescriptorCRUD;
import com.nordicpeak.flowengine.cruds.StandardStatusCRUD;
import com.nordicpeak.flowengine.cruds.StandardStatusGroupCRUD;
import com.nordicpeak.flowengine.cruds.StatusCRUD;
import com.nordicpeak.flowengine.cruds.StepCRUD;
import com.nordicpeak.flowengine.enums.EventType;
import com.nordicpeak.flowengine.enums.ManagerAccess;
import com.nordicpeak.flowengine.enums.QueryState;
import com.nordicpeak.flowengine.enums.ShowMode;
import com.nordicpeak.flowengine.enums.StatisticsMode;
import com.nordicpeak.flowengine.events.FlowVersionAdded;
import com.nordicpeak.flowengine.events.NewMutableFlowInstanceManagerCreatedEvent;
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
import com.nordicpeak.flowengine.interfaces.FlowAdminCRUDCallback;
import com.nordicpeak.flowengine.interfaces.FlowAdminExtensionViewProvider;
import com.nordicpeak.flowengine.interfaces.FlowAdminFragmentExtensionViewProvider;
import com.nordicpeak.flowengine.interfaces.FlowAdminShowFlowExtensionLinkProvider;
import com.nordicpeak.flowengine.interfaces.FlowBrowserExtensionViewProvider;
import com.nordicpeak.flowengine.interfaces.FlowFamilyEventHandler;
import com.nordicpeak.flowengine.interfaces.FlowInstanceAccessController;
import com.nordicpeak.flowengine.interfaces.FlowNotificationHandler;
import com.nordicpeak.flowengine.interfaces.FlowProcessCallback;
import com.nordicpeak.flowengine.interfaces.FlowTypeExtensionProvider;
import com.nordicpeak.flowengine.interfaces.Icon;
import com.nordicpeak.flowengine.interfaces.ImmutableFlow;
import com.nordicpeak.flowengine.interfaces.ImmutableFlowInstance;
import com.nordicpeak.flowengine.interfaces.ImmutableStatus;
import com.nordicpeak.flowengine.interfaces.InstanceMetadata;
import com.nordicpeak.flowengine.interfaces.MultiSigningHandler;
import com.nordicpeak.flowengine.interfaces.Query;
import com.nordicpeak.flowengine.interfaces.StatusFormExtensionProvider;
import com.nordicpeak.flowengine.interfaces.XSDExtensionProvider;
import com.nordicpeak.flowengine.listeners.EvaluatorDescriptorElementableListener;
import com.nordicpeak.flowengine.listeners.FlowFormExportElementableListener;
import com.nordicpeak.flowengine.listeners.QueryDescriptorElementableListener;
import com.nordicpeak.flowengine.managers.FlowInstanceManager;
import com.nordicpeak.flowengine.managers.ManagerResponse;
import com.nordicpeak.flowengine.managers.MutableFlowInstanceManager;
import com.nordicpeak.flowengine.managers.MutableFlowInstanceManager.FlowInstanceManagerRegistery;
import com.nordicpeak.flowengine.managers.UserGroupListFlowManagersConnector;
import com.nordicpeak.flowengine.runnables.ExpiredManagerRemover;
import com.nordicpeak.flowengine.runnables.StaleFlowInstancesRemover;
import com.nordicpeak.flowengine.utils.FlowEngineFileAttachmentUtils;
import com.nordicpeak.flowengine.utils.FlowFamilyUtils;
import com.nordicpeak.flowengine.utils.TextTagReplacer;
import com.nordicpeak.flowengine.validationerrors.EvaluatorImportValidationError;
import com.nordicpeak.flowengine.validationerrors.EvaluatorTypeNotFoundValidationError;
import com.nordicpeak.flowengine.validationerrors.NoQueryDescriptorSortindexValidationError;
import com.nordicpeak.flowengine.validationerrors.NoStepSortindexValidationError;
import com.nordicpeak.flowengine.validationerrors.QueryExportValidationError;
import com.nordicpeak.flowengine.validationerrors.QueryImportValidationError;
import com.nordicpeak.flowengine.validationerrors.QueryTypeNotAllowedInFlowTypeValidationError;
import com.nordicpeak.flowengine.validationerrors.QueryTypeNotFoundValidationError;

import it.sauronsoftware.cron4j.Scheduler;

public class FlowAdminModule extends BaseFlowBrowserModule implements AdvancedCRUDCallback<User>, AccessInterface, FlowProcessCallback, FlowFamilyEventHandler, MultipartLimitProvider, SystemStartupListener, FlowAdminCRUDCallback, UserAdminExtensionProvider, GroupAdminExtensionProvider, ViewFragmentModule<ForegroundModuleDescriptor> {

	//@formatter:off
	private static final Field[] FLOW_FAMILY_CACHE_RELATIONS = {
			FlowFamily.ALIASES_RELATION,
			FlowFamily.MANAGER_USERS_RELATION,
			FlowFamily.MANAGER_GROUPS_RELATION,
			FlowFamily.AUTO_MANAGER_ASSIGNMENT_RULES_RELATION,
			FlowFamily.AUTO_MANAGER_ASSIGNMENT_ALWAYS_USERS_RELATION,
			FlowFamily.AUTO_MANAGER_ASSIGNMENT_ALWAYS_GROUPS_RELATION,
			FlowFamily.AUTO_MANAGER_ASSIGNMENT_NO_MATCH_USERS_RELATION,
			FlowFamily.AUTO_MANAGER_ASSIGNMENT_NO_MATCH_GROUPS_RELATION,
			FlowFamily.AUTO_MANAGER_ASSIGNMENT_STATUS_RULES_RELATION,
			FlowFamily.MESSAGE_TEMPLATES_RELATION,
	};
	
	private static final Field[] FLOW_CACHE_RELATIONS = {
			Flow.CHECKS_RELATION,
			Flow.CATEGORY_RELATION,
			Flow.DEFAULT_FLOW_STATE_MAPPINGS_RELATION,
			Flow.FLOW_FORMS_RELATION,
			Flow.FLOW_TYPE_RELATION,
			Flow.FLOW_FAMILY_RELATION,
			Flow.STEPS_RELATION,
			Flow.STATUSES_RELATION,
			Flow.TAGS_RELATION,
			Flow.OVERVIEW_ATTRIBUTES_RELATION,
			
			Status.MANAGER_GROUPS_RELATION,
			Status.MANAGER_USERS_RELATION,
			
			Step.QUERY_DESCRIPTORS_RELATION,
			
			QueryDescriptor.EVALUATOR_DESCRIPTORS_RELATION,
			
			DefaultStatusMapping.FLOW_STATE_RELATION,
			
			FlowFamily.ALIASES_RELATION,
			FlowFamily.MANAGER_USERS_RELATION,
			FlowFamily.MANAGER_GROUPS_RELATION,
			FlowFamily.AUTO_MANAGER_ASSIGNMENT_RULES_RELATION,
			FlowFamily.AUTO_MANAGER_ASSIGNMENT_ALWAYS_USERS_RELATION,
			FlowFamily.AUTO_MANAGER_ASSIGNMENT_ALWAYS_GROUPS_RELATION,
			FlowFamily.AUTO_MANAGER_ASSIGNMENT_NO_MATCH_USERS_RELATION,
			FlowFamily.AUTO_MANAGER_ASSIGNMENT_NO_MATCH_GROUPS_RELATION,
			FlowFamily.AUTO_MANAGER_ASSIGNMENT_STATUS_RULES_RELATION,
			FlowFamily.MESSAGE_TEMPLATES_RELATION,
			
			FlowType.CATEGORIES_RELATION,
			FlowType.ALLOWED_ADMIN_USERS_RELATION,
			FlowType.ALLOWED_ADMIN_GROUPS_RELATION,
			FlowType.ALLOWED_QUERIES_RELATION
	};
	//@formatter:on
	
	public static final Field[] CACHED_FLOW_CACHE_RELATIONS = {Flow.FLOW_TYPE_RELATION, FlowType.CATEGORIES_RELATION, Flow.CATEGORY_RELATION, Flow.FLOW_FAMILY_RELATION};

	public static final ValidationError FLOW_HAS_NO_CONTENT_VALIDATION_ERROR = new ValidationError("FlowHasNoContent");
	public static final ValidationError FLOW_HAS_NO_STEPS_AND_SKIP_OVERVIEW_IS_SET_VALIDATION_ERROR = new ValidationError("FlowHasNoStepsAndOverviewSkipIsSet");
	public static final ValidationError MAY_NOT_REMOVE_FLOW_FORM_IF_NO_STEPS_VALIDATION_ERROR = new ValidationError("MayNotRemoveFlowFormIfNoSteps");
	public static final ValidationError MAY_NOT_ADD_FLOW_FORM_IF_SKIP_OVERVIEW_IS_SET_VALIDATION_ERROR = new ValidationError("MayNotAddFlowFormIfOverviewSkipIsSet");
	public static final ValidationError MAY_NOT_SET_SKIP_OVERVIEW_IF_FLOW_FORM_IS_SET_VALIDATION_ERROR = new ValidationError("MayNotSetOverviewIfFlowFormIsSet");
	public static final ValidationError NO_MANAGERS_VALIDATION_ERROR = new ValidationError("NoManagersSet");
	public static final ValidationError EXTERNAL_MESSAGE_AND_REQUIRED_SIGNING_MUTUAL_EXCLUSIVE_VALIDATION_ERROR = new ValidationError("ExternalMessageAndRequiredSigningMutualExclusiveError");

	protected static final RelationQuery ADD_NEW_FLOW_AND_FAMILY_RELATION_QUERY = new RelationQuery(Flow.FLOW_FORMS_RELATION, Flow.STATUSES_RELATION, Flow.DEFAULT_FLOW_STATE_MAPPINGS_RELATION, Flow.STEPS_RELATION, Step.QUERY_DESCRIPTORS_RELATION, QueryDescriptor.EVALUATOR_DESCRIPTORS_RELATION, Flow.CHECKS_RELATION, Flow.TAGS_RELATION, Flow.OVERVIEW_ATTRIBUTES_RELATION, Status.MANAGER_USERS_RELATION, Status.MANAGER_GROUPS_RELATION, Flow.FLOW_FAMILY_RELATION, FlowFamily.MESSAGE_TEMPLATES_RELATION);
	protected static final RelationQuery ADD_NEW_FLOW_VERSION_RELATION_QUERY =    new RelationQuery(Flow.FLOW_FORMS_RELATION, Flow.STATUSES_RELATION, Flow.DEFAULT_FLOW_STATE_MAPPINGS_RELATION, Flow.STEPS_RELATION, Step.QUERY_DESCRIPTORS_RELATION, QueryDescriptor.EVALUATOR_DESCRIPTORS_RELATION, Flow.CHECKS_RELATION, Flow.TAGS_RELATION, Flow.OVERVIEW_ATTRIBUTES_RELATION, Status.MANAGER_USERS_RELATION, Status.MANAGER_GROUPS_RELATION);

	public static final List<Field> LIST_FLOWS_IGNORED_FIELDS = Arrays.asList(FlowType.ALLOWED_ADMIN_GROUPS_RELATION, FlowType.ALLOWED_QUERIES_RELATION, FlowType.ALLOWED_ADMIN_USERS_RELATION, FlowType.CATEGORIES_RELATION, Flow.STATUSES_RELATION, Flow.DEFAULT_FLOW_STATE_MAPPINGS_RELATION, Flow.STEPS_RELATION);

	private static final StepSortIndexComparator STEP_COMPARATOR = new StepSortIndexComparator();
	private static final QueryDescriptorSortIndexComparator QUERY_DESCRIPTOR_COMPARATOR = new QueryDescriptorSortIndexComparator();
	private static final FlowVersionComparator ASC_FLOW_VERSION_COMPARATOR = new FlowVersionComparator(Order.ASC);
	private static final FlowVersionComparator DESC_FLOW_VERSION_COMPARATOR = new FlowVersionComparator(Order.DESC);

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
	private String eventStatusesReplacedMessage = "eventStatusesReplacedMessage";

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
	private String eventEvaluatorSortMessage = "eventEvaluatorSortMessage";
	
	@XSLVariable(prefix = "java.")
	private String eventFunctionConfigured = "eventFunctionConfigured";
	
	@XSLVariable(prefix = "java.")
	private String eventFlowInstanceManagerExpired = "eventFlowInstanceManagerExpired";
	
	@XSLVariable(prefix = "java.")
	private String eventUpdateAutoManagerAssignment = "eventUpdateAutoManagerAssignment";
	
	@XSLVariable(prefix = "java.")
	private String eventMessageTemplatesAddedMessage = "message templates added";
	
	@XSLVariable(prefix = "java.")
	private String eventMessageTemplatesUpdatedMessage = "message templates updated";
	
	@XSLVariable(prefix = "java.")
	private String eventMessageTemplatesDeletedMessage = "message templates deleted";

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
	
	@XSLVariable(prefix = "java.")
	protected String hiddenQueryText = "(hidden)";

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
	@CheckboxSettingDescriptor(name = "Show expiry settings modal when adding manager", description = "Controls if the expiry settings modal is show when adding new managers")
	protected boolean showManagerModalOnAdd = false;

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
	
	@ModuleSetting
	@SplitOnLineBreak
	@TextAreaSettingDescriptor(name = "Flow form file extensions", description = "Allowed file extensions for files uploaded as flow forms")
	protected List<String> allowedFlowFormFileExtensions = Arrays.asList(new String[]{"pdf", "xls", "xlsx"});

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
	@ModuleSetting
	@TextFieldSettingDescriptor(name = "Default login help link name", description = "Name of the login help link.")
	private String defaultLoginHelpLinkName;
	
	@ModuleSetting(allowsNull = true)
	@TextFieldSettingDescriptor(name = "Default login help url", description = "URL to redirect the user to for login help.", formatValidator = StringURLPopulator.class)
	private String defaultLoginHelpLinkURL;
	
	@ModuleSetting
	@TextFieldSettingDescriptor(name = "Check for expiring managers interval", description = "How often this module should check for expiring flow managers (specified in crontab format)", required = true, formatValidator = CronStringValidator.class)
	private String managersUpdateInterval = "0 0 * * *";
	
	@ModuleSetting
	@TextFieldSettingDescriptor(name = "Check for stale flow instances interval", description = "How often this module should check for expiring flow managers (specified in crontab format)", required = true, formatValidator = CronStringValidator.class)
	private String removeStaleFlowInstancesInterval = "0 0 * * *";
	
	@ModuleSetting
	@CheckboxSettingDescriptor(name = "Block foreign IDs", description = "Block users logged in with foreign IDs from using flows unless explicity allowed in the flow family")
	protected boolean blockForeignIDs = false;
	
	@ModuleSetting
	@RequiredIfSet(paramNames = "blockForeignIDs")
	@SplitOnLineBreak
	@TextAreaSettingDescriptor(name = "Foreign ID attribute", description = "Attribute that is set when user is logged in with a foreign ID")
	protected List<String> foreignIDattributes;
	
	@ModuleSetting
	@CheckboxSettingDescriptor(name = "Enable fragment XML debug", description = "Enables debugging of fragment XML")
	private boolean debugFragmentXML;
	
	@InstanceManagerDependency(required = true)
	protected SiteProfileHandler siteProfileHandler;

	@InstanceManagerDependency
	protected FlowNotificationHandler notificationHandler;

	@InstanceManagerDependency
	protected MultiSigningHandler multiSigningHandler;
	
	@InstanceManagerDependency(required = true)
	protected StaticContentModule staticContentModule;
	
	@InstanceManagerDependency
	protected FileAttachmentHandler fileAttachmentHandler;
	
	protected AnnotatedDAO<MessageTemplate> messageTemplateDAO;
	
	private FlowFamilyCRUD flowFamilyCRUD;
	private FlowCRUD flowCRUD;
	private StepCRUD stepCRUD;
	private QueryDescriptorCRUD queryDescriptorCRUD;
	private EvaluatorDescriptorCRUD evaluatorDescriptorCRUD;
	private StatusCRUD statusCRUD;
	private StandardStatusCRUD standardStatusCRUD;
	private StandardStatusGroupCRUD standardStatusGroupCRUD;
	private FlowTypeCRUD flowTypeCRUD;
	private CategoryCRUD categoryCRUD;
	private FlowFormCRUD flowFormCRUD;
	private MessageTemplateCRUD messageTemplateCRUD;

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
	
	protected AdvancedAnnotatedDAOWrapper<StandardStatusGroup, Integer> standardStatusGroupDAOWrapper;
	
	protected OrderByCriteria<Flow> flowVersionOrderByCriteria;

	private LinkedHashMap<Integer, FlowType> flowTypeCacheMap;
	
	private FlowCache flowCache;

	protected UserGroupListConnector userGroupListConnector;
	protected UserGroupListConnector unrestrictedUserGroupListConnector;
	protected UserGroupListFlowManagersConnector userGroupListFlowManagersConnector;

	protected CopyOnWriteArrayList<FlowAdminExtensionViewProvider> extensionViewProviders = new CopyOnWriteArrayList<FlowAdminExtensionViewProvider>();
	protected CopyOnWriteArrayList<FlowAdminFragmentExtensionViewProvider> fragmentExtensionViewProviders = new CopyOnWriteArrayList<FlowAdminFragmentExtensionViewProvider>();
	protected CopyOnWriteArrayList<FlowTypeExtensionProvider> flowTypeExtensionProviders = new CopyOnWriteArrayList<FlowTypeExtensionProvider>();

	protected CopyOnWriteArrayList<ExtensionLinkProvider> flowListExtensionLinkProviders = new CopyOnWriteArrayList<ExtensionLinkProvider>();
	protected CopyOnWriteArrayList<FlowAdminShowFlowExtensionLinkProvider> flowShowExtensionLinkProviders = new CopyOnWriteArrayList<FlowAdminShowFlowExtensionLinkProvider>();
	
	protected CopyOnWriteArrayList<StatusFormExtensionProvider> statusFormExtensionProviders = new CopyOnWriteArrayList<>();
	
	protected CopyOnWriteArrayList<FlowBrowserExtensionViewProvider> flowBrowserExtensionViewProviders = new CopyOnWriteArrayList<FlowBrowserExtensionViewProvider>();
	
	protected CopyOnWriteArrayList<XSDExtensionProvider> xsdExtensionProviders = new CopyOnWriteArrayList<XSDExtensionProvider>();

	private Scheduler scheduler;
	private String updateManagersScheduleID;
	private String removeStaleFlowInstancesScheduleID;
	
	private ModuleViewFragmentTransformer<ForegroundModuleDescriptor> viewFragmentTransformer;
	
	private UserGroupAdminExtensionHandler userGroupAdminExtensionHandler;
	
	@Override
	public void init(ForegroundModuleDescriptor moduleDescriptor, SectionInterface sectionInterface, DataSource dataSource) throws Exception {
		
		viewFragmentTransformer = new ModuleViewFragmentTransformer<>(sectionInterface.getForegroundModuleXSLTCache(), this, sectionInterface.getSystemInterface().getEncoding());

		super.init(moduleDescriptor, sectionInterface, dataSource);

		cacheFlows();
		cacheFlowTypes();

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
	public void update(ForegroundModuleDescriptor descriptor, DataSource dataSource) throws Exception {
		
		super.update(descriptor, dataSource);
		
		scheduler.reschedule(updateManagersScheduleID, managersUpdateInterval);
		scheduler.reschedule(removeStaleFlowInstancesScheduleID, removeStaleFlowInstancesInterval);
	}

	@Override
	public void unload() throws Exception {
		
		stopScheduler();

		systemInterface.getInstanceHandler().removeInstance(FlowAdminModule.class, this);

		extensionViewProviders.clear();
		fragmentExtensionViewProviders.clear();
		flowTypeExtensionProviders.clear();
		flowBrowserExtensionViewProviders.clear();

		flowListExtensionLinkProviders.clear();

		flowShowExtensionLinkProviders.clear();
		
		statusFormExtensionProviders.clear();
		
		if (userGroupAdminExtensionHandler != null) {
			
			setUserAdminExtensionHandler(null);
		}
		
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
		flowDAOWrapper.getAddQuery().addRelations(Flow.FLOW_FAMILY_RELATION, Flow.STATUSES_RELATION, Flow.OVERVIEW_ATTRIBUTES_RELATION, Status.DEFAULT_STATUS_MAPPINGS_RELATION, Flow.TAGS_RELATION, Flow.CHECKS_RELATION, FlowFamily.ALIASES_RELATION);
		flowDAOWrapper.getUpdateQuery().addRelations(Flow.FLOW_FAMILY_RELATION, Flow.TAGS_RELATION, Flow.CHECKS_RELATION, Flow.OVERVIEW_ATTRIBUTES_RELATION, FlowFamily.ALIASES_RELATION);

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

		statusCRUD = new StatusCRUD(statusDAOWrapper, this, statusFormExtensionProviders);

		AnnotatedDAOWrapper<StandardStatus, Integer> standardStatusDAOWrapper = daoFactory.getStandardStatusDAO().getWrapper("statusID", Integer.class);
		standardStatusDAOWrapper.addRelations(StandardStatus.DEFAULT_STANDARD_STATUS_MAPPINGS_RELATION, StandardStatus.STANDARD_STATUS_GROUP_RELATION);
		standardStatusDAOWrapper.setUseRelationsOnGet(true);
		
		standardStatusGroupDAOWrapper = daoFactory.getStandardStatusGroupDAO().getAdvancedWrapper("statusGroupID", Integer.class);
		standardStatusGroupDAOWrapper.getGetQuery().addRelations(StandardStatusGroup.STANDARD_STATUSES_RELATION);

		standardStatusCRUD = new StandardStatusCRUD(standardStatusDAOWrapper, standardStatusGroupDAOWrapper, this);
		standardStatusGroupCRUD = new StandardStatusGroupCRUD(standardStatusGroupDAOWrapper, this);

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
		
		HierarchyAnnotatedDAOFactory normalDAOFactory = new HierarchyAnnotatedDAOFactory(dataSource, systemInterface.getUserHandler(), systemInterface.getGroupHandler(), false, false, false);
		
		messageTemplateDAO = normalDAOFactory.getDAO(MessageTemplate.class);
		AnnotatedDAOWrapper<MessageTemplate, Integer> messageTemplateDAOWrapper = messageTemplateDAO.getWrapper(Integer.class);
		messageTemplateDAOWrapper.setUseRelationsOnGet(true);
		messageTemplateDAOWrapper.addRelations(MessageTemplate.FLOW_FAMILY_RELATION);
		
		messageTemplateCRUD = new MessageTemplateCRUD(messageTemplateDAOWrapper, this);
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
		
		viewFragmentTransformer.setDebugXML(debugFragmentXML);
	}

	protected synchronized void cacheFlowTypes() throws SQLException {

		long startTime = System.currentTimeMillis();

		HighLevelQuery<FlowType> query = new HighLevelQuery<FlowType>(
		//@formatter:off
				FlowType.CATEGORIES_RELATION,
				FlowType.ALLOWED_USERS_RELATION,
				FlowType.ALLOWED_GROUPS_RELATION,
				FlowType.ALLOWED_ADMIN_USERS_RELATION,
				FlowType.ALLOWED_ADMIN_GROUPS_RELATION,
				FlowType.ALLOWED_QUERIES_RELATION
		//@formatter:on
		);
		query.addCachedRelation(FlowType.CATEGORIES_RELATION);
		
		List<FlowType> flowTypes = daoFactory.getFlowTypeDAO().getAll(query);

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
		//System.out.println("Cached " + CollectionUtils.getSize(flowTypes) + " flow types in " + TimeUtils.millisecondsToString(System.currentTimeMillis() - startTime) + " ms");
	}

	public synchronized void cacheFlows() throws SQLException {

		TransactionHandler transactionHandler = null;

		try {

			transactionHandler = new TransactionHandler(dataSource);

			long startTime = System.currentTimeMillis();
			
			HighLevelQuery<Flow> query = new HighLevelQuery<Flow>(FLOW_CACHE_RELATIONS);
			query.addCachedRelations(CACHED_FLOW_CACHE_RELATIONS);

			List<Flow> flows = daoFactory.getFlowDAO().getAll(query, transactionHandler);

			if (flows == null) {

				this.flowCache = new FlowCache(new LinkedHashMap<Integer, Flow>(0), new HashMap<Integer, FlowFamily>(0));

			} else {

				LinkedHashMap<Integer, Flow> tempFlowCacheMap = new LinkedHashMap<Integer, Flow>(flows.size());
				HashMap<Integer, FlowFamily> tempFlowFamilyMap = new HashMap<Integer, FlowFamily>();

				for (Flow flow : flows) {

					setCachedFlowDetails(flow,transactionHandler);

					tempFlowCacheMap.put(flow.getFlowID(), flow);
					tempFlowFamilyMap.put(flow.getFlowFamily().getFlowFamilyID(), flow.getFlowFamily());
				}

				this.flowCache = new FlowCache(tempFlowCacheMap, tempFlowFamilyMap);
			}

			log.info("Cached " + flowCache.getFlowCacheMap().size() + " flows from " + flowCache.getFlowFamilyCacheMap().size() + " flow families in " + TimeUtils.millisecondsToString(System.currentTimeMillis() - startTime) + " ms");
			//System.out.println("Cached " + flowCache.getFlowCacheMap().size() + " flows from " + flowCache.getFlowFamilyCacheMap().size() + " flow families in " + TimeUtils.millisecondsToString(System.currentTimeMillis() - startTime) + " ms");

		} finally {

			TransactionHandler.autoClose(transactionHandler);
		}
	}
	
	private synchronized void cacheFlowFamilies(List<Integer> flowFamilyIDs) throws SQLException {

		long startTime = System.currentTimeMillis();
		
		HighLevelQuery<FlowFamily> query = new HighLevelQuery<FlowFamily>(FLOW_FAMILY_CACHE_RELATIONS);
		
		query.addParameter(flowFamiliyIDParamFactory.getWhereInParameter(flowFamilyIDs));
		
		List<FlowFamily> flowFamilies = this.daoFactory.getFlowFamilyDAO().getAll(query);
		
		if (flowFamilies == null) {

			log.error("Flow family ID's " + flowFamilyIDs + " not found in DB, unbable to cache flow families");

		} else {
	
			LinkedHashMap<Integer, Flow> tempFlowCacheMap = new LinkedHashMap<Integer, Flow>(flowCache.getFlowCacheMap());
			HashMap<Integer, FlowFamily> tempFlowFamilyMap = new HashMap<Integer, FlowFamily>(flowCache.getFlowFamilyCacheMap());

			for (FlowFamily flowFamily : flowFamilies) {

				tempFlowFamilyMap.put(flowFamily.getFlowFamilyID(), flowFamily);
				
				//Update flow family of cached flows
				for(Flow flow : tempFlowCacheMap.values()) {
					
					if(flow.getFlowFamily().equals(flowFamily)) {
						
						flow.setFlowFamily(flowFamily);
					}
				}
			}

			this.flowCache = new FlowCache(tempFlowCacheMap, tempFlowFamilyMap);
		}
		
		log.info("Cached " + flowFamilyIDs.size() + " flow families in " + TimeUtils.millisecondsToString(System.currentTimeMillis() - startTime) + " ms");
		//System.out.println("Cached " + flowFamilyIDs.size() + " flow families in " + TimeUtils.millisecondsToString(System.currentTimeMillis() - startTime) + " ms");
	}
	
	private synchronized void deleteFlowFamiliesFromCache(List<FlowFamily> flowFamilies) {

		LinkedHashMap<Integer, Flow> tempFlowCacheMap = new LinkedHashMap<Integer, Flow>(flowCache.getFlowCacheMap());
		HashMap<Integer, FlowFamily> tempFlowFamilyMap = new HashMap<Integer, FlowFamily>(flowCache.getFlowFamilyCacheMap());
		
		for(FlowFamily flowFamily : flowFamilies) {
			
			if(tempFlowFamilyMap.remove(flowFamily.getFlowFamilyID()) != null) {
				
				log.info("Removed flow family " + flowFamily + " from cache");
				
			}else {
				
				log.warn("Flow family " + flowFamily + " not found in cache");
			}
		}
		
		this.flowCache = new FlowCache(tempFlowCacheMap, tempFlowFamilyMap);
	}
	
	private synchronized void cacheFlows(List<Integer> flowIDs) throws SQLException {

		TransactionHandler transactionHandler = null;

		try {

			transactionHandler = new TransactionHandler(dataSource);

			long startTime = System.currentTimeMillis();
			
			HighLevelQuery<Flow> query = new HighLevelQuery<Flow>(FLOW_CACHE_RELATIONS);
			query.addCachedRelations(CACHED_FLOW_CACHE_RELATIONS);
			query.addParameter(flowIDParamFactory.getWhereInParameter(flowIDs));

			List<Flow> flows = daoFactory.getFlowDAO().getAll(query, transactionHandler);

			if (flows == null) {

				log.error("Flow ID's " + flowIDs + " not found in DB, unbable to cache flows");

			} else {
		
				LinkedHashMap<Integer, Flow> tempFlowCacheMap = new LinkedHashMap<Integer, Flow>(flowCache.getFlowCacheMap());
				HashMap<Integer, FlowFamily> tempFlowFamilyMap = new HashMap<Integer, FlowFamily>(flowCache.getFlowFamilyCacheMap());

				for (Flow flow : flows) {

					setCachedFlowDetails(flow,transactionHandler);

					if(flow.isLatestVersion()) {
						
						for(Flow cachedFlow : tempFlowCacheMap.values()) {
							
							if(cachedFlow.getFlowFamily().equals(flow.getFlowFamily()) && cachedFlow.isLatestVersion()) {
								
								cachedFlow.setLatestVersion(false);
							}
						}
					}
					
					tempFlowCacheMap.put(flow.getFlowID(), flow);
					tempFlowFamilyMap.put(flow.getFlowFamily().getFlowFamilyID(), flow.getFlowFamily());
				}

				CollectionUtils.sortMapByValue(tempFlowCacheMap, CaseInsensitiveNameComparator.getInstance());
				
				this.flowCache = new FlowCache(tempFlowCacheMap, tempFlowFamilyMap);
			}

			log.info("Cached " + flowIDs.size() + " flows in " + TimeUtils.millisecondsToString(System.currentTimeMillis() - startTime) + " ms");

		} finally {

			TransactionHandler.autoClose(transactionHandler);
		}
		
	}
	
	private synchronized void deleteFlowsFromCache(List<Flow> flows) {

		LinkedHashMap<Integer, Flow> tempFlowCacheMap = new LinkedHashMap<Integer, Flow>(flowCache.getFlowCacheMap());
		HashMap<Integer, FlowFamily> tempFlowFamilyMap = new HashMap<Integer, FlowFamily>(flowCache.getFlowFamilyCacheMap());
		
		Set<FlowFamily> familiesWithoutLatestVersion = null;
		
		for(Flow flow : flows) {
			
			Flow deletedFlow;
			
			if((deletedFlow = tempFlowCacheMap.remove(flow.getFlowID())) != null) {
				
				log.info("Removed flow " + flow + " from cache");
				
				if(deletedFlow.isLatestVersion()) {
					
					familiesWithoutLatestVersion = CollectionUtils.addAndInstantiateIfNeeded(familiesWithoutLatestVersion, deletedFlow.getFlowFamily());
				}
				
			}else {
				
				log.warn("Flow " + flow + " not found in cache");
			}
		}
		
		if(familiesWithoutLatestVersion != null && !tempFlowCacheMap.isEmpty()) {
			
			for(FlowFamily flowFamily : familiesWithoutLatestVersion) {
				
				Flow latestVersion = null;
				
				for(Flow flow : tempFlowCacheMap.values()) {
					
					if(!flow.getFlowFamily().equals(flowFamily)) {
						
						continue;
					}
					
					if(latestVersion == null || latestVersion.getVersion() < flow.getVersion()) {
						
						latestVersion = flow;
					}
				}
				
				if(latestVersion != null) {
					
					latestVersion.setLatestVersion(true);
				}
			}
		}
		
		this.flowCache = new FlowCache(tempFlowCacheMap, tempFlowFamilyMap);
	}
	
	private void setCachedFlowDetails(Flow flow, TransactionHandler transactionHandler) throws SQLException {

		flow.setFlowInstanceCount(getFlowInstanceCount(flow, transactionHandler));
		flow.setFlowSubmittedInstanceCount(getFlowSubmittedInstanceCount(flow, transactionHandler));
		flow.setLatestVersion(isLatestVersion(flow, transactionHandler));

		setStatusFlowInstanceCount(flow, transactionHandler);
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

		if (hasAnyFlowTypeAccess(user)) {

			XMLGeneratorDocument generatorDocument = new XMLGeneratorDocument(doc);
			generatorDocument.setIgnoredFields(LIST_FLOWS_IGNORED_FIELDS);

			listFlowsElement.appendChild(doc.createElement("AddAccess"));

			Collection<Flow> flows = this.flowCache.getFlowCacheMap().values();

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
		
		appendAdditionalListInformation(doc, listFlowsElement);

		if (validationErrors != null) {

			XMLUtils.append(doc, listFlowsElement, validationErrors);
		}

		ExtensionLinkUtils.appendExtensionLinks(this.flowListExtensionLinkProviders, user, req, doc, listFlowsElement);

		return new SimpleForegroundModuleResponse(doc, this.getDefaultBreadcrumb());
	}

	protected void appendAdditionalListInformation(Document doc, Element listFlowsElement) {
		
		return;
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

	public boolean hasAnyFlowTypeAccess(User user) {

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
	
	@Override
	public ForegroundModuleResponse showFlow(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser, List<ValidationError> validationErrors) throws Exception {

		return flowCRUD.show(req, res, user, uriParser, validationErrors);
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

		if(!HTTPUtils.isPost(req)) {
			
			throw new AccessDeniedException("Delete flow family requests using method " + req.getMethod() + " are not allowed.");
		}
		
		FlowFamily flowFamily;

		if (uriParser.size() != 3 || !NumberUtils.isInt(uriParser.get(2)) || (flowFamily = flowCache.getFlowFamilyCacheMap().get(NumberUtils.toInt(uriParser.get(2)))) == null) {

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
			
			if (flow.getFlowFamily().getMessageTemplates() != null) {
				
				ArrayList<MessageTemplate> messageTemplates = new ArrayList<MessageTemplate>(flow.getFlowFamily().getMessageTemplates().size());
				
				for (MessageTemplate messageTemplate : flow.getFlowFamily().getMessageTemplates()) {
					
					MessageTemplate messageTemplateCopy = SerializationUtils.cloneSerializable(messageTemplate);
					messageTemplateCopy.setTemplateID(null);
					
					messageTemplates.add(messageTemplateCopy);
				}
				
				flowFamily.setMessageTemplates(messageTemplates);
			}
			
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
		
		boolean familyUpdated = false;

		try {
			transactionHandler = daoFactory.getFlowDAO().createTransaction();

			if (newFamily) {

				daoFactory.getFlowDAO().add(flowCopy, transactionHandler, ADD_NEW_FLOW_AND_FAMILY_RELATION_QUERY);

			} else {

				Integer version = getNextVersion(flow.getFlowFamily().getFlowFamilyID(), transactionHandler);

				if (version == null) {

					throw new RuntimeException("Flow family " + flow.getFlowFamily() + " not found in database.");
				}
				
				if (version.equals(flow.getVersion())) {

					log.error("Duplicate version created from getNextVersion. Original " + flow + ", new version " + version);
				}

				flowCopy.getFlowFamily().setVersionCount(version);
				flowCopy.setVersion(version);

				daoFactory.getFlowFamilyDAO().update(flowCopy.getFlowFamily(), transactionHandler, null);
				daoFactory.getFlowDAO().add(flowCopy, transactionHandler, ADD_NEW_FLOW_VERSION_RELATION_QUERY);
				
				familyUpdated = true;
			}

			if (flow.getSteps() != null) {

				int stepIndex = 0;

				while (stepIndex < flowCopy.getSteps().size()) {

					Step step = flow.getSteps().get(stepIndex);

					if (step.getQueryDescriptors() != null) {

						int queryIndex = 0;

						while (queryIndex < step.getQueryDescriptors().size()) {

							QueryDescriptor queryDescriptor = step.getQueryDescriptors().get(queryIndex);

							queryHandler.copyQuery(queryDescriptor, flowCopy.getSteps().get(stepIndex).getQueryDescriptors().get(queryIndex), transactionHandler, statusConversionMap);

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
		eventHandler.sendEvent(Flow.class, new FlowVersionAdded(flow, statusConversionMap), EventTarget.ALL);
		
		if (familyUpdated) {
			eventHandler.sendEvent(FlowFamily.class, new CRUDEvent<FlowFamily>(CRUDAction.ADD, flowCopy.getFlowFamily()), EventTarget.ALL);
		}

		addFlowFamilyEvent(eventCopyFlowMessage + " " + flow.getVersion() + " \"" + flow.getName() + "\"", flowCopy, user);

		redirectToMethod(req, res, "/showflow/" + flowCopy.getFlowID());

		return null;
	}

	@WebPublic(toLowerCase = true)
	public ForegroundModuleResponse updateIcon(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws ModuleConfigurationException, SQLException, AccessDeniedException, IOException {

		Flow flow = flowCRUD.getRequestedBean(req, null, user, uriParser, GenericCRUD.UPDATE);

		if (flow == null) {

			return list(req, res, user, uriParser, new ValidationError("UpdateFailedFlowNotFound"));

		} else if (!hasFlowAccess(user, flow)) {

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

						} catch (Exception e) {

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

		} else if (!hasFlowAccess(user, flow)) {

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
	public ForegroundModuleResponse sortEvaluators(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws ModuleConfigurationException, SQLException, AccessDeniedException, IOException {

		Integer queryID = uriParser.getInt(2);
		
		if (queryID == null) {
			
			return list(req, res, user, uriParser, new ValidationError("QueryNotFound"));
		}
		
		QueryDescriptor queryDescriptor = getQueryDescriptor(queryID);
		
		if (queryDescriptor == null) {
			
			return list(req, res, user, uriParser, new ValidationError("QueryNotFound"));
		}
		
		Flow flow = queryDescriptor.getStep().getFlow();

		if (!hasFlowAccess(user, flow)) {

			throw new AccessDeniedException("User does not have access to flow type " + flow.getFlowType());
		}
		
		if (queryDescriptor.getEvaluatorDescriptors() == null) {
			
			log.info("User " + user + " requested sort evaluators form for query " + queryDescriptor + " in flow " + flow + " which has no evaluators.");

			redirectToMethod(req, res, "/showflow/" + flow.getFlowID());
			return null;
		}

		List<ValidationError> validationErrors = null;
		
		if (req.getMethod().equalsIgnoreCase("POST")) {
			
			validationErrors = new ArrayList<ValidationError>();

			for (EvaluatorDescriptor evaluatorDescription : queryDescriptor.getEvaluatorDescriptors()) {

				Integer sortIndex = ValidationUtils.validateParameter("sortorder_" + evaluatorDescription.getEvaluatorID(), req, true, IntegerPopulator.getPopulator(), validationErrors);
				
				if (sortIndex != null) {

					evaluatorDescription.setSortIndex(sortIndex);
				}

				evaluatorDescription.setQueryDescriptor(queryDescriptor);
			}

			daoFactory.getEvaluatorDescriptorDAO().update(queryDescriptor.getEvaluatorDescriptors(), null);

			addFlowFamilyEvent(eventEvaluatorSortMessage + " \"" + queryDescriptor.getName() + "\"", flow, user);

			getEventHandler().sendEvent(EvaluatorDescriptor.class, new CRUDEvent<EvaluatorDescriptor>(EvaluatorDescriptor.class, CRUDAction.UPDATE, queryDescriptor.getEvaluatorDescriptors()), EventTarget.ALL);

			redirectToMethod(req, res, "/showflow/" + flow.getFlowID() + "#steps");
			return null;
		}

		log.info("User " + user + " requesting sort status form for flow " + flow);

		Document doc = createDocument(req, uriParser, user);

		Element sortEvaluatorsElement = doc.createElement("SortEvaluators");
		doc.getDocumentElement().appendChild(sortEvaluatorsElement);

		sortEvaluatorsElement.appendChild(queryDescriptor.toXML(doc));

		return new SimpleForegroundModuleResponse(doc);
	}
	
	@WebPublic(toLowerCase = true)
	public ForegroundModuleResponse sortStatuses(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws ModuleConfigurationException, SQLException, AccessDeniedException, IOException {

		Flow flow = flowCRUD.getRequestedBean(req, null, user, uriParser, GenericCRUD.UPDATE);

		if (flow == null) {

			return list(req, res, user, uriParser, new ValidationError("FlowNotFound"));

		} else if (!hasFlowAccess(user, flow)) {

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
	public ForegroundModuleResponse copyStandardStatusGroup(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws ModuleConfigurationException, SQLException, AccessDeniedException, IOException, URINotFoundException {

		Integer statusGroupID = uriParser.getInt(2);
		StandardStatusGroup statusGroup = null;

		if (statusGroupID != null) {

			HighLevelQuery<StandardStatusGroup> query = new HighLevelQuery<>(StandardStatusGroup.STANDARD_STATUSES_RELATION, StandardStatus.DEFAULT_STANDARD_STATUS_MAPPINGS_RELATION);
			query.addParameter(standardStatusGroupDAOWrapper.getParameterFactory().getParameter(statusGroupID));
			
			statusGroup = standardStatusGroupDAOWrapper.getAnnotatedDAO().get(query);
		}

		if (statusGroup == null) {
			throw new URINotFoundException(uriParser);
		}

		log.info("User " + user + " copying " + statusGroup);

		statusGroup.setStatusGroupID(null);
		statusGroup.setName(statusGroup.getName() + flowNameCopySuffix);

		if(statusGroup.getStandardStatuses() != null) {
		
			for (StandardStatus status : statusGroup.getStandardStatuses()) {
				
				status.setStatusID(null);
			}
		}

		RelationQuery query = new RelationQuery(StandardStatusGroup.STANDARD_STATUSES_RELATION, StandardStatus.DEFAULT_STANDARD_STATUS_MAPPINGS_RELATION);
		
		daoFactory.getStandardStatusGroupDAO().add(statusGroup, query);

		getEventHandler().sendEvent(StandardStatusGroup.class, new CRUDEvent<StandardStatusGroup>(StandardStatusGroup.class, CRUDAction.ADD, statusGroup), EventTarget.ALL);

		redirectToMethod(req, res, "/standardstatuses");
		return null;
	}
	
	@WebPublic(toLowerCase = true)
	public ForegroundModuleResponse sortStandardStatuses(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws ModuleConfigurationException, SQLException, AccessDeniedException, IOException, URINotFoundException {
		
		Integer statusGroupID = uriParser.getInt(2);
		StandardStatusGroup statusGroup = null;

		if (statusGroupID != null) {
			
			statusGroup = standardStatusGroupDAOWrapper.get(statusGroupID);
		}
		
		if (statusGroup == null) {
			throw new URINotFoundException(uriParser);
		}
		
		List<StandardStatus> statuses = statusGroup.getStandardStatuses();
		
		if (CollectionUtils.isEmpty(statuses)) {
			
			log.info("User " + user + " requested sort standard statuses but there are none.");
			redirectToMethod(req, res, "/standardstatuses");
		}
		
		if (req.getMethod().equalsIgnoreCase("POST")) {
			
			for (StandardStatus status : statuses) {
				
				status.setStandardStatusGroup(statusGroup);
				
				String sortIndex = req.getParameter("sortorder_" + status.getStatusID());
				
				if (NumberUtils.isInt(sortIndex)) {
					
					status.setSortIndex(NumberUtils.toInt(sortIndex));
				}
			}
			
			daoFactory.getStandardStatusDAO().update(statuses, null);
			
			getEventHandler().sendEvent(StandardStatus.class, new CRUDEvent<StandardStatus>(StandardStatus.class, CRUDAction.UPDATE, statuses), EventTarget.ALL);
			
			redirectToMethod(req, res, "/showstandardstatusgroup/" + statusGroup.getStatusGroupID());
			return null;
		}
		
		log.info("User " + user + " requesting sort standard status form");
		
		Document doc = createDocument(req, uriParser, user);
		
		Element sortStandardStatusesElement = XMLUtils.appendNewElement(doc, doc.getDocumentElement(), "SortStandardStatuses");
		
		sortStandardStatusesElement.appendChild(statusGroup.toXML(doc));
		
		return new SimpleForegroundModuleResponse(doc);
	}
	
	@WebPublic(toLowerCase = true)
	public ForegroundModuleResponse replaceFlowStatuses(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws ModuleConfigurationException, SQLException, AccessDeniedException, IOException, URINotFoundException {

		Flow flow = flowCRUD.getRequestedBean(req, null, user, uriParser, GenericCRUD.SHOW);

		if (flow == null) {

			return list(req, res, user, uriParser, new ValidationError("FlowNotFound"));

		} else if (!hasFlowAccess(user, flow)) {

			throw new AccessDeniedException("User does not have access to flow type " + flow.getFlowType());
		}
		
		if (flow.getStatuses() != null) {
			for (Status status : flow.getStatuses()) {
				if (status.getFlowInstanceCount() > 0) {

					log.warn("User " + user + " attempted to replace " + flow + " statuses but it has flow instances");
					
					redirectToMethod(req, res, "/showflow/" + flow.getFlowID());
					return null;
				}
			}
		}
		
		List<ValidationError> validationErrors = new ArrayList<>(1);

		if (req.getMethod().equalsIgnoreCase("POST")) {
			
			Integer statusGroupID = ValidationUtils.validateParameter("statusGroupID", req, true, PositiveStringIntegerPopulator.getPopulator(), validationErrors);
			
			if (statusGroupID != null) {
				
				StandardStatusGroup statusGroup = getStatusGroup(statusGroupID, StandardStatusGroup.STANDARD_STATUSES_RELATION, StandardStatus.DEFAULT_STANDARD_STATUS_MAPPINGS_RELATION);
		
				if (statusGroup == null) {
			
					validationErrors.add(new ValidationError("statusGroupID", ValidationErrorType.InvalidFormat));
					
				} else {
	
					log.info("User " + user + " replacing " + flow + " statuses with " + statusGroup);
	
					replaceFlowStatusesWithStandardStatuses(flow, statusGroup);
	
					RelationQuery query = new RelationQuery(Flow.STATUSES_RELATION, Status.DEFAULT_STATUS_MAPPINGS_RELATION);
					daoFactory.getFlowDAO().update(flow, query);
	
					getEventHandler().sendEvent(Flow.class, new CRUDEvent<Flow>(Flow.class, CRUDAction.UPDATE, flow), EventTarget.ALL);
					
					addFlowFamilyEvent(eventStatusesReplacedMessage + " \"" + statusGroup.getName() + "\"", flow, user);
	
					redirectToMethod(req, res, "/showflow/" + flow.getFlowID() + "#statuses");
					return null;
				}
			}
		}
		
		log.info("User " + user + " requesting replace statuses form for flow " + flow);

		Document doc = createDocument(req, uriParser, user);

		Element updateFlowStatusesElement = doc.createElement("ReplaceFlowStatuses");
		doc.getDocumentElement().appendChild(updateFlowStatusesElement);

		updateFlowStatusesElement.appendChild(flow.toXML(doc));
		
		List<StandardStatusGroup> statusGroups = getDAOFactory().getStandardStatusGroupDAO().getAll();
		XMLUtils.append(doc, updateFlowStatusesElement, "StandardStatusGroups", statusGroups);

		if (validationErrors != null) {

			XMLUtils.append(doc, updateFlowStatusesElement, "ValidationErrors", validationErrors);
		}

		return new SimpleForegroundModuleResponse(doc);
	}
	
	public void replaceFlowStatusesWithStandardStatuses(Flow flow, StandardStatusGroup statusGroup) {

		List<StandardStatus> standardStatuses = statusGroup.getStandardStatuses();

		if (standardStatuses != null) {

			List<Status> statuses = new ArrayList<Status>(standardStatuses.size());

			for (StandardStatus standardStatus : standardStatuses) {

				Status status = new Status(standardStatus);

				if (standardStatus.getDefaultStandardStatusMappings() != null) {

					List<DefaultStatusMapping> statusMappings = new ArrayList<DefaultStatusMapping>(standardStatus.getDefaultStandardStatusMappings().size());

					for (DefaultStandardStatusMapping defaultStandardStatusMapping : standardStatus.getDefaultStandardStatusMappings()) {

						DefaultStatusMapping statusMapping = new DefaultStatusMapping();

						statusMapping.setActionID(defaultStandardStatusMapping.getActionID());
						statusMapping.setFlow(flow);

						statusMappings.add(statusMapping);
					}

					status.setDefaultStatusMappings(statusMappings);
				}

				statuses.add(status);
			}

			flow.setStatuses(statuses);
		}
	}

	@WebPublic(toLowerCase = true)
	public ForegroundModuleResponse updateNotifications(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Exception {

		Flow flow = flowCRUD.getRequestedBean(req, null, user, uriParser, GenericCRUD.SHOW);

		if (flow == null) {

			return list(req, res, user, uriParser, new ValidationError("FlowNotFound"));

		} else if (!hasFlowAccess(user, flow)) {

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

		log.info("User " + user + " updating notification settings for flow " + flow);

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

		} else if (!hasFlowAccess(user, flow)) {

			throw new AccessDeniedException("User does not have access to flow type " + flow.getFlowType());
		}

		log.info("User " + user + " requesting XSD for flow " + flow);

		Document doc = XMLUtils.parseXML(FlowAdminModule.class.getResourceAsStream("xsd/base-schema.xsd"), false, false);

		XPath xPath = PooledXPathFactory.newXPath();

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

						Query query = queryHandler.getQuery(queryDescriptor, true);

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

		for(XSDExtensionProvider xsdExtensionProvider : xsdExtensionProviders) {
			
			try {
				xsdExtensionProvider.processXSD(flow, doc);
				
			}catch(Exception e) {
				
				log.error("Error in XSD extension provider " + xsdExtensionProvider + " while processing XSD for flow " + flow, e);
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
		
		flowFormCRUD.checkFlowTypeAccess(user, flow);
		
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
	public ForegroundModuleResponse listStandardStatusGroups(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Exception, Throwable {

		return standardStatusGroupCRUD.list(req, res, user, uriParser, null);
	}

	@WebPublic(toLowerCase = true)
	public ForegroundModuleResponse addStandardStatusGroup(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Exception, Throwable {

		return standardStatusGroupCRUD.add(req, res, user, uriParser);
	}

	@WebPublic(toLowerCase = true)
	public ForegroundModuleResponse updateStandardStatusGroup(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Exception, Throwable {

		return standardStatusGroupCRUD.update(req, res, user, uriParser);
	}

	@WebPublic(toLowerCase = true)
	public ForegroundModuleResponse deleteStandardStatusGroup(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Exception, Throwable {

		return standardStatusGroupCRUD.delete(req, res, user, uriParser);
	}

	@WebPublic(toLowerCase = true)
	public ForegroundModuleResponse showStandardStatusGroup(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Exception, Throwable {

		return standardStatusGroupCRUD.show(req, res, user, uriParser, null);
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
			if (uriParser.size() == 3 && (flowID = NumberUtils.toInt(uriParser.get(2))) != null && flowCache.getFlowCacheMap().get(flowID) != null) {

				//Create new instance or get instance from session
				instanceManager = getUnsavedMutableFlowInstanceManager(flowID, updateAccessController, req.getSession(true), user, user, null, uriParser, req, true, false, false, false, DEFAULT_REQUEST_METADATA);

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

	@WebPublic(toLowerCase = true)
	public ForegroundModuleResponse testFlowAllSteps(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Exception, Throwable {

		Integer flowID = null;

		try {
			if (uriParser.size() == 3 && (flowID = NumberUtils.toInt(uriParser.get(2))) != null && flowCache.getFlowCacheMap().get(flowID) != null) {
				
				//Create new instance or get instance from session, only needed for query request processors
				MutableFlowInstanceManager normalTestFlowInstanceManager = getUnsavedMutableFlowInstanceManager(flowID, updateAccessController, req.getSession(true), user, user, null, uriParser, req, true, false, false, false, DEFAULT_REQUEST_METADATA);

				if (normalTestFlowInstanceManager == null) {

					log.info("User " + user + " requested non-existing flow with ID " + flowID + ", listing flows");
					return list(req, res, user, uriParser, FLOW_NOT_FOUND_VALIDATION_ERROR);
				}

				//Create new temporary instance with no evaluators

				Flow flow = getFlow(flowID);

				if (flow == null) {

					log.info("User " + user + " requested non-existing flow with ID " + flowID + ", listing flows");

					return list(req, res, user, uriParser, FLOW_NOT_FOUND_VALIDATION_ERROR);

				} else if (!flow.isInternal() || flow.getSteps() == null) {

					throw new FlowNotAvailiableInRequestedFormat(flowID);
				}

				for (Step step : flow.getSteps()) {

					if (step.getQueryDescriptors() != null) {
						for (QueryDescriptor queryDescriptor : step.getQueryDescriptors()) {

							queryDescriptor.setEvaluatorDescriptors(null);

							if (queryDescriptor.getDefaultQueryState() == QueryState.HIDDEN) {

								queryDescriptor.setDefaultQueryState(QueryState.VISIBLE);
								queryDescriptor.setName(queryDescriptor.getName() + " " + hiddenQueryText);
							}
						}
					}
				}

				SiteProfile profile = getCurrentSiteProfile(req, user, uriParser, flow.getFlowFamily());

				updateAccessController.checkNewFlowInstanceAccess(flow, user, profile);

				log.info("Creating new instance of flow " + flow + " for user " + user);

				InstanceMetadata instanceMetadata = new DefaultInstanceMetadata(profile);

				MutableFlowInstanceManager instanceManager = new MutableFlowInstanceManager(flow, queryHandler, evaluationHandler, getNewInstanceManagerID(user), req, user, user, instanceMetadata, DEFAULT_REQUEST_METADATA, getAbsoluteFileURL(uriParser, flow));
				
				systemInterface.getEventHandler().sendEvent(MutableFlowInstanceManager.class, new NewMutableFlowInstanceManagerCreatedEvent(user, instanceManager), EventTarget.ALL);
				
				try {
					log.info("User " + user + " requested testFlowAllSteps of flow instance " + instanceManager.getFlowInstance());
		
					List<ManagerResponse> managerResponses = instanceManager.getFullFormHTML(queryHandler, req, user, user, getMutableQueryRequestBaseURL(req, normalTestFlowInstanceManager), DEFAULT_REQUEST_METADATA);
		
					Document doc = createDocument(req, uriParser, user);
					Element flowInstanceManagerPreviewElement = doc.createElement("FlowInstanceManagerAllStepsForm");
					doc.getDocumentElement().appendChild(flowInstanceManagerPreviewElement);
		
					flowInstanceManagerPreviewElement.appendChild(instanceManager.getFlowInstance().toXML(doc));
		
					XMLUtils.append(doc, flowInstanceManagerPreviewElement, "ManagerResponses", managerResponses);
		
					appendFormData(doc, flowInstanceManagerPreviewElement, instanceManager, req, user);
		
					SimpleForegroundModuleResponse moduleResponse = new SimpleForegroundModuleResponse(doc, instanceManager.getFlowInstance().getFlow().getName());
		
					appendLinksAndScripts(moduleResponse, managerResponses);
		
					return moduleResponse;
					
				} catch (FlowInstanceManagerClosedException e) {

					log.info("User " + user + " requested flow instance manager for flow instance " + e.getFlowInstance() + " which has already been closed. Removing flow instance manager from session.");
		
					redirectToMethod(req, res, "/testflowallsteps/" + flowID);
					return null;
					
				} finally {
					
					instanceManager.close(queryHandler);
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

	@WebPublic(toLowerCase = true)
	public ForegroundModuleResponse addMessageTemplate(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Exception, Throwable {

		return messageTemplateCRUD.add(req, res, user, uriParser);
	}

	@WebPublic(toLowerCase = true)
	public ForegroundModuleResponse updateMessageTemplate(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Exception, Throwable {

		return messageTemplateCRUD.update(req, res, user, uriParser);
	}

	@WebPublic(toLowerCase = true)
	public ForegroundModuleResponse deleteMessageTemplate(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Exception, Throwable {

		return messageTemplateCRUD.delete(req, res, user, uriParser);
	}
	
	@EventListener(channel=FlowType.class)
	public void processFlowTypeEvent(CRUDEvent<FlowType> event, EventSource source) throws SQLException {
		
		log.info("Received CRUD event regarding " + event.getAction() + " of " + event.getBeans().size() + " flow types");
		
		cacheFlowTypes();
		cacheFlows();
	}
	
	@EventListener(channel=Category.class)
	public void processCategoryEvent(CRUDEvent<Category> event, EventSource source) throws SQLException {
		
		log.info("Received CRUD event regarding " + event.getAction() + " of " + event.getBeans().size() + " categories");
		
		cacheFlowTypes();
		cacheFlows();
	}
	
	@EventListener(channel=FlowFamily.class)
	public void processFlowFamilyEvent(CRUDEvent<FlowFamily> event, EventSource source) throws SQLException {
		
		log.info("Received CRUD event regarding " + event.getAction() + " of " + event.getBeans().size() + " flow families");
		
		if(event.getAction() == CRUDAction.DELETE) {

			deleteFlowFamiliesFromCache(event.getBeans());
			
		}else {
			
			List<Integer> flowFamilyIDs = new ArrayList<Integer>(event.getBeans().size());
			
			for(FlowFamily flowFamily : event.getBeans()) {
				
				flowFamilyIDs.add(flowFamily.getFlowFamilyID());
			}
			
			cacheFlowFamilies(flowFamilyIDs);
		}
	}
	
	@EventListener(channel = Flow.class)
	public void processFlowEvent(CRUDEvent<Flow> event, EventSource source) throws SQLException {

		log.info("Received CRUD event regarding " + event.getAction() + " of " + event.getBeans().size() + " flows");

		if (event.getAction() != CRUDAction.ADD) {

			for (Flow flow : event.getBeans()) {

				closeInstanceManagers(flow);
			}

		}
		
		if(event.getAction() == CRUDAction.DELETE) {

			//This code may leave loose files if the bean does not have all relations set
			for (Flow flow : event.getBeans()) {

				if (!CollectionUtils.isEmpty(flow.getFlowForms())) {

					for (FlowForm flowForm : flow.getFlowForms()) {

						deleteFlowFormFile(flowForm);
					}
				}
			}
			
			deleteFlowsFromCache(event.getBeans());

			for(Flow flow : event.getBeans()) {
				
				try {
					log.info("Deleting file attachments for flow " + flow);
					
					FlowEngineFileAttachmentUtils.deleteAttachments(fileAttachmentHandler, flow);
					
				}catch(Throwable t) {
					
					log.error("Error deleting file attachments for flow " + flow, t);
				}
			}
			
		} else {

			List<Integer> flowIDs = new ArrayList<Integer>(event.getBeans().size());

			for (Flow flow : event.getBeans()) {

				flowIDs.add(flow.getFlowID());
			}

			cacheFlows(flowIDs);
		}
	}
	
	@EventListener(channel=Step.class)
	public void processStepEvent(CRUDEvent<Step> event, EventSource source) throws SQLException {
	
		log.info("Received CRUD event regarding " + event.getAction() + " of " + event.getBeans().size() + " steps");
		
		for (Step step : event.getBeans()) {

			closeInstanceManagers(step.getFlow());
		}
		
		List<Integer> flowIDs = new ArrayList<Integer>(event.getBeans().size());
		
		for(Step step : event.getBeans()) {
			
			flowIDs.add(step.getFlow().getFlowID());
		}
		
		cacheFlows(flowIDs);
	}
	
	@EventListener(channel=QueryDescriptor.class)
	public void processQueryDescriptorEvent(CRUDEvent<QueryDescriptor> event, EventSource source) throws SQLException {
		
		log.info("Received CRUD event regarding " + event.getAction() + " of " + event.getBeans().size() + " query descriptors");
		
		for (QueryDescriptor queryDescriptor : event.getBeans()) {

			if (queryDescriptor.getStep() == null || queryDescriptor.getStep().getFlow() == null) {

				log.error("Received CRUD event regarding query descriptor " + queryDescriptor + " without a flow set.");

				continue;
			}

			closeInstanceManagers(queryDescriptor.getStep().getFlow());
		}
		
		List<Integer> flowIDs = new ArrayList<Integer>(event.getBeans().size());
		
		for(QueryDescriptor queryDescriptor : event.getBeans()) {
			
			flowIDs.add(queryDescriptor.getStep().getFlow().getFlowID());
		}
		
		cacheFlows(flowIDs);
	}
	
	@EventListener(channel=EvaluatorDescriptor.class)
	public void processEvaluatorDescriptorEvent(CRUDEvent<EvaluatorDescriptor> event, EventSource source) throws SQLException {
	
		log.info("Received CRUD event regarding " + event.getAction() + " of " + event.getBeans().size() + " evaluator descriptors");
		
		for (EvaluatorDescriptor evaluatorDescriptor : event.getBeans()) {

			closeInstanceManagers(evaluatorDescriptor.getQueryDescriptor().getStep().getFlow());
		}
		
		List<Integer> flowIDs = new ArrayList<Integer>(event.getBeans().size());
		
		for(EvaluatorDescriptor evaluatorDescriptor : event.getBeans()) {
			
			flowIDs.add(evaluatorDescriptor.getQueryDescriptor().getStep().getFlow().getFlowID());
		}
		
		cacheFlows(flowIDs);
	}
	
	@EventListener(channel=Status.class)
	public void processStatusEvent(CRUDEvent<Status> event, EventSource source) throws SQLException {
		
		log.info("Received CRUD event regarding " + event.getAction() + " of " + event.getBeans().size() + " statuses");
		
		List<Integer> flowIDs = new ArrayList<Integer>(event.getBeans().size());
		
		for(Status status : event.getBeans()) {
			
			flowIDs.add(status.getFlow().getFlowID());
		}
		
		cacheFlows(flowIDs);
	}
	
	@EventListener(channel=FlowInstance.class)
	public void processFlowInstanceEvent(CRUDEvent<FlowInstance> event, EventSource source) throws SQLException {
		
		log.info("Received CRUD event regarding " + event.getAction() + " of " + event.getBeans().size() + " flow instances");
		
		for (FlowInstance flowInstance : event.getBeans()) {

			Flow flow;

			//Check if the given flow is found in the cache else reload the whole cache
			if (flowInstance.getFlow() == null || (flow = flowCache.getFlowCacheMap().get(flowInstance.getFlow().getFlowID())) == null) {

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

				try {
					log.info("Deleting file attachments for flow instance " + flowInstance);
					
					FlowEngineFileAttachmentUtils.deleteAttachments(fileAttachmentHandler, flowInstance);
					
				}catch(Throwable t) {
					
					log.error("Error deleting file attachments for flow instance " + flowInstance, t);
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

		return flowCache.getFlowCacheMap().get(flowID);
	}

	@Override
	public String getTitlePrefix() {

		return this.moduleDescriptor.getName();
	}

	public Flow getCachedFlow(Integer flowID) {

		return flowCache.getFlowCacheMap().get(flowID);
	}

	public Collection<FlowType> getCachedFlowTypes() {

		return this.flowTypeCacheMap.values();
	}

	public FlowType getCachedFlowType(Integer flowTypeID) {

		return flowTypeCacheMap.get(flowTypeID);
	}

	@Override
	public EventHandler getEventHandler() {

		return eventHandler;
	}

	@Override
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

		} else if (!hasFlowAccess(user, flow)) {

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

		return flowCache.getFlowCacheMap().get(flowID);
	}

	public FlowFamily getFlowFamily(int flowFamilyID) {

		return flowCache.getFlowFamilyCacheMap().get(flowFamilyID);
	}

	public List<Flow> getFlowVersions(FlowFamily flowFamily, Order sortOrder) {
		
		List<Flow> flows = new ArrayList<Flow>(flowFamily.getVersionCount());

		for (Flow flow : flowCache.getFlowCacheMap().values()) {

			if (flow.getFlowFamily().getFlowFamilyID().equals(flowFamily.getFlowFamilyID())) {

				flows.add(flow);
			}
		}

		if (!flows.isEmpty()) {

			if(sortOrder == Order.ASC) {

				Collections.sort(flows, ASC_FLOW_VERSION_COMPARATOR);
				
			} else {
				
				Collections.sort(flows, DESC_FLOW_VERSION_COMPARATOR);
				
			}
			
		}

		return flows;
	}
	
	public List<Flow> getFlowVersions(FlowFamily flowFamily) {

		return getFlowVersions(flowFamily, Order.ASC);
	}

	public List<Flow> getFlows(int flowTypeID) {

		List<Flow> flows = new ArrayList<Flow>();

		for (Flow flow : flowCache.getFlowCacheMap().values()) {

			if (flow.getFlowType().getFlowTypeID() == flowTypeID) {

				flows.add(flow);
			}
		}

		return flows;
	}

	public List<FlowFamily> getFlowFamilies(int flowTypeID) {

		HashSet<FlowFamily> flowFamilies = new HashSet<FlowFamily>(this.flowCache.getFlowFamilyCacheMap().size());

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
	public List<Integer> getAllowedGroupIDs() {

		return adminGroupIDs;
	}

	@Override
	public List<Integer> getAllowedUserIDs() {

		return adminUserIDs;
	}

	public List<Integer> getPublisherGroupIDs() {

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
	protected FlowInstanceEvent save(MutableFlowInstanceManager instanceManager, User user, User poster, HttpServletRequest req, String actionID, EventType eventType, Map<String,String> eventAttributes, RequestMetadata requestMetadata) throws FlowInstanceManagerClosedException, UnableToSaveQueryInstanceException, SQLException, FlowDefaultStatusNotFound {

		return null;
	}

	@Override
	protected Flow getFlow(Integer flowID) throws SQLException {

		return SerializationUtils.cloneSerializable(getCachedFlow(flowID));
	}

	public Collection<Flow> getCachedFlows() {

		return this.flowCache.getFlowCacheMap().values();
	}

	public Collection<FlowFamily> getCachedFlowFamilies() {

		return this.flowCache.getFlowFamilyCacheMap().values();
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
	public String getStandalonePaymentURL(FlowInstanceManager instanceManager, HttpServletRequest req) {

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

		} else if (!hasFlowAccess(user, flow)) {

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
		res.setContentType("text/oeflow");

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

		xmlGeneratorDocument.addIgnoredField(Flow.FLOW_TYPE_RELATION);
		xmlGeneratorDocument.addFieldElementableListener(QueryDescriptor.class, new QueryDescriptorElementableListener(queryHandler, validationErrors));
		xmlGeneratorDocument.addFieldElementableListener(EvaluatorDescriptor.class, new EvaluatorDescriptorElementableListener(evaluationHandler, validationErrors));
		xmlGeneratorDocument.addFieldElementableListener(FlowForm.class, new FlowFormExportElementableListener(this, validationErrors));

		Element flowNode = flow.toXML(xmlGeneratorDocument);
		XMLUtils.append(doc, flowNode, "OverviewAttributes", flow.getOverviewAttributes());
		
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

		} else if (!hasFlowAccess(user, flow)) {

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
				
				List<ValidationError> errors = new ArrayList<ValidationError>();
				
				flowFamily.setMessageTemplates(XMLPopulationUtils.populateBeans(xmlParser, "FlowFamily/MessageTemplates/MessageTemplate", MessageTemplate.class, errors));
				
				if (flowFamily.getMessageTemplates() == null) {
					
					flowFamily.setMessageTemplates(XMLPopulationUtils.populateBeans(xmlParser, "FlowFamily/ExternalMessageTemplates/ExternalMessageTemplate", MessageTemplate.class, errors));
				}

				//TODO Import flowfamily settings
				
				if (!errors.isEmpty()) {

					throw new ValidationException(errors);
				}
				
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
			
			boolean familyUpdated = false;

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
					
					familyUpdated = true;
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
									importedQueryMap.put(queryDescriptor, queryHandler.importQuery(queryDescriptor, transactionHandler, statusConversionMap));

								} catch (Exception e) {

									log.error("Error importing query " + queryDescriptor + " of type " + queryDescriptor.getQueryTypeID() + " into flow " + flow + " uploaded by user " + user, e);

									queryDescriptor.setEvaluatorDescriptors(null);
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
				eventHandler.sendEvent(Flow.class, new FlowVersionAdded(flow, statusConversionMap), EventTarget.ALL);

				if (familyUpdated) {
					eventHandler.sendEvent(FlowFamily.class, new CRUDEvent<FlowFamily>(CRUDAction.ADD, flow.getFlowFamily()), EventTarget.ALL);
				}

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

		} else if (!hasFlowAccess(user, flow)) {

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
							queryHandler.importQuery(queryDescriptor, transactionHandler, Collections.emptyMap());

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

		if(NumberUtils.isInt(req.getParameter("step"))) {
			
			XMLUtils.appendNewElement(doc, importQueriesElement, "SelectedStep", req.getParameter("step"));
		}
		
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

		if (!hasFlowAccess(user, flow)) {

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
			Query query = queryHandler.getQuery(queryDescriptor, false);

			queryDescriptorElement.appendChild(query.toXML(doc));

		} catch (Exception e) {

			log.error("Error exporting query " + queryDescriptor, e);

			return flowCRUD.showBean(flowCache.getFlowCacheMap().get(flow.getFlowID()), req, res, user, uriParser, new QueryExportValidationError(queryDescriptor));
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
	
	@WebPublic(alias = "managergroups")
	public ForegroundModuleResponse getManagerGroups(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Throwable {

		return userGroupListFlowManagersConnector.getGroups(req, res, user, uriParser);
	}
	
	public FlowNotificationHandler getNotificationHandler() {

		return notificationHandler;
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

			if (uriParser.size() == 3) {

				FlowForm flowForm = flow.getFlowForms().get(0);
				flowForm.setFlow(flow);

				return sendFlowForm(flowForm, req, res, user, uriParser);

			} else if (uriParser.size() == 4 && (flowFormID = uriParser.getInt(3)) != null) {

				for (FlowForm flowForm : flow.getFlowForms()) {

					if (flowForm.getFlowFormID().equals(flowFormID)) {

						flowForm.setFlow(flow);

						return sendFlowForm(flowForm, req, res, user, uriParser);
					}
				}
			}
		}

		throw new URINotFoundException(uriParser);
	}

	@WebPublic(alias = "statuses")
	public ForegroundModuleResponse getStatuses(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Throwable {

		Integer flowID;
		Flow flow;

		if (uriParser.size() >= 3 && (flowID = uriParser.getInt(2)) != null && (flow = flowCRUD.getBean(flowID, FlowCRUD.SHOW)) != null) {

			flowCRUD.checkAccess(user, flow);

			if (CollectionUtils.isEmpty(flow.getStatuses())) {

				sendEmptyJSONResponse(res);
				return null;
			}
			
			Set<String> statusNames = new HashSet<>();

			for (Status status : flow.getStatuses()) {
				
				statusNames.add(status.getName());
			}

			log.info("User " + user + " getting statuses for flow " + flow + ", found " + statusNames.size() + " hits");
			
			List<String> sortedStatusNames = new ArrayList<>(statusNames);
			Collections.sort(sortedStatusNames);
			
			JsonArray jsonArray = new JsonArray();

			sortedStatusNames.forEach(jsonArray::addNode);
			
			sendJSONResponse(jsonArray, res);
			return null;
		}

		sendEmptyJSONResponse(res);
		return null;
	}

	protected static void sendEmptyJSONResponse(HttpServletResponse res) throws IOException {
		
		JsonObject jsonObject = new JsonObject(1);
		jsonObject.putField("hitCount", "0");
		HTTPUtils.sendReponse(jsonObject.toJson(), JsonUtils.getContentType(), res);
	}
	
	protected void sendJSONResponse(JsonArray jsonArray, HttpServletResponse res) throws IOException {
		
		JsonObject jsonObject = new JsonObject(2);
		jsonObject.putField("hitCount", Integer.toString(jsonArray.size()));
		jsonObject.putField("hits", jsonArray);
		HTTPUtils.sendReponse(jsonObject.toJson(), JsonUtils.getContentType(), res);
	}

	public ForegroundModuleResponse sendFlowForm(FlowForm flowForm, HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws IOException, ModuleConfigurationException, URINotFoundException {

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

		try {
			HTTPUtils.sendFile(file, flowForm.getName() + "." + flowForm.getFileExtension(), req, res, ContentDisposition.ATTACHMENT);

		} catch (IOException e) {

			log.info("Error sending file " + file + " to user " + user + ", " + e);
		}

		return null;
	}

	@Override
	public String getFlowFormFilestore() {

		return flowFormFilestore;
	}

	@Override
	public String getFlowFormFilePath(FlowForm form) {

		return getFlowFormFilestore() + java.io.File.separator + form.getFlowFormID() + "." + form.getFileExtension();
	}

	@Override
	public boolean deleteFlowFormFile(FlowForm form) {

		return FileUtils.deleteFile(getFlowFormFilePath(form));
	}

	@Override
	public boolean allowSkipOverviewForFlowForms() {

		return allowSkipOverviewForFlowForms;
	}

	public FlowFamily getFlowFamilyByAlias(String alias) {

		for (FlowFamily flowFamily : flowCache.getFlowFamilyCacheMap().values()) {

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

	public synchronized void addExtensionViewProvider(FlowAdminExtensionViewProvider flowAdminExtensionProvider) {
		
		if (!extensionViewProviders.contains(flowAdminExtensionProvider)) {
			
			log.info("Extension view provider " + flowAdminExtensionProvider + " added");
			
			List<FlowAdminExtensionViewProvider> tempProviders = new ArrayList<FlowAdminExtensionViewProvider>(extensionViewProviders);
			
			tempProviders.add(flowAdminExtensionProvider);
			
			Collections.sort(tempProviders, FlowAdminExtensionViewProviderComparator.getInstance());
			
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
	
	public synchronized void addFragmentExtensionViewProvider(FlowAdminFragmentExtensionViewProvider flowAdminFragmentExtensionProvider) {
		
		if (!fragmentExtensionViewProviders.contains(flowAdminFragmentExtensionProvider)) {
			
			log.info("Fragment extension view provider " + flowAdminFragmentExtensionProvider + " added");
			
			List<FlowAdminFragmentExtensionViewProvider> tempProviders = new ArrayList<FlowAdminFragmentExtensionViewProvider>(fragmentExtensionViewProviders);
			
			tempProviders.add(flowAdminFragmentExtensionProvider);
			
			Collections.sort(tempProviders, PriorityComparator.ASC_COMPARATOR);
			
			fragmentExtensionViewProviders = new CopyOnWriteArrayList<FlowAdminFragmentExtensionViewProvider>(tempProviders);
		}
	}
	
	public synchronized void removeFragmentExtensionViewProvider(FlowAdminFragmentExtensionViewProvider flowAdminFragmentExtensionProvider) {
		
		fragmentExtensionViewProviders.remove(flowAdminFragmentExtensionProvider);
		
		log.info("Fragment extension view provider " + flowAdminFragmentExtensionProvider + " removed");
	}
	
	public List<FlowAdminFragmentExtensionViewProvider> getFragmentExtensionViewProviders() {
		
		return fragmentExtensionViewProviders;
	}
	
	public FlowAdminFragmentExtensionViewProvider getFragmentExtensionViewProvider(int moduleID) {
		
		for (FlowAdminFragmentExtensionViewProvider extension : fragmentExtensionViewProviders) {
			
			if (extension.getModuleID() == moduleID) {
				return extension;
			}
		}
		
		return null;
	}
	
	public synchronized void addFlowTypeExtensionProvider(FlowTypeExtensionProvider flowAdminFragmentExtensionProvider) {
		
		if (!flowTypeExtensionProviders.contains(flowAdminFragmentExtensionProvider)) {
			
			log.info("Flow type extension provider " + flowAdminFragmentExtensionProvider + " added");
			
			List<FlowTypeExtensionProvider> tempProviders = new ArrayList<FlowTypeExtensionProvider>(flowTypeExtensionProviders);
			
			tempProviders.add(flowAdminFragmentExtensionProvider);
			
			Collections.sort(tempProviders, PriorityComparator.ASC_COMPARATOR);
			
			flowTypeExtensionProviders = new CopyOnWriteArrayList<FlowTypeExtensionProvider>(tempProviders);
		}
	}
	
	public synchronized void removeFlowTypeExtensionProvider(FlowTypeExtensionProvider flowAdminFragmentExtensionProvider) {
		
		flowTypeExtensionProviders.remove(flowAdminFragmentExtensionProvider);
		
		log.info("Flow type extension provider " + flowAdminFragmentExtensionProvider + " removed");
	}
	
	public List<FlowTypeExtensionProvider> getFlowTypeExtensionsProviders() {
		
		return flowTypeExtensionProviders;
	}
	
	public void addFlowListExtensionLinkProvider(ExtensionLinkProvider e) {
		
		if (!flowListExtensionLinkProviders.contains(e)) {
			
			flowListExtensionLinkProviders.add(e);
			
			log.info("List flow extension link provider " + e + " added");
			
			if (systemInterface.getSystemStatus() == SystemStatus.STARTED) {
				
				sectionInterface.getMenuCache().moduleUpdated(moduleDescriptor, this);
			}
			
		}
	}
	
	public void removeFlowListExtensionLinkProvider(ExtensionLinkProvider e) {
		
		flowListExtensionLinkProviders.remove(e);
		
		log.info("List flow extension link provider " + e + " removed");
		
		if (systemInterface.getSystemStatus() == SystemStatus.STARTED) {
			
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
	
	public void addStatusFormExtensionProvider(StatusFormExtensionProvider e) {
		
		if (!statusFormExtensionProviders.contains(e)) {
			
			statusFormExtensionProviders.add(e);
			
			log.info("StatusFormExtensionProvider " + e + " added");
		}
	}
	
	public void removeStatusFormExtensionProvider(StatusFormExtensionProvider e) {
		
		statusFormExtensionProviders.remove(e);
		
		log.info("StatusFormExtensionProvider " + e + " removed");
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
		
		Integer flowID;
		Flow flow;
		
		if (uriParser.size() == 3 && (flowID = PositiveStringIntegerPopulator.getPopulator().getValue(uriParser.get(2))) != null && (flow = flowCache.getFlowCacheMap().get(flowID)) != null) {
			
			if (!hasFlowAccess(user, flow)) {
				
				throw new AccessDeniedException("User does not have access to flow type " + flow.getFlowType());
			}
			
			Document doc = createDocument(req, uriParser, user);
			Element showFlowFamilyEventsElement = doc.createElement("ShowFlowFamilyEvents");
			doc.getDocumentElement().appendChild(showFlowFamilyEventsElement);
			
			showFlowFamilyEventsElement.appendChild(flow.toXML(doc));
			XMLUtils.append(doc, showFlowFamilyEventsElement, "FlowFamilyEvents", getFlowFamilyEvents(flow.getFlowFamily()));
			
			return new SimpleForegroundModuleResponse(doc, getTitlePrefix(), getDefaultBreadcrumb(), new Breadcrumb(this, flow.getName(), "/showFlow/" + flow.getFlowID()));
		}
		
		throw new URINotFoundException(uriParser);
	}
	
	@WebPublic(toLowerCase = true)
	public ForegroundModuleResponse addFlowFamilyEvent(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Exception {

		Integer flowID;
		Flow flow;
		
		if (uriParser.size() == 3 && (flowID = PositiveStringIntegerPopulator.getPopulator().getValue(uriParser.get(2))) != null && (flow = flowCache.getFlowCacheMap().get(flowID)) != null) {

			if (!hasFlowAccess(user, flow)) {

				throw new AccessDeniedException("User does not have access to flow type " + flow.getFlowType());
			}
			
			List<ValidationError> validationErrors = null;
			
			if (req.getMethod().equalsIgnoreCase("POST")) {
				
				validationErrors = new ArrayList<ValidationError>();
				
				String eventMessage = ValidationUtils.validateParameter("event-message", req, true, 0, 1024, StringPopulator.getPopulator(), validationErrors);
				
				if (validationErrors.isEmpty()) {
					
					log.info("User " + user + " adding flow family event to " + flow);
					
					addFlowFamilyEvent(eventMessage, flow, user);
					
					redirectToMethod(req, res, "/showflow/" + flow.getFlowID() + "#events");
					return null;
				}
			}
			
			log.info("User " + user + " requesting add flow family event form for flow " + flow);

			Document doc = createDocument(req, uriParser, user);
			Element addFlowFamilyEventElement = doc.createElement("AddFlowFamilyEvent");
			doc.getDocumentElement().appendChild(addFlowFamilyEventElement);

			addFlowFamilyEventElement.appendChild(flow.toXML(doc));
			
			if (validationErrors != null) {

				addFlowFamilyEventElement.appendChild(RequestUtils.getRequestParameters(req, doc));
				XMLUtils.append(doc, addFlowFamilyEventElement, "ValidationErrors", validationErrors);
			}

			return new SimpleForegroundModuleResponse(doc, getTitlePrefix(), getDefaultBreadcrumb(), new Breadcrumb(this, flow.getName(), "/showFlow/" + flow.getFlowID()));
		}

		throw new URINotFoundException(uriParser);
	}

	@Override
	public void addFlowFamilyEvent(String message, ImmutableFlow flow, User user) {

		if (flow.getFlowFamily() != null) {

			addFlowFamilyEvent(message, flowCache.getFlowFamilyCacheMap().get(flow.getFlowFamily().getFlowFamilyID()), flow.getVersion(), user);

		} else {

			addFlowFamilyEvent(message, flowCache.getFlowCacheMap().get(flow.getFlowID()).getFlowFamily(), flow.getVersion(), user);
		}
	}

	@Override
	public void addFlowFamilyEvent(String message, FlowFamily flowFamily, User user) {

		addFlowFamilyEvent(message, flowFamily, null, user);
	}

	private void addFlowFamilyEvent(String message, FlowFamily flowFamily, Integer flowVersion, User user) {

		if (message.length() > 1024) {
			
			log.warn("Too long message for FlowFamilyEvent, truncating. FlowFamily " + flowFamily + ", version " + flowVersion + ", user " + user);
			message = StringUtils.substring(message, 1024, "...");
		}

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

		} else if (!hasFlowAccess(user, requestedFlow)) {

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

	@Override
	public Icon getFlowIcon(Integer flowID) {

		Flow flow = flowCache.getFlowCacheMap().get(flowID);
		
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
		
		initScheduler();
	}
	
	protected synchronized void initScheduler() {
		
		if (scheduler != null) {
			
			log.warn("Invalid state, scheduler already running!");
			stopScheduler();
		}
		
		scheduler = new Scheduler(systemInterface.getApplicationName() + " - " + moduleDescriptor.toString());
		scheduler.setDaemon(true);
		updateManagersScheduleID = scheduler.schedule(managersUpdateInterval, new ExpiredManagerRemover(this, daoFactory));
		removeStaleFlowInstancesScheduleID = scheduler.schedule(removeStaleFlowInstancesInterval, new StaleFlowInstancesRemover(this, daoFactory));
		
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
	
	@WebPublic(toLowerCase = true)
	public ForegroundModuleResponse unPublishFlowFamily(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws ModuleConfigurationException, SQLException, AccessDeniedException, IOException {

		if (!hasPublishAccess(user)) {
			throw new AccessDeniedException("User does not have publishing access");
		}
		
		if(!HTTPUtils.isPost(req)) {
			
			throw new AccessDeniedException("Unpublish flow requests using method " + req.getMethod() + " are not allowed.");
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

	public List<FlowBrowserExtensionViewProvider> getFlowBrowserExtensionViewProviders() {
		
		return flowBrowserExtensionViewProviders;
	}
	
	public String getEventFunctionConfigured() {
	
		return eventFunctionConfigured;
	}
	
	public String getEventFlowInstanceManagerExpired() {
	
		return eventFlowInstanceManagerExpired;
	}

	public boolean isShowManagerModalOnAdd() {
		return showManagerModalOnAdd;
	}
	
	@WebPublic(toLowerCase = true)
	public ForegroundModuleResponse updateAutoManagerAssignment(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws ModuleConfigurationException, SQLException, AccessDeniedException, IOException, URINotFoundException {
		
		Integer flowFamilyID;
		Integer flowID;
		FlowFamily flowFamily;
		ImmutableFlow flow;

		if (uriParser.size() >= 4 && (flowFamilyID = NumberUtils.toInt(uriParser.get(2))) != null && (flowID = NumberUtils.toInt(uriParser.get(3))) != null && (flowFamily = SerializationUtils.cloneSerializable(getFlowFamily(flowFamilyID))) != null && (flow = flowCache.getFlowCacheMap().get(flowID)) != null) {

			if (!AccessUtils.checkAccess(user, flow.getFlowType().getAdminAccessInterface()) && !AccessUtils.checkAccess(user, this)) {

				throw new AccessDeniedException("User does not have access to flow type " + flow.getFlowType());
			}

			List<User> allowedManagers = FlowFamilyUtils.getAllowedManagerUsers(flowFamily, systemInterface.getUserHandler());
			List<Group> allowedManagerGroups = FlowFamilyUtils.getAllowedManagerGroups(flowFamily, systemInterface.getGroupHandler());
			
			List<User> noMatchUsers = FlowFamilyUtils.filterSelectedManagerUsers(allowedManagers, flowFamily.getAutoManagerAssignmentNoMatchUserIDs(), null);
			List<Group> noMatchGroups = FlowFamilyUtils.filterSelectedManagerGroups(allowedManagerGroups, flowFamily.getAutoManagerAssignmentNoMatchGroupIDs(), null);
			List<User> alwaysUsers = FlowFamilyUtils.filterSelectedManagerUsers(allowedManagers, flowFamily.getAutoManagerAssignmentAlwaysUserIDs(), null);
			List<Group> alwaysGroups = FlowFamilyUtils.filterSelectedManagerGroups(allowedManagerGroups, flowFamily.getAutoManagerAssignmentAlwaysGroupIDs(), null);

			setRuleUsersAndGroups(flowFamily, allowedManagers, allowedManagerGroups);
			
			List<ValidationError> validationErrors = null;
			
			if (req.getMethod().equalsIgnoreCase("POST")) {
				
				validationErrors = new ArrayList<ValidationError>();
				
				List<Integer> noMatchUserIDs = CollectionUtils.removeDuplicates(NumberUtils.toInt(req.getParameterValues("auto-manager-nomatch-user")));
				List<Integer> noMatchGroupIDs = CollectionUtils.removeDuplicates(NumberUtils.toInt(req.getParameterValues("auto-manager-nomatch-group")));
				List<Integer> alwaysUserIDs = CollectionUtils.removeDuplicates(NumberUtils.toInt(req.getParameterValues("auto-manager-always-user")));
				List<Integer> alwaysGroupIDs = CollectionUtils.removeDuplicates(NumberUtils.toInt(req.getParameterValues("auto-manager-always-group")));
				
				noMatchUsers = noMatchUserIDs == null ? null : systemInterface.getUserHandler().getUsers(noMatchUserIDs, false, true);
				noMatchGroups = noMatchGroupIDs == null ? null : systemInterface.getGroupHandler().getGroups(noMatchGroupIDs, false);
				alwaysUsers = alwaysUserIDs == null ? null : systemInterface.getUserHandler().getUsers(alwaysUserIDs, false, true);
				alwaysGroups = alwaysGroupIDs == null ? null : systemInterface.getGroupHandler().getGroups(alwaysGroupIDs, false);
				
				FlowFamilyUtils.validateSelectedManagerUsers(allowedManagers, noMatchUsers, noMatchUserIDs, validationErrors);
				FlowFamilyUtils.validateSelectedManagerGroups(allowedManagerGroups, noMatchGroups, noMatchGroupIDs, validationErrors);
				FlowFamilyUtils.validateSelectedManagerUsers(allowedManagers, alwaysUsers, alwaysUserIDs, validationErrors);
				FlowFamilyUtils.validateSelectedManagerGroups(allowedManagerGroups, alwaysGroups, alwaysGroupIDs, validationErrors);
				
				String[] ruleIDs = req.getParameterValues("auto-manager-rule");
				List<AutoManagerAssignmentRule> rules = null;
				
				if (ruleIDs != null) {
					
					rules = new ArrayList<AutoManagerAssignmentRule>();
					
					for (String ruleID : ruleIDs) {
						
						AutoManagerAssignmentRule rule = new AutoManagerAssignmentRule();
						rule.setGeneratedRuleID(ruleID);
						
						rules.add(rule);
						
						String attributeName = ValidationUtils.validateParameter("auto-manager-rule-attribute-" + ruleID, req, true, 1, 255, StringPopulator.getPopulator(), validationErrors);
						String attributeValues = ValidationUtils.validateParameter("auto-manager-rule-values-" + ruleID, req, true, StringPopulator.getPopulator(), validationErrors);
						boolean invert = "true".equalsIgnoreCase(req.getParameter("auto-manager-rule-invert-" + ruleID));
						
						rule.setAttributeName(attributeName);
						rule.setInverted(invert);
						
						if (!StringUtils.isEmpty(attributeValues)) {
							
							List<String> splitValues = StringUtils.splitOnLineBreak(attributeValues, true);
							
							for (String value : splitValues) {
								
								if (value.length() > 255) {
									
									validationErrors.add(new ValidationError("auto-manager-rule-values-" + ruleID, ValidationErrorType.TooLong));
								}
							}
							
							rule.setValues(splitValues);
						}
						
						String usersIDsString = ValidationUtils.validateParameter("auto-manager-rule-users-" + ruleID, req, false, 1, 255, StringPopulator.getPopulator(), validationErrors);
						String groupIDsString = ValidationUtils.validateParameter("auto-manager-rule-groups-" + ruleID, req, false, 1, 255, StringPopulator.getPopulator(), validationErrors);
						
						List<Integer> userIDs = new ArrayList<Integer>();
						List<Integer> groupIDs = new ArrayList<Integer>();
						
						if (!StringUtils.isEmpty(usersIDsString)) {
							
							String[] splitUsersIDs = usersIDsString.split(",");
							
							for (String userIDS : splitUsersIDs) {
								
								Integer userID = NumberUtils.toInt(userIDS);
								
								if (userID != null) {
									
									userIDs.add(userID);
									
								} else {
									
									validationErrors.add(new ValidationError("auto-manager-rule-users-" + ruleID, ValidationErrorType.InvalidFormat));
								}
							}
						}
						
						if (!StringUtils.isEmpty(groupIDsString)) {
							
							String[] splitGroupIDs = groupIDsString.split(",");
							
							for (String groupIDS : splitGroupIDs) {
								
								Integer groupID = NumberUtils.toInt(groupIDS);
								
								if (groupID != null) {
									
									groupIDs.add(groupID);
									
								} else {
									
									validationErrors.add(new ValidationError("auto-manager-rule-groups-" + ruleID, ValidationErrorType.InvalidFormat));
								}
							}
						}
						
						userIDs = CollectionUtils.removeDuplicates(userIDs);
						groupIDs = CollectionUtils.removeDuplicates(groupIDs);
						
						List<User> users = userIDs.isEmpty() ? null : systemInterface.getUserHandler().getUsers(userIDs, false, true);
						List<Group> groups = groupIDs.isEmpty() ? null : systemInterface.getGroupHandler().getGroups(groupIDs, false);
						
						FlowFamilyUtils.validateSelectedManagerUsers(allowedManagers, users, userIDs, validationErrors);
						FlowFamilyUtils.validateSelectedManagerGroups(allowedManagerGroups, groups, groupIDs, validationErrors);
						
						rule.setUsers(users);
						rule.setGroups(groups);
					}
				}
				
				flowFamily.setAutoManagerAssignmentRules(rules);
				flowFamily.setAutoManagerAssignmentNoMatchUserIDs(UserUtils.getUserIDs(noMatchUsers));
				flowFamily.setAutoManagerAssignmentNoMatchGroupIDs(UserUtils.getGroupIDs(noMatchGroups));
				flowFamily.setAutoManagerAssignmentAlwaysUserIDs(UserUtils.getUserIDs(alwaysUsers));
				flowFamily.setAutoManagerAssignmentAlwaysGroupIDs(UserUtils.getGroupIDs(alwaysGroups));
				
				if (!FlowFamilyUtils.isAutoManagerRulesValid(flowFamily, systemInterface.getUserHandler())) {
					
					validationErrors.add(new ValidationError("FullManagerOrFallbackManagerRequired"));
				}
				
				setAutoManagerAssignmentStatusRules(req, flowFamily, allowedManagers, allowedManagerGroups, validationErrors);
				
				if (validationErrors.isEmpty()) {

					log.info("User " + user + " updating auto manager assignment settings for " + flow);

					TransactionHandler transactionHandler = null;

					try {
						transactionHandler = daoFactory.getFlowFamilyDAO().createTransaction();

						RelationQuery updateQuery = new RelationQuery(FlowFamily.AUTO_MANAGER_ASSIGNMENT_RULES_RELATION, FlowFamily.AUTO_MANAGER_ASSIGNMENT_ALWAYS_USERS_RELATION, FlowFamily.AUTO_MANAGER_ASSIGNMENT_ALWAYS_GROUPS_RELATION, FlowFamily.AUTO_MANAGER_ASSIGNMENT_NO_MATCH_USERS_RELATION, FlowFamily.AUTO_MANAGER_ASSIGNMENT_NO_MATCH_GROUPS_RELATION, FlowFamily.AUTO_MANAGER_ASSIGNMENT_STATUS_RULES_RELATION);
						daoFactory.getFlowFamilyDAO().update(flowFamily, transactionHandler, updateQuery);
						
						transactionHandler.commit();

						eventHandler.sendEvent(FlowFamily.class, new CRUDEvent<FlowFamily>(FlowFamily.class, CRUDAction.UPDATE, flowFamily), EventTarget.ALL);
						addFlowFamilyEvent(eventUpdateAutoManagerAssignment, flow, user);

						redirectToMethod(req, res, "/showflow/" + flow.getFlowID() + "#managers");
						return null;

					} finally {
						TransactionHandler.autoClose(transactionHandler);
					}
				}
			}
			
			log.info("User " + user + " requesting auto manager assignment form for flow " + flow);
			
			Document doc = createDocument(req, uriParser, user);
			
			Element autoManagerAssignmentElement = doc.createElement("AutoManagerAssignment");
			doc.getDocumentElement().appendChild(autoManagerAssignmentElement);
			
			XMLGeneratorDocument genDoc = new XMLGeneratorDocument(doc);
			genDoc.addIgnoredFields(Flow.FLOW_FAMILY_RELATION);
			
			autoManagerAssignmentElement.appendChild(flow.toXML(genDoc));
			autoManagerAssignmentElement.appendChild(flowFamily.toXML(doc));
			
			XMLUtils.append(doc, autoManagerAssignmentElement, "AutoManagerAssignmentNoMatchUsers", noMatchUsers);
			XMLUtils.append(doc, autoManagerAssignmentElement, "AutoManagerAssignmentNoMatchGroups", noMatchGroups);
			XMLUtils.append(doc, autoManagerAssignmentElement, "AutoManagerAssignmentAlwaysUsers", alwaysUsers);
			XMLUtils.append(doc, autoManagerAssignmentElement, "AutoManagerAssignmentAlwaysGroups", alwaysGroups);
			
			if (validationErrors != null) {
				
				XMLUtils.append(doc, autoManagerAssignmentElement, "ValidationErrors", validationErrors);
				autoManagerAssignmentElement.appendChild(RequestUtils.getRequestParameters(req, doc));
			}
			
			return new SimpleForegroundModuleResponse(doc);
		}

		throw new URINotFoundException(uriParser);
	}
	
	private void setRuleUsersAndGroups(FlowFamily flowFamily, List<User> allowedManagers, List<Group> allowedManagerGroups) {
		
		if (flowFamily.getAutoManagerAssignmentRules() != null) {
			
			for (AutoManagerAssignmentRule rule : flowFamily.getAutoManagerAssignmentRules()) {
				
				rule.setUsersAndGroups(allowedManagers, allowedManagerGroups, true);
			}
		}
		
		if (flowFamily.getAutoManagerAssignmentStatusRules() != null) {
			
			for (AutoManagerAssignmentStatusRule rule : flowFamily.getAutoManagerAssignmentStatusRules()) {
				
				rule.setUsersAndGroups(allowedManagers, allowedManagerGroups, true);
			}
		}
	}
	
	private void setAutoManagerAssignmentStatusRules(HttpServletRequest req, FlowFamily flowFamily, List<User> allowedManagers, List<Group> allowedManagerGroups, List<ValidationError> validationErrors) {
		
		String[] ruleIDs = req.getParameterValues("auto-manager-status-rule");
		List<AutoManagerAssignmentStatusRule> rules = null;
		
		if (ruleIDs != null) {
			
			rules = new ArrayList<AutoManagerAssignmentStatusRule>();
			
			for (String ruleID : ruleIDs) {
				
				AutoManagerAssignmentStatusRule rule = new AutoManagerAssignmentStatusRule();
				
				rule.setGeneratedRuleID(ruleID);
				
				String statusName = ValidationUtils.validateParameter("auto-manager-status-rule-statusName-" + ruleID, req, true, 1, 255, StringPopulator.getPopulator(), validationErrors);
				boolean addManagers = "true".equalsIgnoreCase(req.getParameter("auto-manager-status-rule-addManagers-" + ruleID));
				boolean removePreviousManagers = "true".equalsIgnoreCase(req.getParameter("auto-manager-status-rule-removePreviousManagers-" + ruleID));
				boolean sendNotification = "true".equalsIgnoreCase(req.getParameter("auto-manager-status-rule-sendNotification-" + ruleID));
				
				if (containsRuleWithStatusName(rules, statusName)) {
					
					validationErrors.add(new ValidationError("auto-manager-status-rule-statusName-" + ruleID, ValidationErrorType.Other, "DuplicateStatusRule"));
				}
				
				if (BooleanUtils.isFalse(addManagers, removePreviousManagers)) {
					
					validationErrors.add(new ValidationError("auto-manager-status-rule-" + ruleID, ValidationErrorType.Other, "NoActionsSelected"));
				}
			
				rule.setStatusName(statusName);
				rule.setAddManagers(addManagers);
				rule.setRemovePreviousManagers(removePreviousManagers);
				rule.setSendNotification(sendNotification);
				
				if (addManagers) {
					
					String usersIDsString = ValidationUtils.validateParameter("auto-manager-status-rule-users-" + ruleID, req, false, 1, 255, StringPopulator.getPopulator(), validationErrors);
					String groupIDsString = ValidationUtils.validateParameter("auto-manager-status-rule-groups-" + ruleID, req, false, 1, 255, StringPopulator.getPopulator(), validationErrors);
					
					List<Integer> userIDs = new ArrayList<Integer>();
					List<Integer> groupIDs = new ArrayList<Integer>();
					
					if (!StringUtils.isEmpty(usersIDsString)) {
						
						String[] splitUsersIDs = usersIDsString.split(",");
						
						for (String userIDS : splitUsersIDs) {
							
							Integer userID = NumberUtils.toInt(userIDS);
							
							if (userID != null) {
								
								userIDs.add(userID);
								
							} else {
								
								validationErrors.add(new ValidationError("auto-manager-status-rule-users-" + ruleID, ValidationErrorType.InvalidFormat));
							}
						}
					}
					
					if (!StringUtils.isEmpty(groupIDsString)) {
						
						String[] splitGroupIDs = groupIDsString.split(",");
						
						for (String groupIDS : splitGroupIDs) {
							
							Integer groupID = NumberUtils.toInt(groupIDS);
							
							if (groupID != null) {
								
								groupIDs.add(groupID);
								
							} else {
								
								validationErrors.add(new ValidationError("auto-manager-status-rule-groups-" + ruleID, ValidationErrorType.InvalidFormat));
							}
						}
					}
					
					userIDs = CollectionUtils.removeDuplicates(userIDs);
					groupIDs = CollectionUtils.removeDuplicates(groupIDs);
					
					List<User> users = userIDs.isEmpty() ? null : systemInterface.getUserHandler().getUsers(userIDs, false, true);
					List<Group> groups = groupIDs.isEmpty() ? null : systemInterface.getGroupHandler().getGroups(groupIDs, false);
					
					FlowFamilyUtils.validateSelectedManagerUsers(allowedManagers, users, userIDs, validationErrors);
					FlowFamilyUtils.validateSelectedManagerGroups(allowedManagerGroups, groups, groupIDs, validationErrors);
					
					rule.setUsers(users);
					rule.setGroups(groups);
					
				} else {
					
					rule.setUsers(null);
					rule.setGroups(null);
				}
				
				if (sendNotification) {
					
					String fieldName = "auto-manager-status-rule-emailRecipients-" + ruleID;
					
					ArrayList<String> emailRecipients = StringUtils.splitOnLineBreak(req.getParameter(fieldName), true);
					
					if(emailRecipients != null) {
						
						HashSet<String> duplicatesCheck = new HashSet<>();
						ListIterator<String> iterator = emailRecipients.listIterator();
						
						while (iterator.hasNext()) {
							
							String email = iterator.next().trim();
							
							if (email.isEmpty() || duplicatesCheck.contains(email.toLowerCase())) {
								
								iterator.remove();

							} else {

								duplicatesCheck.add(email.toLowerCase());
								iterator.set(email);

								if (email.length() > 255) {

									validationErrors.add(new ValidationError(fieldName, ValidationErrorType.TooLong));
									break;

								} else if (!EmailPopulator.getPopulator().validateFormat(email)) {

									validationErrors.add(new ValidationError(fieldName, ValidationErrorType.InvalidFormat));
									break;
								}
							}
						}
					}
					
					if (CollectionUtils.isEmpty(emailRecipients)) {
						
						validationErrors.add(new ValidationError(fieldName, ValidationErrorType.RequiredField));
						
					}
					
					rule.setEmailRecipients(emailRecipients);
				}
				
				rules.add(rule);
			}
		}
		
		flowFamily.setAutoManagerAssignmentStatusRules(rules);
	}

	private boolean containsRuleWithStatusName(List<AutoManagerAssignmentStatusRule> rules, String statusName) {
		
		if (!CollectionUtils.isEmpty(rules)) {
			
			for (AutoManagerAssignmentStatusRule rule : rules) {
				
				if (rule.getStatusName() != null && rule.getStatusName().equalsIgnoreCase(statusName)) {
					
					return true;
				}
			}
		}
		
		return false;
	}
	
	@WebPublic(requireLogin = true)
	public ForegroundModuleResponse checkForExpiringManagers(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws URINotFoundException {
		
		if (user.isAdmin()) {
			
			new ExpiredManagerRemover(this, daoFactory).run();
			return new SimpleForegroundModuleResponse("See log", getDefaultBreadcrumb());
		}
		
		throw new URINotFoundException(uriParser);
	}
	
	@WebPublic(requireLogin = true)
	public ForegroundModuleResponse checkForStaleFlowInstances(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws URINotFoundException {
		
		if (user.isAdmin()) {
			
			new StaleFlowInstancesRemover(this, daoFactory).run();
			return new SimpleForegroundModuleResponse("See log", getDefaultBreadcrumb());
		}
		
		throw new URINotFoundException(uriParser);
	}
	
	@Override
	public boolean hasFlowAccess(User user, Flow flow) {
		
		return AccessUtils.checkAccess(user, flow.getFlowType().getAdminAccessInterface()) || AccessUtils.checkAccess(user, this);
	}
	
	@Override
	public MultiSigningHandler getMultiSigningHandler() {
		
		return multiSigningHandler;
	}
	
	public boolean isBlockForeignIDs() {
		return blockForeignIDs;
	}
	
	public List<String> getForeignIDattributes() {
		return foreignIDattributes;
	}
	
	@WebPublic(alias = "extension", autoAppendLinks = false, autoAppendScripts = false)
	public ForegroundModuleResponse processFragmentExtensionView(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Exception {
		
		Integer flowID;
		Integer extensionModuleID;
		Flow flow;
		FlowAdminFragmentExtensionViewProvider fragmentExtension;
		
		if (uriParser.size() >= 3 && (flowID = uriParser.getInt(2)) != null && (extensionModuleID = uriParser.getInt(3)) != null && (flow = flowCache.getFlowCacheMap().get(flowID)) != null && (fragmentExtension = getFragmentExtensionViewProvider(extensionModuleID)) != null) {

			if (!hasFlowAccess(user, flow)) {
				
				throw new AccessDeniedException("User does not have access to flow type " + flow.getFlowType());
			}
			
			String extensionRequestURL = getFragmentExtensionViewProviderURL(fragmentExtension, flow);
			
			if (uriParser.size() >= 4 && "static".equals(uriParser.get(4)) && fragmentExtension instanceof Module) {
				
				return staticContentModule.processRequest((Module<?>) fragmentExtension, fragmentExtension.getModuleDescriptor(), req, res, user, uriParser.getNextLevel(4));
			}
			
			ViewFragment viewFragment = fragmentExtension.processRequest(extensionRequestURL, flow, req, res, user, uriParser);
			
			if (res.isCommitted()) {
				return null;
			}
			
			if (viewFragment == null) {
				
				redirectToMethod(req, res, "/showflow/" + flow.getFlowID() + "#" + fragmentExtension.getExtensionViewLinkName());
				return null;
			}
			
			Document doc = createDocument(req, uriParser, user);
			Element updateUserElement = doc.createElement("ViewFragmentExtension");
			doc.getFirstChild().appendChild(updateUserElement);
			
			updateUserElement.appendChild(viewFragment.toXML(doc));
			
			SimpleForegroundModuleResponse moduleResponse = new SimpleForegroundModuleResponse(doc);
			ViewFragmentUtils.appendLinksAndScripts(moduleResponse, viewFragment, extensionRequestURL);
			
			return moduleResponse;
		}
		
		throw new URINotFoundException(uriParser);
	}
	
	public String getFragmentExtensionViewProviderURL(FlowAdminFragmentExtensionViewProvider fragmentExtensionProvider, Flow flow) {
		return getFullAlias() + "/extension/" + flow.getFlowID() + "/" + fragmentExtensionProvider.getModuleID();
	}

	public String getAlias() {
		return moduleDescriptor.getAlias();
	}

	@Override
	public List<String> getAllowedFlowFormFileExtensions() {
		return allowedFlowFormFileExtensions;
	}
	
	public AccessInterface getAccessInterface() {
		
		return moduleDescriptor;
	}
	
	public void addXSDExtensionProvider(XSDExtensionProvider provider) {

		if (provider == null) {
			throw new NullPointerException();
		}

		xsdExtensionProviders.add(provider);
	}

	public void removeXSDExtensionProvider(XSDExtensionProvider provider) {

		xsdExtensionProviders.remove(provider);
	}
	
	public String getCkConnectorModuleAlias() {

		return ckConnectorModuleAlias;
	}

	public String getCssPath() {

		return cssPath;
	}
	
	@Override
	public String getFileMissing() {

		return fileMissing;
	}

	@Override
	public String getEventFlowFormAddedMessage() {

		return eventFlowFormAddedMessage;
	}

	@Override
	public String getEventFlowFormUpdatedMessage() {

		return eventFlowFormUpdatedMessage;
	}

	@Override
	public String getEventFlowFormDeletedMessage() {

		return eventFlowFormDeletedMessage;
	}
	
	@Override
	public String getEventMessageTemplatesAddedMessage() {

		return eventMessageTemplatesAddedMessage;
	}
	
	@Override
	public String getEventMessageTemplatesUpdatedMessage() {

		return eventMessageTemplatesUpdatedMessage;
	}
	
	@Override
	public String getEventMessageTemplatesDeletedMessage() {

		return eventMessageTemplatesDeletedMessage;
	}

	@Override
	public Integer getMaxPDFFormFileSize() {

		return maxPDFFormFileSize;
	}

	@Override
	public Icon getFlowTypeIcon(Integer flowTypeID) {

		return flowTypeCacheMap.get(flowTypeID);
	}

	public FlowFamilyCRUD getFlowFamilyCRUD() {
		
		
		return flowFamilyCRUD;
	}

	public FlowCache getFlowCache() {
		return flowCache;
	}
	
	public Transformer getModuleTransformer() {
		
		try {
			
			return sectionInterface.getForegroundModuleXSLTCache().getModuleTranformer(moduleDescriptor);
			
		} catch (TransformerConfigurationException e) {
			
			log.error("Unable to get module transformer");
		}
		
		return null;
	}
	
	@InstanceManagerDependency
	public void setUserAdminExtensionHandler(UserGroupAdminExtensionHandler userGroupAdminExtensionHandler) {

		if (this.userGroupAdminExtensionHandler != null) {

			this.userGroupAdminExtensionHandler.removeUserAdminExtensionProvider(this);
			this.userGroupAdminExtensionHandler.removeGroupAdminExtensionProvider(this);
		}
		
		this.userGroupAdminExtensionHandler = userGroupAdminExtensionHandler;

		if (userGroupAdminExtensionHandler != null) {

			userGroupAdminExtensionHandler.addUserAdminExtensionProvider(this);
			userGroupAdminExtensionHandler.addGroupAdminExtensionProvider(this);
		}
	}

	@Override
	public ViewFragment getUserAdminExtensionViewFragment(User requestedUser, HttpServletRequest req, URIParser uriParser, User user) throws Exception {

		Document doc = createDocument(req, uriParser, user);

		Element extensionElement = XMLUtils.appendNewElement(doc, doc.getDocumentElement(), "FlowUserAdminExtension");

		Collection<FlowFamily> flowFamilies = getCachedFlowFamilies();

		if (flowFamilies != null) {

			Element flowsElement = XMLUtils.appendNewElement(doc, extensionElement, "Flows");
			int flowsWithManagerAccess = 0;

			for (FlowFamily flowFamily : flowFamilies) {

				Flow flow = getLatestFlowVersion(flowFamily);
				ManagerAccess access = getFlowFamilyManagerAccess(flowFamily, requestedUser);

				if (flow != null && access != null) {

					Element flowElement = XMLUtils.appendNewElement(doc, flowsElement, "Flow");

					XMLUtils.appendNewElement(doc, flowElement, "flowID", flow.getFlowID());
					XMLUtils.appendNewElement(doc, flowElement, "name", flow.getName());
					XMLUtils.appendNewElement(doc, flowElement, "access", access);

					flowsWithManagerAccess++;
				}
			}

			if (flowsWithManagerAccess > 0) {
				
				XMLUtils.appendNewElement(doc, extensionElement, "FlowAdminURL", req.getContextPath() + sectionInterface.getSectionDescriptor().getFullAlias() + "/" + this.moduleDescriptor.getAlias());

				return viewFragmentTransformer.createViewFragment(doc);
			}
		}

		return null;
	}
	
	@Override
	public ViewFragment getGroupAdminExtensionViewFragment(Group requestedGroup, HttpServletRequest req, URIParser uriParser, User user) throws Exception {

		Document doc = createDocument(req, uriParser, user);

		Element extensionElement = XMLUtils.appendNewElement(doc, doc.getDocumentElement(), "FlowUserAdminExtension");

		Collection<FlowFamily> flowFamilies = getCachedFlowFamilies();

		if (flowFamilies != null) {

			Element flowsElement = XMLUtils.appendNewElement(doc, extensionElement, "Flows");
			int flowsWithManagerAccess = 0;

			for (FlowFamily flowFamily : flowFamilies) {

				Flow flow = getLatestFlowVersion(flowFamily);
				ManagerAccess access = flowFamily.getManagerAccess(requestedGroup);

				if (flow != null && access != null) {

					Element flowElement = XMLUtils.appendNewElement(doc, flowsElement, "Flow");

					XMLUtils.appendNewElement(doc, flowElement, "flowID", flow.getFlowID());
					XMLUtils.appendNewElement(doc, flowElement, "name", flow.getName());
					XMLUtils.appendNewElement(doc, flowElement, "access", access);

					flowsWithManagerAccess++;
				}
			}

			if (flowsWithManagerAccess > 0) {
				
				XMLUtils.appendNewElement(doc, extensionElement, "FlowAdminURL", req.getContextPath() + sectionInterface.getSectionDescriptor().getFullAlias() + "/" + this.moduleDescriptor.getAlias());

				return viewFragmentTransformer.createViewFragment(doc);
			}
		}

		return null;
	}
	
	public boolean hasFlowFamilyManagerAccess(Integer flowFamilyID, User user) {
		
		FlowFamily flowFamily = getFlowFamily(flowFamilyID);
		
		return hasFlowFamilyManagerAccess(flowFamily, user);
	}
	
	public boolean hasFlowFamilyManagerAccess(FlowFamily flowFamily, User user) {
		
		return getFlowFamilyManagerAccess(flowFamily, user) != null;
	}
	
	protected ManagerAccess getFlowFamilyManagerAccess(FlowFamily flowFamily, User user) {

		return flowFamily.getManagerAccess(user);
	}
	
	@Override
	public int getPriority() {
	
		return 1;
	}

	@Override
	public ForegroundModuleDescriptor getModuleDescriptor() {

		return moduleDescriptor;
	}

	@Override
	public List<LinkTag> getLinkTags() {

		return null;
	}

	@Override
	public List<ScriptTag> getScriptTags() {

		return null;
	}

	public StandardStatusGroup getStatusGroup(Integer statusGroupID, Field... relations) throws SQLException {
		
		HighLevelQuery<StandardStatusGroup> query = new HighLevelQuery<>();
		query.addParameter(standardStatusGroupDAOWrapper.getParameterFactory().getParameter(statusGroupID));
		
		if (relations != null) {
			query.addRelations(relations);
		}
		
		return standardStatusGroupDAOWrapper.getAnnotatedDAO().get(query);
	}
	
}
