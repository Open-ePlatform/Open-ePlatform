package com.nordicpeak.flowengine.internalnotifications;

import java.io.Writer;
import java.lang.reflect.Field;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import se.unlogic.cron4jutils.CronStringValidator;
import se.unlogic.hierarchy.core.annotations.CheckboxSettingDescriptor;
import se.unlogic.hierarchy.core.annotations.ModuleSetting;
import se.unlogic.hierarchy.core.annotations.TextFieldSettingDescriptor;
import se.unlogic.hierarchy.core.annotations.WebPublic;
import se.unlogic.hierarchy.core.beans.LinkTag;
import se.unlogic.hierarchy.core.beans.ScriptTag;
import se.unlogic.hierarchy.core.beans.SimpleForegroundModuleResponse;
import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.hierarchy.core.enums.CRUDAction;
import se.unlogic.hierarchy.core.enums.EventSource;
import se.unlogic.hierarchy.core.events.CRUDEvent;
import se.unlogic.hierarchy.core.interfaces.ForegroundModuleResponse;
import se.unlogic.hierarchy.core.interfaces.SectionInterface;
import se.unlogic.hierarchy.core.interfaces.ViewFragment;
import se.unlogic.hierarchy.core.interfaces.modules.descriptors.ForegroundModuleDescriptor;
import se.unlogic.hierarchy.core.utils.ModuleViewFragmentTransformer;
import se.unlogic.hierarchy.core.utils.ViewFragmentModule;
import se.unlogic.hierarchy.foregroundmodules.AnnotatedForegroundModule;
import se.unlogic.standardutils.collections.CollectionUtils;
import se.unlogic.standardutils.dao.AnnotatedDAO;
import se.unlogic.standardutils.dao.HighLevelQuery;
import se.unlogic.standardutils.dao.LowLevelQuery;
import se.unlogic.standardutils.dao.QueryOperators;
import se.unlogic.standardutils.dao.QueryParameterFactory;
import se.unlogic.standardutils.dao.SimpleAnnotatedDAOFactory;
import se.unlogic.standardutils.db.DBUtils;
import se.unlogic.standardutils.string.StringUtils;
import se.unlogic.standardutils.time.TimeUtils;
import se.unlogic.standardutils.validation.PositiveStringIntegerValidator;
import se.unlogic.standardutils.xml.XMLUtils;
import se.unlogic.webutils.http.RequestUtils;
import se.unlogic.webutils.http.URIParser;

import com.nordicpeak.flowengine.beans.Flow;
import com.nordicpeak.flowengine.beans.FlowInstance;
import com.nordicpeak.flowengine.dao.FlowEngineDAOFactory;
import com.nordicpeak.flowengine.internalnotifications.beans.StoredNotification;
import com.nordicpeak.flowengine.internalnotifications.beans.StoredNotificationAttribute;
import com.nordicpeak.flowengine.internalnotifications.interfaces.Notification;
import com.nordicpeak.flowengine.internalnotifications.interfaces.NotificationHandler;
import com.nordicpeak.flowengine.internalnotifications.interfaces.NotificationSource;

import it.sauronsoftware.cron4j.Scheduler;

public class InternalNotificationHandlerModule extends AnnotatedForegroundModule implements NotificationHandler, Runnable, ViewFragmentModule<ForegroundModuleDescriptor> {
	
	protected static final List<Field> FLOW_INSTANCE_EXCLUDED_FIELDS = Arrays.asList(new Field[] { FlowInstance.POSTER_FIELD, FlowInstance.EDITOR_FIELD, Flow.ICON_FILE_NAME_FIELD, Flow.DESCRIPTION_SHORT_FIELD, Flow.DESCRIPTION_LONG_FIELD, Flow.SUBMITTED_MESSAGE_FIELD, Flow.HIDE_EXTERNAL_MESSAGES_FIELD, Flow.HIDE_EXTERNAL_MESSAGE_ATTACHMENTS_FIELD, Flow.READ_RECEIPTS_ENABLED_FIELD, Flow.READ_RECEIPTS_ENABLED_BY_DEFAULT_FIELD, Flow.HIDE_INTERNAL_MESSAGES_FIELD, Flow.HIDE_FROM_OVERVIEW_FIELD, Flow.FLOW_FORMS_FIELD, Flow.HIDE_SUBMIT_STEP_TEXT_FIELD, Flow.SHOW_SUBMIT_SURVEY_FIELD, Flow.REQUIRES_SIGNING_FIELD, Flow.REQUIRE_AUTHENTICATION_FIELD, Flow.USE_PREVIEW_FIELD, Flow.PUBLISH_DATE_FIELD });
	
