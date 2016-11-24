package com.nordicpeak.flowengine;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.hierarchy.core.interfaces.ForegroundModuleDescriptor;
import se.unlogic.hierarchy.core.interfaces.ForegroundModuleResponse;
import se.unlogic.hierarchy.core.interfaces.SectionInterface;
import se.unlogic.hierarchy.foregroundmodules.AnnotatedForegroundModule;
import se.unlogic.standardutils.dao.TransactionHandler;
import se.unlogic.webutils.http.URIParser;

import com.nordicpeak.flowengine.beans.EvaluatorDescriptor;
import com.nordicpeak.flowengine.beans.EvaluatorTypeDescriptor;
import com.nordicpeak.flowengine.exceptions.evaluationprovider.EvaluationProviderErrorException;
import com.nordicpeak.flowengine.exceptions.evaluationprovider.EvaluationProviderNotFoundException;
import com.nordicpeak.flowengine.exceptions.evaluationprovider.EvaluatorNotFoundInEvaluationProviderException;
import com.nordicpeak.flowengine.interfaces.EvaluationHandler;
import com.nordicpeak.flowengine.interfaces.EvaluationProvider;
import com.nordicpeak.flowengine.interfaces.Evaluator;
import com.nordicpeak.flowengine.interfaces.ImmutableEvaluatorDescriptor;
import com.nordicpeak.flowengine.interfaces.ImmutableStatus;
import com.nordicpeak.flowengine.interfaces.InstanceMetadata;
import com.nordicpeak.flowengine.interfaces.MutableEvaluatorDescriptor;
import com.nordicpeak.flowengine.interfaces.Query;

public class EvaluationHandlerModule extends AnnotatedForegroundModule implements EvaluationHandler {

	private ConcurrentHashMap<String, EvaluationProvider> evaluationProviderMap = new ConcurrentHashMap<String, EvaluationProvider>();

	@Override
	public void init(ForegroundModuleDescriptor moduleDescriptor, SectionInterface sectionInterface, DataSource dataSource) throws Exception {

		super.init(moduleDescriptor, sectionInterface, dataSource);

		if(!systemInterface.getInstanceHandler().addInstance(EvaluationHandler.class, this)){

			throw new RuntimeException("Unable to register module in global instance handler using key " + EvaluationHandler.class.getSimpleName() + ", another instance is already registered using this key.");
		}
	}

	@Override
	public void unload() throws Exception {

		systemInterface.getInstanceHandler().removeInstance(EvaluationHandler.class, this);

		evaluationProviderMap.clear();
		
		super.unload();
	}

	@Override
	public List<EvaluatorTypeDescriptor> getAvailableEvaluatorTypes() {

		ArrayList<EvaluatorTypeDescriptor> evaluatorTypes = new ArrayList<EvaluatorTypeDescriptor>(evaluationProviderMap.size());

		for(EvaluationProvider evaluationProvider : evaluationProviderMap.values()){

			evaluatorTypes.add(evaluationProvider.getEvaluatorType());
		}

		return evaluatorTypes;
	}

	@Override
	public List<EvaluatorTypeDescriptor> getAvailableEvaluatorTypes(Class<? extends Query> queryClass) {

		ArrayList<EvaluatorTypeDescriptor> evaluatorTypes = new ArrayList<EvaluatorTypeDescriptor>(evaluationProviderMap.size());

		for(EvaluationProvider evaluationProvider : evaluationProviderMap.values()){

			if(evaluationProvider.supportsQueryType(queryClass)){

				evaluatorTypes.add(evaluationProvider.getEvaluatorType());
			}
		}

		return evaluatorTypes;
	}

	@Override
	public boolean addEvaluationProvider(EvaluationProvider evaluationProvider) {

		EvaluatorTypeDescriptor evaluatorType = evaluationProvider.getEvaluatorType();

		boolean result = evaluationProviderMap.putIfAbsent(evaluationProvider.getEvaluatorType().getEvaluatorTypeID(), evaluationProvider) == null;

		if(result){
			log.info("Evaluator provider for evaluator type " + evaluatorType + " added");
		}

		return result;
	}

	@Override
	public boolean removeEvaluationProvider(EvaluatorTypeDescriptor evaluatorType) {

		boolean result = evaluationProviderMap.remove(evaluatorType.getEvaluatorTypeID()) != null;

		if(result){
			log.info("Evaluator provider for evaluator type " + evaluatorType + " removed");
		}

		return result;
	}

	@Override
	public Evaluator createEvaluator(MutableEvaluatorDescriptor descriptor, TransactionHandler transactionHandler) throws EvaluationProviderNotFoundException, EvaluationProviderErrorException {

		EvaluationProvider evaluationProvider = evaluationProviderMap.get(descriptor.getEvaluatorTypeID());

		if(evaluationProvider == null){

			throw new EvaluationProviderNotFoundException(descriptor.getEvaluatorTypeID());
		}

		try{
			return evaluationProvider.createEvaluator(descriptor, transactionHandler);
		}catch(Throwable t){
			throw new EvaluationProviderErrorException("Exception thrown by evaluation provider for evaluator type " + evaluationProvider.getEvaluatorType() + " while creating evaluator " + descriptor, t, evaluationProvider);
		}
	}

