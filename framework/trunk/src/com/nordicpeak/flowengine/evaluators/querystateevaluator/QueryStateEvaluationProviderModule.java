package com.nordicpeak.flowengine.evaluators.querystateevaluator;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import se.unlogic.hierarchy.core.annotations.WebPublic;
import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.hierarchy.core.interfaces.ForegroundModuleResponse;
import se.unlogic.hierarchy.core.interfaces.MutableAttributeHandler;
import se.unlogic.standardutils.collections.CollectionUtils;
import se.unlogic.standardutils.dao.AnnotatedDAO;
import se.unlogic.standardutils.dao.HighLevelQuery;
import se.unlogic.standardutils.dao.QueryParameterFactory;
import se.unlogic.standardutils.dao.SimpleAnnotatedDAOFactory;
import se.unlogic.standardutils.dao.TransactionHandler;
import se.unlogic.standardutils.db.tableversionhandler.TableVersionHandler;
import se.unlogic.standardutils.db.tableversionhandler.UpgradeResult;
import se.unlogic.standardutils.db.tableversionhandler.XMLDBScriptProvider;
import se.unlogic.webutils.http.URIParser;

import com.nordicpeak.flowengine.beans.EvaluationResponse;
import com.nordicpeak.flowengine.enums.QueryState;
import com.nordicpeak.flowengine.evaluators.basequerystateevaluator.BaseQueryStateEvaluationProviderModule;
import com.nordicpeak.flowengine.interfaces.EvaluationCallback;
import com.nordicpeak.flowengine.interfaces.Evaluator;
import com.nordicpeak.flowengine.interfaces.ImmutableAlternative;
import com.nordicpeak.flowengine.interfaces.ImmutableStatus;
import com.nordicpeak.flowengine.interfaces.MutableEvaluatorDescriptor;
import com.nordicpeak.flowengine.interfaces.Query;
import com.nordicpeak.flowengine.interfaces.QueryInstance;
import com.nordicpeak.flowengine.queries.fixedalternativesquery.FixedAlternativesQuery;
import com.nordicpeak.flowengine.queries.fixedalternativesquery.FixedAlternativesQueryInstance;

public class QueryStateEvaluationProviderModule extends BaseQueryStateEvaluationProviderModule<QueryStateEvaluator> {
	
	
	protected QueryStateEvaluatorCRUD evaluatorCRUD;
	
	protected AnnotatedDAO<QueryStateEvaluator> evaluatorDAO;
	
	protected QueryParameterFactory<QueryStateEvaluator, Integer> evaluatorIDParamFactory;
	
	@Override
	protected void createDAOs(DataSource dataSource) throws Exception {
		
		//Automatic table version handling
		UpgradeResult upgradeResult = TableVersionHandler.upgradeDBTables(dataSource, QueryStateEvaluationProviderModule.class.getName(), new XMLDBScriptProvider(this.getClass().getResourceAsStream("DB script.xml")));
		
		if (upgradeResult.isUpgrade()) {
			
			log.info(upgradeResult.toString());
		}
		
		evaluatorDAO = new SimpleAnnotatedDAOFactory(dataSource).getDAO(QueryStateEvaluator.class);
		evaluatorIDParamFactory = evaluatorDAO.getParamFactory("evaluatorID", Integer.class);
		
		evaluatorCRUD = new QueryStateEvaluatorCRUD(QueryStateEvaluator.class, evaluatorDAO.getWrapper("evaluatorID", Integer.class), this);
	}
	
