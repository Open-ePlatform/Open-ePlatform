package com.nordicpeak.flowengine.comparators;

import java.util.Comparator;

import com.nordicpeak.flowengine.beans.UserFavourite;


public class UserFavouriteFlowNameComparator implements Comparator<UserFavourite> {

	@Override
	public int compare(UserFavourite u1, UserFavourite u2) {

		return u1.getFlowName().compareTo(u2.getFlowName());
	}

}
