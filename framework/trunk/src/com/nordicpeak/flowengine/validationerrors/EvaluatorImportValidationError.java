package com.nordicpeak.flowengine.validationerrors;

import se.unlogic.standardutils.validation.ValidationError;
import se.unlogic.standardutils.xml.XMLElement;

import com.nordicpeak.flowengine.beans.EvaluatorDescriptor;

//TODO fix case...
@XMLElement(name="validationError")
public class EvaluatorImportValidationError extends ValidationError {

	private static final long serialVersionUID = 1825624959361748761L;
	@XMLElement
	private final EvaluatorDescriptor evaluatorDescriptor;

	public EvaluatorImportValidationError(EvaluatorDescriptor evaluatorDescriptor) {

		super("EvaluatorImportException");
		this.evaluatorDescriptor = evaluatorDescriptor;
	}


	public EvaluatorDescriptor getEvaluatorDescriptor() {

		return evaluatorDescriptor;
	}
}
