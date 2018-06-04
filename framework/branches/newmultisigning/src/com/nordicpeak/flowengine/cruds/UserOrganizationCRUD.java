package com.nordicpeak.flowengine.cruds;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.hierarchy.core.utils.IntegerBasedCRUD;
import se.unlogic.standardutils.dao.AnnotatedDAO;
import se.unlogic.standardutils.dao.AnnotatedDAOWrapper;
import se.unlogic.standardutils.dao.HighLevelQuery;
import se.unlogic.standardutils.dao.QueryOperators;
import se.unlogic.standardutils.dao.QueryParameterFactory;
import se.unlogic.standardutils.validation.ValidationError;
import se.unlogic.standardutils.validation.ValidationException;
import se.unlogic.webutils.http.BeanRequestPopulator;
import se.unlogic.webutils.http.URIParser;

import com.nordicpeak.flowengine.UserOrganizationsModule;
import com.nordicpeak.flowengine.beans.UserOrganization;

public class UserOrganizationCRUD extends IntegerBasedCRUD<UserOrganization, UserOrganizationsModule> {

	protected QueryParameterFactory<UserOrganization, Integer> organizationIDParamFactory;
	
	protected QueryParameterFactory<UserOrganization, User> organizationUserParamFactory;
	
	protected QueryParameterFactory<UserOrganization, String> organizationNameParamFactory;
	
	protected QueryParameterFactory<UserOrganization, String> organizationNumberParamFactory;
	
	protected AnnotatedDAO<UserOrganization> userOrganizationDAO;
	
	public UserOrganizationCRUD(AnnotatedDAOWrapper<UserOrganization, Integer> crudDAO, BeanRequestPopulator<UserOrganization> populator, UserOrganizationsModule callback) {
		
		super(crudDAO, populator, "Organization", "organization", "/", callback);
		
		userOrganizationDAO = crudDAO.getAnnotatedDAO();

		organizationIDParamFactory = crudDAO.getParameterFactory();
		organizationUserParamFactory = userOrganizationDAO.getParamFactory("user", User.class);
		organizationNameParamFactory = userOrganizationDAO.getParamFactory("name", String.class);
		organizationNumberParamFactory = userOrganizationDAO.getParamFactory("organizationNumber", String.class);
	}

	@Override
	protected UserOrganization populateFromAddRequest(HttpServletRequest req, User user, URIParser uriParser) throws ValidationException, Exception {
		
		UserOrganization organization = super.populateFromAddRequest(req, user, uriParser);
		
		organization.setUser(user);
		
		return organization;
		
	}

	@Override
	protected UserOrganization populateFromUpdateRequest(UserOrganization bean, HttpServletRequest req, User user, URIParser uriParser) throws ValidationException, Exception {
		
		UserOrganization organization = super.populateFromUpdateRequest(bean, req, user, uriParser);

		organization.setUser(user);
		
		return organization;
		
	}
	
	@Override
	protected void validateAddPopulation(UserOrganization organization, HttpServletRequest req, User user, URIParser uriParser) throws ValidationException, SQLException, Exception {
		
		validatePopulation(organization, user);
	}

	@Override
	protected void validateUpdatePopulation(UserOrganization organization, HttpServletRequest req, User user, URIParser uriParser) throws ValidationException, SQLException, Exception {
		
		validatePopulation(organization, user);
	}

	protected void validatePopulation(UserOrganization organization, User user) throws SQLException, ValidationException {
		
		List<ValidationError> errors = new ArrayList<ValidationError>();
		
		if(userOrganizationNameExists(organization, user)) {
			errors.add(new ValidationError("NameExists"));
		}
		
		if(userOrganizationNumberExists(organization, user)) {
			errors.add(new ValidationError("OrganizationNumberExists"));
		}
		
		if(!errors.isEmpty()) {
			throw new ValidationException(errors);
		}
		
	}
	
	protected boolean userOrganizationNameExists(UserOrganization organization, User user) throws SQLException {
		
		HighLevelQuery<UserOrganization> query = new HighLevelQuery<UserOrganization>();
		
		if(organization.getOrganizationID() != null) {
			
			query.addParameter(organizationIDParamFactory.getParameter(organization.getOrganizationID(), QueryOperators.NOT_EQUALS));
		
		}
		
		query.addParameter(organizationNameParamFactory.getParameter(organization.getName()));
		query.addParameter(organizationUserParamFactory.getParameter(user));
		
		return userOrganizationDAO.getBoolean(query);
	}
	
	protected boolean userOrganizationNumberExists(UserOrganization organization, User user) throws SQLException {
		
		HighLevelQuery<UserOrganization> query = new HighLevelQuery<UserOrganization>();

		if(organization.getOrganizationID() != null) {
		
			query.addParameter(organizationIDParamFactory.getParameter(organization.getOrganizationID(), QueryOperators.NOT_EQUALS));
		
		}
		
		query.addParameter(organizationNumberParamFactory.getParameter(organization.getOrganizationNumber()));
		query.addParameter(organizationUserParamFactory.getParameter(user));
		
		
		return userOrganizationDAO.getBoolean(query);
	}
	
	@Override
	protected List<UserOrganization> getAllBeans(User user) throws SQLException {
		
		HighLevelQuery<UserOrganization> query = new HighLevelQuery<UserOrganization>();
		
		query.addParameter(organizationUserParamFactory.getParameter(user));
		
		return userOrganizationDAO.getAll(query);
	
	}
	
	public List<UserOrganization> getUserOrganizations(User user) throws SQLException {
		
		return getAllBeans(user);
		
	}
	
	public UserOrganization update(User user, UserOrganization organization) throws SQLException, ValidationException  {

		validatePopulation(organization, user);
		
		log.info("User " + user + " updating " + this.typeLogName + " " + organization);

		crudDAO.update(organization);

		return organization;
	}
	
	public UserOrganization add(User user, UserOrganization organization) throws SQLException, ValidationException  {

		validatePopulation(organization, user);
		
		log.info("User " + user + " adding " + this.typeLogName + " " + organization);

		crudDAO.add(organization);

		return organization;
	}

}
