package com.nordicpeak.flowengine.interfaces;

import java.util.List;

import se.unlogic.standardutils.xml.XMLParser;


public interface MutableEvaluatorDescriptor extends ImmutableEvaluatorDescriptor{

	public void setName(String name);

	public void setEnabled(boolean enabled);

	public void setTargetQueryIDs(List<Integer> targetQueryIDs);
	
	public XMLParser getImportParser();
}