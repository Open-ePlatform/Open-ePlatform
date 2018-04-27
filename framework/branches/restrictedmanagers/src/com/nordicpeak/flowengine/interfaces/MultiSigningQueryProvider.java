package com.nordicpeak.flowengine.interfaces;

import java.sql.SQLException;
import java.util.List;

public interface MultiSigningQueryProvider extends QueryProvider {
	
	public List<Integer> getQueryInstanceIDs(String citizenIdentifier) throws SQLException;
}
