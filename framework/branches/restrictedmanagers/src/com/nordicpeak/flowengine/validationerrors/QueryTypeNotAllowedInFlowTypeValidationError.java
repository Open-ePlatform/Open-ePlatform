package com.nordicpeak.flowengine.validationerrors;

import se.unlogic.standardutils.validation.ValidationError;
import se.unlogic.standardutils.xml.XMLElement;

import com.nordicpeak.flowengine.beans.FlowType;
import com.nordicpeak.flowengine.beans.QueryDescriptor;

//TODO fix case...
@XMLElement(name = "validationError")
public class QueryTypeNotAllowedInFlowTypeValidationError extends ValidationError {

	@XMLElement
	private final QueryDescriptor queryDescriptor;

	@XMLElement
	private final FlowType flowType;

	public QueryTypeNotAllowedInFlowTypeValidationError(QueryDescriptor queryDescriptor, FlowType flowType) {

		super("QueryTypeNotAllowedInFlowTypeValidationError");
		this.queryDescriptor = queryDescriptor;
		this.flowType = flowType;
	}

	public QueryDescriptor getQueryDescriptor() {

		return queryDescriptor;
	}

	public FlowType getFlowType() {

		return flowType;
	}

}
