package com.nordicpeak.flowengine.runnables;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Collection;

import org.apache.log4j.Logger;

import se.unlogic.standardutils.dao.AnnotatedDAO;
import se.unlogic.standardutils.dao.HighLevelQuery;
import se.unlogic.standardutils.dao.QueryParameterFactory;
import se.unlogic.standardutils.dao.querys.ArrayListQuery;
import se.unlogic.standardutils.populators.IntegerPopulator;

import com.nordicpeak.flowengine.FlowAdminModule;
import com.nordicpeak.flowengine.beans.Flow;
import com.nordicpeak.flowengine.beans.FlowInstance;
import com.nordicpeak.flowengine.dao.FlowEngineDAOFactory;

public class StaleFlowInstancesRemover implements Runnable {
	
	private static final Logger log = Logger.getLogger(StaleFlowInstancesRemover.class);
	
//	private final FlowAdminModule flowAdminModule;
	private final AnnotatedDAO<FlowInstance> flowInstanceDAO;
	
	private final QueryParameterFactory<FlowInstance, Integer> flowInstanceIDParamFactory;
	
	public StaleFlowInstancesRemover(FlowAdminModule flowAdminModule, FlowEngineDAOFactory daoFactory) {
		super();
		
		//this.flowAdminModule = flowAdminModule;
		
		flowInstanceDAO = daoFactory.getFlowInstanceDAO();
		
		flowInstanceIDParamFactory = daoFactory.getFlowInstanceDAO().getParamFactory("flowInstanceID", Integer.class);
	}
	
	@Override
	public void run() {
		
		try {
			Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.DATE, -1);
			
			Timestamp yesterday = new Timestamp(calendar.getTimeInMillis());
			
			//@formatter:off
			String sql = "SELECT f.flowInstanceID f FROM flowengine_flow_instances f " +
			             "LEFT JOIN flowengine_flow_instance_owners o ON f.flowInstanceID = o.flowInstanceID " +
			             "WHERE f.firstSubmitted IS NULL AND f.poster IS NULL AND o.userID IS NULL AND f.added < ?";
			//@formatter:on
			
			ArrayListQuery<Integer> idQuery = new ArrayListQuery<Integer>(flowInstanceDAO.getDataSource(), sql, IntegerPopulator.getPopulator());
			idQuery.setTimestamp(1, yesterday);
			
			Collection<Integer> flowInstanceIDs = idQuery.executeQuery();
			
			if (flowInstanceIDs != null) {
				
				log.warn("Found " + flowInstanceIDs.size() + " stale flow instances");
				
				for (Integer flowInstanceID : flowInstanceIDs) {
					
					FlowInstance flowInstance = getFlowInstance(flowInstanceID);
					
					log.info("Detected stale flow instance: " + flowInstance);
					
//					log.info("Removing stale flow instance: " + flowInstance);
//
//					flowInstanceDAO.delete(flowInstance);
//
//					if (flowInstance.getStatus().getContentType() == ContentType.NEW) {
//
//						AbortedFlowInstance abortedFlowInstance = new AbortedFlowInstance();
//
//						abortedFlowInstance.setFlowInstanceID(flowInstance.getFlowInstanceID());
//						abortedFlowInstance.setAdded(TimeUtils.getCurrentTimestamp());
//						abortedFlowInstance.setFlowFamilyID(flowInstance.getFlow().getFlowFamily().getFlowFamilyID());
//						abortedFlowInstance.setFlowID(flowInstance.getFlow().getFlowID());
//						abortedFlowInstance.setStepID(flowInstance.getStepID());
//
//						log.info("Adding aborted flow instance entry for flow " + flowInstance.getFlow());
//						flowAdminModule.getDAOFactory().getAbortedFlowInstanceDAO().add(abortedFlowInstance);
//					}
//
//					flowAdminModule.getEventHandler().sendEvent(FlowInstance.class, new CRUDEvent<FlowInstance>(CRUDAction.DELETE, flowInstance), EventTarget.ALL);
				}
				
			} else {
				
				log.info("Found no stale flow instances");
			}
			
		} catch (Throwable t) {
			log.error("Error removing stale flow instances", t);
		}
	}
	
	private FlowInstance getFlowInstance(int flowInstanceID) throws SQLException {
		
		HighLevelQuery<FlowInstance> query = new HighLevelQuery<FlowInstance>(FlowInstance.MANAGERS_RELATION, FlowInstance.STATUS_RELATION, FlowInstance.FLOW_RELATION, Flow.FLOW_FAMILY_RELATION);
		query.addParameter(flowInstanceIDParamFactory.getParameter(flowInstanceID));
		
		return flowInstanceDAO.get(query);
	}
	
}
