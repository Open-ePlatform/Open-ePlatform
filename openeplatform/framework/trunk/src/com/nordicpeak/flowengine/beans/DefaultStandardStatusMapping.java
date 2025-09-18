package com.nordicpeak.flowengine.beans;

import java.io.Serializable;

import se.unlogic.standardutils.dao.annotations.DAOManaged;
import se.unlogic.standardutils.dao.annotations.Key;
import se.unlogic.standardutils.dao.annotations.ManyToOne;
import se.unlogic.standardutils.dao.annotations.Table;
import se.unlogic.standardutils.xml.GeneratedElementable;
import se.unlogic.standardutils.xml.XMLElement;

@Table(name="flowengine_default_standard_statuses")
@XMLElement
public class DefaultStandardStatusMapping extends GeneratedElementable implements Serializable{

	private static final long serialVersionUID = -5747962435866086916L;

	@DAOManaged
	@Key
	@XMLElement
	private String actionID;

	@DAOManaged(columnName="statusID")
	@ManyToOne
	@XMLElement
	private StandardStatus status;

	public DefaultStandardStatusMapping() {}

	public DefaultStandardStatusMapping(String actionID, StandardStatus status) {

		super();
		this.actionID = actionID;
		this.status = status;
	}

	public String getActionID() {

		return actionID;
	}

	public void setActionID(String actionID) {

		this.actionID = actionID;
	}

	public StandardStatus getStatus() {

		return status;
	}

	public void setStatus(StandardStatus status) {

		this.status = status;
	}
}
