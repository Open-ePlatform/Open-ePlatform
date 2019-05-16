package com.nordicpeak.flowengine.accesscontrollers;

import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.hierarchy.core.exceptions.AccessDeniedException;
import se.unlogic.openhierarchy.foregroundmodules.siteprofile.interfaces.SiteProfile;

import com.nordicpeak.flowengine.beans.Flow;
import com.nordicpeak.flowengine.beans.FlowFamilyManagerDetailedAccess;
import com.nordicpeak.flowengine.enums.ManagerAccess;
import com.nordicpeak.flowengine.interfaces.FlowInstanceAccessController;
import com.nordicpeak.flowengine.interfaces.ImmutableFlowInstance;

public class ManagerFlowInstanceAccessController implements FlowInstanceAccessController {

	protected final boolean requireMutableState;
	protected final boolean requireDeletableState;
	protected final boolean requireFullManager;
	protected final boolean requireUpdateManagers;

	public ManagerFlowInstanceAccessController(boolean requireMutableState, boolean requireDeletableState, boolean requireFullManager, boolean requireUpdateManagers) {

		super();
		this.requireMutableState = requireMutableState;
		this.requireDeletableState = requireDeletableState;
		this.requireFullManager = requireFullManager;
		this.requireUpdateManagers = requireUpdateManagers;
	}

	@Override
	public void checkNewFlowInstanceAccess(Flow flow, User user, SiteProfile profile) throws AccessDeniedException {}

	@Override
	public void checkFlowInstanceAccess(ImmutableFlowInstance flowInstance, User user) throws AccessDeniedException {

		if (flowInstance.getFirstSubmitted() == null) {

			throw new AccessDeniedException("Access denied to flow instance " + flowInstance + ", the requested instance has not been submitted.");
		}

		checkManagerAccess(flowInstance, user);

		if (requireMutableState && !flowInstance.getStatus().isAdminMutable()) {

			throw new AccessDeniedException("Access denied to flow instance " + flowInstance + ", the requested instance is not in a manager mutable state.");

		} else if (requireDeletableState && !flowInstance.getStatus().isAdminDeletable()) {

			throw new AccessDeniedException("Access denied to flow instance " + flowInstance + ", the requested instance is not in a manager deletable state.");
		}
	}

	@Override
	public boolean isMutable(ImmutableFlowInstance flowInstance, User user) {

		return flowInstance.getStatus().isAdminMutable();
	}

	public void checkManagerAccess(ImmutableFlowInstance flowInstance, User user) throws AccessDeniedException {

		FlowFamilyManagerDetailedAccess detailedAccess = flowInstance.getFlow().getFlowFamily().getManagerDetailedAccess(user);

		if (detailedAccess == null) {

			throw new AccessDeniedException("User is not manager for flow family " + flowInstance.getFlow().getFlowFamily());
		}

		if (detailedAccess.getAccess() == ManagerAccess.RESTRICTED) {

			if (requireFullManager) {

				throw new AccessDeniedException("User is restricted manager for flow family " + flowInstance.getFlow().getFlowFamily() + " but full manager access is required for the requested operation");

			} else if (!flowInstance.isManager(user)) {

				throw new AccessDeniedException("User is restricted manager for flow family " + flowInstance.getFlow().getFlowFamily() + " but not manager for flow instace " + flowInstance);
			}

			if (requireUpdateManagers && !detailedAccess.isAllowUpdatingManagers()) {
				
				throw new AccessDeniedException("User is not allowed to update flow instance managers for flow family " + flowInstance.getFlow().getFlowFamily());
			}
		}
	}
}
