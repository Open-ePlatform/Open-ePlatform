package com.nordicpeak.flowengine.queries.childquery;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.standardutils.dao.AnnotatedDAOWrapper;
import se.unlogic.standardutils.populators.StringPopulator;
import se.unlogic.standardutils.templates.TemplateUtils;
import se.unlogic.standardutils.validation.ValidationError;
import se.unlogic.standardutils.validation.ValidationErrorType;
import se.unlogic.standardutils.validation.ValidationException;
import se.unlogic.standardutils.xml.XMLUtils;
import se.unlogic.webutils.http.BeanRequestPopulator;
import se.unlogic.webutils.http.URIParser;
import se.unlogic.webutils.validation.ValidationUtils;

import com.nordicpeak.flowengine.beans.QueryDescriptor;
import com.nordicpeak.flowengine.queries.basequery.BaseQueryCRUD;
import com.nordicpeak.flowengine.queries.childquery.beans.ChildQuery;
import com.nordicpeak.flowengine.queries.childquery.beans.SelectedChildAttribute;
import com.nordicpeak.flowengine.queries.childquery.enums.ChildAttributeDisplayMode;
import com.nordicpeak.flowengine.queries.childquery.interfaces.ChildQueryFilterEndpoint;

public class ChildQueryCRUD extends BaseQueryCRUD<ChildQuery, ChildQueryProviderModule> {

	protected AnnotatedDAOWrapper<ChildQuery, Integer> queryDAO;

	public ChildQueryCRUD(AnnotatedDAOWrapper<ChildQuery, Integer> queryDAO, BeanRequestPopulator<ChildQuery> populator, String typeElementName, String typeLogName, String listMethodAlias, ChildQueryProviderModule callback) {

		super(ChildQuery.class, queryDAO, populator, typeElementName, typeLogName, listMethodAlias, callback);

		this.queryDAO = queryDAO;
	}
	
	@Override
	protected void appendBean(ChildQuery childQuery, Element targetElement, Document doc, User user) {
		
		TemplateUtils.setTemplatedFields(childQuery, callback);
		
		super.appendBean(childQuery, targetElement, doc, user);
	}
	
	@Override
	protected void appendUpdateFormData(ChildQuery bean, Document doc, Element updateTypeElement, User user, HttpServletRequest req, URIParser uriParser) throws Exception {

		super.appendUpdateFormData(bean, doc, updateTypeElement, user, req, uriParser);

		XMLUtils.append(doc, updateTypeElement, "FilterApiEndpoints", callback.getFilterEndpoints());
	}

	@Override
	protected ChildQuery populateFromUpdateRequest(ChildQuery oldChildQuery, HttpServletRequest req, User user, URIParser uriParser) throws ValidationException, Exception {

		ChildQuery query = super.populateFromUpdateRequest(oldChildQuery, req, user, uriParser);
		
		TemplateUtils.clearUnchangedTemplatedFields(query, callback);

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

		if (query.getMinAge() != null && query.getMaxAge() != null && query.getMinAge() > query.getMaxAge()) {

			validationErrors.add(new ValidationError("MinAgeLargerThanMaxAge"));
		}

		String endpointName = ValidationUtils.validateParameter("filterEndpoint", req, false, StringPopulator.getPopulator(), validationErrors);

		if (endpointName != null) {

			ChildQueryFilterEndpoint filterEndpoint = callback.getFilterEndpoint(endpointName);

			if (filterEndpoint == null) {

				validationErrors.add(new ValidationError("filterEndpoint", ValidationErrorType.InvalidFormat));

			} else {

				query.setFilterEndpointName(filterEndpoint.getName());

				if (filterEndpoint.getFields() != null) {

					List<SelectedChildAttribute> selectedChildAttributes = new ArrayList<SelectedChildAttribute>(filterEndpoint.getFields().size());

					int sortIndex = 0;
					
					for (String fieldName : filterEndpoint.getFields()) {

						ChildAttributeDisplayMode displayMode = ValidationUtils.validateParameter("attribute-" + filterEndpoint.getName() + "-" + fieldName, req, true, SelectedChildAttribute.DISPLAY_MODE_POPULATOR, validationErrors);

						if (displayMode != null) {
							selectedChildAttributes.add(new SelectedChildAttribute(fieldName, displayMode, sortIndex++));
						}
					}

					query.setSelectedChildAttributes(selectedChildAttributes);
				}
			}

		} else {

			query.setFilterEndpointName(null);
			query.setSelectedChildAttributes(null);
		}

		if (!validationErrors.isEmpty()) {
			throw new ValidationException(validationErrors);
		}

		return query;
	}

}
