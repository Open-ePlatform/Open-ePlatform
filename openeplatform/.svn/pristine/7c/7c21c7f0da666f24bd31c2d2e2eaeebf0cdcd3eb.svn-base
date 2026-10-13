package com.nordicpeak.flowengine.interfaces;

import se.unlogic.standardutils.dao.TransactionHandler;
import se.unlogic.webutils.http.SimpleRequest;

public interface APISourceAuthenticationProvider {

	public String getProviderID();
	
	public String getTypeName();

	public APISourceAccount getAccount(APISourceAccountDescriptor accountDescriptor) throws Exception;
	
	public APISourceAccount createAccount(APISourceAccountDescriptor descriptor, TransactionHandler transactionHandler) throws Exception;
	
	public void deleteAccount(APISourceAccountDescriptor descriptor, TransactionHandler transactionHandler) throws Exception;
	
	public void prepareRequest(APISourceAccountDescriptor descriptor, SimpleRequest request) throws Exception;
	
	public String getShowURL(APISourceAccountDescriptor descriptor);
	
	public String getConfigURL(APISourceAccountDescriptor descriptor);

}
