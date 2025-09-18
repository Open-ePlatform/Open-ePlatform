package com.nordicpeak.flowengine;

import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import se.unlogic.hierarchy.core.annotations.InstanceManagerDependency;
import se.unlogic.hierarchy.core.annotations.WebPublic;
import se.unlogic.hierarchy.core.beans.SimpleForegroundModuleResponse;
import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.hierarchy.core.enums.CRUDAction;
import se.unlogic.hierarchy.core.enums.EventTarget;
import se.unlogic.hierarchy.core.events.CRUDEvent;
import se.unlogic.hierarchy.core.interfaces.ForegroundModuleResponse;
import se.unlogic.hierarchy.foregroundmodules.AnnotatedForegroundModule;
import se.unlogic.standardutils.collections.CollectionUtils;
import se.unlogic.standardutils.dao.HighLevelQuery;
import se.unlogic.standardutils.dao.QueryParameterFactory;
import se.unlogic.standardutils.dao.TransactionHandler;
import se.unlogic.standardutils.numbers.NumberUtils;
import se.unlogic.standardutils.random.RandomUtils;
import se.unlogic.webutils.http.URIParser;

import com.nordicpeak.flowengine.beans.EvaluatorDescriptor;
import com.nordicpeak.flowengine.beans.Flow;
import com.nordicpeak.flowengine.beans.FlowFamily;
import com.nordicpeak.flowengine.beans.FlowInstance;
import com.nordicpeak.flowengine.beans.QueryDescriptor;
import com.nordicpeak.flowengine.beans.Step;
import com.nordicpeak.flowengine.dao.FlowEngineDAOFactory;
import com.nordicpeak.flowengine.interfaces.EvaluationHandler;
import com.nordicpeak.flowengine.interfaces.QueryHandler;

public class FlowDeleterModule extends AnnotatedForegroundModule {

	private static final List<Field> FLOW_RELATIONS = CollectionUtils.getList(Flow.STEPS_RELATION, Flow.FLOW_FAMILY_RELATION, Step.QUERY_DESCRIPTORS_RELATION, QueryDescriptor.EVALUATOR_DESCRIPTORS_RELATION);

	@InstanceManagerDependency(required = true)
	protected QueryHandler queryHandler;

	@InstanceManagerDependency(required = true)
	protected EvaluationHandler evaluationHandler;
	
	protected FlowEngineDAOFactory daoFactory;

	protected QueryParameterFactory<Flow, FlowFamily> flowFlowFamilyParamFactory;
	protected QueryParameterFactory<Flow, Integer> flowIDParamFactory;
	protected QueryParameterFactory<FlowInstance, Flow> flowInstanceFlowParamFactory;
	
	private int randomNumber = RandomUtils.getRandomInt(10000, 10000000);

	@Override
	protected void createDAOs(DataSource dataSource) throws Exception {

		this.daoFactory = new FlowEngineDAOFactory(dataSource, systemInterface.getUserHandler(), systemInterface.getGroupHandler());

		flowFlowFamilyParamFactory = daoFactory.getFlowDAO().getParamFactory("flowFamily", FlowFamily.class);
		flowIDParamFactory = daoFactory.getFlowDAO().getParamFactory("flowID", Integer.class);
		flowInstanceFlowParamFactory = daoFactory.getFlowInstanceDAO().getParamFactory("flow", Flow.class);
	}

