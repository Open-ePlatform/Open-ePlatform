package com.nordicpeak.flowengine.cruds;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

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
import se.unlogic.standardutils.dao.HighLevelQuery;
import se.unlogic.standardutils.dao.QueryParameterFactory;
import se.unlogic.standardutils.dao.TransactionHandler;
import se.unlogic.standardutils.validation.ValidationException;
import se.unlogic.standardutils.xml.XMLUtils;
import se.unlogic.webutils.http.URIParser;
import se.unlogic.webutils.populators.annotated.AnnotatedRequestPopulator;

import com.nordicpeak.flowengine.FlowAdminModule;
import com.nordicpeak.flowengine.beans.DefaultStandardStatusMapping;
import com.nordicpeak.flowengine.beans.FlowAction;
import com.nordicpeak.flowengine.beans.StandardStatus;


public class StandardStatusCRUD extends IntegerBasedCRUD<StandardStatus, FlowAdminModule> {

	private QueryParameterFactory<FlowAction, String> flowActionIDParamFactory;
	private QueryParameterFactory<DefaultStandardStatusMapping, String> defaultStatusMappingActionIDParamFactory;
	private QueryParameterFactory<DefaultStandardStatusMapping, StandardStatus> defaultStatusMappingStatusParamFactory;

	public StandardStatusCRUD(CRUDDAO<StandardStatus, Integer> crudDAO, FlowAdminModule callback) {

		super(crudDAO, new AnnotatedRequestPopulator<StandardStatus>(StandardStatus.class), "StandardStatus", "StandardStatuses", "standard status", "standard statuses", "/standardstatuses", callback);

		flowActionIDParamFactory = callback.getDAOFactory().getFlowActionDAO().getParamFactory("actionID", String.class);
		defaultStatusMappingActionIDParamFactory = callback.getDAOFactory().getDefaultStandardStatusMappingDAO().getParamFactory("actionID", String.class);
		defaultStatusMappingStatusParamFactory = callback.getDAOFactory().getDefaultStandardStatusMappingDAO().getParamFactory("status", StandardStatus.class);
	}

	@Override
	protected void checkAddAccess(User user, HttpServletRequest req, URIParser uriParser) throws AccessDeniedException, URINotFoundException, SQLException {

		checkAccess(user);
	}

	@Override
	protected void checkUpdateAccess(StandardStatus bean, User user, HttpServletRequest req, URIParser uriParser) throws AccessDeniedException, URINotFoundException, SQLException {

		checkAccess(user);
	}

	@Override
	protected void checkDeleteAccess(StandardStatus bean, User user, HttpServletRequest req, URIParser uriParser) throws AccessDeniedException, URINotFoundException, SQLException {

		checkAccess(user);
	}

	private void checkAccess(User user) throws AccessDeniedException {

		if(!AccessUtils.checkAccess(user, callback)){

			throw new AccessDeniedException("User does not have access to administrate standard statuses");
		}
	}

