package com.nordicpeak.flowengine.cruds;

import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.hierarchy.core.exceptions.AccessDeniedException;
import se.unlogic.hierarchy.core.interfaces.ForegroundModuleResponse;
import se.unlogic.hierarchy.core.utils.GenericCRUD;
import se.unlogic.standardutils.dao.AnnotatedDAOWrapper;
import se.unlogic.standardutils.validation.ValidationError;
import se.unlogic.standardutils.validation.ValidationException;
import se.unlogic.webutils.http.BeanRequestPopulator;
import se.unlogic.webutils.http.URIParser;

import com.nordicpeak.flowengine.TextTagAdminModule;
import com.nordicpeak.flowengine.beans.TextTag;


public class TextTagCRUD extends GenericCRUD<TextTag, String, User, TextTagAdminModule> {

	protected TextTagAdminModule textTagModule;
	
	protected AnnotatedDAOWrapper<TextTag, String> textTagDAO;
	
	public TextTagCRUD(AnnotatedDAOWrapper<TextTag, String> crudDAO, BeanRequestPopulator<TextTag> populator, TextTagAdminModule textTagModule) {

		super(crudDAO, populator, "TextTag", "text tag", "/", textTagModule);
		
		this.textTagModule = textTagModule;
		this.textTagDAO = crudDAO;
		
	}

	@Override
	protected ForegroundModuleResponse beanAdded(TextTag bean, HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Exception {

		textTagModule.cacheSiteProfileSettings();
		
		callback.ensureDefaultValue(bean);
		
		return super.beanAdded(bean, req, res, user, uriParser);
	}

	@Override
	protected ForegroundModuleResponse beanUpdated(TextTag bean, HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Exception {

		textTagModule.cacheSiteProfileSettings();
		
		callback.ensureDefaultValue(bean);
		
		return super.beanUpdated(bean, req, res, user, uriParser);
	}

	@Override
	protected ForegroundModuleResponse beanDeleted(TextTag bean, HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Exception {

		textTagModule.cacheSiteProfileSettings();
		
		return super.beanDeleted(bean, req, res, user, uriParser);
	}

	@Override
	protected void validateAddPopulation(TextTag bean, HttpServletRequest req, User user, URIParser uriParser) throws ValidationException, SQLException, Exception {

		validateName(bean);	
		
	}

	@Override
	protected void validateUpdatePopulation(TextTag bean, HttpServletRequest req, User user, URIParser uriParser) throws ValidationException, SQLException, Exception {

		validateName(bean);
		
	}

	protected void validateName(TextTag bean) throws SQLException, ValidationException {
		
		TextTag textTag = crudDAO.get(bean.getName());
		
		if(textTag != null && (bean.getTextTagID() == null || !textTag.getTextTagID().equals(bean.getTextTagID()))) {
			
			throw new ValidationException(new ValidationError("TextTagNameExists"));
			
		}
		
	}
	
	@Override
	public TextTag getRequestedBean(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser, String getMode) throws SQLException, AccessDeniedException {

		String beanID = null;
		
		if (uriParser.size() > 2 && (beanID = uriParser.get(2))!= null) {

			return crudDAO.get(beanID);
		}

		return null;
	}
	
}
