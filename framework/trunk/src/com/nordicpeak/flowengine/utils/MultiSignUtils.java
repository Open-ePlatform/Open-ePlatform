package com.nordicpeak.flowengine.utils;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.standardutils.collections.CollectionUtils;

import com.nordicpeak.flowengine.beans.SigningParty;
import com.nordicpeak.flowengine.enums.QueryState;
import com.nordicpeak.flowengine.interfaces.MultiSignQueryinstance;
import com.nordicpeak.flowengine.managers.FlowInstanceManager;

public class MultiSignUtils {
	
	public static Set<SigningParty> getSigningParties(FlowInstanceManager instanceManager) {
		
		List<MultiSignQueryinstance> multiSignQueryinstances = instanceManager.getQueries(MultiSignQueryinstance.class);
		
		if (multiSignQueryinstances != null) {
			
			LinkedHashSet<SigningParty> signingParties = new LinkedHashSet<SigningParty>();
			
			for (MultiSignQueryinstance multiSignQueryinstance : multiSignQueryinstances) {
				
				if (multiSignQueryinstance.getQueryInstanceDescriptor().getQueryState() != QueryState.HIDDEN && multiSignQueryinstance.getQueryInstanceDescriptor().isPopulated()) {
					
					List<? extends SigningParty> querySigningParties = multiSignQueryinstance.getSigningParties();
					
					if(!CollectionUtils.isEmpty(querySigningParties)) {
					
						for (SigningParty signingParty : multiSignQueryinstance.getSigningParties()) {
							
							if (signingParty != null && !isSigningInitiator(signingParty, instanceManager)) {
								
								signingParties.add(signingParty);
							}
						}
					}
				}
			}
			
			if (!signingParties.isEmpty()) {
				
				return signingParties;
			}
		}
		
		return null;
	}
	
	public static boolean requiresMultiSigning(FlowInstanceManager instanceManager) {
		
		List<MultiSignQueryinstance> multiSignQueryinstances = instanceManager.getQueries(MultiSignQueryinstance.class);
		
		if (multiSignQueryinstances != null) {
			
			for (MultiSignQueryinstance multiSignQueryinstance : multiSignQueryinstances) {
				
				if (multiSignQueryinstance.getQueryInstanceDescriptor().getQueryState() != QueryState.HIDDEN && multiSignQueryinstance.getQueryInstanceDescriptor().isPopulated()) {
					
					List<? extends SigningParty> querySigningParties = multiSignQueryinstance.getSigningParties();
					
					if(!CollectionUtils.isEmpty(querySigningParties)) {
						
						for (SigningParty signingParty : querySigningParties) {
							
							if (!isSigningInitiator(signingParty, instanceManager)) {
								
								return true;
							}
						}
					}
				}
			}
		}
		
		return false;
	}
	
	private static boolean isSigningInitiator(SigningParty signingParty, FlowInstanceManager instanceManager) {
		
		if (!signingParty.isUnsecure() && signingParty.getSocialSecurityNumber().equals(getCurrentInstanceUserCitizenIdentifier(instanceManager))) {
			
			return true;
		}
		
		return false;
	}
	
	private static String getCurrentInstanceUserCitizenIdentifier(FlowInstanceManager instanceManager) {
		
		User instanceUser = instanceManager.getFlowInstance().getEditor();
		
		if (instanceUser == null) {
			
			instanceUser = instanceManager.getFlowInstance().getPoster();
		}
		
		return CitizenIdentifierUtils.getUserOrManagerCitizenIdentifier(instanceUser);
	}
}
