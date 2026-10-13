package com.nordicpeak.flowengine;

import java.sql.SQLException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.openhierarchy.foregroundmodules.siteprofile.interfaces.SiteProfile;

import com.nordicpeak.flowengine.exceptions.flow.FlowDefaultStatusNotFound;
import com.nordicpeak.flowengine.exceptions.flowinstancemanager.FlowInstanceManagerClosedException;
import com.nordicpeak.flowengine.exceptions.queryinstance.UnableToSaveQueryInstanceException;
import com.nordicpeak.flowengine.interfaces.FlowPaymentCallback;
import com.nordicpeak.flowengine.managers.FlowInstanceManager;


public class BaseFlowModuleStandalonePaymentCallback implements FlowPaymentCallback {

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
	public void paymentComplete(FlowInstanceManager instanceManager, HttpServletRequest req, User user, boolean addPaymentEvent, String details, Map<String,String> eventAttributes) throws FlowInstanceManagerClosedException, UnableToSaveQueryInstanceException, FlowDefaultStatusNotFound, SQLException {

		baseFlowModule.paymentComplete(instanceManager, req, user, siteProfile, actionID, addPaymentEvent, details, eventAttributes);
	}

	@Override
	public String getPaymentSuccessURL(FlowInstanceManager instanceManager, HttpServletRequest req) {
		
		return baseFlowModule.getPaymentSuccessURL(instanceManager, req);
	}
	
	@Override
	public String getPaymentFailURL(FlowInstanceManager instanceManager, HttpServletRequest req) {

		return baseFlowModule.getStandalonePaymentURL(instanceManager, req);
	}

	@Override
	public String getPaymentURL(FlowInstanceManager instanceManager, HttpServletRequest req) {

		return baseFlowModule.getStandalonePaymentURL(instanceManager, req);
	}

	@Override
	public String getActionID() {

		return actionID;
	}

}
