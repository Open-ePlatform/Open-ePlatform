package com.nordicpeak.flowengine.comparators;

import java.util.Comparator;

import se.unlogic.hierarchy.core.comparators.PriorityComparator;

import com.nordicpeak.flowengine.interfaces.FlowAdminExtensionViewProvider;


public class FlowAdminExtensionViewProviderComparator implements Comparator<FlowAdminExtensionViewProvider> {
	
	private static final FlowAdminExtensionViewProviderComparator INSTANCE = new FlowAdminExtensionViewProviderComparator();
	
	@Override
	public int compare(FlowAdminExtensionViewProvider o1, FlowAdminExtensionViewProvider o2) {

		int result = PriorityComparator.ASC_COMPARATOR.compare(o1, o2);
		
		if(result == 0) {
			
			result = o1.getExtensionViewTitle().compareTo(o2.getExtensionViewTitle());
		}
		
		return result;
	}

	public static FlowAdminExtensionViewProviderComparator getInstance(){
		
		return INSTANCE;
	}
}
