package com.nordicpeak.flowengine;

import java.io.IOException;
import java.nio.charset.Charset;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CopyOnWriteArraySet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;

import org.apache.log4j.Level;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import se.unlogic.cron4jutils.CronStringValidator;
import se.unlogic.emailutils.framework.EmailUtils;
import se.unlogic.emailutils.framework.SimpleEmail;
import se.unlogic.hierarchy.core.annotations.CheckboxSettingDescriptor;
import se.unlogic.hierarchy.core.annotations.EnumDropDownSettingDescriptor;
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
import se.unlogic.hierarchy.core.beans.SimpleForegroundModuleResponse;
import se.unlogic.hierarchy.core.beans.SimpleViewFragment;
import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.hierarchy.core.enums.ResponseType;
import se.unlogic.hierarchy.core.enums.SystemStatus;
import se.unlogic.hierarchy.core.exceptions.URINotFoundException;
import se.unlogic.hierarchy.core.interfaces.ForegroundModuleResponse;
import se.unlogic.hierarchy.core.interfaces.SectionInterface;
import se.unlogic.hierarchy.core.interfaces.ViewFragment;
import se.unlogic.hierarchy.core.interfaces.listeners.ServerStartupListener;
import se.unlogic.hierarchy.core.interfaces.modules.descriptors.ForegroundModuleDescriptor;
import se.unlogic.hierarchy.core.utils.CRUDCallback;
import se.unlogic.hierarchy.core.utils.HierarchyAnnotatedDAOFactory;
import se.unlogic.hierarchy.core.utils.ModuleUtils;
import se.unlogic.hierarchy.core.utils.ModuleViewFragmentTransformer;
import se.unlogic.hierarchy.core.utils.ViewFragmentModule;
import se.unlogic.hierarchy.core.utils.usergrouplist.UserGroupListConnector;
import se.unlogic.hierarchy.foregroundmodules.AnnotatedForegroundModule;
import se.unlogic.openhierarchy.foregroundmodules.siteprofile.SiteProfileFilterModule;
import se.unlogic.standardutils.collections.CollectionUtils;
import se.unlogic.standardutils.dao.AdvancedAnnotatedDAOWrapper;
import se.unlogic.standardutils.dao.AnnotatedDAO;
import se.unlogic.standardutils.dao.HighLevelQuery;
import se.unlogic.standardutils.dao.QueryOperators;
import se.unlogic.standardutils.dao.QueryParameterFactory;
import se.unlogic.standardutils.string.SingleTagSource;
import se.unlogic.standardutils.string.StringUtils;
import se.unlogic.standardutils.string.TagReplacer;
import se.unlogic.standardutils.time.MillisecondTimeUnits;
import se.unlogic.standardutils.time.TimeUtils;
import se.unlogic.standardutils.validation.StringIntegerValidator;
import se.unlogic.standardutils.validation.ValidationError;
import se.unlogic.standardutils.validation.ValidationException;
import se.unlogic.standardutils.xml.XMLParser;
import se.unlogic.standardutils.xml.XMLUtils;
import se.unlogic.webutils.http.HTTPUtils;
import se.unlogic.webutils.http.RequestUtils;
import se.unlogic.webutils.http.SimpleRequest;
import se.unlogic.webutils.http.URIParser;
import se.unlogic.webutils.populators.annotated.AnnotatedRequestPopulator;

import com.nordicpeak.flowengine.beans.ExternalOperatingMessage;
import com.nordicpeak.flowengine.beans.ExternalOperatingMessageSource;
import com.nordicpeak.flowengine.beans.Flow;
import com.nordicpeak.flowengine.beans.FlowAdminExtensionShowView;
import com.nordicpeak.flowengine.beans.OperatingMessage;
import com.nordicpeak.flowengine.beans.OperatingMessageNotificationSettings;
import com.nordicpeak.flowengine.cruds.OperatingMessageCRUD;
import com.nordicpeak.flowengine.cruds.OperatingMessageFragmentExtensionCRUD;
import com.nordicpeak.flowengine.dao.FlowEngineDAOFactory;
import com.nordicpeak.flowengine.enums.OperatingMessageType;
import com.nordicpeak.flowengine.interfaces.FlowAdminFragmentExtensionViewProvider;
import com.nordicpeak.flowengine.interfaces.FlowNotificationHandler;
import com.nordicpeak.flowengine.interfaces.OperatingStatus;
import com.nordicpeak.flowengine.validators.OperatingMessageSubscriptionValidator;

