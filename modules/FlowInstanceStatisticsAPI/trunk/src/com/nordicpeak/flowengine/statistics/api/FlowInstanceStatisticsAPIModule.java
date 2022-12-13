package com.nordicpeak.flowengine.statistics.api;

import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import javax.xml.transform.TransformerException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import se.unlogic.hierarchy.core.annotations.InstanceManagerDependency;
import se.unlogic.hierarchy.core.annotations.ModuleSetting;
import se.unlogic.hierarchy.core.annotations.TextFieldSettingDescriptor;
import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.hierarchy.core.enums.SystemStatus;
import se.unlogic.hierarchy.core.exceptions.URINotFoundException;
import se.unlogic.hierarchy.core.interfaces.SectionInterface;
import se.unlogic.hierarchy.core.interfaces.modules.descriptors.ForegroundModuleDescriptor;
import se.unlogic.hierarchy.core.utils.HierarchyAnnotatedDAOFactory;
import se.unlogic.hierarchy.foregroundmodules.rest.AnnotatedRESTModule;
import se.unlogic.hierarchy.foregroundmodules.rest.RESTMethod;
import se.unlogic.hierarchy.foregroundmodules.rest.URIParam;
import se.unlogic.standardutils.collections.CollectionUtils;
import se.unlogic.standardutils.dao.AnnotatedDAO;
import se.unlogic.standardutils.dao.HighLevelQuery;
import se.unlogic.standardutils.dao.MySQLRowLimiter;
import se.unlogic.standardutils.dao.QueryOperators;
import se.unlogic.standardutils.dao.QueryParameterFactory;
import se.unlogic.standardutils.dao.QueryResultsStreamer;
import se.unlogic.standardutils.dao.querys.ObjectQuery;
import se.unlogic.standardutils.date.DateUtils;
import se.unlogic.standardutils.db.DBUtils;
import se.unlogic.standardutils.enums.Order;
import se.unlogic.standardutils.numbers.NumberUtils;
import se.unlogic.standardutils.populators.CombinedPopulator;
import se.unlogic.standardutils.populators.DatePopulator;
import se.unlogic.standardutils.populators.PositiveStringIntegerPopulator;
import se.unlogic.standardutils.populators.SwedishSocialSecurity12DigitsPopulator;
import se.unlogic.standardutils.populators.SwedishSocialSecurityPopulator;
import se.unlogic.standardutils.populators.TimeStampPopulator;
import se.unlogic.standardutils.string.StringUtils;
import se.unlogic.standardutils.validation.ValidationError;
import se.unlogic.standardutils.validation.ValidationErrorType;
import se.unlogic.standardutils.xml.XMLUtils;
import se.unlogic.webutils.http.URIParser;
import se.unlogic.webutils.validation.ValidationUtils;

import com.nordicpeak.flowengine.Constants;
import com.nordicpeak.flowengine.FlowAdminModule;
import com.nordicpeak.flowengine.beans.AbortedFlowInstance;
import com.nordicpeak.flowengine.beans.Flow;
import com.nordicpeak.flowengine.beans.FlowFamily;
import com.nordicpeak.flowengine.beans.FlowInstance;
import com.nordicpeak.flowengine.beans.Step;
import com.nordicpeak.flowengine.dao.FlowEngineDAOFactory;
import com.nordicpeak.flowengine.interfaces.FlowSubmitSurveyProvider;
import com.nordicpeak.flowengine.statistics.beans.FlowInstanceStatistic;
import com.nordicpeak.flowengine.statistics.interfaces.StatisticsAPIExtensionProvider;
import com.nordicpeak.flowengine.statistics.interfaces.StatisticsExtensionConsumer;

public class FlowInstanceStatisticsAPIModule extends AnnotatedRESTModule implements StatisticsExtensionConsumer {

	private static final String FLOW_CHANGED_DATE_SQL = "SELECT MAX(added) FROM flowengine_flow_family_events WHERE flowFamilyID = ? AND (flowVersion IS NULL OR flowVersion = ?)";

	protected static final Field[] FLOW_INSTANCE_RELATIONS = { FlowInstance.FLOW_RELATION, Flow.STEPS_RELATION };
	protected static final Field[] FLOW_INSTANCE_CACHED_RELATIONS = { FlowInstance.FLOW_RELATION };

	//	@XSLVariable(prefix = "java.")
	private String sexMale = "Male";

	//	@XSLVariable(prefix = "java.")
	private String sexFemale = "Female";

