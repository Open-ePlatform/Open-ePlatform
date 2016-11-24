package com.nordicpeak.flowengine.queries.fixedalternativesquery;

import java.sql.SQLException;
import java.util.List;



public interface FixedAlternativesQueryCallback<Q extends FixedAlternativesQuery> {

	public List<? extends FixedAlternativesQueryInstance> getQueryInstances(Q query, List<Integer> queryInstanceIDs) throws SQLException;
}
