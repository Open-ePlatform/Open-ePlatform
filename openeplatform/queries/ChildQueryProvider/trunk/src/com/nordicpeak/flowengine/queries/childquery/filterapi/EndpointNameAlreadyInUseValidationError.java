package com.nordicpeak.flowengine.queries.childquery.filterapi;

import se.unlogic.standardutils.validation.ValidationError;
import se.unlogic.standardutils.xml.XMLElement;

import com.nordicpeak.flowengine.queries.childquery.interfaces.ChildQueryFilterEndpoint;
import com.nordicpeak.flowengine.queries.childquery.interfaces.ChildQueryFilterProvider;

@XMLElement(name = "validationError")
public class EndpointNameAlreadyInUseValidationError extends ValidationError {

	private static final long serialVersionUID = -5722978774396903664L;

	@XMLElement
	protected ChildQueryFilterEndpoint endpoint;

	@XMLElement
	protected String providerName;

	public EndpointNameAlreadyInUseValidationError(ChildQueryFilterEndpoint existingEndpoint, ChildQueryFilterProvider existingProvider) {
		super("EndpointNameAlreadyInUse");

		this.endpoint = existingEndpoint;
		providerName = existingProvider.getName();
	}

}