	@ModuleSetting
	@TextFieldSettingDescriptor(name = "Notification lifetime in days", description = "How many days notifications should be stored before being deleted", required = true, formatValidator = PositiveStringIntegerValidator.class)
	private Integer notificationLifetime = 31;
	
	@ModuleSetting
	@TextFieldSettingDescriptor(name = "Notification seentime in minutes", description = "How many minutes notifications should remain 'hot' after initial viewing", required = true, formatValidator = PositiveStringIntegerValidator.class)
	private Integer notificationSeentime = 30;
	
	@ModuleSetting
	@TextFieldSettingDescriptor(name = "Notification delete interval", description = "How often this module should check for old notifications to delete (specified in crontab format)", required = true, formatValidator = CronStringValidator.class)
	private String notificationDeleteInterval = "0 * * * *";
	
	@ModuleSetting
	@TextFieldSettingDescriptor(name = "JSON notification count", description = "How many notifications to include in JSON requests", required = true, formatValidator = PositiveStringIntegerValidator.class)
	private Integer notificationCount = 5;
	
	@ModuleSetting
	@CheckboxSettingDescriptor(name = "Enable fragment XML debug", description = "Enables debugging of fragment XML")
	private boolean debugFragmentXML = false;
	
	private Scheduler scheduler;
	private ModuleViewFragmentTransformer<ForegroundModuleDescriptor> moduleViewFragmentTransformer;
	
	protected FlowEngineDAOFactory daoFactory;
	
	private AnnotatedDAO<StoredNotification> notificationDAO;
	private QueryParameterFactory<StoredNotification, Timestamp> addedParamFactory;
	private QueryParameterFactory<StoredNotification, Timestamp> seenParamFactory;
	private QueryParameterFactory<StoredNotification, Integer> externaIDParamFactory;
	private QueryParameterFactory<StoredNotification, Integer> userIDParamFactory;
	private QueryParameterFactory<StoredNotification, Integer> sourceModuleParamFactory;
	private QueryParameterFactory<StoredNotification, String> typeParamFactory;
	private QueryParameterFactory<StoredNotification, Integer> flowInstanceIDParamFactory;
	private QueryParameterFactory<FlowInstance, Integer> flowInstanceID2ParamFactory;
	
	private ConcurrentHashMap<Integer, NotificationSource> notificationSourceMap = new ConcurrentHashMap<Integer, NotificationSource>();
	
	@Override
	public void init(ForegroundModuleDescriptor descriptor, SectionInterface sectionInterface, DataSource dataSource) throws Exception {
		
		super.init(descriptor, sectionInterface, dataSource);
		
		if (!systemInterface.getInstanceHandler().addInstance(NotificationHandler.class, this)) {
			
			throw new RuntimeException("Unable to register " + this.moduleDescriptor + " in global instance handler using key " + NotificationHandler.class.getSimpleName() + ", another instance is already registered using this key.");
		}
		
		moduleViewFragmentTransformer = new ModuleViewFragmentTransformer<ForegroundModuleDescriptor>(sectionInterface.getForegroundModuleXSLTCache(), this, systemInterface.getEncoding());
		
		initScheduler();
	}
	
	//TODO listen on crudevent remove flow instance
	
	@Override
	public void update(ForegroundModuleDescriptor descriptor, DataSource dataSource) throws Exception {
		
		super.update(descriptor, dataSource);
		
		stopScheduler();
		initScheduler();
	}
	
