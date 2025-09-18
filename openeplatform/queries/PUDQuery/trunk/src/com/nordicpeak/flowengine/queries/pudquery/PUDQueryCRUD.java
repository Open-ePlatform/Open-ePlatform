package com.nordicpeak.flowengine.queries.pudquery;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.standardutils.dao.AnnotatedDAOWrapper;
import se.unlogic.standardutils.populators.EnumPopulator;
import se.unlogic.standardutils.validation.ValidationError;
import se.unlogic.standardutils.validation.ValidationException;
import se.unlogic.webutils.http.BeanRequestPopulator;
import se.unlogic.webutils.http.URIParser;

import com.nordicpeak.flowengine.beans.QueryDescriptor;
import com.nordicpeak.flowengine.queries.basequery.BaseQueryCRUD;

public class PUDQueryCRUD extends BaseQueryCRUD<PUDQuery, PUDQueryProviderModule> {
	
	protected static EnumPopulator<SearchService> SEARCH_SERVICE_POPULATOR = new EnumPopulator<SearchService>(SearchService.class);
	
	public PUDQueryCRUD(AnnotatedDAOWrapper<PUDQuery, Integer> queryDAO, BeanRequestPopulator<PUDQuery> populator, String typeElementName, String typeLogName, String listMethodAlias, PUDQueryProviderModule callback) {
		
		super(PUDQuery.class, queryDAO, populator, typeElementName, typeLogName, listMethodAlias, callback);
		
	}
	
	@Override
	protected PUDQuery populateFromUpdateRequest(PUDQuery bean, HttpServletRequest req, User user, URIParser uriParser) throws ValidationException, Exception {
		
		PUDQuery query = super.populateFromUpdateRequest(bean, req, user, uriParser);
		
		List<ValidationError> validationErrors = new ArrayList<ValidationError>();
		
		this.populateQueryDescriptor((QueryDescriptor) query.getQueryDescriptor(), req, validationErrors);
		
		if (query.isUseAddressAsResult()) {
			
			query.setAllowedSearchServices(Collections.singletonList(SearchService.ADDRESS));
		}
		
		if (!validationErrors.isEmpty()) {
			throw new ValidationException(validationErrors);
		}
		
		return query;
	}
	
}
