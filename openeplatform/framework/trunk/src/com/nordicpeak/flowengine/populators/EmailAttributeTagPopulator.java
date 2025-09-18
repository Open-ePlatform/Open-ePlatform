package com.nordicpeak.flowengine.populators;

import se.unlogic.emailutils.framework.EmailUtils;
import se.unlogic.hierarchy.core.utils.AttributeTagUtils;
import se.unlogic.standardutils.populators.BaseStringPopulator;
import se.unlogic.standardutils.populators.BeanStringPopulator;
import se.unlogic.standardutils.validation.StringFormatValidator;


public class EmailAttributeTagPopulator  extends BaseStringPopulator<String> implements BeanStringPopulator<String>{
	
	private static final EmailAttributeTagPopulator POPULATOR = new EmailAttributeTagPopulator();

	public static EmailAttributeTagPopulator getPopulator(){
		return POPULATOR;
	}

	public EmailAttributeTagPopulator() {
		super("email");
	}

	public EmailAttributeTagPopulator(String populatorID) {
		super(populatorID);
	}

	public EmailAttributeTagPopulator(String populatorID, StringFormatValidator formatValidator) {
		super(populatorID, formatValidator);
	}

	public String getValue(String value) {
		
		return value.toLowerCase();
	}

	@Override
	public boolean validateDefaultFormat(String value) {
		
		return EmailUtils.isValidEmailAddress(value) || AttributeTagUtils.getAttributeTags(value) != null;
	}

	public Class<? extends String> getType() {
		return String.class;
	}
}
