package com.nordicpeak.flowengine.notifications;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import se.unlogic.emailutils.framework.ByteArrayAttachment;
import se.unlogic.emailutils.framework.EmailUtils;
import se.unlogic.emailutils.framework.FileAttachment;
import se.unlogic.emailutils.framework.SimpleEmail;
import se.unlogic.emailutils.populators.EmailPopulator;
import se.unlogic.hierarchy.core.annotations.CheckboxSettingDescriptor;
import se.unlogic.hierarchy.core.annotations.EventListener;
import se.unlogic.hierarchy.core.annotations.GroupMultiListSettingDescriptor;
import se.unlogic.hierarchy.core.annotations.HTMLEditorSettingDescriptor;
import se.unlogic.hierarchy.core.annotations.InstanceManagerDependency;
import se.unlogic.hierarchy.core.annotations.ModuleSetting;
import se.unlogic.hierarchy.core.annotations.TextAreaSettingDescriptor;
import se.unlogic.hierarchy.core.annotations.TextFieldSettingDescriptor;
import se.unlogic.hierarchy.core.annotations.WebPublic;
import se.unlogic.hierarchy.core.annotations.XSLVariable;
import se.unlogic.hierarchy.core.beans.LinkTag;
import se.unlogic.hierarchy.core.beans.ScriptTag;
import se.unlogic.hierarchy.core.beans.SimpleAccessInterface;
import se.unlogic.hierarchy.core.beans.SimpleForegroundModuleResponse;
import se.unlogic.hierarchy.core.beans.SimpleSMS;
import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.hierarchy.core.enums.CRUDAction;
import se.unlogic.hierarchy.core.enums.EventSource;
import se.unlogic.hierarchy.core.events.CRUDEvent;
import se.unlogic.hierarchy.core.exceptions.AccessDeniedException;
import se.unlogic.hierarchy.core.exceptions.ModuleConfigurationException;
import se.unlogic.hierarchy.core.exceptions.URINotFoundException;
import se.unlogic.hierarchy.core.interfaces.AttributeHandler;
import se.unlogic.hierarchy.core.interfaces.ForegroundModuleDescriptor;
import se.unlogic.hierarchy.core.interfaces.ForegroundModuleResponse;
import se.unlogic.hierarchy.core.interfaces.MutableSettingHandler;
import se.unlogic.hierarchy.core.interfaces.SMSSender;
import se.unlogic.hierarchy.core.interfaces.SectionInterface;
import se.unlogic.hierarchy.core.interfaces.ViewFragment;
import se.unlogic.hierarchy.core.utils.AccessUtils;
import se.unlogic.hierarchy.core.utils.ModuleViewFragmentTransformer;
import se.unlogic.hierarchy.core.utils.UserUtils;
import se.unlogic.hierarchy.core.utils.ViewFragmentModule;
import se.unlogic.hierarchy.foregroundmodules.AnnotatedForegroundModule;
import se.unlogic.openhierarchy.foregroundmodules.siteprofile.interfaces.SiteProfile;
import se.unlogic.standardutils.arrays.ArrayUtils;
import se.unlogic.standardutils.collections.CollectionUtils;
import se.unlogic.standardutils.dao.AnnotatedDAO;
import se.unlogic.standardutils.dao.AnnotatedDAOWrapper;
import se.unlogic.standardutils.dao.HighLevelQuery;
import se.unlogic.standardutils.dao.QueryParameterFactory;
import se.unlogic.standardutils.dao.SimpleAnnotatedDAOFactory;
import se.unlogic.standardutils.db.tableversionhandler.TableVersionHandler;
import se.unlogic.standardutils.db.tableversionhandler.UpgradeResult;
import se.unlogic.standardutils.db.tableversionhandler.XMLDBScriptProvider;
import se.unlogic.standardutils.io.BinarySizes;
import se.unlogic.standardutils.io.FileUtils;
import se.unlogic.standardutils.mime.MimeUtils;
import se.unlogic.standardutils.numbers.NumberUtils;
import se.unlogic.standardutils.string.AnnotatedBeanTagSourceFactory;
import se.unlogic.standardutils.string.SingleTagSource;
import se.unlogic.standardutils.string.TagReplacer;
import se.unlogic.standardutils.string.TagSource;
import se.unlogic.standardutils.validation.ValidationException;
import se.unlogic.standardutils.xml.XMLUtils;
import se.unlogic.webutils.http.RequestUtils;
import se.unlogic.webutils.http.URIParser;
import se.unlogic.webutils.populators.annotated.AnnotatedRequestPopulator;

import com.nordicpeak.flowengine.FlowBrowserModule;
import com.nordicpeak.flowengine.UserFlowInstanceModule;
import com.nordicpeak.flowengine.beans.Contact;
import com.nordicpeak.flowengine.beans.DefaultInstanceMetadata;
import com.nordicpeak.flowengine.beans.DefaultStatusMapping;
import com.nordicpeak.flowengine.beans.Flow;
import com.nordicpeak.flowengine.beans.FlowFamily;
import com.nordicpeak.flowengine.beans.FlowInstance;
import com.nordicpeak.flowengine.beans.FlowInstanceEvent;
import com.nordicpeak.flowengine.beans.FlowType;
import com.nordicpeak.flowengine.beans.QueryDescriptor;
import com.nordicpeak.flowengine.beans.QueryInstanceDescriptor;
import com.nordicpeak.flowengine.beans.SigningParty;
import com.nordicpeak.flowengine.beans.Status;
import com.nordicpeak.flowengine.beans.Step;
import com.nordicpeak.flowengine.dao.FlowEngineDAOFactory;
import com.nordicpeak.flowengine.enums.ContentType;
import com.nordicpeak.flowengine.enums.EventType;
import com.nordicpeak.flowengine.enums.SenderType;
import com.nordicpeak.flowengine.events.ExternalMessageAddedEvent;
import com.nordicpeak.flowengine.events.ManagersChangedEvent;
import com.nordicpeak.flowengine.events.MultiSigningInitiatedEvent;
import com.nordicpeak.flowengine.events.StatusChangedByManagerEvent;
import com.nordicpeak.flowengine.events.SubmitEvent;
import com.nordicpeak.flowengine.exceptions.flowinstance.InvalidFlowInstanceStepException;
import com.nordicpeak.flowengine.exceptions.flowinstance.MissingQueryInstanceDescriptor;
import com.nordicpeak.flowengine.exceptions.flowinstancemanager.DuplicateFlowInstanceManagerIDException;
import com.nordicpeak.flowengine.exceptions.queryprovider.QueryInstanceNotFoundInQueryProviderException;
import com.nordicpeak.flowengine.exceptions.queryprovider.QueryProviderErrorException;
import com.nordicpeak.flowengine.exceptions.queryprovider.QueryProviderNotFoundException;
import com.nordicpeak.flowengine.interfaces.FlowNotificationHandler;
import com.nordicpeak.flowengine.interfaces.ImmutableFlow;
import com.nordicpeak.flowengine.interfaces.ImmutableFlowInstance;
import com.nordicpeak.flowengine.interfaces.MultiSigningHandler;
import com.nordicpeak.flowengine.interfaces.PDFProvider;
import com.nordicpeak.flowengine.interfaces.QueryHandler;
import com.nordicpeak.flowengine.managers.FlowInstanceManager;
import com.nordicpeak.flowengine.managers.ImmutableFlowInstanceManager;
import com.nordicpeak.flowengine.utils.AttributeTagUtils;
import com.nordicpeak.flowengine.utils.FlowInstanceUtils;
import com.nordicpeak.flowengine.utils.MultiSignUtils;
import com.nordicpeak.flowengine.utils.PDFByteAttachment;

public class StandardFlowNotificationHandler extends AnnotatedForegroundModule implements FlowNotificationHandler, ViewFragmentModule<ForegroundModuleDescriptor> {

	public static final Field[] FLOW_INSTANCE_RELATIONS = { FlowInstance.EVENTS_RELATION, FlowInstanceEvent.ATTRIBUTES_RELATION, FlowInstance.ATTRIBUTES_RELATION, FlowInstance.MANAGERS_RELATION, FlowInstance.FLOW_RELATION, FlowInstance.FLOW_STATE_RELATION, Flow.FLOW_TYPE_RELATION, Flow.FLOW_FAMILY_RELATION, FlowType.ALLOWED_ADMIN_GROUPS_RELATION, FlowType.ALLOWED_ADMIN_USERS_RELATION, Flow.STEPS_RELATION, Flow.FLOW_FAMILY_RELATION, FlowFamily.MANAGER_GROUPS_RELATION, FlowFamily.MANAGER_USERS_RELATION, Step.QUERY_DESCRIPTORS_RELATION, QueryDescriptor.EVALUATOR_DESCRIPTORS_RELATION, Flow.DEFAULT_FLOW_STATE_MAPPINGS_RELATION, DefaultStatusMapping.FLOW_STATE_RELATION, QueryDescriptor.QUERY_INSTANCE_DESCRIPTORS_RELATION, FlowInstance.OWNERS_RELATION};

	private static final AnnotatedRequestPopulator<FlowFamililyNotificationSettings> POPULATOR = new AnnotatedRequestPopulator<FlowFamililyNotificationSettings>(FlowFamililyNotificationSettings.class);

	private static final AnnotatedBeanTagSourceFactory<Flow> FLOW_TAG_SOURCE_FACTORY = new AnnotatedBeanTagSourceFactory<Flow>(Flow.class, "$flow.");
	private static final AnnotatedBeanTagSourceFactory<FlowInstance> FLOWINSTANCE_TAG_SOURCE_FACTORY = new AnnotatedBeanTagSourceFactory<FlowInstance>(FlowInstance.class, "$flowInstance.");
	private static final AnnotatedBeanTagSourceFactory<Status> STATUS_TAG_SOURCE_FACTORY = new AnnotatedBeanTagSourceFactory<Status>(Status.class, "$status.");
	private static final AnnotatedBeanTagSourceFactory<Contact> CONTACT_TAG_SOURCE_FACTORY = new AnnotatedBeanTagSourceFactory<Contact>(Contact.class, "$contact.");
	private static final AnnotatedBeanTagSourceFactory<User> MANAGER_TAG_SOURCE_FACTORY = new AnnotatedBeanTagSourceFactory<User>(User.class, "$manager.");
	private static final AnnotatedBeanTagSourceFactory<SigningParty> SIGNING_PARTY_TAG_SOURCE_FACTORY = new AnnotatedBeanTagSourceFactory<SigningParty>(SigningParty.class, "$signingParty.");

	@ModuleSetting
	@CheckboxSettingDescriptor(name = "Enable fragment XML debug", description = "Enables debugging of fragment XML")
	private boolean debugFragmententXML;

	@ModuleSetting
	@TextFieldSettingDescriptor(name = "SMS sender name", description = "The name of the sender used for SMS messages", required = true)
	private String smsSenderName = "Not set";

	@ModuleSetting
	@TextFieldSettingDescriptor(name = "Email sender name", description = "The name displayed in the sender field of sent e-mail", required = true)
	protected String emailSenderName = "Not set";

	@ModuleSetting
	@TextFieldSettingDescriptor(name = "Email sender address", description = "The sender address", required = true, formatValidator = EmailPopulator.class)
	protected String emailSenderAddress = "not.set@not.set.com";

	@ModuleSetting
	@TextFieldSettingDescriptor(name = "User flow instance module URL", description = "The full URL of the user flow instance module", required = true)
	protected String userFlowInstanceModuleAlias = "not set";

	@ModuleSetting
	@TextFieldSettingDescriptor(name = "Flow instance admin module URL", description = "The full URL of the flow instance module", required = true)
	protected String flowInstanceAdminModuleAlias = "not set";

	@ModuleSetting
	@CheckboxSettingDescriptor(name = "Send SMS to users on status change", description = "Controls if SMS messages are the sent to the users when the status of their flow instance changes.")
	private boolean sendStatusChangedUserSMS;

	@ModuleSetting
	@TextAreaSettingDescriptor(name = "SMS to users on status change", description = "The SMS messages sent to the users when the status of their flow instance changes.", required = true)
	@XSLVariable(prefix = "java.")
	private String statusChangedUserSMS;

	@ModuleSetting
	@CheckboxSettingDescriptor(name = "Send SMS to users when new messages are received", description = "Controls if SMS messages are sent to the users when they receive new messages.")
	private boolean sendExternalMessageReceivedUserSMS;

	@ModuleSetting
	@TextAreaSettingDescriptor(name = "SMS to users when new messages are received", description = "The SMS messages sent to the users when they receive new messages.", required = true)
	@XSLVariable(prefix = "java.")
	private String externalMessageReceivedUserSMS;

