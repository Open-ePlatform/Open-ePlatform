package com.nordicpeak.flowengine.search.events;

import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadPoolExecutor;

import se.unlogic.standardutils.dao.QueryResultsStreamer;
import se.unlogic.standardutils.time.TimeUtils;

import com.nordicpeak.flowengine.beans.Flow;
import com.nordicpeak.flowengine.beans.FlowInstance;
import com.nordicpeak.flowengine.search.FlowInstanceIndexer;
import com.nordicpeak.flowengine.search.tasks.AddUpdateFlowInstanceTask;


public class AddFlowEvent extends QueuedIndexEvent {

	private Flow flow;
	protected QueryResultsStreamer<FlowInstance, Integer> resultsStreamer;
	private boolean moreResults = true;

	long start = System.currentTimeMillis();
	
	public AddFlowEvent(Flow flow, QueryResultsStreamer<FlowInstance, Integer> resultsStreamer) {

		super();
		this.flow = flow;
		this.resultsStreamer = resultsStreamer;
	}
	
	@Override
	public int queueTasks(ThreadPoolExecutor executor, FlowInstanceIndexer flowInstanceIndexer) {

		List<FlowInstance> flowInstances;

		try {
			flowInstances = resultsStreamer.getBeans();

		} catch (SQLException e) {

			log.error("Error getting flow instances for " + flow + " from DB.", e);

			moreResults = false;
			return 0;
		}

		if (flowInstances != null) {

			int taskCount = 0;

			for (FlowInstance flowInstance : flowInstances) {

				if (!flowInstanceIndexer.isIndexable(flowInstance)) {
					continue;
				}

				try {
					executor.execute(new AddUpdateFlowInstanceTask(flowInstanceIndexer, flowInstance));
					taskCount++;
				} catch (RejectedExecutionException e) {}
			}

			return taskCount;
		}

		if (flowInstanceIndexer.isLogIndexing()) {
			
			start = System.currentTimeMillis() - start;
			log.info("Flow instance indexing for " + flow + " took " + TimeUtils.millisecondsToString(start));
		}
		
		moreResults = false;
		return 0;
	}

	@Override
	public boolean hasRemainingTasks() {
		return moreResults;
	}

	@Override
	public String toString(){
		
		return "add event for flow " + flow;
	}
}
