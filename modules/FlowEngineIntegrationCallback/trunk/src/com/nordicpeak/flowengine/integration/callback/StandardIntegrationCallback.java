package com.nordicpeak.flowengine.integration.callback;

import java.lang.reflect.Field;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.jws.WebService;
import javax.sql.rowset.serial.SerialBlob;
import javax.xml.datatype.XMLGregorianCalendar;

import se.unlogic.hiearchy.foregroundmodules.jaxws.BaseWSModuleService;
import se.unlogic.hiearchy.foregroundmodules.jaxws.WSModuleCallback;
import se.unlogic.hiearchy.foregroundmodules.jaxws.WSModuleInstanceResolver;
import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.hierarchy.core.enums.CRUDAction;
import se.unlogic.hierarchy.core.enums.EventTarget;
import se.unlogic.hierarchy.core.events.CRUDEvent;
import se.unlogic.hierarchy.core.interfaces.InstanceListener;
import se.unlogic.standardutils.collections.CollectionUtils;
import se.unlogic.standardutils.dao.HighLevelQuery;
import se.unlogic.standardutils.dao.QueryParameterFactory;
import se.unlogic.standardutils.dao.RelationQuery;
import se.unlogic.standardutils.date.DateUtils;
import se.unlogic.standardutils.random.RandomUtils;
import se.unlogic.standardutils.string.StringUtils;
import se.unlogic.standardutils.time.TimeUtils;

import com.nordicpeak.flowengine.FlowAdminModule;
import com.nordicpeak.flowengine.beans.ExternalMessage;
import com.nordicpeak.flowengine.beans.ExternalMessageAttachment;
import com.nordicpeak.flowengine.beans.Flow;
import com.nordicpeak.flowengine.beans.FlowInstance;
import com.nordicpeak.flowengine.beans.FlowInstanceEvent;
import com.nordicpeak.flowengine.beans.Status;
import com.nordicpeak.flowengine.dao.FlowEngineDAOFactory;
import com.nordicpeak.flowengine.enums.EventType;
import com.nordicpeak.flowengine.enums.SenderType;
import com.nordicpeak.flowengine.events.ExternalMessageAddedEvent;
import com.nordicpeak.flowengine.events.ManagersChangedEvent;
import com.nordicpeak.flowengine.events.StatusChangedByManagerEvent;
import com.nordicpeak.flowengine.integration.callback.events.DeliveryConfirmationEvent;
import com.nordicpeak.flowengine.interfaces.ImmutableFlowInstance;

@WebService(endpointInterface="com.nordicpeak.flowengine.integration.callback.IntegrationCallback", name="IntegrationCallback",serviceName="IntegrationCallback")
@WSModuleInstanceResolver
public class StandardIntegrationCallback extends BaseWSModuleService implements IntegrationCallback, InstanceListener<FlowAdminModule> {
	
	protected static final RelationQuery FLOW_INSTANCE_ATTRIBUTE_RELATION_QUERY = new RelationQuery(FlowInstance.ATTRIBUTES_RELATION);
	protected static final RelationQuery FLOW_INSTANCE_MANAGERS_RELATION_QUERY = new RelationQuery(FlowInstance.MANAGERS_RELATION);
	protected static final RelationQuery FLOW_INSTANCE_EVENT_ATTRIBUTE_RELATION_QUERY = new RelationQuery(FlowInstanceEvent.ATTRIBUTES_RELATION);
	
	private FlowAdminModule flowAdminModule;
	
	private FlowEngineDAOFactory daoFactory;
	protected QueryParameterFactory<FlowInstance, Integer> flowInstanceIDParamFactory;
	private QueryParameterFactory<Status, Integer> statusIDParamFactory;
	private QueryParameterFactory<Status, Flow> statusFlowParamFactory;
	
	@Override
	public void init(WSModuleCallback callback) throws Exception {

		super.init(callback);
		
		daoFactory = new FlowEngineDAOFactory(callback.getDataSource(), callback.getSystemInterface().getUserHandler(), callback.getSystemInterface().getGroupHandler());
		
		flowInstanceIDParamFactory = daoFactory.getFlowInstanceDAO().getParamFactory("flowInstanceID", Integer.class);
		statusIDParamFactory = daoFactory.getStatusDAO().getParamFactory("statusID", Integer.class);
		statusFlowParamFactory = daoFactory.getStatusDAO().getParamFactory("flow", Flow.class);
		
		callback.getSystemInterface().getInstanceHandler().addInstanceListener(FlowAdminModule.class, this);
	}

