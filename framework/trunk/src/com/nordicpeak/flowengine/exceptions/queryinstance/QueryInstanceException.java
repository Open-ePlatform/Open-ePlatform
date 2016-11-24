package com.nordicpeak.flowengine.exceptions.queryinstance;

import com.nordicpeak.flowengine.exceptions.FlowEngineException;
import com.nordicpeak.flowengine.interfaces.ImmutableQueryInstanceDescriptor;

public abstract class QueryInstanceException extends FlowEngineException {

	private static final long serialVersionUID = 7972212276440856822L;

	public QueryInstanceException(ImmutableQueryInstanceDescriptor queryInstanceDescriptor, String message) {

		super(message);
		this.queryInstanceDescriptor = queryInstanceDescriptor;
	}

	private final ImmutableQueryInstanceDescriptor queryInstanceDescriptor;

	public QueryInstanceException(ImmutableQueryInstanceDescriptor queryInstanceDescriptor, Throwable cause) {

		super(cause);
		this.queryInstanceDescriptor = queryInstanceDescriptor;
	}

	public ImmutableQueryInstanceDescriptor getQueryInstanceDescriptor() {

		return queryInstanceDescriptor;
	}

	@Override
	public String toString() {

		return super.toString() + " in query instance " + queryInstanceDescriptor;
	}	
}
