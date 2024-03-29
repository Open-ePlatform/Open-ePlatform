package com.nordicpeak.flowengine.beans;

import java.sql.Timestamp;

import se.unlogic.standardutils.dao.annotations.DAOManaged;
import se.unlogic.standardutils.dao.annotations.Key;
import se.unlogic.standardutils.dao.annotations.Table;
import se.unlogic.standardutils.xml.GeneratedElementable;
import se.unlogic.standardutils.xml.XMLElement;

@Table(name = "flowengine_external_flow_redirects")
@XMLElement
public class ExternalFlowRedirect extends GeneratedElementable {

	@Key
	@DAOManaged(autoGenerated = true)
	@XMLElement
	private Integer redirectID;

	@DAOManaged
	@XMLElement
	private Integer flowID;

	@DAOManaged
	@XMLElement
	private Timestamp time;

	public ExternalFlowRedirect(Integer flowID, Timestamp time) {

		this.flowID = flowID;
		this.time = time;
	}

	public Integer getRedirectID() {

		return redirectID;
	}

	public void setRedirectID(Integer redirectID) {

		this.redirectID = redirectID;
	}

	public Integer getFlowID() {

		return flowID;
	}

	public void setFlowID(Integer flowID) {

		this.flowID = flowID;
	}

	public Timestamp getTime() {

		return time;
	}

	public void setTime(Timestamp time) {

		this.time = time;
	}

}
