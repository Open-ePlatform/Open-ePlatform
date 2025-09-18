package com.nordicpeak.flowengine.search.events;

import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadPoolExecutor;

import com.nordicpeak.flowengine.beans.FlowFamily;
import com.nordicpeak.flowengine.search.FlowInstanceIndexer;
import com.nordicpeak.flowengine.search.tasks.DeleteFlowFamilyTask;

public class DeleteFlowFamilyEvent extends QueuedIndexEvent {

	protected FlowFamily flowFamily;

	public DeleteFlowFamilyEvent(FlowFamily flowFamily) {

		super();
		this.flowFamily = flowFamily;
	}

	@Override
	public int queueTasks(ThreadPoolExecutor executor, FlowInstanceIndexer flowInstanceIndexer) {

		try {
			executor.execute(new DeleteFlowFamilyTask(flowInstanceIndexer, flowFamily));
			return 1;
		} catch (RejectedExecutionException e) {}

		return 0;
	}
	
	@Override
	public String toString(){
		
		return "Delete flow family event for flow family" + flowFamily;
	}
}
