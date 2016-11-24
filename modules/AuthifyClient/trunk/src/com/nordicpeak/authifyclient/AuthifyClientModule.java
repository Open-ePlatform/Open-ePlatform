package com.nordicpeak.authifyclient;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import se.unlogic.hierarchy.core.annotations.ModuleSetting;
import se.unlogic.hierarchy.core.annotations.TextFieldSettingDescriptor;
import se.unlogic.hierarchy.core.annotations.WebPublic;
import se.unlogic.hierarchy.core.beans.SimpleForegroundModuleResponse;
import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.hierarchy.core.interfaces.ForegroundModuleResponse;
import se.unlogic.hierarchy.foregroundmodules.AnnotatedForegroundModule;
import se.unlogic.standardutils.xml.XMLUtils;
import se.unlogic.webutils.http.RequestUtils;
import se.unlogic.webutils.http.URIParser;

public class AuthifyClientModule extends AnnotatedForegroundModule {

	@ModuleSetting
	@TextFieldSettingDescriptor(name = "Authify API key", description = "The API key used for requests to Authify rest service", required = true)
	protected String authifyAPIKey;
	
	@ModuleSetting
	@TextFieldSettingDescriptor(name = "Authify secret key", description = "The secret key used for requests to Authify rest service", required = true)
	protected String authifySecretKey;
	
	protected AuthifyClient authifyClient;
	
	//TEMP
	protected static String FLOWINSTANCE_ID = "123";
	
	@Override
	protected void moduleConfigured() {
		
		authifyClient = new AuthifyClient(authifyAPIKey, authifySecretKey);
		
	}

	@Override
	public ForegroundModuleResponse defaultMethod(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Throwable {
		
		String fullAlias = RequestUtils.getFullContextPathURL(req) + this.getFullAlias();
		
		AuthifySession session = authifyClient.getAuthifySession(FLOWINSTANCE_ID, user, req, true);
		
		if(session == null) {
			
			authifyClient.login(FLOWINSTANCE_ID, user, fullAlias + "?foo=true", res);
			
			return null;
			
		}
		
		String idp = req.getParameter("idp");
		
		if(idp != null) {
			
			authifyClient.sign(idp, "Du signerar nu ansokan om eldstad, nr: " + FLOWINSTANCE_ID, session, fullAlias + "/signed", user, req, res);
			
			return null;
			
		}
		
		Document doc = this.createDocument(req, uriParser, user);
		
		Element signElement = doc.createElement("ChooseIDP");
		
		doc.getDocumentElement().appendChild(signElement);
		
		signElement.appendChild(session.toXML(doc));
		
		return new SimpleForegroundModuleResponse(doc, this.getDefaultBreadcrumb());
		
	}
	
	@WebPublic
	public ForegroundModuleResponse signed(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Throwable {
		
		AuthifySession session = authifyClient.getAuthifySession(FLOWINSTANCE_ID, user, req, false);
		
		if(session == null) {
			
			redirectToDefaultMethod(req, res);
			
			return null;
			
		}	
		
		authifyClient.getUpdatedSignAttributes(session, req);
		
		Document doc = this.createDocument(req, uriParser, user);
		
		Element signElement = doc.createElement("Signed");
		
		doc.getDocumentElement().appendChild(signElement);

		signElement.appendChild(session.toXML(doc));
		
		authifyClient.logout(session, req);
		
		return new SimpleForegroundModuleResponse(doc, this.getDefaultBreadcrumb());
		
	}
	
	public Document createDocument(HttpServletRequest req, URIParser uriParser, User user) {
		
		Document doc = XMLUtils.createDomDocument();
		Element document = doc.createElement("Document");
		document.appendChild(RequestUtils.getRequestInfoAsXML(doc, req, uriParser));
		document.appendChild(this.sectionInterface.getSectionDescriptor().toXML(doc));
		document.appendChild(this.moduleDescriptor.toXML(doc));
		
		doc.appendChild(document);
		
		return doc;
	}
	
}
