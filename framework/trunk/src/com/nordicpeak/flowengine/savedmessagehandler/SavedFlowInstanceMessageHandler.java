package com.nordicpeak.flowengine.savedmessagehandler;

import java.util.List;

public interface SavedFlowInstanceMessageHandler {

	public boolean addSavedFlowInstanceMessageProvider(SavedFlowInstanceMessageProvider provider);
	
	public boolean removeSavedFlowInstanceMessageProvider(SavedFlowInstanceMessageProvider provider);
	
	public List<SavedFlowInstanceMessageProvider> getProviders();
	
	public List<String> getMessages(Integer flowFamilyID, String statusName);
}
