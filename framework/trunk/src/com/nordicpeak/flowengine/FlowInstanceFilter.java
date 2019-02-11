package com.nordicpeak.flowengine;

import com.nordicpeak.flowengine.interfaces.ImmutableFlowInstance;

public interface FlowInstanceFilter {

	public boolean evaluateFlowInstance(ImmutableFlowInstance flowInstance) throws Exception;

}