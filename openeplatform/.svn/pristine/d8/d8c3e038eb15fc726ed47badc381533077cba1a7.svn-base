package com.nordicpeak.flowengine.interfaces;

import java.sql.SQLException;
import java.util.List;

import com.nordicpeak.flowengine.beans.APISourceTag;

public interface APISourceHandler {

	public boolean addAPISourceProvider(APISourceProvider provider);

	public boolean removeAPISourceProvider(APISourceProvider provider);

	public String getFullAlias();
	
	public Integer getSectionID();
	
	public List<APISourceTag> getAvailableTags() throws SQLException;
	
	public <T extends APISourceProvider> List<APISourceTag> getSelectedTags(Class<T> sourceProviderClass, APISource apiSource) throws Exception;
	
	public <T extends APISourceProvider> void updateSelectedTags(List<Integer> tagIDs, Class<T> sourceProviderClass, APISource apiSource) throws SQLException;

	public List<? extends APISourceDescriptor> getAPIAccountSources(APISourceAccountDescriptor descriptor) throws SQLException;
	
	public boolean addExtensionProvider(APISourceHandlerExtensionProvider provider);
	
	public boolean removeExtensionProvider(APISourceHandlerExtensionProvider provider);
}
