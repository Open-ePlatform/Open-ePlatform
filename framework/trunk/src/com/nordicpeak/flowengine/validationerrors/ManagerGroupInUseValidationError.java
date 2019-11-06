package com.nordicpeak.flowengine.validationerrors;

import se.unlogic.hierarchy.core.beans.Group;
import se.unlogic.standardutils.validation.ValidationError;
import se.unlogic.standardutils.xml.XMLElement;

//TODO fix case...
@XMLElement(name = "validationError")
public class ManagerGroupInUseValidationError extends ValidationError {

	private static final long serialVersionUID = -2350703591299509892L;
	@XMLElement
	private final Group group;

	public ManagerGroupInUseValidationError(Group group) {

		super("ManagerGroupInUseError");
		this.group = group;
	}

	public Group getGroup() {

		return group;
	}

}
