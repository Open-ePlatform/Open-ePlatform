package com.nordicpeak.flowengine.interfaces;

import java.util.List;

import com.nordicpeak.flowengine.beans.Flow;
import com.nordicpeak.flowengine.beans.FlowFamilyManager;
import com.nordicpeak.flowengine.beans.FlowFamilyManagerGroup;
import com.nordicpeak.flowengine.enums.ManagerAccess;

import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.standardutils.xml.Elementable;

public interface ImmutableFlowFamily extends Elementable {

	public Integer getFlowFamilyID();

	public Integer getVersionCount();

	public List<Flow> getFlows();
	
	public List<Integer> getManagerUserIDs();
		
	public List<Integer> getManagerGroupIDs();
	
	public List<FlowFamilyManager> getManagerUsers();
	
	public List<FlowFamilyManagerGroup> getManagerGroups();

	public ManagerAccess getManagerAccess(User user);
}