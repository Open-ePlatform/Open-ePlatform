package com.nordicpeak.flowengine.queries.textfieldquery.api;

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
import com.nordicpeak.flowengine.queries.textfieldquery.TextFieldQuery;

public class TextFieldQueryEndpointCRUD extends ModularCRUD<TextFieldQueryEndpoint, Integer, User, TextFieldQueryEndpointAdminModule> {
	
	public TextFieldQueryEndpointCRUD(CRUDDAO<TextFieldQueryEndpoint, Integer> crudDAO, TextFieldQueryEndpointAdminModule callback) {

		super(IntegerBeanIDParser.getInstance(), crudDAO, new AnnotatedRequestPopulator<TextFieldQueryEndpoint>(TextFieldQueryEndpoint.class), "TextFieldQueryEndpoint", "endpoint", "/", callback);
	
		setRequirePostForDelete(true);
	}
	
	@Override
	protected void checkDeleteAccess(TextFieldQueryEndpoint bean, User user, HttpServletRequest req, URIParser uriParser) throws AccessDeniedException, URINotFoundException, SQLException {

		super.checkDeleteAccess(bean, user, req, uriParser);
		
		if (!CollectionUtils.isEmpty(bean.getQueries())) {
			throw new AccessDeniedException("User " + user + " not allowed to delete endpoint " + bean + " whilst having queries");
		}
	}
	
	@Override
	protected void appendBean(TextFieldQueryEndpoint bean, Element targetElement, Document doc, User user) {
		
		targetElement.appendChild(bean.toXMLFull(doc));
	}

	@Override
	protected void appendShowFormData(TextFieldQueryEndpoint bean, Document doc, Element showTypeElement, User user, HttpServletRequest req, HttpServletResponse res, URIParser uriParser) throws SQLException, IOException, Exception {

		List<TextFieldQuery> queries = callback.getQueries(bean);
		
		if (!CollectionUtils.isEmpty(queries)) {
			
			Element queriesElement = XMLUtils.appendNewElement(doc, showTypeElement, "Queries");
			
			Map<Integer, Element> flowElements = new HashMap<Integer, Element>();
			Map<Integer, Element> stepElements = new HashMap<Integer, Element>();
			
			for (TextFieldQuery query : queries) {
				
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
	protected void appendUpdateFormData(TextFieldQueryEndpoint bean, Document doc, Element updateTypeElement, User user, HttpServletRequest req, URIParser uriParser) throws Exception {
		
		appendFormData(bean, doc, updateTypeElement);
	}
	
	private void appendFormData(TextFieldQueryEndpoint bean, Document doc, Element typeElement) throws Exception {
		
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
	protected TextFieldQueryEndpoint populateFromUpdateRequest(TextFieldQueryEndpoint bean, HttpServletRequest req, User user, URIParser uriParser) throws Exception {

		populateTags(req, bean);

		return super.populateFromUpdateRequest(bean, req, user, uriParser);
	}

	@Override
	protected ForegroundModuleResponse beanAdded(TextFieldQueryEndpoint bean, HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Exception {

		populateTags(req, bean);
		
		return super.beanAdded(bean, req, res, user, uriParser);
	}

	private void populateTags(HttpServletRequest req, TextFieldQueryEndpoint source) throws SQLException {

		List<Integer> tagIDs = NumberUtils.toInt(req.getParameterValues("tags"));

		callback.updateTags(tagIDs, callback.getClass(), source);
	}

	@Override
	protected void validateAddPopulation(TextFieldQueryEndpoint bean, HttpServletRequest req, User user, URIParser uriParser) throws ValidationException, SQLException, Exception {

		checkEncoding(bean);
	}

	@Override
	protected void validateUpdatePopulation(TextFieldQueryEndpoint bean, HttpServletRequest req, User user, URIParser uriParser) throws ValidationException, SQLException, Exception {

		checkEncoding(bean);
	}

	private void checkEncoding(TextFieldQueryEndpoint bean) throws ValidationException {

		if (!callback.getAllowedEncodings().contains(bean.getEncoding())) {
			throw new ValidationException(new ValidationError("encoding", ValidationErrorType.InvalidFormat));
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