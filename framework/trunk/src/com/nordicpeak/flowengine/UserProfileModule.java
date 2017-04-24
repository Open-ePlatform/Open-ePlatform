package com.nordicpeak.flowengine;

import javax.servlet.http.HttpServletRequest;

import org.w3c.dom.Document;

import se.unlogic.hierarchy.core.annotations.InstanceManagerDependency;
import se.unlogic.hierarchy.core.annotations.ModuleSetting;
import se.unlogic.hierarchy.core.annotations.TextFieldSettingDescriptor;
import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.hierarchy.core.interfaces.AccessInterface;
import se.unlogic.hierarchy.core.utils.extensionlinks.ExtensionLink;
import se.unlogic.standardutils.xml.XMLUtils;
import se.unlogic.webutils.http.URIParser;

import com.nordicpeak.flowengine.interfaces.UserMenuProvider;

public class UserProfileModule extends se.unlogic.hierarchy.foregroundmodules.userprofile.UserProfileModule implements UserMenuProvider {

	@ModuleSetting(allowsNull = true)
	@TextFieldSettingDescriptor(name = "Cancel redirect URI", description = "Cancel redirect URI")
	protected String cancelRedirectURI;
	
	protected UserFlowInstanceMenuModule userFlowInstanceMenuModule;

	@Override
	public void unload() throws Exception {

		if(userFlowInstanceMenuModule != null){
			
			setUserFlowInstanceMenuModule(null);
		}
		
		super.unload();
	}
	
	@InstanceManagerDependency
	public void setUserFlowInstanceMenuModule(UserFlowInstanceMenuModule userFlowInstanceMenuModule) {

		if(userFlowInstanceMenuModule == null && this.userFlowInstanceMenuModule != null){
			
			this.userFlowInstanceMenuModule.removeUserMenuProvider(this);
		}
		
		this.userFlowInstanceMenuModule = userFlowInstanceMenuModule;

		if (this.userFlowInstanceMenuModule != null) {

			this.userFlowInstanceMenuModule.addUserMenuProvider(this);
		}
	}
	
	@Override
	public Document createDocument(HttpServletRequest req, URIParser uriParser) {

		Document doc = super.createDocument(req, uriParser);

		XMLUtils.appendNewElement(doc, doc.getDocumentElement(), "cancelRedirectURI", cancelRedirectURI);

		return doc;
	}
	
	@Override
	public AccessInterface getAccessInterface() {
		return moduleDescriptor;
	}
	
	@Override
	public String getUserMenuAlias() {
		return getFullAlias();
	}

	@Override
	public ExtensionLink getUserMenuExtensionLink(User user) {
		return new ExtensionLink(moduleDescriptor.getName(), getFullAlias(), "u", "2");
	}

}
