package com.nordicpeak.flowengine.validationerrors;

import se.unlogic.hierarchy.core.beans.Group;
import se.unlogic.standardutils.validation.ValidationError;
import se.unlogic.standardutils.xml.XMLElement;

//TODO fix case...
@XMLElement(name = "validationError")
public class InUseManagerGroupValidationError extends ValidationError {

	@XMLElement
	private final Group group;

	public InUseManagerGroupValidationError(Group group) {

		super("InUseManagerGroupError");
		this.group = group;
	}

	public Group getGroup() {

		return group;
	}

}
