package com.nordicpeak.flowengine.utils;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import se.unlogic.standardutils.collections.CollectionUtils;

import com.nordicpeak.flowengine.beans.SigningParty;
import com.nordicpeak.flowengine.enums.QueryState;
import com.nordicpeak.flowengine.interfaces.MultiSigningQuery;
import com.nordicpeak.flowengine.managers.FlowInstanceManager;


public class MultiSignUtils {

	public static Set<SigningParty> getSigningParties(FlowInstanceManager instanceManager){
		
		List<MultiSigningQuery> multiSigningQueries = instanceManager.getQueries(MultiSigningQuery.class);
		
		if(multiSigningQueries != null) {
			
			LinkedHashSet<SigningParty> signingParties = new LinkedHashSet<SigningParty>();
			
			for(MultiSigningQuery multiSigningQuery : multiSigningQueries) {
				
				if(multiSigningQuery.getQueryInstanceDescriptor().getQueryState() != QueryState.HIDDEN && !CollectionUtils.isEmpty(multiSigningQuery.getSigningParties())) {
					
					signingParties.addAll(multiSigningQuery.getSigningParties());
				}
				
			}
			
			if(!signingParties.isEmpty()){
				
				return signingParties;
			}
		}
		
		return null;
	}
}
