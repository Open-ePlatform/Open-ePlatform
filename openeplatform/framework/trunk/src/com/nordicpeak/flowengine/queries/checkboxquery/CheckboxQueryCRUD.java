package com.nordicpeak.flowengine.queries.checkboxquery;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.standardutils.collections.CollectionUtils;
import se.unlogic.standardutils.dao.AnnotatedDAOWrapper;
import se.unlogic.standardutils.string.StringUtils;
import se.unlogic.standardutils.validation.ValidationError;
import se.unlogic.standardutils.validation.ValidationErrorType;
import se.unlogic.standardutils.validation.ValidationException;
import se.unlogic.standardutils.xml.XMLUtils;
import se.unlogic.webutils.http.BeanRequestPopulator;
import se.unlogic.webutils.http.URIParser;

import com.nordicpeak.flowengine.beans.QueryDescriptor;
import com.nordicpeak.flowengine.populators.AlternativesPopulator;
import com.nordicpeak.flowengine.queries.basequery.BaseQueryCRUD;

public class CheckboxQueryCRUD extends BaseQueryCRUD<CheckboxQuery, CheckboxQueryProviderModule> {

	protected AnnotatedDAOWrapper<CheckboxQuery, Integer> queryDAO;

	protected static AlternativesPopulator<CheckboxAlternative> ALTERNATIVES_POPLATOR = new AlternativesPopulator<CheckboxAlternative>(CheckboxAlternative.class);

	public CheckboxQueryCRUD(AnnotatedDAOWrapper<CheckboxQuery, Integer> queryDAO, BeanRequestPopulator<CheckboxQuery> populator, String typeElementName, String typeLogName, String listMethodAlias, CheckboxQueryProviderModule callback) {

		super(CheckboxQuery.class, queryDAO, populator, typeElementName, typeLogName, listMethodAlias, callback);

		this.queryDAO = queryDAO;
	}

	@Override
	protected CheckboxQuery populateFromUpdateRequest(CheckboxQuery bean, HttpServletRequest req, User user, URIParser uriParser) throws ValidationException, Exception {

		CheckboxQuery query = super.populateFromUpdateRequest(bean, req, user, uriParser);

		List<ValidationError> validationErrors = new ArrayList<ValidationError>();

		this.populateQueryDescriptor((QueryDescriptor) query.getQueryDescriptor(), req, validationErrors);

		if (req.getParameter("useFreeTextAlternative") != null && StringUtils.isEmpty(bean.getFreeTextAlternative())) {
			validationErrors.add(new ValidationError("freeTextAlternative", ValidationErrorType.RequiredField));
		}

		List<CheckboxAlternative> alternatives = ALTERNATIVES_POPLATOR.populate(bean.getAlternatives(), req, 1024, 255, validationErrors);

		if (CollectionUtils.isEmpty(alternatives)) {

			validationErrors.add(new ValidationError("TooFewAlternatives1Min"));

		} else {

			Integer minChecked = query.getMinChecked();
			Integer maxChecked = query.getMaxChecked();
			boolean freeTextAlternative = query.getFreeTextAlternative() != null;

			validateMinAndMax(minChecked, maxChecked, alternatives, validationErrors, freeTextAlternative);

		}

		if (!validationErrors.isEmpty()) {
			throw new ValidationException(validationErrors);
		}
		
		if (bean.getFreeTextAlternative() == null) {
			bean.setFormatValidator(null);
			bean.setInvalidFormatMessage(null);
		}

		query.setAlternatives(alternatives);

		return query;

	}

	@Override
	protected void appendAddFormData(Document doc, Element addTypeElement, User user, HttpServletRequest req, URIParser uriParser) throws Exception {

		appendFormatValidators(doc, addTypeElement);
	}

	@Override
	protected void appendUpdateFormData(CheckboxQuery bean, Document doc, Element updateTypeElement, User user, HttpServletRequest req, URIParser uriParser) throws Exception {

		appendFormatValidators(doc, updateTypeElement);
	}

	private void appendFormatValidators(Document doc, Element element) {

		XMLUtils.append(doc, element, callback.getFormatValidators());
	}

	public static void validateMinAndMax(Integer minChecked, Integer maxChecked, List<CheckboxAlternative> alternatives, List<ValidationError> validationErrors, boolean freeTextAlternative) {

		int alternativeCount = alternatives.size();

		if (freeTextAlternative) {

			alternativeCount++;
		}
		
		if (minChecked != null) {

			if (minChecked > alternativeCount) {
				validationErrors.add(new ValidationError("MinCheckedToBig"));
			}

			if (maxChecked != null && (minChecked > maxChecked || maxChecked < minChecked)) {
				validationErrors.add(new ValidationError("MinCheckedBiggerThanMaxChecked"));
			}

		}

		if (maxChecked != null) {

			if (maxChecked > alternativeCount) {
				validationErrors.add(new ValidationError("MaxCheckedToBig"));
			}

		}

	}

	@Override
	protected List<Field> getBeanRelations() {

		return Arrays.asList(CheckboxQuery.ALTERNATIVES_RELATION);
	}

}
