package com.nordicpeak.flowengine.flowinstancerafflesummary;

import java.io.Serializable;

import javax.servlet.http.HttpSession;

import com.nordicpeak.flowengine.beans.FlowInstance;
import com.nordicpeak.flowengine.managers.FlowInstanceManager;
import com.nordicpeak.flowengine.managers.MutableFlowInstanceManager;

public class AddFlowInstanceCallback implements com.nordicpeak.flowengine.addflowinstance.AddFlowInstanceCallback, Serializable {
	
	private static final long serialVersionUID = -1518909826970607855L;

	@Override
	public void beforeSave(MutableFlowInstanceManager instanceManager) {
		
		FlowInstance flowInstance = (FlowInstance) instanceManager.getFlowInstance();
		flowInstance.getAttributeHandler().setAttribute("manual", "true");
	}

	@Override
	public String getReturnURL(FlowInstanceManager instanceManager) {
		return null;
	}

	@Override
	public void afterSave(FlowInstanceManager instanceManager, HttpSession session) {
	}
	
}
