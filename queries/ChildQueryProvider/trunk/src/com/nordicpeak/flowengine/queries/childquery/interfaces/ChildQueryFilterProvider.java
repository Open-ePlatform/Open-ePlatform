package com.nordicpeak.flowengine.queries.childquery.interfaces;

import java.sql.SQLException;
import java.util.List;

public interface ChildQueryFilterProvider {

	public ChildQueryFilterEndpoint getEndpoint(String name) throws SQLException;

	public List<? extends ChildQueryFilterEndpoint> getEndpoints() throws SQLException;

	public String getName();
	
}
