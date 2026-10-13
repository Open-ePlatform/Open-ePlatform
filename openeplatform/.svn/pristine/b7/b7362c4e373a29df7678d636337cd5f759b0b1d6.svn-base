package com.nordicpeak.flowengine.events;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import se.unlogic.openhierarchy.foregroundmodules.siteprofile.interfaces.SiteProfile;

import com.nordicpeak.flowengine.beans.FlowFamily;
import com.nordicpeak.flowengine.beans.FlowInstance;

public class ExpiredFlowInstanceNotificationEvent implements Serializable {

	private static final long serialVersionUID = -3105656362605519707L;

	private FlowFamily flowFamily;

	private List<Integer> flowInstanceIDs;

	private Map<Integer, SiteProfile> flowInstanceIDToSiteProfileMap;

	public ExpiredFlowInstanceNotificationEvent(FlowFamily flowFamily, List<Integer> flowInstanceIDs, Map<Integer, SiteProfile> flowInstanceIDToSiteProfileMap) {

		this.flowFamily = flowFamily;
		this.flowInstanceIDs = flowInstanceIDs;
		this.flowInstanceIDToSiteProfileMap = flowInstanceIDToSiteProfileMap;
	}

	public FlowFamily getFlowFamily() {

		return flowFamily;
	}

	public List<Integer> getFlowInstanceIDs() {

		return flowInstanceIDs;
	}

	public SiteProfile getSiteProfile(FlowInstance flowInstance) {

		Integer flowInstanceID = flowInstance.getFlowInstanceID();

		return flowInstanceIDToSiteProfileMap.get(flowInstanceID);
	}

}
