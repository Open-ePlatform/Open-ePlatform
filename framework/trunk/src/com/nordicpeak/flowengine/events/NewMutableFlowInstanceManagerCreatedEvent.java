package com.nordicpeak.flowengine.events;

import java.io.Serializable;

import se.unlogic.hierarchy.core.beans.User;

import com.nordicpeak.flowengine.managers.MutableFlowInstanceManager;

public class NewMutableFlowInstanceManagerCreatedEvent implements Serializable {

	private static final long serialVersionUID = -4016272776605027732L;

	private final User user;
	private final MutableFlowInstanceManager instanceManager;

	public NewMutableFlowInstanceManagerCreatedEvent(User user, MutableFlowInstanceManager instanceManager) {

		super();
		this.user = user;
		this.instanceManager = instanceManager;
	}

	public User getUser() {

		return user;
	}

	public MutableFlowInstanceManager getInstanceManager() {

		return instanceManager;
	}

}
