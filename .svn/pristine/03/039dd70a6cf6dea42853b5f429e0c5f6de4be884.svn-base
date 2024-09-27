package com.nordicpeak.flowengine.interfaces;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

import com.nordicpeak.flowengine.statistics.FlowFamilyStatistics;
import com.nordicpeak.flowengine.statistics.IntegerEntry;

public interface StatisticsExtensionProvider {

	public List<IntegerEntry> getGlobalFlowInstanceCount(Timestamp startDate, Timestamp endDate) throws SQLException;
	
	public void getFlowFamilyStatistics(FlowFamilyStatistics familyStatistics, Timestamp startDate, Timestamp endDate, boolean detailed) throws SQLException;

}
