package com.nordicpeak.flowengine.comparators;

import java.util.Comparator;

import com.nordicpeak.flowengine.beans.FlowFamilyManagerGroup;

public class FlowFamilyManagerGroupComparator implements Comparator<FlowFamilyManagerGroup> {
	
	private static final FlowFamilyManagerGroupComparator COMPARATOR = new FlowFamilyManagerGroupComparator();
	
	public static FlowFamilyManagerGroupComparator getComparator() {
		return COMPARATOR;
	}
	
	@Override
	public int compare(FlowFamilyManagerGroup o1, FlowFamilyManagerGroup o2) {
		
		if (o1.getGroup() != null && o2.getGroup() != null) {
			
			return o1.getGroup().getName().compareTo(o2.getGroup().getName());
			
		} else if (o1.getGroup() != null) {
			
			return -1;
			
		} else if (o2.getGroup() != null) {
			
			return 1;
		}
		
		return 0;
	}
}