package com.nordicpeak.flowengine.interfaces;

import se.unlogic.hierarchy.core.beans.User;

public interface UsersClosestManagerQueryInstance extends QueryInstance {

	public User getClosestManager(QueryHandler queryHandler);
}
