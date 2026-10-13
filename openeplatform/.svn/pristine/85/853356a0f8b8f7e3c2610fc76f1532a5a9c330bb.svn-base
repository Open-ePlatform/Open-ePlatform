package com.nordicpeak.flowengine.comparators;

import java.util.Comparator;

import se.unlogic.hierarchy.foregroundmodules.usersessionadmin.UserNameComparator;

import com.nordicpeak.flowengine.beans.FlowFamilyManager;

public class FlowFamilyManagerComparator implements Comparator<FlowFamilyManager> {

	private static final FlowFamilyManagerComparator INSTANCE = new FlowFamilyManagerComparator();

	public static FlowFamilyManagerComparator getInstance() {

		return INSTANCE;
	}

	@Override
	public int compare(FlowFamilyManager o1, FlowFamilyManager o2) {

		if (o1.getUser() != null && o2.getUser() != null) {

			return UserNameComparator.getInstance().compare(o1.getUser(), o2.getUser());
			
		} else if (o1.getUser() != null) {

			return -1;

		} else if (o2.getUser() != null){

			return 1;
		}
		
		return 0;
		
	}

}
