package com.nordicpeak.flowengine.flowapprovalmodule;

import java.io.File;
import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import se.unlogic.hierarchy.core.annotations.CheckboxSettingDescriptor;
import se.unlogic.hierarchy.core.annotations.InstanceManagerDependency;
import se.unlogic.hierarchy.core.annotations.ModuleSetting;
import se.unlogic.hierarchy.core.annotations.TextFieldSettingDescriptor;
import se.unlogic.hierarchy.core.annotations.XSLVariable;
import se.unlogic.hierarchy.core.beans.LinkTag;
import se.unlogic.hierarchy.core.beans.ScriptTag;
import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.hierarchy.core.exceptions.AccessDeniedException;
import se.unlogic.hierarchy.core.exceptions.URINotFoundException;
import se.unlogic.hierarchy.core.interfaces.ForegroundModuleResponse;
import se.unlogic.hierarchy.core.interfaces.SectionInterface;
import se.unlogic.hierarchy.core.interfaces.ViewFragment;
import se.unlogic.hierarchy.core.interfaces.modules.descriptors.ForegroundModuleDescriptor;
import se.unlogic.hierarchy.core.utils.HierarchyAnnotatedDAOFactory;
import se.unlogic.hierarchy.core.utils.ModuleViewFragmentTransformer;
import se.unlogic.hierarchy.core.utils.ViewFragmentModule;
import se.unlogic.hierarchy.core.utils.extensionlinks.ExtensionLink;
import se.unlogic.hierarchy.foregroundmodules.AnnotatedForegroundModule;
import se.unlogic.standardutils.collections.CollectionUtils;
import se.unlogic.standardutils.dao.AdvancedAnnotatedDAOWrapper;
import se.unlogic.standardutils.dao.AnnotatedDAO;
import se.unlogic.standardutils.dao.HighLevelQuery;
import se.unlogic.standardutils.dao.QueryParameterFactory;
import se.unlogic.standardutils.db.tableversionhandler.TableVersionHandler;
import se.unlogic.standardutils.db.tableversionhandler.UpgradeResult;
import se.unlogic.standardutils.db.tableversionhandler.XMLDBScriptProvider;
import se.unlogic.standardutils.io.FileUtils;
import se.unlogic.standardutils.numbers.NumberUtils;
import se.unlogic.standardutils.string.StringUtils;
import se.unlogic.standardutils.time.TimeUtils;
import se.unlogic.standardutils.validation.ValidationError;
import se.unlogic.standardutils.xml.XMLUtils;
import se.unlogic.webutils.http.HTTPUtils;
import se.unlogic.webutils.http.RequestUtils;
import se.unlogic.webutils.http.URIParser;
import se.unlogic.webutils.http.enums.ContentDisposition;

import com.nordicpeak.flowengine.FlowInstanceAdminModule;
import com.nordicpeak.flowengine.beans.FlowInstance;
import com.nordicpeak.flowengine.enums.EventType;
import com.nordicpeak.flowengine.flowapprovalmodule.beans.FlowApprovalActivity;
import com.nordicpeak.flowengine.flowapprovalmodule.beans.FlowApprovalActivityGroup;
import com.nordicpeak.flowengine.flowapprovalmodule.beans.FlowApprovalActivityProgress;
import com.nordicpeak.flowengine.flowapprovalmodule.beans.FlowApprovalActivityRound;
import com.nordicpeak.flowengine.flowapprovalmodule.beans.FlowApprovalReminder;
import com.nordicpeak.flowengine.flowapprovalmodule.beans.ReminderType;
import com.nordicpeak.flowengine.interfaces.FlowInstanceOverviewExtensionProvider;
import com.nordicpeak.flowengine.utils.FlowInstanceUtils;

public class FlowApprovalManagerModule extends AnnotatedForegroundModule implements FlowInstanceOverviewExtensionProvider, ViewFragmentModule<ForegroundModuleDescriptor> {

	@XSLVariable(prefix = "java.")
	protected String tabTitle;

	@ModuleSetting
	@TextFieldSettingDescriptor(name = "Title attribute name", description = "The name of the user attribute that contains their title", required = false)
	protected String titleAttribute = "title";
	
