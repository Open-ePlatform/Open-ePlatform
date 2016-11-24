package com.nordicpeak.flowengine.interfaces;

import se.unlogic.hierarchy.core.interfaces.SystemInterface;

import com.nordicpeak.flowengine.dao.FlowEngineDAOFactory;


public interface FlowEngineInterface {

	public EvaluationHandler getEvaluationHandler();

	public QueryHandler getQueryHandler();

	public SystemInterface getSystemInterface();

	public FlowEngineDAOFactory getDAOFactory();
}
