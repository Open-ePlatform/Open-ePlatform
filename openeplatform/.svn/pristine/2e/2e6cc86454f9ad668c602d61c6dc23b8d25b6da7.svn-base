package com.nordicpeak.flowengine.beans;

import se.unlogic.standardutils.dao.annotations.DAOManaged;
import se.unlogic.standardutils.dao.annotations.Key;
import se.unlogic.standardutils.dao.annotations.Table;
import se.unlogic.standardutils.xml.GeneratedElementable;
import se.unlogic.standardutils.xml.XMLElement;

@Table(name = "flowengine_api_source_tags")
@XMLElement
public class APISourceTag extends GeneratedElementable {

	@Key
	@DAOManaged
	@XMLElement
	private Integer tagID;

	@Key
	@DAOManaged
	@XMLElement
	private String value;

	public APISourceTag() {}

	public APISourceTag(String value) {

		super();
		this.value = value;
	}

	public Integer getTagID() {

		return tagID;
	}

	public void setTagID(Integer tagID) {

		this.tagID = tagID;
	}

	public String getValue() {

		return value;
	}

	public void setValue(String value) {

		this.value = value;
	}

}
