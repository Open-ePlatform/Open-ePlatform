package com.nordicpeak.flowengine.statistics;

import java.io.IOException;
import java.io.Writer;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import se.unlogic.cron4jutils.CronStringValidator;
import se.unlogic.hierarchy.core.annotations.CheckboxSettingDescriptor;
import se.unlogic.hierarchy.core.annotations.EventListener;
import se.unlogic.hierarchy.core.annotations.GroupMultiListSettingDescriptor;
import se.unlogic.hierarchy.core.annotations.HTMLEditorSettingDescriptor;
import se.unlogic.hierarchy.core.annotations.InstanceManagerDependency;
import se.unlogic.hierarchy.core.annotations.ModuleSetting;
import se.unlogic.hierarchy.core.annotations.TextFieldSettingDescriptor;
import se.unlogic.hierarchy.core.annotations.WebPublic;
import se.unlogic.hierarchy.core.annotations.XSLVariable;
import se.unlogic.hierarchy.core.beans.SimpleAccessInterface;
import se.unlogic.hierarchy.core.beans.SimpleBackgroundModuleResponse;
import se.unlogic.hierarchy.core.beans.SimpleForegroundModuleResponse;
import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.hierarchy.core.enums.EventSource;
import se.unlogic.hierarchy.core.enums.SystemStatus;
import se.unlogic.hierarchy.core.events.CRUDEvent;
import se.unlogic.hierarchy.core.exceptions.AccessDeniedException;
import se.unlogic.hierarchy.core.exceptions.URINotFoundException;
import se.unlogic.hierarchy.core.interfaces.AccessInterface;
import se.unlogic.hierarchy.core.interfaces.BackgroundModuleResponse;
import se.unlogic.hierarchy.core.interfaces.ForegroundModuleResponse;
import se.unlogic.hierarchy.core.interfaces.listeners.SystemStartupListener;
import se.unlogic.hierarchy.core.utils.AccessUtils;
import se.unlogic.hierarchy.foregroundmodules.AnnotatedForegroundModule;
import se.unlogic.standardutils.collections.CollectionUtils;
import se.unlogic.standardutils.dao.HighLevelQuery;
import se.unlogic.standardutils.dao.LowLevelQuery;
import se.unlogic.standardutils.dao.OrderByCriteria;
import se.unlogic.standardutils.dao.QueryParameterFactory;
import se.unlogic.standardutils.dao.querys.ArrayListQuery;
import se.unlogic.standardutils.dao.querys.ObjectQuery;
import se.unlogic.standardutils.datatypes.SimpleEntry;
import se.unlogic.standardutils.date.DateUtils;
import se.unlogic.standardutils.enums.Order;
import se.unlogic.standardutils.io.FileUtils;
import se.unlogic.standardutils.json.JsonArray;
import se.unlogic.standardutils.json.JsonObject;
import se.unlogic.standardutils.json.JsonUtils;
import se.unlogic.standardutils.numbers.NumberUtils;
import se.unlogic.standardutils.populators.annotated.AnnotatedResultSetPopulator;
import se.unlogic.standardutils.string.AnnotatedBeanTagSourceFactory;
import se.unlogic.standardutils.string.TagReplacer;
import se.unlogic.standardutils.time.TimeUtils;
import se.unlogic.standardutils.validation.PositiveStringIntegerValidator;
import se.unlogic.standardutils.xml.XMLUtils;
import se.unlogic.webutils.http.HTTPUtils;
import se.unlogic.webutils.http.RequestUtils;
import se.unlogic.webutils.http.URIParser;

import com.nordicpeak.flowengine.beans.Flow;
import com.nordicpeak.flowengine.beans.FlowFamily;
import com.nordicpeak.flowengine.beans.FlowInstance;
import com.nordicpeak.flowengine.beans.Step;
import com.nordicpeak.flowengine.dao.FlowEngineDAOFactory;
import com.nordicpeak.flowengine.enums.ContentType;
import com.nordicpeak.flowengine.enums.StatisticsMode;
import com.nordicpeak.flowengine.interfaces.FlowSubmitSurveyProvider;

import it.sauronsoftware.cron4j.Scheduler;
import it.sauronsoftware.cron4j.Task;

public class StatisticsModule extends AnnotatedForegroundModule implements Runnable, SystemStartupListener {

	private static final AnnotatedBeanTagSourceFactory<FlowFamilyStatistics> FAMILY_STATISTICS_TAG_SOURCE_FACTORY = new AnnotatedBeanTagSourceFactory<FlowFamilyStatistics>(FlowFamilyStatistics.class, "$family.");

	private static final AnnotatedResultSetPopulator<IntegerEntry> INTEGER_ENTRY_POPULATOR = new AnnotatedResultSetPopulator<IntegerEntry>(IntegerEntry.class);

	private static final String GLOBAL_FLOW_INSTANCE_COUNT_QUERY = "SELECT DISTINCT(YEARWEEK(firstSubmitted)) as id, count(flowInstanceID) as value FROM flowengine_flow_instances WHERE firstSubmitted BETWEEN ? AND ? GROUP BY id ORDER BY id ASC;";
	private static final String GLOBAL_FLOW_FAMILY_COUNT = "SELECT COUNT(DISTINCT flowFamilyID) as value, YEARWEEK(?) as id FROM flowengine_flows WHERE publishDate <= ? AND (unPublishDate IS NULL OR unPublishDate > ?);";

	private static final String GLOBAL_INTERNAL_FLOW_FAMILY_COUNT = "SELECT COUNT(DISTINCT flowFamilyID) as value, YEARWEEK(?) as id FROM flowengine_flows INNER JOIN flowengine_steps ON(flowengine_steps.flowID = flowengine_flows.flowID) WHERE publishDate <= ? AND (unPublishDate IS NULL OR unPublishDate > ?);";
	private static final String GLOBAL_EXTERNAL_FLOW_FAMILY_COUNT = "SELECT COUNT(DISTINCT flowFamilyID) as value, YEARWEEK(?) as id FROM flowengine_flows WHERE externalLink IS NOT NULL AND publishDate <= ? AND (unPublishDate IS NULL OR unPublishDate > ?);";
	private static final String GLOBAL_PDFFORM_FLOW_FAMILY_COUNT = "SELECT COUNT(DISTINCT flowFamilyID) as value, YEARWEEK(?) as id FROM flowengine_flows LEFT OUTER JOIN flowengine_steps ON(flowengine_steps.flowID = flowengine_flows.flowID) INNER JOIN flowengine_flow_forms ON(flowengine_flow_forms.flowID = flowengine_flows.flowID) WHERE publishDate <= ? AND (unPublishDate IS NULL OR unPublishDate > ?) AND flowengine_steps.flowID IS NULL;";

