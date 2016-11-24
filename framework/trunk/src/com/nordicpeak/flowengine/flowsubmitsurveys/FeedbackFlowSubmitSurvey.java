package com.nordicpeak.flowengine.flowsubmitsurveys;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import se.unlogic.hierarchy.core.annotations.CheckboxSettingDescriptor;
import se.unlogic.hierarchy.core.annotations.InstanceManagerDependency;
import se.unlogic.hierarchy.core.annotations.ModuleSetting;
import se.unlogic.hierarchy.core.annotations.XSLVariable;
import se.unlogic.hierarchy.core.beans.LinkTag;
import se.unlogic.hierarchy.core.beans.ScriptTag;
import se.unlogic.hierarchy.core.beans.SimpleForegroundModuleResponse;
import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.hierarchy.core.exceptions.AccessDeniedException;
import se.unlogic.hierarchy.core.exceptions.URINotFoundException;
import se.unlogic.hierarchy.core.interfaces.ForegroundModuleDescriptor;
import se.unlogic.hierarchy.core.interfaces.ForegroundModuleResponse;
import se.unlogic.hierarchy.core.interfaces.SectionInterface;
import se.unlogic.hierarchy.core.interfaces.ViewFragment;
import se.unlogic.hierarchy.core.utils.ModuleViewFragmentTransformer;
import se.unlogic.hierarchy.core.utils.ViewFragmentModule;
import se.unlogic.hierarchy.foregroundmodules.AnnotatedForegroundModule;
import se.unlogic.standardutils.dao.AnnotatedDAO;
import se.unlogic.standardutils.dao.HighLevelQuery;
import se.unlogic.standardutils.dao.LowLevelQuery;
import se.unlogic.standardutils.dao.QueryParameterFactory;
import se.unlogic.standardutils.dao.SimpleAnnotatedDAOFactory;
import se.unlogic.standardutils.dao.TransactionHandler;
import se.unlogic.standardutils.db.tableversionhandler.TableVersionHandler;
import se.unlogic.standardutils.db.tableversionhandler.UpgradeResult;
import se.unlogic.standardutils.db.tableversionhandler.XMLDBScriptProvider;
import se.unlogic.standardutils.json.JsonArray;
import se.unlogic.standardutils.numbers.NumberUtils;
import se.unlogic.standardutils.populators.EnumPopulator;
import se.unlogic.standardutils.time.TimeUtils;
import se.unlogic.standardutils.validation.ValidationException;
import se.unlogic.standardutils.xml.XMLUtils;
import se.unlogic.webutils.http.URIParser;
import se.unlogic.webutils.populators.annotated.AnnotatedRequestPopulator;

import com.nordicpeak.flowengine.FlowAdminModule;
import com.nordicpeak.flowengine.beans.FlowInstance;
import com.nordicpeak.flowengine.interfaces.FlowSubmitSurveyProvider;
import com.nordicpeak.flowengine.interfaces.ImmutableFlow;
import com.nordicpeak.flowengine.managers.FlowInstanceManager;

public class FeedbackFlowSubmitSurvey extends AnnotatedForegroundModule implements FlowSubmitSurveyProvider, ViewFragmentModule<ForegroundModuleDescriptor> {

	private static final String FLOW_FAMILY_FEEDBACK_QUERY = "SELECT feedback_flow_submit_surveys.* FROM feedback_flow_submit_surveys INNER JOIN flowengine_flows ON feedback_flow_submit_surveys.flowID = flowengine_flows.flowID WHERE flowFamilyID = ? AND (added BETWEEN ? AND ?);";

	private static final AnnotatedRequestPopulator<FeedbackSurvey> FEEDBACK_SURVEY_POPULATOR = new AnnotatedRequestPopulator<FeedbackSurvey>(FeedbackSurvey.class, new EnumPopulator<Answer>(Answer.class));

	@XSLVariable(prefix = "java.")
	private String chartDataTitle;

	@ModuleSetting
	@CheckboxSettingDescriptor(name="Show comment field", description="Controls if the comment field is shown or not")
	private boolean showCommentField = true;
	
	@InstanceManagerDependency(required = true)
	private FlowAdminModule flowAdminModule;

	private AnnotatedDAO<FeedbackSurvey> feedbackSurveyDAO;

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

