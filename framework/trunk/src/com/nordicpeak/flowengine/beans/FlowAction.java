package com.nordicpeak.flowengine.beans;

import se.unlogic.standardutils.dao.annotations.DAOManaged;
import se.unlogic.standardutils.dao.annotations.Key;
import se.unlogic.standardutils.dao.annotations.OrderBy;
import se.unlogic.standardutils.dao.annotations.Table;
import se.unlogic.standardutils.xml.GeneratedElementable;
import se.unlogic.standardutils.xml.XMLElement;

@Table(name = "flowengine_flow_actions")
@XMLElement
public class FlowAction extends GeneratedElementable {

	@DAOManaged
	@Key
	@XMLElement
	private String actionID;

	@DAOManaged
	@OrderBy
	@XMLElement
	private String name;

	@DAOManaged
	@XMLElement
	private boolean required;

	public String getActionID() {

		return actionID;
	}

	public void setActionID(String actionID) {

		this.actionID = actionID;
	}

	public String getName() {

		return name;
	}

	public void setName(String name) {

		this.name = name;
	}

	public boolean isRequired() {
		
		return required;
	}

	public void setRequired(boolean required) {
		
		this.required = required;
	}

}
