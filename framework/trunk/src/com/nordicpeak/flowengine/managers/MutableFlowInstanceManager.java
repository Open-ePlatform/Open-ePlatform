package com.nordicpeak.flowengine.managers;

import java.io.Serializable;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.hierarchy.core.handlers.SimpleMutableAttributeHandler;
import se.unlogic.hierarchy.core.interfaces.attributes.AttributeHandler;
import se.unlogic.hierarchy.core.interfaces.attributes.MutableAttributeHandler;
import se.unlogic.hierarchy.core.utils.FCKUtils;
import se.unlogic.openhierarchy.foregroundmodules.siteprofile.interfaces.SiteProfile;
import se.unlogic.standardutils.collections.CollectionUtils;
import se.unlogic.standardutils.dao.RelationQuery;
import se.unlogic.standardutils.dao.TransactionHandler;
import se.unlogic.standardutils.json.JsonArray;
import se.unlogic.standardutils.json.JsonObject;
import se.unlogic.standardutils.json.JsonUtils;
import se.unlogic.standardutils.time.TimeUtils;
import se.unlogic.standardutils.validation.ValidationError;
import se.unlogic.standardutils.validation.ValidationException;
import se.unlogic.webutils.url.URLRewriter;

import com.nordicpeak.flowengine.beans.EvaluationResponse;
import com.nordicpeak.flowengine.beans.Flow;
import com.nordicpeak.flowengine.beans.FlowInstance;
import com.nordicpeak.flowengine.beans.PDFQueryResponse;
import com.nordicpeak.flowengine.beans.QueryDescriptor;
import com.nordicpeak.flowengine.beans.QueryInstanceDescriptor;
import com.nordicpeak.flowengine.beans.QueryModification;
import com.nordicpeak.flowengine.beans.QueryResponse;
import com.nordicpeak.flowengine.beans.RequestMetadata;
import com.nordicpeak.flowengine.beans.Status;
import com.nordicpeak.flowengine.beans.Step;
import com.nordicpeak.flowengine.beans.SubmitCheckFailedResponse;
import com.nordicpeak.flowengine.dao.FlowEngineDAOFactory;
import com.nordicpeak.flowengine.enums.EventType;
import com.nordicpeak.flowengine.enums.FlowDirection;
import com.nordicpeak.flowengine.enums.ModificationAction;
import com.nordicpeak.flowengine.enums.QueryState;
import com.nordicpeak.flowengine.exceptions.evaluation.EvaluationException;
import com.nordicpeak.flowengine.exceptions.evaluationprovider.EvaluationProviderErrorException;
import com.nordicpeak.flowengine.exceptions.evaluationprovider.EvaluationProviderNotFoundException;
import com.nordicpeak.flowengine.exceptions.evaluationprovider.EvaluatorNotFoundInEvaluationProviderException;
import com.nordicpeak.flowengine.exceptions.flowinstance.InvalidFlowInstanceStepException;
import com.nordicpeak.flowengine.exceptions.flowinstance.MissingQueryInstanceDescriptor;
import com.nordicpeak.flowengine.exceptions.flowinstancemanager.DuplicateFlowInstanceManagerIDException;
import com.nordicpeak.flowengine.exceptions.flowinstancemanager.FlowInstanceManagerClosedException;
import com.nordicpeak.flowengine.exceptions.queryinstance.IllegalQueryInstanceAccessException;
import com.nordicpeak.flowengine.exceptions.queryinstance.QueryModificationException;
import com.nordicpeak.flowengine.exceptions.queryinstance.SubmitCheckException;
import com.nordicpeak.flowengine.exceptions.queryinstance.UnableToGetQueryInstanceFormHTMLException;
import com.nordicpeak.flowengine.exceptions.queryinstance.UnableToGetQueryInstancePDFContentException;
import com.nordicpeak.flowengine.exceptions.queryinstance.UnableToGetQueryInstanceShowHTMLException;
import com.nordicpeak.flowengine.exceptions.queryinstance.UnableToPopulateQueryInstanceException;
import com.nordicpeak.flowengine.exceptions.queryinstance.UnableToResetQueryInstanceException;
import com.nordicpeak.flowengine.exceptions.queryinstance.UnableToSaveQueryInstanceException;
import com.nordicpeak.flowengine.exceptions.queryprovider.QueryInstanceNotFoundInQueryProviderException;
import com.nordicpeak.flowengine.exceptions.queryprovider.QueryNotFoundInQueryProviderException;
import com.nordicpeak.flowengine.exceptions.queryprovider.QueryProviderErrorException;
import com.nordicpeak.flowengine.exceptions.queryprovider.QueryProviderNotFoundException;
import com.nordicpeak.flowengine.interfaces.EvaluationHandler;
import com.nordicpeak.flowengine.interfaces.Evaluator;
import com.nordicpeak.flowengine.interfaces.FlowEngineInterface;
import com.nordicpeak.flowengine.interfaces.ImmutableFlowEngineInterface;
import com.nordicpeak.flowengine.interfaces.ImmutableFlowInstance;
import com.nordicpeak.flowengine.interfaces.ImmutableQueryInstance;
import com.nordicpeak.flowengine.interfaces.ImmutableStep;
import com.nordicpeak.flowengine.interfaces.InstanceMetadata;
import com.nordicpeak.flowengine.interfaces.MutableQueryInstanceDescriptor;
import com.nordicpeak.flowengine.interfaces.QueryHandler;
import com.nordicpeak.flowengine.interfaces.QueryInstance;
import com.nordicpeak.flowengine.interfaces.SubmitCheck;
import com.nordicpeak.flowengine.utils.FlowInstanceUtils;
import com.nordicpeak.flowengine.utils.TextTagReplacer;

public class MutableFlowInstanceManager implements Serializable, HttpSessionBindingListener, FlowInstanceManager {

	private static final RelationQuery FLOW_INSTANCE_ADD_RELATIONS = new RelationQuery(FlowInstance.ATTRIBUTES_RELATION, FlowInstance.OWNERS_RELATION);
	private static final RelationQuery FLOW_INSTANCE_UPDATE_RELATIONS = new RelationQuery(FlowInstance.ATTRIBUTES_RELATION);

	// Nested class to keep track of active flow instance managers in a protected fashion
	public final static class FlowInstanceManagerRegistery implements Serializable {

		private static final long serialVersionUID = -2452906097547060784L;

		private static FlowInstanceManagerRegistery REGISTERY;

		private final ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();
		private final Lock r = readWriteLock.readLock();
		private final Lock w = readWriteLock.writeLock();

		private final ArrayList<MutableFlowInstanceManager> sessionBoundInstanceManagers = new ArrayList<MutableFlowInstanceManager>();
		private transient ArrayList<MutableFlowInstanceManager> nonSessionBoundInstanceManagers = new ArrayList<MutableFlowInstanceManager>();

		private FlowInstanceManagerRegistery() {};

		public static synchronized FlowInstanceManagerRegistery getInstance() {

			if(REGISTERY == null){
				REGISTERY = new FlowInstanceManagerRegistery();
			}

			return REGISTERY;
		}

		private Object readResolve() {

			if(REGISTERY == null){
				REGISTERY = this;

				if(REGISTERY.nonSessionBoundInstanceManagers == null){

					nonSessionBoundInstanceManagers = new ArrayList<MutableFlowInstanceManager>();
				}
			}

			return REGISTERY;
		}

