package com.nordicpeak.childrelationprovider;

import java.util.List;

public class SimpleChild extends BasePerson implements Child {

	private static final long serialVersionUID = 5461893983752333815L;

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
}
