package com.nordicpeak.flowengine.sharing.validators;

import java.util.regex.Pattern;

import se.unlogic.standardutils.string.StringUtils;
import se.unlogic.standardutils.validation.StringFormatValidator;

public class RepositoryConfigurationValidator implements StringFormatValidator {

	private static final Pattern PATTERN = Pattern.compile("^https?://[^:]+:[^:]+:[^:]+$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);

	@Override
	public boolean validateFormat(String value) {

		if (StringUtils.isEmpty(value)) {
			return false;
		}

		return PATTERN.matcher(value).matches();
	}

}
