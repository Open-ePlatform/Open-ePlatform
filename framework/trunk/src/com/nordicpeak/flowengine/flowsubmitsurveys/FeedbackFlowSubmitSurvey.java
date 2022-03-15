package com.nordicpeak.flowengine.flowsubmitsurveys;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import javax.xml.transform.TransformerException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import se.unlogic.emailutils.framework.EmailUtils;
import se.unlogic.emailutils.framework.SimpleEmail;
import se.unlogic.hierarchy.core.annotations.CheckboxSettingDescriptor;
import se.unlogic.hierarchy.core.annotations.EventListener;
import se.unlogic.hierarchy.core.annotations.HTMLEditorSettingDescriptor;
import se.unlogic.hierarchy.core.annotations.InstanceManagerDependency;
import se.unlogic.hierarchy.core.annotations.ModuleSetting;
import se.unlogic.hierarchy.core.annotations.TextFieldSettingDescriptor;
import se.unlogic.hierarchy.core.annotations.WebPublic;
import se.unlogic.hierarchy.core.annotations.XSLVariable;
import se.unlogic.hierarchy.core.beans.LinkTag;
import se.unlogic.hierarchy.core.beans.ScriptTag;
import se.unlogic.hierarchy.core.beans.SimpleForegroundModuleResponse;
import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.hierarchy.core.enums.CRUDAction;
import se.unlogic.hierarchy.core.enums.EventSource;
import se.unlogic.hierarchy.core.enums.SystemStatus;
import se.unlogic.hierarchy.core.events.CRUDEvent;
import se.unlogic.hierarchy.core.exceptions.AccessDeniedException;
import se.unlogic.hierarchy.core.exceptions.URINotFoundException;
import se.unlogic.hierarchy.core.interfaces.ForegroundModuleResponse;
import se.unlogic.hierarchy.core.interfaces.SectionInterface;
import se.unlogic.hierarchy.core.interfaces.ViewFragment;
import se.unlogic.hierarchy.core.interfaces.modules.descriptors.ForegroundModuleDescriptor;
import se.unlogic.hierarchy.core.utils.HierarchyAnnotatedDAOFactory;
import se.unlogic.hierarchy.core.utils.ModuleViewFragmentTransformer;
import se.unlogic.hierarchy.core.utils.ViewFragmentModule;
import se.unlogic.hierarchy.foregroundmodules.rest.AnnotatedRESTModule;
import se.unlogic.hierarchy.foregroundmodules.rest.RESTMethod;
import se.unlogic.hierarchy.foregroundmodules.rest.URIParam;
import se.unlogic.hierarchy.foregroundmodules.staticcontent.StaticContentModule;
import se.unlogic.standardutils.collections.CollectionUtils;
import se.unlogic.standardutils.dao.AnnotatedDAO;
import se.unlogic.standardutils.dao.AnnotatedDAOWrapper;
import se.unlogic.standardutils.dao.HighLevelQuery;
import se.unlogic.standardutils.dao.LowLevelQuery;
import se.unlogic.standardutils.dao.QueryOperators;
import se.unlogic.standardutils.dao.QueryParameterFactory;
import se.unlogic.standardutils.dao.TransactionHandler;
import se.unlogic.standardutils.db.tableversionhandler.TableVersionHandler;
import se.unlogic.standardutils.db.tableversionhandler.UpgradeResult;
import se.unlogic.standardutils.db.tableversionhandler.XMLDBScriptProvider;
import se.unlogic.standardutils.json.JsonArray;
import se.unlogic.standardutils.numbers.NumberUtils;
import se.unlogic.standardutils.populators.EnumPopulator;
import se.unlogic.standardutils.string.SingleTagSource;
import se.unlogic.standardutils.string.StringUtils;
import se.unlogic.standardutils.string.TagReplacer;
import se.unlogic.standardutils.time.TimeUtils;
import se.unlogic.standardutils.validation.NonNegativeStringIntegerValidator;
import se.unlogic.standardutils.validation.ValidationError;
import se.unlogic.standardutils.validation.ValidationException;
import se.unlogic.standardutils.xml.XMLUtils;
import se.unlogic.webutils.http.HTTPUtils;
import se.unlogic.webutils.http.RequestUtils;
import se.unlogic.webutils.http.URIParser;
import se.unlogic.webutils.populators.annotated.AnnotatedRequestPopulator;

