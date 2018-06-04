package com.nordicpeak.flowengine.events;

import java.io.Serializable;

import se.unlogic.hierarchy.core.interfaces.modules.descriptors.ForegroundModuleDescriptor;
import se.unlogic.openhierarchy.foregroundmodules.siteprofile.interfaces.SiteProfile;

import com.nordicpeak.flowengine.beans.FlowInstanceEvent;
import com.nordicpeak.flowengine.managers.FlowInstanceManager;

public class SubmitEvent implements Serializable {

	private static final long serialVersionUID = 4129670393435968733L;

	private final FlowInstanceManager flowInstanceManager;
	private final FlowInstanceEvent event;
	private final ForegroundModuleDescriptor moduleDescriptor;
	private final String actionID;
	private final SiteProfile siteProfile;
	private final boolean forcePDF;

	public SubmitEvent(FlowInstanceManager flowInstanceManager, FlowInstanceEvent event, ForegroundModuleDescriptor moduleDescriptor, String actionID, SiteProfile siteProfile, boolean forcePDF) {

		super();
		this.flowInstanceManager = flowInstanceManager;
		this.event = event;
		this.moduleDescriptor = moduleDescriptor;
		this.actionID = actionID;
		this.siteProfile = siteProfile;
		this.forcePDF = forcePDF;
	}

	public FlowInstanceManager getFlowInstanceManager() {

		return flowInstanceManager;
	}

	public FlowInstanceEvent getEvent() {

		return event;
	}

	public ForegroundModuleDescriptor getModuleDescriptor() {

		return moduleDescriptor;
	}

	public String getActionID() {

		return actionID;
	}

	public SiteProfile getSiteProfile() {

		return siteProfile;
	}

	public boolean isForcePDF() {

		return forcePDF;
	}
	
	@Override
	public String toString(){
		
		return event.toString();
	}
}
