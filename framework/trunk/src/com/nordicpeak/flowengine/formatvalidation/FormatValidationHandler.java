package com.nordicpeak.flowengine.formatvalidation;

import java.util.List;


public interface FormatValidationHandler {

	public List<FormatValidator> getFormatValidators();
	
	public FormatValidator getFormatValidator(String className);
	
	public boolean addFormatValidator(FormatValidator formatValidator);
	
	public boolean removeFormatValidator(FormatValidator formatValidator);
}
