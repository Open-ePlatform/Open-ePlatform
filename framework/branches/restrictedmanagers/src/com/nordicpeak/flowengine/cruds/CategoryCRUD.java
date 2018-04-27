package com.nordicpeak.flowengine.cruds;

import java.sql.SQLException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.hierarchy.core.enums.CRUDAction;
import se.unlogic.hierarchy.core.enums.EventTarget;
import se.unlogic.hierarchy.core.events.CRUDEvent;
import se.unlogic.hierarchy.core.exceptions.AccessDeniedException;
import se.unlogic.hierarchy.core.exceptions.URINotFoundException;
import se.unlogic.hierarchy.core.interfaces.ForegroundModuleResponse;
import se.unlogic.hierarchy.core.utils.AccessUtils;
import se.unlogic.hierarchy.core.utils.IntegerBasedCRUD;
import se.unlogic.standardutils.dao.CRUDDAO;
import se.unlogic.standardutils.validation.ValidationError;
import se.unlogic.standardutils.validation.ValidationException;
import se.unlogic.webutils.http.URIParser;
import se.unlogic.webutils.populators.annotated.AnnotatedRequestPopulator;

import com.nordicpeak.flowengine.FlowAdminModule;
import com.nordicpeak.flowengine.beans.Category;
import com.nordicpeak.flowengine.beans.FlowType;


public class CategoryCRUD extends IntegerBasedCRUD<Category, FlowAdminModule> {

	public CategoryCRUD(CRUDDAO<Category, Integer> crudDAO, FlowAdminModule callback) {

		super(crudDAO, new AnnotatedRequestPopulator<Category>(Category.class), "Category", "category", "/flowtype", callback);
	}

	@Override
	protected void validateAddPopulation(Category bean, HttpServletRequest req, User user, URIParser uriParser) throws ValidationException, SQLException, Exception {

		FlowType flowType = (FlowType)req.getAttribute("flowType");

		bean.setFlowType(flowType);
	}

	@Override
	protected void checkAddAccess(User user, HttpServletRequest req, URIParser uriParser) throws AccessDeniedException, URINotFoundException, SQLException {

		FlowType flowType = (FlowType)req.getAttribute("flowType");

		checkModificationAccess(user, flowType);
	}

	@Override
	protected void checkUpdateAccess(Category bean, User user, HttpServletRequest req, URIParser uriParser) throws AccessDeniedException, URINotFoundException, SQLException {

		checkModificationAccess(user, bean.getFlowType());
	}

	@Override
	protected void checkDeleteAccess(Category bean, User user, HttpServletRequest req, URIParser uriParser) throws AccessDeniedException, URINotFoundException, SQLException {

		checkModificationAccess(user, bean.getFlowType());
	}

	public void checkModificationAccess(User user, FlowType flowType) throws AccessDeniedException{

		if(!AccessUtils.checkAccess(user, callback) && !AccessUtils.checkAccess(user, flowType.getAdminAccessInterface())){

			throw new AccessDeniedException("User does not have access to flow type " + flowType);
		}
	}

	@Override
	protected void redirectToListMethod(HttpServletRequest req, HttpServletResponse res, Category bean) throws Exception {

		res.sendRedirect(req.getContextPath() + callback.getFullAlias() + listMethodAlias + "/" + bean.getFlowType().getFlowTypeID());
	}

	@Override
	public ForegroundModuleResponse list(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser, List<ValidationError> validationErrors) throws Exception {

		return callback.getFlowTypeCRUD().list(req, res, user, uriParser, validationErrors);
	}

	@Override
	protected ForegroundModuleResponse beanAdded(Category bean, HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Exception {

		callback.getEventHandler().sendEvent(Category.class, new CRUDEvent<Category>(CRUDAction.ADD, bean), EventTarget.ALL);

		return super.beanAdded(bean, req, res, user, uriParser);
	}

	@Override
	protected ForegroundModuleResponse beanUpdated(Category bean, HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Exception {

		callback.getEventHandler().sendEvent(Category.class, new CRUDEvent<Category>(CRUDAction.UPDATE, bean), EventTarget.ALL);

		return super.beanUpdated(bean, req, res, user, uriParser);
	}

	@Override
	protected ForegroundModuleResponse beanDeleted(Category bean, HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Exception {

		callback.getEventHandler().sendEvent(Category.class, new CRUDEvent<Category>(CRUDAction.DELETE, bean), EventTarget.ALL);

		return super.beanDeleted(bean, req, res, user, uriParser);
	}

}
