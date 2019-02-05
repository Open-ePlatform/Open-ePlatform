package com.nordicpeak.flowengine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.hierarchy.core.interfaces.ForegroundModuleResponse;
import se.unlogic.hierarchy.core.interfaces.SectionInterface;
import se.unlogic.hierarchy.core.interfaces.modules.descriptors.ForegroundModuleDescriptor;
import se.unlogic.hierarchy.foregroundmodules.AnnotatedForegroundModule;
import se.unlogic.standardutils.collections.NameComparator;
import se.unlogic.standardutils.dao.TransactionHandler;
import se.unlogic.webutils.http.URIParser;

import com.nordicpeak.flowengine.beans.QueryTypeDescriptor;
import com.nordicpeak.flowengine.exceptions.queryprovider.QueryInstanceNotFoundInQueryProviderException;
import com.nordicpeak.flowengine.exceptions.queryprovider.QueryNotFoundInQueryProviderException;
import com.nordicpeak.flowengine.exceptions.queryprovider.QueryProviderErrorException;
import com.nordicpeak.flowengine.exceptions.queryprovider.QueryProviderNotFoundException;
import com.nordicpeak.flowengine.interfaces.ImmutableQueryDescriptor;
import com.nordicpeak.flowengine.interfaces.ImmutableQueryInstance;
import com.nordicpeak.flowengine.interfaces.ImmutableQueryInstanceDescriptor;
import com.nordicpeak.flowengine.interfaces.InstanceMetadata;
import com.nordicpeak.flowengine.interfaces.MutableQueryDescriptor;
import com.nordicpeak.flowengine.interfaces.MutableQueryInstanceDescriptor;
import com.nordicpeak.flowengine.interfaces.Query;
import com.nordicpeak.flowengine.interfaces.QueryHandler;
import com.nordicpeak.flowengine.interfaces.QueryInstance;
import com.nordicpeak.flowengine.interfaces.QueryProvider;

public class QueryHandlerModule extends AnnotatedForegroundModule implements QueryHandler {

	private ConcurrentHashMap<String, QueryProvider> queryProviderMap = new ConcurrentHashMap<String, QueryProvider>();

	@Override
	public void init(ForegroundModuleDescriptor moduleDescriptor, SectionInterface sectionInterface, DataSource dataSource) throws Exception {

		super.init(moduleDescriptor, sectionInterface, dataSource);

		if(!systemInterface.getInstanceHandler().addInstance(QueryHandler.class, this)){

			throw new RuntimeException("Unable to register module in global instance handler using key " + QueryHandler.class.getSimpleName() + ", another instance is already registered using this key.");
		}
	}

	@Override
	public void unload() throws Exception {

		systemInterface.getInstanceHandler().removeInstance(QueryHandler.class, this);

		queryProviderMap.clear();
		super.unload();
	}

	@Override
	public List<QueryTypeDescriptor> getQueryTypes(List<String> queryTypeIDs) {

		ArrayList<QueryTypeDescriptor> queryTypes = new ArrayList<QueryTypeDescriptor>(queryTypeIDs.size());

		for(String queryTypeID : queryTypeIDs){

			QueryProvider queryProvider = queryProviderMap.get(queryTypeID);

			if(queryProvider != null){

				queryTypes.add(queryProvider.getQueryType());
			}
		}

		if(!queryTypes.isEmpty()){
			
			Collections.sort(queryTypes, NameComparator.getInstance());
		}
		
		return queryTypes;
	}

	@Override
	public List<QueryTypeDescriptor> getAvailableQueryTypes() {

		ArrayList<QueryTypeDescriptor> queryTypes = new ArrayList<QueryTypeDescriptor>(queryProviderMap.size());

		for(QueryProvider queryProvider : queryProviderMap.values()){

			queryTypes.add(queryProvider.getQueryType());
		}

		if(!queryTypes.isEmpty()){
			
			Collections.sort(queryTypes, NameComparator.getInstance());
		}
		
		return queryTypes;
	}

	@Override
	public boolean addQueryProvider(QueryProvider queryProvider) {

		QueryTypeDescriptor queryType = queryProvider.getQueryType();

		boolean result = queryProviderMap.putIfAbsent(queryProvider.getQueryType().getQueryTypeID(), queryProvider) == null;

		if(result){
			log.info("Query provider for query type " + queryType + " added");
		}

		return result;
	}

	@Override
	public boolean removeQueryProvider(QueryTypeDescriptor queryType, QueryProvider instance) {

		boolean result = queryProviderMap.remove(queryType.getQueryTypeID(), instance);

		if(result){
			log.info("Query provider for query type " + queryType + " removed");
		}

		return result;
	}

