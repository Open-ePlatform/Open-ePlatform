package com.nordicpeak.flowengine.interfaces;

import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;

import se.unlogic.hierarchy.core.beans.User;

import com.nordicpeak.flowengine.exceptions.flow.FlowDefaultStatusNotFound;
import com.nordicpeak.flowengine.exceptions.flowinstancemanager.FlowInstanceManagerClosedException;
import com.nordicpeak.flowengine.exceptions.queryinstance.UnableToSaveQueryInstanceException;
import com.nordicpeak.flowengine.managers.ImmutableFlowInstanceManager;


public interface StandalonePaymentCallback {

	void paymentComplete(ImmutableFlowInstanceManager instanceManager, HttpServletRequest req, User user) throws FlowInstanceManagerClosedException, UnableToSaveQueryInstanceException, FlowDefaultStatusNotFound, SQLException;
	
	String getPaymentFailURL(ImmutableFlowInstanceManager instanceManager, HttpServletRequest req);

	String getPaymentSuccessURL(ImmutableFlowInstanceManager instanceManager, HttpServletRequest req);

	String getPaymentURL(ImmutableFlowInstanceManager instanceManager, HttpServletRequest req);

	String getActionID(); 
	
}
