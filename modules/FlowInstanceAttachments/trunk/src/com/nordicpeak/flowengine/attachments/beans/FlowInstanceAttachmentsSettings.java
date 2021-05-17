package com.nordicpeak.flowengine.attachments.beans;

import java.io.Serializable;

import se.unlogic.standardutils.dao.annotations.DAOManaged;
import se.unlogic.standardutils.dao.annotations.Key;
import se.unlogic.standardutils.dao.annotations.Table;
import se.unlogic.standardutils.xml.GeneratedElementable;
import se.unlogic.standardutils.xml.XMLElement;

@Table(name = "flowengine_flow_instance_attachments_settings")
@XMLElement
public class FlowInstanceAttachmentsSettings extends GeneratedElementable  implements Serializable{

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
	private boolean emailEnabled;
	
	@DAOManaged
	@XMLElement
	private boolean smsEnabled;

	
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

	
	public boolean isEmailEnabled() {
	
		return emailEnabled;
	}

	
	public void setEmailEnabled(boolean emailEnabled) {
	
		this.emailEnabled = emailEnabled;
	}

	
	public boolean isSmsEnabled() {
	
		return smsEnabled;
	}

	
	public void setSmsEnabled(boolean smsEnabled) {
	
		this.smsEnabled = smsEnabled;
	}


	@Override
	public String toString() {

		return "FlowInstanceAttachmentsSettings [flowFamilyID=" + flowFamilyID + ", moduleEnabled=" + moduleEnabled + ", emailEnabled=" + emailEnabled + ", smsEnabled=" + smsEnabled + "]";
	}

	
	
	
}