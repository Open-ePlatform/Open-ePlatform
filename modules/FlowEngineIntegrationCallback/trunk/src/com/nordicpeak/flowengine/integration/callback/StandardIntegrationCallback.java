package com.nordicpeak.flowengine.integration.callback;

import java.lang.reflect.Field;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.jws.WebService;
import javax.xml.datatype.XMLGregorianCalendar;

import se.unlogic.hiearchy.foregroundmodules.jaxws.BaseWSModuleService;
import se.unlogic.hiearchy.foregroundmodules.jaxws.WSModuleCallback;
import se.unlogic.hiearchy.foregroundmodules.jaxws.WSModuleInstanceResolver;
import se.unlogic.hierarchy.core.annotations.CheckboxSettingDescriptor;
import se.unlogic.hierarchy.core.annotations.ModuleSetting;
import se.unlogic.hierarchy.core.beans.Group;
import se.unlogic.hierarchy.core.beans.SettingDescriptor;
import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.hierarchy.core.enums.CRUDAction;
import se.unlogic.hierarchy.core.enums.EventTarget;
import se.unlogic.hierarchy.core.events.CRUDEvent;
import se.unlogic.hierarchy.core.interfaces.settings.SettingProvider;
import se.unlogic.hierarchy.core.utils.ModuleUtils;
import se.unlogic.standardutils.collections.CollectionUtils;
import se.unlogic.standardutils.dao.HighLevelQuery;
import se.unlogic.standardutils.dao.QueryParameterFactory;
import se.unlogic.standardutils.dao.RelationQuery;
import se.unlogic.standardutils.dao.TransactionHandler;
import se.unlogic.standardutils.date.DateUtils;
import se.unlogic.standardutils.fileattachments.FileAttachment;
import se.unlogic.standardutils.fileattachments.FileAttachmentHandler;
import se.unlogic.standardutils.fileattachments.FileAttachmentUtils;
import se.unlogic.standardutils.reflection.ReflectionUtils;
import se.unlogic.standardutils.string.StringUtils;
import se.unlogic.standardutils.time.TimeUtils;

import com.nordicpeak.flowengine.Constants;
import com.nordicpeak.flowengine.FlowAdminModule;
import com.nordicpeak.flowengine.beans.ExternalMessage;
import com.nordicpeak.flowengine.beans.ExternalMessageAttachment;
import com.nordicpeak.flowengine.beans.Flow;
import com.nordicpeak.flowengine.beans.FlowFamily;
import com.nordicpeak.flowengine.beans.FlowInstance;
import com.nordicpeak.flowengine.beans.FlowInstanceEvent;
import com.nordicpeak.flowengine.beans.InternalMessage;
import com.nordicpeak.flowengine.beans.InternalMessageAttachment;
import com.nordicpeak.flowengine.beans.Status;
import com.nordicpeak.flowengine.dao.FlowEngineDAOFactory;
import com.nordicpeak.flowengine.enums.EventType;
import com.nordicpeak.flowengine.enums.SenderType;
import com.nordicpeak.flowengine.events.ExternalMessageAddedEvent;
import com.nordicpeak.flowengine.events.InternalMessageAddedEvent;
import com.nordicpeak.flowengine.events.ManagersChangedEvent;
import com.nordicpeak.flowengine.events.StatusChangedByManagerEvent;
import com.nordicpeak.flowengine.integration.callback.events.DeliveryConfirmationEvent;
import com.nordicpeak.flowengine.integration.callback.exceptions.AccessDenied;
import com.nordicpeak.flowengine.integration.callback.exceptions.AccessDeniedException;
import com.nordicpeak.flowengine.integration.callback.exceptions.FlowInstanceNotFound;
import com.nordicpeak.flowengine.integration.callback.exceptions.FlowInstanceNotFoundException;
import com.nordicpeak.flowengine.integration.callback.exceptions.StatusNotFound;
import com.nordicpeak.flowengine.integration.callback.exceptions.StatusNotFoundException;
import com.nordicpeak.flowengine.integration.callback.listeners.FieldInstanceListener;
import com.nordicpeak.flowengine.interfaces.ImmutableFlowInstance;
import com.nordicpeak.flowengine.utils.FlowEngineFileAttachmentUtils;
import com.nordicpeak.flowengine.utils.FlowFamilyUtils;
import com.nordicpeak.flowengine.utils.FlowInstanceUtils;

