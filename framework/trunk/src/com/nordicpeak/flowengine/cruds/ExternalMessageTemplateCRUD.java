package com.nordicpeak.flowengine.cruds;

import java.sql.SQLException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.hierarchy.core.enums.CRUDAction;
import se.unlogic.hierarchy.core.enums.EventTarget;
import se.unlogic.hierarchy.core.events.CRUDEvent;
import se.unlogic.hierarchy.core.exceptions.AccessDeniedException;
import se.unlogic.hierarchy.core.exceptions.URINotFoundException;
import se.unlogic.hierarchy.core.interfaces.ForegroundModuleResponse;
import se.unlogic.hierarchy.core.utils.crud.ModularCRUD;
import se.unlogic.standardutils.dao.CRUDDAO;
import se.unlogic.standardutils.validation.ValidationError;
import se.unlogic.standardutils.validation.ValidationException;
import se.unlogic.webutils.http.URIParser;
import se.unlogic.webutils.populators.annotated.AnnotatedRequestPopulator;

import com.nordicpeak.flowengine.beans.ExternalMessageTemplate;
import com.nordicpeak.flowengine.beans.Flow;
import com.nordicpeak.flowengine.beans.FlowFamily;
import com.nordicpeak.flowengine.interfaces.FlowAdminCRUDCallback;
import com.nordicpeak.flowengine.populators.ExternalMessageTemplateBeanIDParser;

public class ExternalMessageTemplateCRUD extends ModularCRUD<ExternalMessageTemplate, Integer, User, FlowAdminCRUDCallback> {

	public ExternalMessageTemplateCRUD(CRUDDAO<ExternalMessageTemplate, Integer> crudDAO, FlowAdminCRUDCallback callback) {

		super(ExternalMessageTemplateBeanIDParser.getInstance(), crudDAO, new AnnotatedRequestPopulator<ExternalMessageTemplate>(ExternalMessageTemplate.class), "ExternalMessageTemplate", "external message template", "", callback);
	
		setRequirePostForDelete(true);
	}

	@Override
	protected List<ExternalMessageTemplate> getAllBeans(User user, HttpServletRequest req, URIParser uriParser) throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	protected void checkAddAccess(User user, HttpServletRequest req, URIParser uriParser) throws AccessDeniedException, URINotFoundException, SQLException {

		checkAccess(user, req, uriParser);
	}

	@Override
	protected void checkUpdateAccess(ExternalMessageTemplate bean, User user, HttpServletRequest req, URIParser uriParser) throws AccessDeniedException, URINotFoundException, SQLException {

		checkAccess(user, req, uriParser);
	}

	@Override
	protected void checkDeleteAccess(ExternalMessageTemplate bean, User user, HttpServletRequest req, URIParser uriParser) throws AccessDeniedException, URINotFoundException, SQLException {

		checkAccess(user, req, uriParser);
	}

	private void checkAccess(User user, HttpServletRequest req, URIParser uriParser) throws AccessDeniedException, URINotFoundException, SQLException {

		Flow flow = callback.getRequestedFlow(req, user, uriParser);

		if (flow == null) {

			throw new URINotFoundException(uriParser);
		}

		if (!callback.hasFlowAccess(user, flow)) {

			throw new AccessDeniedException("User does not have access to flow type " + flow.getFlowType());
		}

		req.setAttribute("flow", flow);
	}

	@Override
	protected ExternalMessageTemplate populateFromAddRequest(HttpServletRequest req, User user, URIParser uriParser) throws ValidationException, Exception {

		ExternalMessageTemplate template = super.populateFromAddRequest(req, user, uriParser);

		Flow flow = (Flow) req.getAttribute("flow");

		template.setFlowFamily(flow.getFlowFamily());

		return template;
	}

	@Override
	protected void redirectToListMethod(HttpServletRequest req, HttpServletResponse res, ExternalMessageTemplate template) throws Exception {

		Flow flow = (Flow) req.getAttribute("flow");

		res.sendRedirect(req.getContextPath() + callback.getFullAlias() + "/showflow/" + flow.getFlowID() + "#externalmessagetemplates");
	}

	@Override
	public ForegroundModuleResponse list(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser, List<ValidationError> validationErrors) throws Exception {

		return callback.showFlow(req, res, user, uriParser, validationErrors);
	}

	@Override
	protected ForegroundModuleResponse beanAdded(ExternalMessageTemplate template, HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Exception {

		callback.addFlowFamilyEvent(callback.getEventExternalMessageTemplatesAddedMessage() + " \"" + template.getName() + "\"", template.getFlowFamily(), user);

		return beanEvent(template, req, res);
	}

	@Override
	protected ForegroundModuleResponse beanUpdated(ExternalMessageTemplate template, HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Exception {

		callback.addFlowFamilyEvent(callback.getEventExternalMessageTemplatesUpdatedMessage() + " \"" + template.getName() + "\"", template.getFlowFamily(), user);

		return beanEvent(template, req, res);
	}

	@Override
	protected ForegroundModuleResponse beanDeleted(ExternalMessageTemplate template, HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Exception {

		callback.addFlowFamilyEvent(callback.getEventExternalMessageTemplatesDeletedMessage() + " \"" + template.getName() + "\"", template.getFlowFamily(), user);

		return beanEvent(template, req, res);
	}

	private ForegroundModuleResponse beanEvent(ExternalMessageTemplate template, HttpServletRequest req, HttpServletResponse res) throws Exception {

		callback.getEventHandler().sendEvent(FlowFamily.class, new CRUDEvent<FlowFamily>(CRUDAction.UPDATE, template.getFlowFamily()), EventTarget.ALL);

		redirectToListMethod(req, res, template);
		return null;
	}

}
