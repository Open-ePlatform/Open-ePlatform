package com.nordicpeak.flowengine.formatvalidation;

import se.unlogic.standardutils.dao.annotations.DAOManaged;
import se.unlogic.standardutils.dao.annotations.Key;
import se.unlogic.standardutils.dao.annotations.OrderBy;
import se.unlogic.standardutils.dao.annotations.Table;
import se.unlogic.standardutils.reflection.ReflectionUtils;
import se.unlogic.standardutils.string.StringUtils;
import se.unlogic.standardutils.validation.StringFormatValidator;
import se.unlogic.standardutils.xml.XMLElement;

@Table(name = "format_validators")
public class SimpleFormatValidator extends FormatValidator {

	@DAOManaged
	@Key
	@XMLElement
	private String className;
	
	@DAOManaged
	@OrderBy
	private String name;

	@DAOManaged
	@XMLElement
	private String validationMessage;
	
	@DAOManaged
	@XMLElement
	private String placeholder;
	
	private StringFormatValidator instance;

	public SimpleFormatValidator() {}

	public SimpleFormatValidator(String name, String className, String validationMessage) {

		this.name = name;
		this.className = className;
		this.validationMessage = validationMessage;
	}

	@Override
	public String getName() {

		return name;
	}

	@Override
	public String getClassName() {

		return className;
	}

	@Override
	public String getValidationMessage() {

		return validationMessage;
	}

	public void init() throws NoClassDefFoundError, ClassNotFoundException, InstantiationException, IllegalAccessException{
		
		instance = (StringFormatValidator) ReflectionUtils.getInstance(className);
	}
	
	@Override
	public boolean validateFormat(String value) {

		return instance.validateFormat(value);
	}

	@Override
	public Class<? extends StringFormatValidator> getInstanceClass() {

		if (instance == null) {
			return null;
		}

		return instance.getClass();
	}

	public String getPlaceholder() {
	
		return placeholder;
	}

	
	protected void setPlaceholder(String placeholder) {
	
		this.placeholder = placeholder;
	}
	
	@Override
	public String toString(){
		
		return StringUtils.toLogFormat(name, 30) + " (" + className + ")";
	}
}
