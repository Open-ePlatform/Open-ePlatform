package com.nordicpeak.childrelationprovider;

import java.io.Serializable;

import se.unlogic.standardutils.xml.Elementable;

public interface Guardian extends Serializable, Elementable {

	public String getFirstname();

	public String getLastname();

	public String getCitizenIdentifier();

	public String getAddress();

	public String getZipCode();

	public String getPostalAddress();
}
