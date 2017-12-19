package com.nordicpeak.flowengine.managers;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.hierarchy.core.interfaces.attributes.MutableAttributeHandler;

import com.nordicpeak.flowengine.beans.RequestMetadata;
import com.nordicpeak.flowengine.beans.Status;
import com.nordicpeak.flowengine.exceptions.flowinstancemanager.FlowInstanceManagerClosedException;
import com.nordicpeak.flowengine.exceptions.queryinstance.UnableToGetQueryInstancePDFContentException;
import com.nordicpeak.flowengine.exceptions.queryinstance.UnableToGetQueryInstanceShowHTMLException;
import com.nordicpeak.flowengine.interfaces.ImmutableFlowEngineInterface;
import com.nordicpeak.flowengine.interfaces.ImmutableFlowInstance;
import com.nordicpeak.flowengine.interfaces.ImmutableQueryInstance;
import com.nordicpeak.flowengine.interfaces.QueryHandler;

public interface FlowInstanceManager {

	public List<ManagerResponse> getFullShowHTML(HttpServletRequest req, User user, ImmutableFlowEngineInterface flowEngineInterface, boolean onlyPopulatedQueries, String baseUpdateURL, String baseQueryRequestURL, RequestMetadata requestMetadata) throws UnableToGetQueryInstanceShowHTMLException, FlowInstanceManagerClosedException;

	public List<PDFManagerResponse> getPDFContent(ImmutableFlowEngineInterface flowEngineInterface) throws FlowInstanceManagerClosedException, UnableToGetQueryInstancePDFContentException;

	public Integer getFlowInstanceID();

	public Integer getFlowID();

	public Status getFlowState();

	public ImmutableFlowInstance getFlowInstance();

	/**
	 * Gets a query instance by the queryID of it's parent query.
	 *
	 * @param queryID
	 * @return The query instance or null if the query instance was not found
	 */
	public ImmutableQueryInstance getQueryInstance(int queryID);

	public <T extends ImmutableQueryInstance> T getQuery(Class<T> queryInstanceClass);

	public <T> List<T> getQueries(Class<T> queryInstanceClass);

	public <T extends ImmutableQueryInstance> T getQuery(Class<T> queryInstanceClass, String name);

	public List<Element> getExportXMLElements(Document doc, QueryHandler queryHandler) throws Exception;
	
	public MutableAttributeHandler getSessionAttributeHandler();
}