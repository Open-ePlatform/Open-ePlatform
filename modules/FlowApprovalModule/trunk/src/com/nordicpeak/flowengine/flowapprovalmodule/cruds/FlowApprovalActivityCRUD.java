package com.nordicpeak.flowengine.flowapprovalmodule.cruds;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import se.unlogic.hierarchy.core.beans.Group;
import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.hierarchy.core.enums.CRUDAction;
import se.unlogic.hierarchy.core.exceptions.AccessDeniedException;
import se.unlogic.hierarchy.core.exceptions.URINotFoundException;
import se.unlogic.hierarchy.core.interfaces.ForegroundModuleResponse;
import se.unlogic.hierarchy.core.utils.crud.ModularCRUD;
import se.unlogic.standardutils.collections.CollectionUtils;
import se.unlogic.standardutils.dao.CRUDDAO;
import se.unlogic.standardutils.populators.IntegerPopulator;
import se.unlogic.standardutils.validation.ValidationError;
import se.unlogic.standardutils.validation.ValidationErrorType;
import se.unlogic.standardutils.validation.ValidationException;
import se.unlogic.standardutils.xml.XMLUtils;
import se.unlogic.webutils.http.URIParser;
import se.unlogic.webutils.populators.annotated.AnnotatedRequestPopulator;
import se.unlogic.webutils.validation.ValidationUtils;

import com.nordicpeak.flowengine.beans.Flow;
import com.nordicpeak.flowengine.flowapprovalmodule.FlowApprovalAdminModule;
import com.nordicpeak.flowengine.flowapprovalmodule.beans.FlowApprovalActivity;
import com.nordicpeak.flowengine.flowapprovalmodule.beans.FlowApprovalActivityGroup;
import com.nordicpeak.flowengine.flowapprovalmodule.beans.FlowApprovalActivityResponsibleUser;
import com.nordicpeak.flowengine.populators.FlowAdminFragmentExtensionViewCRUDIDParser;

public class FlowApprovalActivityCRUD extends ModularCRUD<FlowApprovalActivity, Integer, User, FlowApprovalAdminModule> {

	public FlowApprovalActivityCRUD(CRUDDAO<FlowApprovalActivity, Integer> crudDAO, FlowApprovalAdminModule callback) {

		super(FlowAdminFragmentExtensionViewCRUDIDParser.getInstance(), crudDAO, new AnnotatedRequestPopulator<FlowApprovalActivity>(FlowApprovalActivity.class), "Activity", "activity", "", callback);
	}

	@Override
	protected void checkAddAccess(User user, HttpServletRequest req, URIParser uriParser) throws AccessDeniedException, URINotFoundException, SQLException {

		Flow flow = (Flow) req.getAttribute("flow");

		if (flow == null) {

			throw new URINotFoundException(uriParser);
		}

		FlowApprovalActivityGroup activityGroup = null;

		Integer activityGroupID = idParser.getBeanID(uriParser, req, null);

		if (activityGroupID != null) {

			activityGroup = callback.getActivityGroup(activityGroupID);
		}

		if (activityGroup == null || !activityGroup.getFlowFamilyID().equals(flow.getFlowFamily().getFlowFamilyID())) {

			throw new URINotFoundException(uriParser);
		}

		req.setAttribute("activityGroup", activityGroup);
	}

	@Override
	protected void checkUpdateAccess(FlowApprovalActivity activity, User user, HttpServletRequest req, URIParser uriParser) throws AccessDeniedException, URINotFoundException, SQLException {

		Flow flow = (Flow) req.getAttribute("flow");

		if (flow == null) {

			throw new URINotFoundException(uriParser);
		}
	}

	@Override
	protected void checkDeleteAccess(FlowApprovalActivity activity, User user, HttpServletRequest req, URIParser uriParser) throws AccessDeniedException, URINotFoundException, SQLException {

		Flow flow = (Flow) req.getAttribute("flow");

		if (flow == null) {

			throw new URINotFoundException(uriParser);
		}
	}
	
	@Override
	protected void appendShowFormData(FlowApprovalActivity activity, Document doc, Element showTypeElement, User user, HttpServletRequest req, HttpServletResponse res, URIParser uriParser) throws SQLException, IOException, Exception {
		super.appendShowFormData(activity, doc, showTypeElement, user, req, res, uriParser);

		appendFormData(doc, showTypeElement, user, req, uriParser);
	}

	@Override
	protected void appendAddFormData(Document doc, Element addTypeElement, User user, HttpServletRequest req, URIParser uriParser) throws Exception {
		super.appendAddFormData(doc, addTypeElement, user, req, uriParser);

		FlowApprovalActivityGroup activityGroup = (FlowApprovalActivityGroup) req.getAttribute("activityGroup");

		addTypeElement.appendChild(activityGroup.toXML(doc));

		appendFormData(doc, addTypeElement, user, req, uriParser);
	}

	@Override
	protected void appendUpdateFormData(FlowApprovalActivity activity, Document doc, Element updateTypeElement, User user, HttpServletRequest req, URIParser uriParser) throws Exception {
		super.appendUpdateFormData(activity, doc, updateTypeElement, user, req, uriParser);

		appendFormData(doc, updateTypeElement, user, req, uriParser);
	}

	protected void appendFormData(Document doc, Element typeElement, User user, HttpServletRequest req, URIParser uriParser) throws SQLException, IOException, Exception {

		typeElement.appendChild(((Flow) req.getAttribute("flow")).toXML(doc));
		XMLUtils.appendNewElement(doc, typeElement, "extensionRequestURL", req.getAttribute("extensionRequestURL"));
	}

