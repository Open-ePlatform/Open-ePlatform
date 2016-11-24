package com.nordicpeak.flowengine.interfaces;

import java.util.List;

import com.nordicpeak.flowengine.beans.QueryModification;
import com.nordicpeak.flowengine.exceptions.queryinstance.IllegalQueryInstanceAccessException;


public interface EvaluationCallback {

	public QueryInstance getQueryInstance(Integer queryID) throws IllegalQueryInstanceAccessException;
	
	public int getMinStepIndex();
	
	public int getMinQueryIndex();

	public List<QueryModification> sendEvent(Object event, boolean skipCurrentQuery);	
	
	public QueryInstance getQueryInstance();
}
