package com.nordicpeak.flowengine.queries.stopquery;

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

public class StopQueryCRUD extends BaseQueryCRUD<StopQuery, StopQueryProviderModule> {

	protected AnnotatedDAOWrapper<StopQuery, Integer> queryDAO;
	
	public StopQueryCRUD(AnnotatedDAOWrapper<StopQuery, Integer> queryDAO, BeanRequestPopulator<StopQuery> populator, String typeElementName, String typeLogName, String listMethodAlias, StopQueryProviderModule callback) {
		
		super(StopQuery.class, queryDAO, populator, typeElementName, typeLogName, listMethodAlias, callback);
		
		this.queryDAO = queryDAO;
	}

	@Override
	protected StopQuery populateFromUpdateRequest(StopQuery oldQuery, HttpServletRequest req, User user, URIParser uriParser) throws ValidationException, Exception {
		
		StopQuery query = super.populateFromUpdateRequest(oldQuery, req, user, uriParser);
		
		List<ValidationError> validationErrors = new ArrayList<ValidationError>();

		this.populateQueryDescriptor((QueryDescriptor) query.getQueryDescriptor(), req, validationErrors);
		
		if(!validationErrors.isEmpty()) {
			throw new ValidationException(validationErrors);
		}

		return query;
	}

}
