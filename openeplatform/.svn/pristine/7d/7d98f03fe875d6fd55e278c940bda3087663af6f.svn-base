package com.nordicpeak.flowengine.validationerrors;

import se.unlogic.standardutils.validation.ValidationError;
import se.unlogic.standardutils.xml.XMLElement;

import com.nordicpeak.flowengine.beans.QueryDescriptor;

@XMLElement(name = "validationError")
public class NoQueryDescriptorSortindexValidationError extends ValidationError {

	private static final long serialVersionUID = -1496501677721095017L;
	@XMLElement
	private QueryDescriptor queryDescriptor;

	public NoQueryDescriptorSortindexValidationError(QueryDescriptor queryDescriptor) {

		super("NoQueryDescriptorSortindex");
	}

	public QueryDescriptor getQueryDescriptor() {

		return queryDescriptor;
	}
}
