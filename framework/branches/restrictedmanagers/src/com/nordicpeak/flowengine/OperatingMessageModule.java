package com.nordicpeak.flowengine;

import it.sauronsoftware.cron4j.Scheduler;

import java.io.IOException;
import java.nio.charset.Charset;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.apache.log4j.Level;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import se.unlogic.cron4jutils.CronStringValidator;
import se.unlogic.hierarchy.core.annotations.CheckboxSettingDescriptor;
import se.unlogic.hierarchy.core.annotations.InstanceManagerDependency;
import se.unlogic.hierarchy.core.annotations.ModuleSetting;
import se.unlogic.hierarchy.core.annotations.TextAreaSettingDescriptor;
import se.unlogic.hierarchy.core.annotations.TextFieldSettingDescriptor;
import se.unlogic.hierarchy.core.annotations.WebPublic;
import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.hierarchy.core.enums.SystemStatus;
import se.unlogic.hierarchy.core.exceptions.URINotFoundException;
import se.unlogic.hierarchy.core.interfaces.ForegroundModuleResponse;
import se.unlogic.hierarchy.core.interfaces.SectionInterface;
import se.unlogic.hierarchy.core.interfaces.listeners.SystemStartupListener;
import se.unlogic.hierarchy.core.interfaces.modules.descriptors.ForegroundModuleDescriptor;
import se.unlogic.hierarchy.core.utils.CRUDCallback;
import se.unlogic.hierarchy.core.utils.ModuleUtils;
import se.unlogic.hierarchy.foregroundmodules.AnnotatedForegroundModule;
import se.unlogic.openhierarchy.foregroundmodules.siteprofile.SiteProfileFilterModule;
import se.unlogic.standardutils.collections.CollectionUtils;
import se.unlogic.standardutils.dao.AnnotatedDAO;
import se.unlogic.standardutils.dao.HighLevelQuery;
import se.unlogic.standardutils.dao.QueryOperators;
import se.unlogic.standardutils.dao.QueryParameterFactory;
import se.unlogic.standardutils.string.StringUtils;
import se.unlogic.standardutils.time.MillisecondTimeUnits;
import se.unlogic.standardutils.time.TimeUtils;
import se.unlogic.standardutils.validation.StringIntegerValidator;
import se.unlogic.standardutils.xml.XMLParser;
import se.unlogic.standardutils.xml.XMLUtils;
import se.unlogic.webutils.http.HTTPUtils;
import se.unlogic.webutils.http.RequestUtils;
import se.unlogic.webutils.http.SimpleRequest;
import se.unlogic.webutils.http.URIParser;

import com.nordicpeak.flowengine.beans.ExternalOperatingMessage;
import com.nordicpeak.flowengine.beans.ExternalOperatingMessageSource;
import com.nordicpeak.flowengine.beans.OperatingMessage;
import com.nordicpeak.flowengine.cruds.OperatingMessageCRUD;
import com.nordicpeak.flowengine.dao.FlowEngineDAOFactory;
import com.nordicpeak.flowengine.enums.OperatingMessageType;
import com.nordicpeak.flowengine.interfaces.OperatingStatus;
import com.nordicpeak.flowengine.validators.OperatingMessageSubscriptionValidator;

public class OperatingMessageModule extends AnnotatedForegroundModule implements CRUDCallback<User>, Runnable, SystemStartupListener {
	
	
	private OperatingMessageCRUD messageCRUD;
	
	private AnnotatedDAO<OperatingMessage> operatingMessageDAO;
	
	private CopyOnWriteArraySet<OperatingMessage> internalOperatingMessageCache;
	private CopyOnWriteArrayList<ExternalOperatingMessage> externalOperatingMessageCache;
	
	private QueryParameterFactory<OperatingMessage, Timestamp> endTimeParameterFactory;
	
	@ModuleSetting
	@CheckboxSettingDescriptor(name = "Enable site profile support", description = "Controls if site profile support is enabled")
	protected boolean enableSiteProfileSupport;
	
	@ModuleSetting(allowsNull = true)
	@TextAreaSettingDescriptor(name = "Subscriptions", description = "name:http|https://url", formatValidator = OperatingMessageSubscriptionValidator.class)
	protected List<String> subscriptions;
	
	@ModuleSetting
	@TextFieldSettingDescriptor(name = "Subscriptions update interval", description = "How often this module should update external operating messages (specified in crontab format)", required = true, formatValidator = CronStringValidator.class)
	private String subscriptionUpdateInterval = "*/5 * * * *";
	
	@ModuleSetting
	@TextFieldSettingDescriptor(name = "Connection Timeout", description = "Connection timeout in seconds", formatValidator = StringIntegerValidator.class, required = true)
	protected Integer connectionTimeout = 5;
	
	@ModuleSetting
	@TextFieldSettingDescriptor(name = "Read Timeout", description = "Read timeout in seconds", formatValidator = StringIntegerValidator.class, required = true)
	protected Integer readTimeout = 5;
	
	@InstanceManagerDependency(required = true)
	private FlowAdminModule flowAdminModule;
	
