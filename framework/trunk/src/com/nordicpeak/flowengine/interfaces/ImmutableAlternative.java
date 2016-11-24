package com.nordicpeak.flowengine.interfaces;

import se.unlogic.standardutils.xml.Elementable;


public interface ImmutableAlternative extends Elementable{

	public Integer getAlternativeID();

	public String getName();

	public Integer getSortIndex();
	
}
