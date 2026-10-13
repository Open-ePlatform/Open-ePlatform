package com.nordicpeak.flowengine.utils;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Map;
import java.util.Map.Entry;

import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.hierarchy.core.interfaces.attributes.MutableAttributeHandler;
import se.unlogic.standardutils.dao.AnnotatedDAO;
import se.unlogic.standardutils.dao.LowLevelQuery;
import se.unlogic.standardutils.dao.RelationQuery;
import se.unlogic.standardutils.dao.TransactionHandler;
import se.unlogic.standardutils.string.StringUtils;
import se.unlogic.standardutils.time.TimeUtils;

import com.nordicpeak.flowengine.beans.FlowInstance;
import com.nordicpeak.flowengine.beans.FlowInstanceEvent;
import com.nordicpeak.flowengine.dao.FlowEngineDAOFactory;
import com.nordicpeak.flowengine.enums.EventType;
import com.nordicpeak.flowengine.interfaces.ImmutableFlowInstance;

public class FlowInstanceEventGenerator {

	public static final RelationQuery FLOW_INSTANCE_EVENT_ATTRIBUTE_RELATION_QUERY = new RelationQuery(FlowInstanceEvent.ATTRIBUTES_RELATION);

	public final AnnotatedDAO<FlowInstanceEvent> eventDAO;

	public final AnnotatedDAO<FlowInstance> flowInstanceDAO;

	public FlowInstanceEventGenerator(FlowEngineDAOFactory daoFactory) {

		eventDAO = daoFactory.getFlowInstanceEventDAO();
		flowInstanceDAO = daoFactory.getFlowInstanceDAO();
	}

	public FlowInstanceEvent addFlowInstanceEvent(ImmutableFlowInstance flowInstance, EventType eventType, String details, User user) throws SQLException {

		return addFlowInstanceEvent(flowInstance, eventType, details, user, null);
	}

	public FlowInstanceEvent addFlowInstanceEvent(ImmutableFlowInstance flowInstance, EventType eventType, String details, User user, Timestamp timestamp) throws SQLException {

		return addFlowInstanceEvent(flowInstance, eventType, details, user, timestamp, null);
	}

	public FlowInstanceEvent addFlowInstanceEvent(ImmutableFlowInstance flowInstance, EventType eventType, String details, User user, Timestamp timestamp, Map<String, String> eventAttributes) throws SQLException {

		return addFlowInstanceEvent(flowInstance, eventType, details, user, timestamp, eventAttributes, null);
	}

	public FlowInstanceEvent addFlowInstanceEvent(ImmutableFlowInstance flowInstance, EventType eventType, String details, User user, Timestamp timestamp, Map<String, String> eventAttributes, TransactionHandler transactionHandler) throws SQLException {

		FlowInstanceEvent flowInstanceEvent = new FlowInstanceEvent();
		flowInstanceEvent.setFlowInstance((FlowInstance) flowInstance);
		flowInstanceEvent.setEventType(eventType);
		flowInstanceEvent.setDetails(StringUtils.substring(details, 255, "..."));
		flowInstanceEvent.setPoster(user);
		flowInstanceEvent.setStatus(flowInstance.getStatus().getName());
		flowInstanceEvent.setStatusDescription(flowInstance.getStatus().getDescription());

		if (timestamp == null) {

			flowInstanceEvent.setAdded(TimeUtils.getCurrentTimestamp());

		} else {

			flowInstanceEvent.setAdded(timestamp);
		}

		if (eventAttributes != null) {

			MutableAttributeHandler attributeHandler = flowInstanceEvent.getAttributeHandler();

			for (Entry<String, String> entry : eventAttributes.entrySet()) {

				attributeHandler.setAttribute(entry.getKey(), entry.getValue());
			}

			if (transactionHandler != null) {

				eventDAO.add(flowInstanceEvent, transactionHandler, FLOW_INSTANCE_EVENT_ATTRIBUTE_RELATION_QUERY);

			} else {

				eventDAO.add(flowInstanceEvent, FLOW_INSTANCE_EVENT_ATTRIBUTE_RELATION_QUERY);
			}

		} else {

			if (transactionHandler != null) {

				eventDAO.add(flowInstanceEvent, transactionHandler, null);

			} else {

				eventDAO.add(flowInstanceEvent);
			}
		}

		LowLevelQuery<FlowInstance> lastAddedEventQuery = new LowLevelQuery<>("UPDATE " + flowInstanceDAO.getTableName() + " SET lastEventAdded = ? WHERE flowInstanceID = ?");

		lastAddedEventQuery.addParameter(flowInstanceEvent.getAdded());
		lastAddedEventQuery.addParameter(flowInstance.getFlowInstanceID());

		if (transactionHandler != null) {
			
			flowInstanceDAO.update(lastAddedEventQuery, transactionHandler);
		} else {
			
			flowInstanceDAO.update(lastAddedEventQuery);
		}

		return flowInstanceEvent;
	}
}
