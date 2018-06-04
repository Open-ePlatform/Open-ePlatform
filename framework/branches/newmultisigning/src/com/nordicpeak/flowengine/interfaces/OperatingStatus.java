package com.nordicpeak.flowengine.interfaces;

import se.unlogic.standardutils.xml.Elementable;


public interface OperatingStatus extends Elementable {

	public String getMessage();
	
	public boolean isDisabled();
	
}
