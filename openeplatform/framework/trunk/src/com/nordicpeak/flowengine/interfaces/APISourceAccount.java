package com.nordicpeak.flowengine.interfaces;

import se.unlogic.standardutils.beans.Named;

public interface APISourceAccount extends Named {

	public Integer getAccountID();
	
	public APISourceAccountDescriptor getDescriptor();
	
	public void init(APISourceAccountDescriptor descriptor, String configURL);
	
	public boolean isConfigured();
	
}
