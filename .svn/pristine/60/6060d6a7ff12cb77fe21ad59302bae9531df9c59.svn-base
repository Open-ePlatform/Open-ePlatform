package com.nordicpeak.flowengine.beans;

import se.unlogic.standardutils.dao.annotations.DAOManaged;
import se.unlogic.standardutils.dao.annotations.Key;
import se.unlogic.standardutils.dao.annotations.Table;

@Table(name = "flowengine_api_source_selected_tags")
public class APISourceSelectedTag {

	@Key
	@DAOManaged
	private Integer tagID;

	@Key
	@DAOManaged
	private Integer sourceID;

	@Key
	@DAOManaged
	private String sourceClassname;

	public APISourceSelectedTag() {}

	public APISourceSelectedTag(Integer tagID, Integer sourceID, String sourceClassname) {

		this.tagID = tagID;
		this.sourceID = sourceID;
		this.sourceClassname = sourceClassname;
	}

	public Integer getTagID() {

		return tagID;
	}

	public void setTagID(Integer tagID) {

		this.tagID = tagID;
	}

	public Integer getSourceID() {

		return sourceID;
	}

	public void setSourceID(Integer sourceID) {

		this.sourceID = sourceID;
	}

	public String getSourceClassname() {

		return sourceClassname;
	}

	public void setSourceClassname(String sourceClassname) {

		this.sourceClassname = sourceClassname;
	}

}
