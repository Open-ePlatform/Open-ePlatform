package com.nordicpeak.flowengine.search.events;

import com.nordicpeak.flowengine.beans.FlowInstance;


public abstract class FlowInstanceEvent extends QueuedIndexEvent {

	protected FlowInstance flowInstance;

	public FlowInstanceEvent(FlowInstance flowInstance) {

		super();
		this.flowInstance = flowInstance;
	}
}
