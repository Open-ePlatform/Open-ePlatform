package com.nordicpeak.childrelationprovider;

import java.io.Serializable;
import java.util.List;

import se.unlogic.standardutils.xml.Elementable;
import se.unlogic.standardutils.xml.XMLElement;

@XMLElement
public interface Child extends Serializable, Elementable {

	public String getFirstname();

	public String getLastname();

	public String getCitizenIdentifier();

	public List<Guardian> getGuardians();
	
	public String getAddress();

	public String getZipCode();

	public String getPostalAddress();

	public String getMunicipalityCode();
}