package com.nordicpeak.flowengine.interfaces;

import com.nordicpeak.flowengine.enums.QueryState;

public interface MutableQueryInstanceDescriptor extends ImmutableQueryInstanceDescriptor{

	public void setQueryState(QueryState queryState);

	public void setPopulated(boolean values);

}