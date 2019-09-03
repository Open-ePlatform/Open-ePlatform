package com.nordicpeak.flowengine.cruds;

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.hierarchy.core.exceptions.AccessDeniedException;
import se.unlogic.hierarchy.core.exceptions.URINotFoundException;
import se.unlogic.hierarchy.core.interfaces.ForegroundModuleResponse;
import se.unlogic.hierarchy.core.utils.crud.ModularCRUD;
import se.unlogic.standardutils.dao.CRUDDAO;
import se.unlogic.standardutils.date.DateUtils;
import se.unlogic.standardutils.time.TimeUtils;
import se.unlogic.standardutils.validation.ValidationError;
import se.unlogic.standardutils.validation.ValidationException;
import se.unlogic.standardutils.xml.XMLUtils;
import se.unlogic.webutils.http.URIParser;
import se.unlogic.webutils.populators.annotated.AnnotatedRequestPopulator;
import se.unlogic.webutils.validation.ValidationUtils;

import com.nordicpeak.flowengine.OperatingMessageModule;
import com.nordicpeak.flowengine.beans.Flow;
import com.nordicpeak.flowengine.beans.OperatingMessage;
import com.nordicpeak.flowengine.populators.FlowAdminFragmentExtensionViewCRUDIDParser;

public class OperatingMessageFragmentExtensionCRUD extends ModularCRUD<OperatingMessage, Integer, User, OperatingMessageModule> {

	public OperatingMessageFragmentExtensionCRUD(CRUDDAO<OperatingMessage, Integer> crudDAO, OperatingMessageModule callback) {

		super(FlowAdminFragmentExtensionViewCRUDIDParser.getInstance(), crudDAO, new AnnotatedRequestPopulator<OperatingMessage>(OperatingMessage.class), "OperatingMessage", "operating message", "/", callback);
	}
	
	@Override
	protected void checkAddAccess(User user, HttpServletRequest req, URIParser uriParser) throws AccessDeniedException, URINotFoundException, SQLException {

		Flow flow = (Flow) req.getAttribute("flow");

		if (flow == null) {
			throw new URINotFoundException(uriParser);
		}
	}

	@Override
	protected void checkUpdateAccess(OperatingMessage operatingMessage, User user, HttpServletRequest req, URIParser uriParser) throws AccessDeniedException, URINotFoundException, SQLException {

		Flow flow = (Flow) req.getAttribute("flow");

		if (flow == null) {
			throw new URINotFoundException(uriParser);
		}
		
		checkAccess(operatingMessage, flow);
	}

	@Override
	protected void checkDeleteAccess(OperatingMessage operatingMessage, User user, HttpServletRequest req, URIParser uriParser) throws AccessDeniedException, URINotFoundException, SQLException {

		Flow flow = (Flow) req.getAttribute("flow");

		if (flow == null) {
			throw new URINotFoundException(uriParser);
		}
		
		checkAccess(operatingMessage, flow);
	}
	
	private void checkAccess(OperatingMessage operatingMessage, Flow flow) throws AccessDeniedException {
		
		if (operatingMessage.isGlobal() || operatingMessage.getFlowFamilyIDs().size() > 1 || !operatingMessage.getFlowFamilyIDs().contains(flow.getFlowFamily().getFlowFamilyID())) {
			throw new AccessDeniedException("May only modify flow specific operating messages from this module");
		}
	}

	@Override
	protected OperatingMessage populateFromAddRequest(HttpServletRequest req, User user, URIParser uriParser) throws ValidationException, Exception {

		OperatingMessage operatingMessage = super.populateFromAddRequest(req, user, uriParser);

		operatingMessage = populateFromRequest(operatingMessage, req, user, uriParser);

		operatingMessage.setPosted(TimeUtils.getCurrentTimestamp());
		operatingMessage.setPoster(user);

		return operatingMessage;
	}

