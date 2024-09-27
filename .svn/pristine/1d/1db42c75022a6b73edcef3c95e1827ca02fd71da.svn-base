package com.nordicpeak.flowengine.events;

import se.unlogic.hierarchy.core.beans.User;

import com.nordicpeak.flowengine.beans.RequestMetadata;
import com.nordicpeak.flowengine.beans.Status;
import com.nordicpeak.flowengine.enums.EventType;
import com.nordicpeak.flowengine.managers.FlowInstanceManager;

public class FlowInstanceSaveStatusOverrideCommitedEvent extends FlowInstanceSaveStatusOverrideEvent {

	private static final long serialVersionUID = 8613805955361748990L;

	private Status previousStatus;

	public FlowInstanceSaveStatusOverrideCommitedEvent(FlowInstanceManager flowInstanceManager, User user, String actionID, EventType eventType, RequestMetadata requestMetadata, Status previousStatus) {

		super(flowInstanceManager, user, actionID, eventType, requestMetadata);

		this.previousStatus = previousStatus;
	}

	public Status getPreviousStatus() {

		return previousStatus;
	}

}
