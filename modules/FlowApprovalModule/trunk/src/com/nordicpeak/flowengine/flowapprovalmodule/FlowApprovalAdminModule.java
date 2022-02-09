package com.nordicpeak.flowengine.flowapprovalmodule;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.net.URL;
import java.net.URLDecoder;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactoryConfigurationError;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadBase.FileSizeLimitExceededException;
import org.apache.commons.fileupload.FileUploadBase.SizeLimitExceededException;
import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Level;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentCatalog;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;
import org.apache.pdfbox.pdmodel.common.PDMetadata;
import org.apache.pdfbox.pdmodel.graphics.color.PDOutputIntent;
import org.apache.xmpbox.XMPMetadata;
import org.apache.xmpbox.schema.AdobePDFSchema;
import org.apache.xmpbox.schema.DublinCoreSchema;
import org.apache.xmpbox.schema.PDFAIdentificationSchema;
import org.apache.xmpbox.schema.XMPBasicSchema;
import org.apache.xmpbox.xml.XmpSerializer;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xhtmlrenderer.pdf.ITextRenderer;

import se.unlogic.cron4jutils.CronStringValidator;
import se.unlogic.emailutils.framework.EmailUtils;
import se.unlogic.emailutils.framework.FileAttachment;
import se.unlogic.emailutils.framework.SimpleEmail;
import se.unlogic.fileuploadutils.MultipartRequest;
import se.unlogic.hierarchy.core.annotations.CheckboxSettingDescriptor;
import se.unlogic.hierarchy.core.annotations.EventListener;
import se.unlogic.hierarchy.core.annotations.HTMLEditorSettingDescriptor;
import se.unlogic.hierarchy.core.annotations.InstanceManagerDependency;
import se.unlogic.hierarchy.core.annotations.ModuleSetting;
import se.unlogic.hierarchy.core.annotations.TextAreaSettingDescriptor;
import se.unlogic.hierarchy.core.annotations.TextFieldSettingDescriptor;
import se.unlogic.hierarchy.core.annotations.WebPublic;
import se.unlogic.hierarchy.core.annotations.XSLVariable;
import se.unlogic.hierarchy.core.beans.Group;
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
import se.unlogic.hierarchy.core.exceptions.AccessDeniedException;
import se.unlogic.hierarchy.core.exceptions.ModuleConfigurationException;
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
import se.unlogic.hierarchy.core.utils.GenericCRUD;
import se.unlogic.hierarchy.core.utils.HierarchyAnnotatedDAOFactory;
import se.unlogic.hierarchy.core.utils.ModuleUtils;
import se.unlogic.hierarchy.core.utils.ModuleViewFragmentTransformer;
import se.unlogic.hierarchy.core.utils.ViewFragmentModule;
import se.unlogic.hierarchy.core.utils.usergrouplist.UserGroupListConnector;
import se.unlogic.hierarchy.core.validationerrors.FileSizeLimitExceededValidationError;
import se.unlogic.hierarchy.core.validationerrors.InvalidFileExtensionValidationError;
import se.unlogic.hierarchy.core.validationerrors.RequestSizeLimitExceededValidationError;
import se.unlogic.hierarchy.core.validationerrors.UnableToParseFileValidationError;
import se.unlogic.hierarchy.foregroundmodules.AnnotatedForegroundModule;
import se.unlogic.hierarchy.foregroundmodules.groupproviders.SimpleGroup;
import se.unlogic.hierarchy.foregroundmodules.staticcontent.StaticContentModule;
import se.unlogic.hierarchy.foregroundmodules.userproviders.SimpleUser;
import se.unlogic.openhierarchy.foregroundmodules.siteprofile.interfaces.SiteProfile;
import se.unlogic.standardutils.arrays.ArrayUtils;
import se.unlogic.standardutils.collections.CollectionUtils;
import se.unlogic.standardutils.dao.AdvancedAnnotatedDAOWrapper;
import se.unlogic.standardutils.dao.AnnotatedDAO;
import se.unlogic.standardutils.dao.HighLevelQuery;
import se.unlogic.standardutils.dao.LowLevelQuery;
import se.unlogic.standardutils.dao.MySQLRowLimiter;
import se.unlogic.standardutils.dao.OrderByCriteria;
import se.unlogic.standardutils.dao.QueryParameterFactory;
import se.unlogic.standardutils.dao.RelationQuery;
import se.unlogic.standardutils.dao.TransactionHandler;
import se.unlogic.standardutils.dao.querys.ObjectQuery;
import se.unlogic.standardutils.db.tableversionhandler.TableVersionHandler;
import se.unlogic.standardutils.db.tableversionhandler.UpgradeResult;
import se.unlogic.standardutils.db.tableversionhandler.XMLDBScriptProvider;
import se.unlogic.standardutils.enums.Order;
import se.unlogic.standardutils.io.BinarySizeFormater;
import se.unlogic.standardutils.io.BinarySizes;
import se.unlogic.standardutils.io.CloseUtils;
import se.unlogic.standardutils.io.FileUtils;
import se.unlogic.standardutils.json.JsonArray;
import se.unlogic.standardutils.json.JsonObject;
import se.unlogic.standardutils.json.JsonUtils;
import se.unlogic.standardutils.numbers.NumberUtils;
import se.unlogic.standardutils.object.ObjectUtils;
import se.unlogic.standardutils.populators.IntegerPopulator;
import se.unlogic.standardutils.string.AnnotatedBeanTagSourceFactory;
import se.unlogic.standardutils.string.SingleTagSource;
import se.unlogic.standardutils.string.StringUtils;
import se.unlogic.standardutils.string.TagReplacer;
import se.unlogic.standardutils.string.TagSource;
import se.unlogic.standardutils.time.TimeUtils;
import se.unlogic.standardutils.validation.NonNegativeStringIntegerValidator;
import se.unlogic.standardutils.validation.PositiveStringIntegerValidator;
import se.unlogic.standardutils.validation.ValidationError;
import se.unlogic.standardutils.validation.ValidationException;
import se.unlogic.standardutils.xml.ClassPathURIResolver;
import se.unlogic.standardutils.xml.XMLGeneratorDocument;
import se.unlogic.standardutils.xml.XMLParser;
import se.unlogic.standardutils.xml.XMLTransformer;
import se.unlogic.standardutils.xml.XMLUtils;
import se.unlogic.standardutils.xsl.URIXSLTransformer;
import se.unlogic.webutils.http.HTTPUtils;
import se.unlogic.webutils.http.RequestUtils;
import se.unlogic.webutils.http.URIParser;

import com.lowagie.text.pdf.PdfArray;
import com.lowagie.text.pdf.PdfFileSpecification;
import com.lowagie.text.pdf.PdfName;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfStamper;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.pdf.RandomAccessFileOrArray;
import com.nordicpeak.flowengine.FlowAdminModule;
import com.nordicpeak.flowengine.FlowInstanceAdminModule;
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
import com.nordicpeak.flowengine.flowapprovalmodule.beans.FlowApprovalActivityRound;
import com.nordicpeak.flowengine.flowapprovalmodule.beans.FlowApprovalReminder;
import com.nordicpeak.flowengine.flowapprovalmodule.beans.ReminderType;
import com.nordicpeak.flowengine.flowapprovalmodule.cruds.FlowApprovalActivityCRUD;
import com.nordicpeak.flowengine.flowapprovalmodule.cruds.FlowApprovalActivityGroupCRUD;
import com.nordicpeak.flowengine.flowapprovalmodule.enums.Comment;
import com.nordicpeak.flowengine.flowapprovalmodule.validationerrors.ActivityGroupInvalidStatus;
import com.nordicpeak.flowengine.flowapprovalmodule.validationerrors.AssignableGroupNotFound;
import com.nordicpeak.flowengine.flowapprovalmodule.validationerrors.AssignableUserNotFound;
import com.nordicpeak.flowengine.flowapprovalmodule.validationerrors.ResponsibleFallbackUserNotFound;
import com.nordicpeak.flowengine.flowapprovalmodule.validationerrors.ResponsibleGroupNotFound;
import com.nordicpeak.flowengine.flowapprovalmodule.validationerrors.ResponsibleUserNotFound;
import com.nordicpeak.flowengine.flowapprovalmodule.validationerrors.StatusNotFound;
import com.nordicpeak.flowengine.interfaces.FlowAdminFragmentExtensionViewProvider;
import com.nordicpeak.flowengine.interfaces.ImmutableFlowInstance;
import com.nordicpeak.flowengine.interfaces.PDFProvider;
import com.nordicpeak.flowengine.managers.FlowInstanceManager;
import com.nordicpeak.flowengine.notifications.StandardFlowNotificationHandler;
import com.nordicpeak.flowengine.utils.FlowInstanceUtils;
import com.nordicpeak.flowengine.utils.PDFXMLUtils;

import it.sauronsoftware.cron4j.Scheduler;

public class FlowApprovalAdminModule extends AnnotatedForegroundModule implements FlowAdminFragmentExtensionViewProvider, ViewFragmentModule<ForegroundModuleDescriptor>, CRUDCallback<User>, Runnable {

	private static final AnnotatedBeanTagSourceFactory<FlowApprovalActivityGroup> ACTIVITY_GROUP_TAG_SOURCE_FACTORY = new AnnotatedBeanTagSourceFactory<>(FlowApprovalActivityGroup.class, "$activityGroup.");
	private static final AnnotatedBeanTagSourceFactory<User> MANAGER_TAG_SOURCE_FACTORY = new AnnotatedBeanTagSourceFactory<>(User.class, "$manager.");
	private static final AnnotatedBeanTagSourceFactory<FlowInstance> FLOW_INSTANCE_TAG_SOURCE_FACTORY = new AnnotatedBeanTagSourceFactory<>(FlowInstance.class, "$flowInstance.");
	private static final AnnotatedBeanTagSourceFactory<Flow> FLOW_TAG_SOURCE_FACTORY = new AnnotatedBeanTagSourceFactory<>(Flow.class, "$flow.");

	private static final ITextPDFCreationListener ITEXT_PDF_CREATION_LISTENER = new ITextPDFCreationListener();

	@XSLVariable(prefix = "java.")
	private String adminExtensionViewTitle = "Flow approval settings";

	@XSLVariable(prefix = "java.")
	private String copySuffix = " (copy)";

	@XSLVariable(prefix = "java.")
	private String signaturesFilename = "signature";

	@XSLVariable(prefix = "java.")
	private String pdfSignatureAttachment = "Signature";

	@XSLVariable(prefix = "java.")
	private String pdfSigningDataAttachment = "Signed data";

	@XSLVariable(prefix = "java.")
	private String eventActivityGroupAdded;

	@XSLVariable(prefix = "java.")
	private String eventActivityGroupUpdated;

	@XSLVariable(prefix = "java.")
	private String eventActivityGroupDeleted;

	@XSLVariable(prefix = "java.")
	private String eventActivityGroupCopied;

	@XSLVariable(prefix = "java.")
	private String eventActivityGroupsSorted;

	@XSLVariable(prefix = "java.")
	private String eventActivityAdded;

	@XSLVariable(prefix = "java.")
	private String eventActivityUpdated;

	@XSLVariable(prefix = "java.")
	private String eventActivityDeleted;

	@XSLVariable(prefix = "java.")
	private String eventActivityCopied;

	@XSLVariable(prefix = "java.")
	private String eventActivityGroupStarted;

	@XSLVariable(prefix = "java.")
	private String eventActivityGroupCancelled;

	@XSLVariable(prefix = "java.")
	private String eventActivityGroupCompleted;

	@XSLVariable(prefix = "java.")
	private String eventActivityGroupCompletedMissingStatus;

	@XSLVariable(prefix = "java.")
	private String eventActivityGroupApproved;

	@XSLVariable(prefix = "java.")
	private String eventActivityGroupDenied;

	@XSLVariable(prefix = "java.")
	private String eventActivityGroupSkipped;

	@XSLVariable(prefix = "java.")
	private String eventActivityOwnerChanged;

	@XSLVariable(prefix = "java.")
	private String eventActivityOwnerDefault = "default";
	
	@XSLVariable(prefix = "java.")
	private String whenToCommentAlways;
	
	@XSLVariable(prefix = "java.")
	private String whenToCommentDuringDeny;

	@ModuleSetting
	@TextFieldSettingDescriptor(name = "PDF dir", description = "The directory where PDF files be stored", required = true)
	protected String pdfDir;

	@ModuleSetting
	@TextFieldSettingDescriptor(name = "Temp dir", description = "The directory where temporary files be stored", required = true)
	protected String tempDir;

	@ModuleSetting
	@TextFieldSettingDescriptor(name = "Signature PDF XSL stylesheet", description = "The path in classpath relative from this class to the XSL stylesheet used to transform the XHTML for PDF output", required = true)
	protected String pdfStyleSheet = "FlowApprovalSignaturesPDF.sv.xsl";

