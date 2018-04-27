package com.nordicpeak.flowengine.exceptions.flowinstancemanager;

import com.nordicpeak.flowengine.interfaces.ImmutableFlowInstance;

public abstract class MutableFlowInstanceManagerException extends FlowInstanceManagerException {

	private static final long serialVersionUID = -6609669617451449873L;

	private final String instanceManagerID;

	public MutableFlowInstanceManagerException(String message, ImmutableFlowInstance flowInstance, String instanceManagerID, Throwable cause) {

		super(message, flowInstance, cause);
		this.instanceManagerID = instanceManagerID;
	}

	public MutableFlowInstanceManagerException(String message, ImmutableFlowInstance flowInstance, String instanceManagerID) {

		super(message, flowInstance);
		this.instanceManagerID = instanceManagerID;
	}

	public String getInstanceManagerID() {

		return instanceManagerID;
	}
}
