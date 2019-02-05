package com.nordicpeak.flowengine.cruds;

import java.sql.SQLException;
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
import se.unlogic.hierarchy.core.utils.IntegerBasedCRUD;
import se.unlogic.standardutils.dao.CRUDDAO;
import se.unlogic.standardutils.dao.TransactionHandler;
import se.unlogic.standardutils.dao.querys.ObjectQuery;
import se.unlogic.standardutils.populators.IntegerPopulator;
import se.unlogic.standardutils.validation.ValidationError;
import se.unlogic.standardutils.validation.ValidationErrorType;
import se.unlogic.standardutils.validation.ValidationException;
import se.unlogic.standardutils.xml.XMLUtils;
import se.unlogic.webutils.http.URIParser;
import se.unlogic.webutils.populators.annotated.AnnotatedRequestPopulator;

import com.nordicpeak.flowengine.FlowAdminModule;
import com.nordicpeak.flowengine.beans.EvaluatorDescriptor;
import com.nordicpeak.flowengine.beans.EvaluatorTypeDescriptor;
import com.nordicpeak.flowengine.beans.QueryDescriptor;
import com.nordicpeak.flowengine.interfaces.Evaluator;
import com.nordicpeak.flowengine.interfaces.Query;


public class EvaluatorDescriptorCRUD extends IntegerBasedCRUD<EvaluatorDescriptor, FlowAdminModule> {

	public EvaluatorDescriptorCRUD(CRUDDAO<EvaluatorDescriptor, Integer> crudDAO, FlowAdminModule callback) {

		super(crudDAO, new AnnotatedRequestPopulator<EvaluatorDescriptor>(EvaluatorDescriptor.class), "EvaluatorDescriptor", "evaluator descriptor", "", callback);
	}

	@Override
	protected void checkAddAccess(User user, HttpServletRequest req, URIParser uriParser) throws AccessDeniedException, URINotFoundException, SQLException {

		QueryDescriptor queryDescriptor = callback.getRequestedQueryDescriptor(req, user, uriParser);

		if(queryDescriptor == null){

			throw new URINotFoundException(uriParser);
		}

		callback.checkFlowStructureManipulationAccess(user, queryDescriptor.getStep().getFlow());

		req.setAttribute("queryDescriptor", queryDescriptor);
	}

	@Override
	protected void checkUpdateAccess(EvaluatorDescriptor bean, User user, HttpServletRequest req, URIParser uriParser) throws AccessDeniedException, URINotFoundException, SQLException {

		callback.checkFlowStructureManipulationAccess(user,bean.getQueryDescriptor().getStep().getFlow());
	}

	@Override
	protected void checkDeleteAccess(EvaluatorDescriptor bean, User user, HttpServletRequest req, URIParser uriParser) throws AccessDeniedException, URINotFoundException, SQLException {

		callback.checkFlowStructureManipulationAccess(user,bean.getQueryDescriptor().getStep().getFlow());
	}

	@Override
	protected void validateAddPopulation(EvaluatorDescriptor bean, HttpServletRequest req, User user, URIParser uriParser) throws ValidationException, SQLException, Exception {

		QueryDescriptor queryDescriptor = (QueryDescriptor)req.getAttribute("queryDescriptor");

		Query query = callback.getQueryHandler().getQuery(queryDescriptor, false);

		String evaluatorTypeID = req.getParameter("evaluatorTypeID");

		if(evaluatorTypeID == null){

			throw new ValidationException(new ValidationError("evaluatorTypeID", ValidationErrorType.RequiredField));
		}

		for(EvaluatorTypeDescriptor evaluatorTypeDescriptor : callback.getEvaluationHandler().getAvailableEvaluatorTypes(query.getClass())){

			if(evaluatorTypeDescriptor.getEvaluatorTypeID().equals(evaluatorTypeID)){

				bean.setEvaluatorTypeID(evaluatorTypeID);
			}
		}

		if(bean.getEvaluatorTypeID() == null){

			throw new ValidationException(new ValidationError("SelectedEvaluatorTypeNotFound"));
		}

		bean.setQueryDescriptor(queryDescriptor);
		bean.setSortIndex(getCurrentMaxSortIndex(queryDescriptor) + 1);
	}

