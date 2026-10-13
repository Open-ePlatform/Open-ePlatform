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
import se.unlogic.openhierarchy.foregroundmodules.siteprofile.interfaces.SiteProfile;
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
import com.nordicpeak.flowengine.beans.OperatingMessageNotificationSettings;

public class OperatingMessageCRUD extends IntegerBasedCRUD<OperatingMessage, OperatingMessageModule> {
	
	protected static final UnixTimeDatePopulator DATE_POPULATOR = new UnixTimeDatePopulator();
	
	public OperatingMessageCRUD(CRUDDAO<OperatingMessage, Integer> crudDAO, OperatingMessageModule callback) {
		
		super(crudDAO, new AnnotatedRequestPopulator<OperatingMessage>(OperatingMessage.class), "OperatingMessage", "operating message", "/", callback);
	
		setRequirePostForDelete(true);
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
		
		Date startDate = ValidationUtils.validateParameter("startDate", req, true, DATE_POPULATOR, errors);
		
		Date endDate = ValidationUtils.validateParameter("endDate", req, true, DATE_POPULATOR, errors);
		
		Timestamp startTime = null;
		
		Timestamp endTime = null;
		
		if (startDate != null && endDate != null) {
			
			startTime = getTime("startTime", req, startDate, errors);
			
			endTime = getTime("endTime", req, endDate, errors);
			
			if (startTime != null && endTime != null) {
				
				if (startDate.equals(endDate) && (endTime.equals(startTime) || endTime.before(startTime))) {
					errors.add(new ValidationError("EndTimeBeforeStartTime"));
				}
				
				if (DateUtils.getDaysBetween(startTime, endTime) < 0) {
					errors.add(new ValidationError("DaysBetweenToSmall"));
				}
				
			}
			
		}
		
		if (!operatingMessage.isGlobal() && operatingMessage.getFlowFamilyIDs() == null) {
			
			errors.add(new ValidationError("NoFlowFamilyChosen"));
		}
		
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
		
		appendFormData(null, doc, addTypeElement, user, req, uriParser);
	}
	
	@Override
	protected void appendUpdateFormData(OperatingMessage operatingMessage, Document doc, Element updateTypeElement, User user, HttpServletRequest req, URIParser uriParser) throws Exception {
		
		appendFormData(operatingMessage, doc, updateTypeElement, user, req, uriParser);
	}
	
	protected void appendFormData(OperatingMessage operatingMessage, Document doc, Element typeElement, User user, HttpServletRequest req, URIParser uriParser) throws Exception {
		
		appendFlowFamilies(doc, typeElement);
		appendProfiles(callback, doc, typeElement, user, req, uriParser);
	}
	
	@Override
	protected void appendListFormData(Document doc, Element listTypeElement, User user, HttpServletRequest req, URIParser uriParser, List<ValidationError> validationError) throws SQLException {
		
		appendFlowFamilies(doc, listTypeElement);
		appendProfiles(callback, doc, listTypeElement, user, req, uriParser);
		
		XMLUtils.append(doc, listTypeElement, "ExternalOperatingMessages", callback.getExternalOperatingMessages());
		XMLUtils.append(doc, listTypeElement, "ExternalOperatingMessageSources", callback.getExternalOperatingMessageSources());
		
		OperatingMessageNotificationSettings notificationSettings = callback.getNotificationSettings(true);
		
		if (notificationSettings != null) {
			listTypeElement.appendChild(notificationSettings.toXML(doc));
		}
	}
	
	private void appendFlowFamilies(Document doc, Element element) {
		
		Collection<FlowFamily> flowFamilies = callback.getFlowAdminModule().getCachedFlowFamilies();
		
		if (flowFamilies != null) {
			
			for (FlowFamily flowFamily : flowFamilies) {
				
				Flow latestFlow = callback.getFlowAdminModule().getLatestFlowVersion(flowFamily);
				
				if (latestFlow != null) {
					
					Element flowFamilyElement = flowFamily.toXML(doc);
					
					XMLUtils.appendNewElement(doc, flowFamilyElement, "name", latestFlow.getName());
					
					element.appendChild(flowFamilyElement);
				}
			}
		}
	}
	
	protected static void appendProfiles(OperatingMessageModule callback, Document doc, Element element, User user, HttpServletRequest req, URIParser uriParser) {
		
		Collection<? extends SiteProfile> profiles = callback.getFlowAdminModule().getSiteProfileHandler().getProfiles();
		
		if (profiles != null) {
			
			for (SiteProfile profile : profiles) {
				
				Element profileElement = profile.toXML(doc);
				
				element.appendChild(profileElement);
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
	
	protected static Timestamp getTime(String fieldname, HttpServletRequest req, Date date, List<ValidationError> errors) {
		
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
