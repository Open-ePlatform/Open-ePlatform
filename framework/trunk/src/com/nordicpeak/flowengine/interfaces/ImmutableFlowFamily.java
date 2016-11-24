package com.nordicpeak.flowengine.interfaces;

import java.util.List;

import se.unlogic.hierarchy.core.interfaces.AccessInterface;

import com.nordicpeak.flowengine.beans.Flow;

public interface ImmutableFlowFamily extends AccessInterface{

	public Integer getFlowFamilyID();

	public Integer getVersionCount();

	public List<Flow> getFlows();
	
	public List<? extends ImmutableUserFavourite> getUserFavourites();
	
}