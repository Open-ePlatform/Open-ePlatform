package com.nordicpeak.flowengine.infomodule;

import it.sauronsoftware.cron4j.Scheduler;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactoryConfigurationError;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import se.unlogic.hierarchy.core.annotations.InstanceManagerDependency;
import se.unlogic.hierarchy.core.annotations.ModuleSetting;
import se.unlogic.hierarchy.core.annotations.TextFieldSettingDescriptor;
import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.hierarchy.core.enums.EventSource;
import se.unlogic.hierarchy.core.events.CRUDEvent;
import se.unlogic.hierarchy.core.exceptions.URINotFoundException;
import se.unlogic.hierarchy.core.interfaces.EventListener;
import se.unlogic.hierarchy.core.interfaces.ForegroundModuleDescriptor;
import se.unlogic.hierarchy.core.interfaces.SectionInterface;
import se.unlogic.hierarchy.foregroundmodules.rest.AnnotatedRESTModule;
import se.unlogic.hierarchy.foregroundmodules.rest.RESTMethod;
import se.unlogic.hierarchy.foregroundmodules.rest.URIParam;
import se.unlogic.standardutils.collections.CollectionUtils;
import se.unlogic.standardutils.dao.AnnotatedDAO;
import se.unlogic.standardutils.dao.SimpleAnnotatedDAOFactory;
import se.unlogic.standardutils.json.JsonArray;
import se.unlogic.standardutils.json.JsonObject;
import se.unlogic.standardutils.string.StringUtils;
import se.unlogic.standardutils.xml.XMLUtils;
import se.unlogic.webutils.http.RequestUtils;
import se.unlogic.webutils.http.URIParser;

import com.nordicpeak.flowengine.FlowBrowserModule;
import com.nordicpeak.flowengine.PopularFlowFamiliesModule;
import com.nordicpeak.flowengine.beans.Flow;
import com.nordicpeak.flowengine.beans.FlowType;

public class FlowInfoModule extends AnnotatedRESTModule implements EventListener<CRUDEvent<Flow>>, Runnable {

	@ModuleSetting
	@TextFieldSettingDescriptor(name = "Intervall size", description = "Controls how any hours back in time that the statistics should be based on")
	private int interval = 72;

	@ModuleSetting
	@TextFieldSettingDescriptor(name = "Flow count", description = "Controls how many flows this module should display")
	private int flowCount = 100;

	private List<Flow> popularFlows;

	private Scheduler scheduler;

	@InstanceManagerDependency
	private FlowBrowserModule flowBrowserModule;

	private AnnotatedDAO<Flow> flowDAO;

	@SuppressWarnings("unchecked")
	@Override
	public void init(ForegroundModuleDescriptor descriptor, SectionInterface sectionInterface, DataSource dataSource) throws Exception {

		super.init(descriptor, sectionInterface, dataSource);

		cacheFlows();

		systemInterface.getEventHandler().addEventListener(CRUDEvent.class, this, Flow.class);

		scheduler = new Scheduler();
		scheduler.schedule("0 * * * *", this);
		scheduler.start();
	}

	@Override
	public void update(ForegroundModuleDescriptor descriptor, DataSource dataSource) throws Exception {

		super.update(descriptor, dataSource);

		cacheFlows();
	}

	@SuppressWarnings("unchecked")
	@Override
	public void unload() throws Exception {

		try {
			scheduler.stop();
		} catch (IllegalStateException e) {
			log.error("Error stopping scheduler", e);
		}

		systemInterface.getEventHandler().removeEventListener(Flow.class, CRUDEvent.class, this);

		super.unload();
	}

	@Override
	protected void createDAOs(DataSource dataSource) throws Exception {

		SimpleAnnotatedDAOFactory daoFactory = new SimpleAnnotatedDAOFactory(dataSource);
		flowDAO = daoFactory.getDAO(Flow.class);
	}

	private void cacheFlows() {

		popularFlows = PopularFlowFamiliesModule.cacheFlows(dataSource, flowDAO, log, interval, flowCount);
	}

