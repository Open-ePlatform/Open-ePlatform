package com.nordicpeak.flowengine.runnables;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.hierarchy.core.enums.CRUDAction;
import se.unlogic.hierarchy.core.enums.EventTarget;
import se.unlogic.hierarchy.core.events.CRUDEvent;
import se.unlogic.standardutils.collections.CollectionUtils;
import se.unlogic.standardutils.dao.AnnotatedDAO;
import se.unlogic.standardutils.dao.HighLevelQuery;
import se.unlogic.standardutils.dao.QueryParameterFactory;
import se.unlogic.standardutils.dao.RelationQuery;
import se.unlogic.standardutils.dao.TransactionHandler;
import se.unlogic.standardutils.dao.querys.ArrayListQuery;
import se.unlogic.standardutils.date.DateUtils;
import se.unlogic.standardutils.populators.IntegerPopulator;
import se.unlogic.standardutils.time.TimeUtils;

import com.nordicpeak.flowengine.FlowAdminModule;
import com.nordicpeak.flowengine.beans.Flow;
import com.nordicpeak.flowengine.beans.FlowFamily;
import com.nordicpeak.flowengine.beans.FlowFamilyManager;
import com.nordicpeak.flowengine.beans.FlowInstance;
import com.nordicpeak.flowengine.beans.FlowInstanceEvent;
import com.nordicpeak.flowengine.dao.FlowEngineDAOFactory;
import com.nordicpeak.flowengine.enums.EventType;
import com.nordicpeak.flowengine.events.ManagerExpiredEvent;
import com.nordicpeak.flowengine.events.ManagersChangedEvent;

public class ExpiredManagerRemover implements Runnable {
	
	private static final Logger log = Logger.getLogger(ExpiredManagerRemover.class);
	private static final RelationQuery UPDATE_QUERY = new RelationQuery(FlowFamily.MANAGER_USERS_RELATION);
	
	private final FlowAdminModule flowAdminModule;
	private final AnnotatedDAO<FlowFamily> flowFamilyDAO;
	private final AnnotatedDAO<FlowInstance> flowInstanceDAO;
	
	private final QueryParameterFactory<FlowInstance, Integer> flowInstanceIDParamFactory;
	
	public ExpiredManagerRemover(FlowAdminModule flowAdminModule, FlowEngineDAOFactory daoFactory) {
		super();
		
		this.flowAdminModule = flowAdminModule;
		
		flowFamilyDAO = daoFactory.getFlowFamilyDAO();
		flowInstanceDAO = daoFactory.getFlowInstanceDAO();
		
		flowInstanceIDParamFactory = daoFactory.getFlowInstanceDAO().getParamFactory("flowInstanceID", Integer.class);
	}
	
