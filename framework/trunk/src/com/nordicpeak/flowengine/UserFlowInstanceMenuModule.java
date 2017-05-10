package com.nordicpeak.flowengine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import se.unlogic.hierarchy.backgroundmodules.AnnotatedBackgroundModule;
import se.unlogic.hierarchy.core.beans.SimpleBackgroundModuleDescriptor;
import se.unlogic.hierarchy.core.beans.SimpleBackgroundModuleResponse;
import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.hierarchy.core.enums.SystemStatus;
import se.unlogic.hierarchy.core.interfaces.BackgroundModuleDescriptor;
import se.unlogic.hierarchy.core.interfaces.BackgroundModuleResponse;
import se.unlogic.hierarchy.core.interfaces.SectionInterface;
import se.unlogic.hierarchy.core.interfaces.SystemStartupListener;
import se.unlogic.hierarchy.core.utils.AccessUtils;
import se.unlogic.hierarchy.core.utils.extensionlinks.ExtensionLink;
import se.unlogic.standardutils.string.StringUtils;
import se.unlogic.standardutils.xml.XMLUtils;
import se.unlogic.webutils.http.URIParser;

import com.nordicpeak.flowengine.comparators.UserMenuProviderComparator;
import com.nordicpeak.flowengine.interfaces.UserMenuProvider;

public class UserFlowInstanceMenuModule extends AnnotatedBackgroundModule implements SystemStartupListener, Runnable {
	
	private static final UserMenuProviderComparator COMPARATOR = new UserMenuProviderComparator();
	
	public static final String REQUEST_DISABLE_MENU = UserFlowInstanceMenuModule.class.getSimpleName() + "_disable";
	
	protected ArrayList<UserMenuProvider> extensionLinkProviders = new ArrayList<UserMenuProvider>();
	
	@Override
	public void init(BackgroundModuleDescriptor descriptor, SectionInterface sectionInterface, DataSource dataSource) throws Exception {
		super.init(descriptor, sectionInterface, dataSource);
		
		if (!systemInterface.getInstanceHandler().addInstance(UserFlowInstanceMenuModule.class, this)) {
			
			throw new RuntimeException("Unable to register module in global instance handler using key " + UserFlowInstanceMenuModule.class.getSimpleName() + ", another instance is already registered using this key.");
		}
		
		systemInterface.addStartupListener(this);
	}
	
	@Override
	public void unload() throws Exception {
		
		systemInterface.getInstanceHandler().removeInstance(UserFlowInstanceMenuModule.class, this);
		
		extensionLinkProviders.clear();
		
		super.unload();
	}
	
	@Override
	public void systemStarted() throws Exception {
		
		if (sectionInterface.getBackgroundModuleCache().isBeingCached(moduleDescriptor)) {
			
			log.info("Delaying alias generation until module is done caching");
			new Thread(this).start();
			
		} else {
			
			generateAliases();
		}
	}
	
	private void generateAliases() {
		
		if (!sectionInterface.getBackgroundModuleCache().isCached(moduleDescriptor)) {
			
			log.error("Invalid state, module must be cached to allow generation of aliases!");
			return;
		}
		
		List<String> aliases = new ArrayList<String>();
		
		for (UserMenuProvider extensionLinkProvider : extensionLinkProviders) {
			
			String alias = extensionLinkProvider.getUserMenuAlias();
			
			if (!StringUtils.isEmpty(alias)) {
				
				if (alias.startsWith("/")) {
					alias = alias.substring(1);
				}
				
				aliases.add(alias + "*");
			}
		}
		
		SimpleBackgroundModuleDescriptor backgroundModuleDescriptor = (SimpleBackgroundModuleDescriptor) moduleDescriptor;
		backgroundModuleDescriptor.setAliases(aliases);
		
		try {
			sectionInterface.getBackgroundModuleCache().update(backgroundModuleDescriptor);
			
		} catch (Exception e) {
			log.error("Error updating aliases", e);
		}
		
		log.info("Generated " + aliases.size() + " aliases. " + StringUtils.toCommaSeparatedString(aliases));
	}
	
	@Override
	public void run() {
		
		try {
			while (sectionInterface.getBackgroundModuleCache().isBeingCached(moduleDescriptor)) {
				Thread.sleep(100);
			}
			
			generateAliases();
			
		} catch (Throwable t) {
			log.error("Error while generating aliases", t);
		}
	}
	
	@Override
	protected BackgroundModuleResponse processBackgroundRequest(HttpServletRequest req, User user, URIParser uriParser) throws Exception {
		
		Boolean override = (Boolean) req.getAttribute(REQUEST_DISABLE_MENU);
		
		if (override != null && override) {
			return null;
		}
		
		Document doc = this.createDocument(req, uriParser, user);
		
		Element document = doc.getDocumentElement();
		
		String formattedURI = uriParser.getFormattedURI();
		
		for (UserMenuProvider linkProvider : extensionLinkProviders) {
			
			if (linkProvider.getAccessInterface() == null || AccessUtils.checkAccess(user, linkProvider.getAccessInterface())) {
				
				try {
					ExtensionLink link = linkProvider.getUserMenuExtensionLink();
					
					if (link != null) {
						
						Element linkElement = (Element) document.appendChild(link.toXML(doc));
						
						if (formattedURI.startsWith(linkProvider.getUserMenuAlias())) {
							
							XMLUtils.appendNewElement(doc, linkElement, "Active");
						}
					}
					
				} catch (Exception e) {
					
					log.error("Error getting extension link from provider " + linkProvider, e);
				}
			}
		}
		
		return new SimpleBackgroundModuleResponse(doc);
	}
	
	public Document createDocument(HttpServletRequest req, URIParser uriParser, User user) {
		
		Document doc = XMLUtils.createDomDocument();
		Element document = doc.createElement("Document");
		document.appendChild(XMLUtils.createElement("contextpath", req.getContextPath(), doc));
		doc.appendChild(document);
		return doc;
	}
	
	public synchronized void addUserMenuProvider(UserMenuProvider e) {
		
		if (!extensionLinkProviders.contains(e)) {
			
			extensionLinkProviders.add(e);
			
			sortProviders();
			
			log.info("User menu provider " + e + " added");
			
			if (systemInterface.getSystemStatus() == SystemStatus.STARTED && sectionInterface.getBackgroundModuleCache().isCached(moduleDescriptor)) {
				
				generateAliases();
			}
		}
	}
	
	public synchronized void removeUserMenuProvider(UserMenuProvider e) {
		
		if(extensionLinkProviders.remove(e)) {
		
			log.info("User menu provider " + e + " removed");
		
			if (systemInterface.getSystemStatus() == SystemStatus.STARTED && sectionInterface.getBackgroundModuleCache().isCached(moduleDescriptor)) {
			
				generateAliases();
			}
		}
	}
	
	public synchronized void sortProviders() {
		
		Collections.sort(extensionLinkProviders, COMPARATOR);
	}
	
}