	@Override
	public void processEvent(CRUDEvent<Flow> event, EventSource source) {

		cacheFlows();
	}

	@Override
	public void run() {

		cacheFlows();
	}

	@Override
	public int getPriority() {

		return 0;
	}

	@RESTMethod(alias = "getcategories/{responseType}", method = "get")
	public void getCategories(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser, @URIParam(name = "responseType") String responseType) throws Throwable {

		Collection<Flow> flows = flowBrowserModule.getLatestPublishedFlowVersions();

		Set<FlowType> flowTypes = null;

		if (!CollectionUtils.isEmpty(flows)) {

			flowTypes = new HashSet<FlowType>();

			for (Flow flow : flows) {
				flowTypes.add(flow.getFlowType());
			}
		}

		getCategoriesResponse(req, res, responseType, flowTypes, uriParser);
	}

	private void getCategoriesResponse(HttpServletRequest req, HttpServletResponse res, String responseType, Collection<FlowType> flowTypes, URIParser uriParser) throws TransformerFactoryConfigurationError, TransformerException, IOException, URINotFoundException {

		if (responseType.equals("xml")) {

			res.setContentType("text/xml");

			XMLUtils.writeXML(getXMLResponse(flowTypes), res.getOutputStream(), true, sectionInterface.getSystemInterface().getEncoding());

		} else if (responseType.equals("json")) {

			res.setContentType("application/json");

			res.getWriter().write(getJsonResponse(flowTypes).toJson());
			res.getWriter().flush();

		} else if (responseType.equals("jsonp")) {

			res.setContentType("application/javascript");

			res.getWriter().write(getJsonpCallback(req, uriParser) + "(" + getJsonResponse(flowTypes).toJson() + ");");
			res.getWriter().flush();

		} else {

			// Invalid requested response type
			throw new URINotFoundException(uriParser);
		}
	}

	private Document getXMLResponse(Collection<FlowType> flowTypes) {

		Document doc = XMLUtils.createDomDocument();
		Element flowsElement = doc.createElement("Categories");
		doc.appendChild(flowsElement);

		if (!CollectionUtils.isEmpty(flowTypes)) {

			for (FlowType flow : flowTypes) {

				Element flowElement = XMLUtils.appendNewElement(doc, flowsElement, "Category");

				XMLUtils.appendNewElement(doc, flowElement, "ID", flow.getFlowTypeID());
				XMLUtils.appendNewElement(doc, flowElement, "Name", flow.getName());

				flowsElement.appendChild(flowElement);
			}
		}

		return doc;
	}

	private JsonObject getJsonResponse(Collection<FlowType> flowTypes) {

		JsonObject jsonObject = new JsonObject(1);

		JsonArray array = new JsonArray();

		if (!CollectionUtils.isEmpty(flowTypes)) {
			for (FlowType flow : flowTypes) {

				JsonObject flowsJson = new JsonObject(8);

				flowsJson.putField("ID", flow.getFlowTypeID());
				flowsJson.putField("Name", flow.getName());

				array.addNode(flowsJson);
			}
		}

		jsonObject.putField("Categories", array);

		return jsonObject;
	}

	@RESTMethod(alias = "getflow/{flowID}/{responseType}", method = "get")
	public void getFlow(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser, @URIParam(name = "responseType") String responseType, @URIParam(name = "flowID") Integer flowID) throws Throwable {

		log.info("User " + user + " requested flowID " + flowID + " as " + StringUtils.toLogFormat(responseType, 30));

		Collection<Flow> flows = flowBrowserModule.getLatestPublishedFlowVersions();

		Flow requestedFlow = getFlow(flowID, flows);

		if (requestedFlow != null) {

			getResponse(req, res, responseType, Collections.singletonList(requestedFlow), uriParser);

		} else {

			getResponse(req, res, responseType, null, uriParser);
		}
	}

	private Flow getFlow(Integer flowID, Collection<Flow> flows) {

		for (Flow flow : flows) {

			if (flow.getFlowID().equals(flowID)) {

				return flow;
			}
		}

		return null;
	}

