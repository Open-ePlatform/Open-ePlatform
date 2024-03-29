package com.nordicpeak.flowengine.interfaces;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.standardutils.dao.TransactionHandler;

import com.nordicpeak.flowengine.beans.QueryTypeDescriptor;
import com.nordicpeak.flowengine.exceptions.queryprovider.QueryInstanceNotFoundInQueryProviderException;
import com.nordicpeak.flowengine.exceptions.queryprovider.QueryNotFoundInQueryProviderException;
import com.nordicpeak.flowengine.exceptions.queryprovider.QueryProviderErrorException;
import com.nordicpeak.flowengine.exceptions.queryprovider.QueryProviderNotFoundException;

public interface QueryHandler {

	public List<QueryTypeDescriptor> getAvailableQueryTypes();

	public List<QueryTypeDescriptor> getQueryTypes(List<String> queryTypeIDs);

	public boolean addQueryProvider(QueryProvider queryProvider);

	public boolean removeQueryProvider(QueryTypeDescriptor queryType, QueryProvider instance);

	public Query createQuery(MutableQueryDescriptor descriptor, TransactionHandler transactionHandler) throws QueryProviderNotFoundException, QueryProviderErrorException;
	
	public Query importQuery(MutableQueryDescriptor descriptor, TransactionHandler transactionHandler, Map<Integer, ImmutableStatus> statusConversionMap, QueryContentFilter contentFilter) throws QueryProviderNotFoundException, QueryProviderErrorException;

	public Query getQuery(MutableQueryDescriptor descriptor, boolean extraData) throws QueryProviderNotFoundException, QueryNotFoundInQueryProviderException, QueryProviderErrorException;

	public Query getQuery(MutableQueryDescriptor descriptor, TransactionHandler transactionHandler) throws QueryProviderNotFoundException, QueryNotFoundInQueryProviderException, QueryProviderErrorException;

	public QueryInstance getQueryInstance(MutableQueryInstanceDescriptor descriptor, String instanceManagerID, HttpServletRequest req, User user, User poster, InstanceMetadata instanceMetadata) throws QueryProviderNotFoundException, QueryProviderErrorException, QueryInstanceNotFoundInQueryProviderException;

	public ImmutableQueryInstance getImmutableQueryInstance(MutableQueryInstanceDescriptor descriptor, HttpServletRequest req, InstanceMetadata instanceMetadata) throws QueryProviderNotFoundException, QueryProviderErrorException, QueryInstanceNotFoundInQueryProviderException;

	public <X extends QueryProvider> X getQueryProvider(String queryTypeID, Class<X> clazz);
	
	public <X extends QueryProvider> List<X> getAssignableQueryProviders(Class<X> clazz);

	public QueryProvider getQueryProvider(String queryTypeID);

	public void deleteQuery(ImmutableQueryDescriptor descriptor, TransactionHandler transactionHandler) throws QueryProviderErrorException, QueryProviderNotFoundException, QueryNotFoundInQueryProviderException;

	public void deleteQueryInstance(ImmutableQueryInstanceDescriptor descriptor, TransactionHandler transactionHandler) throws QueryProviderErrorException, QueryProviderNotFoundException, QueryInstanceNotFoundInQueryProviderException;

	public void copyQuery(MutableQueryDescriptor sourceQueryDescriptor, MutableQueryDescriptor copyQueryDescriptor, TransactionHandler transactionHandler, Map<Integer, ImmutableStatus> statusConversionMap) throws QueryProviderErrorException, QueryProviderNotFoundException;

	public QueryTypeDescriptor getQueryType(String queryTypeID);
}
