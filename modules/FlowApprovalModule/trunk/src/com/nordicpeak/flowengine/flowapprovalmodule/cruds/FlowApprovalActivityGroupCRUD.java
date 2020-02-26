package com.nordicpeak.flowengine.flowapprovalmodule.cruds;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.hierarchy.core.enums.CRUDAction;
import se.unlogic.hierarchy.core.exceptions.AccessDeniedException;
import se.unlogic.hierarchy.core.exceptions.URINotFoundException;
import se.unlogic.hierarchy.core.interfaces.ForegroundModuleResponse;
import se.unlogic.hierarchy.core.utils.crud.ModularCRUD;
import se.unlogic.standardutils.arrays.ArrayUtils;
import se.unlogic.standardutils.collections.CollectionUtils;
import se.unlogic.standardutils.dao.CRUDDAO;
import se.unlogic.standardutils.templates.TemplateUtils;
import se.unlogic.standardutils.validation.ValidationError;
import se.unlogic.standardutils.validation.ValidationException;
import se.unlogic.standardutils.xml.XMLUtils;
import se.unlogic.webutils.http.URIParser;
import se.unlogic.webutils.populators.annotated.AnnotatedRequestPopulator;

import com.nordicpeak.flowengine.beans.Flow;
import com.nordicpeak.flowengine.beans.Status;
import com.nordicpeak.flowengine.enums.ContentType;
import com.nordicpeak.flowengine.flowapprovalmodule.FlowApprovalAdminModule;
import com.nordicpeak.flowengine.flowapprovalmodule.beans.FlowApprovalActivityGroup;
import com.nordicpeak.flowengine.populators.FlowAdminFragmentExtensionViewCRUDIDParser;

public class FlowApprovalActivityGroupCRUD extends ModularCRUD<FlowApprovalActivityGroup, Integer, User, FlowApprovalAdminModule> {
	
	public static final ContentType[] INVALID_STATUS_TYPES = new ContentType[] {ContentType.NEW, ContentType.WAITING_FOR_PAYMENT, ContentType.WAITING_FOR_MULTISIGN};

	public FlowApprovalActivityGroupCRUD(CRUDDAO<FlowApprovalActivityGroup, Integer> crudDAO, FlowApprovalAdminModule callback) {

		super(FlowAdminFragmentExtensionViewCRUDIDParser.getInstance(), crudDAO, new AnnotatedRequestPopulator<FlowApprovalActivityGroup>(FlowApprovalActivityGroup.class), "ActivityGroup", "activity group", "", callback);
	}

	@Override
	protected void checkAddAccess(User user, HttpServletRequest req, URIParser uriParser) throws AccessDeniedException, URINotFoundException, SQLException {

		Flow flow = (Flow) req.getAttribute("flow");

		if (flow == null) {

			throw new URINotFoundException(uriParser);
		}
	}

	@Override
	protected void checkUpdateAccess(FlowApprovalActivityGroup activityGroup, User user, HttpServletRequest req, URIParser uriParser) throws AccessDeniedException, URINotFoundException, SQLException {

		Flow flow = (Flow) req.getAttribute("flow");

		if (flow == null) {

			throw new URINotFoundException(uriParser);
		}
	}

	@Override
	protected void checkDeleteAccess(FlowApprovalActivityGroup activityGroup, User user, HttpServletRequest req, URIParser uriParser) throws AccessDeniedException, URINotFoundException, SQLException {

		Flow flow = (Flow) req.getAttribute("flow");

		if (flow == null) {

			throw new URINotFoundException(uriParser);
		}
	}

	@Override
	protected void appendBean(FlowApprovalActivityGroup activityGroup, Element targetElement, Document doc, User user) {
		
		TemplateUtils.setTemplatedFields(activityGroup, callback);
		
		super.appendBean(activityGroup, targetElement, doc, user);
	}

	@Override
	protected void appendShowFormData(FlowApprovalActivityGroup activityGroup, Document doc, Element showTypeElement, User user, HttpServletRequest req, HttpServletResponse res, URIParser uriParser) throws SQLException, IOException, Exception {
		super.appendShowFormData(activityGroup, doc, showTypeElement, user, req, res, uriParser);

		appendFormData(doc, showTypeElement, user, req, uriParser);
	}

	@Override
	protected void appendAddFormData(Document doc, Element addTypeElement, User user, HttpServletRequest req, URIParser uriParser) throws Exception {
		super.appendAddFormData(doc, addTypeElement, user, req, uriParser);
		
		FlowApprovalActivityGroup dummy = new FlowApprovalActivityGroup();
		appendBean(dummy, addTypeElement, doc, user);

		appendFormData(doc, addTypeElement, user, req, uriParser);
	}

	@Override
	protected void appendUpdateFormData(FlowApprovalActivityGroup activityGroup, Document doc, Element updateTypeElement, User user, HttpServletRequest req, URIParser uriParser) throws Exception {
		super.appendUpdateFormData(activityGroup, doc, updateTypeElement, user, req, uriParser);

		appendFormData(doc, updateTypeElement, user, req, uriParser);
	}

	protected void appendFormData(Document doc, Element typeElement, User user, HttpServletRequest req, URIParser uriParser) throws SQLException, IOException, Exception {

		typeElement.appendChild(((Flow) req.getAttribute("flow")).toXML(doc));
		XMLUtils.appendNewElement(doc, typeElement, "extensionRequestURL", req.getAttribute("extensionRequestURL"));
	}