	private static final String FLOW_INSTANCE_COUNT_QUERY = "SELECT DISTINCT(YEARWEEK(flowengine_flow_instances.firstSubmitted)) as id, count(flowInstanceID) as value FROM flowengine_flow_instances INNER JOIN flowengine_flows ON (flowengine_flows.flowID=flowengine_flow_instances.flowID) WHERE flowengine_flows.flowFamilyID = ? AND (flowengine_flow_instances.firstSubmitted BETWEEN ? AND ?) GROUP BY id ORDER BY id ASC;";
	private static final String EXTERNAL_FLOW_REDIRECT_COUNT_QUERY = "SELECT DISTINCT(YEARWEEK(flowengine_external_flow_redirects.time)) as id, count(redirectID)/4 as value FROM flowengine_external_flow_redirects INNER JOIN flowengine_flows ON (flowengine_flows.flowID=flowengine_external_flow_redirects.flowID) WHERE flowengine_flows.flowFamilyID = ? AND (flowengine_external_flow_redirects.time BETWEEN ? AND ?) GROUP BY id ORDER BY id ASC;";
	private static final String STEP_ABORT_COUNT_QUERY = "SELECT DISTINCT(stepID) as id, count(abortID) as value FROM flowengine_aborted_flow_instances WHERE flowID = ? AND added BETWEEN ? AND ? GROUP BY id;";
	private static final String STEP_UNSUBMITTED_COUNT_QUERY = "SELECT DISTINCT(stepID) as id, count(flowInstanceID) as value FROM flowengine_flow_instances WHERE statusID IN (SELECT statusID FROM flowengine_flow_statuses WHERE flowID = ? AND contentType = ?) AND ((updated IS NOT NULL AND updated BETWEEN ? AND ?) OR (updated IS NULL AND added BETWEEN ? AND ?)) GROUP BY id;";

	private static final String FLOW_INSTANCE_QUERY = "SELECT * FROM flowengine_flow_instances INNER JOIN flowengine_flows ON (flowengine_flows.flowID=flowengine_flow_instances.flowID) WHERE flowengine_flows.flowFamilyID = ? AND (flowengine_flow_instances.firstSubmitted BETWEEN ? AND ?);";

	public static final String CITIZEN_IDENTIFIER = "citizenIdentifier";

	@ModuleSetting
	@GroupMultiListSettingDescriptor(name = "Internal groups", description = "Groups containing users allowed to view internal statistics")
	private List<Integer> internalGroups;

	@ModuleSetting
	@TextFieldSettingDescriptor(name = "Weeks", description = "How many weeks back in time that statistics should be generated for.", formatValidator = PositiveStringIntegerValidator.class)
	private Integer weeksBackInTime = 20;

	@ModuleSetting
	@HTMLEditorSettingDescriptor(name = "Global statistics message", description = "The message displayed above the global statistics")
	private String globalStatisticsMessage;

	@ModuleSetting
	@HTMLEditorSettingDescriptor(name = "Flow statistics message", description = "The message displayed above the flow statistics")
	private String flowStatisticsMessage;

	@ModuleSetting
	@TextFieldSettingDescriptor(name = "Slot", description = "The slot for the generated background module responses")
	private String slot;

	@ModuleSetting
	@TextFieldSettingDescriptor(name = "Change check interval", description = "How often this module should check if there has been changes done which require the statistics to be reloaded", required = true, formatValidator = CronStringValidator.class)
	private String changeCheckInterval = "*/5 * * * *";

	@ModuleSetting
	@TextFieldSettingDescriptor(name = "Chart color", description = "The color code for generated charts (default #1F77B4)", required = true)
	private String chartColor = "#1F77B4";

	@ModuleSetting
	@CheckboxSettingDescriptor(name = "Enable data export support", description = "Controls if data from the graphs can be downloaded in CSV format.")
	private boolean enableExportSupport;

	@XSLVariable(name = "i18n.FlowInstancesPerWeek")
	private String flowInstanceCountChartLabel = "Not set";

	@XSLVariable(name = "i18n.RedirectsPerWeek")
	private String redirectCountChartLabel = "Not set";

	@XSLVariable(name = "java.flowFamilyCountChartLabel")
	private String flowFamilyCountChartLabel;

	@XSLVariable(name = "i18n.FlowStepAbortCountChartLabel")
	private String flowStepAbortCountChartLabel = "Not set";

	@XSLVariable(name = "i18n.FlowStepUnsubmittedCountChartLabel")
	private String flowStepUnsubmittedCountChartLabel = "Not set";

	@XSLVariable(name = "java.surveyRatingsChartLabel")
	private String surveyRatingsChartLabel;

	@XSLVariable(prefix = "java.")
	private String csvWeek = "Week";

	@XSLVariable(prefix = "java.")
	private String csvTotalCountWeek = "Total";

	@XSLVariable(prefix = "java.")
	private String csvFemaleCountWeek = "Female";

	@XSLVariable(prefix = "java.")
	private String csvMaleCountWeek = "Male";

	@XSLVariable(prefix = "java.")
	private String csvUnkownCountWeek = "Unknown";

	@XSLVariable(prefix = "java.")
	private String csvFlowInstanceCount = "Case count";

	@XSLVariable(prefix = "java.")
	private String csvFlowInstanceCountFile = "case count.csv";

	@XSLVariable(prefix = "java.")
	private String csvDetailedFlowInstanceCountFile = "detailed case count.csv";

	@XSLVariable(prefix = "java.")
	private String csvGlobalFlowCount = "E-service count";

	@XSLVariable(prefix = "java.")
	private String csvGlobaExternalFlowCount = "External e-service count";

	@XSLVariable(prefix = "java.")
	private String csvGlobaPDFFormFlowCount = "PDF form count";

	@XSLVariable(prefix = "java.")
	private String csvGlobalFlowCountFile = "service count.csv";

	@XSLVariable(prefix = "java.")
	private String csvDetailedGlobalFlowCountFile = "detailed service count.csv";

	@XSLVariable(prefix = "java.")
	private String csvFamilyRating = "Rating";

	@XSLVariable(prefix = "java.")
	private String csvFamilyRatingFile = "ratings.csv";

	@XSLVariable(prefix = "java.")
	private String csvStep = "Step";

	@XSLVariable(prefix = "java.")
	private String csvAbortCount = "Abort count";

	@XSLVariable(prefix = "java.")
	private String csvStepAbortCountFile = "abort count.csv";

	@XSLVariable(prefix = "java.")
	private String csvUnsubmittedCount = "Unsubmitted count";

	@XSLVariable(prefix = "java.")
	private String csvStepUnsubmittedCountFile = "unsubmitted count.csv";

	@XSLVariable(prefix = "java.")
	private String csvFlowName = "E-service";

	@XSLVariable(prefix = "java.")
	private String csvFlowType = "Category";
	
	@ModuleSetting
	@CheckboxSettingDescriptor(name = "Log SQL", description = "Log generated SQL queries")
	private boolean logSQL;

	@InstanceManagerDependency
	private FlowSubmitSurveyProvider surveyProvider;

	private FlowEngineDAOFactory daoFactory;

	private QueryParameterFactory<FlowFamily, StatisticsMode> statsModeParamFactory;

	private OrderByCriteria<Flow> flowVersionOrderCriteria;

	private LinkedHashMap<Integer, FlowFamilyStatistics> flowFamilyStatisticsMap;
	private List<IntegerEntry> globalFlowInstanceCount;
	private List<IntegerEntry> globalFlowFamilyCount;

	private List<IntegerEntry> globalInternalFlowFamilyCount;
	private List<IntegerEntry> globalExternalFlowFamilyCount;
	private List<IntegerEntry> globalPDFFormFlowFamilyCount;

	private List<FlowFamilyStatistics> publicFamilyStatistics;

	private AccessInterface internalAccessInterface;

	private Scheduler scheduler;

