package com.nordicpeak.flowengine.interfaces;

import java.sql.SQLException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import se.unlogic.standardutils.dao.TransactionHandler;
import se.unlogic.standardutils.validation.ValidationException;
import se.unlogic.webutils.http.SimpleRequest;

public interface APISourceAccountHandler {

	public boolean addAuthenticationProvider(APISourceAuthenticationProvider provider);

	public boolean removeAuthenticationProvider(APISourceAuthenticationProvider provider);
	
	public List<? extends APISourceAccountDescriptor> getAccountDescriptors() throws SQLException;
	
	public APISourceAccountDescriptor getAccountDescriptor(Integer accountID, boolean getAPISources) throws SQLException;

	public APISourceAccountDescriptor populateDescriptor(APISourceAccountDescriptor descriptor, HttpServletRequest req) throws ValidationException, SQLException;
	
	public void updateDescriptor(APISourceAccountDescriptor descriptor, TransactionHandler transactionHandler) throws SQLException;
	
	public void deleteDescriptor(APISourceAccountDescriptor descriptor, TransactionHandler transactionHandler) throws SQLException;
	
	public void prepareRequest(APISourceAccountDescriptor descriptor, SimpleRequest request) throws Exception;
	
	public String getFullAlias();
	
	public void setAPISourceHandler(APISourceHandler apiSourceHandler);
	
	public void apiSourceProvidersCached(List<APISourceProvider> providers);
	
	public void apiSourceProviderCached(APISourceProvider provider);
	
}
