package com.nordicpeak.flowengine.queries.dropdownquery;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.standardutils.collections.CollectionUtils;
import se.unlogic.standardutils.dao.AnnotatedDAOWrapper;
import se.unlogic.standardutils.string.StringUtils;
import se.unlogic.standardutils.validation.ValidationError;
import se.unlogic.standardutils.validation.ValidationErrorType;
import se.unlogic.standardutils.validation.ValidationException;
import se.unlogic.webutils.http.BeanRequestPopulator;
import se.unlogic.webutils.http.URIParser;

import com.nordicpeak.flowengine.beans.QueryDescriptor;
import com.nordicpeak.flowengine.populators.AlternativesPopulator;
import com.nordicpeak.flowengine.queries.basequery.BaseQueryCRUD;

public class DropDownQueryCRUD extends BaseQueryCRUD<DropDownQuery, DropDownQueryProviderModule> {

	protected AnnotatedDAOWrapper<DropDownQuery, Integer> queryDAO;
	
	protected static AlternativesPopulator<DropDownAlternative> ALTERNATIVES_POPLATOR = new AlternativesPopulator<DropDownAlternative>(DropDownAlternative.class);
	
	public DropDownQueryCRUD(AnnotatedDAOWrapper<DropDownQuery, Integer> queryDAO, BeanRequestPopulator<DropDownQuery> populator, String typeElementName, String typeLogName, String listMethodAlias, DropDownQueryProviderModule callback) {
		
		super(DropDownQuery.class, queryDAO, populator, typeElementName, typeLogName, listMethodAlias, callback);
		
		this.queryDAO = queryDAO;
	}

	@Override
	protected DropDownQuery populateFromUpdateRequest(DropDownQuery bean, HttpServletRequest req, User user, URIParser uriParser) throws ValidationException, Exception {
		
		DropDownQuery query = super.populateFromUpdateRequest(bean, req, user, uriParser);

		List<ValidationError> validationErrors = new ArrayList<ValidationError>();
		
		this.populateQueryDescriptor((QueryDescriptor) query.getQueryDescriptor(), req, validationErrors);
		
		if(req.getParameter("useFreeTextAlternative") != null && StringUtils.isEmpty(bean.getFreeTextAlternative())) {
			validationErrors.add(new ValidationError("freeTextAlternative", ValidationErrorType.RequiredField));
		}
		
		List<DropDownAlternative> alternatives = ALTERNATIVES_POPLATOR.populate(bean.getAlternatives(), req, validationErrors);
		
		if(CollectionUtils.isEmpty(alternatives) || alternatives.size() + (bean.getFreeTextAlternative() != null ? 1 : 0) < 1) {
			validationErrors.add(new ValidationError("TooFewAlternatives"));
		}
		
		if(!validationErrors.isEmpty()) {
			throw new ValidationException(validationErrors);
		}
		
		query.setAlternatives(alternatives);
		
		return query;
		
	}
	
	@Override
	protected List<Field> getBeanRelations() {
		
		return Arrays.asList(DropDownQuery.ALTERNATIVES_RELATION);
	}
	
}
