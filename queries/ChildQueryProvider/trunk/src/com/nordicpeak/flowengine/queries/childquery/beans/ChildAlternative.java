package com.nordicpeak.flowengine.queries.childquery.beans;

import java.io.Serializable;

import se.unlogic.standardutils.xml.GeneratedElementable;
import se.unlogic.standardutils.xml.XMLElement;

import com.nordicpeak.flowengine.interfaces.ImmutableAlternative;

@XMLElement
public class ChildAlternative extends GeneratedElementable implements ImmutableAlternative, Serializable {

	private static final long serialVersionUID = -6799049641623354471L;

	@XMLElement
	private final Integer alternativeID;

	@XMLElement
	private String name;

	@XMLElement
	private final Integer sortIndex;

	public ChildAlternative(String name, Integer alternativeID, Integer sortIndex) {

		this.alternativeID = alternativeID;
		this.name = name;
		this.sortIndex = sortIndex;
	}

	@Override
	public Integer getAlternativeID() {

		return alternativeID;
	}

	@Override
	public String getName() {

		return name;
	}

	public Integer getSortIndex() {

		return sortIndex;
	}

	@Override
	public String getExportXMLValue() {
		return null;
	}

	@Override
	public String getAttributeValue() {
		return null;
	}
}
