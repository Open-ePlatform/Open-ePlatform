package com.nordicpeak.flowengine.interfaces;

import java.util.Map;

import se.unlogic.hierarchy.core.beans.User;

public interface SigningResponse {
	
	public User getUser();
	
	public Map<String, String> getSigningAttributes();
}
