package com.nordicpeak.flowengine.flowinstancerafflesummary.validationerrors;

import se.unlogic.standardutils.validation.ValidationError;
import se.unlogic.standardutils.xml.XMLElement;

import com.nordicpeak.flowengine.beans.FlowInstance;

@XMLElement(name = "validationError")
public class FlowInstanceNotInGrantedStatus extends ValidationError {

	@XMLElement
	private FlowInstance flowInstance;
	
	public FlowInstanceNotInGrantedStatus(FlowInstance flowInstance, String fieldName) {
		super("FlowInstanceNotInGrantedStatus", "", fieldName);

		this.flowInstance = flowInstance;
	}

}
