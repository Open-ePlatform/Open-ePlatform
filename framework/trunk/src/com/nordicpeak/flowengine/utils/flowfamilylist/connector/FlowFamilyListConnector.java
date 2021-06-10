package com.nordicpeak.flowengine.utils.flowfamilylist.connector;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.hierarchy.core.interfaces.ForegroundModuleResponse;
import se.unlogic.standardutils.collections.CollectionUtils;
import se.unlogic.standardutils.json.JsonArray;
import se.unlogic.standardutils.json.JsonObject;
import se.unlogic.standardutils.json.JsonUtils;
import se.unlogic.standardutils.string.StringUtils;
import se.unlogic.webutils.http.HTTPUtils;

import com.nordicpeak.flowengine.FlowAdminModule;
import com.nordicpeak.flowengine.beans.Flow;
import com.nordicpeak.flowengine.utils.flowfamilylist.bean.FlowFamilyListItem;

public class FlowFamilyListConnector {

	protected final Logger log = Logger.getLogger(this.getClass());

	private final String encoding;

	private FlowAdminModule flowAdminModule;

	public FlowFamilyListConnector(String encoding, FlowAdminModule flowAdminModule) {

		this.encoding = encoding;
		this.flowAdminModule = flowAdminModule;
	}

	protected static void sendEmptyJSONResponse(HttpServletResponse res) throws IOException {

		JsonObject jsonObject = new JsonObject(1);
		jsonObject.putField("hitCount", "0");
		HTTPUtils.sendReponse(jsonObject.toJson(), JsonUtils.getContentType(), res);
	}

	protected void sendJSONResponse(JsonArray jsonArray, HttpServletResponse res) throws IOException {

		JsonObject jsonObject = new JsonObject(2);
		jsonObject.putField("hitCount", Integer.toString(jsonArray.size()));
		jsonObject.putField("hits", jsonArray);
		HTTPUtils.sendReponse(jsonObject.toJson(), JsonUtils.getContentType(), res);
	}

	protected String parseQuery(HttpServletRequest req) throws IOException {

		String query = req.getParameter("q");

		if (StringUtils.isEmpty(query)) {
			return null;
		}
		
		if (!encoding.equalsIgnoreCase(StandardCharsets.UTF_8.name())) {
			query = URLDecoder.decode(query, StandardCharsets.UTF_8.name());
		}

		if (StringUtils.isEmpty(query)) {
			return null;
		}

		return query;
	}

	public ForegroundModuleResponse getFlowWithFamily(HttpServletRequest req, HttpServletResponse res, User user) throws Exception {

		String query = parseQuery(req);

		if (query == null) {

			sendEmptyJSONResponse(res);
			return null;
		}

		List<FlowFamilyListItem> flow = getLatestFlowFamilyListItems(query);

		log.info("User " + user + " searching for flows using query " + query + ", found " + CollectionUtils.getSize(flow) + " hits");

		if (flow == null) {
			

			sendEmptyJSONResponse(res);
			return null;
		}

		sendJSONResponse(getFlowJsonArray(flow), res);
		return null;
	}

	protected List<FlowFamilyListItem> getLatestFlowFamilyListItems(String contains) {

		LinkedHashMap<Integer, Flow> flowCacheMap = flowAdminModule.getFlowCache().getFlowCacheMap();
		return convertToFlowFamilyListItems(flowCacheMap.values().stream().filter(Objects::nonNull).filter(e -> e.getName().toLowerCase().contains(contains.toLowerCase())).filter(Flow::isLatestVersion).collect(Collectors.toList()));
	}

	private List<FlowFamilyListItem> convertToFlowFamilyListItems(List<Flow> list) {

		List<FlowFamilyListItem> newList = new ArrayList<>(list.size());

		list.stream().forEach(e -> 
			newList.add(new FlowFamilyListItem(e)));

		list.sort(Comparator.comparing(Flow::getName));

		return newList;
	}
	
	protected List<FlowFamilyListItem> getFlows(List<Integer> flowFamilyIDList) {

		if (flowFamilyIDList == null)
			return Collections.emptyList();

		LinkedHashMap<Integer, Flow> flowCacheMap = flowAdminModule.getFlowCache().getFlowCacheMap();

		return convertToFlowFamilyListItems(flowCacheMap.values().stream().filter(Objects::nonNull).filter(e -> flowFamilyIDList.contains(e.getFlowFamily().getFlowFamilyID())).filter(Flow::isLatestVersion).collect(Collectors.toList()));

	}

	protected JsonArray getFlowJsonArray(List<FlowFamilyListItem> flows) {

		JsonArray jsonArray = new JsonArray();

		for (FlowFamilyListItem currentFlow : flows) {

			jsonArray.addNode(getFlowJson(currentFlow));
		}

		return jsonArray;
	}

	protected JsonObject getFlowJson(FlowFamilyListItem flow) {

		JsonObject instance = new JsonObject(5);
		instance.putField(FlowFamilyListItem.FLOWFAMILYID, flow.getID());
		instance.putField("Name", flow.toString());
		instance.putField("Enabled", true);

		return instance;
	}

	public void addFlowFamilyListToDoc(Document doc, List<Integer> flowFamilies) {

		List<FlowFamilyListItem> flows = getFlows(flowFamilies);
		if (flows == null || flows.isEmpty()) {

			return;
		}

		Element flowFamilyList = doc.createElement(FlowFamilyListItem.NAME);

		for (FlowFamilyListItem flow : flows) {
			Element flowFamilyElement = doc.createElement("FlowFamily");
			Element flowFamilyIDElement = doc.createElement(FlowFamilyListItem.FLOWFAMILYID);
			flowFamilyIDElement.appendChild(doc.createTextNode(String.valueOf(flow.getID())));
			Element flowFamilyNameElement = doc.createElement(FlowFamilyListItem.FLOWFAMILYNAME);
			flowFamilyNameElement.appendChild(doc.createTextNode(flow.toString()));

			flowFamilyElement.appendChild(flowFamilyIDElement);
			flowFamilyElement.appendChild(flowFamilyNameElement);
			flowFamilyList.appendChild(flowFamilyElement);
		}

		Element root = doc.getDocumentElement();

		root.appendChild(flowFamilyList);

	}

}
