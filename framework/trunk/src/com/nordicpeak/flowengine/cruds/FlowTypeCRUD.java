package com.nordicpeak.flowengine.cruds;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.rowset.serial.SerialBlob;
import javax.sql.rowset.serial.SerialException;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.io.FilenameUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import se.unlogic.fileuploadutils.MultipartRequest;
import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.hierarchy.core.enums.CRUDAction;
import se.unlogic.hierarchy.core.enums.EventTarget;
import se.unlogic.hierarchy.core.events.CRUDEvent;
import se.unlogic.hierarchy.core.exceptions.AccessDeniedException;
import se.unlogic.hierarchy.core.exceptions.URINotFoundException;
import se.unlogic.hierarchy.core.interfaces.ForegroundModuleResponse;
import se.unlogic.hierarchy.core.interfaces.ViewFragment;
import se.unlogic.hierarchy.core.utils.AccessUtils;
import se.unlogic.hierarchy.core.utils.crud.IntegerBeanIDParser;
import se.unlogic.hierarchy.core.utils.crud.ModularCRUD;
import se.unlogic.standardutils.collections.CollectionUtils;
import se.unlogic.standardutils.dao.CRUDDAO;
import se.unlogic.standardutils.image.ImageUtils;
import se.unlogic.standardutils.io.FileUtils;
import se.unlogic.standardutils.serialization.SerializationUtils;
import se.unlogic.standardutils.string.StringUtils;
import se.unlogic.standardutils.time.TimeUtils;
import se.unlogic.standardutils.validation.ValidationError;
import se.unlogic.standardutils.validation.ValidationException;
import se.unlogic.standardutils.xml.XMLUtils;
import se.unlogic.webutils.http.URIParser;
import se.unlogic.webutils.populators.annotated.AnnotatedRequestPopulator;

import com.nordicpeak.flowengine.FlowAdminModule;
import com.nordicpeak.flowengine.beans.FlowType;
import com.nordicpeak.flowengine.interfaces.FlowTypeExtensionProvider;


public class FlowTypeCRUD extends ModularCRUD<FlowType, Integer, User, FlowAdminModule> {

	public FlowTypeCRUD(CRUDDAO<FlowType, Integer> crudDAO, FlowAdminModule callback) {

		super(IntegerBeanIDParser.getInstance(), crudDAO, new AnnotatedRequestPopulator<FlowType>(FlowType.class), "FlowType", "flow type", "/flowtypes", callback);
	}

	@Override
	public FlowType getBean(Integer beanID, String getMode, HttpServletRequest req) throws SQLException, AccessDeniedException {

		if(getMode != null && (getMode == FlowCRUD.SHOW || getMode == FlowCRUD.DELETE)){

			return callback.getCachedFlowType(beanID);

		}else{

			FlowType flowType = callback.getCachedFlowType(beanID);

			if(flowType == null){

				return null;
			}

			Blob icon = flowType.getIcon();
			
			flowType = SerializationUtils.cloneSerializable(flowType);

			flowType.setIcon(icon);
			
			return flowType;
		}
	}

	@Override
	protected List<FlowType> getAllBeans(User user) throws SQLException {

		ArrayList<FlowType> filteredFlowtypes = new ArrayList<FlowType>(callback.getCachedFlowTypes());

		//Filter flow types if user not admin for this module
		if(!AccessUtils.checkAccess(user, callback)){

			Iterator<FlowType> iterator = filteredFlowtypes.iterator();

			FlowType flowType;

			while(iterator.hasNext()){

				flowType = iterator.next();

				if(!AccessUtils.checkAccess(user, flowType.getAdminAccessInterface())){

					iterator.remove();
				}
			}
		}

		return filteredFlowtypes;
	}

	@Override
	protected String getBeanName(FlowType bean) {

		return bean.getName();
	}

	@Override
	protected void checkAddAccess(User user, HttpServletRequest req, URIParser uriParser) throws AccessDeniedException, URINotFoundException, SQLException {

		checkModificationAccess(user);
	}

	@Override
	protected void checkUpdateAccess(FlowType bean, User user, HttpServletRequest req, URIParser uriParser) throws AccessDeniedException, URINotFoundException, SQLException {

		checkModificationAccess(user);
	}

