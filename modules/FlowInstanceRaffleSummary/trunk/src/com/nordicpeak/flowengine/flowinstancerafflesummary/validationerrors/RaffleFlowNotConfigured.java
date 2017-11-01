package com.nordicpeak.flowengine.flowinstancerafflesummary.validationerrors;

import se.unlogic.standardutils.validation.ValidationError;
import se.unlogic.standardutils.xml.XMLElement;

import com.nordicpeak.flowengine.flowinstancerafflesummary.beans.RaffleFlow;

@XMLElement(name = "validationError")
public class RaffleFlowNotConfigured extends ValidationError {

	@XMLElement
	private RaffleFlow raffleFlow;

	public RaffleFlowNotConfigured(RaffleFlow raffleFlow) {
		super("RaffleFlowNotConfigured");

		this.raffleFlow = raffleFlow;
	}

}
