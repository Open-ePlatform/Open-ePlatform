package com.nordicpeak.flowengine.comparators;

import java.util.Comparator;

import com.nordicpeak.flowengine.interfaces.UserMenuProvider;

public class UserMenuProviderComparator implements Comparator<UserMenuProvider> {

	@Override
	public int compare(UserMenuProvider o1, UserMenuProvider o2) {

		return o1.getUserMenuExtensionLink().getSlot().compareTo(o2.getUserMenuExtensionLink().getSlot());
	}

}
