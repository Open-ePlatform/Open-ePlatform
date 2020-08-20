package com.nordicpeak.flowengine.flowapprovalmodule;

import java.io.IOException;
import java.lang.reflect.Field;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import se.unlogic.hierarchy.core.annotations.InstanceManagerDependency;
import se.unlogic.hierarchy.core.annotations.ModuleSetting;
import se.unlogic.hierarchy.core.annotations.TextFieldSettingDescriptor;
import se.unlogic.hierarchy.core.annotations.WebPublic;
import se.unlogic.hierarchy.core.annotations.XSLVariable;
import se.unlogic.hierarchy.core.beans.SimpleForegroundModuleResponse;
import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.hierarchy.core.enums.CRUDAction;
import se.unlogic.hierarchy.core.enums.EventTarget;
import se.unlogic.hierarchy.core.events.CRUDEvent;
import se.unlogic.hierarchy.core.exceptions.AccessDeniedException;
import se.unlogic.hierarchy.core.exceptions.ModuleConfigurationException;
import se.unlogic.hierarchy.core.exceptions.URINotFoundException;
import se.unlogic.hierarchy.core.interfaces.AccessInterface;
import se.unlogic.hierarchy.core.interfaces.ForegroundModuleResponse;
import se.unlogic.hierarchy.core.interfaces.SectionInterface;
import se.unlogic.hierarchy.core.interfaces.ViewFragment;
import se.unlogic.hierarchy.core.interfaces.modules.descriptors.ForegroundModuleDescriptor;
import se.unlogic.hierarchy.core.utils.AccessUtils;
import se.unlogic.hierarchy.core.utils.AttributeTagUtils;
import se.unlogic.hierarchy.core.utils.HierarchyAnnotatedDAOFactory;
import se.unlogic.hierarchy.core.utils.UserUtils;
import se.unlogic.hierarchy.core.utils.ViewFragmentUtils;
import se.unlogic.hierarchy.core.utils.crud.FragmentLinkScriptFilter;
import se.unlogic.hierarchy.core.utils.extensionlinks.ExtensionLink;
import se.unlogic.hierarchy.foregroundmodules.rest.AnnotatedRESTModule;
import se.unlogic.hierarchy.foregroundmodules.rest.RESTMethod;
import se.unlogic.hierarchy.foregroundmodules.rest.URIParam;
import se.unlogic.standardutils.collections.CollectionUtils;
import se.unlogic.standardutils.dao.AdvancedAnnotatedDAOWrapper;
import se.unlogic.standardutils.dao.AnnotatedDAO;
import se.unlogic.standardutils.dao.LowLevelQuery;
import se.unlogic.standardutils.db.tableversionhandler.TableVersionHandler;
import se.unlogic.standardutils.db.tableversionhandler.UpgradeResult;
import se.unlogic.standardutils.db.tableversionhandler.XMLDBScriptProvider;
import se.unlogic.standardutils.hash.HashAlgorithms;
import se.unlogic.standardutils.hash.HashUtils;
import se.unlogic.standardutils.populators.StringPopulator;
import se.unlogic.standardutils.string.StringUtils;
import se.unlogic.standardutils.time.TimeUtils;
import se.unlogic.standardutils.validation.ValidationError;
import se.unlogic.standardutils.validation.ValidationException;
import se.unlogic.standardutils.xml.XMLUtils;
import se.unlogic.webutils.http.RequestUtils;
import se.unlogic.webutils.http.URIParser;
import se.unlogic.webutils.validation.ValidationUtils;

import com.nordicpeak.flowengine.BaseFlowModule;
import com.nordicpeak.flowengine.FlowAdminModule;
import com.nordicpeak.flowengine.FlowInstanceAdminModule;
import com.nordicpeak.flowengine.UserFlowInstanceMenuModule;
import com.nordicpeak.flowengine.beans.ExternalMessage;
import com.nordicpeak.flowengine.beans.Flow;
import com.nordicpeak.flowengine.beans.FlowInstance;
import com.nordicpeak.flowengine.beans.FlowInstanceEvent;
import com.nordicpeak.flowengine.beans.RequestMetadata;
import com.nordicpeak.flowengine.beans.SimpleSigningRequest;
import com.nordicpeak.flowengine.enums.EventType;
import com.nordicpeak.flowengine.enums.SenderType;
import com.nordicpeak.flowengine.events.ExternalMessageAddedEvent;
import com.nordicpeak.flowengine.exceptions.evaluation.EvaluationException;
import com.nordicpeak.flowengine.exceptions.evaluationprovider.EvaluationProviderException;
import com.nordicpeak.flowengine.exceptions.flow.FlowDefaultStatusNotFound;
import com.nordicpeak.flowengine.exceptions.flowinstance.InvalidFlowInstanceStepException;
import com.nordicpeak.flowengine.exceptions.flowinstance.MissingQueryInstanceDescriptor;
import com.nordicpeak.flowengine.exceptions.flowinstancemanager.DuplicateFlowInstanceManagerIDException;
import com.nordicpeak.flowengine.exceptions.queryinstance.QueryRequestException;
import com.nordicpeak.flowengine.exceptions.queryinstance.UnableToGetQueryInstanceShowHTMLException;
import com.nordicpeak.flowengine.exceptions.queryprovider.QueryInstanceNotFoundInQueryProviderException;
import com.nordicpeak.flowengine.exceptions.queryprovider.QueryProviderErrorException;
import com.nordicpeak.flowengine.exceptions.queryprovider.QueryProviderException;
import com.nordicpeak.flowengine.exceptions.queryprovider.QueryProviderNotFoundException;
import com.nordicpeak.flowengine.flowapprovalmodule.beans.FlowApprovalActivity;
import com.nordicpeak.flowengine.flowapprovalmodule.beans.FlowApprovalActivityGroup;
import com.nordicpeak.flowengine.flowapprovalmodule.beans.FlowApprovalActivityProgress;
import com.nordicpeak.flowengine.flowapprovalmodule.beans.FlowApprovalActivityRound;
import com.nordicpeak.flowengine.interfaces.GenericSigningProvider;
import com.nordicpeak.flowengine.interfaces.ImmutableFlowEngineInterface;
import com.nordicpeak.flowengine.interfaces.QueryHandler;
import com.nordicpeak.flowengine.interfaces.SigningResponse;
import com.nordicpeak.flowengine.interfaces.UserMenuProvider;
import com.nordicpeak.flowengine.managers.FlowInstanceManager;
import com.nordicpeak.flowengine.managers.ImmutableFlowInstanceManager;
import com.nordicpeak.flowengine.managers.ManagerResponse;
import com.nordicpeak.flowengine.utils.CitizenIdentifierUtils;
import com.nordicpeak.flowengine.utils.ExternalMessageUtils;

