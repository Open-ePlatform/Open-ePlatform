package com.nordicpeak.flowengine.interfaces;

import org.w3c.dom.Document;

import se.unlogic.standardutils.xml.Elementable;



public interface Query extends Elementable{

	public ImmutableQueryDescriptor getQueryDescriptor();

	public String getConfigAlias();

	public String getXSDTypeName();

	public void toXSD(Document doc);
}
