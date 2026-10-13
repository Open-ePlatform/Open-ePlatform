package com.nordicpeak.flowengine.comparators;

import java.util.Comparator;

import se.unlogic.standardutils.enums.Order;

import com.nordicpeak.flowengine.beans.Flow;

public class FlowVersionComparator implements Comparator<Flow> {

	private final Order sortOrder;
	
	public FlowVersionComparator(Order sortOrder) {
		
		this.sortOrder = sortOrder;
	}
	
	@Override
	public int compare(Flow f1, Flow f2) {

		if(sortOrder == Order.ASC) {
		
			return f1.getVersion().compareTo(f2.getVersion());
		}
		
		return f2.getVersion().compareTo(f1.getVersion());
	}

}
