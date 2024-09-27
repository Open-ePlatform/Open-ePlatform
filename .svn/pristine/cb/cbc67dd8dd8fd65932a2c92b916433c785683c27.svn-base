package com.nordicpeak.flowengine.events;

import java.io.Serializable;
import java.util.List;

import se.unlogic.hierarchy.core.beans.Group;
import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.openhierarchy.foregroundmodules.siteprofile.interfaces.SiteProfile;

import com.nordicpeak.flowengine.beans.FlowInstance;
import com.nordicpeak.flowengine.beans.FlowInstanceEvent;

public class ManagersChangedEvent implements Serializable {

	private static final long serialVersionUID = -3105656362605519708L;

	private final FlowInstance flowInstance;
	private final FlowInstanceEvent event;
	private final SiteProfile siteProfile;
	private final List<User> previousManagers;
	private final List<Group> previousManagerGroups;
	private final User user;
	private List<String> additionalGlobalEmailRecipients;

	public ManagersChangedEvent(FlowInstance flowInstance, FlowInstanceEvent event,  SiteProfile siteProfile, List<User> previousManagers, List<Group> previousManagerGroups, User user) {

		super();
		this.flowInstance = flowInstance;
		this.event = event;
		this.siteProfile = siteProfile;
		this.previousManagers = previousManagers;
		this.previousManagerGroups = previousManagerGroups;
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
	
	public List<User> getPreviousManagers() {
		
		return previousManagers;
	}

	public List<Group> getPreviousManagerGroups() {

		return previousManagerGroups;
	}

	public User getUser() {

		return user;
	}

	public List<String> getAdditionalGlobalEmailRecipients() {
		return additionalGlobalEmailRecipients;
	}

	public void setAdditionalGlobalEmailRecipients(List<String> additionalGlobalEmailRecipients) {
		this.additionalGlobalEmailRecipients = additionalGlobalEmailRecipients;
	}

}