		private void addSessionBoundInstance(MutableFlowInstanceManager mutableFlowInstanceManager) {

			try{
				w.lock();
				this.nonSessionBoundInstanceManagers.remove(mutableFlowInstanceManager);
				this.sessionBoundInstanceManagers.add(mutableFlowInstanceManager);
			}finally{
				w.unlock();
			}
		}

		private void removeSessionBoundInstance(MutableFlowInstanceManager mutableFlowInstanceManager) {

			try{
				w.lock();
				this.sessionBoundInstanceManagers.remove(mutableFlowInstanceManager);
			}finally{
				w.unlock();
			}
		}

		public void addNonSessionBoundInstance(MutableFlowInstanceManager mutableFlowInstanceManager) {

			try{
				w.lock();
				this.nonSessionBoundInstanceManagers.add(mutableFlowInstanceManager);
			}finally{
				w.unlock();
			}
		}

		public void removeNonSessionBoundInstance(MutableFlowInstanceManager mutableFlowInstanceManager) {

			try{
				w.lock();
				this.nonSessionBoundInstanceManagers.remove(mutableFlowInstanceManager);
			}finally{
				w.unlock();
			}
		}

		public ArrayList<MutableFlowInstanceManager> getSessionBoundInstances() {

			try{
				r.lock();
				return new ArrayList<MutableFlowInstanceManager>(this.sessionBoundInstanceManagers);
			}finally{
				r.unlock();
			}
		}

		public ArrayList<MutableFlowInstanceManager> getNonSessionBoundInstances() {

			try{
				r.lock();
				return new ArrayList<MutableFlowInstanceManager>(this.nonSessionBoundInstanceManagers);
			}finally{
				r.unlock();
			}
		}

		public boolean isActiveInstance(String id) {

			try{
				r.lock();

				for(MutableFlowInstanceManager manager : sessionBoundInstanceManagers){

					if(manager.getInstanceManagerID().equals(id)){

						return true;
					}
				}

				for(MutableFlowInstanceManager manager : nonSessionBoundInstanceManagers){

					if(manager.getInstanceManagerID().equals(id)){

						return true;
					}
				}

				return false;

			}finally{
				r.unlock();
			}
		}

		public int closeInstances(int flowInstanceID, QueryHandler queryHandler) {

			int closedCounter = 0;

			try{
				w.lock();

				for(MutableFlowInstanceManager manager : sessionBoundInstanceManagers){

					if(manager.getFlowInstanceID() != null && manager.getFlowInstanceID().equals(flowInstanceID)){

						manager.close(queryHandler);

						closedCounter++;
					}
				}

				for(MutableFlowInstanceManager manager : nonSessionBoundInstanceManagers){

					if(manager.getFlowInstanceID() != null && manager.getFlowInstanceID().equals(flowInstanceID)){

						manager.close(queryHandler);

						closedCounter++;
					}
				}

				return closedCounter;

			}finally{
				w.unlock();
			}
		}

		public int closeInstances(Flow flow, QueryHandler queryHandler) {

			if(flow == null){
				
				throw new NullPointerException("Flow cannot be null");
			}
			
			if(queryHandler == null){
				
				throw new NullPointerException("QueryHandler cannot be null");
			}
			
			int closedCounter = 0;

			try{
				w.lock();

				for(MutableFlowInstanceManager manager : sessionBoundInstanceManagers){

					if(manager.getFlowInstance().getFlow().getFlowID().equals(flow.getFlowID()) && !manager.isClosed()){

						manager.close(queryHandler);

						closedCounter++;
					}
				}

				for(MutableFlowInstanceManager manager : nonSessionBoundInstanceManagers){

					if(manager.getFlowInstance().getFlow().getFlowID().equals(flow.getFlowID()) && !manager.isClosed()){

						manager.close(queryHandler);

						closedCounter++;
					}
				}

				return closedCounter;

			}finally{
				w.unlock();
			}
		}
	}

	private static final long serialVersionUID = 7224301693975233584L;

	private final FlowInstanceManagerRegistery registery = FlowInstanceManagerRegistery.getInstance();

	protected final FlowInstance flowInstance;
	private final List<ManagedStep> managedSteps;
	private Integer currentStepIndex;
	private boolean closed;
	private String instanceManagerID;
	private long created = System.currentTimeMillis();

	private boolean hasUnsavedChanges;
	private int changesCounter = 0;

	private boolean concurrentModificationLock;
	
	private MutableAttributeHandler sessionAttributeHandler;
	
	/**
	 * Creates a new flow instance for the given flow and user
	 *
	 * @param flow The flow to create the instance from. The flow must include it's steps, default flow states, query descriptors and evaluator descriptors.
	 * @param instanceManagerID
	 * @param instanceMetadata
	 * @throws QueryNotFoundInQueryProviderException
	 * @throws QueryProviderNotFoundException
	 * @throws QueryProviderErrorException
	 * @throws DuplicateFlowInstanceManagerIDException
	 * @throws QueryInstanceNotFoundInQueryProviderException
	 * @throws EvaluatorNotFoundInEvaluationProviderException
	 * @throws EvaluationProviderErrorException
	 * @throws EvaluationProviderNotFoundException
	 * @throws EvaluationException
	 * @throws UnableToResetQueryInstanceException
	 */
	public MutableFlowInstanceManager(Flow flow, QueryHandler queryHandler, EvaluationHandler evaluationHandler, String instanceManagerID, HttpServletRequest req, User user, User poster, InstanceMetadata instanceMetadata, RequestMetadata requestMetadata, String absoluteFileURL) throws QueryProviderNotFoundException, QueryProviderErrorException, DuplicateFlowInstanceManagerIDException, QueryInstanceNotFoundInQueryProviderException, EvaluationProviderNotFoundException, EvaluationProviderErrorException, EvaluatorNotFoundInEvaluationProviderException, EvaluationException, UnableToResetQueryInstanceException {

		//Create new FlowInstance with default "new" state
		this.flowInstance = new FlowInstance();
		flowInstance.setPoster(poster);

		SiteProfile siteProfile = instanceMetadata.getSiteProfile();
		
		if (siteProfile != null) {
			
			flowInstance.setProfileID(siteProfile.getProfileID());
		}
		
		setID(instanceManagerID);
		
		TextTagReplacer.replaceTextTags(flow, siteProfile);
		
		if (absoluteFileURL != null) {
			
			FCKUtils.setAbsoluteFileUrls(flow, absoluteFileURL);
		}
		
		if (req != null) {
			
			URLRewriter.setAbsoluteLinkUrls(flow, req);
		}
		
		flowInstance.setFlow(flow);
		
		//Parse steps
		managedSteps = new ArrayList<ManagedStep>(flow.getSteps().size());

		for(Step step : flow.getSteps()){

			if(step.getQueryDescriptors() == null){

				managedSteps.add(new ManagedStep(step, new ArrayList<ManagedQueryInstance>(0)));
				continue;
			}
			
			step.setFlow(flow);

			List<ManagedQueryInstance> managedQueryInstances = new ArrayList<ManagedQueryInstance>(step.getQueryDescriptors().size());

			for(QueryDescriptor queryDescriptor : step.getQueryDescriptors()){

				queryDescriptor.setStep(step);
				
				QueryInstanceDescriptor queryInstanceDescriptor = new QueryInstanceDescriptor(queryDescriptor);

				queryInstanceDescriptor.setQueryDescriptor(queryDescriptor);

				queryInstanceDescriptor.copyQueryDescriptorValues();

				QueryInstance queryInstance = queryHandler.getQueryInstance(queryInstanceDescriptor, instanceManagerID, req, user, poster, instanceMetadata);

				List<Evaluator> evaluators = null;

				if(!CollectionUtils.isEmpty(queryDescriptor.getEvaluatorDescriptors())){

					evaluators = evaluationHandler.getEvaluators(queryDescriptor.getEvaluatorDescriptors(), instanceMetadata);
				}

				managedQueryInstances.add(new ManagedQueryInstance(queryInstance, evaluators));
			}

			managedSteps.add(new ManagedStep(step, managedQueryInstances));
		}

		currentStepIndex = 0;

		flowInstance.setStepID(managedSteps.get(currentStepIndex).getStep().getStepID());
		
		initEvaluators(evaluationHandler, user, poster, instanceMetadata, requestMetadata, req);
	}

