package com.nordicpeak.flowengine.interfaces;

import se.unlogic.standardutils.beans.Named;
import se.unlogic.standardutils.xml.Elementable;

public interface APISourceAccountDescriptor extends Named, Elementable {

	public Integer getAccountID();
	
	public String getName();
	
	public String getDescription();

	public String getProviderID();
	
	public String getTypeDescription();

	public boolean isInUse();
	
}