	private int getCurrentMaxSortIndex(QueryDescriptor queryDescriptor) throws SQLException {

		ObjectQuery<Integer> evaluator = new ObjectQuery<Integer>(callback.getDataSource(), "SELECT MAX(sortIndex) FROM " + callback.getDAOFactory().getEvaluatorDescriptorDAO().getTableName() + " WHERE queryID = ?", IntegerPopulator.getPopulator());

		evaluator.setInt(1, queryDescriptor.getQueryID());

		Integer sortIndex = evaluator.executeQuery();

		if(sortIndex == null){

			return 0;
		}

		return sortIndex;
	}

	@Override
	protected void addBean(EvaluatorDescriptor bean, HttpServletRequest req, User user, URIParser uriParser) throws Exception {

		TransactionHandler transactionHandler = null;

		try{
			transactionHandler = callback.getDAOFactory().getTransactionHandler();

			callback.getDAOFactory().getEvaluatorDescriptorDAO().add(bean,transactionHandler,null);

			Evaluator evaluator = callback.getEvaluationHandler().createEvaluator(bean, transactionHandler);

			req.setAttribute("evaluator", evaluator);

			transactionHandler.commit();

		}finally{

			TransactionHandler.autoClose(transactionHandler);
		}

		callback.getEventHandler().sendEvent(EvaluatorDescriptor.class, new CRUDEvent<EvaluatorDescriptor>(CRUDAction.ADD, bean), EventTarget.ALL);
	}

	@Override
	protected void deleteBean(EvaluatorDescriptor bean, HttpServletRequest req, User user, URIParser uriParser) throws Exception {

		TransactionHandler transactionHandler = null;

		try{
			transactionHandler = callback.getDAOFactory().getTransactionHandler();

			callback.getDAOFactory().getEvaluatorDescriptorDAO().delete(bean,transactionHandler);

			callback.getEvaluationHandler().deleteEvaluator(bean, transactionHandler);

			transactionHandler.commit();

		}finally{

			TransactionHandler.autoClose(transactionHandler);
		}

		callback.getEventHandler().sendEvent(EvaluatorDescriptor.class, new CRUDEvent<EvaluatorDescriptor>(CRUDAction.DELETE, bean), EventTarget.ALL);
	}

	@Override
	protected ForegroundModuleResponse beanAdded(EvaluatorDescriptor bean, HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Exception {

		callback.addFlowFamilyEvent(callback.getEventEvaluatorAddedMessage() + " \"" + bean.getName() + "\"", bean.getQueryDescriptor().getStep().getFlow(), user);
		
		Evaluator evaluator = (Evaluator)req.getAttribute("evaluator");

		res.sendRedirect(req.getContextPath() + evaluator.getConfigAlias());

		return null;
	}

	@Override
	protected ForegroundModuleResponse beanDeleted(EvaluatorDescriptor bean, HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Exception {

		callback.addFlowFamilyEvent(callback.getEventEvaluatorDeletedMessage() + " \"" + bean.getName() + "\"", bean.getQueryDescriptor().getStep().getFlow(), user);
		
		callback.redirectToMethod(req, res, "/showflow/" + bean.getQueryDescriptor().getStep().getFlow().getFlowID() + "#steps");

		return null;
	}


	@Override
	protected void appendAddFormData(Document doc, Element addTypeElement, User user, HttpServletRequest req, URIParser uriParser) throws Exception {

		QueryDescriptor queryDescriptor = (QueryDescriptor)req.getAttribute("queryDescriptor");

		addTypeElement.appendChild(queryDescriptor.toXML(doc));

		Query query = callback.getQueryHandler().getQuery(queryDescriptor, false);

		XMLUtils.append(doc, addTypeElement, "EvaluatorTypes", callback.getEvaluationHandler().getAvailableEvaluatorTypes(query.getClass()));
	}

	@Override
	public ForegroundModuleResponse showUpdateForm(EvaluatorDescriptor bean, HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser, ValidationException validationException) throws Exception {

		Evaluator evaluator = callback.getEvaluationHandler().getEvaluator(bean, null);

		res.sendRedirect(req.getContextPath() + evaluator.getConfigAlias());

		return null;
	}

	@Override
	public ForegroundModuleResponse list(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser, List<ValidationError> validationErrors) throws Exception {

		return callback.list(req, res, user, uriParser, validationErrors);
	}
}
