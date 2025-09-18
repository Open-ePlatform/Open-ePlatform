package com.nordicpeak.flowengine.attachments.exception;

public class FlowNotFoundException  extends Exception {

	private static final long serialVersionUID = 2237262634158628318L;
	
	private Integer flowID; 
	
	public FlowNotFoundException(Integer flowID) {
		this.flowID = flowID;
	}	

	@Override
	public String toString() {
		return "Could not get flow with flowID "+flowID;
	}
	
}
