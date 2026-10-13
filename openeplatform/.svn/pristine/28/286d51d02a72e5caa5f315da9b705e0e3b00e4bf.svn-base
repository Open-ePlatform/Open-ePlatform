package com.nordicpeak.flowengine.exceptions.flowinstancemanager;

import com.nordicpeak.flowengine.interfaces.ImmutableFlowInstance;

public class DuplicateFlowInstanceManagerIDException extends MutableFlowInstanceManagerException {

	private static final long serialVersionUID = 6128971103767775365L;

	public DuplicateFlowInstanceManagerIDException(ImmutableFlowInstance flowInstance, String instanceManagerID) {

		super("There is already a flow instance manager with ID " + instanceManagerID, flowInstance, instanceManagerID);

	}

}