	@Override
	protected void validateAddPopulation(FlowApprovalActivity activity, HttpServletRequest req, User user, URIParser uriParser) throws ValidationException, SQLException, Exception {
		super.validateAddPopulation(activity, req, user, uriParser);

		activity.setActivityGroup((FlowApprovalActivityGroup) req.getAttribute("activityGroup"));

		validatePopulation(activity, req, user, uriParser);
	}

	@Override
	protected void validateUpdatePopulation(FlowApprovalActivity activity, HttpServletRequest req, User user, URIParser uriParser) throws ValidationException, SQLException, Exception {
		super.validateUpdatePopulation(activity, req, user, uriParser);

		validatePopulation(activity, req, user, uriParser);
	}

	protected void validatePopulation(FlowApprovalActivity activity, HttpServletRequest req, User user, URIParser uriParser) throws ValidationException, SQLException, Exception {

		List<ValidationError> validationErrors = new ArrayList<ValidationError>();

		List<Integer> responsibleUserIDs = ValidationUtils.validateParameters("responsible-user", req, false, IntegerPopulator.getPopulator(), validationErrors);

		if (responsibleUserIDs == null) {

			activity.setResponsibleUsers(null);

		} else {

			List<FlowApprovalActivityResponsibleUser> responsibleUsers = new ArrayList<>(responsibleUserIDs.size());

			for (Integer userID : responsibleUserIDs) {

				User responsibleUser = callback.getUserHandler().getUser(userID, false, false);

				if (responsibleUser != null) {

					responsibleUsers.add(new FlowApprovalActivityResponsibleUser(responsibleUser, "true".equals(req.getParameter("responsible-user-fallback" + userID))));

				} else {

					validationErrors.add(new ValidationError("responsible-user", ValidationErrorType.InvalidFormat));
				}
			}

			activity.setResponsibleUsers(responsibleUsers);
		}

		List<Integer> responsibleGroupIDs = ValidationUtils.validateParameters("responsibleGroup", req, false, IntegerPopulator.getPopulator(), validationErrors);

		if (responsibleGroupIDs == null) {

			activity.setResponsibleGroups(null);

		} else {

			List<Group> responsibleGroups = new ArrayList<Group>(responsibleGroupIDs.size());

			for (Integer groupID : responsibleGroupIDs) {

				Group group = callback.getGroupHandler().getGroup(groupID, false);

				if (group != null) {

					responsibleGroups.add(group);

				} else {

					validationErrors.add(new ValidationError("responsibleGroup", ValidationErrorType.InvalidFormat));
				}
			}

			activity.setResponsibleGroups(responsibleGroups);
		}

		if (activity.getResponsibleUserAttributeName() != null) {
			
			if (CollectionUtils.isEmpty(activity.getResponsibleUsers()) && CollectionUtils.isEmpty(activity.getResponsibleGroups())) {
				
				validationErrors.add(new ValidationError("ResponsibleFallbackRequired"));
			}
			
		} else {
			
			if (CollectionUtils.isEmpty(activity.getResponsibleUsers()) && CollectionUtils.isEmpty(activity.getResponsibleGroups())) {
				
				validationErrors.add(new ValidationError("ResponsibleRequired"));
			}
		}
		

		if (!validationErrors.isEmpty()) {
			throw new ValidationException(validationErrors);
		}
	}

	@Override
	protected ForegroundModuleResponse beanAdded(FlowApprovalActivity activity, HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Exception {

		callback.addFlowFamilyEvent(callback.getEventActivityAddedMessage() + " \"" + activity.getName() + "\"", ((Flow) req.getAttribute("flow")).getFlowFamily(), user);

		return beanEvent(activity, req, res, CRUDAction.ADD);
	}

	@Override
	protected ForegroundModuleResponse beanUpdated(FlowApprovalActivity activity, HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Exception {

		callback.addFlowFamilyEvent(callback.getEventActivityUpdatedMessage() + " \"" + activity.getName() + "\"", ((Flow) req.getAttribute("flow")).getFlowFamily(), user);

		return beanEvent(activity, req, res, CRUDAction.UPDATE);
	}

	@Override
	protected ForegroundModuleResponse beanDeleted(FlowApprovalActivity activity, HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Exception {

		callback.addFlowFamilyEvent(callback.getEventActivityDeletedMessage() + " \"" + activity.getName() + "\"", ((Flow) req.getAttribute("flow")).getFlowFamily(), user);

		return beanEvent(activity, req, res, CRUDAction.DELETE);
	}

	private ForegroundModuleResponse beanEvent(FlowApprovalActivity activity, HttpServletRequest req, HttpServletResponse res, CRUDAction action) throws IOException {

//		callback.getEventHandler().sendEvent(FlowApprovalActivity.class, new CRUDEvent<FlowApprovalActivity>(action, activity), EventTarget.ALL);

		res.sendRedirect(req.getContextPath() + req.getAttribute("extensionRequestURL") + "/showactivitygroup/" + activity.getActivityGroup().getActivityGroupID());
		return null;
	}

	@Override
	public ForegroundModuleResponse list(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser, List<ValidationError> validationErrors) throws Exception {

		return callback.list((String) req.getAttribute("extensionRequestURL"), (Flow) req.getAttribute("flow"), req, res, user, uriParser, validationErrors);
	}

}
