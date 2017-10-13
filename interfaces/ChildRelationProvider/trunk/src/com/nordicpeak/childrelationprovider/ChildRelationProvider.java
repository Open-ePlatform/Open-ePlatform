package com.nordicpeak.childrelationprovider;

import com.nordicpeak.childrelationprovider.exceptions.ChildRelationProviderException;

public interface ChildRelationProvider {
	
	// Returns empty map if citizen has no visible children
	public ChildrenResponse getChildrenWithGuardians(String citizenIdentifier) throws ChildRelationProviderException;
	
}
