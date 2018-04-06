package com.nordicpeak.flowengine.interfaces;

import java.sql.Timestamp;

import javax.servlet.http.HttpServletRequest;

import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.hierarchy.core.interfaces.ViewFragment;
import se.unlogic.webutils.http.URIParser;

import com.nordicpeak.flowengine.beans.SigningParty;
import com.nordicpeak.flowengine.managers.ImmutableFlowInstanceManager;


public interface MultiSigningHandler {

	ViewFragment getSigningStatus(HttpServletRequest req, User user, URIParser uriParser, ImmutableFlowInstanceManager immutableFlowInstanceManager) throws Exception;
	
	public String getSigningURL(ImmutableFlowInstance flowInstance, SigningParty signingParty);
	
	public boolean partyHasSigned(Integer flowInstanceID, SigningParty signingParty, Timestamp latestSubmitTimestamp) throws Exception;
	
}
