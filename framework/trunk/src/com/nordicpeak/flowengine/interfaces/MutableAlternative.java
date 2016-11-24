package com.nordicpeak.flowengine.interfaces;

import se.unlogic.standardutils.xml.XMLParserPopulateable;

public interface MutableAlternative extends ImmutableAlternative, XMLParserPopulateable {

	public void setAlternativeID(Integer alternativeID);

	public void setName(String name);

	public void setSortIndex(Integer sortIndex);
}
