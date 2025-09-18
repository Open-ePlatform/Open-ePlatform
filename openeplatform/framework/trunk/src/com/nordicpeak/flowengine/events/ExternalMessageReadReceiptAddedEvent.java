package com.nordicpeak.flowengine.events;

import java.io.Serializable;

import se.unlogic.openhierarchy.foregroundmodules.siteprofile.interfaces.SiteProfile;

import com.nordicpeak.flowengine.beans.ExternalMessageReadReceipt;
import com.nordicpeak.flowengine.beans.FlowInstance;
import com.nordicpeak.flowengine.beans.FlowInstanceEvent;

public class ExternalMessageReadReceiptAddedEvent implements Serializable {

	private static final long serialVersionUID = 1873981649620240956L;

	private final FlowInstance flowInstance;
	private final FlowInstanceEvent event;
	private final SiteProfile siteProfile;
	private final ExternalMessageReadReceipt readReceipt;

	public ExternalMessageReadReceiptAddedEvent(FlowInstance flowInstance, FlowInstanceEvent event, SiteProfile siteProfile, ExternalMessageReadReceipt readReceipt) {

		this.flowInstance = flowInstance;
		this.event = event;
		this.siteProfile = siteProfile;
		this.readReceipt = readReceipt;
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

	public ExternalMessageReadReceipt getReadReceipt() {

		return readReceipt;
	}
}
