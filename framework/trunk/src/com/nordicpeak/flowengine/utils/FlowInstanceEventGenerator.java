package com.nordicpeak.flowengine.utils;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Map;
import java.util.Map.Entry;

import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.hierarchy.core.interfaces.attributes.MutableAttributeHandler;
import se.unlogic.standardutils.dao.AnnotatedDAO;
import se.unlogic.standardutils.dao.RelationQuery;
import se.unlogic.standardutils.time.TimeUtils;

import com.nordicpeak.flowengine.beans.FlowInstance;
import com.nordicpeak.flowengine.beans.FlowInstanceEvent;
import com.nordicpeak.flowengine.dao.FlowEngineDAOFactory;
import com.nordicpeak.flowengine.enums.EventType;
import com.nordicpeak.flowengine.interfaces.ImmutableFlowInstance;

public class FlowInstanceEventGenerator {

	public static final RelationQuery FLOW_INSTANCE_EVENT_ATTRIBUTE_RELATION_QUERY = new RelationQuery(FlowInstanceEvent.ATTRIBUTES_RELATION);
	
	public final AnnotatedDAO<FlowInstanceEvent> eventDAO;
	
	public FlowInstanceEventGenerator(FlowEngineDAOFactory daoFactory){
		
		eventDAO = daoFactory.getFlowInstanceEventDAO();
	}
	
	public FlowInstanceEvent addFlowInstanceEvent(ImmutableFlowInstance flowInstance, EventType eventType, String details, User user) throws SQLException {

		return addFlowInstanceEvent(flowInstance, eventType, details, user, null);
	}

	public FlowInstanceEvent addFlowInstanceEvent(ImmutableFlowInstance flowInstance, EventType eventType, String details, User user, Timestamp timestamp) throws SQLException {

		return addFlowInstanceEvent(flowInstance, eventType, details, user, timestamp, null);
	}

	public FlowInstanceEvent addFlowInstanceEvent(ImmutableFlowInstance flowInstance, EventType eventType, String details, User user, Timestamp timestamp, Map<String,String> eventAttributes) throws SQLException {

		FlowInstanceEvent flowInstanceEvent = new FlowInstanceEvent();
		flowInstanceEvent.setFlowInstance((FlowInstance) flowInstance);
		flowInstanceEvent.setEventType(eventType);
		flowInstanceEvent.setDetails(details);
		flowInstanceEvent.setPoster(user);
		flowInstanceEvent.setStatus(flowInstance.getStatus().getName());
		flowInstanceEvent.setStatusDescription(flowInstance.getStatus().getDescription());

		if(timestamp == null){

			flowInstanceEvent.setAdded(TimeUtils.getCurrentTimestamp());

		}else{

			flowInstanceEvent.setAdded(timestamp);
		}

		if(eventAttributes != null){

			MutableAttributeHandler attributeHandler = flowInstanceEvent.getAttributeHandler();

			for(Entry<String,String> entry : eventAttributes.entrySet()){

				attributeHandler.setAttribute(entry.getKey(), entry.getValue());
			}

			eventDAO.add(flowInstanceEvent, FLOW_INSTANCE_EVENT_ATTRIBUTE_RELATION_QUERY);

		}else{

			eventDAO.add(flowInstanceEvent);
		}

		return flowInstanceEvent;
	}		
}