	@Override
	protected void checkDeleteAccess(FlowType bean, User user, HttpServletRequest req, URIParser uriParser) throws AccessDeniedException, URINotFoundException, SQLException {

		checkModificationAccess(user);
	}

	public void checkModificationAccess(User user) throws AccessDeniedException{

		if(!AccessUtils.checkAccess(user, callback)){

			throw new AccessDeniedException("User does not have access to administrate flow types.");
		}
	}

	@Override
	protected void checkShowAccess(FlowType bean, User user, HttpServletRequest req, URIParser uriParser) throws AccessDeniedException, URINotFoundException, SQLException {

		if(!AccessUtils.checkAccess(user, bean.getAdminAccessInterface()) && !AccessUtils.checkAccess(user, callback)){

			throw new AccessDeniedException("User does not have access to requested flow type.");
		}
	}

	@Override
	protected ForegroundModuleResponse beanAdded(FlowType bean, HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Exception {

		callback.getEventHandler().sendEvent(FlowType.class, new CRUDEvent<FlowType>(CRUDAction.ADD, bean), EventTarget.ALL);

		res.sendRedirect(req.getContextPath() + callback.getFullAlias() + "/flowtypes");

		return null;
	}

	@Override
	protected ForegroundModuleResponse beanUpdated(FlowType bean, HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Exception {

		callback.getEventHandler().sendEvent(FlowType.class, new CRUDEvent<FlowType>(CRUDAction.UPDATE, bean), EventTarget.ALL);

		res.sendRedirect(req.getContextPath() + callback.getFullAlias() + "/flowtypes");

		return null;
	}

	@Override
	protected ForegroundModuleResponse beanDeleted(FlowType bean, HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Exception {

		callback.getEventHandler().sendEvent(FlowType.class, new CRUDEvent<FlowType>(CRUDAction.DELETE, bean), EventTarget.ALL);

		return super.beanDeleted(bean, req, res, user, uriParser);
	}
	
	@Override
	protected void appendShowFormData(FlowType bean, Document doc, Element showTypeElement, User user, HttpServletRequest req, HttpServletResponse res, URIParser uriParser) throws SQLException, IOException, Exception {
		
		appendAdminAccess(user, doc, showTypeElement);
		
		if (bean.getAllowedAdminGroupIDs() != null) {
			
			XMLUtils.append(doc, showTypeElement, "AllowedAdminGroups", callback.getGroupHandler().getGroups(bean.getAllowedAdminGroupIDs(), false));
		}
		
		if (bean.getAllowedAdminUserIDs() != null) {
			
			XMLUtils.append(doc, showTypeElement, "AllowedAdminUsers", callback.getUserHandler().getUsers(bean.getAllowedAdminUserIDs(), false, true));
		}
		
		if (bean.getAllowedGroupIDs() != null) {
			
			XMLUtils.append(doc, showTypeElement, "AllowedGroups", callback.getGroupHandler().getGroups(bean.getAllowedGroupIDs(), false));
		}
		
		if (bean.getAllowedUserIDs() != null) {
			
			XMLUtils.append(doc, showTypeElement, "AllowedUsers", callback.getUserHandler().getUsers(bean.getAllowedUserIDs(), false, true));
		}
		
		if (bean.getAllowedQueryTypes() != null) {
			
			XMLUtils.append(doc, showTypeElement, "QueryTypeDescriptors", callback.getQueryHandler().getQueryTypes(bean.getAllowedQueryTypes()));
		}
		
		if (!callback.getFlowTypeExtensionsProviders().isEmpty()) {
			
			for (FlowTypeExtensionProvider extension : callback.getFlowTypeExtensionsProviders()) {
				
				try {
					ViewFragment paymentProviderViewFragment = extension.getShowFlowTypeFragment(bean, req, user, uriParser);
			
					showTypeElement.appendChild(paymentProviderViewFragment.toXML(doc));
					
				} catch (Exception e){
					
					log.error("Error getting getShowFlowTypeFragment view fragment from flow type extension " + extension, e);
				}
			}
		}
		
		XMLUtils.appendNewElement(doc, showTypeElement, "flowFamilyCount", callback.getFlowFamilies(bean.getFlowTypeID()).size());
	}

	@Override
	protected void appendListFormData(Document doc, Element listTypeElement, User user, HttpServletRequest req, URIParser uriParser, List<ValidationError> validationErrors) throws SQLException {

		appendAdminAccess(user, doc, listTypeElement);
	}

	private void appendAdminAccess(User user, Document doc, Element element){

		if(AccessUtils.checkAccess(user, callback)){

			XMLUtils.appendNewElement(doc, element, "AdminAccess");
		}
	}

	@Override
	protected void appendAllBeans(Document doc, Element listTypeElement, User user, HttpServletRequest req, URIParser uriParser, List<ValidationError> validationErrors) throws SQLException {

		List<FlowType> flowTypes = getAllBeans(user, req, uriParser);

		if(CollectionUtils.isEmpty(flowTypes)){

			return;
		}

		Element flowTypesElement = doc.createElement(this.typeElementPluralName);
		listTypeElement.appendChild(flowTypesElement);

		for(FlowType flowType : flowTypes){

			Element flowTypeElement = flowType.toXML(doc);
			flowTypesElement.appendChild(flowTypeElement);

			XMLUtils.appendNewElement(doc, flowTypeElement, "flowFamilyCount", callback.getFlowFamilies(flowType.getFlowTypeID()).size());
		}
	}

	@Override
	protected void appendAddFormData(Document doc, Element addTypeElement, User user, HttpServletRequest req, URIParser uriParser) throws Exception {

		appendFormData(doc, addTypeElement, user, req, uriParser);
	}
	
	@Override
	protected void appendUpdateFormData(FlowType bean, Document doc, Element updateTypeElement, User user, HttpServletRequest req, URIParser uriParser) throws Exception {
		
		appendFormData(doc, updateTypeElement, user, req, uriParser);
		
		if (bean.getAllowedAdminGroupIDs() != null) {
			
			XMLUtils.append(doc, updateTypeElement, "AllowedAdminGroups", callback.getGroupHandler().getGroups(bean.getAllowedAdminGroupIDs(), false));
		}
		
		if (bean.getAllowedAdminUserIDs() != null) {
			
			XMLUtils.append(doc, updateTypeElement, "AllowedAdminUsers", callback.getUserHandler().getUsers(bean.getAllowedAdminUserIDs(), false, true));
		}
		
		if (bean.getAllowedGroupIDs() != null) {
			
			XMLUtils.append(doc, updateTypeElement, "AllowedGroups", callback.getGroupHandler().getGroups(bean.getAllowedGroupIDs(), false));
		}
		
		if (bean.getAllowedUserIDs() != null) {
			
			XMLUtils.append(doc, updateTypeElement, "AllowedUsers", callback.getUserHandler().getUsers(bean.getAllowedUserIDs(), false, true));
		}
		
		if (!callback.getFlowTypeExtensionsProviders().isEmpty()) {
			
			for (FlowTypeExtensionProvider extension : callback.getFlowTypeExtensionsProviders()) {
				
				try {
					
					ViewFragment paymentProviderViewFragment = extension.getUpdateFlowTypeFragment(bean, req, user, uriParser, (ValidationException) req.getAttribute(extension.getProviderID() + "-validationException"));
					
					updateTypeElement.appendChild(paymentProviderViewFragment.toXML(doc));
					
				} catch (Exception e) {
					
					log.error("Error getting getUpdateFlowTypeFragment view fragment from flow type extension " + extension, e);
				}
			}
		}
	}
	
	private void appendFormData(Document doc, Element updateTypeElement, User user, HttpServletRequest req, URIParser uriParser) {

		XMLUtils.append(doc, updateTypeElement, "QueryTypeDescriptors", callback.getQueryHandler().getAvailableQueryTypes());
		
		if (callback.useFlowTypeIconUpload()) {
			
			XMLUtils.appendNewElement(doc, updateTypeElement, "useFlowTypeIconUpload", "true");
		}
	}
	
	@Override
	protected FlowType populateFromUpdateRequest(FlowType flowType, HttpServletRequest req, User user, URIParser uriParser) throws ValidationException, Exception {
		
		boolean extensionErrors = false;
		
		if (!callback.getFlowTypeExtensionsProviders().isEmpty()) {
			
			for (FlowTypeExtensionProvider extension : callback.getFlowTypeExtensionsProviders()) {
				
				try {
					extension.validateUpdateFlowType(flowType, req, user, uriParser);
					
				} catch (ValidationException e) {
					
					extensionErrors = true;
					req.setAttribute(extension.getClass().getName() + "-validationException", e);
				}
			}
			
		}
		
		flowType = super.populateFromUpdateRequest(flowType, req, user, uriParser);
		
		if (extensionErrors) {
			
			throw new ValidationException(new ValidationError("ExtensionErrors"));
		}
		
		return flowType;
	}
	
	@Override
	protected void validateAddPopulation(FlowType bean, HttpServletRequest req, User user, URIParser uriParser) throws ValidationException, SQLException, Exception {
		
		super.validateAddPopulation(bean, req, user, uriParser);
		
		if (callback.useFlowTypeIconUpload()) {
			
			setIcon(req, bean, user);
		}
		
		bean.setIconLastModified(TimeUtils.getCurrentTimestamp());
	}
	
	@Override
	protected void validateUpdatePopulation(FlowType bean, HttpServletRequest req, User user, URIParser uriParser) throws ValidationException, SQLException, Exception {
		
		super.validateUpdatePopulation(bean, req, user, uriParser);
		
		if (callback.useFlowTypeIconUpload()) {
			
			setIcon(req, bean, user);
		}
	}
	
	@Override
	protected void updateFilteredBean(FlowType flowType, HttpServletRequest req, User user, URIParser uriParser) throws Exception {
		
		super.updateFilteredBean(flowType, req, user, uriParser);
		
		if (!callback.getFlowTypeExtensionsProviders().isEmpty()) {
			
			boolean extensionErrors = false;
			
			for (FlowTypeExtensionProvider extension : callback.getFlowTypeExtensionsProviders()) {
				
				try {
					extension.updateFlowType(flowType, req, user, uriParser);
					
				} catch (ValidationException e) {
					
					extensionErrors = true;
					req.setAttribute(extension.getProviderID() + "-validationException", e);
				}
			}
			
			if (extensionErrors) {
				
				throw new ValidationException(new ValidationError("ExtensionErrors"));
			}
		}
	}

	private void setIcon(HttpServletRequest req, FlowType bean, User user) throws ValidationException, IOException, SerialException, SQLException {

		if(req.getParameter("deleteicon") != null){

			bean.setIcon(null);
			bean.setIconFileName(null);
			bean.setIconLastModified(TimeUtils.getCurrentTimestamp());
		}
		
		if (!(req instanceof MultipartRequest)) {

			return;
		}

		MultipartRequest multipartRequest = (MultipartRequest) req;

		if (multipartRequest.getFileCount() > 0 && !StringUtils.isEmpty(multipartRequest.getFile(0).getName())) {

			FileItem file = multipartRequest.getFile(0);

			String lowerCasefileName = file.getName().toLowerCase();

			if (!(lowerCasefileName.endsWith(".png") || lowerCasefileName.endsWith(".jpg") || lowerCasefileName.endsWith(".gif") || lowerCasefileName.endsWith(".bmp"))) {

				throw new ValidationException(new ValidationError("InvalidIconFileFormat"));

			} else {

				BufferedImage image = null;
				
				try {

					image = ImageUtils.getImage(file.get());

					image = ImageUtils.cropAsSquare(image);
					
					if (image.getWidth() > callback.getMaxFlowTypeIconWidth() || image.getHeight() > callback.getMaxFlowTypeIconHeight()) {

						image = ImageUtils.scale(image, callback.getMaxFlowTypeIconHeight(), callback.getMaxFlowTypeIconWidth(), Image.SCALE_SMOOTH, BufferedImage.TYPE_INT_ARGB);

					}

				} catch (Exception e) {

					throw new ValidationException(new ValidationError("UnableToParseIcon"));
				}
				
				ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
				ImageIO.write(image, ImageUtils.PNG, byteArrayOutputStream);
				
				bean.setIcon(new SerialBlob(byteArrayOutputStream.toByteArray()));
				bean.setIconFileName(FileUtils.replaceFileExtension(FilenameUtils.getName(file.getName()), "png"));
				bean.setIconLastModified(TimeUtils.getCurrentTimestamp());
			}
			
		}

	}
	
}