	@Override
	public void unload() throws Exception {

		callback.getSystemInterface().getInstanceHandler().removeInstanceListener(FlowAdminModule.class, this);
		
		super.unload();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int setStatus(Integer flowInstanceID, ExternalID externalID, Integer statusID, String statusAlias, Principal principal) throws AccessDeniedException, FlowInstanceNotFoundException, StatusNotFoundException {

		checkDependencies();
		
		FlowInstance flowInstance = getFlowInstance(flowInstanceID, externalID);
		
		User principalUser = getPrincipalUser(principal);
		
		log.info("User " + callback.getUser() + " requested set status for flow instance " + flowInstance + " using principal " + principalUser);

		Status status = null;
		
		if(statusID != null){
			
			status = getStatus(flowInstance.getFlow(), statusID);
		}
		
		if(status == null){
			
			throw new StatusNotFoundException("Status with ID " + statusID + " not found for flow " + flowInstance.getFlow(), new StatusNotFound());
			
		}else if(flowInstance.getStatus().equals(status)){
			
			log.info("Flow instance " + flowInstance + " already has the requested status of " + status + ", ignoring request from user " + callback.getUser());
			
			return 0;
			
		}else{
			
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

			callback.getSystemInterface().getEventHandler().sendEvent(FlowInstance.class, new CRUDEvent<FlowInstance>(CRUDAction.UPDATE, flowInstance), EventTarget.ALL);

			callback.getSystemInterface().getEventHandler().sendEvent(FlowInstance.class, new StatusChangedByManagerEvent(flowInstance, flowInstanceEvent, flowAdminModule.getSiteProfile(flowInstance), previousStatus, principalUser), EventTarget.ALL);
			
			return flowInstanceEvent.getEventID();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int addEvent(Integer flowInstanceID, ExternalID externalID, XMLGregorianCalendar date, String message, Principal principal) throws AccessDeniedException, FlowInstanceNotFoundException {

		checkDependencies();
		
		FlowInstance flowInstance = getFlowInstance(flowInstanceID, externalID);
		
		User principalUser = getPrincipalUser(principal);
		
		log.info("User " + callback.getUser() + " requested add event for flow instance " + flowInstance + " using principal " + principal);
		
		FlowInstanceEvent flowInstanceEvent = addFlowInstanceEvent(flowInstance, EventType.OTHER_EVENT, message, principalUser, TimeUtils.getTimeStamp(date));
		
		return flowInstanceEvent.getEventID();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int addMessage(Integer flowInstanceID, ExternalID externalID, IntegrationMessage message, Principal principal) throws AccessDeniedException, FlowInstanceNotFoundException {

		checkDependencies();
		
		FlowInstance flowInstance = getFlowInstance(flowInstanceID, externalID);
		
		User principalUser = getPrincipalUser(principal);
		
		log.info("User " + callback.getUser() + " requested add message for flow instance " + flowInstance + " using principal " + principal);
		
		ExternalMessage externalMessage = getExternalMessage(message);
		
		externalMessage.setPoster(principalUser);
		externalMessage.setFlowInstance(flowInstance);
		
		try {
			daoFactory.getExternalMessageDAO().add(externalMessage);
			
		} catch (SQLException e) {

			throw new RuntimeException(e);
		}
		
		FlowInstanceEvent flowInstanceEvent = this.addFlowInstanceEvent(flowInstance, EventType.MANAGER_MESSAGE_SENT, null, principalUser, externalMessage.getAdded());

		callback.getSystemInterface().getEventHandler().sendEvent(FlowInstance.class, new ExternalMessageAddedEvent(flowInstance, flowInstanceEvent, flowAdminModule.getSiteProfile(flowInstance), externalMessage, SenderType.MANAGER), EventTarget.ALL);		
	
		return externalMessage.getMessageID();
	}

	
	private ExternalMessage getExternalMessage(IntegrationMessage message) {

		if(message == null){
			
			throw new NullPointerException("Integration message cannot be null");
		}

		if(message.getAdded() == null){
			
			throw new NullPointerException("Added field cannot be null");
		}
			
			
		if(StringUtils.isEmpty(message.getMessage())){
			
			throw new RuntimeException("Message field cannot be null or empty");
		}
		
		ExternalMessage externalMessage = new ExternalMessage();
		
		externalMessage.setAdded(TimeUtils.getTimeStamp(message.getAdded()));
		externalMessage.setMessage(message.getMessage());
		
		if(!CollectionUtils.isEmpty(message.getAttachments())){
			
			ArrayList<ExternalMessageAttachment> externalMessageAttachments = new ArrayList<ExternalMessageAttachment>(message.getAttachments().size());
			
			for(Attachment attachment : message.getAttachments()){
				
				if(StringUtils.isEmpty(attachment.getFilename())){
					
					throw new RuntimeException("Message field cannot be null or empty");
				}
				
				if(attachment.getEncodedData() == null){
					
					throw new NullPointerException("Encoded data field cannot be null");
				}
				
				if(attachment.getSize() <= 0){
					
					throw new RuntimeException("Size cannot be < 0");
				}
				
				ExternalMessageAttachment externalMessageAttachment = new ExternalMessageAttachment();
				
				externalMessageAttachment.setAdded(externalMessage.getAdded());
				
				externalMessageAttachment.setFilename(attachment.getFilename());
				
				try {
					externalMessageAttachment.setData(new SerialBlob(attachment.getEncodedData()));

				} catch (SQLException e) {

					throw new RuntimeException(e);
				}
				
				externalMessageAttachment.setSize(attachment.getSize());
				
				externalMessageAttachments.add(externalMessageAttachment);
			}
			
			externalMessage.setAttachments(externalMessageAttachments);
		}
		
		return externalMessage;
	}	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void confirmDelivery(Integer flowInstanceID, ExternalID externalID, boolean delivered, String logMessage) throws AccessDeniedException, FlowInstanceNotFoundException {

		FlowInstance flowInstance = getFlowInstance(flowInstanceID, externalID);
		
		log.info("User " + callback.getUser() + " requested add message for flow instance " + flowInstance + " with delived flag set to " + delivered + " and log message se to " + logMessage);
		
		if(delivered){
		
			//Check if this flow instance has already been confirmed
			String wasDelivered = flowInstance.getAttributeHandler().getString(IntegrationCallbackConstants.DELIVERY_CONFIRMED_ATTRIBUTE);
			
			if(wasDelivered != null){
				
				log.info("Duplicate delivery confirmation received from user " + callback.getUser() + " for flow instance " + flowInstance + ", ignoring confirmation");
				
				return;
			}
			
			//Set delivered flag
			flowInstance.getAttributeHandler().setAttribute(IntegrationCallbackConstants.DELIVERY_CONFIRMED_ATTRIBUTE, DateUtils.DATE_TIME_SECONDS_FORMATTER.format(new Date()));
			
			if(externalID != null){
				
				if(StringUtils.isEmpty(externalID.getID())){
					
					throw new RuntimeException("ID field of ExternalID cannot be empty");
				}
				
				if(StringUtils.isEmpty(externalID.getSystem())){
					
					throw new RuntimeException("System field of ExternalID cannot be empty");
				}
				
				flowInstance.getAttributeHandler().setAttribute(IntegrationCallbackConstants.EXTERNAL_ID_ATTRIBUTE, externalID.getID());
				flowInstance.getAttributeHandler().setAttribute(IntegrationCallbackConstants.EXTERNAL_SYSTEM_ATTRIBUTE, externalID.getSystem());
			}
			
			try {
				daoFactory.getFlowInstanceDAO().update(flowInstance, FLOW_INSTANCE_ATTRIBUTE_RELATION_QUERY);
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}
		}
		
		callback.getSystemInterface().getEventHandler().sendEvent(IntegrationCallback.class, new DeliveryConfirmationEvent(flowInstance, delivered, logMessage), EventTarget.ALL);
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
	public int setManagers(Integer flowInstanceID, ExternalID externalID, List<Principal> managers) throws AccessDeniedException, FlowInstanceNotFoundException {

		checkDependencies();
		
		FlowInstance flowInstance = getFlowInstance(flowInstanceID, externalID);
		
		log.info("User " + callback.getUser() + " requested set managers for flow instance " + flowInstance);
		
		List<User> previousManagers = flowInstance.getManagers();
		
		String detailString;
		
		if(!CollectionUtils.isEmpty(managers)){
			
			ArrayList<User> managerUsers = new ArrayList<User>(managers.size());
			
			StringBuilder stringBuilder = new StringBuilder();
			
			for(Principal principal : managers){
				
				User managerUser = getPrincipalUser(principal);
				
				if(managerUser != null){
					
					managerUsers.add(managerUser);
					
					if(stringBuilder.length() > 0){

						stringBuilder.append(", ");
					}

					stringBuilder.append(managerUser.getFirstname());
					stringBuilder.append(" ");
					stringBuilder.append(managerUser.getLastname());
					
				}else{
					
					log.warn("Unable to find local user matching principal " + principal);
				}
			}
			
			detailString = stringBuilder.toString();
			flowInstance.setManagers(managerUsers);
			
		}else{
			
			flowInstance.setManagers(null);
			detailString = null;
		}
		
		if(!CollectionUtils.equals(previousManagers, flowInstance.getManagers())){
			
			log.info("User " + callback.getUser() + " setting managers of instance " + flowInstance + " to " + flowInstance.getManagers());
			
			try {
				daoFactory.getFlowInstanceDAO().update(flowInstance, FLOW_INSTANCE_MANAGERS_RELATION_QUERY);
				
			} catch (SQLException e) {

				throw new RuntimeException(e);
			}
			
			FlowInstanceEvent flowInstanceEvent = addFlowInstanceEvent(flowInstance, EventType.MANAGERS_UPDATED, detailString, null, null);
			
			callback.getSystemInterface().getEventHandler().sendEvent(FlowInstance.class, new CRUDEvent<FlowInstance>(CRUDAction.UPDATE, flowInstance), EventTarget.ALL);
			
			callback.getSystemInterface().getEventHandler().sendEvent(FlowInstance.class, new ManagersChangedEvent(flowInstance, flowInstanceEvent, flowAdminModule.getSiteProfile(flowInstance), previousManagers, null), EventTarget.ALL);			
			
		}else{
			
			log.info("No change in managers detected in request from user " + callback.getUser() + " for flow instance " + flowInstance + ", ignoring request.");
			
			return 0;
		}
		
		return RandomUtils.getRandomInt(0, Integer.MAX_VALUE);
	}

	@Override
	public <InstanceType extends FlowAdminModule> void instanceAdded(Class<FlowAdminModule> key, InstanceType instance) {

		this.flowAdminModule = instance;
	}

	@Override
	public <InstanceType extends FlowAdminModule> void instanceRemoved(Class<FlowAdminModule> key, InstanceType instance) {

		this.flowAdminModule = null;		
	}
	
	protected void checkDependencies(){
	
		throw new RuntimeException("Missing required dependency " + FlowAdminModule.class.getSimpleName());
	}
	
	private FlowInstance getFlowInstance(Integer flowInstanceID, ExternalID externalID) throws FlowInstanceNotFoundException {

		FlowInstance flowInstance = null;
		
		if(flowInstanceID != null){
			
			try {
				flowInstance = flowAdminModule.getFlowInstance(flowInstanceID, null, FlowInstance.FLOW_RELATION, FlowInstance.STATUS_RELATION, Flow.FLOW_FAMILY_RELATION, FlowInstance.ATTRIBUTES_RELATION, FlowInstance.MANAGERS_RELATION);
				
			} catch (SQLException e) {

				throw new RuntimeException(e);
			}
			
		}else if(externalID != null){
			
			throw new RuntimeException("External ID support not implemented");
		}
		
		if(flowInstance == null){
		
			throw new FlowInstanceNotFoundException("The requested flow instance was not found", new FlowInstanceNotFound());	
		}
		
		return flowInstance;
	}
	
	private Status getStatus(Flow flow, Integer statusID) {

		HighLevelQuery<Status> query = new HighLevelQuery<Status>();

		query.addParameter(statusIDParamFactory.getParameter(statusID));
		query.addParameter(statusFlowParamFactory.getParameter(flow));

		try {
			return daoFactory.getStatusDAO().get(query);
			
		} catch (SQLException e) {

			throw new RuntimeException(e);
		}
	}
	
	public FlowInstance getFlowInstance(int flowInstanceID, List<Field> excludedFields, Field... relations) throws SQLException {

		HighLevelQuery<FlowInstance> query = new HighLevelQuery<FlowInstance>(relations);

		if (excludedFields != null) {
			query.addExcludedFields(excludedFields);
		}

		query.addParameter(flowInstanceIDParamFactory.getParameter(flowInstanceID));

		return daoFactory.getFlowInstanceDAO().get(query);
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
		
		if(timestamp == null){
			
			flowInstanceEvent.setAdded(TimeUtils.getCurrentTimestamp());
			
		}else{
			
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

		if(principal != null && principal.getUserID() != null){
			
			return callback.getSystemInterface().getUserHandler().getUserByUsername(principal.getUserID(), false, true);
		}

		return null;
	}
}
