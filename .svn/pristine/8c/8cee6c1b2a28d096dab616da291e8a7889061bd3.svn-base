package com.nordicpeak.flowengine.exceptions.queryprovider;

import com.nordicpeak.flowengine.exceptions.FlowEngineException;
import com.nordicpeak.flowengine.interfaces.QueryProvider;


public abstract class QueryProviderException extends FlowEngineException {

	private static final long serialVersionUID = -5527880264194084072L;

	private final QueryProvider queryProvider;

	public QueryProviderException(String message, QueryProvider queryProvider) {

		super(message);
		this.queryProvider = queryProvider;
	}

	public QueryProviderException(String message, Throwable cause, QueryProvider queryProvider) {

		super(message, cause);
		this.queryProvider = queryProvider;
	}

	public QueryProvider getQueryProvider() {

		return queryProvider;
	}
}
