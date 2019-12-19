package com.nordicpeak.flowengine.notifications;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
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
import se.unlogic.hierarchy.core.beans.Group;
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
import se.unlogic.hierarchy.core.interfaces.ForegroundModuleResponse;
import se.unlogic.hierarchy.core.interfaces.SectionInterface;
import se.unlogic.hierarchy.core.interfaces.ViewFragment;
import se.unlogic.hierarchy.core.interfaces.modules.descriptors.ForegroundModuleDescriptor;
import se.unlogic.hierarchy.core.interfaces.settings.MutableSettingHandler;
import se.unlogic.hierarchy.core.interfaces.sms.SMSSender;
import se.unlogic.hierarchy.core.utils.AccessUtils;
import se.unlogic.hierarchy.core.utils.AttributeTagUtils;
import se.unlogic.hierarchy.core.utils.ModuleViewFragmentTransformer;
import se.unlogic.hierarchy.core.utils.ViewFragmentModule;
import se.unlogic.hierarchy.foregroundmodules.AnnotatedForegroundModule;
import se.unlogic.openhierarchy.foregroundmodules.siteprofile.interfaces.SiteProfile;
import se.unlogic.standardutils.arrays.ArrayUtils;
import se.unlogic.standardutils.collections.CollectionUtils;
import se.unlogic.standardutils.dao.AnnotatedDAO;
import se.unlogic.standardutils.dao.AnnotatedDAOWrapper;
import se.unlogic.standardutils.dao.HighLevelQuery;
import se.unlogic.standardutils.dao.MySQLRowLimiter;
import se.unlogic.standardutils.dao.OrderByCriteria;
import se.unlogic.standardutils.dao.QueryParameterFactory;
import se.unlogic.standardutils.dao.SimpleAnnotatedDAOFactory;
import se.unlogic.standardutils.db.tableversionhandler.TableVersionHandler;
import se.unlogic.standardutils.db.tableversionhandler.UpgradeResult;
import se.unlogic.standardutils.db.tableversionhandler.XMLDBScriptProvider;
import se.unlogic.standardutils.enums.Order;
import se.unlogic.standardutils.io.BinarySizeFormater;
import se.unlogic.standardutils.io.BinarySizes;
import se.unlogic.standardutils.io.FileUtils;
import se.unlogic.standardutils.mime.MimeUtils;
import se.unlogic.standardutils.numbers.NumberUtils;
import se.unlogic.standardutils.string.AnnotatedBeanTagSourceFactory;
import se.unlogic.standardutils.string.SingleTagSource;
import se.unlogic.standardutils.string.StringUtils;
import se.unlogic.standardutils.string.TagReplacer;
import se.unlogic.standardutils.string.TagSource;
import se.unlogic.standardutils.templates.TemplateUtils;
import se.unlogic.standardutils.validation.ValidationException;
import se.unlogic.standardutils.xml.XMLUtils;
import se.unlogic.webutils.http.RequestUtils;
import se.unlogic.webutils.http.URIParser;
import se.unlogic.webutils.populators.annotated.AnnotatedRequestPopulator;

import com.nordicpeak.flowengine.Constants;
import com.nordicpeak.flowengine.FlowBrowserModule;
import com.nordicpeak.flowengine.UserFlowInstanceModule;
import com.nordicpeak.flowengine.beans.Contact;
import com.nordicpeak.flowengine.beans.DefaultInstanceMetadata;
import com.nordicpeak.flowengine.beans.DefaultStatusMapping;
import com.nordicpeak.flowengine.beans.Flow;
import com.nordicpeak.flowengine.beans.FlowFamily;
import com.nordicpeak.flowengine.beans.FlowFamilyManagerGroup;
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
import com.nordicpeak.flowengine.events.InternalMessageAddedEvent;
import com.nordicpeak.flowengine.events.ManagerExpiredEvent;
import com.nordicpeak.flowengine.events.ManagerMentionedEvent;
import com.nordicpeak.flowengine.events.ManagersChangedEvent;
import com.nordicpeak.flowengine.events.MultiSigningCanceledEvent;
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
import com.nordicpeak.flowengine.interfaces.ImmutableFlowFamily;
import com.nordicpeak.flowengine.interfaces.ImmutableFlowInstance;
import com.nordicpeak.flowengine.interfaces.MultiSigningHandler;
import com.nordicpeak.flowengine.interfaces.PDFProvider;
import com.nordicpeak.flowengine.interfaces.QueryHandler;
import com.nordicpeak.flowengine.interfaces.XMLProvider;
import com.nordicpeak.flowengine.managers.FlowInstanceManager;
import com.nordicpeak.flowengine.managers.ImmutableFlowInstanceManager;
import com.nordicpeak.flowengine.notifications.enums.NotificationRecipient;
import com.nordicpeak.flowengine.utils.FlowFamilyUtils;
import com.nordicpeak.flowengine.utils.FlowInstanceUtils;
import com.nordicpeak.flowengine.utils.MultiSignUtils;
import com.nordicpeak.flowengine.utils.PDFByteAttachment;

public class StandardFlowNotificationHandler extends AnnotatedForegroundModule implements FlowNotificationHandler, ViewFragmentModule<ForegroundModuleDescriptor> {

	public static final Field[] FLOW_INSTANCE_RELATIONS = { FlowInstance.EVENTS_RELATION, FlowInstanceEvent.ATTRIBUTES_RELATION, FlowInstance.ATTRIBUTES_RELATION, FlowInstance.MANAGERS_RELATION, FlowInstance.MANAGER_GROUPS_RELATION, FlowInstance.FLOW_RELATION, FlowInstance.STATUS_RELATION, Flow.FLOW_TYPE_RELATION, Flow.FLOW_FAMILY_RELATION, FlowType.ALLOWED_ADMIN_GROUPS_RELATION, FlowType.ALLOWED_ADMIN_USERS_RELATION, Flow.STEPS_RELATION, Flow.FLOW_FAMILY_RELATION, FlowFamily.MANAGER_GROUPS_RELATION, FlowFamily.MANAGER_USERS_RELATION, Step.QUERY_DESCRIPTORS_RELATION, QueryDescriptor.EVALUATOR_DESCRIPTORS_RELATION, Flow.DEFAULT_FLOW_STATE_MAPPINGS_RELATION, DefaultStatusMapping.FLOW_STATE_RELATION, QueryDescriptor.QUERY_INSTANCE_DESCRIPTORS_RELATION, FlowInstance.OWNERS_RELATION };

	private static final AnnotatedRequestPopulator<FlowFamililyNotificationSettings> POPULATOR = new AnnotatedRequestPopulator<FlowFamililyNotificationSettings>(FlowFamililyNotificationSettings.class);

	public static final AnnotatedBeanTagSourceFactory<Flow> FLOW_TAG_SOURCE_FACTORY = new AnnotatedBeanTagSourceFactory<Flow>(Flow.class, "$flow.");
	public static final AnnotatedBeanTagSourceFactory<FlowInstance> FLOWINSTANCE_TAG_SOURCE_FACTORY = new AnnotatedBeanTagSourceFactory<FlowInstance>(FlowInstance.class, "$flowInstance.");
	public static final AnnotatedBeanTagSourceFactory<Status> STATUS_TAG_SOURCE_FACTORY = new AnnotatedBeanTagSourceFactory<Status>(Status.class, "$status.");
	public static final AnnotatedBeanTagSourceFactory<Contact> CONTACT_TAG_SOURCE_FACTORY = new AnnotatedBeanTagSourceFactory<Contact>(Contact.class, "$contact.");
	public static final AnnotatedBeanTagSourceFactory<User> MANAGER_TAG_SOURCE_FACTORY = new AnnotatedBeanTagSourceFactory<User>(User.class, "$manager.");
	public static final AnnotatedBeanTagSourceFactory<SigningParty> SIGNING_PARTY_TAG_SOURCE_FACTORY = new AnnotatedBeanTagSourceFactory<SigningParty>(SigningParty.class, "$signingParty.");
	public static final AnnotatedBeanTagSourceFactory<User> POSTER_TAG_SOURCE_FACTORY = new AnnotatedBeanTagSourceFactory<User>(User.class, "$poster.");

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

	@XSLVariable(name = "java.FlowInstance")
	private String flowInstanceTitle;

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
	@TextAreaSettingDescriptor(name = "Flow instance multi sign cancel SMS message (users)", description = "The SMS sent to the users when a flow instance they had to sign is canceled", required = true)
	@XSLVariable(prefix = "java.")
	private String flowInstanceMultiSignCanceledUserSMS;

	@ModuleSetting
	@TextAreaSettingDescriptor(name = "Flow instance multi sign cancel SMS message (owners)", description = "The SMS sent to the users when a flow instance they wanted to be signed is canceled", required = true)
	@XSLVariable(prefix = "java.")
	private String flowInstanceMultiSignCanceledOwnerSMS;

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
	@TextFieldSettingDescriptor(name = "Flow instance multi sign cancel email subject (users)", description = "The subject of emails sent to the users when a flow instance they had to sign is canceled", required = true)
	@XSLVariable(prefix = "java.")
	private String flowInstanceMultiSignCanceledUserEmailSubject;

	@ModuleSetting
	@HTMLEditorSettingDescriptor(name = "Flow instance multi sign cancel email message (users)", description = "The message of emails sent to the users when a flow instance they had to sign is canceled", required = true)
	@XSLVariable(prefix = "java.")
	private String flowInstanceMultiSignCanceledUserEmailMessage;

	@ModuleSetting
	@TextFieldSettingDescriptor(name = "Flow instance multi sign cancel email subject (owners)", description = "The subject of emails sent to the users when a flow instance they wanted to be signed is canceled", required = true)
	@XSLVariable(prefix = "java.")
	private String flowInstanceMultiSignCanceledOwnerEmailSubject;

	@ModuleSetting
	@HTMLEditorSettingDescriptor(name = "Flow instance multi sign cancel email message (owners)", description = "The message of emails sent to the users when a flow instance they wanted to be signed is canceled", required = true)
	@XSLVariable(prefix = "java.")
	private String flowInstanceMultiSignCanceledOwnerEmailMessage;

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
	@CheckboxSettingDescriptor(name = "Send email to managers when new internal notes are added", description = "Controls if email messages are sent to the managers when new internal notes are added.")
	private boolean sendInternalMessageAddedManagerEmail;
	
	@ModuleSetting
	@TextFieldSettingDescriptor(name = "New internal note added email subject (managers)", description = "The subject of emails sent to the managers when new internal notes are added", required = true)
	@XSLVariable(prefix = "java.")
	private String internalMessageAddedManagerEmailSubject;
	
	@ModuleSetting
	@HTMLEditorSettingDescriptor(name = "New internal note added email message (managers)", description = "The message of emails sent to the managers when new internal notes are added", required = true)
	@XSLVariable(prefix = "java.")
	private String internalMessageAddedManagerEmailMessage;

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
	@CheckboxSettingDescriptor(name = "Send email to mentioned managers", description = "Controls if email messages should be sent to mentioned users.")
	private boolean sendManagerMentionedEmail;

