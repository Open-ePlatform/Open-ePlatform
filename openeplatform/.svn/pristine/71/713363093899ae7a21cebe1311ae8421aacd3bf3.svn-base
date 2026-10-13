package com.nordicpeak.flowengine.exceptions.flow;

import com.nordicpeak.flowengine.exceptions.FlowEngineException;
import com.nordicpeak.flowengine.interfaces.ImmutableFlow;

public abstract class FlowException extends FlowEngineException {

	private static final long serialVersionUID = -3174449007559687682L;

	private final ImmutableFlow flow;

	public FlowException(String message, ImmutableFlow flow) {

		super(message);
		this.flow = flow;
	}

	public FlowException(String message, Throwable cause, ImmutableFlow flow) {

		super(message, cause);
		this.flow = flow;
	}

	public ImmutableFlow getFlow() {

		return flow;
	}
}
