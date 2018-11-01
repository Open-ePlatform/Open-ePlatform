package com.nordicpeak.flowengine.utils;

import java.util.ArrayList;
import java.util.List;

import com.nordicpeak.flowengine.enums.EventType;
import com.nordicpeak.flowengine.interfaces.ImmutableFlowInstance;
import com.nordicpeak.flowengine.interfaces.ImmutableFlowInstanceEvent;

import se.unlogic.standardutils.collections.ReverseListIterator;

public class SigningUtils {
	
	public static List<ImmutableFlowInstanceEvent> getLastestSignEvents(ImmutableFlowInstance flowInstance, boolean skipImmediateSubmitEvent) {
		
		return getLastestSignEvents(flowInstance.getEvents(), skipImmediateSubmitEvent);
	}
	
	/** @return Returned list order is newest event first */
	public static List<ImmutableFlowInstanceEvent> getLastestSignEvents(List<? extends ImmutableFlowInstanceEvent> events, boolean skipImmediateSubmitEvent) {
		
		List<ImmutableFlowInstanceEvent> signEvents = null;
		
		if (events != null) {
			
			signEvents = new ArrayList<ImmutableFlowInstanceEvent>(events.size());
			
			boolean firstSubmitEvent = true;
			
			for (ImmutableFlowInstanceEvent event : new ReverseListIterator<ImmutableFlowInstanceEvent>(events)) {
				
				if (event.getEventType() == EventType.SIGNED) {
					
					signEvents.add(event);
					
					if (event.getAttributeHandler().getPrimitiveBoolean("pdf")) {
						
						break; // Found start of sign event chain, stop
					}
					
				} else if (event.getEventType() == EventType.PAYED) {
					
					continue;
					
				} else if (event.getEventType() == EventType.SUBMITTED && skipImmediateSubmitEvent && firstSubmitEvent) {
					
					firstSubmitEvent = false;
					continue;
					
				} else { // Other event types, stop
					
					break;
				}
			}
			
			if (!signEvents.isEmpty()) {
				
				return signEvents;
			}
		}
		
		return null;
	}
	
	public static ImmutableFlowInstanceEvent getLastPosterSignEvent(ImmutableFlowInstance flowInstance) {
		
		return getLastPosterSignEvent(flowInstance.getEvents());
	}
	
	public static ImmutableFlowInstanceEvent getLastPosterSignEvent(List<? extends ImmutableFlowInstanceEvent> events) {
		
		if (events != null) {
			
			for (ImmutableFlowInstanceEvent event : new ReverseListIterator<ImmutableFlowInstanceEvent>(events)) {
				
				//TODO replace with Constants.FLOW_INSTANCE_EVENT_SIGNING_SESSION_EVENT_SIGNING_PDF.equals(event.getAttributeHandler().getString(Constants.FLOW_INSTANCE_EVENT_SIGNING_SESSION_EVENT) when everyone uses new signing module
				if ((event.getEventType() == EventType.SIGNED || event.getEventType() == EventType.SIGNING_SKIPPED) && event.getAttributeHandler().getPrimitiveBoolean("pdf")) {
					
					return event;
				}
			}
		}
		
		return null;
	}
	
}
