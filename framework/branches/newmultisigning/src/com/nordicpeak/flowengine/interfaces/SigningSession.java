package com.nordicpeak.flowengine.interfaces;

import java.sql.Timestamp;
import java.util.List;

public interface SigningSession {
	
	public Integer getSessionID();
	
	public Integer getFlowInstanceID();
	
	public Timestamp getAdded();
	
	public List<? extends SigningSessionParty> getSigningParties();
	
	public boolean isFullySigned();
}
