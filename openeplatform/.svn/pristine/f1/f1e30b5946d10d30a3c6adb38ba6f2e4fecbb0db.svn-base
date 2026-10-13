package com.nordicpeak.flowengine.utils;

import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import se.unlogic.standardutils.collections.CollectionUtils;
import se.unlogic.standardutils.numbers.NumberUtils;

public class MentionedUserTagUtils {

	private static final Pattern MENTION_TAG_PATTERN = Pattern.compile("(\\$mention)[{](.*?):(.*?)}");

	public static Set<Integer> getMentionedUserIDs(String text) {

		Matcher matcher = MENTION_TAG_PATTERN.matcher(text);

		Set<Integer> userIDs = null;

		while (matcher.find()) {

			Integer userID = NumberUtils.toInt(matcher.group(3));

			userIDs = CollectionUtils.addAndInstantiateIfNeeded(userIDs, userID);
		}

		return userIDs;
	}

}