	@Override
	protected void validateAddPopulation(FlowApprovalActivityGroup activityGroup, HttpServletRequest req, User user, URIParser uriParser) throws ValidationException, SQLException, Exception {
		super.validateAddPopulation(activityGroup, req, user, uriParser);

		Flow flow = (Flow) req.getAttribute("flow");
		
		activityGroup.setFlowFamilyID(flow.getFlowFamily().getFlowFamilyID());
		activityGroup.setSortIndex(1 + callback.getApprovalGroupMaxSortIndex(flow.getFlowFamily()));
		
		validatePopulation(activityGroup, req, user, uriParser);
	}

	@Override
	protected void validateUpdatePopulation(FlowApprovalActivityGroup activityGroup, HttpServletRequest req, User user, URIParser uriParser) throws ValidationException, SQLException, Exception {
		super.validateUpdatePopulation(activityGroup, req, user, uriParser);

		validatePopulation(activityGroup, req, user, uriParser);
	}

	protected void validatePopulation(FlowApprovalActivityGroup activityGroup, HttpServletRequest req, User user, URIParser uriParser) throws ValidationException, SQLException, Exception {

		List<ValidationError> validationErrors = null;
		
		TemplateUtils.clearUnchangedTemplatedFields(activityGroup, callback);

		Flow flow = (Flow) req.getAttribute("flow");

		if (activityGroup.getStartStatus() != null) {

			boolean statusFound = false;

			for (Status status : flow.getStatuses()) {

				if (ArrayUtils.contains(INVALID_STATUS_TYPES, status.getContentType())) {
					continue;
				}
				
				if (status.getName().equalsIgnoreCase(activityGroup.getStartStatus())) {
					statusFound = true;
					break;
				}
			}

			if (!statusFound) {
				validationErrors = CollectionUtils.addAndInstantiateIfNeeded(validationErrors, new ValidationError("InvalidStatus", "", "startStatus"));
			}
		}
		
		if (activityGroup.getCompleteStatus() != null) {

			boolean statusFound = false;

			for (Status status : flow.getStatuses()) {
				
				if (ArrayUtils.contains(INVALID_STATUS_TYPES, status.getContentType())) {
					continue;
				}

				if (status.getName().equalsIgnoreCase(activityGroup.getCompleteStatus())) {
					statusFound = true;
					break;
				}
			}

			if (!statusFound) {
				validationErrors = CollectionUtils.addAndInstantiateIfNeeded(validationErrors, new ValidationError("InvalidStatus", "", "completeStatus"));
			}
		}
		
		if (activityGroup.getDenyStatus() != null) {

			boolean statusFound = false;

			for (Status status : flow.getStatuses()) {
				
				if (ArrayUtils.contains(INVALID_STATUS_TYPES, status.getContentType())) {
					continue;
				}

				if (status.getName().equalsIgnoreCase(activityGroup.getDenyStatus())) {
					statusFound = true;
					break;
				}
			}

			if (!statusFound) {
				validationErrors = CollectionUtils.addAndInstantiateIfNeeded(validationErrors, new ValidationError("InvalidStatus", "", "denyStatus"));
			}
		}
		
		if (validationErrors != null) {
			throw new ValidationException(validationErrors);
		}
	}

	@Override
	protected ForegroundModuleResponse beanAdded(FlowApprovalActivityGroup activityGroup, HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Exception {

		callback.addFlowFamilyEvent(callback.getEventActivityGroupAddedMessage() + " \"" + activityGroup.getName() + "\"", ((Flow) req.getAttribute("flow")).getFlowFamily(), user);

		beanEvent(activityGroup, req, res, CRUDAction.ADD);

		res.sendRedirect(req.getContextPath() + req.getAttribute("extensionRequestURL") + "/showactivitygroup/" + activityGroup.getActivityGroupID());
		return null;
	}

	@Override
	protected ForegroundModuleResponse beanUpdated(FlowApprovalActivityGroup activityGroup, HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Exception {

		callback.addFlowFamilyEvent(callback.getEventActivityGroupUpdatedMessage() + " \"" + activityGroup.getName() + "\"", ((Flow) req.getAttribute("flow")).getFlowFamily(), user);

		beanEvent(activityGroup, req, res, CRUDAction.UPDATE);

		res.sendRedirect(req.getContextPath() + req.getAttribute("extensionRequestURL") + "/showactivitygroup/" + activityGroup.getActivityGroupID());
		return null;
	}

	@Override
	protected ForegroundModuleResponse beanDeleted(FlowApprovalActivityGroup activityGroup, HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Exception {

		callback.addFlowFamilyEvent(callback.getEventActivityGroupDeletedMessage() + " \"" + activityGroup.getName() + "\"", ((Flow) req.getAttribute("flow")).getFlowFamily(), user);

		beanEvent(activityGroup, req, res, CRUDAction.DELETE);

		return null;
	}

	private void beanEvent(FlowApprovalActivityGroup activityGroup, HttpServletRequest req, HttpServletResponse res, CRUDAction action) throws IOException {

//		callback.getEventHandler().sendEvent(FlowApprovalActivityGroup.class, new CRUDEvent<FlowApprovalActivityGroup>(action, activityGroup), EventTarget.ALL);
	}

	@Override
	public ForegroundModuleResponse list(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser, List<ValidationError> validationErrors) throws Exception {

		return callback.list((String) req.getAttribute("extensionRequestURL"), (Flow) req.getAttribute("flow"), req, res, user, uriParser, validationErrors);
	}

}
