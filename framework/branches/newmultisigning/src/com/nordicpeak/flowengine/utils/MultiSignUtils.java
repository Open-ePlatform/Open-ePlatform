package com.nordicpeak.flowengine.utils;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import com.nordicpeak.flowengine.beans.SigningParty;
import com.nordicpeak.flowengine.enums.QueryState;
import com.nordicpeak.flowengine.interfaces.MultiSignQueryinstance;
import com.nordicpeak.flowengine.managers.FlowInstanceManager;

import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.standardutils.collections.CollectionUtils;

public class MultiSignUtils {
	
	public static Set<SigningParty> getSigningParties(FlowInstanceManager instanceManager) {
		
		List<MultiSignQueryinstance> multiSignQueryinstances = instanceManager.getQueries(MultiSignQueryinstance.class);
		
		if (multiSignQueryinstances != null) {
			
			LinkedHashSet<SigningParty> signingParties = new LinkedHashSet<SigningParty>();
			
			for (MultiSignQueryinstance multiSignQueryinstance : multiSignQueryinstances) {
				
				if (multiSignQueryinstance.getQueryInstanceDescriptor().getQueryState() != QueryState.HIDDEN && !CollectionUtils.isEmpty(multiSignQueryinstance.getSigningParties())) {
					
					for (SigningParty signer : multiSignQueryinstance.getSigningParties()) {
						
						if (!isSigningInitiator(signer, instanceManager)) {
							
							signingParties.add(signer);
						}
					}
				}
			}
			
			//User poster = instanceManager.getFlowInstance().getPoster();
			
			//TODO: this code is trying to handle the scenario when multipart signer is editing the insteance and poster needs to be part of the multipart signing chain. This scenario is not handled in other places so this code does not solve the whole problem.
			//TODO: If this logic is implemented then it also needs to look at the editor field not just the poster field
			
			//Line below contains potential NPE
			//			if(!CitizenIdentifierUtils.getUserOrManagerCitizenIdentifier(poster).equals(getCurrentInstanceUserCitizenIdentifier(instanceManager))) {
			//
			//				SigningParty posterSigningParty = new SigningParty(poster.getFirstname(), poster.getLastname(), poster.getEmail(), null, CitizenIdentifierUtils.getUserOrManagerCitizenIdentifier(poster), false);
			//
			//				signingParties.add(posterSigningParty);
			//			}
			
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
				
				if (multiSignQueryinstance.getQueryInstanceDescriptor().getQueryState() != QueryState.HIDDEN && !CollectionUtils.isEmpty(multiSignQueryinstance.getSigningParties())) {
					
					for (SigningParty signer : multiSignQueryinstance.getSigningParties()) {
						
						if (!isSigningInitiator(signer, instanceManager)) {
							
							return true;
						}
					}
				}
			}
		}
		
		return false;
	}
	
	private static boolean isSigningInitiator(SigningParty signingParty, FlowInstanceManager instanceManager) {
		
		if (signingParty.getSocialSecurityNumber().equals(getCurrentInstanceUserCitizenIdentifier(instanceManager))) {
			
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
