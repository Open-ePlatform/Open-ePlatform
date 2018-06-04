package com.nordicpeak.flowengine.interfaces;

import java.io.Serializable;

import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.hierarchy.core.interfaces.attributes.MutableAttributeHandler;
import se.unlogic.standardutils.xml.Elementable;

import com.nordicpeak.flowengine.beans.EvaluationResponse;



public interface Evaluator extends Serializable, Elementable{

	public ImmutableEvaluatorDescriptor getEvaluatorDescriptor();

	public String getConfigAlias();

	public EvaluationResponse evaluate(QueryInstance queryInstance, User user, User poster, EvaluationCallback callback, EvaluationHandler evaluationHandler, boolean hasQueryValidationErrors, MutableAttributeHandler attributeHandler);

	public boolean forceAjaxPosting();
	
	public boolean requiresInitialization();
}
