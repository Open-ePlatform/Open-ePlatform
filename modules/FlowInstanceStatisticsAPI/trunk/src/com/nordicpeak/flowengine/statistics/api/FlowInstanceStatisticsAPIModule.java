package com.nordicpeak.flowengine.statistics.api;

import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
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
import com.nordicpeak.flowengine.beans.FlowInstance;
import com.nordicpeak.flowengine.beans.Step;
import com.nordicpeak.flowengine.dao.FlowEngineDAOFactory;
import com.nordicpeak.flowengine.flowsubmitsurveys.FeedbackSurvey;
import com.nordicpeak.flowengine.interfaces.FlowSubmitSurveyProvider;
import com.nordicpeak.flowengine.statistics.beans.FlowInstanceStatistic;
import com.nordicpeak.flowengine.statistics.interfaces.StatisticsAPIExtensionProvider;

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

	private AnnotatedDAO<FlowInstance> flowInstanceDAO;
	private AnnotatedDAO<AbortedFlowInstance> abortedFlowInstanceDAO;
	private AnnotatedDAO<FeedbackSurvey> flowInstanceFeedbackSurveyDAO;

	protected QueryParameterFactory<FlowInstance, Timestamp> flowInstanceAddedParamFactory;
	protected QueryParameterFactory<AbortedFlowInstance, Timestamp> abortedFlowInstanceAddedParamFactory;
	protected QueryParameterFactory<FeedbackSurvey, Integer> feedbackSurveyFlowInstanceIDParamFactory;
	
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

		flowInstanceDAO = flowEngineDAOFactory.getFlowInstanceDAO();
		abortedFlowInstanceDAO = flowEngineDAOFactory.getAbortedFlowInstanceDAO();
		flowInstanceFeedbackSurveyDAO = daoFactory.getDAO(FeedbackSurvey.class);

		flowInstanceAddedParamFactory = flowInstanceDAO.getParamFactory("added", Timestamp.class);
		abortedFlowInstanceAddedParamFactory = abortedFlowInstanceDAO.getParamFactory("added", Timestamp.class);
		feedbackSurveyFlowInstanceIDParamFactory = flowInstanceFeedbackSurveyDAO.getParamFactory("flowInstanceID", Integer.class);
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

				log.info("User " + user + " requested list flowinstance statistics as " + StringUtils.toLogFormat(responseType, 30) + " from " + from + " to " + to);

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

				List<FlowInstanceStatistic> flowInstanceStatistics = new ArrayList<FlowInstanceStatistic>();

				int normalFlowInstances = 0;
				int abortedFlowInstances = 0;
				int extensionFlowInstances = 0;

				Connection connection = dataSource.getConnection();

				try {

					{ // Normal flow instances
						HighLevelQuery<FlowInstance> flowInstancesQuery = new HighLevelQuery<FlowInstance>();
						flowInstancesQuery.addRelations(FLOW_INSTANCE_RELATIONS);
						flowInstancesQuery.addCachedRelations(FLOW_INSTANCE_CACHED_RELATIONS);
						flowInstancesQuery.setRowLimiter(new MySQLRowLimiter(rowLimit));

						if (fromTimestamp != null) {
							flowInstancesQuery.addParameter(flowInstanceAddedParamFactory.getParameter(fromTimestamp, QueryOperators.BIGGER_THAN_OR_EQUALS));
						}

						if (toTimestamp != null) {
							flowInstancesQuery.addParameter(flowInstanceAddedParamFactory.getParameter(toTimestamp, QueryOperators.SMALLER_THAN_OR_EQUALS));
						}

						QueryResultsStreamer<FlowInstance, Integer> resultsStreamer = new QueryResultsStreamer<FlowInstance, Integer>(flowInstanceDAO, FlowInstance.ID_FIELD, Integer.class, Order.ASC, flowInstancesQuery, connection);

						SwedishSocialSecurityPopulator citizenIDValidatorHyphen = new SwedishSocialSecurityPopulator(true, true, true, false);
						SwedishSocialSecurityPopulator citizenIDValidatorNoHyphen = new SwedishSocialSecurityPopulator(true, true, true, true);
						
						@SuppressWarnings("unchecked")
						CombinedPopulator<String> citizenIDValidator = new CombinedPopulator<String>(String.class, citizenIDValidatorHyphen, citizenIDValidatorNoHyphen);
						
						Calendar today = Calendar.getInstance();

						List<FlowInstance> flowInstances = resultsStreamer.getBeans();

						while (flowInstances != null) {

							normalFlowInstances += flowInstances.size();

							if (systemInterface.getSystemStatus() == SystemStatus.STOPPING) {
								log.info("List flowinstance statistics aborted...");
								return;
							}

							for (FlowInstance flowInstance : flowInstances) {

								FlowInstanceStatistic statistic = new FlowInstanceStatistic(flowInstance.getFlow().getFlowID(), flowInstance.getAdded());

								if (flowInstance.getFirstSubmitted() != null) {
									statistic.setSubmitted(flowInstance.getFirstSubmitted());
									
									if(this.flowSubmitSurveyProvider != null) {
										
										statistic.setSurveyAnswer(flowSubmitSurveyProvider.getFlowInstanceSurveyResult(flowInstance.getFlowInstanceID(), connection));
									}
								}

								String citizenIdentifier;

								if (flowInstance.getPoster() != null && flowInstance.getPoster().getAttributeHandler() != null && (citizenIdentifier = flowInstance.getPoster().getAttributeHandler().getString(Constants.USER_CITIZEN_IDENTIFIER_ATTRIBUTE)) != null && citizenIDValidator.validateFormat(citizenIdentifier)) {

									if (citizenIdentifier.length() < 12) {

										citizenIdentifier = SwedishSocialSecurityPopulator.addCentury(citizenIdentifier);
									}

									Calendar calendar = Calendar.getInstance();
									calendar.set(Calendar.YEAR, Integer.valueOf(citizenIdentifier.substring(0, 4)));
									calendar.set(Calendar.MONTH, Integer.valueOf(citizenIdentifier.substring(4, 6)) - 1);
									calendar.set(Calendar.DAY_OF_MONTH, Integer.valueOf(citizenIdentifier.substring(6, 8)));

									statistic.setAge(today.get(Calendar.YEAR) - calendar.get(Calendar.YEAR));

									String genderPart = citizenIdentifier.substring(citizenIdentifier.length() - 2, citizenIdentifier.length() - 1);
									Integer genderPartInt = NumberUtils.toInt(genderPart);

									if (genderPartInt != null) {

										if (genderPartInt % 2 == 0) {
											statistic.setSex(sexFemale);

										} else {
											statistic.setSex(sexMale);
										}
									}
								}

								if (flowInstance.getFirstSubmitted() == null) {

									int stepIndex = 1;

									for (Step step : flowInstance.getFlow().getSteps()) {

										if (step.getStepID().equals(flowInstance.getStepID())) {
											statistic.setSavedStep(stepIndex);
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

					{ // Aborted flow instances
						HighLevelQuery<AbortedFlowInstance> flowInstancesQuery = new HighLevelQuery<AbortedFlowInstance>();
						flowInstancesQuery.setRowLimiter(new MySQLRowLimiter(rowLimit));

						if (fromTimestamp != null) {
							flowInstancesQuery.addParameter(abortedFlowInstanceAddedParamFactory.getParameter(fromTimestamp, QueryOperators.BIGGER_THAN_OR_EQUALS));
						}

						if (toTimestamp != null) {
							flowInstancesQuery.addParameter(abortedFlowInstanceAddedParamFactory.getParameter(toTimestamp, QueryOperators.SMALLER_THAN_OR_EQUALS));
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
					
					List<FlowInstanceStatistic> extensionStatistics = extension.getFlowInstanceAPIStatistics(fromTimestamp, toTimestamp, rowLimit);
					
					if (extensionStatistics != null) {
						
						extensionFlowInstances += extensionStatistics.size();
						flowInstanceStatistics.addAll(extensionStatistics);
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
							
							if(flow.getFlowFamily().getStatisticsMode() != null) {
								XMLUtils.appendNewElement(doc, flowElement, "StatisticsMode", flow.getFlowFamily().getStatisticsMode());
							}
							
							responseElement.appendChild(flowElement);
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
		query.setInt(2, flow.getFlowID());

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
