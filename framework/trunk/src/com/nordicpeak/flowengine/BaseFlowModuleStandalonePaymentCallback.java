package com.nordicpeak.flowengine;

import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;

import se.unlogic.hierarchy.core.beans.User;

import com.nordicpeak.flowengine.exceptions.flow.FlowDefaultStatusNotFound;
import com.nordicpeak.flowengine.exceptions.flowinstancemanager.FlowInstanceManagerClosedException;
import com.nordicpeak.flowengine.exceptions.queryinstance.UnableToSaveQueryInstanceException;
import com.nordicpeak.flowengine.interfaces.StandalonePaymentCallback;
import com.nordicpeak.flowengine.managers.ImmutableFlowInstanceManager;


public class BaseFlowModuleStandalonePaymentCallback implements StandalonePaymentCallback {

	private final BaseFlowModule baseFlowModule;
	private final String actionID;
	
	public BaseFlowModuleStandalonePaymentCallback(BaseFlowModule baseFlowModule, String actionID) {

		super();
		this.baseFlowModule = baseFlowModule;
		this.actionID = actionID;
	}
	
	@Override
	public void paymentComplete(ImmutableFlowInstanceManager instanceManager, HttpServletRequest req, User user) throws FlowInstanceManagerClosedException, UnableToSaveQueryInstanceException, FlowDefaultStatusNotFound, SQLException {

		baseFlowModule.standalonePaymentComplete(instanceManager, req, user, actionID);
	}

	@Override
	public String getPaymentFailURL(ImmutableFlowInstanceManager instanceManager, HttpServletRequest req) {

		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getPaymentSuccessURL(ImmutableFlowInstanceManager instanceManager, HttpServletRequest req) {

		return baseFlowModule.getPaymentSuccessURL(instanceManager, req);
	}

	@Override
	public String getPaymentURL(ImmutableFlowInstanceManager instanceManager, HttpServletRequest req) {

		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getActionID() {

		return actionID;
	}

}
