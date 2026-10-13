package com.nordicpeak.flowengine.listeners;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.standardutils.xml.ElementableListener;
import se.unlogic.standardutils.xml.XMLUtils;

import com.nordicpeak.flowengine.beans.ExternalMessage;

public class ExternalMessageReadReceiptElementableListener implements ElementableListener<ExternalMessage> {

	protected final User user;

	public ExternalMessageReadReceiptElementableListener(User user) {

		this.user = user;
	}

	@Override
	public void elementGenerated(Document doc, Element element, ExternalMessage externalMessage) {

		if (!externalMessage.hasReadReceiptAccess(user)) {

			XMLUtils.appendNewElement(doc, element, "noReadReceiptAccess");
		}
	}
}
