package com.nordicpeak.flowengine.search;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


public class CallbackThreadPoolExecutor extends ThreadPoolExecutor {

	private final FlowInstanceIndexer flowInstanceIndexer;
	private int executingThreadCount;

	public CallbackThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, FlowInstanceIndexer flowInstanceIndexer) {

		super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);

		this.flowInstanceIndexer = flowInstanceIndexer;
	}

	@Override
	protected void beforeExecute(Thread t, Runnable r) {

		synchronized(this){
			executingThreadCount++;
		}
	}

	@Override
	protected void afterExecute(Runnable r, Throwable t) {

		synchronized(this){
			executingThreadCount--;
		}

		flowInstanceIndexer.checkQueueState(true);
	}


	public int getExecutingThreadCount() {

		return executingThreadCount;
	}
}
