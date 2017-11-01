package com.nordicpeak.flowengine.flowinstancerafflesummary.validationerrors;

import se.unlogic.standardutils.validation.ValidationError;
import se.unlogic.standardutils.xml.XMLElement;

@XMLElement(name = "validationError")
public class PaymentExceedsGrantValidationError extends ValidationError {

	@XMLElement
	private Integer grant;
	
	@XMLElement
	private Integer payment;
	
	@XMLElement
	private Integer totalPayment;

	public PaymentExceedsGrantValidationError(String fieldName, Integer grant, Integer payment, Integer totalPayment) {
		super("PaymentExceedsGrant", "", fieldName);

		this.grant = grant;
		this.payment = payment;
		this.totalPayment = totalPayment;
	}

}