	@ModuleSetting
	@TextFieldSettingDescriptor(name = "DB row limit", description = "Number of rows to get each time.")
	private int rowLimit = 500;

	@InstanceManagerDependency(required = true)
	private FlowAdminModule flowAdminModule;

	@InstanceManagerDependency(required = false)
	private FlowSubmitSurveyProvider flowSubmitSurveyProvider;

	private AnnotatedDAO<FlowInstanceMinimizedForStatistics> flowInstanceMinimizedDAO;
	private AnnotatedDAO<AbortedFlowInstance> abortedFlowInstanceDAO;

	protected QueryParameterFactory<FlowInstance, Timestamp> flowInstanceAddedParamFactory;
	protected QueryParameterFactory<FlowInstanceMinimizedForStatistics, Timestamp> flowInstanceMinimizedAddedParamFactory;
	protected QueryParameterFactory<FlowInstanceMinimizedForStatistics, Integer> flowInstanceMinimizedFlowIDParamFactory;
	protected QueryParameterFactory<AbortedFlowInstance, Timestamp> abortedFlowInstanceAddedParamFactory;
	protected QueryParameterFactory<AbortedFlowInstance, Integer> abortedFlowInstanceFlowIDParamFactory;
	protected QueryParameterFactory<AbortedFlowInstance, Integer> abortedFlowInstanceFlowFamilyIDParamFactory;

	protected CopyOnWriteArrayList<StatisticsAPIExtensionProvider> extensions = new CopyOnWriteArrayList<>();

	@Override
	public void init(ForegroundModuleDescriptor moduleDescriptor, SectionInterface sectionInterface, DataSource dataSource) throws Exception {

		super.init(moduleDescriptor, sectionInterface, dataSource);

		if (!systemInterface.getInstanceHandler().addInstance(StatisticsExtensionConsumer.class, this)) {

			throw new RuntimeException("Unable to register module in global instance handler using key " + StatisticsExtensionConsumer.class.getSimpleName() + ", another instance is already registered using this key.");
		}
	}

	@Override
	protected void createDAOs(DataSource dataSource) throws Exception {

		FlowEngineDAOFactory flowEngineDAOFactory = new FlowEngineDAOFactory(dataSource, systemInterface.getUserHandler(), systemInterface.getGroupHandler());
		HierarchyAnnotatedDAOFactory daoFactory = new HierarchyAnnotatedDAOFactory(dataSource, systemInterface);

		flowInstanceMinimizedDAO = daoFactory.getDAO(FlowInstanceMinimizedForStatistics.class);
		abortedFlowInstanceDAO = flowEngineDAOFactory.getAbortedFlowInstanceDAO();

		flowInstanceMinimizedAddedParamFactory = flowInstanceMinimizedDAO.getParamFactory("added", Timestamp.class);
		flowInstanceMinimizedFlowIDParamFactory = flowInstanceMinimizedDAO.getParamFactory("flowID", Integer.class);
		abortedFlowInstanceAddedParamFactory = abortedFlowInstanceDAO.getParamFactory("added", Timestamp.class);
		abortedFlowInstanceFlowIDParamFactory = abortedFlowInstanceDAO.getParamFactory("flowID", Integer.class);
		abortedFlowInstanceFlowFamilyIDParamFactory = abortedFlowInstanceDAO.getParamFactory("flowFamilyID", Integer.class);
	}

	@Override
	public void unload() throws Exception {

		systemInterface.getInstanceHandler().removeInstance(StatisticsExtensionConsumer.class, this);

		super.unload();
	}

