package com.nordicpeak.flowengine.exceptions.flow;

import java.util.List;

import com.nordicpeak.flowengine.beans.FlowInstance;
import com.nordicpeak.flowengine.interfaces.ImmutableFlow;

public class FlowLimitExceededException extends FlowException {

	private static final long serialVersionUID = -1495106811513719186L;

	private List<FlowInstance> flowInstances;
	
	public FlowLimitExceededException(String message, ImmutableFlow flow, List<FlowInstance> flowInstances) {
		
		super(message, flow);
		
		this.flowInstances = flowInstances;
	}

	public List<FlowInstance> getFlowInstances() {
		return flowInstances;
	}

}
