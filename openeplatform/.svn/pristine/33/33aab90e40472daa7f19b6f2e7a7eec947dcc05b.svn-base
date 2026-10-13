package com.nordicpeak.flowengine.search.tasks;

import com.nordicpeak.flowengine.beans.Flow;
import com.nordicpeak.flowengine.search.FlowInstanceIndexer;

public class DeleteFlowTask extends BaseTask {

	protected final Flow flow;

	public DeleteFlowTask(FlowInstanceIndexer flowInstanceIndexer, Flow flow) {

		super(flowInstanceIndexer);
		this.flow = flow;
	}

	@Override
	public void run() {

		if(!flowInstanceIndexer.isValidState()){
			return;
		}
		
		try {
			flowInstanceIndexer.deleteDocuments(flow);
		} catch (Throwable t) {

			log.error("Error deleting documents belonging to flow " + flow, t);
		}
	}
}
