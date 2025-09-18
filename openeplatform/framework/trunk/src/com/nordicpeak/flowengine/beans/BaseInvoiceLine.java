package com.nordicpeak.flowengine.beans;

import java.math.BigDecimal;

import se.unlogic.standardutils.date.BigDecimalStringyfier;
import se.unlogic.standardutils.xml.GeneratedElementable;
import se.unlogic.standardutils.xml.XMLElement;

import com.nordicpeak.flowengine.interfaces.InvoiceLine;

@XMLElement(name = "InvoiceLine")
public class BaseInvoiceLine extends GeneratedElementable implements InvoiceLine {

	@XMLElement(valueFormatter = BigDecimalStringyfier.class)
	private BigDecimal quantity;

	@XMLElement(valueFormatter = BigDecimalStringyfier.class)
	private BigDecimal unitPrice;

	@XMLElement
	private String description;

	@XMLElement
	private String unitMeasure;

	public BaseInvoiceLine(int quantity, int unitPrice, String description, String unitMeasure) {
		super();

		this.quantity = BigDecimal.valueOf(quantity);
		this.unitPrice = BigDecimal.valueOf(unitPrice);
		this.description = description;
		this.unitMeasure = unitMeasure;
	}

	public BaseInvoiceLine(BigDecimal quantity, BigDecimal unitPrice, String description, String unitMeasure) {
		super();

		this.quantity = quantity;
		this.unitPrice = unitPrice;
		this.description = description;
		this.unitMeasure = unitMeasure;
	}
	
	@Override
	public String getDescription() {

		return description;
	}

	@Override
	public BigDecimal getQuantity() {

		return quantity;
	}

	@Override
	public BigDecimal getUnitPrice() {

		return unitPrice;
	}

	public void setQuantity(BigDecimal quantity) {

		this.quantity = quantity;
	}

	public void setUnitPrice(BigDecimal unitPrice) {

		this.unitPrice = unitPrice;
	}

	public void setDescription(String description) {

		this.description = description;
	}

	@Override
	public String getUnitMeasure() {

		return unitMeasure;
	}

	public void setUnitMeasure(String unitMeasure) {

		this.unitMeasure = unitMeasure;
	}

}
