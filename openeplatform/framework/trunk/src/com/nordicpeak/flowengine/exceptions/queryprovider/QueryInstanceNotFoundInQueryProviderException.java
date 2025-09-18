package com.nordicpeak.flowengine.exceptions.queryprovider;

import com.nordicpeak.flowengine.interfaces.ImmutableQueryInstanceDescriptor;
import com.nordicpeak.flowengine.interfaces.QueryProvider;

public class QueryInstanceNotFoundInQueryProviderException extends QueryProviderException {

	private static final long serialVersionUID = -7699297063016874614L;

	private final ImmutableQueryInstanceDescriptor queryInstanceDescriptor;

	public QueryInstanceNotFoundInQueryProviderException(QueryProvider queryProvider, ImmutableQueryInstanceDescriptor queryInstanceDescriptor) {

		super("Query instance " + queryInstanceDescriptor + " not found in query provider " + queryProvider + " for query type " + queryProvider.getQueryType(), queryProvider);
		this.queryInstanceDescriptor = queryInstanceDescriptor;
	}

	public ImmutableQueryInstanceDescriptor getQueryInstanceDescriptor() {

		return queryInstanceDescriptor;
	}
}
