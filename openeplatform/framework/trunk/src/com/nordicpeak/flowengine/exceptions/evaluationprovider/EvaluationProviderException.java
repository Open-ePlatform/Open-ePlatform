package com.nordicpeak.flowengine.exceptions.evaluationprovider;

import com.nordicpeak.flowengine.exceptions.FlowEngineException;
import com.nordicpeak.flowengine.interfaces.EvaluationProvider;


public abstract class EvaluationProviderException extends FlowEngineException {

	private static final long serialVersionUID = -5527880264194084072L;

	private final EvaluationProvider evaluationProvider;

	public EvaluationProviderException(String message, EvaluationProvider evaluationProvider) {

		super(message);
		this.evaluationProvider = evaluationProvider;
	}

	public EvaluationProviderException(String message, Throwable cause, EvaluationProvider evaluationProvider) {

		super(message, cause);
		this.evaluationProvider = evaluationProvider;
	}

	public EvaluationProvider getEvaluationProvider() {

		return evaluationProvider;
	}
}
