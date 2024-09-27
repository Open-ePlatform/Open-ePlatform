package com.nordicpeak.flowengine.flowapprovalmodule.validationerrors;

import se.unlogic.standardutils.validation.ValidationError;
import se.unlogic.standardutils.xml.XMLElement;

@XMLElement(name = "validationError")
public class AssignableUserNotFound extends ValidationError {
	
	private static final long serialVersionUID = -7570051293282363819L;
	@XMLElement
	private String userName;
	
	public AssignableUserNotFound(String userName) {

		super("AssignableUserNotFound");

		this.userName = userName;
	}
}
