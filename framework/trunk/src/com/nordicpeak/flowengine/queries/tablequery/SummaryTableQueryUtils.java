package com.nordicpeak.flowengine.queries.tablequery;

import com.nordicpeak.flowengine.interfaces.QueryHandler;


public class SummaryTableQueryUtils {

	@SuppressWarnings("unchecked")
	public static <X extends SummaryTableQuery> SummaryTableQueryCallback<X> getGenericTableQueryCallback(Class<? extends SummaryTableQuery> clazz, QueryHandler queryHandler, String queryTypeID) {

		return (SummaryTableQueryCallback<X>) queryHandler.getQueryProvider(queryTypeID);
	}
}