	@ModuleSetting
	@CheckboxSettingDescriptor(name = "Enable fragment XML debug", description = "Enables debugging of fragment XML")
	private boolean debugFragmententXML;

	private AnnotatedDAO<FlowApprovalActivityGroup> activityGroupDAO;
	private AnnotatedDAO<FlowApprovalActivityRound> activityRoundDAO;
	private AnnotatedDAO<FlowApprovalActivityProgress> activityProgressDAO;
	private AnnotatedDAO<FlowApprovalReminder> reminderDAO;

	private AdvancedAnnotatedDAOWrapper<FlowApprovalActivityProgress, Integer> activityProgressDAOWrapper;

	private QueryParameterFactory<FlowApprovalActivityRound, Integer> activityRoundFlowInstanceIDParamFactory;
	private QueryParameterFactory<FlowApprovalActivityRound, Integer> activityRoundIDParamFactory;
	private QueryParameterFactory<FlowApprovalActivityGroup, Integer> activityGroupFlowFamilyIDParamFactory;

	protected FlowInstanceAdminModule flowInstanceAdminModule;

	@InstanceManagerDependency
	protected FlowApprovalUserModule approvalUserModule;

	@InstanceManagerDependency(required = true)
	protected FlowApprovalAdminModule approvalAdminModule;

	private ModuleViewFragmentTransformer<ForegroundModuleDescriptor> viewFragmentTransformer;
	
	protected UserGroupListConnectorWithFilter userGroupListConnector;

	@Override
	public void init(ForegroundModuleDescriptor moduleDescriptor, SectionInterface sectionInterface, DataSource dataSource) throws Exception {

		super.init(moduleDescriptor, sectionInterface, dataSource);

		userGroupListConnector = new UserGroupListConnectorWithFilter(systemInterface, titleAttribute);
		userGroupListConnector.setOnlyEnabledUsers(true);
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

		activityGroupDAO = daoFactory.getDAO(FlowApprovalActivityGroup.class);
		activityRoundDAO = daoFactory.getDAO(FlowApprovalActivityRound.class);
		activityProgressDAO = daoFactory.getDAO(FlowApprovalActivityProgress.class);
		reminderDAO = daoFactory.getDAO(FlowApprovalReminder.class);

		activityProgressDAOWrapper = activityProgressDAO.getAdvancedWrapper(Integer.class);
		activityProgressDAOWrapper.getGetQuery().addRelations(FlowApprovalActivityProgress.ACTIVITY_ROUND_RELATION, FlowApprovalActivityProgress.ACTIVITY_RELATION, FlowApprovalActivity.ACTIVITY_GROUP_RELATION, FlowApprovalActivity.RESPONSIBLE_USERS_RELATION, FlowApprovalActivity.RESPONSIBLE_GROUPS_RELATION, FlowApprovalActivityProgress.ACTIVITY_REMINDER_RELATION);

		activityRoundIDParamFactory = activityRoundDAO.getParamFactory("activityRoundID", Integer.class);
		activityRoundFlowInstanceIDParamFactory = activityRoundDAO.getParamFactory("flowInstanceID", Integer.class);
		activityGroupFlowFamilyIDParamFactory = activityGroupDAO.getParamFactory("flowFamilyID", Integer.class);
	}

	@InstanceManagerDependency(required = true)
	public void setFlowInstanceAdminModule(FlowInstanceAdminModule flowInstanceAdminModule) {

		if (flowInstanceAdminModule != null) {

			flowInstanceAdminModule.addFlowInstanceOverviewExtensionProvider(this);

		} else {

			this.flowInstanceAdminModule.removeFlowInstanceOverviewExtensionProvider(this);
		}

		this.flowInstanceAdminModule = flowInstanceAdminModule;
	}

	@Override
	protected void moduleConfigured() throws Exception {

		super.moduleConfigured();

		try {
			viewFragmentTransformer = new ModuleViewFragmentTransformer<>(sectionInterface.getForegroundModuleXSLTCache(), this, systemInterface.getEncoding());
			viewFragmentTransformer.setDebugXML(debugFragmententXML);

		} catch (Exception e) {

			log.error("Unable to parse XSL stylesheet for tab contents in module " + this.moduleDescriptor, e);
		}
	}

