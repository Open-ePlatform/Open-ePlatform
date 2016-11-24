package com.nordicpeak.flowengine.queries.checkboxquery.validationerrors;

import se.unlogic.standardutils.validation.ValidationError;
import se.unlogic.standardutils.xml.XMLElement;

@XMLElement(name = "validationError")
public class TooFewAlternativesSelectedValidationError extends ValidationError {

	@XMLElement
	private final Integer checked;

	@XMLElement
	private final Integer minChecked;

	public TooFewAlternativesSelectedValidationError(Integer checked, Integer minChecked) {

		super("TooFewAlternativesSelected");
		this.checked = checked;
		this.minChecked = minChecked;
	}

	public Integer getChecked() {

		return checked;
	}

	public Integer getMinChecked() {

		return minChecked;
	}
}
