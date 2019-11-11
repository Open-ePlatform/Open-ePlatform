package com.nordicpeak.flowengine.interfaces;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.standardutils.dao.TransactionHandler;

import com.nordicpeak.flowengine.beans.QueryTypeDescriptor;

public interface QueryProvider {

	public QueryTypeDescriptor getQueryType();
	
	public String getQueryDescription();

	public Query createQuery(MutableQueryDescriptor descriptor, TransactionHandler transactionHandler) throws Throwable;
	
	public Query importQuery(MutableQueryDescriptor descriptor, TransactionHandler transactionHandler, Map<Integer, ImmutableStatus> statusConversionMap) throws Throwable;

	public Query getQuery(MutableQueryDescriptor descriptor, boolean extraData) throws Throwable;

	public Query getQuery(MutableQueryDescriptor descriptor, TransactionHandler transactionHandler) throws Throwable;

	public QueryInstance getQueryInstance(MutableQueryInstanceDescriptor descriptor, String instanceManagerID, HttpServletRequest req, User user, User poster, InstanceMetadata instanceMetadata) throws Throwable;

	public ImmutableQueryInstance getImmutableQueryInstance(MutableQueryInstanceDescriptor descriptor, HttpServletRequest req, InstanceMetadata instanceMetadata) throws Throwable;

	public boolean deleteQuery(ImmutableQueryDescriptor descriptor, TransactionHandler transactionHandler) throws Throwable;

	public boolean deleteQueryInstance(ImmutableQueryInstanceDescriptor descriptor, TransactionHandler transactionHandler) throws Throwable;

	public void copyQuery(MutableQueryDescriptor sourceQueryDescriptor, MutableQueryDescriptor copyQueryDescriptor, TransactionHandler transactionHandler, Map<Integer, ImmutableStatus> statusConversionMap) throws Exception;
}
