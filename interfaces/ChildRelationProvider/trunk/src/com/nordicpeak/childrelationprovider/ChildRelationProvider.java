package com.nordicpeak.childrelationprovider;

import java.util.Map;

import com.nordicpeak.childrelationprovider.exceptions.ChildRelationProviderException;

public interface ChildRelationProvider {
	
	// Returns empty map if citizen has no visible children
	public Map<String, Child> getChildrenWithGuardians(String citizenIdentifier) throws ChildRelationProviderException;
	
}
