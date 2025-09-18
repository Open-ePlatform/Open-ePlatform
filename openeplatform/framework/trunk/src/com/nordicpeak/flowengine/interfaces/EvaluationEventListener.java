package com.nordicpeak.flowengine.interfaces;

import java.util.List;

import se.unlogic.hierarchy.core.beans.User;

import com.nordicpeak.flowengine.beans.QueryModification;


public interface EvaluationEventListener {

	public boolean supportsEvent(Object event);
	
	public List<QueryModification> processEvent(Object event, QueryInstance queryInstance, User user, User poster, EvaluationCallback callback, EvaluationHandler evaluationHandler);
}
