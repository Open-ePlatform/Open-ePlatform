package com.nordicpeak.flowengine.interfaces;

import java.io.Serializable;
import java.util.List;

import se.unlogic.standardutils.xml.Elementable;

import com.nordicpeak.flowengine.enums.QueryState;

public interface ImmutableQueryDescriptor extends Serializable, Elementable {

	public Integer getQueryID();

	public String getName();

	public Integer getSortIndex();

	public QueryState getDefaultQueryState();

	public ImmutableStep getStep();

	public String getQueryTypeID();

	public List<? extends ImmutableEvaluatorDescriptor> getEvaluatorDescriptors();

	public boolean isExported();

	public String getXSDElementName();
}