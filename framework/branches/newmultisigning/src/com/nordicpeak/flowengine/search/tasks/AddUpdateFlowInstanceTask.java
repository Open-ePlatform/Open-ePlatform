package com.nordicpeak.flowengine.search.tasks;

import com.nordicpeak.flowengine.beans.Flow;
import com.nordicpeak.flowengine.beans.FlowFamily;
import com.nordicpeak.flowengine.beans.FlowInstance;
import com.nordicpeak.flowengine.search.FlowInstanceIndexer;


public class AddUpdateFlowInstanceTask extends BaseTask {

	protected final FlowInstance flowInstance;
	protected final Flow flow;
	protected final FlowFamily flowFamily;

	public AddUpdateFlowInstanceTask(FlowInstanceIndexer flowInstanceIndexer, FlowInstance flowInstance) {

		super(flowInstanceIndexer);
		this.flowInstance = flowInstance;
		this.flow = flowInstance.getFlow();
		this.flowFamily = flow.getFlowFamily();
	}

	public AddUpdateFlowInstanceTask(FlowInstanceIndexer flowInstanceIndexer, FlowInstance flowInstance, Flow flow) {

		super(flowInstanceIndexer);
		this.flowInstance = flowInstance;
		this.flow = flow;
		this.flowFamily = flow.getFlowFamily();
	}

	public AddUpdateFlowInstanceTask(FlowInstanceIndexer flowInstanceIndexer, FlowInstance flowInstance, Flow flow, FlowFamily flowFamily) {

		super(flowInstanceIndexer);
		this.flowInstance = flowInstance;
		this.flow = flow;
		this.flowFamily = flowFamily;
	}

	@Override
	public void run() {

		if(!flowInstanceIndexer.isValidState()){
			return;
		}
		
		try {
			flowInstanceIndexer.deleteDocument(flowInstance);
			flowInstanceIndexer.indexFlowInstance(flowInstance, flow, flowFamily);
			
		} catch (Throwable t) {
			
			log.error("Error deleting and adding document for flow instance " + flowInstance, t);
		}
	}
}