	@Override
	public void unload() throws Exception {
		
		stopScheduler();
		
		systemInterface.getInstanceHandler().removeInstance(NotificationHandler.class, this);
		
		super.unload();
	}
	
	@Override
	protected void createDAOs(DataSource dataSource) throws Exception {
		
		daoFactory = new FlowEngineDAOFactory(dataSource, systemInterface.getUserHandler(), systemInterface.getGroupHandler());
		
		notificationDAO = new SimpleAnnotatedDAOFactory(dataSource).getDAO(StoredNotification.class);
		
		addedParamFactory = notificationDAO.getParamFactory("added", Timestamp.class);
		seenParamFactory = notificationDAO.getParamFactory("seen", Timestamp.class);
		externaIDParamFactory = notificationDAO.getParamFactory("externalNotificationID", Integer.class);
		userIDParamFactory = notificationDAO.getParamFactory("userID", Integer.class);
		sourceModuleParamFactory = notificationDAO.getParamFactory("sourceModuleID", Integer.class);
		typeParamFactory = notificationDAO.getParamFactory("notificationType", String.class);
		flowInstanceIDParamFactory = notificationDAO.getParamFactory("flowInstanceID", Integer.class);
		
		flowInstanceID2ParamFactory = daoFactory.getFlowInstanceDAO().getParamFactory("flowInstanceID", Integer.class);
	}
	
	@Override
	public void addNotification(int flowInstanceID, int userID, int sourceModuleID, String notificationType, Integer externalNotificationID, String title, Map<String, String> attributes) throws SQLException {
		
		StoredNotification storedNotification = new StoredNotification();
		
		storedNotification.setFlowInstanceID(flowInstanceID);
		storedNotification.setUserID(userID);
		storedNotification.setSourceModuleID(sourceModuleID);
		storedNotification.setNotificationType(notificationType);
		storedNotification.setExternalNotificationID(externalNotificationID);
		storedNotification.setTitle(title);
		storedNotification.setAdded(TimeUtils.getCurrentTimestamp());
		
		if (attributes != null) {
			
			List<StoredNotificationAttribute> attributesList = new ArrayList<StoredNotificationAttribute>(attributes.size());
			
			for (Entry<String, String> entry : attributes.entrySet()) {
				
				attributesList.add(new StoredNotificationAttribute(entry.getKey(), entry.getValue()));
			}
			
			storedNotification.setAttributes(attributesList);
		}
		
		log.info("Adding notification " + storedNotification);
		
		this.notificationDAO.add(storedNotification);
	}
	
	@Override
	public void deleteNotifications(int userID) throws SQLException {
		
		log.info("Deleting notifications for userID " + userID);
		
		HighLevelQuery<StoredNotification> query = new HighLevelQuery<StoredNotification>();
		
		query.addParameter(userIDParamFactory.getParameter(userID));
		
		notificationDAO.delete(query);
	}
	
	@Override
	public void deleteNotifications(int sourceModuleID, int externalNotificationID, String notificationType) throws SQLException {
		
		log.info("Deleting notifications for sourceModuleID " + sourceModuleID + ", externalNotificationID " + externalNotificationID + ", notificationType " + notificationType);
		
		HighLevelQuery<StoredNotification> query = new HighLevelQuery<StoredNotification>();
		
		query.addParameter(sourceModuleParamFactory.getParameter(sourceModuleID));
		query.addParameter(externaIDParamFactory.getParameter(externalNotificationID));
		
		if (notificationType != null) {
			
			query.addParameter(typeParamFactory.getParameter(notificationType));
		}
		
		notificationDAO.delete(query);
	}
	
