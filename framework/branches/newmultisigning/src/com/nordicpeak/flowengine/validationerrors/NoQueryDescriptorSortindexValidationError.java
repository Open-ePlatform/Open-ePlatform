package com.nordicpeak.flowengine.validationerrors;

import se.unlogic.standardutils.validation.ValidationError;
import se.unlogic.standardutils.xml.XMLElement;

import com.nordicpeak.flowengine.beans.QueryDescriptor;

@XMLElement(name = "validationError")
public class NoQueryDescriptorSortindexValidationError extends ValidationError {

	@XMLElement
	private QueryDescriptor queryDescriptor;

	public NoQueryDescriptorSortindexValidationError(QueryDescriptor queryDescriptor) {

		super("NoQueryDescriptorSortindex");
	}

	public QueryDescriptor getQueryDescriptor() {

		return queryDescriptor;
	}
}
