package com.nordicpeak.flowengine.exceptions.flowinstancemanager;

import com.nordicpeak.flowengine.interfaces.ImmutableFlowInstance;

public class QueryInstanceNotFoundInFlowInstanceManagerException extends FlowInstanceManagerException {

	private static final long serialVersionUID = -6362549935541139014L;
	
	private final int queryID;

	public QueryInstanceNotFoundInFlowInstanceManagerException(int queryID, ImmutableFlowInstance flowInstance) {

		super("No query instance found for query with queryID " + queryID + " in flow instance " + flowInstance, flowInstance);

		this.queryID = queryID;
	}

	public int getQueryID() {

		return queryID;
	}
}
