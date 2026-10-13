package com.nordicpeak.flowengine.listeners;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.hierarchy.core.utils.AccessUtils;
import se.unlogic.standardutils.collections.CollectionUtils;
import se.unlogic.standardutils.xml.ElementableListener;
import se.unlogic.standardutils.xml.XMLUtils;

import com.nordicpeak.flowengine.beans.Status;

public class FlowStatusManagerAccessElementableListener implements ElementableListener<Status> {
	
	protected final User user;
	
	protected final Status currentStatus;
	
	public FlowStatusManagerAccessElementableListener(User user, Status status) {
		
		super();
		
		this.user = user;
		
		this.currentStatus = status;
	}
	
	@Override
	public void elementGenerated(Document doc, Element element, Status status) {
		
		if(status.isUseAccessCheckByUser() && !AccessUtils.checkAccess(user, status)) {
			
			XMLUtils.appendNewElement(doc, element, "noAccess");
			return;
		}
		
		if(status.isUseAccessCheckByStatus()) {
			
			if(CollectionUtils.isEmpty(status.getAcceptedStatusIDs()) || !status.getAcceptedStatusIDs().contains(currentStatus.getStatusID())) {

				XMLUtils.appendNewElement(doc, element, "noAccess");
			}
		}
	}
}
