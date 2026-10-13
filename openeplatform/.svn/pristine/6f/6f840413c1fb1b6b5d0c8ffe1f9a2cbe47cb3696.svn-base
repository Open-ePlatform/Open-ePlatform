package com.nordicpeak.flowengine.exceptions.flowinstance;

import com.nordicpeak.flowengine.exceptions.FlowEngineException;
import com.nordicpeak.flowengine.interfaces.ImmutableFlowInstance;

public abstract class FlowInstanceException extends FlowEngineException {

	private static final long serialVersionUID = -549607655978868445L;
	private final ImmutableFlowInstance flowInstance;

	public FlowInstanceException(String message, ImmutableFlowInstance flowInstance) {

		super(message);
		this.flowInstance = flowInstance;
	}

	public FlowInstanceException(String message, ImmutableFlowInstance flowInstance, Throwable cause) {

		super(message, cause);
		this.flowInstance = flowInstance;
	}

	public ImmutableFlowInstance getFlowInstance() {

		return flowInstance;
	}
}
