package com.nordicpeak.flowengine.beans;

import com.nordicpeak.flowengine.enums.ManagerAccess;

public class FlowFamilyManagerDetailedAccess {

	private ManagerAccess access;

	private boolean allowUpdatingManagers;

	public ManagerAccess getAccess() {
		return access;
	}

	public void setAccess(ManagerAccess access) {
		this.access = access;
	}

	public boolean isAllowUpdatingManagers() {
		return allowUpdatingManagers;
	}

	public void setAllowUpdatingManagers(boolean allowUpdatingManagers) {
		this.allowUpdatingManagers = allowUpdatingManagers;
	}

}
