package com.nordicpeak.flowengine.search.tasks;

import com.nordicpeak.flowengine.beans.FlowInstance;
import com.nordicpeak.flowengine.search.FlowInstanceIndexer;


public class DeleteFlowInstanceTask extends BaseTask {

	protected final FlowInstance flowInstance;

	public DeleteFlowInstanceTask(FlowInstanceIndexer flowInstanceIndexer, FlowInstance flowInstance) {

		super(flowInstanceIndexer);
		this.flowInstance = flowInstance;
	}

	@Override
	public void run() {

		if(!flowInstanceIndexer.isValidState()){
			return;
		}

		try {
			flowInstanceIndexer.deleteDocument(flowInstance);
		} catch (Throwable t) {

			log.error("Error deleting document belonging to flow instance " + flowInstance, t);
		}
	}

}