	@ModuleSetting
	@CheckboxSettingDescriptor(name = "Send SMS to users on flow instance submit", description = "Controls if SMS messages are the sent to the users when they submit new flow instances.")
	private boolean sendFlowInstanceSubmittedUserSMS;

	@ModuleSetting
	@TextAreaSettingDescriptor(name = "SMS to sent users on flow instance submit", description = "The SMS message sent to the users when they submit new flow instances.", required = true)
	@XSLVariable(prefix = "java.")
	private String flowInstanceSubmittedUserSMS;

	@ModuleSetting
	@TextAreaSettingDescriptor(name = "SMS to sent not logged in users on flow instance submit", description = "The SMS message sent to not logged in users when they submit new flow instances.", required = true)
	@XSLVariable(prefix = "java.")
	private String flowInstanceSubmittedNotLoggedInUserSMS;

	@ModuleSetting
	@CheckboxSettingDescriptor(name = "Send SMS to users when a flow instance is archived", description = "Controls if SMS messages are the sent to the users when their flow instances are archived.")
	private boolean sendFlowInstanceArchivedUserSMS;

	@ModuleSetting
	@TextAreaSettingDescriptor(name = "SMS sent to logged in users when a flow instance is archived", description = "The SMS message sent to logged in users when their flow instances are archived.", required = true)
	@XSLVariable(prefix = "java.")
	private String flowInstanceArchivedUserSMS;

	@ModuleSetting
	@TextAreaSettingDescriptor(name = "SMS sent to not logged in users when a flow instance is archived", description = "The SMS message sent to not logged in users when their flow instances are archived.", required = true)
	@XSLVariable(prefix = "java.")
	private String flowInstanceArchivedNotLoggedInUserSMS;

	@ModuleSetting
	@TextAreaSettingDescriptor(name = "Flow instance multi sign SMS message (users)", description = "The SMS sent to the users when they have to sign a multi sign flow instance", required = true)
	@XSLVariable(prefix = "java.")
	private String flowInstanceMultiSignInitiatedUserSMS;

	@ModuleSetting
	@CheckboxSettingDescriptor(name = "Send email to users on status change", description = "Controls if email messages are the sent to the users when the status of their flow instances changes.")
	private boolean sendStatusChangedUserEmail;

	@ModuleSetting
	@TextFieldSettingDescriptor(name = "Status changed email subject (users)", description = "The subject of emails sent to the users when the status of their flow instance changes", required = true)
	@XSLVariable(prefix = "java.")
	private String statusChangedUserEmailSubject;

	@ModuleSetting
	@HTMLEditorSettingDescriptor(name = "Status changed email message (users)", description = "The message of emails sent to the users when the status of their flow instance changes", required = true)
	@XSLVariable(prefix = "java.")
	private String statusChangedUserEmailMessage;

	@ModuleSetting
	@CheckboxSettingDescriptor(name = "Send email to users when new messages are received", description = "Controls if email messages are sent to the users when they receive new messages.")
	private boolean sendExternalMessageReceivedUserEmail;

	@ModuleSetting
	@TextFieldSettingDescriptor(name = "New message received email subject (users)", description = "The subject of emails sent to the users when new messages are received", required = true)
	@XSLVariable(prefix = "java.")
	private String externalMessageReceivedUserEmailSubject;

	@ModuleSetting
	@HTMLEditorSettingDescriptor(name = "New message received email message (users)", description = "The message of emails sent to the users when new messages are received", required = true)
	@XSLVariable(prefix = "java.")
	private String externalMessageReceivedUserEmailMessage;

	@ModuleSetting
	@CheckboxSettingDescriptor(name = "Send email to users on flow instance submit", description = "Controls if email messages are the sent to the users when they submit new flow instances.")
	private boolean sendFlowInstanceSubmittedUserEmail;

	@ModuleSetting
	@TextFieldSettingDescriptor(name = "Flow instance submitted email subject", description = "The subject of emails sent to the users when they submit a new flow instance", required = true)
	@XSLVariable(prefix = "java.")
	private String flowInstanceSubmittedUserEmailSubject;

	@ModuleSetting
	@HTMLEditorSettingDescriptor(name = "Flow instance submitted email message (logged in users)", description = "The message of emails sent to logged in users when they submit a new flow instance", required = true)
	@XSLVariable(prefix = "java.")
	private String flowInstanceSubmittedUserEmailMessage;

	@ModuleSetting
	@HTMLEditorSettingDescriptor(name = "Flow instance submitted email message (not logged in users)", description = "The message of emails sent to not logged in users when they submit a new flow instance", required = true)
	@XSLVariable(prefix = "java.")
	private String flowInstanceSubmittedNotLoggedInUserEmailMessage;

	@ModuleSetting
	@CheckboxSettingDescriptor(name = "Attach PDF to flow instance submitted emails (users)", description = "Controls if generated PDF documents are attached to email messages when new flow instances are submitted.")
	private boolean flowInstanceSubmittedUserEmailAttachPDF;

	@ModuleSetting
	@TextFieldSettingDescriptor(name = "PDF size limit for flow instance submitted emails (users)", description = "The size limit in megabyte for PDF documents attached to email messages when new flow instances are submitted. If this size is exceeded no PDF is attached. No value set means no size limit.")
	private Integer flowInstanceSubmittedUserEmailPDFSizeLimit;

	@ModuleSetting
	@TextFieldSettingDescriptor(name = "PDF attached text", description = "The text replaced for $flowInstance.pdfAttachedText tag when pdf is attached to submitted email message", required = true)
	@XSLVariable(prefix = "java.")
	private String flowInstanceSubmittedUserEmailPDFAttachedText;

	@ModuleSetting
	@TextFieldSettingDescriptor(name = "PDF size limit exceeded text", description = "Text text replaced for $flowInstance.pdfAttachedText tag when pdf size exceeds the allowed size limit", required = true)
	@XSLVariable(prefix = "java.")
	private String flowInstanceSubmittedUserEmailPDFSizeLimitExceededText;

	@ModuleSetting
	@CheckboxSettingDescriptor(name = "Send email to users when flow instance is archived", description = "Controls if email messages are the sent to the users when their flow instances are archived.")
	private boolean sendFlowInstanceArchivedUserEmail;

	@ModuleSetting
	@TextFieldSettingDescriptor(name = "Flow instance archived email subject (users)", description = "The subject of emails sent to the users when their flow instances are archived", required = true)
	@XSLVariable(prefix = "java.")
	private String flowInstanceArchivedUserEmailSubject;

	@ModuleSetting
	@HTMLEditorSettingDescriptor(name = "Flow instance archived email message (logged in users)", description = "The message of emails sent to logged in users when their flow instances are archived", required = true)
	@XSLVariable(prefix = "java.")
	private String flowInstanceArchivedUserEmailMessage;

	@ModuleSetting
	@HTMLEditorSettingDescriptor(name = "Flow instance archived email message (not logged in users)", description = "The message of emails sent to not logged in users when their flow instances are archived", required = true)
	@XSLVariable(prefix = "java.")
	private String flowInstanceArchivedNotLoggedInUserEmailMessage;

	@ModuleSetting
	@TextFieldSettingDescriptor(name = "Flow instance multi sign email subject (users)", description = "The subject of emails sent to the users when they have to sign a multi sign flow instance", required = true)
	@XSLVariable(prefix = "java.")
	private String flowInstanceMultiSignInitiatedUserEmailSubject;

	@ModuleSetting
	@HTMLEditorSettingDescriptor(name = "Flow instance multi sign email message (users)", description = "The message of emails sent to the users when they have to sign a multi sign flow instance", required = true)
	@XSLVariable(prefix = "java.")
	private String flowInstanceMultiSignInitiatedUserEmailMessage;

	@ModuleSetting
	@CheckboxSettingDescriptor(name = "Send email to managers when new messages are received from users", description = "Controls if email messages are sent to the managers when they receive new messages from users.")
	private boolean sendExternalMessageReceivedManagerEmail;

	@ModuleSetting
	@TextFieldSettingDescriptor(name = "New message received email subject (managers)", description = "The subject of emails sent to the managers when new messages are received", required = true)
	@XSLVariable(prefix = "java.")
	private String externalMessageReceivedManagerEmailSubject;

	@ModuleSetting
	@HTMLEditorSettingDescriptor(name = "New message received email message (managers)", description = "The message of emails sent to the managers when new messages are received", required = true)
	@XSLVariable(prefix = "java.")
	private String externalMessageReceivedManagerEmailMessage;

	@ModuleSetting
	@CheckboxSettingDescriptor(name = "Send email to managers when they are assigned new flow instances", description = "Controls if email messages are sent to managers when they are assigned new flow instances.")
	private boolean sendFlowInstanceAssignedManagerEmail;

	@ModuleSetting
	@TextFieldSettingDescriptor(name = "Assigned new flow instance email subject (managers)", description = "The subject of emails sent to the managers when they are assigned new flow instances", required = true)
	@XSLVariable(prefix = "java.")
	private String flowInstanceAssignedManagerEmailSubject;

	@ModuleSetting
	@HTMLEditorSettingDescriptor(name = "Assigned new flow instance email message (managers)", description = "The message of emails sent to the users when they are assigned new flow instances", required = true)
	@XSLVariable(prefix = "java.")
	private String flowInstanceAssignedManagerEmailMessage;

	@ModuleSetting
	@CheckboxSettingDescriptor(name = "Send email to managers on status change", description = "Controls if email messages are the sent to the users when the status of their flow instances changes.")
	private boolean sendStatusChangedManagerEmail;

	@ModuleSetting
	@TextFieldSettingDescriptor(name = "Status changed email subject (managers)", description = "The subject of emails sent to the managers when the status of their flow instance changes", required = true)
	@XSLVariable(prefix = "java.")
	private String statusChangedManagerEmailSubject;

	@ModuleSetting
	@HTMLEditorSettingDescriptor(name = "Status changed email message (managers)", description = "The message of emails sent to the managers when the status of their flow instance changes", required = true)
	@XSLVariable(prefix = "java.")
	private String statusChangedManagerEmailMessage;

	@ModuleSetting
	@CheckboxSettingDescriptor(name = "Send email to managers when new flow instances are submitted", description = "Controls if email messages are the sent to managers when new flow instances are submitted.")
	private boolean sendFlowInstanceSubmittedManagerEmail;

	@ModuleSetting
	@TextFieldSettingDescriptor(name = "Flow instance submitted email subject (managers)", description = "The subject of emails sent to the managers when a new flow instance is submitted", required = true)
	@XSLVariable(prefix = "java.")
	private String flowInstanceSubmittedManagerEmailSubject;

	@ModuleSetting
	@HTMLEditorSettingDescriptor(name = "Flow instance submitted email message (managers)", description = "The message of emails sent to the managers when a new flow instance is submitted", required = true)
	@XSLVariable(prefix = "java.")
	private String flowInstanceSubmittedManagerEmailMessage;

	@ModuleSetting
	@CheckboxSettingDescriptor(name = "Send email to the specified address when new flow instances are submitted", description = "Controls if email messages are the sent to specified address when new flow instances are submitted.")
	private boolean sendFlowInstanceSubmittedGlobalEmail;

	@ModuleSetting
	@TextFieldSettingDescriptor(name = "Flow instance submitted email subject (global)", description = "The subject of emails sent when a new flow instance is submitted", required = true)
	@XSLVariable(prefix = "java.")
	private String flowInstanceSubmittedGlobalEmailSubject;

	@ModuleSetting
	@HTMLEditorSettingDescriptor(name = "Flow instance submitted email message (global)", description = "The message of emails sent when a new flow instance is submitted", required = true)
	@XSLVariable(prefix = "java.")
	private String flowInstanceSubmittedGlobalEmailMessage;

	@ModuleSetting(allowsNull=true)
	@TextAreaSettingDescriptor(name = "Flow instance submitted email address (global)", description = "Global address to be notified when new flow instances are submitted", formatValidator = EmailPopulator.class)
	private List<String> flowInstanceSubmittedGlobalEmailAddress;

	@ModuleSetting
	@CheckboxSettingDescriptor(name = "Attach PDF to flow instance submitted emails (global)", description = "Controls if generated PDF documents are attached to email messages when new flow instances are submitted.")
	private boolean flowInstanceSubmittedGlobalEmailAttachPDF;

	@ModuleSetting
	@CheckboxSettingDescriptor(name = "Attach PDF attachments to flow instance submitted emails (global) separately", description = "Controls if attachments for generated PDF documents are attached to email messages separately when new flow instances are submitted.")
	private boolean flowInstanceSubmittedGlobalEmailAttachPDFAttachmentsSeparately;