	@RESTMethod(alias = "getflowinstances/{responseType}", method = "get")
	public void getFlowInstances(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser, @URIParam(name = "responseType") String responseType) throws Throwable {

		List<ValidationError> validationErrors = new ArrayList<ValidationError>();

		Date from = ValidationUtils.validateParameter("from", req, false, DatePopulator.getPopulator(), validationErrors);
		Date to = ValidationUtils.validateParameter("to", req, false, DatePopulator.getPopulator(), validationErrors);
		Integer filterFlowID = ValidationUtils.validateParameter("flowid", req, false, PositiveStringIntegerPopulator.getPopulator(), validationErrors);
		Integer filterFlowFamilyID = ValidationUtils.validateParameter("flowfamilyid", req, false, PositiveStringIntegerPopulator.getPopulator(), validationErrors);

		FlowFamily filterFlowFamily = null;

		if (filterFlowFamilyID != null) {

			filterFlowFamily = flowAdminModule.getFlowFamily(filterFlowFamilyID);
		}

		Charset charset = null;
		String encoding = req.getParameter("encoding");

		if (encoding != null) {
			try {
				charset = Charset.forName(encoding);

			} catch (Exception e) {
				validationErrors.add(new ValidationError("encoding", ValidationErrorType.InvalidFormat));
			}
		}

		if (responseType.equals("xml")) {

			res.setContentType("text/xml");

			Document doc = XMLUtils.createDomDocument();
			Element responseElement = doc.createElement("FlowInstances");
			doc.appendChild(responseElement);

			if (validationErrors.isEmpty()) {

				log.info("User " + user + " requested list flowinstance statistics as " + StringUtils.toLogFormat(responseType, 30) + ", from " + from + " to " + to + ", flowID " + filterFlowID + ", flowFamilyID " + filterFlowFamilyID);

				Timestamp fromTimestamp = null;
				Timestamp toTimestamp = null;

				if (from != null) {

					fromTimestamp = new Timestamp(from.getTime());
					DateUtils.setTimeToMidnight(fromTimestamp);
				}

				if (to != null) {

					toTimestamp = new Timestamp(to.getTime());
					DateUtils.setTimeToMaximum(toTimestamp);
				}

				List<FlowInstanceStatistic> flowInstanceStatistics = new ArrayList<FlowInstanceStatistic>(1024);

				int normalFlowInstances = 0;
				int abortedFlowInstances = 0;
				int extensionFlowInstances = 0;

				Set<Integer> abortedFlowInstanceIDsSet = new HashSet<>();

				Connection connection = dataSource.getConnection();

				try {

					// Normal flow instances
					if (filterFlowFamilyID == null || filterFlowFamily != null) {

						HighLevelQuery<FlowInstanceMinimizedForStatistics> flowInstancesQuery = new HighLevelQuery<FlowInstanceMinimizedForStatistics>();
						flowInstancesQuery.setRowLimiter(new MySQLRowLimiter(rowLimit));

						if (fromTimestamp != null) {
							flowInstancesQuery.addParameter(flowInstanceMinimizedAddedParamFactory.getParameter(fromTimestamp, QueryOperators.BIGGER_THAN_OR_EQUALS));
						}

						if (toTimestamp != null) {
							flowInstancesQuery.addParameter(flowInstanceMinimizedAddedParamFactory.getParameter(toTimestamp, QueryOperators.SMALLER_THAN_OR_EQUALS));
						}

						if (filterFlowID != null) {
							flowInstancesQuery.addParameter(flowInstanceMinimizedFlowIDParamFactory.getParameter(filterFlowID));

						} else if (filterFlowFamily != null) {

							List<Integer> flowIDs = new ArrayList<>();

							for (Flow flow : flowAdminModule.getFlowVersions(filterFlowFamily)) {
								flowIDs.add(flow.getFlowID());
							}

							flowInstancesQuery.addParameter(flowInstanceMinimizedFlowIDParamFactory.getWhereInParameter(flowIDs));
						}

						QueryResultsStreamer<FlowInstanceMinimizedForStatistics, Integer> resultsStreamer = new QueryResultsStreamer<FlowInstanceMinimizedForStatistics, Integer>(flowInstanceMinimizedDAO, FlowInstanceMinimizedForStatistics.ID_FIELD, Integer.class, Order.ASC, flowInstancesQuery, connection);

						SwedishSocialSecurityPopulator citizenIDValidatorHyphen = new SwedishSocialSecurityPopulator(true, true, true, false);
						SwedishSocialSecurityPopulator citizenIDValidatorNoHyphen = new SwedishSocialSecurityPopulator(true, true, true, true);

						@SuppressWarnings("unchecked")
						CombinedPopulator<String> citizenIDValidator = new CombinedPopulator<String>(String.class, citizenIDValidatorHyphen, citizenIDValidatorNoHyphen);

						List<FlowInstanceMinimizedForStatistics> flowInstances = resultsStreamer.getBeans();

						HashMap<Integer, User> usersMap = new HashMap<>(1024);

						while (flowInstances != null) {

							normalFlowInstances += flowInstances.size();

							if (systemInterface.getSystemStatus() == SystemStatus.STOPPING) {
								log.info("List flowinstance statistics aborted...");
								return;
							}

							List<Integer> missingPosterIDs = new ArrayList<>(flowInstances.size());

							for (FlowInstanceMinimizedForStatistics flowInstance : flowInstances) {

								if (!usersMap.containsKey(flowInstance.getPosterID())) {
									missingPosterIDs.add(flowInstance.getPosterID());
								}
							}

							if (!missingPosterIDs.isEmpty()) {

								List<User> posters = systemInterface.getUserHandler().getUsers(missingPosterIDs, false, true);

								if (posters != null) {

									for (User poster : posters) {
										usersMap.put(poster.getUserID(), poster);
									}
								}
							}

							for (FlowInstanceMinimizedForStatistics flowInstance : flowInstances) {

								FlowInstanceStatistic statistic = new FlowInstanceStatistic(flowInstance.getFlowID(), flowInstance.getAdded());

								if (flowInstance.getFirstSubmitted() != null) {
									statistic.setSubmitted(flowInstance.getFirstSubmitted());

									if (flowSubmitSurveyProvider != null) {

										statistic.setSurveyAnswer(flowSubmitSurveyProvider.getFlowInstanceSurveyResult(flowInstance.getFlowInstanceID(), connection));
									}
								}

								String citizenIdentifier;

								User poster = usersMap.get(flowInstance.getPosterID());

								if (poster != null && poster.getAttributeHandler() != null && (citizenIdentifier = poster.getAttributeHandler().getString(Constants.USER_CITIZEN_IDENTIFIER_ATTRIBUTE)) != null && citizenIDValidator.validateFormat(citizenIdentifier)) {

									if (citizenIdentifier.length() < 12) {

										citizenIdentifier = SwedishSocialSecurityPopulator.addCentury(citizenIdentifier);
									}

									if (SwedishSocialSecurity12DigitsPopulator.getPopulator().validateFormat(citizenIdentifier)) {

										int year = Integer.valueOf(citizenIdentifier.substring(0, 4));
										int month = Integer.valueOf(citizenIdentifier.substring(4, 6));
										int day = Integer.valueOf(citizenIdentifier.substring(6, 8));

										LocalDate birthDate = LocalDate.of(year, month, day);
										LocalDate submitOrAddedDate;

										if (flowInstance.getFirstSubmitted() != null) {

											submitOrAddedDate = flowInstance.getFirstSubmitted().toLocalDateTime().toLocalDate();

										} else {

											submitOrAddedDate = flowInstance.getAdded().toLocalDateTime().toLocalDate();
										}

										Period period = Period.between(birthDate, submitOrAddedDate);

										statistic.setAge((int) period.get(ChronoUnit.YEARS));

										String genderPart = citizenIdentifier.substring(citizenIdentifier.length() - 2, citizenIdentifier.length() - 1);
										Integer genderPartInt = NumberUtils.toInt(genderPart);

										if (genderPartInt != null) {

											if (genderPartInt % 2 == 0) {
												statistic.setSex(sexFemale);

											} else {
												statistic.setSex(sexMale);
											}
										}

									} else {
										log.warn("Skipping age and gender statistic for user " + poster + " due to not having a valid citizen identifier " + citizenIdentifier);
									}
								}

								if (flowInstance.getFirstSubmitted() == null) {

									Flow flow = flowAdminModule.getCachedFlow(flowInstance.getFlowID());

									if (flow == null) {

										log.warn("Unable to find flow with ID " + flowInstance.getFlowID());

									} else {

										int stepIndex = 1;

										for (Step step : flow.getSteps()) {

											if (step.getStepID().equals(flowInstance.getStepID())) {
												statistic.setSavedStep(stepIndex);
												break;
											}

											stepIndex++;
										}
									}
								}

								flowInstanceStatistics.add(statistic);
							}

							if (flowInstances.size() == rowLimit) {

								flowInstances = resultsStreamer.getBeans();

							} else {
								break;
							}
						}
					}

					{ // Aborted flow instances
						HighLevelQuery<AbortedFlowInstance> flowInstancesQuery = new HighLevelQuery<AbortedFlowInstance>();
						flowInstancesQuery.setRowLimiter(new MySQLRowLimiter(rowLimit));

						if (fromTimestamp != null) {
							flowInstancesQuery.addParameter(abortedFlowInstanceAddedParamFactory.getParameter(fromTimestamp, QueryOperators.BIGGER_THAN_OR_EQUALS));
						}

						if (toTimestamp != null) {
							flowInstancesQuery.addParameter(abortedFlowInstanceAddedParamFactory.getParameter(toTimestamp, QueryOperators.SMALLER_THAN_OR_EQUALS));
						}

						if (filterFlowID != null) {
							flowInstancesQuery.addParameter(abortedFlowInstanceFlowIDParamFactory.getParameter(filterFlowID));

						} else if (filterFlowFamilyID != null) {
							flowInstancesQuery.addParameter(abortedFlowInstanceFlowFamilyIDParamFactory.getParameter(filterFlowFamilyID));
						}

						QueryResultsStreamer<AbortedFlowInstance, Integer> resultsStreamer = new QueryResultsStreamer<AbortedFlowInstance, Integer>(abortedFlowInstanceDAO, AbortedFlowInstance.ID_FIELD, Integer.class, Order.ASC, flowInstancesQuery, connection);

						List<AbortedFlowInstance> flowInstances = resultsStreamer.getBeans();

						while (flowInstances != null) {

							abortedFlowInstances += flowInstances.size();

							if (systemInterface.getSystemStatus() == SystemStatus.STOPPING) {
								log.info("List flowinstance statistics aborted...");
								return;
							}

							for (AbortedFlowInstance flowInstance : flowInstances) {

								if (flowInstance.getFlowInstanceID() != null) {
									abortedFlowInstanceIDsSet.add(flowInstance.getFlowInstanceID());
								}

								FlowInstanceStatistic statistic = new FlowInstanceStatistic(flowInstance.getFlowID(), flowInstance.getAdded());

								Flow flow = flowAdminModule.getCachedFlow(flowInstance.getFlowID());

								if (flow != null && flow.getSteps() != null) {

									int stepIndex = 1;

									for (Step step : flow.getSteps()) {

										if (step.getStepID().equals(flowInstance.getStepID())) {
											statistic.setAbortedStep(stepIndex);
											break;
										}

										stepIndex++;
									}
								}

								flowInstanceStatistics.add(statistic);
							}

							if (flowInstances.size() == rowLimit) {
								flowInstances = resultsStreamer.getBeans();

							} else {
								break;
							}
						}
					}

				} finally {
					DBUtils.closeConnection(connection);
				}

				for (StatisticsAPIExtensionProvider extension : extensions) {

					List<FlowInstanceStatistic> extensionStatistics = extension.getFlowInstanceAPIStatistics(fromTimestamp, toTimestamp, filterFlowID, filterFlowFamilyID, rowLimit);

					if (extensionStatistics != null) {

						for (FlowInstanceStatistic flowInstanceStatistic : extensionStatistics) {

							// Skip flow instances that exist in both aborted and deleted. Only happens when users delete saved but unsubmitted flow instances.
							if (flowInstanceStatistic.getSubmitted() == null && abortedFlowInstanceIDsSet.contains(flowInstanceStatistic.getFlowInstanceID())) {
								continue;
							}

							extensionFlowInstances += 1;
							flowInstanceStatistics.add(flowInstanceStatistic);
						}
					}
				}

				log.info("Responding to user " + user + " with flow instance statistics: normalFlowInstances " + normalFlowInstances + ", abortedFlowInstances " + abortedFlowInstances + ", deletedFlowInstances " + extensionFlowInstances);

				Collections.sort(flowInstanceStatistics);

				XMLUtils.appendNewElement(doc, responseElement, "Count", flowInstanceStatistics.size());

				XMLUtils.append(doc, responseElement, "FlowInstances", flowInstanceStatistics);

			} else {

				log.info("User " + user + " got following validation errors when trying to list flowinstance statistics: " + validationErrors);

				XMLUtils.append(doc, responseElement, "ValidationErrors", validationErrors);
			}

			try {

				if (charset != null) {

					res.setCharacterEncoding(charset.name());
					XMLUtils.writeXML(doc, res.getOutputStream(), true, charset.name());

				} else {

					XMLUtils.writeXML(doc, res.getOutputStream(), true, sectionInterface.getSystemInterface().getEncoding());
				}

			} catch (TransformerException e) {

				log.warn("Error sending flow instance statistics to " + user, e);

			} catch (IOException e) {

				log.warn("Error sending flow instance statistics to " + user, e);
			}

		} else {

			// Invalid requested response type
			throw new URINotFoundException(uriParser);
		}
	}

