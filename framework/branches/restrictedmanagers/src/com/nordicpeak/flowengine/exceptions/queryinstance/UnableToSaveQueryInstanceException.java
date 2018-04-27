package com.nordicpeak.flowengine.exceptions.queryinstance;

import com.nordicpeak.flowengine.interfaces.ImmutableQueryInstanceDescriptor;

public class UnableToSaveQueryInstanceException extends QueryInstanceException {

	private static final long serialVersionUID = 5461000666027945165L;
	
	public UnableToSaveQueryInstanceException(ImmutableQueryInstanceDescriptor queryInstanceDescriptor, Throwable cause) {

		super(queryInstanceDescriptor, cause);
	}
}
