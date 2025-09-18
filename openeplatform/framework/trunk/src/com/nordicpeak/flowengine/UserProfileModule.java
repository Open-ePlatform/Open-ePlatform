package com.nordicpeak.flowengine;

import javax.servlet.http.HttpServletRequest;

import org.w3c.dom.Document;

import se.unlogic.hierarchy.core.annotations.InstanceManagerDependency;
import se.unlogic.hierarchy.core.annotations.ModuleSetting;
import se.unlogic.hierarchy.core.annotations.TextFieldSettingDescriptor;
import se.unlogic.hierarchy.core.annotations.XSLVariable;
import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.hierarchy.core.interfaces.AccessInterface;
import se.unlogic.hierarchy.core.utils.extensionlinks.ExtensionLink;
import se.unlogic.standardutils.xml.XMLUtils;
import se.unlogic.webutils.http.URIParser;

import com.nordicpeak.flowengine.interfaces.UserMenuProvider;

public class UserProfileModule extends se.unlogic.hierarchy.foregroundmodules.userprofile.UserProfileModule implements UserMenuProvider {

	@XSLVariable(prefix = "java.")
	private String userMenuTabTitle = "My details";
	
	@ModuleSetting(allowsNull = true)
	@TextFieldSettingDescriptor(name = "Cancel redirect URI", description = "Cancel redirect URI")
	protected String cancelRedirectURI;

	@ModuleSetting
	@TextFieldSettingDescriptor(name = "User menu item slot", description = "User menu item slot")
	protected String userMenuExtensionLinkSlot = "20";
	
	protected UserFlowInstanceMenuModule userFlowInstanceMenuModule;
	
	protected ExtensionLink userMenuLink;

	@Override
	public void unload() throws Exception {

		if(userFlowInstanceMenuModule != null){
			
			setUserFlowInstanceMenuModule(null);
		}
		
		super.unload();
	}
	
	@Override
	protected void moduleConfigured() throws Exception {

		super.moduleConfigured();
		
		this.userMenuLink = new ExtensionLink(userMenuTabTitle, getFullAlias(), "u", userMenuExtensionLinkSlot);
		
		if(userFlowInstanceMenuModule != null) {
			
			userFlowInstanceMenuModule.sortProviders();
		}
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
		return userMenuLink;
	}

	@Override
	public String getUserMenuPriority() {

		return userMenuExtensionLinkSlot;
	}
}