	@Override
	public Query createQuery(MutableQueryDescriptor descriptor, TransactionHandler transactionHandler) throws QueryProviderNotFoundException, QueryProviderErrorException {

		QueryProvider queryProvider = queryProviderMap.get(descriptor.getQueryTypeID());

		if(queryProvider == null){

			throw new QueryProviderNotFoundException(descriptor.getQueryTypeID());
		}

		try{
			return queryProvider.createQuery(descriptor, transactionHandler);
		}catch(Throwable t){
			throw new QueryProviderErrorException("Exception thrown by query provider for query type " + queryProvider.getQueryType() + " while creating query " + descriptor, t, queryProvider);
		}
	}

	@Override
	public Query getQuery(MutableQueryDescriptor descriptor, boolean extraData) throws QueryProviderNotFoundException, QueryNotFoundInQueryProviderException, QueryProviderErrorException {

		QueryProvider queryProvider = queryProviderMap.get(descriptor.getQueryTypeID());

		if(queryProvider != null){

			Query query;
			try{
				query = queryProvider.getQuery(descriptor, extraData);

				if(query != null){

					return query;
				}
			}catch(Throwable t){

				throw new QueryProviderErrorException("Exception thrown by query provider for query type " + queryProvider.getQueryType() + " while getting query " + descriptor, t, queryProvider);
			}

			throw new QueryNotFoundInQueryProviderException(queryProvider, descriptor);
		}

		throw new QueryProviderNotFoundException(descriptor.getQueryTypeID());
	}

	@Override
	public Query getQuery(MutableQueryDescriptor descriptor, TransactionHandler transactionHandler) throws QueryProviderNotFoundException, QueryNotFoundInQueryProviderException, QueryProviderErrorException {

		QueryProvider queryProvider = queryProviderMap.get(descriptor.getQueryTypeID());

		if(queryProvider != null){

			Query query;
			try{
				query = queryProvider.getQuery(descriptor, transactionHandler);

				if(query != null){

					return query;
				}
			}catch(Throwable t){

				throw new QueryProviderErrorException("Exception thrown by query provider for query type " + queryProvider.getQueryType() + " while getting query " + descriptor, t, queryProvider);
			}

			throw new QueryNotFoundInQueryProviderException(queryProvider, descriptor);
		}

		throw new QueryProviderNotFoundException(descriptor.getQueryTypeID());
	}

	@Override
	public QueryInstance getQueryInstance(MutableQueryInstanceDescriptor descriptor, String instanceManagerID, HttpServletRequest req, User user, User poster, InstanceMetadata instanceMetadata) throws QueryProviderNotFoundException, QueryProviderErrorException, QueryInstanceNotFoundInQueryProviderException {

		QueryProvider queryProvider = queryProviderMap.get(descriptor.getQueryDescriptor().getQueryTypeID());

		if(queryProvider != null){

			try{

				QueryInstance queryinstance = queryProvider.getQueryInstance(descriptor, instanceManagerID, req, user, poster, instanceMetadata);

				if(queryinstance != null){

					return queryinstance;
				}

			}catch(Throwable t){

				throw new QueryProviderErrorException("Exception thrown by query provider for query type " + queryProvider.getQueryType() + " while getting instance of query " + descriptor.getQueryDescriptor(), t, queryProvider);
			}

			throw new QueryInstanceNotFoundInQueryProviderException(queryProvider, descriptor);
		}

		throw new QueryProviderNotFoundException(descriptor.getQueryDescriptor().getQueryTypeID());
	}

	@Override
	public ImmutableQueryInstance getImmutableQueryInstance(MutableQueryInstanceDescriptor descriptor, HttpServletRequest req, InstanceMetadata instanceMetadata) throws QueryProviderNotFoundException, QueryProviderErrorException, QueryInstanceNotFoundInQueryProviderException {

		if(descriptor.getQueryInstanceID() == null){

			throw new NullPointerException("query instance ID cannot be null when requesting immutable query instances");
		}

		QueryProvider queryProvider = queryProviderMap.get(descriptor.getQueryDescriptor().getQueryTypeID());

		if(queryProvider != null){

			try{

				ImmutableQueryInstance queryinstance = queryProvider.getImmutableQueryInstance(descriptor, req, instanceMetadata);

				if(queryinstance != null){

					return queryinstance;
				}

			}catch(Throwable t){

				throw new QueryProviderErrorException("Exception thrown by query provider for query type " + queryProvider.getQueryType() + " while getting immutable instance of query " + descriptor.getQueryDescriptor(), t, queryProvider);
			}

			throw new QueryInstanceNotFoundInQueryProviderException(queryProvider, descriptor);
		}

		throw new QueryProviderNotFoundException(descriptor.getQueryDescriptor().getQueryTypeID());
	}

