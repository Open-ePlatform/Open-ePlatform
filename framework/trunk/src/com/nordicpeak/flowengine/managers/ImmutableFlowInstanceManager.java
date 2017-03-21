package com.nordicpeak.flowengine.managers;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.hierarchy.core.handlers.SimpleMutableAttributeHandler;
import se.unlogic.hierarchy.core.interfaces.AttributeHandler;
import se.unlogic.hierarchy.core.interfaces.MutableAttributeHandler;
import se.unlogic.hierarchy.core.utils.FCKUtils;
import se.unlogic.standardutils.collections.CollectionUtils;
import se.unlogic.webutils.url.URLRewriter;

import com.nordicpeak.flowengine.beans.FlowInstance;
import com.nordicpeak.flowengine.beans.PDFQueryResponse;
import com.nordicpeak.flowengine.beans.QueryDescriptor;
import com.nordicpeak.flowengine.beans.QueryInstanceDescriptor;
import com.nordicpeak.flowengine.beans.QueryResponse;
import com.nordicpeak.flowengine.beans.RequestMetadata;
import com.nordicpeak.flowengine.beans.Status;
import com.nordicpeak.flowengine.beans.Step;
import com.nordicpeak.flowengine.enums.QueryState;
import com.nordicpeak.flowengine.exceptions.flowinstance.InvalidFlowInstanceStepException;
import com.nordicpeak.flowengine.exceptions.flowinstance.MissingQueryInstanceDescriptor;
import com.nordicpeak.flowengine.exceptions.flowinstancemanager.DuplicateFlowInstanceManagerIDException;
import com.nordicpeak.flowengine.exceptions.flowinstancemanager.FlowInstanceManagerClosedException;
import com.nordicpeak.flowengine.exceptions.queryinstance.UnableToGetQueryInstancePDFContentException;
import com.nordicpeak.flowengine.exceptions.queryinstance.UnableToGetQueryInstanceShowHTMLException;
import com.nordicpeak.flowengine.exceptions.queryprovider.QueryInstanceNotFoundInQueryProviderException;
import com.nordicpeak.flowengine.exceptions.queryprovider.QueryNotFoundInQueryProviderException;
import com.nordicpeak.flowengine.exceptions.queryprovider.QueryProviderErrorException;
import com.nordicpeak.flowengine.exceptions.queryprovider.QueryProviderNotFoundException;
import com.nordicpeak.flowengine.interfaces.ImmutableFlowEngineInterface;
import com.nordicpeak.flowengine.interfaces.ImmutableFlowInstance;
import com.nordicpeak.flowengine.interfaces.ImmutableQueryInstance;
import com.nordicpeak.flowengine.interfaces.InstanceMetadata;
import com.nordicpeak.flowengine.interfaces.QueryHandler;
import com.nordicpeak.flowengine.utils.TextTagReplacer;


public class ImmutableFlowInstanceManager implements Serializable, FlowInstanceManager {

	private static final long serialVersionUID = -12255806392014427L;

	private final FlowInstance flowInstance;
	private final List<ImmutableManagedStep> managedSteps;
	
	private MutableAttributeHandler sessionAttributeHandler;