	@Override
	public synchronized ForegroundModuleResponse defaultMethod(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Throwable {

		Integer submittedID = NumberUtils.toInt(req.getParameter("code"));

		if (submittedID == null || submittedID != randomNumber) {

			return new SimpleForegroundModuleResponse(getHTML("Flow deleter (use with caution!)", "Submit the code " + randomNumber + " to delete all flows without flow instances."));
		}

		List<Flow> deletedFlows = null;
		List<FlowFamily> deletedFlowFamilies = null;
		
		int skippedCount = 0;

		log.info("User " + user + " deleting all flows without flow instances...");

		TransactionHandler transactionHandler = null;

		try {
			transactionHandler = new TransactionHandler(dataSource);

			HighLevelQuery<Flow> query = new HighLevelQuery<>();
			
			query.setRelations(FLOW_RELATIONS);
			
			List<Flow> flows = this.daoFactory.getFlowDAO().getAll(query, transactionHandler);

			if(flows != null){

				deletedFlows = new ArrayList<Flow>(flows.size());
				deletedFlowFamilies = new ArrayList<FlowFamily>(flows.size()/2);
				
				for(Flow flow : flows){
					
					if(hasFlowInstances(flow)){
						
						log.info("Flow " + flow + " has one or more flow instances, skipping flow.");
						
						skippedCount++;
						
						continue;
					}
					
					this.daoFactory.getFlowDAO().delete(flow, transactionHandler);

					if (flow.getSteps() != null) {

						for (Step step : flow.getSteps()) {

							if (step.getQueryDescriptors() != null) {

								for (QueryDescriptor queryDescriptor : step.getQueryDescriptors()) {

									if (queryDescriptor.getEvaluatorDescriptors() != null) {

										for (EvaluatorDescriptor evaluatorDescriptor : queryDescriptor.getEvaluatorDescriptors()) {

											evaluationHandler.deleteEvaluator(evaluatorDescriptor, transactionHandler);
										}
									}

									queryHandler.deleteQuery(queryDescriptor, transactionHandler);
								}
							}
						}
					}

					deletedFlows.add(flow);
					
					//Check if the flow family has any more flow, else delete the family too
					if (!hasFlows(flow.getFlowFamily(), transactionHandler)) {

						this.daoFactory.getFlowFamilyDAO().delete(flow.getFlowFamily(), transactionHandler);
						
						deletedFlowFamilies.add(flow.getFlowFamily());
					}
				}
			}
			
			transactionHandler.commit();
			
			if(!CollectionUtils.isEmpty(deletedFlows)){
				
				systemInterface.getEventHandler().sendEvent(Flow.class, new CRUDEvent<Flow>(Flow.class, CRUDAction.DELETE, deletedFlows), EventTarget.ALL);
				
				if(!CollectionUtils.isEmpty(deletedFlowFamilies)){
					
					systemInterface.getEventHandler().sendEvent(FlowFamily.class, new CRUDEvent<FlowFamily>(FlowFamily.class, CRUDAction.DELETE, deletedFlowFamilies), EventTarget.ALL);
				}
			}

		} finally {

			randomNumber = RandomUtils.getRandomInt(10000, 10000000);

			TransactionHandler.autoClose(transactionHandler);
		}

		return new SimpleForegroundModuleResponse(getHTML("Flows deleted", CollectionUtils.getSize(deletedFlows) + " flows deleted, " + skippedCount + " flows skipped."));
	}

	@WebPublic
	public synchronized ForegroundModuleResponse deleteFlowsByID(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Throwable {

		Integer submittedID = NumberUtils.toInt(req.getParameter("code"));
		
		List<Integer> flowIDs = NumberUtils.toInt(req.getParameterValues("id"));

		if (submittedID == null || submittedID != randomNumber || flowIDs == null) {

			return new SimpleForegroundModuleResponse(getHTML("Flow deleter (use with caution!)", "Submit the code " + randomNumber + " and id parameters to delete selected flows without flow instances."));
		}

		List<Flow> deletedFlows = null;
		List<FlowFamily> deletedFlowFamilies = null;
		
		int skippedCount = 0;

		log.info("User " + user + " deleting flows " + flowIDs.toString() + " that don't have any flow instances...");

		TransactionHandler transactionHandler = null;

		try {
			transactionHandler = new TransactionHandler(dataSource);

			HighLevelQuery<Flow> query = new HighLevelQuery<>();
			
			query.setRelations(FLOW_RELATIONS);
			
			query.addParameter(flowIDParamFactory.getWhereInParameter(flowIDs));
			
			List<Flow> flows = this.daoFactory.getFlowDAO().getAll(query, transactionHandler);

			if(flows != null){

				deletedFlows = new ArrayList<Flow>(flows.size());
				deletedFlowFamilies = new ArrayList<FlowFamily>(flows.size()/2);
				
				for(Flow flow : flows){
					
					if(hasFlowInstances(flow)){
						
						log.info("Flow " + flow + " has one or more flow instances, skipping flow.");
						
						skippedCount++;
						
						continue;
					}
					
					this.daoFactory.getFlowDAO().delete(flow, transactionHandler);

					if (flow.getSteps() != null) {

						for (Step step : flow.getSteps()) {

							if (step.getQueryDescriptors() != null) {

								for (QueryDescriptor queryDescriptor : step.getQueryDescriptors()) {

									if (queryDescriptor.getEvaluatorDescriptors() != null) {

										for (EvaluatorDescriptor evaluatorDescriptor : queryDescriptor.getEvaluatorDescriptors()) {

											evaluationHandler.deleteEvaluator(evaluatorDescriptor, transactionHandler);
										}
									}

									queryHandler.deleteQuery(queryDescriptor, transactionHandler);
								}
							}
						}
					}

					deletedFlows.add(flow);
					
					//Check if the flow family has any more flow, else delete the family too
					if (!hasFlows(flow.getFlowFamily(), transactionHandler)) {

						this.daoFactory.getFlowFamilyDAO().delete(flow.getFlowFamily(), transactionHandler);
						
						deletedFlowFamilies.add(flow.getFlowFamily());
					}
				}
			}
			
			transactionHandler.commit();
			
			if(!CollectionUtils.isEmpty(deletedFlows)){
				
				systemInterface.getEventHandler().sendEvent(Flow.class, new CRUDEvent<Flow>(Flow.class, CRUDAction.DELETE, deletedFlows), EventTarget.ALL);
				
				if(!CollectionUtils.isEmpty(deletedFlowFamilies)){
					
					systemInterface.getEventHandler().sendEvent(FlowFamily.class, new CRUDEvent<FlowFamily>(FlowFamily.class, CRUDAction.DELETE, deletedFlowFamilies), EventTarget.ALL);
				}
			}

		} finally {

			randomNumber = RandomUtils.getRandomInt(10000, 10000000);

			TransactionHandler.autoClose(transactionHandler);
		}

		return new SimpleForegroundModuleResponse(getHTML("Flows deleted", CollectionUtils.getSize(deletedFlows) + " flows deleted, " + skippedCount + " flows skipped."));
	}
	
	private boolean hasFlowInstances(Flow flow) throws SQLException {

		HighLevelQuery<FlowInstance> query = new HighLevelQuery<FlowInstance>(); 
		
		query.addParameter(flowInstanceFlowParamFactory.getParameter(flow));
		
		return daoFactory.getFlowInstanceDAO().getBoolean(query);
	}

	private String getHTML(String title, String message) {

		return "<div class=\"contentitem\"><h1>" + title + "</h1><p>" + message + "</p></div>";
	}

	public boolean hasFlows(FlowFamily flowFamily, TransactionHandler transactionHandler) throws SQLException {

		HighLevelQuery<Flow> query = new HighLevelQuery<Flow>();

		query.addParameter(flowFlowFamilyParamFactory.getParameter(flowFamily));

		return daoFactory.getFlowDAO().getBoolean(query, transactionHandler);
	}
}
