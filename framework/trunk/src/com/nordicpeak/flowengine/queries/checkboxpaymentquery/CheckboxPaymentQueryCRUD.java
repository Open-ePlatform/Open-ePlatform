package com.nordicpeak.flowengine.queries.checkboxpaymentquery;

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
import com.nordicpeak.flowengine.queries.basequery.BaseQueryCRUD;

public class CheckboxPaymentQueryCRUD extends BaseQueryCRUD<CheckboxPaymentQuery, CheckboxPaymentQueryProviderModule> {

	protected AnnotatedDAOWrapper<CheckboxPaymentQuery, Integer> queryDAO;

	protected static CheckboxPaymentAlternativesPopulator ALTERNATIVES_POPLATOR = new CheckboxPaymentAlternativesPopulator();

	public CheckboxPaymentQueryCRUD(AnnotatedDAOWrapper<CheckboxPaymentQuery, Integer> queryDAO, BeanRequestPopulator<CheckboxPaymentQuery> populator, String typeElementName, String typeLogName, String listMethodAlias, CheckboxPaymentQueryProviderModule callback) {

		super(CheckboxPaymentQuery.class, queryDAO, populator, typeElementName, typeLogName, listMethodAlias, callback);

		this.queryDAO = queryDAO;
	}

	@Override
	protected CheckboxPaymentQuery populateFromUpdateRequest(CheckboxPaymentQuery bean, HttpServletRequest req, User user, URIParser uriParser) throws ValidationException, Exception {

		CheckboxPaymentQuery query = super.populateFromUpdateRequest(bean, req, user, uriParser);

		List<ValidationError> validationErrors = new ArrayList<ValidationError>();

		this.populateQueryDescriptor((QueryDescriptor) query.getQueryDescriptor(), req, validationErrors);

		if(req.getParameter("useFreeTextAlternative") != null && StringUtils.isEmpty(bean.getFreeTextAlternative())) {
			validationErrors.add(new ValidationError("freeTextAlternative", ValidationErrorType.RequiredField));
		}

		List<CheckboxPaymentAlternative> alternatives = ALTERNATIVES_POPLATOR.populate(bean.getAlternatives(), req, validationErrors);

		if(CollectionUtils.isEmpty(alternatives)) {

			validationErrors.add(new ValidationError("TooFewAlternatives1Min"));

		} else {

			Integer minChecked = query.getMinChecked();
			Integer maxChecked = query.getMaxChecked();

			validateMinAndMax(minChecked, maxChecked, alternatives, validationErrors);
			
		}

		if(!validationErrors.isEmpty()) {
			throw new ValidationException(validationErrors);
		}

		query.setAlternatives(alternatives);

		return query;

	}

	public static void validateMinAndMax(Integer minChecked, Integer maxChecked, List<CheckboxPaymentAlternative> alternatives, List<ValidationError> validationErrors) {
		
		if(minChecked != null) {

			if(minChecked > alternatives.size()) {
				validationErrors.add(new ValidationError("MinCheckedToBig"));
			}

			if(maxChecked != null && (minChecked > maxChecked || maxChecked < minChecked)) {
				validationErrors.add(new ValidationError("MinCheckedBiggerThanMaxChecked"));
			}

		}

		if(maxChecked != null) {

			if(maxChecked > alternatives.size()) {
				validationErrors.add(new ValidationError("MaxCheckedToBig"));
			}

		}
		
	}
	
	@Override
	protected List<Field> getBeanRelations() {

		return Arrays.asList(CheckboxPaymentQuery.ALTERNATIVES_RELATION);
	}

}
