package com.nordicpeak.flowengine.statistics;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;

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

	@StringTag
	@XMLElement
	private String flowTypeName;

	private StatisticsMode statisticsMode;

	private List<IntegerEntry> flowInstanceCount;

	private List<IntegerEntry> externalRedirectCount;

	private List<FloatEntry> surveyRating;

	private LinkedHashMap<Integer, FlowStatistics> flowStatistics;

	private List<Entry<String, Integer>> totalFlowInstancesCountByWeek;

	private List<Entry<String, Integer>> femaleFlowInstancesCountByWeek;

	private List<Entry<String, Integer>> maleFlowInstancesCountByWeek;

	private List<Entry<String, Integer>> unkownFlowInstancesCountByWeek;

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

	public String getFlowTypeName() {

		return flowTypeName;
	}

	public void setFlowTypeName(String flowTypeName) {

		this.flowTypeName = flowTypeName;
	}

	public StatisticsMode getStatisticsMode() {

		return statisticsMode;
	}

	public void setStatisticsMode(StatisticsMode statisticsMode) {

		this.statisticsMode = statisticsMode;
	}

	public List<Entry<String, Integer>> getTotalFlowInstancesCountByWeek() {

		return totalFlowInstancesCountByWeek;
	}

	public void setTotalFlowInstancesCountByWeek(List<Entry<String, Integer>> totalFlowInstancesCountByWeek) {

		this.totalFlowInstancesCountByWeek = totalFlowInstancesCountByWeek;
	}

	public List<Entry<String, Integer>> getFemaleFlowInstancesCountByWeek() {

		return femaleFlowInstancesCountByWeek;
	}

	public void setFemaleFlowInstancesCountByWeek(List<Entry<String, Integer>> femaleFlowInstancesCountByWeek) {

		this.femaleFlowInstancesCountByWeek = femaleFlowInstancesCountByWeek;
	}

	public List<Entry<String, Integer>> getMaleFlowInstancesCountByWeek() {

		return maleFlowInstancesCountByWeek;
	}

	public void setMaleFlowInstancesCountByWeek(List<Entry<String, Integer>> maleFlowInstancesCountByWeek) {

		this.maleFlowInstancesCountByWeek = maleFlowInstancesCountByWeek;
	}

	public List<Entry<String, Integer>> getUnkownFlowInstancesCountByWeek() {

		return unkownFlowInstancesCountByWeek;
	}

	public void setUnkownFlowInstancesCountByWeek(List<Entry<String, Integer>> unkownFlowInstancesCountByWeek) {

		this.unkownFlowInstancesCountByWeek = unkownFlowInstancesCountByWeek;
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
