package com.nordicpeak.flowengine.beans;

import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import se.unlogic.hierarchy.core.interfaces.attributes.MutableAttribute;
import se.unlogic.hierarchy.core.interfaces.attributes.MutableAttributeHandler;
import se.unlogic.standardutils.xml.XMLElement;
import se.unlogic.standardutils.xml.XMLUtils;

import com.nordicpeak.flowengine.interfaces.ImmutableFlowInstance;
import com.nordicpeak.flowengine.managers.FlowInstanceManager;

@XMLElement
public class InstanceRequestMetadata extends RequestMetadata {

	//TODO: re-write it as an interface instead of extending RequestMetadata
	
	private static final long serialVersionUID = 7490019871683501561L;

	private RequestMetadata requestMetadata;

	private FlowInstanceManager flowInstanceManager;

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

	public Integer getFlowFamilyID() {
		
		return flowInstanceManager.getFlowFamilyID();
	}
	
	public String getFlowInstanceManagerID() {

		return flowInstanceManager.getInstanceManagerID();
	}

	public boolean flowInstanceIsSubmitted() {

		ImmutableFlowInstance flowInstance = getFlowInstance();

		return flowInstance != null && flowInstance.getFirstSubmitted() != null;
	}

	protected ImmutableFlowInstance getFlowInstance() {

		return flowInstanceManager.getFlowInstance();
	}

	@Override
	public String toString() {

		return getClass().getSimpleName() + " (manager=" + requestMetadata.manager + ", flowInstanceManager=" + flowInstanceManager + ")";
	}

	@Override
	public Element toXML(Document doc) {

		Element element = doc.createElement("RequestMetadata");
		
		// Do not use supers member variable manager
		XMLUtils.appendNewElement(doc, element, "manager", requestMetadata.isManager());

		XMLUtils.appendNewElement(doc, element, "flowInstanceIsSubmitted", flowInstanceIsSubmitted());

		return element;
	}

}
