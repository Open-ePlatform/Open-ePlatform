package com.nordicpeak.flowengine.listeners;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import se.unlogic.standardutils.xml.ElementableListener;
import se.unlogic.standardutils.xml.XMLUtils;

import com.nordicpeak.flowengine.beans.FlowInstance;

public class FlowInstanceExternalMessageElementableListener implements ElementableListener<FlowInstance> {

	@Override
	public void elementGenerated(Document doc, Element element, FlowInstance flowInstance) {

		if (!flowInstance.isExternalMessagesEnabled()) {
			
			XMLUtils.appendNewElement(doc, element, "hideExternalMessages", true);
		}
		
		if (!flowInstance.isNewExternalMessagesAllowed()) {
			
			XMLUtils.appendNewElement(doc, element, "hideSendExternalMessage", true);
		}
	}

}
