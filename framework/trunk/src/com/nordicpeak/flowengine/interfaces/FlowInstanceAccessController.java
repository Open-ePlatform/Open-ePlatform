package com.nordicpeak.flowengine.interfaces;

import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.hierarchy.core.exceptions.AccessDeniedException;

import com.nordicpeak.flowengine.beans.Flow;


public interface FlowInstanceAccessController {

	void checkNewFlowInstanceAccess(Flow flow, User user) throws AccessDeniedException;

	void checkFlowInstanceAccess(ImmutableFlowInstance flowInstance, User user) throws AccessDeniedException;

	boolean isMutable(ImmutableFlowInstance flowInstance, User user);
}
