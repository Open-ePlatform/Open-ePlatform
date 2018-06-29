package com.nordicpeak.flowengine.interfaces;

import javax.servlet.http.HttpServletRequest;

import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.standardutils.dao.TransactionHandler;

import com.nordicpeak.flowengine.beans.QueryTypeDescriptor;
import com.nordicpeak.flowengine.managers.MutableFlowInstanceManager;

public interface QueryProvider {

	/**
	 * @return The query type that this class provides
	 */
	public QueryTypeDescriptor getQueryType();

	public Query createQuery(MutableQueryDescriptor descriptor, TransactionHandler transactionHandler) throws Throwable;
	
	public Query importQuery(MutableQueryDescriptor descriptor, TransactionHandler transactionHandler) throws Throwable;

	public Query getQuery(MutableQueryDescriptor descriptor) throws Throwable;

	public Query getQuery(MutableQueryDescriptor descriptor, TransactionHandler transactionHandler) throws Throwable;

	/**
	 * @param descriptor
	 * @param instanceManagerID the ID of the {@link MutableFlowInstanceManager} handling this query instance.
	 * @param user
	 * @param poster
	 * @param instanceMetadata
	 * @return
	 * @throws Throwable
	 */
	public QueryInstance getQueryInstance(MutableQueryInstanceDescriptor descriptor, String instanceManagerID, HttpServletRequest req, User user, User poster, InstanceMetadata instanceMetadata) throws Throwable;

	/**
	 * Returns a immutable query instance based on a existing query instance.
	 * 
	 * @param descriptor
	 * @param instanceMetadata
	 * @return an immutable query instance
	 * @throws Throwable
	 */
	public ImmutableQueryInstance getImmutableQueryInstance(MutableQueryInstanceDescriptor descriptor, HttpServletRequest req, InstanceMetadata instanceMetadata) throws Throwable;

	public boolean deleteQuery(ImmutableQueryDescriptor descriptor, TransactionHandler transactionHandler) throws Throwable;

	public boolean deleteQueryInstance(ImmutableQueryInstanceDescriptor descriptor, TransactionHandler transactionHandler) throws Throwable;

	public void copyQuery(MutableQueryDescriptor sourceQueryDescriptor, MutableQueryDescriptor copyQueryDescriptor, TransactionHandler transactionHandler) throws Exception;
}
