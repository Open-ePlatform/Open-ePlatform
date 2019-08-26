package com.nordicpeak.flowengine.search.events;

import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadPoolExecutor;

import se.unlogic.standardutils.dao.QueryResultsStreamer;
import se.unlogic.standardutils.time.TimeUtils;

import com.nordicpeak.flowengine.beans.FlowInstance;
import com.nordicpeak.flowengine.search.FlowInstanceIndexer;
import com.nordicpeak.flowengine.search.tasks.AddUpdateFlowInstanceTask;

public class InitialFlowInstanceIndexingEvent extends QueuedIndexEvent {

	protected QueryResultsStreamer<FlowInstance, Integer> resultsStreamer;
	private boolean moreResults = true;
	
	long start = System.currentTimeMillis();

	public InitialFlowInstanceIndexingEvent(QueryResultsStreamer<FlowInstance, Integer> resultsStreamer) {

		super();
		this.resultsStreamer = resultsStreamer;
	}

	@Override
	public int queueTasks(ThreadPoolExecutor executor, FlowInstanceIndexer flowInstanceIndexer) {

		List<FlowInstance> flowInstances;

		try {
			flowInstances = resultsStreamer.getBeans();

		} catch (SQLException e) {

			log.error("Error getting flow instances from DB.", e);

			moreResults = false;
			return 0;
		}

		if (flowInstances != null) {

			int taskCount = 0;

			for (FlowInstance flowInstance : flowInstances) {

				if (flowInstance.getFlow() == null || !flowInstanceIndexer.isIndexable(flowInstance)) {
					continue;
				}

				try {
					executor.execute(new AddUpdateFlowInstanceTask(flowInstanceIndexer, flowInstance));
					taskCount++;
				} catch (RejectedExecutionException e) {}
			}

			return taskCount;
		}

		start = System.currentTimeMillis() - start;
		log.info("Initial flow instance indexing took " + TimeUtils.millisecondsToString(start));
		
		moreResults = false;
		return 0;
	}

	@Override
	public boolean hasRemainingTasks() {
		return moreResults;
	}

	@Override
	public String toString() {

		return "initial flow instance indexing event";
	}
}
