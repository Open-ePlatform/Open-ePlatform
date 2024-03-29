package com.nordicpeak.flowengine.interfaces;

import java.sql.Timestamp;

import javax.servlet.http.HttpServletRequest;

import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.hierarchy.core.interfaces.ViewFragment;
import se.unlogic.webutils.http.URIParser;

import com.nordicpeak.flowengine.beans.FlowInstance;
import com.nordicpeak.flowengine.beans.SigningParty;


public interface MultiSigningHandler {

	ViewFragment getSigningStatusViewFragment(HttpServletRequest req, User user, URIParser uriParser, ImmutableFlowInstance flowInstance) throws Exception;
	
	public String getSigningURL(ImmutableFlowInstance flowInstance, SigningParty signingParty);
	
	public void cancelSigning(FlowInstance flowInstance) throws Exception;
	
	public boolean partyHasSigned(Integer flowInstanceID, SigningParty signingParty, Timestamp latestSubmitTimestamp) throws Exception;
	
	public boolean supportsSequentialSigning();
	
}
