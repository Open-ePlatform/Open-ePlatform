package com.nordicpeak.flowengine.exceptions.evaluation;

import com.nordicpeak.flowengine.exceptions.FlowEngineException;
import com.nordicpeak.flowengine.interfaces.ImmutableEvaluatorDescriptor;

public class EvaluatorException extends FlowEngineException {

	private static final long serialVersionUID = 3763111486619337003L;

	private final ImmutableEvaluatorDescriptor evaluatorDescriptor;

	public EvaluatorException(ImmutableEvaluatorDescriptor evaluatorDescriptor, String message, Throwable cause) {

		super(message, cause);
		this.evaluatorDescriptor = evaluatorDescriptor;
	}

	public EvaluatorException(ImmutableEvaluatorDescriptor evaluatorDescriptor, Throwable cause) {

		super(cause);
		this.evaluatorDescriptor = evaluatorDescriptor;
	}

	public ImmutableEvaluatorDescriptor getEvaluatorDescriptor() {

		return evaluatorDescriptor;
	}
}
