package com.nordicpeak.flowengine.exceptions.flowinstancemanager;

import com.nordicpeak.flowengine.interfaces.ImmutableFlowInstance;



public class FlowInstanceManagerClosedException extends MutableFlowInstanceManagerException {

	private static final long serialVersionUID = 7372175933415699255L;

	public FlowInstanceManagerClosedException(ImmutableFlowInstance flowInstance, String instanceManagerID) {

		super("The flow instance manager with ID " + instanceManagerID + " for flow instance " + flowInstance + " has been closed.", flowInstance, instanceManagerID);
	}
}
