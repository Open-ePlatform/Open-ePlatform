package com.nordicpeak.flowengine.search;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import se.unlogic.standardutils.collections.CollectionUtils;

import com.nordicpeak.flowengine.beans.FlowInstance;
import com.nordicpeak.flowengine.beans.QuerySearchValue;
import com.nordicpeak.flowengine.interfaces.QueryInstance;
import com.nordicpeak.flowengine.interfaces.SearchableQueryInstance;

public class QuerySearchValueUtils {

	public static void addQuerySearchValues(FlowInstance flowInstance, QueryInstance queryInstance) {

		if (queryInstance instanceof SearchableQueryInstance) {

			SearchableQueryInstance indexedQueryInstance = (SearchableQueryInstance) queryInstance;

			clearQuerySearchValues(flowInstance, queryInstance);

			Set<String> uniqueValues = indexedQueryInstance.getSearchableValues() != null ? new HashSet<>(indexedQueryInstance.getSearchableValues()) : null;

			Integer queryID = queryInstance.getQuery().getQueryID();

			List<QuerySearchValue> querySearchValues = CollectionUtils.map(uniqueValues, value -> new QuerySearchValue(flowInstance, queryID, value));

			if (querySearchValues != null) {

				flowInstance.addQuerySearchValues(querySearchValues);
			}
		}
	}

	public static void clearQuerySearchValues(FlowInstance flowInstance, QueryInstance queryInstance) {

		if (queryInstance instanceof SearchableQueryInstance && flowInstance.getQuerySearchValues() != null) {

			Integer queryID = queryInstance.getQuery().getQueryID();

			flowInstance.getQuerySearchValues().removeIf(querySearchValue -> querySearchValue.getQueryID().equals(queryID));
		}
	}

}