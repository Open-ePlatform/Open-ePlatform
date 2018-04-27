package com.nordicpeak.flowengine.interfaces;

import se.unlogic.standardutils.xml.XMLParser;

import com.nordicpeak.flowengine.enums.QueryState;

public interface MutableQueryDescriptor extends ImmutableQueryDescriptor{

	public XMLParser getImportParser();
	
	public void setName(String name);

	public void setDefaultQueryState(QueryState defaultQueryState);

	public void setExported(boolean exported);
}