import it.sauronsoftware.cron4j.Scheduler;

public class OperatingMessageModule extends AnnotatedForegroundModule implements CRUDCallback<User>, Runnable, ServerStartupListener, FlowAdminFragmentExtensionViewProvider, ViewFragmentModule<ForegroundModuleDescriptor> {

	private static final AnnotatedRequestPopulator<OperatingMessageNotificationSettings> EXTERNAL_SETTINGS_POPULATOR = new AnnotatedRequestPopulator<OperatingMessageNotificationSettings>(OperatingMessageNotificationSettings.class);

	@XSLVariable(prefix = "java.")
	private String adminExtensionViewTitle = "Operating message settings";
	
	@ModuleSetting
	@XSLVariable(prefix = "java.")
	@TextFieldSettingDescriptor(name = "New external operating message email subject", description = "The subject of emails sent to the selected users when a new external operating message is found", required = true)
	private String newExternalOperatingMessageEmailSubject;

	@ModuleSetting
	@XSLVariable(prefix = "java.")
	@HTMLEditorSettingDescriptor(name = "New external operating message email message", description = "The subject of emails sent to the selected users when a new external operating message is found", required = true)
	private String newExternalOperatingMessageEmailMessage;

	@ModuleSetting
	@XSLVariable(prefix = "java.")
	@TextFieldSettingDescriptor(name = "Removed external operating message email subject", description = "The subject of emails sent to the selected users when a external operating message is removed", required = true)
	private String removedExternalOperatingMessageEmailSubject;

	@ModuleSetting
	@XSLVariable(prefix = "java.")
	@HTMLEditorSettingDescriptor(name = "Removed external operating message email message", description = "The subject of emails sent to the selected users when a external operating message is removed", required = true)
	private String removedExternalOperatingMessageEmailMessage;

	@ModuleSetting
	@CheckboxSettingDescriptor(name = "Enable site profile support", description = "Controls if site profile support is enabled")
	protected boolean enableSiteProfileSupport;

	@ModuleSetting(allowsNull = true)
	@TextAreaSettingDescriptor(name = "Subscriptions", description = "name:http|https://url#encoding (encoding is optional)", formatValidator = OperatingMessageSubscriptionValidator.class)
	protected List<String> subscriptions;

	@ModuleSetting
	@TextFieldSettingDescriptor(name = "Default subscription file encoding", description = "The encoding to use when parsing subscriptions which have no encoding set")
	protected String subscriptionFileEncoding = "ISO-8859-1";

	@ModuleSetting
	@TextFieldSettingDescriptor(name = "Subscriptions update interval", description = "How often this module should update external operating messages (specified in crontab format)", required = true, formatValidator = CronStringValidator.class)
	private String subscriptionUpdateInterval = "*/5 * * * *";

	@ModuleSetting(allowsNull = true)
	@GroupMultiListSettingDescriptor(name = "Subscriber groups", description = "Groups allowed to subscribe to notifications (if not set this setting defaults to the groups set on the module)")
	protected List<Integer> subscriberGroupIDs;
	
	@ModuleSetting
	@TextFieldSettingDescriptor(name = "Connection Timeout", description = "Connection timeout in seconds", formatValidator = StringIntegerValidator.class, required = true)
	protected Integer connectionTimeout = 5;

	@ModuleSetting
	@TextFieldSettingDescriptor(name = "Read Timeout", description = "Read timeout in seconds", formatValidator = StringIntegerValidator.class, required = true)
	protected Integer readTimeout = 5;
	
	@ModuleSetting
	@CheckboxSettingDescriptor(name = "Enable fragment XML debug", description = "Enables debugging of fragment XML")
	private boolean debugFragmentXML;
	
	@ModuleSetting(allowsNull = true)
	@EnumDropDownSettingDescriptor(name = "Force message type in extension", description = "Force a operating message type when editing from flow admin extension")
	private OperatingMessageType forceOperatingMessageTypeInViewFragmentExtension;

