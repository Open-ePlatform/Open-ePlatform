package com.nordicpeak.flowengine.flowinstancerafflesummary;

import java.io.Serializable;

import javax.servlet.http.HttpSession;

import se.unlogic.hierarchy.core.interfaces.attributes.MutableAttributeHandler;

import com.nordicpeak.flowengine.Constants;
import com.nordicpeak.flowengine.addflowinstance.AddFlowInstanceModule;
import com.nordicpeak.flowengine.beans.FlowInstance;
import com.nordicpeak.flowengine.beans.FlowInstanceEvent;
import com.nordicpeak.flowengine.enums.EventType;
import com.nordicpeak.flowengine.managers.FlowInstanceManager;
import com.nordicpeak.flowengine.managers.MutableFlowInstanceManager;

public class AddFlowInstanceCallback implements com.nordicpeak.flowengine.addflowinstance.AddFlowInstanceCallback, Serializable {
	
	private static final long serialVersionUID = -1518909826970607855L;

	@Override
	public void createdFlowInstance(MutableFlowInstanceManager instanceManager) {}
	
	@Override
	public void beforeSave(MutableFlowInstanceManager instanceManager, EventType eventType) {
		
		FlowInstance flowInstance = (FlowInstance) instanceManager.getFlowInstance();
		MutableAttributeHandler flowInstanceAttributeHandler = flowInstance.getAttributeHandler();
		
		flowInstanceAttributeHandler.setAttribute("manual", "true");
		flowInstanceAttributeHandler.setAttribute(Constants.FLOW_INSTANCE_ADDED_BY_MANAGER_ATTRIBUTE, "true");
	}

	@Override
	public void afterSave(FlowInstanceManager instanceManager, EventType eventType, HttpSession session, FlowInstanceEvent saveEvent, AddFlowInstanceModule addFlowInstanceModule) {}
	
	@Override
	public String getCustomReturnURL(FlowInstanceManager instanceManager) {
		return null;
	}

}
