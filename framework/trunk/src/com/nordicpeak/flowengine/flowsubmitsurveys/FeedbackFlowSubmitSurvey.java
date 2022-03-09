package com.nordicpeak.flowengine.flowsubmitsurveys;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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
import se.unlogic.hierarchy.core.exceptions.AccessDeniedException;
import se.unlogic.hierarchy.core.exceptions.URINotFoundException;
import se.unlogic.hierarchy.core.interfaces.ForegroundModuleResponse;
import se.unlogic.hierarchy.core.interfaces.SectionInterface;
import se.unlogic.hierarchy.core.interfaces.ViewFragment;
import se.unlogic.hierarchy.core.interfaces.modules.descriptors.ForegroundModuleDescriptor;
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
import se.unlogic.standardutils.dao.QueryParameterFactory;
import se.unlogic.standardutils.dao.SimpleAnnotatedDAOFactory;
import se.unlogic.standardutils.dao.TransactionHandler;
import se.unlogic.standardutils.db.tableversionhandler.TableVersionHandler;
import se.unlogic.standardutils.db.tableversionhandler.UpgradeResult;
import se.unlogic.standardutils.db.tableversionhandler.XMLDBScriptProvider;
import se.unlogic.standardutils.html.HTMLUtils;
import se.unlogic.standardutils.json.JsonArray;
import se.unlogic.standardutils.numbers.NumberUtils;
import se.unlogic.standardutils.populators.EnumPopulator;
import se.unlogic.standardutils.string.SingleTagSource;
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
import com.nordicpeak.flowengine.beans.FlowInstance;
import com.nordicpeak.flowengine.interfaces.FlowNotificationHandler;
import com.nordicpeak.flowengine.interfaces.FlowSubmitSurveyProvider;
import com.nordicpeak.flowengine.interfaces.ImmutableFlow;
import com.nordicpeak.flowengine.interfaces.ImmutableFlowInstance;

import it.sauronsoftware.cron4j.Scheduler;

public class FeedbackFlowSubmitSurvey extends AnnotatedRESTModule implements FlowSubmitSurveyProvider, ViewFragmentModule<ForegroundModuleDescriptor>, Runnable{

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
	private String surveyScheduleInterval = "0 23 * * Sun";

	@InstanceManagerDependency(required = true)
	private FlowAdminModule flowAdminModule;
	
	@InstanceManagerDependency(required = true)
	private FlowNotificationHandler flowNotificationHandler;

	@InstanceManagerDependency(required = true)
	private StaticContentModule staticContentModule;
	
	private Scheduler scheduler;
	private String releseOldSurveyScheduleID;
	
	private AnnotatedDAO<FeedbackSurvey> feedbackSurveyDAO;
	protected AnnotatedDAOWrapper<FeedbackSurveySettings, Integer> feedbackSurveySettingsDAOWrapper;

	private ModuleViewFragmentTransformer<ForegroundModuleDescriptor> viewFragmentTransformer;

	private QueryParameterFactory<FeedbackSurvey, Integer> flowInstanceIDParameterFactory;

	private QueryParameterFactory<FeedbackSurvey, Integer> flowIDParameterFactory;

	@Override
	protected void createDAOs(DataSource dataSource) throws Exception {

		UpgradeResult upgradeResult = TableVersionHandler.upgradeDBTables(dataSource, FeedbackFlowSubmitSurvey.class.getName(), new XMLDBScriptProvider(this.getClass().getResourceAsStream("DB script.xml")));

		if (upgradeResult.isUpgrade()) {

			log.info(upgradeResult.toString());
		}

		SimpleAnnotatedDAOFactory daoFactory = new SimpleAnnotatedDAOFactory(dataSource);

		feedbackSurveyDAO = daoFactory.getDAO(FeedbackSurvey.class);
		feedbackSurveySettingsDAOWrapper = daoFactory.getDAO(FeedbackSurveySettings.class).getWrapper(Integer.class);


		flowInstanceIDParameterFactory = feedbackSurveyDAO.getParamFactory("flowInstanceID", Integer.class);
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
	
		rescheduleScheduler();
	}
	
	private void rescheduleScheduler() {

		scheduler.reschedule(releseOldSurveyScheduleID, surveyScheduleInterval);		
	}
	
