package com.nordicpeak.flowengine.exceptions.queryinstance;

import com.nordicpeak.flowengine.interfaces.ImmutableQueryInstanceDescriptor;

public class UnableToResetQueryInstanceException extends QueryInstanceException {

	private static final long serialVersionUID = 5449878463401561768L;

	public UnableToResetQueryInstanceException(ImmutableQueryInstanceDescriptor queryInstanceDescriptor, Throwable cause) {

		super(queryInstanceDescriptor, cause);
	}

}
