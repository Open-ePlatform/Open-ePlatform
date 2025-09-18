package com.nordicpeak.flowengine.validationerrors;

import se.unlogic.standardutils.validation.ValidationError;
import se.unlogic.standardutils.xml.XMLElement;

//TODO fix case...
@XMLElement(name = "validationError")
public class FlowFamilyAliasCollisionValidationError extends ValidationError {

	private static final long serialVersionUID = 2266897517805476869L;

	@XMLElement
	private final String alias;
	
	@XMLElement
	private final String collidingFlowFamilyName;

	public FlowFamilyAliasCollisionValidationError(String alias, String collidingFlowFamilyName) {

		super("FlowFamilyAliasCollision");
		this.alias = alias;
		this.collidingFlowFamilyName = collidingFlowFamilyName;
	}

	public String getCollidingFlowFamilyName() {

		return collidingFlowFamilyName;
	}

	public String getAlias() {

		return alias;
	}
}
