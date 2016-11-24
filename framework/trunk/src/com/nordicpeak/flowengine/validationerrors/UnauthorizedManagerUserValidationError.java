package com.nordicpeak.flowengine.validationerrors;

import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.standardutils.validation.ValidationError;
import se.unlogic.standardutils.xml.XMLElement;

//TODO fix case...
@XMLElement(name = "validationError")
public class UnauthorizedManagerUserValidationError extends ValidationError {

	@XMLElement
	private final User user;

	public UnauthorizedManagerUserValidationError(User user) {

		super("UnauthorizedManagerUserError");
		this.user = user;
	}

	public User getUser() {

		return user;
	}

}
