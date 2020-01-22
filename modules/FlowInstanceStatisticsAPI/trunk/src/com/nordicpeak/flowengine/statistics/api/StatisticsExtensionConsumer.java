package com.nordicpeak.flowengine.statistics.api;

import com.nordicpeak.flowengine.statistics.interfaces.StatisticsAPIExtensionProvider;

public interface StatisticsExtensionConsumer {

	boolean addStatisticsExtensionProvider(StatisticsAPIExtensionProvider statisticsExtensionProvider);

	boolean removeStatisticsExtensionProvider(StatisticsAPIExtensionProvider statisticsExtensionProvider);

}