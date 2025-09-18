package com.nordicpeak.flowengine.notifications;

import se.unlogic.standardutils.numbers.NumberUtils;
import se.unlogic.standardutils.validation.StringFormatValidator;


public class EmailAttachmentSizeFormatValidator implements StringFormatValidator {

	@Override
	public boolean validateFormat(String value) {

		Integer intValue = NumberUtils.toInt(value);
		
		if(intValue == null) {
			
			return false;
		}
		
		if(intValue < 1) {
			
			return false;
		}
		
		if(intValue > 20) {
			
			return false;
		}
		
		return true;
	}

}
