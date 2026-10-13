package com.nordicpeak.flowengine.managers;

import java.io.Serializable;
import java.util.List;

import com.nordicpeak.flowengine.interfaces.Evaluator;
import com.nordicpeak.flowengine.interfaces.QueryInstance;

public class ManagedQueryInstance implements Serializable{

	private static final long serialVersionUID = -8813330479479239498L;

	private final QueryInstance queryInstance;
	private final List<Evaluator> evaluators;

	public ManagedQueryInstance(QueryInstance queryInstance, List<Evaluator> evaluators) {

		super();
		this.queryInstance = queryInstance;
		this.evaluators = evaluators;
	}

	public QueryInstance getQueryInstance() {

		return queryInstance;
	}

	public List<Evaluator> getEvaluators() {

		return evaluators;
	}

}
