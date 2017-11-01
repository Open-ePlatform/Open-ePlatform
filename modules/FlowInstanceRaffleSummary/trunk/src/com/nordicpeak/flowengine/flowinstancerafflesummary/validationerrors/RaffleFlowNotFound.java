package com.nordicpeak.flowengine.flowinstancerafflesummary.validationerrors;

import se.unlogic.standardutils.validation.ValidationError;
import se.unlogic.standardutils.xml.XMLElement;

@XMLElement(name = "validationError")
public class RaffleFlowNotFound extends ValidationError {

	@XMLElement
	private Integer raffleFlowID;

	public RaffleFlowNotFound(Integer raffleFlowID) {
		super("RaffleFlowNotFound");

		this.raffleFlowID = raffleFlowID;
	}

}
