package com.nordicpeak.flowengine.queries.tablequery;

import java.sql.SQLException;
import java.util.List;

import se.unlogic.standardutils.datatypes.Matrix;



public interface SummaryTableQueryCallback<Q extends SummaryTableQuery> {

	public Matrix<String> getSummaryTable(Q query, List<Integer> queryInstanceIDs) throws SQLException;
}
