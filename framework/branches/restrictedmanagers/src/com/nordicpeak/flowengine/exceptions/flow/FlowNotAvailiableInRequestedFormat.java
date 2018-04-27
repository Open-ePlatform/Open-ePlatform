package com.nordicpeak.flowengine.exceptions.flow;

import com.nordicpeak.flowengine.exceptions.FlowEngineException;

public class FlowNotAvailiableInRequestedFormat extends FlowEngineException {

	private static final long serialVersionUID = 5892913131193190721L;

	public FlowNotAvailiableInRequestedFormat(int flowID) {

		super("Flow " + flowID + " is not availiable in requested format");
	}
}