	@Override
	public void run() {
		
		try {
			log.info("Checking for expired flow family managers..");
			
			Collection<FlowFamily> flowFamilies = flowAdminModule.getCachedFlowFamilies();
			
			if (!CollectionUtils.isEmpty(flowFamilies)) {
				
				TransactionHandler transactionHandler = flowFamilyDAO.createTransaction();
				
				try {
					HashMap<Integer, List<FlowFamily>> removedManagers = new HashMap<Integer, List<FlowFamily>>();
					HashSet<FlowFamily> updatedFlowFamilies = new HashSet<FlowFamily>(flowFamilies.size());
					Timestamp startOfToday = DateUtils.setTimeToMidnight(TimeUtils.getCurrentTimestamp());
					
					for (FlowFamily flowFamily : flowFamilies) {
						
						if (!CollectionUtils.isEmpty(flowFamily.getManagers())) {
							
							boolean managerRemoved = false;
							
							Iterator<FlowFamilyManager> iterator = flowFamily.getManagers().iterator();
							
							while (iterator.hasNext()) {
								
								FlowFamilyManager manager = iterator.next();
								
								if (manager.getValidToDate() != null && startOfToday.compareTo(manager.getValidToDate()) > 0) {
									
									log.info("Removing expired manager " + manager.getUserID() + " from flow family " + flowFamily);
									iterator.remove();
									managerRemoved = true;
									
									List<FlowFamily> managerRemovedFlowFamily = removedManagers.get(manager.getUserID());
									
									if (managerRemovedFlowFamily == null) {
										
										managerRemovedFlowFamily = new ArrayList<FlowFamily>();
										removedManagers.put(manager.getUserID(), managerRemovedFlowFamily);
									}
									
									managerRemovedFlowFamily.add(flowFamily);
								}
							}
							
							if (managerRemoved) {
								
								flowFamilyDAO.update(flowFamily, transactionHandler, UPDATE_QUERY);
								updatedFlowFamilies.add(flowFamily);
							}
						}
					}
					
					if (!removedManagers.isEmpty()) {
						
						List<ManagerExpiredEvent> managerExpiredEvents = new ArrayList<ManagerExpiredEvent>();
						
						for (Entry<Integer, List<FlowFamily>> entry : removedManagers.entrySet()) {
							
							Integer managerID = entry.getKey();
							User manager = flowAdminModule.getUserHandler().getUser(managerID, false, false);
							
							String managerToString = manager != null ? manager.toString() : managerID.toString();
							
							log.debug("Removing expired manager " + managerToString + " from flow instances");
							
							for (FlowFamily flowFamily : entry.getValue()) {
								
								ArrayListQuery<Integer> query = new ArrayListQuery<Integer>(flowFamilyDAO.getDataSource(), "SELECT fi.flowInstanceID FROM flowengine_flow_instances fi INNER JOIN flowengine_flows ff ON fi.flowID = ff.flowID INNER JOIN flowengine_flow_instance_managers fim ON fi.flowInstanceID = fim.flowInstanceID WHERE ff.flowFamilyID = ? AND fim.userID = ?", IntegerPopulator.getPopulator());
								query.setInt(1, flowFamily.getFlowFamilyID());
								query.setInt(2, managerID);
								
								List<Integer> flowInstanceIDs = query.executeQuery();
								
								if (flowInstanceIDs != null) {
									
									for (Integer flowInstanceID : flowInstanceIDs) {
										
										FlowInstance flowInstance = getFlowInstance(flowInstanceID);
										
										log.info("Removing expired manager " + managerToString + " from " + flowInstance);

										List<User> previousManagers = new ArrayList<User>(flowInstance.getManagers());
										
										if (manager != null) { // If user was removed an update is enough to remove the stray ID

											Iterator<User> iterator = flowInstance.getManagers().iterator();

											while (iterator.hasNext()) {

												User flowInstanceManager = iterator.next();

												if (flowInstanceManager.equals(manager)) {

													iterator.remove();
													break;
												}
											}
										}
										
										RelationQuery updateQuery = new RelationQuery(FlowInstance.MANAGERS_RELATION);
										updateQuery.addExcludedFields(FlowInstance.STATUS_RELATION, FlowInstance.FLOW_RELATION);
										flowInstanceDAO.update(flowInstance, updateQuery);
										
										String managerName = manager != null ? manager.getFirstname() + " " + manager.getLastname() : managerID.toString();
										String detailString = flowAdminModule.getEventFlowInstanceManagerExpired() + " " + managerName;
										FlowInstanceEvent flowInstanceEvent = flowAdminModule.getFlowInstanceEventGenerator().addFlowInstanceEvent(flowInstance, EventType.MANAGERS_UPDATED, detailString, null);
										
										flowAdminModule.getEventHandler().sendEvent(FlowInstance.class, new CRUDEvent<FlowInstance>(CRUDAction.UPDATE, flowInstance), EventTarget.ALL);
										flowAdminModule.getEventHandler().sendEvent(FlowInstance.class, new ManagersChangedEvent(flowInstance, flowInstanceEvent, flowAdminModule.getSiteProfile(flowInstance), previousManagers, flowInstance.getManagerGroups(), null), EventTarget.ALL);
									}
								}
								
								managerExpiredEvents.add(new ManagerExpiredEvent(manager, flowFamily, flowInstanceIDs));
							}
						}
						
						transactionHandler.commit();
						
						for (ManagerExpiredEvent event : managerExpiredEvents) {
							
							flowAdminModule.getEventHandler().sendEvent(FlowFamily.class, event, EventTarget.ALL);
						}
						
						flowAdminModule.getEventHandler().sendEvent(FlowFamily.class, new CRUDEvent<FlowFamily>(FlowFamily.class, CRUDAction.UPDATE, new ArrayList<FlowFamily>(updatedFlowFamilies)), EventTarget.ALL);
						
						flowAdminModule.cacheFlows();
					}
				} finally {
					TransactionHandler.autoClose(transactionHandler);
				}
			}
			
		} catch (Throwable t) {
			log.error("Error updating expiring flow managers", t);
			
			try {
				flowAdminModule.cacheFlows();
			} catch (SQLException e) {
				log.error("Error caching flows", e);
			}
		}
	}
	
	private FlowInstance getFlowInstance(int flowInstanceID) throws SQLException {
		
		HighLevelQuery<FlowInstance> query = new HighLevelQuery<FlowInstance>(FlowInstance.MANAGERS_RELATION, FlowInstance.STATUS_RELATION, FlowInstance.FLOW_RELATION, Flow.FLOW_FAMILY_RELATION);
		query.addParameter(flowInstanceIDParamFactory.getParameter(flowInstanceID));
		
		return flowInstanceDAO.get(query);
	}
	
}
