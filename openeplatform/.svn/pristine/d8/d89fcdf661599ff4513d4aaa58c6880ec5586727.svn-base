package com.nordicpeak.flowengine.queries.basequery;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import se.unlogic.hierarchy.core.interfaces.attributes.AttributeHandler;
import se.unlogic.standardutils.xml.ElementableListener;
import se.unlogic.standardutils.xml.XMLUtils;

public class BaseQueryTextsListener implements ElementableListener<BaseQuery> {

	private final AttributeHandler attributeHandler;
	
	public BaseQueryTextsListener(AttributeHandler attributeHandler) {

		super();
		this.attributeHandler = attributeHandler;
	}

	@Override
	public void elementGenerated(Document doc, Element element, BaseQuery query) {

		if (query.getHelpText() != null) {
			XMLUtils.appendNewCDATAElement(doc, element, "helpText", query.getHelpText(attributeHandler));
		}
		
		if (query.getDescription() != null) {
			XMLUtils.appendNewCDATAElement(doc, element, "description", query.getDescription(attributeHandler));
		}
	}
}