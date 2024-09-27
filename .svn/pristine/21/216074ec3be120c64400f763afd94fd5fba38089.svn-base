package com.nordicpeak.flowengine.cruds;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import se.unlogic.fileuploadutils.MultipartRequest;
import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.hierarchy.core.enums.CRUDAction;
import se.unlogic.hierarchy.core.enums.EventTarget;
import se.unlogic.hierarchy.core.events.CRUDEvent;
import se.unlogic.hierarchy.core.exceptions.AccessDeniedException;
import se.unlogic.hierarchy.core.exceptions.ModuleConfigurationException;
import se.unlogic.hierarchy.core.exceptions.URINotFoundException;
import se.unlogic.hierarchy.core.interfaces.ForegroundModuleResponse;
import se.unlogic.hierarchy.core.utils.crud.IntegerBeanIDParser;
import se.unlogic.hierarchy.core.utils.crud.ModularCRUD;
import se.unlogic.standardutils.collections.CollectionUtils;
import se.unlogic.standardutils.dao.CRUDDAO;
import se.unlogic.standardutils.io.BinarySizes;
import se.unlogic.standardutils.io.FileUtils;
import se.unlogic.standardutils.string.StringUtils;
import se.unlogic.standardutils.validation.ValidationError;
import se.unlogic.standardutils.validation.ValidationException;
import se.unlogic.standardutils.xml.XMLUtils;
import se.unlogic.webutils.http.URIParser;
import se.unlogic.webutils.populators.annotated.AnnotatedRequestPopulator;

import com.nordicpeak.flowengine.FlowAdminModule;
import com.nordicpeak.flowengine.beans.Flow;
import com.nordicpeak.flowengine.beans.FlowForm;
import com.nordicpeak.flowengine.interfaces.FlowAdminCRUDCallback;
import com.nordicpeak.flowengine.listeners.FlowFormElementableListener;

public class FlowFormCRUD extends ModularCRUD<FlowForm, Integer, User, FlowAdminCRUDCallback> {
	
	
	protected final FlowFormElementableListener flowFormElementableListener;
	
	public FlowFormCRUD(CRUDDAO<FlowForm, Integer> crudDAO, FlowAdminCRUDCallback callback) {
		
		super(IntegerBeanIDParser.getInstance(), crudDAO, new AnnotatedRequestPopulator<FlowForm>(FlowForm.class), "FlowForm", "flow form", "", callback);
		
		flowFormElementableListener = new FlowFormElementableListener(callback);
		
		setRequirePostForDelete(true);
	}
	
