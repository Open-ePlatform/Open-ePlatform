package com.nordicpeak.flowengine.beans;

import se.unlogic.standardutils.xml.GeneratedElementable;
import se.unlogic.standardutils.xml.XMLElement;

@XMLElement
public class ExternalOperatingMessageSource extends GeneratedElementable {
	
	@XMLElement
	private String name;
	
	private String url;
	
	@XMLElement
	private boolean enabled;
	
	public ExternalOperatingMessageSource(String name, String url, boolean enabled) {
		super();
		this.name = name;
		this.url = url;
		this.enabled = enabled;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getURL() {
		return url;
	}
	
	public void setURL(String url) {
		this.url = url;
	}
	
	public boolean isEnabled() {
		return enabled;
	}
	
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	@Override
	public String toString() {
		return "ExternalOperatingMessageSource (name=" + name + ", url=" + url + ", enabled=" + enabled + ")";
	}
	
}
