package com.nordicpeak.flowengine.beans;

import java.util.List;

import se.unlogic.hierarchy.core.interfaces.attributes.MutableAttribute;
import se.unlogic.hierarchy.core.interfaces.attributes.MutableAttributeHandler;
import se.unlogic.standardutils.xml.XMLElement;

import com.nordicpeak.flowengine.interfaces.ImmutableFlowInstance;
import com.nordicpeak.flowengine.managers.FlowInstanceManager;

@XMLElement
public class InstanceRequestMetadata extends RequestMetadata {

	private static final long serialVersionUID = 7490019871683501561L;

	private RequestMetadata requestMetadata;

	FlowInstanceManager flowInstanceManager;

	public InstanceRequestMetadata(RequestMetadata requestMetadata, FlowInstanceManager flowInstanceManager) {

		this.requestMetadata = requestMetadata;
		this.flowInstanceManager = flowInstanceManager;
	}

	@Override
	public boolean isManager() {

		return requestMetadata.isManager();
	}

	@Override
	public synchronized MutableAttributeHandler getAttributeHandler() {

		return requestMetadata.getAttributeHandler();
	}

	@Override
	public List<? extends MutableAttribute> getAttributes() {

		return requestMetadata.getAttributes();
	}

	@Override
	public void addAttribute(String name, String value) {

		requestMetadata.addAttribute(name, value);
	}

	public Integer getFlowInstanceID() {

		return flowInstanceManager.getFlowInstanceID();
	}

	public String getFlowInstanceManagerID() {

		return flowInstanceManager.getInstanceManagerID();
	}
	
	public boolean flowInstanceIsSubmitted() {

		ImmutableFlowInstance flowInstance = flowInstanceManager.getFlowInstance();

		return flowInstance != null && flowInstance.getFirstSubmitted() != null;
	}

	@Override
	public String toString() {

		return getClass().getSimpleName() + " (manager=" + requestMetadata.manager + ", flowInstanceManager=" + flowInstanceManager + ")";
	}

}