	private synchronized void initScheduler() {

		if (scheduler != null) {

			log.warn("Invalid state, scheduler already running!");
			stopScheduler();
		}

		scheduler = new Scheduler(systemInterface.getApplicationName() + " - " + moduleDescriptor.toString());
		scheduler.setDaemon(true);
		releseOldSurveyScheduleID = scheduler.schedule(surveyScheduleInterval, this);

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
					
					
					FeedbackSurveySettings settings = feedbackSurveySettingsDAOWrapper.get(flowInstance.getFlow().getFlowFamily().getFlowFamilyID());

					FeedbackSurvey feedbackSurvey = FEEDBACK_SURVEY_POPULATOR.populate(req);

					feedbackSurvey.setFlowID(flowInstance.getFlow().getFlowID());
					feedbackSurvey.setFlowInstanceID(flowInstance.getFlowInstanceID());
					feedbackSurvey.setAdded(TimeUtils.getCurrentTimestamp());
					feedbackSurvey.setCommentSent(true);
					
					if(settings != null && settings.isSendEmail()) {
						feedbackSurvey.setCommentSent(false);
					}

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
		
		if(settings != null) {
		
			XMLUtils.append(doc, showElement, settings);
		}

		HighLevelQuery<FeedbackSurvey> query = new HighLevelQuery<>();

		query.addParameter(flowIDParameterFactory.getParameter(flowID));

		List<FeedbackSurvey> surveys = feedbackSurveyDAO.getAll(query);

		if (surveys != null) {

			List<FeedbackSurvey> commentSurveys = new ArrayList<>();

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

				commentSurveys.add(survey);				

			}

			JsonArray jsonArray = new JsonArray(6);
			jsonArray.addNode(chartDataTitle);
			jsonArray.addNode(veryDissatisfiedCount + "");
			jsonArray.addNode(dissatisfiedCount + "");
			jsonArray.addNode(neitherCount + "");
			jsonArray.addNode(satisfiedCount + "");
			jsonArray.addNode(verySatisfiedCount + "");

			XMLUtils.appendNewElement(doc, showElement, "ChartData", jsonArray.toJson());
			XMLUtils.append(doc, showElement, "Comments", commentSurveys);

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
			
			if (settings == null) {
				
				settings = new FeedbackSurveySettings();
			}
			
			try {
				POPULATOR.populate(settings, req);
				
				if (CollectionUtils.isEmpty(validationErrors)) {
						
					log.info("User " + user + " updated feedbackflowsubmit settings for flow " + flow);
					
					if (settings.getFlowFamilyID() == null) {
						
						settings.setFlowFamilyID(flow.getFlowFamily().getFlowFamilyID());
						feedbackSurveySettingsDAOWrapper.add(settings);
						
					} else {
						
						feedbackSurveySettingsDAOWrapper.update(settings);
					}
					
					flowAdminModule.addFlowFamilyEvent(flowAdminModule.getEventFunctionConfigured() + " " + flowEventDescription, flow.getFlowFamily(), user);
					flowAdminModule.redirectToMethod(req, res, "/showflow/" + flow.getFlowID() + "#feedbackflowsubmitsettings");
					return null;
				}
				
			} catch (ValidationException e) {
				
				validationErrors.addAll(e.getErrors());
			}
		}
		
		log.info("User " + user + " requesting update deduplication settings form for flow " + flow);
		
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
		
		if(!HTTPUtils.isPost(req)) {
			
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
			
			log.info("User " + user + " deleting feedbackflowsurvey settings for flow " + flow);
			
			feedbackSurveySettingsDAOWrapper.delete(settings);
			
		} else {
			
			log.warn("User " + user + " trying to delete feedbackflowsurvey settings for flow " + flow + " which has no settings");
		}
		
		flowAdminModule.redirectToMethod(req, res, "/showflow/" + flow.getFlowID() + "#feedbackflowsurveysettings");
		
