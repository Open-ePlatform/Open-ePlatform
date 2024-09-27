package com.nordicpeak.flowengine.flowapprovalmodule.validationerrors;

import se.unlogic.standardutils.validation.ValidationError;
import se.unlogic.standardutils.xml.XMLElement;

@XMLElement(name = "validationError")
public class StatusNotFound extends ValidationError {
	
	private static final long serialVersionUID = 8789033832573772665L;
	@XMLElement
	private String status;
	
	public StatusNotFound(String status) {

		super("StatusNotFound");

		this.status = status;
	}
}
