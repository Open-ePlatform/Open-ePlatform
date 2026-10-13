package com.nordicpeak.flowengine.accesscontrollers;

import javax.servlet.http.HttpSession;

import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.hierarchy.core.exceptions.AccessDeniedException;
import se.unlogic.openhierarchy.foregroundmodules.siteprofile.interfaces.SiteProfile;

import com.nordicpeak.flowengine.FlowBrowserModule;
import com.nordicpeak.flowengine.beans.Flow;
import com.nordicpeak.flowengine.interfaces.FlowInstanceAccessController;
import com.nordicpeak.flowengine.interfaces.ImmutableFlowInstance;
import com.nordicpeak.flowengine.utils.FlowInstanceUtils;


public class FlowBrowserAccessController implements FlowInstanceAccessController {

	private final FlowBrowserModule flowBrowserModule;
	private final HttpSession session;

	public FlowBrowserAccessController(FlowBrowserModule flowBrowserModule, HttpSession session) {

		this.flowBrowserModule = flowBrowserModule;
		this.session = session;
	}

	@Override
	public void checkNewFlowInstanceAccess(Flow flow, User user, SiteProfile profile) throws AccessDeniedException {

		flowBrowserModule.checkNewFlowInstanceAccess(flow, user, profile);
	}
	
	@Override
	public void checkFlowInstanceAccess(ImmutableFlowInstance flowInstance, User user) throws AccessDeniedException {
		
		flowBrowserModule.checkFlowInstanceTypeAccess(flowInstance, user);
		
		if (flowInstance.getOwners() == null && FlowInstanceUtils.getLatestSubmitEvent(flowInstance) == null) {
			
			SessionAccessController.checkFlowInstanceAccess(flowInstance, user, session, FlowBrowserModule.SESSION_ACCESS_CONTROLLER_TAG);
			
		} else {
			
			flowBrowserModule.checkFlowInstanceAccess(flowInstance, user);
		}
	}

	@Override
	public boolean isMutable(ImmutableFlowInstance flowInstance, User user) {

		return flowBrowserModule.isMutable(flowInstance, user);
	}
}
