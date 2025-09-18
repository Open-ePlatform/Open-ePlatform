package com.nordicpeak.flowengine.beans;

import java.math.BigDecimal;

import se.unlogic.standardutils.date.BigDecimalStringyfier;
import se.unlogic.standardutils.xml.GeneratedElementable;
import se.unlogic.standardutils.xml.XMLElement;

import com.nordicpeak.flowengine.interfaces.PaymentProduct;

@XMLElement(name = "PaymentProduct")
public class BasePaymentProduct extends GeneratedElementable implements PaymentProduct {

	@XMLElement(valueFormatter = BigDecimalStringyfier.class)
	private BigDecimal unitPrice;

	@XMLElement
	private String description;
	
	public BasePaymentProduct(BigDecimal unitPrice, String description) {
		
		this.unitPrice = unitPrice;
		this.description = description;
	}

	public BasePaymentProduct(int unitPrice, String description) {

		this.unitPrice = BigDecimal.valueOf(unitPrice);
		this.description = description;
	}

	@Override
	public BigDecimal getUnitPrice() {

		return unitPrice;
	}

	public void setUnitPrice(BigDecimal unitPrice) {

		this.unitPrice = unitPrice;
	}

	@Override
	public String getDescription() {

		return description;
	}

	public void setDescription(String description) {

		this.description = description;
	}

}
