package com.nordicpeak.flowengine.utils;

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
}
