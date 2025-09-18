package com.nordicpeak.listuserprovider;

import java.io.Serializable;

public interface ListUser extends Serializable {

	public String getFirstname();

	public String getLastname();

	public String getProviderUserID();

	public String getCitizenIdentifier();

	public String getEmail();

	public String getPhone();

	public String getUsername();
	
	public Boolean isEnabled();
}
