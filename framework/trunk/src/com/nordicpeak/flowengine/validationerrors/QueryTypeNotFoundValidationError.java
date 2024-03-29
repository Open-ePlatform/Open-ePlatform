package com.nordicpeak.flowengine.validationerrors;

import se.unlogic.standardutils.validation.ValidationError;
import se.unlogic.standardutils.xml.XMLElement;

import com.nordicpeak.flowengine.beans.QueryDescriptor;

//TODO fix case...
@XMLElement(name = "validationError")
public class QueryTypeNotFoundValidationError extends ValidationError {

	private static final long serialVersionUID = -4953259109636144675L;
	@XMLElement
	private final QueryDescriptor queryDescriptor;

	public QueryTypeNotFoundValidationError(QueryDescriptor queryDescriptor) {

		super("QueryTypeNotFound");
		this.queryDescriptor = queryDescriptor;
	}

	public QueryDescriptor getQueryDescriptor() {

		return queryDescriptor;
	}
}
