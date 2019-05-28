package com.nordicpeak.flowengine.utils;

import java.util.ArrayList;
import java.util.List;

import se.unlogic.standardutils.collections.ReverseListIterator;

import com.nordicpeak.flowengine.Constants;
import com.nordicpeak.flowengine.enums.EventType;
import com.nordicpeak.flowengine.interfaces.ImmutableFlowInstance;
import com.nordicpeak.flowengine.interfaces.ImmutableFlowInstanceEvent;

public class SigningUtils {
	
	public static List<ImmutableFlowInstanceEvent> getLastestSigningSessionEvents(ImmutableFlowInstance flowInstance) {

		return getLastestSigningSessionEvents(flowInstance.getEvents());
	}
	
	/** @return Returned list order is newest event first */
	public static List<ImmutableFlowInstanceEvent> getLastestSigningSessionEvents(List<? extends ImmutableFlowInstanceEvent> events) {

		List<ImmutableFlowInstanceEvent> signEvents = null;

		if (events != null) {

			signEvents = new ArrayList<ImmutableFlowInstanceEvent>(events.size());

			String latestSigningSessionID = null;

			for (ImmutableFlowInstanceEvent event : new ReverseListIterator<ImmutableFlowInstanceEvent>(events)) {

				if (event.getEventType() == EventType.SIGNED) {

					String eventSigningSessionID = event.getAttributeHandler().getString(Constants.FLOW_INSTANCE_EVENT_SIGNING_SESSION);
					
					if (eventSigningSessionID != null) {
					
						String signingEventType = event.getAttributeHandler().getString(Constants.FLOW_INSTANCE_EVENT_SIGNING_SESSION_EVENT);
						boolean isSigningCompleteEvent = Constants.FLOW_INSTANCE_EVENT_SIGNING_SESSION_EVENT_SIGNED_PDF.equals(signingEventType);
	
						if (latestSigningSessionID == null) {
	
								latestSigningSessionID = eventSigningSessionID;
	
								if (!isSigningCompleteEvent) {
	
									signEvents.add(event);
								}
	
						} else if (latestSigningSessionID.equals(eventSigningSessionID) && !isSigningCompleteEvent) {
							
							signEvents.add(event);
						}
					}
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
