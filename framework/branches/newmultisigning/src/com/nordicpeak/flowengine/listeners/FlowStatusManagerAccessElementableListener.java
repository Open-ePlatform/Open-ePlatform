package com.nordicpeak.flowengine.listeners;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.hierarchy.core.utils.AccessUtils;
import se.unlogic.standardutils.xml.ElementableListener;
import se.unlogic.standardutils.xml.XMLUtils;

import com.nordicpeak.flowengine.beans.Status;

public class FlowStatusManagerAccessElementableListener implements ElementableListener<Status> {
	
	protected final User user;
	
	public FlowStatusManagerAccessElementableListener(User user) {
		
		super();
		
		this.user = user;
	}
	
	@Override
	public void elementGenerated(Document doc, Element element, Status status) {
		
		if (status.isUseAccessCheck()) {
			
			if (!AccessUtils.checkAccess(user, status)) {
				
				XMLUtils.appendNewElement(doc, element, "noAccess");
			}
		}
	}
}
