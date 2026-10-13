package com.nordicpeak.flowengine.comparators;

import java.util.Comparator;

import se.unlogic.hierarchy.core.beans.Group;


public class GroupNameComparator implements Comparator<Group> {

	private static final GroupNameComparator INSTANCE = new GroupNameComparator();
	
	public static GroupNameComparator getInstance() {
		return INSTANCE;
	}
	
	@Override
	public int compare(Group o1, Group o2) {
		
		return o1.getName().compareTo(o2.getName());
	}
	
}
