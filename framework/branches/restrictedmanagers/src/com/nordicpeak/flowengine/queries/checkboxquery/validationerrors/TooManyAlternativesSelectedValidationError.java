package com.nordicpeak.flowengine.queries.checkboxquery.validationerrors;

import se.unlogic.standardutils.validation.ValidationError;
import se.unlogic.standardutils.xml.XMLElement;

@XMLElement(name = "validationError")
public class TooManyAlternativesSelectedValidationError extends ValidationError {

	@XMLElement
	private final Integer checked;

	@XMLElement
	private final Integer maxChecked;

	public TooManyAlternativesSelectedValidationError(Integer checked, Integer maxChecked) {

		super("TooManyAlternativesSelected");
		this.checked = checked;
		this.maxChecked = maxChecked;
	}


	public Integer getChecked() {

		return checked;
	}


	public Integer getMaxChecked() {

		return maxChecked;
	}
}
