package com.nordicpeak.flowengine.exceptions.evaluationprovider;

import com.nordicpeak.flowengine.interfaces.EvaluationProvider;
import com.nordicpeak.flowengine.interfaces.ImmutableEvaluatorDescriptor;

public class EvaluatorNotFoundInEvaluationProviderException extends EvaluationProviderException {

	private static final long serialVersionUID = -6257131577558177411L;

	private final ImmutableEvaluatorDescriptor evaluatorDescriptor;

	public EvaluatorNotFoundInEvaluationProviderException(EvaluationProvider evaluationProvider, ImmutableEvaluatorDescriptor evaluatorDescriptor) {

		super("Evaluator " + evaluatorDescriptor + " not found in evaluation provider " + evaluationProvider + " for query type " + evaluationProvider.getEvaluatorType(), evaluationProvider);
		this.evaluatorDescriptor = evaluatorDescriptor;
	}

	public ImmutableEvaluatorDescriptor getEvaluatorDescriptor() {

		return evaluatorDescriptor;
	}
}
