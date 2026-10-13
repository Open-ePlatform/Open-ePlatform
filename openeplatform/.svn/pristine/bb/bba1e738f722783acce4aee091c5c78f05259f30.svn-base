package com.nordicpeak.flowengine.events;

import java.io.Serializable;
import java.util.Map;

import com.nordicpeak.flowengine.interfaces.ImmutableFlow;
import com.nordicpeak.flowengine.interfaces.ImmutableStatus;

public class FlowVersionAdded implements Serializable {

	private static final long serialVersionUID = -3228936233736542847L;

	private ImmutableFlow flow;

	private Map<Integer, ImmutableStatus> statusConversionMap;

	public FlowVersionAdded(ImmutableFlow flow, Map<Integer, ImmutableStatus> statusConversionMap) {

		this.flow = flow;
		this.statusConversionMap = statusConversionMap;
	}

	@Override
	public String toString() {

		return "(flow=" + flow + ")";
	}

	public ImmutableFlow getFlow() {

		return flow;
	}

	public void setFlow(ImmutableFlow flow) {

		this.flow = flow;
	}

	public Map<Integer, ImmutableStatus> getStatusConversionMap() {

		return statusConversionMap;
	}

	public void setStatusConversionMap(Map<Integer, ImmutableStatus> statusConversionMap) {

		this.statusConversionMap = statusConversionMap;
	}

}