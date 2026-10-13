package com.nordicpeak.flowengine.interfaces;

import java.sql.SQLException;

import se.unlogic.standardutils.dao.TransactionHandler;

public interface APISourceBasicAuthenticationProvider extends APISourceAuthenticationProvider {

	public APISourceAccount getAccount(String username, String passsword, TransactionHandler transactionHandler) throws Exception;

	public APISourceAccount createAccount(String username, String password, APISourceAccountDescriptor sourceAccountDescriptor, TransactionHandler transactionHandler) throws Exception;

	public boolean usernameExists(String username, TransactionHandler transactionHandler) throws SQLException;

}