		flowInstanceIDParameterFactory = feedbackSurveyDAO.getParamFactory("flowInstanceID", Integer.class);
		flowIDParameterFactory = feedbackSurveyDAO.getParamFactory("flowID", Integer.class);
	}

	@Override
	public void init(ForegroundModuleDescriptor moduleDescriptor, SectionInterface sectionInterface, DataSource dataSource) throws Exception {

		super.init(moduleDescriptor, sectionInterface, dataSource);

		if (!systemInterface.getInstanceHandler().addInstance(FlowSubmitSurveyProvider.class, this)) {

			log.warn("Unable to register module " + moduleDescriptor + " in instance handler, another module is already registered for class " + FlowSubmitSurveyProvider.class.getName());
		}

		this.viewFragmentTransformer = new ModuleViewFragmentTransformer<ForegroundModuleDescriptor>(sectionInterface.getModuleXSLTCache(), this, systemInterface.getEncoding());
	}

	@Override
	public void update(ForegroundModuleDescriptor descriptor, DataSource dataSource) throws Exception {

		super.update(descriptor, dataSource);

		this.viewFragmentTransformer = new ModuleViewFragmentTransformer<ForegroundModuleDescriptor>(sectionInterface.getModuleXSLTCache(), this, systemInterface.getEncoding());
	}

	@Override
	public void unload() throws Exception {

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

					FeedbackSurvey feedbackSurvey = FEEDBACK_SURVEY_POPULATOR.populate(req);

					feedbackSurvey.setFlowID(flowInstance.getFlow().getFlowID());
					feedbackSurvey.setFlowInstanceID(flowInstance.getFlowInstanceID());
					feedbackSurvey.setAdded(TimeUtils.getCurrentTimestamp());

					transactionHandler = feedbackSurveyDAO.createTransaction();
					
					if(!feedbackSurveyDAO.beanExists(feedbackSurvey, transactionHandler)){
						
						feedbackSurveyDAO.add(feedbackSurvey, transactionHandler, null);
						
						transactionHandler.commit();
					}

					XMLUtils.appendNewElement(doc, document, "FeedbackSurveySuccess");

				} catch (ValidationException validationException) {

					XMLUtils.append(doc, document, validationException.getErrors());
				
				} finally{
					
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
	public ViewFragment getSurveyFormFragment(HttpServletRequest req, User user, FlowInstanceManager instanceManager) throws TransformerConfigurationException, TransformerException, SQLException {

		FeedbackSurvey feedbackSurvey = getFeedbackSurvey(instanceManager.getFlowInstanceID());

		if (feedbackSurvey == null) {

			Document doc = XMLUtils.createDomDocument();
			Element document = doc.createElement("Document");
			doc.appendChild(document);

			Element formElement = doc.createElement("FeedbackSurveyForm");
			document.appendChild(formElement);

			XMLUtils.appendNewElement(doc, formElement, "ShowCommentField", this.showCommentField);
			
			ImmutableFlow flow = instanceManager.getFlowInstance().getFlow();

			XMLUtils.appendNewElement(doc, formElement, "flowName", flow.getName());
			XMLUtils.appendNewElement(doc, formElement, "flowInstanceID", instanceManager.getFlowInstanceID());

			XMLUtils.appendNewElement(doc, formElement, "ModuleURI", this.getModuleURI(req));

			return viewFragmentTransformer.createViewFragment(doc);

		}

		return null;

	}

	@Override
	public ViewFragment getShowFlowSurveysFragment(Integer flowID) throws TransformerConfigurationException, TransformerException, SQLException {

		Document doc = XMLUtils.createDomDocument();
		Element document = doc.createElement("Document");
		doc.appendChild(document);

		Element showElement = doc.createElement("ShowFlowFeedbackSurveys");
		document.appendChild(showElement);

		HighLevelQuery<FeedbackSurvey> query = new HighLevelQuery<FeedbackSurvey>();

		query.addParameter(flowIDParameterFactory.getParameter(flowID));

		List<FeedbackSurvey> surveys = feedbackSurveyDAO.getAll(query);

		if (surveys != null) {

			List<FeedbackSurvey> commentSurveys = new ArrayList<FeedbackSurvey>();

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

				if(survey.getComment() != null) {

					commentSurveys.add(survey);
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
			XMLUtils.append(doc, showElement, "Comments", commentSurveys);


		}

		return viewFragmentTransformer.createViewFragment(doc);
	}

	private FeedbackSurvey getFeedbackSurvey(Integer flowInstanceID) throws SQLException {

		HighLevelQuery<FeedbackSurvey> query = new HighLevelQuery<FeedbackSurvey>();

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
	public Float getWeeklyAverage(Integer flowFamilyID, Timestamp startDate, Timestamp endDate) throws SQLException{

		LowLevelQuery<FeedbackSurvey> query = new LowLevelQuery<FeedbackSurvey>(FLOW_FAMILY_FEEDBACK_QUERY);

		query.addParameter(flowFamilyID);
		query.addParameter(startDate);
		query.addParameter(endDate);

		List<FeedbackSurvey> feedbackList = feedbackSurveyDAO.getAll(query);

		if(feedbackList == null){

			return null;
		}

		int sum = 0;

		for(FeedbackSurvey feedback : feedbackList){

			sum += feedback.getAnswer().ordinal() + 1;
		}

		return (float)sum/(float)feedbackList.size();
	}

}
