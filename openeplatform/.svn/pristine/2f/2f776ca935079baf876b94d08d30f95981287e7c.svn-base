package com.nordicpeak.flowengine.queries.childquery.filterapi;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.hierarchy.core.exceptions.AccessDeniedException;
import se.unlogic.hierarchy.core.exceptions.URINotFoundException;
import se.unlogic.hierarchy.core.interfaces.ForegroundModuleResponse;
import se.unlogic.hierarchy.core.utils.crud.IntegerBeanIDParser;
import se.unlogic.hierarchy.core.utils.crud.ModularCRUD;
import se.unlogic.standardutils.collections.CollectionUtils;
import se.unlogic.standardutils.dao.CRUDDAO;
import se.unlogic.standardutils.numbers.NumberUtils;
import se.unlogic.standardutils.validation.ValidationError;
import se.unlogic.standardutils.validation.ValidationErrorType;
import se.unlogic.standardutils.validation.ValidationException;
import se.unlogic.standardutils.xml.XMLUtils;
import se.unlogic.webutils.http.URIParser;
import se.unlogic.webutils.populators.annotated.AnnotatedRequestPopulator;

import com.nordicpeak.flowengine.interfaces.APISourceAccountDescriptor;
import com.nordicpeak.flowengine.interfaces.ImmutableFlow;
import com.nordicpeak.flowengine.interfaces.ImmutableStep;
import com.nordicpeak.flowengine.queries.childquery.beans.ChildQuery;
import com.nordicpeak.flowengine.queries.childquery.interfaces.ChildQueryFilterEndpoint;

public class ChildQueryFilterEndpointCRUD extends ModularCRUD<ChildQuerySimpleFilterEndpoint, Integer, User, ChildQueryFilterEndpointAdminModule> {

	public ChildQueryFilterEndpointCRUD(CRUDDAO<ChildQuerySimpleFilterEndpoint, Integer> crudDAO, ChildQueryFilterEndpointAdminModule callback) {

		super(IntegerBeanIDParser.getInstance(), crudDAO, new AnnotatedRequestPopulator<ChildQuerySimpleFilterEndpoint>(ChildQuerySimpleFilterEndpoint.class), "ChildQueryFilterEndpoint", "endpoint", "/", callback);
	
		setRequirePostForDelete(true);
	}

	@Override
	protected void checkDeleteAccess(ChildQuerySimpleFilterEndpoint endpoint, User user, HttpServletRequest req, URIParser uriParser) throws AccessDeniedException, URINotFoundException, SQLException {

		super.checkDeleteAccess(endpoint, user, req, uriParser);

		if (callback.getQueries(endpoint) != null) {
			throw new AccessDeniedException("User " + user + " not allowed to delete endpoint " + endpoint + " whilst having queries");
		}
	}

	@Override
	protected void appendAllBeans(Document doc, Element listTypeElement, User user, HttpServletRequest req, URIParser uriParser, List<ValidationError> validationError) throws SQLException {
		
		List<ChildQuerySimpleFilterEndpoint> endpoints = getAllBeans(user, req, uriParser);
		
		if (endpoints != null) {

			Element listElement = XMLUtils.appendNewElement(doc, listTypeElement, typeElementPluralName);

			for (ChildQuerySimpleFilterEndpoint endpoint : endpoints) {

				Element element = endpoint.toXML(doc);

				try {
					if (callback.isInUse(endpoint)) {
						
						XMLUtils.appendNewElement(doc, element, "InUse");
					}
				} catch (SQLException e) {
					log.error(e);
				}

				listElement.appendChild(element);
			}
		}
	}

	@Override
	protected void appendBean(ChildQuerySimpleFilterEndpoint endpoint, Element targetElement, Document doc, User user) {

		targetElement.appendChild(endpoint.toXMLFull(doc));
	}

	@Override
	protected void appendShowFormData(ChildQuerySimpleFilterEndpoint endpoint, Document doc, Element showTypeElement, User user, HttpServletRequest req, HttpServletResponse res, URIParser uriParser) throws SQLException, IOException, Exception {

		List<ChildQuery> queries = callback.getQueries(endpoint);

		if (!CollectionUtils.isEmpty(queries)) {

			Element queriesElement = XMLUtils.appendNewElement(doc, showTypeElement, "Queries");

			Map<Integer, Element> flowElements = new HashMap<Integer, Element>();
			Map<Integer, Element> stepElements = new HashMap<Integer, Element>();

			for (ChildQuery query : queries) {

				ImmutableFlow flow = query.getQueryDescriptor().getStep().getFlow();
				Element flowElement = flowElements.get(flow.getFlowID());

				if (flowElement == null) {

					flowElement = (Element) queriesElement.appendChild(flow.toXML(doc));
					flowElements.put(flow.getFlowID(), flowElement);
				}

				ImmutableStep step = query.getQueryDescriptor().getStep();
				Element stepElement = stepElements.get(step.getStepID());

				if (stepElement == null) {

					stepElement = (Element) flowElement.appendChild(step.toXML(doc));
					stepElements.put(step.getStepID(), stepElement);
				}

				Element queryElement = XMLUtils.appendNewElement(doc, stepElement, "Query");

				queryElement.appendChild(query.getQueryDescriptor().toXML(doc));
			}
		}
	}

