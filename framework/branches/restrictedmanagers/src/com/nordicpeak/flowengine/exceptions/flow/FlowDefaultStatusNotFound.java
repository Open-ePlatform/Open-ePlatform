package com.nordicpeak.flowengine.exceptions.flow;

import com.nordicpeak.flowengine.interfaces.ImmutableFlow;


public class FlowDefaultStatusNotFound extends FlowException {

	private static final long serialVersionUID = -6419667887728829555L;

	public FlowDefaultStatusNotFound(String actionID, ImmutableFlow flow) {

		super("Unable to find default status for action ID " + actionID + " for flow " + flow, flow);
	}
}
