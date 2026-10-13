package com.nordicpeak.listuserprovider;

import java.util.List;

import se.unlogic.hierarchy.core.beans.User;

public interface ListUserProvider {
	
	public String getListName();
	
	public String getListID();
	
	public User getUser(String providerUserID);
	
	public ListUser getListUser(String providerUserID);
	
	public List<ListUser> getUsers(Integer flowID, List<? extends ListParameter> filterParameters) throws Exception;
	
	public List<ListUser> searchUsers(Integer flowID, String searchString, List<? extends ListParameter> filterParameters, User user) throws Exception;
	
}