	/**
	 * Opens an existing flow instance for the given user
	 *
	 * @param flowInstance The flow instance to be managed, must include it's flow, steps, default flow states, query descriptors, query instance descriptors relation and evaluator descriptors.
	 * @param instanceMetadata
	 * @throws MissingQueryInstanceDescriptor
	 * @throws QueryNotFoundInQueryProviderException
	 * @throws QueryProviderNotFoundException
	 * @throws InvalidFlowInstanceStepException
	 * @throws QueryProviderErrorException
	 * @throws DuplicateFlowInstanceManagerIDException
	 * @throws QueryInstanceNotFoundInQueryProviderException
	 * @throws EvaluatorNotFoundInEvaluationProviderException
	 * @throws EvaluationProviderErrorException
	 * @throws EvaluationProviderNotFoundException
	 * @throws EvaluationException
	 * @throws UnableToResetQueryInstanceException
	 */
	public MutableFlowInstanceManager(FlowInstance flowInstance, QueryHandler queryHandler, EvaluationHandler evaluationHandler, String instanceManagerID, HttpServletRequest req, User user, InstanceMetadata instanceMetadata, RequestMetadata requestMetadata, String absoluteFileURL) throws MissingQueryInstanceDescriptor, QueryProviderNotFoundException, InvalidFlowInstanceStepException, QueryProviderErrorException, DuplicateFlowInstanceManagerIDException, QueryInstanceNotFoundInQueryProviderException, EvaluationProviderNotFoundException, EvaluationProviderErrorException, EvaluatorNotFoundInEvaluationProviderException, EvaluationException, UnableToResetQueryInstanceException {

		this.flowInstance = flowInstance;
		User resolvedPoster = getPoster(null);

		setID(instanceManagerID);

		TextTagReplacer.replaceTextTags(flowInstance.getFlow(), instanceMetadata.getSiteProfile());
		
		if(absoluteFileURL != null){
			
			FCKUtils.setAbsoluteFileUrls(flowInstance.getFlow(), absoluteFileURL);
		}
		
		if(req != null){
			
			URLRewriter.setAbsoluteLinkUrls(flowInstance.getFlow(), req);
		}
		
		managedSteps = new ArrayList<ManagedStep>(flowInstance.getFlow().getSteps().size());

		for(Step step : flowInstance.getFlow().getSteps()){

			if(step.getQueryDescriptors() == null){

				managedSteps.add(new ManagedStep(step, new ArrayList<ManagedQueryInstance>(0)));
				continue;
			}
			
			step.setFlow(flowInstance.getFlow());

			List<ManagedQueryInstance> managedQueryInstances = new ArrayList<ManagedQueryInstance>(step.getQueryDescriptors().size());

			for(QueryDescriptor queryDescriptor : step.getQueryDescriptors()){

				if(CollectionUtils.isEmpty(queryDescriptor.getQueryInstanceDescriptors())){

					throw new MissingQueryInstanceDescriptor(flowInstance, queryDescriptor);
				}
				
				queryDescriptor.setStep(step);

				QueryInstanceDescriptor queryInstanceDescriptor = queryDescriptor.getQueryInstanceDescriptors().get(0);

				//Reverse bean relations to avoid recursion problems when generating XML
				queryInstanceDescriptor.setQueryDescriptor(queryDescriptor);
				queryDescriptor.setQueryInstanceDescriptors(null);

				QueryInstance queryInstance = queryHandler.getQueryInstance(queryInstanceDescriptor, instanceManagerID, req, user, resolvedPoster, instanceMetadata);

				List<Evaluator> evaluators = null;

				if(!CollectionUtils.isEmpty(queryDescriptor.getEvaluatorDescriptors())){

					evaluators = evaluationHandler.getEvaluators(queryDescriptor.getEvaluatorDescriptors(), instanceMetadata);
				}

				managedQueryInstances.add(new ManagedQueryInstance(queryInstance, evaluators));
			}

			managedSteps.add(new ManagedStep(step, managedQueryInstances));
		}

		if(flowInstance.isFullyPopulated() || flowInstance.getStepID() == null){

			currentStepIndex = 0;

		}else{

			int index = 0;

			for(ManagedStep managedStep : managedSteps){

				if(managedStep.getStep().getStepID().equals(flowInstance.getStepID())){

					currentStepIndex = index;
					break;
				}

				index++;
			}

			if(currentStepIndex == null){

				throw new InvalidFlowInstanceStepException(flowInstance);
			}
		}
		
		initEvaluators(evaluationHandler, user, resolvedPoster, instanceMetadata, requestMetadata, req);
	}

	public void initEvaluators(EvaluationHandler evaluationHandler, User user, InstanceMetadata metadata, RequestMetadata requestMetadata, HttpServletRequest req) throws EvaluationException, UnableToResetQueryInstanceException {
		
		initEvaluators(evaluationHandler, user, getPoster(null), metadata, requestMetadata, req);
	}
	
	private void initEvaluators(EvaluationHandler evaluationHandler, User user, User resolvedPoster, InstanceMetadata metadata, RequestMetadata requestMetadata, HttpServletRequest req) throws EvaluationException, UnableToResetQueryInstanceException {

		int initStepIndex = 0;
		int initQueryIndex = 0;
		
		ArrayList<QueryModification> queryModifications = null;
		
		for(ManagedStep managedStep : managedSteps){
			
			for(ManagedQueryInstance managedQueryInstance : managedStep.getManagedQueryInstances()){
				
				if(managedQueryInstance.getEvaluators() != null){
					
					ManagedEvaluationCallback evaluationCallback = null;
					
					for(Evaluator evaluator : managedQueryInstance.getEvaluators()){
						
						if(evaluator.getEvaluatorDescriptor().isEnabled() && evaluator.requiresInitialization()){
							
							if(queryModifications == null){
								
								queryModifications = new ArrayList<QueryModification>();
							}
							
							if(evaluationCallback == null){
								
								evaluationCallback = new ManagedEvaluationCallback(flowInstance, managedSteps, initStepIndex, initQueryIndex, evaluationHandler, user, resolvedPoster, metadata.getSiteProfile(), requestMetadata, req);
							}
							
							try {
								evaluate(managedQueryInstance.getQueryInstance(), evaluator, user, resolvedPoster, evaluationCallback, evaluationHandler, false, queryModifications, false, metadata.getSiteProfile(), requestMetadata, req);
								
							} catch (UnableToResetQueryInstanceException e) {

								throw new EvaluationException(evaluator.getEvaluatorDescriptor(),e);
							}
						}
					}
				}
				
				initQueryIndex++;
			}
			
			initStepIndex++;
			initQueryIndex = 0;
		}
		
		// Reset hidden query instances
		if (!CollectionUtils.isEmpty(queryModifications)) {
			
			HashSet<QueryInstance> hiddenQueryInstances = new HashSet<QueryInstance>();
			
			for (QueryModification queryModification : queryModifications) {
				
				if (queryModification.getAction() == ModificationAction.HIDE) {
					
					hiddenQueryInstances.add(queryModification.getQueryInstance());
				}
			}
			
			for (QueryInstance queryInstance : hiddenQueryInstances) {
				
				if (queryInstance.getQueryInstanceDescriptor().getQueryState() == QueryState.HIDDEN) {
					
					try {
						queryInstance.reset(flowInstance.getAttributeHandler());
						
					} catch (RuntimeException e) {
						
						throw new UnableToResetQueryInstanceException(queryInstance.getQueryInstanceDescriptor(), e);
					}
				}
			}
		}
	}

