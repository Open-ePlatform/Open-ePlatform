package com.nordicpeak.flowengine.beans;

import se.unlogic.standardutils.string.StringTag;

public class Contact {
	
	@StringTag
	private String firstname;
	
	@StringTag
	private String lastname;
	
	@StringTag
	private String email;
	
	@StringTag
	private String mobilePhone;
	
	@StringTag
	private String phone;
	
	@StringTag
	private String address;
	
	@StringTag
	private String zipCode;
	
	@StringTag
	private String postalAddress;
	
	@StringTag
	private String citizenIdentifier;
	
	@StringTag
	private String organizationName;
	
	@StringTag
	private String organizationNumber;
	
	private boolean contactBySMS;
	
	private boolean contactByEmail;
	
	public String getFirstname() {
		
		return firstname;
	}
	
	public String getLastname() {
		
		return lastname;
	}
	
	public String getEmail() {
		
		return email;
	}
	
	public String getMobilePhone() {
		
		return mobilePhone;
	}
	
	public String getPhone() {
		
		return phone;
	}
	
	public String getAddress() {
		
		return address;
	}
	
	public String getZipCode() {
		
		return zipCode;
	}
	
	public String getPostalAddress() {
		
		return postalAddress;
	}
	
	public String getCitizenIdentifier() {
		
		return citizenIdentifier;
	}
	
	public String getOrganizationNumber() {
		
		return organizationNumber;
	}
	
	public void setFirstname(String firstname) {
		
		this.firstname = firstname;
	}
	
	public void setLastname(String lastname) {
		
		this.lastname = lastname;
	}
	
	public void setEmail(String email) {
		
		this.email = email;
	}
	
	public void setMobilePhone(String mobilePhone) {
		
		this.mobilePhone = mobilePhone;
	}
	
	public void setPhone(String phone) {
		
		this.phone = phone;
	}
	
	public void setAddress(String address) {
		
		this.address = address;
	}
	
	public void setZipCode(String zipCode) {
		
		this.zipCode = zipCode;
	}
	
	public void setPostalAddress(String postalAddress) {
		
		this.postalAddress = postalAddress;
	}
	
	public void setCitizenIdentifier(String citizenIdentifier) {
		
		this.citizenIdentifier = citizenIdentifier;
	}
	
	public void setOrganizationNumber(String organizationNumber) {
		
		this.organizationNumber = organizationNumber;
	}
	
	public String getOrganizationName() {
		return organizationName;
	}
	
	public void setOrganizationName(String organizationName) {
		this.organizationName = organizationName;
	}
	
	public boolean isContactBySMS() {
		
		return contactBySMS;
	}
	
	public void setContactBySMS(boolean contactBySMS) {
		
		this.contactBySMS = contactBySMS;
	}
	
	public boolean isContactByEmail() {
		
		return contactByEmail;
	}
	
	public void setContactByEmail(boolean contactByEmail) {
		
		this.contactByEmail = contactByEmail;
	}
	
}
