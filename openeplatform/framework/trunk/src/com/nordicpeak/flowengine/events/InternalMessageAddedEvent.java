package com.nordicpeak.flowengine.events;

import java.io.Serializable;

import se.unlogic.openhierarchy.foregroundmodules.siteprofile.interfaces.SiteProfile;

import com.nordicpeak.flowengine.beans.FlowInstance;
import com.nordicpeak.flowengine.beans.InternalMessage;

public class InternalMessageAddedEvent implements Serializable {

	private static final long serialVersionUID = 1873981649620240956L;

	private final FlowInstance flowInstance;
	private final SiteProfile siteProfile;
	private final InternalMessage internalMessage;

	public InternalMessageAddedEvent(FlowInstance flowInstance, SiteProfile siteProfile, InternalMessage internalMessage) {

		this.flowInstance = flowInstance;
		this.siteProfile = siteProfile;
		this.internalMessage = internalMessage;
	}

	public FlowInstance getFlowInstance() {

		return flowInstance;
	}

	public SiteProfile getSiteProfile() {

		return siteProfile;
	}

	public InternalMessage getInternalMessage() {

		return internalMessage;
	}

}