	private OperatingMessageCRUD messageCRUD;
	private OperatingMessageFragmentExtensionCRUD messageViewFragmentCRUD;

	private AnnotatedDAO<OperatingMessage> operatingMessageDAO;
	private AnnotatedDAO<OperatingMessageNotificationSettings> externalSettingsDAO;

	private QueryParameterFactory<OperatingMessage, Timestamp> endTimeParameterFactory;
	private QueryParameterFactory<OperatingMessage, Boolean> operatingMessageGlobalParamFactory;
	
	private ModuleViewFragmentTransformer<ForegroundModuleDescriptor> viewFragmentTransformer;

	private CopyOnWriteArraySet<OperatingMessage> internalOperatingMessageCache;
	private List<ExternalOperatingMessage> externalOperatingMessageCache;

	private FlowAdminModule flowAdminModule;

	@InstanceManagerDependency
	private FlowNotificationHandler flowNotificationHandler;

	private Scheduler scheduler;
	private String scheduleID;
	private List<ExternalOperatingMessageSource> externalMessageSources = new ArrayList<ExternalOperatingMessageSource>();

	protected UserGroupListConnector userGroupListConnector;

	@Override
	public void init(ForegroundModuleDescriptor moduleDescriptor, SectionInterface sectionInterface, DataSource dataSource) throws Exception {

		viewFragmentTransformer = new ModuleViewFragmentTransformer<ForegroundModuleDescriptor>(sectionInterface.getForegroundModuleXSLTCache(), this, sectionInterface.getSystemInterface().getEncoding());

		userGroupListConnector = new UserGroupListConnector(sectionInterface.getSystemInterface());
		
		super.init(moduleDescriptor, sectionInterface, dataSource);

		if (!systemInterface.getInstanceHandler().addInstance(OperatingMessageModule.class, this)) {

			throw new RuntimeException("Unable to register module in global instance handler using key " + OperatingMessageModule.class.getSimpleName() + ", another instance is already registered using this key.");
		}

		cacheComingOperatingMessages();

		if (systemInterface.getSystemStatus() != SystemStatus.STARTED) {
			
			systemInterface.addServerStartupListener(this);
		}
	}
	