	@Override
	protected void appendAddFormData(Document doc, Element addTypeElement, User user, HttpServletRequest req, URIParser uriParser) throws Exception {

		appendFormData(null, doc, addTypeElement);
	}

	@Override
	protected void appendUpdateFormData(ChildQuerySimpleFilterEndpoint bean, Document doc, Element updateTypeElement, User user, HttpServletRequest req, URIParser uriParser) throws Exception {

		appendFormData(bean, doc, updateTypeElement);
	}
	
	private void appendFormData(ChildQuerySimpleFilterEndpoint bean, Document doc, Element typeElement) throws Exception {

		XMLUtils.append(doc, typeElement, "AllowedEncodings", "Encoding", callback.getAllowedEncodings());
		
		// Check if APISourceHandler is in another section
		if (callback.getAPISourceHandler() != null) {

			XMLUtils.append(doc, typeElement, "SelectedTags", callback.getSelectedTags(bean));
			XMLUtils.appendNewElement(doc, typeElement, "HasTagSupport");

			if (callback.getAPISourceAccountHandler() != null) {
				
				List<? extends APISourceAccountDescriptor> accounts = callback.getAPISourceAccountHandler().getAccountDescriptors();
				XMLUtils.appendNewElement(doc, typeElement, "UseAPISourceAccounts", true);
				XMLUtils.append(doc, typeElement, "APISourceAccountDescriptors", accounts);
			}
		}
	}

	@Override
	protected ChildQuerySimpleFilterEndpoint populateFromUpdateRequest(ChildQuerySimpleFilterEndpoint bean, HttpServletRequest req, User user, URIParser uriParser) throws Exception {

		populateTags(req, bean);

		return super.populateFromUpdateRequest(bean, req, user, uriParser);
	}

	@Override
	protected ForegroundModuleResponse beanAdded(ChildQuerySimpleFilterEndpoint bean, HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Exception {

		populateTags(req, bean);

		return super.beanAdded(bean, req, res, user, uriParser);
	}

	private void populateTags(HttpServletRequest req, ChildQuerySimpleFilterEndpoint source) throws SQLException {

		List<Integer> tagIDs = NumberUtils.toInt(req.getParameterValues("tags"));

		callback.updateTags(tagIDs, callback.getClass(), source);
	}

	@Override
	protected void validateAddPopulation(ChildQuerySimpleFilterEndpoint bean, HttpServletRequest req, User user, URIParser uriParser) throws ValidationException, SQLException, Exception {

		validatePopulation(bean, req, user, uriParser);
	}

	@Override
	protected void validateUpdatePopulation(ChildQuerySimpleFilterEndpoint bean, HttpServletRequest req, User user, URIParser uriParser) throws ValidationException, SQLException, Exception {

		validatePopulation(bean, req, user, uriParser);
	}

	protected void validatePopulation(ChildQuerySimpleFilterEndpoint endpoint, HttpServletRequest req, User user, URIParser uriParser) throws ValidationException, SQLException, Exception {

		List<ValidationError> validationErrors = null;

		if (!callback.getAllowedEncodings().contains(endpoint.getEncoding())) {
			validationErrors = CollectionUtils.addAndInstantiateIfNeeded(validationErrors, new ValidationError("encoding", ValidationErrorType.InvalidFormat));
		}

		ChildQueryFilterEndpoint existingEndpoint = callback.getChildQueryProviderModule().getFilterEndpoint(endpoint.getName());

		if (existingEndpoint != null && existingEndpoint != endpoint) {

			if (!existingEndpoint.getProvider().equals(callback)) {

				validationErrors = CollectionUtils.addAndInstantiateIfNeeded(validationErrors, new EndpointNameAlreadyInUseValidationError(existingEndpoint, existingEndpoint.getProvider()));

			} else {

				ChildQuerySimpleFilterEndpoint simpleEndpoint = (ChildQuerySimpleFilterEndpoint) existingEndpoint;

				if (!simpleEndpoint.getEndpointID().equals(endpoint.getEndpointID())) {

					validationErrors = CollectionUtils.addAndInstantiateIfNeeded(validationErrors, new EndpointNameAlreadyInUseValidationError(existingEndpoint, callback));
				}
			}
		}

		if (validationErrors != null) {
			throw new ValidationException(validationErrors);
		}
	}

	@Override
	public ForegroundModuleResponse list(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser, List<ValidationError> validationErrors) throws Exception {

		if(callback.getAPISourceHandler() != null) {
			
			res.sendRedirect(uriParser.getContextPath() + callback.getAPISourceHandler().getFullAlias());
			return null;
			
		}else {
			
			return super.list(req, res, user, uriParser, validationErrors);
		}
	}
}