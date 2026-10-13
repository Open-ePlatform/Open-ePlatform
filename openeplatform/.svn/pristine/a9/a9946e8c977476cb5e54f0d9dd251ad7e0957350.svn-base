package com.nordicpeak.flowengine.beans;

import java.util.Collection;


public class FlowTypeAdminAccessInterface extends FlowTypeAccessInterface {

	public FlowTypeAdminAccessInterface(FlowType flowType) {

		super(flowType);
	}

	@Override
	public boolean allowsAdminAccess() {

		return false;
	}

	@Override
	public boolean allowsUserAccess() {

		return false;
	}

	@Override
	public boolean allowsAnonymousAccess() {

		return false;
	}

	@Override
	public Collection<Integer> getAllowedGroupIDs() {

		return flowType.getAllowedAdminGroupIDs();
	}

	@Override
	public Collection<Integer> getAllowedUserIDs() {

		return flowType.getAllowedAdminUserIDs();
	}
}