	@WebPublic(alias = "config")
	public ForegroundModuleResponse configureEvaluator(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Exception {
		
		return this.evaluatorCRUD.update(req, res, user, uriParser);
	}
	
	@Override
	public EvaluationResponse evaluate(QueryInstance queryInstance, User user, User poster, QueryStateEvaluator evaluator, EvaluationCallback callback, boolean hasQueryValidationErrors, MutableAttributeHandler attributeHandler) {
		
		if ((evaluator.getRequiredAlternativeIDs() == null && !evaluator.isFreeTextAlternative()) || getTargetQueryIDs(evaluator, callback) == null) {
			
			return null;
		}
		
		if (!(queryInstance instanceof FixedAlternativesQueryInstance)) {
			
			log.warn("Query instance " + queryInstance + " does not implement interface " + FixedAlternativesQueryInstance.class.getName());
			
			return null;
		}
		
		List<? extends ImmutableAlternative> alternatives = ((FixedAlternativesQueryInstance) queryInstance).getAlternatives();
		
		String freeTextAlternative = ((FixedAlternativesQueryInstance) queryInstance).getFreeTextAlternativeValue();
		
		if (CollectionUtils.isEmpty(alternatives) && freeTextAlternative == null) {
			
			return restoreDefaultQueryStates(queryInstance, evaluator, callback, attributeHandler);
			
		} else if (evaluator.getSelectionMode() == SelectionMode.ANY) {
			
			if (freeTextAlternative != null && evaluator.isFreeTextAlternative()) {
				
				return applyEvaluatorQueryStates(queryInstance, evaluator, callback, attributeHandler);
			}
			
			if (alternatives != null && !CollectionUtils.isEmpty(evaluator.getRequiredAlternativeIDs())) {
				
				for (ImmutableAlternative alternative : alternatives) {
					
					if (evaluator.getRequiredAlternativeIDs().contains(alternative.getAlternativeID())) {
						
						return applyEvaluatorQueryStates(queryInstance, evaluator, callback, attributeHandler);
					}
				}
			}
			
			return restoreDefaultQueryStates(queryInstance, evaluator, callback, attributeHandler);
			
		} else if (evaluator.getSelectionMode() == SelectionMode.ALL) {
			
			if (evaluator.isFreeTextAlternative() && freeTextAlternative == null) {
				
				return restoreDefaultQueryStates(queryInstance, evaluator, callback, attributeHandler);
			}
			
			if (!CollectionUtils.isEmpty(evaluator.getRequiredAlternativeIDs())) {
				
				outer: for (Integer alternativeID : evaluator.getRequiredAlternativeIDs()) {
					
					if (alternatives != null) {
						
						for (ImmutableAlternative alternative : alternatives) {
							
							if (alternative.getAlternativeID().equals(alternativeID)) {
								
								continue outer;
							}
						}
					}
					
					return restoreDefaultQueryStates(queryInstance, evaluator, callback, attributeHandler);
				}
			}
			
			return applyEvaluatorQueryStates(queryInstance, evaluator, callback, attributeHandler);
			
		} else {
			
			throw new RuntimeException("Unknown selection mode: " + evaluator.getSelectionMode());
		}
	}
	
	@Override
	public Evaluator createEvaluator(MutableEvaluatorDescriptor descriptor, TransactionHandler transactionHandler) throws Throwable {
		
		QueryStateEvaluator evaluator = new QueryStateEvaluator();
		
		evaluator.setEvaluatorID(descriptor.getEvaluatorID());
		evaluator.setQueryState(QueryState.VISIBLE);
		evaluator.setSelectionMode(SelectionMode.ANY);
		
		this.evaluatorDAO.add(evaluator, transactionHandler, null);
		
		evaluator.init(descriptor, getFullAlias() + "/config/" + descriptor.getEvaluatorID());
		
		return evaluator;
	}
	
	@Override
	public boolean supportsQueryType(Class<? extends Query> queryClass) {
		
		return FixedAlternativesQuery.class.isAssignableFrom(queryClass);
	}
	
	@Override
	public void copyEvaluator(MutableEvaluatorDescriptor sourceEvaluatorDescriptor, MutableEvaluatorDescriptor copyEvaluatorDescriptor, Query sourceQuery, Query copyQuery, Map<Integer, ImmutableStatus> statusConversionMap, TransactionHandler transactionHandler) throws SQLException {
		
		QueryStateEvaluator evaluator = evaluatorDAO.get(new HighLevelQuery<QueryStateEvaluator>(evaluatorIDParamFactory.getParameter(sourceEvaluatorDescriptor.getEvaluatorID())));
		
		evaluator.setEvaluatorID(copyEvaluatorDescriptor.getEvaluatorID());
		
		if (evaluator.getRequiredAlternativeIDs() != null) {
			
			List<? extends ImmutableAlternative> sourceAlternatives = ((FixedAlternativesQuery) sourceQuery).getAlternatives();
			List<? extends ImmutableAlternative> copyAlternatives = ((FixedAlternativesQuery) copyQuery).getAlternatives();
			
			if (!CollectionUtils.isEmpty(sourceAlternatives) && !CollectionUtils.isEmpty(copyAlternatives)) {
				
				List<Integer> newRequiredAlternativeIDs = new ArrayList<Integer>(evaluator.getRequiredAlternativeIDs().size());
				
				for (Integer requiredAlternativeID : evaluator.getRequiredAlternativeIDs()) {
					
					int alternativeIndex = 0;
					
					while (alternativeIndex < sourceAlternatives.size()) {
						
						if (sourceAlternatives.get(alternativeIndex).getAlternativeID().equals(requiredAlternativeID)) {
							
							newRequiredAlternativeIDs.add(copyAlternatives.get(alternativeIndex).getAlternativeID());
							
							break;
						}
						
						alternativeIndex++;
					}
				}
				
				evaluator.setRequiredAlternativeIDs(newRequiredAlternativeIDs);
			}
		}
		
		this.evaluatorDAO.add(evaluator, transactionHandler, null);
	}
	
	@Override
	public QueryStateEvaluator importEvaluator(MutableEvaluatorDescriptor descriptor, TransactionHandler transactionHandler, Query query, Map<Integer, ImmutableStatus> statusConversionMap) throws Throwable {
		
		QueryStateEvaluator evaluator = importEvaluatorDescriptor(descriptor, transactionHandler, query, statusConversionMap);
		
		if (evaluator.getRequiredAlternativeIDs() != null && query instanceof FixedAlternativesQuery && ((FixedAlternativesQuery) query).getAlternativeConversionMap() != null) {
			
			Map<Integer, Integer> conversionMap = ((FixedAlternativesQuery) query).getAlternativeConversionMap();
			
			List<Integer> newIDList = new ArrayList<Integer>(evaluator.getRequiredAlternativeIDs().size());
			
			for (Integer alternativeID : evaluator.getRequiredAlternativeIDs()) {
				
				Integer newID = conversionMap.get(alternativeID);
				
				if (newID != null) {
					
					newIDList.add(newID);
				}
			}
			
			evaluator.setRequiredAlternativeIDs(newIDList);
		}
		
		this.evaluatorDAO.add(evaluator, transactionHandler, null);
		
		return evaluator;
	}
	
	@Override
	protected Class<QueryStateEvaluator> getEvaluatorClass() {
		
		return QueryStateEvaluator.class;
	}
	
	@Override
	protected AnnotatedDAO<QueryStateEvaluator> getEvaluatorDAO() {
		
		return evaluatorDAO;
	}
	
	@Override
	protected QueryParameterFactory<QueryStateEvaluator, Integer> getEvaluatorIDParameterFactory() {
		
		return evaluatorIDParamFactory;
	}
	
}