	@Override
	@SuppressWarnings("unchecked")
	public <X extends QueryProvider> X getQueryProvider(String queryTypeID, Class<X> clazz) {

		QueryProvider queryProvider = this.queryProviderMap.get(queryTypeID);

		if(queryProvider == null || !(clazz.isAssignableFrom(queryProvider.getClass()))){
			return null;
		}

		return (X)queryProvider;
	}

	@Override
	public QueryProvider getQueryProvider(String queryTypeID) {

		return this.queryProviderMap.get(queryTypeID);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <X extends QueryProvider> List<X> getAssignableQueryProviders(Class<X> clazz) {
		
		List<X> values = new ArrayList<X>();
		
		for (QueryProvider queryProvider : queryProviderMap.values()) {
			
			if (clazz.isAssignableFrom(queryProvider.getClass())) {
				
				values.add((X) queryProvider);
			}
		}
		
		if (values.isEmpty()) {
			return null;
		}
		
		return values;
	}

	@Override
	public ForegroundModuleResponse defaultMethod(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Exception, Throwable {

		//List currently available query types

		return null;
	}

	@Override
	public void deleteQuery(ImmutableQueryDescriptor descriptor, TransactionHandler transactionHandler) throws QueryProviderErrorException, QueryProviderNotFoundException, QueryNotFoundInQueryProviderException {

		QueryProvider queryProvider = queryProviderMap.get(descriptor.getQueryTypeID());

		if(queryProvider != null){

			boolean deleted;

			try{
				deleted = queryProvider.deleteQuery(descriptor, transactionHandler);

			}catch(Throwable t){

				throw new QueryProviderErrorException("Exception thrown by query provider for query type " + queryProvider.getQueryType() + " while getting query " + descriptor, t, queryProvider);
			}

			if(!deleted){

				throw new QueryNotFoundInQueryProviderException(queryProvider, descriptor);
			}

			return;
		}

		throw new QueryProviderNotFoundException(descriptor.getQueryTypeID());
	}

	@Override
	public void deleteQueryInstance(ImmutableQueryInstanceDescriptor descriptor, TransactionHandler transactionHandler) throws QueryProviderErrorException, QueryProviderNotFoundException, QueryInstanceNotFoundInQueryProviderException {

		QueryProvider queryProvider = queryProviderMap.get(descriptor.getQueryDescriptor().getQueryTypeID());

		if(queryProvider != null){

			boolean deleted;

			try{
				deleted = queryProvider.deleteQueryInstance(descriptor, transactionHandler);

			}catch(Throwable t){

				throw new QueryProviderErrorException("Exception thrown by query provider for query type " + queryProvider.getQueryType() + " while getting query instance " + descriptor, t, queryProvider);
			}

			if(!deleted){

				throw new QueryInstanceNotFoundInQueryProviderException(queryProvider, descriptor);
			}

			return;
		}

		throw new QueryProviderNotFoundException(descriptor.getQueryDescriptor().getQueryTypeID());
	}

	@Override
	public void copyQuery(MutableQueryDescriptor sourceQueryDescriptor, MutableQueryDescriptor copyQueryDescriptor, TransactionHandler transactionHandler) throws QueryProviderErrorException, QueryProviderNotFoundException {

		QueryProvider queryProvider = queryProviderMap.get(sourceQueryDescriptor.getQueryTypeID());

		if(queryProvider != null){

			try{

				queryProvider.copyQuery(sourceQueryDescriptor, copyQueryDescriptor, transactionHandler);

				return;

			}catch(Throwable t){

				throw new QueryProviderErrorException("Exception thrown by query provider for query type " + queryProvider.getQueryType() + " while creating copy of query " + sourceQueryDescriptor, t, queryProvider);
			}
		}

		throw new QueryProviderNotFoundException(sourceQueryDescriptor.getQueryTypeID());
	}
	
	@Override
	public Query importQuery(MutableQueryDescriptor descriptor, TransactionHandler transactionHandler) throws QueryProviderNotFoundException, QueryProviderErrorException {

		QueryProvider queryProvider = queryProviderMap.get(descriptor.getQueryTypeID());

		if(queryProvider == null){

			throw new QueryProviderNotFoundException(descriptor.getQueryTypeID());
		}

		try{
			return queryProvider.importQuery(descriptor, transactionHandler);
		}catch(Throwable t){
			throw new QueryProviderErrorException("Exception thrown by query provider for query type " + queryProvider.getQueryType() + " while importing query " + descriptor, t, queryProvider);
		}
	}

	@Override
	public QueryTypeDescriptor getQueryType(String queryTypeID) {

		QueryProvider queryProvider = queryProviderMap.get(queryTypeID);
		
		if(queryProvider != null){
			
			return queryProvider.getQueryType();
		}
		
		return null;
	}

	
}
