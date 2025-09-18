package com.nordicpeak.flowengine.exceptions.queryprovider;

import com.nordicpeak.flowengine.interfaces.ImmutableQueryDescriptor;
import com.nordicpeak.flowengine.interfaces.QueryProvider;

public class QueryNotFoundInQueryProviderException extends QueryProviderException {

	private static final long serialVersionUID = -6257131577558177411L;

	private final ImmutableQueryDescriptor queryDescriptor;

	public QueryNotFoundInQueryProviderException(QueryProvider queryProvider, ImmutableQueryDescriptor queryDescriptor) {

		super("Query " + queryDescriptor + " not found in query provider " + queryProvider + " for query type " + queryProvider.getQueryType(), queryProvider);
		this.queryDescriptor = queryDescriptor;
	}

	public ImmutableQueryDescriptor getQueryDescriptor() {

		return queryDescriptor;
	}
}
