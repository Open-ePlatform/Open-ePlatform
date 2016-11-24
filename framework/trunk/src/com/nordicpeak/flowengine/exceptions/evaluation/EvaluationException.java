package com.nordicpeak.flowengine.exceptions.evaluation;

import com.nordicpeak.flowengine.interfaces.ImmutableEvaluatorDescriptor;

public class EvaluationException extends EvaluatorException {

	private static final long serialVersionUID = 2234365986753594639L;

	public EvaluationException(String message, Throwable cause, ImmutableEvaluatorDescriptor evaluatorDescriptor) {

		super(evaluatorDescriptor, message, cause);
	}

	public EvaluationException(ImmutableEvaluatorDescriptor evaluatorDescriptor, Throwable cause) {

		super(evaluatorDescriptor, cause);
	}

}
