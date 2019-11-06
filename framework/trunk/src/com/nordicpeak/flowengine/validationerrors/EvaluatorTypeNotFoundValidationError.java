package com.nordicpeak.flowengine.validationerrors;

import se.unlogic.standardutils.validation.ValidationError;
import se.unlogic.standardutils.xml.XMLElement;

import com.nordicpeak.flowengine.beans.EvaluatorDescriptor;

//TODO fix case...
@XMLElement(name = "validationError")
public class EvaluatorTypeNotFoundValidationError extends ValidationError {

	private static final long serialVersionUID = 3247494896016298460L;
	@XMLElement
	private final EvaluatorDescriptor evaluatorDescriptor;

	public EvaluatorTypeNotFoundValidationError(EvaluatorDescriptor evaluatorDescriptor) {

		super("EvaluatorTypeNotFound");
		this.evaluatorDescriptor = evaluatorDescriptor;
	}

	public EvaluatorDescriptor getEvaluatorDescriptor() {

		return evaluatorDescriptor;
	}
}
