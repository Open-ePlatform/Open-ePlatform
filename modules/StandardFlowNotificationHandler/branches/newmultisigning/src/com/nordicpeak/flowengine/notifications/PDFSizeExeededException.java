package com.nordicpeak.flowengine.notifications;


public class PDFSizeExeededException extends Exception {
	
	private static final long serialVersionUID = -1827856012377686675L;

	final private long size;

	public PDFSizeExeededException(long size) {
		super();
		this.size = size;
	}

	
	public long getSize() {
	
		return size;
	}
	
}
