package com.nordicpeak.flowengine.evaluators.baseevaluator;

import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.hierarchy.core.interfaces.MutableAttributeHandler;

import com.nordicpeak.flowengine.beans.EvaluationResponse;
import com.nordicpeak.flowengine.interfaces.EvaluationCallback;
import com.nordicpeak.flowengine.interfaces.EvaluationProvider;
import com.nordicpeak.flowengine.interfaces.QueryInstance;



public interface GenericEvaluationProvider<E extends BaseEvaluator> extends EvaluationProvider{

	public EvaluationResponse evaluate(QueryInstance queryInstance, User user, User poster, E evaluator, EvaluationCallback callback, boolean hasQueryValidationErrors, MutableAttributeHandler attributeHandler);

}
