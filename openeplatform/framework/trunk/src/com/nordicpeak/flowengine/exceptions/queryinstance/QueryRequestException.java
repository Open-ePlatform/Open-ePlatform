package com.nordicpeak.flowengine.exceptions.queryinstance;

import com.nordicpeak.flowengine.interfaces.ImmutableQueryInstanceDescriptor;



public class QueryRequestException extends QueryInstanceException {

	private static final long serialVersionUID = -4056806067621536904L;

	public QueryRequestException(ImmutableQueryInstanceDescriptor queryInstanceDescriptor, Throwable cause) {

		super(queryInstanceDescriptor, cause);
	}
}
