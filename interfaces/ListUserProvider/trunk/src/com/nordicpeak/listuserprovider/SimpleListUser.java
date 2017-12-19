package com.nordicpeak.listuserprovider;

import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.hierarchy.core.interfaces.attributes.AttributeHandler;


public class SimpleListUser implements ListUser {

	private static final long serialVersionUID = -4237891998934109535L;

	private String providerUserID;

	private String firstname;

	private String lastname;

	private String username;

	private String citizenIdentifier;

	private String email;

	private String phone;

	public SimpleListUser(){}
	
	public SimpleListUser(User user) {

		providerUserID = user.getUserID().toString();
		firstname = user.getFirstname();
		lastname = user.getLastname();
		email = user.getEmail();
		username = user.getUsername();

		AttributeHandler attributeHandler = user.getAttributeHandler();

		if (attributeHandler != null) {

			citizenIdentifier = attributeHandler.getString("citizenIdentifier");
			phone = attributeHandler.getString("mobilePhone");
		}
	}

	@Override
	public String getProviderUserID() {

		return providerUserID;
	}

	public void setProviderUserID(String providerUserID) {

		this.providerUserID = providerUserID;
	}

	@Override
	public String getFirstname() {

		return firstname;
	}

	public void setFirstname(String firstname) {

		this.firstname = firstname;
	}

	@Override
	public String getLastname() {

		return lastname;
	}

	public void setLastname(String lastname) {

		this.lastname = lastname;
	}

	@Override
	public String getUsername() {

		return username;
	}

	public void setUsername(String username) {

		this.username = username;
	}

	@Override
	public String getCitizenIdentifier() {

		return citizenIdentifier;
	}

	public void setCitizenIdentifier(String citizenIdentifier) {

		this.citizenIdentifier = citizenIdentifier;
	}

	@Override
	public String getEmail() {

		return email;
	}

	public void setEmail(String email) {

		this.email = email;
	}

	@Override
	public String getPhone() {

		return phone;
	}

	public void setPhone(String phone) {

		this.phone = phone;
	}
}
