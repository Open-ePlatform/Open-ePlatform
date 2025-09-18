package com.nordicpeak.flowengine.interfaces;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.hierarchy.core.interfaces.ForegroundModuleResponse;
import se.unlogic.webutils.http.URIParser;

import com.nordicpeak.flowengine.beans.APISourceTag;

public interface APISourceProvider {

	public List<? extends APISource> getAPISources() throws Exception;

	public String getShortTypeName();
	
	public String getFullTypeName();

	public String getAddURL();
	
	public String getBaseShowURL();
	
	public String getBaseUpdateURL();

	public String getBaseDeleteURL();

	public List<APISourceTag> getSelectedTags(APISource apiSource) throws Exception;

	public <T extends APISourceProvider> void updateTags(List<Integer> tagIDs, Class<T> sourceProviderClass, APISource apiSource) throws SQLException;
	
	public ForegroundModuleResponse getAvailableTags(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws SQLException, IOException;

	public boolean isInUse(APISource apiSource) throws Exception;
	
	public List<? extends APISource> getAPIAccountSources(APISourceAccountDescriptor descriptor) throws SQLException;
	
}