	@ModuleSetting
	@TextFieldSettingDescriptor(name = "Priority", description = "The priority of this extension provider compared to other providers. A low value means a higher priority. Valid values are 0 - " + Integer.MAX_VALUE + ".", required = true, formatValidator = NonNegativeStringIntegerValidator.class)
	protected int priority = 0;

	@ModuleSetting
	@CheckboxSettingDescriptor(name = "Enable fragment XML debug", description = "Enables debugging of fragment XML")
	private boolean debugFragmentXML;

	//@ModuleSetting
	//@TextFieldSettingDescriptor(name = "User approval module URL", description = "The full URL of the user approval module", required = true)
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
	@TextFieldSettingDescriptor(name = "Activity group started email subject", description = "The subject of emails sent to the global adresses when an activity group is completed", required = true)
	@XSLVariable(prefix = "java.")
	private String activityGroupCompletedEmailSubject;

	@ModuleSetting
	@HTMLEditorSettingDescriptor(name = "Activity group started email message", description = "The message of emails sent to the global adresses when an activity group is completed", required = true)
	@XSLVariable(prefix = "java.")
	private String activityGroupCompletedEmailMessage;

	@ModuleSetting
	@TextFieldSettingDescriptor(name = "Activity reminder email subject prefix", description = "The subject prefix of emails sent to remind users of an activity", required = true)
	@XSLVariable(prefix = "java.")
	private String reminderEmailPrefix;

	@ModuleSetting
	@TextFieldSettingDescriptor(name = "Check for expiring managers interval", description = "How often this module should check for expiring flow managers (specified in crontab format)", required = true, formatValidator = CronStringValidator.class)
	private String managersUpdateInterval = "0 0 * * *";

	@ModuleSetting
	@CheckboxSettingDescriptor(name = "Send reminders repeatedly", description = "Controls wheter to send reminder emails repeatadly or not")
	private boolean sendRemindersRepeatedly = false;

	@ModuleSetting(allowsNull = true)
	@TextAreaSettingDescriptor(name = "Included fonts", description = "Path to the fonts that should be included in the PDF (the paths can be either in filesystem or classpath)")
	private List<String> includedFonts;

	@ModuleSetting
	@TextFieldSettingDescriptor(name = "Max request size", description = "Maximum allowed request size in megabytes", required = true, formatValidator = PositiveStringIntegerValidator.class)
	protected Integer maxRequestSize = 1000;

	@ModuleSetting
	@TextFieldSettingDescriptor(name = "RAM threshold", description = "Maximum size of files in KB to be buffered in RAM during file uploads. Files exceeding the threshold are written to disk instead.", required = true, formatValidator = PositiveStringIntegerValidator.class)
	protected Integer ramThreshold = 500;

	private AnnotatedDAO<FlowApprovalActivity> activityDAO;
	private AnnotatedDAO<FlowApprovalActivityGroup> activityGroupDAO;
	private AnnotatedDAO<FlowApprovalActivityRound> activityRoundDAO;
	private AnnotatedDAO<FlowApprovalActivityProgress> activityProgressDAO;
	private AnnotatedDAO<FlowApprovalReminder> reminderDAO;

	private AdvancedAnnotatedDAOWrapper<FlowApprovalActivity, Integer> activityDAOWrapper;
	private AdvancedAnnotatedDAOWrapper<FlowApprovalActivityGroup, Integer> activityGroupDAOWrapper;

	private QueryParameterFactory<FlowApprovalActivityRound, Integer> activityRoundFlowInstanceIDParamFactory;
	private QueryParameterFactory<FlowApprovalActivityRound, FlowApprovalActivityGroup> activityRoundActivityGroupParamFactory;
	private QueryParameterFactory<FlowApprovalActivityGroup, Integer> activityGroupFlowFamilyIDParamFactory;
	private QueryParameterFactory<FlowApprovalActivityGroup, String> activityGroupStartStatusParamFactory;

	private OrderByCriteria<FlowApprovalActivityRound> activityRoundIDOrderBy;

	@InstanceManagerDependency(required = true)
	private StaticContentModule staticContentModule;
	
	@InstanceManagerDependency(required = true)
	protected FlowInstanceAdminModule flowInstanceAdminModule;

	@InstanceManagerDependency
	protected PDFProvider pdfProvider;

	protected StandardFlowNotificationHandler notificationHandler;

	private FlowAdminModule flowAdminModule;

	private ModuleViewFragmentTransformer<ForegroundModuleDescriptor> viewFragmentTransformer;
	private UserGroupListConnector userGroupListConnector;

	private FlowApprovalActivityCRUD activityCRUD;
	private FlowApprovalActivityGroupCRUD activityGroupCRUD;

	private Scheduler scheduler;
	private String updateManagersScheduleID;

	protected URIXSLTransformer pdfTransformer;

	private HierarchyAnnotatedDAOFactory daoFactory;

	@Override
	public void init(ForegroundModuleDescriptor moduleDescriptor, SectionInterface sectionInterface, DataSource dataSource) throws Exception {

		viewFragmentTransformer = new ModuleViewFragmentTransformer<>(sectionInterface.getForegroundModuleXSLTCache(), this, sectionInterface.getSystemInterface().getEncoding());

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

		daoFactory = new HierarchyAnnotatedDAOFactory(dataSource, systemInterface.getUserHandler(), systemInterface.getGroupHandler(), false, true, false);

		activityDAO = daoFactory.getDAO(FlowApprovalActivity.class);
		activityGroupDAO = daoFactory.getDAO(FlowApprovalActivityGroup.class);
		activityRoundDAO = daoFactory.getDAO(FlowApprovalActivityRound.class);
		activityProgressDAO = daoFactory.getDAO(FlowApprovalActivityProgress.class);
		reminderDAO = daoFactory.getDAO(FlowApprovalReminder.class);

		activityDAOWrapper = activityDAO.getAdvancedWrapper(Integer.class);
		activityDAOWrapper.getAddQuery().addRelations(FlowApprovalActivity.RESPONSIBLE_USERS_RELATION, FlowApprovalActivity.RESPONSIBLE_GROUPS_RELATION, FlowApprovalActivity.RESPONSIBLE_FALLBACK_RELATION, FlowApprovalActivity.ASSIGNABLE_USERS_RELATION, FlowApprovalActivity.ASSIGNABLE_GROUPS_RELATION);
		activityDAOWrapper.getUpdateQuery().addRelations(FlowApprovalActivity.RESPONSIBLE_USERS_RELATION, FlowApprovalActivity.RESPONSIBLE_GROUPS_RELATION, FlowApprovalActivity.RESPONSIBLE_FALLBACK_RELATION, FlowApprovalActivity.ASSIGNABLE_USERS_RELATION, FlowApprovalActivity.ASSIGNABLE_GROUPS_RELATION);
		activityDAOWrapper.getGetQuery().addRelations(FlowApprovalActivity.ACTIVITY_GROUP_RELATION, FlowApprovalActivity.RESPONSIBLE_USERS_RELATION, FlowApprovalActivity.RESPONSIBLE_GROUPS_RELATION, FlowApprovalActivity.RESPONSIBLE_FALLBACK_RELATION, FlowApprovalActivity.ASSIGNABLE_USERS_RELATION, FlowApprovalActivity.ASSIGNABLE_GROUPS_RELATION);

		activityGroupDAOWrapper = activityGroupDAO.getAdvancedWrapper(Integer.class);
		activityGroupDAOWrapper.getGetQuery().addRelations(FlowApprovalActivityGroup.ACTIVITIES_RELATION, FlowApprovalActivity.RESPONSIBLE_USERS_RELATION, FlowApprovalActivity.RESPONSIBLE_GROUPS_RELATION, FlowApprovalActivity.RESPONSIBLE_FALLBACK_RELATION,FlowApprovalActivity.ASSIGNABLE_USERS_RELATION, FlowApprovalActivity.ASSIGNABLE_GROUPS_RELATION);
		activityGroupDAOWrapper.getAddQuery().addRelations(FlowApprovalActivityGroup.ACTIVITIES_RELATION, FlowApprovalActivity.RESPONSIBLE_USERS_RELATION, FlowApprovalActivity.RESPONSIBLE_GROUPS_RELATION, FlowApprovalActivity.RESPONSIBLE_FALLBACK_RELATION,FlowApprovalActivity.ASSIGNABLE_USERS_RELATION, FlowApprovalActivity.ASSIGNABLE_GROUPS_RELATION);
		activityGroupDAOWrapper.getUpdateQuery().addRelations(FlowApprovalActivityGroup.ACTIVITIES_RELATION, FlowApprovalActivity.RESPONSIBLE_USERS_RELATION, FlowApprovalActivity.RESPONSIBLE_GROUPS_RELATION, FlowApprovalActivity.RESPONSIBLE_FALLBACK_RELATION,FlowApprovalActivity.ASSIGNABLE_USERS_RELATION, FlowApprovalActivity.ASSIGNABLE_GROUPS_RELATION);

		activityRoundFlowInstanceIDParamFactory = activityRoundDAO.getParamFactory("flowInstanceID", Integer.class);
		activityRoundActivityGroupParamFactory = activityRoundDAO.getParamFactory("activityGroup", FlowApprovalActivityGroup.class);

		activityGroupFlowFamilyIDParamFactory = activityGroupDAO.getParamFactory("flowFamilyID", Integer.class);
		activityGroupStartStatusParamFactory = activityGroupDAO.getParamFactory("startStatus", String.class);

		activityRoundIDOrderBy = activityRoundDAO.getOrderByCriteria("activityRoundID", Order.DESC);

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

		if (ModuleUtils.checkRequiredModuleSettings(moduleDescriptor, this, systemInterface, Level.ERROR)) {

			File pdfDirFile = new File(pdfDir);

			if (!pdfDirFile.exists()) {

				if (pdfDirFile.getParentFile().exists()) {

					log.info("Creating missing  " + pdfDirFile.getName() + " directory for PDF filestore directory");

					if (!pdfDirFile.mkdir()) {

						log.error("Unable to create missing " + pdfDirFile.getName() + " directory for PDF filestore directory");
					}

				} else {

					log.error("The set PDF filestore directory does not exist");
				}
			}

			if (!FileUtils.directoryExists(tempDir)) {

				log.error("The set temp directory does not exist");
			}
		}

		if (pdfStyleSheet == null) {

			pdfTransformer = null;

		} else {

			URL styleSheetURL = FlowApprovalAdminModule.class.getResource(pdfStyleSheet);

			if (styleSheetURL != null) {

				try {
					pdfTransformer = new URIXSLTransformer(styleSheetURL.toURI(), ClassPathURIResolver.getInstance(), true);

					log.info("Succesfully parsed PDF stylesheet " + pdfStyleSheet);

				} catch (Exception e) {

					log.error("Unable to cache PDF style sheet " + pdfStyleSheet, e);

					pdfTransformer = null;
				}

			} else {
				log.error("Unable to cache PDF style sheet. Resource " + pdfStyleSheet + " not found");
			}
		}

		viewFragmentTransformer.setDebugXML(debugFragmentXML);
		viewFragmentTransformer.modifyScriptsAndLinks(true, null);
	}