	@Override
	public ForegroundModuleResponse add(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Exception {
		
		checkConfiguration();
		
		return super.add(req, res, user, uriParser);
	}
	
	@Override
	public ForegroundModuleResponse update(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Exception {
		
		checkConfiguration();
		
		return super.update(req, res, user, uriParser);
	}
	
	@Override
	public ForegroundModuleResponse delete(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Exception {
		
		checkConfiguration();
		
		return super.delete(req, res, user, uriParser);
	}
	
	private void checkConfiguration() throws ModuleConfigurationException {
		
		if (callback.getFlowFormFilestore() == null) {
			
			throw new ModuleConfigurationException("Form filestore not set");
		}
	}
	
	@Override
	protected void checkUpdateAccess(FlowForm flowForm, User user, HttpServletRequest req, URIParser uriParser) throws AccessDeniedException, URINotFoundException, SQLException {
		
		checkFlowTypeAccess(user, flowForm.getFlow());
	}
	
	@Override
	protected void checkDeleteAccess(FlowForm flowForm, User user, HttpServletRequest req, URIParser uriParser) throws AccessDeniedException, URINotFoundException, SQLException {
		
		checkFlowTypeAccess(user, flowForm.getFlow());
		
		if (flowForm.getFlow().isEnabled() && flowForm.getFlow().getSteps() == null && flowForm.getFlow().isInternal() && CollectionUtils.getSize(flowForm.getFlow().getFlowForms()) == 1) {
			
			throw new AccessDeniedException("Unable to delete flowForm " + flowForm + " since its flow is enabled with no steps.");
		}
	}
	
	public void checkFlowTypeAccess(User user, Flow flow) throws AccessDeniedException {
		
		if (!callback.hasFlowAccess(user, flow)) {
			
			throw new AccessDeniedException("User does not have access to flow type " + flow.getFlowType());
		}
	}
	
	@Override
	public ForegroundModuleResponse showAddForm(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser, ValidationException validationException) throws Exception {
		
		if (preShowForms((Flow) req.getAttribute("flow"), req, res, user, uriParser)) {
			return null;
		}
		
		return super.showAddForm(req, res, user, uriParser, validationException);
	}
	
	@Override
	public ForegroundModuleResponse showUpdateForm(FlowForm flowForm, HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser, ValidationException validationException) throws Exception {
		
		if (preShowForms(flowForm.getFlow(), req, res, user, uriParser)) {
			return null;
		}
		
		return super.showUpdateForm(flowForm, req, res, user, uriParser, validationException);
	}
	
	private boolean preShowForms(Flow flow, HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws IOException {
		
		if (flow.skipOverview() && !callback.allowSkipOverviewForFlowForms()) {
			
			res.sendRedirect(req.getContextPath() + callback.getFullAlias() + "/showflow/" + flow.getFlowID() + "?error=" + FlowAdminModule.MAY_NOT_ADD_FLOW_FORM_IF_SKIP_OVERVIEW_IS_SET_VALIDATION_ERROR.getMessageKey());
			return true;
		}
		
		return false;
	}
	
	@Override
	protected void appendAddFormData(Document doc, Element addTypeElement, User user, HttpServletRequest req, URIParser uriParser) throws Exception {
		
		super.appendAddFormData(doc, addTypeElement, user, req, uriParser);
		
		appendFormData(doc, addTypeElement, user, req, uriParser);
	}
	
	@Override
	protected void appendUpdateFormData(FlowForm flowForm, Document doc, Element updateTypeElement, User user, HttpServletRequest req, URIParser uriParser) throws Exception {
		
		super.appendUpdateFormData(flowForm, doc, updateTypeElement, user, req, uriParser);
		
		appendFormData(doc, updateTypeElement, user, req, uriParser);
	}
	
	protected void appendFormData(Document doc, Element element, User user, HttpServletRequest req, URIParser uriParser) throws Exception {
		
		XMLUtils.appendNewElement(doc, element, "maxFileSize", callback.getMaxPDFFormFileSize() * BinarySizes.MegaByte);
		XMLUtils.appendNewElement(doc, element, "formattedMaxFileSize", callback.getMaxPDFFormFileSize() + "MB");
	}
	
	@Override
	protected void appendBean(FlowForm flowForm, Element targetElement, Document doc, User user) {
		
		Element childElement = flowForm.toXML(doc);
		targetElement.appendChild(childElement);
		
		flowFormElementableListener.elementGenerated(doc, childElement, flowForm);
	}
	
	@Override
	protected void validateAddPopulation(FlowForm flowForm, HttpServletRequest req, User user, URIParser uriParser) throws ValidationException, SQLException, Exception {
		
		flowForm.setFlow((Flow) req.getAttribute("flow"));
		validatePopulation(flowForm, req, user, uriParser);
	}
	
	@Override
	protected void validateUpdatePopulation(FlowForm flowForm, HttpServletRequest req, User user, URIParser uriParser) throws ValidationException, SQLException, Exception {
		
		validatePopulation(flowForm, req, user, uriParser);
	}
	
	private void validatePopulation(FlowForm flowForm, HttpServletRequest req, User user, URIParser uriParser) throws ValidationException, SQLException, Exception {
		
		if (flowForm.getFlow().skipOverview() && !callback.allowSkipOverviewForFlowForms()) {
			
			throw new ValidationException(FlowAdminModule.MAY_NOT_ADD_FLOW_FORM_IF_SKIP_OVERVIEW_IS_SET_VALIDATION_ERROR);
		}
		
		MultipartRequest mReq = (MultipartRequest) req;
		FileItem fileItem = getUploadedFileItem(mReq);

		if (fileItem != null) {

			String lowerCaseFileExtension = FileUtils.getFileExtension(fileItem.getName().toLowerCase());

			if (!callback.getAllowedFlowFormFileExtensions().contains(lowerCaseFileExtension)) {

				throw new ValidationException(new ValidationError("InvalidFlowFormFileFormat", StringUtils.toCommaSeparatedString(callback.getAllowedFlowFormFileExtensions())));
			}

			flowForm.setFileExtension(lowerCaseFileExtension);
			flowForm.setExternalURL(null);

		} else if (StringUtils.isEmpty(flowForm.getExternalURL())) {

			if (flowForm.getFlowFormID() == null || !(new File(callback.getFlowFormFilePath(flowForm)).exists())) {

				throw new ValidationException(new ValidationError("NoAttachedFile"));
			}

		} else {
			flowForm.setFileExtension(null);
		}
	}
	
	private FileItem getUploadedFileItem(MultipartRequest mReq) {
		
		if (mReq.getFileCount() > 0) {
			
			FileItem fileItem = mReq.getFile(0);
			
			if (fileItem.getSize() > 0) {
				
				return fileItem;
			}
		}
		
		return null;
	}
	
	@Override
	protected ForegroundModuleResponse beanAdded(FlowForm flowForm, HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Exception {
		
		updateFile(flowForm, req);
		
		return super.beanAdded(flowForm, req, res, user, uriParser);
	}
	
	@Override
	protected ForegroundModuleResponse beanUpdated(FlowForm flowForm, HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Exception {
		
		updateFile(flowForm, req);
		
		return super.beanUpdated(flowForm, req, res, user, uriParser);
	}
	
	private void updateFile(FlowForm flowForm, HttpServletRequest req) throws Exception {
		
		MultipartRequest mReq = (MultipartRequest) req;
		FileItem fileItem = getUploadedFileItem(mReq);
		
		if (fileItem != null) {
			
			File file = new File(callback.getFlowFormFilePath(flowForm));
			
			fileItem.write(file);
			
		} else if (!StringUtils.isEmpty(flowForm.getExternalURL())) {
			
			callback.deleteFlowFormFile(flowForm);
		}
	}
	
	@Override
	protected void deleteBean(FlowForm flowForm, HttpServletRequest req, User user, URIParser uriParser) throws Exception {
		
		callback.deleteFlowFormFile(flowForm);
		
		super.deleteBean(flowForm, req, user, uriParser);
	}
	
	@Override
	protected ForegroundModuleResponse filteredBeanAdded(FlowForm flowForm, HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Exception {
		
		callback.addFlowFamilyEvent(callback.getEventFlowFormAddedMessage() + " \"" + flowForm.getName() + "\"", flowForm.getFlow(), user);
		
		return beanEvent(flowForm, req, res, CRUDAction.ADD);
	}
	
	@Override
	protected ForegroundModuleResponse filteredBeanUpdated(FlowForm flowForm, HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Exception {
		
		callback.addFlowFamilyEvent(callback.getEventFlowFormUpdatedMessage() + " \"" + flowForm.getName() + "\"", flowForm.getFlow(), user);
		
		return beanEvent(flowForm, req, res, CRUDAction.UPDATE);
	}
	
	@Override
	protected ForegroundModuleResponse filteredBeanDeleted(FlowForm flowForm, HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Exception {
		
		callback.addFlowFamilyEvent(callback.getEventFlowFormDeletedMessage() + " \"" + flowForm.getName() + "\"", flowForm.getFlow(), user);
		
		return beanEvent(flowForm, req, res, CRUDAction.DELETE);
	}
	
	private ForegroundModuleResponse beanEvent(FlowForm flowForm, HttpServletRequest req, HttpServletResponse res, CRUDAction action) throws IOException {
		
		callback.getEventHandler().sendEvent(FlowForm.class, new CRUDEvent<FlowForm>(action, flowForm), EventTarget.ALL);
		
		callback.getEventHandler().sendEvent(Flow.class, new CRUDEvent<Flow>(CRUDAction.UPDATE, flowForm.getFlow()), EventTarget.ALL);
		
		callback.redirectToMethod(req, res, "/showflow/" + flowForm.getFlow().getFlowID() + "#pdfform");
		
		return null;
	}
	
	@Override
	protected void redirectToListMethod(HttpServletRequest req, HttpServletResponse res, FlowForm flowForm) throws Exception {
		
		res.sendRedirect(req.getContextPath() + callback.getFullAlias() + "/showflow/" + flowForm.getFlow().getFlowID());
	}
	
	@Override
	public ForegroundModuleResponse list(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser, List<ValidationError> validationErrors) throws Exception {
		
		return callback.list(req, res, user, uriParser, validationErrors);
	}
}
