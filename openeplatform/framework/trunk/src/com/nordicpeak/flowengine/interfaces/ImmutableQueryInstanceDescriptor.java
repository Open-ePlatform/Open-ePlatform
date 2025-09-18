package com.nordicpeak.flowengine.interfaces;

import se.unlogic.standardutils.xml.Elementable;

import com.nordicpeak.flowengine.enums.QueryState;

public interface ImmutableQueryInstanceDescriptor extends Elementable{

	public Integer getQueryInstanceID();
	
	public QueryState getQueryState();

	public boolean isPopulated();
	
	public ImmutableQueryDescriptor getQueryDescriptor();
	
	public Integer getFlowInstanceID();
}