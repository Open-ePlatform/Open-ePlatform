package com.nordicpeak.flowengine;

import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import se.unlogic.hierarchy.backgroundmodules.AnnotatedBackgroundModule;
import se.unlogic.hierarchy.core.annotations.DropDownSettingDescriptor;
import se.unlogic.hierarchy.core.annotations.InstanceManagerDependency;
import se.unlogic.hierarchy.core.annotations.ModuleSetting;
import se.unlogic.hierarchy.core.annotations.TextFieldSettingDescriptor;
import se.unlogic.hierarchy.core.beans.SimpleBackgroundModuleResponse;
import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.hierarchy.core.interfaces.BackgroundModuleResponse;
import se.unlogic.hierarchy.core.utils.HierarchyAnnotatedDAOFactory;
import se.unlogic.standardutils.xml.XMLUtils;
import se.unlogic.webutils.http.URIParser;

import com.nordicpeak.flowengine.beans.UserFavourite;
import com.nordicpeak.flowengine.dao.UserFavouriteDAO;

public class UserFavouriteBackgroundModule extends AnnotatedBackgroundModule {

	@ModuleSetting
	@DropDownSettingDescriptor(name="Module mode",description="Specifies whether show or edit mode should be used in this module",required=true,values={"SHOW", "EDIT"},valueDescriptions={"Show", "Edit"})
	protected String mode = "SHOW";
	
	@ModuleSetting
	@TextFieldSettingDescriptor(name="Edit favourites module alias", description="Full alias of the edit favourites module", required=false)
	protected String editFavouritesAlias;
	
	@InstanceManagerDependency(required=true)
	protected FlowBrowserModule flowBrowserModule;
	
	protected UserFavouriteDAO userFavouriteDAO;
	
	@Override
	protected void createDAOs(DataSource dataSource) throws Exception {
		
		super.createDAOs(dataSource);
	
		userFavouriteDAO = new UserFavouriteDAO(dataSource, UserFavourite.class, new HierarchyAnnotatedDAOFactory(dataSource, systemInterface.getUserHandler(), systemInterface.getGroupHandler()));
	}
	
	@Override
	protected BackgroundModuleResponse processBackgroundRequest(HttpServletRequest req, User user, URIParser uriParser) throws Exception {
		
		Document doc = this.createDocument(req, uriParser, user);
		
		Element document = doc.getDocumentElement();
		
		XMLUtils.append(doc, document, userFavouriteDAO.getAll(user, req.getSession(), flowBrowserModule.getLatestPublishedFlowVersionMap()));
		XMLUtils.appendNewElement(doc, document, "flowBrowserAlias", flowBrowserModule.getFullAlias());
		XMLUtils.appendNewElement(doc, document, "userFavouriteModuleAlias", flowBrowserModule.getUserFavouriteModuleAlias());
		XMLUtils.appendNewElement(doc, document, "editFavouritesAlias", editFavouritesAlias);
		XMLUtils.appendNewElement(doc, document, "mode", mode);
		
		return new SimpleBackgroundModuleResponse(doc);
		
	}

	public Document createDocument(HttpServletRequest req, URIParser uriParser, User user) {

		Document doc = XMLUtils.createDomDocument();
		Element document = doc.createElement("Document");
		document.appendChild(this.moduleDescriptor.toXML(doc));
		document.appendChild(XMLUtils.createElement("contextpath", req.getContextPath(), doc));
		doc.appendChild(document);
		return doc;
	}
	
}
