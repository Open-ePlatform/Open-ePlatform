package com.nordicpeak.flowengine.validationerrors;

import se.unlogic.standardutils.validation.ValidationError;
import se.unlogic.standardutils.xml.XMLElement;

import com.nordicpeak.flowengine.utils.flowfamilylist.bean.FlowFamilyListItem;

@XMLElement(name = "validationError")
public class FlowFamilyInUseValidationError extends ValidationError {
	
	private static final long serialVersionUID = -2350703591299509892L;
	@XMLElement
	private final FlowFamilyListItem flowFamilyList;

	public FlowFamilyInUseValidationError(FlowFamilyListItem flowFamilyList) {

		super("FlowFamilyInUseValidationError");
		this.flowFamilyList = flowFamilyList;
	}

	
	public FlowFamilyListItem getFlowFamilyList() {
	
		return flowFamilyList;
	}

	@Override
	public int hashCode() {

		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((flowFamilyList == null) ? 0 : flowFamilyList.hashCode());
		return result;
	}


	@Override
	public boolean equals(Object obj) {

		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		FlowFamilyInUseValidationError other = (FlowFamilyInUseValidationError) obj;
		if (flowFamilyList == null) {
			if (other.flowFamilyList != null)
				return false;
		} else if (!flowFamilyList.equals(other.flowFamilyList))
			return false;
		return true;
	}

}