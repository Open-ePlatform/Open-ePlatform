package com.nordicpeak.childrelationprovider.exceptions;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import se.unlogic.standardutils.xml.XMLUtils;
import se.unlogic.standardutils.xml.XMLable;

public abstract class ChildRelationProviderException extends Exception implements XMLable {
	
	private static final long serialVersionUID = 7845992401201772898L;
	
	public ChildRelationProviderException() {
		super();
	}
	
	public ChildRelationProviderException(String message) {
		super(message);
	}
	
	public ChildRelationProviderException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public ChildRelationProviderException(Throwable cause) {
		super(cause);
	}
	
	@Override
	public Element toXML(Document doc) {
		
		Element element = doc.createElement(this.getClass().getSimpleName());
		
		XMLUtils.appendNewElement(doc, element, "Message", getMessage());
		
		return element;
	}
}
