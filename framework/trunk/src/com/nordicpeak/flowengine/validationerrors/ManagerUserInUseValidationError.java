package com.nordicpeak.flowengine.validationerrors;

import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.standardutils.validation.ValidationError;
import se.unlogic.standardutils.xml.XMLElement;

//TODO fix case...
@XMLElement(name = "validationError")
public class ManagerUserInUseValidationError extends ValidationError {

	@XMLElement
	private final User user;

	public ManagerUserInUseValidationError(User user) {

		super("ManagerUserInUseError");
		this.user = user;
	}

	public User getUser() {

		return user;
	}

}
