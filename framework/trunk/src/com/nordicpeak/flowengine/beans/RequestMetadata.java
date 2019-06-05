package com.nordicpeak.flowengine.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import se.unlogic.hierarchy.core.beans.SimpleAttribute;
import se.unlogic.hierarchy.core.handlers.SourceAttributeHandler;
import se.unlogic.hierarchy.core.interfaces.attributes.AttributeSource;
import se.unlogic.hierarchy.core.interfaces.attributes.MutableAttribute;
import se.unlogic.hierarchy.core.interfaces.attributes.MutableAttributeHandler;
import se.unlogic.standardutils.xml.GeneratedElementable;
import se.unlogic.standardutils.xml.XMLElement;

@XMLElement
public class RequestMetadata extends GeneratedElementable implements Serializable, AttributeSource {
	
	private static final long serialVersionUID = -2715157789979993384L;
	
	@XMLElement
	private final boolean manager;
	
	protected List<SimpleAttribute> attributes;
	
	private SourceAttributeHandler attributeHandler;
	
	public RequestMetadata(boolean manager) {
		super();
		
		this.manager = manager;
	}
	
	public boolean isManager() {
		
		return manager;
	}
	
	public synchronized MutableAttributeHandler getAttributeHandler() {
		
		if (attributeHandler == null) {
			
			this.attributeHandler = new SourceAttributeHandler(this, 255, 65536);
		}
		
		return attributeHandler;
	}
	
	@Override
	public List<? extends MutableAttribute> getAttributes() {
		return attributes;
	}
	
	@Override
	public void addAttribute(String name, String value) {
		
		if (attributes == null) {
			
			attributes = new ArrayList<SimpleAttribute>();
		}
		
		attributes.add(new SimpleAttribute(name, value));
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + " (manager=" + manager + ")";
	}
	
}
