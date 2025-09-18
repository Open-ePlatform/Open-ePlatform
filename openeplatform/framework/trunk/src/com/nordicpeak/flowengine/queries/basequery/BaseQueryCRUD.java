package com.nordicpeak.flowengine.queries.basequery;

import java.lang.reflect.Field;
import java.sql.SQLException;
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
import se.unlogic.hierarchy.core.exceptions.AccessDeniedException;
import se.unlogic.hierarchy.core.exceptions.URINotFoundException;
import se.unlogic.hierarchy.core.interfaces.ForegroundModuleResponse;
import se.unlogic.hierarchy.core.utils.AdvancedIntegerBasedCRUD;
import se.unlogic.hierarchy.core.utils.FCKUtils;
import se.unlogic.standardutils.dao.AnnotatedDAOWrapper;
import se.unlogic.standardutils.dao.HighLevelQuery;
import se.unlogic.standardutils.dao.TransactionHandler;
import se.unlogic.standardutils.validation.ValidationError;
import se.unlogic.standardutils.validation.ValidationException;
import se.unlogic.standardutils.xml.XMLUtils;
import se.unlogic.webutils.http.BeanRequestPopulator;
import se.unlogic.webutils.http.URIParser;
import se.unlogic.webutils.populators.annotated.AnnotatedRequestPopulator;
import se.unlogic.webutils.url.URLRewriter;

import com.nordicpeak.flowengine.beans.Flow;
import com.nordicpeak.flowengine.beans.QueryDescriptor;

public class BaseQueryCRUD<BeanType extends BaseQuery, CallbackType extends BaseQueryCRUDCallback> extends AdvancedIntegerBasedCRUD<BeanType, CallbackType> {

	protected AnnotatedDAOWrapper<BeanType, Integer> queryDAO;

	protected static AnnotatedRequestPopulator<QueryDescriptor> QUERY_DESCRIPTOR_POPULATOR = new AnnotatedRequestPopulator<QueryDescriptor>(QueryDescriptor.class);

	public BaseQueryCRUD(Class<BeanType> beanClass, AnnotatedDAOWrapper<BeanType, Integer> queryDAO, BeanRequestPopulator<BeanType> populator, String typeElementName, String typeLogName, String listMethodAlias, CallbackType callback) {

		super(beanClass, queryDAO, populator, typeElementName, typeLogName, listMethodAlias, callback);

		this.queryDAO = queryDAO;
	}

	@Override
	protected void updateBean(BeanType bean, HttpServletRequest req, User user, URIParser uriParser) throws Exception {

		if (hasFCKContentAnnotations) {

			FCKUtils.removeAbsoluteFileUrls(bean, callback.getAbsoluteFileURL(uriParser, bean));
		}

		if(hasURLRewriteAnnotations){

			URLRewriter.removeAbsoluteLinkUrls(bean, req);
		}

		callback.getFlowAdminModule().filterHTML(bean);
		
		TransactionHandler transactionHandler = null;

		try {

			transactionHandler = queryDAO.getAnnotatedDAO().createTransaction();

			queryDAO.update(bean, transactionHandler);

			callback.getFlowAdminModule().getDAOFactory().getQueryDescriptorDAO().update((QueryDescriptor) bean.getQueryDescriptor(), transactionHandler, null);

			transactionHandler.commit();

		} finally{

			TransactionHandler.autoClose(transactionHandler);
		}

	}

	@Override
	protected ForegroundModuleResponse beanUpdated(BeanType bean, HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Exception {

		callback.getEventHandler().sendEvent(QueryDescriptor.class, new CRUDEvent<QueryDescriptor>(CRUDAction.UPDATE, (QueryDescriptor) bean.getQueryDescriptor()), EventTarget.ALL);
		
		callback.getFlowAdminModule().addFlowFamilyEvent(callback.getFlowAdminModule().getEventQueryUpdatedMessage() + " \"" + bean.getQueryDescriptor().getName() + "\"", bean.getQueryDescriptor().getStep().getFlow(), user);

		res.sendRedirect(callback.getFlowAdminModule().getFlowQueryRedirectURL(req, bean.getQueryDescriptor().getStep().getFlow().getFlowID()));

		return null;
	}

	@Override
	public ForegroundModuleResponse list(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser, List<ValidationError> validationErrors) throws Exception {

		if(validationErrors != null) {

			Document doc = callback.createDocument(req, uriParser, user);

			XMLUtils.append(doc, doc.getDocumentElement(), validationErrors);

			return new SimpleForegroundModuleResponse(doc, callback.getDefaultBreadcrumb());

		}

		throw new URINotFoundException(uriParser);
	}

	@Override
	protected void appendUpdateFormData(BeanType bean, Document doc, Element updateTypeElement, User user, HttpServletRequest req, URIParser uriParser) throws Exception {

		XMLUtils.appendNewElement(doc, updateTypeElement, "flowTypeID", bean.getQueryDescriptor().getStep().getFlow().getFlowType().getFlowTypeID());

	}

	@Override
	public BeanType getBean(Integer beanID) throws SQLException, AccessDeniedException {

		HighLevelQuery<BeanType> query = new HighLevelQuery<BeanType>();

		List<Field> relations = this.getBeanRelations();

		if(relations != null) {
			query.addRelations(relations);
		}

		query.addParameter(queryDAO.getParameterFactory().getParameter(beanID));

		BeanType beanQuery = queryDAO.getAnnotatedDAO().get(query);

		if(beanQuery != null) {

			beanQuery.init(callback.getFlowAdminModule().getQueryDescriptor(beanQuery.getQueryID()), null);

		}

		return beanQuery;

	}

	@Override
	protected void checkAddAccess(User user, HttpServletRequest req, URIParser uriParser) throws AccessDeniedException, URINotFoundException, SQLException {

		throw new RuntimeException("Add is not supported via this CRUD");
	}
	
	@Override
	protected void checkUpdateAccess(BeanType bean, User user, HttpServletRequest req, URIParser uriParser) throws AccessDeniedException, URINotFoundException, SQLException {

		callback.getFlowAdminModule().checkFlowStructureManipulationAccess(user, (Flow) bean.getQueryDescriptor().getStep().getFlow());
	}

	@Override
	protected void checkDeleteAccess(BeanType bean, User user, HttpServletRequest req, URIParser uriParser) throws AccessDeniedException, URINotFoundException, SQLException {

		callback.getFlowAdminModule().checkFlowStructureManipulationAccess(user, (Flow) bean.getQueryDescriptor().getStep().getFlow());
	}

	protected List<Field> getBeanRelations() {

		return null;
	}

	protected QueryDescriptor populateQueryDescriptor(QueryDescriptor queryDescriptor, HttpServletRequest req, List<ValidationError> errors) {

		//TODO validate that the xsdElementName is unique

		try {

			return QUERY_DESCRIPTOR_POPULATOR.populate(queryDescriptor, req);

		} catch (ValidationException exception) {

			errors.addAll(exception.getErrors());

		}

		return queryDescriptor;

	}

	@Override
	protected String getUpdateTitle(BeanType bean, HttpServletRequest req, User user, URIParser uriParser) {

		return callback.getQueryTypeName();
	}
}