	/**
	 * Opens an existing flow instance for the given user
	 *
	 * @param flowInstance The flow instance to be loaded, must include it's flow, steps, query descriptors and query instance descriptors relation.
	 * @param instanceMetadata
	 * @throws MissingQueryInstanceDescriptor
	 * @throws QueryNotFoundInQueryProviderException
	 * @throws QueryProviderNotFoundException
	 * @throws InvalidFlowInstanceStepException
	 * @throws QueryProviderErrorException
	 * @throws DuplicateFlowInstanceManagerIDException
	 * @throws QueryInstanceNotFoundInQueryProviderException
	 */
	public ImmutableFlowInstanceManager(FlowInstance flowInstance, QueryHandler queryHandler, HttpServletRequest req, InstanceMetadata instanceMetadata, String absoluteFileURL) throws MissingQueryInstanceDescriptor, QueryProviderNotFoundException, InvalidFlowInstanceStepException, QueryProviderErrorException, DuplicateFlowInstanceManagerIDException, QueryInstanceNotFoundInQueryProviderException {

		this.flowInstance = flowInstance;

		managedSteps = new ArrayList<ImmutableManagedStep>(flowInstance.getFlow().getSteps().size());

		TextTagReplacer.replaceTextTags(flowInstance.getFlow(), instanceMetadata.getSiteProfile());
		
		if(absoluteFileURL != null){
			
			FCKUtils.setAbsoluteFileUrls(flowInstance.getFlow(), absoluteFileURL);
		}
		
		if(req != null){
			
			URLRewriter.setAbsoluteLinkUrls(flowInstance.getFlow(), req);
		}

		for(Step step : flowInstance.getFlow().getSteps()){

			if(step.getQueryDescriptors() == null){

				managedSteps.add(new ImmutableManagedStep(step, new ArrayList<ImmutableQueryInstance>(0)));
				continue;
			}

			List<ImmutableQueryInstance> queryInstances = new ArrayList<ImmutableQueryInstance>(step.getQueryDescriptors().size());

			for(QueryDescriptor queryDescriptor : step.getQueryDescriptors()){

				if(CollectionUtils.isEmpty(queryDescriptor.getQueryInstanceDescriptors())){

					throw new MissingQueryInstanceDescriptor(flowInstance, queryDescriptor);
				}

				QueryInstanceDescriptor queryInstanceDescriptor = queryDescriptor.getQueryInstanceDescriptors().get(0);

				//Reverse bean relations to avoid recursion problems when generating XML
				queryInstanceDescriptor.setQueryDescriptor(queryDescriptor);
				queryDescriptor.setQueryInstanceDescriptors(null);

				queryInstances.add(queryHandler.getImmutableQueryInstance(queryInstanceDescriptor, req, instanceMetadata));
			}

			managedSteps.add(new ImmutableManagedStep(step, queryInstances));
		}
		
		//TODO init
	}

	@Override
	public List<ManagerResponse> getFullShowHTML(HttpServletRequest req, User user, ImmutableFlowEngineInterface flowEngineInterface, boolean onlyPopulatedQueries, String baseUpdateURL, String baseQueryRequestURL, RequestMetadata requestMetadata) throws UnableToGetQueryInstanceShowHTMLException {

		List<ManagerResponse> managerResponses = new ArrayList<ManagerResponse>(this.managedSteps.size());

		for(int stepIndex=0; stepIndex < this.managedSteps.size(); stepIndex++){

			managerResponses.add(getStepShowHTML(stepIndex, req, user, flowEngineInterface, onlyPopulatedQueries, baseUpdateURL, baseQueryRequestURL));
		}

		return managerResponses;
	}

	public ManagerResponse getStepShowHTML(int stepIndex, HttpServletRequest req, User user, ImmutableFlowEngineInterface flowEngineInterface, boolean onlyPopulatedQueries, String baseUpdateURL, String baseQueryRequestURL) throws UnableToGetQueryInstanceShowHTMLException {

		ImmutableManagedStep managedStep = managedSteps.get(stepIndex);

		String stepUpdateURL;

		if(baseUpdateURL == null){

			stepUpdateURL = null;

		}else{

			stepUpdateURL = baseUpdateURL + "?step=" + managedStep.getStep().getStepID();
		}

		ArrayList<QueryResponse> queryResponses = new ArrayList<QueryResponse>(managedStep.getQueryInstances().size());

		AttributeHandler attributeHandler = flowInstance.getAttributeHandler();
		
		for(ImmutableQueryInstance queryInstance : managedStep.getQueryInstances()){

			if(queryInstance.getQueryInstanceDescriptor().getQueryState() != QueryState.HIDDEN && !(onlyPopulatedQueries && !queryInstance.getQueryInstanceDescriptor().isPopulated())){

				try{
					queryResponses.add(queryInstance.getShowHTML(req, user, flowInstance.getPoster(), flowEngineInterface.getQueryHandler(), stepUpdateURL, getQueryRequestURL(queryInstance, baseQueryRequestURL), attributeHandler));
				}catch(Throwable e){
					throw new UnableToGetQueryInstanceShowHTMLException(queryInstance.getQueryInstanceDescriptor(), e);
				}
			}
		}

		return new ManagerResponse(managedStep.getStep().getStepID(), stepIndex, queryResponses, false, false);
	}

