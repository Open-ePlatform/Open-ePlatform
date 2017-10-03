package com.nordicpeak.flowengine.beans;

import java.io.Serializable;

import se.unlogic.standardutils.xml.GeneratedElementable;
import se.unlogic.standardutils.xml.XMLElement;

@XMLElement
public class RequestMetadata extends GeneratedElementable implements Serializable {

	private static final long serialVersionUID = -2715157789979993384L;

	@XMLElement
	private final boolean manager;

	public RequestMetadata(boolean manager) {
		super();
		
		this.manager = manager;
	}

	public boolean isManager() {

		return manager;
	}
}
