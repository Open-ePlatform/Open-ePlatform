package com.nordicpeak.flowengine.queries.childquery.interfaces;

import java.util.List;
import java.util.Map;

import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.standardutils.xml.XMLable;

import com.nordicpeak.childrelationprovider.Child;
import com.nordicpeak.childrelationprovider.exceptions.ChildRelationProviderException;
import com.nordicpeak.flowengine.interfaces.ImmutableFlow;
import com.nordicpeak.flowengine.queries.childquery.filterapi.FilterAPIChild;

public interface ChildQueryFilterEndpoint extends XMLable {

	public String getName();
	
	public ChildQueryFilterProvider getProvider();
	
	public List<String> getFields();

	public Map<String, FilterAPIChild> getChildren(Map<String, Child> navetChildMap, User user, String parentCitizenID, ImmutableFlow flow) throws ChildRelationProviderException;

}
