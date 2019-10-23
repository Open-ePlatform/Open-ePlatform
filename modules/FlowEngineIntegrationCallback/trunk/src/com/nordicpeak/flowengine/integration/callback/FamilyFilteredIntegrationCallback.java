package com.nordicpeak.flowengine.integration.callback;

import java.util.List;

import se.unlogic.hierarchy.core.annotations.ModuleSetting;
import se.unlogic.hierarchy.core.annotations.TextAreaSettingDescriptor;

import com.nordicpeak.flowengine.beans.FlowInstance;
import com.nordicpeak.flowengine.integration.callback.exceptions.FlowInstanceNotFound;
import com.nordicpeak.flowengine.integration.callback.exceptions.FlowInstanceNotFoundException;

public class FamilyFilteredIntegrationCallback extends StandardIntegrationCallback {

	@ModuleSetting
	@TextAreaSettingDescriptor(name="Allowed flow families", description="Flow families that are allowed to be accessed via this module")
	protected List<Integer> allowedFlowFamilies;
	
	@Override
	protected FlowInstance getFlowInstance(Integer flowInstanceID, ExternalID externalID) throws FlowInstanceNotFoundException {

		FlowInstance flowInstance = super.getFlowInstance(flowInstanceID, externalID);
		
		if(allowedFlowFamilies == null || !allowedFlowFamilies.contains(flowInstance.getFlow().getFlowFamily().getFlowFamilyID())){
			
			log.warn("Access denied to flow instance " + flowInstance + " for user " + callback.getUser());
			throw new FlowInstanceNotFoundException("The requested flow instance was not found", new FlowInstanceNotFound());	
		}
		
		return flowInstance;
	}
}
