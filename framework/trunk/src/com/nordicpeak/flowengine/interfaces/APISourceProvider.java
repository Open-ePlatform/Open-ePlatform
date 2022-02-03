package com.nordicpeak.flowengine.interfaces;

import java.util.List;

public interface APISourceProvider {

	public List<? extends APISource> getAPISources() throws Exception;

	public String getTypeDescription();

	public String getBaseShowURL();
	
	public String getBaseUpdateURL();

	public String getBaseDeleteURL();

	public boolean isInUse(APISource apiSource) throws Exception;
}