	private void setID(String instanceManagerID) throws DuplicateFlowInstanceManagerIDException {

		if(instanceManagerID == null){

			throw new NullPointerException("instanceManagerID cannot be null");

		}else if(registery.isActiveInstance(instanceManagerID)){

			throw new DuplicateFlowInstanceManagerIDException(flowInstance, instanceManagerID);
		}

		this.instanceManagerID = instanceManagerID;
	}

	public synchronized ManagerResponse getCurrentStepFormHTML(QueryHandler queryHandler, HttpServletRequest req, User user, User poster, String baseQueryRequestURL, RequestMetadata requestMetadata) throws UnableToGetQueryInstanceFormHTMLException, FlowInstanceManagerClosedException {

		checkState();

		req.setAttribute("MutableFlowInstanceManager.currentStepIndex", currentStepIndex);
		req.setAttribute("MutableFlowInstanceManager.flowFamilyID", this.flowInstance.getFlow().getFlowFamily().getFlowFamilyID());

		ManagedStep currentStep = managedSteps.get(currentStepIndex);

		ArrayList<QueryResponse> queryResponses = new ArrayList<QueryResponse>(currentStep.getManagedQueryInstances().size());

		for(ManagedQueryInstance managedQueryInstance : currentStep.getManagedQueryInstances()){

			if(managedQueryInstance.getQueryInstance().getQueryInstanceDescriptor().getQueryState() != QueryState.HIDDEN){

				try{
					queryResponses.add(managedQueryInstance.getQueryInstance().getFormHTML(req, user, getPoster(poster), null, queryHandler, requiresAjaxPosting(managedQueryInstance, currentStep), getQueryRequestURL(managedQueryInstance, baseQueryRequestURL), requestMetadata, flowInstance.getAttributeHandler()));
				}catch(Throwable e){
					throw new UnableToGetQueryInstanceFormHTMLException(managedQueryInstance.getQueryInstance().getQueryInstanceDescriptor(), e);
				}
			} else {
				queryResponses.add(new QueryResponse(managedQueryInstance.getQueryInstance().getQueryInstanceDescriptor().getQueryDescriptor()));
			}
		}
		
		return new ManagerResponse(currentStep.getStep().getStepID(), currentStepIndex, queryResponses, false, concurrentModificationLock);
	}

	public boolean requiresAjaxPosting(ManagedQueryInstance managedQueryInstance, ManagedStep currentStep) {

		//Check if this query should enable ajax posting
		if(managedQueryInstance.getQueryInstance().getQueryInstanceDescriptor().getQueryDescriptor().getEvaluatorDescriptors() != null){

			for(Evaluator evaluator : managedQueryInstance.getEvaluators()){

				if(evaluator.getEvaluatorDescriptor().isEnabled()){
				
					if(evaluator.forceAjaxPosting()){
						
						return true;
					
					}else if(evaluator.getEvaluatorDescriptor().getTargetQueryIDs() != null){

						for(Integer queryID : evaluator.getEvaluatorDescriptor().getTargetQueryIDs()){

							for(ManagedQueryInstance mQueryInstance : currentStep.getManagedQueryInstances()){

								if(mQueryInstance.getQueryInstance().getQueryInstanceDescriptor().getQueryDescriptor().getQueryID().equals(queryID)){

									return true;
								}
							}
						}
					}
				}
			}
		}

		return false;
	}

	public synchronized ManagerResponse getCurrentStepShowHTML(HttpServletRequest req, User user, User poster, FlowEngineInterface flowEngineInterface, boolean onlyPopulatedQueries, String baseUpdateURL, String baseQueryRequestURL, RequestMetadata requestMetadata) throws UnableToGetQueryInstanceShowHTMLException, FlowInstanceManagerClosedException {

		checkState();

		return getStepShowHTML(currentStepIndex, req, user, getPoster(poster), flowEngineInterface, onlyPopulatedQueries, baseUpdateURL, baseQueryRequestURL, requestMetadata);
	}

	@Override
	public synchronized List<ManagerResponse> getFullShowHTML(HttpServletRequest req, User user, User poster, ImmutableFlowEngineInterface flowEngineInterface, boolean onlyPopulatedQueries, String baseUpdateURL, String baseQueryRequestURL, RequestMetadata requestMetadata) throws UnableToGetQueryInstanceShowHTMLException, FlowInstanceManagerClosedException {

		checkState();

		List<ManagerResponse> managerResponses = new ArrayList<ManagerResponse>(this.managedSteps.size());

		for(int stepIndex = 0; stepIndex < this.managedSteps.size(); stepIndex++){

			managerResponses.add(getStepShowHTML(stepIndex, req, user, getPoster(poster), flowEngineInterface, onlyPopulatedQueries, baseUpdateURL, baseQueryRequestURL, requestMetadata));
		}

		return managerResponses;
	}

	private synchronized ManagerResponse getStepShowHTML(int stepIndex, HttpServletRequest req, User user, User resolvedPoster, ImmutableFlowEngineInterface flowEngineInterface, boolean onlyPopulatedQueries, String baseUpdateURL, String baseQueryRequestURL, RequestMetadata requestMetadata) throws UnableToGetQueryInstanceShowHTMLException, FlowInstanceManagerClosedException {

		ManagedStep managedStep = managedSteps.get(stepIndex);

		String stepUpdateURL;

		if(baseUpdateURL == null){

			stepUpdateURL = null;

		}else{

			stepUpdateURL = baseUpdateURL + "?step=" + managedStep.getStep().getStepID();
		}

		AttributeHandler attributeHandler = flowInstance.getAttributeHandler();
		
		ArrayList<QueryResponse> queryResponses = new ArrayList<QueryResponse>(managedStep.getManagedQueryInstances().size());

		for(ManagedQueryInstance managedQueryInstance : managedStep.getManagedQueryInstances()){

			if(managedQueryInstance.getQueryInstance().getQueryInstanceDescriptor().getQueryState() != QueryState.HIDDEN && !(onlyPopulatedQueries && !managedQueryInstance.getQueryInstance().getQueryInstanceDescriptor().isPopulated())){

				try{
					queryResponses.add(managedQueryInstance.getQueryInstance().getShowHTML(req, user, resolvedPoster, flowEngineInterface.getQueryHandler(), stepUpdateURL, getQueryRequestURL(managedQueryInstance, baseQueryRequestURL), attributeHandler));
				}catch(Throwable e){
					throw new UnableToGetQueryInstanceShowHTMLException(managedQueryInstance.getQueryInstance().getQueryInstanceDescriptor(), e);
				}
			}
		}

		return new ManagerResponse(managedStep.getStep().getStepID(), stepIndex, queryResponses, false, concurrentModificationLock);
	}

	private String getQueryRequestURL(ManagedQueryInstance managedQueryInstance, String baseQueryRequestURL) {

		return baseQueryRequestURL + managedQueryInstance.getQueryInstance().getQueryInstanceDescriptor().getQueryDescriptor().getQueryID();
	}
	
