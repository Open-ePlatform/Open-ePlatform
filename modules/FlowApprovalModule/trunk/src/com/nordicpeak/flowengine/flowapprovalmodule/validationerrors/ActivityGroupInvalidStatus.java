package com.nordicpeak.flowengine.flowapprovalmodule.validationerrors;

import se.unlogic.standardutils.validation.ValidationError;
import se.unlogic.standardutils.xml.XMLElement;

@XMLElement(name="validationError")
public class ActivityGroupInvalidStatus extends ValidationError {

	@XMLElement
	private String activityGroupName;
	
	@XMLElement
	private String invalidStatus;

	public ActivityGroupInvalidStatus(String activityGroupName, String invalidStatus) {
		super("ActivityGroupInvalidStatus");

		this.activityGroupName = activityGroupName;
		this.invalidStatus = invalidStatus;
	}

	
}