	@Override
	public void unload() throws Exception {

		if (flowInstanceAdminModule != null) {

			flowInstanceAdminModule.removeFlowInstanceOverviewExtensionProvider(this);
		}

		super.unload();
	}

	@Override
	public ExtensionLink getOverviewTabHeaderExtensionLink(FlowInstance flowInstance, HttpServletRequest req, URIParser uriParser, User user) throws Exception {

		return new ExtensionLink(tabTitle, "#flow-approval", null, null);
	}

	@Override
	public ViewFragment getOverviewTabContentsViewFragment(FlowInstance flowInstance, HttpServletRequest req, URIParser uriParser, User user) throws Exception {

		HighLevelQuery<FlowApprovalActivityGroup> query = new HighLevelQuery<>();

		query.addParameter(activityGroupFlowFamilyIDParamFactory.getParameter(flowInstance.getFlow().getFlowFamily().getFlowFamilyID()));

		if (activityGroupDAO.getCount(query) > 0) {

			return getTabContentsViewFragment(flowInstance, req, uriParser, user, null);
		}

		return null;
	}

	private ViewFragment getTabContentsViewFragment(FlowInstance flowInstance, HttpServletRequest req, URIParser uriParser, User user, List<ValidationError> validationErrors) throws Exception {

		Document doc = createDocument(req, uriParser, user);
		Element documentElement = doc.getDocumentElement();

		Element tabElement = XMLUtils.appendNewElement(doc, documentElement, "TabContents");

		List<FlowApprovalActivityGroup> activityGroups = getActivityGroups(flowInstance, FlowApprovalActivity.RESPONSIBLE_USERS_RELATION, FlowApprovalActivity.RESPONSIBLE_GROUPS_RELATION, FlowApprovalActivityProgress.ACTIVITY_REMINDER_RELATION);

		XMLUtils.append(doc, tabElement, "ActivityGroups", activityGroups);

		XMLUtils.appendNewElement(doc, tabElement, "PostURL", RequestUtils.getFullContextPathURL(req) + flowInstanceAdminModule.getOverviewExtensionRequestMethodAlias(flowInstance, getOverviewExtensionProviderID()));

		if (approvalUserModule != null) {
			XMLUtils.appendNewElement(doc, tabElement, "UserModuleURL", RequestUtils.getFullContextPathURL(req) + approvalUserModule.getFullAlias());
		}

		XMLUtils.append(doc, tabElement, validationErrors);

		return viewFragmentTransformer.createViewFragment(doc);
	}

