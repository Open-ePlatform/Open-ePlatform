package com.nordicpeak.flowengine.exceptions.flowinstance;

import com.nordicpeak.flowengine.beans.QueryDescriptor;
import com.nordicpeak.flowengine.interfaces.ImmutableFlowInstance;

public class MissingQueryInstanceDescriptor extends FlowInstanceException {

	private static final long serialVersionUID = -5662646317737787641L;

	private QueryDescriptor queryDescriptor;

	public MissingQueryInstanceDescriptor(ImmutableFlowInstance flowInstance, QueryDescriptor queryDescriptor) {

		super("Flow instance " + flowInstance + " is missing the query instance descriptor for query descriptor " + queryDescriptor, flowInstance);
		this.queryDescriptor = queryDescriptor;
	}

	public QueryDescriptor getQueryDescriptor() {

		return queryDescriptor;
	}
}
