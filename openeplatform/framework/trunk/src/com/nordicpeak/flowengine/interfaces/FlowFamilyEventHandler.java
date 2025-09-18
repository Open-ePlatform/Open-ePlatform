package com.nordicpeak.flowengine.interfaces;

import se.unlogic.hierarchy.core.beans.User;

import com.nordicpeak.flowengine.beans.FlowFamily;

public interface FlowFamilyEventHandler {
	
	public void addFlowFamilyEvent(String message, ImmutableFlow flow, User user);
	public void addFlowFamilyEvent(String message, FlowFamily flowFamily, User user);
}