@WebService(endpointInterface = "com.nordicpeak.flowengine.integration.callback.IntegrationCallback", name = "IntegrationCallback", serviceName = "IntegrationCallback")
@WSModuleInstanceResolver
public class StandardIntegrationCallback extends BaseWSModuleService implements IntegrationCallback, SettingProvider {

	protected static final RelationQuery FLOW_INSTANCE_ATTRIBUTE_RELATION_QUERY = new RelationQuery(FlowInstance.ATTRIBUTES_RELATION);
	protected static final RelationQuery FLOW_INSTANCE_MANAGERS_RELATION_QUERY = new RelationQuery(FlowInstance.MANAGERS_RELATION, FlowInstance.MANAGER_GROUPS_RELATION);
	protected static final RelationQuery FLOW_INSTANCE_EVENT_ATTRIBUTE_RELATION_QUERY = new RelationQuery(FlowInstanceEvent.ATTRIBUTES_RELATION);
	
	private static final Field FLOW_ADMIN_MODULE_FIELD = ReflectionUtils.getField(StandardIntegrationCallback.class, "flowAdminModule");
	
	private static final Field FILE_ATTACHMENT_HANDLER_FIELD = ReflectionUtils.getField(StandardIntegrationCallback.class, "fileAttachmentHandler");

	@ModuleSetting
	@CheckboxSettingDescriptor(name = "Set user on events", description = "Set the API user on generated events")
	private boolean setUserOnEvents;
	
	@ModuleSetting
	@CheckboxSettingDescriptor(name = "Enable attribute prefix", description = "Enable the callback. prefix on set attributes")
	private boolean enableAttributePrefix = true;

	protected FlowAdminModule flowAdminModule;
	
	protected FileAttachmentHandler fileAttachmentHandler;

	private FlowEngineDAOFactory daoFactory;
	protected QueryParameterFactory<FlowInstance, Integer> flowInstanceIDParamFactory;
	private QueryParameterFactory<Status, Integer> statusIDParamFactory;
	private QueryParameterFactory<Status, String> statusNameParamFactory;
	private QueryParameterFactory<Status, Flow> statusFlowParamFactory;

	private FieldInstanceListener<FlowAdminModule> flowAdminModuleListener = new FieldInstanceListener<>(this, FLOW_ADMIN_MODULE_FIELD, true, null);
	
	private FieldInstanceListener<FileAttachmentHandler> fileAttachmentHandlerListener = new FieldInstanceListener<>(this, FILE_ATTACHMENT_HANDLER_FIELD, false, null);
	
	@Override
	public void init(WSModuleCallback callback) throws Exception {

		super.init(callback);

		daoFactory = new FlowEngineDAOFactory(callback.getDataSource(), callback.getSystemInterface().getUserHandler(), callback.getSystemInterface().getGroupHandler());

		flowInstanceIDParamFactory = daoFactory.getFlowInstanceDAO().getParamFactory("flowInstanceID", Integer.class);
		statusIDParamFactory = daoFactory.getStatusDAO().getParamFactory("statusID", Integer.class);
		statusNameParamFactory = daoFactory.getStatusDAO().getParamFactory("name", String.class);
		statusFlowParamFactory = daoFactory.getStatusDAO().getParamFactory("flow", Flow.class);

		ModuleUtils.setModuleSettings(this, BaseWSModuleService.class, callback.getModuleDescriptor().getMutableSettingHandler(), callback.getSystemInterface());

		callback.getSystemInterface().getInstanceHandler().addInstanceListener(FlowAdminModule.class, flowAdminModuleListener);
		callback.getSystemInterface().getInstanceHandler().addInstanceListener(FileAttachmentHandler.class, fileAttachmentHandlerListener);
	}

