package com.nordicpeak.flowengine.exceptions.flow;

import com.nordicpeak.flowengine.interfaces.ImmutableFlow;


public class FlowDisabledException extends FlowException {

	private static final long serialVersionUID = -3857126933584602510L;

	public FlowDisabledException(ImmutableFlow flow) {

		super("The requested flow " + flow + " is not enabled", flow);
	}

}
