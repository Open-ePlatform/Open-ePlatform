package com.nordicpeak.flowengine.interfaces;

import java.util.Collection;

import se.unlogic.hierarchy.core.beans.User;

public interface UserValuesQueryInstance extends QueryInstance{

	public Collection<User> getUserValues(QueryHandler queryHandler);
}
