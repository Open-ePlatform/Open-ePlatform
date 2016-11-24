package com.nordicpeak.flowengine.accesscontrollers;

import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.hierarchy.core.exceptions.AccessDeniedException;
import se.unlogic.hierarchy.core.utils.AccessUtils;

import com.nordicpeak.flowengine.beans.Flow;
import com.nordicpeak.flowengine.interfaces.FlowInstanceAccessController;
import com.nordicpeak.flowengine.interfaces.ImmutableFlowInstance;


public class AdminUserFlowInstanceAccessController implements FlowInstanceAccessController {

	private final boolean mutable;
	
	public AdminUserFlowInstanceAccessController(boolean mutable) {

		super();
		this.mutable = mutable;
	}

	@Override
	public void checkNewFlowInstanceAccess(Flow flow, User user) throws AccessDeniedException {

		if(!AccessUtils.checkAccess(user, flow.getFlowType().getAdminAccessInterface())){

			throw new AccessDeniedException("User does not have access to flows belonging to flow type " + flow.getFlowType());
		}
	}

	@Override
	public void checkFlowInstanceAccess(ImmutableFlowInstance flowInstance, User user) throws AccessDeniedException {}

	@Override
	public boolean isMutable(ImmutableFlowInstance flowInstance, User user) {

		return mutable;
	}

}
