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
import se.unlogic.standardutils.numbers.NumberUtils;
import se.unlogic.standardutils.serialization.SerializationUtils;
import se.unlogic.standardutils.validation.ValidationError;
import se.unlogic.standardutils.validation.ValidationErrorType;
import se.unlogic.standardutils.validation.ValidationException;
import se.unlogic.standardutils.xml.XMLUtils;
import se.unlogic.webutils.http.URIParser;
import se.unlogic.webutils.populators.annotated.AnnotatedRequestPopulator;

import com.nordicpeak.flowengine.FlowAdminModule;
import com.nordicpeak.flowengine.beans.EvaluatorDescriptor;
import com.nordicpeak.flowengine.beans.Flow;
import com.nordicpeak.flowengine.beans.QueryDescriptor;
import com.nordicpeak.flowengine.beans.QueryTypeDescriptor;
import com.nordicpeak.flowengine.interfaces.Query;


public class QueryDescriptorCRUD extends IntegerBasedCRUD<QueryDescriptor, FlowAdminModule> {

	public QueryDescriptorCRUD(CRUDDAO<QueryDescriptor, Integer> crudDAO, FlowAdminModule callback) {

		super(crudDAO, new AnnotatedRequestPopulator<QueryDescriptor>(QueryDescriptor.class), "QueryDescriptor", "query descriptor", "", callback);
		
		setRequirePostForDelete(true);
	}

	@Override
	protected void checkAddAccess(User user, HttpServletRequest req, URIParser uriParser) throws AccessDeniedException, URINotFoundException, SQLException {

		Flow flow = callback.getRequestedFlow(req, user, uriParser);

		if(flow == null){

			throw new URINotFoundException(uriParser);
		}

		callback.checkFlowStructureManipulationAccess(user, flow);

		req.setAttribute("flow", flow);
	}

	@Override
	protected void checkUpdateAccess(QueryDescriptor bean, User user, HttpServletRequest req, URIParser uriParser) throws AccessDeniedException, URINotFoundException, SQLException {

		callback.checkFlowStructureManipulationAccess(user,bean.getStep().getFlow());
	}

	@Override
	protected void checkDeleteAccess(QueryDescriptor bean, User user, HttpServletRequest req, URIParser uriParser) throws AccessDeniedException, URINotFoundException, SQLException {

		callback.checkFlowStructureManipulationAccess(user,bean.getStep().getFlow());
	}

	@Override
	protected void validateAddPopulation(QueryDescriptor bean, HttpServletRequest req, User user, URIParser uriParser) throws ValidationException, SQLException, Exception {

		Flow flow = (Flow)req.getAttribute("flow");

		Integer stepID = NumberUtils.toInt(req.getParameter("stepID"));

		if(stepID == null){

			throw new ValidationException(new ValidationError("stepID", ValidationErrorType.RequiredField));
		}

		bean.setStep(flow.getStep(stepID));

		if(bean.getStep() == null){

			throw new ValidationException(new ValidationError("SelectedStepNotFound"));
			
		}else{
			
			bean.setStep(SerializationUtils.cloneSerializable(bean.getStep()));
			bean.getStep().setFlow(flow);
		}

		String queryTypeID = req.getParameter("queryTypeID");

		if(queryTypeID == null){

			throw new ValidationException(new ValidationError("queryTypeID", ValidationErrorType.RequiredField));
		}

		if(flow.getFlowType().getAllowedQueryTypes() == null || !flow.getFlowType().getAllowedQueryTypes().contains(queryTypeID)){

			throw new ValidationException(new ValidationError("FlowTypeQueryTypeAccessDenied"));
		}

		for(QueryTypeDescriptor queryTypeDescriptor : callback.getQueryHandler().getAvailableQueryTypes()){

			if(queryTypeDescriptor.getQueryTypeID().equals(queryTypeID)){

				bean.setQueryTypeID(queryTypeID);
				break;
			}
		}

		if(bean.getQueryTypeID() == null){

			throw new ValidationException(new ValidationError("SelectedQueryTypeNotFound"));
		}

		bean.setSortIndex(callback.getCurrentMaxSortIndex(bean.getStep()) + 1);
	}

