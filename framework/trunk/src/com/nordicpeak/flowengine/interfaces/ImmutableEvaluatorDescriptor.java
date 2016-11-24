package com.nordicpeak.flowengine.interfaces;

import java.io.Serializable;
import java.util.List;

import se.unlogic.standardutils.xml.Elementable;

public interface ImmutableEvaluatorDescriptor extends Serializable, Elementable {

	public Integer getEvaluatorID();

	public String getName();

	public Integer getSortIndex();

	public String getEvaluatorTypeID();

	public boolean isEnabled();

	public ImmutableQueryDescriptor getQueryDescriptor();

	public List<Integer> getTargetQueryIDs();
}