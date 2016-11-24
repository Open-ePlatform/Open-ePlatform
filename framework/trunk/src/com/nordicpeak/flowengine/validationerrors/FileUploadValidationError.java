package com.nordicpeak.flowengine.validationerrors;

import se.unlogic.standardutils.validation.ValidationError;
import se.unlogic.standardutils.xml.XMLElement;

//TODO fix case...
@XMLElement(name="validationError")
public class FileUploadValidationError extends ValidationError {

	@XMLElement
	private final int maxRequestSize;

	public FileUploadValidationError(int maxRequestSize) {

		super("FileUploadException");
		this.maxRequestSize = maxRequestSize;
	}


	public int getMaxRequestSize() {

		return maxRequestSize;
	}
}