	@Override
	public void deleteNotifications(int sourceModuleID, int flowInstanceID, User user, String notificationType) throws SQLException {
		
		log.info("Deleting notifications for sourceModuleID " + sourceModuleID + ", flowInstanceID " + flowInstanceID + ", user " + user + ", notificationType " + notificationType);
		
		HighLevelQuery<StoredNotification> query = new HighLevelQuery<StoredNotification>();
		
		query.addParameter(sourceModuleParamFactory.getParameter(sourceModuleID));
		query.addParameter(flowInstanceIDParamFactory.getParameter(flowInstanceID));
		
		if (user != null) {
			
			query.addParameter(userIDParamFactory.getParameter(user.getUserID()));
		}
		
		if (notificationType != null) {
			
			query.addParameter(typeParamFactory.getParameter(notificationType));
		}
		
		notificationDAO.delete(query);
	}
	
	@Override
	public int getUnreadCount(int userID) throws SQLException {
		
		HighLevelQuery<StoredNotification> query = new HighLevelQuery<StoredNotification>();
		
		query.addParameter(userIDParamFactory.getParameter(userID));
		query.addParameter(seenParamFactory.getIsNullParameter());
		
		Integer count = notificationDAO.getCount(query);
		
		if (count == null) {
			return 0;
		}
		
		return count;
	}
	
	@Override
	public Notification getNotification(int userID, int sourceModuleID, Integer externalNotificationID, String notificationType) throws SQLException {
		
		HighLevelQuery<StoredNotification> query = new HighLevelQuery<StoredNotification>();
		
		query.addParameter(userIDParamFactory.getParameter(userID));
		
		query.addParameter(sourceModuleParamFactory.getParameter(sourceModuleID));
		
		if (externalNotificationID != null) {
			
			query.addParameter(externaIDParamFactory.getParameter(externalNotificationID));
		}
		
		if (notificationType != null) {
			
			query.addParameter(typeParamFactory.getParameter(notificationType));
		}
		
		return notificationDAO.get(query);
	}
	
	@Override
	public List<StoredNotification> getNotificationsForModule(int sourceModuleID, Integer externalNotificationID, String notificationType, Boolean seenStatus) throws SQLException {
		
		HighLevelQuery<StoredNotification> query = new HighLevelQuery<StoredNotification>();
		
		query.addParameter(sourceModuleParamFactory.getParameter(sourceModuleID));
		
		if (externalNotificationID != null) {
			
			query.addParameter(externaIDParamFactory.getParameter(externalNotificationID));
		}
		
		if (notificationType != null) {
			
			query.addParameter(typeParamFactory.getParameter(notificationType));
		}
		
		if (seenStatus != null) {
			
			if (seenStatus) {
				
				query.addParameter(seenParamFactory.getIsNotNullParameter());
				
			} else {
				
				query.addParameter(seenParamFactory.getIsNullParameter());
			}
		}
		
		return notificationDAO.getAll(query);
	}
	
	@Override
	public List<StoredNotification> getNotificationsForUser(int userID, Integer count, Timestamp breakpoint, boolean showAll) throws SQLException {
		
		StringBuilder sql = new StringBuilder("SELECT * FROM " + notificationDAO.getTableName() + " WHERE userID = ?");
		
		if (!showAll) {
			
			sql.append(" AND (" + seenParamFactory.getColumnName() + " IS NULL OR " + seenParamFactory.getColumnName() + " >= ? )");
		}
		
		if (breakpoint != null) {
			
			sql.append(" AND " + addedParamFactory.getColumnName() + " >= ?");
		}
		
		sql.append(" ORDER BY " + addedParamFactory.getColumnName() + " DESC");
		
		if (count != null) {
			
			sql.append(" LIMIT ?");
		}
		
		LowLevelQuery<StoredNotification> query = new LowLevelQuery<StoredNotification>(sql.toString());
		
		query.addParameter(userID);
		
		if (!showAll) {
			
			Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.MINUTE, -notificationSeentime);
			
			Timestamp seenCutoff = new Timestamp(calendar.getTimeInMillis());
			
			query.addParameter(seenCutoff);
		}
		
		if (breakpoint != null) {
			
			query.addParameter(breakpoint);
		}
		
		if (count != null) {
			
			query.addParameter(count);
		}
		
		List<StoredNotification> storedNotifications = notificationDAO.getAll(query);
		
		if (storedNotifications == null) {
			
			return null;
		}
		
