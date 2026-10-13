package com.nordicpeak.flowengine.validationerrors;

import se.unlogic.standardutils.validation.ValidationError;
import se.unlogic.standardutils.xml.XMLElement;

import com.nordicpeak.flowengine.beans.Step;

@XMLElement(name = "validationError")
public class NoStepSortindexValidationError extends ValidationError {

	private static final long serialVersionUID = -1678856243990518661L;
	@XMLElement
	private Step step;

	public NoStepSortindexValidationError(Step step) {

		super("NoStepSortindex");
	}

	public Step getStep() {

		return step;
	}
}
