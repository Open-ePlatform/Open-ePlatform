package com.nordicpeak.flowengine.accesscontrollers;

import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.hierarchy.core.exceptions.AccessDeniedException;
import se.unlogic.openhierarchy.foregroundmodules.siteprofile.interfaces.SiteProfile;

import com.nordicpeak.flowengine.beans.Flow;
import com.nordicpeak.flowengine.interfaces.FlowInstanceAccessController;
import com.nordicpeak.flowengine.interfaces.ImmutableFlowInstance;

public class UserFlowInstanceAccessController implements FlowInstanceAccessController {

	private boolean requireMutableState;
	private boolean requireDeletableState;

	public UserFlowInstanceAccessController(boolean requireMutableState, boolean requireDeletableState) {

		super();
		this.requireMutableState = requireMutableState;
		this.requireDeletableState = requireDeletableState;
	}

	@Override
	public void checkNewFlowInstanceAccess(Flow flow, User user, SiteProfile profile) throws AccessDeniedException {}

	@Override
	public void checkFlowInstanceAccess(ImmutableFlowInstance flowInstance, User user) throws AccessDeniedException {

		if (flowInstance.getOwners() == null || user == null || !flowInstance.getOwners().contains(user)) {

			throw new AccessDeniedException("Access denied to flow instance " + flowInstance + ", the current user is not owner of the requested instance.");

		} else if (requireMutableState && !flowInstance.getStatus().isUserMutable()) {

			throw new AccessDeniedException("Access denied to flow instance " + flowInstance + ", the requested instance is not in a user mutable state.");

		} else if (requireDeletableState && !flowInstance.getStatus().isUserDeletable()) {

			throw new AccessDeniedException("Access denied to flow instance " + flowInstance + ", the requested instance is not in a user deletable state.");

		} else if (flowInstance.getFlow().isHideFromUser() && flowInstance.getFirstSubmitted() != null) {

			throw new AccessDeniedException("Access denied to flow instance " + flowInstance + ", the submitted flow instance is not visible to owners.");
		}
	}

	@Override
	public boolean isMutable(ImmutableFlowInstance flowInstance, User user) {

		return flowInstance.getStatus() != null && flowInstance.getStatus().isUserMutable();
	}
}