	private boolean changesDetected;

	@Override
	protected void createDAOs(DataSource dataSource) throws Exception {

		daoFactory = new FlowEngineDAOFactory(dataSource, systemInterface.getUserHandler(), systemInterface.getGroupHandler());

		statsModeParamFactory = daoFactory.getFlowFamilyDAO().getParamFactory("statisticsMode", StatisticsMode.class);

		flowVersionOrderCriteria = daoFactory.getFlowDAO().getOrderByCriteria("version", Order.DESC);
	}

	@Override
	protected void moduleConfigured() throws Exception {

		stopScheduler();

		internalAccessInterface = new SimpleAccessInterface(internalGroups, null);

		systemInterface.addSystemStartupListener(this);
	}

	@Override
	public void unload() throws Exception {

		stopScheduler();

		super.unload();
	}

	protected synchronized void initScheduler() {

		scheduler = new Scheduler();

		String taskID = scheduler.schedule("1 0 * * 1", new WeeklyStatisticsReloader());
		scheduler.schedule(this.changeCheckInterval, this);

		scheduler.start();

		Task task = scheduler.getTask(taskID);
		scheduler.launch(task);
	}

	protected synchronized void stopScheduler() {

		try {
			if (scheduler != null) {

				scheduler.stop();
				scheduler = null;
			}

		} catch (IllegalStateException e) {
			log.error("Error stopping scheduler", e);
		}
	}

	private void cacheStatistics() throws SQLException {

		Calendar calendar = GregorianCalendar.getInstance();
		calendar.add(Calendar.WEEK_OF_YEAR, -1);
		calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);

		Timestamp endDate = DateUtils.setTimeToMaximum(new Timestamp(calendar.getTimeInMillis()));

