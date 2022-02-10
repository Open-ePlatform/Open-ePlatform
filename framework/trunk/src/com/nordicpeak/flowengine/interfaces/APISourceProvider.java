package com.nordicpeak.flowengine.interfaces;

import java.util.List;

public interface APISourceProvider {

	public List<? extends APISource> getAPISources() throws Exception;

	public String getShortTypeName();
	
	public String getFullTypeName();

	public String getAddURL();
	
	public String getBaseShowURL();
	
	public String getBaseUpdateURL();

	public String getBaseDeleteURL();

	public boolean isInUse(APISource apiSource) throws Exception;
}
