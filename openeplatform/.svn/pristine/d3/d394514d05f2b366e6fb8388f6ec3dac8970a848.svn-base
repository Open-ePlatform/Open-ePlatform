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
import com.nordicpeak.flowengine.populators.FlowAdminFragmentExtensionViewCRUDIDParser;

public class FlowApprovalActivityCRUD extends ModularCRUD<FlowApprovalActivity, Integer, User, FlowApprovalAdminModule> {

	public FlowApprovalActivityCRUD(CRUDDAO<FlowApprovalActivity, Integer> crudDAO, FlowApprovalAdminModule callback) {

		super(FlowAdminFragmentExtensionViewCRUDIDParser.getInstance(), crudDAO, new AnnotatedRequestPopulator<>(FlowApprovalActivity.class), "Activity", "activity", "", callback);
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

		List<ValidationError> validationErrors = new ArrayList<>();

		List<Integer> assignableUserIDs = ValidationUtils.validateParameters("assignable-user", req, false, IntegerPopulator.getPopulator(), validationErrors);
		List<Integer> responsibleUserIDs = ValidationUtils.validateParameters("responsible-user", req, false, IntegerPopulator.getPopulator(), validationErrors);
		
		//TODO Om ansvarig användare eller grupp inte söks via attribut så ska inte handläggare få byta ansvarig. Sätt alltså den till false i det fallet.
		
		List<User> assignableUsers = null;
		List<User> responsibleUsers = null;
		
		if (responsibleUserIDs != null) {

			responsibleUsers = new ArrayList<>(responsibleUserIDs.size());

			for (Integer userID : responsibleUserIDs) {

				User responsibleUser = callback.getUserHandler().getUser(userID, false, false);

				if (responsibleUser != null) {

					responsibleUsers.add(responsibleUser);

				} else {

					validationErrors.add(new ValidationError("responsible-user", ValidationErrorType.InvalidFormat));
				}
			}
		}

		if (assignableUserIDs != null) {

			assignableUsers = new ArrayList<>(assignableUserIDs.size());

			for (Integer userID : assignableUserIDs) {

				User assignableUser = callback.getUserHandler().getUser(userID, false, false);

				if (assignableUser != null) {

					assignableUsers.add(assignableUser);

				} else {

					validationErrors.add(new ValidationError("assignable-user", ValidationErrorType.InvalidFormat));
				}
			}
		}

		activity.setAssignableUsers(assignableUsers);
		activity.setResponsibleUsers(responsibleUsers);

		List<Integer> assignableGroupIDs = ValidationUtils.validateParameters("assignable-group", req, false, IntegerPopulator.getPopulator(), validationErrors);
		
		List<Integer> responsibleGroupIDs = ValidationUtils.validateParameters("responsibleGroup", req, false, IntegerPopulator.getPopulator(), validationErrors);
		
		List<Integer> responsibleFallbackUserIDs = ValidationUtils.validateParameters("responsible-user-fallback", req, false, IntegerPopulator.getPopulator(), validationErrors);

		List<Group> assignableGroups = null;
		List<Group> responsibleGroups = null;
		
		List<User> responsibleFallbackUsers = null;
		
				
		if (responsibleGroupIDs != null) {

			responsibleGroups = new ArrayList<>(CollectionUtils.getSize(responsibleGroupIDs));

			if (responsibleGroupIDs != null) {

				for (Integer groupID : responsibleGroupIDs) {

					Group responsibleGroup = callback.getGroupHandler().getGroup(groupID, false);

					if (responsibleGroup != null) {

						responsibleGroups.add(responsibleGroup);

					} else {

						validationErrors.add(new ValidationError("responsibleGroup", ValidationErrorType.InvalidFormat));
					}
				}
			}

			
		}
		
		if (responsibleFallbackUserIDs != null && (activity.getResponsibleUserAttributeNames() != null || activity.getResponsibleGroupAttributeNames() != null)) {
			
			responsibleFallbackUsers = new ArrayList<>(CollectionUtils.getSize(responsibleFallbackUserIDs));


			for (Integer userID : responsibleFallbackUserIDs) {
				
				User responsibleFallbackUser = callback.getUserHandler().getUser(userID, false, false);
				
				boolean responsibleUserMatches = responsibleUsers != null && responsibleUsers.contains(responsibleFallbackUser);
				boolean assignableUserMatches = responsibleUsers != null && responsibleUsers.contains(responsibleFallbackUser);

				if (responsibleFallbackUser != null) {

					if(!responsibleUserMatches && !assignableUserMatches) {
						responsibleFallbackUsers.add(responsibleFallbackUser);
					}

				} else {

					validationErrors.add(new ValidationError("responsible-user-fallback", ValidationErrorType.InvalidFormat));
				}
			}
		}
		
		
		if (assignableGroupIDs != null) {

			assignableGroups = new ArrayList<>(assignableGroupIDs.size());

			for (Integer groupID : assignableGroupIDs) {

				Group group = callback.getGroupHandler().getGroup(groupID, false);

				if (group != null) {

					assignableGroups.add(group);

				} else {

					validationErrors.add(new ValidationError("assignable-group", ValidationErrorType.InvalidFormat));
				}
			}
		}

		activity.setAssignableGroups(assignableGroups);
		activity.setResponsibleGroups(responsibleGroups);
		activity.setResponsibleFallbackUsers(responsibleFallbackUsers);

		if (activity.getResponsibleUserAttributeNames() != null || activity.getResponsibleGroupAttributeNames() != null) {

			if (CollectionUtils.isEmpty(activity.getResponsibleUsers()) && CollectionUtils.isEmpty(activity.getResponsibleGroups())
					&& CollectionUtils.isEmpty(activity.getResponsibleFallbackUsers())) {

				validationErrors.add(new ValidationError("ResponsibleFallbackRequired"));
			}

		} else {

			if (CollectionUtils.isEmpty(activity.getResponsibleUsers()) && CollectionUtils.isEmpty(activity.getResponsibleGroups()) &&
					CollectionUtils.isEmpty(activity.getResponsibleFallbackUsers())) {

				validationErrors.add(new ValidationError("ResponsibleRequired"));
			}
		}

		boolean useResponsibleUserAttributeName = Boolean.parseBoolean(req.getParameter("useResponsibleUserAttributeName"));

		if (useResponsibleUserAttributeName && CollectionUtils.isEmpty(activity.getResponsibleUserAttributeNames())) {

			validationErrors.add(new ValidationError("ResposibleAttributeNamesRequired"));
		}
		
		boolean useResponsibleGroupAttributeName = Boolean.parseBoolean(req.getParameter("useResponsibleGroupAttributeName"));

		if (useResponsibleGroupAttributeName && CollectionUtils.isEmpty(activity.getResponsibleGroupAttributeNames())) {

			validationErrors.add(new ValidationError("ResponsibleAttributeGroupNamesRequired"));
		}
		
			
		if (activity.isAllowManagersToAssignOwner() && assignableUsers == null && assignableGroups == null) {

			validationErrors.add(new ValidationError("AssignableRequired"));
		}

		if (!validationErrors.isEmpty()) {
			throw new ValidationException(validationErrors);
		}
	}

	@Override
	protected FlowApprovalActivity populateFromAddRequest(HttpServletRequest req, User user, URIParser uriParser) throws ValidationException, Exception {

		FlowApprovalActivity activity = super.populateFromAddRequest(req, user, uriParser);

		return populate(activity);
	}

	@Override
	protected FlowApprovalActivity populateFromUpdateRequest(FlowApprovalActivity activity, HttpServletRequest req, User user, URIParser uriParser) throws ValidationException, Exception {

		activity = super.populateFromUpdateRequest(activity, req, user, uriParser);

		return populate(activity);
	}

	protected FlowApprovalActivity populate(FlowApprovalActivity activity) {

		if (activity.getAttributeName() == null) {

			activity.setInverted(false);
			activity.setAttributeValues(null);
		}

		return activity;
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
