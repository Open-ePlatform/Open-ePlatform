package com.nordicpeak.flowengine.managers;

import java.util.List;

import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.openhierarchy.foregroundmodules.siteprofile.interfaces.SiteProfile;
import se.unlogic.standardutils.collections.CollectionUtils;

import com.nordicpeak.flowengine.beans.QueryModification;
import com.nordicpeak.flowengine.beans.RequestMetadata;
import com.nordicpeak.flowengine.exceptions.queryinstance.IllegalQueryInstanceAccessException;
import com.nordicpeak.flowengine.interfaces.EvaluationCallback;
import com.nordicpeak.flowengine.interfaces.EvaluationEventListener;
import com.nordicpeak.flowengine.interfaces.EvaluationHandler;
import com.nordicpeak.flowengine.interfaces.Evaluator;
import com.nordicpeak.flowengine.interfaces.QueryInstance;

public class ManagedEvaluationCallback implements EvaluationCallback {

	private final List<ManagedStep> managedSteps;
	private final int minStepIndex;
	private int minQueryIndex;
	private final EvaluationHandler evaluationHandler;
	private final User user;
	private final User poster;
	private final SiteProfile siteProfile;
	private final RequestMetadata requestMetadata;
	
	public ManagedEvaluationCallback(List<ManagedStep> managedSteps, int minStepIndex, int minQueryIndex, EvaluationHandler evaluationHandler, User user, User poster, SiteProfile siteProfile, RequestMetadata requestMetadata) {

		super();
		this.managedSteps = managedSteps;
		this.minStepIndex = minStepIndex;
		this.minQueryIndex = minQueryIndex;
		this.evaluationHandler = evaluationHandler;
		this.user = user;
		this.poster = poster;
		this.siteProfile = siteProfile;
		this.requestMetadata = requestMetadata;
	}

	@Override
	public QueryInstance getQueryInstance(Integer queryID) throws IllegalQueryInstanceAccessException {

		int stepIndex = 0;

		for(ManagedStep managedStep : managedSteps){

			int queryIndex = 0;

			for(ManagedQueryInstance managedQueryInstance : managedStep.getManagedQueryInstances()){

				if(managedQueryInstance.getQueryInstance().getQueryInstanceDescriptor().getQueryDescriptor().getQueryID().equals(queryID)){

					if(stepIndex < minStepIndex || (stepIndex == minStepIndex && queryIndex < minQueryIndex)){

						throw new IllegalQueryInstanceAccessException(managedQueryInstance.getQueryInstance().getQueryInstanceDescriptor(),"Evaluators can only access query instances positioned after the current query in the flow");
					}

					return managedQueryInstance.getQueryInstance();
				}

				queryIndex++;
			}

			stepIndex++;
		}

		return null;
	}

	protected void incrementQueryIndex(){

		minQueryIndex++;
	}

	
	@Override
	public int getMinStepIndex() {
	
		return minStepIndex;
	}

	
	@Override
	public int getMinQueryIndex() {
	
		return minQueryIndex;
	}

	@Override
	public List<QueryModification> sendEvent(Object event, boolean skipCurrentQuery) {

		List<QueryModification> queryModifications = null;
		
		//Start at the correct step and query
		Integer stepIndex = minStepIndex;
		Integer queryIndex = minQueryIndex;
		
		for(;stepIndex < managedSteps.size(); stepIndex++){
			
			List<ManagedQueryInstance> managedQueryInstances = managedSteps.get(stepIndex).getManagedQueryInstances();
			
			for(;queryIndex < managedQueryInstances.size(); queryIndex++){
				
				if(skipCurrentQuery && stepIndex == minStepIndex && queryIndex == minQueryIndex){
					
					continue;
				}
				
				ManagedQueryInstance managedQueryInstance = managedQueryInstances.get(queryIndex);
				
				if(managedQueryInstance.getEvaluators() != null){
					
					for(Evaluator evaluator : managedQueryInstance.getEvaluators()){
						
						if(evaluator.getEvaluatorDescriptor().isEnabled() && evaluator instanceof EvaluationEventListener && ((EvaluationEventListener) evaluator).supportsEvent(event)){
							
							queryModifications = CollectionUtils.addAndInstantiateIfNeeded(queryModifications, ((EvaluationEventListener)evaluator).processEvent(event, managedQueryInstance.getQueryInstance(), user, user, new ManagedEvaluationCallback(managedSteps, stepIndex, queryIndex, evaluationHandler, user, poster, siteProfile, requestMetadata), evaluationHandler));
						}
					}
				}
			}
			
			queryIndex = 0;
		}
		
		return queryModifications;
	}
	
	@Override
	public QueryInstance getQueryInstance(){
		
		return managedSteps.get(minStepIndex).getManagedQueryInstances().get(minQueryIndex).getQueryInstance();
	}

	@Override
	public SiteProfile getSiteProfile() {
	
		return siteProfile;
	}

	
	@Override
	public RequestMetadata getRequestMetadata() {
	
		return requestMetadata;
	}
}
