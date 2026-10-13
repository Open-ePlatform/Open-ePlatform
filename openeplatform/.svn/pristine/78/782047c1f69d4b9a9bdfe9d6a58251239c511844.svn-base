package com.nordicpeak.flowengine.interfaces;

import se.unlogic.standardutils.dao.TransactionHandler;

import com.nordicpeak.flowengine.beans.APISourceBasicAuthCredentials;

public interface APISourceBasicAuthMigrationProvider extends APISourceProvider {

	public String getProviderID();
	
	public APISourceBasicAuthCredentials getBasicAuthCredentials(APISource apiSource);
	
	public void migrateAccount(APISource apiSource, APISourceAccount apiSourceAccount, TransactionHandler transactionHandler) throws Exception;
	
}
