package com.nordicpeak.flowengine.savedmessagehandler;

import se.unlogic.hierarchy.core.interfaces.Prioritized;

public interface SavedFlowInstanceMessageProvider extends Prioritized {

	String getSavedInstanceMessage(Integer flowFamilyID, String statusName) throws Exception;
}
