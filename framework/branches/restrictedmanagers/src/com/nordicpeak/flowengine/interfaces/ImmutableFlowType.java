package com.nordicpeak.flowengine.interfaces;

import java.util.List;

import se.unlogic.standardutils.xml.Elementable;

import com.nordicpeak.flowengine.beans.FlowTypeAdminAccessInterface;
import com.nordicpeak.flowengine.beans.FlowTypeUserAccessInterface;

public interface ImmutableFlowType extends Elementable{

	public Integer getFlowTypeID();

	public String getName();

	public List<? extends ImmutableCategory> getCategories();

	public List<? extends ImmutableFlow> getFlows();

	public List<String> getAllowedQueryTypes();

	public FlowTypeAdminAccessInterface getAdminAccessInterface();

	public FlowTypeUserAccessInterface getUserAccessInterface();
}