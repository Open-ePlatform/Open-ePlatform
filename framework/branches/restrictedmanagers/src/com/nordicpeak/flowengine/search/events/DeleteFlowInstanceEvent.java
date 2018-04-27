package com.nordicpeak.flowengine.search.events;

import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadPoolExecutor;

import com.nordicpeak.flowengine.beans.FlowInstance;
import com.nordicpeak.flowengine.search.FlowInstanceIndexer;
import com.nordicpeak.flowengine.search.tasks.DeleteFlowInstanceTask;


public class DeleteFlowInstanceEvent extends FlowInstanceEvent {

	public DeleteFlowInstanceEvent(FlowInstance flowInstance) {

		super(flowInstance);
	}

	@Override
	public int queueTasks(ThreadPoolExecutor executor, FlowInstanceIndexer flowInstanceIndexer) {

		try{
			executor.execute(new DeleteFlowInstanceTask(flowInstanceIndexer, flowInstance));
			return 1;
		} catch (RejectedExecutionException e) {}

		return 0;
	}
	
	@Override
	public String toString(){
		
		return "Delete flow instance event for flow instance" + flowInstance;
	}	
}