	@ModuleSetting
	@TextFieldSettingDescriptor(name = "Manager mentioned email subject", description = "The subject of emails sent to manager mentioned in internal message", required = true)
	@XSLVariable(prefix = "java.")
	private String managerMentionedEmailSubject;

	@ModuleSetting
	@HTMLEditorSettingDescriptor(name = "Manager mentioned email message", description = "The message of emails sent to manager mentioned in internal message", required = true)
	@XSLVariable(prefix = "java.")
	private String managerMentionedEmailMessage;
	
	@ModuleSetting
	@CheckboxSettingDescriptor(name = "Send email to groups when they are assigned new flow instances", description = "Controls if email messages are sent to groups when they are assigned new flow instances.")
	private boolean sendFlowInstanceAssignedGroupEmail;
	
	@ModuleSetting
	@TextFieldSettingDescriptor(name = "Assigned new flow instance email subject (groups)", description = "The subject of emails sent to the groups when they are assigned new flow instances", required = true)
	@XSLVariable(prefix = "java.")
	private String flowInstanceAssignedGroupEmailSubject;
	
	@ModuleSetting
	@HTMLEditorSettingDescriptor(name = "Assigned new flow instance email message (groups)", description = "The message of emails sent to the users when they are assigned new flow instances", required = true)
	@XSLVariable(prefix = "java.")
	private String flowInstanceAssignedGroupEmailMessage;

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

	@ModuleSetting(allowsNull = true)
	@TextAreaSettingDescriptor(name = "Flow instance submitted email address (global)", description = "Global address to be notified when new flow instances are submitted", formatValidator = EmailPopulator.class)
	private List<String> flowInstanceSubmittedGlobalEmailAddress;

	//TODO unused
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
	@CheckboxSettingDescriptor(name = "Send email to the specified address when flow instances are assigned new managers", description = "Controls if email messages are the sent to specified address when flow instances are assigned new managers.")
	private boolean sendFlowInstanceAssignedGlobalEmail;
	
	@ModuleSetting
	@TextFieldSettingDescriptor(name = "Flow instance assigned email subject (global)", description = "The subject of emails sent when a new flow instance is assigned", required = true)
	@XSLVariable(prefix = "java.")
	private String flowInstanceAssignedGlobalEmailSubject;
	
	@ModuleSetting
	@HTMLEditorSettingDescriptor(name = "Flow instance assigned email message (global)", description = "The message of emails sent when a new flow instance is assigned", required = true)
	@XSLVariable(prefix = "java.")
	private String flowInstanceAssignedGlobalEmailMessage;
	
	@ModuleSetting(allowsNull = true)
	@TextAreaSettingDescriptor(name = "Flow instance assigned email address (global)", description = "Global address to be notified when new flow instances are assigned", formatValidator = EmailPopulator.class)
	private List<String> flowInstanceAssignedGlobalEmailAddress;

	@ModuleSetting
	@CheckboxSettingDescriptor(name = "Send email to the specified address when new flow instances are archived", description = "Controls if email messages are the sent to specified address when new flow instances are archived.")
	private boolean sendFlowInstanceArchivedGlobalEmail;

	@ModuleSetting
	@TextFieldSettingDescriptor(name = "Flow instance archived email subject (global)", description = "The subject of emails sent when a new flow instance is archived", required = true)
	@XSLVariable(prefix = "java.")
	private String flowInstanceArchivedGlobalEmailSubject;

	@ModuleSetting
	@HTMLEditorSettingDescriptor(name = "Flow instance archived email message (global)", description = "The message of emails sent when a new flow instance is archived", required = true)
	@XSLVariable(prefix = "java.")
	private String flowInstanceArchivedGlobalEmailMessage;

	@ModuleSetting(allowsNull = true)
	@TextAreaSettingDescriptor(name = "Flow instance archived email address (global)", description = "Global address to be notified when new flow instances are archived", formatValidator = EmailPopulator.class)
	private List<String> flowInstanceArchivedGlobalEmailAddresses;
	
	@ModuleSetting
	@TextFieldSettingDescriptor(name = "PDF filename (without file extension)", description = "Filename of the attached PDF (without file extension). Available tags: $flow.name, $flow.version, $flowInstance.flowInstanceID, $poster.*", required = true)
	protected String pdfFilename = "$flow.name, $flowInstance.flowInstanceID";

	@ModuleSetting
	@TextFieldSettingDescriptor(name = "XML filename (without file extension)", description = "Filename of the attached XML (without file extension). Available tags: $flow.name, $flow.version, $flowInstance.flowInstanceID, $poster.*", required = true)
	protected String xmlFilename = "$flow.name, $flowInstance.flowInstanceID";

	@ModuleSetting
	@CheckboxSettingDescriptor(name = "Send email to the specified address when new messages are received", description = "Controls if email messages are the sent to specified address when new messages are received.")
	private boolean sendExternalMessageReceivedGlobalEmail;

	@ModuleSetting
	@TextFieldSettingDescriptor(name = "New message received email subject (global)", description = "The subject of emails sent when a new messages are received", required = true)
	@XSLVariable(prefix = "java.")
	private String externalMessageReceivedGlobalEmailSubject;

	@ModuleSetting
	@HTMLEditorSettingDescriptor(name = "New message received email message (global)", description = "The message of emails sent when a new messages are received", required = true)
	@XSLVariable(prefix = "java.")
	private String externalMessageReceivedGlobalEmailMessage;

	@ModuleSetting(allowsNull = true)
	@TextAreaSettingDescriptor(name = "New message received email address (global)", description = "Global address to be notified when new messages are received", formatValidator = EmailPopulator.class)
	private List<String> externalMessageReceivedGlobalEmailAddresses;

	@ModuleSetting
	@TextFieldSettingDescriptor(name = "Manager expired email subject (global)", description = "The subject of emails sent when a manager expires", required = true)
	@XSLVariable(prefix = "java.")
	private String managerExpiredGlobalEmailSubject;

	@ModuleSetting
	@HTMLEditorSettingDescriptor(name = "Manager expired email message (global)", description = "The message of emails sent when a manager expires", required = true)
	@XSLVariable(prefix = "java.")
	private String managerExpiredGlobalEmailMessage;

	@ModuleSetting(allowsNull = true)
	@TextAreaSettingDescriptor(name = "Manager expired email address (global)", description = "Global address to be notified when new messages are received", formatValidator = EmailPopulator.class)
	private List<String> managerExpiredGlobalEmailAddresses;

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

	@InstanceManagerDependency
	protected XMLProvider xmlProvider;

	private AnnotatedDAOWrapper<FlowFamililyNotificationSettings, Integer> notificationSettingsDAO;
	private AnnotatedDAOWrapper<FlowFamily, Integer> flowFamilyDAO;

	private AnnotatedDAO<Flow> flowDAO;
	private AnnotatedDAO<FlowInstance> flowInstanceDAO;

	protected QueryParameterFactory<FlowInstance, Integer> flowInstanceIDParamFactory;
	protected QueryParameterFactory<Flow, FlowFamily> flowFlowFamilyIDParamFactory;
	protected QueryParameterFactory<QueryInstanceDescriptor, Integer> queryInstanceDescriptorFlowInstanceIDParamFactory;

	protected OrderByCriteria<Flow> flowOrderByLatestVersionCriteria;

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

		this.viewFragmentTransformer = new ModuleViewFragmentTransformer<ForegroundModuleDescriptor>(sectionInterface.getForegroundModuleXSLTCache(), this, systemInterface.getEncoding());

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
		UpgradeResult upgradeResult = TableVersionHandler.upgradeDBTables(dataSource, StandardFlowNotificationHandler.class.getName(), new XMLDBScriptProvider(StandardFlowNotificationHandler.class.getResourceAsStream("DB script.xml")));

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

