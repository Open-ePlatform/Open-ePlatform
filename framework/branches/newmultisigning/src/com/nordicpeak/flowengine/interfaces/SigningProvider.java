package com.nordicpeak.flowengine.interfaces;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.hierarchy.core.interfaces.ViewFragment;

import com.nordicpeak.flowengine.beans.SigningParty;
import com.nordicpeak.flowengine.managers.ImmutableFlowInstanceManager;
import com.nordicpeak.flowengine.managers.MutableFlowInstanceManager;


public interface SigningProvider {

	public ViewFragment sign(HttpServletRequest req, HttpServletResponse res, User user, MutableFlowInstanceManager instanceManager, SigningCallback signingCallback, boolean modifiedSinceLastSignRequest) throws Exception;

	public ViewFragment sign(HttpServletRequest req, HttpServletResponse res, User user, ImmutableFlowInstanceManager instanceManager, MultiSigningCallback signingCallback, SigningParty signingParty) throws Exception;
	
}
