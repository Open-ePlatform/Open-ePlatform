package com.nordicpeak.flowengine.interfaces;

import java.sql.SQLException;
import java.util.Map;

import se.unlogic.standardutils.dao.TransactionHandler;

import com.nordicpeak.flowengine.beans.EvaluatorTypeDescriptor;


public interface EvaluationProvider {

	/**
	 * @return The query type that this class provides
	 */
	public EvaluatorTypeDescriptor getEvaluatorType();

	public Evaluator createEvaluator(MutableEvaluatorDescriptor descriptor, TransactionHandler transactionHandler) throws Throwable;
	
	public Evaluator importEvaluator(MutableEvaluatorDescriptor descriptor, TransactionHandler transactionHandler, Query query, Map<Integer, ImmutableStatus> statusConversionMap) throws Throwable;

	public Evaluator getEvaluator(MutableEvaluatorDescriptor descriptor, InstanceMetadata instanceMetadata) throws Throwable;

	public boolean deleteEvaluator(ImmutableEvaluatorDescriptor descriptor, TransactionHandler transactionHandler) throws Throwable;

	public boolean supportsQueryType(Class<? extends Query> queryClass);

	public void copyEvaluator(MutableEvaluatorDescriptor sourceEvaluatorDescriptor, MutableEvaluatorDescriptor copyEvaluatorDescriptor, Query sourceQuery, Query copyQuery, Map<Integer, ImmutableStatus> statusConversionMap, TransactionHandler transactionHandler) throws SQLException;
}