	private String getQueryRequestURL(QueryInstance queryInstance, String baseQueryRequestURL) {

		return baseQueryRequestURL + queryInstance.getQueryInstanceDescriptor().getQueryDescriptor().getQueryID();
	}

	public synchronized String populateQueryInCurrentStep(HttpServletRequest req, User user, User poster, int queryID, QueryHandler queryHandler, EvaluationHandler evaluationHandler, String baseQueryRequestURL, RequestMetadata requestMetadata, SiteProfile siteProfile) throws FlowInstanceManagerClosedException, UnableToPopulateQueryInstanceException, EvaluationException, QueryModificationException, UnableToResetQueryInstanceException{

		checkState();

		User resolvedPoster = getPoster(poster);
		ManagedQueryInstance managedQueryInstance = null;

		int queryIndex = 0;

		for(ManagedQueryInstance managedInstance : managedSteps.get(currentStepIndex).getManagedQueryInstances()){

			if(managedInstance.getQueryInstance().getQueryInstanceDescriptor().getQueryDescriptor().getQueryID().equals(queryID)){

				managedQueryInstance = managedInstance;

				break;
			}

			queryIndex++;
		}

		if(managedQueryInstance == null){

			return null;//"Query not found in current step error exception something....";

		}else if(managedQueryInstance.getQueryInstance().getQueryInstanceDescriptor().getQueryState() == QueryState.HIDDEN){

			return null;//"Query is currently hidden and we dont populate hidden queries error exception something....";
		}

		List<ValidationError> validationErrors = null;

		try{
			managedQueryInstance.getQueryInstance().populate(req, user, resolvedPoster, true, queryHandler, flowInstance.getAttributeHandler(), requestMetadata);

		}catch(RuntimeException e){

			throw new UnableToPopulateQueryInstanceException(managedQueryInstance.getQueryInstance().getQueryInstanceDescriptor(), e);

		}catch(ValidationException e){

			validationErrors = e.getErrors();
		}

		setUnsavedChanges();

		List<QueryModification> queryModifications = null;

		if(managedQueryInstance.getEvaluators() != null){

			ManagedEvaluationCallback evaluationCallback = new ManagedEvaluationCallback(flowInstance, managedSteps, currentStepIndex, queryIndex, evaluationHandler, user, resolvedPoster, siteProfile, requestMetadata, req);

			for(Evaluator evaluator : managedQueryInstance.getEvaluators()){

				if(!evaluator.getEvaluatorDescriptor().isEnabled()){

					continue;
				}
				
				if(queryModifications == null){
					
					queryModifications = new ArrayList<QueryModification>();
				}

				evaluate(managedQueryInstance.getQueryInstance(), evaluator, user, resolvedPoster, evaluationCallback, evaluationHandler, validationErrors != null, queryModifications, siteProfile, requestMetadata, req);
			}
		}

		JsonObject response = new JsonObject();

		if(!CollectionUtils.isEmpty(queryModifications)) {

			String contextPath = req.getContextPath();

			JsonArray modifications = new JsonArray();

			for(QueryModification queryModification : queryModifications) {

				ManagedQueryInstance modifiedQuery = getQueryInCurrentStep(queryModification.getQueryInstance());
				
				if(modifiedQuery == null){

					continue;
				}

				try {

					modifications.addNode(queryModification.toJson(req, user, resolvedPoster, queryHandler, requiresAjaxPosting(modifiedQuery, managedSteps.get(currentStepIndex)), contextPath, getQueryRequestURL(queryModification.getQueryInstance(), baseQueryRequestURL), requestMetadata));

				} catch (Throwable e) {

					throw new QueryModificationException(queryModification.getQueryInstance().getQueryInstanceDescriptor(), e);
				}

			}

			response.putField("QueryModifications", modifications);

		}

		if(validationErrors != null) {

			JsonObject errorJson = new JsonObject();
			
			errorJson.putField("queryType", managedQueryInstance.getQueryInstance().getClass().getSimpleName());
			errorJson.putField("queryID", managedQueryInstance.getQueryInstance().getQueryInstanceDescriptor().getQueryDescriptor().getQueryID().toString());
			errorJson.putField("errors", JsonUtils.encode(validationErrors));
			
			response.putField("ValidationErrors", errorJson);
		}

		return response.toJson();
	}

	private void evaluate(QueryInstance queryInstance, Evaluator evaluator, User user, User resolvedPoster, ManagedEvaluationCallback evaluationCallback, EvaluationHandler evaluationHandler, boolean hasValidationErrors, List<QueryModification> queryModifications, SiteProfile siteProfile, RequestMetadata requestMetadata, HttpServletRequest req) throws UnableToResetQueryInstanceException, EvaluationException {
		
		evaluate(queryInstance, evaluator, user, resolvedPoster, evaluationCallback, evaluationHandler, hasValidationErrors, queryModifications, true, siteProfile, requestMetadata, req);
	}
	
	private void evaluate(QueryInstance queryInstance, Evaluator evaluator, User user, User resolvedPoster, ManagedEvaluationCallback evaluationCallback, EvaluationHandler evaluationHandler, boolean hasValidationErrors, List<QueryModification> queryModifications, boolean resetHiddenInstances, SiteProfile siteProfile, RequestMetadata requestMetadata, HttpServletRequest req) throws UnableToResetQueryInstanceException, EvaluationException {
		
		try {
			EvaluationResponse response = evaluator.evaluate(queryInstance, user, resolvedPoster, evaluationCallback, evaluationHandler, hasValidationErrors, flowInstance.getAttributeHandler());
			
			if (response != null) {
				
				if (!CollectionUtils.isEmpty(response.getModifications())) {
					
					if (resetHiddenInstances) {
						for (QueryModification queryModification : response.getModifications()) {
							
							if (queryModification.getAction() == ModificationAction.HIDE) {
								
								try {
									queryModification.getQueryInstance().reset(flowInstance.getAttributeHandler());
									
								} catch (RuntimeException e) {
									
									throw new UnableToResetQueryInstanceException(queryModification.getQueryInstance().getQueryInstanceDescriptor(), e);
								}
							}
						}
					}
					
					if (queryModifications != null) {
						
						queryModifications.addAll(response.getModifications());
					}
					
					for (QueryModification queryModification : response.getModifications()) {
						
						Integer queryID = queryModification.getQueryInstance().getQueryInstanceDescriptor().getQueryDescriptor().getQueryID();
						
						ManagedQueryInstance managedQueryInstance = null;
						
						int queryIndex = 0;
						int stepIndex = 0;
						
						outer: for (ManagedStep managedStep : managedSteps) {
							
							if (stepIndex < evaluationCallback.getMinStepIndex()) {
								
								stepIndex++;
								
								continue;
							}
							
							queryIndex = 0;
							
							for (ManagedQueryInstance managedInstance : managedStep.getManagedQueryInstances()) {
								
								if (managedInstance.getQueryInstance().getQueryInstanceDescriptor().getQueryDescriptor().getQueryID().equals(queryID)) {
									
									if (stepIndex == evaluationCallback.getMinStepIndex() && queryIndex < evaluationCallback.getMinQueryIndex()) {
										
										throw new EvaluationException(evaluator.getEvaluatorDescriptor(), new IllegalQueryInstanceAccessException(managedInstance.getQueryInstance().getQueryInstanceDescriptor(), "Evaluators can only access query instances positioned after the current query in the flow"));
									}
									
									managedQueryInstance = managedInstance;
									
									break outer;
								}
								
								queryIndex++;
							}
							
							stepIndex++;
						}
						
						if (managedQueryInstance.getEvaluators() != null) {
							
							boolean skipUntilCallingEvaluator = queryInstance == managedQueryInstance.getQueryInstance();
							
							for (Evaluator triggeredEvaluator : managedQueryInstance.getEvaluators()) {
								
								if (skipUntilCallingEvaluator) {
									
									if (evaluator == triggeredEvaluator) {
										skipUntilCallingEvaluator = false;
									}
									
									continue;
								}
								
								if (!triggeredEvaluator.getEvaluatorDescriptor().isEnabled()) {
									
									continue;
								}
								
								ManagedEvaluationCallback generatedEvalutionCallback = new ManagedEvaluationCallback(flowInstance, managedSteps, stepIndex, queryIndex, evaluationHandler, user, resolvedPoster, siteProfile, requestMetadata, req);
								
								evaluate(managedQueryInstance.getQueryInstance(), triggeredEvaluator, user, resolvedPoster, generatedEvalutionCallback, evaluationHandler, hasValidationErrors, queryModifications, resetHiddenInstances, siteProfile, requestMetadata, req);
							}
						}
					}
				}
			}
			
		} catch (RuntimeException e) {
			
			throw new EvaluationException(evaluator.getEvaluatorDescriptor(), e);
		}
	}

