package com.nordicpeak.flowengine.populators;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import se.unlogic.standardutils.string.StringUtils;
import se.unlogic.standardutils.validation.ValidationError;

public class FreeTextAlternativePopulator {

	public static String populate(Integer queryID, String alternativeSuffix, HttpServletRequest req, List<ValidationError> errors) {
		
		String freeTextAlternativeValue = null;
		
		if(!StringUtils.isEmpty(req.getParameter("q" + queryID + alternativeSuffix))) {
			
			freeTextAlternativeValue = req.getParameter("q" + queryID + alternativeSuffix + "Value");
			
			if(StringUtils.isEmpty(freeTextAlternativeValue)) {
				
				errors.add(new ValidationError("FreeTextAlternativeValueRequired"));
				
				return "";
				
			} else if(freeTextAlternativeValue.length() > 255) {
				
				errors.add(new ValidationError("FreeTextAlternativeValueToLong"));
				
				return "";
			}
			
		}
		
		return freeTextAlternativeValue;
		
	}
	
}
