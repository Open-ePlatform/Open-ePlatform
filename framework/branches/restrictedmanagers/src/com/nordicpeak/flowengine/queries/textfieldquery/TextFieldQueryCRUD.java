package com.nordicpeak.flowengine.queries.textfieldquery;

import java.io.IOException;
import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import se.unlogic.hierarchy.core.beans.SimpleForegroundModuleResponse;
import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.hierarchy.core.enums.CRUDAction;
import se.unlogic.hierarchy.core.enums.EventTarget;
import se.unlogic.hierarchy.core.events.CRUDEvent;
import se.unlogic.hierarchy.core.exceptions.URINotFoundException;
import se.unlogic.hierarchy.core.interfaces.ForegroundModuleResponse;
import se.unlogic.standardutils.collections.CollectionUtils;
import se.unlogic.standardutils.dao.AnnotatedDAOWrapper;
import se.unlogic.standardutils.numbers.NumberUtils;
import se.unlogic.standardutils.validation.ValidationError;
import se.unlogic.standardutils.validation.ValidationException;
import se.unlogic.standardutils.xml.XMLUtils;
import se.unlogic.webutils.http.BeanRequestPopulator;
import se.unlogic.webutils.http.URIParser;

import com.nordicpeak.flowengine.beans.QueryDescriptor;
import com.nordicpeak.flowengine.queries.basequery.BaseQueryCRUD;

public class TextFieldQueryCRUD extends BaseQueryCRUD<TextFieldQuery, TextFieldQueryProviderModule> {

	protected AnnotatedDAOWrapper<TextFieldQuery, Integer> queryDAO;

	public TextFieldQueryCRUD(AnnotatedDAOWrapper<TextFieldQuery, Integer> queryDAO, BeanRequestPopulator<TextFieldQuery> populator, String typeElementName, String typeLogName, String listMethodAlias, TextFieldQueryProviderModule callback) {

		super(TextFieldQuery.class, queryDAO, populator, typeElementName, typeLogName, listMethodAlias, callback);

		this.queryDAO = queryDAO;
	}

	public ForegroundModuleResponse sort(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Exception {

		TextFieldQuery query = this.getRequestedBean(req, res, user, uriParser, UPDATE);

		if (query != null) {

			this.checkUpdateAccess(query, user, req, uriParser);

			if (req.getMethod().equalsIgnoreCase("POST")) {

				List<TextField> textFields = query.getFields();

				if(!CollectionUtils.isEmpty(textFields)) {

					for(TextField textField : textFields) {

						String sortIndex = req.getParameter("sortorder_" + textField.getTextFieldID());

						if(NumberUtils.isInt(sortIndex)) {

							textField.setSortIndex(NumberUtils.toInt(sortIndex));

						}

					}

					queryDAO.update(query);

					callback.getEventHandler().sendEvent(QueryDescriptor.class, new CRUDEvent<QueryDescriptor>(CRUDAction.UPDATE, (QueryDescriptor) query.getQueryDescriptor()), EventTarget.ALL);

					callback.redirectToQueryConfig(query, req, res);

					return null;

				}

			}

			Document doc = this.callback.createDocument(req, uriParser, user);
			Element updateTypeElement = doc.createElement("SortTextFields");
			doc.getFirstChild().appendChild(updateTypeElement);

			updateTypeElement.appendChild(query.toXML(doc));

			return new SimpleForegroundModuleResponse(doc);

		}

		throw new URINotFoundException(uriParser);
	}

	@Override
	protected TextFieldQuery populateFromUpdateRequest(TextFieldQuery bean, HttpServletRequest req, User user, URIParser uriParser) throws ValidationException, Exception {

		TextFieldQuery query = super.populateFromUpdateRequest(bean, req, user, uriParser);

		List<ValidationError> validationErrors = new ArrayList<ValidationError>();

		this.populateQueryDescriptor((QueryDescriptor) query.getQueryDescriptor(), req, validationErrors);

		if(!validationErrors.isEmpty()) {
			throw new ValidationException(validationErrors);
		}

		return query;
	}

	@Override
	protected void appendShowFormData(TextFieldQuery bean, Document doc, Element showTypeElement, User user, HttpServletRequest req, HttpServletResponse res, URIParser uriParser) throws SQLException, IOException, Exception {

		XMLUtils.appendNewElement(doc, showTypeElement, "showFlowURL", callback.getFlowAdminModule().getFlowQueryRedirectURL(req, bean.getQueryDescriptor().getStep().getFlow().getFlowID()));
		
		XMLUtils.append(doc, showTypeElement, callback.getFormatValidators());
		
		this.appendFieldLayouts(doc, showTypeElement);

	}

	@Override
	protected void appendUpdateFormData(TextFieldQuery bean, Document doc, Element updateTypeElement, User user, HttpServletRequest req, URIParser uriParser) throws Exception {

		this.appendFieldLayouts(doc, updateTypeElement);
		
		updateTypeElement.appendChild(bean.getQueryDescriptor().getStep().getFlow().getFlowType().toXML(doc));
	}

	@Override
	protected ForegroundModuleResponse beanUpdated(TextFieldQuery bean, HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Exception {

		callback.getEventHandler().sendEvent(QueryDescriptor.class, new CRUDEvent<QueryDescriptor>(CRUDAction.UPDATE, (QueryDescriptor) bean.getQueryDescriptor()), EventTarget.ALL);
		
		callback.getFlowAdminModule().addFlowFamilyEvent(callback.getFlowAdminModule().getEventQueryUpdatedMessage() + " \"" + bean.getQueryDescriptor().getName() + "\"", bean.getQueryDescriptor().getStep().getFlow(), user);

		callback.redirectToQueryConfig(bean, req, res);

		return null;

	}

	@Override
	protected List<Field> getBeanRelations() {

		return Arrays.asList(TextFieldQuery.TEXT_FIELDS_RELATION);
	}

	protected void appendFieldLayouts(Document doc, Element element) {

		for(FieldLayout layout : FieldLayout.values()) {

			Element fieldLayoutElement = doc.createElement("FieldLayout");
			element.appendChild(fieldLayoutElement);

			XMLUtils.appendNewElement(doc, fieldLayoutElement, "name", callback.getFieldLayoutName(layout));
			XMLUtils.appendNewElement(doc, fieldLayoutElement, "value", layout);
		}
	}
}