	private Scheduler scheduler;
	private String scheduleID;
	private List<ExternalOperatingMessageSource> externalMessageSources = new ArrayList<ExternalOperatingMessageSource>();
	
	@Override
	public void init(ForegroundModuleDescriptor moduleDescriptor, SectionInterface sectionInterface, DataSource dataSource) throws Exception {
		
		super.init(moduleDescriptor, sectionInterface, dataSource);
		
		if (!systemInterface.getInstanceHandler().addInstance(OperatingMessageModule.class, this)) {
			
			throw new RuntimeException("Unable to register module in global instance handler using key " + OperatingMessageModule.class.getSimpleName() + ", another instance is already registered using this key.");
		}
		
		cacheComingOperatingMessages();
		initScheduler();
		
		if (systemInterface.getSystemStatus() == SystemStatus.STARTED) {
			systemStarted();
			
		} else if (systemInterface.getSystemStatus() == SystemStatus.STARTING) {
			systemInterface.addSystemStartupListener(this);
		}
	}
	
	@Override
	public void systemStarted() throws Exception {
		
		new Thread(this, moduleDescriptor.getName() + " Initial Caching").start();;
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
				moduleDescriptor.getMutableSettingHandler().setSetting("source." + source.getName(), "true");
			}
		}
		
		if (settingsMissing) {
			moduleDescriptor.saveSettings(systemInterface);
		}
		
		externalMessageSources.clear();
		
		if (externalOperatingMessageCache != null) {
			
			externalOperatingMessageCache.clear();
		}
		
		if (ModuleUtils.checkRequiredModuleSettings(moduleDescriptor, this, systemInterface, Level.ERROR)) {
			
			if (!CollectionUtils.isEmpty(subscriptions)) {
				for (String subscription : subscriptions) {
					
					String[] splits = subscription.split("(?<!https?):");
					
					String name = splits[0];
					String url = splits[1];
					
					externalMessageSources.add(new ExternalOperatingMessageSource(name, url, moduleDescriptor.getMutableSettingHandler().getPrimitiveBoolean("source." + name)));
				}
			}
		}
		
		super.moduleConfigured();
	}
	
	@Override
	public void unload() throws Exception {
		
		stopScheduler();
		
		systemInterface.getInstanceHandler().removeInstance(OperatingMessageModule.class, this);
		
		super.unload();
	}
	
	@Override
	protected void createDAOs(DataSource dataSource) throws Exception {
		
		super.createDAOs(dataSource);
		
		FlowEngineDAOFactory daoFactory = new FlowEngineDAOFactory(dataSource, systemInterface.getUserHandler(), systemInterface.getGroupHandler());
		
		operatingMessageDAO = daoFactory.getOperatingMessageDAO();
		
		endTimeParameterFactory = operatingMessageDAO.getParamFactory("endTime", Timestamp.class);
		
		messageCRUD = new OperatingMessageCRUD(operatingMessageDAO.getAdvancedWrapper("messageID", Integer.class), this);
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
	
	public OperatingStatus getOperatingStatus(Integer flowFamilyID, boolean manager) {
		
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
		
		return getOperatingStatus(null, manager);
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
		
		scheduler = new Scheduler();
		
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
		
//		log.info("Caching external operating messages..");
		
		List<ExternalOperatingMessage> operatingMessages = new ArrayList<ExternalOperatingMessage>();
		
		for (ExternalOperatingMessageSource source : externalMessageSources) {
			
			try {
				SimpleRequest listRequest = new SimpleRequest(source.getURL());

				listRequest.setConnectionTimeout(connectionTimeout * MillisecondTimeUnits.SECOND);
				listRequest.setReadTimeout(readTimeout * MillisecondTimeUnits.SECOND);

				String response = HTTPUtils.sendHTTPGetRequest(listRequest, Charset.forName("ISO-8859-1")).getValue();
				
				if (!StringUtils.isEmpty(response)) {
					
					try {
						XMLParser parser = new XMLParser(XMLUtils.parseXML(response, false, false));
						
						XMLParser item = parser.getNode("/Document/OperatingMessage");
						
						if (item != null) {
							
							OperatingMessageType type = OperatingMessageType.valueOf(item.getString("Type"));
							String message = item.getString("Text");
							
							operatingMessages.add(new ExternalOperatingMessage(source, type, message));
						}
						
					} catch (Exception e) {
						
						log.warn("Error parsing alternatives XML for " + source, e);
					}
				}
				
			} catch (Exception e) {
				log.warn("Error getting alternatives for " + source, e);
			}
		}
		
		if (operatingMessages.size() > 0) {
			
			log.info("Cached " + operatingMessages.size() + " external operating messages");
		}
		
		externalOperatingMessageCache = new CopyOnWriteArrayList<ExternalOperatingMessage>(operatingMessages);
	}
	
	@WebPublic(requireLogin = true)
	public ForegroundModuleResponse reloadExternal(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws URINotFoundException {
		
		if (user.isAdmin()) {
			
			cacheExternalOperatingMessages();
			return null;
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
	
}