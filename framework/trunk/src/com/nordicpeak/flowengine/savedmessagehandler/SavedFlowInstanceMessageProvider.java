package com.nordicpeak.flowengine.savedmessagehandler;

import se.unlogic.hierarchy.core.interfaces.Prioritized;

import com.nordicpeak.flowengine.interfaces.ImmutableFlowInstance;

public interface SavedFlowInstanceMessageProvider extends Prioritized {

	String getSavedInstanceMessage(ImmutableFlowInstance flowInstance) throws Exception;
}
