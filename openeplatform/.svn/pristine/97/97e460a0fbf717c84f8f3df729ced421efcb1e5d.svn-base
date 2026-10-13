package com.nordicpeak.flowengine.cruds;

import java.io.IOException;
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
import se.unlogic.hierarchy.core.utils.crud.IntegerBeanIDParser;
import se.unlogic.hierarchy.core.utils.crud.ModularCRUD;
import se.unlogic.standardutils.dao.CRUDDAO;
import se.unlogic.standardutils.dao.TransactionHandler;
import se.unlogic.standardutils.dao.querys.ObjectQuery;
import se.unlogic.standardutils.populators.IntegerPopulator;
import se.unlogic.standardutils.validation.ValidationError;
import se.unlogic.standardutils.validation.ValidationException;
import se.unlogic.webutils.http.URIParser;
import se.unlogic.webutils.populators.annotated.AnnotatedRequestPopulator;

import com.nordicpeak.flowengine.FlowAdminModule;
import com.nordicpeak.flowengine.beans.EvaluatorDescriptor;
import com.nordicpeak.flowengine.beans.Flow;
import com.nordicpeak.flowengine.beans.QueryDescriptor;
import com.nordicpeak.flowengine.beans.Step;


public class StepCRUD extends ModularCRUD<Step,Integer, User, FlowAdminModule> {

	public StepCRUD(CRUDDAO<Step, Integer> crudDAO, FlowAdminModule callback) {

		super(IntegerBeanIDParser.getInstance(), crudDAO, new AnnotatedRequestPopulator<Step>(Step.class), "Step", "step", "", callback);
		
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
	protected void validateAddPopulation(Step bean, HttpServletRequest req, User user, URIParser uriParser) throws ValidationException, SQLException, Exception {

		bean.setFlow((Flow) req.getAttribute("flow"));
		bean.setSortIndex(getCurrentMaxSortIndex(bean.getFlow(), req) + 1);
	}	
	
	@Override
	protected void checkUpdateAccess(Step bean, User user, HttpServletRequest req, URIParser uriParser) throws AccessDeniedException, URINotFoundException, SQLException {

		callback.checkFlowStructureManipulationAccess(user, bean.getFlow());
	}	
	
	@Override
	protected void checkDeleteAccess(Step bean, User user, HttpServletRequest req, URIParser uriParser) throws AccessDeniedException, URINotFoundException, SQLException {

		callback.checkFlowStructureManipulationAccess(user, bean.getFlow());
	}	
	
	private Integer getCurrentMaxSortIndex(Flow flow, HttpServletRequest req) throws SQLException {

		ObjectQuery<Integer> query = getTransactionHandler(req).getObjectQuery("SELECT MAX(sortIndex) FROM " + callback.getDAOFactory().getStepDAO().getTableName() + " WHERE flowID = ?", IntegerPopulator.getPopulator());
		
		query.setInt(1, flow.getFlowID());
		
		Integer sortIndex = query.executeQuery();
		
		if(sortIndex == null){
			
			return 0;
		}
		
		return sortIndex;
	}	
	
	@Override
	protected ForegroundModuleResponse beanAdded(Step bean, HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Exception {

		callback.addFlowFamilyEvent(callback.getEventStepAddedMessage() + " \"" + bean.getName() + "\"", bean.getFlow(), user);
		
		return beanEvent(bean, req, res, CRUDAction.ADD);
	}

	@Override
	protected ForegroundModuleResponse beanUpdated(Step bean, HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Exception {

		callback.addFlowFamilyEvent(callback.getEventStepUpdatedMessage() + " \"" + bean.getName() + "\"", bean.getFlow(), user);
		
		return beanEvent(bean, req, res, CRUDAction.UPDATE);
	}

	@Override
	protected ForegroundModuleResponse beanDeleted(Step step, HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Exception {

		callback.addFlowFamilyEvent(callback.getEventStepDeletedMessage() + " \"" + step.getName() + "\"", step.getFlow(), user);
		
		return beanEvent(step, req, res, CRUDAction.DELETE);
	}

	private ForegroundModuleResponse beanEvent(Step bean, HttpServletRequest req, HttpServletResponse res, CRUDAction action) throws IOException{
		
		callback.getEventHandler().sendEvent(Step.class, new CRUDEvent<Step>(action, bean), EventTarget.ALL);

		callback.redirectToMethod(req, res, "/showflow/" + bean.getFlow().getFlowID() + "#steps");
		
		return null;
	}
	
	@Override
	public ForegroundModuleResponse list(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser, List<ValidationError> validationErrors) throws Exception {

		return callback.list(req, res, user, uriParser, validationErrors);
	}

	@Override
	protected void deleteFilteredBean(Step step, HttpServletRequest req, User user, URIParser uriParser) throws Exception {

		TransactionHandler transactionHandler = getTransactionHandler(req);
		
		if (step.getQueryDescriptors() != null) {

			for (QueryDescriptor queryDescriptor : step.getQueryDescriptors()) {

				if (queryDescriptor.getEvaluatorDescriptors() != null) {

					for (EvaluatorDescriptor evaluatorDescriptor : queryDescriptor.getEvaluatorDescriptors()) {

						callback.getEvaluationHandler().deleteEvaluator(evaluatorDescriptor, transactionHandler);
					}
				}

				callback.getQueryHandler().deleteQuery(queryDescriptor, transactionHandler);
			}
		}
		
		super.deleteFilteredBean(step, req, user, uriParser);
	}
}
