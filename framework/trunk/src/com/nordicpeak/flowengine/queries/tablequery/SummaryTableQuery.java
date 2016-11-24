package com.nordicpeak.flowengine.queries.tablequery;

import java.sql.SQLException;
import java.util.List;

import se.unlogic.standardutils.datatypes.Matrix;

import com.nordicpeak.flowengine.interfaces.Query;
import com.nordicpeak.flowengine.interfaces.QueryHandler;


public interface SummaryTableQuery extends Query{

	public Matrix<String> getDataTable(List<Integer> queryInstanceIDs, QueryHandler queryHandler) throws SQLException;
}
