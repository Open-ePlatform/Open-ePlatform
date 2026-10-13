package com.nordicpeak.flowengine.queries.basequery;

import com.nordicpeak.flowengine.interfaces.QueryHandler;


public class BaseQueryUtils {

	@SuppressWarnings("unchecked")
	public static <X extends BaseQueryInstance> BaseQueryInstanceCallback<X> getGenericQueryInstanceProvider(Class<? extends BaseQueryInstance> clazz, QueryHandler queryHandler, String queryTypeID) {

		return (BaseQueryInstanceCallback<X>) queryHandler.getQueryProvider(queryTypeID);
	}
}
