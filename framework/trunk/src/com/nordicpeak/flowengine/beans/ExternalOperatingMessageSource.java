package com.nordicpeak.flowengine.beans;

import se.unlogic.standardutils.xml.GeneratedElementable;
import se.unlogic.standardutils.xml.XMLElement;

@XMLElement
public class ExternalOperatingMessageSource extends GeneratedElementable {

	@XMLElement
	private final String name;

	private final String url;

	private final String encoding;

	@XMLElement
	private boolean enabled;

	public ExternalOperatingMessageSource(String name, String url, String encoding, boolean enabled) {

		super();
		this.name = name;
		this.url = url;
		this.encoding = encoding;
		this.enabled = enabled;
	}

	public String getName() {

		return name;
	}

	public String getURL() {

		return url;
	}

	public boolean isEnabled() {

		return enabled;
	}

	public void setEnabled(boolean enabled) {

		this.enabled = enabled;
	}

	public String getEncoding() {

		return encoding;
	}
	
	@Override
	public String toString() {

		return name + " (url: " + url + ", enabled: " + enabled + ")";
	}
}
