package com.nordicpeak.flowengine.events;

import java.io.Serializable;

import com.nordicpeak.flowengine.beans.FlowInstanceEvent;
import com.nordicpeak.flowengine.managers.FlowInstanceManager;

public class MultiSigningInitiatedEvent implements Serializable {

	private static final long serialVersionUID = -4738711597966694401L;

	private final FlowInstanceManager flowInstanceManager;
	private final FlowInstanceEvent event;
	private final String fullContextPath;

	public MultiSigningInitiatedEvent(FlowInstanceManager flowInstanceManager, FlowInstanceEvent event, String fullContextPath) {

		super();
		this.flowInstanceManager = flowInstanceManager;
		this.event = event;
		this.fullContextPath = fullContextPath;
	}

	public FlowInstanceManager getFlowInstanceManager() {

		return flowInstanceManager;
	}

	public FlowInstanceEvent getEvent() {

		return event;
	}
	
	public String getFullContextPath() {
		return fullContextPath;
	}
}
