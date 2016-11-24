package com.nordicpeak.flowengine;

import javax.servlet.http.HttpServletRequest;

import org.w3c.dom.Document;

import se.unlogic.hierarchy.core.annotations.ModuleSetting;
import se.unlogic.hierarchy.core.annotations.TextFieldSettingDescriptor;
import se.unlogic.standardutils.xml.XMLUtils;
import se.unlogic.webutils.http.URIParser;

public class UserProfileModule extends se.unlogic.hierarchy.foregroundmodules.userprofile.UserProfileModule {

	@ModuleSetting(allowsNull = true)
	@TextFieldSettingDescriptor(name = "Cancel redirect URI", description = "Cancel redirect URI")
	protected String cancelRedirectURI;

	@Override
	public Document createDocument(HttpServletRequest req, URIParser uriParser) {

		Document doc = super.createDocument(req, uriParser);

		XMLUtils.appendNewElement(doc, doc.getDocumentElement(), "cancelRedirectURI", cancelRedirectURI);

		return doc;

	}

}
