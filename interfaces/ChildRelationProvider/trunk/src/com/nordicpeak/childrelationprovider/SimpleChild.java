package com.nordicpeak.childrelationprovider;

import java.util.List;

public class SimpleChild extends BasePerson implements Child {

	private static final long serialVersionUID = 5461893983752333815L;

	private boolean secrecy;
	
	private List<Guardian> guardians;

	public SimpleChild(){};
	
	public SimpleChild(String firstname, String lastname, String citizenIdentifier) {
		
		super();

		this.firstname = firstname;
		this.lastname = lastname;
		this.citizenIdentifier = citizenIdentifier;
	}
	
	@Override
	public List<Guardian> getGuardians() {

		return guardians;
	}

	public void setGuardians(List<Guardian> guardians) {

		this.guardians = guardians;
	}

	@Override
	public boolean isUnderSecrecy() {
		return secrecy;
	}

	public void setSecrecy(boolean secrecy) {
		this.secrecy = secrecy;
		
		if (secrecy) {
			
			citizenIdentifier = null;
			firstname = null;
			lastname = null;
			address = null;
			zipCode = null;
			postalAddress = null;
			
			guardians = null;
		}
	}
	
}
