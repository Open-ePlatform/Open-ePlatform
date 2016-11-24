package com.nordicpeak.flowengine.evaluators.baseevaluator;

import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.hierarchy.core.interfaces.MutableAttributeHandler;
import se.unlogic.standardutils.dao.annotations.DAOManaged;
import se.unlogic.standardutils.dao.annotations.Key;
import se.unlogic.standardutils.xml.GeneratedElementable;
import se.unlogic.standardutils.xml.XMLElement;
import se.unlogic.standardutils.xml.XMLParserPopulateable;

import com.nordicpeak.flowengine.beans.EvaluationResponse;
import com.nordicpeak.flowengine.interfaces.EvaluationCallback;
import com.nordicpeak.flowengine.interfaces.EvaluationHandler;
import com.nordicpeak.flowengine.interfaces.Evaluator;
import com.nordicpeak.flowengine.interfaces.MutableEvaluatorDescriptor;
import com.nordicpeak.flowengine.interfaces.QueryInstance;


public abstract class BaseEvaluator extends GeneratedElementable implements Evaluator, XMLParserPopulateable {

	private static final long serialVersionUID = -8708777700485841600L;

	@DAOManaged
	@Key
	@XMLElement
	private Integer evaluatorID;
	
	@XMLElement
	protected String configURL;

	@XMLElement
	protected MutableEvaluatorDescriptor evaluatorDescriptor;

	public void init(MutableEvaluatorDescriptor evaluatorDescriptor, String configURL) {

		this.configURL = configURL;
		this.evaluatorDescriptor = evaluatorDescriptor;
	}

	@Override
	public MutableEvaluatorDescriptor getEvaluatorDescriptor() {

		return evaluatorDescriptor;
	}

	@Override
	public String getConfigAlias() {

		return configURL;
	}

	@Override
	public EvaluationResponse evaluate(QueryInstance queryInstance, User user, User poster, EvaluationCallback callback, EvaluationHandler evaluationHandler, boolean hasQueryValidationErrors, MutableAttributeHandler attributeHandler) {

		return BaseEvaluatorUtils.getGenericEvaluationProvider(this.getClass(), evaluationHandler, evaluatorDescriptor.getEvaluatorTypeID()).evaluate(queryInstance, user, poster, this, callback, hasQueryValidationErrors, attributeHandler);
	}

	public Integer getEvaluatorID() {

		return evaluatorID;
	}

	public void setEvaluatorID(Integer evaluatorID) {

		this.evaluatorID = evaluatorID;
	}

	@Override
	public boolean forceAjaxPosting() {

		return false;
	}

	@Override
	public boolean requiresInitialization() {

		return false;
	}
}
