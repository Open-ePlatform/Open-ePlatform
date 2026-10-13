package com.nordicpeak.flowengine.events;

import java.io.Serializable;
import java.util.List;

import se.unlogic.hierarchy.core.beans.User;

import com.nordicpeak.flowengine.beans.FlowFamily;

public class ManagerExpiredEvent implements Serializable {
	
	private static final long serialVersionUID = -3105656362605519707L;
	
	private final User manager;
	private final FlowFamily flowFamily;
	private final List<Integer> flowInstanceIDs;
	
	public ManagerExpiredEvent(User manager, FlowFamily flowFamily, List<Integer> flowInstanceIDs) {
		
		this.manager = manager;
		this.flowFamily = flowFamily;
		this.flowInstanceIDs = flowInstanceIDs;
	}
	
	public User getManager() {
		return manager;
	}
	
	public FlowFamily getFlowFamily() {
		return flowFamily;
	}
	
	public List<Integer> getFlowInstanceIDs() {
		return flowInstanceIDs;
	}
	
}
