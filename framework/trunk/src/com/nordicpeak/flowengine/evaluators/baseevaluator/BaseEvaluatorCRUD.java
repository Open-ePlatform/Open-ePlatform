package com.nordicpeak.flowengine.evaluators.baseevaluator;

import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.w3c.dom.Document;

import se.unlogic.hierarchy.core.beans.SimpleForegroundModuleResponse;
import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.hierarchy.core.enums.CRUDAction;
import se.unlogic.hierarchy.core.enums.EventTarget;
import se.unlogic.hierarchy.core.events.CRUDEvent;
import se.unlogic.hierarchy.core.exceptions.AccessDeniedException;
import se.unlogic.hierarchy.core.exceptions.URINotFoundException;
import se.unlogic.hierarchy.core.interfaces.ForegroundModuleResponse;
import se.unlogic.hierarchy.core.utils.AdvancedIntegerBasedCRUD;
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

import com.nordicpeak.flowengine.beans.EvaluatorDescriptor;
import com.nordicpeak.flowengine.beans.Flow;

public class BaseEvaluatorCRUD<BeanType extends BaseEvaluator, CallbackType extends BaseEvaluatorCRUDCallback> extends AdvancedIntegerBasedCRUD<BeanType, CallbackType> {

	protected AnnotatedDAOWrapper<BeanType, Integer> evaluatorDAO;

	protected static AnnotatedRequestPopulator<EvaluatorDescriptor> EVALUATOR_DESCRIPTOR_POPULATOR = new AnnotatedRequestPopulator<EvaluatorDescriptor>(EvaluatorDescriptor.class);

	public BaseEvaluatorCRUD(Class<BeanType> beanClass, AnnotatedDAOWrapper<BeanType, Integer> evaluatorDAO, BeanRequestPopulator<BeanType> populator, String typeElementName, String typeLogName, String listMethodAlias, CallbackType callback) {

		super(beanClass, evaluatorDAO, populator, typeElementName, typeLogName, listMethodAlias, callback);

		this.evaluatorDAO = evaluatorDAO;
	}

	@Override
	protected void updateBean(BeanType bean, HttpServletRequest req, User user, URIParser uriParser) throws Exception {

		if(hasURLRewriteAnnotations){

			URLRewriter.removeAbsoluteLinkUrls(bean, req);
		}

		TransactionHandler transactionHandler = null;

		try {

			transactionHandler = evaluatorDAO.getAnnotatedDAO().createTransaction();

			evaluatorDAO.update(bean, transactionHandler);

			callback.getFlowAdminModule().getDAOFactory().getEvaluatorDescriptorDAO().update((EvaluatorDescriptor) bean.getEvaluatorDescriptor(), transactionHandler, null);

			transactionHandler.commit();

		} finally{

			TransactionHandler.autoClose(transactionHandler);
		}

	}

	@Override
	protected ForegroundModuleResponse beanUpdated(BeanType bean, HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Exception {

		callback.getEventHandler().sendEvent(EvaluatorDescriptor.class, new CRUDEvent<EvaluatorDescriptor>(CRUDAction.UPDATE, (EvaluatorDescriptor) bean.getEvaluatorDescriptor()), EventTarget.ALL);
		
		callback.getFlowAdminModule().addFlowFamilyEvent(callback.getFlowAdminModule().getEventEvaluatorUpdatedMessage() + " \"" + bean.getEvaluatorDescriptor().getName() + "\"", bean.getEvaluatorDescriptor().getQueryDescriptor().getStep().getFlow(), user);

		res.sendRedirect(callback.getFlowAdminModule().getFlowQueryRedirectURL(req, bean.getEvaluatorDescriptor().getQueryDescriptor().getStep().getFlow().getFlowID()));

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
	public BeanType getBean(Integer beanID) throws SQLException, AccessDeniedException {

		HighLevelQuery<BeanType> query = new HighLevelQuery<BeanType>();

		List<Field> relations = this.getBeanRelations();

		if(relations != null) {
			query.addRelations(relations);
		}

		query.addParameter(evaluatorDAO.getParameterFactory().getParameter(beanID));

		BeanType evaluator = evaluatorDAO.getAnnotatedDAO().get(query);

		if(evaluator != null) {

			evaluator.init(callback.getFlowAdminModule().getEvaluatorDescriptor(evaluator.getEvaluatorID()), null);

		}

		return evaluator;

	}

	@Override
	protected void checkUpdateAccess(BeanType bean, User user, HttpServletRequest req, URIParser uriParser) throws AccessDeniedException, URINotFoundException, SQLException {

		callback.getFlowAdminModule().checkFlowStructureManipulationAccess(user, (Flow) bean.getEvaluatorDescriptor().getQueryDescriptor().getStep().getFlow());
	}

	@Override
	protected void checkDeleteAccess(BeanType bean, User user, HttpServletRequest req, URIParser uriParser) throws AccessDeniedException, URINotFoundException, SQLException {

		callback.getFlowAdminModule().checkFlowStructureManipulationAccess(user, (Flow) bean.getEvaluatorDescriptor().getQueryDescriptor().getStep().getFlow());
	}

	protected List<Field> getBeanRelations() {

		return null;
	}

	protected void populateDescriptor(EvaluatorDescriptor descriptor, HttpServletRequest req, List<ValidationError> errors) {

		try {

			EVALUATOR_DESCRIPTOR_POPULATOR.populate(descriptor, req);

		} catch (ValidationException exception) {

			errors.addAll(exception.getErrors());

		}
	}

	@Override
	protected BeanType populateFromUpdateRequest(BeanType bean, HttpServletRequest req, User user, URIParser uriParser) throws ValidationException, Exception {

		List<ValidationError> validationErrors = new ArrayList<ValidationError>(5);

		this.populateDescriptor((EvaluatorDescriptor)bean.getEvaluatorDescriptor(), req, validationErrors);

		try{
			super.populateFromUpdateRequest(bean, req, user, uriParser);

		}catch(ValidationException e){

			if(validationErrors.isEmpty()){
				throw e;
			}

			validationErrors.addAll(e.getErrors());
		}

		if(!validationErrors.isEmpty()){

			throw new ValidationException(validationErrors);
		}

		return bean;
	}

}
