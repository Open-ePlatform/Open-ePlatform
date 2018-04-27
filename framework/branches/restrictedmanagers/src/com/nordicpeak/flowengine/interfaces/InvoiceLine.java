package com.nordicpeak.flowengine.interfaces;

import se.unlogic.standardutils.xml.Elementable;
import se.unlogic.standardutils.xml.XMLElement;

@XMLElement
public interface InvoiceLine extends Elementable {

	public String getDescription();
	
	public int getQuanitity();
	
	public int getUnitPrice();
	
	public String getUnitMeasure();
	
}
