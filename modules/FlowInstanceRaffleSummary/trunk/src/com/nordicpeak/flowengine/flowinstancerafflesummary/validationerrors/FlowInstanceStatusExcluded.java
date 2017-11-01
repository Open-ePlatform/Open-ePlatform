package com.nordicpeak.flowengine.flowinstancerafflesummary.validationerrors;

import se.unlogic.standardutils.validation.ValidationError;
import se.unlogic.standardutils.xml.XMLElement;

import com.nordicpeak.flowengine.beans.FlowInstance;

@XMLElement(name = "validationError")
public class FlowInstanceStatusExcluded extends ValidationError {

	@XMLElement
	private FlowInstance flowInstance;
	
	public FlowInstanceStatusExcluded(FlowInstance flowInstance, String fieldName) {
		super("FlowInstanceStatusExcluded", "", fieldName);

		this.flowInstance = flowInstance;
	}

}
