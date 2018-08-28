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
	
	private Integer signerUserID;
	
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
	
	public SigningParty(String firstname, String lastname, String email, String mobilePhone, String socialSecurityNumber, boolean addAsOwner, boolean unsecure) {
		
		super();
		this.firstname = firstname;
		this.lastname = lastname;
		this.email = email;
		this.mobilePhone = mobilePhone;
		this.socialSecurityNumber = socialSecurityNumber;
		this.addAsOwner = addAsOwner;
		this.unsecure = unsecure;
	}
	
	public SigningParty(String firstname, String lastname, String email, String mobilePhone, boolean addAsOwner) {
		
		super();
		this.firstname = firstname;
		this.lastname = lastname;
		this.email = email;
		this.mobilePhone = mobilePhone;
		this.addAsOwner = addAsOwner;
		
		socialSecurityNumber = null;
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
	
	public Integer getSignerUserID() {
		return signerUserID;
	}
	
	public void setSignerUserID(Integer signerUserID) {
		this.signerUserID = signerUserID;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result + ((firstname == null) ? 0 : firstname.hashCode());
		result = prime * result + ((lastname == null) ? 0 : lastname.hashCode());
		result = prime * result + ((mobilePhone == null) ? 0 : mobilePhone.hashCode());
		result = prime * result + ((socialSecurityNumber == null) ? 0 : socialSecurityNumber.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SigningParty other = (SigningParty) obj;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		if (firstname == null) {
			if (other.firstname != null)
				return false;
		} else if (!firstname.equals(other.firstname))
			return false;
		if (lastname == null) {
			if (other.lastname != null)
				return false;
		} else if (!lastname.equals(other.lastname))
			return false;
		if (mobilePhone == null) {
			if (other.mobilePhone != null)
				return false;
		} else if (!mobilePhone.equals(other.mobilePhone))
			return false;
		if (socialSecurityNumber == null) {
			if (other.socialSecurityNumber != null)
				return false;
		} else if (!socialSecurityNumber.equals(other.socialSecurityNumber))
			return false;
		return true;
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
