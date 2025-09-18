package com.nordicpeak.flowengine.events;

import java.io.Serializable;

import se.unlogic.openhierarchy.foregroundmodules.siteprofile.interfaces.SiteProfile;

import com.nordicpeak.flowengine.beans.ExternalMessage;
import com.nordicpeak.flowengine.beans.FlowInstance;
import com.nordicpeak.flowengine.beans.FlowInstanceEvent;
import com.nordicpeak.flowengine.enums.SenderType;

public class ExternalMessageAddedEvent implements Serializable {

	private static final long serialVersionUID = 1873981649620240956L;

	private final FlowInstance flowInstance;
	private final FlowInstanceEvent event;
	private final SiteProfile siteProfile;
	private final ExternalMessage externalMessage;
	private final SenderType senderType;

	public ExternalMessageAddedEvent(FlowInstance flowInstance, FlowInstanceEvent event, SiteProfile siteProfile, ExternalMessage externalMessage, SenderType senderType) {

		super();
		this.flowInstance = flowInstance;
		this.event = event;
		this.siteProfile = siteProfile;
		this.externalMessage = externalMessage;
		this.senderType = senderType;
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

	public ExternalMessage getExternalMessage() {

		return externalMessage;
	}

	public SenderType getSenderType() {

		return senderType;
	}
}
