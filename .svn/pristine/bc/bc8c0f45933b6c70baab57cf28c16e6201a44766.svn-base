package com.nordicpeak.flowengine.listeners;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import se.unlogic.standardutils.xml.ElementableListener;
import se.unlogic.standardutils.xml.XMLUtils;

import com.nordicpeak.flowengine.beans.FlowInstance;
import com.nordicpeak.flowengine.beans.Status;
import com.nordicpeak.flowengine.utils.FlowInstanceUtils;

public class FlowInstanceStatusMessageElementableListener implements ElementableListener<Status> {

	protected final FlowInstance flowInstance;

	public FlowInstanceStatusMessageElementableListener(FlowInstance flowInstance) {

		super();

		this.flowInstance = flowInstance;
	}

	@Override
	public void elementGenerated(Document doc, Element element, Status status) {

		if (!FlowInstanceUtils.isNewExternalMessagesAllowed(flowInstance, status)) {

			XMLUtils.appendNewElement(doc, element, "hideSendExternalMessage", true);
		}

		if (!flowInstance.isInternalMessagesEnabled()) {

			XMLUtils.appendNewElement(doc, element, "hideSendInternalMessage", true);
		}
	}

}
