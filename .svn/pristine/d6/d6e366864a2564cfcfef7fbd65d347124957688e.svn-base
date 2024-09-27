package com.nordicpeak.flowengine.queries.textareaquery;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.standardutils.dao.AnnotatedDAOWrapper;
import se.unlogic.standardutils.validation.ValidationError;
import se.unlogic.standardutils.validation.ValidationException;
import se.unlogic.standardutils.xml.XMLUtils;
import se.unlogic.webutils.http.BeanRequestPopulator;
import se.unlogic.webutils.http.URIParser;

import com.nordicpeak.flowengine.beans.QueryDescriptor;
import com.nordicpeak.flowengine.queries.basequery.BaseQueryCRUD;

public class TextAreaQueryCRUD extends BaseQueryCRUD<TextAreaQuery, TextAreaQueryProviderModule> {

	protected AnnotatedDAOWrapper<TextAreaQuery, Integer> queryDAO;
	
	public TextAreaQueryCRUD(AnnotatedDAOWrapper<TextAreaQuery, Integer> queryDAO, BeanRequestPopulator<TextAreaQuery> populator, String typeElementName, String typeLogName, String listMethodAlias, TextAreaQueryProviderModule callback) {
		
		super(TextAreaQuery.class, queryDAO, populator, typeElementName, typeLogName, listMethodAlias, callback);
		
		this.queryDAO = queryDAO;
	}

	@Override
	protected TextAreaQuery populateFromUpdateRequest(TextAreaQuery bean, HttpServletRequest req, User user, URIParser uriParser) throws ValidationException, Exception {
		
		TextAreaQuery query = super.populateFromUpdateRequest(bean, req, user, uriParser);
		
		List<ValidationError> validationErrors = new ArrayList<ValidationError>();

		this.populateQueryDescriptor((QueryDescriptor) query.getQueryDescriptor(), req, validationErrors);
		
		if(query.getMaxLength() != null) {
			
			if(query.getMaxLength() > 65535) {
				validationErrors.add(new ValidationError("MaxLengthToBig"));
			}
		}
		
		if(query.getRows() != null) {
			
			if(query.getRows() > 255) {
				validationErrors.add(new ValidationError("RowCountTooHigh"));
			}
		}
		
		if(!validationErrors.isEmpty()) {
			throw new ValidationException(validationErrors);
		}

		return query;
	}
	
	@Override
	protected void appendAddFormData(Document doc, Element addTypeElement, User user, HttpServletRequest req, URIParser uriParser) throws Exception {

		appendFormatValidators(doc, addTypeElement);
	}

	@Override
	protected void appendUpdateFormData(TextAreaQuery bean, Document doc, Element updateTypeElement, User user, HttpServletRequest req, URIParser uriParser) throws Exception {

		appendFormatValidators(doc, updateTypeElement);
	}
	
	private void appendFormatValidators(Document doc, Element element) {

		XMLUtils.append(doc, element, callback.getFormatValidators());
	}

}
