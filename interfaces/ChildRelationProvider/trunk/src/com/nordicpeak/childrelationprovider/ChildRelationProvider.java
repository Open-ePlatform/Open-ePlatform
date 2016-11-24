package com.nordicpeak.childrelationprovider;

import java.util.Map;


public interface ChildRelationProvider {
	
	public Map<String, Child> getChildrenWithGuardians(String citizenIdentifier);

}