	@Override
	protected void createDAOs(DataSource dataSource) throws Exception {

		super.createDAOs(dataSource);

		HierarchyAnnotatedDAOFactory daoFactory = new HierarchyAnnotatedDAOFactory(dataSource, systemInterface.getUserHandler(), systemInterface.getGroupHandler(), false, true, false);

		externalSettingsDAO = daoFactory.getDAO(OperatingMessageNotificationSettings.class);

		FlowEngineDAOFactory flowDaoFactory = new FlowEngineDAOFactory(dataSource, systemInterface.getUserHandler(), systemInterface.getGroupHandler());

		operatingMessageDAO = flowDaoFactory.getOperatingMessageDAO();
		operatingMessageGlobalParamFactory = operatingMessageDAO.getParamFactory("global", boolean.class);

		endTimeParameterFactory = operatingMessageDAO.getParamFactory("endTime", Timestamp.class);

		AdvancedAnnotatedDAOWrapper<OperatingMessage, Integer> operatingMessageDAOWrapper = operatingMessageDAO.getAdvancedWrapper("messageID", Integer.class);
		
		messageCRUD = new OperatingMessageCRUD(operatingMessageDAOWrapper, this);
		messageViewFragmentCRUD = new OperatingMessageFragmentExtensionCRUD(operatingMessageDAOWrapper, this);
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
	public void serverStarted() throws Exception {

		new Thread(this, moduleDescriptor.getName() + " Initial Caching").start();
		
		initScheduler();
	}

	@Override
	public void update(ForegroundModuleDescriptor descriptor, DataSource dataSource) throws Exception {

		super.update(descriptor, dataSource);

		scheduler.reschedule(scheduleID, subscriptionUpdateInterval);
		cacheExternalOperatingMessages();
	}

	@Override
	protected void moduleConfigured() throws Exception {

		// Resave old enabled sources
		boolean settingsMissing = false;

		for (ExternalOperatingMessageSource source : externalMessageSources) {

			if (source.isEnabled() && !moduleDescriptor.getMutableSettingHandler().isSet("source." + source.getName())) {

				settingsMissing = true;
				moduleDescriptor.getMutableSettingHandler().setSetting("source." + source.getName(), "false");
			}
		}

		if (settingsMissing) {
			moduleDescriptor.saveSettings(systemInterface);
		}

		externalMessageSources.clear();

		if (externalOperatingMessageCache != null) {

			externalOperatingMessageCache = null;
		}

		if (ModuleUtils.checkRequiredModuleSettings(moduleDescriptor, this, systemInterface, Level.ERROR)) {

			if (!CollectionUtils.isEmpty(subscriptions)) {
				
				for (String subscription : subscriptions) {

					String[] splits = subscription.split(":", 2);

					String name = splits[0];
					String url = splits[1];
					
					String encoding;
					
					if(url.contains("#")) {
						
						encoding = StringUtils.substringAfter(url, "#");
						url = StringUtils.substringBefore(url, "#");
						
					}else {
						
						encoding = this.subscriptionFileEncoding;
					}

					externalMessageSources.add(new ExternalOperatingMessageSource(name, url, encoding, moduleDescriptor.getMutableSettingHandler().getPrimitiveBoolean("source." + name)));
				}
			}
		}

		viewFragmentTransformer.setDebugXML(debugFragmentXML);
		viewFragmentTransformer.modifyScriptsAndLinks(true, null);
		
		if(this.subscriberGroupIDs == null) {
			
			subscriberGroupIDs = (List<Integer>) moduleDescriptor.getAllowedGroupIDs();
		}
		
		userGroupListConnector.setUserGroupFilter(subscriberGroupIDs);
		
		super.moduleConfigured();
	}

	@Override
	public void unload() throws Exception {

		stopScheduler();

		systemInterface.getInstanceHandler().removeInstance(OperatingMessageModule.class, this);

		super.unload();
	}

	private void cacheComingOperatingMessages() throws SQLException {

		log.info("Caching coming operating messages");

		HighLevelQuery<OperatingMessage> query = new HighLevelQuery<OperatingMessage>();

		Timestamp currentTimestamp = TimeUtils.getCurrentTimestamp();

		query.addParameter(endTimeParameterFactory.getParameter(currentTimestamp, QueryOperators.BIGGER_THAN));

		List<OperatingMessage> operatingMessages = operatingMessageDAO.getAll(query);

		if (operatingMessages != null) {

			internalOperatingMessageCache = new CopyOnWriteArraySet<OperatingMessage>(operatingMessages);

			return;
		}

		internalOperatingMessageCache = new CopyOnWriteArraySet<OperatingMessage>();
	}

	@Override
	public ForegroundModuleResponse defaultMethod(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Throwable {

		return messageCRUD.list(req, res, user, uriParser, null);
	}

	@WebPublic
	public ForegroundModuleResponse add(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Throwable {

		return messageCRUD.add(req, res, user, uriParser);
	}

	@WebPublic
	public ForegroundModuleResponse update(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Throwable {

		return messageCRUD.update(req, res, user, uriParser);
	}

	@WebPublic
	public ForegroundModuleResponse delete(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Throwable {

		return messageCRUD.delete(req, res, user, uriParser);
	}

	@Override
	public Document createDocument(HttpServletRequest req, URIParser uriParser, User user) {

		Document doc = XMLUtils.createDomDocument();
		Element documentElement = doc.createElement("Document");
		documentElement.appendChild(RequestUtils.getRequestInfoAsXML(doc, req, uriParser));
		documentElement.appendChild(this.moduleDescriptor.toXML(doc));

		if (enableSiteProfileSupport) {
			XMLUtils.appendNewElement(doc, documentElement, "enableSiteProfileSupport", "true");
		}

		doc.appendChild(documentElement);
		return doc;
	}

	@Override
	public String getTitlePrefix() {

		return moduleDescriptor.getName();
	}

	public FlowAdminModule getFlowAdminModule() {

		return flowAdminModule;
	}

	public OperatingStatus getOperatingStatus(Integer flowFamilyID, boolean manager, boolean submittedFlowInstance) {

		List<OperatingMessage> pastMessages = null;

		Timestamp currentTime = TimeUtils.getCurrentTimestamp();

		OperatingStatus operatingStatus = null;

		for (OperatingMessage operatingMessage : internalOperatingMessageCache) {

			if (enableSiteProfileSupport) {

				if (operatingMessage.getProfileIDs() != null && SiteProfileFilterModule.getCurrentProfile() != null && !operatingMessage.getProfileIDs().contains(SiteProfileFilterModule.getCurrentProfile().getProfileID())) {

					//Message is set for specific profiles, but current profile is not one of them. Message should not be shown.
					continue;
				}
			}

			if (operatingMessage.getStartTime().before(currentTime) && operatingMessage.getEndTime().after(currentTime)) {

				if (manager && operatingMessage.allowsManagingOfInstances()) {

					continue;
				}

				if (!manager && submittedFlowInstance && operatingMessage.allowsUserHandlingOfSubmittedInstances()) {

					continue;
				}

				if (operatingStatus == null && operatingMessage.isGlobal()) {

					operatingStatus = operatingMessage;

				} else if ((operatingMessage.getFlowFamilyIDs() != null && flowFamilyID != null && operatingMessage.getFlowFamilyIDs().contains(flowFamilyID))) {

					operatingStatus = operatingMessage;

					break;
				}

			} else if (!operatingMessage.getEndTime().after(currentTime)) {

				pastMessages = CollectionUtils.addAndInstantiateIfNeeded(pastMessages, operatingMessage);
			}
		}

		if (pastMessages != null) {

			internalOperatingMessageCache.removeAll(pastMessages);
		}

		if (operatingStatus == null && externalOperatingMessageCache != null) {

			for (ExternalOperatingMessage operatingMessage : externalOperatingMessageCache) {

				if (!operatingMessage.isDisabled()) {

					operatingStatus = operatingMessage;
					break;
				}
			}
		}

		return operatingStatus;
	}

	public OperatingStatus getGlobalOperatingStatus(boolean manager) {

		return getOperatingStatus(null, manager, false);
	}

	public void addOrUpdateOperatingMessage(OperatingMessage operatingMessage) {

		internalOperatingMessageCache.remove(operatingMessage);

		if (operatingMessage.getEndTime().after(TimeUtils.getCurrentTimestamp())) {

			internalOperatingMessageCache.add(operatingMessage);
		}
	}

	public void deleteOperatingMessage(OperatingMessage operatingMessage) {

		internalOperatingMessageCache.remove(operatingMessage);
	}

	protected synchronized void initScheduler() {

		if (scheduler != null) {

			log.warn("Invalid state: scheduler not null");
			stopScheduler();
		}

		scheduler = new Scheduler(systemInterface.getApplicationName() + " - " + moduleDescriptor.toString());
		scheduler.setDaemon(true);

		scheduleID = scheduler.schedule(subscriptionUpdateInterval, this);
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
	public void run() {

		try {
			cacheExternalOperatingMessages();

		} catch (Throwable t) {
			log.error("Error caching external operating messages", t);
		}
	}

	private void cacheExternalOperatingMessages() {

		List<ExternalOperatingMessage> newExternalOperatingMessages = new ArrayList<ExternalOperatingMessage>();

		for (ExternalOperatingMessageSource source : externalMessageSources) {

			if(systemInterface.getSystemStatus() != SystemStatus.STARTED) {
				
				return;
			}
			
			if(!source.isEnabled()) {
				
				continue;
			}
			
			try {
				SimpleRequest listRequest = new SimpleRequest(source.getURL());

				listRequest.setConnectionTimeout(connectionTimeout * MillisecondTimeUnits.SECOND);
				listRequest.setReadTimeout(readTimeout * MillisecondTimeUnits.SECOND);
				String response = HTTPUtils.sendHTTPGetRequest(listRequest, Charset.forName(source.getEncoding())).getValue();

				if (!StringUtils.isEmpty(response)) {

					try {
						XMLParser parser = new XMLParser(XMLUtils.parseXML(response, false, false));

						XMLParser item = parser.getNode("/Document/OperatingMessage", true);

						if (item != null) {

							OperatingMessageType type = OperatingMessageType.valueOf(item.getString("Type"));
							String message = item.getString("Text");

							newExternalOperatingMessages.add(new ExternalOperatingMessage(source, type, message));
						}

					} catch (Exception e) {

						log.warn("Error parsing XML from source " + source, e);
					}
				}

			} catch (Exception e) {
				log.warn("Error getting alternatives for " + source, e);
			}
		}

		if (newExternalOperatingMessages.size() > 0 && log.isDebugEnabled()) {

			log.debug("Cached " + newExternalOperatingMessages.size() + " external operating messages");
		}

		if(newExternalOperatingMessages.isEmpty() && CollectionUtils.isEmpty(this.externalOperatingMessageCache)) {
			
			//No new or old messages, nothing to do
			return;
		}
		
		List<ExternalOperatingMessage> oldExternalOperatingMessageCache = this.externalOperatingMessageCache;

		//Cache messages
		this.externalOperatingMessageCache = newExternalOperatingMessages;

		sendNotifications(externalOperatingMessageCache, oldExternalOperatingMessageCache);
	}

	private void sendNotifications(List<ExternalOperatingMessage> newOperatingMessages, Collection<ExternalOperatingMessage> oldOperatingMessages) {

		try {
			OperatingMessageNotificationSettings notificationSettings = getNotificationSettings(true);

			if (notificationSettings == null || notificationSettings.getNotificationUsers() == null || flowNotificationHandler == null) {
				return;
			}

			String emailSenderName = flowNotificationHandler.getEmailSenderName(null);
			String emailSenderAddress = flowNotificationHandler.getEmailSenderAddress(null);

			// Send notifications for removed operating messages
			if (oldOperatingMessages != null) {
				
				for (ExternalOperatingMessage oldExternalOperationMessage : oldOperatingMessages) {

					if (!newOperatingMessages.contains(oldExternalOperationMessage)) {

						TagReplacer tagReplacer = new TagReplacer();
						tagReplacer.addTagSource(new SingleTagSource("$name", oldExternalOperationMessage.getSource().getName()));
						tagReplacer.addTagSource(new SingleTagSource("$message", oldExternalOperationMessage.getMessage()));

						for (User user : notificationSettings.getNotificationUsers()) {

							if (user.getEmail() == null) {

								continue;
							}

							SimpleEmail email = new SimpleEmail(systemInterface.getEncoding());

							try {
								email.addRecipient(user.getEmail());
								email.setMessageContentType(SimpleEmail.HTML);
								email.setSenderName(emailSenderName);
								email.setSenderAddress(emailSenderAddress);
								email.setSubject(tagReplacer.replace(removedExternalOperatingMessageEmailSubject));
								email.setMessage(EmailUtils.addMessageBody(tagReplacer.replace(removedExternalOperatingMessageEmailMessage)));

								systemInterface.getEmailHandler().send(email);

							} catch (Exception e) {

								log.error("Error generating/sending email " + email, e);
							}
						}
					}
				}
			}

			// Send notifications for new operating messages
			for (ExternalOperatingMessage newExternalOperationMessage : newOperatingMessages) {

				if (oldOperatingMessages == null || !oldOperatingMessages.contains(newExternalOperationMessage)) {

					TagReplacer tagReplacer = new TagReplacer();
					tagReplacer.addTagSource(new SingleTagSource("$name", newExternalOperationMessage.getSource().getName()));
					tagReplacer.addTagSource(new SingleTagSource("$message", newExternalOperationMessage.getMessage()));

					for (User user : notificationSettings.getNotificationUsers()) {

						if (user.getEmail() == null) {

							continue;
						}

						SimpleEmail email = new SimpleEmail(systemInterface.getEncoding());

						try {
							email.addRecipient(user.getEmail());
							email.setMessageContentType(SimpleEmail.HTML);
							email.setSenderName(emailSenderName);
							email.setSenderAddress(emailSenderAddress);
							email.setSubject(tagReplacer.replace(newExternalOperatingMessageEmailSubject));
							email.setMessage(EmailUtils.addMessageBody(tagReplacer.replace(newExternalOperatingMessageEmailMessage)));

							systemInterface.getEmailHandler().send(email);

						} catch (Exception e) {

							log.error("Error generating/sending email " + email, e);
						}
					}
				}
			}

		} catch (Exception e) {

			log.error("Error notifying users about changes to external operating messages", e);
		}
	}

	@WebPublic(requireLogin = true)
	public ForegroundModuleResponse reloadExternal(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws URINotFoundException {

		if (user.isAdmin()) {

			cacheExternalOperatingMessages();

			return new SimpleForegroundModuleResponse("done", getDefaultBreadcrumb());
		}

		throw new URINotFoundException(uriParser);
	}

	@WebPublic
	public ForegroundModuleResponse enableExternalSource(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws URINotFoundException, IOException, SQLException {

		if (uriParser.size() == 3) {

			String name = uriParser.get(2);

			for (ExternalOperatingMessageSource source : externalMessageSources) {

				if (source.getName().equals(name)) {

					source.setEnabled(true);
					moduleDescriptor.getMutableSettingHandler().setSetting("source." + name, "true");
					moduleDescriptor.saveSettings(systemInterface);
				}
			}

			redirectToDefaultMethod(req, res);
			return null;
		}

		throw new URINotFoundException(uriParser);
	}

	@WebPublic
	public ForegroundModuleResponse disableExternalSource(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws URINotFoundException, IOException, SQLException {

		if (uriParser.size() == 3) {

			String name = uriParser.get(2);

			for (ExternalOperatingMessageSource source : externalMessageSources) {

				if (source.getName().equals(name)) {

					source.setEnabled(false);
					moduleDescriptor.getMutableSettingHandler().setSetting("source." + name, "false");
					moduleDescriptor.saveSettings(systemInterface);
				}
			}

			redirectToDefaultMethod(req, res);
			return null;
		}

		throw new URINotFoundException(uriParser);
	}

	public List<ExternalOperatingMessage> getExternalOperatingMessages() {

		return externalOperatingMessageCache;
	}

	public List<ExternalOperatingMessageSource> getExternalOperatingMessageSources() {

		return externalMessageSources;
	}

	@WebPublic(alias = "users")
	public ForegroundModuleResponse getUsers(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Throwable {

		return userGroupListConnector.getUsers(req, res, user, uriParser);
	}

	public OperatingMessageNotificationSettings getNotificationSettings(boolean withUsers) throws SQLException {

		HighLevelQuery<OperatingMessageNotificationSettings> query = new HighLevelQuery<OperatingMessageNotificationSettings>();

		OperatingMessageNotificationSettings notificationSettings = externalSettingsDAO.get(query);

		if (notificationSettings != null && notificationSettings.getNotificationUserIDs() != null && withUsers) {
			notificationSettings.setNotificationUsers(systemInterface.getUserHandler().getUsers(notificationSettings.getNotificationUserIDs(), false, true));
		}

		return notificationSettings;
	}

	@WebPublic(toLowerCase = true)
	public ForegroundModuleResponse updateNotificationSettings(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Exception {

		OperatingMessageNotificationSettings notificationSettings = getNotificationSettings(true);

		ValidationException validationException = null;

		if (req.getMethod().equalsIgnoreCase("POST")) {

			try {
				notificationSettings = EXTERNAL_SETTINGS_POPULATOR.populate(notificationSettings, req);

				log.info("User " + user + " updating notification settings");

				externalSettingsDAO.addOrUpdate(notificationSettings, null);

				redirectToDefaultMethod(req, res);
				return null;

			} catch (ValidationException e) {

				validationException = e;
			}
		}

		log.info("User " + user + " viewing notification settings form");

		Document doc = createDocument(req, uriParser, user);

		Element settingsElement = doc.createElement("UpdateNotificationSettings");
		doc.getDocumentElement().appendChild(settingsElement);

		if (notificationSettings != null) {

			settingsElement.appendChild(notificationSettings.toXML(doc));
		}

		if (validationException != null) {

			settingsElement.appendChild(validationException.toXML(doc));
			settingsElement.appendChild(RequestUtils.getRequestParameters(req, doc));
		}

		return new SimpleForegroundModuleResponse(doc, moduleDescriptor.getName(), getDefaultBreadcrumb());
	}

	@Override
	public int getPriority() {

		return 0;
	}
	
	@Override
	public String getExtensionViewTitle() {
		return adminExtensionViewTitle;
	}

	@Override
	public String getExtensionViewLinkName() {
		return "operatingmessages";
	}

	@Override
	public FlowAdminExtensionShowView getShowView(String extensionRequestURL, Flow flow, HttpServletRequest req, User user, URIParser uriParser) throws TransformerConfigurationException, TransformerException, SQLException {

		Document doc = createDocument(req, uriParser, user);

		Element showViewElement = doc.createElement("FlowOverviewExtension");
		doc.getDocumentElement().appendChild(showViewElement);

		boolean enabled = appendFlowOverviewExtension(doc, showViewElement, extensionRequestURL, flow, req, user, uriParser);

		return new FlowAdminExtensionShowView(viewFragmentTransformer.createViewFragment(doc, true), enabled);
	}

	public ForegroundModuleResponse processRequestError(String extensionRequestURL, Flow flow, HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser, List<ValidationError> validationErrors) throws SQLException {

		Document doc = createDocument(req, uriParser, user);

		Element showViewElement = doc.createElement("FlowOverviewExtensionError");
		doc.getDocumentElement().appendChild(showViewElement);

		appendFlowOverviewExtension(doc, showViewElement, extensionRequestURL, flow, req, user, uriParser);

		XMLUtils.appendNewElement(doc, showViewElement, "Title", getExtensionViewTitle());

		XMLUtils.append(doc, showViewElement, "ValidationErrors", validationErrors);

		return new SimpleForegroundModuleResponse(doc, getDefaultBreadcrumb());
	}

	private boolean appendFlowOverviewExtension(Document doc, Element showViewElement, String extensionRequestURL, Flow flow, HttpServletRequest req, User user, URIParser uriParser) throws SQLException {

		showViewElement.appendChild(flow.toXML(doc));
		XMLUtils.appendNewElement(doc, showViewElement, "extensionRequestURL", extensionRequestURL);

		HighLevelQuery<OperatingMessage> getQuery = new HighLevelQuery<>();
		getQuery.addParameter(operatingMessageGlobalParamFactory.getParameter(false));

		List<OperatingMessage> operatingMessages = operatingMessageDAO.getAll(getQuery);

		if (operatingMessages != null) {

			Element operatingMessagesElement = XMLUtils.appendNewElement(doc, showViewElement, "OperatingMessages");

			for (OperatingMessage operatingMessage : operatingMessages) {

				if (operatingMessage.getFlowFamilyIDs() != null && operatingMessage.getFlowFamilyIDs().contains(flow.getFlowFamily().getFlowFamilyID())) {

					Element operatingMessageElement = operatingMessage.toXML(doc);

					if (operatingMessage.getFlowFamilyIDs().size() > 1) {

						XMLUtils.appendNewElement(doc, operatingMessageElement, "ReadOnly");
					}

					operatingMessagesElement.appendChild(operatingMessageElement);
				}
			}
		}

		return !CollectionUtils.isEmpty(operatingMessages);
	}

	@Override
	public ViewFragment processRequest(String extensionRequestURL, Flow flow, HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Exception {

		String method = uriParser.get(4);

		req.setAttribute("extensionRequestURL", extensionRequestURL);
		req.setAttribute("flow", flow);

		//TODO use methodMap with a new FlowAdminFragmentExtensionViewProviderProcessRequest annotation

		if ("add".equals(method)) {

			return getViewFragmentResponse(messageViewFragmentCRUD.add(req, res, user, uriParser));

		} else if ("update".equals(method)) {

			return getViewFragmentResponse(messageViewFragmentCRUD.update(req, res, user, uriParser));

		} else if ("delete".equals(method)) {

			return getViewFragmentResponse(messageViewFragmentCRUD.delete(req, res, user, uriParser));

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

	public OperatingMessageType getForceOperatingMessageTypeInViewFragmentExtension() {
		return forceOperatingMessageTypeInViewFragmentExtension;
	}

}
