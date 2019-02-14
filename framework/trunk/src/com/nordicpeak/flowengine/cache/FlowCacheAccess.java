package com.nordicpeak.flowengine.cache;

import java.util.List;

import com.nordicpeak.flowengine.beans.FlowFamily;

public class FlowCacheAccess {

	private List<FlowFamily> flowFamiliesFullAccess;
	private List<FlowFamily> flowFamiliesRestrictedAccess;

	public FlowCacheAccess(List<FlowFamily> flowFamiliesFullAccess, List<FlowFamily> flowFamiliesRestrictedAccess) {
		super();
		this.flowFamiliesFullAccess = flowFamiliesFullAccess;
		this.flowFamiliesRestrictedAccess = flowFamiliesRestrictedAccess;
	}

	public List<FlowFamily> getFlowFamiliesFullAccess() {
		return flowFamiliesFullAccess;
	}

	public List<FlowFamily> getFlowFamiliesRestrictedAccess() {
		return flowFamiliesRestrictedAccess;
	}

	public void setFlowFamiliesFullAccess(List<FlowFamily> flowFamiliesFullAccess) {
		this.flowFamiliesFullAccess = flowFamiliesFullAccess;
	}

	public void setFlowFamiliesRestrictedAccess(List<FlowFamily> flowFamiliesRestrictedAccess) {
		this.flowFamiliesRestrictedAccess = flowFamiliesRestrictedAccess;
	}

}
