package com.nordicpeak.flowengine.events;

import java.io.Serializable;

import com.nordicpeak.flowengine.managers.FlowInstanceManager;

public class MultiSigningCanceledEvent implements Serializable {

	private static final long serialVersionUID = -4738711597966694402L;

	private final FlowInstanceManager flowInstanceManager;

	public MultiSigningCanceledEvent(FlowInstanceManager flowInstanceManager) {

		super();
		this.flowInstanceManager = flowInstanceManager;
	}

	public FlowInstanceManager getFlowInstanceManager() {

		return flowInstanceManager;
	}
}
