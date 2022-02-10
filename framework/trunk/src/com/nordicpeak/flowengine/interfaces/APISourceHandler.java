package com.nordicpeak.flowengine.interfaces;

public interface APISourceHandler {

	public boolean addAPISourceProvider(APISourceProvider provider);

	public boolean removeAPISourceProvider(APISourceProvider provider);

	public String getFullAlias();
}