	@RESTMethod(alias = "getflows/{responseType}", method = "get")
	public void getFlows(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser, @URIParam(name = "responseType") String responseType) throws Throwable {

		log.info("User " + user + " requested flows as " + StringUtils.toLogFormat(responseType, 30));

		Collection<Flow> flows = flowBrowserModule.getLatestPublishedFlowVersions();

		getResponse(req, res, responseType, flows, uriParser);
	}

	@RESTMethod(alias = "search/{responseType}", method = "get")
	public void search(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser, @URIParam(name = "responseType") String responseType) throws Throwable {

		String query = req.getParameter("q");

		log.info("User " + user + " requested flows matching query " + StringUtils.toLogFormat(query, 30) + " as " + StringUtils.toLogFormat(responseType, 30));

		List<Flow> flowHits = null;

		if (query != null) {

			if (req.getCharacterEncoding() != null) {

				try {
					query = URLDecoder.decode(query, req.getCharacterEncoding());
				} catch (UnsupportedEncodingException e) {
					log.warn("Unsupported character set on request from address " + req.getRemoteHost() + ", skipping decoding of query parameter");
				}

			}

			if (flowBrowserModule.getFlowIndexer() != null) {

				List<Integer> hits = flowBrowserModule.getFlowIndexer().search(query);

				if (hits != null) {

					Collection<Flow> flows = flowBrowserModule.getLatestPublishedFlowVersions();

					flowHits = new ArrayList<Flow>(hits.size());

					for (Integer flowID : hits) {

						Flow flow = getFlow(flowID, flows);

						if (flow != null) {

							flowHits.add(flow);
						}
					}
				}

			}
		}

		getResponse(req, res, responseType, flowHits, uriParser);
	}

	@RESTMethod(alias = "getpopularflows/{resultCount}/{responseType}", method = "get")
	public void getPopularFlows(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser, @URIParam(name = "resultCount") Integer resultCount, @URIParam(name = "responseType") String responseType) throws Throwable {

		if (resultCount < 0) {

			// Negative resultCount
			throw new URINotFoundException(uriParser);
		}

		log.info("User " + user + " requested popular flows with max count of " + resultCount + " as " + StringUtils.toLogFormat(responseType, 30));

		if (popularFlows != null) {

			if (resultCount > popularFlows.size()) {

				resultCount = popularFlows.size();
			}

			getResponse(req, res, responseType, popularFlows.subList(0, resultCount), uriParser);

		} else {

			getResponse(req, res, responseType, null, uriParser);
		}
	}

	@RESTMethod(alias = "getflowsincategory/{categoryID}/{responseType}", method = "get")
	public void getFlowsInCategory(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser, @URIParam(name = "responseType") String responseType, @URIParam(name = "categoryID") Integer categoryID) throws Throwable {

		Collection<Flow> flows = flowBrowserModule.getLatestPublishedFlowVersions();
		List<Flow> flowsInCategory = null;

		log.info("User " + user + " requested flows in categoryID " + categoryID + " as " + StringUtils.toLogFormat(responseType, 30));

		if (!CollectionUtils.isEmpty(flows)) {

			flowsInCategory = new ArrayList<Flow>();

			for (Flow flow : flows) {
				if (flow.getFlowType().getFlowTypeID().equals(categoryID)) {
					flowsInCategory.add(flow);
				}
			}
		}

		getResponse(req, res, responseType, flowsInCategory, uriParser);
	}

	private String getFlowURL(HttpServletRequest req, Flow flow) {

		return RequestUtils.getFullContextPathURL(req) + flowBrowserModule.getFullAlias() + "/flowoverview/" + flow.getFlowID();
	}

	private String getIconURL(HttpServletRequest req, Flow flow) {

		return RequestUtils.getFullContextPathURL(req) + flowBrowserModule.getFullAlias() + "/icon/" + flow.getFlowID();
	}

