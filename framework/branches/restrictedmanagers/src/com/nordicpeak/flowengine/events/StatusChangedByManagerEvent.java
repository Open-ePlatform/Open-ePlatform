package com.nordicpeak.flowengine.events;

import java.io.Serializable;

import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.openhierarchy.foregroundmodules.siteprofile.interfaces.SiteProfile;

import com.nordicpeak.flowengine.beans.FlowInstance;
import com.nordicpeak.flowengine.beans.FlowInstanceEvent;
import com.nordicpeak.flowengine.beans.Status;

public class StatusChangedByManagerEvent implements Serializable {

	private static final long serialVersionUID = -3105656362605519708L;

	private final FlowInstance flowInstance;
	private final FlowInstanceEvent event;
	private final SiteProfile siteProfile;
	private final Status previousStatus;
	private final User user;
	private final boolean suppressUserNotifications;

	public StatusChangedByManagerEvent(FlowInstance flowInstance, FlowInstanceEvent event, SiteProfile siteProfile, Status previousStatus, User user) {
		
		this(flowInstance, event, siteProfile, previousStatus, user, false);
	}
	
	public StatusChangedByManagerEvent(FlowInstance flowInstance, FlowInstanceEvent event, SiteProfile siteProfile, Status previousStatus, User user, boolean suppressUserNotifications) {

		super();
		
		this.flowInstance = flowInstance;
		this.event = event;
		this.siteProfile = siteProfile;
		this.previousStatus = previousStatus;
		this.user = user;
		this.suppressUserNotifications = suppressUserNotifications;
	}

	public FlowInstance getFlowInstance() {

		return flowInstance;
	}

	public FlowInstanceEvent getEvent() {

		return event;
	}

	public SiteProfile getSiteProfile() {

		return siteProfile;
	}

	public Status getPreviousStatus() {

		return previousStatus;
	}

	public User getUser() {

		return user;
	}
	
	public boolean isSuppressUserNotifications() {
	
		return suppressUserNotifications;
	}
}