import com.nordicpeak.flowengine.FlowAdminModule;
import com.nordicpeak.flowengine.beans.Flow;
import com.nordicpeak.flowengine.beans.FlowFamily;
import com.nordicpeak.flowengine.beans.FlowInstance;
import com.nordicpeak.flowengine.interfaces.FlowNotificationHandler;
import com.nordicpeak.flowengine.interfaces.FlowSubmitSurveyProvider;
import com.nordicpeak.flowengine.interfaces.ImmutableFlow;
import com.nordicpeak.flowengine.interfaces.ImmutableFlowInstance;

import it.sauronsoftware.cron4j.Scheduler;

public class FeedbackFlowSubmitSurvey extends AnnotatedRESTModule implements FlowSubmitSurveyProvider, ViewFragmentModule<ForegroundModuleDescriptor>, Runnable {

	private static final String FLOW_FAMILY_FEEDBACK_QUERY = "SELECT feedback_flow_submit_surveys.* FROM feedback_flow_submit_surveys INNER JOIN flowengine_flows ON feedback_flow_submit_surveys.flowID = flowengine_flows.flowID WHERE flowFamilyID = ? AND (added BETWEEN ? AND ?);";

	private static final AnnotatedRequestPopulator<FeedbackSurvey> FEEDBACK_SURVEY_POPULATOR = new AnnotatedRequestPopulator<>(FeedbackSurvey.class, new EnumPopulator<Answer>(Answer.class));
	private static final AnnotatedRequestPopulator<FeedbackSurveySettings> POPULATOR = new AnnotatedRequestPopulator<>(FeedbackSurveySettings.class);

	@XSLVariable(prefix = "java.")
	private String chartDataTitle;

	@XSLVariable(prefix = "java.")
	private String extensionViewTitle = "FeedbackFlowSubmitSurvey";

	@XSLVariable(prefix = "java.")
	private String flowEventDescription = "FeedbackFlowSubmitSurvey";

	@ModuleSetting
	@CheckboxSettingDescriptor(name = "Show comment field", description = "Controls if the comment field is shown or not")
	private boolean showCommentField = true;

	@ModuleSetting
	@TextFieldSettingDescriptor(name = "Priority", description = "The priority of this extension provider compared to other providers. A low value means a higher priority. Valid values are 0 - " + Integer.MAX_VALUE + ".", required = true, formatValidator = NonNegativeStringIntegerValidator.class)
	protected int extensionPriority = 0;

	@ModuleSetting
	@TextFieldSettingDescriptor(name = "FeedbackFlowSurvey email subject (global)", description = "The subject of emails sent when feedbackflowsurvey comments are sent", required = true)
	@XSLVariable(prefix = "java.")
	private String feedbackFlowSurveyGlobalEmailSubject;

	@ModuleSetting
	@HTMLEditorSettingDescriptor(name = "FeedbackFlowSurvey email message (global)", description = "The message of emails sent when feedbackflowsurvey comments are sent", required = true)
	@XSLVariable(prefix = "java.")
	private String feedbackFlowSurveyGlobalEmailMessage;

	@ModuleSetting
	@TextFieldSettingDescriptor(name = "FeedbackFlowSurvey check interval", description = "How often to check and send emails about new comments, crontab format", required = true)
	private String surveyScheduleInterval = "0 8 * * Mon";

	@InstanceManagerDependency(required = true)
	private FlowAdminModule flowAdminModule;

	@InstanceManagerDependency(required = true)
	private FlowNotificationHandler flowNotificationHandler;

	@InstanceManagerDependency(required = true)
	private StaticContentModule staticContentModule;

	private Scheduler scheduler;

	private AnnotatedDAO<FeedbackSurvey> feedbackSurveyDAO;
	private AnnotatedDAO<FeedbackSurveySettings> feedbackSurveySettingsDAO;
	protected AnnotatedDAOWrapper<FeedbackSurveySettings, Integer> feedbackSurveySettingsDAOWrapper;

	private ModuleViewFragmentTransformer<ForegroundModuleDescriptor> viewFragmentTransformer;

