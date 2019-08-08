
package com.nordicpeak.flowengine.integration.callback.exceptions;

import java.util.List;

import se.unlogic.standardutils.string.StringUtils;

import com.nordicpeak.flowengine.integration.callback.Principal;
import com.nordicpeak.flowengine.integration.callback.PrincipalGroup;

public class InvalidManagers {

	private List<Principal> managers;
	private List<PrincipalGroup> managerGroups;

	public List<Principal> getManagers() {
		return managers;
	}

	public void setManagers(List<Principal> managers) {
		this.managers = managers;
	}

	public List<PrincipalGroup> getManagerGroups() {
		return managerGroups;
	}

	public void setManagerGroups(List<PrincipalGroup> managerGroups) {
		this.managerGroups = managerGroups;
	}

	@Override
	public String toString() {
		return "(managers=" + (managers != null ? StringUtils.toCommaSeparatedString(managers) : null) + ", managerGroups=" + (managerGroups != null ? StringUtils.toCommaSeparatedString(managerGroups) : null) + ")";
	}
}