	private String getQueryRequestURL(ImmutableQueryInstance queryInstance, String baseQueryRequestURL) {

		return baseQueryRequestURL + queryInstance.getQueryInstanceDescriptor().getQueryDescriptor().getQueryID();
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

	@Override
	public ImmutableFlowInstance getFlowInstance(){

		return flowInstance;
	}

	@Override
	public ImmutableQueryInstance getQueryInstance(int queryID) {

		for(ImmutableManagedStep managedStep : this.managedSteps){

			for(ImmutableQueryInstance queryInstance : managedStep.getQueryInstances()){

				if(queryInstance.getQueryInstanceDescriptor().getQueryDescriptor().getQueryID() == queryID){

					return queryInstance;
				}
			}
		}

		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends ImmutableQueryInstance> T getQuery(Class<T> queryInstanceClass) {

		for(ImmutableManagedStep managedStep : this.managedSteps){

			for(ImmutableQueryInstance queryInstance : managedStep.getQueryInstances()){

				if(queryInstanceClass.isAssignableFrom(queryInstance.getClass())){

					return (T)queryInstance;
				}
			}
		}

		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends ImmutableQueryInstance> T getQuery(Class<T> queryInstanceClass, String name) {

		for(ImmutableManagedStep managedStep : this.managedSteps){

			for(ImmutableQueryInstance queryInstance : managedStep.getQueryInstances()){

				if(queryInstanceClass.isAssignableFrom(queryInstance.getClass()) && queryInstance.getQueryInstanceDescriptor().getQueryDescriptor().getName().equals(name)){

					return (T)queryInstance;
				}
			}
		}

		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> List<T> getQueries(Class<T> queryInstanceClass) {

		List<T> queryList = new ArrayList<T>();

		for(ImmutableManagedStep managedStep : this.managedSteps){

			for(ImmutableQueryInstance queryInstance : managedStep.getQueryInstances()){

				if(queryInstanceClass.isAssignableFrom(queryInstance.getClass())){

					queryList.add((T) queryInstance);
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

			ImmutableManagedStep managedStep = managedSteps.get(stepIndex);

			ArrayList<PDFQueryResponse> queryResponses = new ArrayList<PDFQueryResponse>(managedStep.getQueryInstances().size());

			for(ImmutableQueryInstance queryInstance : managedStep.getQueryInstances()){

				if(queryInstance.getQueryInstanceDescriptor().getQueryState() != QueryState.HIDDEN && queryInstance.getQueryInstanceDescriptor().isPopulated()){

					try{
						queryResponses.add(queryInstance.getPDFContent(flowEngineInterface.getQueryHandler(), flowInstance.getAttributeHandler()));
					}catch(Throwable e){
						throw new UnableToGetQueryInstancePDFContentException(queryInstance.getQueryInstanceDescriptor(), e);
					}
				}
			}

			managerResponses.add(new PDFManagerResponse(managedStep.getStep().getStepID(), stepIndex, queryResponses));
		}

		return managerResponses;
	}

	@Override
	public String toString(){

		return flowInstance.toString();
	}

	@Override
	public List<Element> getExportXMLElements(Document doc, QueryHandler queryHandler) throws Exception {

		List<Element> elements = new ArrayList<Element>(this.managedSteps.size() * 5);

		for(ImmutableManagedStep managedStep : this.managedSteps){

			for(ImmutableQueryInstance queryInstance : managedStep.getQueryInstances()){

				if(queryInstance.getQueryInstanceDescriptor().getQueryDescriptor().isExported() && queryInstance.getQueryInstanceDescriptor().isPopulated()){

					Element queryElement = queryInstance.toExportXML(doc, queryHandler);

					if(queryElement != null){

						elements.add(queryElement);
					}
				}
			}
		}

		return elements;
	}
	
	@Override
	public synchronized MutableAttributeHandler getSessionAttributeHandler() {
		
		if (sessionAttributeHandler == null) {
			sessionAttributeHandler = new SimpleMutableAttributeHandler(255, 1024);
		}
		
		return sessionAttributeHandler;
	}
}
