package com.nordicpeak.flowengine.utils;

import com.nordicpeak.flowengine.beans.Flow;
import com.nordicpeak.flowengine.interfaces.Icon;

public class FlowIconUtils {

	public static Icon getFlowIcon(Flow flow) {

		if (flow != null && flow.getFlowType().useIconOnAllFlows()) {

			return flow.getFlowType();
		}

		return flow;
	}

}
