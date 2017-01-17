package com.nordicpeak.flowengine.interfaces;

import java.io.File;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;

import se.unlogic.hierarchy.core.beans.User;

import com.nordicpeak.flowengine.beans.FlowInstanceEvent;
import com.nordicpeak.flowengine.beans.SigningParty;
import com.nordicpeak.flowengine.managers.ImmutableFlowInstanceManager;


public interface MultiSigningCallback {

	public void signingComplete(ImmutableFlowInstanceManager instanceManager, FlowInstanceEvent event, SigningParty signingParty, User user, HttpServletRequest req) throws SQLException;

	public void abortSigning(ImmutableFlowInstanceManager instanceManager);

	public String getSignFailURL(ImmutableFlowInstanceManager instanceManager, HttpServletRequest req);

	public String getSignSuccessURL(ImmutableFlowInstanceManager instanceManager, HttpServletRequest req);

	public String getSigningURL(ImmutableFlowInstanceManager instanceManager, HttpServletRequest req);

	public File getSigningPDF(ImmutableFlowInstanceManager instanceManager);
	
	public String getSigningChainID(ImmutableFlowInstanceManager instanceManager);
	
}
