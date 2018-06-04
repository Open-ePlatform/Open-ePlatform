package com.nordicpeak.flowengine.notifications;

import javax.servlet.http.HttpServletRequest;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import se.unlogic.hierarchy.backgroundmodules.AnnotatedBackgroundModule;
import se.unlogic.hierarchy.core.annotations.InstanceManagerDependency;
import se.unlogic.hierarchy.core.beans.SimpleBackgroundModuleResponse;
import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.hierarchy.core.interfaces.BackgroundModuleResponse;
import se.unlogic.standardutils.xml.XMLUtils;
import se.unlogic.webutils.http.RequestUtils;
import se.unlogic.webutils.http.URIParser;

import com.nordicpeak.flowengine.notifications.interfaces.NotificationHandler;

public class NotificationBackgroundModule extends AnnotatedBackgroundModule {
	
	@InstanceManagerDependency(required = true)
	protected NotificationHandler notificationHandler;
	
	@Override
	protected BackgroundModuleResponse processBackgroundRequest(HttpServletRequest req, User user, URIParser uriParser) throws Exception {
		
		Document doc = createDocument(req, uriParser, user);
		Element documentElement = (Element) doc.getFirstChild();
		
		XMLUtils.appendNewElement(doc, documentElement, "UnreadCount", notificationHandler.getUnreadCount(user.getUserID()));
		XMLUtils.appendNewElement(doc, documentElement, "NotificationHandlerURL", notificationHandler.getModuleURL(req));
		
		return new SimpleBackgroundModuleResponse(doc);
	}
	
	public Document createDocument(HttpServletRequest req, URIParser uriParser, User user) {

		Document doc = XMLUtils.createDomDocument();
		Element documentElement = doc.createElement("Document");
		documentElement.appendChild(RequestUtils.getRequestInfoAsXML(doc, req, uriParser));
		documentElement.appendChild(moduleDescriptor.toXML(doc));
		
		doc.appendChild(documentElement);
		return doc;
	}
	
}