	@Override
	public void unload() throws Exception {

		callback.getSystemInterface().getInstanceHandler().removeInstanceListener(FlowAdminModule.class, flowAdminModuleListener);
		callback.getSystemInterface().getInstanceHandler().removeInstanceListener(FileAttachmentHandler.class, fileAttachmentHandlerListener);
		
		super.unload();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int setStatus(Integer flowInstanceID, ExternalID externalID, Integer statusID, String statusAlias, Principal principal) throws AccessDeniedException, FlowInstanceNotFoundException, StatusNotFoundException {

		try {

			checkDependencies();

			FlowInstance flowInstance = getFlowInstance(flowInstanceID, externalID);

			User principalUser = getPrincipalUser(principal);

			log.info("User " + callback.getUser() + " requested set status for flow instance " + flowInstance + " using principal " + principalUser);

			Status status = null;

			if (statusID != null) {

				status = getStatus(flowInstance.getFlow(), statusID);

			} else if (statusAlias != null) {

				status = getStatus(flowInstance.getFlow(), statusAlias);
			}

			if (status == null) {

				throw new StatusNotFoundException("Status with ID " + statusID + " not found for flow " + flowInstance.getFlow(), new StatusNotFound());

			} else if (flowInstance.getStatus().equals(status)) {

				log.info("Flow instance " + flowInstance + " already has the requested status of " + status + ", ignoring request from user " + callback.getUser());

				return 0;

			} else {

				log.info("User " + callback.getUser() + " changing status of instance " + flowInstance + " from " + flowInstance.getStatus() + " to " + status + " using principal " + principalUser);

				Status previousStatus = flowInstance.getStatus();

				flowInstance.setStatus(status);
				flowInstance.setLastStatusChange(TimeUtils.getCurrentTimestamp());

				try {
					this.daoFactory.getFlowInstanceDAO().update(flowInstance);

				} catch (SQLException e) {

					throw new RuntimeException(e);
				}

				FlowInstanceEvent flowInstanceEvent = addFlowInstanceEvent(flowInstance, EventType.STATUS_UPDATED, null, principalUser, null);

				callback.getSystemInterface().getEventHandler().sendEvent(FlowInstance.class, new CRUDEvent<>(CRUDAction.UPDATE, flowInstance), EventTarget.ALL);

				callback.getSystemInterface().getEventHandler().sendEvent(FlowInstance.class, new StatusChangedByManagerEvent(flowInstance, flowInstanceEvent, flowAdminModule.getSiteProfile(flowInstance), previousStatus, principalUser), EventTarget.ALL);

				return flowInstanceEvent.getEventID();
			}

		} catch (RuntimeException e) {

			log.error("Error changing status", e);

			throw e;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int addEvent(Integer flowInstanceID, ExternalID externalID, XMLGregorianCalendar date, String message, Principal principal) throws AccessDeniedException, FlowInstanceNotFoundException {

		try {
			checkDependencies();

			FlowInstance flowInstance = getFlowInstance(flowInstanceID, externalID);

			User principalUser = getPrincipalUser(principal);

			log.info("User " + callback.getUser() + " requested add event for flow instance " + flowInstance + " using principal " + principal);

			FlowInstanceEvent flowInstanceEvent = addFlowInstanceEvent(flowInstance, EventType.OTHER_EVENT, message, principalUser, TimeUtils.getTimeStamp(date));

			return flowInstanceEvent.getEventID();

		} catch (RuntimeException e) {

			log.error("Error adding event", e);

			throw e;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int addMessage(Integer flowInstanceID, ExternalID externalID, IntegrationMessage message, Principal principal) throws AccessDeniedException, FlowInstanceNotFoundException {

		try {
			checkDependencies();

			FlowInstance flowInstance = getFlowInstance(flowInstanceID, externalID);

			User principalUser = getPrincipalUser(principal);

			log.info("User " + callback.getUser() + " requested add message for flow instance " + flowInstance + " using principal " + principal);

			ExternalMessage externalMessage = getExternalMessage(message);

			externalMessage.setPoster(principalUser);
			externalMessage.setFlowInstance(flowInstance);
			externalMessage.setPostedByManager(true);

			TransactionHandler transactionHandler = null;
			
			List<FileAttachment> addedFileAttachments = null;
			
			try {
				transactionHandler = new TransactionHandler(callback.getDataSource());
				
				daoFactory.getExternalMessageDAO().add(externalMessage, transactionHandler , null);
				
				addedFileAttachments = FlowEngineFileAttachmentUtils.saveAttachmentData(fileAttachmentHandler, externalMessage);
				
				transactionHandler.commit();

			} catch (Throwable e) {

				FileAttachmentUtils.deleteFileAttachments(addedFileAttachments);
				
				throw new RuntimeException(e);
				
			} finally {
				
				TransactionHandler.autoClose(transactionHandler);
			}

			FlowInstanceEvent flowInstanceEvent = this.addFlowInstanceEvent(flowInstance, EventType.MANAGER_MESSAGE_SENT, null, principalUser, externalMessage.getAdded());

			callback.getSystemInterface().getEventHandler().sendEvent(FlowInstance.class, new ExternalMessageAddedEvent(flowInstance, flowInstanceEvent, flowAdminModule.getSiteProfile(flowInstance), externalMessage, SenderType.MANAGER), EventTarget.ALL);

			return externalMessage.getMessageID();

		} catch (RuntimeException e) {

			log.error("Error adding message", e);

			throw e;
		}
	}
	
	@Override
	public int addInternalMessage(Integer flowInstanceID, ExternalID internalID, IntegrationMessage message, Principal principal) throws AccessDeniedException, FlowInstanceNotFoundException {

		try {
			checkDependencies();

			FlowInstance flowInstance = getFlowInstance(flowInstanceID, internalID);

			User principalUser = getPrincipalUser(principal);

			log.info("User " + callback.getUser() + " requested add internal message for flow instance " + flowInstance + " using principal " + principal);

			//TODO
			
			InternalMessage internalMessage = getInternalMessage(message);

			internalMessage.setPoster(principalUser);
			internalMessage.setFlowInstance(flowInstance);

			TransactionHandler transactionHandler = null;
			
			List<FileAttachment> addedFileAttachments = null;
			
			try {
				transactionHandler = new TransactionHandler(callback.getDataSource());
				
				daoFactory.getInternalMessageDAO().add(internalMessage, transactionHandler, null);
				
				addedFileAttachments = FlowEngineFileAttachmentUtils.saveAttachmentData(fileAttachmentHandler, internalMessage);
				
				transactionHandler.commit();

			} catch (Throwable e) {

				FileAttachmentUtils.deleteFileAttachments(addedFileAttachments);
				
				throw new RuntimeException(e);
				
			} finally {
				
				TransactionHandler.autoClose(transactionHandler);
			}

			callback.getSystemInterface().getEventHandler().sendEvent(FlowInstance.class, new InternalMessageAddedEvent(flowInstance, flowAdminModule.getSiteProfile(flowInstance), internalMessage), EventTarget.ALL);

			return internalMessage.getMessageID();

		} catch (RuntimeException e) {

			log.error("Error adding message", e);

			throw e;
		}
	}	

	private ExternalMessage getExternalMessage(IntegrationMessage message) {

		validateIntegrationMessage(message);

		ExternalMessage externalMessage = new ExternalMessage();

		externalMessage.setAdded(TimeUtils.getTimeStamp(message.getAdded()));
		externalMessage.setMessage(message.getMessage());

		if (!CollectionUtils.isEmpty(message.getAttachments())) {

			ArrayList<ExternalMessageAttachment> externalMessageAttachments = new ArrayList<>(message.getAttachments().size());

			for (Attachment attachment : message.getAttachments()) {

				validateAttachment(attachment);

				try {
					externalMessageAttachments.add(FlowEngineFileAttachmentUtils.createAttachment(fileAttachmentHandler, attachment.getFilename(), attachment.getSize(), attachment.getEncodedData(), ExternalMessageAttachment.class));
					
				} catch (SQLException e) {

					throw new RuntimeException(e);
				}
			}

			externalMessage.setAttachments(externalMessageAttachments);
		}

		return externalMessage;
	}

	private InternalMessage getInternalMessage(IntegrationMessage message) {

		validateIntegrationMessage(message);

		InternalMessage internalMessage = new InternalMessage();

		internalMessage.setAdded(TimeUtils.getTimeStamp(message.getAdded()));
		internalMessage.setMessage(message.getMessage());

		if (!CollectionUtils.isEmpty(message.getAttachments())) {

			ArrayList<InternalMessageAttachment> internalMessageAttachments = new ArrayList<>(message.getAttachments().size());

			for (Attachment attachment : message.getAttachments()) {

				validateAttachment(attachment);

				try {
					internalMessageAttachments.add(FlowEngineFileAttachmentUtils.createAttachment(fileAttachmentHandler, attachment.getFilename(), attachment.getSize(), attachment.getEncodedData(), InternalMessageAttachment.class));
					
				} catch (SQLException e) {
					
					throw new RuntimeException(e);
				}
			}

			internalMessage.setAttachments(internalMessageAttachments);
		}

		return internalMessage;
	}
	
	protected void validateAttachment(Attachment attachment) {

		if (StringUtils.isEmpty(attachment.getFilename())) {

			throw new RuntimeException("Filename field cannot be null or empty");
		}

		if (attachment.getEncodedData() == null) {

			throw new NullPointerException("Encoded data field cannot be null");
		}

		if (attachment.getSize() <= 0) {

			throw new RuntimeException("Size cannot be < 0");
		}
	}

	protected void validateIntegrationMessage(IntegrationMessage message) {
		
		if (message == null) {

			throw new NullPointerException("Integration message cannot be null");
		}

		if (message.getAdded() == null) {

			throw new NullPointerException("Added field cannot be null");
		}

		if (StringUtils.isEmpty(message.getMessage())) {

			throw new RuntimeException("Message field cannot be null or empty");
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void confirmDelivery(int flowInstanceID, ExternalID externalID, boolean delivered, String logMessage) throws AccessDeniedException, FlowInstanceNotFoundException {

		try {
			FlowInstance flowInstance = getFlowInstance(flowInstanceID, externalID);

			log.info("User " + callback.getUser() + " requested add message for flow instance " + flowInstance + " with delived flag set to " + delivered + " and log message se to " + logMessage);

			if (delivered) {

				//Check if this flow instance has already been confirmed
				String wasDelivered = flowInstance.getAttributeHandler().getString(IntegrationCallbackConstants.DELIVERY_CONFIRMED_ATTRIBUTE);

				if (wasDelivered != null) {

					log.info("Duplicate delivery confirmation received from user " + callback.getUser() + " for flow instance " + flowInstance + ", ignoring confirmation");

					return;
				}

				//Set delivered flag
				flowInstance.getAttributeHandler().setAttribute(IntegrationCallbackConstants.DELIVERY_CONFIRMED_ATTRIBUTE, DateUtils.DATE_TIME_SECONDS_FORMATTER.format(new Date()));

				if (externalID != null) {

					if (StringUtils.isEmpty(externalID.getID())) {

						throw new RuntimeException("ID field of ExternalID cannot be empty");
					}

					if (StringUtils.isEmpty(externalID.getSystem())) {

						throw new RuntimeException("System field of ExternalID cannot be empty");
					}

					flowInstance.getAttributeHandler().setAttribute(Constants.FLOW_INSTANCE_EXTERNAL_ID_ATTRIBUTE, externalID.getID());
					flowInstance.getAttributeHandler().setAttribute(IntegrationCallbackConstants.EXTERNAL_SYSTEM_ATTRIBUTE, externalID.getSystem());
				}

				try {
					daoFactory.getFlowInstanceDAO().update(flowInstance, FLOW_INSTANCE_ATTRIBUTE_RELATION_QUERY);
				} catch (SQLException e) {
					throw new RuntimeException(e);
				}
			}

			callback.getSystemInterface().getEventHandler().sendEvent(IntegrationCallback.class, new DeliveryConfirmationEvent(flowInstance, delivered, logMessage), EventTarget.ALL);

		} catch (RuntimeException e) {

			log.error("Error confirming delivery", e);

			throw e;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void deleteInstance(Integer flowInstanceID, ExternalID externalID, String logMessage) throws AccessDeniedException, FlowInstanceNotFoundException {

		throw new AccessDeniedException("Deleting of flow instances is currently not supported", new AccessDenied());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int setManagers(Integer flowInstanceID, ExternalID externalID, List<Principal> managers, List<PrincipalGroup> managerGroups) throws AccessDeniedException, FlowInstanceNotFoundException {

		try {
			checkDependencies();

			FlowInstance flowInstance = getFlowInstance(flowInstanceID, externalID);

			log.info("User " + callback.getUser() + " requested set managers for flow instance " + flowInstance);

			String detailString;

			List<User> previousManagers = flowInstance.getManagers();
			List<Group> previousManagerGroups = flowInstance.getManagerGroups();

			if (!CollectionUtils.isEmpty(managers) || !CollectionUtils.isEmpty(managerGroups)) {

				List<User> allowedManagers = FlowFamilyUtils.getAllowedManagerUsers(flowInstance.getFlow().getFlowFamily(), callback.getSystemInterface().getUserHandler());
				List<Group> allowedManagerGroups = FlowFamilyUtils.getAllowedManagerGroups(flowInstance.getFlow().getFlowFamily(), callback.getSystemInterface().getGroupHandler());

				try {
					flowInstance.setManagers(filterSelectedManagerUsers(allowedManagers, managers));
					flowInstance.setManagerGroups(filterSelectedManagerGroups(allowedManagerGroups, managerGroups));

				} catch (AccessDeniedException e) {

					log.warn("Invalid users/groups when setting managers for flow instance " + flowInstanceID);
					throw e;
				}

				detailString = FlowInstanceUtils.getManagersString(flowInstance.getManagers(), flowInstance.getManagerGroups());

			} else {

				detailString = null; //TODO FlowInstanceAdminModule.noManagersSelected
				flowInstance.setManagers(null);
				flowInstance.setManagerGroups(null);
			}

			if (!CollectionUtils.equals(previousManagers, flowInstance.getManagers()) || !CollectionUtils.equals(previousManagerGroups, flowInstance.getManagerGroups())) {

				log.info("User " + callback.getUser() + " setting managers of instance " + flowInstance + " to " + detailString);

				try {
					daoFactory.getFlowInstanceDAO().update(flowInstance, FLOW_INSTANCE_MANAGERS_RELATION_QUERY);

				} catch (SQLException e) {

					throw new RuntimeException(e);
				}

				User eventUser;

				if (setUserOnEvents) {

					eventUser = callback.getUser();

				} else {

					eventUser = null;
				}

				FlowInstanceEvent flowInstanceEvent = addFlowInstanceEvent(flowInstance, EventType.MANAGERS_UPDATED, detailString, eventUser, null);

				callback.getSystemInterface().getEventHandler().sendEvent(FlowInstance.class, new CRUDEvent<>(CRUDAction.UPDATE, flowInstance), EventTarget.ALL);
				callback.getSystemInterface().getEventHandler().sendEvent(FlowInstance.class, new ManagersChangedEvent(flowInstance, flowInstanceEvent, flowAdminModule.getSiteProfile(flowInstance), previousManagers, previousManagerGroups, eventUser), EventTarget.ALL);

				return flowInstanceEvent.getEventID();

			} else {

				log.info("No change in managers detected in request from user " + callback.getUser() + " for flow instance " + flowInstance + ", ignoring request.");

				return 0;
			}

		} catch (RuntimeException e) {

			log.error("Error setting managers", e);

			throw e;
		}
	}
	
	@Override
	public GetManagersResponse getManagers(GetManagersRequest parameters) throws AccessDeniedException, FlowInstanceNotFoundException {

		try {
			checkDependencies();

			Integer flowInstanceID = null;
			ExternalID externalID = null;
			
			if(parameters != null) {
				
				flowInstanceID = parameters.getFlowInstanceID();
				externalID = parameters.getExternalID();
			}
			
			FlowInstance flowInstance = getFlowInstance(flowInstanceID, externalID);

			log.info("User " + callback.getUser() + " requested get managers for flow instance " + flowInstance);

			GetManagersResponse response = new GetManagersResponse();
			
			if(flowInstance.getManagers() != null) {
				
				for(User user : flowInstance.getManagers()) {
					
					Principal principal = new Principal();
					
					principal.setUserID(user.getUsername());
					principal.setName(user.getFirstname() + " " + user.getLastname());
					
					response.getManagers().add(principal);
				}
			}
			
			if(flowInstance.getManagerGroups() != null) {
				
				for(Group group : flowInstance.getManagerGroups()) {
					
					PrincipalGroup principalGroup = new PrincipalGroup();
					
					principalGroup.setName(group.getName());
					
					response.getManagerGroups().add(principalGroup);
				}
			}

			return response;

		} catch (RuntimeException e) {

			log.error("Error getting managers", e);

			throw e;
		}
	}

	private List<User> filterSelectedManagerUsers(List<User> allowedManagers, List<Principal> selectedUsers) throws AccessDeniedException {

		if (!CollectionUtils.isEmpty(selectedUsers)) {

			List<User> selectedManagers = new ArrayList<User>();

			for (Principal principal : selectedUsers) {

				if (allowedManagers != null) {
					for (User manager : allowedManagers) {

						if (manager.getUsername() != null && manager.getUsername().equalsIgnoreCase(principal.getUserID())) {

							selectedManagers.add(manager);
							break;
						}
					}
				}
			}

			if (CollectionUtils.getSize(selectedManagers) != selectedUsers.size()) {

				throw new AccessDeniedException("One or more of the specified users are not valid managers for this flow", new AccessDenied());
			}

			return selectedManagers;

		} else {

			return null;
		}
	}

	private List<Group> filterSelectedManagerGroups(List<Group> allowedManagerGroups, List<PrincipalGroup> selectedGroups) throws AccessDeniedException {

		if (!CollectionUtils.isEmpty(selectedGroups)) {

			List<Group> selectedManagerGroups = new ArrayList<Group>();

			for (PrincipalGroup principalGroup : selectedGroups) {

				if (allowedManagerGroups != null) {
					for (Group managerGroup : allowedManagerGroups) {

						if (managerGroup.getName() != null && managerGroup.getName().equalsIgnoreCase(principalGroup.getName())) {

							selectedManagerGroups.add(managerGroup);
							break;
						}
					}
				}
			}

			if (CollectionUtils.getSize(selectedManagerGroups) != selectedGroups.size()) {

				throw new AccessDeniedException("One or more of the specified groups are not valid managers for this flow", new AccessDenied());
			}

			return selectedManagerGroups;

		} else {

			return null;
		}
	}

	protected void checkDependencies() {

		if (flowAdminModule == null) {

			throw new RuntimeException("Missing required dependency " + FlowAdminModule.class.getSimpleName());
		}
	}

	protected FlowInstance getFlowInstance(Integer flowInstanceID, ExternalID externalID) throws FlowInstanceNotFoundException {

		FlowInstance flowInstance = null;

		if (flowInstanceID != null) {

			try {
				flowInstance = flowAdminModule.getFlowInstance(flowInstanceID, null, FlowInstance.FLOW_RELATION, FlowInstance.OWNERS_RELATION, FlowInstance.STATUS_RELATION, Flow.FLOW_FAMILY_RELATION, FlowInstance.ATTRIBUTES_RELATION, FlowInstance.MANAGERS_RELATION, FlowInstance.MANAGER_GROUPS_RELATION, FlowFamily.MANAGER_USERS_RELATION, FlowFamily.MANAGER_GROUPS_RELATION);

			} catch (SQLException e) {

				throw new RuntimeException(e);
			}

		} else if (externalID != null) {

			throw new RuntimeException("External ID support not implemented");
		}

		if (flowInstance == null) {

			throw new FlowInstanceNotFoundException("The requested flow instance was not found", new FlowInstanceNotFound());
		}

		return flowInstance;
	}

	private Status getStatus(Flow flow, Integer statusID) {

		HighLevelQuery<Status> query = new HighLevelQuery<>();

		query.addParameter(statusIDParamFactory.getParameter(statusID));
		query.addParameter(statusFlowParamFactory.getParameter(flow));

		try {
			return daoFactory.getStatusDAO().get(query);

		} catch (SQLException e) {

			throw new RuntimeException(e);
		}
	}

	private Status getStatus(Flow flow, String statusName) {

		HighLevelQuery<Status> query = new HighLevelQuery<>();

		query.addParameter(statusNameParamFactory.getParameter(statusName));
		query.addParameter(statusFlowParamFactory.getParameter(flow));

		try {
			return daoFactory.getStatusDAO().get(query);

		} catch (SQLException e) {

			throw new RuntimeException(e);
		}
	}

	public FlowInstanceEvent addFlowInstanceEvent(ImmutableFlowInstance flowInstance, EventType eventType, String details, User user, Timestamp timestamp) {

		FlowInstanceEvent flowInstanceEvent = new FlowInstanceEvent();
		flowInstanceEvent.setFlowInstance((FlowInstance) flowInstance);
		flowInstanceEvent.setEventType(eventType);
		flowInstanceEvent.setDetails(details);
		flowInstanceEvent.setPoster(user);
		flowInstanceEvent.setStatus(flowInstance.getStatus().getName());
		flowInstanceEvent.setStatusDescription(flowInstance.getStatus().getDescription());

		flowInstanceEvent.getAttributeHandler().setAttribute("integration", true);

		if (timestamp == null) {

			flowInstanceEvent.setAdded(TimeUtils.getCurrentTimestamp());

		} else {

			flowInstanceEvent.setAdded(timestamp);
		}

		try {
			daoFactory.getFlowInstanceEventDAO().add(flowInstanceEvent, FLOW_INSTANCE_EVENT_ATTRIBUTE_RELATION_QUERY);

		} catch (Exception e) {

			throw new RuntimeException(e);
		}

		return flowInstanceEvent;
	}

	private User getPrincipalUser(Principal principal) {

		if (principal != null && principal.getUserID() != null) {

			return callback.getSystemInterface().getUserHandler().getUserByUsername(principal.getUserID(), false, true);
		}

		return null;
	}

	@Override
	public List<SettingDescriptor> getSettings() {

		try {
			return ModuleUtils.getAnnotatedSettingDescriptors(this, BaseWSModuleService.class, callback.getSystemInterface());

		} catch (RuntimeException e) {

			throw e;

		} catch (Exception e) {

			throw new RuntimeException(e);
		}
	}

	@Override
	public void setAttribute(Integer flowInstanceID, ExternalID externalID, String name, String value) throws AccessDeniedException, FlowInstanceNotFoundException {

		try {
			checkDependencies();

			FlowInstance flowInstance = getFlowInstance(flowInstanceID, externalID);

			if (StringUtils.isEmpty(name)) {
				throw new RuntimeException("Name cannot be empty");
			}

			if(enableAttributePrefix) {
				
				name = "callback." + name;
			}

			log.info("User " + callback.getUser() + " requested set attribute for flow instance " + flowInstance + " attribute \"" + name + "\" with value \"" + value + "\"");

			if (StringUtils.isEmpty(value)) {

				if (!flowInstance.getAttributeHandler().isSet(name)) {
					return;
				}

				flowInstance.getAttributeHandler().removeAttribute(name);

			} else if (value.equals(flowInstance.getAttributeHandler().getString(name))) {

				return;

			} else {

				flowInstance.getAttributeHandler().setAttribute(name, value);
			}

			FlowInstanceUtils.setDescriptions(flowInstance);
			
			try {
				daoFactory.getFlowInstanceDAO().update(flowInstance, new RelationQuery(FlowInstance.ATTRIBUTES_RELATION));

			} catch (SQLException e) {

				throw new RuntimeException(e);
			}

			callback.getSystemInterface().getEventHandler().sendEvent(FlowInstance.class, new CRUDEvent<>(CRUDAction.UPDATE, flowInstance), EventTarget.ALL);

		} catch (RuntimeException e) {

			log.error("Error setting attribute", e);

			throw e;
		}
	}
}