	private QueryParameterFactory<FeedbackSurvey, Integer> flowInstanceIDParameterFactory;
	private QueryParameterFactory<FeedbackSurvey, Timestamp> flowInstanceTimestampParameterFactory;
	private QueryParameterFactory<FeedbackSurvey, String> flowInstanceCommentParameterFactory;

	private QueryParameterFactory<FeedbackSurvey, Integer> flowIDParameterFactory;

	private QueryParameterFactory<FeedbackSurvey, Integer> feedbackSurveyIDParameterFactory;

	private QueryParameterFactory<FeedbackSurveySettings, Integer> feedbackSurveySettingsIDParameterFactory;
	private QueryParameterFactory<FeedbackSurveySettings, Timestamp> feedbackSurveySettingsDateParameterFactory;

	@Override
	protected void createDAOs(DataSource dataSource) throws Exception {

		UpgradeResult upgradeResult = TableVersionHandler.upgradeDBTables(dataSource, FeedbackFlowSubmitSurvey.class.getName(), new XMLDBScriptProvider(this.getClass().getResourceAsStream("DB script.xml")));

		if (upgradeResult.isUpgrade()) {

			log.info(upgradeResult.toString());
		}

		HierarchyAnnotatedDAOFactory daoFactory = new HierarchyAnnotatedDAOFactory(dataSource, systemInterface.getUserHandler(), systemInterface.getGroupHandler(), false, false, false);

		feedbackSurveyDAO = daoFactory.getDAO(FeedbackSurvey.class);
		feedbackSurveySettingsDAO = daoFactory.getDAO(FeedbackSurveySettings.class);
		feedbackSurveySettingsDAOWrapper = feedbackSurveySettingsDAO.getWrapper(Integer.class);

		feedbackSurveyIDParameterFactory = daoFactory.getDAO(FeedbackSurvey.class).getParamFactory("flowID", Integer.class);

		feedbackSurveySettingsDateParameterFactory = daoFactory.getDAO(FeedbackSurveySettings.class).getParamFactory("sendEmail", Timestamp.class);
		feedbackSurveySettingsIDParameterFactory = daoFactory.getDAO(FeedbackSurveySettings.class).getParamFactory("flowFamilyID", Integer.class);

		flowInstanceIDParameterFactory = feedbackSurveyDAO.getParamFactory("flowInstanceID", Integer.class);
		flowInstanceTimestampParameterFactory = feedbackSurveyDAO.getParamFactory("added", Timestamp.class);
		flowInstanceCommentParameterFactory = feedbackSurveyDAO.getParamFactory("comment", String.class);
		flowIDParameterFactory = feedbackSurveyDAO.getParamFactory("flowID", Integer.class);

	}

	@Override
	public void init(ForegroundModuleDescriptor moduleDescriptor, SectionInterface sectionInterface, DataSource dataSource) throws Exception {

		super.init(moduleDescriptor, sectionInterface, dataSource);

		if (!systemInterface.getInstanceHandler().addInstance(FlowSubmitSurveyProvider.class, this)) {

			log.warn("Unable to register module " + moduleDescriptor + " in instance handler, another module is already registered for class " + FlowSubmitSurveyProvider.class.getName());
		}

		this.viewFragmentTransformer = new ModuleViewFragmentTransformer<>(sectionInterface.getForegroundModuleXSLTCache(), this, systemInterface.getEncoding());
		initScheduler();
	}

	@Override
	public void update(ForegroundModuleDescriptor descriptor, DataSource dataSource) throws Exception {

		super.update(descriptor, dataSource);

		this.viewFragmentTransformer = new ModuleViewFragmentTransformer<>(sectionInterface.getForegroundModuleXSLTCache(), this, systemInterface.getEncoding());

		stopScheduler();
		initScheduler();
	}

	private synchronized void initScheduler() {

		if (scheduler != null) {

			log.warn("Invalid state, scheduler already running!");
			stopScheduler();
		}

		scheduler = new Scheduler(systemInterface.getApplicationName() + " - " + moduleDescriptor.toString());
		scheduler.setDaemon(true);
		scheduler.schedule(surveyScheduleInterval, this);

		scheduler.start();
	}

	private synchronized void stopScheduler() {

		try {
			if (scheduler != null) {

				scheduler.stop();
				scheduler = null;
			}

		} catch (IllegalStateException e) {
			log.error("Error stopping scheduler", e);
		}
	}

