package com.nordicpeak.flowengine.search.events;

import java.sql.SQLException;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadPoolExecutor;

import com.nordicpeak.flowengine.beans.Flow;
import com.nordicpeak.flowengine.beans.FlowInstance;
import com.nordicpeak.flowengine.search.FlowInstanceIndexer;
import com.nordicpeak.flowengine.search.tasks.AddUpdateFlowInstanceTask;


public class AddFlowEvent extends QueuedIndexEvent {

	protected Flow flow;

	public AddFlowEvent(Flow flow) {

		super();
		this.flow = flow;
	}

	@Override
	public int queueTasks(ThreadPoolExecutor executor, FlowInstanceIndexer flowInstanceIndexer) {

		//Get flow with correct relations from DB
		Flow dbFlow;
		try {
			dbFlow = flowInstanceIndexer.getFlow(flow.getFlowID());
			
		} catch (SQLException e) {

			log.error("Error getting flow " + flow + " from DB.",e);
			
			return 0;
		}

		if(dbFlow != null && dbFlow.getFlowInstances() != null){

			int taskCount = 0;

			for(FlowInstance flowInstance : dbFlow.getFlowInstances()){

				if(!flowInstanceIndexer.isIndexable(flowInstance)){

					continue;
				}

				try{
					executor.execute(new AddUpdateFlowInstanceTask(flowInstanceIndexer, flowInstance, dbFlow, dbFlow.getFlowFamily()));
					taskCount++;
				}catch(RejectedExecutionException e){}
			}

			return taskCount;
		}

		return 0;
	}
	
	@Override
	public String toString(){
		
		return "add event for flow " + flow;
	}
}
