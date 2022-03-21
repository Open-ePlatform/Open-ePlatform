package com.nordicpeak.flowengine.savedmessagehandler;

import java.util.List;

import com.nordicpeak.flowengine.interfaces.ImmutableFlowInstance;

public interface SavedFlowInstanceMessageHandler {

	public boolean addSavedFlowInstanceMessageProvider(SavedFlowInstanceMessageProvider provider);
	
	public boolean removeSavedFlowInstanceMessageProvider(SavedFlowInstanceMessageProvider provider);
	
	public List<SavedFlowInstanceMessageProvider> getProviders();
	
	public List<String> getMessages(ImmutableFlowInstance flowInstance);
}
