package com.nordicpeak.flowengine.queries.childquery.filterapi;

import com.nordicpeak.childrelationprovider.exceptions.ChildRelationProviderException;

public class IncompleteFilterAPIDataException extends ChildRelationProviderException {

	private static final long serialVersionUID = 7845992401201772898L;
	
	public IncompleteFilterAPIDataException() {
		super();
	}
	
	public IncompleteFilterAPIDataException(String message) {
		super(message);
	}
	
	public IncompleteFilterAPIDataException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public IncompleteFilterAPIDataException(Throwable cause) {
		super(cause);
	}
}
