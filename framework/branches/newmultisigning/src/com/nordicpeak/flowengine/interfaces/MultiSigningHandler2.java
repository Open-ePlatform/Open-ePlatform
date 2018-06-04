package com.nordicpeak.flowengine.interfaces;

import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;

import com.nordicpeak.flowengine.beans.FlowInstanceEvent;
import com.nordicpeak.flowengine.managers.FlowInstanceManager;

import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.hierarchy.core.interfaces.ViewFragment;
import se.unlogic.webutils.http.URIParser;


public interface MultiSigningHandler2 {

	public SigningSession createSigningSession(FlowInstanceManager instanceManager, FlowInstanceEvent pdfEvent, boolean sequentialSigning, String fullContextPath) throws SQLException;
	
	public SigningSession getSigningSession(Integer flowInstanceID) throws SQLException;
	
	ViewFragment getSigningStatusViewFragment(HttpServletRequest req, User user, URIParser uriParser, ImmutableFlowInstance flowInstance) throws Exception;
	
	public String getSigningURL(ImmutableFlowInstance flowInstance, SigningSession signingSession, SigningSessionParty signingParty, String fullContextPath);
	
}