		calendar.add(Calendar.WEEK_OF_YEAR, -weeksBackInTime);
		calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);

		Timestamp startDate = DateUtils.setTimeToMidnight(new Timestamp(calendar.getTimeInMillis()));

		long startTime = System.currentTimeMillis();

		log.info("Generating statistics for perdiod: " + DateUtils.DATE_TIME_FORMATTER.format(startDate) + " - " + DateUtils.DATE_TIME_FORMATTER.format(endDate));

		HighLevelQuery<FlowFamily> flowFamiliesQuery = new HighLevelQuery<FlowFamily>(FlowFamily.FLOWS_RELATION, Flow.STEPS_RELATION, Flow.FLOW_TYPE_RELATION);

		flowFamiliesQuery.addParameter(statsModeParamFactory.getIsNotNullParameter());
		flowFamiliesQuery.addRelationOrderByCriteria(Flow.class, flowVersionOrderCriteria);

		List<FlowFamily> flowFamilies = daoFactory.getFlowFamilyDAO().getAll(flowFamiliesQuery);

		LinkedHashMap<Integer, FlowFamilyStatistics> statisticsMap = new LinkedHashMap<Integer, FlowFamilyStatistics>();

		List<FlowFamilyStatistics> publicList = new ArrayList<FlowFamilyStatistics>();

		if (flowFamilies != null) {

			List<FlowFamilyStatistics> globalList = new ArrayList<FlowFamilyStatistics>();

			for (FlowFamily flowFamily : flowFamilies) {

				if(systemInterface.getSystemStatus() == SystemStatus.STOPPING){
					
					log.info("Caching of statistics aborted...");
					return;
				}
				
				if (flowFamily.getFlows() == null) {

					log.warn("Flow familiy with ID " + flowFamily.getFlowFamilyID() + " has no flows");
					continue;
				}

				FlowFamilyStatistics familyStatistics = new FlowFamilyStatistics();

				familyStatistics.setFlowFamilyID(flowFamily.getFlowFamilyID());
				familyStatistics.setName(flowFamily.getFlows().get(0).getName());
				familyStatistics.setFlowTypeName(flowFamily.getFlows().get(0).getFlowType().getName());
				familyStatistics.setFlowInstanceCount(getFlowFamilyInstanceCount(flowFamily.getFlowFamilyID(), startDate, endDate));
				familyStatistics.setExternalRedirectCount(getExternalFlowRedirectCount(flowFamily.getFlowFamilyID(), startDate, endDate));
				familyStatistics.setStatisticsMode(flowFamily.getStatisticsMode());

				if (enableExportSupport) {

					List<Entry<String, List<FlowInstance>>> flowInstancesByWeek = getFlowInstancesByWeek(flowFamily.getFlowFamilyID());

					if (flowInstancesByWeek != null) {

						List<Entry<String, Integer>> totalFlowInstancesCountByWeek = new ArrayList<Entry<String, Integer>>();
						List<Entry<String, Integer>> femaleFlowInstancesCountByWeek = new ArrayList<Entry<String, Integer>>();
						List<Entry<String, Integer>> maleFlowInstancesCountByWeek = new ArrayList<Entry<String, Integer>>();
						List<Entry<String, Integer>> unkownFlowInstancesCountByWeek = new ArrayList<Entry<String, Integer>>();

						for (Entry<String, List<FlowInstance>> entry : flowInstancesByWeek) {

							if(systemInterface.getSystemStatus() == SystemStatus.STOPPING){
								
								log.info("Caching of statistics aborted...");
								return;
							}
							
							int totalCount = 0;
							int femaleCount = 0;
							int maleCount = 0;
							int unkownCount = 0;

							if (entry.getValue() != null) {

								totalCount = entry.getValue().size();

								for (FlowInstance flowInstance : entry.getValue()) {

									if(systemInterface.getSystemStatus() == SystemStatus.STOPPING){
										
										log.info("Caching of statistics aborted...");
										return;
									}
									
									String citizenIdentifier;

									if (flowInstance.getPoster() == null || flowInstance.getPoster().getAttributeHandler() == null || (citizenIdentifier = flowInstance.getPoster().getAttributeHandler().getString(CITIZEN_IDENTIFIER)) == null) {
										unkownCount++;
										continue;
									}

									String genderPart = citizenIdentifier.substring(citizenIdentifier.length() - 2, citizenIdentifier.length() - 1);

									Integer genderPartInt = NumberUtils.toInt(genderPart);
									
									if(genderPartInt == null){
										
										unkownCount++;
										continue;
									}
									
									if (genderPartInt % 2 == 0) {
										femaleCount++;
									} else {
										maleCount++;
									}

								}

							}

							totalFlowInstancesCountByWeek.add(new SimpleEntry<String, Integer>(entry.getKey(), totalCount));
							femaleFlowInstancesCountByWeek.add(new SimpleEntry<String, Integer>(entry.getKey(), femaleCount));
							maleFlowInstancesCountByWeek.add(new SimpleEntry<String, Integer>(entry.getKey(), maleCount));
							unkownFlowInstancesCountByWeek.add(new SimpleEntry<String, Integer>(entry.getKey(), unkownCount));

						}

						familyStatistics.setTotalFlowInstancesCountByWeek(totalFlowInstancesCountByWeek);
						familyStatistics.setFemaleFlowInstancesCountByWeek(femaleFlowInstancesCountByWeek);
						familyStatistics.setMaleFlowInstancesCountByWeek(maleFlowInstancesCountByWeek);
						familyStatistics.setUnkownFlowInstancesCountByWeek(unkownFlowInstancesCountByWeek);

					}

				}

				if (surveyProvider != null) {

					familyStatistics.setSurveyRating(getSurveyRating(flowFamily.getFlowFamilyID()));
				}

				//TODO get finished versus aborted

				familyStatistics.setFlowStatistics(new LinkedHashMap<Integer, FlowStatistics>(flowFamily.getFlows().size()));

				for (Flow flow : flowFamily.getFlows()) {

					if(systemInterface.getSystemStatus() == SystemStatus.STOPPING){
						
						log.info("Caching of statistics aborted...");
						return;
					}

					FlowStatistics flowStatistics = new FlowStatistics();

					flowStatistics.setFlowID(flow.getFlowID());
					flowStatistics.setVersion(flow.getVersion());
					flowStatistics.setSteps(flow.getSteps());

					//Get saved step statistics
					flowStatistics.setStepUnsubmittedCount(getUnsubmittedInstancesPerStep(flow, startDate, endDate));

					//Get step abort statistics
					flowStatistics.setStepAbortCount(getAbortedInstancesPerStep(flow, startDate, endDate));

					familyStatistics.getFlowStatistics().put(flow.getFlowID(), flowStatistics);
				}

				if (!familyStatistics.getFlowStatistics().isEmpty()) {

					globalList.add(familyStatistics);

					if (familyStatistics.getStatisticsMode() == StatisticsMode.PUBLIC) {

						publicList.add(familyStatistics);
					}
				}
			}

			//Sorting
			if (!globalList.isEmpty()) {

				Collections.sort(globalList);

				for (FlowFamilyStatistics familyStatistics : globalList) {

					statisticsMap.put(familyStatistics.getFlowFamilyID(), familyStatistics);
				}

				if (!publicList.isEmpty()) {
					Collections.sort(publicList);
				}
			}
		}

		this.globalFlowInstanceCount = getGlobalFlowInstanceCount(startDate, endDate);
		this.globalFlowFamilyCount = getGlobalFlowFamilyCount();
		this.globalInternalFlowFamilyCount = getGlobalInternalFlowFamilyCount();
		this.globalExternalFlowFamilyCount = getGlobalExternalFlowFamilyCount();
		this.globalPDFFormFlowFamilyCount = getGlobalPDFFormFlowFamilyCount();
		this.flowFamilyStatisticsMap = statisticsMap;
		this.publicFamilyStatistics = publicList;

		log.info("Statistics generated in " + TimeUtils.millisecondsToString(System.currentTimeMillis() - startTime));
	}

	private List<FloatEntry> getSurveyRating(Integer flowFamilyID) throws SQLException {

		Calendar calendar = GregorianCalendar.getInstance();

		List<FloatEntry> entries = new ArrayList<FloatEntry>(weeksBackInTime);

		calendar.add(Calendar.WEEK_OF_YEAR, -weeksBackInTime);

		int weeksChecked = 0;

		while (weeksChecked < weeksBackInTime) {

			calendar.add(Calendar.WEEK_OF_YEAR, 1);
			calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);

			Timestamp endDate = DateUtils.setTimeToMaximum(new Timestamp(calendar.getTimeInMillis()));

			calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);

			Timestamp startDate = DateUtils.setTimeToMidnight(new Timestamp(calendar.getTimeInMillis()));

			Float rating = surveyProvider.getWeeklyAverage(flowFamilyID, startDate, endDate);

			if (rating != null) {

				FloatEntry entry = new FloatEntry();

				entry.setId((calendar.get(Calendar.YEAR) * 100) + calendar.get(Calendar.WEEK_OF_YEAR));
				entry.setValue(rating);

				entries.add(entry);

			}

			weeksChecked++;
		}

		if (entries.isEmpty()) {

			return null;
		}

		return entries;
	}

	private List<IntegerEntry> getGlobalFlowFamilyCount() throws SQLException {

		return getGlobalFlowFamilyCount(GLOBAL_FLOW_FAMILY_COUNT);
	}

	private List<IntegerEntry> getGlobalPDFFormFlowFamilyCount() throws SQLException {

		return getGlobalFlowFamilyCount(GLOBAL_PDFFORM_FLOW_FAMILY_COUNT);
	}

	private List<IntegerEntry> getGlobalExternalFlowFamilyCount() throws SQLException {

		return getGlobalFlowFamilyCount(GLOBAL_EXTERNAL_FLOW_FAMILY_COUNT);
	}

	private List<IntegerEntry> getGlobalInternalFlowFamilyCount() throws SQLException {

		return getGlobalFlowFamilyCount(GLOBAL_INTERNAL_FLOW_FAMILY_COUNT);
	}

	private List<IntegerEntry> getGlobalFlowFamilyCount(String sqlQuery) throws SQLException {

		Calendar calendar = GregorianCalendar.getInstance();

		List<IntegerEntry> entries = new ArrayList<IntegerEntry>(weeksBackInTime);

		calendar.add(Calendar.WEEK_OF_YEAR, -weeksBackInTime);

		while (entries.size() < weeksBackInTime) {

			calendar.add(Calendar.WEEK_OF_YEAR, 1);
			calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);

			Timestamp endDate = DateUtils.setTimeToMaximum(new Timestamp(calendar.getTimeInMillis()));

			calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);

			Timestamp startDate = DateUtils.setTimeToMidnight(new Timestamp(calendar.getTimeInMillis()));

			entries.add(getFlowFamilyCount(startDate, endDate, sqlQuery));

		}

		return entries;

	}

	private List<Entry<String, List<FlowInstance>>> getFlowInstancesByWeek(Integer flowFamilyID) throws SQLException {

		Calendar calendar = GregorianCalendar.getInstance();

		List<Entry<String, List<FlowInstance>>> entries = new ArrayList<Entry<String, List<FlowInstance>>>();

		calendar.add(Calendar.WEEK_OF_YEAR, -weeksBackInTime);

		while (entries.size() < weeksBackInTime) {

			calendar.add(Calendar.WEEK_OF_YEAR, 1);
			calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);

			Timestamp endDate = DateUtils.setTimeToMaximum(new Timestamp(calendar.getTimeInMillis()));

			calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);

			Timestamp startDate = DateUtils.setTimeToMidnight(new Timestamp(calendar.getTimeInMillis()));

			String week = calendar.get(Calendar.YEAR) + " v." + calendar.get(Calendar.WEEK_OF_YEAR) + "";

			List<FlowInstance> flowInstances = getFlowInstances(flowFamilyID, startDate, endDate);

			SimpleEntry<String, List<FlowInstance>> entry = new SimpleEntry<String, List<FlowInstance>>(week, flowInstances);

			entries.add(entry);

		}

		return entries;

	}

	private List<FlowInstance> getFlowInstances(Integer flowFamilyID, Timestamp startDate, Timestamp endDate) throws SQLException {

		LowLevelQuery<FlowInstance> query = new LowLevelQuery<FlowInstance>(FLOW_INSTANCE_QUERY);

		query.addParameter(flowFamilyID);
		query.addParameter(startDate);
		query.addParameter(endDate);

		return daoFactory.getFlowInstanceDAO().getAll(query);

	}

	private IntegerEntry getFlowFamilyCount(Timestamp startDate, Timestamp endDate, String sqlQuery) throws SQLException {

		ObjectQuery<IntegerEntry> query = new ObjectQuery<IntegerEntry>(dataSource, sqlQuery, INTEGER_ENTRY_POPULATOR);

		query.setTimestamp(1, startDate);
		query.setTimestamp(2, startDate);
		query.setTimestamp(3, endDate);

		if (logSQL) {

			log.info(query);
		}

		return query.executeQuery();
	}

	@Override
	public ForegroundModuleResponse defaultMethod(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Throwable {

		log.info("User " + user + " listing global statistics");
		
		Document doc = createDocument(req, uriParser);

		Element globalStatisticsElement = doc.createElement("GlobalStatistics");
		doc.getDocumentElement().appendChild(globalStatisticsElement);

		if (globalFlowInstanceCount != null) {

			JsonArray weekArray = new JsonArray();

			JsonArray submitCountArray = new JsonArray();
			submitCountArray.addNode(flowInstanceCountChartLabel);

			for (IntegerEntry entry : globalFlowInstanceCount) {

				weekArray.addNode(formatWeek(entry.getId()));
				submitCountArray.addNode(entry.getValue());
			}

			XMLUtils.appendNewElement(doc, globalStatisticsElement, "FlowInstanceCountWeeks", weekArray.toJson());
			XMLUtils.appendNewElement(doc, globalStatisticsElement, "FlowInstanceCountValues", submitCountArray.toJson());
		}

		if (globalFlowFamilyCount != null) {

			JsonArray weekArray = new JsonArray();

			JsonArray familyCountArray = new JsonArray();
			familyCountArray.addNode(flowFamilyCountChartLabel);

			for (IntegerEntry entry : globalFlowFamilyCount) {

				weekArray.addNode(formatWeek(entry.getId()));
				familyCountArray.addNode(entry.getValue());
			}

			XMLUtils.appendNewElement(doc, globalStatisticsElement, "FlowFamilyCountWeeks", weekArray.toJson());
			XMLUtils.appendNewElement(doc, globalStatisticsElement, "FlowFamilyCountValues", familyCountArray.toJson());
		}

		XMLUtils.appendNewCDATAElement(doc, globalStatisticsElement, "Message", globalStatisticsMessage);

		return appendBackgroundModuleResponse(new SimpleForegroundModuleResponse(doc, moduleDescriptor.getName(), getDefaultBreadcrumb()), user, req, uriParser);
	}

	private ForegroundModuleResponse appendBackgroundModuleResponse(SimpleForegroundModuleResponse moduleResponse, User user, HttpServletRequest req, URIParser uriParser) throws TransformerConfigurationException {

		if (slot != null) {

			Collection<FlowFamilyStatistics> familyStatistics;

			if (AccessUtils.checkAccess(user, internalAccessInterface)) {

				if (flowFamilyStatisticsMap == null) {
					
					return showStatisticsLoading(req, uriParser);
				}
				
				familyStatistics = this.flowFamilyStatisticsMap.values();

			} else {

				familyStatistics = publicFamilyStatistics;
			}

			if (!CollectionUtils.isEmpty(familyStatistics)) {

				Document doc = createDocument(req, uriParser);
				Element flowFamillyListElement = XMLUtils.appendNewElement(doc, doc.getDocumentElement(), "FlowFamilyList");

				XMLUtils.append(doc, flowFamillyListElement, familyStatistics);

				SimpleBackgroundModuleResponse backgroundModuleResponse = new SimpleBackgroundModuleResponse(doc, getTransformer());
				backgroundModuleResponse.setSlots(Collections.singletonList(slot));

				ArrayList<BackgroundModuleResponse> backgroundModuleResponses = new ArrayList<BackgroundModuleResponse>();
				backgroundModuleResponses.add(backgroundModuleResponse);

				moduleResponse.addBackgroundModuleResponses(backgroundModuleResponses);
			}
		}

		return moduleResponse;
	}

	private Transformer getTransformer() throws TransformerConfigurationException {

		return this.sectionInterface.getModuleXSLTCache().getModuleTranformer(moduleDescriptor);
	}

	private static String formatWeek(Integer id) {

		String weekString = id.toString();

		weekString = weekString.substring(0, 4) + " v." + Integer.parseInt(weekString.substring(4, 6));

		return weekString;
	}

	private List<IntegerEntry> getFlowFamilyInstanceCount(Integer flowFamilyID, Timestamp startDate, Timestamp endDate) throws SQLException {

		ArrayListQuery<IntegerEntry> query = new ArrayListQuery<IntegerEntry>(dataSource, FLOW_INSTANCE_COUNT_QUERY, INTEGER_ENTRY_POPULATOR);

		query.setInt(1, flowFamilyID);
		query.setTimestamp(2, startDate);
		query.setTimestamp(3, endDate);

		if (logSQL) {

			log.info(query);
		}

		return query.executeQuery();
	}

	private List<IntegerEntry> getExternalFlowRedirectCount(Integer flowFamilyID, Timestamp startDate, Timestamp endDate) throws SQLException {

		ArrayListQuery<IntegerEntry> query = new ArrayListQuery<IntegerEntry>(dataSource, EXTERNAL_FLOW_REDIRECT_COUNT_QUERY, INTEGER_ENTRY_POPULATOR);

		query.setInt(1, flowFamilyID);
		query.setTimestamp(2, startDate);
		query.setTimestamp(3, endDate);

		if (logSQL) {

			log.info(query);
		}

		return query.executeQuery();
	}

	@WebPublic(alias = "family")
	public ForegroundModuleResponse showFlowFamilyStatistics(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Throwable {

		if (flowFamilyStatisticsMap == null) {
			
			return showStatisticsLoading(req, uriParser);
		}
		
		FlowFamilyStatistics familyStatistics = this.flowFamilyStatisticsMap.get(uriParser.getInt(2));

		if (familyStatistics == null) {

			throw new URINotFoundException(uriParser);

		} else if (familyStatistics.getStatisticsMode() == StatisticsMode.INTERNAL && !AccessUtils.checkAccess(user, internalAccessInterface)) {

			throw new AccessDeniedException("Access to internal statistics denied");
		}

		log.info("User " + user + " listing statistics for flow family " + familyStatistics);

		Document doc = createDocument(req, uriParser);

		Element familyStatisticsElement = doc.createElement("FlowFamilyStatistics");
		doc.getDocumentElement().appendChild(familyStatisticsElement);

		XMLUtils.appendNewElement(doc, familyStatisticsElement, "flowFamilyID", familyStatistics.getFlowFamilyID());

		if (familyStatistics.getFlowInstanceCount() != null) {

			JsonArray weekArray = new JsonArray();

			JsonArray submitCountArray = new JsonArray();
			submitCountArray.addNode(flowInstanceCountChartLabel);

			for (IntegerEntry entry : familyStatistics.getFlowInstanceCount()) {

				weekArray.addNode(formatWeek(entry.getId()));
				submitCountArray.addNode(entry.getValue());
			}

			XMLUtils.appendNewElement(doc, familyStatisticsElement, "FlowInstanceCountWeeks", weekArray.toJson());
			XMLUtils.appendNewElement(doc, familyStatisticsElement, "FlowInstanceCountValues", submitCountArray.toJson());
		}

		if (familyStatistics.getExternalRedirectCount() != null) {

			JsonArray weekArray = new JsonArray();

			JsonArray redirectCountArray = new JsonArray();
			redirectCountArray.addNode(redirectCountChartLabel);

			for (IntegerEntry entry : familyStatistics.getExternalRedirectCount()) {

				weekArray.addNode(formatWeek(entry.getId()));
				redirectCountArray.addNode(entry.getValue());
			}

			XMLUtils.appendNewElement(doc, familyStatisticsElement, "ExternalFlowRedirectCountWeeks", weekArray.toJson());
			XMLUtils.appendNewElement(doc, familyStatisticsElement, "ExternalFlowRedirectCountValues", redirectCountArray.toJson());
		}

		FlowStatistics flowStatistics = familyStatistics.getFlowStatistics().values().iterator().next();

		if (flowStatistics.getSteps() != null) {

			JsonArray stepArray = new JsonArray();

			JsonArray abortCountArray = new JsonArray();
			abortCountArray.addNode(flowStepAbortCountChartLabel);

			JsonArray unsubmittedCountArray = new JsonArray();
			unsubmittedCountArray.addNode(flowStepUnsubmittedCountChartLabel);

			for (Step step : flowStatistics.getSteps()) {

				stepArray.addNode(step.getName());

				abortCountArray.addNode(getMatchingEntryValue(step, flowStatistics.getStepAbortCount()));
				unsubmittedCountArray.addNode(getMatchingEntryValue(step, flowStatistics.getStepUnsubmittedCount()));
			}

			XMLUtils.appendNewElement(doc, familyStatisticsElement, "Steps", stepArray.toJson());
			XMLUtils.appendNewElement(doc, familyStatisticsElement, "StepAbortCount", abortCountArray.toJson());
			XMLUtils.appendNewElement(doc, familyStatisticsElement, "StepUnsubmittedCount", unsubmittedCountArray.toJson());

		}

		TagReplacer tagReplacer = new TagReplacer(FAMILY_STATISTICS_TAG_SOURCE_FACTORY.getTagSource(familyStatistics));

		XMLUtils.appendNewCDATAElement(doc, familyStatisticsElement, "Message", tagReplacer.replace(flowStatisticsMessage));

		XMLUtils.append(doc, familyStatisticsElement, "Versions", familyStatistics.getFlowStatistics().values());

		if (familyStatistics.getSurveyRating() != null) {

			JsonArray weekArray = new JsonArray();

			JsonArray ratingArray = new JsonArray();
			ratingArray.addNode(surveyRatingsChartLabel);

			for (FloatEntry entry : familyStatistics.getSurveyRating()) {

				weekArray.addNode(formatWeek(entry.getId()));
				ratingArray.addNode(entry.getValue());
			}

			XMLUtils.appendNewElement(doc, familyStatisticsElement, "RatingWeeks", weekArray.toJson());
			XMLUtils.appendNewElement(doc, familyStatisticsElement, "RatingValues", ratingArray.toJson());
		}

		return appendBackgroundModuleResponse(new SimpleForegroundModuleResponse(doc, moduleDescriptor.getName(), getDefaultBreadcrumb()), user, req, uriParser);
	}

	@WebPublic(alias = "versionabortcount")
	public ForegroundModuleResponse getVersionAbortCount(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Throwable {

		FlowFamilyStatistics familyStatistics = this.flowFamilyStatisticsMap.get(uriParser.getInt(2));

		if (familyStatistics == null) {

			throw new URINotFoundException(uriParser);

		} else if (familyStatistics.getStatisticsMode() == StatisticsMode.INTERNAL && !AccessUtils.checkAccess(user, internalAccessInterface)) {

			throw new AccessDeniedException("Access to internal statistics denied");
		}

		FlowStatistics flowStatistics = familyStatistics.getFlowStatistics().get(uriParser.getInt(3));

		if (flowStatistics == null) {

			throw new URINotFoundException(uriParser);
		}

		log.info("User " + user + " getting step abort statistics for flowID " + flowStatistics.getFlowID());

		JsonArray stepArray = new JsonArray();

		JsonArray abortCountArray = new JsonArray();
		abortCountArray.addNode(flowStepAbortCountChartLabel);

		for (Step step : flowStatistics.getSteps()) {

			stepArray.addNode(step.getName());

			abortCountArray.addNode(getMatchingEntryValue(step, flowStatistics.getStepAbortCount()));
		}

		JsonObject jsonObject = new JsonObject(2);
		jsonObject.putField("steps", stepArray);
		jsonObject.putField("count", abortCountArray);

		HTTPUtils.sendReponse(jsonObject.toJson(), JsonUtils.getContentType(), res);

		return null;
	}

	@WebPublic(alias = "versionunsubmitcount")
	public ForegroundModuleResponse getVersionUnsubmitCount(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Throwable {

		FlowFamilyStatistics familyStatistics = this.flowFamilyStatisticsMap.get(uriParser.getInt(2));

		if (familyStatistics == null) {

			throw new URINotFoundException(uriParser);

		} else if (familyStatistics.getStatisticsMode() == StatisticsMode.INTERNAL && !AccessUtils.checkAccess(user, internalAccessInterface)) {

			throw new AccessDeniedException("Access to internal statistics denied");
		}

		FlowStatistics flowStatistics = familyStatistics.getFlowStatistics().get(uriParser.getInt(3));

		if (flowStatistics == null) {

			throw new URINotFoundException(uriParser);
		}

		log.info("User " + user + " getting step saved statistics for flowID " + flowStatistics.getFlowID());

		JsonArray stepArray = new JsonArray();

		JsonArray unsubmittedCountArray = new JsonArray();
		unsubmittedCountArray.addNode(flowStepUnsubmittedCountChartLabel);

		for (Step step : flowStatistics.getSteps()) {

			stepArray.addNode(step.getName());

			unsubmittedCountArray.addNode(getMatchingEntryValue(step, flowStatistics.getStepUnsubmittedCount()));
		}

		JsonObject jsonObject = new JsonObject(2);
		jsonObject.putField("steps", stepArray);
		jsonObject.putField("count", unsubmittedCountArray);

		HTTPUtils.sendReponse(jsonObject.toJson(), JsonUtils.getContentType(), res);

		return null;
	}

	@WebPublic(alias = "instancecount")
	public ForegroundModuleResponse downloadGlobalFlowInstanceCount(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Throwable {

		log.info("User " + user + " downloading global flow instance count statistics");

		sendCSV(res, user, globalFlowInstanceCount, csvWeek, csvFlowInstanceCount, csvFlowInstanceCountFile);

		return null;
	}

	@WebPublic(alias = "detailedinstancecount")
	public ForegroundModuleResponse downloadDetailedGlobalFlowInstanceCount(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Throwable {

		if (!enableExportSupport) {
			throw new URINotFoundException(uriParser);
		}

		log.info("User " + user + " downloading detailed global flow instance count statistics");

		res.setHeader("Content-Disposition", "attachment;filename=\"" + FileUtils.toValidHttpFilename(csvDetailedFlowInstanceCountFile) + "\"");
		res.setContentType("text/csv");

		Writer writer = res.getWriter();

		boolean appendFirstRow = true;

		try {

			for (Map.Entry<Integer, FlowFamilyStatistics> flowFamilyEntry : flowFamilyStatisticsMap.entrySet()) {

				FlowFamilyStatistics flowFamilyStatistics = flowFamilyEntry.getValue();

				if (flowFamilyStatistics.getStatisticsMode() == StatisticsMode.INTERNAL && !AccessUtils.checkAccess(user, internalAccessInterface)) {
					continue;
				}

				if (appendFirstRow) {

					writer.write(csvFlowType);
					writer.write(";");
					writer.write(csvFlowName);
					writer.write(";");

					if (flowFamilyStatistics.getTotalFlowInstancesCountByWeek() != null) {

						for (Entry<String, Integer> entry : flowFamilyStatistics.getTotalFlowInstancesCountByWeek()) {

							writer.write(entry.getKey() + " " + csvTotalCountWeek);
							writer.write(";");
							writer.write(entry.getKey() + " " + csvFemaleCountWeek);
							writer.write(";");
							writer.write(entry.getKey() + " " + csvMaleCountWeek);
							writer.write(";");
							writer.write(entry.getKey() + " " + csvUnkownCountWeek);
							writer.write(";");
						}
					}

					writer.write("\n");

					appendFirstRow = false;
				}

				writer.write(flowFamilyStatistics.getFlowTypeName());
				writer.write(";");
				writer.write(flowFamilyStatistics.getName());
				writer.write(";");

				if (flowFamilyStatistics.getTotalFlowInstancesCountByWeek() != null) {

					for (int i = 0; i < flowFamilyStatistics.getTotalFlowInstancesCountByWeek().size(); i++) {

						writer.write(flowFamilyStatistics.getTotalFlowInstancesCountByWeek().get(i).getValue() + ";");
						writer.write(flowFamilyStatistics.getFemaleFlowInstancesCountByWeek().get(i).getValue() + ";");
						writer.write(flowFamilyStatistics.getMaleFlowInstancesCountByWeek().get(i).getValue() + ";");
						writer.write(flowFamilyStatistics.getUnkownFlowInstancesCountByWeek().get(i).getValue() + ";");

					}

					writer.write("\n");

				}

			}

			writer.flush();

		} catch (IOException e) {

			log.info("Error sending " + csvDetailedFlowInstanceCountFile + " to user " + user);
		}

		return null;
	}

	@WebPublic(alias = "flowcount")
	public ForegroundModuleResponse downloadGlobalFlowCount(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Throwable {

		log.info("User " + user + " downloading global flow count statistics");

		sendCSV(res, user, globalFlowFamilyCount, csvWeek, csvGlobalFlowCount, csvGlobalFlowCountFile);

		return null;
	}

	@WebPublic(alias = "detailedflowcount")
	public ForegroundModuleResponse downloadDetailedGlobalFlowCount(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Throwable {

		log.info("User " + user + " downloading detailed global flow count statistics");

		res.setHeader("Content-Disposition", "attachment;filename=\"" + FileUtils.toValidHttpFilename(csvDetailedGlobalFlowCountFile) + "\"");
		res.setContentType("text/csv");

		try {
			Writer writer = res.getWriter();

			writer.write(csvWeek);
			writer.write(";");
			writer.write(csvGlobalFlowCount);
			writer.write(";");
			writer.write(csvGlobaExternalFlowCount);
			writer.write(";");
			writer.write(csvGlobaPDFFormFlowCount);
			writer.write(";\n");

			if (globalInternalFlowFamilyCount != null) {

				for (int i = 0; i < globalInternalFlowFamilyCount.size(); i++) {

					writer.write(formatWeek(globalInternalFlowFamilyCount.get(i).getId()));
					writer.write(";");
					writer.write(globalInternalFlowFamilyCount.get(i).getValue().toString());
					writer.write(";");
					writer.write(globalExternalFlowFamilyCount.get(i).getValue().toString());
					writer.write(";");
					writer.write(globalPDFFormFlowFamilyCount.get(i).getValue().toString());
					writer.write(";\n");

				}

			}

			writer.flush();

		} catch (IOException e) {

			log.info("Error sending " + csvDetailedGlobalFlowCountFile + " to user " + user);
		}

		return null;
	}

	@WebPublic(alias = "familiyinstancecount")
	public ForegroundModuleResponse downloadFlowFamilyInstanceCount(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Throwable {

		FlowFamilyStatistics familyStatistics = this.flowFamilyStatisticsMap.get(uriParser.getInt(2));

		if (familyStatistics == null) {

			throw new URINotFoundException(uriParser);

		} else if (familyStatistics.getStatisticsMode() == StatisticsMode.INTERNAL && !AccessUtils.checkAccess(user, internalAccessInterface)) {

			throw new AccessDeniedException("Access to internal statistics denied");
		}

		log.info("User " + user + " downloading family instance count statistics for family " + familyStatistics);

		sendCSV(res, user, familyStatistics.getFlowInstanceCount(), csvWeek, csvFlowInstanceCount, familyStatistics.getName() + " - " + csvFlowInstanceCountFile);

		return null;
	}

	@WebPublic(alias = "redirectcount")
	public ForegroundModuleResponse downloadExternalFlowRedirectCount(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Throwable {

		FlowFamilyStatistics familyStatistics = this.flowFamilyStatisticsMap.get(uriParser.getInt(2));

		if (familyStatistics == null) {

			throw new URINotFoundException(uriParser);

		} else if (familyStatistics.getStatisticsMode() == StatisticsMode.INTERNAL && !AccessUtils.checkAccess(user, internalAccessInterface)) {

			throw new AccessDeniedException("Access to internal statistics denied");
		}

		log.info("User " + user + " downloading external flow redirect count statistics for family " + familyStatistics);

		sendCSV(res, user, familyStatistics.getExternalRedirectCount(), csvWeek, csvFlowInstanceCount, familyStatistics.getName() + " - " + csvFlowInstanceCountFile);

		return null;
	}

	@WebPublic(alias = "ratings")
	public ForegroundModuleResponse downloadFlowFamilyRating(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Throwable {

		FlowFamilyStatistics familyStatistics = this.flowFamilyStatisticsMap.get(uriParser.getInt(2));

		if (familyStatistics == null) {

			throw new URINotFoundException(uriParser);

		} else if (familyStatistics.getStatisticsMode() == StatisticsMode.INTERNAL && !AccessUtils.checkAccess(user, internalAccessInterface)) {

			throw new AccessDeniedException("Access to internal statistics denied");
		}

		log.info("User " + user + " downloading rating statistics for family " + familyStatistics);

		sendCSV(res, user, familyStatistics.getSurveyRating(), csvWeek, csvFamilyRating, familyStatistics.getName() + " - " + csvFamilyRatingFile);

		return null;
	}

	@WebPublic(alias = "stepabortcount")
	public ForegroundModuleResponse downloadStepAbortCount(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Throwable {

		FlowFamilyStatistics familyStatistics = this.flowFamilyStatisticsMap.get(uriParser.getInt(2));

		if (familyStatistics == null) {

			throw new URINotFoundException(uriParser);

		} else if (familyStatistics.getStatisticsMode() == StatisticsMode.INTERNAL && !AccessUtils.checkAccess(user, internalAccessInterface)) {

			throw new AccessDeniedException("Access to internal statistics denied");
		}

		FlowStatistics flowStatistics = familyStatistics.getFlowStatistics().get(uriParser.getInt(3));

		if (flowStatistics == null) {

			throw new URINotFoundException(uriParser);
		}

		log.info("User " + user + " downloading step abort statistics for flowID " + flowStatistics.getFlowID());

		sendCSV(res, user, flowStatistics.getSteps(), flowStatistics.getStepAbortCount(), csvStep, csvAbortCount, familyStatistics.getName() + " - " + flowStatistics.getVersion() + " - " + csvStepAbortCountFile);

		return null;
	}

	@WebPublic(alias = "unsubmitcount")
	public ForegroundModuleResponse downloadStepUnsubmitCount(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Throwable {

		FlowFamilyStatistics familyStatistics = this.flowFamilyStatisticsMap.get(uriParser.getInt(2));

		if (familyStatistics == null) {

			throw new URINotFoundException(uriParser);

		} else if (familyStatistics.getStatisticsMode() == StatisticsMode.INTERNAL && !AccessUtils.checkAccess(user, internalAccessInterface)) {

			throw new AccessDeniedException("Access to internal statistics denied");
		}

		FlowStatistics flowStatistics = familyStatistics.getFlowStatistics().get(uriParser.getInt(3));

		if (flowStatistics == null) {

			throw new URINotFoundException(uriParser);
		}

		log.info("User " + user + " downloading step unsubmitted statistics for flowID " + flowStatistics.getFlowID());

		sendCSV(res, user, flowStatistics.getSteps(), flowStatistics.getStepUnsubmittedCount(), csvStep, csvUnsubmittedCount, familyStatistics.getName() + " - " + flowStatistics.getVersion() + " - " + csvStepUnsubmittedCountFile);

		return null;
	}

	private void sendCSV(HttpServletResponse res, User user, List<? extends NumberEntry> values, String col1, String col2, String fileName) {

		res.setHeader("Content-Disposition", "attachment;filename=\"" + FileUtils.toValidHttpFilename(fileName) + "\"");
		res.setContentType("text/csv");

		try {
			Writer writer = res.getWriter();

			writer.write(col1);
			writer.write(";");
			writer.write(col2);
			writer.write(";\n");

			if (values != null) {

				for (NumberEntry entry : values) {

					writer.write(formatWeek(entry.getId()));
					writer.write(";");
					writer.write(entry.getValue().toString());
					writer.write(";\n");
				}
			}

			writer.flush();

		} catch (IOException e) {

			log.info("Error sending " + fileName + " to user " + user);
		}
	}

	private void sendCSV(HttpServletResponse res, User user, List<Step> steps, List<IntegerEntry> values, String col1, String col2, String fileName) {

		res.setHeader("Content-Disposition", "attachment;filename=\"" + FileUtils.toValidHttpFilename(fileName) + "\"");
		res.setContentType("text/csv");

		try {
			Writer writer = res.getWriter();

			writer.write(col1);
			writer.write(";");
			writer.write(col2);
			writer.write(";\n");

			if (steps != null) {

				for (Step step : steps) {

					writer.write(step.getName());
					writer.write(";");
					writer.write(getMatchingEntryValue(step, values).toString());
					writer.write(";\n");
				}
			}

			writer.flush();

		} catch (IOException e) {

			log.info("Error sending " + fileName + " to user " + user);
		}
	}

	private Integer getMatchingEntryValue(Step step, List<IntegerEntry> entries) {

		if (entries != null) {

			for (IntegerEntry entry : entries) {

				if (entry.getId().equals(step.getStepID())) {

					return entry.getValue();
				}
			}
		}

		return 0;
	}

	private List<IntegerEntry> getUnsubmittedInstancesPerStep(Flow flow, Timestamp startDate, Timestamp endDate) throws SQLException {

		ArrayListQuery<IntegerEntry> query = new ArrayListQuery<IntegerEntry>(dataSource, STEP_UNSUBMITTED_COUNT_QUERY, INTEGER_ENTRY_POPULATOR);

		query.setInt(1, flow.getFlowID());
		query.setString(2, ContentType.NEW.toString());
		query.setTimestamp(3, startDate);
		query.setTimestamp(4, endDate);
		query.setTimestamp(5, startDate);
		query.setTimestamp(6, endDate);

		if (logSQL) {

			log.info(query);
		}

		return query.executeQuery();
	}

	private List<IntegerEntry> getAbortedInstancesPerStep(Flow flow, Timestamp startDate, Timestamp endDate) throws SQLException {

		ArrayListQuery<IntegerEntry> query = new ArrayListQuery<IntegerEntry>(dataSource, STEP_ABORT_COUNT_QUERY, INTEGER_ENTRY_POPULATOR);

		query.setInt(1, flow.getFlowID());
		query.setTimestamp(2, startDate);
		query.setTimestamp(3, endDate);

		if (logSQL) {

			log.info(query);
		}

		return query.executeQuery();
	}

	private List<IntegerEntry> getGlobalFlowInstanceCount(Timestamp startDate, Timestamp endDate) throws SQLException {

		ArrayListQuery<IntegerEntry> query = new ArrayListQuery<IntegerEntry>(dataSource, GLOBAL_FLOW_INSTANCE_COUNT_QUERY, INTEGER_ENTRY_POPULATOR);

		query.setTimestamp(1, startDate);
		query.setTimestamp(2, endDate);

		if (logSQL) {

			log.info(query);
		}

		return query.executeQuery();
	}

	public Document createDocument(HttpServletRequest req, URIParser uriParser) {

		Document doc = XMLUtils.createDomDocument();
		Element documentElement = doc.createElement("Document");
		doc.appendChild(documentElement);
		documentElement.appendChild(RequestUtils.getRequestInfoAsXML(doc, req, uriParser));
		documentElement.appendChild(this.moduleDescriptor.toXML(doc));

		if (enableExportSupport) {

			XMLUtils.appendNewElement(doc, documentElement, "ExportSupportEnabled");
		}

		XMLUtils.appendNewElement(doc, documentElement, "ChartColor", chartColor);

		return doc;
	}

	@EventListener(channel = Flow.class)
	public void processFlowEvent(CRUDEvent<Flow> event, EventSource source) {

		this.changesDetected = true;
	}

	@EventListener(channel = FlowFamily.class)
	public void processFlowFamilyEvent(CRUDEvent<FlowFamily> event, EventSource source) {

		this.changesDetected = true;
	}

	@Override
	public void run() {

		if (changesDetected) {

			log.info("Changes detected, reloading statistics...");

			try {
				changesDetected = false;
				this.cacheStatistics();

			} catch (Exception e) {

				log.error("Error generating statistics", e);
			}
		}
	}

	protected class WeeklyStatisticsReloader implements Runnable {

		@Override
		public void run() {

			log.info("Changes detected, reloading statistics...");

			try {
				changesDetected = false;
				cacheStatistics();

			} catch (Exception e) {

				log.error("Error generating statistics", e);
			}
		}
	}

	@Override
	public void systemStarted() {

		initScheduler();
	}

	protected ForegroundModuleResponse showStatisticsLoading(HttpServletRequest req, URIParser uriParser) {
		
		Document doc = createDocument(req, uriParser);

		Element familyStatisticsElement = doc.createElement("StatisticsLoading");
		doc.getDocumentElement().appendChild(familyStatisticsElement);
		
		return new SimpleForegroundModuleResponse(doc, moduleDescriptor.getName(), getDefaultBreadcrumb());
	}
	
}
