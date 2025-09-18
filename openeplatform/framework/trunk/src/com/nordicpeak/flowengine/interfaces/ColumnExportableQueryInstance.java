package com.nordicpeak.flowengine.interfaces;

import java.util.List;

public interface ColumnExportableQueryInstance {

	public List<String> getColumnLabels(QueryHandler queryHandler);
	public List<String> getColumnValues(); 
}