		return storedNotifications;
	}
	
	public LinkedHashMap<Integer, FlowInstance> setNotificationMetadata(List<StoredNotification> notifications, String fullContextPath) throws SQLException {
		
		if (notifications != null) {
			
			// Notifications are already sorted in most recent first. The flow instances will be in the same order.
			LinkedHashMap<Integer, FlowInstance> flowInstanceMap = new LinkedHashMap<Integer, FlowInstance>(notifications.size());
			
			for (Notification notification : notifications) {
				
				if (notification.getFlowInstanceID() != null && !flowInstanceMap.containsKey(notification.getFlowInstanceID())) {
					
					FlowInstance flowInstance = getFlowInstance(notification.getFlowInstanceID(), FLOW_INSTANCE_EXCLUDED_FIELDS, FlowInstance.FLOW_RELATION, FlowInstance.OWNERS_RELATION);
					
					if (flowInstance != null) {
						
						flowInstanceMap.put(flowInstance.getFlowInstanceID(), flowInstance);
					}
				}
			}
			
			for (Iterator<StoredNotification> it = notifications.iterator(); it.hasNext();) {
				
				StoredNotification notification = it.next();
				
				NotificationSource notificationSource = notificationSourceMap.get(notification.getSourceModuleID());
				
				if (notificationSource == null) {
					
					log.warn("Unable to get notification extras for notification " + notification + ", module not found.");
					it.remove();
					
				} else {
					
					try {
						notification.setNotificationExtra(notificationSource.getNotificationMetadata(notification, flowInstanceMap.get(notification.getFlowInstanceID()), fullContextPath));
						
					} catch (Exception e) {
						
						log.error("Error getting notification extras for notification " + notification + " from module " + notificationSource, e);
						it.remove();
					}
				}
			}
			
			return flowInstanceMap;
		}
		
		return null;
	}
	
	@Override
	public void markAsSeen(List<? extends Notification> notifications) throws SQLException {
		
		if (!CollectionUtils.isEmpty(notifications)) {
			
			if (notifications.get(0) instanceof StoredNotification) {
				
				@SuppressWarnings("unchecked")
				List<StoredNotification> storedNotifications = (List<StoredNotification>) notifications;
				
				LowLevelQuery<StoredNotification> query = new LowLevelQuery<StoredNotification>();
				
				query.setSql("UPDATE " + notificationDAO.getTableName() + " SET seen = ? WHERE notificationID IN (" + DBUtils.getPreparedStatementQuestionMarks(storedNotifications) + ")");
				
				query.addParameter(new Timestamp(System.currentTimeMillis()));
				
				for (StoredNotification notification : storedNotifications) {
					
					query.addParameter(notification.getNotificationID());
				}
				
				notificationDAO.update(query);
				
			} else {
				
				throw new UnsupportedOperationException("Only supports StoredNotification");
			}
		}
	}
	
	@Override
	public void run() {
		
		log.info("Deleting notifications older than " + notificationLifetime + " days...");
		
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, -notificationLifetime);
		
		HighLevelQuery<StoredNotification> query = new HighLevelQuery<StoredNotification>();
		
		query.addParameter(addedParamFactory.getParameter(new Timestamp(calendar.getTimeInMillis()), QueryOperators.SMALLER_THAN));
		
		try {
			Integer deleteCount = notificationDAO.delete(query);
			
			if (deleteCount > 0) {
				
				log.info("Deleted " + deleteCount + " notifications");
			}
			
		} catch (SQLException e) {
			
			log.error("Error deleting old notifications", e);
		}
	}
	
	protected synchronized void initScheduler() {
		
		scheduler = new Scheduler(systemInterface.getApplicationName() + " - " + moduleDescriptor.toString());
		scheduler.setDaemon(true);
		
		scheduler.schedule(this.notificationDeleteInterval, this);
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
	public ForegroundModuleResponse defaultMethod(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Throwable {
		
		log.info("User " + user + " listing all notifications");
		
		Document doc = createDocument(req, uriParser, user);
		Element documentElement = (Element) doc.getFirstChild();
		
		XMLUtils.appendNewElement(doc, doc.getDocumentElement(), "fullAlias", getFullAlias());
		
		Element listNotificationsElement = XMLUtils.appendNewElement(doc, documentElement, "ListNotifications");
		
		List<StoredNotification> notifications = getNotificationsForUser(user.getUserID(), null, null, true);
		
		if (notifications != null) {
			
			LinkedHashMap<Integer, FlowInstance> flowInstances = setNotificationMetadata(notifications, RequestUtils.getFullContextPathURL(req));
			
			appendNotifications(notifications, flowInstances.values(), doc, listNotificationsElement);
		}
		
		ForegroundModuleResponse response = new SimpleForegroundModuleResponse(doc);
		
		markAsSeen(notifications);
		
		return response;
	}
	
	@WebPublic(alias = "latest")
	public ForegroundModuleResponse getLatestNotifications(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Throwable {
		
		List<StoredNotification> notifications = getNotificationsForUser(user.getUserID(), notificationCount, null, false);
		
		res.setContentType("text/html");
		res.addHeader("notifications", "true");
		Writer writer = res.getWriter();
		
		Document doc = createDocument(req, uriParser, user);
		Element documentElement = (Element) doc.getFirstChild();
		
		XMLUtils.appendNewElement(doc, doc.getDocumentElement(), "fullAlias", getFullAlias());
		
		Element inlineNotificationsElement = XMLUtils.appendNewElement(doc, documentElement, "InlineNotifications");
		
		int unreadCount = getUnreadCount(user.getUserID());
		
		if (notifications != null) {
			
			LinkedHashMap<Integer, FlowInstance> flowInstances = setNotificationMetadata(notifications, RequestUtils.getFullContextPathURL(req));
			
			appendNotifications(notifications, flowInstances.values(), doc, inlineNotificationsElement);
			
			for (StoredNotification notification : notifications) {
				
				if (notification.getSeen() == null) {
					unreadCount--;
				}
			}
		}
		
		XMLUtils.appendNewElement(doc, inlineNotificationsElement, "UnreadCount", unreadCount);
		
		ViewFragment viewFragment = moduleViewFragmentTransformer.createViewFragment(doc);
		writer.write(viewFragment.getHTML());
		
		res.getWriter().flush();
		res.getWriter().close();
		
		markAsSeen(notifications);
		
		if (systemInterface.isModuleXMLDebug() && debugFragmentXML && !StringUtils.isEmpty(systemInterface.getModuleXMLDebugFile())) {
			
			log.debug("XML debug mode enabled, writing module XML to " + systemInterface.getModuleXMLDebugFile() + " for module " + getModuleDescriptor());
			
			try {
				XMLUtils.writeXMLFile(doc, systemInterface.getApplicationFileSystemPath() + "WEB-INF/" + systemInterface.getModuleXMLDebugFile(), true, systemInterface.getEncoding());
				
				log.debug("Finished writing module XML to " + systemInterface.getApplicationFileSystemPath() + "WEB-INF/" + systemInterface.getModuleXMLDebugFile());
				
			} catch (Exception e) {
				
				log.error("Error writing module XML to " + systemInterface.getApplicationFileSystemPath() + "WEB-INF/" + systemInterface.getModuleXMLDebugFile(), e);
			}
		}
		
		return null;
	}
	
	private void appendNotifications(List<? extends Notification> notifications, Collection<FlowInstance> flowInstances, Document doc, Element element) throws SQLException {
		
		XMLUtils.append(doc, element, "FlowInstances", flowInstances);
		XMLUtils.append(doc, element, "Notifications", notifications);
	}
	
	public Document createDocument(HttpServletRequest req, URIParser uriParser, User user) {
		
		Document doc = XMLUtils.createDomDocument();
		Element documentElement = doc.createElement("Document");
		documentElement.appendChild(RequestUtils.getRequestInfoAsXML(doc, req, uriParser));
		documentElement.appendChild(user.toXML(doc));
		
		doc.appendChild(documentElement);
		return doc;
	}
	
	@Override
	public String getModuleURL(HttpServletRequest req) {
		
		return req.getContextPath() + this.getFullAlias();
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
	
	@Override
	public void addNotificationSource(NotificationSource notificationSource) {
		
		if(notificationSourceMap.putIfAbsent(notificationSource.getModuleDescriptor().getModuleID(), notificationSource) == null){
			
			log.info("NotificationSource " + notificationSource + " registered");
		
		}else{
			
			log.warn("NotificationSource " + notificationSource + " is already registered");
		}
	}
	
	@Override
	public boolean removeNotificationSource(NotificationSource notificationSource) {
		
		boolean removed = notificationSourceMap.remove(notificationSource.getModuleDescriptor().getModuleID()) != null;
		
		if (removed) {
			
			log.info("NotificationSource " + notificationSource + " unregistered");
		}
		
		return removed;
	}
	
	public FlowInstance getFlowInstance(int flowInstanceID, List<Field> excludedFields, Field... relations) throws SQLException {
		
		HighLevelQuery<FlowInstance> query = new HighLevelQuery<FlowInstance>(relations);
		
		if (excludedFields != null) {
			query.addExcludedFields(excludedFields);
		}
		
		query.addParameter(flowInstanceID2ParamFactory.getParameter(flowInstanceID));
		
		return daoFactory.getFlowInstanceDAO().get(query);
	}
	
	@Override
	public void sendNotificationToFlowInstanceOwners(NotificationSource notificationSource, Integer flowInstanceID, String title, User sendingUser, String type, Map<String, String> attributes) throws SQLException {
		
		FlowInstance flowInstance = getFlowInstance(flowInstanceID, null, FlowInstance.OWNERS_RELATION);
		
		if (flowInstance != null && !CollectionUtils.isEmpty(flowInstance.getOwners())) {
			
			for (User owner : flowInstance.getOwners()) {
				
				Integer sendingUserID;
				
				if(sendingUser != null){
					
					sendingUserID = sendingUser.getUserID();
				
				}else{
				
					sendingUserID = null;
				}
				
				addNotification(flowInstanceID, owner.getUserID(), notificationSource.getModuleDescriptor().getModuleID(), type, sendingUserID, title, attributes);
			}
		}
	}
	
	@Override
	public void sendNotificationToFlowInstanceManagers(NotificationSource notificationSource, Integer flowInstanceID, String title, User sendingUser, String type, Map<String, String> attributes) throws SQLException {
		
		FlowInstance flowInstance = getFlowInstance(flowInstanceID, null, FlowInstance.MANAGERS_RELATION);
		
		if (flowInstance != null && !CollectionUtils.isEmpty(flowInstance.getManagers())) {
			
			for (User manager : flowInstance.getManagers()) {
				
				addNotification(flowInstanceID, manager.getUserID(), notificationSource.getModuleDescriptor().getModuleID(), type, sendingUser.getUserID(), title, attributes);
			}
		}
	}
	
	@se.unlogic.hierarchy.core.annotations.EventListener(channel = FlowInstance.class)
	public void processEvent(CRUDEvent<FlowInstance> event, EventSource source) {
		
		log.debug("Received crud event regarding " + event.getAction() + " of " + event.getBeans().size() + " beans with " + event.getBeanClass());
		
		if (event.getAction() == CRUDAction.DELETE) {
			
			//TODO wrap loop in transaction
			
			for (FlowInstance flowInstance : event.getBeans()) {
				
				try {
					log.info("Deleting notifications for deleted flowInstance " + flowInstance);
					
					HighLevelQuery<StoredNotification> query = new HighLevelQuery<StoredNotification>();
					
					query.addParameter(flowInstanceIDParamFactory.getParameter(flowInstance.getFlowInstanceID()));
					
					notificationDAO.delete(query);
					
				} catch (SQLException e) {
					
					log.error("Error removing notifications for flowInstanceID " + flowInstance.getFlowInstanceID(), e);
				}
			}
		}
	}
	
}
