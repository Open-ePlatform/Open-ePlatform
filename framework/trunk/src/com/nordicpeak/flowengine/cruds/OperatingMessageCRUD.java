package com.nordicpeak.flowengine.cruds;

import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.hierarchy.core.interfaces.ForegroundModuleResponse;
import se.unlogic.hierarchy.core.utils.IntegerBasedCRUD;
import se.unlogic.standardutils.dao.CRUDDAO;
import se.unlogic.standardutils.date.DateUtils;
import se.unlogic.standardutils.numbers.NumberUtils;
import se.unlogic.standardutils.populators.UnixTimeDatePopulator;
import se.unlogic.standardutils.string.StringUtils;
import se.unlogic.standardutils.time.TimeUtils;
import se.unlogic.standardutils.validation.ValidationError;
import se.unlogic.standardutils.validation.ValidationErrorType;
import se.unlogic.standardutils.validation.ValidationException;
import se.unlogic.standardutils.xml.XMLUtils;
import se.unlogic.webutils.http.URIParser;
import se.unlogic.webutils.populators.annotated.AnnotatedRequestPopulator;
import se.unlogic.webutils.validation.ValidationUtils;

import com.nordicpeak.flowengine.OperatingMessageModule;
import com.nordicpeak.flowengine.beans.Flow;
import com.nordicpeak.flowengine.beans.FlowFamily;
import com.nordicpeak.flowengine.beans.OperatingMessage;

public class OperatingMessageCRUD extends IntegerBasedCRUD<OperatingMessage, OperatingMessageModule> {

	private static final UnixTimeDatePopulator DATE_POPULATOR = new UnixTimeDatePopulator();

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

	private OperatingMessage populateFromRequest(OperatingMessage bean, HttpServletRequest req, User user, URIParser uriParser) throws ValidationException, Exception {

		List<ValidationError> errors = new ArrayList<ValidationError>();

		Date startDate = ValidationUtils.validateParameter("startDate", req, true, DATE_POPULATOR, errors);

		Date endDate = ValidationUtils.validateParameter("endDate", req, true, DATE_POPULATOR, errors);

		Timestamp startTime = null;

		Timestamp endTime = null;

		if (startDate != null && endDate != null) {

			startTime = this.getTime("startTime", req, startDate, errors);

			endTime = this.getTime("endTime", req, endDate, errors);

			if (startTime != null && endTime != null) {

				if (startDate.equals(endDate) && (endTime.equals(startTime) || endTime.before(startTime))) {
					errors.add(new ValidationError("EndTimeBeforeStartTime"));
				}

				if (DateUtils.daysBetween(startTime, endTime) < 0) {
					errors.add(new ValidationError("DaysBetweenToSmall"));
				}

			}

		}
		
		if(!bean.isGlobal() && bean.getFlowFamilyIDs() == null) {
			
			errors.add(new ValidationError("NoFlowFamilyChoosen"));
		}

		if (!errors.isEmpty()) {
			throw new ValidationException(errors);
		}

		bean.setStartTime(startTime);
		bean.setEndTime(endTime);

		return bean;

	}

	public OperatingMessageCRUD(CRUDDAO<OperatingMessage, Integer> crudDAO, OperatingMessageModule callback) {

		super(crudDAO, new AnnotatedRequestPopulator<OperatingMessage>(OperatingMessage.class), "OperatingMessage", "operating message", "/", callback);

	}

	@Override
	protected void appendAddFormData(Document doc, Element addTypeElement, User user, HttpServletRequest req, URIParser uriParser) throws Exception {

		appendFlowFamilies(doc, addTypeElement);
	}

	@Override
	protected void appendUpdateFormData(OperatingMessage bean, Document doc, Element updateTypeElement, User user, HttpServletRequest req, URIParser uriParser) throws Exception {

		appendFlowFamilies(doc, updateTypeElement);
	}

	@Override
	protected void appendListFormData(Document doc, Element listTypeElement, User user, HttpServletRequest req, URIParser uriParser, List<ValidationError> validationError) throws SQLException {

		appendFlowFamilies(doc, listTypeElement);
	}

	private void appendFlowFamilies(Document doc, Element element) {
		
		Collection<FlowFamily> flowFamilies = callback.getFlowAdminModule().getCachedFlowFamilies();
		
		if(flowFamilies != null) {
			
			for(FlowFamily flowFamily : flowFamilies) {
				
				Flow latestFlow = callback.getFlowAdminModule().getLatestFlowVersion(flowFamily);
				
				if(latestFlow != null) {
					
					Element flowFamilyElement = flowFamily.toXML(doc);
					
					XMLUtils.appendNewElement(doc, flowFamilyElement, "name", latestFlow.getName());

					element.appendChild(flowFamilyElement);
				}
				
			}
			
		}
		
	}
	
	@Override
	protected ForegroundModuleResponse beanAdded(OperatingMessage bean, HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Exception {

		callback.addOrUpdateOperatingMessage(bean);
		
		return super.beanAdded(bean, req, res, user, uriParser);
	}

	@Override
	protected ForegroundModuleResponse beanUpdated(OperatingMessage bean, HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Exception {

		callback.addOrUpdateOperatingMessage(bean);

		return super.beanUpdated(bean, req, res, user, uriParser);
	}

	@Override
	protected ForegroundModuleResponse beanDeleted(OperatingMessage bean, HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Exception {

		callback.deleteOperatingMessage(bean);
		
		return super.beanDeleted(bean, req, res, user, uriParser);
	}

	private Timestamp getTime(String fieldname, HttpServletRequest req, Date date, List<ValidationError> errors) {

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);

		String time = req.getParameter(fieldname);

		if (!StringUtils.isEmpty(time)) {

			String[] timeParts = time.split(":");

			if (timeParts.length == 2 && NumberUtils.isInt(timeParts[0]) && NumberUtils.isInt(timeParts[1])) {

				calendar.set(Calendar.HOUR, Integer.parseInt(timeParts[0]));
				calendar.set(Calendar.MINUTE, Integer.parseInt(timeParts[1]));
				calendar.set(Calendar.MILLISECOND, 0);

				return new Timestamp(calendar.getTimeInMillis());

			}

			errors.add(new ValidationError(fieldname, ValidationErrorType.InvalidFormat));

		} else {

			errors.add(new ValidationError(fieldname, ValidationErrorType.RequiredField));
		}

		return null;

	}

}
