package com.nordicpeak.flowengine.interfaces;

import java.util.List;

import se.unlogic.hierarchy.core.interfaces.AccessInterface;
import se.unlogic.standardutils.xml.Elementable;

import com.nordicpeak.flowengine.beans.Flow;

public interface ImmutableFlowFamily extends AccessInterface, Elementable {

	public Integer getFlowFamilyID();

	public Integer getVersionCount();

	public List<Flow> getFlows();
}