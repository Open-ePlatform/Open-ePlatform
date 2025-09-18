package com.nordicpeak.flowengine.exceptions.queryinstance;

import com.nordicpeak.flowengine.interfaces.ImmutableQueryInstanceDescriptor;


public class IllegalQueryInstanceAccessException extends QueryInstanceException {

	private static final long serialVersionUID = -8849511164383535092L;

	public IllegalQueryInstanceAccessException(ImmutableQueryInstanceDescriptor queryInstanceDescriptor, String message) {

		super(queryInstanceDescriptor, message);
	}
}
