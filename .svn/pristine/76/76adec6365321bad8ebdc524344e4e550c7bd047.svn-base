package com.nordicpeak.flowengine.cache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.standardutils.collections.CollectionUtils;

import com.nordicpeak.flowengine.beans.Flow;
import com.nordicpeak.flowengine.beans.FlowFamily;
import com.nordicpeak.flowengine.enums.ManagerAccess;

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
	
	public FlowCacheAccess getManagedFlowFamilies(User user) {
		
		List<FlowFamily> flowFamiliesFullAccess = new ArrayList<FlowFamily>();
		List<FlowFamily> flowFamiliesRestrictedAccess = null;
		
		for (FlowFamily flowFamily : flowFamilyCacheMap.values()) {
			
			ManagerAccess access = flowFamily.getManagerAccess(user);
			
			if (access == ManagerAccess.FULL) {
				
				flowFamiliesFullAccess.add(flowFamily);
				
			} else if (access == ManagerAccess.RESTRICTED) {
				
				flowFamiliesRestrictedAccess = CollectionUtils.addAndInstantiateIfNeeded(flowFamiliesRestrictedAccess, flowFamily);
			}
		}
		
		if (flowFamiliesFullAccess.isEmpty()) {
			flowFamiliesFullAccess = null;
		}
		
		return new FlowCacheAccess(flowFamiliesFullAccess, flowFamiliesRestrictedAccess);
	}
}
