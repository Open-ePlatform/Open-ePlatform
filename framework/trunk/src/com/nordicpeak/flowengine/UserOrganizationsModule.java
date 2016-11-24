package com.nordicpeak.flowengine;

import java.sql.SQLException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import se.unlogic.hierarchy.core.annotations.WebPublic;
import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.hierarchy.core.exceptions.AccessDeniedException;
import se.unlogic.hierarchy.core.interfaces.ForegroundModuleDescriptor;
import se.unlogic.hierarchy.core.interfaces.ForegroundModuleResponse;
import se.unlogic.hierarchy.core.interfaces.SectionInterface;
import se.unlogic.hierarchy.core.utils.CRUDCallback;
import se.unlogic.hierarchy.core.utils.HierarchyAnnotatedDAOFactory;
import se.unlogic.hierarchy.foregroundmodules.AnnotatedForegroundModule;
import se.unlogic.standardutils.dao.AnnotatedDAO;
import se.unlogic.standardutils.validation.ValidationException;
import se.unlogic.standardutils.xml.XMLUtils;
import se.unlogic.webutils.http.RequestUtils;
import se.unlogic.webutils.http.URIParser;
import se.unlogic.webutils.populators.annotated.AnnotatedRequestPopulator;

import com.nordicpeak.flowengine.beans.UserOrganization;
import com.nordicpeak.flowengine.cruds.UserOrganizationCRUD;

public class UserOrganizationsModule extends AnnotatedForegroundModule implements CRUDCallback<User> {

	protected UserOrganizationCRUD userOrganizationCRUD;
	
	@Override
	public void init(ForegroundModuleDescriptor moduleDescriptor, SectionInterface sectionInterface, DataSource dataSource) throws Exception {
		
		super.init(moduleDescriptor, sectionInterface, dataSource);
		
		if(!systemInterface.getInstanceHandler().addInstance(UserOrganizationsModule.class, this)){

			throw new RuntimeException("Unable to register module in global instance handler using key " + UserOrganizationsModule.class.getSimpleName() + ", another instance is already registered using this key.");
		}
		
	}
	
	@Override
	public ForegroundModuleResponse defaultMethod(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Throwable {
		
		return userOrganizationCRUD.list(req, res, user, uriParser, null);
	}

	@Override
	protected void createDAOs(DataSource dataSource) throws Exception {

		super.createDAOs(dataSource);
		
		HierarchyAnnotatedDAOFactory daoFactory = new HierarchyAnnotatedDAOFactory(dataSource, systemInterface.getUserHandler(), systemInterface.getGroupHandler());
		
		AnnotatedDAO<UserOrganization> organizationDAO = daoFactory.getDAO(UserOrganization.class);
		
		userOrganizationCRUD = new UserOrganizationCRUD(organizationDAO.getWrapper(Integer.class), new AnnotatedRequestPopulator<UserOrganization>(UserOrganization.class), this);
		
	}
	
	@WebPublic(alias = "add")
	public ForegroundModuleResponse addOrganization(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Exception {

		return userOrganizationCRUD.add(req, res, user, uriParser);
	}
	
	@WebPublic(alias = "update")
	public ForegroundModuleResponse updateOrganization(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Exception {

		return userOrganizationCRUD.update(req, res, user, uriParser);
	}
	
	@WebPublic(alias = "delete")
	public ForegroundModuleResponse deleteOrganization(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Exception {

		return userOrganizationCRUD.delete(req, res, user, uriParser);
	}
	
	public List<UserOrganization> getUserOrganizations(User user) throws SQLException {
		
		return userOrganizationCRUD.getUserOrganizations(user);
	}
	
	public UserOrganization getOrganization(Integer organizationID) throws SQLException, AccessDeniedException {
		
		return userOrganizationCRUD.getBean(organizationID);
	}
	
	public UserOrganization addOrganization(User user, UserOrganization organization) throws SQLException, ValidationException  {
		
		return userOrganizationCRUD.add(user, organization);		
	}
	
	public UserOrganization updateOrganization(User user, UserOrganization organization) throws SQLException, ValidationException {
		
		return userOrganizationCRUD.update(user, organization);
	}

	@Override
	public Document createDocument(HttpServletRequest req, URIParser uriParser, User user) {
		
		Document doc = XMLUtils.createDomDocument();
		Element document = doc.createElement("Document");
		document.appendChild(RequestUtils.getRequestInfoAsXML(doc, req, uriParser));
		document.appendChild(this.moduleDescriptor.toXML(doc));
		doc.appendChild(document);
		return doc;
		
	}

	@Override
	public String getTitlePrefix() {
		
		return this.moduleDescriptor.getName();
	}
	
	@Override
	public void unload() throws Exception {

		super.unload();

		systemInterface.getInstanceHandler().removeInstance(UserOrganizationsModule.class, this);
	}
	
}