	@Override
	public Evaluator getEvaluator(MutableEvaluatorDescriptor descriptor, InstanceMetadata instanceMetadata) throws EvaluationProviderNotFoundException, EvaluatorNotFoundInEvaluationProviderException, EvaluationProviderErrorException {

		EvaluationProvider evaluationProvider = evaluationProviderMap.get(descriptor.getEvaluatorTypeID());

		if(evaluationProvider != null){

			Evaluator evaluator;
			try{
				evaluator = evaluationProvider.getEvaluator(descriptor, instanceMetadata);

				if(evaluator != null){

					return evaluator;
				}
			}catch(Throwable t){

				throw new EvaluationProviderErrorException("Exception thrown by evaluation provider for evaluator type " + evaluationProvider.getEvaluatorType() + " while getting evaluator " + descriptor, t, evaluationProvider);
			}

			throw new EvaluatorNotFoundInEvaluationProviderException(evaluationProvider, descriptor);
		}

		throw new EvaluationProviderNotFoundException(descriptor.getEvaluatorTypeID());
	}

	@Override
	@SuppressWarnings("unchecked")
	public <X extends EvaluationProvider> X getEvaluationProvider(String evaluatorTypeID, Class<X> clazz) {

		EvaluationProvider evaluationProvider = this.evaluationProviderMap.get(evaluatorTypeID);

		if(evaluationProvider == null || !(clazz.isAssignableFrom(evaluationProvider.getClass()))){
			return null;
		}

		return (X)evaluationProvider;
	}

	@Override
	public EvaluationProvider getEvaluationProvider(String evaluatorTypeID) {

		return this.evaluationProviderMap.get(evaluatorTypeID);
	}

	@Override
	public ForegroundModuleResponse defaultMethod(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Exception, Throwable {

		//List currently available evaluator types

		return null;
	}

	@Override
	public void deleteEvaluator(ImmutableEvaluatorDescriptor descriptor, TransactionHandler transactionHandler) throws EvaluationProviderErrorException, EvaluationProviderNotFoundException, EvaluatorNotFoundInEvaluationProviderException {

		EvaluationProvider evaluationProvider = evaluationProviderMap.get(descriptor.getEvaluatorTypeID());

		if(evaluationProvider != null){

			boolean deleted;

			try{
				deleted = evaluationProvider.deleteEvaluator(descriptor, transactionHandler);

			}catch(Throwable t){

				throw new EvaluationProviderErrorException("Exception thrown by evaluation provider for evaluator type " + evaluationProvider.getEvaluatorType() + " while getting evaluator " + descriptor, t, evaluationProvider);
			}

			if(!deleted){

				throw new EvaluatorNotFoundInEvaluationProviderException(evaluationProvider, descriptor);
			}

			return;
		}

		throw new EvaluationProviderNotFoundException(descriptor.getEvaluatorTypeID());
	}

	@Override
	public List<Evaluator> getEvaluators(List<EvaluatorDescriptor> evaluatorDescriptors, InstanceMetadata instanceMetadata) throws EvaluationProviderNotFoundException, EvaluationProviderErrorException, EvaluatorNotFoundInEvaluationProviderException {

		List<Evaluator> evaluatorInstances = new ArrayList<Evaluator>(evaluatorDescriptors.size());

		for(EvaluatorDescriptor evaluatorDescriptor : evaluatorDescriptors){

			if(!evaluatorDescriptor.isEnabled()){

				continue;
			}

			evaluatorInstances.add(getEvaluator(evaluatorDescriptor, instanceMetadata));
		}

		return evaluatorInstances;
	}

	@Override
	public void copyEvaluator(MutableEvaluatorDescriptor sourceEvaluatorDescriptor, MutableEvaluatorDescriptor copyEvaluatorDescriptor, Query sourceQuery, Query copyQuery, Map<Integer, ImmutableStatus> statusConversionMap, TransactionHandler transactionHandler) throws EvaluationProviderNotFoundException, EvaluationProviderErrorException {

		EvaluationProvider evaluationProvider = evaluationProviderMap.get(sourceEvaluatorDescriptor.getEvaluatorTypeID());

		if(evaluationProvider != null){

			try{

				evaluationProvider.copyEvaluator(sourceEvaluatorDescriptor, copyEvaluatorDescriptor, sourceQuery, copyQuery, statusConversionMap, transactionHandler);

				return;

			}catch(Throwable t){

				throw new EvaluationProviderErrorException("Exception thrown by evaluation provider for evaluation type " + evaluationProvider.getEvaluatorType() + " while creating copy of evaluatior " + sourceEvaluatorDescriptor, t, evaluationProvider);
			}
		}

		throw new EvaluationProviderNotFoundException(sourceEvaluatorDescriptor.getEvaluatorTypeID());
	}

	@Override
	public Evaluator importEvaluator(MutableEvaluatorDescriptor descriptor, TransactionHandler transactionHandler, Query query, Map<Integer, ImmutableStatus> statusConversionMap) throws EvaluationProviderNotFoundException, EvaluationProviderErrorException {

		EvaluationProvider evaluationProvider = evaluationProviderMap.get(descriptor.getEvaluatorTypeID());

		if(evaluationProvider == null){

			throw new EvaluationProviderNotFoundException(descriptor.getEvaluatorTypeID());
		}

		try{
			return evaluationProvider.importEvaluator(descriptor, transactionHandler, query, statusConversionMap);
		}catch(Throwable t){
			throw new EvaluationProviderErrorException("Exception thrown by evaluation provider for evaluator type " + evaluationProvider.getEvaluatorType() + " while importing evaluator " + descriptor, t, evaluationProvider);
		}
	}

}
