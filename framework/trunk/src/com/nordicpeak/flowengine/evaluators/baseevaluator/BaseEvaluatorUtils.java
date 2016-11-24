package com.nordicpeak.flowengine.evaluators.baseevaluator;

import com.nordicpeak.flowengine.interfaces.EvaluationHandler;


public class BaseEvaluatorUtils {

	@SuppressWarnings("unchecked")
	public static <X extends BaseEvaluator> GenericEvaluationProvider<X> getGenericEvaluationProvider(Class<? extends BaseEvaluator> clazz, EvaluationHandler evaluationHandler, String evaluatorTypeID) {

		return (GenericEvaluationProvider<X>) evaluationHandler.getEvaluationProvider(evaluatorTypeID);
	}

}