	@ModuleSetting
	@TextFieldSettingDescriptor(name = "PDF size limit for flow instance submitted emails (global)", description = "The size limit in megabyte for PDF documents attached to email messages when new flow instances are submitted. If this size is exceeded no PDF is attached. No value set means no size limit.")
	private Integer flowInstanceSubmittedGlobalEmailPDFSizeLimit;

	@ModuleSetting
	@TextFieldSettingDescriptor(name = "PDF filename (without file extension)", description = "Filename of the attached PDF (without file extension). Available tags: $flow.name, $flow.version, $flowInstance.flowInstanceID, $poster.*", required = true)
	protected String pdfFilename = "$flow.name, $flowInstance.flowInstanceID";

	@ModuleSetting
	@CheckboxSettingDescriptor(name = "Send email to the specified address when new flow instances are submitted", description = "Controls if email messages are the sent to specified address when new flow instances are submitted.")
	private boolean sendExternalMessageReceivedGlobalEmail;

	@ModuleSetting
	@TextFieldSettingDescriptor(name = "New message received email subject (global)", description = "The subject of emails sent when a new messages are received", required = true)
	@XSLVariable(prefix = "java.")
	private String externalMessageReceivedGlobalEmailSubject;

	@ModuleSetting
	@HTMLEditorSettingDescriptor(name = "New message received email message (global)", description = "The message of emails sent when a new messages are received", required = true)
	@XSLVariable(prefix = "java.")
	private String externalMessageReceivedGlobalEmailMessage;

	@ModuleSetting(allowsNull=true)
	@TextAreaSettingDescriptor(name = "New message received email address (global)", description = "Global address to be notified when new messages are received", formatValidator = EmailPopulator.class)
	private List<String> externalMessageReceivedGlobalEmailAddresses;
	
	@ModuleSetting(allowsNull = true)
	@GroupMultiListSettingDescriptor(name = "Admin group", description = "Groups that have access to the administrative functions in this module.")
	protected List<Integer> adminGroupIDs;

	@InstanceManagerDependency
	protected PDFProvider pdfProvider;

	@InstanceManagerDependency
	protected SMSSender smsSender;

	@InstanceManagerDependency
	protected MultiSigningHandler multiSigningHandler;

	@InstanceManagerDependency(required = true)
	protected QueryHandler queryHandler;

	private AnnotatedDAOWrapper<FlowFamililyNotificationSettings, Integer> notificationSettingsDAO;
	private AnnotatedDAOWrapper<FlowFamily, Integer> flowFamilyDAO;

	private AnnotatedDAO<FlowInstance> flowInstanceDAO;

	protected QueryParameterFactory<FlowInstance, Integer> flowInstanceIDParamFactory;
	protected QueryParameterFactory<QueryInstanceDescriptor, Integer> queryInstanceDescriptorFlowInstanceIDParamFactory;

	private ModuleViewFragmentTransformer<ForegroundModuleDescriptor> viewFragmentTransformer;

	private Integer initialTableVersion;

	protected CopyOnWriteArrayList<NotificationEmailFilter> notificationEmailFilters = new CopyOnWriteArrayList<NotificationEmailFilter>();

	@Override
	public void init(ForegroundModuleDescriptor moduleDescriptor, SectionInterface sectionInterface, DataSource dataSource) throws Exception {

		super.init(moduleDescriptor, sectionInterface, dataSource);

		if (initialTableVersion != null && initialTableVersion < 4) {

			flowInstanceAssignedManagerEmailMessage = fixPosterTag(flowInstanceAssignedManagerEmailMessage);
			flowInstanceSubmittedManagerEmailMessage = fixPosterTag(flowInstanceSubmittedManagerEmailMessage);
			externalMessageReceivedManagerEmailMessage = fixPosterTag(externalMessageReceivedManagerEmailMessage);
			statusChangedManagerEmailMessage = fixPosterTag(statusChangedManagerEmailMessage);
			flowInstanceSubmittedGlobalEmailMessage = fixPosterTag(flowInstanceSubmittedGlobalEmailMessage);

			MutableSettingHandler settingHandler = this.moduleDescriptor.getMutableSettingHandler();

			settingHandler.setSetting("flowInstanceAssignedManagerEmailMessage", flowInstanceAssignedManagerEmailMessage);
			settingHandler.setSetting("flowInstanceSubmittedManagerEmailMessage", flowInstanceSubmittedManagerEmailMessage);
			settingHandler.setSetting("externalMessageReceivedManagerEmailMessage", externalMessageReceivedManagerEmailMessage);
			settingHandler.setSetting("statusChangedManagerEmailMessage", statusChangedManagerEmailMessage);
			settingHandler.setSetting("flowInstanceSubmittedGlobalEmailMessage", flowInstanceSubmittedGlobalEmailMessage);

			this.moduleDescriptor.saveSettings(systemInterface);
		}

		if (initialTableVersion != null && initialTableVersion < 7) {
			
			statusChangedUserEmailMessage = fixLinkTags(statusChangedUserEmailMessage);
			externalMessageReceivedUserEmailMessage = fixLinkTags(externalMessageReceivedUserEmailMessage);
			flowInstanceSubmittedUserEmailMessage = fixLinkTags(flowInstanceSubmittedUserEmailMessage);
			flowInstanceSubmittedNotLoggedInUserEmailMessage = fixLinkTags(flowInstanceSubmittedNotLoggedInUserEmailMessage);
			flowInstanceArchivedUserEmailMessage = fixLinkTags(flowInstanceArchivedUserEmailMessage);
			flowInstanceArchivedNotLoggedInUserEmailMessage = fixLinkTags(flowInstanceArchivedNotLoggedInUserEmailMessage);
			flowInstanceMultiSignInitiatedUserEmailMessage = fixLinkTags(flowInstanceMultiSignInitiatedUserEmailMessage);
			externalMessageReceivedManagerEmailSubject = fixLinkTags(externalMessageReceivedManagerEmailSubject);
			externalMessageReceivedManagerEmailMessage = fixLinkTags(externalMessageReceivedManagerEmailMessage);
			flowInstanceAssignedManagerEmailMessage = fixLinkTags(flowInstanceAssignedManagerEmailMessage);
			
			MutableSettingHandler settingHandler = this.moduleDescriptor.getMutableSettingHandler();
			
			settingHandler.setSetting("statusChangedUserEmailMessage", statusChangedUserEmailMessage);
			settingHandler.setSetting("externalMessageReceivedUserEmailMessage", externalMessageReceivedUserEmailMessage);
			settingHandler.setSetting("flowInstanceSubmittedUserEmailMessage", flowInstanceSubmittedUserEmailMessage);
			settingHandler.setSetting("flowInstanceSubmittedNotLoggedInUserEmailMessage", flowInstanceSubmittedNotLoggedInUserEmailMessage);
			settingHandler.setSetting("flowInstanceArchivedUserEmailMessage", flowInstanceArchivedUserEmailMessage);
			settingHandler.setSetting("flowInstanceArchivedNotLoggedInUserEmailMessage", flowInstanceArchivedNotLoggedInUserEmailMessage);
			settingHandler.setSetting("flowInstanceMultiSignInitiatedUserEmailMessage", flowInstanceMultiSignInitiatedUserEmailMessage);
			settingHandler.setSetting("externalMessageReceivedManagerEmailSubject", externalMessageReceivedManagerEmailSubject);
			settingHandler.setSetting("externalMessageReceivedManagerEmailMessage", externalMessageReceivedManagerEmailMessage);
			settingHandler.setSetting("flowInstanceAssignedManagerEmailMessage", flowInstanceAssignedManagerEmailMessage);


			this.moduleDescriptor.saveSettings(systemInterface);
		}
		
		if (!systemInterface.getInstanceHandler().addInstance(FlowNotificationHandler.class, this)) {

			throw new RuntimeException("Unable to register module in global instance handler using key " + FlowNotificationHandler.class.getSimpleName() + ", another instance is already registered using this key.");
		}

		if (!systemInterface.getInstanceHandler().addInstance(StandardFlowNotificationHandler.class, this)) {

			throw new RuntimeException("Unable to register module in global instance handler using key " + StandardFlowNotificationHandler.class.getSimpleName() + ", another instance is already registered using this key.");
		}

		this.viewFragmentTransformer = new ModuleViewFragmentTransformer<ForegroundModuleDescriptor>(sectionInterface.getModuleXSLTCache(), this, systemInterface.getEncoding());

		this.viewFragmentTransformer.setDebugXML(debugFragmententXML);
	}

	@Override
	public void update(ForegroundModuleDescriptor descriptor, DataSource dataSource) throws Exception {

		super.update(descriptor, dataSource);

		this.viewFragmentTransformer.setDebugXML(debugFragmententXML);
	}

	@Override
	public void unload() throws Exception {

		systemInterface.getInstanceHandler().removeInstance(FlowNotificationHandler.class, this);
		systemInterface.getInstanceHandler().removeInstance(StandardFlowNotificationHandler.class, this);

		notificationEmailFilters.clear();

		super.unload();
	}

	@Override
	protected void createDAOs(DataSource dataSource) throws Exception {

		initialTableVersion = TableVersionHandler.getTableGroupVersion(dataSource, StandardFlowNotificationHandler.class.getName());

		//Automatic table version handling
		UpgradeResult upgradeResult = TableVersionHandler.upgradeDBTables(dataSource, StandardFlowNotificationHandler.class.getName(), new XMLDBScriptProvider(this.getClass().getResourceAsStream("DB script.xml")));

		if (upgradeResult.isUpgrade()) {

			log.info(upgradeResult.toString());
		}

		SimpleAnnotatedDAOFactory daoFactory = new SimpleAnnotatedDAOFactory(dataSource);

		notificationSettingsDAO = daoFactory.getDAO(FlowFamililyNotificationSettings.class).getWrapper(Integer.class);

		FlowEngineDAOFactory flowEngineDAOFactory = new FlowEngineDAOFactory(dataSource, systemInterface.getUserHandler(), systemInterface.getGroupHandler());

		flowInstanceDAO = flowEngineDAOFactory.getFlowInstanceDAO();
		flowInstanceIDParamFactory = flowInstanceDAO.getParamFactory("flowInstanceID", Integer.class);
		queryInstanceDescriptorFlowInstanceIDParamFactory = flowEngineDAOFactory.getQueryInstanceDescriptorDAO().getParamFactory("flowInstanceID", Integer.class);

		flowFamilyDAO = flowEngineDAOFactory.getFlowFamilyDAO().getWrapper(Integer.class);
		flowFamilyDAO.addRelations(FlowFamily.MANAGER_USERS_RELATION, FlowFamily.MANAGER_GROUPS_RELATION);
		flowFamilyDAO.setUseRelationsOnGet(true);
	}

	private String fixPosterTag(String message) {

		if (message != null) {

			return message.replace("$poster", "$contact");
		}

		return null;
	}
	
	private String fixLinkTags(String message) {

		if(message != null){
			
			return message.replace("<a src=", "<a href=");
		}
		
		return null;
	}


	@EventListener(channel = FlowFamily.class)
	public void processEvent(CRUDEvent<FlowFamily> event, EventSource eventSource) {

		if (event.getAction() == CRUDAction.DELETE) {

			for (FlowFamily flowFamily : event.getBeans()) {

				log.info("Deleteing notification settings for flowFamiliyID: " + flowFamily.getFlowFamilyID());

				try {
					notificationSettingsDAO.deleteByID(flowFamily.getFlowFamilyID());

				} catch (SQLException e) {

					log.error("Error deleteing notification settings for flowFamiliyID: " + flowFamily.getFlowFamilyID(), e);
				}
			}
		}
	}

	@Override
	public ViewFragment getCurrentSettingsView(ImmutableFlow flow, HttpServletRequest req, User user, URIParser uriParser) throws SQLException, TransformerConfigurationException, TransformerException {

		FlowFamililyNotificationSettings notificationSettings = getNotificationSettings(flow);

		Document doc = createDocument(req, uriParser);

		Element showSettingsElement = doc.createElement("ShowSettings");
		doc.getDocumentElement().appendChild(showSettingsElement);

		showSettingsElement.appendChild(notificationSettings.toXML(doc));

		return viewFragmentTransformer.createViewFragment(doc);
	}

	@Override
	public ViewFragment getUpdateSettingsView(ImmutableFlow flow, HttpServletRequest req, User user, URIParser uriParser, ValidationException validationException) throws TransformerConfigurationException, TransformerException, SQLException {

		FlowFamililyNotificationSettings notificationSettings = getNotificationSettings(flow);

		Document doc = createDocument(req, uriParser);

		Element updateSettingsElement = doc.createElement("UpdateSettings");
		doc.getDocumentElement().appendChild(updateSettingsElement);

		updateSettingsElement.appendChild(notificationSettings.toXML(doc));

		if (validationException != null) {

			updateSettingsElement.appendChild(validationException.toXML(doc));
			updateSettingsElement.appendChild(RequestUtils.getRequestParameters(req, doc));
		}

		return viewFragmentTransformer.createViewFragment(doc);
	}

