package com.nordicpeak.flowengine.notifications.beans;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.hierarchy.core.interfaces.attributes.AttributeHandler;
import se.unlogic.standardutils.xml.GeneratedElementable;
import se.unlogic.standardutils.xml.XMLElement;
import se.unlogic.standardutils.xml.XMLUtils;

@XMLElement
public class NotificationMetadata extends GeneratedElementable {
	
	
	@XMLElement
	private String url;
	
	@XMLElement
	private String showURL;
	
	private User poster;
	
	public String getUrl() {
		return url;
	}
	
	public void setUrl(String url) {
		this.url = url;
	}
	
	public String getShowURL() {
		return showURL;
	}
	
	public void setShowURL(String showURL) {
		this.showURL = showURL;
	}
	
	public User getPoster() {
		return poster;
	}
	
	public void setPoster(User poster) {
		this.poster = poster;
	}
	
	@Override
	public Element toXML(Document doc) {
		
		Element element = super.toXML(doc);
		
		if (poster != null) {
			
			Element userElement = poster.toXML(doc);
			
			AttributeHandler attributeHandler = poster.getAttributeHandler();
			
			if (attributeHandler != null && !attributeHandler.isEmpty()) {
				
				userElement.appendChild(attributeHandler.toXML(doc));
			}
			
			Element posterElement = XMLUtils.appendNewElement(doc, element, "poster");
			
			posterElement.appendChild(userElement);
		}
		
		return element;
	}
}
