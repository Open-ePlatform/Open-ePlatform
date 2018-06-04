package com.nordicpeak.flowengine.interfaces;

public interface SigningSessionParty {
	
	public String getIdentifier();
	
	public String getFirstname();
	
	public String getLastname();
	
	public String getEmail();
	
	public String getMobilePhone();
	
	public boolean isUnsecure();
	
	public boolean isAddAsOwner();
}
