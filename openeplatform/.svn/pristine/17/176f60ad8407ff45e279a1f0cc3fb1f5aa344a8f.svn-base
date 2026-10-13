package com.nordicpeak.flowengine.search.events;

import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadPoolExecutor;

import com.nordicpeak.flowengine.beans.Flow;
import com.nordicpeak.flowengine.search.FlowInstanceIndexer;
import com.nordicpeak.flowengine.search.tasks.DeleteFlowTask;

public class DeleteFlowEvent extends QueuedIndexEvent {

	protected Flow flow;

	public DeleteFlowEvent(Flow flow) {

		super();
		this.flow = flow;
	}

	@Override
	public int queueTasks(ThreadPoolExecutor executor, FlowInstanceIndexer flowInstanceIndexer) {

		try {
			executor.execute(new DeleteFlowTask(flowInstanceIndexer, flow));
			return 1;
		} catch (RejectedExecutionException e) {}

		return 0;
	}
	
	@Override
	public String toString(){
		
		return "Delete flow event for flow " + flow;
	}	
}
