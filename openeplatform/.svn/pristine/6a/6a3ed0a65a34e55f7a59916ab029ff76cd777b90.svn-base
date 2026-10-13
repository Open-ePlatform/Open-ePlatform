package com.nordicpeak.flowengine.beans;

import java.util.Collection;


public class FlowTypeUserAccessInterface extends FlowTypeAccessInterface{

	public FlowTypeUserAccessInterface(FlowType flowType) {

		super(flowType);
	}

	@Override
	public boolean allowsAdminAccess() {

		return !flowType.isUseAccessFilter();
	}

	@Override
	public boolean allowsUserAccess() {

		return !flowType.isUseAccessFilter();
	}

	@Override
	public boolean allowsAnonymousAccess() {

		return !flowType.isUseAccessFilter() || flowType.allowsAnonymousAccess();
	}

	@Override
	public Collection<Integer> getAllowedGroupIDs() {

		return flowType.getAllowedGroupIDs();
	}

	@Override
	public Collection<Integer> getAllowedUserIDs() {

		return flowType.getAllowedUserIDs();
	}
}