public class FlowApprovalUserModule extends AnnotatedRESTModule implements UserMenuProvider, ImmutableFlowEngineInterface {

	@XSLVariable(prefix = "java.")
	private String userMenuTabTitle = "My organizations";

	@XSLVariable(prefix = "java.")
	protected String signingMessage = "Set $activity.name (ID $activityProgress.ID) to $state for flow instance $flowInstance.ID. The activity has the following unique key: $hash";
	
	@XSLVariable(prefix = "i18n.", name = "FlowInstance.flowInstanceID")
	private String i18nFlowInstanceID = "FlowinstanceID";
	
	@XSLVariable(prefix = "i18n.", name = "Flow.name")
	private String i18nFlow = "Flow";
	
	@XSLVariable(prefix = "java.", name = "Signing.user")
	private String i18nSigningUser = "User";
	
	@XSLVariable(prefix = "java.", name = "Signing.flowInstanceWasVisible")
	private String i18nFlowInstanceWasVisible = "FlowInstance visible";
	
	@XSLVariable(prefix = "i18n.", name = "Activity")
	private String i18nActivity = "Activity";
	
	@XSLVariable(prefix = "java.", name = "Signing.ActivityProgress.State")
	private String i18nActivityProgressState = "State";
	
	@XSLVariable(prefix = "i18n.", name = "ActivityProgress.comment")
	private String i18nActivityProgressComment = "Comment";
	
	@XSLVariable(prefix = "i18n.", name = "Activity.description")
	private String i18nActivityDescription = "Description";
	
	@XSLVariable(prefix = "i18n.", name = "Activity.shortDescription")
	private String i18nActivityShortDescription = "ShortDescription";
	
	@XSLVariable(prefix = "i18n.", name = "ActivityProgress.approved")
	private String i18nActivityProgressApproved = "Approved";
	
	@XSLVariable(prefix = "i18n.", name = "ActivityProgress.denied")
	private String i18nActivityProgressDenied = "Denied";
	
	@ModuleSetting
	@TextFieldSettingDescriptor(name = "User menu item slot", description = "User menu item slot")
	protected String userMenuExtensionLinkSlot = "30";

	private AnnotatedDAO<FlowApprovalActivity> activityDAO;
	private AnnotatedDAO<FlowApprovalActivityProgress> activityProgressDAO;

	private AdvancedAnnotatedDAOWrapper<FlowApprovalActivityProgress, Integer> activityProgressDAOWrapper;

	@InstanceManagerDependency(required = true)
	protected FlowAdminModule flowAdminModule;

	@InstanceManagerDependency(required = true)
	protected FlowApprovalAdminModule approvalAdminModule;
	
	@InstanceManagerDependency
	protected GenericSigningProvider genericSigningProvider;

	protected UserFlowInstanceMenuModule userFlowInstanceMenuModule;

	protected ExtensionLink userMenuLink;

	@Override
	public void init(ForegroundModuleDescriptor moduleDescriptor, SectionInterface sectionInterface, DataSource dataSource) throws Exception {

		super.init(moduleDescriptor, sectionInterface, dataSource);

		if (!systemInterface.getInstanceHandler().addInstance(FlowApprovalUserModule.class, this)) {

			throw new RuntimeException("Unable to register module in global instance handler using key " + FlowApprovalUserModule.class.getSimpleName() + ", another instance is already registered using this key.");
		}
	}

	@Override
	protected void createDAOs(DataSource dataSource) throws Exception {

		super.createDAOs(dataSource);

		//Automatic table version handling
		UpgradeResult upgradeResult = TableVersionHandler.upgradeDBTables(dataSource, FlowApprovalAdminModule.class.getName(), new XMLDBScriptProvider(FlowApprovalAdminModule.class.getResourceAsStream("DB script.xml")));

		if (upgradeResult.isUpgrade()) {

			log.info(upgradeResult.toString());
		}

		HierarchyAnnotatedDAOFactory daoFactory = new HierarchyAnnotatedDAOFactory(dataSource, systemInterface.getUserHandler(), systemInterface.getGroupHandler(), true, true, false);

		activityDAO = daoFactory.getDAO(FlowApprovalActivity.class);
		activityProgressDAO = daoFactory.getDAO(FlowApprovalActivityProgress.class);

		activityProgressDAOWrapper = activityProgressDAO.getAdvancedWrapper(Integer.class);
		activityProgressDAOWrapper.getGetQuery().addRelations(FlowApprovalActivityProgress.ACTIVITY_ROUND_RELATION, FlowApprovalActivityProgress.ACTIVITY_RELATION, FlowApprovalActivity.ACTIVITY_GROUP_RELATION, FlowApprovalActivity.USERS_RELATION, FlowApprovalActivity.GROUPS_RELATION);
	}

