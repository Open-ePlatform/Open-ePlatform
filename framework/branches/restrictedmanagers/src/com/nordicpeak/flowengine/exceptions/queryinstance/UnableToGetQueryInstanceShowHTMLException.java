package com.nordicpeak.flowengine.exceptions.queryinstance;

import com.nordicpeak.flowengine.interfaces.ImmutableQueryInstanceDescriptor;

public class UnableToGetQueryInstanceShowHTMLException extends QueryInstanceHTMLException {

	private static final long serialVersionUID = -6241505063990519740L;

	public UnableToGetQueryInstanceShowHTMLException(ImmutableQueryInstanceDescriptor queryInstanceDescriptor, Throwable cause) {

		super(queryInstanceDescriptor, cause);
	}
}
