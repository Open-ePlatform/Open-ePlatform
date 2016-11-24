package com.nordicpeak.flowengine.evaluators.basequerystateevaluator;

import java.util.List;
import java.util.Map;

import se.unlogic.hierarchy.core.annotations.InstanceManagerDependency;
import se.unlogic.hierarchy.core.interfaces.MutableAttributeHandler;
import se.unlogic.standardutils.collections.CollectionUtils;
import se.unlogic.standardutils.dao.AnnotatedDAO;
import se.unlogic.standardutils.dao.HighLevelQuery;
import se.unlogic.standardutils.dao.QueryParameterFactory;
import se.unlogic.standardutils.dao.TransactionHandler;
import se.unlogic.standardutils.xml.XMLGenerator;

import com.nordicpeak.flowengine.beans.EvaluationResponse;
import com.nordicpeak.flowengine.beans.QueryModification;
import com.nordicpeak.flowengine.enums.ModificationAction;
import com.nordicpeak.flowengine.enums.QueryState;
import com.nordicpeak.flowengine.evaluators.baseevaluator.BaseEvaluationProviderModule;
import com.nordicpeak.flowengine.exceptions.queryinstance.IllegalQueryInstanceAccessException;
import com.nordicpeak.flowengine.interfaces.EvaluationCallback;
import com.nordicpeak.flowengine.interfaces.Evaluator;
import com.nordicpeak.flowengine.interfaces.ImmutableEvaluatorDescriptor;
import com.nordicpeak.flowengine.interfaces.ImmutableStatus;
import com.nordicpeak.flowengine.interfaces.InstanceMetadata;
import com.nordicpeak.flowengine.interfaces.MutableEvaluatorDescriptor;
import com.nordicpeak.flowengine.interfaces.Query;
import com.nordicpeak.flowengine.interfaces.QueryHandler;
import com.nordicpeak.flowengine.interfaces.QueryInstance;

public abstract class BaseQueryStateEvaluationProviderModule<T extends BaseQueryStateEvaluator> extends BaseEvaluationProviderModule<T> {

	@InstanceManagerDependency(required = true)
	protected QueryHandler queryHandler;

	protected EvaluationResponse applyEvaluatorQueryStates(QueryInstance queryInstance, T evaluator, EvaluationCallback callback, MutableAttributeHandler attributeHandler) {

		List<QueryModification> queryModifications = null;

		QueryState evaluatorQueryState = evaluator.getQueryState();

		for (Integer queryID : getTargetQueryIDs(evaluator, callback)) {

			try {
				QueryInstance targetInstance = callback.getQueryInstance(queryID);

				if (targetInstance == null) {

					continue;
				}

				if (targetInstance.getQueryInstanceDescriptor().getQueryState() != evaluatorQueryState) {

					targetInstance.getQueryInstanceDescriptor().setQueryState(evaluatorQueryState);

					if (evaluatorQueryState == QueryState.HIDDEN) {

						queryModifications = CollectionUtils.addAndInstantiateIfNeeded(queryModifications, new QueryModification(targetInstance, ModificationAction.HIDE, attributeHandler));

					} else if (evaluatorQueryState == QueryState.VISIBLE) {

						queryModifications = CollectionUtils.addAndInstantiateIfNeeded(queryModifications, new QueryModification(targetInstance, ModificationAction.SHOW, attributeHandler));

					} else if (evaluatorQueryState == QueryState.VISIBLE_REQUIRED) {

						queryModifications = CollectionUtils.addAndInstantiateIfNeeded(queryModifications, new QueryModification(targetInstance, ModificationAction.MAKE_REQUIRED, attributeHandler));

					} else {

						throw new RuntimeException("Unknown query state: " + evaluatorQueryState);
					}
				}

			} catch (IllegalQueryInstanceAccessException e) {

				throw new RuntimeException(e);
			}
		}

		return new EvaluationResponse(queryModifications);
	}

	protected List<Integer> getTargetQueryIDs(T evaluator, EvaluationCallback callback) {

		return evaluator.getEvaluatorDescriptor().getTargetQueryIDs();
	}
	
	protected EvaluationResponse restoreDefaultQueryStates(QueryInstance queryInstance, T evaluator, EvaluationCallback callback, MutableAttributeHandler attributeHandler) {

		if (evaluator.isDoNotResetQueryState()) {

			return null;
		}

		List<QueryModification> queryModifications = null;

		for (Integer queryID : getTargetQueryIDs(evaluator, callback)) {

			try {
				QueryInstance targetInstance = callback.getQueryInstance(queryID);

				if (targetInstance == null) {

					continue;
				}

				QueryState defaultQueryState = targetInstance.getQueryInstanceDescriptor().getQueryDescriptor().getDefaultQueryState();

				if (targetInstance.getQueryInstanceDescriptor().getQueryState() != defaultQueryState) {

					targetInstance.getQueryInstanceDescriptor().setQueryState(defaultQueryState);

					if (defaultQueryState == QueryState.HIDDEN) {

						queryModifications = CollectionUtils.addAndInstantiateIfNeeded(queryModifications, new QueryModification(targetInstance, ModificationAction.HIDE, attributeHandler));

					} else if (defaultQueryState == QueryState.VISIBLE) {

						queryModifications = CollectionUtils.addAndInstantiateIfNeeded(queryModifications, new QueryModification(targetInstance, ModificationAction.SHOW, attributeHandler));

					} else if (defaultQueryState == QueryState.VISIBLE_REQUIRED) {

						queryModifications = CollectionUtils.addAndInstantiateIfNeeded(queryModifications, new QueryModification(targetInstance, ModificationAction.MAKE_REQUIRED, attributeHandler));

					} else {

						throw new RuntimeException("Unknown query state: " + defaultQueryState);
					}
				}

			} catch (IllegalQueryInstanceAccessException e) {

				throw new RuntimeException(e);
			}
		}

		return new EvaluationResponse(queryModifications);
	}
	
	public T importEvaluatorDescriptor(MutableEvaluatorDescriptor descriptor, TransactionHandler transactionHandler, Query query, Map<Integer, ImmutableStatus> statusConversionMap) throws Throwable {

		T evaluator = getEvaluatorClass().newInstance();

		evaluator.setEvaluatorID(descriptor.getEvaluatorID());

		evaluator.populate(descriptor.getImportParser().getNode(XMLGenerator.getElementName(evaluator.getClass())));
	
		return evaluator;
	}

	public QueryHandler getQueryHandler() {

		return this.queryHandler;
	}

	@Override
	public Evaluator getEvaluator(MutableEvaluatorDescriptor descriptor, InstanceMetadata instanceMetadata) throws Throwable {

		T evaluator = getEvaluatorDAO().get(new HighLevelQuery<T>(getEvaluatorIDParameterFactory().getParameter(descriptor.getEvaluatorID())));

		if (evaluator != null) {

			evaluator.init(descriptor, getFullAlias() + "/config/" + descriptor.getEvaluatorID());
		}

		return evaluator;
	}

	@Override
	public boolean deleteEvaluator(ImmutableEvaluatorDescriptor descriptor, TransactionHandler transactionHandler) throws Throwable {

		getEvaluatorDAO().delete(new HighLevelQuery<T>(getEvaluatorIDParameterFactory().getParameter(descriptor.getEvaluatorID())), transactionHandler);

		return true;
	}

	protected abstract Class<T> getEvaluatorClass();

	protected abstract AnnotatedDAO<T> getEvaluatorDAO();

	protected abstract QueryParameterFactory<T, Integer> getEvaluatorIDParameterFactory();
	
}
