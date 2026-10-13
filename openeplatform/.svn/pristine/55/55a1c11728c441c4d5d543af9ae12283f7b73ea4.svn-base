package com.nordicpeak.flowengine;

import javax.servlet.http.HttpServletRequest;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import se.unlogic.hierarchy.backgroundmodules.AnnotatedBackgroundModule;
import se.unlogic.hierarchy.core.annotations.InstanceManagerDependency;
import se.unlogic.hierarchy.core.beans.SimpleBackgroundModuleResponse;
import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.hierarchy.core.interfaces.BackgroundModuleResponse;
import se.unlogic.standardutils.xml.XMLUtils;
import se.unlogic.webutils.http.URIParser;

import com.nordicpeak.flowengine.interfaces.OperatingStatus;

public class OperatingMessageBackgroundModule extends AnnotatedBackgroundModule {

	@InstanceManagerDependency
	private OperatingMessageModule operatingMessageModule;

	@Override
	protected BackgroundModuleResponse processBackgroundRequest(HttpServletRequest req, User user, URIParser uriParser) throws Exception {

		if (operatingMessageModule != null) {

			OperatingStatus operatingStatus = operatingMessageModule.getGlobalOperatingStatus(false);

			if (operatingStatus != null) {

				Document doc = XMLUtils.createDomDocument();
				
				Element document = doc.createElement("Document");
				
				doc.appendChild(document);
				
				document.appendChild(operatingStatus.toXML(doc));

				return new SimpleBackgroundModuleResponse(doc);
			}

		}

		return null;
	}

}