		return null;
	}

	@RESTMethod(alias = "deletecomment/{flowInstanceID}", method = { "post" }, requireLogin = true)
	public ForegroundModuleResponse deleteComment(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser, @URIParam(name = "flowInstanceID") Integer flowInstanceID) throws IOException, SQLException, URINotFoundException {

		FlowInstance flowInstance = flowAdminModule.getFlowInstance(flowInstanceID);
		
		if(flowInstance != null && flowAdminModule.hasFlowAccess(user, flowInstance.getFlow())) {
		
			FeedbackSurvey feedbackSurvey = getFeedbackSurvey(flowInstanceID);
			
			if(feedbackSurvey == null) {
				log.warn("Unknown feedbackSurvey with flowinstanceID "+flowInstanceID);
				throw new URINotFoundException(req.getRequestURI());
			}
			
			feedbackSurvey.setComment(null);
			feedbackSurvey.setCommentDeleted(Timestamp.valueOf(LocalDateTime.now()));
			feedbackSurvey.setCommentDeletedByUser(user.getUsername());
			feedbackSurveyDAO.update(feedbackSurvey);
			
			log.info("User "+user.getUsername()+" deleted comment for flowinstance "+flowInstanceID);
	
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
			log.info("Checking for feedbackflow comments");
			
			LowLevelQuery<FeedbackSurvey> commentQuery = new LowLevelQuery<>();
			
			List<FeedbackSurveySettings> settingsList = feedbackSurveySettingsDAOWrapper.getAll();
			
			if(CollectionUtils.isEmpty(settingsList) || settingsList.stream().noneMatch(FeedbackSurveySettings::isSendEmail)) {
				log.info("No feedbackflowsettings active");
				return;
			}
			
			Map<FeedbackSurveySettings, List<Flow>> settingFlows = getAffectedFlows(settingsList);
			
			if(settingFlows.isEmpty()) {
				log.info("No matching flows from feedbackflowsettings");
				return;
			}
			
			
			List<Flow> affectedFlows = new ArrayList<>();
			
			for (List<Flow> flows: settingFlows.values()) {
				affectedFlows.addAll(flows);
			}
			
			String sql = "SELECT * FROM " + feedbackSurveyDAO.getTableName() + " WHERE flowID IN ("+affectedFlows.stream().map(Flow::getFlowID).map(Object::toString).collect(Collectors.joining(","))+") AND commentSent=0 AND NOT COMMENT IS NULL";
						
						
			commentQuery.setSql(sql);
			
			List<FeedbackSurvey> feedbackSurveys = feedbackSurveyDAO.getAll(commentQuery);

			if (feedbackSurveys != null) {
				
				for (FeedbackSurvey feedbackSurvey : feedbackSurveys) {
					
					TransactionHandler transactionHandler = feedbackSurveyDAO.createTransaction();

					try {
						String updatesql = "UPDATE " + feedbackSurveyDAO.getTableName() + " SET commentSent=1 WHERE FLOWID=?";
						
						LowLevelQuery<FeedbackSurvey> updateCommentQuery = new LowLevelQuery<>();
						
						updateCommentQuery.setSql(updatesql);
						updateCommentQuery.addParameter(feedbackSurvey.getFlowID());
					
						feedbackSurveyDAO.update(updateCommentQuery, transactionHandler);
						
						Optional<Flow> affectedFlow = affectedFlows.stream().filter(e-> e.getFlowID().equals(feedbackSurvey.getFlowID())).findFirst();
						
						if(!affectedFlow.isPresent()) {
							throw new IllegalArgumentException("Missing affected flow from feedbacksurvey");
						}
						
						TagReplacer tagReplacer = new TagReplacer();
						tagReplacer.addTagSource(new SingleTagSource("$flow.name", affectedFlow.get().getName()));
						tagReplacer.addTagSource(new SingleTagSource("$flow.url", HTMLUtils.replaceURLsInString(flowNotificationHandler.getFlowAdminModuleAlias() +"/showflow/" + affectedFlow.get().getFlowID()+"#flowsurveys", false)));
						
						SimpleEmail email = new SimpleEmail(systemInterface.getEncoding());
						
						email.addRecipients(getNotificationEmailAddresses(feedbackSurvey.getFlowID(), settingFlows));
							
						email.setMessageContentType(SimpleEmail.HTML);
						email.setSenderName(flowNotificationHandler.getEmailSenderName(null));
						email.setSenderAddress(flowNotificationHandler.getEmailSenderAddress(null));
						email.setSubject(tagReplacer.replace(feedbackFlowSurveyGlobalEmailSubject));
						email.setMessage(EmailUtils.addMessageBody(tagReplacer.replace(feedbackFlowSurveyGlobalEmailMessage)));

						log.info("Sending feedbackflowsubmit mail of comments for flow "+feedbackSurvey.getFlowID());
						systemInterface.getEmailHandler().send(email);
						
						transactionHandler.commit();
					
					}catch(Exception ex) {
						transactionHandler.abort();
						log.error("Could not update feedbackflowsurvey when sending mail.",ex);
					} finally {
						TransactionHandler.autoClose(transactionHandler);
					}
					
					
				}
			}

		} catch (Exception ex) {
			log.error("Error sending mail for comments", ex);
		}
		
	}
	
	private List<String> getNotificationEmailAddresses(Integer flowID, Map<FeedbackSurveySettings, List<Flow>> settingFlows) {

		for(FeedbackSurveySettings settings : settingFlows.keySet()) {
			if(settingFlows.get(settings).stream().map(Flow::getFlowID).anyMatch(e-> e.equals(flowID))) {
				return settings.getNotificationEmailAddresses();
			}
		}
		throw new IllegalArgumentException("Missing notificationemailaddresses");
	}

	private Map<FeedbackSurveySettings,List<Flow>> getAffectedFlows(List<FeedbackSurveySettings> settingsList) {

		Map<FeedbackSurveySettings, List<Flow>> affectedFlows = new HashMap<>();
		Collection<Flow> flows = flowAdminModule.getFlowCache().getFlowCacheMap().values();
		
		if(flows != null) {
			
			for (FeedbackSurveySettings setting : settingsList) {
				if(setting.isSendEmail() && flows.stream().anyMatch(e-> e.getFlowFamily().getFlowFamilyID().equals(setting.getFlowFamilyID()))) {
					affectedFlows.put(setting, flows.stream().filter(e-> e.getFlowFamily().getFlowFamilyID().equals(setting.getFlowFamilyID())).collect(Collectors.toList()));
				}
			}
		}
		
		return affectedFlows;
		
	}
}
