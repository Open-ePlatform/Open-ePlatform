package com.nordicpeak.flowengine.integration.callback.events;

import java.io.Serializable;

import com.nordicpeak.flowengine.beans.FlowInstance;


public class DeliveryConfirmationEvent implements Serializable {

	private static final long serialVersionUID = 8591865982213386357L;

	private final FlowInstance flowInstance;
	private final boolean delivered;
	private final String logMessage;
	
	public DeliveryConfirmationEvent(FlowInstance flowInstance, boolean delivered, String logMessage) {

		super();
		this.flowInstance = flowInstance;
		this.delivered = delivered;
		this.logMessage = logMessage;
	}

	
	public FlowInstance getFlowInstance() {
	
		return flowInstance;
	}

	
	public boolean isDelivered() {
	
		return delivered;
	}

	
	public String getLogMessage() {
	
		return logMessage;
	}
}
