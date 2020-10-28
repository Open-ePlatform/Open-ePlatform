package com.nordicpeak.flowengine.queries.childquery.beans;

import java.io.Serializable;

import se.unlogic.standardutils.dao.annotations.DAOManaged;
import se.unlogic.standardutils.dao.annotations.Key;
import se.unlogic.standardutils.dao.annotations.ManyToOne;
import se.unlogic.standardutils.dao.annotations.Table;
import se.unlogic.standardutils.xml.GeneratedElementable;
import se.unlogic.standardutils.xml.XMLElement;

import com.nordicpeak.childrelationprovider.Guardian;

@Table(name = "child_query_guardians")
@XMLElement(name="Guardian")
public class StoredGuardian extends GeneratedElementable implements Serializable {

	private static final long serialVersionUID = 7634780772195411123L;

	@DAOManaged
	@Key
	@XMLElement
	private Integer guardianID;

	@XMLElement
	@DAOManaged
	private String firstname;

	@XMLElement
	@DAOManaged
	private String lastname;

	@XMLElement
	@DAOManaged
	private String email;

	@XMLElement
	@DAOManaged
	private String phone;

	@XMLElement
	@DAOManaged
	private String citizenIdentifier;

	@DAOManaged
	@XMLElement
	private String address;

	@DAOManaged
	@XMLElement
	//TODO fix typo
	private String zipcode;

	@DAOManaged
	@XMLElement
	private String postalAddress;
	
	@DAOManaged
	@XMLElement(fixCase = true)
	protected String addressUUID;

	@DAOManaged
	private boolean poster;

	@DAOManaged(columnName = "queryInstanceID")
	@ManyToOne
	private ChildQueryInstance queryInstance;

	public StoredGuardian() {};

	public StoredGuardian(Guardian guardian) {
		super();

		this.firstname = guardian.getFirstname();
		this.lastname = guardian.getLastname();
		this.citizenIdentifier = guardian.getCitizenIdentifier();
		this.address = guardian.getAddress();
		this.zipcode = guardian.getZipCode();
		this.postalAddress = guardian.getPostalAddress();
		this.addressUUID = guardian.getAddressUUID();
	}
	
	public StoredGuardian(String firstname, String lastname, String citizenIdentifier) {
		super();
		
		this.firstname = firstname;
		this.lastname = lastname;
		this.citizenIdentifier = citizenIdentifier;
	}

	public Integer getGuardianID() {

		return guardianID;
	}

	public void setGuardianID(Integer guardianID) {

		this.guardianID = guardianID;
	}

	public String getEmail() {

		return email;
	}

	public void setEmail(String email) {

		this.email = email;
	}

	public String getPhone() {

		return phone;
	}

	public void setPhone(String phone) {

		this.phone = phone;
	}

	public void setFirstname(String firstname) {

		this.firstname = firstname;
	}

	public void setLastname(String lastname) {

		this.lastname = lastname;
	}

	public void setCitizenIdentifier(String citizenIdentifier) {

		this.citizenIdentifier = citizenIdentifier;
	}

	public String getFirstname() {

		return firstname;
	}

	public String getLastname() {

		return lastname;
	}

	public String getCitizenIdentifier() {

		return citizenIdentifier;
	}

	public String getAddress() {

		return address;
	}

	public void setAddress(String address) {

		this.address = address;
	}

	public String getZipcode() {

		return zipcode;
	}

	public void setZipcode(String zipcode) {

		this.zipcode = zipcode;
	}

	public String getPostalAddress() {

		return postalAddress;
	}

	public void setPostalAddress(String postalAddress) {

		this.postalAddress = postalAddress;
	}

	public boolean isPoster() {

		return poster;
	}

	public void setPoster(boolean poster) {

		this.poster = poster;
	}

	
	public String getAddressUUID() {
	
		return addressUUID;
	}
	
	public void setAddressUUID(String addressUUID) {
	
		this.addressUUID = addressUUID;
	}

	public boolean addressEquals(StoredGuardian other) {

		if (this == other) {
			return true;
		}
		
		if (other == null) {
			return false;
		}
		
		if (address == null || !address.equals(other.address)) {
			return false;
		}
		
		if (postalAddress == null || !postalAddress.equals(other.postalAddress)) {
			return false;
		}
		
		if (zipcode == null || !zipcode.equals(other.zipcode)) {
			return false;
		}
		
		if (addressUUID == null) {
			if (other.addressUUID != null) {
				return false;
			}
		} else if (!addressUUID.equals(other.addressUUID)) {
			return false;
		}
		
		return true;
	}

}
