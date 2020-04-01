package com.nordicpeak.flowengine.flowapprovalmodule.listeners;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import se.unlogic.standardutils.xml.ElementableListener;
import se.unlogic.standardutils.xml.XMLUtils;

import com.nordicpeak.flowengine.flowapprovalmodule.beans.FlowApprovalActivityProgress;


public class FlowApprovalElementableListener implements ElementableListener<FlowApprovalActivityProgress> {

	@Override
	public void elementGenerated(Document doc, Element element, FlowApprovalActivityProgress activityProgress) {
		
		XMLUtils.appendNewElement(doc, element, "signingData", activityProgress.getSigningData());
	}

}