	@Override
	public ForegroundModuleResponse processOverviewExtensionRequest(FlowInstance flowInstance, HttpServletRequest req, HttpServletResponse res, URIParser uriParser, User user) throws Exception {

		Integer sendReminderForActivityProgressID = NumberUtils.toInt(req.getParameter("sendreminder"));
		Integer signatureActivityProgressID = NumberUtils.toInt(req.getParameter("signature"));
		Integer signatureActivityRoundID = NumberUtils.toInt(req.getParameter("signatures"));
		Integer assignOwnerActivityProgressID = NumberUtils.toInt(req.getParameter("assignowner"));
		Integer assignOwnerSearchActivityProgressID = NumberUtils.toInt(req.getParameter("assignownersearch"));

		if (sendReminderForActivityProgressID != null) {

			if (!HTTPUtils.isPost(req)) {
				
				throw new AccessDeniedException("Sending reminder using method " + req.getMethod() + " is not allowed.");
			}
			
			FlowApprovalActivityProgress activityProgress = activityProgressDAOWrapper.get(sendReminderForActivityProgressID);

			if (activityProgress == null) {
				throw new URINotFoundException(uriParser);
			}

			if (!activityProgress.getActivityRound().getFlowInstanceID().equals(flowInstance.getFlowInstanceID())) {
				throw new AccessDeniedException("Wrong flow instance");
			}

			//TODO don't allow new reminder if reminded very recently

			FlowApprovalActivityRound round = activityProgress.getActivityRound();

			if (round.getCompleted() == null && round.getCancelled() == null && activityProgress.getCompleted() == null) {

				approvalAdminModule.sendActivityGroupStartedNotifications(Collections.singletonMap(activityProgress.getActivity(), activityProgress), activityProgress.getActivity().getActivityGroup(), flowInstance, true);
				
				FlowApprovalReminder reminder = new FlowApprovalReminder(activityProgress, TimeUtils.getCurrentTimestamp(), ReminderType.MANUAL, user);

				reminderDAO.add(reminder);
			}
			
			//TODO show notification that reminder was sent

		} else if (signatureActivityProgressID != null) {

			FlowApprovalActivityProgress activityProgress = activityProgressDAOWrapper.get(signatureActivityProgressID);

			if (activityProgress == null || activityProgress.getSignedDate() == null) {
				throw new URINotFoundException(uriParser);
			}

			if (!activityProgress.getActivityRound().getFlowInstanceID().equals(flowInstance.getFlowInstanceID())) {
				throw new AccessDeniedException("Wrong flow instance");
			}

			String eventSignature = activityProgress.getSigningData() + "\r\n\r\n" + activityProgress.getSignatureData();

			log.info("Sending signature for " + activityProgress + " to user " + user);

			String filename = flowInstance.getFlow().getName() + " - " + flowInstance.getFlowInstanceID() + " - " + activityProgress.getActivity().getName() + " - " + approvalAdminModule.getSignaturesFilename() + " - " + activityProgress.getActivityProgressID() + ".txt";

			res.setHeader("Content-Disposition", ContentDisposition.ATTACHMENT + ";" + FileUtils.toContentDispositionFilename(filename));
			res.setContentType("text/plain");

			res.getWriter().write(eventSignature);
			res.getWriter().close();
			return null;

		} else if (signatureActivityRoundID != null) {

			HighLevelQuery<FlowApprovalActivityRound> query = new HighLevelQuery<>();
			query.addRelation(FlowApprovalActivityRound.ACTIVITY_GROUP_RELATION);
			query.addParameter(activityRoundIDParamFactory.getParameter(signatureActivityRoundID));

			FlowApprovalActivityRound round = activityRoundDAO.get(query);

			if (round == null) {
				
				log.warn("FlowApprovalActivityRound with ID " + signatureActivityRoundID + " not found");
				throw new URINotFoundException(uriParser);
			}

			if (!round.getFlowInstanceID().equals(flowInstance.getFlowInstanceID())) {
				throw new AccessDeniedException("Wrong flow instance");
			}

			File pdfFile = approvalAdminModule.getSignaturesPDF(round);

			if (pdfFile == null || !pdfFile.exists()) {

				log.warn("PDF for " + round + " not found");
				throw new URINotFoundException(uriParser);
			}

			try {
				log.info("Sending signature PDF for " + round + " to user " + user);

				String filename = flowInstance.getFlow().getName() + " - " + flowInstance.getFlowInstanceID() + " - " + round.getActivityGroup().getName() + " - signature - " + round.getActivityRoundID() + ".pdf";

				HTTPUtils.sendFile(pdfFile, filename, req, res, ContentDisposition.ATTACHMENT);

			} catch (Exception e) {
				log.info("Error sending PDF for " + round + " to user " + user + ", " + e);
			}

			return null;
			
		} else if (assignOwnerActivityProgressID != null) {
			
			if (!HTTPUtils.isPost(req)) {
				
				throw new AccessDeniedException("Assigning owner using method " + req.getMethod() + " is not allowed.");
			}
			
			FlowApprovalActivityProgress activityProgress = activityProgressDAOWrapper.get(assignOwnerActivityProgressID);

			if (activityProgress == null) {
				log.warn("ActivityProgress with ID " + signatureActivityProgressID + " not found");
				throw new URINotFoundException(uriParser);
			}

			if (!activityProgress.getActivityRound().getFlowInstanceID().equals(flowInstance.getFlowInstanceID())) {
				throw new AccessDeniedException("Wrong flow instance");
			}
			
			FlowApprovalActivityRound round = activityProgress.getActivityRound();
			FlowApprovalActivity activity = activityProgress.getActivity();
			FlowApprovalActivityGroup activityGroup = activity.getActivityGroup();

			if (round.getCompleted() != null || round.getCancelled() != null || activityProgress.getCompleted() != null) {
				throw new AccessDeniedException("ActivityProgress " + activityProgress + " is not in a mutable state");
			}
			
			if (!activity.isAllowManagersToAssignOwner() || activity.getResponsibleUserAttributeNames() == null) {
				throw new AccessDeniedException("ActivityProgress " + activityProgress + " does not allow managers to assign owners");
			}
			
			String[] assignUserIDs = req.getParameterValues("assign-user");
			
			if (assignUserIDs != null) {
				
				List<User> users = new ArrayList<User>(assignUserIDs.length);
				
				for (String userIDString : assignUserIDs) {
					Integer userID = NumberUtils.toInt(userIDString);
					
					if (userID != null) {
						
						User assignUser = systemInterface.getUserHandler().getUser(userID, true, true);
						
						if (assignUser != null) {
							users.add(assignUser);
						}
					}
				}
				
				if (users.size() != assignUserIDs.length) {
					
					log.warn("User count != to user parameters: " + StringUtils.toCommaSeparatedString(users) + "  != " + assignUserIDs);
					throw new URINotFoundException(uriParser);
				}
				
				log.info("User " + user + " assigning activity progress ID  " + activityProgress.getActivityProgressID() + " from " + (activityProgress.getResponsibleAttributedUsers() != null ? StringUtils.toCommaSeparatedString(activityProgress.getResponsibleAttributedUsers()) : "null") + " to " + StringUtils.toCommaSeparatedString(users));
				
				String namesFrom = activityProgress.getResponsibleAttributedUsers() != null ? FlowInstanceUtils.getManagersString(activityProgress.getResponsibleAttributedUsers(), null) : approvalAdminModule.getEventActivityOwnerDefault();
				String eventMessage = approvalAdminModule.getEventActivityOwnerChanged().replace("$activity", activity.getName()).replace("$from", namesFrom).replace("$to", FlowInstanceUtils.getManagersString(users, null));
				
				activityProgress.setResponsibleAttributedUsers(users);
				activityProgressDAO.update(activityProgress);
				
				flowInstanceAdminModule.getFlowInstanceEventGenerator().addFlowInstanceEvent(flowInstance, EventType.OTHER_EVENT, eventMessage, user);
				
				if (activityGroup.isSendActivityGroupStartedEmail()) {

					approvalAdminModule.sendActivityAssignedNotifications(users, activityProgress, activity, activityGroup, flowInstance, false);
				}

			} else {

				List<User> responsibleUsers = approvalAdminModule.getResponsibleUsersFromAttribute(activity, flowInstance);

				boolean responsibleUsersChanged = CollectionUtils.getSize(responsibleUsers) != CollectionUtils.getSize(activityProgress.getResponsibleAttributedUsers());

				if (!responsibleUsersChanged && responsibleUsers != null) { // Same size but not nulls, compare contents

					Set<User> oldUsers = new HashSet<>(activityProgress.getResponsibleAttributedUsers());
					Set<User> newUsers = new HashSet<>(responsibleUsers);

					responsibleUsersChanged = !oldUsers.equals(newUsers);
				}

				if (responsibleUsersChanged) {

					String namesFromLog = activityProgress.getResponsibleAttributedUsers() != null ? FlowInstanceUtils.getManagersString(activityProgress.getResponsibleAttributedUsers(), null) : "null";
					String namesToLog = responsibleUsers != null ? FlowInstanceUtils.getManagersString(responsibleUsers, null) : "null";
					log.info("User " + user + " assigning activity progress ID " + activityProgress.getActivityProgressID() + " from " + namesFromLog + " to default " + namesToLog);
					
					String namesFrom = activityProgress.getResponsibleAttributedUsers() != null ? FlowInstanceUtils.getManagersString(activityProgress.getResponsibleAttributedUsers(), null) : approvalAdminModule.getEventActivityOwnerDefault();
					String namesTo = approvalAdminModule.getEventActivityOwnerDefault();
					
					if (responsibleUsers != null) {
						namesTo += " " + FlowInstanceUtils.getManagersString(responsibleUsers, null);
					}
					
					String eventMessage = approvalAdminModule.getEventActivityOwnerChanged().replace("$activity", activity.getName()).replace("$from", namesFrom).replace("$to", namesTo);
					
					activityProgress.setResponsibleAttributedUsers(responsibleUsers);
					activityProgressDAO.update(activityProgress);
					
					flowInstanceAdminModule.getFlowInstanceEventGenerator().addFlowInstanceEvent(flowInstance, EventType.OTHER_EVENT, eventMessage, user);
					
				} else {
					
					log.info("User " + user + " tried assigning activity progress ID " + activityProgress.getActivityProgressID() + " to default owners but is was already.");
				}
			}
			
		} else if (assignOwnerSearchActivityProgressID != null) {
			
			HighLevelQuery<FlowApprovalActivityProgress> query = new HighLevelQuery<>(FlowApprovalActivityProgress.ACTIVITY_ROUND_RELATION, FlowApprovalActivityProgress.ACTIVITY_RELATION, FlowApprovalActivity.ASSIGNABLE_USERS_RELATION, FlowApprovalActivity.ASSIGNABLE_GROUPS_RELATION);
			query.addParameter(activityProgressDAOWrapper.getParameterFactory().getParameter(assignOwnerSearchActivityProgressID));
			
			FlowApprovalActivityProgress activityProgress = activityProgressDAOWrapper.getAnnotatedDAO().get(query);

			if (activityProgress == null) {
				throw new URINotFoundException(uriParser);
			}

			if (!activityProgress.getActivityRound().getFlowInstanceID().equals(flowInstance.getFlowInstanceID())) {
				throw new AccessDeniedException("Wrong flow instance");
			}
			
			return userGroupListConnector.getUsers(activityProgress.getActivity().getAssignableUsers(), activityProgress.getActivity().getAssignableGroups(), req, res, user, uriParser);
		}
		
		res.sendRedirect(RequestUtils.getFullContextPathURL(req) + flowInstanceAdminModule.getFullAlias() + "/overview/" + flowInstance.getFlowInstanceID() + "#flow-approval");
		return null;
	}

