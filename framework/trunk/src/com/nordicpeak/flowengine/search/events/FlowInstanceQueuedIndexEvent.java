package com.nordicpeak.flowengine.search.events;

import com.nordicpeak.flowengine.beans.FlowInstance;


public abstract class FlowInstanceQueuedIndexEvent extends QueuedIndexEvent {

	protected FlowInstance flowInstance;

	public FlowInstanceQueuedIndexEvent(FlowInstance flowInstance) {

		super();
		this.flowInstance = flowInstance;
	}
}
