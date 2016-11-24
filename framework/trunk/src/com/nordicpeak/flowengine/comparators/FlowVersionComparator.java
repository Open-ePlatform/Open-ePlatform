package com.nordicpeak.flowengine.comparators;

import java.util.Comparator;

import com.nordicpeak.flowengine.beans.Flow;

public class FlowVersionComparator implements Comparator<Flow> {

	@Override
	public int compare(Flow f1, Flow f2) {

		return f1.getVersion().compareTo(f2.getVersion());
	}

}
