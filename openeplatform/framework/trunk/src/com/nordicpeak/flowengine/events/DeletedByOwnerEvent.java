package com.nordicpeak.flowengine.events;

import java.io.Serializable;

import com.nordicpeak.flowengine.beans.FlowInstance;

import se.unlogic.hierarchy.core.beans.User;

public class DeletedByOwnerEvent implements Serializable {

	private static final long serialVersionUID = -3228936233736542847L;
	
	private FlowInstance flowInstance;
	private User user;

	public DeletedByOwnerEvent(FlowInstance flowInstance, User user) {

		this.flowInstance = flowInstance;
		this.user = user;
	}

	@Override
	public String toString() {

		return flowInstance.getFlowInstanceID().toString();
	}

	public FlowInstance getFlowInstance() {

		return flowInstance;
	}

	public void setFlowInstance(FlowInstance flowInstance) {

		this.flowInstance = flowInstance;
	}

	public User getUser() {

		return user;
	}

	public void setUser(User user) {

		this.user = user;
	}
}