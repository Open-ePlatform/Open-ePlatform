package com.nordicpeak.flowengine.queries.childquery;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.standardutils.dao.AnnotatedDAOWrapper;
import se.unlogic.standardutils.validation.ValidationError;
import se.unlogic.standardutils.validation.ValidationException;
import se.unlogic.webutils.http.BeanRequestPopulator;
import se.unlogic.webutils.http.URIParser;

import com.nordicpeak.flowengine.beans.QueryDescriptor;
import com.nordicpeak.flowengine.queries.basequery.BaseQueryCRUD;
import com.nordicpeak.flowengine.queries.childquery.beans.ChildQuery;

public class ChildQueryCRUD extends BaseQueryCRUD<ChildQuery, ChildQueryProviderModule> {

	protected AnnotatedDAOWrapper<ChildQuery, Integer> queryDAO;

	public ChildQueryCRUD(AnnotatedDAOWrapper<ChildQuery, Integer> queryDAO, BeanRequestPopulator<ChildQuery> populator, String typeElementName, String typeLogName, String listMethodAlias, ChildQueryProviderModule callback) {

		super(ChildQuery.class, queryDAO, populator, typeElementName, typeLogName, listMethodAlias, callback);

		this.queryDAO = queryDAO;
	}

	@Override
	protected ChildQuery populateFromUpdateRequest(ChildQuery bean, HttpServletRequest req, User user, URIParser uriParser) throws ValidationException, Exception {

		ChildQuery query = super.populateFromUpdateRequest(bean, req, user, uriParser);

		if (query.isUseMultipartSigning()) {

			String requiredContactWays = req.getParameter("contactWays");

			if ("both".equals(requiredContactWays)) {

				query.setRequireGuardianEmail(true);
				query.setRequireGuardianPhone(true);

			} else if ("email".equals(requiredContactWays)) {

				query.setRequireGuardianEmail(true);
				query.setRequireGuardianPhone(false);

			} else if ("phone".equals(requiredContactWays)) {

				query.setRequireGuardianEmail(false);
				query.setRequireGuardianPhone(true);

			} else {

				query.setRequireGuardianEmail(false);
				query.setRequireGuardianPhone(false);
			}
		}

		List<ValidationError> validationErrors = new ArrayList<ValidationError>();

		this.populateQueryDescriptor((QueryDescriptor) query.getQueryDescriptor(), req, validationErrors);

		if(query.getMinAge() != null && query.getMaxAge() != null && query.getMinAge() > query.getMaxAge()){

			validationErrors.add(new ValidationError("MinAgeLargerThanMaxAge"));
		}

		if (!validationErrors.isEmpty()) {
			throw new ValidationException(validationErrors);
		}

		return query;
	}

}
