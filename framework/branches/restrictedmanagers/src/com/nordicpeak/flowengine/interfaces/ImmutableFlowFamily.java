package com.nordicpeak.flowengine.interfaces;

import java.util.List;

import com.nordicpeak.flowengine.beans.AutoManagerAssignmentRule;
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
	
	public List<Integer> getActiveManagerUserIDs();
		
	public List<Integer> getManagerGroupIDs();
	
	public List<FlowFamilyManager> getManagers();
	
	public List<FlowFamilyManagerGroup> getManagerGroups();
	
	public ManagerAccess getManagerAccess(User user);
	
	public List<AutoManagerAssignmentRule> getAutoManagerAssignmentRules();
	
	public List<Integer> getAutoManagerAssignmentAlwaysUserIDs();
	
	public List<Integer> getAutoManagerAssignmentAlwaysGroupIDs();
	
	public List<Integer> getAutoManagerAssignmentNoMatchUserIDs();
	
	public List<Integer> getAutoManagerAssignmentNoMatchGroupIDs();
	
}