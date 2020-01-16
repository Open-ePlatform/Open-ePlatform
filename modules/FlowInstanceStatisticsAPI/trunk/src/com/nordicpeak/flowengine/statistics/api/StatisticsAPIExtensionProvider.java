package com.nordicpeak.flowengine.statistics.api;

import java.sql.Timestamp;
import java.util.List;

public interface StatisticsAPIExtensionProvider {

	public List<FlowInstanceStatistic> getFlowInstanceAPIStatistics(Timestamp fromTimestamp, Timestamp toTimestamp, int rowLimit);

}