	@Override
	protected ForegroundModuleResponse beanAdded(StandardStatus bean, HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Exception {

		return beanEvent(bean, req, res, CRUDAction.ADD);
	}

	@Override
	protected ForegroundModuleResponse beanUpdated(StandardStatus bean, HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Exception {

		return beanEvent(bean, req, res, CRUDAction.UPDATE);
	}

	@Override
	protected ForegroundModuleResponse beanDeleted(StandardStatus bean, HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Exception {

		return beanEvent(bean, req, res, CRUDAction.DELETE);
	}

	private ForegroundModuleResponse beanEvent(StandardStatus bean, HttpServletRequest req, HttpServletResponse res, CRUDAction action) throws Exception{

		callback.getEventHandler().sendEvent(StandardStatus.class, new CRUDEvent<StandardStatus>(action, bean), EventTarget.ALL);

		this.redirectToListMethod(req, res, bean);

		return null;
	}

	@Override
	protected void appendAddFormData(Document doc, Element addTypeElement, User user, HttpServletRequest req, URIParser uriParser) throws Exception {

		XMLUtils.append(doc, addTypeElement, "FlowActions", callback.getDAOFactory().getFlowActionDAO().getAll());
	}

	@Override
	protected void appendUpdateFormData(StandardStatus bean, Document doc, Element updateTypeElement, User user, HttpServletRequest req, URIParser uriParser) throws Exception {

		XMLUtils.append(doc, updateTypeElement, "FlowActions", callback.getDAOFactory().getFlowActionDAO().getAll());
	}

	@Override
	protected void validateAddPopulation(StandardStatus bean, HttpServletRequest req, User user, URIParser uriParser) throws ValidationException, SQLException, Exception {

		setDefaultStatusMappings(bean, req);
	}

	@Override
	protected void validateUpdatePopulation(StandardStatus bean, HttpServletRequest req, User user, URIParser uriParser) throws ValidationException, SQLException, Exception {

		setDefaultStatusMappings(bean, req);
	}

	private void setDefaultStatusMappings(StandardStatus bean, HttpServletRequest req) throws SQLException {

		String[] actions = req.getParameterValues("actionID");

		if(actions != null){

			List<FlowAction> flowActions = getFlowActions(Arrays.asList(actions));

			if(flowActions != null){

				ArrayList<DefaultStandardStatusMapping> defaultStatusMappings = new ArrayList<DefaultStandardStatusMapping>(flowActions.size());
				bean.setDefaultStandardStatusMappings(defaultStatusMappings);

				for(FlowAction flowAction : flowActions){

					defaultStatusMappings.add(new DefaultStandardStatusMapping(flowAction.getActionID(),bean));
				}

				return;
			}
		}

		bean.setDefaultStandardStatusMappings(null);
	}

	private List<FlowAction> getFlowActions(List<String> actionIDs) throws SQLException {

		HighLevelQuery<FlowAction> query = new HighLevelQuery<FlowAction>();

		query.addParameter(flowActionIDParamFactory.getWhereInParameter(actionIDs));

		return callback.getDAOFactory().getFlowActionDAO().getAll(query);
	}

	@Override
	protected void addBean(StandardStatus bean, HttpServletRequest req, User user, URIParser uriParser) throws Exception {

		TransactionHandler transactionHandler = null;

		try{
			transactionHandler = callback.getDAOFactory().getTransactionHandler();

			callback.getDAOFactory().getStandardStatusDAO().add(bean, transactionHandler, null);

			if(bean.getDefaultStandardStatusMappings() != null){

				clearPreviousActionMappings(bean, transactionHandler);
				callback.getDAOFactory().getDefaultStandardStatusMappingDAO().addAll(bean.getDefaultStandardStatusMappings(), transactionHandler, null);
			}

			transactionHandler.commit();

		}finally{

			TransactionHandler.autoClose(transactionHandler);
		}
	}

	@Override
	protected void updateBean(StandardStatus bean, HttpServletRequest req, User user, URIParser uriParser) throws Exception {

		TransactionHandler transactionHandler = null;

		try{
			transactionHandler = callback.getDAOFactory().getTransactionHandler();

			callback.getDAOFactory().getStandardStatusDAO().update(bean, transactionHandler, null);

			clearStatusActionMappings(bean, transactionHandler);

			if(bean.getDefaultStandardStatusMappings() != null){

				clearPreviousActionMappings(bean, transactionHandler);
				callback.getDAOFactory().getDefaultStandardStatusMappingDAO().addAll(bean.getDefaultStandardStatusMappings(), transactionHandler, null);
			}

			transactionHandler.commit();

		}finally{

			TransactionHandler.autoClose(transactionHandler);
		}
	}

	private void clearPreviousActionMappings(StandardStatus bean, TransactionHandler transactionHandler) throws SQLException {

		ArrayList<String> actionIDs = new ArrayList<String>(bean.getDefaultStandardStatusMappings().size());

		for(DefaultStandardStatusMapping defaultStatusMapping : bean.getDefaultStandardStatusMappings()){

			actionIDs.add(defaultStatusMapping.getActionID());
		}

		HighLevelQuery<DefaultStandardStatusMapping> query = new HighLevelQuery<DefaultStandardStatusMapping>();

		query.addParameter(defaultStatusMappingActionIDParamFactory.getWhereInParameter(actionIDs));

		callback.getDAOFactory().getDefaultStandardStatusMappingDAO().delete(query,transactionHandler);
	}

	private void clearStatusActionMappings(StandardStatus bean, TransactionHandler transactionHandler) throws SQLException {

		HighLevelQuery<DefaultStandardStatusMapping> query = new HighLevelQuery<DefaultStandardStatusMapping>();

		query.addParameter(defaultStatusMappingStatusParamFactory.getParameter(bean));

		callback.getDAOFactory().getDefaultStandardStatusMappingDAO().delete(query,transactionHandler);
	}
}
