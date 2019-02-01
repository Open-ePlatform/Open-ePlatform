package com.nordicpeak.flowengine.cache;

import java.util.HashMap;
import java.util.LinkedHashMap;

import com.nordicpeak.flowengine.beans.Flow;
import com.nordicpeak.flowengine.beans.FlowFamily;

public class FlowCache {

	private final LinkedHashMap<Integer, Flow> flowCacheMap;
	private final HashMap<Integer, FlowFamily> flowFamilyCacheMap;

	public FlowCache(LinkedHashMap<Integer, Flow> flowCacheMap, HashMap<Integer, FlowFamily> flowFamilyCacheMap) {

		super();
		this.flowCacheMap = flowCacheMap;
		this.flowFamilyCacheMap = flowFamilyCacheMap;
	}

	public LinkedHashMap<Integer, Flow> getFlowCacheMap() {

		return flowCacheMap;
	}

	public HashMap<Integer, FlowFamily> getFlowFamilyCacheMap() {

		return flowFamilyCacheMap;
	}
}
