package com.nordicpeak.flowengine.beans;

import com.nordicpeak.flowengine.interfaces.ImmutableQueryInstance;

public class SubmitCheckFailedResponse {
	
	
	private final ImmutableQueryInstance queryInstance;
	private String redirectURL;
	
	public SubmitCheckFailedResponse(ImmutableQueryInstance queryInstance, String redirectURL) {
		super();
		this.queryInstance = queryInstance;
		this.redirectURL = redirectURL;
	}
	
	public String getRedirectURL() {
		return redirectURL;
	}
	
	public void setRedirectURL(String redirectURL) {
		this.redirectURL = redirectURL;
	}
	
	public ImmutableQueryInstance getQueryInstance() {
		return queryInstance;
	}
	
}