	private ManagedQueryInstance getQueryInCurrentStep(QueryInstance queryInstance) {

		for(ManagedQueryInstance managedInstance : managedSteps.get(currentStepIndex).getManagedQueryInstances()){

			if(managedInstance.getQueryInstance().equals(queryInstance)){

				return managedInstance;
			}
		}

		return null;
	}

	public synchronized ManagerResponse populateCurrentStep(HttpServletRequest req, User user, User poster, FlowDirection flowDirection, QueryHandler queryHandler, EvaluationHandler evaluationHandler, String baseQueryRequestURL, RequestMetadata requestMetadata, SiteProfile siteProfile) throws UnableToPopulateQueryInstanceException, UnableToResetQueryInstanceException, UnableToGetQueryInstanceFormHTMLException, FlowInstanceManagerClosedException, EvaluationException {

		checkState();

		boolean allowPartialPopulation;

		if(flowDirection == FlowDirection.BACKWARD || flowDirection == FlowDirection.STAY_AND_POPULATE_PARTIALLY){

			//We are going backwards from the last non-populated step, allow partial population
			allowPartialPopulation = true;

		}else{

			allowPartialPopulation = false;
		}

		HashMap<Integer, List<ValidationError>> validationErrorMap = new HashMap<Integer, List<ValidationError>>();

		User resolvedPoster = getPoster(poster);
		
		ManagedEvaluationCallback evaluationCallback = new ManagedEvaluationCallback(flowInstance, managedSteps, currentStepIndex, 0, evaluationHandler, user, resolvedPoster, siteProfile, requestMetadata, req);

		//Iterate over all questions in the current step and populate them
		for(ManagedQueryInstance managedQueryInstance : managedSteps.get(currentStepIndex).getManagedQueryInstances()){

			QueryInstance queryInstance = managedQueryInstance.getQueryInstance();

			try{
				if(queryInstance.getQueryInstanceDescriptor().getQueryState() != QueryState.HIDDEN){

					try{
						queryInstance.populate(req, user, resolvedPoster, allowPartialPopulation, queryHandler, flowInstance.getAttributeHandler(), requestMetadata);

					}catch(RuntimeException e){
						throw new UnableToPopulateQueryInstanceException(queryInstance.getQueryInstanceDescriptor(), e);
					}

				}else{

					try{
						queryInstance.reset(flowInstance.getAttributeHandler());

					}catch(RuntimeException e){
						
						throw new UnableToResetQueryInstanceException(queryInstance.getQueryInstanceDescriptor(), e);
					}
				}

			}catch(ValidationException e){
				validationErrorMap.put(queryInstance.getQueryInstanceDescriptor().getQueryDescriptor().getQueryID(), e.getErrors());
			}

			if(managedQueryInstance.getEvaluators() != null){

				for(Evaluator evaluator : managedQueryInstance.getEvaluators()){

					if(!evaluator.getEvaluatorDescriptor().isEnabled()){

						continue;
					}

					try{
						evaluate(queryInstance, evaluator, user, resolvedPoster, evaluationCallback, evaluationHandler, validationErrorMap.get(queryInstance.getQueryInstanceDescriptor().getQueryDescriptor().getQueryID()) != null, null, siteProfile, requestMetadata, req);
						
					}catch(RuntimeException e){

						throw new EvaluationException(evaluator.getEvaluatorDescriptor(),e);
					}
				}
			}

			evaluationCallback.incrementQueryIndex();
		}

		setUnsavedChanges();

		//If we have any validation errors stay in the current step, else follow the requested flow direction
		if(!validationErrorMap.isEmpty()){

			ManagedStep currentStep = managedSteps.get(currentStepIndex);

			ArrayList<QueryResponse> queryResponses = new ArrayList<QueryResponse>(currentStep.getManagedQueryInstances().size());

			for(ManagedQueryInstance managedQueryInstance : currentStep.getManagedQueryInstances()){

				QueryInstance queryInstance = managedQueryInstance.getQueryInstance();

				if(queryInstance.getQueryInstanceDescriptor().getQueryState() != QueryState.HIDDEN){

					try{
						queryResponses.add(queryInstance.getFormHTML(req, user, resolvedPoster, validationErrorMap.get(queryInstance.getQueryInstanceDescriptor().getQueryDescriptor().getQueryID()), queryHandler, requiresAjaxPosting(managedQueryInstance, currentStep), getQueryRequestURL(managedQueryInstance, baseQueryRequestURL), requestMetadata, flowInstance.getAttributeHandler()));
					}catch(Throwable e){
						throw new UnableToGetQueryInstanceFormHTMLException(queryInstance.getQueryInstanceDescriptor(), e);
					}
				} else {
					queryResponses.add(new QueryResponse(managedQueryInstance.getQueryInstance().getQueryInstanceDescriptor().getQueryDescriptor()));
				}
			}

			flowInstance.setFullyPopulated(false);

			return new ManagerResponse(currentStep.getStep().getStepID(), currentStepIndex, queryResponses, true, concurrentModificationLock);
		}

		if(flowDirection == FlowDirection.STAY_AND_POPULATE_FULLY){

			if(currentStepIndex == (managedSteps.size() - 1)){

				flowInstance.setFullyPopulated(true);
			}

		}else if(flowDirection == FlowDirection.FORWARD){

			if(currentStepIndex < (managedSteps.size() - 1)){

				currentStepIndex++;
				flowInstance.setFullyPopulated(false);

			}else{

				flowInstance.setFullyPopulated(true);
			}

		}else if(flowDirection == FlowDirection.BACKWARD && currentStepIndex > 0){

			currentStepIndex--;
			flowInstance.setFullyPopulated(false);
		}

		flowInstance.setStepID(managedSteps.get(currentStepIndex).getStep().getStepID());

		return getCurrentStepFormHTML(queryHandler, req, user, poster, baseQueryRequestURL, requestMetadata);
	}

