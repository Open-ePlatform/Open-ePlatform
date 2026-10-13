package com.nordicpeak.flowengine.search.tasks;

import com.nordicpeak.flowengine.beans.FlowFamily;
import com.nordicpeak.flowengine.search.FlowInstanceIndexer;


public class DeleteFlowFamilyTask extends BaseTask {

	protected final FlowFamily flowFamily;
	
	public DeleteFlowFamilyTask(FlowInstanceIndexer flowInstanceIndexer, FlowFamily flowFamily) {

		super(flowInstanceIndexer);
		this.flowFamily = flowFamily;
	}

	@Override
	public void run() {

		if(!flowInstanceIndexer.isValidState()){
			return;
		}
		
		try {
			flowInstanceIndexer.deleteDocuments(flowFamily);
		} catch (Throwable t) {

			log.error("Error deleting documents belonging to flow family " + flowFamily, t);
		}
	}
}
