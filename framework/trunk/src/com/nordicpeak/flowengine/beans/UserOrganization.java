package com.nordicpeak.flowengine.beans;

import se.unlogic.emailutils.populators.EmailPopulator;
import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.standardutils.annotations.RequiredIfSet;
import se.unlogic.standardutils.annotations.WebPopulate;
import se.unlogic.standardutils.dao.annotations.DAOManaged;
import se.unlogic.standardutils.dao.annotations.Key;
import se.unlogic.standardutils.dao.annotations.OrderBy;
import se.unlogic.standardutils.dao.annotations.Table;
import se.unlogic.standardutils.populators.SwedishSocialSecurityOrOrganizationNumberPopulator;
import se.unlogic.standardutils.xml.GeneratedElementable;
import se.unlogic.standardutils.xml.XMLElement;

@Table(name = "flowengine_user_organizations")
@XMLElement(name = "Organization")
public class UserOrganization extends GeneratedElementable {

	@Key
	@DAOManaged(autoGenerated = true)
	@XMLElement
	private Integer organizationID;

	@WebPopulate(maxLength = 255, required = true)
	@OrderBy
	@DAOManaged
	@XMLElement
	private String name;

	@WebPopulate(maxLength = 16, required = true, populator=SwedishSocialSecurityOrOrganizationNumberPopulator.class)
	@OrderBy
	@DAOManaged
	@XMLElement
	private String organizationNumber;

	@WebPopulate(maxLength = 255)
	@RequiredIfSet(paramNames = "contactByLetter")
	@OrderBy
	@DAOManaged
	@XMLElement
	private String address;

	@WebPopulate(maxLength = 255)
	@RequiredIfSet(paramNames = "contactByLetter")
	@DAOManaged
	@XMLElement
	private String zipCode;

	@WebPopulate(maxLength = 255)
	@RequiredIfSet(paramNames = "contactByLetter")
	@DAOManaged
	@XMLElement
	private String postalAddress;

	@WebPopulate(maxLength = 255, required = true)
	@DAOManaged
	@XMLElement
	private String firstname;

	@WebPopulate(maxLength = 255, required = true)
	@DAOManaged
	@XMLElement
	private String lastname;

	@WebPopulate(maxLength = 255)
	@DAOManaged
	@XMLElement
	private String phone;

	@WebPopulate(maxLength = 255)
	@RequiredIfSet(paramNames = "contactBySMS")
	@DAOManaged
	@XMLElement
	private String mobilePhone;

	@WebPopulate(maxLength = 255, populator = EmailPopulator.class)
	@RequiredIfSet(paramNames = "contactByEmail")
	@DAOManaged
	@XMLElement
	private String email;

	@WebPopulate
	@DAOManaged
	@XMLElement
	private boolean contactBySMS;

	@DAOManaged(columnName = "userID")
	@XMLElement
	private User user;

	public Integer getOrganizationID() {

		return organizationID;
	}

	public void setOrganizationID(Integer organizationID) {

		this.organizationID = organizationID;
	}

	public String getName() {

		return name;
	}

	public void setName(String name) {

		this.name = name;
	}

	public String getOrganizationNumber() {

		return organizationNumber;
	}

	public void setOrganizationNumber(String organizationNumber) {

		this.organizationNumber = organizationNumber;
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

	public String getPhone() {

		return phone;
	}

	public void setPhone(String phone) {

		this.phone = phone;
	}

	public String getMobilePhone() {

		return mobilePhone;
	}

	public void setMobilePhone(String mobilePhone) {

		this.mobilePhone = mobilePhone;
	}

	public String getEmail() {

		return email;
	}

	public void setEmail(String email) {

		this.email = email;
	}

	public boolean isContactBySMS() {

		return contactBySMS;
	}

	public void setContactBySMS(boolean contactBySMS) {

		this.contactBySMS = contactBySMS;
	}

	public User getUser() {

		return user;
	}

	public void setUser(User user) {

		this.user = user;
	}

	@Override
	public String toString() {

		return name + " (ID: " + organizationID + ")";
	}

}
