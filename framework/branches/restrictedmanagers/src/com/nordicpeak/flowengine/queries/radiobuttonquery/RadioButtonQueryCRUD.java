package com.nordicpeak.flowengine.queries.radiobuttonquery;

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

public class RadioButtonQueryCRUD extends BaseQueryCRUD<RadioButtonQuery, RadioButtonQueryProviderModule> {

	protected AnnotatedDAOWrapper<RadioButtonQuery, Integer> queryDAO;
	
	protected static AlternativesPopulator<RadioButtonAlternative> ALTERNATIVES_POPLATOR = new AlternativesPopulator<RadioButtonAlternative>(RadioButtonAlternative.class);
	
	public RadioButtonQueryCRUD(AnnotatedDAOWrapper<RadioButtonQuery, Integer> queryDAO, BeanRequestPopulator<RadioButtonQuery> populator, String typeElementName, String typeLogName, String listMethodAlias, RadioButtonQueryProviderModule callback) {
		
		super(RadioButtonQuery.class, queryDAO, populator, typeElementName, typeLogName, listMethodAlias, callback);
		
		this.queryDAO = queryDAO;
	}

	@Override
	protected RadioButtonQuery populateFromUpdateRequest(RadioButtonQuery bean, HttpServletRequest req, User user, URIParser uriParser) throws ValidationException, Exception {
		
		RadioButtonQuery query = super.populateFromUpdateRequest(bean, req, user, uriParser);

		List<ValidationError> validationErrors = new ArrayList<ValidationError>();
		
		this.populateQueryDescriptor((QueryDescriptor) query.getQueryDescriptor(), req, validationErrors);
		
		if(req.getParameter("useFreeTextAlternative") != null && StringUtils.isEmpty(bean.getFreeTextAlternative())) {
			validationErrors.add(new ValidationError("freeTextAlternative", ValidationErrorType.RequiredField));
		}
		
		List<RadioButtonAlternative> alternatives = ALTERNATIVES_POPLATOR.populate(bean.getAlternatives(), req, 1024, validationErrors);
		
		if(CollectionUtils.isEmpty(alternatives) || alternatives.size() + (bean.getFreeTextAlternative() != null ? 1 : 0) < 2) {
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
		
		return Arrays.asList(RadioButtonQuery.ALTERNATIVES_RELATION);
	}
	
}
