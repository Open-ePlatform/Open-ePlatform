package com.nordicpeak.flowengine.persondatasavinginformer.query;

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

public class PersonDataInformerQueryCRUD extends BaseQueryCRUD<PersonDataInformerQuery, PersonDataInformerQueryProviderModule> {
	
	public PersonDataInformerQueryCRUD(AnnotatedDAOWrapper<PersonDataInformerQuery, Integer> queryDAO, BeanRequestPopulator<PersonDataInformerQuery> populator, String typeElementName, String typeLogName, String listMethodAlias, PersonDataInformerQueryProviderModule callback) {
		
		super(PersonDataInformerQuery.class, queryDAO, populator, typeElementName, typeLogName, listMethodAlias, callback);
	}
	
	@Override
	protected PersonDataInformerQuery populateFromUpdateRequest(PersonDataInformerQuery bean, HttpServletRequest req, User user, URIParser uriParser) throws ValidationException, Exception {

		PersonDataInformerQuery query = super.populateFromUpdateRequest(bean, req, user, uriParser);

		List<ValidationError> validationErrors = new ArrayList<ValidationError>();

		this.populateQueryDescriptor((QueryDescriptor) query.getQueryDescriptor(), req, validationErrors);
		
		if (!validationErrors.isEmpty()) {
			throw new ValidationException(validationErrors);
		}

		return query;
	}
}