	@Override
	protected void addBean(QueryDescriptor bean, HttpServletRequest req, User user, URIParser uriParser) throws Exception {

		TransactionHandler transactionHandler = null;

		try{
			transactionHandler = callback.getDAOFactory().getTransactionHandler();

			callback.getDAOFactory().getQueryDescriptorDAO().add(bean,transactionHandler,null);

			Query query = callback.getQueryHandler().createQuery(bean, transactionHandler);

			req.setAttribute("query", query);

			transactionHandler.commit();

		}finally{

			TransactionHandler.autoClose(transactionHandler);
		}

		callback.getEventHandler().sendEvent(QueryDescriptor.class, new CRUDEvent<QueryDescriptor>(CRUDAction.ADD, bean), EventTarget.ALL);
	}

	@Override
	protected void deleteBean(QueryDescriptor bean, HttpServletRequest req, User user, URIParser uriParser) throws Exception {

		TransactionHandler transactionHandler = null;

		try{
			transactionHandler = callback.getDAOFactory().getTransactionHandler();

			callback.getDAOFactory().getQueryDescriptorDAO().delete(bean,transactionHandler);

			callback.getQueryHandler().deleteQuery(bean, transactionHandler);

			if(bean.getEvaluatorDescriptors() != null){

				for(EvaluatorDescriptor evaluatorDescriptor : bean.getEvaluatorDescriptors()){

					callback.getEvaluationHandler().deleteEvaluator(evaluatorDescriptor, transactionHandler);
				}
			}
			
			transactionHandler.commit();

		}finally{

			TransactionHandler.autoClose(transactionHandler);
		}

		callback.getEventHandler().sendEvent(QueryDescriptor.class, new CRUDEvent<QueryDescriptor>(CRUDAction.DELETE, bean), EventTarget.ALL);
	}

	@Override
	protected ForegroundModuleResponse beanAdded(QueryDescriptor bean, HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Exception {

		callback.addFlowFamilyEvent(callback.getEventQueryAddedMessage() + " \"" + bean.getName() + "\"", bean.getStep().getFlow(), user);
		
		Query query = (Query)req.getAttribute("query");

		res.sendRedirect(req.getContextPath() + query.getConfigAlias());

		return null;
	}

	@Override
	protected ForegroundModuleResponse beanDeleted(QueryDescriptor bean, HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Exception {

		callback.addFlowFamilyEvent(callback.getEventQueryDeletedMessage() + " \"" + bean.getName() + "\"", bean.getStep().getFlow(), user);
		
		callback.redirectToMethod(req, res, "/showflow/" + bean.getStep().getFlow().getFlowID() + "#steps");

		return null;
	}

	@Override
	protected void appendAddFormData(Document doc, Element addTypeElement, User user, HttpServletRequest req, URIParser uriParser) throws Exception {

		Flow flow = (Flow)req.getAttribute("flow");

		XMLUtils.append(doc, addTypeElement, "Steps", flow.getSteps());

		if(flow.getFlowType().getAllowedQueryTypes() != null){

			XMLUtils.append(doc, addTypeElement, "QueryTypes", callback.getQueryHandler().getQueryTypes(flow.getFlowType().getAllowedQueryTypes()));
		}
		
		if(NumberUtils.isInt(req.getParameter("step"))) {
			
			XMLUtils.appendNewElement(doc, addTypeElement, "SelectedStep", req.getParameter("step"));
		}
	}


	@Override
	public ForegroundModuleResponse showUpdateForm(QueryDescriptor bean, HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser, ValidationException validationException) throws Exception {

		Query query = callback.getQueryHandler().getQuery(bean, false);

		res.sendRedirect(req.getContextPath() + query.getConfigAlias());

		return null;
	}

	@Override
	public ForegroundModuleResponse list(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser, List<ValidationError> validationErrors) throws Exception {

		return callback.list(req, res, user, uriParser, validationErrors);
	}
}