	@InstanceManagerDependency
	public void setUserFlowInstanceMenuModule(UserFlowInstanceMenuModule userFlowInstanceMenuModule) {

		if (userFlowInstanceMenuModule == null && this.userFlowInstanceMenuModule != null) {

			this.userFlowInstanceMenuModule.removeUserMenuProvider(this);
		}

		this.userFlowInstanceMenuModule = userFlowInstanceMenuModule;

		if (this.userFlowInstanceMenuModule != null) {

			this.userFlowInstanceMenuModule.addUserMenuProvider(this);
		}
	}

	@Override
	public void unload() throws Exception {

		if (userFlowInstanceMenuModule != null) {

			setUserFlowInstanceMenuModule(null);
		}

		systemInterface.getInstanceHandler().removeInstance(FlowApprovalUserModule.class, this);

		super.unload();
	}

	@Override
	protected void moduleConfigured() throws Exception {

		super.moduleConfigured();

		this.userMenuLink = new ExtensionLink(userMenuTabTitle, getFullAlias(), "b", userMenuExtensionLinkSlot);

		if (userFlowInstanceMenuModule != null) {

			userFlowInstanceMenuModule.sortProviders();
		}
	}

	@Override
	public ForegroundModuleResponse defaultMethod(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws AccessDeniedException, SQLException {

		return listPending(req, res, user, uriParser, null);
	}

	public ForegroundModuleResponse listPending(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser, List<ValidationError> validationErrors) throws AccessDeniedException, SQLException {

		if (user == null) {
			throw new AccessDeniedException("User needs to be logged in");
		}

		log.info("User " + user + " listing pending activities");

		Document doc = createDocument(req, uriParser, user);

		Element listPending = XMLUtils.appendNewElement(doc, doc.getDocumentElement(), "ListPendingActivities");

		List<FlowApprovalActivityProgress> pendingActivities = getPendingActivities(user, FlowApprovalActivityProgress.ACTIVITY_RELATION, FlowApprovalActivityProgress.ACTIVITY_ROUND_RELATION, FlowApprovalActivityRound.ACTIVITY_GROUP_RELATION);

		appendActivities(pendingActivities, doc, listPending);

		if (validationErrors != null) {

			listPending.appendChild(RequestUtils.getRequestParameters(req, doc));
			XMLUtils.append(doc, listPending, "ValidationErrors", validationErrors);
		}

		return new SimpleForegroundModuleResponse(doc, moduleDescriptor.getName(), getDefaultBreadcrumb());
	}

	@WebPublic(requireLogin = true, toLowerCase = true)
	public ForegroundModuleResponse listCompleted(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws AccessDeniedException, SQLException {

		log.info("User " + user + " listing completed activities");

		Document doc = createDocument(req, uriParser, user);

		Element listCompleted = XMLUtils.appendNewElement(doc, doc.getDocumentElement(), "ListCompletedActivities");

		List<FlowApprovalActivityProgress> completedActivities = getCompletedActivities(user, FlowApprovalActivityProgress.ACTIVITY_RELATION, FlowApprovalActivityProgress.ACTIVITY_ROUND_RELATION, FlowApprovalActivityRound.ACTIVITY_GROUP_RELATION);

		appendActivities(completedActivities, doc, listCompleted);

		return new SimpleForegroundModuleResponse(doc, moduleDescriptor.getName(), getDefaultBreadcrumb());
	}

	private void appendActivities(List<FlowApprovalActivityProgress> activities, Document doc, Element listElement) throws SQLException {

		// flowInstanceID, activityGroup, activityRound, activityProgress
		Map<Integer, Map<FlowApprovalActivityGroup, Set<FlowApprovalActivityRound>>> mapping = new TreeMap<>();

		if (activities != null) {
			for (FlowApprovalActivityProgress activityProgress : activities) {

				FlowApprovalActivityRound round = activityProgress.getActivityRound();
				
				round.setActivityProgresses(CollectionUtils.addAndInstantiateIfNeeded(round.getActivityProgresses(), activityProgress));
				
				activityProgress.setActivityRound(null);
				
				Map<FlowApprovalActivityGroup, Set<FlowApprovalActivityRound>> activityGroupToRoundMap = mapping.get(round.getFlowInstanceID());

				if (activityGroupToRoundMap == null) {

					activityGroupToRoundMap = new TreeMap<>();
					mapping.put(round.getFlowInstanceID(), activityGroupToRoundMap);
				}

				Set<FlowApprovalActivityRound> rounds = activityGroupToRoundMap.get(round.getActivityGroup());

				if (rounds == null) {

					rounds = new HashSet<>();
					activityGroupToRoundMap.put(round.getActivityGroup(), rounds);
				}

				rounds.add(round);
			}
		}

		Element flowInstancesElement = XMLUtils.appendNewElement(doc, listElement, "FlowInstances");

		for (Entry<Integer, Map<FlowApprovalActivityGroup, Set<FlowApprovalActivityRound>>> entry : mapping.entrySet()) {

			Integer flowInstanceID = entry.getKey();

			FlowInstance flowInstance = flowAdminModule.getFlowInstance(flowInstanceID, null, FlowInstance.FLOW_RELATION, FlowInstance.STATUS_RELATION, Flow.FLOW_TYPE_RELATION, Flow.FLOW_FAMILY_RELATION, FlowInstance.ATTRIBUTES_RELATION);

			if (flowInstance != null && flowInstance.getFlow().isEnabled()) {
				
				Element flowInstanceElement = flowInstance.toXML(doc);

				Map<FlowApprovalActivityGroup, Set<FlowApprovalActivityRound>> activityGroupToRoundMap = entry.getValue();

				Element activityGroupsElement = XMLUtils.appendNewElement(doc, flowInstanceElement, "ActivityGroups");

				for (Entry<FlowApprovalActivityGroup, Set<FlowApprovalActivityRound>> entry2 : activityGroupToRoundMap.entrySet()) {

					FlowApprovalActivityGroup activityGroup = entry2.getKey();

					Element activityGroupElement = activityGroup.toXML(doc);

					Set<FlowApprovalActivityRound> activityRounds = entry2.getValue();
					
					Element roundsElement = XMLUtils.appendNewElement(doc, activityGroupElement, "ActivityRounds");
					
					for (FlowApprovalActivityRound activityRound : activityRounds) {
					
						List<FlowApprovalActivityProgress> activityProgresses = activityRound.getActivityProgresses();
						
						activityRound.setActivityGroup(null);
						activityRound.setActivityProgresses(null);
						
						Element roundElement = activityRound.toXML(doc);
						
						Element activityProgressesElement = XMLUtils.appendNewElement(doc, activityGroupElement, "ActivityProgresses");

						for (FlowApprovalActivityProgress activityProgress : activityProgresses) {

							Element activityProgressElement = activityProgress.toXML(doc);

							if (activityProgress.getActivity().getShortDescription() != null) {

								XMLUtils.appendNewElement(doc, activityProgressElement, "ShortDescription", AttributeTagUtils.replaceTags(activityProgress.getActivity().getShortDescription(), flowInstance.getAttributeHandler()));
							}

							activityProgressesElement.appendChild(activityProgressElement);
						}

						roundElement.appendChild(activityProgressesElement);
						roundsElement.appendChild(roundElement);
					}
					
					activityGroupsElement.appendChild(activityGroupElement);
				}

				flowInstancesElement.appendChild(flowInstanceElement);
			}
		}
	}

	private List<FlowApprovalActivityProgress> getPendingActivities(User user, Field... relations) throws SQLException {

		return getActivities(user, false, relations);
	}

	private List<FlowApprovalActivityProgress> getCompletedActivities(User user, Field... relations) throws SQLException {

		return getActivities(user, true, relations);
	}
	
	private List<FlowApprovalActivityProgress> getActivities(User user, boolean active, Field... relations) throws SQLException {
		
		return getActivities(user, null, active, relations);
	}
	
	protected List<FlowApprovalActivityProgress> getActivities(User user, Integer flowInstanceID, Boolean completed, Field... relations) throws SQLException {
		
		ArrayList<Integer> userGroupIDs = UserUtils.getUserGroupIDs(user, true);
		
		//TODO split into multiple smaller queries
		
		// @formatter:off
		StringBuilder builder = new StringBuilder();
		builder.append("SELECT DISTINCT ap.activityProgressID as dummy, ap.* FROM " + activityProgressDAO.getTableName() + " ap"
		              +" INNER JOIN flowapproval_activity_rounds r ON r.activityRoundID = ap.activityRoundID"
		              +" INNER JOIN " + activityDAO.getTableName() + " a ON ap.activityID = a.activityID"
		              +" LEFT OUTER JOIN flowapproval_activity_users u ON u.activityID = a.activityID "
		              +" LEFT OUTER JOIN flowapproval_activity_groups g ON g.activityID = a.activityID "
		              +" LEFT OUTER JOIN flowapproval_activity_resp_user_attribute ra ON ra.activityID = a.activityID"
		              +" LEFT OUTER JOIN flowapproval_activity_progress_resp_attr_users ru ON ap.activityProgressID = ru.activityProgressID"
		              +" WHERE");
		// @formatter:on
		
		if (flowInstanceID != null) {
			builder.append(" r.flowInstanceID = " + flowInstanceID + " AND");
		}
		
		if (completed != null) {
			if (completed) {
				builder.append(" (r.completed IS NOT NULL OR r.cancelled IS NOT NULL OR ap.completed IS NOT NULL) AND");
				
			} else {
				builder.append(" r.completed IS NULL AND r.cancelled IS NULL AND ap.completed IS NULL AND");
			}
		}
		
		// @formatter:off
		builder.append(" ((u.fallback = 0 AND u.userID = " + user.getUserID() + ") OR g.groupID IN (" + (userGroupIDs != null ? StringUtils.toCommaSeparatedString(userGroupIDs) : null) + ")"
		              +" OR (ra.attributeName IS NOT NULL AND (ru.userID = " + user.getUserID() + " OR (ru.userID IS NULL AND u.fallback = 1 AND u.userID = " + user.getUserID() + "))))");
		// @formatter:on
		
		LowLevelQuery<FlowApprovalActivityProgress> query = new LowLevelQuery<>(builder.toString());

		if (relations != null) {

			query.addRelations(relations);
			query.addCachedRelations(FlowApprovalActivity.ACTIVITY_GROUP_RELATION, FlowApprovalActivityProgress.ACTIVITY_ROUND_RELATION);
		}

		return activityProgressDAO.getAll(query);
	}

	@RESTMethod(alias = "show/{activityProgressID}", method = { "get", "post" }, requireLogin = true)
	public ForegroundModuleResponse showActivity(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser, @URIParam(name = "activityProgressID") Integer activityProgressID) throws SQLException, URINotFoundException, IOException, AccessDeniedException, MissingQueryInstanceDescriptor, QueryProviderNotFoundException, InvalidFlowInstanceStepException, QueryProviderErrorException, QueryInstanceNotFoundInQueryProviderException, ModuleConfigurationException {

		FlowApprovalActivityProgress activityProgress = activityProgressDAOWrapper.get(activityProgressID);

		if (activityProgress == null) {
			throw new URINotFoundException(uriParser);
		}

		FlowApprovalActivity activity = activityProgress.getActivity();
		FlowApprovalActivityGroup activityGroup = activity.getActivityGroup();
//		FlowApprovalActivityRound round = activityProgress.getActivityRound();

		FlowInstance flowInstance;
		ImmutableFlowInstanceManager instanceManager = null;
		
		if (activity.isShowFlowInstance()) {
			
			instanceManager = flowAdminModule.getImmutableFlowInstanceManager(activityProgress.getActivityRound().getFlowInstanceID());
			flowInstance = (FlowInstance) instanceManager.getFlowInstance();
			
		} else {
			
			flowInstance = flowAdminModule.getFlowInstance(activityProgress.getActivityRound().getFlowInstanceID());
		}

		if (!AccessUtils.checkAccess(user, activityProgress)) {

			try {
				FlowInstanceAdminModule.GENERAL_ACCESS_CONTROLLER.checkManagerAccess(flowInstance, user);

			} catch (AccessDeniedException e) {

				throw new AccessDeniedException("User does not have access to activity " + activity + " nor is a manager for " + flowInstance);
			}
		}
		
		if (!flowInstance.getFlow().isEnabled()) {
			
			return listPending(req, res, user, uriParser, Collections.singletonList(BaseFlowModule.FLOW_DISABLED_VALIDATION_ERROR));
		}

		List<ValidationError> validationErrors = null;

		if (req.getMethod().equalsIgnoreCase("POST") && activityProgress.isMutable()) {

			validationErrors = new ArrayList<>();

			boolean completed = false;

			if (activityGroup.isUseApproveDeny()) {

				if ("approved".equals(req.getParameter("completed"))) {

					log.info("User " + user + " approved activity progress " + activityProgress);

					activityProgress.setDenied(false);
					completed = true;

				} else if ("denied".equals(req.getParameter("completed"))) {

					log.info("User " + user + " denied activity progress " + activityProgress);

					activityProgress.setDenied(true);
					completed = true;
				}

			} else {

				if ("true".equals(req.getParameter("completed"))) {

					log.info("User " + user + " completing activity progress " + activityProgress);

					completed = true;
				}
			}

			String comment = ValidationUtils.validateParameter("comment", req, false, 0, 65535, StringPopulator.getPopulator(), validationErrors);

			if (validationErrors.isEmpty() && (completed || !StringUtils.compare(comment, activityProgress.getComment()))) {

				activityProgress.setComment(comment);
				
				if (completed && !activity.isRequireSigning()) {

					activityProgress.setCompleted(TimeUtils.getCurrentTimestamp());
					activityProgress.setCompletingUser(user);
				}

				activityProgressDAOWrapper.update(activityProgress);

				if (completed) {

					if (activity.isRequireSigning()) {
					
						redirectToMethod(req, res, "/signactivity/" + activityProgress.getActivityProgressID());
						return null;
					}
					
					//TODO show error if next status was not found
					approvalAdminModule.checkApprovalCompletion(activityProgress.getActivity().getActivityGroup(), flowInstance);
					
					if (activityGroup.isAppendCommentsToExternalMessages() && flowInstance.isExternalMessagesEnabled() && !StringUtils.isEmpty(comment) && flowInstance.getOwners() != null) {

						log.info("Copying comment to external messages for flowinstance " + flowInstance);

						ExternalMessage externalMessage = new ExternalMessage();
						externalMessage.setFlowInstance(flowInstance);
						externalMessage.setPoster(user);
						externalMessage.setMessage(activity.getName() + ":\r" + comment);
						externalMessage.setAdded(TimeUtils.getCurrentTimestamp());
						externalMessage.setAttachments(null);
						externalMessage.setPostedByManager(true);

						flowAdminModule.getDAOFactory().getExternalMessageDAO().add(externalMessage);

						FlowInstanceEvent flowInstanceEvent = flowAdminModule.getFlowInstanceEventGenerator().addFlowInstanceEvent(flowInstance, EventType.MANAGER_MESSAGE_SENT, null, user, null, ExternalMessageUtils.getFlowInstanceEventAttributes(externalMessage));

						systemInterface.getEventHandler().sendEvent(FlowInstance.class, new ExternalMessageAddedEvent(flowInstance, flowInstanceEvent, flowAdminModule.getSiteProfile(flowInstance), externalMessage, SenderType.MANAGER), EventTarget.ALL);
						systemInterface.getEventHandler().sendEvent(ExternalMessage.class, new CRUDEvent<ExternalMessage>(CRUDAction.ADD, externalMessage), EventTarget.ALL);
					}

					redirectToDefaultMethod(req, res);
					return null;
				}
			}
		}

		log.info("User " + user + " requested activity progress " + activityProgress);

		if (activity.getDescription() != null) {
			activity.setDescription(AttributeTagUtils.replaceTags(activity.getDescription(), flowInstance.getAttributeHandler()));
		}

		Document doc = createDocument(req, uriParser, user);

		Element showActivity = XMLUtils.appendNewElement(doc, doc.getDocumentElement(), "ShowActivity");

		showActivity.appendChild(activityProgress.toXML(doc));
		showActivity.appendChild(flowInstance.toXML(doc));

		if (activityProgress.getActivity().getShortDescription() != null) {
			
			XMLUtils.appendNewElement(doc, showActivity, "ShortDescription", AttributeTagUtils.replaceTags(activityProgress.getActivity().getShortDescription(), flowInstance.getAttributeHandler()));
		}
		
		if (activity.isShowFlowInstance()) {
			try {
				List<ManagerResponse> managerResponses = instanceManager.getFullShowHTML(req, user, this, true, null, getImmutableQueryRequestBaseURL(req, instanceManager), new RequestMetadata(false));
				XMLUtils.append(doc, showActivity, "ManagerResponses", managerResponses);
				
			} catch (UnableToGetQueryInstanceShowHTMLException e) {
				log.error("Unable to preview flow instance " + instanceManager, e);
			}
		}
		
		if (validationErrors != null) {

			showActivity.appendChild(RequestUtils.getRequestParameters(req, doc));
			XMLUtils.append(doc, showActivity, "ValidationErrors", validationErrors);
		}

		return new SimpleForegroundModuleResponse(doc, moduleDescriptor.getName(), getDefaultBreadcrumb());
	}
	
	@RESTMethod(alias = "signactivity/{activityProgressID}", method = { "get", "post" }, requireLogin = true)
	public ForegroundModuleResponse signActivity(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser, @URIParam(name = "activityProgressID") Integer activityProgressID) throws Exception {
		
		FlowApprovalActivityProgress activityProgress = activityProgressDAOWrapper.get(activityProgressID);

		if (activityProgress == null) {
			throw new URINotFoundException(uriParser);
		}
		
		if (genericSigningProvider == null) {
			throw new ModuleConfigurationException("No signing provider");
		}

		FlowApprovalActivity activity = activityProgress.getActivity();

		FlowInstance flowInstance = flowAdminModule.getFlowInstance(activityProgress.getActivityRound().getFlowInstanceID());

		if (!AccessUtils.checkAccess(user, activityProgress)) {

			try {
				FlowInstanceAdminModule.GENERAL_ACCESS_CONTROLLER.checkManagerAccess(flowInstance, user);

			} catch (AccessDeniedException e) {

				throw new AccessDeniedException("User does not have access to activity " + activity + " nor is a manager for " + flowInstance);
			}
		}
		
		if (!flowInstance.getFlow().isEnabled()) {
			
			return listPending(req, res, user, uriParser, Collections.singletonList(BaseFlowModule.FLOW_DISABLED_VALIDATION_ERROR));
		}
		
		if (!activityProgress.isMutable()) {
			
			log.warn("Activity " + activityProgress + " is not in a mutable state");
			
			return showActivity(req, res, user, uriParser, activityProgressID);
		}
		
		log.info("User " + user + " viewing sign form for " + activityProgress);
		
		return showSignForm(req, res, user, uriParser, activityProgress, flowInstance, null);
	}
	
	private String getDataToSign(FlowApprovalActivityProgress activityProgress, FlowInstance flowInstance, User user) {

		String shortDescription = null;
		String description = null;
		
		if (activityProgress.getActivity().getShortDescription() != null) {
			shortDescription = AttributeTagUtils.replaceTags(activityProgress.getActivity().getShortDescription(), flowInstance.getAttributeHandler());
		}
		
		if (activityProgress.getActivity().getDescription() != null) {
			description = AttributeTagUtils.replaceTags(activityProgress.getActivity().getDescription(), flowInstance.getAttributeHandler());
		}
		
		StringBuilder builder = new StringBuilder();
		
		builder.append(i18nFlow + ": " + flowInstance.getFlow().getName() + "\r\n");
		builder.append(i18nFlowInstanceID + ": " + flowInstance.getFlowInstanceID() + "\r\n");
		builder.append(i18nActivity + ": " + activityProgress.getActivity().getName() + " (ID " + activityProgress.getActivityProgressID() + ")\r\n");
		builder.append(i18nActivityProgressState + ": " + getActivityProgressState(activityProgress) + "\r\n");
		builder.append(i18nSigningUser + ": " + user.getFirstname() + " " + user.getLastname() + " (" + CitizenIdentifierUtils.getUserOrManagerCitizenIdentifier(user) + ")\r\n");
		builder.append(i18nActivityShortDescription + ": " + (shortDescription == null ? "" : shortDescription) + "\r\n");
		builder.append(i18nActivityDescription + ": " + (description == null ? "" : description) + "\r\n");
		
		if (activityProgress.getActivity().isShowFlowInstance()) {
			builder.append(i18nFlowInstanceWasVisible + "\r\n");
		}
		
		builder.append(i18nActivityProgressComment + ": " + (activityProgress.getComment() == null ? "" : activityProgress.getComment()));
		
		return builder.toString();
	}
	
	private String getActivityProgressState(FlowApprovalActivityProgress activityProgress) {
		
		if (activityProgress.isDenied()) {
			
			if (activityProgress.getActivity().getActivityGroup().getDeniedText() != null) {
				
				return activityProgress.getActivity().getActivityGroup().getDeniedText();

			} else {
				
				return i18nActivityProgressDenied;
			}
			
		} else {

			if (activityProgress.getActivity().getActivityGroup().getApprovedText() != null) {
				
				return activityProgress.getActivity().getActivityGroup().getApprovedText();

			} else {
				
				return i18nActivityProgressApproved;
			}
		}
	}
	
	private SimpleSigningRequest getSigningRequest(String dataToSign, FlowApprovalActivityProgress activityProgress, FlowInstance flowInstance, User signer, URIParser uriParser) throws IOException {
		
		String dataHash = HashUtils.hash(dataToSign, HashAlgorithms.SHA1);
		
		String description = signingMessage.replace("$activityProgress.ID", activityProgress.getActivityProgressID() + "");
		description = description.replace("$activity.name", activityProgress.getActivity().getName());
		description = description.replace("$activitygroup.name", activityProgress.getActivity().getActivityGroup().getName());
		description = description.replace("$flowInstance.ID", flowInstance.getFlowInstanceID().toString());
		description = description.replace("$flow.name", flowInstance.getFlow().getName());
		description = description.replace("$state", getActivityProgressState(activityProgress));
		description = description.replace("$hash", dataHash);
		
		String signingFormURL = uriParser.getFullContextPath() + getFullAlias() + "/sign/" + activityProgress.getActivityProgressID();
		String processSigningURL = uriParser.getFullContextPath() + getFullAlias() + "/processsign/" + activityProgress.getActivityProgressID() + "?signreq=1";
		
		return new SimpleSigningRequest(description, dataToSign, signingFormURL, processSigningURL);
	}
	
	private ForegroundModuleResponse showSignForm(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser, FlowApprovalActivityProgress activityProgress, FlowInstance flowInstance, List<ValidationError> validationErrors) throws Exception {
		
		Document doc = createDocument(req, uriParser, user);
		
		Element signingFormElement = XMLUtils.appendNewElement(doc, doc.getDocumentElement(), "Signing");
		
		String dataToSign = getDataToSign(activityProgress, flowInstance, user);
		SimpleSigningRequest signingRequest = getSigningRequest(dataToSign, activityProgress, flowInstance, user, uriParser);

		ViewFragment viewFragment = genericSigningProvider.showSignForm(req, res, user, signingRequest, validationErrors);
		
		signingFormElement.appendChild(activityProgress.toXML(doc));
		signingFormElement.appendChild(viewFragment.toXML(doc));
		signingFormElement.appendChild(flowInstance.toXML(doc));
		
		FragmentLinkScriptFilter.addViewFragment(viewFragment, req);
		
		SimpleForegroundModuleResponse response = new SimpleForegroundModuleResponse(doc, moduleDescriptor.getName(), getDefaultBreadcrumb());
		ViewFragmentUtils.appendLinksAndScripts(response, viewFragment);
		
		return response;
	}
	
	@RESTMethod(alias = "processsign/{activityProgressID}", method = { "get", "post" }, requireLogin = true)
	public ForegroundModuleResponse processSigning(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser, @URIParam(name = "activityProgressID") Integer activityProgressID) throws Exception {
		
		FlowApprovalActivityProgress activityProgress = activityProgressDAOWrapper.get(activityProgressID);

		if (activityProgress == null) {
			throw new URINotFoundException(uriParser);
		}
		
		if (genericSigningProvider == null) {
			throw new ModuleConfigurationException("No signing provider");
		}

		FlowApprovalActivity activity = activityProgress.getActivity();
		FlowApprovalActivityGroup activityGroup = activity.getActivityGroup();

		FlowInstance flowInstance = flowAdminModule.getFlowInstance(activityProgress.getActivityRound().getFlowInstanceID());

		if (!AccessUtils.checkAccess(user, activityProgress)) {

			try {
				FlowInstanceAdminModule.GENERAL_ACCESS_CONTROLLER.checkManagerAccess(flowInstance, user);

			} catch (AccessDeniedException e) {

				throw new AccessDeniedException("User does not have access to activity " + activity + " nor is a manager for " + flowInstance);
			}
		}
		
		if (!flowInstance.getFlow().isEnabled()) {
			
			return listPending(req, res, user, uriParser, Collections.singletonList(BaseFlowModule.FLOW_DISABLED_VALIDATION_ERROR));
		}
		
		if (!activityProgress.isMutable()) {
			
			log.warn("Activity " + activityProgress + " is not in a mutable state");
			
			return showActivity(req, res, user, uriParser, activityProgressID);
		}

		log.info("User " + user + " processing singing for " + activityProgress);
		
		List<ValidationError> validationErrors = null;
		
		try {
			String dataToSign = getDataToSign(activityProgress, flowInstance, user);
			SimpleSigningRequest signingRequest = getSigningRequest(dataToSign, activityProgress, flowInstance, user, uriParser);
			
			SigningResponse signingResponse = genericSigningProvider.processSigning(req, res, user, signingRequest);
			
			if (res.isCommitted()) {
				return null;
			}
			
			if (signingResponse != null) {
				
				if (user == null) {
					
					user = signingResponse.getUser();
				}
				
				log.info("User " + user + " signed " + activityProgress);

				Timestamp now = TimeUtils.getCurrentTimestamp();

				StringBuilder signatureData = new StringBuilder();

				if (signingResponse.getSigningAttributes() != null) {

					for (Entry<String, String> entry : signingResponse.getSigningAttributes().entrySet()) {

						if (entry.getKey().equals("signingChecksum")) {
							
							activityProgress.setSignedChecksum(entry.getValue());
							
						} else {
							
							signatureData.append(entry.getKey() + " = " + entry.getValue() + "\n");
						}
					}
				}
				
				if (!signingRequest.getHashToSign().equals(activityProgress.getSignedChecksum())) {
					throw new RuntimeException("Mismatching checksums for " + activityProgress + ": " + signingRequest.getHashToSign() + " != " + activityProgress.getSignedChecksum());
				}

				activityProgress.setSigningData(dataToSign);
				activityProgress.setCompleted(now);
				activityProgress.setCompletingUser(user);
				
				activityProgress.setSignedDate(now);
				activityProgress.setSignatureData(signatureData.toString());

				activityProgressDAO.update(activityProgress);
				
				//TODO show error if next status was not found
				approvalAdminModule.checkApprovalCompletion(activityProgress.getActivity().getActivityGroup(), flowInstance);
					
				if (activityGroup.isAppendCommentsToExternalMessages() && flowInstance.isExternalMessagesEnabled() && !StringUtils.isEmpty(activityProgress.getComment()) && flowInstance.getOwners() != null) {

					log.info("Copying comment to external messages for flowinstance " + flowInstance);

					ExternalMessage externalMessage = new ExternalMessage();
					externalMessage.setFlowInstance(flowInstance);
					externalMessage.setPoster(user);
					externalMessage.setMessage(activity.getName() + ":\r" + activityProgress.getComment());
					externalMessage.setAdded(TimeUtils.getCurrentTimestamp());
					externalMessage.setAttachments(null);
					externalMessage.setPostedByManager(true);

					flowAdminModule.getDAOFactory().getExternalMessageDAO().add(externalMessage);

					FlowInstanceEvent flowInstanceEvent = flowAdminModule.getFlowInstanceEventGenerator().addFlowInstanceEvent(flowInstance, EventType.MANAGER_MESSAGE_SENT, null, user, null, ExternalMessageUtils.getFlowInstanceEventAttributes(externalMessage));

					systemInterface.getEventHandler().sendEvent(FlowInstance.class, new ExternalMessageAddedEvent(flowInstance, flowInstanceEvent, flowAdminModule.getSiteProfile(flowInstance), externalMessage, SenderType.MANAGER), EventTarget.ALL);
					systemInterface.getEventHandler().sendEvent(ExternalMessage.class, new CRUDEvent<ExternalMessage>(CRUDAction.ADD, externalMessage), EventTarget.ALL);
				}

				redirectToDefaultMethod(req, res);
				return null;
				
			} else {
				
				log.warn("Signing failed");
			}
			
		} catch (ValidationException e) {
			
			validationErrors = new ArrayList<ValidationError>();
			validationErrors.addAll(e.getErrors());
		}
		
		return showSignForm(req, res, user, uriParser, activityProgress, flowInstance, validationErrors);
	}

	public Document createDocument(HttpServletRequest req, URIParser uriParser, User user) {

		Document doc = XMLUtils.createDomDocument();
		Element document = doc.createElement("Document");
		document.appendChild(RequestUtils.getRequestInfoAsXML(doc, req, uriParser));
		document.appendChild(this.moduleDescriptor.toXML(doc));
		doc.appendChild(document);
		return doc;
	}

	@Override
	public AccessInterface getAccessInterface() {
		return moduleDescriptor;
	}

	@Override
	public String getUserMenuAlias() {
		return getFullAlias();
	}

	@Override
	public ExtensionLink getUserMenuExtensionLink() {
		return userMenuLink;
	}

	@WebPublic(alias = "icon")
	public ForegroundModuleResponse getFlowIcon(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws SQLException, URINotFoundException {

		return flowAdminModule.getFlowIcon(req, res, user, uriParser);
	}
	
	protected String getImmutableQueryRequestBaseURL(HttpServletRequest req, FlowInstanceManager instanceManager) {
		
		return req.getContextPath() + getFullAlias() + "/iquery/" + instanceManager.getFlowID() + "/" + instanceManager.getFlowInstanceID() + "/q/";
	}
	
	@WebPublic(alias = "iquery")
	public ForegroundModuleResponse processImmutableQueryRequest(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws ModuleConfigurationException, SQLException, AccessDeniedException, IOException, FlowDefaultStatusNotFound, EvaluationException, URINotFoundException, QueryRequestException, QueryProviderException, EvaluationProviderException, InvalidFlowInstanceStepException, MissingQueryInstanceDescriptor, DuplicateFlowInstanceManagerIDException {
		
		return flowAdminModule.processImmutableQueryRequest(req, res, user, uriParser, new FlowApprovalFlowInstanceAccessController(this), true, false);
	}
	
	@Override
	public QueryHandler getQueryHandler() {
		
		return flowAdminModule.getQueryHandler();
	}
}
