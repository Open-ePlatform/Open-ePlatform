package com.nordicpeak.flowengine.interfaces;

import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;

import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.openhierarchy.foregroundmodules.siteprofile.interfaces.SiteProfile;

import com.nordicpeak.flowengine.SigningConfirmedResponse;
import com.nordicpeak.flowengine.beans.FlowInstanceEvent;
import com.nordicpeak.flowengine.exceptions.flow.FlowDefaultStatusNotFound;
import com.nordicpeak.flowengine.exceptions.flowinstancemanager.FlowInstanceManagerClosedException;
import com.nordicpeak.flowengine.exceptions.queryinstance.UnableToSaveQueryInstanceException;
import com.nordicpeak.flowengine.managers.MutableFlowInstanceManager;


public interface SigningCallback{

	SigningConfirmedResponse signingConfirmed(MutableFlowInstanceManager instanceManager, HttpServletRequest req, User user) throws FlowInstanceManagerClosedException, UnableToSaveQueryInstanceException, SQLException, FlowDefaultStatusNotFound;

	void signingComplete(MutableFlowInstanceManager instanceManager, FlowInstanceEvent event, HttpServletRequest req) throws FlowInstanceManagerClosedException, UnableToSaveQueryInstanceException, FlowDefaultStatusNotFound, SQLException;

	void abortSigning(MutableFlowInstanceManager instanceManager);

	String getSignFailURL(MutableFlowInstanceManager instanceManager, HttpServletRequest req);

	String getSignSuccessURL(MutableFlowInstanceManager instanceManager, HttpServletRequest req);

	String getSigningURL(MutableFlowInstanceManager instanceManager, HttpServletRequest req);

	SiteProfile getSiteProfile();
	
	String getActionID();
}
