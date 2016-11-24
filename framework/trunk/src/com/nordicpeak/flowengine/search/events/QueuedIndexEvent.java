package com.nordicpeak.flowengine.search.events;

import java.util.concurrent.ThreadPoolExecutor;

import org.apache.log4j.Logger;

import com.nordicpeak.flowengine.search.FlowInstanceIndexer;

public abstract class QueuedIndexEvent {

	protected final Logger log = Logger.getLogger(this.getClass());
	
	public abstract int queueTasks(ThreadPoolExecutor executor, FlowInstanceIndexer flowInstanceIndexer);
}
