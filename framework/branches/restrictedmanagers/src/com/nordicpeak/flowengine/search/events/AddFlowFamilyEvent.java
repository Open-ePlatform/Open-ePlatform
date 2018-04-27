package com.nordicpeak.flowengine.search.events;

import java.sql.SQLException;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadPoolExecutor;

import com.nordicpeak.flowengine.beans.Flow;
import com.nordicpeak.flowengine.beans.FlowFamily;
import com.nordicpeak.flowengine.beans.FlowInstance;
import com.nordicpeak.flowengine.search.FlowInstanceIndexer;
import com.nordicpeak.flowengine.search.tasks.AddUpdateFlowInstanceTask;


public class AddFlowFamilyEvent extends QueuedIndexEvent {

	protected FlowFamily flowFamily;

	public AddFlowFamilyEvent(FlowFamily flowFamily) {

		super();
		this.flowFamily = flowFamily;
	}

	@Override
	public int queueTasks(ThreadPoolExecutor executor, FlowInstanceIndexer flowInstanceIndexer) {

		//Get flow family with correct relations from DB
		FlowFamily dbFamily;
		try {
			dbFamily = flowInstanceIndexer.getFlowFamily(flowFamily.getFlowFamilyID());
			
		} catch (SQLException e) {

			log.error("Error getting flow family " + flowFamily + " from DB.", e);
			
			return 0;
		}

		if(dbFamily != null && dbFamily.getFlows() != null){

			int taskCount = 0;

			for(Flow flow : dbFamily.getFlows()){

				if(flow.getFlowInstances() != null){

					for(FlowInstance flowInstance : flow.getFlowInstances()){

						if(!flowInstanceIndexer.isIndexable(flowInstance)){

							continue;
						}

						try{
							executor.execute(new AddUpdateFlowInstanceTask(flowInstanceIndexer, flowInstance, flow, dbFamily));
							taskCount++;
						}catch(RejectedExecutionException e){}
					}
				}
			}

			return taskCount;
		}

		return 0;
	}
	
	@Override
	public String toString(){
		
		return "add event for flow family " + flowFamily;
	}	
}
