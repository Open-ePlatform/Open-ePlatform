package com.nordicpeak.childrelationprovider.exceptions;

public class IncompleteDataException extends ChildRelationProviderException {

	private static final long serialVersionUID = 7845992401201772898L;
	
	public IncompleteDataException() {
		super();
	}
	
	public IncompleteDataException(String message) {
		super(message);
	}
	
	public IncompleteDataException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public IncompleteDataException(Throwable cause) {
		super(cause);
	}
}
