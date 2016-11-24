package com.nordicpeak.flowengine.queries.basemapquery;

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
import com.nordicpeak.flowengine.queries.basequery.BaseQueryProviderModule;

public class BaseMapQueryCRUD<MapQueryType extends BaseMapQuery, MapQueryInstanceType extends BaseMapQueryInstance<MapQueryType>, MapQueryProviderType extends BaseQueryProviderModule<MapQueryInstanceType>> extends BaseQueryCRUD<MapQueryType, MapQueryProviderType> {

	public BaseMapQueryCRUD(Class<MapQueryType> mapQueryClass, AnnotatedDAOWrapper<MapQueryType, Integer> queryDAO, BeanRequestPopulator<MapQueryType> populator, String typeElementName, String typeLogName, String listMethodAlias,MapQueryProviderType callback) {
		
		super(mapQueryClass, queryDAO, populator, typeElementName, typeLogName, listMethodAlias, callback);
		
	}

	@Override
	protected MapQueryType populateFromUpdateRequest(MapQueryType bean, HttpServletRequest req, User user, URIParser uriParser) throws ValidationException, Exception {
		
		MapQueryType query = super.populateFromUpdateRequest(bean, req, user, uriParser);
		
		List<ValidationError> validationErrors = new ArrayList<ValidationError>();
		
		this.populateQueryDescriptor((QueryDescriptor) query.getQueryDescriptor(), req, validationErrors);
		
		if(!validationErrors.isEmpty()) {
			throw new ValidationException(validationErrors);
		}

		return query;
	}
	
}
