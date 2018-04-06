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

	public static Set<SigningParty> getSigningParties(FlowInstanceManager instanceManager){
		
		List<MultiSignQueryinstance> multiSignQueryinstances = instanceManager.getQueries(MultiSignQueryinstance.class);
		
		if(multiSignQueryinstances != null) {
			
			LinkedHashSet<SigningParty> signingParties = new LinkedHashSet<SigningParty>();
			
			for(MultiSignQueryinstance multiSignQueryinstance : multiSignQueryinstances) {
				
				if(multiSignQueryinstance.getQueryInstanceDescriptor().getQueryState() != QueryState.HIDDEN && !CollectionUtils.isEmpty(multiSignQueryinstance.getSigningParties())) {
					
					for(SigningParty signer : multiSignQueryinstance.getSigningParties()) {
						
						if(!isSigningInitiator(signer, instanceManager)) {
							
							signingParties.add(signer);
						}
					}
				}
			}
			
			User poster = instanceManager.getFlowInstance().getPoster();
			
			if(!CitizenIdentifierUtils.getUserOrManagerCitizenIdentifier(poster).equals(getCurrentInstanceUserCitizenIdentifier(instanceManager))) {
				
				SigningParty posterSigningParty = new SigningParty(poster.getFirstname(), poster.getLastname(), poster.getEmail(), null, CitizenIdentifierUtils.getUserOrManagerCitizenIdentifier(poster), false);
				
				signingParties.add(posterSigningParty);
			}
			
			if(!signingParties.isEmpty()){
				
				return signingParties;
			}
		}
		
		return null;
	}
	
	public static boolean requiresMultiSigning(FlowInstanceManager instanceManager) {

		List<MultiSignQueryinstance> multiSignQueryinstances = instanceManager.getQueries(MultiSignQueryinstance.class);

		if (multiSignQueryinstances != null) {

			String instanceUserSocialSecurityNumber = getCurrentInstanceUserCitizenIdentifier(instanceManager);
			
			for (MultiSignQueryinstance multiSignQueryinstance : multiSignQueryinstances) {

				if (multiSignQueryinstance.getQueryInstanceDescriptor().getQueryState() != QueryState.HIDDEN && !CollectionUtils.isEmpty(multiSignQueryinstance.getSigningParties())) {

					for(SigningParty signer : multiSignQueryinstance.getSigningParties()) {
						
						if(!isSigningInitiator(signer, instanceManager)) {

							return true;
							
						} else if(signer.getSocialSecurityNumber().equals(instanceUserSocialSecurityNumber)) {
							
							return true;
						}
					}
				}
			}
		}

		return false;
	}

	private static boolean isSigningInitiator(SigningParty signingParty, FlowInstanceManager instanceManager) {
	
		if(signingParty.getSocialSecurityNumber().equals(getCurrentInstanceUserCitizenIdentifier(instanceManager))) {
			
			return true;
		}
		
		return false;
	}
	
	private static String getCurrentInstanceUserCitizenIdentifier(FlowInstanceManager instanceManager) {
		
		User instanceUser = instanceManager.getFlowInstance().getEditor();
		
		if(instanceUser == null) {
			
			instanceUser = instanceManager.getFlowInstance().getPoster();
		}
		
		return CitizenIdentifierUtils.getUserOrManagerCitizenIdentifier(instanceUser);
	}
}
