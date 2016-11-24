package com.nordicpeak.flowengine.queries.basequery;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import se.unlogic.hierarchy.core.interfaces.AttributeHandler;
import se.unlogic.standardutils.xml.ElementableListener;
import se.unlogic.standardutils.xml.XMLUtils;


public class QueryDescriptionListener implements ElementableListener<BaseQuery> {

	private final AttributeHandler attributeHandler;
	
	public QueryDescriptionListener(AttributeHandler attributeHandler) {

		super();
		this.attributeHandler = attributeHandler;
	}

	@Override
	public void elementGenerated(Document doc, Element element, BaseQuery query) {

		XMLUtils.appendNewCDATAElement(doc, element, "description", query.getDescription(attributeHandler));
	}
}
