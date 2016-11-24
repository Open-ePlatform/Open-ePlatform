package org.oeplatform.pbl;

import javax.servlet.http.HttpServletRequest;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import se.unlogic.hierarchy.backgroundmodules.AnnotatedBackgroundModule;
import se.unlogic.hierarchy.core.annotations.ModuleSetting;
import se.unlogic.hierarchy.core.annotations.TextFieldSettingDescriptor;
import se.unlogic.hierarchy.core.beans.SimpleBackgroundModuleResponse;
import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.hierarchy.core.interfaces.BackgroundModuleResponse;
import se.unlogic.standardutils.xml.XMLUtils;
import se.unlogic.webutils.http.RequestUtils;
import se.unlogic.webutils.http.URIParser;


public class PBLKnowledgeBankBgModule extends AnnotatedBackgroundModule {

	@ModuleSetting
	@TextFieldSettingDescriptor(name = "PBL Proxy module alias ", description = "The full alias of the PBL proxy module (relative from contextpath)", required = true)
	protected String pblProxyModuleAlias;
	
	@Override
	protected BackgroundModuleResponse processBackgroundRequest(HttpServletRequest req, User user, URIParser uriParser) throws Exception {

		Document doc = XMLUtils.createDomDocument();
		Element document = doc.createElement("Document");
		doc.appendChild(document);
		
		XMLUtils.appendNewElement(doc, document, "pblProxyModuleURI", req.getContextPath() + pblProxyModuleAlias);
		
		return new SimpleBackgroundModuleResponse(doc);
	}

	protected Document createDocument(HttpServletRequest req, URIParser uriParser) {
		
		Document doc = XMLUtils.createDomDocument();
		Element document = doc.createElement("Document");
		document.appendChild(RequestUtils.getRequestInfoAsXML(doc, req, uriParser));
		document.appendChild(this.moduleDescriptor.toXML(doc));
		doc.appendChild(document);
		
		return doc;
	}
	
}
