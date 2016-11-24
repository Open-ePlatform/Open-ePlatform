package com.nordicpeak.flowengine.statistics;

import java.util.LinkedHashMap;
import java.util.List;

import se.unlogic.standardutils.string.StringTag;
import se.unlogic.standardutils.string.StringUtils;
import se.unlogic.standardutils.xml.GeneratedElementable;
import se.unlogic.standardutils.xml.XMLElement;

import com.nordicpeak.flowengine.enums.StatisticsMode;

@XMLElement
public class FlowFamilyStatistics extends GeneratedElementable implements Comparable<FlowFamilyStatistics> {

	@XMLElement
	private Integer flowFamilyID;

	@StringTag
	@XMLElement
	private String name;

	private StatisticsMode statisticsMode;

	private List<IntegerEntry> flowInstanceCount;

	private List<IntegerEntry> externalRedirectCount;

	private List<FloatEntry> surveyRating;

	private LinkedHashMap<Integer, FlowStatistics> flowStatistics;

	public List<IntegerEntry> getFlowInstanceCount() {

		return flowInstanceCount;
	}

	public void setFlowInstanceCount(List<IntegerEntry> flowInstanceCount) {

		this.flowInstanceCount = flowInstanceCount;
	}

	public List<IntegerEntry> getExternalRedirectCount() {

		return externalRedirectCount;
	}

	public void setExternalRedirectCount(List<IntegerEntry> externalRedirectCount) {

		this.externalRedirectCount = externalRedirectCount;
	}

	public LinkedHashMap<Integer, FlowStatistics> getFlowStatistics() {

		return flowStatistics;
	}

	public void setFlowStatistics(LinkedHashMap<Integer, FlowStatistics> flowStatistics) {

		this.flowStatistics = flowStatistics;
	}

	public Integer getFlowFamilyID() {

		return flowFamilyID;
	}

	public void setFlowFamilyID(Integer flowFamilyID) {

		this.flowFamilyID = flowFamilyID;
	}

	public String getName() {

		return name;
	}

	public void setName(String name) {

		this.name = name;
	}

	public StatisticsMode getStatisticsMode() {

		return statisticsMode;
	}

	public void setStatisticsMode(StatisticsMode statisticsMode) {

		this.statisticsMode = statisticsMode;
	}

	@Override
	public int compareTo(FlowFamilyStatistics familyStatistics) {

		return name.compareToIgnoreCase(familyStatistics.getName());
	}

	@Override
	public String toString() {

		return StringUtils.toLogFormat(name, 30) + " (ID: " + flowFamilyID + ")";
	}

	public List<FloatEntry> getSurveyRating() {

		return surveyRating;
	}

	public void setSurveyRating(List<FloatEntry> surveyRating) {

		this.surveyRating = surveyRating;
	}
}