		flowDAO = flowEngineDAOFactory.getFlowDAO();
		flowFlowFamilyIDParamFactory = flowDAO.getParamFactory("flowFamily", FlowFamily.class);
		flowOrderByLatestVersionCriteria = flowDAO.getOrderByCriteria("version", Order.DESC);
	}

	private String fixPosterTag(String message) {

		if (message != null) {

			return message.replace("$poster", "$contact");
		}

		return null;
	}

	private String fixLinkTags(String message) {

		if (message != null) {

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

		TemplateUtils.clearUnchangedTemplatedFields(notificationSettings, this);

		//TODO move every setting over to @Templated

		if (notificationSettings.getStatusChangedUserEmailSubject() != null && notificationSettings.getStatusChangedUserEmailSubject().trim().equals(statusChangedUserEmailSubject.trim())) {

			notificationSettings.setStatusChangedUserEmailSubject(null);
		}

		if (notificationSettings.getStatusChangedUserEmailMessage() != null && notificationSettings.getStatusChangedUserEmailMessage().trim().equals(statusChangedUserEmailMessage.trim())) {

			notificationSettings.setStatusChangedUserEmailMessage(null);
		}

		if (notificationSettings.getExternalMessageReceivedUserEmailSubject() != null && notificationSettings.getExternalMessageReceivedUserEmailSubject().trim().equals(externalMessageReceivedUserEmailSubject.trim())) {

			notificationSettings.setExternalMessageReceivedUserEmailSubject(null);
		}

		if (notificationSettings.getExternalMessageReceivedUserEmailMessage() != null && notificationSettings.getExternalMessageReceivedUserEmailMessage().trim().equals(externalMessageReceivedUserEmailMessage.trim())) {

			notificationSettings.setExternalMessageReceivedUserEmailMessage(null);
		}

		if (notificationSettings.getExternalMessageReceivedManagerSubject() != null && notificationSettings.getExternalMessageReceivedManagerSubject().trim().equals(externalMessageReceivedManagerEmailSubject.trim())) {

			notificationSettings.setExternalMessageReceivedManagerSubject(null);
		}

		if (notificationSettings.getExternalMessageReceivedManagerMessage() != null && notificationSettings.getExternalMessageReceivedManagerMessage().trim().equals(externalMessageReceivedManagerEmailMessage.trim())) {

			notificationSettings.setExternalMessageReceivedManagerMessage(null);
		}
		
		if (notificationSettings.getFlowInstanceArchivedUserEmailMessage() != null && notificationSettings.getFlowInstanceArchivedUserEmailMessage().trim().equals(flowInstanceArchivedUserEmailMessage.trim())) {

			notificationSettings.setFlowInstanceArchivedUserEmailMessage(null);
		}

		if (notificationSettings.getFlowInstanceArchivedUserEmailSubject() != null && notificationSettings.getFlowInstanceArchivedUserEmailSubject().trim().equals(flowInstanceArchivedUserEmailSubject.trim())) {

			notificationSettings.setFlowInstanceArchivedUserEmailSubject(null);
		}

		if (notificationSettings.getFlowInstanceArchivedNotLoggedInUserEmailMessage() != null && notificationSettings.getFlowInstanceArchivedNotLoggedInUserEmailMessage().trim().equals(flowInstanceArchivedNotLoggedInUserEmailMessage.trim())) {

			notificationSettings.setFlowInstanceArchivedNotLoggedInUserEmailMessage(null);
		}

		if (notificationSettings.getFlowInstanceSubmittedUserEmailMessage() != null && notificationSettings.getFlowInstanceSubmittedUserEmailMessage().trim().equals(flowInstanceSubmittedUserEmailMessage.trim())) {

			notificationSettings.setFlowInstanceSubmittedUserEmailMessage(null);
		}

		if (notificationSettings.getFlowInstanceSubmittedUserEmailSubject() != null && notificationSettings.getFlowInstanceSubmittedUserEmailSubject().trim().equals(flowInstanceSubmittedUserEmailSubject.trim())) {

			notificationSettings.setFlowInstanceSubmittedUserEmailSubject(null);
		}

		if (notificationSettings.getFlowInstanceSubmittedNotLoggedInUserEmailMessage() != null && notificationSettings.getFlowInstanceSubmittedNotLoggedInUserEmailMessage().trim().equals(flowInstanceSubmittedNotLoggedInUserEmailMessage.trim())) {

			notificationSettings.setFlowInstanceSubmittedNotLoggedInUserEmailMessage(null);
		}

		if (notificationSettings.getFlowInstanceSubmittedGlobalEmailSubject() != null && notificationSettings.getFlowInstanceSubmittedGlobalEmailSubject().trim().equals(flowInstanceSubmittedGlobalEmailSubject.trim())) {

			notificationSettings.setFlowInstanceSubmittedGlobalEmailSubject(null);
		}

		if (notificationSettings.getFlowInstanceSubmittedGlobalEmailMessage() != null && notificationSettings.getFlowInstanceSubmittedGlobalEmailMessage().trim().equals(flowInstanceSubmittedGlobalEmailMessage.trim())) {

			notificationSettings.setFlowInstanceSubmittedGlobalEmailMessage(null);
		}

		notificationSettings.setFlowFamilyID(flow.getFlowFamily().getFlowFamilyID());

		notificationSettingsDAO.getAnnotatedDAO().addOrUpdate(notificationSettings, null);
	}

	public FlowFamililyNotificationSettings getNotificationSettings(ImmutableFlow flow) throws SQLException {

		return getNotificationSettings(flow.getFlowFamily());
	}

	public FlowFamililyNotificationSettings getNotificationSettings(ImmutableFlowFamily flowFamily) throws SQLException {

		FlowFamililyNotificationSettings notificationSettings = notificationSettingsDAO.get(flowFamily.getFlowFamilyID());

		if (notificationSettings != null) {

			TemplateUtils.setTemplatedFields(notificationSettings, this);

			if (notificationSettings.getStatusChangedUserEmailSubject() == null) {

				notificationSettings.setStatusChangedUserEmailSubject(statusChangedUserEmailSubject);
			}

			if (notificationSettings.getStatusChangedUserEmailMessage() == null) {

				notificationSettings.setStatusChangedUserEmailMessage(statusChangedUserEmailMessage);
			}

			if (notificationSettings.getExternalMessageReceivedUserEmailSubject() == null) {

				notificationSettings.setExternalMessageReceivedUserEmailSubject(externalMessageReceivedUserEmailSubject);
			}

			if (notificationSettings.getExternalMessageReceivedUserEmailMessage() == null) {

				notificationSettings.setExternalMessageReceivedUserEmailMessage(externalMessageReceivedUserEmailMessage);
			}

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

			if (notificationSettings.getExternalMessageReceivedManagerSubject() == null) {

				notificationSettings.setExternalMessageReceivedManagerSubject(externalMessageReceivedManagerEmailSubject);
			}

			if (notificationSettings.getExternalMessageReceivedManagerMessage() == null) {

				notificationSettings.setExternalMessageReceivedManagerMessage(externalMessageReceivedManagerEmailMessage);
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

		TemplateUtils.setTemplatedFields(notificationSettings, this);

		//TODO move every text setting over to @Templated

		notificationSettings.setStatusChangedUserEmailSubject(statusChangedUserEmailSubject);
		notificationSettings.setStatusChangedUserEmailMessage(statusChangedUserEmailMessage);
		notificationSettings.setExternalMessageReceivedUserEmailSubject(externalMessageReceivedUserEmailSubject);
		notificationSettings.setExternalMessageReceivedUserEmailMessage(externalMessageReceivedUserEmailMessage);

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
		notificationSettings.setSendInternalMessageAddedManagerEmail(sendInternalMessageAddedManagerEmail);
		notificationSettings.setSendExternalMessageReceivedUserEmail(sendExternalMessageReceivedUserEmail);
		notificationSettings.setSendExternalMessageReceivedUserSMS(sendExternalMessageReceivedUserSMS);

		notificationSettings.setExternalMessageReceivedManagerSubject(externalMessageReceivedManagerEmailSubject);
		notificationSettings.setExternalMessageReceivedManagerMessage(externalMessageReceivedManagerEmailMessage);

		notificationSettings.setSendFlowInstanceAssignedManagerEmail(sendFlowInstanceAssignedManagerEmail);

		notificationSettings.setSendStatusChangedManagerEmail(sendStatusChangedManagerEmail);
		notificationSettings.setSendStatusChangedUserEmail(sendStatusChangedUserEmail);
		notificationSettings.setSendStatusChangedUserSMS(sendStatusChangedUserSMS);

		notificationSettings.setSendFlowInstanceSubmittedManagerEmail(sendFlowInstanceSubmittedManagerEmail);

		notificationSettings.setSendFlowInstanceAssignedGroupEmail(sendFlowInstanceAssignedGroupEmail);

		notificationSettings.setSendFlowInstanceSubmittedGlobalEmail(sendFlowInstanceSubmittedGlobalEmail);
		notificationSettings.setFlowInstanceSubmittedGlobalEmailAddresses(flowInstanceSubmittedGlobalEmailAddress);
		notificationSettings.setFlowInstanceSubmittedGlobalEmailSubject(flowInstanceSubmittedGlobalEmailSubject);
		notificationSettings.setFlowInstanceSubmittedGlobalEmailMessage(flowInstanceSubmittedGlobalEmailMessage);
		
		notificationSettings.setSendFlowInstanceAssignedGlobalEmail(sendFlowInstanceAssignedGlobalEmail);

		notificationSettings.setSendFlowInstanceArchivedGlobalEmail(sendFlowInstanceArchivedGlobalEmail);
		
		notificationSettings.setExternalMessageReceivedGlobalEmailAddresses(externalMessageReceivedGlobalEmailAddresses);
		notificationSettings.setManagerExpiredGlobalEmailAddresses(managerExpiredGlobalEmailAddresses);

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

			File pdfFile = null;
			File xmlFile = null;

			if (notificationSettings.isFlowInstanceSubmittedGlobalEmailAttachPDF()) {

				try {

					pdfFile = getEventPDF(flowInstance.getFlowInstanceID(), eventID, flowInstanceSubmittedGlobalEmailPDFSizeLimit);

				} catch (PDFSizeExeededException e) {

					log.warn("PDF file (" + BinarySizeFormater.getFormatedSize(e.getSize()) + ") for flow instance " + flowInstance + " exceeds the size limit of " + flowInstanceSubmittedGlobalEmailPDFSizeLimit + " MB set for global email submit notifications and will not be attached to the generated email.");
				}
			}

			if (notificationSettings.isFlowInstanceSubmittedGlobalEmailAttachXML()) {
				try {

					xmlFile = getEventXML(flowInstance.getFlowInstanceID(), eventID, flowInstanceSubmittedGlobalEmailPDFSizeLimit);

				} catch (PDFSizeExeededException e) {

					log.warn("XML file (" + BinarySizeFormater.getFormatedSize(e.getSize()) + ") for flow instance " + flowInstance + " exceeds the size limit of " + flowInstanceSubmittedGlobalEmailPDFSizeLimit + " MB set for global email submit notifications and will not be attached to the generated email.");
				}
			}

			for (String email : notificationSettings.getFlowInstanceSubmittedGlobalEmailAddresses()) {

				sendGlobalEmail(null, flowInstance, getPosterContact(flowInstance, null), email, flowInstanceSubmittedGlobalEmailSubject, flowInstanceSubmittedGlobalEmailMessage, pdfFile, notificationSettings.isFlowInstanceSubmittedGlobalEmailAttachPDFAttachmentsSeparately(), new NotificationOptions(xmlFile));
			}

			return new SimpleForegroundModuleResponse("Global email notification triggered for flow instance " + flowInstance);
		}

		return new SimpleForegroundModuleResponse("Global email notifications are disabled for flow " + flowInstance.getFlow());
	}

	public File getEventPDF(Integer flowInstanceID, Integer eventID, Integer pdfSizeLimit) throws PDFSizeExeededException {

		File pdfFile = null;

		if (pdfProvider != null) {

			pdfFile = pdfProvider.getPDF(flowInstanceID, eventID);

			if (pdfFile != null) {

				if (isValidAttachmentSize(pdfSizeLimit, pdfFile)) {

					return pdfFile;

				} else {

					throw new PDFSizeExeededException(pdfFile.length());
				}
			}
		}

		return null;
	}

	public File getEventXML(Integer flowInstanceID, Integer eventID, Integer xmlSizeLimit) throws PDFSizeExeededException {

		File xmlFile = null;

		if (xmlProvider != null) {

			xmlFile = xmlProvider.getXML(flowInstanceID, eventID);

			if (xmlFile != null) {

				if (isValidAttachmentSize(xmlSizeLimit, xmlFile)) {

					return xmlFile;

				} else {

					throw new PDFSizeExeededException(xmlFile.length());
				}
			}
		}

		return null;
	}

	public Integer getFlowInstanceSubmittedUserEmailPDFSizeLimit() {

		return flowInstanceSubmittedUserEmailPDFSizeLimit;
	}

	public Integer getFlowInstanceSubmittedGlobalEmailPDFSizeLimit() {

		return flowInstanceSubmittedGlobalEmailPDFSizeLimit;
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

		if (flowInstanceIDs == null) {

			throw new URINotFoundException(uriParser);
		}

		for (Integer flowInstanceID : flowInstanceIDs) {

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

				String message = getFlowInstaceSubmittedUserSMSMessage(notificationSettings, flowInstance);

				for (Contact contact : contacts) {

					if (sendContactSMS(flowInstance, contact, message)) {

						sentSMS++;
					}
				}
			}

			if (notificationSettings.isSendFlowInstanceSubmittedUserEmail()) {

				boolean attachPDF = false;

				String message = getFlowInstaceSubmittedUserEmailMessage(notificationSettings, flowInstance);

				if (notificationSettings.isFlowInstanceSubmittedUserEmailAttachPDF() && pdfFile != null) {

					if (isValidAttachmentSize(flowInstanceSubmittedUserEmailPDFSizeLimit, pdfFile)) {

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

				if (flowInstanceID != null) {

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

			ImmutableFlowInstance flowInstance = event.getFlowInstanceManager().getFlowInstance();

			FlowFamililyNotificationSettings notificationSettings = getNotificationSettings(flowInstance.getFlow());

			File pdfFile = null;
			File xmlFile = null;

			if (pdfProvider != null && (notificationSettings.isFlowInstanceSubmittedUserEmailAttachPDF() || notificationSettings.isFlowInstanceSubmittedGlobalEmailAttachPDF())) {

				pdfFile = pdfProvider.getPDF(flowInstance.getFlowInstanceID(), event.getEvent().getEventID());
			}

			if (xmlProvider != null && notificationSettings.isFlowInstanceSubmittedGlobalEmailAttachXML()) {

				xmlFile = xmlProvider.getXML(flowInstance.getFlowInstanceID(), event.getEvent().getEventID());
			}

			if (!flowInstance.getAttributeHandler().getPrimitiveBoolean(Constants.FLOW_INSTANCE_SUPPRESS_USER_SUBMITTED_NOTIFICATION_ATTRIBUTE)) {

				Collection<Contact> contacts = getContacts(flowInstance);

				if (contacts != null) {

					if (notificationSettings.isSendFlowInstanceSubmittedUserSMS()) {

						String message = getFlowInstaceSubmittedUserSMSMessage(notificationSettings, flowInstance);

						for (Contact contact : contacts) {

							sendContactSMS(flowInstance, contact, message);
						}
					}

					if (notificationSettings.isSendFlowInstanceSubmittedUserEmail()) {

						boolean attachPDF = false;

						String message = getFlowInstaceSubmittedUserEmailMessage(notificationSettings, flowInstance);

						if (notificationSettings.isFlowInstanceSubmittedUserEmailAttachPDF() && pdfFile != null) {

							if (isValidAttachmentSize(flowInstanceSubmittedUserEmailPDFSizeLimit, pdfFile)) {

								attachPDF = true;

								message = message.replaceAll("\\$flowInstance.pdfAttachedText", flowInstanceSubmittedUserEmailPDFAttachedText);

							} else {

								message = message.replaceAll("\\$flowInstance.pdfAttachedText", flowInstanceSubmittedUserEmailPDFSizeLimitExceededText);

								log.warn("PDF file for flow instance " + flowInstance + " exceeds the size limit of " + flowInstanceSubmittedUserEmailPDFSizeLimit + " MB set for user email submit notifications and will not be attached to the generated email.");
							}
						}

						for (Contact contact : contacts) {

							sendContactEmail(flowInstance, contact, notificationSettings.getFlowInstanceSubmittedUserEmailSubject(), message, attachPDF ? pdfFile : null);
						}
					}
				}
			}

			if (!flowInstance.getAttributeHandler().getPrimitiveBoolean(Constants.FLOW_INSTANCE_SUPPRESS_MANAGERS_SUBMITTED_NOTIFICATION_ATTRIBUTE)) {

				Contact posterContact = getPosterContact(event.getFlowInstanceManager().getFlowInstance());

				if (notificationSettings.isSendFlowInstanceSubmittedManagerEmail()) {

					sendManagerEmails(flowInstance, posterContact, flowInstanceSubmittedManagerEmailSubject, flowInstanceSubmittedManagerEmailMessage, null, true);
				}

				if (notificationSettings.isSendFlowInstanceSubmittedGlobalEmail() && notificationSettings.getFlowInstanceSubmittedGlobalEmailAddresses() != null) {

					boolean attachPDF = false;
					boolean attachXML = false;

					if (notificationSettings.isFlowInstanceSubmittedGlobalEmailAttachPDF() && pdfFile != null) {

						if (isValidAttachmentSize(flowInstanceSubmittedGlobalEmailPDFSizeLimit, pdfFile)) {

							attachPDF = true;

						} else {

							log.warn("PDF file for flow instance " + flowInstance + " exceeds the size limit of " + flowInstanceSubmittedGlobalEmailPDFSizeLimit + " MB set for global email submit notifications and will not be attached to the generated email.");
						}
					}

					if (notificationSettings.isFlowInstanceSubmittedGlobalEmailAttachXML() && xmlFile != null) {

						if (isValidAttachmentSize(flowInstanceSubmittedGlobalEmailPDFSizeLimit, xmlFile)) {

							attachXML = true;

						} else {

							log.warn("XML file for flow instance " + flowInstance + " exceeds the size limit of " + flowInstanceSubmittedGlobalEmailPDFSizeLimit + " MB set for global email submit notifications and will not be attached to the generated email.");
						}
					}

					NotificationOptions options = null;

					if (attachXML) {

						options = new NotificationOptions(xmlFile);
					}

					for (String email : notificationSettings.getFlowInstanceSubmittedGlobalEmailAddresses()) {

						sendGlobalEmail(event.getSiteProfile(), flowInstance, posterContact, email, notificationSettings.getFlowInstanceSubmittedGlobalEmailSubject(), notificationSettings.getFlowInstanceSubmittedGlobalEmailMessage(), attachPDF ? pdfFile : null, notificationSettings.isFlowInstanceSubmittedGlobalEmailAttachPDFAttachmentsSeparately(), options);
					}
				}
			}

		} else if (event.getActionID() != null) {

			log.warn("Submit event received with unsupported action ID: " + event.getActionID());
		}
	}

	private boolean isValidAttachmentSize(Integer sizeLimit, File pdfFile) {

		return sizeLimit == null || ((sizeLimit * BinarySizes.MegaByte) >= pdfFile.length());

	}

	@EventListener(channel = FlowInstance.class)
	public void processEvent(ExternalMessageAddedEvent event, EventSource eventSource) throws SQLException {

		if (CollectionUtils.isEmpty(event.getFlowInstance().getOwners())) {

			return;
		}

		FlowInstance flowInstance = getFlowInstance(event.getFlowInstance().getFlowInstanceID());

		FlowFamililyNotificationSettings notificationSettings = getNotificationSettings(flowInstance.getFlow());

		if (event.getSenderType() == SenderType.MANAGER) {

			if (notificationSettings.isSendExternalMessageReceivedUserEmail() || notificationSettings.isSendExternalMessageReceivedUserSMS()) {

				Collection<Contact> contacts = getContacts(flowInstance);

				if (contacts != null) {

					for (Contact contact : contacts) {

						if (notificationSettings.isSendExternalMessageReceivedUserEmail()) {

							sendContactEmail(flowInstance, contact, notificationSettings.getExternalMessageReceivedUserEmailSubject(), notificationSettings.getExternalMessageReceivedUserEmailMessage(), null);
						}

						if (notificationSettings.isSendExternalMessageReceivedUserSMS()) {

							sendContactSMS(flowInstance, contact, externalMessageReceivedUserSMS);
						}
					}
				}
			}

		} else if (event.getSenderType() == SenderType.USER) {

			if (notificationSettings.isSendExternalMessageReceivedManagerEmail() || notificationSettings.isSendExternalMessageReceivedGlobalEmail()) {

				Contact contact = getPosterContact(flowInstance, event.getSiteProfile());

				if (notificationSettings.isSendExternalMessageReceivedManagerEmail()) {

					sendManagerEmails(flowInstance, contact, notificationSettings.getExternalMessageReceivedManagerSubject(), notificationSettings.getExternalMessageReceivedManagerMessage(), null, false);
				}

				if (notificationSettings.isSendExternalMessageReceivedGlobalEmail() && notificationSettings.getExternalMessageReceivedGlobalEmailAddresses() != null) {

					for (String email : notificationSettings.getExternalMessageReceivedGlobalEmailAddresses()) {

						sendGlobalEmail(event.getSiteProfile(), flowInstance, contact, email, externalMessageReceivedGlobalEmailSubject, externalMessageReceivedGlobalEmailMessage, null, false);
					}
				}
			}

		} else {

			log.warn("External message added event received with unsupported sender type: " + event.getSenderType());
		}
	}
	
	@EventListener(channel = FlowInstance.class)
	public void processEvent(InternalMessageAddedEvent event, EventSource eventSource) throws SQLException {

		if (CollectionUtils.isEmpty(event.getFlowInstance().getManagers())) {

			return;
		}

		FlowInstance flowInstance = getFlowInstance(event.getFlowInstance().getFlowInstanceID());

		FlowFamililyNotificationSettings notificationSettings = getNotificationSettings(flowInstance.getFlow());

		if (notificationSettings.isSendInternalMessageAddedManagerEmail()) {

			List<User> excludedManagers = Collections.singletonList(event.getInternalMessage().getPoster());

			sendManagerEmails(flowInstance, null, notificationSettings.getInternalMessageAddedManagerEmailSubject(), notificationSettings.getInternalMessageAddedManagerEmailMessage(), excludedManagers, false);
		}
	}

	@EventListener(channel = FlowInstance.class)
	public void processEvent(StatusChangedByManagerEvent event, EventSource eventSource) throws SQLException {

		FlowInstance flowInstance = event.getFlowInstance();

		try { // Read from DB as event flow instance might not have all the fields we need
			flowInstance = getFlowInstance(flowInstance.getFlowInstanceID());

		} catch (Exception e) {

			log.error("Error getting flow instance " + flowInstance + " with full relations, using instance from event instead", e);
		}

		FlowFamililyNotificationSettings notificationSettings = getNotificationSettings(event.getFlowInstance().getFlow());

		if (!event.isSuppressUserNotifications()) {

			Collection<Contact> contacts = getContacts(flowInstance);

			if (contacts != null) {

				//Check which type of notification the contact should get
				if (event.getPreviousStatus().getContentType() != ContentType.ARCHIVED && event.getFlowInstance().getStatus().getContentType() == ContentType.ARCHIVED) {

					if (!flowInstance.getAttributeHandler().getPrimitiveBoolean(Constants.FLOW_INSTANCE_SUPPRESS_ARCHIVED_NOTIFICATION_ATTRIBUTE)) {

						if (notificationSettings.isSendFlowInstanceArchivedUserEmail() || notificationSettings.isSendFlowInstanceArchivedUserSMS()) {

							for (Contact contact : contacts) {

								if (notificationSettings.isSendFlowInstanceArchivedUserEmail()) {

									sendContactEmail(flowInstance, contact, notificationSettings.getFlowInstanceArchivedUserEmailSubject(), getFlowInstaceArchivedUserEmailMessage(notificationSettings, flowInstance), null);
								}

								if (notificationSettings.isSendFlowInstanceArchivedUserSMS()) {

									sendContactSMS(flowInstance, contact, getFlowInstanceArchivedUserSMSMessage(notificationSettings, flowInstance));
								}
							}
						}
					}

				} else if (!CollectionUtils.isEmpty(flowInstance.getOwners())) {

					for (Contact contact : contacts) {

						if (notificationSettings.isSendStatusChangedUserEmail()) {

							sendContactEmail(flowInstance, contact, notificationSettings.getStatusChangedUserEmailSubject(), notificationSettings.getStatusChangedUserEmailMessage(), null);
						}

						if (notificationSettings.isSendStatusChangedUserSMS()) {

							sendContactSMS(flowInstance, contact, statusChangedUserSMS);
						}
					}
				}
			}
		}

		Contact posterContact = getPosterContact(flowInstance);
		
		if (notificationSettings.isSendStatusChangedManagerEmail()) {

			sendManagerEmails(flowInstance, posterContact, statusChangedManagerEmailSubject, statusChangedManagerEmailMessage, CollectionUtils.getList(event.getUser()), false);
		}
		
		if (notificationSettings.isSendFlowInstanceArchivedGlobalEmail() && notificationSettings.getFlowInstanceArchivedGlobalEmailAddresses() != null) {

			if (event.getPreviousStatus().getContentType() != ContentType.ARCHIVED && event.getFlowInstance().getStatus().getContentType() == ContentType.ARCHIVED) {

				File pdfFile = null;
				boolean attachPDF = false;

				if (notificationSettings.isFlowInstanceArchivedGlobalEmailAttachPDF()) {

					if (pdfProvider != null) {

						pdfFile = pdfProvider.getPDF(flowInstance.getFlowInstanceID(), FlowInstanceUtils.getLatestSubmitEvent(flowInstance).getEventID());

						if (pdfFile != null) {

							if (isValidAttachmentSize(flowInstanceSubmittedGlobalEmailPDFSizeLimit, pdfFile)) {

								attachPDF = true;

							} else {

								log.warn("PDF file for flow instance " + flowInstance + " exceeds the size limit of " + flowInstanceSubmittedGlobalEmailPDFSizeLimit + " MB set for global email archived notifications and will not be attached to the generated email.");
							}
						}
					}
				}
				
				for (String email : notificationSettings.getFlowInstanceArchivedGlobalEmailAddresses()) {
					
					sendGlobalEmail(event.getSiteProfile(), flowInstance, posterContact, email, notificationSettings.getFlowInstanceArchivedGlobalEmailSubject(), notificationSettings.getFlowInstanceArchivedGlobalEmailMessage(), attachPDF ? pdfFile : null, false);
				}
			}
		}
	}

	@EventListener(channel = FlowInstance.class)
	public void processEvent(ManagersChangedEvent event, EventSource eventSource) throws SQLException {

		FlowFamililyNotificationSettings notificationSettings = getNotificationSettings(event.getFlowInstance().getFlow());

		if (notificationSettings.isSendFlowInstanceAssignedManagerEmail() || notificationSettings.isSendFlowInstanceAssignedGroupEmail()  || notificationSettings.isSendFlowInstanceAssignedGlobalEmail() || event.getAdditionalGlobalEmailRecipients() != null) {

			List<User> excludedManagers = new ArrayList<>();
			List<Group> excludedManagerGroups = new ArrayList<>();

			if (event.getPreviousManagers() != null) {

				excludedManagers.addAll(event.getPreviousManagers());
			}
			
			if (event.getPreviousManagerGroups() != null) {
				
				excludedManagerGroups.addAll(event.getPreviousManagerGroups());
			}

			if (event.getUser() != null) {

				excludedManagers.add(event.getUser());
			}

			FlowInstance flowInstance = event.getFlowInstance();

			try { // Read from DB as event flow instance might not have all the fields we need
				flowInstance = getFlowInstance(flowInstance.getFlowInstanceID());

			} catch (Exception e) {

				log.error("Error getting flow instance " + flowInstance + " with full relations, using instance from event instead", e);
			}
			
			Contact posterContact = getPosterContact(flowInstance);
			
			if (notificationSettings.isSendFlowInstanceAssignedManagerEmail()) {
				
				sendManagerEmails(flowInstance, posterContact, notificationSettings.getFlowInstanceAssignedManagerEmailSubject(), notificationSettings.getFlowInstanceAssignedManagerEmailMessage(), excludedManagers, false);
			}
			
			if (notificationSettings.isSendFlowInstanceAssignedGroupEmail()) {
				
				Set<String> managerGroupEmailRecipientAddresses = getManagerGroupEmailRecipientAddresses(flowInstance, excludedManagerGroups);
				
				for (String email : managerGroupEmailRecipientAddresses) {
					
					sendGlobalEmail(event.getSiteProfile(), flowInstance, posterContact, email, notificationSettings.getFlowInstanceAssignedGroupEmailSubject(), notificationSettings.getFlowInstanceAssignedGroupEmailMessage(), null, false);
				}
			}
			
			if (notificationSettings.isSendFlowInstanceAssignedGlobalEmail() && !CollectionUtils.isEmpty(notificationSettings.getFlowInstanceAssignedGlobalEmailAddresses())) {
				
				for (String email : notificationSettings.getFlowInstanceAssignedGlobalEmailAddresses()) {
					
					sendGlobalEmail(event.getSiteProfile(), flowInstance, posterContact, email, notificationSettings.getFlowInstanceAssignedGlobalEmailSubject(), notificationSettings.getFlowInstanceAssignedGlobalEmailMessage(), null, false);
				}
			}
			
			if (event.getAdditionalGlobalEmailRecipients() != null) {
				
				for (String email : event.getAdditionalGlobalEmailRecipients()) {
					
					sendGlobalEmail(event.getSiteProfile(), flowInstance, posterContact, email, notificationSettings.getFlowInstanceAssignedGlobalEmailSubject(), notificationSettings.getFlowInstanceAssignedGlobalEmailMessage(), null, false);
				}
			}
		}
	}

	private Set<String> getManagerGroupEmailRecipientAddresses(FlowInstance flowInstance, List<Group> excludedManagerGroups) {

		List<Group> managerGroups = flowInstance.getManagerGroups();
		List<FlowFamilyManagerGroup> flowFamilyManagerGroups = flowInstance.getFlow().getFlowFamily().getManagerGroups();
		
		if (!CollectionUtils.isEmpty(managerGroups) && !CollectionUtils.isEmpty(flowFamilyManagerGroups)) {
			
			Set<String> addresses = new HashSet<>();
			
			managerGroups.removeAll(excludedManagerGroups);
			
			for (Group managerGroup : managerGroups) {
				
				for (FlowFamilyManagerGroup flowFamilyManagerGroup : flowFamilyManagerGroups) {
					
					if (managerGroup.getGroupID().equals(flowFamilyManagerGroup.getGroupID()) && flowFamilyManagerGroup.getNotificationEmailAddresses() != null) {
						
						addresses.addAll(flowFamilyManagerGroup.getNotificationEmailAddresses());
					}
				}
			}
			
			return addresses;
		}
		
		return Collections.emptySet();
	}

	@EventListener(channel = FlowInstance.class, priority = 100)
	public void processEvent(MultiSigningInitiatedEvent event, EventSource eventSource) throws SQLException {
		
		FlowFamililyNotificationSettings notificationSettings = getNotificationSettings(event.getFlowInstanceManager().getFlowInstance().getFlow());

		if (!event.getFlowInstanceManager().getFlowInstance().getFlow().usesSequentialSigning()) {

			Set<SigningParty> signingParties = MultiSignUtils.getSigningParties(event.getFlowInstanceManager());

			if (signingParties != null) {

				Contact contact = getPosterContact(event.getFlowInstanceManager().getFlowInstance());

				for (SigningParty signingParty : signingParties) {

					sendSigningPartyEmail(event.getFlowInstanceManager().getFlowInstance(), signingParty, contact, NotificationRecipient.SIGNING_PARTY, notificationSettings.getFlowInstanceMultiSignInitiatedUserEmailSubject(), notificationSettings.getFlowInstanceMultiSignInitiatedUserEmailMessage());
					sendSigningPartySMS(event.getFlowInstanceManager().getFlowInstance(), signingParty, contact, NotificationRecipient.SIGNING_PARTY, notificationSettings.getFlowInstanceMultiSignInitiatedUserSMS());
				}
			}
		}
	}

	public void sendSigningPartyNotifications(ImmutableFlowInstance flowInstance, SigningParty signingParty) throws SQLException {
		
		FlowFamililyNotificationSettings notificationSettings = getNotificationSettings(flowInstance.getFlow());

		Contact contact = getPosterContact(flowInstance);

		sendSigningPartyEmail(flowInstance, signingParty, contact, NotificationRecipient.SIGNING_PARTY, notificationSettings.getFlowInstanceMultiSignInitiatedUserEmailSubject(), notificationSettings.getFlowInstanceMultiSignInitiatedUserEmailMessage());
		sendSigningPartySMS(flowInstance, signingParty, contact, NotificationRecipient.SIGNING_PARTY, notificationSettings.getFlowInstanceMultiSignInitiatedUserSMS());
	}

	@EventListener(channel = FlowInstance.class)
	public void processEvent(MultiSigningCanceledEvent event, EventSource eventSource) throws SQLException, DuplicateFlowInstanceManagerIDException, MissingQueryInstanceDescriptor, QueryProviderNotFoundException, InvalidFlowInstanceStepException, QueryProviderErrorException, QueryInstanceNotFoundInQueryProviderException {

		FlowFamililyNotificationSettings notificationSettings = getNotificationSettings(event.getFlowInstance().getFlow());
		
		Contact contact = null;

		if (event.getCancellingSigningParty() == null) { // One of the owners cancelled

			contact = getPosterContact(event.getFlowInstance());

		} else { // One of the signers cancelled

			List<Contact> ownerContacts = getContacts(event.getFlowInstance());

			if (ownerContacts != null) {
				for (Contact ownerContact : ownerContacts) {

					sendSigningPartyEmail(event.getFlowInstance(), event.getCancellingSigningParty(), ownerContact, NotificationRecipient.OWNER, true, notificationSettings.getFlowInstanceMultiSignCanceledOwnerEmailSubject(), notificationSettings.getFlowInstanceMultiSignCanceledOwnerEmailMessage());
					sendSigningPartySMS(event.getFlowInstance(), event.getCancellingSigningParty(), ownerContact, NotificationRecipient.OWNER, true, notificationSettings.getFlowInstanceMultiSignCanceledOwnerSMS());
				}
			}

			if (event.getUser() != null) {

				contact = FlowInstanceUtils.getContactForUser(event.getUser());

			} else {

				// They cancelling user was not logged in, this code is most likely not used at the curren time
				contact = new Contact();

				contact.setFirstname(event.getCancellingSigningParty().getFirstname());
				contact.setLastname(event.getCancellingSigningParty().getLastname());
				contact.setEmail(event.getCancellingSigningParty().getEmail());
				contact.setMobilePhone(event.getCancellingSigningParty().getMobilePhone());
			}
		}

		for (SigningParty signingParty : event.getSigningParties()) {

			if (signingParty.equals(event.getCancellingSigningParty())) {
				continue;
			}

			sendSigningPartyEmail(event.getFlowInstance(), signingParty, contact, NotificationRecipient.SIGNING_PARTY, true, notificationSettings.getFlowInstanceMultiSignCanceledUserEmailSubject(), notificationSettings.getFlowInstanceMultiSignCanceledUserEmailMessage());
			sendSigningPartySMS(event.getFlowInstance(), signingParty, contact, NotificationRecipient.SIGNING_PARTY, true, notificationSettings.getFlowInstanceMultiSignCanceledUserSMS());
		}
	}

	@EventListener(channel = FlowInstance.class)
	public void processEvent(ManagerMentionedEvent event, EventSource eventSource) throws SQLException {

		if (sendManagerMentionedEmail && event.getMentionedUsers() != null) {

			List<TagSource> sharedTagSources = new ArrayList<TagSource>(4);

			sharedTagSources.add(FLOWINSTANCE_TAG_SOURCE_FACTORY.getTagSource(event.getFlowInstance()));
			sharedTagSources.add(FLOW_TAG_SOURCE_FACTORY.getTagSource(event.getFlowInstance().getFlow()));
			sharedTagSources.add(POSTER_TAG_SOURCE_FACTORY.getTagSource(event.getUser()));
			sharedTagSources.add(new SingleTagSource("$flowInstance.url", getFlowInstanceAdminModuleAlias(event.getFlowInstance()) + "/overview/" + event.getFlowInstance().getFlowInstanceID()));
			sharedTagSources.add(new SingleTagSource("$flowInstance.messagesUrl", getFlowInstanceAdminModuleAlias(event.getFlowInstance()) + "/messages/" + event.getFlowInstance().getFlowInstanceID()));
			sharedTagSources.add(new SingleTagSource("$flowInstance.notesUrl", getFlowInstanceAdminModuleAlias(event.getFlowInstance()) + "/notes/" + event.getFlowInstance().getFlowInstanceID()));

			for (User user : event.getMentionedUsers()) {

				if (EmailUtils.isValidEmailAddress(user.getEmail())) {

					TagReplacer tagReplacer = new TagReplacer();

					tagReplacer.addTagSources(sharedTagSources);
					tagReplacer.addTagSource(MANAGER_TAG_SOURCE_FACTORY.getTagSource(user));

					SimpleEmail email = new SimpleEmail(systemInterface.getEncoding());

					try {
						email.addRecipient(user.getEmail());
						email.setMessageContentType(SimpleEmail.HTML);
						email.setSenderName(getEmailSenderName(event.getFlowInstance()));
						email.setSenderAddress(getEmailSenderAddress(event.getFlowInstance()));
						email.setSubject(tagReplacer.replace(managerMentionedEmailSubject));
						email.setMessage(tagReplacer.replace(managerMentionedEmailMessage));

						systemInterface.getEmailHandler().send(email);

					} catch (Exception e) {

						log.error("Error generating/sending mentioned email " + email, e);
					}

				} else {

					log.warn("Mentioned user " + user + " has invalid email");
				}
			}
		}
	}

	@EventListener(channel = FlowFamily.class)
	public void processEvent(ManagerExpiredEvent event, EventSource eventSource) throws SQLException {

		FlowFamililyNotificationSettings notificationSettings = getNotificationSettings(event.getFlowFamily());

		if (notificationSettings.isSendManagerExpiredGlobalEmail() && notificationSettings.getManagerExpiredGlobalEmailAddresses() != null) {

			if (managerExpiredGlobalEmailSubject != null && managerExpiredGlobalEmailMessage != null) {

				Flow flow = getLatestFlow(event.getFlowFamily());

				StringBuilder flowInstanceLinks = new StringBuilder();

				if (!CollectionUtils.isEmpty(event.getFlowInstanceIDs())) {
					for (int flowInstanceID : event.getFlowInstanceIDs()) {

						FlowInstance flowInstance = getFlowInstance(flowInstanceID);

						flowInstanceLinks.append("<a style=\"display: block;\" href=\"");
						flowInstanceLinks.append(getFlowInstanceAdminModuleAlias(flowInstance) + "/overview/" + flowInstanceID);
						flowInstanceLinks.append("\">");
						flowInstanceLinks.append(flowInstanceTitle + " " + flowInstanceID);
						flowInstanceLinks.append("</a>");
					}
				}

				TagReplacer tagReplacer = new TagReplacer();
				tagReplacer.addTagSource(FLOW_TAG_SOURCE_FACTORY.getTagSource(flow));
				tagReplacer.addTagSource(MANAGER_TAG_SOURCE_FACTORY.getTagSource(event.getManager()));
				tagReplacer.addTagSource(new SingleTagSource("$flowInstances", flowInstanceLinks.toString()));

				String subject = tagReplacer.replace(managerExpiredGlobalEmailSubject);
				String message = tagReplacer.replace(managerExpiredGlobalEmailMessage);

				for (String address : notificationSettings.getManagerExpiredGlobalEmailAddresses()) {

					SimpleEmail email = new SimpleEmail(systemInterface.getEncoding());

					try {
						email.addRecipient(address);
						email.setMessageContentType(SimpleEmail.HTML);
						email.setSenderName(getEmailSenderName(null));
						email.setSenderAddress(getEmailSenderAddress(null));
						email.setSubject(subject);
						email.setMessage(EmailUtils.addMessageBody(message));

						systemInterface.getEmailHandler().send(email);

					} catch (Exception e) {

						log.error("Error generating/sending email " + email, e);
					}
				}
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

	private boolean sendSigningPartyEmail(ImmutableFlowInstance flowInstance, SigningParty signingParty, Contact contact, NotificationRecipient recipient, String subject, String message) {

		return sendSigningPartyEmail(flowInstance, signingParty, contact, recipient, false, subject, message);
	}

	private boolean sendSigningPartyEmail(ImmutableFlowInstance flowInstance, SigningParty signingParty, Contact contact, NotificationRecipient recipient, boolean skipURL, String subject, String message) {

		if (signingParty.getEmail() == null || subject == null || message == null || multiSigningHandler == null) {
			return false;
		}

		if (!EmailUtils.isValidEmailAddress(signingParty.getEmail())) {

			log.warn("Signing party " + signingParty + " from " + flowInstance + " has invalid email");
			return false;
		}

		String url = null;

		if (!skipURL) {

			url = multiSigningHandler.getSigningURL(flowInstance, signingParty);

			if (url == null) {

				log.error("Unable to get signing URL for flow instance " + flowInstance + " and signing party " + signingParty);
				return false;
			}
		}

		TagReplacer tagReplacer = new TagReplacer();

		tagReplacer.addTagSource(FLOWINSTANCE_TAG_SOURCE_FACTORY.getTagSource((FlowInstance) flowInstance));
		tagReplacer.addTagSource(FLOW_TAG_SOURCE_FACTORY.getTagSource((Flow) flowInstance.getFlow()));
		tagReplacer.addTagSource(STATUS_TAG_SOURCE_FACTORY.getTagSource((Status) flowInstance.getStatus()));

		if (contact != null) {

			tagReplacer.addTagSource(CONTACT_TAG_SOURCE_FACTORY.getTagSource(contact));
		}

		tagReplacer.addTagSource(SIGNING_PARTY_TAG_SOURCE_FACTORY.getTagSource(signingParty));
		tagReplacer.addTagSource(new SingleTagSource("$flowInstanceSign.url", url));
		tagReplacer.addTagSource(new SingleTagSource("$flowInstance.url", getUserFlowInstanceModuleAlias(flowInstance) + "/overview/" + flowInstance.getFlow().getFlowID() + "/" + flowInstance.getFlowInstanceID()));
		tagReplacer.addTagSource(new SingleTagSource("$flowInstance.messagesUrl", getUserFlowInstanceModuleAlias(flowInstance) + "/messages/" + flowInstance.getFlow().getFlowID() + "/" + flowInstance.getFlowInstanceID()));
		tagReplacer.addTagSource(new SingleTagSource("$flowInstance.notesUrl", getUserFlowInstanceModuleAlias(flowInstance) + "/notes/" + flowInstance.getFlow().getFlowID() + "/" + flowInstance.getFlowInstanceID()));

		SimpleEmail email = new SimpleEmail(systemInterface.getEncoding());

		try {

			if (recipient.equals(NotificationRecipient.OWNER)) {

				email.addRecipient(contact.getEmail());

			} else {

				email.addRecipient(signingParty.getEmail());
			}

			email.setMessageContentType(SimpleEmail.HTML);
			email.setSenderName(this.getEmailSenderName(flowInstance));
			email.setSenderAddress(this.getEmailSenderAddress(flowInstance));
			email.setSubject(replaceTags(subject, tagReplacer, flowInstance));
			email.setMessage(EmailUtils.addMessageBody(replaceTags(message, tagReplacer, flowInstance)));

			systemInterface.getEmailHandler().send(email);

		} catch (Exception e) {

			log.error("Error generating/sending email " + email, e);
			return false;
		}

		return true;
	}

	private void sendSigningPartySMS(ImmutableFlowInstance flowInstance, SigningParty signingParty, Contact contact, NotificationRecipient recipient, String message) {

		sendSigningPartySMS(flowInstance, signingParty, contact, recipient, false, message);
	}

	private void sendSigningPartySMS(ImmutableFlowInstance flowInstance, SigningParty signingParty, Contact contact, NotificationRecipient recipient, boolean skipURL, String message) {

		if (smsSender == null || message == null || multiSigningHandler == null) {
			return;
		}
		
		if(recipient == NotificationRecipient.OWNER && contact.getMobilePhone() == null) {
			
			return;
		}
		
		if (recipient == NotificationRecipient.SIGNING_PARTY && signingParty.getMobilePhone() == null){
			
			return;
		}

		String url = null;

		if (!skipURL) {

			url = multiSigningHandler.getSigningURL(flowInstance, signingParty);

			if (url == null) {

				log.error("Unable to get signing URL for flow instance " + flowInstance + " and signing party " + signingParty);
				return;
			}
		}

		TagReplacer tagReplacer = new TagReplacer();

		tagReplacer.addTagSource(FLOWINSTANCE_TAG_SOURCE_FACTORY.getTagSource((FlowInstance) flowInstance));
		tagReplacer.addTagSource(FLOW_TAG_SOURCE_FACTORY.getTagSource((Flow) flowInstance.getFlow()));
		tagReplacer.addTagSource(STATUS_TAG_SOURCE_FACTORY.getTagSource((Status) flowInstance.getStatus()));

		if (contact != null) {

			tagReplacer.addTagSource(CONTACT_TAG_SOURCE_FACTORY.getTagSource(contact));
		}

		tagReplacer.addTagSource(SIGNING_PARTY_TAG_SOURCE_FACTORY.getTagSource(signingParty));
		tagReplacer.addTagSource(new SingleTagSource("$flowInstanceSign.url", url));
		tagReplacer.addTagSource(new SingleTagSource("$flowInstance.url", getUserFlowInstanceModuleAlias(flowInstance) + "/overview/" + flowInstance.getFlow().getFlowID() + "/" + flowInstance.getFlowInstanceID()));
		tagReplacer.addTagSource(new SingleTagSource("$flowInstance.messagesUrl", getUserFlowInstanceModuleAlias(flowInstance) + "/messages/" + flowInstance.getFlow().getFlowID() + "/" + flowInstance.getFlowInstanceID()));
		tagReplacer.addTagSource(new SingleTagSource("$flowInstance.notesUrl", getUserFlowInstanceModuleAlias(flowInstance) + "/notes/" + flowInstance.getFlow().getFlowID() + "/" + flowInstance.getFlowInstanceID()));

		SimpleSMS sms = new SimpleSMS();

		try {
			sms.setSenderName(getSMSSenderName(flowInstance));
			sms.setMessage(replaceTags(message, tagReplacer, flowInstance));

			if(recipient == NotificationRecipient.OWNER) {
				
				sms.addRecipient(contact.getMobilePhone());
				
			} else if(recipient == NotificationRecipient.SIGNING_PARTY) {
				
				sms.addRecipient(signingParty.getMobilePhone());
				
			} else {
				
				throw new RuntimeException("Unknown recipient type " + recipient);
			}

			smsSender.send(sms);

		} catch (Exception e) {

			log.error("Error generating/sending sms " + sms, e);
		}
	}

	public boolean sendContactEmail(ImmutableFlowInstance flowInstance, Contact contact, String subject, String message, File pdfFile) {

		if (!contact.isContactByEmail() || contact.getEmail() == null || subject == null || message == null) {

			return false;
		}

		if (!EmailUtils.isValidEmailAddress(contact.getEmail())) {

			log.warn("Contact " + contact + " from " + flowInstance + " has invalid email");
			return false;
		}

		TagReplacer tagReplacer = new TagReplacer();

		tagReplacer.addTagSource(FLOWINSTANCE_TAG_SOURCE_FACTORY.getTagSource((FlowInstance) flowInstance));
		tagReplacer.addTagSource(FLOW_TAG_SOURCE_FACTORY.getTagSource((Flow) flowInstance.getFlow()));
		tagReplacer.addTagSource(STATUS_TAG_SOURCE_FACTORY.getTagSource((Status) flowInstance.getStatus()));
		tagReplacer.addTagSource(CONTACT_TAG_SOURCE_FACTORY.getTagSource(contact));
		tagReplacer.addTagSource(new SingleTagSource("$flowInstance.url", getUserFlowInstanceModuleAlias(flowInstance) + "/overview/" + flowInstance.getFlow().getFlowID() + "/" + flowInstance.getFlowInstanceID()));
		tagReplacer.addTagSource(new SingleTagSource("$flowInstance.messagesUrl", getUserFlowInstanceModuleAlias(flowInstance) + "/messages/" + flowInstance.getFlow().getFlowID() + "/" + flowInstance.getFlowInstanceID()));
		tagReplacer.addTagSource(new SingleTagSource("$flowInstance.notesUrl", getUserFlowInstanceModuleAlias(flowInstance) + "/notes/" + flowInstance.getFlow().getFlowID() + "/" + flowInstance.getFlowInstanceID()));

		SimpleEmail email = new SimpleEmail(systemInterface.getEncoding());

		try {
			email.addRecipient(contact.getEmail());
			email.setMessageContentType(SimpleEmail.HTML);
			email.setSenderName(this.getEmailSenderName(flowInstance));
			email.setSenderAddress(this.getEmailSenderAddress(flowInstance));
			email.setSubject(replaceTags(subject, tagReplacer, flowInstance));
			email.setMessage(EmailUtils.addMessageBody(replaceTags(message, tagReplacer, flowInstance)));

			if (pdfFile != null && pdfFilename != null) {

				String generatedFilename = tagReplacer.replace(pdfFilename) + ".pdf";

				email.add(new FileAttachment(pdfFile, FileUtils.toValidHttpFilename(generatedFilename)));
			}

			systemInterface.getEmailHandler().send(email);

		} catch (Exception e) {

			log.error("Error generating/sending email " + email, e);
			return false;
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
		tagReplacer.addTagSource(new SingleTagSource("$flowInstance.url", getUserFlowInstanceModuleAlias(flowInstance) + "/overview/" + flowInstance.getFlow().getFlowID() + "/" + flowInstance.getFlowInstanceID()));
		tagReplacer.addTagSource(new SingleTagSource("$flowInstance.messagesUrl", getUserFlowInstanceModuleAlias(flowInstance) + "/messages/" + flowInstance.getFlow().getFlowID() + "/" + flowInstance.getFlowInstanceID()));
		tagReplacer.addTagSource(new SingleTagSource("$flowInstance.notesUrl", getUserFlowInstanceModuleAlias(flowInstance) + "/notes/" + flowInstance.getFlow().getFlowID() + "/" + flowInstance.getFlowInstanceID()));

		SimpleSMS sms = new SimpleSMS();

		try {
			sms.setSenderName(this.getSMSSenderName(flowInstance));
			sms.setMessage(replaceTags(message, tagReplacer, flowInstance));
			sms.addRecipient(contact.getMobilePhone());

			smsSender.send(sms);

		} catch (Exception e) {

			log.error("Error generating/sending sms " + sms, e);
			return false;
		}

		return true;
	}

	public void sendManagerEmails(ImmutableFlowInstance flowInstance, Contact contact, String subject, String message, List<User> excludedManagers, boolean useFlowFamilyManagers) throws SQLException {

		if (subject == null || message == null) {

			return;
		}

		List<User> managers;

		if (useFlowFamilyManagers) {

			managers = getFlowFamilyManagers(flowInstance);

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
		sharedTagSources.add(new SingleTagSource("$flowInstance.url", getFlowInstanceAdminModuleAlias(flowInstance) + "/overview/" + flowInstance.getFlowInstanceID()));
		sharedTagSources.add(new SingleTagSource("$flowInstance.messagesUrl", getFlowInstanceAdminModuleAlias(flowInstance) + "/messages/" + flowInstance.getFlowInstanceID()));
		sharedTagSources.add(new SingleTagSource("$flowInstance.notesUrl", getFlowInstanceAdminModuleAlias(flowInstance) + "/notes/" + flowInstance.getFlowInstanceID()));

		if (contact != null) {

			sharedTagSources.add(CONTACT_TAG_SOURCE_FACTORY.getTagSource(contact));
		}

		for (User manager : managers) {

			if (EmailUtils.isValidEmailAddress(manager.getEmail())) {

				TagReplacer tagReplacer = new TagReplacer();

				tagReplacer.addTagSource(MANAGER_TAG_SOURCE_FACTORY.getTagSource(manager));
				tagReplacer.addTagSources(sharedTagSources);

				SimpleEmail email = new SimpleEmail(systemInterface.getEncoding());

				try {
					email.addRecipient(manager.getEmail());
					email.setMessageContentType(SimpleEmail.HTML);
					email.setSenderName(this.getEmailSenderName(flowInstance));
					email.setSenderAddress(this.getEmailSenderAddress(flowInstance));
					email.setSubject(replaceTags(subject, tagReplacer, flowInstance));
					email.setMessage(EmailUtils.addMessageBody(replaceTags(message, tagReplacer, flowInstance)));

					systemInterface.getEmailHandler().send(email);

				} catch (Exception e) {

					log.error("Error generating/sending email " + email, e);
				}

			} else {

				log.warn("Manager " + manager + " has invalid email");
			}
		}
	}

	public void sendGlobalEmail(SiteProfile siteProfile, ImmutableFlowInstance flowInstance, Contact contact, String address, String subject, String message, File pdfFile, boolean sendPDFAttachmentsSeparately) {

		sendGlobalEmail(siteProfile, flowInstance, contact, address, subject, message, pdfFile, sendPDFAttachmentsSeparately, null);
	}

	public void sendGlobalEmail(SiteProfile siteProfile, ImmutableFlowInstance flowInstance, Contact contact, String address, String subject, String message, File pdfFile, boolean sendPDFAttachmentsSeparately, NotificationOptions options) {

		if (subject == null || message == null) {

			return;
		}

		TagReplacer tagReplacer = new TagReplacer();

		tagReplacer.addTagSource(FLOWINSTANCE_TAG_SOURCE_FACTORY.getTagSource((FlowInstance) flowInstance));
		tagReplacer.addTagSource(FLOW_TAG_SOURCE_FACTORY.getTagSource((Flow) flowInstance.getFlow()));
		tagReplacer.addTagSource(STATUS_TAG_SOURCE_FACTORY.getTagSource((Status) flowInstance.getStatus()));
		tagReplacer.addTagSource(new SingleTagSource("$flowInstance.url", getFlowInstanceAdminModuleAlias(flowInstance) + "/overview/" + flowInstance.getFlowInstanceID()));
		tagReplacer.addTagSource(new SingleTagSource("$flowInstance.messagesUrl", getFlowInstanceAdminModuleAlias(flowInstance) + "/messages/" + flowInstance.getFlowInstanceID()));
		tagReplacer.addTagSource(new SingleTagSource("$flowInstance.notesUrl", getFlowInstanceAdminModuleAlias(flowInstance) + "/notes/" + flowInstance.getFlowInstanceID()));

		if (contact != null) {

			tagReplacer.addTagSource(CONTACT_TAG_SOURCE_FACTORY.getTagSource(contact));
		}

		SimpleEmail email = new SimpleEmail(systemInterface.getEncoding());

		try {
			email.addRecipient(address);
			email.setMessageContentType(SimpleEmail.HTML);

			if (options != null && !StringUtils.isEmpty(options.getSenderName()) && !StringUtils.isEmpty(options.getSenderAddress())) {

				email.setSenderName(options.getSenderName());
				email.setSenderAddress(options.getSenderAddress());

			} else {

				email.setSenderName(this.getEmailSenderName(flowInstance));
				email.setSenderAddress(this.getEmailSenderAddress(flowInstance));
			}

			email.setSubject(replaceTags(subject, tagReplacer, flowInstance));
			email.setMessage(EmailUtils.addMessageBody(replaceTags(message, tagReplacer, flowInstance)));

			String generatedPDFFilename = null;
			String generatedXMLFilename = null;

			if (pdfFile != null && pdfFilename != null) {

				generatedPDFFilename = FileUtils.toValidHttpFilename(tagReplacer.replace(pdfFilename) + ".pdf");

				if (!sendPDFAttachmentsSeparately) {

					email.add(new FileAttachment(pdfFile, generatedPDFFilename));

				} else {

					email.add(new ByteArrayAttachment(pdfProvider.removePDFAttachments(pdfFile), MimeUtils.getMimeType(generatedPDFFilename), generatedPDFFilename));

					List<PDFByteAttachment> attachments = pdfProvider.getPDFAttachments(pdfFile, true);

					if (!CollectionUtils.isEmpty(attachments)) {

						for (PDFByteAttachment attachment : attachments) {

							String attachmentName;

							if (attachment.getAttachmentName().equals(attachment.getFilename())) {

								attachmentName = attachment.getFilename();

							} else {

								attachmentName = attachment.getAttachmentName() + " - " + attachment.getFilename();
							}

							email.add(new ByteArrayAttachment(attachment.getData(), MimeUtils.getMimeType(attachment.getFilename()), attachmentName));
						}
					}
				}
			}

			File xmlFile = options != null ? options.getXmlFile() : null;

			if (xmlFile != null && xmlFilename != null) {

				generatedXMLFilename = FileUtils.toValidHttpFilename(tagReplacer.replace(xmlFilename) + ".xml");
				email.add(new FileAttachment(xmlFile, generatedXMLFilename));
			}

			if (!CollectionUtils.isEmpty(notificationEmailFilters)) {

				for (NotificationEmailFilter filter : notificationEmailFilters) {

					try {
						filter.filterGlobalEmail(siteProfile, flowInstance, contact, pdfFile, generatedPDFFilename, sendPDFAttachmentsSeparately, xmlFile, generatedXMLFilename, email);

					} catch (Throwable t) {
						log.error("Error running filterGlobalEmail on filter " + filter, t);
					}
				}
			}

			systemInterface.getEmailHandler().send(email);

		} catch (Exception e) {

			log.error("Error generating/sending email " + email, e);
		}

	}

	public boolean sendGlobalSMS(ImmutableFlowInstance flowInstance, Contact contact, String recipient, String message) {

		if (recipient == null || smsSender == null || message == null) {

			return false;
		}

		TagReplacer tagReplacer = new TagReplacer();

		tagReplacer.addTagSource(FLOWINSTANCE_TAG_SOURCE_FACTORY.getTagSource((FlowInstance) flowInstance));
		tagReplacer.addTagSource(FLOW_TAG_SOURCE_FACTORY.getTagSource((Flow) flowInstance.getFlow()));
		tagReplacer.addTagSource(STATUS_TAG_SOURCE_FACTORY.getTagSource((Status) flowInstance.getStatus()));
		tagReplacer.addTagSource(CONTACT_TAG_SOURCE_FACTORY.getTagSource(contact));
		tagReplacer.addTagSource(new SingleTagSource("$flowInstance.url", getUserFlowInstanceModuleAlias(flowInstance) + "/overview/" + flowInstance.getFlow().getFlowID() + "/" + flowInstance.getFlowInstanceID()));
		tagReplacer.addTagSource(new SingleTagSource("$flowInstance.messagesUrl", getUserFlowInstanceModuleAlias(flowInstance) + "/messages/" + flowInstance.getFlow().getFlowID() + "/" + flowInstance.getFlowInstanceID()));
		tagReplacer.addTagSource(new SingleTagSource("$flowInstance.notesUrl", getUserFlowInstanceModuleAlias(flowInstance) + "/notes/" + flowInstance.getFlow().getFlowID() + "/" + flowInstance.getFlowInstanceID()));

		SimpleSMS sms = new SimpleSMS();

		try {
			sms.setSenderName(this.getSMSSenderName(flowInstance));
			sms.setMessage(replaceTags(message, tagReplacer, flowInstance));
			sms.addRecipient(recipient);

			smsSender.send(sms);

		} catch (Exception e) {

			log.error("Error generating/sending sms " + sms, e);
			return false;
		}

		return true;
	}
	
	public String replaceTags(String template, TagReplacer tagReplacer, ImmutableFlowInstance flowInstance) {

		return AttributeTagUtils.replaceTags(tagReplacer.replace(template), flowInstance.getAttributeHandler());
	}

	public List<User> getFlowFamilyManagers(ImmutableFlowInstance flowInstance) throws SQLException {

		FlowFamily flowFamily = flowFamilyDAO.get(flowInstance.getFlow().getFlowFamily().getFlowFamilyID());

		if (flowFamily != null) {

			return FlowFamilyUtils.getActiveManagerUsers(true, flowFamily, systemInterface.getUserHandler());
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

			log.error("Error getting flow instance with attributes and owners " + flowInstance, e);
		}

		return null;
	}

	public List<Contact> getContactsFromDB(FlowInstance flowInstance) {

		try {
			flowInstance = getFlowInstanceWithAttributesAndOwners(flowInstance.getFlowInstanceID());

			if (flowInstance == null) {

				return null;
			}

			return getContacts(flowInstance);

		} catch (Exception e) {

			log.error("Error getting flow instance with attributes and owners " + flowInstance, e);
		}

		return null;
	}

	public List<Contact> getContacts(ImmutableFlowInstance flowInstance) {

		return FlowInstanceUtils.getContacts(flowInstance);
	}

	public Contact getPosterContact(ImmutableFlowInstance flowInstance) {

		return FlowInstanceUtils.getPosterContact(flowInstance);
	}

	public ImmutableFlowInstanceManager getImmutableFlowInstanceManager(FlowInstance flowInstance, SiteProfile siteProfile) throws DuplicateFlowInstanceManagerIDException, MissingQueryInstanceDescriptor, QueryProviderNotFoundException, InvalidFlowInstanceStepException, QueryProviderErrorException, QueryInstanceNotFoundInQueryProviderException {

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

	public Flow getLatestFlow(FlowFamily flowFamily) throws SQLException {

		HighLevelQuery<Flow> query = new HighLevelQuery<Flow>(FLOW_INSTANCE_RELATIONS);

		query.addParameter(flowFlowFamilyIDParamFactory.getParameter(flowFamily));
		query.addOrderByCriteria(flowOrderByLatestVersionCriteria);
		query.addRelationRowLimiter(Flow.class, new MySQLRowLimiter(1));

		return flowDAO.get(query);
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

	private String getFlowInstaceSubmittedUserSMSMessage(FlowFamililyNotificationSettings notificationSettings, ImmutableFlowInstance flowInstance) {

		if (!CollectionUtils.isEmpty(flowInstance.getOwners())) {

			return notificationSettings.getFlowInstanceSubmittedUserSMS();

		} else {

			return notificationSettings.getFlowInstanceSubmittedNotLoggedInUserSMS();
		}
	}

	private String getFlowInstanceArchivedUserSMSMessage(FlowFamililyNotificationSettings notificationSettings, ImmutableFlowInstance flowInstance) {

		if (!CollectionUtils.isEmpty(flowInstance.getOwners())) {

			return notificationSettings.getFlowInstanceArchivedUserSMS();

		} else {

			return notificationSettings.getFlowInstanceArchivedNotLoggedInUserSMS();
		}
	}

	@Override
	public String getEmailSenderName(ImmutableFlowInstance flowInstance) {

		return emailSenderName;
	}

	@Override
	public String getEmailSenderAddress(ImmutableFlowInstance flowInstance) {

		return emailSenderAddress;
	}

	public String getEmailSenderAddress() {

		return emailSenderAddress;
	}

	@Override
	public String getSMSSenderName(ImmutableFlowInstance flowInstance) {

		return smsSenderName;
	}

	public String getUserFlowInstanceModuleAlias(ImmutableFlowInstance flowInstance) {

		return userFlowInstanceModuleAlias;
	}

	public boolean addNotificationEmailFilter(NotificationEmailFilter hook) {

		return notificationEmailFilters.add(hook);
	}

	public boolean removeNotificationEmailFilter(NotificationEmailFilter hook) {

		return notificationEmailFilters.remove(hook);
	}

	public String getFlowInstanceAdminModuleAlias(ImmutableFlowInstance flowInstance) {

		return flowInstanceAdminModuleAlias;
	}

	public static Set<String> getContactTags() {

		Set<String> tags = new LinkedHashSet<String>();

		tags.addAll(StandardFlowNotificationHandler.FLOW_TAG_SOURCE_FACTORY.getTagsSet());
		tags.addAll(StandardFlowNotificationHandler.FLOWINSTANCE_TAG_SOURCE_FACTORY.getTagsSet());
		tags.addAll(StandardFlowNotificationHandler.STATUS_TAG_SOURCE_FACTORY.getTagsSet());
		tags.addAll(StandardFlowNotificationHandler.CONTACT_TAG_SOURCE_FACTORY.getTagsSet());
		tags.add("$flowInstance.url");
		tags.add("$flowInstance.messagesUrl");
		tags.add("$flowInstance.notesUrl");

		return tags;
	}

	public static Set<String> getManagerTags() {

		Set<String> tags = new LinkedHashSet<String>();

		tags.addAll(StandardFlowNotificationHandler.FLOW_TAG_SOURCE_FACTORY.getTagsSet());
		tags.addAll(StandardFlowNotificationHandler.FLOWINSTANCE_TAG_SOURCE_FACTORY.getTagsSet());
		tags.addAll(StandardFlowNotificationHandler.STATUS_TAG_SOURCE_FACTORY.getTagsSet());
		tags.addAll(StandardFlowNotificationHandler.CONTACT_TAG_SOURCE_FACTORY.getTagsSet());
		tags.addAll(StandardFlowNotificationHandler.MANAGER_TAG_SOURCE_FACTORY.getTagsSet());
		tags.add("$flowInstance.url");
		tags.add("$flowInstance.messagesUrl");
		tags.add("$flowInstance.notesUrl");

		return tags;
	}

	public static Set<String> getGlobalTags() {

		Set<String> tags = new LinkedHashSet<String>();

		tags.addAll(StandardFlowNotificationHandler.FLOW_TAG_SOURCE_FACTORY.getTagsSet());
		tags.addAll(StandardFlowNotificationHandler.FLOWINSTANCE_TAG_SOURCE_FACTORY.getTagsSet());
		tags.addAll(StandardFlowNotificationHandler.STATUS_TAG_SOURCE_FACTORY.getTagsSet());
		tags.addAll(StandardFlowNotificationHandler.CONTACT_TAG_SOURCE_FACTORY.getTagsSet());
		tags.add("$flowInstance.url");
		tags.add("$flowInstance.messagesUrl");
		tags.add("$flowInstance.notesUrl");

		return tags;
	}

	public static Set<String> getSigningPartyTags() {

		Set<String> tags = new LinkedHashSet<String>();

		tags.addAll(StandardFlowNotificationHandler.FLOW_TAG_SOURCE_FACTORY.getTagsSet());
		tags.addAll(StandardFlowNotificationHandler.FLOWINSTANCE_TAG_SOURCE_FACTORY.getTagsSet());
		tags.addAll(StandardFlowNotificationHandler.STATUS_TAG_SOURCE_FACTORY.getTagsSet());
		tags.addAll(StandardFlowNotificationHandler.CONTACT_TAG_SOURCE_FACTORY.getTagsSet());
		tags.addAll(StandardFlowNotificationHandler.SIGNING_PARTY_TAG_SOURCE_FACTORY.getTagsSet());
		tags.add("$flowInstance.url");
		tags.add("$flowInstance.messagesUrl");
		tags.add("$flowInstance.notesUrl");

		return tags;
	}

	public String getFlowInstanceSubmittedManagerEmailSubject() {

		return flowInstanceSubmittedManagerEmailSubject;
	}

	public String getFlowInstanceSubmittedManagerEmailMessage() {

		return flowInstanceSubmittedManagerEmailMessage;
	}

	public String getStatusChangedUserSMS() {

		return statusChangedUserSMS;
	}
}
