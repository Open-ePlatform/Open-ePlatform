package com.nordicpeak.flowengine;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import se.unlogic.hierarchy.backgroundmodules.AnnotatedBackgroundModule;
import se.unlogic.hierarchy.core.annotations.ModuleSetting;
import se.unlogic.hierarchy.core.beans.MenuItem;
import se.unlogic.hierarchy.core.beans.SectionMenu;
import se.unlogic.hierarchy.core.beans.SettingDescriptor;
import se.unlogic.hierarchy.core.beans.SimpleBackgroundModuleResponse;
import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.hierarchy.core.beans.ValueDescriptor;
import se.unlogic.hierarchy.core.interfaces.BackgroundModuleResponse;
import se.unlogic.hierarchy.core.interfaces.SectionDescriptor;
import se.unlogic.hierarchy.core.sections.Section;
import se.unlogic.hierarchy.core.utils.ModuleUtils;
import se.unlogic.standardutils.xml.XMLUtils;
import se.unlogic.webutils.http.URIParser;


public class UserFlowInstanceMenuModule extends AnnotatedBackgroundModule {

	@ModuleSetting
	protected Integer sectionID;
	
	@Override
	protected BackgroundModuleResponse processBackgroundRequest(HttpServletRequest req, User user, URIParser uriParser) throws Exception {
		
		if(sectionID != null) {
		
			Entry<SectionDescriptor, Section> section = systemInterface.getRootSection().getSectionCache().getEntry(sectionID);
			
			if(section != null){

				SectionMenu sectionMenu = section.getValue().getMenuCache().getUserMenu(user, null, uriParser);
				
				Document doc = this.createDocument(req, uriParser, user);
				
				Element document = doc.getDocumentElement();
				
				document.appendChild(section.getKey().toXML(doc));
				
				for(MenuItem menuItem : sectionMenu.getMenuItems()) {
					document.appendChild(menuItem.toXML(doc));
				}
				
				return new SimpleBackgroundModuleResponse(doc);				
			}
		}
		
		return null;
	}
	
	public Document createDocument(HttpServletRequest req, URIParser uriParser, User user) {

		Document doc = XMLUtils.createDomDocument();
		Element document = doc.createElement("Document");
		document.appendChild(XMLUtils.createElement("contextpath", req.getContextPath(), doc));
		doc.appendChild(document);
		return doc;
	}

	@Override
	public List<SettingDescriptor> getSettings() {

		ArrayList<SettingDescriptor> settingDescriptors = new ArrayList<SettingDescriptor>();

		List<SectionDescriptor> sections = systemInterface.getRootSection().getSectionCache().getCachedSections();

		List<ValueDescriptor> valueDescriptors = new ArrayList<ValueDescriptor>();

		if(sections != null) {

			for(SectionDescriptor section : sections) {

				valueDescriptors.add(new ValueDescriptor(section.getName(), section.getSectionID()));

			}

		}

		settingDescriptors.add(SettingDescriptor.createDropDownSetting("sectionID", "Section", "Choose which sectionmenu to show in this module", true, "", valueDescriptors));

		ModuleUtils.addSettings(settingDescriptors, super.getSettings());

		return settingDescriptors;

	}
	
}
