package com.nordicpeak.flowengine.evaluators.baseevaluator;

import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.hierarchy.core.interfaces.EventHandler;
import se.unlogic.hierarchy.core.utils.AdvancedCRUDCallback;

import com.nordicpeak.flowengine.FlowAdminModule;

public interface BaseEvaluatorCRUDCallback extends AdvancedCRUDCallback<User> {

	public FlowAdminModule getFlowAdminModule();
	
	public EventHandler getEventHandler();
	
}
