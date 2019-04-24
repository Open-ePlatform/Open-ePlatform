package com.nordicpeak.flowengine.queries.childquery;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.standardutils.dao.AnnotatedDAOWrapper;
import se.unlogic.standardutils.populators.IntegerPopulator;
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
import com.nordicpeak.flowengine.queries.childquery.filterapi.ChildQueryFilterEndpoint;

public class ChildQueryCRUD extends BaseQueryCRUD<ChildQuery, ChildQueryProviderModule> {

	protected AnnotatedDAOWrapper<ChildQuery, Integer> queryDAO;

	public ChildQueryCRUD(AnnotatedDAOWrapper<ChildQuery, Integer> queryDAO, BeanRequestPopulator<ChildQuery> populator, String typeElementName, String typeLogName, String listMethodAlias, ChildQueryProviderModule callback) {

		super(ChildQuery.class, queryDAO, populator, typeElementName, typeLogName, listMethodAlias, callback);

		this.queryDAO = queryDAO;
	}

	@Override
	protected void appendUpdateFormData(ChildQuery bean, Document doc, Element updateTypeElement, User user, HttpServletRequest req, URIParser uriParser) throws Exception {

		super.appendUpdateFormData(bean, doc, updateTypeElement, user, req, uriParser);

		if (callback.getFilterAPIModule() != null) {
		
			XMLUtils.append(doc, updateTypeElement, "FilterApiEndpoints", callback.getFilterAPIModule().getEndpoints());
		}
	}

	@Override
	protected ChildQuery populateFromUpdateRequest(ChildQuery bean, HttpServletRequest req, User user, URIParser uriParser) throws ValidationException, Exception {

		ChildQuery query = super.populateFromUpdateRequest(bean, req, user, uriParser);

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

		Integer endpointID = null;

		if (callback.getFilterAPIModule() != null) {

			endpointID = ValidationUtils.validateParameter("filterEndpointID", req, false, IntegerPopulator.getPopulator(), validationErrors);
		}

		if (endpointID != null) {

			ChildQueryFilterEndpoint endpoint = callback.getFilterAPIModule().getEndpoint(endpointID);

			if (endpoint == null) {

				validationErrors.add(new ValidationError("filterEndpointID", ValidationErrorType.InvalidFormat));

			} else {

				query.setFilterEndpoint(endpoint);

				if (endpoint.getFields() != null) {

					List<SelectedChildAttribute> selectedChildAttributes = new ArrayList<SelectedChildAttribute>(endpoint.getFields().size());

					int sortIndex = 0;
					
					for (String fieldName : endpoint.getFields()) {

						ChildAttributeDisplayMode displayMode = ValidationUtils.validateParameter("attribute-" + endpoint.getEndpointID() + "-" + fieldName, req, true, SelectedChildAttribute.DISPLAY_MODE_POPULATOR, validationErrors);

						if (displayMode != null) {
							selectedChildAttributes.add(new SelectedChildAttribute(fieldName, displayMode, sortIndex++));
						}
					}

					query.setSelectedChildAttributes(selectedChildAttributes);
				}
			}

		} else {

			query.setFilterEndpoint(null);
			query.setSelectedChildAttributes(null);
		}

		if (!validationErrors.isEmpty()) {
			throw new ValidationException(validationErrors);
		}

		return query;
	}

}
