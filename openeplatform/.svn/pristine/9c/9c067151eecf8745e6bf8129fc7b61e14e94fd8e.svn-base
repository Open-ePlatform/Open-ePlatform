package com.nordicpeak.flowengine.accesscontrollers;

import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.hierarchy.core.exceptions.AccessDeniedException;
import se.unlogic.hierarchy.core.utils.AccessUtils;
import se.unlogic.openhierarchy.foregroundmodules.siteprofile.interfaces.SiteProfile;

import com.nordicpeak.flowengine.FlowAdminModule;
import com.nordicpeak.flowengine.beans.Flow;
import com.nordicpeak.flowengine.interfaces.FlowInstanceAccessController;
import com.nordicpeak.flowengine.interfaces.ImmutableFlowInstance;


public class AdminUserFlowInstanceAccessController implements FlowInstanceAccessController {

	private final FlowAdminModule flowAdminModule;
	private final boolean mutable;
	
	public AdminUserFlowInstanceAccessController(FlowAdminModule flowAdminModule, boolean mutable) {

		super();
		this.flowAdminModule = flowAdminModule;
		this.mutable = mutable;
	}

	@Override
	public void checkNewFlowInstanceAccess(Flow flow, User user, SiteProfile profile) throws AccessDeniedException {

		if(!AccessUtils.checkAccess(user, flowAdminModule) && !AccessUtils.checkAccess(user, flow.getFlowType().getAdminAccessInterface())){

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
