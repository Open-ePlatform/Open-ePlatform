package com.nordicpeak.flowengine.exceptions.flowinstance;

import com.nordicpeak.flowengine.interfaces.ImmutableFlowInstance;


public class FlowInstanceNoLongerAvailableException extends FlowInstanceException {

	private static final long serialVersionUID = 6083112446441654492L;

	public FlowInstanceNoLongerAvailableException(ImmutableFlowInstance flowInstance) {

		super("The requested flow instance is no longer available in the database", flowInstance);
	}

}