	private List<FlowApprovalActivityGroup> getActivityGroups(FlowInstance flowInstance, Field... relations) throws SQLException {

		HighLevelQuery<FlowApprovalActivityGroup> query = new HighLevelQuery<>();

		query.addParameter(activityGroupFlowFamilyIDParamFactory.getParameter(flowInstance.getFlow().getFlowFamily().getFlowFamilyID()));
		query.addRelationParameter(FlowApprovalActivityRound.class, activityRoundFlowInstanceIDParamFactory.getParameter(flowInstance.getFlowInstanceID()));

		query.addRelations(FlowApprovalActivityGroup.ACTIVIY_ROUNDS_RELATION, FlowApprovalActivityRound.ACTIVITY_PROGRESSES_RELATION, FlowApprovalActivityProgress.ACTIVITY_RELATION);

		if (relations != null) {
			query.addRelations(relations);
			query.addCachedRelations(FlowApprovalActivityProgress.ACTIVITY_RELATION);
		}

		return activityGroupDAO.getAll(query);
	}

	private Document createDocument(HttpServletRequest req, URIParser uriParser, User user) {

		Document doc = XMLUtils.createDomDocument();
		Element document = doc.createElement("Document");
		document.appendChild(RequestUtils.getRequestInfoAsXML(doc, req, uriParser));
		document.appendChild(this.sectionInterface.getSectionDescriptor().toXML(doc));
		document.appendChild(this.moduleDescriptor.toXML(doc));

		doc.appendChild(document);

		return doc;
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
	public String getOverviewExtensionProviderID() {

		return moduleDescriptor.getModuleID().toString();
	}
}
