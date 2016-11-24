package com.nordicpeak.flowengine.interfaces;

import java.util.List;

public interface ExportableQueryInstance {

	public List<String> getExportValueLabels(QueryHandler queryHandler);
	public List<String> getExportValues(); 
}