	@Override
	public void updateSettings(ImmutableFlow flow, HttpServletRequest req, User user, URIParser uriParser) throws ValidationException, Exception {

		if (req.getParameter("reset") != null) {

			this.notificationSettingsDAO.deleteByID(flow.getFlowFamily().getFlowFamilyID());
			return;
		}

		FlowFamililyNotificationSettings notificationSettings = POPULATOR.populate(req);

		//Remove texts if they are set to default values
		if (notificationSettings.getFlowInstanceArchivedUserEmailMessage() != null && notificationSettings.getFlowInstanceArchivedUserEmailMessage().equals(flowInstanceArchivedUserEmailMessage)) {

			notificationSettings.setFlowInstanceArchivedUserEmailMessage(null);
		}

		if (notificationSettings.getFlowInstanceArchivedUserEmailSubject() != null && notificationSettings.getFlowInstanceArchivedUserEmailSubject().equals(flowInstanceArchivedUserEmailSubject)) {

			notificationSettings.setFlowInstanceArchivedUserEmailSubject(null);
		}

		if (notificationSettings.getFlowInstanceArchivedNotLoggedInUserEmailMessage() != null && notificationSettings.getFlowInstanceArchivedNotLoggedInUserEmailMessage().equals(flowInstanceArchivedNotLoggedInUserEmailMessage)) {

			notificationSettings.setFlowInstanceArchivedNotLoggedInUserEmailMessage(null);
		}

		if (notificationSettings.getFlowInstanceSubmittedUserEmailMessage() != null && notificationSettings.getFlowInstanceSubmittedUserEmailMessage().equals(flowInstanceSubmittedUserEmailMessage)) {

			notificationSettings.setFlowInstanceSubmittedUserEmailMessage(null);
		}

		if (notificationSettings.getFlowInstanceSubmittedUserEmailSubject() != null && notificationSettings.getFlowInstanceSubmittedUserEmailSubject().equals(flowInstanceSubmittedUserEmailSubject)) {

			notificationSettings.setFlowInstanceSubmittedUserEmailSubject(null);
		}

		if (notificationSettings.getFlowInstanceSubmittedNotLoggedInUserEmailMessage() != null && notificationSettings.getFlowInstanceSubmittedNotLoggedInUserEmailMessage().equals(flowInstanceSubmittedNotLoggedInUserEmailMessage)) {

			notificationSettings.setFlowInstanceSubmittedNotLoggedInUserEmailMessage(null);
		}

		if (notificationSettings.getFlowInstanceSubmittedGlobalEmailSubject() != null && notificationSettings.getFlowInstanceSubmittedGlobalEmailSubject().equals(flowInstanceSubmittedGlobalEmailSubject)) {

			notificationSettings.setFlowInstanceSubmittedGlobalEmailSubject(null);
		}

		if (notificationSettings.getFlowInstanceSubmittedGlobalEmailMessage() != null && notificationSettings.getFlowInstanceSubmittedGlobalEmailMessage().equals(flowInstanceSubmittedGlobalEmailMessage)) {

			notificationSettings.setFlowInstanceSubmittedGlobalEmailMessage(null);
		}

		notificationSettings.setFlowFamilyID(flow.getFlowFamily().getFlowFamilyID());

		notificationSettingsDAO.getAnnotatedDAO().addOrUpdate(notificationSettings, null);
	}

	public FlowFamililyNotificationSettings getNotificationSettings(ImmutableFlow flow) throws SQLException {

		FlowFamililyNotificationSettings notificationSettings = notificationSettingsDAO.get(flow.getFlowFamily().getFlowFamilyID());

		if (notificationSettings != null) {

			if (notificationSettings.getFlowInstanceArchivedUserEmailMessage() == null) {

				notificationSettings.setFlowInstanceArchivedUserEmailMessage(flowInstanceArchivedUserEmailMessage);
			}

			if (notificationSettings.getFlowInstanceArchivedNotLoggedInUserEmailMessage() == null) {

				notificationSettings.setFlowInstanceArchivedNotLoggedInUserEmailMessage(flowInstanceArchivedNotLoggedInUserEmailMessage);
			}

			if (notificationSettings.getFlowInstanceArchivedUserEmailSubject() == null) {

				notificationSettings.setFlowInstanceArchivedUserEmailSubject(flowInstanceArchivedUserEmailSubject);
			}

			if (notificationSettings.getFlowInstanceSubmittedUserEmailMessage() == null) {

				notificationSettings.setFlowInstanceSubmittedUserEmailMessage(flowInstanceSubmittedUserEmailMessage);
			}

			if (notificationSettings.getFlowInstanceSubmittedNotLoggedInUserEmailMessage() == null) {

				notificationSettings.setFlowInstanceSubmittedNotLoggedInUserEmailMessage(flowInstanceSubmittedNotLoggedInUserEmailMessage);
			}

			if (notificationSettings.getFlowInstanceSubmittedUserEmailSubject() == null) {

				notificationSettings.setFlowInstanceSubmittedUserEmailSubject(flowInstanceSubmittedUserEmailSubject);
			}

			if (notificationSettings.getFlowInstanceSubmittedGlobalEmailSubject() == null) {

				notificationSettings.setFlowInstanceSubmittedGlobalEmailSubject(flowInstanceSubmittedGlobalEmailSubject);
			}

			if (notificationSettings.getFlowInstanceSubmittedGlobalEmailMessage() == null) {

				notificationSettings.setFlowInstanceSubmittedGlobalEmailMessage(flowInstanceSubmittedGlobalEmailMessage);
			}

			return notificationSettings;
		}

		notificationSettings = new FlowFamililyNotificationSettings();

		notificationSettings.setSendFlowInstanceArchivedUserEmail(sendFlowInstanceArchivedUserEmail);
		notificationSettings.setSendFlowInstanceArchivedUserSMS(sendFlowInstanceArchivedUserSMS);
		notificationSettings.setFlowInstanceArchivedUserEmailMessage(flowInstanceArchivedUserEmailMessage);
		notificationSettings.setFlowInstanceArchivedNotLoggedInUserEmailMessage(flowInstanceArchivedNotLoggedInUserEmailMessage);
		notificationSettings.setFlowInstanceArchivedUserEmailSubject(flowInstanceArchivedUserEmailSubject);

		notificationSettings.setSendFlowInstanceSubmittedUserEmail(sendFlowInstanceSubmittedUserEmail);
		notificationSettings.setSendFlowInstanceSubmittedUserSMS(sendFlowInstanceSubmittedUserSMS);
		notificationSettings.setFlowInstanceSubmittedUserEmailMessage(flowInstanceSubmittedUserEmailMessage);
		notificationSettings.setFlowInstanceSubmittedNotLoggedInUserEmailMessage(flowInstanceSubmittedNotLoggedInUserEmailMessage);
		notificationSettings.setFlowInstanceSubmittedUserEmailSubject(flowInstanceSubmittedUserEmailSubject);
		notificationSettings.setFlowInstanceSubmittedUserEmailAttachPDF(flowInstanceSubmittedUserEmailAttachPDF);

		notificationSettings.setSendExternalMessageReceivedManagerEmail(sendExternalMessageReceivedManagerEmail);
		notificationSettings.setSendExternalMessageReceivedUserEmail(sendExternalMessageReceivedUserEmail);
		notificationSettings.setSendExternalMessageReceivedUserSMS(sendExternalMessageReceivedUserSMS);

		notificationSettings.setSendFlowInstanceAssignedManagerEmail(sendFlowInstanceAssignedManagerEmail);

		notificationSettings.setSendStatusChangedManagerEmail(sendStatusChangedManagerEmail);
		notificationSettings.setSendStatusChangedUserEmail(sendStatusChangedUserEmail);
		notificationSettings.setSendStatusChangedUserSMS(sendStatusChangedUserSMS);

		notificationSettings.setSendFlowInstanceSubmittedManagerEmail(sendFlowInstanceSubmittedManagerEmail);

		notificationSettings.setSendFlowInstanceSubmittedGlobalEmail(sendFlowInstanceSubmittedGlobalEmail);
		notificationSettings.setFlowInstanceSubmittedGlobalEmailAddresses(flowInstanceSubmittedGlobalEmailAddress);
		notificationSettings.setFlowInstanceSubmittedGlobalEmailSubject(flowInstanceSubmittedGlobalEmailSubject);
		notificationSettings.setFlowInstanceSubmittedGlobalEmailMessage(flowInstanceSubmittedGlobalEmailMessage);

		return notificationSettings;
	}

	public Document createDocument(HttpServletRequest req, URIParser uriParser) {

		Document doc = XMLUtils.createDomDocument();
		Element document = doc.createElement("Document");
		document.appendChild(RequestUtils.getRequestInfoAsXML(doc, req, uriParser));
		document.appendChild(this.sectionInterface.getSectionDescriptor().toXML(doc));
		document.appendChild(this.moduleDescriptor.toXML(doc));

		doc.appendChild(document);

		return doc;
	}

	public boolean equalsDefaultValues(FlowFamililyNotificationSettings other) {

		if (flowInstanceArchivedUserEmailMessage == null) {

			if (other.getFlowInstanceArchivedUserEmailMessage() != null) {
				return false;
			}

		} else if (!flowInstanceArchivedUserEmailMessage.equals(other.getFlowInstanceArchivedUserEmailMessage())) {

			return false;
		}

		if (flowInstanceArchivedNotLoggedInUserEmailMessage == null) {

			if (other.getFlowInstanceArchivedNotLoggedInUserEmailMessage() != null) {
				return false;
			}

		} else if (!flowInstanceArchivedNotLoggedInUserEmailMessage.equals(other.getFlowInstanceArchivedNotLoggedInUserEmailMessage())) {

			return false;
		}

		if (flowInstanceArchivedUserEmailSubject == null) {

			if (other.getFlowInstanceArchivedUserEmailSubject() != null) {
				return false;
			}

		} else if (!flowInstanceArchivedUserEmailSubject.equals(other.getFlowInstanceArchivedUserEmailSubject())) {

			return false;
		}

		if (flowInstanceSubmittedGlobalEmailAddress == null) {

			if (other.getFlowInstanceSubmittedGlobalEmailAddresses() != null) {
				
				return false;
			}

		} else if (!flowInstanceSubmittedGlobalEmailAddress.equals(other.getFlowInstanceSubmittedGlobalEmailAddresses())) {

			return false;
		}

		if (flowInstanceSubmittedUserEmailMessage == null) {

			if (other.getFlowInstanceSubmittedUserEmailMessage() != null) {
				return false;
			}

		} else if (!flowInstanceSubmittedUserEmailMessage.equals(other.getFlowInstanceSubmittedUserEmailMessage())) {

			return false;
		}

		if (flowInstanceSubmittedNotLoggedInUserEmailMessage == null) {

			if (other.getFlowInstanceSubmittedNotLoggedInUserEmailMessage() != null) {
				return false;
			}

		} else if (!flowInstanceSubmittedNotLoggedInUserEmailMessage.equals(other.getFlowInstanceSubmittedNotLoggedInUserEmailMessage())) {

			return false;
		}

		if (flowInstanceSubmittedUserEmailSubject == null) {

			if (other.getFlowInstanceSubmittedUserEmailSubject() != null) {
				return false;
			}

		} else if (!flowInstanceSubmittedUserEmailSubject.equals(other.getFlowInstanceSubmittedUserEmailSubject())) {

			return false;
		}

		if (flowInstanceSubmittedGlobalEmailMessage == null) {

			if (other.getFlowInstanceSubmittedGlobalEmailMessage() != null) {
				return false;
			}

		} else if (!flowInstanceSubmittedGlobalEmailMessage.equals(other.getFlowInstanceSubmittedGlobalEmailMessage())) {

			return false;
		}

		if (flowInstanceSubmittedGlobalEmailSubject == null) {

			if (other.getFlowInstanceSubmittedGlobalEmailSubject() != null) {
				return false;
			}

		} else if (!flowInstanceSubmittedGlobalEmailSubject.equals(other.getFlowInstanceSubmittedGlobalEmailSubject())) {

			return false;
		}

		if (sendExternalMessageReceivedManagerEmail != other.isSendExternalMessageReceivedManagerEmail()) {
			return false;
		}

		if (sendExternalMessageReceivedUserEmail != other.isSendExternalMessageReceivedUserEmail()) {
			return false;
		}

		if (sendExternalMessageReceivedUserSMS != other.isSendExternalMessageReceivedUserSMS()) {
			return false;
		}

		if (sendFlowInstanceArchivedUserEmail != other.isSendFlowInstanceArchivedUserEmail()) {
			return false;
		}

		if (sendFlowInstanceArchivedUserSMS != other.isSendFlowInstanceArchivedUserSMS()) {
			return false;
		}

		if (sendFlowInstanceAssignedManagerEmail != other.isSendFlowInstanceAssignedManagerEmail()) {
			return false;
		}

		if (sendFlowInstanceSubmittedManagerEmail != other.isSendFlowInstanceSubmittedManagerEmail()) {
			return false;
		}

		if (sendFlowInstanceSubmittedUserEmail != other.isSendFlowInstanceSubmittedUserEmail()) {
			return false;
		}

		if (flowInstanceSubmittedUserEmailAttachPDF != other.isFlowInstanceSubmittedUserEmailAttachPDF()) {
			return false;
		}

		if (sendFlowInstanceSubmittedUserSMS != other.isSendFlowInstanceSubmittedUserSMS()) {
			return false;
		}

		if (sendStatusChangedManagerEmail != other.isSendStatusChangedManagerEmail()) {
			return false;
		}

		if (sendStatusChangedUserEmail != other.isSendStatusChangedUserEmail()) {
			return false;
		}

		if (sendStatusChangedUserSMS != other.isSendStatusChangedUserSMS()) {
			return false;
		}

		if (sendFlowInstanceSubmittedGlobalEmail != other.isSendFlowInstanceSubmittedGlobalEmail()) {
			return false;
		}

		if (flowInstanceSubmittedGlobalEmailAttachPDF != other.isFlowInstanceSubmittedGlobalEmailAttachPDF()) {
			return false;
		}

		if (flowInstanceSubmittedGlobalEmailAttachPDFAttachmentsSeparately != other.isFlowInstanceSubmittedGlobalEmailAttachPDFAttachmentsSeparately()) {
			return false;
		}

		if(sendExternalMessageReceivedGlobalEmail != other.isSendExternalMessageReceivedGlobalEmail()){
			return false;
		}
		
		return true;
	}

