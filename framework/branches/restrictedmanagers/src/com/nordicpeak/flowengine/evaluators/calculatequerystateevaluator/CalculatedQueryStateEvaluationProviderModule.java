package com.nordicpeak.flowengine.evaluators.calculatequerystateevaluator;

import java.math.BigDecimal;

import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.hierarchy.core.interfaces.attributes.MutableAttributeHandler;
import se.unlogic.standardutils.dao.TransactionHandler;
import se.unlogic.standardutils.validation.ValidationException;

import com.nordicpeak.flowengine.beans.EvaluationResponse;
import com.nordicpeak.flowengine.enums.QueryState;
import com.nordicpeak.flowengine.evaluators.basequerystateevaluator.BaseQueryStateEvaluationProviderModule;
import com.nordicpeak.flowengine.interfaces.EvaluationCallback;
import com.nordicpeak.flowengine.interfaces.Evaluator;
import com.nordicpeak.flowengine.interfaces.MutableEvaluatorDescriptor;
import com.nordicpeak.flowengine.interfaces.QueryInstance;

public abstract class CalculatedQueryStateEvaluationProviderModule<T extends CalculatedQueryStateEvaluator> extends BaseQueryStateEvaluationProviderModule<T> {
	
	@Override
	public EvaluationResponse evaluate(QueryInstance queryInstance, User user, User poster, T evaluator, EvaluationCallback callback, boolean hasQueryValidationErrors, MutableAttributeHandler attributeHandler) {
		
		if (getTargetQueryIDs(evaluator, callback) == null || evaluator.getFromValue() == null) {
			
			return null;
		}
		
		if (!supportsQueryInstance(queryInstance)) {
			
			log.warn("Query instance " + queryInstance + " is not supported for evaluator " + evaluatorDescriptor);
			
			return null;
		}
		
		if (hasQueryValidationErrors) {
			
			return restoreDefaultQueryStates(queryInstance, evaluator, callback, attributeHandler);
		}
		
		BigDecimal calculatedValue = null;
		
		try {
			
			calculatedValue = getCalculatedValue(queryInstance, evaluator);
			
		} catch (ValidationException e) {
			
			return restoreDefaultQueryStates(queryInstance, evaluator, callback, attributeHandler);
		}
		
		if (calculatedValue != null && calculatedValue.compareTo(BigDecimal.valueOf(evaluator.getFromValue())) >= 0 && (evaluator.getToValue() == null || calculatedValue.compareTo(BigDecimal.valueOf(evaluator.getToValue())) <= 0)) {
			
			return applyEvaluatorQueryStates(queryInstance, evaluator, callback, attributeHandler);
		}
		
		return restoreDefaultQueryStates(queryInstance, evaluator, callback, attributeHandler);
	}
	
	@Override
	public Evaluator createEvaluator(MutableEvaluatorDescriptor descriptor, TransactionHandler transactionHandler) throws Throwable {
		
		T evaluator = getEvaluatorClass().newInstance();
		
		evaluator.setEvaluatorID(descriptor.getEvaluatorID());
		evaluator.setQueryState(QueryState.VISIBLE);
		
		getEvaluatorDAO().add(evaluator, transactionHandler, null);
		
		evaluator.init(descriptor, getFullAlias() + "/config/" + descriptor.getEvaluatorID());
		
		return evaluator;
	}
	
	protected abstract BigDecimal getCalculatedValue(QueryInstance queryInstance, T evaluator) throws ValidationException;
	
	protected abstract boolean supportsQueryInstance(QueryInstance queryInstance);
	
}
