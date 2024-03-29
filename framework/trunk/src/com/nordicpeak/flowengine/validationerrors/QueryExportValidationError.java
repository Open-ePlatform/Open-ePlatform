package com.nordicpeak.flowengine.validationerrors;

import se.unlogic.standardutils.validation.ValidationError;
import se.unlogic.standardutils.xml.XMLElement;

import com.nordicpeak.flowengine.beans.QueryDescriptor;

//TODO fix case...
@XMLElement(name="validationError")
public class QueryExportValidationError extends ValidationError {

	private static final long serialVersionUID = -3107235548743682859L;
	@XMLElement
	private final QueryDescriptor queryDescriptor;

	public QueryExportValidationError(QueryDescriptor queryDescriptor) {

		super("QueryExportException");
		this.queryDescriptor = queryDescriptor;
	}


	public QueryDescriptor getQueryDescriptor() {

		return queryDescriptor;
	}
}
