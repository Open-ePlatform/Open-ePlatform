package com.nordicpeak.flowengine.queries.textfieldquery;

import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.nordicpeak.flowengine.beans.QueryDescriptor;

import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.hierarchy.core.enums.CRUDAction;
import se.unlogic.hierarchy.core.enums.EventTarget;
import se.unlogic.hierarchy.core.events.CRUDEvent;
import se.unlogic.hierarchy.core.exceptions.AccessDeniedException;
import se.unlogic.hierarchy.core.exceptions.URINotFoundException;
import se.unlogic.hierarchy.core.interfaces.ForegroundModuleResponse;
import se.unlogic.hierarchy.core.utils.IntegerBasedCRUD;
import se.unlogic.standardutils.dao.AnnotatedDAOWrapper;
import se.unlogic.standardutils.dao.HighLevelQuery;
import se.unlogic.standardutils.dao.querys.ObjectQuery;
import se.unlogic.standardutils.populators.EnumPopulator;
import se.unlogic.standardutils.populators.IntegerPopulator;
import se.unlogic.standardutils.validation.ValidationError;
import se.unlogic.standardutils.validation.ValidationErrorType;
import se.unlogic.standardutils.validation.ValidationException;
import se.unlogic.standardutils.xml.XMLUtils;
import se.unlogic.webutils.http.BeanRequestPopulator;
import se.unlogic.webutils.http.URIParser;

public class TextFieldCRUD extends IntegerBasedCRUD<TextField, TextFieldQueryProviderModule> {

	protected AnnotatedDAOWrapper<TextField, Integer> textFieldDAO;

	protected static EnumPopulator<FieldLayout> LAYOUT_POPULATOR = new EnumPopulator<FieldLayout>(FieldLayout.class);

	public TextFieldCRUD(AnnotatedDAOWrapper<TextField, Integer> textFieldDAO, BeanRequestPopulator<TextField> populator, String typeElementName, String typeLogName, String listMethodAlias, TextFieldQueryProviderModule callback) {

		super(textFieldDAO, populator, typeElementName, typeLogName, listMethodAlias, callback);

		this.textFieldDAO = textFieldDAO;

		setRequirePostForDelete(true);
	}

