package com.nordicpeak.flowengine.utils;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

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
					
					signingParties.addAll(multiSignQueryinstance.getSigningParties());
				}
				
			}
			
			if(!signingParties.isEmpty()){
				
				return signingParties;
			}
		}
		
		return null;
	}
}
