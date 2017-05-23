package com.nordicpeak.flowengine.interfaces;

import se.unlogic.standardutils.beans.Named;
import se.unlogic.standardutils.xml.Elementable;


public interface ImmutableAlternative extends Elementable, Named{

	public Integer getAlternativeID();

	public String getName();

	public Integer getSortIndex();
	
	public String getValue();
}