	@Override
	public void unload() throws Exception {

		stopScheduler();

		systemInterface.getInstanceHandler().removeInstance(FlowSubmitSurveyProvider.class, this);

		super.unload();
	}

	@Override
	public ForegroundModuleResponse defaultMethod(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Throwable {

		//This method could be improved by using a fragment piped through FlowEngine that checks if the user has the given FlowInstance in session after submitting it
		
		if (req.getMethod().equalsIgnoreCase("POST")) {

			Integer flowInstanceID = NumberUtils.toInt(req.getParameter("flowInstanceID"));

			FlowInstance flowInstance = null;

			if (flowInstanceID != null && (flowInstance = flowAdminModule.getFlowInstance(flowInstanceID)) != null && getFeedbackSurvey(flowInstanceID) == null) {

				if (flowInstance.getFlow().requiresAuthentication() && (flowInstance.getOwners() == null || !flowInstance.getOwners().contains(user))) {

					throw new AccessDeniedException(this.sectionInterface.getSectionDescriptor());
				}

				Document doc = XMLUtils.createDomDocument();
				Element document = doc.createElement("Document");
				doc.appendChild(document);

				TransactionHandler transactionHandler = null;

				try {

					log.info("User " + user + " adding feedback for flowinstance " + flowInstance + " and flow " + flowInstance.getFlow());

					FeedbackSurvey feedbackSurvey = FEEDBACK_SURVEY_POPULATOR.populate(req);

					feedbackSurvey.setFlowID(flowInstance.getFlow().getFlowID());
					feedbackSurvey.setFlowInstanceID(flowInstance.getFlowInstanceID());
					feedbackSurvey.setAdded(TimeUtils.getCurrentTimestamp());

					transactionHandler = feedbackSurveyDAO.createTransaction();

					if (!feedbackSurveyDAO.beanExists(feedbackSurvey, transactionHandler)) {

						feedbackSurveyDAO.add(feedbackSurvey, transactionHandler, null);

						transactionHandler.commit();
					}

					XMLUtils.appendNewElement(doc, document, "FeedbackSurveySuccess");

				} catch (ValidationException validationException) {

					XMLUtils.append(doc, document, validationException.getErrors());

				} finally {

					TransactionHandler.autoClose(transactionHandler);
				}

				SimpleForegroundModuleResponse moduleResponse = new SimpleForegroundModuleResponse(doc);
				moduleResponse.excludeSystemTransformation(true);

				return moduleResponse;
			}

		}

		throw new URINotFoundException(uriParser);
	}

	@Override
	public ViewFragment getSurveyFormFragment(HttpServletRequest req, User user, ImmutableFlowInstance flowInstance) throws TransformerException, SQLException {

		FeedbackSurvey feedbackSurvey = getFeedbackSurvey(flowInstance.getFlowInstanceID());

		if (feedbackSurvey == null) {

			Document doc = XMLUtils.createDomDocument();
			Element document = doc.createElement("Document");

			doc.appendChild(document);

			Element formElement = doc.createElement("FeedbackSurveyForm");
			document.appendChild(formElement);

			XMLUtils.appendNewElement(doc, formElement, "ShowCommentField", this.showCommentField);

			ImmutableFlow flow = flowInstance.getFlow();

			XMLUtils.appendNewElement(doc, formElement, "flowName", flow.getName());
			XMLUtils.appendNewElement(doc, formElement, "flowInstanceID", flowInstance.getFlowInstanceID());

			XMLUtils.appendNewElement(doc, formElement, "ModuleURI", this.getModuleURI(req));

			return viewFragmentTransformer.createViewFragment(doc);
		}

		return null;
	}

	@Override
	public ViewFragment getShowFlowSurveysFragment(HttpServletRequest req, Integer flowID) throws TransformerException, SQLException {

		Document doc = XMLUtils.createDomDocument();
		Element document = doc.createElement("Document");
		doc.appendChild(document);

		XMLUtils.appendNewElement(doc, document, "ModuleURI", req.getContextPath() + getFullAlias());
		XMLUtils.appendNewElement(doc, document, "StaticContentURL", staticContentModule.getModuleContentURL(moduleDescriptor));

		Element showElement = doc.createElement("ShowFlowFeedbackSurveys");
		document.appendChild(showElement);

		Flow flow = flowAdminModule.getCachedFlow(flowID);
		XMLUtils.appendNewElement(doc, showElement, "flowID", flowID);

		FeedbackSurveySettings settings = feedbackSurveySettingsDAOWrapper.get(flow.getFlowFamily().getFlowFamilyID());

		if (settings != null) {

			XMLUtils.append(doc, showElement, settings);
		}

		HighLevelQuery<FeedbackSurvey> query = new HighLevelQuery<>();

		query.addParameter(flowIDParameterFactory.getParameter(flowID));

		List<FeedbackSurvey> surveys = feedbackSurveyDAO.getAll(query);

		if (surveys != null) {

			int veryDissatisfiedCount = 0;
			int dissatisfiedCount = 0;
			int neitherCount = 0;
			int satisfiedCount = 0;
			int verySatisfiedCount = 0;

			for (FeedbackSurvey survey : surveys) {

				Answer answer = survey.getAnswer();

				if (answer == Answer.VERY_DISSATISFIED) {
					veryDissatisfiedCount++;
				} else if (answer == Answer.DISSATISFIED) {
					dissatisfiedCount++;
				} else if (answer == Answer.NEITHER) {
					neitherCount++;
				} else if (answer == Answer.SATISFIED) {
					satisfiedCount++;
				} else if (answer == Answer.VERY_SATISFIED) {
					verySatisfiedCount++;
				}

			}

			JsonArray jsonArray = new JsonArray(6);
			jsonArray.addNode(chartDataTitle);
			jsonArray.addNode(veryDissatisfiedCount + "");
			jsonArray.addNode(dissatisfiedCount + "");
			jsonArray.addNode(neitherCount + "");
			jsonArray.addNode(satisfiedCount + "");
			jsonArray.addNode(verySatisfiedCount + "");

			XMLUtils.appendNewElement(doc, showElement, "ChartData", jsonArray.toJson());
			XMLUtils.append(doc, showElement, "Comments", surveys);

		}

		return viewFragmentTransformer.createViewFragment(doc);
	}

	@WebPublic(toLowerCase = true)
	public ForegroundModuleResponse updateSettings(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Exception {

		Flow flow = flowAdminModule.getRequestedFlow(req, user, uriParser);

		if (flow == null) {

			return flowAdminModule.list(req, res, user, uriParser, new ValidationError("FlowNotFound"));

		} else if (!flowAdminModule.hasFlowAccess(user, flow)) {

			throw new AccessDeniedException("User does not have access to flow type " + flow.getFlowType());
		}

		FeedbackSurveySettings settings = feedbackSurveySettingsDAOWrapper.get(flow.getFlowFamily().getFlowFamilyID());

		List<ValidationError> validationErrors = null;

		if (req.getMethod().equalsIgnoreCase("POST")) {

			validationErrors = new ArrayList<>();

			try {
				settings = POPULATOR.populate(settings, req);

				if (req.getParameter("sendEmail") == null) {
					
					settings.setSendEmail(null);
					
				} else {
					
					if (settings.isSendEmail() == null) {
						
						settings.setSendEmail(new Timestamp(System.currentTimeMillis()));
					}
				}
				
				if (CollectionUtils.isEmpty(validationErrors)) {

					if(settings.getNotificationEmailAddresses() == null) {
						
						if(settings.getFlowFamilyID() != null) {
							
							log.info("User " + user + " deleting submit survey settings for family " + flow.getFlowFamily());
							
							feedbackSurveySettingsDAOWrapper.delete(settings);
							
						} else {
							
							log.info("User " + user + " submitted empty survey settings for family " + flow.getFlowFamily());
						}
					
					}else{

						if (settings.getFlowFamilyID() == null) {

							log.info("User " + user + " adding submit survey settings for family " + flow.getFlowFamily());
							
							settings.setFlowFamilyID(flow.getFlowFamily().getFlowFamilyID());

							feedbackSurveySettingsDAOWrapper.add(settings);

						} else {

							log.info("User " + user + " updated submit survey settings for family " + flow.getFlowFamily());
							
							feedbackSurveySettingsDAOWrapper.update(settings);
						}
					}
					
					


					flowAdminModule.addFlowFamilyEvent(flowAdminModule.getEventFunctionConfigured() + " " + flowEventDescription, flow.getFlowFamily(), user);
					flowAdminModule.redirectToMethod(req, res, "/showflow/" + flow.getFlowID() + "#feedbackflowsubmitsettings");
					return null;
				}

			} catch (ValidationException e) {

				validationErrors.addAll(e.getErrors());
			}
		}

		log.info("User " + user + " requesting update submit survey settings form for flow family " + flow.getFlowFamily());

		Document doc = createDocument(req, uriParser);

		Element settingsElement = doc.createElement("UpdateSettings");
		doc.getDocumentElement().appendChild(settingsElement);

		settingsElement.appendChild(flow.toXML(doc));

		XMLUtils.append(doc, settingsElement, settings);

		if (validationErrors != null) {

			XMLUtils.append(doc, settingsElement, "ValidationErrors", validationErrors);
			settingsElement.appendChild(RequestUtils.getRequestParameters(req, doc));
		}

		return new SimpleForegroundModuleResponse(doc, this.getDefaultBreadcrumb());
	}

	@WebPublic(toLowerCase = true)
	public ForegroundModuleResponse deleteSettings(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Exception {

		if (!HTTPUtils.isPost(req)) {

			throw new AccessDeniedException("Delete requests using method " + req.getMethod() + " are not allowed.");
		}

		Flow flow = flowAdminModule.getRequestedFlow(req, user, uriParser);

		if (flow == null) {

			return flowAdminModule.list(req, res, user, uriParser, new ValidationError("FlowNotFound"));

		} else if (!flowAdminModule.hasFlowAccess(user, flow)) {

			throw new AccessDeniedException("User does not have access to flow type " + flow.getFlowType());
		}

		FeedbackSurveySettings settings = feedbackSurveySettingsDAOWrapper.get(flow.getFlowFamily().getFlowFamilyID());

		if (settings != null) {

			log.info("User " + user + " deleting survey settings for flow family " + flow.getFlowFamily().getFlowFamilyID());

			feedbackSurveySettingsDAOWrapper.delete(settings);

		} else {

			log.warn("User " + user + " trying to delete feedbackflowsurvey settings for flow family " + flow.getFlowFamily().getFlowFamilyID() + " which has no settings");
		}

		flowAdminModule.redirectToMethod(req, res, "/showflow/" + flow.getFlowID() + "#feedbackflowsurveysettings");

		return null;
	}

	@RESTMethod(alias = "deletecomment/{flowInstanceID}", method = { "post" }, requireLogin = true)
	public ForegroundModuleResponse deleteComment(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser, @URIParam(name = "flowInstanceID") Integer flowInstanceID) throws IOException, SQLException, URINotFoundException {

		FeedbackSurvey feedbackSurvey = getFeedbackSurvey(flowInstanceID);

		if (feedbackSurvey != null) {

			Flow flow = (Flow) flowAdminModule.getFlow(feedbackSurvey.getFlowID());
			if (flow == null || !flowAdminModule.hasFlowAccess(user, flow)) {
				throw new URINotFoundException(req.getRequestURI());
			}

			feedbackSurvey.setComment(null);
			feedbackSurvey.setCommentDeleted(TimeUtils.getCurrentTimestamp());
			feedbackSurvey.setCommentDeletedByUser(user);
			feedbackSurveyDAO.update(feedbackSurvey);

			log.info("User " + user.getUsername() + " deleted comment for flowinstance " + flowInstanceID);

			res.sendRedirect(req.getContextPath() + "/flowadmin/showflow/" + feedbackSurvey.getFlowID());
			return null;
		}
		throw new URINotFoundException(req.getRequestURI());

	}

	private FeedbackSurvey getFeedbackSurvey(Integer flowInstanceID) throws SQLException {

		HighLevelQuery<FeedbackSurvey> query = new HighLevelQuery<>();

		query.addParameter(flowInstanceIDParameterFactory.getParameter(flowInstanceID));

		return feedbackSurveyDAO.get(query);

	}

	@Override
	public ForegroundModuleDescriptor getModuleDescriptor() {

		return moduleDescriptor;
	}

	@Override
	public List<LinkTag> getLinkTags() {

		return links;
	}

	@Override
	public List<ScriptTag> getScriptTags() {

		return scripts;
	}

	@Override
	public Float getWeeklyAverage(Integer flowFamilyID, Timestamp startDate, Timestamp endDate) throws SQLException {

		//Replace direct join again flow engine tables with query against local tables only and get the flows from FlowAdminModule
		LowLevelQuery<FeedbackSurvey> query = new LowLevelQuery<>(FLOW_FAMILY_FEEDBACK_QUERY);

		query.addParameter(flowFamilyID);
		query.addParameter(startDate);
		query.addParameter(endDate);

		List<FeedbackSurvey> feedbackList = feedbackSurveyDAO.getAll(query);

		if (feedbackList == null) {

			return null;
		}

		int sum = 0;

		for (FeedbackSurvey feedback : feedbackList) {

			sum += feedback.getAnswer().ordinal() + 1;
		}

		return (float) sum / (float) feedbackList.size();
	}

	public Document createDocument(HttpServletRequest req, URIParser uriParser) {

		Document doc = XMLUtils.createDomDocument();
		Element documentElement = doc.createElement("Document");
		doc.appendChild(documentElement);
		documentElement.appendChild(RequestUtils.getRequestInfoAsXML(doc, req, uriParser));
		documentElement.appendChild(this.moduleDescriptor.toXML(doc));

		XMLUtils.appendNewElement(doc, documentElement, "ModuleURI", req.getContextPath() + getFullAlias());
		XMLUtils.appendNewElement(doc, documentElement, "StaticContentURL", staticContentModule.getModuleContentURL(moduleDescriptor));

		return doc;
	}

	@Override
	public Integer getFlowInstanceSurveyResult(int flowInstanceID, Connection connection) throws SQLException {

		HighLevelQuery<FeedbackSurvey> query = new HighLevelQuery<>();
		query.addParameter(flowInstanceIDParameterFactory.getParameter(flowInstanceID));

		FeedbackSurvey survey = feedbackSurveyDAO.get(query, connection);

		if (survey == null) {
			return null;
		}

		return 1 + survey.getAnswer().ordinal();
	}

	@Override
	public void run() {

		try {
			log.info("Checking for flow families with active survey comment notifications");

			HighLevelQuery<FeedbackSurveySettings> settingsListQuery = new HighLevelQuery<>();

			settingsListQuery.addParameter(feedbackSurveySettingsDateParameterFactory.getIsNotNullParameter());

			List<FeedbackSurveySettings> allSurveySettings = feedbackSurveySettingsDAO.getAll(settingsListQuery);

			if (CollectionUtils.isEmpty(allSurveySettings)) {

				log.info("No flow families with active survey comment notifications found");
				return;
			}

			for (FeedbackSurveySettings setting : allSurveySettings) {

				if (systemInterface.getSystemStatus() == SystemStatus.STARTED) {

					FlowFamily flowFamily = flowAdminModule.getFlowFamily(setting.getFlowFamilyID());
					
					List<Flow> flows = flowAdminModule.getFlowVersions(flowFamily);

					HighLevelQuery<FeedbackSurvey> surveyListQuery = new HighLevelQuery<>();

					surveyListQuery.addParameter(flowIDParameterFactory.getWhereInParameter(flows.stream().map(Flow::getFlowID).collect(Collectors.toList())));
					surveyListQuery.addParameter(flowInstanceTimestampParameterFactory.getParameter(setting.isSendEmail(), QueryOperators.BIGGER_THAN_OR_EQUALS));
					surveyListQuery.addParameter(flowInstanceCommentParameterFactory.getIsNotNullParameter());

					List<FeedbackSurvey> feedbackSurveys = feedbackSurveyDAO.getAll(surveyListQuery);

					if (feedbackSurveys == null) {
						continue;
					}

					Flow latestFlow = flowAdminModule.getLatestFlowVersion(flowFamily);
					
					if (latestFlow == null) {
						
						log.error("Could not find latest flow version for flow family " + flowFamily);
						continue;
					}
					
					LinkedHashSet<Integer> flowVersions = new LinkedHashSet<Integer>();
					
					for(Flow flow : flows) {

						Integer flowID = flow.getFlowID();
						
						for(FeedbackSurvey survey : feedbackSurveys) {
							
							if(survey.getFlowID().equals(flowID)) {
								
								flowVersions.add(flow.getFlowID());
							}
						}
					}


					TransactionHandler transactionHandler = feedbackSurveySettingsDAO.createTransaction();

					try {
						TagReplacer tagReplacer = new TagReplacer();

						tagReplacer.addTagSource(new SingleTagSource("$flow.name", latestFlow.getName()));
						tagReplacer.addTagSource(new SingleTagSource("$flow.url", flowNotificationHandler.getInstallationBaseURL(null) + flowAdminModule.getFullAlias() + "/showflow/" + latestFlow.getFlowID() + "#flowsurveys"));
						tagReplacer.addTagSource(new SingleTagSource("$flow.version", StringUtils.toCommaSeparatedString(flowVersions)));

						log.info("Sending new survey comment notifications for flow family " + setting.getFlowFamilyID());
						
						for(String recipient : setting.getNotificationEmailAddresses()) {
							
							SimpleEmail email = new SimpleEmail(systemInterface.getEncoding());

							email.addRecipient(recipient);

							email.setMessageContentType(SimpleEmail.HTML);
							email.setSenderName(flowNotificationHandler.getEmailSenderName(null));
							email.setSenderAddress(flowNotificationHandler.getEmailSenderAddress(null));
							email.setSubject(tagReplacer.replace(feedbackFlowSurveyGlobalEmailSubject));
							email.setMessage(EmailUtils.addMessageBody(tagReplacer.replace(feedbackFlowSurveyGlobalEmailMessage)));
							
							systemInterface.getEmailHandler().send(email);
						}
						
						setting.setSendEmail(new Timestamp(System.currentTimeMillis()));
						feedbackSurveySettingsDAO.update(setting, transactionHandler, null);

						transactionHandler.commit();

					} catch (SQLException e) {
						
						log.error("Could not update survey notification timestamp for flow family " + setting.getFlowFamilyID() + " after sending notifications.", e);
						
					} catch (Exception e) {
						
						log.error("Error sending new survey comment notifications for flow family " + setting.getFlowFamilyID(), e);
					
					} finally {
						
						TransactionHandler.autoClose(transactionHandler);
					}

				}
			}

		} catch (Exception e) {
			
			log.error("Error sending new survey comment notifications", e);
		}
	}

	@EventListener(channel = Flow.class)
	public void processFlowEvent(CRUDEvent<Flow> event, EventSource eventSource) {

		if (event.getAction() == CRUDAction.DELETE) {
			
			try (TransactionHandler transaction = feedbackSurveyDAO.createTransaction()) {
			
				for (Flow flow : event.getBeans()) {

					log.info("Deleting submit surveys for flowID: " + flow.getFlowID());

					try {
						HighLevelQuery<FeedbackSurvey> query = new HighLevelQuery<>();

						query.addParameter(feedbackSurveyIDParameterFactory.getParameter(flow.getFlowID()));

						feedbackSurveyDAO.delete(query, transaction);

					} catch (SQLException e) {

						log.error("Error deleting submit surveys for flowID: " + flow.getFlowID(), e);
					}
				}
				
				transaction.commit();
				
			} catch (Exception e) {
				
				log.error("Error deleting deleting submit surveys for event: " + event, e);
				
			}
		}
	}

	@EventListener(channel = FlowFamily.class)
	public void processFlowFamilyEvent(CRUDEvent<FlowFamily> event, EventSource eventSource) {

		if (event.getAction() == CRUDAction.DELETE) {

			try (TransactionHandler transaction = feedbackSurveySettingsDAO.createTransaction()) {
				
				for (FlowFamily flowFamily : event.getBeans()) {

					log.info("Deleting submit surveys settings for flow family: " + flowFamily.getFlowFamilyID());

					try {
						HighLevelQuery<FeedbackSurveySettings> query = new HighLevelQuery<>();

						query.addParameter(feedbackSurveySettingsIDParameterFactory.getParameter(flowFamily.getFlowFamilyID()));

						feedbackSurveySettingsDAO.delete(query, transaction);

					} catch (SQLException e) {

						log.error("Error deleting submit surveys settings for flow family: " + flowFamily.getFlowFamilyID(), e);
					}
				}
				transaction.commit();
				
			} catch (Exception e) {
				
				log.error("Error deleting ubmit surveys settings for event: " + event, e);
			}
		}
	}
}
