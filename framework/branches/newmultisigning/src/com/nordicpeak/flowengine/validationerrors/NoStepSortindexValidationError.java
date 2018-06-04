package com.nordicpeak.flowengine.validationerrors;

import se.unlogic.standardutils.validation.ValidationError;
import se.unlogic.standardutils.xml.XMLElement;

import com.nordicpeak.flowengine.beans.Step;

@XMLElement(name = "validationError")
public class NoStepSortindexValidationError extends ValidationError {

	@XMLElement
	private Step step;

	public NoStepSortindexValidationError(Step step) {

		super("NoStepSortindex");
	}

	public Step getStep() {

		return step;
	}
}
