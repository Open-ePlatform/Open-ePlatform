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
import se.unlogic.hierarchy.core.utils.crud.IntegerBeanIDParser;
import se.unlogic.hierarchy.core.utils.crud.ModularCRUD;
import se.unlogic.standardutils.collections.CollectionUtils;
import se.unlogic.standardutils.dao.CRUDDAO;
import se.unlogic.standardutils.validation.ValidationError;
import se.unlogic.standardutils.validation.ValidationErrorType;
import se.unlogic.standardutils.validation.ValidationException;
import se.unlogic.standardutils.xml.XMLUtils;
import se.unlogic.webutils.http.URIParser;
import se.unlogic.webutils.populators.annotated.AnnotatedRequestPopulator;

import com.nordicpeak.flowengine.interfaces.ImmutableFlow;
import com.nordicpeak.flowengine.interfaces.ImmutableStep;
import com.nordicpeak.flowengine.queries.childquery.beans.ChildQuery;

public class ChildQueryFilterEndpointCRUD extends ModularCRUD<ChildQueryFilterEndpoint, Integer, User, ChildQueryFilterEndpointAdminModule> {
	
	public ChildQueryFilterEndpointCRUD(CRUDDAO<ChildQueryFilterEndpoint, Integer> crudDAO, ChildQueryFilterEndpointAdminModule callback) {

		super(IntegerBeanIDParser.getInstance(), crudDAO, new AnnotatedRequestPopulator<ChildQueryFilterEndpoint>(ChildQueryFilterEndpoint.class), "ChildQueryFilterEndpoint", "endpoint", "/", callback);
	}
	
	@Override
	protected void checkDeleteAccess(ChildQueryFilterEndpoint bean, User user, HttpServletRequest req, URIParser uriParser) throws AccessDeniedException, URINotFoundException, SQLException {

		super.checkDeleteAccess(bean, user, req, uriParser);
		
		if (!CollectionUtils.isEmpty(bean.getQueries())) {
			throw new AccessDeniedException("User " + user + " not allowed to delete endpoint " + bean + " whilst having queries");
		}
	}

	@Override
	protected void appendShowFormData(ChildQueryFilterEndpoint bean, Document doc, Element showTypeElement, User user, HttpServletRequest req, HttpServletResponse res, URIParser uriParser) throws SQLException, IOException, Exception {

		List<ChildQuery> queries = callback.getQueries(bean);
		
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

		XMLUtils.append(doc, addTypeElement, "AllowedEncodings", "Encoding", callback.getAllowedEncodings());
	}

	@Override
	protected void appendUpdateFormData(ChildQueryFilterEndpoint bean, Document doc, Element updateTypeElement, User user, HttpServletRequest req, URIParser uriParser) throws Exception {

		XMLUtils.append(doc, updateTypeElement, "AllowedEncodings", "Encoding", callback.getAllowedEncodings());
	}

	@Override
	protected void validateAddPopulation(ChildQueryFilterEndpoint bean, HttpServletRequest req, User user, URIParser uriParser) throws ValidationException, SQLException, Exception {

		checkEncoding(bean);
	}

	@Override
	protected void validateUpdatePopulation(ChildQueryFilterEndpoint bean, HttpServletRequest req, User user, URIParser uriParser) throws ValidationException, SQLException, Exception {

		checkEncoding(bean);
	}

	private void checkEncoding(ChildQueryFilterEndpoint bean) throws ValidationException {

		if (!callback.getAllowedEncodings().contains(bean.getEncoding())) {
			throw new ValidationException(new ValidationError("encoding", ValidationErrorType.InvalidFormat));
		}
	}
}