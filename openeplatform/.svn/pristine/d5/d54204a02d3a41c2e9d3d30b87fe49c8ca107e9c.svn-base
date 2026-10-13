package com.nordicpeak.flowengine.events;

import java.io.Serializable;

import com.nordicpeak.flowengine.beans.Flow;

public class FlowPublishedEvent implements Serializable {

	private static final long serialVersionUID = -3105656362605519707L;

	private Flow flow;

	private boolean newPublication;

	public FlowPublishedEvent(Flow flow, boolean newPublication) {

		super();
		this.flow = flow;
		this.newPublication = newPublication;
	}

	public Flow getFlow() {

		return flow;
	}

	public boolean isNewPublication() {

		return newPublication;
	}

}
