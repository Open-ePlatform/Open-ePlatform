package com.nordicpeak.flowengine.beans;

import se.unlogic.standardutils.xml.GeneratedElementable;
import se.unlogic.standardutils.xml.XMLElement;

import com.nordicpeak.flowengine.interfaces.InvoiceLine;

@XMLElement(name = "InvoiceLine")
public class BaseInvoiceLine extends GeneratedElementable implements InvoiceLine {

	@XMLElement
	private int quantity;

	@XMLElement
	private int unitPrice;

	@XMLElement
	private String description;

	@XMLElement
	private String unitMeasure;
	
	public BaseInvoiceLine(int quantity, int unitPrice, String description, String unitMeasure) {

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
	public int getQuanitity() {

		return quantity;
	}

	@Override
	public int getUnitPrice() {

		return unitPrice;
	}

	public int getQuantity() {

		return quantity;
	}

	public void setQuantity(int quantity) {

		this.quantity = quantity;
	}

	public void setUnitPrice(int unitPrice) {

		this.unitPrice = unitPrice;
	}

	public void setDescription(String description) {

		this.description = description;
	}

	
	public String getUnitMeasure() {
	
		return unitMeasure;
	}

	
	public void setUnitMeasure(String unitMeasure) {
	
		this.unitMeasure = unitMeasure;
	}

}
