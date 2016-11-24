package com.nordicpeak.flowengine.formatvalidation;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import se.unlogic.standardutils.beans.Named;
import se.unlogic.standardutils.validation.StringFormatValidator;
import se.unlogic.standardutils.xml.Elementable;
import se.unlogic.standardutils.xml.XMLUtils;

public abstract class FormatValidator implements Elementable, StringFormatValidator, Named {

	public abstract String getName();

	public abstract String getClassName();
	
	public abstract Class<? extends StringFormatValidator> getInstanceClass();

	public abstract String getValidationMessage();
	
	public abstract String getPlaceholder();

	@Override
	public final Element toXML(Document doc) {
		
		Element validatorElement = doc.createElement("FormatValidator");
		
		XMLUtils.appendNewElement(doc, validatorElement, "name", getName());
		XMLUtils.appendNewElement(doc, validatorElement, "className", getClassName());
		XMLUtils.appendNewElement(doc, validatorElement, "validationMessage", getValidationMessage());
		XMLUtils.appendNewElement(doc, validatorElement, "placeholder", getPlaceholder());
		
		XMLUtils.appendNewElement(doc, validatorElement, "formatValidatorID", getClassName().replace(".", "_"));
		
		return validatorElement;
	}
	
	@Override
	public String toString(){
		
		return getName() + " (className: " + getClassName() + ")";
	}
}
