package com.nordicpeak.teis.beans;

import se.unlogic.standardutils.annotations.RequiredIfSet;
import se.unlogic.standardutils.annotations.WebPopulate;
import se.unlogic.standardutils.dao.annotations.DAOManaged;
import se.unlogic.standardutils.dao.annotations.Key;
import se.unlogic.standardutils.dao.annotations.Table;
import se.unlogic.standardutils.xml.GeneratedElementable;
import se.unlogic.standardutils.xml.XMLElement;

@Table(name = "teis_integration_configuration")
@XMLElement
public class IntegrationConfiguration extends GeneratedElementable {

	@DAOManaged
	@Key
	private Integer flowID;

	@DAOManaged
	@WebPopulate
	@XMLElement
	private boolean enabled;

	@DAOManaged
	@WebPopulate(maxLength=255)
	@RequiredIfSet(paramNames="enabled")
	@XMLElement
	private String receiverAddress;

	@DAOManaged
	@WebPopulate(maxLength=255)
	@RequiredIfSet(paramNames="enabled")
	@XMLElement
	private String serviceType;

	@DAOManaged
	@WebPopulate
	@XMLElement
	private boolean supportsExternalMessages;	
	
	@DAOManaged
	@WebPopulate
	@XMLElement
	private boolean testflag;

	public Integer getFlowID() {

		return flowID;
	}

	public void setFlowID(Integer flowID) {

		this.flowID = flowID;
	}

	public boolean isEnabled() {

		return enabled;
	}

	public void setEnabled(boolean enabled) {

		this.enabled = enabled;
	}

	public String getReceiverAddress() {

		return receiverAddress;
	}

	public void setReceiverAddress(String receiverAddress) {

		this.receiverAddress = receiverAddress;
	}

	public String getServiceType() {

		return serviceType;
	}

	public void setServiceType(String serviceType) {

		this.serviceType = serviceType;
	}

	public boolean isTestflag() {

		return testflag;
	}

	public void setTestflag(boolean testflag) {

		this.testflag = testflag;
	}

	
	public boolean supportsExternalMessages() {
	
		return supportsExternalMessages;
	}

	
	public void setSupportsExternalMessages(boolean supportsExternalMessages) {
	
		this.supportsExternalMessages = supportsExternalMessages;
	}
}
