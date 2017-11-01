package com.nordicpeak.flowengine.flowinstancerafflesummary.validationerrors;

import se.unlogic.standardutils.validation.ValidationError;
import se.unlogic.standardutils.xml.XMLElement;

import com.nordicpeak.flowengine.beans.FlowInstance;

@XMLElement(name = "validationError")
public class FlowInstanceAlreadyRaffled extends ValidationError {

	@XMLElement
	private FlowInstance flowInstance;
	
	public FlowInstanceAlreadyRaffled(FlowInstance flowInstance) {
		super("FlowInstanceAlreadyRaffled");

		this.flowInstance = flowInstance;
	}

}
