package com.nordicpeak.flowengine.validationerrors;

import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.standardutils.validation.ValidationError;
import se.unlogic.standardutils.xml.XMLElement;

//TODO fix case...
@XMLElement(name = "validationError")
public class UnauthorizedUserNotManagerValidationError extends ValidationError {

	private static final long serialVersionUID = -1741147998056327591L;
	@XMLElement
	private final User user;

	public UnauthorizedUserNotManagerValidationError(User user) {

		super("UnauthorizedUserNotManager");
		this.user = user;
	}

	public User getUser() {

		return user;
	}

}
