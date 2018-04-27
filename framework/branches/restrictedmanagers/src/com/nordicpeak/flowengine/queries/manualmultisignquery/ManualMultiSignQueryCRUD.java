package com.nordicpeak.flowengine.queries.manualmultisignquery;

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

public class ManualMultiSignQueryCRUD extends BaseQueryCRUD<ManualMultiSignQuery, ManualMultiSignQueryProviderModule> {

	protected AnnotatedDAOWrapper<ManualMultiSignQuery, Integer> queryDAO;

	public ManualMultiSignQueryCRUD(AnnotatedDAOWrapper<ManualMultiSignQuery, Integer> queryDAO, BeanRequestPopulator<ManualMultiSignQuery> populator, String typeElementName, String typeLogName, String listMethodAlias, ManualMultiSignQueryProviderModule callback) {

		super(ManualMultiSignQuery.class, queryDAO, populator, typeElementName, typeLogName, listMethodAlias, callback);

		this.queryDAO = queryDAO;
	}

	@Override
	protected ManualMultiSignQuery populateFromUpdateRequest(ManualMultiSignQuery bean, HttpServletRequest req, User user, URIParser uriParser) throws ValidationException, Exception {

		ManualMultiSignQuery query = super.populateFromUpdateRequest(bean, req, user, uriParser);

		List<ValidationError> validationErrors = new ArrayList<ValidationError>();

		this.populateQueryDescriptor((QueryDescriptor) query.getQueryDescriptor(), req, validationErrors);

		if(!validationErrors.isEmpty()) {
			throw new ValidationException(validationErrors);
		}

		return query;
	}

}