	public synchronized Timestamp saveInstance(FlowEngineInterface flowEngineInterface, User user, User poster, EventType eventType, Timestamp saveTimestamp) throws SQLException, UnableToSaveQueryInstanceException, FlowInstanceManagerClosedException {

		checkState();

		//Start transaction
		TransactionHandler transactionHandler = null;

		boolean isAdd = false;
		boolean setFirstSubmitted = false;

		try{
			FlowEngineDAOFactory daoFactory = flowEngineInterface.getDAOFactory();

			transactionHandler = daoFactory.getTransactionHandler();

			if(saveTimestamp == null) {
				
				saveTimestamp = TimeUtils.getCurrentTimestamp(false);
			}
			
			if(flowInstance.getFirstSubmitted() == null && eventType == EventType.SUBMITTED){
				
				flowInstance.setFirstSubmitted(saveTimestamp);
				setFirstSubmitted = true;
			}
			
			if (eventType == EventType.SUBMITTED || eventType == EventType.UPDATED) {
				
				FlowInstanceUtils.setContactAttributes(this, flowInstance.getAttributeHandler());
			}

			FlowInstanceUtils.setDescriptions(flowInstance);
			
			if (flowInstance.getFlowInstanceID() == null) {
				
				isAdd = true;
				
				flowInstance.setPoster(poster);
				flowInstance.setAdded(saveTimestamp);
				
				if (poster != null) {
					
					if (flowInstance.getOwners() == null) {
						
						flowInstance.setOwners(new ArrayList<User>(1));
					}
					
					if (!flowInstance.getOwners().contains(poster)) {
						
						flowInstance.getOwners().add(poster);
					}
				}
				
				//Add flow instance to database
				daoFactory.getFlowInstanceDAO().add(flowInstance, transactionHandler, FLOW_INSTANCE_ADD_RELATIONS);
				
				for (ManagedStep managedStep : this.managedSteps) {
					
					for (ManagedQueryInstance managedQueryInstance : managedStep.getManagedQueryInstances()) {
						
						//Less nice cast for now...
						QueryInstanceDescriptor queryInstanceDescriptor = (QueryInstanceDescriptor) managedQueryInstance.getQueryInstance().getQueryInstanceDescriptor();
						
						//Set flowInstanceID on QueryInstanceDescriptor
						queryInstanceDescriptor.setFlowInstanceID(flowInstance.getFlowInstanceID());
						
						//Add QueryInstanceDescriptor
						daoFactory.getQueryInstanceDescriptorDAO().add(queryInstanceDescriptor, transactionHandler, null);
					}
				}
				
			} else {
				
				flowInstance.setUpdated(saveTimestamp);
				flowInstance.setEditor(user);
				
				//Update flow instance
				daoFactory.getFlowInstanceDAO().update(flowInstance, transactionHandler, FLOW_INSTANCE_UPDATE_RELATIONS);
				
				//Update all query instance descriptors
				for (ManagedStep managedStep : this.managedSteps) {
					
					for (ManagedQueryInstance managedQueryInstance : managedStep.getManagedQueryInstances()) {
						
						//Less nice cast for now...
						QueryInstanceDescriptor queryInstanceDescriptor = (QueryInstanceDescriptor) managedQueryInstance.getQueryInstance().getQueryInstanceDescriptor();
						
						//Update QueryInstanceDescriptor
						daoFactory.getQueryInstanceDescriptorDAO().update(queryInstanceDescriptor, transactionHandler, FLOW_INSTANCE_UPDATE_RELATIONS);
					}
				}
			}

			//Loop over each step
			for(ManagedStep managedStep : managedSteps){

				//Call save on each query instance
				for(ManagedQueryInstance managedQueryInstance : managedStep.getManagedQueryInstances()){

					try{
						managedQueryInstance.getQueryInstance().save(transactionHandler, flowEngineInterface.getQueryHandler());
					}catch(Throwable e){
						throw new UnableToSaveQueryInstanceException(managedQueryInstance.getQueryInstance().getQueryInstanceDescriptor(), e);
					}
				}
			}
			
			//Commit transaction
			transactionHandler.commit();

			hasUnsavedChanges = false;
			
			return saveTimestamp;
			
		}finally{

			if(setFirstSubmitted && !transactionHandler.isCommited()){
				
				flowInstance.setFirstSubmitted(null);
			}
			
			//Clear all autogenerated ID's if add operation fails
			if(isAdd && !transactionHandler.isCommited()){

				flowInstance.setFlowInstanceID(null);

				for(ManagedStep managedStep : this.managedSteps){

					for(ManagedQueryInstance managedQueryInstance : managedStep.getManagedQueryInstances()){

						//Less nice cast for now...
						QueryInstanceDescriptor queryInstanceDescriptor = (QueryInstanceDescriptor)managedQueryInstance.getQueryInstance().getQueryInstanceDescriptor();

						//Clear flowInstanceID on QueryInstanceDescriptor
						queryInstanceDescriptor.setQueryInstanceID(null);
						
						//TODO call each query instance to reset ID's
					}
				}
			}

			TransactionHandler.autoClose(transactionHandler);
		}
	}

	public SubmitCheckFailedResponse checkValidForSubmit(User poster, QueryHandler queryHandler, String baseUpdateURL, RequestMetadata requestMetadata) throws SubmitCheckException {
		
		User resolvedPoster = getPoster(poster);
		
		ManagedQueryInstance blockingQueryInstance = null;
		ManagedStep blockingStep = null;
		
		outer: for (ManagedStep step : managedSteps) {
			for (ManagedQueryInstance managedQueryInstance : step.getManagedQueryInstances()) {
				
				QueryInstance queryInstance = managedQueryInstance.getQueryInstance();
				
				if (queryInstance.getQueryInstanceDescriptor().getQueryState() != QueryState.HIDDEN && queryInstance.getQueryInstanceDescriptor().isPopulated()) {
					
					if (queryInstance instanceof SubmitCheck) {
						
						try {
							if (!((SubmitCheck) queryInstance).isValidForSubmit(flowInstance.getAttributeHandler(), resolvedPoster, queryHandler)) {
								
								blockingQueryInstance = managedQueryInstance;
								blockingStep = step;
								break outer;
							}
						} catch (Exception e) {
							
							throw new SubmitCheckException(queryInstance.getQueryInstanceDescriptor(), e);
						}
					}
					
//					if (managedQueryInstance.getEvaluators() != null) {
//
//						for (Evaluator evaluator : managedQueryInstance.getEvaluators()) {
//
//							if (evaluator.getEvaluatorDescriptor().isEnabled() && evaluator instanceof SubmitCheck) {
//
//								if (!((SubmitCheck) queryInstance).isValidForSubmit(poster, queryHandler)) {
//
//									blockingQueryInstance = managedQueryInstance;
//									blockingStep = step;
//									break outer;
//								}
//							}
//						}
//					}
				}
			}
		}
		
		if (blockingQueryInstance == null) {
			return null;
		}
		
		flowInstance.setFullyPopulated(false);
		currentStepIndex = managedSteps.indexOf(blockingStep);
		
		String redirectURL = baseUpdateURL + "?step=" + blockingStep.getStep().getStepID() + "#query_" + blockingQueryInstance.getQueryInstance().getQueryInstanceDescriptor().getQueryDescriptor().getQueryID();
		
		return new SubmitCheckFailedResponse(blockingQueryInstance.getQueryInstance(), redirectURL);
	}
	
	public synchronized void close(QueryHandler queryHandler) {
		
		if (closed) {
			
			return;
		}

		for(ManagedStep step : managedSteps){

			for(ManagedQueryInstance managedQueryInstance : step.getManagedQueryInstances()){

				managedQueryInstance.getQueryInstance().close(queryHandler);
			}
		}

		this.closed = true;
	}