	@WebPublic(alias = "globalmailsubmit")
	public ForegroundModuleResponse triggerGlobalSubmit(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws ModuleConfigurationException, SQLException, AccessDeniedException, IOException, URINotFoundException {

		if (!AccessUtils.checkAccess(user, new SimpleAccessInterface(adminGroupIDs, null))) {

			throw new AccessDeniedException("User does not have admin access to this module");
		}

		Integer flowInstanceID = uriParser.getInt(2);
		Integer eventID = uriParser.getInt(3);

		if (flowInstanceID == null || eventID == null) {

			throw new URINotFoundException(uriParser);
		}

		FlowInstance flowInstance = getFlowInstance(flowInstanceID);

		if (flowInstance == null) {

			throw new URINotFoundException(uriParser);
		}
		
		FlowInstanceEvent submitEvent = null;
		
		if (!CollectionUtils.isEmpty(flowInstance.getEvents())) {
			for (FlowInstanceEvent event : flowInstance.getEvents()) {
				
				if (event.getEventID().equals(eventID)) {
					
					submitEvent = event;
					break;
				}
			}
		}
		
		if (submitEvent == null) {
			
			throw new URINotFoundException(uriParser);
		}
		
		if (submitEvent.getEventType() != EventType.SUBMITTED) {
			
			return new SimpleForegroundModuleResponse("Selected event is not a submit event!");
		}

		FlowFamililyNotificationSettings notificationSettings = getNotificationSettings(flowInstance.getFlow());

		if (notificationSettings.isSendFlowInstanceSubmittedGlobalEmail() && notificationSettings.getFlowInstanceSubmittedGlobalEmailAddresses() != null) {

			boolean attachPDF = false;

			File pdfFile = null;

			if (notificationSettings.isFlowInstanceSubmittedGlobalEmailAttachPDF() && pdfProvider != null) {

				pdfFile = pdfProvider.getPDF(flowInstanceID, eventID);

				if (pdfFile != null) {

					if (isValidPDFSize(flowInstanceSubmittedGlobalEmailPDFSizeLimit, pdfFile)) {

						attachPDF = true;

					} else {

						log.warn("PDF file for flow instance " + flowInstance + " exceeds the size limit of " + flowInstanceSubmittedGlobalEmailPDFSizeLimit + " MB set for global email submit notifications and will not be attached to the generated email.");
					}
				}
			}

			for(String email : notificationSettings.getFlowInstanceSubmittedGlobalEmailAddresses()){
				
				sendGlobalEmail(null, flowInstance, getPosterContact(flowInstance, null), email, flowInstanceSubmittedGlobalEmailSubject, flowInstanceSubmittedGlobalEmailMessage, attachPDF ? pdfFile : null, notificationSettings.isFlowInstanceSubmittedGlobalEmailAttachPDFAttachmentsSeparately());
			}
			

			return new SimpleForegroundModuleResponse("Global email notification triggered for flow instance " + flowInstance);
		}

		return new SimpleForegroundModuleResponse("Global email notifications are disabled for flow " + flowInstance.getFlow());
	}

	@WebPublic(toLowerCase = true)
	public ForegroundModuleResponse triggerSubmit(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws ModuleConfigurationException, SQLException, AccessDeniedException, IOException, URINotFoundException {
		
		if (!AccessUtils.checkAccess(user, new SimpleAccessInterface(adminGroupIDs, null))) {
			
			throw new AccessDeniedException("User does not have admin access to this module");
		}
		
		Integer flowInstanceID = uriParser.getInt(2);
		Integer eventID = uriParser.getInt(3);
		
		return triggerSubmit(flowInstanceID, eventID, uriParser);
	}
	
	@WebPublic(toLowerCase = true)
	public ForegroundModuleResponse triggerSubmits(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws ModuleConfigurationException, SQLException, AccessDeniedException, IOException, URINotFoundException {
		
		if (!AccessUtils.checkAccess(user, new SimpleAccessInterface(adminGroupIDs, null))) {
			
			throw new AccessDeniedException("User does not have admin access to this module");
		}
		
		List<Integer> flowInstanceIDs = NumberUtils.toInt(req.getParameterValues("id"));
		
		if(flowInstanceIDs == null){
			
			throw new URINotFoundException(uriParser);
		}
		
		for(Integer flowInstanceID : flowInstanceIDs){
			
			triggerSubmit(flowInstanceID, null, uriParser);
		}
		
		return new SimpleForegroundModuleResponse("Submit notifications triggered, check log for more information.");
	}	
	
	private ForegroundModuleResponse triggerSubmit(Integer flowInstanceID, Integer eventID, URIParser uriParser) throws URINotFoundException, SQLException {

		if (flowInstanceID == null) {
			
			throw new URINotFoundException(uriParser);
		}
		
		FlowInstance flowInstance = getFlowInstance(flowInstanceID);
		
		if (flowInstance == null) {
			
			throw new URINotFoundException(uriParser);
		}
		
		FlowInstanceEvent submitEvent = null;
		
		if (!CollectionUtils.isEmpty(flowInstance.getEvents())) {
			
			if (eventID != null) {
				
				for (FlowInstanceEvent event : flowInstance.getEvents()) {
					
					if (event.getEventID().equals(eventID)) {
						
						submitEvent = event;
						break;
					}
				}
				
			} else {
				
				for (int i = flowInstance.getEvents().size() - 1; i >= 0; i--) {
					
					FlowInstanceEvent event = flowInstance.getEvents().get(i);
					
					if (event.getEventType() == EventType.SUBMITTED) {
						
						submitEvent = event;
						break;
					}
				}
			}
		}
		
		if (submitEvent == null) {
			
			throw new URINotFoundException(uriParser);
		}
		
		if (submitEvent.getEventType() != EventType.SUBMITTED) {
			
			return new SimpleForegroundModuleResponse("Selected event is not a submit event!");
		}
		
		FlowFamililyNotificationSettings notificationSettings = getNotificationSettings(flowInstance.getFlow());
		
		File pdfFile = null;
		
		if (pdfProvider != null && notificationSettings.isFlowInstanceSubmittedUserEmailAttachPDF()) {
			
			pdfFile = pdfProvider.getPDF(flowInstance.getFlowInstanceID(), eventID);
		}
		
		Collection<Contact> contacts = getContacts(flowInstance);
		
		if (contacts != null) {
			
			long sentEmails = 0;
			long sentSMS = 0;
			
			if (notificationSettings.isSendFlowInstanceSubmittedUserSMS()) {
				
				for (Contact contact : contacts) {
					
					if (sendContactSMS(flowInstance, contact, getFlowInstaceSubmittedUserSMSMessage(flowInstance))) {
						
						sentSMS++;
					}
				}
			}
			
			if (notificationSettings.isSendFlowInstanceSubmittedUserEmail()) {
				
				boolean attachPDF = false;
				
				String message = getFlowInstaceSubmittedUserEmailMessage(notificationSettings, flowInstance);
				
				if (notificationSettings.isFlowInstanceSubmittedUserEmailAttachPDF() && pdfFile != null) {
					
					if (isValidPDFSize(flowInstanceSubmittedUserEmailPDFSizeLimit, pdfFile)) {
						
						attachPDF = true;
						
						message = message.replaceAll("\\$flowInstance.pdfAttachedText", flowInstanceSubmittedUserEmailPDFAttachedText);
						
					} else {
						
						message = message.replaceAll("\\$flowInstance.pdfAttachedText", flowInstanceSubmittedUserEmailPDFSizeLimitExceededText);
						
						log.warn("PDF file for flow instance " + flowInstance + " exceeds the size limit of " + flowInstanceSubmittedUserEmailPDFSizeLimit + " MB set for user email submit notifications and will not be attached to the generated email.");
					}
				}
				
				for (Contact contact : contacts) {
					
					if (sendContactEmail(flowInstance, contact, notificationSettings.getFlowInstanceSubmittedUserEmailSubject(), message, attachPDF ? pdfFile : null)) {
						
						sentEmails++;
					}
				}
			}
		
			log.info(sentEmails + " E-mail and " + sentSMS + " SMS submit notifications sent to " + contacts.size() + " contacts for flow instance " + flowInstance);
			
			return new SimpleForegroundModuleResponse(sentEmails + " E-mail and " + sentSMS + " SMS submit notifications sent to " + contacts.size() + " contacts for flow instance " + flowInstance);
		}
		
		log.info("No contacts to send notifications to for flow instance " + flowInstance);
		
		return new SimpleForegroundModuleResponse("No contacts to send notifications to for flow instance " + flowInstance);
	}

	@WebPublic(toLowerCase = true)
	public ForegroundModuleResponse setContactAttributes(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws ModuleConfigurationException, SQLException, AccessDeniedException, IOException, URINotFoundException {
		
		if (!AccessUtils.checkAccess(user, new SimpleAccessInterface(adminGroupIDs, null))) {
			
			throw new AccessDeniedException("User does not have admin access to this module");
		}
		
		List<Integer> flowInstanceIDs = new ArrayList<Integer>();
		
		{
			Integer flowInstanceID = uriParser.getInt(2);
			
			if (flowInstanceID != null) {
				
				flowInstanceIDs.add(flowInstanceID);
			}
		}
		
		String[] paramFlowInstanceIDs = req.getParameterValues("id");
		
		if (!ArrayUtils.isEmpty(paramFlowInstanceIDs)) {
			
			for (String stringID : paramFlowInstanceIDs) {
				
				Integer flowInstanceID = NumberUtils.toInt(stringID);
				
				if(flowInstanceID != null){
					
					flowInstanceIDs.add(flowInstanceID);
				}
			}
		}
		
		long updatedFlowInstances = 0;
		
		for (Integer flowInstanceID : flowInstanceIDs) {
			
			try {
				FlowInstance flowInstance = getFlowInstance(flowInstanceID);
				
				if (flowInstance != null) {
					
					ImmutableFlowInstanceManager instanceManager = new ImmutableFlowInstanceManager(flowInstance, queryHandler, null, new DefaultInstanceMetadata(null), null);
					
					FlowInstanceUtils.setContactAttributes(instanceManager, flowInstance.getAttributeHandler());
					
					HighLevelQuery<FlowInstance> updateQuery = new HighLevelQuery<FlowInstance>(FlowInstance.ATTRIBUTES_RELATION);
					flowInstanceDAO.update(flowInstance, updateQuery);
					
					log.info("Updated contact attributes for flow instance " + flowInstance);
					
					updatedFlowInstances++;
				}
				
			} catch (Exception e) {
				
				log.error("Error updating contact attributes for flow instance ID " + flowInstanceID, e);
			}
		}
		
		return new SimpleForegroundModuleResponse("Updated contact attributes for " + updatedFlowInstances + " flow instances.");
	}
	
	@EventListener(channel = FlowInstanceManager.class, priority = 50)
	public void processEvent(SubmitEvent event, EventSource eventSource) throws SQLException {

		if (event.getActionID() != null && (event.getActionID().equals(FlowBrowserModule.SUBMIT_ACTION_ID) || event.getActionID().equals(UserFlowInstanceModule.SUBMIT_COMPLETION_ACTION_ID))) {

			FlowFamililyNotificationSettings notificationSettings = getNotificationSettings(event.getFlowInstanceManager().getFlowInstance().getFlow());

			Contact posterContact = getPosterContact(event.getFlowInstanceManager().getFlowInstance());
			Collection<Contact> contacts = getContacts(event.getFlowInstanceManager().getFlowInstance());

			ImmutableFlowInstance flowInstance = event.getFlowInstanceManager().getFlowInstance();

			File pdfFile = null;

			if (pdfProvider != null && (notificationSettings.isFlowInstanceSubmittedUserEmailAttachPDF() || notificationSettings.isFlowInstanceSubmittedGlobalEmailAttachPDF())) {

				pdfFile = pdfProvider.getPDF(flowInstance.getFlowInstanceID(), event.getEvent().getEventID());
			}

			if (contacts != null) {

				if (notificationSettings.isSendFlowInstanceSubmittedUserSMS()) {

					for(Contact contact : contacts){
						
						sendContactSMS(flowInstance, contact, getFlowInstaceSubmittedUserSMSMessage(flowInstance));
					}
				}

				if (notificationSettings.isSendFlowInstanceSubmittedUserEmail()) {

					boolean attachPDF = false;

					String message = getFlowInstaceSubmittedUserEmailMessage(notificationSettings, flowInstance);

					if (notificationSettings.isFlowInstanceSubmittedUserEmailAttachPDF() && pdfFile != null) {

						if (isValidPDFSize(flowInstanceSubmittedUserEmailPDFSizeLimit, pdfFile)) {

							attachPDF = true;

							message = message.replaceAll("\\$flowInstance.pdfAttachedText", flowInstanceSubmittedUserEmailPDFAttachedText);

						} else {

							message = message.replaceAll("\\$flowInstance.pdfAttachedText", flowInstanceSubmittedUserEmailPDFSizeLimitExceededText);

							log.warn("PDF file for flow instance " + flowInstance + " exceeds the size limit of " + flowInstanceSubmittedUserEmailPDFSizeLimit + " MB set for user email submit notifications and will not be attached to the generated email.");
						}
					}

					for(Contact contact : contacts){
						
						sendContactEmail(flowInstance, contact, notificationSettings.getFlowInstanceSubmittedUserEmailSubject(), message, attachPDF ? pdfFile : null);
					}
				}
			}

			if (notificationSettings.isSendFlowInstanceSubmittedManagerEmail()) {

				sendManagerEmails(flowInstance, posterContact, flowInstanceSubmittedManagerEmailSubject, flowInstanceSubmittedManagerEmailMessage, null, true);
			}

			if (notificationSettings.isSendFlowInstanceSubmittedGlobalEmail() && notificationSettings.getFlowInstanceSubmittedGlobalEmailAddresses() != null) {

				boolean attachPDF = false;

				if (notificationSettings.isFlowInstanceSubmittedGlobalEmailAttachPDF() && pdfFile != null) {

					if (isValidPDFSize(flowInstanceSubmittedGlobalEmailPDFSizeLimit, pdfFile)) {

						attachPDF = true;

					} else {

						log.warn("PDF file for flow instance " + flowInstance + " exceeds the size limit of " + flowInstanceSubmittedGlobalEmailPDFSizeLimit + " MB set for global email submit notifications and will not be attached to the generated email.");
					}
				}

				for(String email : notificationSettings.getFlowInstanceSubmittedGlobalEmailAddresses()){
					
					sendGlobalEmail(event.getSiteProfile(), flowInstance, posterContact, email, notificationSettings.getFlowInstanceSubmittedGlobalEmailSubject(), notificationSettings.getFlowInstanceSubmittedGlobalEmailMessage(), attachPDF ? pdfFile : null, notificationSettings.isFlowInstanceSubmittedGlobalEmailAttachPDFAttachmentsSeparately());
				}
			}

		} else if (event.getActionID() != null) {

			log.warn("Submit event received with unsupported action ID: " + event.getActionID());
		}
	}

	private boolean isValidPDFSize(Integer sizeLimit, File pdfFile) {

		return sizeLimit == null || ((sizeLimit * BinarySizes.MegaByte) >= pdfFile.length());

	}

	@EventListener(channel = FlowInstance.class)
	public void processEvent(ExternalMessageAddedEvent event, EventSource eventSource) throws SQLException {
		
		if (CollectionUtils.isEmpty(event.getFlowInstance().getOwners())) {
			
			return;
		}
		
		FlowFamililyNotificationSettings notificationSettings = getNotificationSettings(event.getFlowInstance().getFlow());
		
		if (event.getSenderType() == SenderType.MANAGER) {
			
			if (notificationSettings.isSendExternalMessageReceivedUserEmail() || notificationSettings.isSendExternalMessageReceivedUserSMS()) {
				
				Collection<Contact> contacts = getContacts(event.getFlowInstance(), event.getSiteProfile());
				
				if (contacts != null) {
					
					for (Contact contact : contacts) {
						
						if (notificationSettings.isSendExternalMessageReceivedUserEmail()) {
							
							sendContactEmail(event.getFlowInstance(), contact, externalMessageReceivedUserEmailSubject, externalMessageReceivedUserEmailMessage, null);
						}
						
						if (notificationSettings.isSendExternalMessageReceivedUserSMS()) {
							
							sendContactSMS(event.getFlowInstance(), contact, externalMessageReceivedUserSMS);
						}
					}
				}
			}
			
		} else if (event.getSenderType() == SenderType.USER) {
			
			if (notificationSettings.isSendExternalMessageReceivedManagerEmail() || notificationSettings.isSendExternalMessageReceivedGlobalEmail()) {
			
				Contact contact = getPosterContact(event.getFlowInstance(), event.getSiteProfile());
				
				if (notificationSettings.isSendExternalMessageReceivedManagerEmail()) {
					
					sendManagerEmails(event.getFlowInstance(), contact, externalMessageReceivedManagerEmailSubject, externalMessageReceivedManagerEmailMessage, null, false);
				}
				
				if (notificationSettings.isSendExternalMessageReceivedGlobalEmail() && notificationSettings.getExternalMessageReceivedGlobalEmailAddresses() != null) {
					
					for (String email : notificationSettings.getExternalMessageReceivedGlobalEmailAddresses()) {
						
						sendGlobalEmail(event.getSiteProfile(), event.getFlowInstance(), contact, email, externalMessageReceivedGlobalEmailSubject, externalMessageReceivedGlobalEmailMessage, null, false);
					}
				}
			}
			
		} else {
			
			log.warn("External message added event received with unsupported sender type: " + event.getSenderType());
		}
	}

	@EventListener(channel = FlowInstance.class)
	public void processEvent(StatusChangedByManagerEvent event, EventSource eventSource) throws SQLException {

		FlowFamililyNotificationSettings notificationSettings = getNotificationSettings(event.getFlowInstance().getFlow());

		Contact contact = getPosterContact(event.getFlowInstance(), event.getSiteProfile());
		
		if (!event.isSuppressUserNotifications()) {

			//Check which type of notification the contact should get
			if (event.getPreviousStatus().getContentType() != ContentType.ARCHIVED && event.getFlowInstance().getStatus().getContentType() == ContentType.ARCHIVED) {

				if (notificationSettings.isSendFlowInstanceArchivedUserEmail() || notificationSettings.isSendFlowInstanceArchivedUserSMS()) {
					
					Collection<Contact> contacts = getContacts(event.getFlowInstance(), event.getSiteProfile());
					
					if (contacts != null) {
						
						for (Contact contact2 : contacts) {
							
							if (notificationSettings.isSendFlowInstanceArchivedUserEmail()) {
								
								sendContactEmail(event.getFlowInstance(), contact2, notificationSettings.getFlowInstanceArchivedUserEmailSubject(), getFlowInstaceArchivedUserEmailMessage(notificationSettings, event.getFlowInstance()), null);
							}
							
							if (notificationSettings.isSendFlowInstanceArchivedUserSMS()) {
								
								sendContactSMS(event.getFlowInstance(), contact2, getFlowInstaceArchivedUserSMSMessage(event.getFlowInstance()));
							}
						}
					}
				}

			} else {
				
				if (contact != null) {

					if (notificationSettings.isSendStatusChangedUserEmail()) {

						sendContactEmail(event.getFlowInstance(), contact, statusChangedUserEmailSubject, statusChangedUserEmailMessage, null);
					}

					if (notificationSettings.isSendStatusChangedUserSMS()) {

						sendContactSMS(event.getFlowInstance(), contact, statusChangedUserSMS);
					}
				}
			}
		}

		if (notificationSettings.isSendStatusChangedManagerEmail()) {

			sendManagerEmails(event.getFlowInstance(), contact, statusChangedManagerEmailSubject, statusChangedManagerEmailMessage, CollectionUtils.getList(event.getUser()), false);
		}
	}

	@EventListener(channel = FlowInstance.class)
	public void processEvent(ManagersChangedEvent event, EventSource eventSource) throws SQLException {

		FlowFamililyNotificationSettings notificationSettings = getNotificationSettings(event.getFlowInstance().getFlow());

		if (notificationSettings.isSendFlowInstanceAssignedManagerEmail()) {

			List<User> excludedManagers = new ArrayList<User>();

			if (event.getPreviousManagers() != null) {

				excludedManagers.addAll(event.getPreviousManagers());
			}

			if (event.getUser() != null) {

				excludedManagers.add(event.getUser());
			}

			sendManagerEmails(event.getFlowInstance(), getPosterContact(event.getFlowInstance(), event.getSiteProfile()), flowInstanceAssignedManagerEmailSubject, flowInstanceAssignedManagerEmailMessage, excludedManagers, false);
		}
	}

	@EventListener(channel = FlowInstance.class)
	public void processEvent(MultiSigningInitiatedEvent event, EventSource eventSource) throws SQLException {

		Set<SigningParty> signingParties = MultiSignUtils.getSigningParties(event.getFlowInstanceManager());

		if (signingParties != null) {

			Contact contact = getPosterContact(event.getFlowInstanceManager().getFlowInstance());

			for (SigningParty signingParty : signingParties) {

				sendSigningPartyEmail(event.getFlowInstanceManager().getFlowInstance(), signingParty, contact, this.flowInstanceMultiSignInitiatedUserEmailSubject, this.flowInstanceMultiSignInitiatedUserEmailMessage);
				sendSigningPartySMS(event.getFlowInstanceManager().getFlowInstance(), signingParty, contact, this.flowInstanceMultiSignInitiatedUserSMS);
			}
		}
	}

	@Override
	public ForegroundModuleDescriptor getModuleDescriptor() {

		return moduleDescriptor;
	}

	@Override
	public List<LinkTag> getLinkTags() {

		return links;
	}

	@Override
	public List<ScriptTag> getScriptTags() {

		return scripts;
	}

	private void sendSigningPartyEmail(ImmutableFlowInstance flowInstance, SigningParty signingParty, Contact contact, String subject, String message) {

		if (signingParty.getEmail() == null || subject == null || message == null || multiSigningHandler == null) {

			return;
		}

		TagReplacer tagReplacer = new TagReplacer();

		tagReplacer.addTagSource(FLOWINSTANCE_TAG_SOURCE_FACTORY.getTagSource((FlowInstance) flowInstance));
		tagReplacer.addTagSource(FLOW_TAG_SOURCE_FACTORY.getTagSource((Flow) flowInstance.getFlow()));
		tagReplacer.addTagSource(STATUS_TAG_SOURCE_FACTORY.getTagSource((Status) flowInstance.getStatus()));

		if (contact != null) {

			tagReplacer.addTagSource(CONTACT_TAG_SOURCE_FACTORY.getTagSource(contact));
		}

		tagReplacer.addTagSource(SIGNING_PARTY_TAG_SOURCE_FACTORY.getTagSource(signingParty));
		tagReplacer.addTagSource(new SingleTagSource("$flowInstanceSign.url", multiSigningHandler.getSigningURL(flowInstance, signingParty)));

		SimpleEmail email = new SimpleEmail(systemInterface.getEncoding());

		try {
			email.addRecipient(signingParty.getEmail());
			email.setMessageContentType(SimpleEmail.HTML);
			email.setSenderName(emailSenderName);
			email.setSenderAddress(emailSenderAddress);
			email.setSubject(AttributeTagUtils.replaceTags(tagReplacer.replace(subject), flowInstance.getAttributeHandler()));
			email.setMessage(EmailUtils.addMessageBody(AttributeTagUtils.replaceTags(tagReplacer.replace(message), flowInstance.getAttributeHandler())));

			systemInterface.getEmailHandler().send(email);

		} catch (Exception e) {

			log.info("Error generating/sending email " + email, e);
		}
	}

	private void sendSigningPartySMS(ImmutableFlowInstance flowInstance, SigningParty signingParty, Contact contact, String message) {

		if (signingParty.getMobilePhone() == null || smsSender == null || message == null || multiSigningHandler == null) {

			return;
		}

		TagReplacer tagReplacer = new TagReplacer();

		tagReplacer.addTagSource(FLOWINSTANCE_TAG_SOURCE_FACTORY.getTagSource((FlowInstance) flowInstance));
		tagReplacer.addTagSource(FLOW_TAG_SOURCE_FACTORY.getTagSource((Flow) flowInstance.getFlow()));
		tagReplacer.addTagSource(STATUS_TAG_SOURCE_FACTORY.getTagSource((Status) flowInstance.getStatus()));

		if (contact != null) {

			tagReplacer.addTagSource(CONTACT_TAG_SOURCE_FACTORY.getTagSource(contact));
		}

		tagReplacer.addTagSource(SIGNING_PARTY_TAG_SOURCE_FACTORY.getTagSource(signingParty));
		tagReplacer.addTagSource(new SingleTagSource("$flowInstanceSign.url", multiSigningHandler.getSigningURL(flowInstance, signingParty)));

		SimpleSMS sms = new SimpleSMS();

		try {
			sms.setSenderName(smsSenderName);
			sms.setMessage(AttributeTagUtils.replaceTags(tagReplacer.replace(message), flowInstance.getAttributeHandler()));
			sms.addRecipient(signingParty.getMobilePhone());

			smsSender.send(sms);

		} catch (Exception e) {

			log.info("Error generating/sending sms " + sms, e);
		}
	}

	public boolean sendContactEmail(ImmutableFlowInstance flowInstance, Contact contact, String subject, String message, File pdfFile) {

		if (!contact.isContactByEmail() || contact.getEmail() == null || subject == null || message == null) {

			return false;
		}

		TagReplacer tagReplacer = new TagReplacer();

		tagReplacer.addTagSource(FLOWINSTANCE_TAG_SOURCE_FACTORY.getTagSource((FlowInstance) flowInstance));
		tagReplacer.addTagSource(FLOW_TAG_SOURCE_FACTORY.getTagSource((Flow) flowInstance.getFlow()));
		tagReplacer.addTagSource(STATUS_TAG_SOURCE_FACTORY.getTagSource((Status) flowInstance.getStatus()));
		tagReplacer.addTagSource(CONTACT_TAG_SOURCE_FACTORY.getTagSource(contact));
		tagReplacer.addTagSource(new SingleTagSource("$flowInstance.url", userFlowInstanceModuleAlias + "/overview/" + flowInstance.getFlow().getFlowID() + "/" + flowInstance.getFlowInstanceID()));

		SimpleEmail email = new SimpleEmail(systemInterface.getEncoding());

		try {
			email.addRecipient(contact.getEmail());
			email.setMessageContentType(SimpleEmail.HTML);
			email.setSenderName(emailSenderName);
			email.setSenderAddress(emailSenderAddress);
			email.setSubject(AttributeTagUtils.replaceTags(tagReplacer.replace(subject), flowInstance.getAttributeHandler()));
			email.setMessage(EmailUtils.addMessageBody(AttributeTagUtils.replaceTags(tagReplacer.replace(message), flowInstance.getAttributeHandler())));

			if (pdfFile != null && pdfFilename != null) {

				String generatedFilename = tagReplacer.replace(pdfFilename) + ".pdf";

				email.add(new FileAttachment(pdfFile, FileUtils.toValidHttpFilename(generatedFilename)));
			}

			systemInterface.getEmailHandler().send(email);

		} catch (Exception e) {

			log.info("Error generating/sending email " + email, e);
		}
		
		return true;
	}

	public boolean sendContactSMS(ImmutableFlowInstance flowInstance, Contact contact, String message) {

		if (!contact.isContactBySMS() || contact.getMobilePhone() == null || smsSender == null || message == null) {

			return false;
		}

		TagReplacer tagReplacer = new TagReplacer();

		tagReplacer.addTagSource(FLOWINSTANCE_TAG_SOURCE_FACTORY.getTagSource((FlowInstance) flowInstance));
		tagReplacer.addTagSource(FLOW_TAG_SOURCE_FACTORY.getTagSource((Flow) flowInstance.getFlow()));
		tagReplacer.addTagSource(STATUS_TAG_SOURCE_FACTORY.getTagSource((Status) flowInstance.getStatus()));
		tagReplacer.addTagSource(CONTACT_TAG_SOURCE_FACTORY.getTagSource(contact));
		tagReplacer.addTagSource(new SingleTagSource("$flowInstance.url", userFlowInstanceModuleAlias + "/overview/" + flowInstance.getFlow().getFlowID() + "/" + flowInstance.getFlowInstanceID()));

		SimpleSMS sms = new SimpleSMS();

		try {
			sms.setSenderName(smsSenderName);
			sms.setMessage(AttributeTagUtils.replaceTags(tagReplacer.replace(message), flowInstance.getAttributeHandler()));
			sms.addRecipient(contact.getMobilePhone());

			smsSender.send(sms);

		} catch (Exception e) {

			log.info("Error generating/sending sms " + sms, e);
		}
		
		return true;
	}

	public void sendManagerEmails(ImmutableFlowInstance flowInstance, Contact contact, String subject, String message, List<User> excludedManagers, boolean useFlowFamilyManagers) throws SQLException {

		if (subject == null || message == null) {

			return;
		}

		List<User> managers;

		if (useFlowFamilyManagers) {

			managers = getFlowFamilyManagers(flowInstance.getFlow().getFlowFamily().getFlowFamilyID());

		} else {

			managers = getCurrentManagers(flowInstance.getFlowInstanceID());
		}

		if (managers == null || (excludedManagers != null && excludedManagers.containsAll(managers))) {

			return;
		}

		if (excludedManagers != null) {

			managers.removeAll(excludedManagers);
		}

		List<TagSource> sharedTagSources = new ArrayList<TagSource>(4);

		sharedTagSources.add(FLOWINSTANCE_TAG_SOURCE_FACTORY.getTagSource((FlowInstance) flowInstance));
		sharedTagSources.add(FLOW_TAG_SOURCE_FACTORY.getTagSource((Flow) flowInstance.getFlow()));
		sharedTagSources.add(STATUS_TAG_SOURCE_FACTORY.getTagSource((Status) flowInstance.getStatus()));
		sharedTagSources.add(new SingleTagSource("$flowInstance.url", flowInstanceAdminModuleAlias + "/overview/" + flowInstance.getFlowInstanceID()));

		if (contact != null) {

			sharedTagSources.add(CONTACT_TAG_SOURCE_FACTORY.getTagSource(contact));
		}

		for (User manager : managers) {

			TagReplacer tagReplacer = new TagReplacer();

			tagReplacer.addTagSource(MANAGER_TAG_SOURCE_FACTORY.getTagSource(manager));
			tagReplacer.addTagSources(sharedTagSources);

			SimpleEmail email = new SimpleEmail(systemInterface.getEncoding());

			try {
				email.addRecipient(manager.getEmail());
				email.setMessageContentType(SimpleEmail.HTML);
				email.setSenderName(emailSenderName);
				email.setSenderAddress(emailSenderAddress);
				email.setSubject(AttributeTagUtils.replaceTags(tagReplacer.replace(subject), flowInstance.getAttributeHandler()));
				email.setMessage(EmailUtils.addMessageBody(AttributeTagUtils.replaceTags(tagReplacer.replace(message), flowInstance.getAttributeHandler())));

				systemInterface.getEmailHandler().send(email);

			} catch (Exception e) {

				log.info("Error generating/sending email " + email, e);
			}
		}
	}

	public void sendGlobalEmail(SiteProfile siteProfile, ImmutableFlowInstance flowInstance, Contact contact, String address, String subject, String message, File pdfFile, boolean sendPDFAttachmentsSeparately) {

		if (subject == null || message == null) {

			return;
		}

		TagReplacer tagReplacer = new TagReplacer();

		tagReplacer.addTagSource(FLOWINSTANCE_TAG_SOURCE_FACTORY.getTagSource((FlowInstance) flowInstance));
		tagReplacer.addTagSource(FLOW_TAG_SOURCE_FACTORY.getTagSource((Flow) flowInstance.getFlow()));
		tagReplacer.addTagSource(STATUS_TAG_SOURCE_FACTORY.getTagSource((Status) flowInstance.getStatus()));
		tagReplacer.addTagSource(new SingleTagSource("$flowInstance.url", flowInstanceAdminModuleAlias + "/overview/" + flowInstance.getFlowInstanceID()));

		if (contact != null) {

			tagReplacer.addTagSource(CONTACT_TAG_SOURCE_FACTORY.getTagSource(contact));
		}

		SimpleEmail email = new SimpleEmail(systemInterface.getEncoding());

		try {
			email.addRecipient(address);
			email.setMessageContentType(SimpleEmail.HTML);
			email.setSenderName(emailSenderName);
			email.setSenderAddress(emailSenderAddress);
			email.setSubject(AttributeTagUtils.replaceTags(tagReplacer.replace(subject), flowInstance.getAttributeHandler()));
			email.setMessage(EmailUtils.addMessageBody(AttributeTagUtils.replaceTags(tagReplacer.replace(message), flowInstance.getAttributeHandler())));

			String generatedFilename = null;

			if (pdfFile != null && pdfFilename != null) {

				generatedFilename = FileUtils.toValidHttpFilename(tagReplacer.replace(pdfFilename) + ".pdf");

				if (!sendPDFAttachmentsSeparately) {

					email.add(new FileAttachment(pdfFile, generatedFilename));

				} else {

					email.add(new ByteArrayAttachment(pdfProvider.removePDFAttachments(pdfFile), MimeUtils.getMimeType(generatedFilename), generatedFilename));

					List<PDFByteAttachment> attachments = pdfProvider.getPDFAttachments(pdfFile);

					if (!CollectionUtils.isEmpty(attachments)) {

						for (PDFByteAttachment attachment : attachments) {

							email.add(new ByteArrayAttachment(attachment.getData(), MimeUtils.getMimeType(attachment.getFilename()), attachment.getAttachmentName() + " - " + attachment.getFilename()));
						}
					}
				}
			}

			if (!CollectionUtils.isEmpty(notificationEmailFilters)) {

				for (NotificationEmailFilter filter : notificationEmailFilters) {

					try {
						filter.filterGlobalEmail(siteProfile, flowInstance, contact, pdfFile, generatedFilename, sendPDFAttachmentsSeparately, email);

					} catch (Throwable t) {
						log.error("Error running filterGlobalEmail on filter " + filter, t);
					}
				}
			}

			systemInterface.getEmailHandler().send(email);

		} catch (Exception e) {

			log.info("Error generating/sending email " + email, e);
		}

	}

	private List<User> getFlowFamilyManagers(Integer flowFamilyID) throws SQLException {

		FlowFamily flowFamily = flowFamilyDAO.get(flowFamilyID);

		if (flowFamily != null) {

			List<User> managers = null;

			if (flowFamily.getManagerUserIDs() != null) {

				managers = systemInterface.getUserHandler().getUsers(flowFamily.getAllowedUserIDs(), false, true);
			}

			if (flowFamily.getManagerGroupIDs() != null) {

				List<User> groupUsers = systemInterface.getUserHandler().getUsersByGroups(flowFamily.getManagerGroupIDs(), true);

				if (managers != null) {

					CollectionUtils.addNewEntries(managers, groupUsers);

					return managers;
				}

				return groupUsers;
			}

			return managers;
		}

		return null;
	}

	private List<User> getCurrentManagers(Integer flowInstanceID) throws SQLException {

		FlowInstance flowInstance = getFlowInstance(flowInstanceID);

		if (flowInstance != null) {

			return flowInstance.getManagers();
		}

		return null;
	}

	public Contact getPosterContact(FlowInstance flowInstance, SiteProfile siteProfile) {

		try {
			flowInstance = getFlowInstanceWithAttributesAndOwners(flowInstance.getFlowInstanceID());

			if (flowInstance == null) {

				return null;
			}

			return getPosterContact(flowInstance);

		} catch (Exception e) {

			log.error("Error creating immutable flow instance manager for flow instance " + flowInstance, e);
		}

		return null;
	}
	
	
	public Collection<Contact> getContacts(FlowInstance flowInstance, SiteProfile siteProfile) {

		try {
			flowInstance = getFlowInstanceWithAttributesAndOwners(flowInstance.getFlowInstanceID());

			if (flowInstance == null) {

				return null;
			}

			return getContacts(flowInstance);

		} catch (Exception e) {

			log.error("Error creating immutable flow instance manager for flow instance " + flowInstance, e);
		}

		return null;
	}

	public ImmutableFlowInstanceManager getImmutableFlowInstanceManager(FlowInstance flowInstance, SiteProfile siteProfile) throws DuplicateFlowInstanceManagerIDException, MissingQueryInstanceDescriptor, QueryProviderNotFoundException, InvalidFlowInstanceStepException, QueryProviderErrorException, QueryInstanceNotFoundInQueryProviderException{
		
		return new ImmutableFlowInstanceManager(flowInstance, queryHandler, null, new DefaultInstanceMetadata(siteProfile), null);
	}
	
	public FlowInstance getFlowInstance(Integer flowInstanceID) throws SQLException {

		HighLevelQuery<FlowInstance> query = new HighLevelQuery<FlowInstance>(FLOW_INSTANCE_RELATIONS);

		query.addParameter(flowInstanceIDParamFactory.getParameter(flowInstanceID));

		query.addRelationParameter(QueryInstanceDescriptor.class, queryInstanceDescriptorFlowInstanceIDParamFactory.getParameter(flowInstanceID));

		return flowInstanceDAO.get(query);
	}
	
	public FlowInstance getFlowInstanceWithAttributesAndOwners(Integer flowInstanceID) throws SQLException {

		HighLevelQuery<FlowInstance> query = new HighLevelQuery<FlowInstance>(FlowInstance.ATTRIBUTES_RELATION, FlowInstance.OWNERS_RELATION);

		query.addParameter(flowInstanceIDParamFactory.getParameter(flowInstanceID));

		return flowInstanceDAO.get(query);
	}
	
	//TODO change logic here to use editor or latest submit event
	public Contact getPosterContact(ImmutableFlowInstance flowInstance) {

		AttributeHandler attributeHandler = flowInstance.getAttributeHandler();

		if (attributeHandler.isSet("email")) {

			Contact contact = new Contact();
			
			contact.setFirstname(attributeHandler.getString("firstname"));
			contact.setLastname(attributeHandler.getString("lastname"));
			contact.setEmail(attributeHandler.getString("email"));
			contact.setPhone(attributeHandler.getString("phone"));
			contact.setMobilePhone(attributeHandler.getString("mobilePhone"));
			contact.setAddress(attributeHandler.getString("address"));
			contact.setZipCode(attributeHandler.getString("zipCode"));
			contact.setPostalAddress(attributeHandler.getString("postalAddress"));
			contact.setCitizenIdentifier(attributeHandler.getString("citizenIdentifier"));
			contact.setOrganizationNumber(attributeHandler.getString("organizationNumber"));
			contact.setContactBySMS(attributeHandler.getPrimitiveBoolean("contactBySMS"));
			contact.setContactByEmail(true);
			
			return contact;
		}

		User poster = flowInstance.getPoster();

		if (poster != null) {

			Contact contact = new Contact();
			
			contact.setFirstname(poster.getFirstname());
			contact.setLastname(poster.getLastname());
			contact.setEmail(poster.getEmail());
			contact.setMobilePhone(UserUtils.getAttribute("mobilePhone", poster));
			
			contact.setContactBySMS(UserUtils.getBooleanAttribute("contactBySMS", poster));
			contact.setContactByEmail(true);
			
			return contact;
		}

		return null;
	}
	
	public List<Contact> getContacts(ImmutableFlowInstance flowInstance) {
		
		List<Contact> contacts = new ArrayList<Contact>(1);
		
		AttributeHandler attributeHandler = flowInstance.getAttributeHandler();
		
		if (attributeHandler.isSet("email")) {
			
			Contact contact = new Contact();
			
			contact.setFirstname(attributeHandler.getString("firstname"));
			contact.setLastname(attributeHandler.getString("lastname"));
			contact.setEmail(attributeHandler.getString("email"));
			contact.setPhone(attributeHandler.getString("phone"));
			contact.setMobilePhone(attributeHandler.getString("mobilePhone"));
			contact.setAddress(attributeHandler.getString("address"));
			contact.setZipCode(attributeHandler.getString("zipCode"));
			contact.setPostalAddress(attributeHandler.getString("postalAddress"));
			contact.setCitizenIdentifier(attributeHandler.getString("citizenIdentifier"));
			contact.setOrganizationNumber(attributeHandler.getString("organizationNumber"));
			contact.setContactBySMS(attributeHandler.getPrimitiveBoolean("contactBySMS"));
			contact.setContactByEmail(true);
			
			contacts.add(contact);
		}
		
		if (!CollectionUtils.isEmpty(flowInstance.getOwners())) {
			
			for (User owner : flowInstance.getOwners()) {
				
				Contact contact = new Contact();
				
				contact.setFirstname(owner.getFirstname());
				contact.setLastname(owner.getLastname());
				contact.setEmail(owner.getEmail());
				contact.setMobilePhone(UserUtils.getAttribute("mobilePhone", owner));
				
				contact.setContactBySMS(UserUtils.getBooleanAttribute("contactBySMS", owner));
				contact.setContactByEmail(true);
				
				addUniqueContact(contacts, contact);
			}
		}
		
		if (contacts.isEmpty()) {
			return null;
		}
		
		return contacts;
	}
	
	private void addUniqueContact(List<Contact> contacts, Contact contact) {
		
		for (Contact existingContact : contacts) {
			
			if (existingContact.getCitizenIdentifier() != null && contact.getCitizenIdentifier() != null) {
				
				if (existingContact.getCitizenIdentifier().equals(contact.getCitizenIdentifier())) {
					
					return;
				}
				
				continue;
			}
			
			if (existingContact.getEmail() != null && contact.getEmail() != null) {
				
				if (existingContact.getEmail().equals(contact.getEmail())) {
					
					return;
				}
				
				continue;
			}
			
			if (existingContact.getMobilePhone() != null && contact.getMobilePhone() != null) {
				
				if (existingContact.getMobilePhone().equals(contact.getMobilePhone())) {
					
					return;
				}
				
				continue;
			}
		}
		
		contacts.add(contact);
	}

	private String getFlowInstaceSubmittedUserEmailMessage(FlowFamililyNotificationSettings notificationSettings, ImmutableFlowInstance flowInstance) {

		if (!CollectionUtils.isEmpty(flowInstance.getOwners())) {

			return notificationSettings.getFlowInstanceSubmittedUserEmailMessage();
			
		} else {

			return notificationSettings.getFlowInstanceSubmittedNotLoggedInUserEmailMessage();
		}

	}

	private String getFlowInstaceArchivedUserEmailMessage(FlowFamililyNotificationSettings notificationSettings, ImmutableFlowInstance flowInstance) {

		if (!CollectionUtils.isEmpty(flowInstance.getOwners())) {

			return notificationSettings.getFlowInstanceArchivedUserEmailMessage();
			
		} else {

			return notificationSettings.getFlowInstanceArchivedNotLoggedInUserEmailMessage();
		}

	}

	private String getFlowInstaceSubmittedUserSMSMessage(ImmutableFlowInstance flowInstance) {

		if (!CollectionUtils.isEmpty(flowInstance.getOwners())) {

			return flowInstanceSubmittedUserSMS;
			
		} else {

			return flowInstanceSubmittedNotLoggedInUserSMS;
		}
	}

	private String getFlowInstaceArchivedUserSMSMessage(ImmutableFlowInstance flowInstance) {

		if (!CollectionUtils.isEmpty(flowInstance.getOwners())) {

			return flowInstanceArchivedUserSMS;
			
		} else {

			return flowInstanceArchivedNotLoggedInUserSMS;
		}
	}

	public String getEmailSenderName() {

		return emailSenderName;
	}

	public String getEmailSenderAddress() {

		return emailSenderAddress;
	}

	public String getSmsSenderName() {

		return smsSenderName;
	}

	public String getStatusChangedUserEmailSubject() {

		return statusChangedUserEmailSubject;
	}

	public String getUserFlowInstanceModuleAlias() {

		return userFlowInstanceModuleAlias;
	}

	public boolean addNotificationEmailFilter(NotificationEmailFilter hook) {

		return notificationEmailFilters.add(hook);
	}

	public boolean removeNotificationEmailFilter(NotificationEmailFilter hook) {

		return notificationEmailFilters.remove(hook);
	}

	
	public String getFlowInstanceAdminModuleAlias() {
	
		return flowInstanceAdminModuleAlias;
	}

	public static Set<String> getContactTags(){
		
		Set<String> tags = new LinkedHashSet<String>();
		
		tags.addAll(StandardFlowNotificationHandler.FLOW_TAG_SOURCE_FACTORY.getTagsSet());
		tags.addAll(StandardFlowNotificationHandler.FLOWINSTANCE_TAG_SOURCE_FACTORY.getTagsSet());
		tags.addAll(StandardFlowNotificationHandler.STATUS_TAG_SOURCE_FACTORY.getTagsSet());
		tags.addAll(StandardFlowNotificationHandler.CONTACT_TAG_SOURCE_FACTORY.getTagsSet());
		tags.add("$flowInstance.url");
		
		return tags;
	}
	
	public static Set<String> getManagerTags(){
		
		Set<String> tags = new LinkedHashSet<String>();
		
		tags.addAll(StandardFlowNotificationHandler.FLOW_TAG_SOURCE_FACTORY.getTagsSet());
		tags.addAll(StandardFlowNotificationHandler.FLOWINSTANCE_TAG_SOURCE_FACTORY.getTagsSet());
		tags.addAll(StandardFlowNotificationHandler.STATUS_TAG_SOURCE_FACTORY.getTagsSet());
		tags.addAll(StandardFlowNotificationHandler.CONTACT_TAG_SOURCE_FACTORY.getTagsSet());
		tags.addAll(StandardFlowNotificationHandler.MANAGER_TAG_SOURCE_FACTORY.getTagsSet());
		tags.add("$flowInstance.url");
				
		return tags;
	}
	
	public static Set<String> getGlobalTags(){
		
		Set<String> tags = new LinkedHashSet<String>();
		
		tags.addAll(StandardFlowNotificationHandler.FLOW_TAG_SOURCE_FACTORY.getTagsSet());
		tags.addAll(StandardFlowNotificationHandler.FLOWINSTANCE_TAG_SOURCE_FACTORY.getTagsSet());
		tags.addAll(StandardFlowNotificationHandler.STATUS_TAG_SOURCE_FACTORY.getTagsSet());
		tags.addAll(StandardFlowNotificationHandler.CONTACT_TAG_SOURCE_FACTORY.getTagsSet());
		tags.add("$flowInstance.url");
						
		return tags;
	}
	
	public static Set<String> getSigningPartyTags(){
		
		Set<String> tags = new LinkedHashSet<String>();
		
		tags.addAll(StandardFlowNotificationHandler.FLOW_TAG_SOURCE_FACTORY.getTagsSet());
		tags.addAll(StandardFlowNotificationHandler.FLOWINSTANCE_TAG_SOURCE_FACTORY.getTagsSet());
		tags.addAll(StandardFlowNotificationHandler.STATUS_TAG_SOURCE_FACTORY.getTagsSet());
		tags.addAll(StandardFlowNotificationHandler.CONTACT_TAG_SOURCE_FACTORY.getTagsSet());
		tags.addAll(StandardFlowNotificationHandler.SIGNING_PARTY_TAG_SOURCE_FACTORY.getTagsSet());
		tags.add("$flowInstance.url");
						
		return tags;
	}
	
	public String getFlowInstanceSubmittedManagerEmailSubject() {
		
		return flowInstanceSubmittedManagerEmailSubject;
	}

	
	public void setFlowInstanceSubmittedManagerEmailSubject(String flowInstanceSubmittedManagerEmailSubject) {
	
		this.flowInstanceSubmittedManagerEmailSubject = flowInstanceSubmittedManagerEmailSubject;
	}
	
	public String getFlowInstanceSubmittedManagerEmailMessage() {
		
		return flowInstanceSubmittedManagerEmailMessage;
	}

	
	public void setFlowInstanceSubmittedManagerEmailMessage(String flowInstanceSubmittedManagerEmailMessage) {
	
		this.flowInstanceSubmittedManagerEmailMessage = flowInstanceSubmittedManagerEmailMessage;
	}
}
