package com.nordicpeak.listuserprovider;

import java.util.Collection;


public interface ListUserHandler {

	public ListUserProvider getUserListProvider(String id);
	
	public Collection<ListUserProvider> getUserListProviders();
	
	public void addUserListProvider(ListUserProvider userListProvider);
	
	public boolean removeUserListProvider(ListUserProvider userListProvider);
}
