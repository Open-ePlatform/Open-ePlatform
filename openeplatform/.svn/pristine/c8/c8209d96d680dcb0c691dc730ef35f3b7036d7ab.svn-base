package com.nordicpeak.flowengine.events;

import java.io.Serializable;

import se.unlogic.hierarchy.core.beans.User;

import com.nordicpeak.flowengine.beans.RequestMetadata;
import com.nordicpeak.flowengine.beans.Status;
import com.nordicpeak.flowengine.enums.EventType;
import com.nordicpeak.flowengine.managers.FlowInstanceManager;

public class FlowInstanceSaveStatusOverrideEvent implements Serializable {

	private static final long serialVersionUID = 4129670393435968733L;

	private final FlowInstanceManager flowInstanceManager;
	private final User user;
	private final String actionID;
	private final EventType eventType;
	private final RequestMetadata requestMetadata;

	private Status overrideStatus;
	private boolean overrideByManagerEdit;

	public FlowInstanceSaveStatusOverrideEvent(FlowInstanceManager flowInstanceManager, User user, String actionID, EventType eventType, RequestMetadata requestMetadata) {

		super();

		this.flowInstanceManager = flowInstanceManager;
		this.user = user;
		this.actionID = actionID;
		this.eventType = eventType;
		this.requestMetadata = requestMetadata;
	}

	@Override
	public String toString() {

		return this.getClass().getSimpleName() + " (" + flowInstanceManager.toString() + ")";
	}

	public FlowInstanceManager getFlowInstanceManager() {

		return flowInstanceManager;
	}

	public User getUser() {

		return user;
	}

	public String getActionID() {

		return actionID;
	}

	public EventType getEventType() {

		return eventType;
	}

	public RequestMetadata getRequestMetadata() {

		return requestMetadata;
	}

	public Status getOverrideStatus() {

		return overrideStatus;
	}

	public void setOverrideStatus(Status overrideStatus) {

		this.overrideStatus = overrideStatus;
	}

	public boolean isOverrideByManagerEdit() {

		return overrideByManagerEdit;
	}

	public void setOverrideByManagerEdit(boolean overrideByManagerEdit) {

		this.overrideByManagerEdit = overrideByManagerEdit;
	}

}
