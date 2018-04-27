package com.nordicpeak.flowengine;

import java.sql.SQLException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.openhierarchy.foregroundmodules.siteprofile.interfaces.SiteProfile;

import com.nordicpeak.flowengine.exceptions.flow.FlowDefaultStatusNotFound;
import com.nordicpeak.flowengine.exceptions.flowinstancemanager.FlowInstanceManagerClosedException;
import com.nordicpeak.flowengine.exceptions.queryinstance.UnableToSaveQueryInstanceException;
import com.nordicpeak.flowengine.interfaces.StandalonePaymentCallback;
import com.nordicpeak.flowengine.managers.ImmutableFlowInstanceManager;


public class BaseFlowModuleStandalonePaymentCallback implements StandalonePaymentCallback {

	private final BaseFlowModule baseFlowModule;
	private final String actionID;
	private final SiteProfile siteProfile;
	
	public BaseFlowModuleStandalonePaymentCallback(BaseFlowModule baseFlowModule, SiteProfile siteProfile, String actionID) {

		super();
		this.baseFlowModule = baseFlowModule;
		this.actionID = actionID;
		this.siteProfile = siteProfile;
	}
	
	@Override
	public void paymentComplete(ImmutableFlowInstanceManager instanceManager, HttpServletRequest req, User user, boolean addPaymentEvent, String details, Map<String,String> eventAttributes) throws FlowInstanceManagerClosedException, UnableToSaveQueryInstanceException, FlowDefaultStatusNotFound, SQLException {

		baseFlowModule.standalonePaymentComplete(instanceManager, req, user, siteProfile, actionID, addPaymentEvent, details, eventAttributes);
	}

	@Override
	public String getPaymentSuccessURL(ImmutableFlowInstanceManager instanceManager, HttpServletRequest req) {
		
		return baseFlowModule.getPaymentSuccessURL(instanceManager, req);
	}
	
	@Override
	public String getPaymentFailURL(ImmutableFlowInstanceManager instanceManager, HttpServletRequest req) {

		return baseFlowModule.getStandalonePaymentURL(instanceManager, req);
	}

	@Override
	public String getPaymentURL(ImmutableFlowInstanceManager instanceManager, HttpServletRequest req) {

		return baseFlowModule.getStandalonePaymentURL(instanceManager, req);
	}

	@Override
	public String getActionID() {

		return actionID;
	}

}
