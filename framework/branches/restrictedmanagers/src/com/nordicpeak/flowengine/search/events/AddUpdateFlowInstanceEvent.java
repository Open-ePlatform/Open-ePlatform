package com.nordicpeak.flowengine.search.events;

import java.sql.SQLException;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadPoolExecutor;

import com.nordicpeak.flowengine.beans.FlowInstance;
import com.nordicpeak.flowengine.search.FlowInstanceIndexer;
import com.nordicpeak.flowengine.search.tasks.AddUpdateFlowInstanceTask;
import com.nordicpeak.flowengine.search.tasks.DeleteFlowInstanceTask;


public class AddUpdateFlowInstanceEvent extends FlowInstanceEvent {

	public AddUpdateFlowInstanceEvent(FlowInstance flowInstance) {

		super(flowInstance);
	}

	@Override
	public int queueTasks(ThreadPoolExecutor executor, FlowInstanceIndexer flowInstanceIndexer) {

		//Get flow instance with correct relations from DB
		FlowInstance dbInstance;
		try {
			dbInstance = flowInstanceIndexer.getFlowInstance(flowInstance.getFlowInstanceID());
		} catch (SQLException e) {

			log.error("Error getting flow instance " + flowInstance + " from DB", e);
			
			return 0;
		}

		if(dbInstance != null){

			try{
				if(!flowInstanceIndexer.isIndexable(flowInstance)){

					executor.execute(new DeleteFlowInstanceTask(flowInstanceIndexer, dbInstance));
					
				}else{
					
					executor.execute(new AddUpdateFlowInstanceTask(flowInstanceIndexer, dbInstance));
				}
				
				return 1;
			} catch (RejectedExecutionException e) {}
		}

		return 0;
	}
	
	@Override
	public String toString(){
		
		return "add/update event for flow instance " + flowInstance;
	}	
}
