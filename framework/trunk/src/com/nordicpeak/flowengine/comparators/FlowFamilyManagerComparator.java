package com.nordicpeak.flowengine.comparators;

import java.util.Comparator;

import se.unlogic.hierarchy.foregroundmodules.usersessionadmin.UserNameComparator;

import com.nordicpeak.flowengine.beans.FlowFamilyManager;

public class FlowFamilyManagerComparator implements Comparator<FlowFamilyManager> {

	private static final UserNameComparator USER_NAME_COMPARATOR = new UserNameComparator();
	
	private static final FlowFamilyManagerComparator COMPARATOR = new FlowFamilyManagerComparator();

	public static FlowFamilyManagerComparator getComparator(){
		return COMPARATOR;
	}
	
	@Override
	public int compare(FlowFamilyManager o1, FlowFamilyManager o2) {
		
		if (o1 != null && o2 != null) {
			
			boolean a1 = o1.isActive();
			boolean a2 = o2.isActive();
			
			if (a1 == a2) {
				
				return USER_NAME_COMPARATOR.compare(o1.getUser(), o2.getUser());
				
			} else if (a1) {
				
				return -1;
				
			} else {
				
				return 1;
			}
			
		} else if (o1 != null) {
			
			return -1;
			
		} else {
			
			return 1;
		}
	}

}
