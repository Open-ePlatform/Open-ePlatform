package com.nordicpeak.flowengine.beans;

import java.util.Map;

import se.unlogic.hierarchy.core.beans.User;

import com.nordicpeak.flowengine.interfaces.SigningResponse;

public class SimpleSigningResponse implements SigningResponse {
	
	private final User user;
	private final Map<String, String> signingAttributes;
	
	public SimpleSigningResponse(User user, Map<String, String> signingAttributes) {
		super();
		this.user = user;
		this.signingAttributes = signingAttributes;
	}
	
	public User getUser() {
		return user;
	}
	
	@Override
	public Map<String, String> getSigningAttributes() {
		return signingAttributes;
	}
	
}
