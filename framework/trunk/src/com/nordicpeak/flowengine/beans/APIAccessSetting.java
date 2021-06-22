package com.nordicpeak.flowengine.beans;

import se.unlogic.standardutils.dao.annotations.DAOManaged;
import se.unlogic.standardutils.dao.annotations.Key;
import se.unlogic.standardutils.dao.annotations.Table;
import se.unlogic.standardutils.xml.GeneratedElementable;
import se.unlogic.standardutils.xml.XMLElement;

@Table(name = "flowengine_api_access")
@XMLElement
public class APIAccessSetting extends GeneratedElementable {

	@DAOManaged
	@Key
	@XMLElement
	private Integer flowFamilyID;

	@DAOManaged
	@Key
	@XMLElement
	private Integer userID;
	
	public APIAccessSetting() {}

	public APIAccessSetting(Integer flowFamilyID, Integer userID) {
		super();
		this.flowFamilyID = flowFamilyID;
		this.userID = userID;
	}

	public Integer getFlowFamilyID() {
		return flowFamilyID;
	}

	public void setFlowFamilyID(Integer flowFamilyID) {
		this.flowFamilyID = flowFamilyID;
	}

	public Integer getUserID() {
		return userID;
	}

	public void setUserID(Integer userID) {
		this.userID = userID;
	}

	@Override
	public String toString() {

		return this.getClass().getSimpleName() + " (flowFamilyID: " + flowFamilyID + ", userID: " + userID + ")";
	}
}
