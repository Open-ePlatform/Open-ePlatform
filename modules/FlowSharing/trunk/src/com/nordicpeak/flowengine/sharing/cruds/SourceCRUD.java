package com.nordicpeak.flowengine.sharing.cruds;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.hierarchy.core.exceptions.AccessDeniedException;
import se.unlogic.hierarchy.core.exceptions.URINotFoundException;
import se.unlogic.hierarchy.core.utils.AccessUtils;
import se.unlogic.hierarchy.core.utils.IntegerBasedCRUD;
import se.unlogic.standardutils.dao.AnnotatedDAO;
import se.unlogic.standardutils.dao.AnnotatedDAOWrapper;
import se.unlogic.standardutils.dao.HighLevelQuery;
import se.unlogic.standardutils.dao.QueryParameterFactory;
import se.unlogic.standardutils.serialization.SerializationUtils;
import se.unlogic.standardutils.string.StringUtils;
import se.unlogic.standardutils.validation.ValidationError;
import se.unlogic.standardutils.validation.ValidationErrorType;
import se.unlogic.standardutils.validation.ValidationException;
import se.unlogic.webutils.http.URIParser;
import se.unlogic.webutils.populators.annotated.AnnotatedRequestPopulator;

import com.nordicpeak.flowengine.sharing.FlowRepositoryModule;
import com.nordicpeak.flowengine.sharing.beans.Repository;
import com.nordicpeak.flowengine.sharing.beans.Source;

public class SourceCRUD extends IntegerBasedCRUD<Source, FlowRepositoryModule> {
	
	private AnnotatedDAO<Source> sourceDAO;
	private QueryParameterFactory<Source, Repository> sourceRepositoryParamFactory;

	public SourceCRUD(AnnotatedDAOWrapper<Source, Integer> crudDAO, QueryParameterFactory<Source, Repository> sourceRepositoryParamFactory, FlowRepositoryModule callback) {

		super(crudDAO, new AnnotatedRequestPopulator<Source>(Source.class), "Source", "source", "", callback);
		sourceDAO = crudDAO.getAnnotatedDAO();
		this.sourceRepositoryParamFactory = sourceRepositoryParamFactory;
	}
	
	@Override
	protected List<Source> getAllBeans(User user) throws SQLException {

		HighLevelQuery<Source> query = new HighLevelQuery<Source>();
		query.addParameter(sourceRepositoryParamFactory.getParameter(callback.getRepository()));
		
		return sourceDAO.getAll(query);
	}

	public Source populate(Source newSource, Source oldSource, HttpServletRequest req, User user, URIParser uriParser) throws Exception {
		
		newSource.setRepository(callback.getRepository());

		String oldPassword = oldSource == null ? null : oldSource.getPassword();
		String newPassword = newSource.getPassword();

		if (newPassword == null) {

			newSource.setPassword(oldPassword);

		} else {

			newSource.setPassword(callback.getHashedPassword(newPassword));
		}

		return newSource;
	}

	@Override
	protected Source populateFromAddRequest(HttpServletRequest req, User user, URIParser uriParser) throws ValidationException, Exception {

		return populate(super.populateFromAddRequest(req, user, uriParser), null, req, user, uriParser);
	}

	@Override
	protected Source populateFromUpdateRequest(Source oldSource, HttpServletRequest req, User user, URIParser uriParser) throws ValidationException, Exception {

		Source copy = SerializationUtils.cloneSerializable(oldSource);
		
		return populate(super.populateFromUpdateRequest(oldSource, req, user, uriParser), copy, req, user, uriParser);
	}

	@Override
	protected void validateAddPopulation(Source source, HttpServletRequest req, User user, URIParser uriParser) throws ValidationException, SQLException, Exception {

		super.validateAddPopulation(source, req, user, uriParser);

		if (StringUtils.isEmpty(source.getPassword())) {

			throw new ValidationException(new ValidationError("password", ValidationErrorType.RequiredField));
		}

		validatePopulation(source, req, user, uriParser);
	}

	@Override
	protected void validateUpdatePopulation(Source bean, HttpServletRequest req, User user, URIParser uriParser) throws ValidationException, SQLException, Exception {

		super.validateUpdatePopulation(bean, req, user, uriParser);

		validatePopulation(bean, req, user, uriParser);
	}

	protected void validatePopulation(Source source, HttpServletRequest req, User user, URIParser uriParser) throws ValidationException, SQLException {

		ArrayList<ValidationError> validationErrors = new ArrayList<ValidationError>(3);

		Source sourceMatch = callback.getSource(source.getUsername());

		if (sourceMatch != null && !sourceMatch.getSourceID().equals(source.getSourceID())) {

			validationErrors.add(new ValidationError("UsernameAlreadyTaken"));
		}

		if (req.getParameter("password") != null && !req.getParameter("password").equals(req.getParameter("passwordconfirmation"))) {

			validationErrors.add(new ValidationError("PasswordConfirmationMissMatch"));
		}

		if (!validationErrors.isEmpty()) {

			throw new ValidationException(validationErrors);
		}
	}

	@Override
	protected void checkAddAccess(User user, HttpServletRequest req, URIParser uriParser) throws AccessDeniedException, URINotFoundException, SQLException {

		checkAccess(user);
		super.checkAddAccess(user, req, uriParser);
	}

	@Override
	protected void checkUpdateAccess(Source bean, User user, HttpServletRequest req, URIParser uriParser) throws AccessDeniedException, URINotFoundException, SQLException {

		checkAccess(user);
		super.checkUpdateAccess(bean, user, req, uriParser);
	}

	@Override
	protected void checkDeleteAccess(Source bean, User user, HttpServletRequest req, URIParser uriParser) throws AccessDeniedException, URINotFoundException, SQLException {

		checkAccess(user);
		super.checkDeleteAccess(bean, user, req, uriParser);
	}

	@Override
	protected void checkShowAccess(Source bean, User user, HttpServletRequest req, URIParser uriParser) throws AccessDeniedException, URINotFoundException, SQLException {

		checkAccess(user);
		super.checkShowAccess(bean, user, req, uriParser);
	}

	@Override
	protected void checkListAccess(User user, HttpServletRequest req, URIParser uriParser) throws AccessDeniedException, URINotFoundException, SQLException {

		checkAccess(user);
		super.checkListAccess(user, req, uriParser);
	}

	public void checkAccess(User user) throws AccessDeniedException {

		if (!AccessUtils.checkAccess(user, callback)) {

			throw new AccessDeniedException("User does not have access to administer sources");
		}
	}

}
