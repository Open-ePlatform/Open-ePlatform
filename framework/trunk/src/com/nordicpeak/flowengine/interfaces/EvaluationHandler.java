package com.nordicpeak.flowengine.interfaces;

import java.util.List;
import java.util.Map;

import se.unlogic.standardutils.dao.TransactionHandler;

import com.nordicpeak.flowengine.beans.EvaluatorDescriptor;
import com.nordicpeak.flowengine.beans.EvaluatorTypeDescriptor;
import com.nordicpeak.flowengine.exceptions.evaluationprovider.EvaluationProviderErrorException;
import com.nordicpeak.flowengine.exceptions.evaluationprovider.EvaluationProviderNotFoundException;
import com.nordicpeak.flowengine.exceptions.evaluationprovider.EvaluatorNotFoundInEvaluationProviderException;

public interface EvaluationHandler {

	public List<EvaluatorTypeDescriptor> getAvailableEvaluatorTypes();

	public List<EvaluatorTypeDescriptor> getAvailableEvaluatorTypes(Class<? extends Query> queryClass);

	public boolean addEvaluationProvider(EvaluationProvider queryProvider);

	public boolean removeEvaluationProvider(EvaluatorTypeDescriptor queryType);

	public Evaluator createEvaluator(MutableEvaluatorDescriptor descriptor, TransactionHandler transactionHandler) throws EvaluationProviderNotFoundException, EvaluationProviderErrorException;
	
	public Evaluator importEvaluator(MutableEvaluatorDescriptor descriptor, TransactionHandler transactionHandler, Query query, Map<Integer, ImmutableStatus> statusConversionMap) throws EvaluationProviderNotFoundException, EvaluationProviderErrorException;

	public Evaluator getEvaluator(MutableEvaluatorDescriptor descriptor, InstanceMetadata instanceMetadata) throws EvaluationProviderNotFoundException, EvaluatorNotFoundInEvaluationProviderException, EvaluationProviderErrorException;

	public void deleteEvaluator(ImmutableEvaluatorDescriptor descriptor, TransactionHandler transactionHandler) throws EvaluationProviderErrorException, EvaluationProviderNotFoundException, EvaluatorNotFoundInEvaluationProviderException;

	public <X extends EvaluationProvider> X getEvaluationProvider(String evluatorTypeID, Class<X> clazz);

	public EvaluationProvider getEvaluationProvider(String evaluatorTypeID);

	public List<Evaluator> getEvaluators(List<EvaluatorDescriptor> evaluatorDescriptors, InstanceMetadata instanceMetadata) throws EvaluationProviderNotFoundException, EvaluationProviderErrorException, EvaluatorNotFoundInEvaluationProviderException;

	public void copyEvaluator(MutableEvaluatorDescriptor sourceEvaluatorDescriptor, MutableEvaluatorDescriptor copyEvaluatorDescriptor, Query sourceQuery, Query copyQuery, Map<Integer, ImmutableStatus> statusConversionMap, TransactionHandler transactionHandler) throws EvaluationProviderNotFoundException, EvaluationProviderErrorException;
}
