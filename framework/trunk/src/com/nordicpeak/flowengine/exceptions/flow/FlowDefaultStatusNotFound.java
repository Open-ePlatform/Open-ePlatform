package com.nordicpeak.flowengine.exceptions.flow;

import com.nordicpeak.flowengine.interfaces.ImmutableFlow;
import com.nordicpeak.flowengine.interfaces.ImmutableFlowInstance;


public class FlowDefaultStatusNotFound extends FlowException {

	private static final long serialVersionUID = -6419667887728829555L;

	public FlowDefaultStatusNotFound(String actionID, ImmutableFlow flow) {

		super("Unable to find default status for action ID " + actionID + " for flow " + flow, flow);
	}
	
	public FlowDefaultStatusNotFound(String actionID, ImmutableFlowInstance flowInstance) {

		super("Unable to find default status for action ID " + actionID + " for " + flowInstance +  ", flow name" + flowInstance.getFlow().getName(), flowInstance.getFlow());
	}
}
