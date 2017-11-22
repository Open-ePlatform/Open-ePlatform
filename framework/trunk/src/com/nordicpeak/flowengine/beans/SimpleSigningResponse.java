package com.nordicpeak.flowengine.beans;

import java.util.Map;

import com.nordicpeak.flowengine.interfaces.SigningResponse;

public class SimpleSigningResponse implements SigningResponse {
	
	private final Map<String, String> signingAttributes;
	
	public SimpleSigningResponse(Map<String, String> signingAttributes) {
		super();
		this.signingAttributes = signingAttributes;
	}
	
	@Override
	public Map<String, String> getSigningAttributes() {
		return signingAttributes;
	}
	
}
