package com.nordicpeak.flowengine.validationerrors;

import se.unlogic.standardutils.validation.ValidationError;
import se.unlogic.standardutils.xml.XMLElement;

import com.nordicpeak.flowengine.beans.EvaluatorDescriptor;

//TODO fix case...
@XMLElement(name="validationError")
public class EvaluatorExportValidationError extends ValidationError {

	@XMLElement
	private final EvaluatorDescriptor evaluatorDescriptor;

	public EvaluatorExportValidationError(EvaluatorDescriptor evaluatorDescriptor) {

		super("EvaluatorExportException");
		this.evaluatorDescriptor = evaluatorDescriptor;
	}


	public EvaluatorDescriptor getEvaluatorDescriptor() {

		return evaluatorDescriptor;
	}
}
