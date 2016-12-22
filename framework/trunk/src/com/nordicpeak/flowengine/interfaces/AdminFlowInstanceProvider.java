package com.nordicpeak.flowengine.interfaces;

import java.util.List;

import se.unlogic.hierarchy.core.beans.User;

import com.nordicpeak.flowengine.beans.FlowInstance;


public interface AdminFlowInstanceProvider {

	List<FlowInstance> getActiveFlowInstances(User user);
}
