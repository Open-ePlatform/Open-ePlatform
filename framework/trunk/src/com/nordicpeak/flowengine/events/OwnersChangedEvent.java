package com.nordicpeak.flowengine.events;

import java.io.Serializable;
import java.util.List;

import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.openhierarchy.foregroundmodules.siteprofile.interfaces.SiteProfile;

import com.nordicpeak.flowengine.beans.FlowInstance;
import com.nordicpeak.flowengine.beans.FlowInstanceEvent;

public class OwnersChangedEvent implements Serializable {

	private static final long serialVersionUID = -3105656362605519707L;

	private final FlowInstance flowInstance;
	private final FlowInstanceEvent event;
	private final SiteProfile siteProfile;
	private final List<User> previousOwners;
	private final User user;

	public OwnersChangedEvent(FlowInstance flowInstance, FlowInstanceEvent event,  SiteProfile siteProfile, List<User> previousOwners, User user) {

		super();
		this.flowInstance = flowInstance;
		this.event = event;
		this.siteProfile = siteProfile;
		this.previousOwners = previousOwners;
		this.user = user;
	}

	public static long getSerialversionuid() {

		return serialVersionUID;
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

	public List<User> getPreviousOwners() {

		return previousOwners;
	}

	public User getUser() {

		return user;
	}
}
