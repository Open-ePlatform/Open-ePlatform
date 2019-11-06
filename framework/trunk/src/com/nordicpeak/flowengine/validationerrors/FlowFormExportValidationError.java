package com.nordicpeak.flowengine.validationerrors;

import se.unlogic.standardutils.validation.ValidationError;
import se.unlogic.standardutils.xml.XMLElement;

import com.nordicpeak.flowengine.beans.FlowForm;

//TODO fix case...
@XMLElement(name = "validationError")
public class FlowFormExportValidationError extends ValidationError {
	
	private static final long serialVersionUID = 4725088396330035553L;
	@XMLElement
	private final FlowForm flowForm;
	
	public FlowFormExportValidationError(FlowForm flowForm) {
		
		super("FlowFormExportException");
		this.flowForm = flowForm;
	}
	
	public FlowForm getflowForm() {
		
		return flowForm;
	}
}
