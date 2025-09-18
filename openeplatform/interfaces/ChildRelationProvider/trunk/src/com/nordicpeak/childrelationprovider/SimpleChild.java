package com.nordicpeak.childrelationprovider;

import java.util.List;

public class SimpleChild extends BasePerson implements Child {

	private static final long serialVersionUID = 5461893983752333815L;

	private String municipalityCode;
	
	private List<Guardian> guardians;

	public SimpleChild(){};
	
	public SimpleChild(String firstname, String lastname, String citizenIdentifier) {
		
		super();

		this.firstname = firstname;
		this.lastname = lastname;
		this.citizenIdentifier = citizenIdentifier;
	}
	
	public SimpleChild(String firstname, String lastname, String citizenIdentifier, String address, String zipcode, String postalAddress, String municipalityCode) {
		
		super();

		this.firstname = firstname;
		this.lastname = lastname;
		this.citizenIdentifier = citizenIdentifier;
		this.address = address;
		this.zipCode = zipcode;
		this.postalAddress = postalAddress;
		this.municipalityCode = municipalityCode;
	}
	
	@Override
	public List<Guardian> getGuardians() {

		return guardians;
	}

	public void setGuardians(List<Guardian> guardians) {

		this.guardians = guardians;
	}
	
	@Override
	public String getMunicipalityCode() {
	
		return municipalityCode;
	}

	public void setMunicipalityCode(String municipalityCode) {
	
		this.municipalityCode = municipalityCode;
	}
}