	@Override
	protected OperatingMessage populateFromUpdateRequest(OperatingMessage bean, HttpServletRequest req, User user, URIParser uriParser) throws ValidationException, Exception {

		OperatingMessage operatingMessage = super.populateFromUpdateRequest(bean, req, user, uriParser);

		operatingMessage = populateFromRequest(operatingMessage, req, user, uriParser);

		operatingMessage.setUpdated(TimeUtils.getCurrentTimestamp());
		operatingMessage.setEditor(user);

		return operatingMessage;
	}

	private OperatingMessage populateFromRequest(OperatingMessage operatingMessage, HttpServletRequest req, User user, URIParser uriParser) throws ValidationException, Exception {

		List<ValidationError> errors = new ArrayList<ValidationError>();

		Date startDate = ValidationUtils.validateParameter("startDate", req, true, OperatingMessageCRUD.DATE_POPULATOR, errors);

		Date endDate = ValidationUtils.validateParameter("endDate", req, true, OperatingMessageCRUD.DATE_POPULATOR, errors);

		Timestamp startTime = null;

		Timestamp endTime = null;

		if (startDate != null && endDate != null) {

			startTime = OperatingMessageCRUD.getTime("startTime", req, startDate, errors);

			endTime = OperatingMessageCRUD.getTime("endTime", req, endDate, errors);

			if (startTime != null && endTime != null) {

				if (startDate.equals(endDate) && (endTime.equals(startTime) || endTime.before(startTime))) {
					errors.add(new ValidationError("EndTimeBeforeStartTime"));
				}

				if (DateUtils.getDaysBetween(startTime, endTime) < 0) {
					errors.add(new ValidationError("DaysBetweenToSmall"));
				}
			}
		}

		Flow flow = (Flow) req.getAttribute("flow");

		operatingMessage.setGlobal(false);
		operatingMessage.setFlowFamilyIDs(Collections.singletonList(flow.getFlowFamily().getFlowFamilyID()));

		if (operatingMessage.getMessageType() == null) {

			errors.add(new ValidationError("NoMessageTypeChosen"));
		}

		if (!errors.isEmpty()) {
			throw new ValidationException(errors);
		}

		operatingMessage.setStartTime(startTime);
		operatingMessage.setEndTime(endTime);

		return operatingMessage;
	}

	@Override
	protected void appendAddFormData(Document doc, Element addTypeElement, User user, HttpServletRequest req, URIParser uriParser) throws Exception {

		appendFormData(doc, addTypeElement, user, req, uriParser);
	}

	@Override
	protected void appendUpdateFormData(OperatingMessage bean, Document doc, Element updateTypeElement, User user, HttpServletRequest req, URIParser uriParser) throws Exception {

		appendFormData(doc, updateTypeElement, user, req, uriParser);
	}
	
	protected void appendFormData(Document doc, Element typeElement, User user, HttpServletRequest req, URIParser uriParser) throws SQLException, IOException, Exception {

		typeElement.appendChild(((Flow) req.getAttribute("flow")).toXML(doc));
		XMLUtils.appendNewElement(doc, typeElement, "extensionRequestURL", req.getAttribute("extensionRequestURL"));
		
		OperatingMessageCRUD.appendProfiles(callback, doc, typeElement, user, req, uriParser);
	}

	@Override
	protected ForegroundModuleResponse beanAdded(OperatingMessage bean, HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Exception {

		callback.addOrUpdateOperatingMessage(bean);
		return null;
	}

	@Override
	protected ForegroundModuleResponse beanUpdated(OperatingMessage bean, HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Exception {

		callback.addOrUpdateOperatingMessage(bean);
		return null;
	}

	@Override
	protected ForegroundModuleResponse beanDeleted(OperatingMessage bean, HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Exception {

		callback.deleteOperatingMessage(bean);
		return null;
	}

	@Override
	public ForegroundModuleResponse list(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser, List<ValidationError> validationErrors) throws Exception {

		return callback.processRequestError((String) req.getAttribute("extensionRequestURL"), (Flow) req.getAttribute("flow"), req, res, user, uriParser, validationErrors);
	}
}