	@Override
	protected ForegroundModuleResponse beanAdded(TextField bean, HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Exception {

		callback.getEventHandler().sendEvent(QueryDescriptor.class, new CRUDEvent<QueryDescriptor>(CRUDAction.UPDATE, (QueryDescriptor) bean.getQuery().getQueryDescriptor()), EventTarget.ALL);

		callback.getFlowAdminModule().addFlowFamilyEvent(callback.getFlowAdminModule().getEventQueryUpdatedMessage() + " \"" + bean.getQuery().getQueryDescriptor().getName() + "\", " + callback.getEventFieldAdded() + " \"" + bean.getLabel() + "\"", bean.getQuery().getQueryDescriptor().getStep().getFlow(), user);

		callback.redirectToQueryConfig(bean.getQuery(), req, res);

		return null;
	}

	@Override
	protected ForegroundModuleResponse beanUpdated(TextField bean, HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Exception {

		callback.getEventHandler().sendEvent(QueryDescriptor.class, new CRUDEvent<QueryDescriptor>(CRUDAction.UPDATE, (QueryDescriptor) bean.getQuery().getQueryDescriptor()), EventTarget.ALL);

		callback.getFlowAdminModule().addFlowFamilyEvent(callback.getFlowAdminModule().getEventQueryUpdatedMessage() + " \"" + bean.getQuery().getQueryDescriptor().getName() + "\", " + callback.getEventFieldUpdated() + " \"" + bean.getLabel() + "\"", bean.getQuery().getQueryDescriptor().getStep().getFlow(), user);

		callback.redirectToQueryConfig(bean.getQuery(), req, res);

		return null;
	}

	@Override
	protected ForegroundModuleResponse beanDeleted(TextField bean, HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Exception {

		callback.getEventHandler().sendEvent(QueryDescriptor.class, new CRUDEvent<QueryDescriptor>(CRUDAction.UPDATE, (QueryDescriptor) bean.getQuery().getQueryDescriptor()), EventTarget.ALL);

		callback.getFlowAdminModule().addFlowFamilyEvent(callback.getFlowAdminModule().getEventQueryUpdatedMessage() + " \"" + bean.getQuery().getQueryDescriptor().getName() + "\", " + callback.getEventFieldRemoved() + " \"" + bean.getLabel() + "\"", bean.getQuery().getQueryDescriptor().getStep().getFlow(), user);

		callback.redirectToQueryConfig(bean.getQuery(), req, res);

		return null;
	}

	@Override
	public TextField getBean(Integer beanID) throws SQLException, AccessDeniedException {

		HighLevelQuery<TextField> query = new HighLevelQuery<TextField>(TextField.TEXT_FIELD_QUERY_RELATION);

		query.addParameter(textFieldDAO.getParameterFactory().getParameter(beanID));

		TextField textField = textFieldDAO.getAnnotatedDAO().get(query);

		if (textField != null && textField.getQuery() != null) {

			textField.getQuery().init(callback.getFlowAdminModule().getQueryDescriptor(textField.getQuery().getQueryID()), null);

		}

		return textField;

	}

	@Override
	protected void appendAddFormData(Document doc, Element addTypeElement, User user, HttpServletRequest req, URIParser uriParser) throws Exception {

		this.appendFormatValidators(doc, addTypeElement);

	}

	@Override
	protected void appendUpdateFormData(TextField bean, Document doc, Element updateTypeElement, User user, HttpServletRequest req, URIParser uriParser) throws Exception {

		this.appendFormatValidators(doc, updateTypeElement);

	}

	protected void appendFormatValidators(Document doc, Element element) {

		XMLUtils.append(doc, element, callback.getFormatValidators());
	}

	@Override
	protected TextField populateFromAddRequest(HttpServletRequest req, User user, URIParser uriParser) throws ValidationException, Exception {

		TextField textField = super.populateFromAddRequest(req, user, uriParser);

		textField.setQuery((TextFieldQuery) req.getAttribute("TextFieldQuery"));

		textField.setSortIndex(this.getCurrentMaxSortIndex(textField.getQuery()) + 1);

		return textField;
	}

	@Override
	protected void checkUpdateAccess(TextField bean, User user, HttpServletRequest req, URIParser uriParser) throws AccessDeniedException, URINotFoundException, SQLException {

		callback.checkUpdateQueryAccess(user, bean.getQuery());

	}

	@Override
	protected void checkDeleteAccess(TextField bean, User user, HttpServletRequest req, URIParser uriParser) throws AccessDeniedException, URINotFoundException, SQLException {

		callback.checkUpdateQueryAccess(user, bean.getQuery());

	}

	private int getCurrentMaxSortIndex(TextFieldQuery textFieldQuery) throws SQLException {

		ObjectQuery<Integer> query = new ObjectQuery<Integer>(textFieldDAO.getAnnotatedDAO().getDataSource(), "SELECT MAX(sortIndex) FROM " + textFieldDAO.getAnnotatedDAO().getTableName() + " WHERE queryID = ?", IntegerPopulator.getPopulator());

		query.setInt(1, textFieldQuery.getQueryID());

		Integer sortIndex = query.executeQuery();

		if (sortIndex == null) {

			return 0;
		}

		return sortIndex;
	}

	@Override
	protected void validateAddPopulation(TextField bean, HttpServletRequest req, User user, URIParser uriParser) throws ValidationException, SQLException, Exception {

		validatePopulation(bean);
	}

	@Override
	protected void validateUpdatePopulation(TextField bean, HttpServletRequest req, User user, URIParser uriParser) throws ValidationException, SQLException, Exception {

		validatePopulation(bean);
	}

	protected void validatePopulation(TextField bean) throws ValidationException {

		if (bean.getMinContentLength() != null && bean.getMinContentLength() > 255) {

			throw new ValidationException(new ValidationError("minContentLength", ValidationErrorType.TooLong));
		}

		if (bean.getMaxContentLength() != null && bean.getMaxContentLength() > 255) {

			throw new ValidationException(new ValidationError("maxContentLength", ValidationErrorType.TooLong));
		}

		if (!bean.isDisabled()) {

			bean.setContainsPrice(false);

		} else {

			bean.setMaskFieldContent(false);
		}
	}
}
