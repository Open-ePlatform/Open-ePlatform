package com.nordicpeak.flowengine.exceptions.queryinstance;

import com.nordicpeak.flowengine.interfaces.ImmutableQueryInstanceDescriptor;

public class UnableToGetQueryInstancePDFContentException extends QueryInstanceHTMLException {

	private static final long serialVersionUID = -4969774142001273042L;

	public UnableToGetQueryInstancePDFContentException(ImmutableQueryInstanceDescriptor queryInstanceDescriptor, Throwable cause) {

		super(queryInstanceDescriptor, cause);
	}

}
