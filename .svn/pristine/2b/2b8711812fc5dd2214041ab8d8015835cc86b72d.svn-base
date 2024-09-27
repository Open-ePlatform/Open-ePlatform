package com.nordicpeak.childrelationprovider;

import com.nordicpeak.childrelationprovider.exceptions.ChildRelationProviderException;

public interface ChildRelationProvider {
	
	/**
	 * Never returns null. If no children are found an empty ChildrenResponse is returned.
	 */
	public ChildrenResponse getChildren(String citizenIdentifierString, boolean withGuardians, boolean requireCitizenIDsForGuardians) throws ChildRelationProviderException;
	
}
