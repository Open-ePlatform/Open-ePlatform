package com.nordicpeak.flowengine.flowapprovalmodule.validationerrors;

import se.unlogic.standardutils.validation.ValidationError;
import se.unlogic.standardutils.xml.XMLElement;

@XMLElement(name = "validationError")
public class ResponsibleUserNotFound extends ValidationError {
	
	private static final long serialVersionUID = -5529536638244224003L;
	@XMLElement
	private String userName;
	
	public ResponsibleUserNotFound(String userName) {

		super("ResponsibleUserNotFound");

		this.userName = userName;
	}
}
