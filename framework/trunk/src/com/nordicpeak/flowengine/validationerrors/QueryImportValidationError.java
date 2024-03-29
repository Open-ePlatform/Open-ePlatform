package com.nordicpeak.flowengine.validationerrors;

import se.unlogic.standardutils.validation.ValidationError;
import se.unlogic.standardutils.xml.XMLElement;

import com.nordicpeak.flowengine.beans.QueryDescriptor;

//TODO fix case...
@XMLElement(name="validationError")
public class QueryImportValidationError extends ValidationError {

	private static final long serialVersionUID = 2956148262250035444L;
	@XMLElement
	private final QueryDescriptor queryDescriptor;

	public QueryImportValidationError(QueryDescriptor queryDescriptor) {

		super("QueryImportException");
		this.queryDescriptor = queryDescriptor;
	}


	public QueryDescriptor getQueryDescriptor() {

		return queryDescriptor;
	}
}
