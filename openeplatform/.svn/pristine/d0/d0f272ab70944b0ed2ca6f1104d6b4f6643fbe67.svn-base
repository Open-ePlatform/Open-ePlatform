package com.nordicpeak.flowengine.integration.callback;

public class FlowInstanceIDMutex {

	private final Integer flowInstanceID;

	private final ExternalID externalID;

	public FlowInstanceIDMutex(Integer flowInstanceID, ExternalID externalID) {

		super();
		this.flowInstanceID = flowInstanceID;
		this.externalID = externalID;
	}

	@Override
	public int hashCode() {

		final int prime = 31;
		int result = 1;
		result = prime * result + ((externalID == null) ? 0 : externalID.hashCode());
		result = prime * result + ((flowInstanceID == null) ? 0 : flowInstanceID.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {

		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		FlowInstanceIDMutex other = (FlowInstanceIDMutex) obj;
		if (externalID == null) {
			if (other.externalID != null) {
				return false;
			}
		} else if (!externalID.equals(other.externalID)) {
			return false;
		}
		if (flowInstanceID == null) {
			if (other.flowInstanceID != null) {
				return false;
			}
		} else if (!flowInstanceID.equals(other.flowInstanceID)) {
			return false;
		}
		return true;
	}

}
