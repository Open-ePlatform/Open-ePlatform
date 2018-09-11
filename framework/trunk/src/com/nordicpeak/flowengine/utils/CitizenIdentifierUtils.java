package com.nordicpeak.flowengine.utils;

import java.util.Calendar;

import com.nordicpeak.flowengine.Constants;

import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.hierarchy.core.utils.UserUtils;
import se.unlogic.standardutils.string.StringUtils;

public class CitizenIdentifierUtils {

	public static String getUserOrManagerCitizenIdentifier(User user) {

		if (user == null) {

			return null;
		}

		String citizenIdentifier = UserUtils.getAttribute(Constants.USER_CITIZEN_IDENTIFIER_ATTRIBUTE, user);

		if (StringUtils.isEmpty(citizenIdentifier)) {

			citizenIdentifier = UserUtils.getAttribute(Constants.MANAGER_CITIZEN_IDENTIFIER_ATTRIBUTE, user);
		}

		return citizenIdentifier;

	}

	public static String addCentury(String citizenIdentifier) {
		
		int year = Calendar.getInstance().get(Calendar.YEAR);
		
		int currentCentury = year / 100;
		int currentDec = year % 100;
		int decennium = Integer.valueOf(citizenIdentifier.substring(0, 2));
		
		if (decennium > currentDec) {
			currentCentury -= 1;
		}
		
		if (citizenIdentifier.contains("+")) {
			currentCentury -= 1;
			citizenIdentifier = citizenIdentifier.replace("+", "-");
		}
		
		return Integer.toString(currentCentury) + citizenIdentifier;
	}
}
