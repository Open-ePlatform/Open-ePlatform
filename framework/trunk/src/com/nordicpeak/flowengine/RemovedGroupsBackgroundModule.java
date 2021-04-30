package com.nordicpeak.flowengine;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import se.unlogic.hierarchy.backgroundmodules.AnnotatedBackgroundModule;
import se.unlogic.hierarchy.core.annotations.InstanceManagerDependency;
import se.unlogic.hierarchy.core.beans.Group;
import se.unlogic.hierarchy.core.beans.SimpleBackgroundModuleResponse;
import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.hierarchy.core.interfaces.BackgroundModuleResponse;
import se.unlogic.standardutils.xml.XMLUtils;
import se.unlogic.webutils.http.SessionUtils;
import se.unlogic.webutils.http.URIParser;

public class RemovedGroupsBackgroundModule extends AnnotatedBackgroundModule {

	@InstanceManagerDependency
	private OperatingMessageModule operatingMessageModule;

	@Override
	protected BackgroundModuleResponse processBackgroundRequest(HttpServletRequest req, User user, URIParser uriParser) throws Exception {

		HttpSession session = req.getSession(false);
		
		if(session == null) {
			
			return null;
		}
		
		@SuppressWarnings("unchecked")
		List<Group> removedGroups = SessionUtils.getCastAttribute("removedGroups", req, List.class);
		
		if(removedGroups == null) {
			
			return null;
		}
		
		Document doc = XMLUtils.createDomDocument();
		
		Element document = doc.createElement("Document");
		
		doc.appendChild(document);
		
		XMLUtils.append(doc, document, "RemovedGroups", removedGroups);

		return new SimpleBackgroundModuleResponse(doc);
		
	}

}
