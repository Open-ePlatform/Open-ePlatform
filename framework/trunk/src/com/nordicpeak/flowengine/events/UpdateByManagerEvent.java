package com.nordicpeak.flowengine.events;

import java.io.Serializable;

import com.nordicpeak.flowengine.beans.FlowInstanceEvent;
import com.nordicpeak.flowengine.managers.FlowInstanceManager;

public class UpdateByManagerEvent implements Serializable {

	private static final long serialVersionUID = 4129670393435968733L;

	private final FlowInstanceManager flowInstanceManager;
	private final FlowInstanceEvent event;	

	public UpdateByManagerEvent(FlowInstanceManager flowInstanceManager, FlowInstanceEvent event) {

		super();
		this.flowInstanceManager = flowInstanceManager;
		this.event = event;
	}

	public FlowInstanceManager getFlowInstanceManager() {

		return flowInstanceManager;
	}

	public FlowInstanceEvent getEvent() {

		return event;
	}
	
	@Override
	public String toString(){
		
		return event.toString();
	}
}
