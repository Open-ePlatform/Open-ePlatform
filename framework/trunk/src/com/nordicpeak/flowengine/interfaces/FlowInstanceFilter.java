package com.nordicpeak.flowengine.interfaces;

public interface FlowInstanceFilter {

	public boolean include(ImmutableFlowInstance flowInstance) throws Exception;

}