	protected synchronized void initScheduler() {

		if (scheduler != null) {

			log.warn("Invalid state, scheduler already running!");
			stopScheduler();
		}

		scheduler = new Scheduler(systemInterface.getApplicationName() + " - " + moduleDescriptor.toString());
		scheduler.setDaemon(true);
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

				if (flow.getStatuses() != null) {

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

		} else if ("importactivitygroup".equals(method)) {

			return importActivityGroup(extensionRequestURL, req, res, user, uriParser);
		} else if ("exportactivitygroup".equals(method)) {

			return exportActivityGroup(req, res, user, uriParser);
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

		} else if ("copyactivity".equals(method)) {

			return copyActivity(flow, req, res, user, uriParser);

		} else if ("copyactivitygroup".equals(method)) {

			return copyActivityGroup(flow, req, res, user, uriParser);

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

	public ViewFragment exportActivityGroup(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws TransformerFactoryConfigurationError, Exception {

		FlowApprovalActivityGroup activityGroup = activityGroupCRUD.getRequestedBean(req, res, user, uriParser, GenericCRUD.UPDATE);

		if (activityGroup == null) {

			return getViewFragmentResponse(activityGroupCRUD.show(req, res, user, uriParser, Arrays.asList(new ValidationError("ShowFailedActivityGroupNotFound"))));

		}

		checkAccess(user, req, uriParser);

		log.info("User " + user + " exporting activityGroup " + activityGroup);

		List<ValidationError> validationErrors = new ArrayList<>();

		Document doc = getExportActivityGroupDocument(activityGroup, validationErrors);

		if (!validationErrors.isEmpty()) {

			return getViewFragmentResponse(activityGroupCRUD.showBean(activityGroup, req, res, user, uriParser, validationErrors));
		}

		res.setHeader("Content-Disposition", "attachment; filename=\"" + FileUtils.toValidHttpFilename(activityGroup.getName()) + ".oeactgroup\"");
		res.setHeader("Cache-Control", "no-store, no-cache, must-revalidate, proxy-revalidate");
		res.setContentType("text/oeflow");

		try {
			XMLUtils.writeXML(doc, res.getOutputStream(), true, systemInterface.getEncoding());
		} catch (TransformerException e) {

			log.info("Error sending exported flow " + activityGroup + " to user " + user + ", " + e);
		}

		return null;
	}

	private void checkAccess(User user, HttpServletRequest req, URIParser uriParser) throws URINotFoundException, AccessDeniedException {

		Flow flow = (Flow) req.getAttribute("flow");
		if (flow == null) {

			throw new URINotFoundException(uriParser);
		} else if (!hasFlowAccess(user, flow)) {

			throw new AccessDeniedException("User does not have access to flow type " + flow.getFlowType());
		}
	}

	private boolean hasFlowAccess(User user, Flow flow) {

		return AccessUtils.checkAccess(user, flow.getFlowType().getAdminAccessInterface());
	}

	public Document getExportActivityGroupDocument(FlowApprovalActivityGroup activityGroup, List<ValidationError> validationErrors) throws IOException, SQLException {

		Document doc = XMLUtils.createDomDocument();

		XMLGeneratorDocument xmlGeneratorDocument = new XMLGeneratorDocument(doc);

		xmlGeneratorDocument.addIgnoredField(FlowApprovalActivityGroup.ACTIVITIES_RELATION);

		Element activityGroupNode = activityGroup.toXML(xmlGeneratorDocument);

		Element activities = doc.createElement("Activities");

		//secure user information, store only username and group name
		if (!CollectionUtils.isEmpty(activityGroup.getActivities())) {
			XMLGeneratorDocument xmlActivityGeneratorDocument = new XMLGeneratorDocument(doc);
			xmlActivityGeneratorDocument.addIgnoredField(FlowApprovalActivity.RESPONSIBLE_GROUPS_RELATION);
			xmlActivityGeneratorDocument.addIgnoredField(FlowApprovalActivity.RESPONSIBLE_FALLBACK_RELATION);
			xmlActivityGeneratorDocument.addIgnoredField(FlowApprovalActivity.RESPONSIBLE_USERS_RELATION);
			xmlActivityGeneratorDocument.addIgnoredField(FlowApprovalActivity.ASSIGNABLE_GROUPS_RELATION);
			xmlActivityGeneratorDocument.addIgnoredField(FlowApprovalActivity.ASSIGNABLE_USERS_RELATION);
			for (FlowApprovalActivity activity : activityGroup.getActivities()) {

				Element activityElement = activity.toXML(xmlActivityGeneratorDocument);
				
				List<User> cleanedResponsibleUsers = FlowApprovalActivity.clearUnknownUsers(activity.getResponsibleUsers());

				if (!CollectionUtils.isEmpty(cleanedResponsibleUsers)) {
					Element responsibleUsers = doc.createElement("ResponsibleUsers");

					for (User user : cleanedResponsibleUsers) {
						Element responsibleUser = doc.createElement("user");

						if (user != null) {
							XMLUtils.appendNewCDATAElement(doc, responsibleUser, "username", user.getUsername());
						}

						responsibleUsers.appendChild(responsibleUser);
					}
					activityElement.appendChild(responsibleUsers);

				}
				
				List<Group> cleanedResponsibleGroups = FlowApprovalActivity.clearUnknownGroups(activity.getResponsibleGroups());

				if (!CollectionUtils.isEmpty(cleanedResponsibleGroups)) {
					Element responsibleGroups = doc.createElement("ResponsibleGroups");

					for (Group group : cleanedResponsibleGroups) {
						Element responsibleGroup = doc.createElement("group");

						if (group != null) {
							XMLUtils.appendNewCDATAElement(doc, responsibleGroup, "name", group.getName());
						}

						responsibleGroups.appendChild(responsibleGroup);
					}
					activityElement.appendChild(responsibleGroups);

				}
				
				List<User> cleanedAssignableUsers = FlowApprovalActivity.clearUnknownUsers(activity.getAssignableUsers());

				if (!CollectionUtils.isEmpty(cleanedAssignableUsers)) {
					Element assignableUsers = doc.createElement("AssignableUsers");

					for (User user : cleanedAssignableUsers) {
						Element assignableUser = doc.createElement("user");

						if (user != null) {
							XMLUtils.appendNewCDATAElement(doc, assignableUser, "username", user.getUsername());
						}

						assignableUsers.appendChild(assignableUser);
					}
					activityElement.appendChild(assignableUsers);

				}
				
				List<Group> cleanedAssignableGroups = FlowApprovalActivity.clearUnknownGroups(activity.getAssignableGroups());

				if (!CollectionUtils.isEmpty(cleanedAssignableGroups)) {
					Element assignableGroups = doc.createElement("AssignableGroups");

					for (Group group : cleanedAssignableGroups) {
						Element assignableGroup = doc.createElement("group");

						if (group != null) {
							XMLUtils.appendNewCDATAElement(doc, assignableGroup, "name", group.getName());
						}

						assignableGroups.appendChild(assignableGroup);
					}
					activityElement.appendChild(assignableGroups);

				}
				
				List<User> cleanedResponsibleFallbackUsers = FlowApprovalActivity.clearUnknownUsers(activity.getResponsibleFallbackUsers());

				if (!CollectionUtils.isEmpty(cleanedResponsibleFallbackUsers)) {
					Element responsibleFallbackUsers = doc.createElement("ResponsibleFallbackUsers");

					for (User user : cleanedResponsibleFallbackUsers) {
						Element responsibleGroupFallbackUser = doc.createElement("user");

						if (user != null) {
							XMLUtils.appendNewCDATAElement(doc, responsibleGroupFallbackUser, "username", user.getUsername());
						}

						responsibleFallbackUsers.appendChild(responsibleGroupFallbackUser);
					}
					activityElement.appendChild(responsibleFallbackUsers);

				}

				activities.appendChild(activityElement);
			}
		}

		activityGroupNode.appendChild(activities);

		doc.appendChild(activityGroupNode);

		return doc;
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

		Set<String> statusNames = new HashSet<>();

		String searchLower = search.toLowerCase();

		if (flow.getStatuses() != null) {

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

	private ViewFragment sortActivityGroups(String extensionRequestURL, Flow flow, HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws SQLException, TransformerConfigurationException, TransformerException {

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

		XMLUtils.appendNewElement(doc, sortActivityGroupsElement, "extensionRequestURL", extensionRequestURL);

		XMLUtils.append(doc, sortActivityGroupsElement, "ActivityGroups", activityGroups);

		return viewFragmentTransformer.createViewFragment(doc);
	}

	private synchronized ViewFragment importActivityGroup(String extensionRequestURL, HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws TransformerException, ValidationException, URINotFoundException, AccessDeniedException, IOException {

		checkAccess(user, req, uriParser);

		ValidationException validationException = null;

		if (req.getMethod().equalsIgnoreCase("POST")) {
			
			FlowApprovalActivityGroup flowApprovalActivityGroup = null;

			log.info("User " + user + " importing activitygroup...");

			MultipartRequest multipartRequest = null;

			try {
				multipartRequest = MultipartRequest.getMultipartRequest(ramThreshold * BinarySizes.KiloByte, (long) maxRequestSize * BinarySizes.MegaByte, tempDir, req);
				req = multipartRequest;

				if (multipartRequest.getFileCount() == 0 || (multipartRequest.getFileCount() == 1 && multipartRequest.getFile(0).getName().equals(""))) {

					throw new ValidationException(new ValidationError("NoAttachedFile"));
				}

				FileItem fileItem = multipartRequest.getFile(0);

				if (!fileItem.getName().endsWith(".oeactgroup")) {

					throw new ValidationException(new InvalidFileExtensionValidationError(FilenameUtils.getName(fileItem.getName()), "oeactgroup"));
				}

				try (InputStream inputStream = fileItem.getInputStream()) {

					flowApprovalActivityGroup = importActivityGroup(inputStream, FilenameUtils.getName(fileItem.getName()), multipartRequest, res, user, (Flow) req.getAttribute("flow"));

				}

			} catch (ValidationException e) {

				validationException = e;

			} catch (SizeLimitExceededException e) {

				validationException = new ValidationException(new RequestSizeLimitExceededValidationError(e.getActualSize(), e.getPermittedSize()));

			} catch (FileSizeLimitExceededException e) {

				validationException = new ValidationException(new FileSizeLimitExceededValidationError(e.getFileName(), e.getActualSize(), e.getPermittedSize()));

			} catch (Exception e) {

				validationException = new ValidationException(new ValidationError("UnableToParseRequest"));

			} finally {

				if (multipartRequest != null) {

					multipartRequest.deleteFiles();
				}

			}

			if (validationException != null) {

				Document doc = createDocument(req, uriParser, user);

				Element importActivityGroupsElement = doc.createElement("ImportActivityGroup");
				doc.getDocumentElement().appendChild(importActivityGroupsElement);

				XMLUtils.appendNewElement(doc, importActivityGroupsElement, "extensionRequestURL", extensionRequestURL);
				importActivityGroupsElement.appendChild(validationException.toXML(doc));

				return viewFragmentTransformer.createViewFragment(doc);
			}
			
						
			res.sendRedirect(req.getContextPath() + req.getAttribute("extensionRequestURL") + "/showactivitygroup/" + flowApprovalActivityGroup.getActivityGroupID());
			

		}

		Document doc = createDocument(req, uriParser, user);

		Element importActivityGroupsElement = doc.createElement("ImportActivityGroup");
		doc.getDocumentElement().appendChild(importActivityGroupsElement);

		XMLUtils.appendNewElement(doc, importActivityGroupsElement, "extensionRequestURL", extensionRequestURL);

		return viewFragmentTransformer.createViewFragment(doc);
	}

	private synchronized FlowApprovalActivityGroup importActivityGroup(InputStream inputStream, String filename, HttpServletRequest req, HttpServletResponse res, User user, Flow flow) throws Exception {

		Document doc = null;

		try {
			doc = XMLUtils.parseXML(inputStream, false, false);

		} catch (Exception e) {

			log.info("Unable to parse file " + filename, e);

			throw new ValidationException(new UnableToParseFileValidationError(filename));
		}

		Element docElement = doc.getDocumentElement();

		if (!docElement.getTagName().equals("ActivityGroup")) {

			log.info("Error parsing file " + filename + ", unable to find flow element");

			throw new ValidationException(new UnableToParseFileValidationError(filename));
		}

		FlowApprovalActivityGroup activityGroup = FlowApprovalActivityGroup.class.newInstance();

		XMLParser xmlParser = new XMLParser(docElement);

		activityGroup.populate(xmlParser);

		activityGroup.setFlowFamilyID(flow.getFlowFamily().getFlowFamilyID());
		activityGroup.setSortIndex(1 + getApprovalGroupMaxSortIndex(flow.getFlowFamily()));
		
		validateStatusUsersAndGroups(activityGroup, flow.getStatuses(), req);

		//Clear activity IDs
		if (!CollectionUtils.isEmpty(activityGroup.getActivities())) {

			for (FlowApprovalActivity flowApprovalActivity : activityGroup.getActivities()) {

				flowApprovalActivity.setActivityID(null);
			}
		}

		//Add activitygroup to database
		try {

			activityGroupDAOWrapper.add(activityGroup);

		} catch (SQLException e) {

			throw e;
		}

		log.info("User " + user + " succefully imported activityGroup " + activityGroup);
		return activityGroup;

	}

	private void validateStatusUsersAndGroups(FlowApprovalActivityGroup activityGroup, List<Status> statusList, HttpServletRequest req) throws ValidationException {

		List<ValidationError> errors = new ArrayList<>();

		String completeStatus = activityGroup.getCompleteStatus();

		if (CollectionUtils.isEmpty(statusList) || statusList.stream().noneMatch(e -> e.getName().equals(completeStatus))) {
			errors.add(new StatusNotFound(completeStatus));
		}

		if(activityGroup.isUseApproveDeny()) {
			
			String denyStatus = activityGroup.getDenyStatus();
			if (CollectionUtils.isEmpty(statusList) || statusList.stream().noneMatch(e -> e.getName().equals(denyStatus))) {
				errors.add(new StatusNotFound(denyStatus));
			}
		}
		
		String startStatus = activityGroup.getStartStatus();
		if (CollectionUtils.isEmpty(statusList) || statusList.stream().noneMatch(e -> e.getName().equals(startStatus))) {
			errors.add(new StatusNotFound(startStatus));
		}

		String importUsers = req.getParameter("importUsers");
		String importGroups = req.getParameter("importGroups");

		if (!CollectionUtils.isEmpty(activityGroup.getActivities())) {
			List<User> allUsers = getUserHandler().getUsers(false, true);
			List<Group> allGroups = getGroupHandler().getGroups(false);

			for (FlowApprovalActivity activity : activityGroup.getActivities()) {

				if (importUsers == null) {

					if (!CollectionUtils.isEmpty(activity.getResponsibleUsers())) {
						//check that user with assigned username exists
						activity.getResponsibleUsers().forEach(e -> {
							Optional<User> foundUser = allUsers.stream().filter(e1 -> e.getUsername().equals(e1.getUsername())).findFirst();
							if (foundUser.isPresent()) {
								((SimpleUser) e).setUserID(foundUser.get().getUserID());

							} else {
								errors.add(new ResponsibleUserNotFound(e.getUsername()));
							}

						});
					}

					if (!CollectionUtils.isEmpty(activity.getAssignableUsers())) {
						//check that user with assigned username exists
						activity.getAssignableUsers().forEach(e -> {
							Optional<User> foundUser = allUsers.stream().filter(e1 -> e.getUsername().equals(e1.getUsername())).findFirst();
							if (foundUser.isPresent()) {
								((SimpleUser) e).setUserID(foundUser.get().getUserID());

							} else {
								errors.add(new AssignableUserNotFound(e.getUsername()));
							}

						});

					}
				} else {
					activity.setResponsibleUsers(null);
					activity.setAssignableUsers(null);
					activity.setActive(false);
				}

				if (importGroups == null) {
					if (!CollectionUtils.isEmpty(activity.getResponsibleGroups())) {
						//check that groups with assigned name exists
						activity.getResponsibleGroups().forEach(e -> {
							Optional<Group> foundGroup = allGroups.stream().filter(e1 -> e.getName().equals(e1.getName())).findFirst();
							if (foundGroup.isPresent()) {
								((SimpleGroup) e).setGroupID(foundGroup.get().getGroupID());

							} else {
								errors.add(new ResponsibleGroupNotFound(e.getName()));
							}

						});
					}

					if (!CollectionUtils.isEmpty(activity.getAssignableGroups())) {
						//check that groups with assigned name exists
						activity.getAssignableGroups().forEach(e -> {
							Optional<Group> foundGroup = allGroups.stream().filter(e1 -> e.getName().equals(e1.getName())).findFirst();
							if (foundGroup.isPresent()) {
								((SimpleGroup) e).setGroupID(foundGroup.get().getGroupID());

							} else {
								errors.add(new AssignableGroupNotFound(e.getName()));
							}

						});
					}
					if (!CollectionUtils.isEmpty(activity.getResponsibleFallbackUsers())) {
						//check that user with assigned username exists
						activity.getResponsibleFallbackUsers().forEach(e -> {
							Optional<User> foundUser = allUsers.stream().filter(e1 -> e.getUsername().equals(e1.getUsername())).findFirst();
							if (foundUser.isPresent()) {
								((SimpleUser) e).setUserID(foundUser.get().getUserID());

							} else {
								errors.add(new ResponsibleFallbackUserNotFound(e.getUsername()));
							}

						});

					}

				} else {
					activity.setResponsibleGroups(null);
					activity.setResponsibleFallbackUsers(null);
					activity.setAssignableGroups(null);
					activity.setActive(false);
				}

			}

		}

		if (!errors.isEmpty()) {

			throw new ValidationException(errors);
		}

	}

	public void checkApprovalCompletion(URIParser uriParser, FlowApprovalActivityGroup modifiedActivityGroup, FlowInstance flowInstance) throws SQLException, ModuleConfigurationException {

		List<FlowApprovalActivityGroup> activityGroups = getActivityGroups(flowInstance);

		// Add history event if modifiedActivityGroup was completed
		for (FlowApprovalActivityGroup activityGroup : activityGroups) {

			if (activityGroup.getActivityGroupID().equals(modifiedActivityGroup.getActivityGroupID())) {

				FlowApprovalActivityRound round = getLatestActivityRound(activityGroup, flowInstance, FlowApprovalActivityRound.ACTIVITY_PROGRESSES_RELATION, FlowApprovalActivityProgress.ACTIVITY_RELATION);

				if (round != null && round.getActivityProgresses() != null) {

					if (round.getCompleted() != null || round.getCancelled() != null) {

						log.error("Attempt to checkApprovalCompletion on already completed or cancelled round " + round + ", " + modifiedActivityGroup);
						break;
					}

					boolean noPending = true;
					boolean denied = false;

					for (FlowApprovalActivityProgress activityProgress : round.getActivityProgresses()) {

						if (activityProgress.getCompleted() == null) {

							noPending = false;
							break;
						}

						if (activityGroup.isUseApproveDeny() && activityProgress.isDenied()) {

							denied = true;
						}
					}

					if (noPending) {

						round.setCompleted(TimeUtils.getCurrentTimestamp());
						round.setActivityGroup(activityGroup);

						try {
							if (activityGroup.isUseApproveDeny()) {

								if (denied) {

									log.info("Completed denied activity group " + activityGroup + " for " + flowInstance);
									flowAdminModule.getFlowInstanceEventGenerator().addFlowInstanceEvent(flowInstance, EventType.OTHER_EVENT, eventActivityGroupDenied + " " + activityGroup.getName(), null, null, getFlowInstanceEventAttributes(activityGroup, round, denied));

								} else {

									log.info("Completed approved activity group " + activityGroup + " for " + flowInstance);
									flowAdminModule.getFlowInstanceEventGenerator().addFlowInstanceEvent(flowInstance, EventType.OTHER_EVENT, eventActivityGroupApproved + " " + activityGroup.getName(), null, null, getFlowInstanceEventAttributes(activityGroup, round, denied));
								}

							} else {

								log.info("Completed activity group " + activityGroup + " for " + flowInstance);
								flowAdminModule.getFlowInstanceEventGenerator().addFlowInstanceEvent(flowInstance, EventType.OTHER_EVENT, eventActivityGroupCompleted + " " + activityGroup.getName(), null, null, getFlowInstanceEventAttributes(activityGroup, round, denied));
							}

							generateSignaturesPDF(uriParser, flowInstance, activityGroup, round);

						} finally {
							activityRoundDAO.update(round);
						}

						if (activityGroup.isSendActivityGroupCompletedEmail()) {
							sendActivityGroupCompletedNotifications(round.getActivityProgresses(), round, activityGroup, flowInstance);
						}
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

				FlowApprovalActivityRound round = getLatestActivityRound(activityGroup, flowInstance, FlowApprovalActivityRound.ACTIVITY_PROGRESSES_RELATION, FlowApprovalActivityProgress.ACTIVITY_RELATION);

				if (round != null && round.getActivityProgresses() != null) {

					for (FlowApprovalActivityProgress activityProgress : round.getActivityProgresses()) {

						if (activityProgress.getCompleted() == null) {

							noPending = false;
							break outer;
						}

						anyCompleted = true;
						activityGroup.setActivities(Collections.emptyList());

						if (activityGroup.isUseApproveDeny() && activityProgress.isDenied()) {

							denied = true;
						}
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

					log.info("All activities completed but denied for flow instance " + flowInstance + " and status " + currentStatus + ", new status " + newStatusName);

				} else {

					newStatusName = activityGroupsForCurrentStatus.get(0).getCompleteStatus();

					log.info("All activities completed successfully for flow instance " + flowInstance + " and status " + currentStatus + ", new status " + newStatusName);
				}

				Status newStatus = null;

				for (Status status : flowInstance.getFlow().getStatuses()) {

					if (status.getName().equalsIgnoreCase(newStatusName)) {
						newStatus = status;
						break;
					}
				}

				StringBuilder activityGroupNames = new StringBuilder();
				boolean suppressManagerNotifications = false;

				for (FlowApprovalActivityGroup activityGroup : activityGroupsForCurrentStatus) {

					if (activityGroup.getActivities() != null) {

						if (activityGroupNames.length() > 0) {
							activityGroupNames.append(", ");
						}

						activityGroupNames.append(activityGroup.getName());
					}

					if (activityGroup.isSuppressChangeStatusManagerNotifications()) {
						suppressManagerNotifications = true;
					}
				}

				if (newStatus == null) {

					if (denied) {

						log.warn("Unable to find denied status \"" + newStatusName + "\" for " + flowInstance);

					} else {

						log.warn("Unable to find complete status \"" + newStatusName + "\" for " + flowInstance);
					}

					flowAdminModule.getFlowInstanceEventGenerator().addFlowInstanceEvent(flowInstance, EventType.OTHER_EVENT, eventActivityGroupCompleted + " " + activityGroupNames.toString() + ". " + eventActivityGroupCompletedMissingStatus.replace("$status", newStatusName), null);

				} else {

					flowInstance.setStatus(newStatus);
					flowInstance.setLastStatusChange(TimeUtils.getCurrentTimestamp());

					flowAdminModule.getDAOFactory().getFlowInstanceDAO().update(flowInstance);

					FlowInstanceEvent flowInstanceEvent = flowAdminModule.getFlowInstanceEventGenerator().addFlowInstanceEvent(flowInstance, EventType.STATUS_UPDATED, null, null);

					systemInterface.getEventHandler().sendEvent(FlowInstance.class, new CRUDEvent<>(CRUDAction.UPDATE, flowInstance), this, EventTarget.ALL);
					systemInterface.getEventHandler().sendEvent(FlowInstance.class, new StatusChangedByManagerEvent(flowInstance, flowInstanceEvent, flowAdminModule.getSiteProfile(flowInstance), currentStatus, null, false, suppressManagerNotifications), this, EventTarget.ALL);
				}
			}
		}
	}

	public Map<String, String> getFlowInstanceEventAttributes(FlowApprovalActivityGroup activityGroup, FlowApprovalActivityRound round, boolean denied) {

		Map<String, String> eventAttributes = new HashMap<String, String>(2);

		eventAttributes.put("activityGroupID", activityGroup.getActivityGroupID().toString());
		eventAttributes.put("activityRoundID", round.getActivityRoundID().toString());
		eventAttributes.put("approved", String.valueOf(!denied));

		return eventAttributes;
	}

	private boolean isActivityActive(FlowApprovalActivity activity, ImmutableFlowInstance flowInstance) {
		
		if(!activity.isActive()) {
			return false;
		}

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

			return match != activity.isInverted();
		}

		return true;
	}

	private void startActivityGroups(FlowInstance flowInstance, Status newStatus) throws SQLException {

		List<FlowApprovalActivityGroup> activityGroups = getActivityGroups(flowInstance.getFlow().getFlowFamily().getFlowFamilyID(), newStatus.getName(), FlowApprovalActivityGroup.ACTIVITIES_RELATION, FlowApprovalActivity.RESPONSIBLE_USERS_RELATION, FlowApprovalActivity.RESPONSIBLE_GROUPS_RELATION, FlowApprovalActivity.RESPONSIBLE_FALLBACK_RELATION);

		if (activityGroups != null) {

			Timestamp now = TimeUtils.getCurrentTimestamp();

			StringBuilder activityGroupNames = new StringBuilder();

			for (FlowApprovalActivityGroup activityGroup : activityGroups) {

				if (activityGroup.getActivities() != null) {

					TransactionHandler transactionHandler = activityProgressDAO.createTransaction();

					FlowApprovalActivityRound oldRound = getLatestActivityRound(activityGroup, flowInstance);

					if (oldRound == null || oldRound.getCancelled() != null || oldRound.getCompleted() != null && activityGroup.isAllowRestarts()) {

						log.info("Starting activity group " + activityGroup + " for flow instance " + flowInstance);

						FlowApprovalActivityRound newRound = new FlowApprovalActivityRound();
						newRound.setActivityGroup(activityGroup);
						newRound.setFlowInstanceID(flowInstance.getFlowInstanceID());
						newRound.setAdded(TimeUtils.getCurrentTimestamp());

						activityRoundDAO.add(newRound, transactionHandler, null);

						try {

							Map<FlowApprovalActivity, FlowApprovalActivityProgress> createdActivities = new HashMap<>(activityGroup.getActivities().size());

							for (FlowApprovalActivity activity : activityGroup.getActivities()) {

								if (isActivityActive(activity, flowInstance)) {

									FlowApprovalActivityProgress progress = new FlowApprovalActivityProgress();
									progress.setActivityRound(newRound);
									progress.setActivity(activity);
									progress.setAdded(now);

									if (activity.getResponsibleUserAttributeNames() != null) {

										List<User> responsibleUsers = getResponsibleUsersFromAttribute(activity, flowInstance);

										if (responsibleUsers != null) {

											progress.setResponsibleAttributedUsers(responsibleUsers);
										}
									}
									
									if (activity.getResponsibleGroupAttributeNames() != null) {

										List<Group> responsibleGroups = getResponsibleGroupsFromAttribute(activity, flowInstance);

										if (responsibleGroups != null) {

											progress.setResponsibleAttributedGroups(responsibleGroups);
										}
									}

									activityProgressDAO.add(progress, transactionHandler, null);

									createdActivities.put(activity, progress);
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
			}

			if (activityGroupNames.length() > 0) {

				log.info("Started activity groups " + activityGroupNames.toString() + " for " + flowInstance);

				flowAdminModule.getFlowInstanceEventGenerator().addFlowInstanceEvent(flowInstance, EventType.OTHER_EVENT, eventActivityGroupStarted + " " + activityGroupNames.toString(), null);

			} else {

				// If no started/running activities for this status and allowskip then change status

				String nextStatusName = null;

				for (FlowApprovalActivityGroup activityGroup : activityGroups) {

					FlowApprovalActivityRound round = getLatestActivityRound(activityGroup, flowInstance, FlowApprovalActivityRound.ACTIVITY_PROGRESSES_RELATION, FlowApprovalActivityProgress.ACTIVITY_RELATION);

					if (round != null && round.getActivityProgresses() != null && round.getCompleted() == null && round.getCancelled() == null) {

						nextStatusName = null;
						break;
					}

					if (activityGroup.isAllowSkip()) {

						nextStatusName = activityGroup.getCompleteStatus();

						if (activityGroupNames.length() > 0) {
							activityGroupNames.append(", ");
						}

						activityGroupNames.append(activityGroup.getName());
					}
				}

				if (nextStatusName != null) {

					Status nextStatus = null;

					for (Status status : flowInstance.getFlow().getStatuses()) {

						if (status.getName().equalsIgnoreCase(nextStatusName)) {
							nextStatus = status;
							break;
						}
					}

					if (nextStatus == null) {

						log.warn("Unable to skip to next status, unable to find complete status \"" + nextStatusName + "\" for " + flowInstance);

					} else {

						boolean suppressManagerNotifications = false;

						for (FlowApprovalActivityGroup activityGroup : activityGroups) {
							if (activityGroup.isSuppressChangeStatusManagerNotifications()) {
								suppressManagerNotifications = true;
								break;
							}
						}

						log.info("Skipping activity groups " + activityGroupNames + " for " + flowInstance);

						flowInstance.setStatus(nextStatus);
						flowInstance.setLastStatusChange(TimeUtils.getCurrentTimestamp());

						flowAdminModule.getDAOFactory().getFlowInstanceDAO().update(flowInstance);

						flowAdminModule.getFlowInstanceEventGenerator().addFlowInstanceEvent(flowInstance, EventType.OTHER_EVENT, eventActivityGroupSkipped + " " + activityGroupNames.toString(), null);

						FlowInstanceEvent flowInstanceEvent = flowAdminModule.getFlowInstanceEventGenerator().addFlowInstanceEvent(flowInstance, EventType.STATUS_UPDATED, null, null);

						systemInterface.getEventHandler().sendEvent(FlowInstance.class, new CRUDEvent<>(CRUDAction.UPDATE, flowInstance), this, EventTarget.ALL);
						systemInterface.getEventHandler().sendEvent(FlowInstance.class, new StatusChangedByManagerEvent(flowInstance, flowInstanceEvent, flowAdminModule.getSiteProfile(flowInstance), newStatus, null, false, suppressManagerNotifications), this, EventTarget.ALL);
					}
				}
			}
		}
	}

	private void cancelInvalidActivityRounds(ImmutableFlowInstance flowInstance, Status newStatus) throws SQLException {

		List<FlowApprovalActivityRound> rounds = getLatestActivityRounds(flowInstance, FlowApprovalActivityRound.ACTIVITY_GROUP_RELATION);

		if (rounds != null) {

			StringBuilder activityGroupNames = new StringBuilder();

			for (FlowApprovalActivityRound round : rounds) {

				if (round.getCompleted() == null && round.getCancelled() == null && !round.getActivityGroup().getStartStatus().equalsIgnoreCase(newStatus.getName())) {

					log.info("Flowinstance status has changed, cancelling " + round + " for " + flowInstance);

					round.setCancelled(TimeUtils.getCurrentTimestamp());
					activityRoundDAO.update(round);

					if (activityGroupNames.length() > 0) {
						activityGroupNames.append(", ");
					}

					activityGroupNames.append(round.getActivityGroup().getName());
				}
			}

			if (activityGroupNames.length() > 0) {
				flowAdminModule.getFlowInstanceEventGenerator().addFlowInstanceEvent(flowInstance, EventType.OTHER_EVENT, eventActivityGroupCancelled + " " + activityGroupNames.toString(), null);
			}
		}
	}

	public void sendActivityGroupStartedNotifications(Map<FlowApprovalActivity, FlowApprovalActivityProgress> createdActivities, FlowApprovalActivityGroup activityGroup, ImmutableFlowInstance flowInstance, boolean reminder) throws SQLException {

		String subject = ObjectUtils.getFirstNotNull(activityGroup.getActivityGroupStartedEmailSubject(), activityGroupStartedEmailSubject);
		String message = ObjectUtils.getFirstNotNull(activityGroup.getActivityGroupStartedEmailMessage(), activityGroupStartedEmailMessage);

		if (subject == null || message == null) {

			log.warn("no subject or message set, unable to send notifications");
			return;
		}

		if (reminder) {
			subject = reminderEmailPrefix + subject;
		}

		List<TagSource> sharedTagSources = new ArrayList<>(4);

		sharedTagSources.add(ACTIVITY_GROUP_TAG_SOURCE_FACTORY.getTagSource(activityGroup));
		sharedTagSources.add(FLOW_INSTANCE_TAG_SOURCE_FACTORY.getTagSource((FlowInstance) flowInstance));
		sharedTagSources.add(FLOW_TAG_SOURCE_FACTORY.getTagSource((Flow) flowInstance.getFlow()));

		sharedTagSources.add(new SingleTagSource("$myActivitiesURL", getUserApprovalModuleAlias(flowInstance)));

		HashSet<User> managers = new HashSet<>();
		HashSet<String> globalRecipients = new HashSet<>();

		for (Entry<FlowApprovalActivity, FlowApprovalActivityProgress> entry : createdActivities.entrySet()) {

			FlowApprovalActivity activity = entry.getKey();
			FlowApprovalActivityProgress activityProgress = entry.getValue();

			if (!activity.isOnlyUseGlobalNotifications() || activity.getGlobalEmailAddress() == null) {

				//TODO these variables are not needed check managers.isEmpty() instead futher down in the code
				boolean useFallbackUsers = true;
				boolean useFallbackGroups = true;

				if (activityProgress.getResponsibleAttributedUsers() != null) {

					useFallbackUsers = false;
					managers.addAll(activityProgress.getResponsibleAttributedUsers());

				} else if (activity.getResponsibleUserAttributeNames() != null) {

					List<User> users = getResponsibleUsersFromAttribute(activity, flowInstance);

					if (users != null) {

						log.warn("Users not set on activity progress " + activityProgress + ", using attribute directly!");

						useFallbackUsers = false;
						managers.addAll(users);
					}
				}
				
				if (activityProgress.getResponsibleAttributedGroups() != null) {

					for (Group responsibleAttributedGroup : activityProgress.getResponsibleAttributedGroups()) {

						List<User> users = systemInterface.getUserHandler().getUsersByGroup(responsibleAttributedGroup.getGroupID(), true, true);
						
						if(users != null) {
							
							managers.addAll(users);
							useFallbackGroups = false;
						}
					}

				} else if (activity.getResponsibleGroupAttributeNames() != null) {

					List<Group> groups = getResponsibleGroupsFromAttribute(activity, flowInstance);

					if (groups != null) {

						log.warn("Groups not set on activity progress " + activityProgress + ", using attribute directly!");

						useFallbackGroups = false;
						for(Group group : groups) {
							managers.addAll(systemInterface.getUserHandler().getUsersByGroup(group.getGroupID(), true, true));
						}
					}
				}

				if (activity.getResponsibleUsers() != null) {

					for (User responsibleUser : activity.getResponsibleUsers()) {

						managers.add(responsibleUser);
					}
				}
				
				if (activity.getResponsibleFallbackUsers() != null && (useFallbackUsers && useFallbackGroups)) {
					
					for (User responsibleFallbackUser : activity.getResponsibleFallbackUsers()) {

						if(!managers.contains(responsibleFallbackUser)) {
						
							managers.add(responsibleFallbackUser);
						}
					}
				}
			}

			if (activity.getGlobalEmailAddress() != null) {

				globalRecipients.add(activity.getGlobalEmailAddress());
			}
		}

		log.info("Sending emails for started activity group " + activityGroup + " for flow instance " + flowInstance + " to " + managers.size() + " managers and " + globalRecipients.size() + " global recipients");

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
					email.setSenderName(notificationHandler.getEmailSenderName(flowInstance));
					email.setSenderAddress(notificationHandler.getEmailSenderAddress(flowInstance));
					email.setSubject(tagReplacer.replace(subject));
					email.setMessage(EmailUtils.addMessageBody(replaceTags(message, tagReplacer, flowInstance)));

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
					email.setSenderName(notificationHandler.getEmailSenderName(flowInstance));
					email.setSenderAddress(notificationHandler.getEmailSenderAddress(flowInstance));
					email.setSubject(tagReplacer.replace(subject));
					email.setMessage(EmailUtils.addMessageBody(replaceTags(message, tagReplacer, flowInstance)));

					systemInterface.getEmailHandler().send(email);

				} catch (Exception e) {

					log.info("Error generating/sending email " + email, e);
				}
			}
		}
	}

	public void sendActivityAssignedNotifications(List<User> newManagers, FlowApprovalActivityProgress activityProgress, FlowApprovalActivity activity, FlowApprovalActivityGroup activityGroup, ImmutableFlowInstance flowInstance, boolean reminder) throws SQLException {

		if (newManagers == null) {
			return;
		}

		String subject = ObjectUtils.getFirstNotNull(activityGroup.getActivityGroupStartedEmailSubject(), activityGroupStartedEmailSubject);
		String message = ObjectUtils.getFirstNotNull(activityGroup.getActivityGroupStartedEmailMessage(), activityGroupStartedEmailMessage);

		if (subject == null || message == null) {

			log.warn("no subject or message set, unable to send notifications");
			return;
		}

		if (reminder) {
			subject = reminderEmailPrefix + subject;
		}

		List<TagSource> sharedTagSources = new ArrayList<>(4);

		sharedTagSources.add(ACTIVITY_GROUP_TAG_SOURCE_FACTORY.getTagSource(activityGroup));
		sharedTagSources.add(FLOW_INSTANCE_TAG_SOURCE_FACTORY.getTagSource((FlowInstance) flowInstance));
		sharedTagSources.add(FLOW_TAG_SOURCE_FACTORY.getTagSource((Flow) flowInstance.getFlow()));

		sharedTagSources.add(new SingleTagSource("$myActivitiesURL", getUserApprovalModuleAlias(flowInstance)));

		log.info("Sending emails for assigned activity " + activityProgress + " for flow instance " + flowInstance + " to " + StringUtils.toCommaSeparatedString(newManagers));

		for (User manager : newManagers) {

			if (!StringUtils.isEmpty(manager.getEmail())) {

				TagReplacer tagReplacer = new TagReplacer();

				tagReplacer.addTagSources(sharedTagSources);
				tagReplacer.addTagSource(MANAGER_TAG_SOURCE_FACTORY.getTagSource(manager));
				tagReplacer.addTagSource(new SingleTagSource("$activities", activity.getName()));

				SimpleEmail email = new SimpleEmail(systemInterface.getEncoding());

				try {
					email.addRecipient(manager.getEmail());
					email.setMessageContentType(SimpleEmail.HTML);
					email.setSenderName(notificationHandler.getEmailSenderName(flowInstance));
					email.setSenderAddress(notificationHandler.getEmailSenderAddress(flowInstance));
					email.setSubject(tagReplacer.replace(subject));
					email.setMessage(EmailUtils.addMessageBody(replaceTags(message, tagReplacer, flowInstance)));

					systemInterface.getEmailHandler().send(email);

				} catch (Exception e) {

					log.info("Error generating/sending email " + email, e);
				}
			}
		}
	}

	public void sendActivityGroupCompletedNotifications(List<FlowApprovalActivityProgress> activityProgresses, FlowApprovalActivityRound round, FlowApprovalActivityGroup activityGroup, ImmutableFlowInstance flowInstance) throws SQLException {

		String subject = ObjectUtils.getFirstNotNull(activityGroup.getActivityGroupCompletedEmailSubject(), activityGroupCompletedEmailSubject);
		String message = ObjectUtils.getFirstNotNull(activityGroup.getActivityGroupCompletedEmailMessage(), activityGroupCompletedEmailMessage);

		if (subject == null || message == null) {

			log.warn("no subject or message set, unable to send notifications");
			return;
		}

		if (!activityGroup.getActivityGroupCompletedEmailAddresses().isEmpty()) {

			List<TagSource> sharedTagSources = new ArrayList<>(4);

			sharedTagSources.add(ACTIVITY_GROUP_TAG_SOURCE_FACTORY.getTagSource(activityGroup));
			sharedTagSources.add(FLOW_INSTANCE_TAG_SOURCE_FACTORY.getTagSource((FlowInstance) flowInstance));
			sharedTagSources.add(FLOW_TAG_SOURCE_FACTORY.getTagSource((Flow) flowInstance.getFlow()));
			sharedTagSources.add(new SingleTagSource("$myActivitiesURL", getUserApprovalModuleAlias(flowInstance)));

			log.info("Sending emails for completed activity group " + activityGroup + " for flow instance " + flowInstance + " to " + activityGroup.getActivityGroupCompletedEmailAddresses().size() + " global recipients");

			StringBuilder activitiesStringBuilder = new StringBuilder();

			for (FlowApprovalActivityProgress activityProgress : activityProgresses) {

				if (activitiesStringBuilder.length() > 0) {
					activitiesStringBuilder.append("<br/>");
				}

				activitiesStringBuilder.append(activityProgress.getActivity().getName());
			}

			sharedTagSources.add(new SingleTagSource("$activities", activitiesStringBuilder.toString()));

			TagReplacer tagReplacer = new TagReplacer(sharedTagSources);

			File pdfFile = null;

			if (activityGroup.isActivityGroupCompletedEmailAttachPDF()) {

				pdfFile = getSignaturesPDF(round);

				if (pdfFile == null || !pdfFile.exists()) {
					log.warn("PDF for " + round + " not found");

				} else if (notificationHandler.getFlowInstanceGlobalEmailAttachmentSizeLimit() != null && pdfFile.length() > notificationHandler.getFlowInstanceGlobalEmailAttachmentSizeLimit() * BinarySizes.MegaByte) {

					log.warn("PDF file (" + BinarySizeFormater.getFormatedSize(pdfFile.length()) + ") for activity round " + round + " exceeds the size limit of " + notificationHandler.getFlowInstanceGlobalEmailAttachmentSizeLimit() + " MB set for global email submit notifications and will not be attached to the generated email.");
					pdfFile = null;
				}
			}

			File flowInstancePDFFile = null;

			if (activityGroup.isActivityGroupCompletedEmailAttachFlowInstancePDF()) {

				flowInstancePDFFile = pdfProvider.getPDF(flowInstance.getFlowInstanceID(), FlowInstanceUtils.getLatestSubmitEvent(flowInstance).getEventID());

				if (flowInstancePDFFile == null || !flowInstancePDFFile.exists()) {

					log.warn("FlowInstance PDF for " + flowInstance + " not found");
				}
			}

			List<String> recipients = new ArrayList<String>(activityGroup.getActivityGroupCompletedEmailAddresses().size());

			for (String recipient : activityGroup.getActivityGroupCompletedEmailAddresses()) {

				if (EmailUtils.isValidEmailAddress(recipient)) {

					recipients.add(recipient);

				} else {

					recipient = AttributeTagUtils.replaceTags(recipient, flowInstance.getAttributeHandler());

					if (EmailUtils.isValidEmailAddress(recipient)) {

						recipients.add(recipient);

					} else {

						log.warn("The string \"" + recipient + "\" is not a valid email address, skipping recipient for email generated for completed activity group " + activityGroup + " for flow instance " + flowInstance);
					}
				}
			}

			for (String emailAdress : recipients) {

				SimpleEmail email = new SimpleEmail(systemInterface.getEncoding());

				try {
					email.addRecipient(emailAdress);
					email.setMessageContentType(SimpleEmail.HTML);
					email.setSenderName(notificationHandler.getEmailSenderName(flowInstance));
					email.setSenderAddress(notificationHandler.getEmailSenderAddress(flowInstance));
					email.setSubject(tagReplacer.replace(subject));
					email.setMessage(EmailUtils.addMessageBody(replaceTags(message, tagReplacer, flowInstance)));

					if (pdfFile != null) {

						String filename = flowInstance.getFlow().getName() + " - " + flowInstance.getFlowInstanceID() + " - " + round.getActivityGroup().getName() + " - " + signaturesFilename + " - " + round.getActivityRoundID() + ".pdf";

						email.add(new FileAttachment(pdfFile, FileUtils.toValidHttpFilename(filename)));
					}

					if (flowInstancePDFFile != null) {

						String filename = flowInstance.getFlow().getName() + " - " + flowInstance.getFlowInstanceID() + ".pdf";

						email.add(new FileAttachment(flowInstancePDFFile, FileUtils.toValidHttpFilename(filename)));
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
			startActivityGroups((FlowInstance) event.getFlowInstanceManager().getFlowInstance(), event.getFlowInstanceManager().getFlowState());

		} catch (Exception e) {
			log.error("Error processing SubmitEvent " + event, e);
		}
	}

	@EventListener(channel = FlowInstance.class, priority = 100)
	public void processStatusChangedEvent(StatusChangedByManagerEvent event, EventSource eventSource) {

		try {
			FlowInstance flowInstance = event.getFlowInstance();

			cancelInvalidActivityRounds(flowInstance, flowInstance.getStatus());
			startActivityGroups(flowInstance, flowInstance.getStatus());

		} catch (Exception e) {
			log.error("Error processing StatusChangedByManagerEvent " + event, e);
		}
	}

	@EventListener(channel = FlowFamily.class)
	public void processFlowFamilyEvent(CRUDEvent<FlowFamily> event, EventSource source) throws SQLException {

		if (event.getAction() == CRUDAction.DELETE) {

			for (FlowFamily flowFamily : event.getBeans()) {

				log.info("Deleting approval settings for " + flowFamily);

				HighLevelQuery<FlowApprovalActivityGroup> query = new HighLevelQuery<>();
				query.addParameter(activityGroupFlowFamilyIDParamFactory.getParameter(flowFamily.getFlowFamilyID()));

				activityGroupDAO.delete(query);
			}
		}
	}

	@EventListener(channel = FlowInstance.class)
	public void processFlowInstanceEvent(CRUDEvent<FlowInstance> event, EventSource source) throws SQLException {

		if (event.getAction() == CRUDAction.DELETE) {

			//TODO wrap loop in transaction and use transaction commit listener to delete files

			for (FlowInstance flowInstance : event.getBeans()) {

				if (flowInstance.getFirstSubmitted() != null) {

					//Delete from DB
					try {
						log.info("Deleting approval rounds for flow instance " + flowInstance);

						HighLevelQuery<FlowApprovalActivityRound> query = new HighLevelQuery<>();
						query.addParameter(activityRoundFlowInstanceIDParamFactory.getParameter(flowInstance.getFlowInstanceID()));

						activityRoundDAO.delete(query);

					} catch (SQLException e) {

						log.error("Error deleting approval rounds for flow instance " + flowInstance, e);
					}

					//Delete from filesystem
					try {
						File instanceDir = new File(pdfDir + File.separator + flowInstance.getFlowInstanceID());

						if (instanceDir.exists()) {

							log.info("Deleting PDF files for flow instance " + flowInstance);

							FileUtils.deleteFiles(instanceDir, null, true);

							instanceDir.delete();
						}
					} catch (Exception e) {

						log.error("Error deleting PDF files for flow instance " + flowInstance, e);
					}
				}
			}

		} else if (event.getAction() == CRUDAction.UPDATE) {

			for (FlowInstance flowInstance : event.getBeans()) {

				if (flowInstance.getFirstSubmitted() != null) {

					flowInstance = flowAdminModule.getFlowInstance(flowInstance.getFlowInstanceID(), null, FlowInstance.STATUS_RELATION, FlowInstance.ATTRIBUTES_RELATION, FlowInstance.FLOW_RELATION, Flow.FLOW_FAMILY_RELATION, Flow.STATUSES_RELATION);

					List<FlowApprovalActivityGroup> activityGroups = getActivityGroups(flowInstance.getFlow().getFlowFamily().getFlowFamilyID(), flowInstance.getStatus().getName(), FlowApprovalActivityGroup.ACTIVITIES_RELATION);

					if (activityGroups != null) {

						cancelInvalidActivityRounds(flowInstance, flowInstance.getStatus());

						List<FlowApprovalActivityGroup> restartedActivityGroups = null;
						StringBuilder cancelledActivityGroupNames = null;

						for (FlowApprovalActivityGroup activityGroup : activityGroups) {

							if (activityGroup.getActivities() != null) {

								FlowApprovalActivityRound round = getLatestActivityRound(activityGroup, flowInstance, FlowApprovalActivityRound.ACTIVITY_PROGRESSES_RELATION, FlowApprovalActivityProgress.ACTIVITY_RELATION, FlowApprovalActivity.RESPONSIBLE_USERS_RELATION, FlowApprovalActivity.RESPONSIBLE_GROUPS_RELATION);

								if (activityGroup.isAllowRestarts()) {

									Set<FlowApprovalActivity> oldActivities = null;
									Set<FlowApprovalActivity> newActivities = null;

									if (activityGroup.isOnlyRestartIfActivityChanges()) {

										oldActivities = new HashSet<>();
										newActivities = new HashSet<>();

										if (round != null && round.getCancelled() == null) {
											for (FlowApprovalActivityProgress activityProgress : round.getActivityProgresses()) {
												oldActivities.add(activityProgress.getActivity());
											}
										}

										for (FlowApprovalActivity activity : activityGroup.getActivities()) {
											if (isActivityActive(activity, flowInstance)) {
												newActivities.add(activity);
											}
										}
									}

									if (!activityGroup.isOnlyRestartIfActivityChanges() || !newActivities.equals(oldActivities)) {

										if (round != null && round.getCompleted() == null && round.getCancelled() == null) {

											round.setCancelled(TimeUtils.getCurrentTimestamp());

											round.setActivityGroup(activityGroup);
											activityRoundDAO.update(round);

											if (cancelledActivityGroupNames == null) {
												cancelledActivityGroupNames = new StringBuilder();
											}

											if (cancelledActivityGroupNames.length() > 0) {
												cancelledActivityGroupNames.append(", ");
											}

											cancelledActivityGroupNames.append(activityGroup.getName());
										}

										restartedActivityGroups = CollectionUtils.addAndInstantiateIfNeeded(restartedActivityGroups, activityGroup);
										continue;
									}
								}

								if (round != null && round.getCompleted() == null && round.getCancelled() == null) { // No new or removed activities, Update existing activities

									TransactionHandler transactionHandler = activityProgressDAO.createTransaction();

									try {
										Map<FlowApprovalActivity, FlowApprovalActivityProgress> updatedActivities = new HashMap<>(activityGroup.getActivities().size());
										List<String> flowInstanceEventMessages = new ArrayList<>();

										for (FlowApprovalActivityProgress progress : round.getActivityProgresses()) {

											FlowApprovalActivity activity = progress.getActivity();
											
											if(isActivityActive(activity, flowInstance)) {
												
												boolean responsibleUsersChanged = false;
												boolean responsibleGroupsChanged = false;

												if (activity.getResponsibleUserAttributeNames() != null) {
	
													List<User> responsibleUsers = getResponsibleUsersFromAttribute(activity, flowInstance);
	
													responsibleUsersChanged = CollectionUtils.getSize(responsibleUsers) != CollectionUtils.getSize(progress.getResponsibleAttributedUsers());
	
													if (!responsibleUsersChanged && responsibleUsers != null) { // Same size but not nulls, compare contents
	
														Set<User> oldUsers = new HashSet<>(progress.getResponsibleAttributedUsers());
														Set<User> newUsers = new HashSet<>(responsibleUsers);
	
														responsibleUsersChanged = !oldUsers.equals(newUsers);
													}
	
													if (responsibleUsersChanged) {
	
														log.info("Updating responsible user for " + progress + " from " + (progress.getResponsibleAttributedUsers() != null ? StringUtils.toCommaSeparatedString(progress.getResponsibleAttributedUsers()) : "null") + " to " + (responsibleUsers != null ? StringUtils.toCommaSeparatedString(responsibleUsers) : "null"));
	
														String namesFrom = progress.getResponsibleAttributedUsers() != null ? FlowInstanceUtils.getManagersString(progress.getResponsibleAttributedUsers(), null) : eventActivityOwnerDefault;
														String namesTo = eventActivityOwnerDefault;
	
														if (responsibleUsers != null) {
															namesTo += " " + FlowInstanceUtils.getManagersString(responsibleUsers, null);
														}
	
														flowInstanceEventMessages.add(eventActivityOwnerChanged.replace("$activity", activity.getName()).replace("$from", namesFrom).replace("$to", namesTo));
	
														progress.setResponsibleAttributedUsers(responsibleUsers);
	
														
													}
												}
												
												if (activity.getResponsibleGroupAttributeNames() != null) {
													
													List<Group> responsibleGroups = getResponsibleGroupsFromAttribute(activity, flowInstance);
	
													responsibleGroupsChanged = CollectionUtils.getSize(responsibleGroups) != CollectionUtils.getSize(progress.getResponsibleAttributedGroups());
	
													if (!responsibleGroupsChanged && responsibleGroups != null) { // Same size but not nulls, compare contents
	
														Set<Group> oldGroups = new HashSet<>(progress.getResponsibleAttributedGroups());
														Set<Group> newGroups = new HashSet<>(responsibleGroups);
	
														responsibleGroupsChanged = !oldGroups.equals(newGroups);
													}
	
													if (responsibleGroupsChanged) {
	
														log.info("Updating responsible group for " + progress + " from " + (progress.getResponsibleAttributedGroups() != null ? StringUtils.toCommaSeparatedString(progress.getResponsibleAttributedGroups()) : "null") + " to " + (responsibleGroups != null ? StringUtils.toCommaSeparatedString(responsibleGroups) : "null"));
	
														String namesFrom = progress.getResponsibleAttributedGroups() != null ? FlowInstanceUtils.getManagersString(null, progress.getResponsibleAttributedGroups()) : eventActivityOwnerDefault;
														String namesTo = eventActivityOwnerDefault;
	
														if (responsibleGroups != null) {
															namesTo += " " + FlowInstanceUtils.getManagersString(null,responsibleGroups);
														}
	
														flowInstanceEventMessages.add(eventActivityOwnerChanged.replace("$activity", activity.getName()).replace("$from", namesFrom).replace("$to", namesTo));
	
														progress.setResponsibleAttributedGroups(responsibleGroups);
	
														
													}
													
													//TODO hantera groupfallbackusers
												}
												
												if(responsibleUsersChanged || responsibleGroupsChanged) {
													progress.setActivity(activity);
													progress.setActivityRound(round);
													activityProgressDAO.update(progress, transactionHandler, null);

													updatedActivities.put(activity, progress);
												}
											}
										}

										if (!updatedActivities.isEmpty()) {

											transactionHandler.commit();

											if (activityGroup.isSendActivityGroupStartedEmail()) {

												sendActivityGroupStartedNotifications(updatedActivities, activityGroup, flowInstance, false);
											}

											for (String eventMessage : flowInstanceEventMessages) {

												flowAdminModule.getFlowInstanceEventGenerator().addFlowInstanceEvent(flowInstance, EventType.OTHER_EVENT, eventMessage, null);
											}
										}

									} finally {
										TransactionHandler.autoClose(transactionHandler);
									}
								}
							}
						}

						if (restartedActivityGroups != null) {

							log.info("Active activities has changed, restarting " + StringUtils.toCommaSeparatedString(restartedActivityGroups) + " for " + flowInstance);

							if (cancelledActivityGroupNames != null) {
								flowAdminModule.getFlowInstanceEventGenerator().addFlowInstanceEvent(flowInstance, EventType.OTHER_EVENT, eventActivityGroupCancelled + " " + cancelledActivityGroupNames.toString(), null);
							}

							startActivityGroups(flowInstance, flowInstance.getStatus());
						}
					}
				}
			}
		}
	}

	private String replaceTags(String template, TagReplacer tagReplacer, ImmutableFlowInstance flowInstance) {

		return AttributeTagUtils.replaceTags(tagReplacer.replace(template), flowInstance.getAttributeHandler());
	}

	List<User> getResponsibleUsersFromAttribute(FlowApprovalActivity activity, ImmutableFlowInstance flowInstance) {

		Set<User> users = null;

		for (String attributeName : activity.getResponsibleUserAttributeNames()) {

			String username = flowInstance.getAttributeHandler().getString(attributeName);

			if (username != null) {

				User user = systemInterface.getUserHandler().getUserByUsername(username, false, true);

				if (user != null) {

					users = CollectionUtils.addAndInstantiateIfNeeded(users, user);

				} else {

					log.warn("Unable to find user with username " + username + " specified in attribute " + attributeName + " of flow instance " + flowInstance + " for activity " + activity);
				}
			}
		}

		if (users != null) {
			return new ArrayList<>(users);
		}

		return null;
	}
	
	
	List<Group> getResponsibleGroupsFromAttribute(FlowApprovalActivity activity, ImmutableFlowInstance flowInstance) {

		List<String> groupIdentifiers = new ArrayList<>();
		for (String attributeName : activity.getResponsibleGroupAttributeNames()) {

			String groupIdentifier = flowInstance.getAttributeHandler().getString(attributeName);
			
			if(groupIdentifier != null) {
				groupIdentifiers.add(groupIdentifier);
			}
			
		}
		
		if(groupIdentifiers.isEmpty()) {
			log.warn("Unable to find group with groupnames\\id due to missing data specified in attribute " + activity.getResponsibleGroupAttributeNames() + " of flow instance " + flowInstance + " for activity " + activity);

			return null;
		}
		
		List<Group> groups = systemInterface.getGroupHandler().getGroupsByName(groupIdentifiers, true);
		
		List<Integer> groupIDs = NumberUtils.toInt(groupIdentifiers);
		
		List<Group> groupsByID = null;
		
		if(groupIDs != null) {
			groupsByID = CollectionUtils.addAndInstantiateIfNeeded(groupsByID,systemInterface.getGroupHandler().getGroups(groupIDs, true));
		}
		
		if(groupsByID != null) {
			groups = CollectionUtils.addAndInstantiateIfNeeded(groups, groupsByID);
		}
		
		
		if (groups != null) {
			return groups;
		} else {

			log.warn("Unable to find group with groupnames\\id " + groupIdentifiers.toString() + " specified in attribute " + activity.getResponsibleGroupAttributeNames() + " of flow instance " + flowInstance + " for activity " + activity);
		}

		return null;
	}
	
	

	public FlowApprovalActivityGroup getActivityGroup(Integer activityGroupID) throws SQLException {

		return activityGroupDAOWrapper.get(activityGroupID);
	}

	private List<FlowApprovalActivityGroup> getActivityGroups(Integer flowFamilyID, Field... relations) throws SQLException {

		HighLevelQuery<FlowApprovalActivityGroup> query = new HighLevelQuery<>();

		query.addParameter(activityGroupFlowFamilyIDParamFactory.getParameter(flowFamilyID));

		if (relations != null) {
			query.addRelations(relations);
		}

		return activityGroupDAO.getAll(query);
	}

	private List<FlowApprovalActivityGroup> getActivityGroups(Integer flowFamilyID, String startStatusName, Field... relations) throws SQLException {

		HighLevelQuery<FlowApprovalActivityGroup> query = new HighLevelQuery<>();

		query.addParameter(activityGroupFlowFamilyIDParamFactory.getParameter(flowFamilyID));
		query.addParameter(activityGroupStartStatusParamFactory.getParameter(startStatusName));

		if (relations != null) {
			query.addRelations(relations);
		}

		return activityGroupDAO.getAll(query);
	}

	private List<FlowApprovalActivityGroup> getActivityGroups(ImmutableFlowInstance flowInstance, Field... relations) throws SQLException {

		HighLevelQuery<FlowApprovalActivityGroup> query = new HighLevelQuery<>();

		query.addParameter(activityGroupFlowFamilyIDParamFactory.getParameter(flowInstance.getFlow().getFlowFamily().getFlowFamilyID()));
		query.addRelationParameter(FlowApprovalActivityRound.class, activityRoundFlowInstanceIDParamFactory.getParameter(flowInstance.getFlowInstanceID()));

		if (relations != null) {
			query.addRelations(relations);
		}

		return activityGroupDAO.getAll(query);
	}

	private FlowApprovalActivityRound getLatestActivityRound(FlowApprovalActivityGroup activityGroup, ImmutableFlowInstance flowInstance, Field... relations) throws SQLException {

		HighLevelQuery<FlowApprovalActivityRound> query = new HighLevelQuery<>();

		query.addParameter(activityRoundActivityGroupParamFactory.getParameter(activityGroup));
		query.addParameter(activityRoundFlowInstanceIDParamFactory.getParameter(flowInstance.getFlowInstanceID()));
		query.addOrderByCriteria(activityRoundIDOrderBy);
		query.setRowLimiter(new MySQLRowLimiter(1));

		if (relations != null) {
			query.addRelations(relations);
		}

		return activityRoundDAO.get(query);
	}

	private List<FlowApprovalActivityRound> getLatestActivityRounds(ImmutableFlowInstance flowInstance, Field... relations) throws SQLException {

		//@formatter:off
		LowLevelQuery<FlowApprovalActivityRound> query = new LowLevelQuery<>(
			  "SELECT r.* FROM flowapproval_activity_rounds r"
			+ " INNER JOIN ("
			+ "  SELECT MAX(activityRoundID) as activityRoundID FROM flowapproval_activity_rounds"
			+ "  WHERE flowInstanceID = " + flowInstance.getFlowInstanceID()
			+ "  GROUP BY activityGroupID"
			+ " ) a ON r.activityRoundID = a.activityRoundID"
		);
		//@formatter:on

		if (relations != null) {
			query.addRelations(relations);
		}

		return activityRoundDAO.getAll(query);
	}

	@Override
	public Document createDocument(HttpServletRequest req, URIParser uriParser, User user) {

		Document doc = XMLUtils.createDomDocument();
		Element document = doc.createElement("Document");
		document.appendChild(RequestUtils.getRequestInfoAsXML(doc, req, uriParser));
		document.appendChild(this.moduleDescriptor.toXML(doc));

		XMLUtils.appendNewElement(doc, document, "ModuleURI", req.getContextPath() + getFullAlias());
		XMLUtils.appendNewElement(doc, document, "StaticContentURL", staticContentModule.getModuleContentURL(moduleDescriptor));

	
		Element whenToComment = doc.createElement("whenToCommentChoices");

		Element commentAlways = doc.createElement("whenToComment");
		XMLUtils.appendNewElement(doc, commentAlways, "name", whenToCommentAlways);
		XMLUtils.appendNewElement(doc, commentAlways, "value", Comment.ALWAYS);

		Element commentDuringAbort = doc.createElement("whenToComment");
		XMLUtils.appendNewElement(doc, commentDuringAbort, "name", whenToCommentDuringDeny);
		XMLUtils.appendNewElement(doc, commentDuringAbort, "value", Comment.WHEN_DENIED);
		
		whenToComment.appendChild(commentAlways);
		whenToComment.appendChild(commentDuringAbort);

		document.appendChild(whenToComment);
		
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
					+" INNER JOIN " + activityRoundDAO.getTableName() + " r ON ap.activityRoundID = r.activityRoundID"
					+" INNER JOIN " + activityGroupDAO.getTableName() + " ag ON ag.activityGroupID = a.activityGroupID AND ag.reminderAfterXDays IS NOT NULL"
					+" WHERE ap.completed IS NULL AND r.cancelled IS NULL " + (!sendRemindersRepeatedly ? "AND ap.automaticReminderSent = 0 " : "") + "AND DATEDIFF(NOW(), ap.added) >= ag.reminderAfterXDays"
			);
			// @formatter:on

			query.addRelations(FlowApprovalActivityProgress.ACTIVITY_ROUND_RELATION, FlowApprovalActivityProgress.ACTIVITY_RELATION, FlowApprovalActivity.ACTIVITY_GROUP_RELATION, FlowApprovalActivity.RESPONSIBLE_USERS_RELATION, FlowApprovalActivity.RESPONSIBLE_GROUPS_RELATION);
			query.addCachedRelations(FlowApprovalActivityProgress.ACTIVITY_ROUND_RELATION, FlowApprovalActivityProgress.ACTIVITY_RELATION, FlowApprovalActivity.ACTIVITY_GROUP_RELATION, FlowApprovalActivity.RESPONSIBLE_USERS_RELATION, FlowApprovalActivity.RESPONSIBLE_GROUPS_RELATION);

			List<FlowApprovalActivityProgress> activityProgresses = activityProgressDAO.getAll(query);

			if (activityProgresses != null) {

				// flowInstanceID, activity, activityProgress
				Map<Integer, Map<FlowApprovalActivity, FlowApprovalActivityProgress>> flowInstanceMap = new TreeMap<>();

				for (FlowApprovalActivityProgress activityProgress : activityProgresses) {

					Map<FlowApprovalActivity, FlowApprovalActivityProgress> activityMap = flowInstanceMap.get(activityProgress.getActivityRound().getFlowInstanceID());

					if (activityMap == null) {

						activityMap = new HashMap<>();
						flowInstanceMap.put(activityProgress.getActivityRound().getFlowInstanceID(), activityMap);
					}

					activityMap.put(activityProgress.getActivity(), activityProgress);

					activityProgress.setAutomaticReminderSent(true);

					FlowApprovalReminder reminder = new FlowApprovalReminder(activityProgress, TimeUtils.getCurrentTimestamp(), ReminderType.AUTOMATIC, null);

					reminderDAO.add(reminder);
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

		if (notificationHandler != null) {

			String baseURL = notificationHandler.getUserFlowInstanceModuleAlias(null);

			this.userApprovalModuleAlias = StringUtils.substringBeforeLast(baseURL, StringUtils.substringAfterLast(baseURL, "/")) + "flowapproval";
		}

		this.notificationHandler = notificationHandler;
	}

	public int getApprovalGroupMaxSortIndex(FlowFamily flowFamily) throws SQLException {

		ObjectQuery<Integer> query = new ObjectQuery<>(dataSource, "SELECT MAX(sortIndex) FROM " + activityGroupDAO.getTableName() + " WHERE flowFamilyID = " + flowFamily.getFlowFamilyID(), IntegerPopulator.getPopulator());

		return query.executeQuery();
	}

	private ViewFragment copyActivity(Flow flow, HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws AccessDeniedException, SQLException, IOException {

		FlowApprovalActivity activity = activityCRUD.getRequestedBean(req, res, user, uriParser, GenericCRUD.UPDATE);

		log.info("User " + user + " copying activity " + activity);

		activity.setActivityID(null);
		activity.setName(StringUtils.substring(activity.getName() + copySuffix, 255));

		RelationQuery query = new RelationQuery(FlowApprovalActivity.RESPONSIBLE_USERS_RELATION, FlowApprovalActivity.RESPONSIBLE_GROUPS_RELATION, FlowApprovalActivity.RESPONSIBLE_FALLBACK_RELATION, FlowApprovalActivity.ASSIGNABLE_USERS_RELATION, FlowApprovalActivity.ASSIGNABLE_GROUPS_RELATION);
		activityDAO.add(activity, query);

		addFlowFamilyEvent(eventActivityCopied + " \"" + activity.getName() + "\"", ((Flow) req.getAttribute("flow")).getFlowFamily(), user);

		res.sendRedirect(req.getContextPath() + req.getAttribute("extensionRequestURL") + "/showactivitygroup/" + activity.getActivityGroup().getActivityGroupID());
		return null;
	}

	private ViewFragment copyActivityGroup(Flow flow, HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws AccessDeniedException, SQLException {

		FlowApprovalActivityGroup activityGroup = activityGroupCRUD.getRequestedBean(req, res, user, uriParser, GenericCRUD.UPDATE);

		log.info("User " + user + " copying activity group " + activityGroup);

		activityGroup.setActivityGroupID(null);
		activityGroup.setName(StringUtils.substring(activityGroup.getName() + copySuffix, 255));

		if (activityGroup.getActivities() != null) {
			for (FlowApprovalActivity activity : activityGroup.getActivities()) {
				activity.setActivityID(null);
			}
		}

		RelationQuery query = new RelationQuery(FlowApprovalActivityGroup.ACTIVITIES_RELATION, FlowApprovalActivity.RESPONSIBLE_USERS_RELATION, FlowApprovalActivity.ASSIGNABLE_USERS_RELATION, FlowApprovalActivity.ASSIGNABLE_GROUPS_RELATION, FlowApprovalActivity.RESPONSIBLE_FALLBACK_RELATION, FlowApprovalActivity.RESPONSIBLE_GROUPS_RELATION);
		activityGroupDAO.add(activityGroup, query);

		addFlowFamilyEvent(eventActivityGroupCopied + " \"" + activityGroup.getName() + "\"", ((Flow) req.getAttribute("flow")).getFlowFamily(), user);

		return null;
	}
	
	public void replaceTagsDescription(URIParser parser, FlowApprovalActivity activity, FlowInstance flowInstance) {

		String description = AttributeTagUtils.replaceTags(activity.getDescription(), flowInstance.getAttributeHandler());
		
		TagReplacer tagReplacer = new TagReplacer();
		
		String link = parser.getFullContextPath()+flowInstanceAdminModule.getFullAlias() + "/overview/" + flowInstance.getFlowInstanceID();
		
		tagReplacer.addTagSource(new SingleTagSource("$flowInstance.url", "<a href=\"" +link+ "\">"+link+"</a>"));
		
		description = AttributeTagUtils.replaceTags(tagReplacer.replace(description), flowInstance.getAttributeHandler());
		
		activity.setDescription(description);
		
	}

	public File getSignaturesPDF(FlowApprovalActivityRound round) {

		return new File(pdfDir + File.separator + round.getFlowInstanceID() + File.separator + "activity-round-" + round.getActivityRoundID() + "-signatures.pdf");
	}

	private void generateSignaturesPDF(URIParser uriParser, FlowInstance flowInstance, FlowApprovalActivityGroup activityGroup, FlowApprovalActivityRound round) throws ModuleConfigurationException {

		if (pdfDir == null || tempDir == null) {

			log.error("Unable to create filestore(s) not set");
			return;
		}

		File outputFile = getSignaturesPDF(round);

		FileUtils.createMissingDirectories(outputFile);

		File tmpFile1 = null;
		File tmpFile2 = null;
		RandomAccessFileOrArray randomAccessFile = null;
		OutputStream outputStream = null;

		try {
			log.info("Generating signatures page for " + round);

			Document doc = XMLUtils.createDomDocument();
			Element documentElement = doc.createElement("Document");
			doc.appendChild(documentElement);

			documentElement.appendChild(flowInstance.toXML(doc));
			documentElement.appendChild(activityGroup.toXML(doc));

			List<FlowApprovalActivityProgress> activityProgresses = round.getActivityProgresses();

			round.setActivityProgresses(null);

			Element roundElement = round.toXML(doc);

			round.setActivityProgresses(activityProgresses);

			Element activityProgressesElement = XMLUtils.appendNewElement(doc, roundElement, "ActivityProgresses");

			for (FlowApprovalActivityProgress activityProgress : activityProgresses) {

				if (activityProgress.getActivity().getDescription() != null) {
					 
					replaceTagsDescription(uriParser, activityProgress.getActivity(), flowInstance);
				}

				Element activityProgressElement = activityProgress.toXML(doc);

				if (activityProgress.getActivity().getShortDescription() != null) {

					XMLUtils.appendNewElement(doc, activityProgressElement, "ShortDescription", AttributeTagUtils.replaceTags(activityProgress.getActivity().getShortDescription(), flowInstance.getAttributeHandler()));
				}

				activityProgressesElement.appendChild(activityProgressElement);
			}

			documentElement.appendChild(roundElement);

			SiteProfile siteProfile = flowAdminModule.getSiteProfile(flowInstance);
			String logotype = pdfProvider.getLogotype(siteProfile);

			XMLUtils.appendNewCDATAElement(doc, documentElement, "Logotype", logotype);

			StringWriter stringWriter = new StringWriter();

			XMLTransformer.transformToWriter(pdfTransformer.getTransformer(), doc, stringWriter, "UTF-8", "1.1");

			String xml = stringWriter.toString();

			Document document;

			if (systemInterface.getEncoding().equalsIgnoreCase("UTF-8")) {

				document = PDFXMLUtils.parseXML(xml);

			} else {

				document = PDFXMLUtils.parseXML(new ByteArrayInputStream(xml.getBytes("UTF-8")));
			}

			tmpFile1 = File.createTempFile("activity-round-" + round.getActivityRoundID() + "-tmp1-", ".pdf", new File(tempDir));

			try {
				outputStream = new BufferedOutputStream(new FileOutputStream(tmpFile1));

				ITextRenderer renderer = new ITextRenderer();
				ResourceLoaderAgent callback = new ResourceLoaderAgent(renderer.getOutputDevice());
				callback.setSharedContext(renderer.getSharedContext());
				renderer.getSharedContext().setUserAgentCallback(callback);
				renderer.setListener(ITEXT_PDF_CREATION_LISTENER);

				if (includedFonts != null) {

					for (String font : includedFonts) {

						renderer.getFontResolver().addFont(font, true);
					}
				}

				renderer.setDocument(document, "documentsigner");
				renderer.layout();

				renderer.createPDF(outputStream);

			} finally {

				CloseUtils.close(outputStream);
			}

			// Append signature attachments

			tmpFile2 = File.createTempFile("activity-round-" + round.getActivityRoundID() + "-tmp2-", ".pdf", new File(tempDir));
			randomAccessFile = new RandomAccessFileOrArray(tmpFile1.getAbsolutePath(), false, false);
			PdfReader reader = new PdfReader(randomAccessFile, null);
			outputStream = new BufferedOutputStream(new FileOutputStream(tmpFile2));
			PdfStamper stamper = new PdfStamper(reader, outputStream);
			PdfWriter writer = stamper.getWriter();

			PdfArray associatedFilesArray = reader.getCatalog().getAsArray(new PdfName("AF"));

			if (associatedFilesArray == null) {

				associatedFilesArray = new PdfArray();
				reader.getCatalog().put(new PdfName("AF"), associatedFilesArray);
			}

			for (FlowApprovalActivityProgress activityProgress : round.getActivityProgresses()) {

				if (activityProgress.getSignedDate() != null) {

					String signingDataAttachmentName = pdfSigningDataAttachment + " - " + activityProgress.getActivity().getName() + " - " + activityProgress.getActivityProgressID();

					try {
						PdfFileSpecification fs = StreamPdfFileSpecification.fileEmbedded(writer, new ByteArrayInputStream(activityProgress.getSigningData().getBytes("ISO-8859-1")), signingDataAttachmentName + ".txt");
						writer.addFileAttachment(signingDataAttachmentName, fs);
						associatedFilesArray.add(fs.getReference());

					} catch (Exception e) {
						log.error("Error appending signing data for " + activityProgress, e);
					}

					String signatureAttachmentName = pdfSignatureAttachment + " - " + activityProgress.getActivity().getName() + " - " + activityProgress.getActivityProgressID();

					try {
						PdfFileSpecification fs = StreamPdfFileSpecification.fileEmbedded(writer, new ByteArrayInputStream(activityProgress.getSignatureData().getBytes("ISO-8859-1")), signatureAttachmentName + ".txt");
						writer.addFileAttachment(signatureAttachmentName, fs);
						associatedFilesArray.add(fs.getReference());

					} catch (Exception e) {
						log.error("Error appending signature data for " + activityProgress, e);
					}
				}
			}

			if (associatedFilesArray.isEmpty()) {
				reader.getCatalog().put(new PdfName("AF"), null);
			}

			stamper.close();

			writePDFA(tmpFile2, outputFile);

			round.setPdf(true);

		} catch (Exception e) {

			log.error("Error generating signed PDF for " + round, e);
			FileUtils.deleteFile(outputFile);

		} finally {

			CloseUtils.close(outputStream);

			if (randomAccessFile != null) {

				try {
					randomAccessFile.close();
				} catch (IOException e) {}
			}

			FileUtils.deleteFile(tmpFile1);
			FileUtils.deleteFile(tmpFile2);
		}
	}

	private void writePDFA(File inputFile, File outputFile) throws Exception {

		PDDocument document = PDDocument.load(inputFile);

		try {
			PDDocumentCatalog catalog = document.getDocumentCatalog();
			PDDocumentInformation info = document.getDocumentInformation();

			XMPMetadata metadata = XMPMetadata.createXMPMetadata();

			PDFAIdentificationSchema id = metadata.createAndAddPFAIdentificationSchema();
			id.setPart(3);
			id.setConformance("A");

			AdobePDFSchema pdfSchema = metadata.createAndAddAdobePDFSchema();
			pdfSchema.setKeywords("Open ePlatform");
			pdfSchema.setProducer("Open ePlatform");

			GregorianCalendar calendar = new GregorianCalendar();

			XMPBasicSchema basicSchema = metadata.createAndAddXMPBasicSchema();
			basicSchema.setModifyDate(calendar);
			basicSchema.setCreateDate(calendar);
			basicSchema.setCreatorTool("Open ePlatform");
			basicSchema.setMetadataDate(new GregorianCalendar());

			DublinCoreSchema dcSchema = metadata.createAndAddDublinCoreSchema();
			dcSchema.setTitle(info.getTitle());
			dcSchema.addCreator("Open ePlatform");
			dcSchema.setDescription("Open ePlatform");

			PDMetadata metadataStream = new PDMetadata(document);
			catalog.setMetadata(metadataStream);

			XmpSerializer serializer = new XmpSerializer();
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			serializer.serialize(metadata, baos, false);
			metadataStream.importXMPMetadata(baos.toByteArray());

			if (!hasColorProfile(catalog)) {

				InputStream colorProfile = FlowApprovalAdminModule.class.getResourceAsStream("sRGB Color Space Profile.icm");

				PDOutputIntent oi = new PDOutputIntent(document, colorProfile);
				oi.setInfo("sRGB IEC61966-2.1");
				oi.setOutputCondition("sRGB IEC61966-2.1");
				oi.setOutputConditionIdentifier("sRGB IEC61966-2.1");
				oi.setRegistryName("http://www.color.org");
				catalog.addOutputIntent(oi);

			}

			document.save(outputFile);

		} finally {

			document.close();
		}
	}

	private boolean hasColorProfile(PDDocumentCatalog catalog) {

		List<PDOutputIntent> list = catalog.getOutputIntents();

		for (PDOutputIntent outputIntent : list) {

			if (outputIntent.getRegistryName() != null && outputIntent.getRegistryName().equalsIgnoreCase("http://www.color.org")) {

				return true;
			}
		}

		return false;
	}

	public String getSignaturesFilename() {

		return signaturesFilename;
	}

	public String getUserApprovalModuleAlias(ImmutableFlowInstance flowInstance) {

		return userApprovalModuleAlias;
	}
	
	public String getEventActivityOwnerChanged() {

		return eventActivityOwnerChanged;
	}

	public String getEventActivityOwnerDefault() {

		return eventActivityOwnerDefault;
	}

}
