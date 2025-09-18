package com.nordicpeak.flowengine.interfaces;

import java.sql.SQLException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import se.unlogic.hierarchy.core.beans.User;

import com.nordicpeak.flowengine.exceptions.flow.FlowDefaultStatusNotFound;
import com.nordicpeak.flowengine.exceptions.flowinstancemanager.FlowInstanceManagerClosedException;
import com.nordicpeak.flowengine.exceptions.queryinstance.UnableToSaveQueryInstanceException;
import com.nordicpeak.flowengine.managers.FlowInstanceManager;


public interface FlowPaymentCallback {

	void paymentComplete(FlowInstanceManager instanceManager, HttpServletRequest req, User user, boolean addPaymentEvent, String details, Map<String,String> eventAttributes) throws FlowInstanceManagerClosedException, UnableToSaveQueryInstanceException, FlowDefaultStatusNotFound, SQLException;
	
	String getPaymentFailURL(FlowInstanceManager instanceManager, HttpServletRequest req);

	String getPaymentSuccessURL(FlowInstanceManager instanceManager, HttpServletRequest req);

	String getPaymentURL(FlowInstanceManager instanceManager, HttpServletRequest req);

	String getActionID();
	
}