	@RESTMethod(alias = "getflows/{responseType}", method = "get")
	public void getFlows(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser, @URIParam(name = "responseType") String responseType) throws Throwable {

		List<ValidationError> validationErrors = new ArrayList<ValidationError>();

		Charset charset = null;
		String encoding = req.getParameter("encoding");

		if (encoding != null) {
			try {
				charset = Charset.forName(encoding);

			} catch (Exception e) {
				validationErrors.add(new ValidationError("encoding", ValidationErrorType.InvalidFormat));
			}
		}

		if (responseType.equals("xml")) {

			res.setContentType("text/xml");

			Document doc = XMLUtils.createDomDocument();
			Element responseElement = doc.createElement("Flows");
			doc.appendChild(responseElement);

			if (validationErrors.isEmpty()) {

				log.info("User " + user + " listing flows as " + StringUtils.toLogFormat(responseType, 30));

				Collection<Flow> flows = flowAdminModule.getCachedFlows();

				if (!CollectionUtils.isEmpty(flows)) {

					Connection connection = dataSource.getConnection();

					try {
						int skippedFlows = 0;

						for (Flow flow : flows) {

							Element flowElement = XMLUtils.appendNewElement(doc, responseElement, "Flow");

							String published = null;

							if (flow.getPublishDate() != null) {
								published = DateUtils.DATE_FORMATTER.format(flow.getPublishDate());
							}

							String changed = null;
							Date changedDate = getFlowLatestChanged(flow, connection);

							if (changedDate != null) {
								changed = DateUtils.DATE_FORMATTER.format(changedDate);
							}

							XMLUtils.appendNewElement(doc, flowElement, "ID", flow.getFlowID());
							XMLUtils.appendNewElement(doc, flowElement, "FlowFamilyID", flow.getFlowFamily().getFlowFamilyID());
							XMLUtils.appendNewElement(doc, flowElement, "Name", flow.getName());
							XMLUtils.appendNewElement(doc, flowElement, "Category", flow.getFlowType().getName());
							XMLUtils.appendNewElement(doc, flowElement, "CategoryID", flow.getFlowType().getFlowTypeID());
							XMLUtils.appendNewElement(doc, flowElement, "Published", published);
							XMLUtils.appendNewElement(doc, flowElement, "Changed", changed);
							XMLUtils.appendNewElement(doc, flowElement, "Internal", flow.isInternal());
							XMLUtils.appendNewElement(doc, flowElement, "Visible", !flow.isHideFromOverview());

							if (flow.getFlowFamily().getStatisticsMode() != null) {
								XMLUtils.appendNewElement(doc, flowElement, "StatisticsMode", flow.getFlowFamily().getStatisticsMode());
							}

							responseElement.appendChild(flowElement);
						}

						if (skippedFlows > 0) {
							log.info("Skipped " + skippedFlows + " flows from response due to missing flow API access for flow families");
						}

					} finally {
						DBUtils.closeConnection(connection);
					}
				}

			} else {

				log.info("User " + user + " got following validation errors when trying to list flows: " + validationErrors);

				XMLUtils.append(doc, responseElement, "ValidationErrors", validationErrors);
			}

			try {

				if (charset != null) {

					res.setCharacterEncoding(charset.name());
					XMLUtils.writeXML(doc, res.getOutputStream(), true, charset.name());

				} else {

					XMLUtils.writeXML(doc, res.getOutputStream(), true, sectionInterface.getSystemInterface().getEncoding());
				}

			} catch (TransformerException e) {

				log.warn("Error sending flow instance statistics to " + user, e);

			} catch (IOException e) {

				log.warn("Error sending flow instance statistics to " + user, e);
			}

		} else {

			// Invalid requested response type
			throw new URINotFoundException(uriParser);
		}
	}

	private Timestamp getFlowLatestChanged(Flow flow, Connection connection) throws SQLException {

		ObjectQuery<Timestamp> query = new ObjectQuery<Timestamp>(connection, false, FLOW_CHANGED_DATE_SQL, TimeStampPopulator.getPopulator());
		query.setInt(1, flow.getFlowFamily().getFlowFamilyID());
		query.setInt(2, flow.getVersion());

		return query.executeQuery();
	}

	@Override
	public boolean addStatisticsExtensionProvider(StatisticsAPIExtensionProvider statisticsExtensionProvider) {

		return extensions.add(statisticsExtensionProvider);
	}

	@Override
	public boolean removeStatisticsExtensionProvider(StatisticsAPIExtensionProvider statisticsExtensionProvider) {

		return extensions.remove(statisticsExtensionProvider);
	}
}
