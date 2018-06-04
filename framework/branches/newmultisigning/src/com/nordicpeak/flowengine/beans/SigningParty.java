package com.nordicpeak.flowengine.beans;

import se.unlogic.standardutils.string.StringTag;
import se.unlogic.standardutils.xml.GeneratedElementable;
import se.unlogic.standardutils.xml.XMLElement;

@XMLElement
public final class SigningParty extends GeneratedElementable {
	
	@XMLElement
	@StringTag
	private final String firstname;
	
	@XMLElement
	@StringTag
	private final String lastname;
	
	@XMLElement
	@StringTag
	private final String email;
	
	@XMLElement
	@StringTag
	private final String mobilePhone;
	
	@XMLElement
	@StringTag
	private final String socialSecurityNumber;
	
	@XMLElement
	private final boolean addAsOwner;
	
	@XMLElement
	private final boolean unsecure;
	
	public SigningParty(String firstname, String lastname, String email, String mobilePhone, String socialSecurityNumber, boolean addAsOwner) {
		
		super();
		this.firstname = firstname;
		this.lastname = lastname;
		this.email = email;
		this.mobilePhone = mobilePhone;
		this.socialSecurityNumber = socialSecurityNumber;
		this.addAsOwner = addAsOwner;
		
		unsecure = false;
	}
	
	public SigningParty(String firstname, String lastname, String email, String mobilePhone, boolean addAsOwner) {
		
		super();
		this.firstname = firstname;
		this.lastname = lastname;
		this.email = email;
		this.mobilePhone = mobilePhone;
		this.socialSecurityNumber = null;
		this.addAsOwner = addAsOwner;
		
		unsecure = true;
	}
	
	public String getFirstname() {
		
		return firstname;
	}
	
	public String getLastname() {
		
		return lastname;
	}
	
	public String getEmail() {
		
		return email;
	}
	
	public String getSocialSecurityNumber() {
		
		return socialSecurityNumber;
	}
	
	public String getMobilePhone() {
		
		return mobilePhone;
	}
	
	public boolean isAddAsOwner() {
		
		return addAsOwner;
	}
	
	public boolean isUnsecure() {
		return unsecure;
	}
	
	@Override
	public String toString() {
		
		if (socialSecurityNumber != null) {
			
			return firstname + " " + lastname + " (" + socialSecurityNumber + ")";
			
		} else {
			
			return firstname + " " + lastname;
		}
	}
}