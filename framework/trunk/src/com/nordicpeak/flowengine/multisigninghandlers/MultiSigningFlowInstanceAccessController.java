package com.nordicpeak.flowengine.multisigninghandlers;

import com.nordicpeak.flowengine.beans.Flow;
import com.nordicpeak.flowengine.enums.ContentType;
import com.nordicpeak.flowengine.interfaces.FlowInstanceAccessController;
import com.nordicpeak.flowengine.interfaces.ImmutableFlowInstance;
import com.nordicpeak.flowengine.managers.ImmutableFlowInstanceManager;

import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.hierarchy.core.exceptions.AccessDeniedException;
import se.unlogic.openhierarchy.foregroundmodules.siteprofile.interfaces.SiteProfile;

public class MultiSigningFlowInstanceAccessController implements FlowInstanceAccessController {
	
	private final MultiSigningHandlerModule multiSigningHandlerModule;
	private final ImmutableFlowInstanceManager instanceManager;
	
	public MultiSigningFlowInstanceAccessController(MultiSigningHandlerModule multiSigningHandlerModule, ImmutableFlowInstanceManager instanceManager) {
		
		super();
		this.multiSigningHandlerModule = multiSigningHandlerModule;
		this.instanceManager = instanceManager;
	}
	
	@Override
	public void checkNewFlowInstanceAccess(Flow flow, User user, SiteProfile profile) throws AccessDeniedException {
		
		throw new AccessDeniedException("Cannot create new flow instances here");
	}
	
	@Override
	public void checkFlowInstanceAccess(ImmutableFlowInstance flowInstance, User user) throws AccessDeniedException {
		
		try {
			if (flowInstance.getStatus().getContentType() == ContentType.WAITING_FOR_MULTISIGN) {
				
				if (multiSigningHandlerModule.getMatchingSigningParty(user, instanceManager) != null) {
					return;
				}
				
			} else {
				
				if (multiSigningHandlerModule.getValidSignatureForCurrentSigningChain(instanceManager, user) != null) {
					return;
				}
			}
		} catch (Exception e) {
			throw new RuntimeException("Error checking multi sign access to " + flowInstance, e);
		}
		
		throw new AccessDeniedException("User not a signing party for " + flowInstance);
	}
	
	@Override
	public boolean isMutable(ImmutableFlowInstance flowInstance, User user) {
		
		return false;
	}
	
}
