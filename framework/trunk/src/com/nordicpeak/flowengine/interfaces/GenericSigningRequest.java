package com.nordicpeak.flowengine.interfaces;

import javax.servlet.http.HttpServletRequest;

public interface GenericSigningRequest {
	
	String getDescription();
	
	String getHashToSign();
	
	String getSigningFormURL(HttpServletRequest req);
	
	String getProcessSigningURL(HttpServletRequest req);
	
}