	public synchronized void checkState() throws FlowInstanceManagerClosedException {

		if (closed) {

			throw new FlowInstanceManagerClosedException(this.getFlowInstance(), this.instanceManagerID);
		}
	}

	public boolean setStep(Integer stepID){

		if(!isFullyPopulated()){

			return false;
		}

		int index = 0;

		while(index < this.managedSteps.size()){

			if(managedSteps.get(index).getStep().getStepID().equals(stepID)){

				if(isFullyPopulated() || index <= currentStepIndex){

					this.currentStepIndex = index;

					flowInstance.setFullyPopulated(false);

					return true;

				}else{

					return false;
				}
			}

			index++;
		}

		return false;
	}

	public boolean isPreviouslySaved() {

		return this.flowInstance.getFlowInstanceID() != null;
	}

	@Override
	public Integer getFlowInstanceID() {

		return flowInstance.getFlowInstanceID();
	}

	@Override
	public Integer getFlowID() {

		return flowInstance.getFlow().getFlowID();
	}

	@Override
	public Status getFlowState() {

		return flowInstance.getStatus();
	}

	public synchronized void setFlowState(Status flowState) {

		if(flowInstance.getStatus() == null || !flowInstance.getStatus().equals(flowState)) {
			flowInstance.setLastStatusChange(TimeUtils.getCurrentTimestamp(false));
		}

		flowInstance.setStatus(flowState);

	}

	public boolean isFullyPopulated() {

		return flowInstance.isFullyPopulated();
	}

	@Override
	public ImmutableFlowInstance getFlowInstance() {

		return flowInstance;
	}

	public boolean isClosed() {

		return closed;
	}

	public String getInstanceManagerID() {

		return instanceManagerID;
	}

	public long getCreated() {

		return created;
	}

	@Override
	public void valueBound(HttpSessionBindingEvent sessionBindingEvent) {

		this.registery.addSessionBoundInstance(this);
	}

	@Override
	public void valueUnbound(HttpSessionBindingEvent sessionBindingEvent) {

		this.registery.removeSessionBoundInstance(this);
	}

	public ImmutableStep getCurrentStep() {

		return this.managedSteps.get(currentStepIndex).getStep();
	}

	public Integer getCurrentStepIndex() {

		return currentStepIndex;
	}

	@Override
	public String toString(){

		return flowInstance.toString();
	}

	@Override
	public ImmutableQueryInstance getQueryInstance(int queryID) {

		for(ManagedStep managedStep : this.managedSteps){

			for(ManagedQueryInstance managedQueryInstance : managedStep.getManagedQueryInstances()){

				if(managedQueryInstance.getQueryInstance().getQueryInstanceDescriptor().getQueryDescriptor().getQueryID() == queryID){

					return managedQueryInstance.getQueryInstance();
				}
			}
		}

		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends ImmutableQueryInstance> T getQuery(Class<T> queryInstanceClass) {

		for(ManagedStep managedStep : this.managedSteps){

			for(ManagedQueryInstance managedQueryInstance : managedStep.getManagedQueryInstances()){

				if(queryInstanceClass.isAssignableFrom(managedQueryInstance.getQueryInstance().getClass())){

					return (T)managedQueryInstance.getQueryInstance();
				}
			}
		}

		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends ImmutableQueryInstance> T getQuery(Class<T> queryInstanceClass, String name) {

		for(ManagedStep managedStep : this.managedSteps){

			for(ManagedQueryInstance managedQueryInstance : managedStep.getManagedQueryInstances()){

				if(queryInstanceClass.isAssignableFrom(managedQueryInstance.getQueryInstance().getClass()) && managedQueryInstance.getQueryInstance().getQueryInstanceDescriptor().getQueryDescriptor().getName().equals(name)){

					return (T)managedQueryInstance.getQueryInstance();
				}
			}
		}

		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> List<T> getQueries(Class<T> queryInstanceClass) {

		List<T> queryList = new ArrayList<T>();

		for(ManagedStep managedStep : this.managedSteps){

			for(ManagedQueryInstance managedQueryInstance : managedStep.getManagedQueryInstances()){

				if(queryInstanceClass.isAssignableFrom(managedQueryInstance.getQueryInstance().getClass())){

					queryList.add((T) managedQueryInstance.getQueryInstance());
				}
			}
		}

		if(queryList.isEmpty()){

			return null;
		}

		return queryList;
	}

	@Override
	public List<PDFManagerResponse> getPDFContent(ImmutableFlowEngineInterface flowEngineInterface) throws FlowInstanceManagerClosedException, UnableToGetQueryInstancePDFContentException {

		List<PDFManagerResponse> managerResponses = new ArrayList<PDFManagerResponse>(this.managedSteps.size());

		for(int stepIndex=0; stepIndex < this.managedSteps.size(); stepIndex++){

			ManagedStep managedStep = managedSteps.get(stepIndex);

			ArrayList<PDFQueryResponse> queryResponses = new ArrayList<PDFQueryResponse>(managedStep.getManagedQueryInstances().size());

			for(ManagedQueryInstance managedQueryInstance : managedStep.getManagedQueryInstances()){

				MutableQueryInstanceDescriptor queryInstanceDescriptor = managedQueryInstance.getQueryInstance().getQueryInstanceDescriptor();

				if(queryInstanceDescriptor.getQueryState() != QueryState.HIDDEN && queryInstanceDescriptor.isPopulated()){

					try{
						queryResponses.add(managedQueryInstance.getQueryInstance().getPDFContent(flowEngineInterface.getQueryHandler(), flowInstance.getAttributeHandler()));
					}catch(Throwable e){
						throw new UnableToGetQueryInstancePDFContentException(queryInstanceDescriptor, e);
					}
				}
			}

			managerResponses.add(new PDFManagerResponse(managedStep.getStep().getStepID(), stepIndex, queryResponses));
		}

		return managerResponses;
	}

	public boolean hasUnsavedChanges() {

		return hasUnsavedChanges;
	}
	
	private void setUnsavedChanges(){
		
		hasUnsavedChanges = true;
		changesCounter++;
	}
	
	public int getChangesCounter() {
		
		return changesCounter;
	}
	
	public boolean isConcurrentModificationLocked() {

		return concurrentModificationLock;
	}

	public void setConcurrentModificationLock(boolean concurrentModificationLock) {

		this.concurrentModificationLock = concurrentModificationLock;
	}

	@Override
	public List<Element> getExportXMLElements(Document doc, QueryHandler queryHandler) throws Exception {

		List<Element> elements = new ArrayList<Element>(this.managedSteps.size() * 5);

		for(ManagedStep managedStep : this.managedSteps){

			for(ManagedQueryInstance managedQueryInstance : managedStep.getManagedQueryInstances()){

				if(managedQueryInstance.getQueryInstance().getQueryInstanceDescriptor().getQueryDescriptor().isExported() && managedQueryInstance.getQueryInstance().getQueryInstanceDescriptor().isPopulated()){

					Element queryElement = managedQueryInstance.getQueryInstance().toExportXML(doc, queryHandler);

					if(queryElement != null){

						elements.add(queryElement);
					}
				}
			}
		}

		return elements;
	}

	public User getPoster(User poster) {

		if (flowInstance.getPoster() != null || flowInstance.getFirstSubmitted() != null) {

			return flowInstance.getPoster();
		}

		return poster;
	}

	@Override
	public synchronized MutableAttributeHandler getSessionAttributeHandler() {
		
		if (sessionAttributeHandler == null) {
			sessionAttributeHandler = new SimpleMutableAttributeHandler(255, 1024);
		}
		
		return sessionAttributeHandler;
	}
}