	private void getResponse(HttpServletRequest req, HttpServletResponse res, String responseType, Collection<Flow> flows, URIParser uriParser) throws TransformerFactoryConfigurationError, TransformerException, IOException, URINotFoundException {

		if (responseType.equals("xml")) {

			res.setContentType("text/xml");

			XMLUtils.writeXML(getXMLResponse(req, flows), res.getOutputStream(), true, sectionInterface.getSystemInterface().getEncoding());

		} else if (responseType.equals("json")) {

			res.setContentType("application/json");

			res.getWriter().write(getJsonResponse(req, flows).toJson());
			res.getWriter().flush();

		} else if (responseType.equals("jsonp")) {

			res.setContentType("application/javascript");

			res.getWriter().write(getJsonpCallback(req, uriParser) + "(" + getJsonResponse(req, flows).toJson() + ");");
			res.getWriter().flush();

		} else {

			// Invalid requested response type
			throw new URINotFoundException(uriParser);
		}
	}

	private Document getXMLResponse(HttpServletRequest req, Collection<Flow> flows) {

		Document doc = XMLUtils.createDomDocument();
		Element flowsElement = doc.createElement("Flows");
		doc.appendChild(flowsElement);

		if (!CollectionUtils.isEmpty(flows)) {
			for (Flow flow : flows) {

				Element flowElement = XMLUtils.appendNewElement(doc, flowsElement, "Flow");

				XMLUtils.appendNewElement(doc, flowElement, "ID", flow.getFlowID());
				XMLUtils.appendNewElement(doc, flowElement, "Name", flow.getName());
				XMLUtils.appendNewElement(doc, flowElement, "URL", getFlowURL(req, flow));
				XMLUtils.appendNewElement(doc, flowElement, "Category", flow.getFlowType().getName());
				XMLUtils.appendNewElement(doc, flowElement, "CategoryID", flow.getFlowType().getFlowTypeID());
				XMLUtils.appendNewElement(doc, flowElement, "Icon", getIconURL(req, flow));
				XMLUtils.appendNewElement(doc, flowElement, "ShortDescription", flow.getShortDescription());
				XMLUtils.appendNewElement(doc, flowElement, "LongDescription", flow.getLongDescription());
				XMLUtils.appendNewElement(doc, flowElement, "RequiresAuthentication", flow.requiresAuthentication());
				XMLUtils.appendNewElement(doc, flowElement, "RequiresSigning", flow.requiresSigning());

				flowsElement.appendChild(flowElement);
			}
		}

		return doc;
	}

	private JsonObject getJsonResponse(HttpServletRequest req, Collection<Flow> flows) {

		JsonObject jsonObject = new JsonObject(1);

		JsonArray array = new JsonArray();

		if (!CollectionUtils.isEmpty(flows)) {
			for (Flow flow : flows) {

				JsonObject flowsJson = new JsonObject(8);

				flowsJson.putField("ID", flow.getFlowID());
				flowsJson.putField("Name", flow.getName());
				flowsJson.putField("URL", getFlowURL(req, flow));
				flowsJson.putField("Category", flow.getFlowType().getName());
				flowsJson.putField("CategoryID", flow.getFlowType().getFlowTypeID());
				flowsJson.putField("Icon", getIconURL(req, flow));
				flowsJson.putField("ShortDescription", flow.getShortDescription());
				flowsJson.putField("LongDescription", flow.getLongDescription());
				flowsJson.putField("RequiresAuthentication", flow.requiresAuthentication());
				flowsJson.putField("RequiresSigning", flow.requiresSigning());

				array.addNode(flowsJson);
			}
		}

		jsonObject.putField("Flows", array);

		return jsonObject;
	}

	private String getJsonpCallback(HttpServletRequest req, URIParser uriParser) throws URINotFoundException {

		String callback = req.getParameter("callback");

		if (callback == null) {
			callback = req.getParameter("jsonp");
		}

		if (callback == null) {

			// No callback or jsonp parameter specified
			throw new URINotFoundException(uriParser);
		}

		return callback;
	}

}
