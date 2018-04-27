package com.nordicpeak.flowengine.beans;

import javax.servlet.http.HttpServletRequest;

import com.nordicpeak.flowengine.interfaces.GenericSigningRequest;

public class SimpleSigningRequest implements GenericSigningRequest {
	
	private final String description;
	private final String dataToSign;
	private final String signingFormURL;
	private final String processSigningURL;
	
	public SimpleSigningRequest(String description, String dataToSign, String signingFormURL, String processSigningURL) {
		super();
		
		this.description = description;
		this.dataToSign = dataToSign;
		this.signingFormURL = signingFormURL;
		this.processSigningURL = processSigningURL;
	}
	
	@Override
	public String getDescription() {
		return description;
	}
	
	@Override
	public String getDataToSign() {
		return dataToSign;
	}

	@Override
	public String getSigningFormURL(HttpServletRequest req) {
		return signingFormURL;
	}

	@Override
	public String getProcessSigningURL(HttpServletRequest req) {
		return processSigningURL;
	}
	
}
