package com.nordicpeak.flowengine.attachments.beans;

import java.io.Serializable;

import se.unlogic.standardutils.dao.annotations.DAOManaged;
import se.unlogic.standardutils.dao.annotations.Key;
import se.unlogic.standardutils.dao.annotations.Table;
import se.unlogic.standardutils.xml.GeneratedElementable;
import se.unlogic.standardutils.xml.XMLElement;

@Table(name = "flowengine_flow_instance_attachments_settings")
@XMLElement
public class FlowInstanceAttachmentsSettings extends GeneratedElementable implements Serializable {

	private static final long serialVersionUID = -7196741621029300739L;

	@DAOManaged
	@XMLElement
	@Key
	private Integer flowFamilyID;

	@DAOManaged
	@XMLElement
	private boolean moduleEnabled;

	@DAOManaged
	@XMLElement
	private boolean userEmailEnabled;

	@DAOManaged
	@XMLElement
	private boolean userSmsEnabled;

	@DAOManaged
	@XMLElement
	private boolean managerEmailEnabled;

	@DAOManaged
	@XMLElement
	private boolean allowUserUpload;

	public Integer getFlowFamilyID() {

		return flowFamilyID;
	}

	public void setFlowFamilyID(Integer flowFamilyID) {

		this.flowFamilyID = flowFamilyID;
	}

	public boolean isModuleEnabled() {

		return moduleEnabled;
	}

	public void setModuleEnabled(boolean moduleEnabled) {

		this.moduleEnabled = moduleEnabled;
	}

	public boolean isUserEmailEnabled() {

		return userEmailEnabled;
	}

	public void setUserEmailEnabled(boolean emailEnabled) {

		this.userEmailEnabled = emailEnabled;
	}

	public boolean isUserSmsEnabled() {

		return userSmsEnabled;
	}

	public void setUserSmsEnabled(boolean smsEnabled) {

		this.userSmsEnabled = smsEnabled;
	}

	public boolean isManagerEmailEnabled() {

		return managerEmailEnabled;
	}

	public void setManagerEmailEnabled(boolean managerEmailEnabled) {

		this.managerEmailEnabled = managerEmailEnabled;
	}

	public boolean isAllowUserUpload() {

		return allowUserUpload;
	}

	public void setAllowUserUpload(boolean allowUserUpload) {

		this.allowUserUpload = allowUserUpload;
	}

	@Override
	public String toString() {

		return "FlowInstanceAttachmentsSettings [flowFamilyID=" + flowFamilyID + ", moduleEnabled=" + moduleEnabled + ", emailEnabled=" + userEmailEnabled + ", smsEnabled=" + userSmsEnabled + "]";
	}

}