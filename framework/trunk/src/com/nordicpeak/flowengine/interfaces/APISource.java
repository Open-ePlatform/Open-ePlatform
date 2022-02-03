package com.nordicpeak.flowengine.interfaces;

import se.unlogic.standardutils.beans.Named;

public interface APISource extends Named{

	public Integer getEndpointID();
	
	public String getName();
	
	public String getAddress();
}
