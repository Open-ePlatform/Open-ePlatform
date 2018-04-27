package com.nordicpeak.flowengine.interfaces;

import java.io.Serializable;
import java.util.List;

import se.unlogic.standardutils.xml.Elementable;

public interface ImmutableStep extends Serializable, Elementable {

	public String getName();

	public Integer getSortIndex();

	public ImmutableFlow getFlow();

	public Integer getStepID();

	public List<? extends ImmutableQueryDescriptor> getQueryDescriptors();

}