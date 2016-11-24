package com.nordicpeak.childrelationprovider;

import java.io.Serializable;

import se.unlogic.standardutils.xml.GeneratedElementable;
import se.unlogic.standardutils.xml.XMLElement;

public class BasePerson extends GeneratedElementable implements Serializable {

	private static final long serialVersionUID = 3281488377667875545L;

	@XMLElement
	protected String firstname;

	@XMLElement
	protected String lastname;

	@XMLElement
	protected String citizenIdentifier;

	@XMLElement
	protected String address;

	@XMLElement
	protected String zipCode;

	@XMLElement
	protected String postalAddress;

	public String getFirstname() {

		return firstname;
	}

	public void setFirstname(String firstname) {

		this.firstname = firstname;
	}

	public String getLastname() {

		return lastname;
	}

	public void setLastname(String lastname) {

		this.lastname = lastname;
	}

	public String getCitizenIdentifier() {

		return citizenIdentifier;
	}

	public void setCitizenIdentifier(String citizenIdentifier) {

		this.citizenIdentifier = citizenIdentifier;
	}

	public String getAddress() {

		return address;
	}

	public void setAddress(String address) {

		this.address = address;
	}

	public String getZipCode() {

		return zipCode;
	}

	public void setZipCode(String zipCode) {

		this.zipCode = zipCode;
	}

	public String getPostalAddress() {

		return postalAddress;
	}

	public void setPostalAddress(String postalAddress) {

		this.postalAddress = postalAddress;
	}
}
