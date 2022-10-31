package com.nordicpeak.flowengine.flowapprovalmodule.cruds;

import java.sql.SQLException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.hierarchy.core.exceptions.AccessDeniedException;
import se.unlogic.hierarchy.core.exceptions.URINotFoundException;
import se.unlogic.hierarchy.core.interfaces.ForegroundModuleResponse;
import se.unlogic.hierarchy.core.utils.crud.ModularCRUD;
import se.unlogic.standardutils.dao.CRUDDAO;
import se.unlogic.standardutils.numbers.NumberUtils;
import se.unlogic.standardutils.validation.ValidationError;
import se.unlogic.webutils.http.URIParser;
import se.unlogic.webutils.populators.annotated.AnnotatedRequestPopulator;

import com.nordicpeak.flowengine.beans.Flow;
import com.nordicpeak.flowengine.flowapprovalmodule.FlowApprovalAdminModule;
import com.nordicpeak.flowengine.flowapprovalmodule.beans.FlowApprovalActivityGroup;
import com.nordicpeak.flowengine.flowapprovalmodule.beans.FlowApprovalActivityGroupMessageTemplate;
import com.nordicpeak.flowengine.flowapprovalmodule.populators.FlowApprovalActivityGroupMessageTemplateBeanIDParser;

public class FlowApprovalActivityGroupMessageCRUD extends ModularCRUD<FlowApprovalActivityGroupMessageTemplate, Integer, User, FlowApprovalAdminModule> {

	public FlowApprovalActivityGroupMessageCRUD(CRUDDAO<FlowApprovalActivityGroupMessageTemplate, Integer> crudDAO, FlowApprovalAdminModule callback) {

		super(FlowApprovalActivityGroupMessageTemplateBeanIDParser.getInstance(), crudDAO, new AnnotatedRequestPopulator<>(FlowApprovalActivityGroupMessageTemplate.class), "FlowApprovalActivityGroupMessageTemplate", "message template", "", callback);
	
		setRequirePostForDelete(true);
	}

	@Override
	protected List<FlowApprovalActivityGroupMessageTemplate> getAllBeans(User user, HttpServletRequest req, URIParser uriParser) throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	protected void checkAddAccess(User user, HttpServletRequest req, URIParser uriParser) throws AccessDeniedException, URINotFoundException, SQLException {

		callback.checkAccess(user, req, uriParser);
	}

	@Override
	protected void checkUpdateAccess(FlowApprovalActivityGroupMessageTemplate bean, User user, HttpServletRequest req, URIParser uriParser) throws AccessDeniedException, URINotFoundException, SQLException {

		callback.checkAccess(user, req, uriParser);
	}

	@Override
	protected void checkDeleteAccess(FlowApprovalActivityGroupMessageTemplate bean, User user, HttpServletRequest req, URIParser uriParser) throws AccessDeniedException, URINotFoundException, SQLException {

		callback.checkAccess(user, req, uriParser);
	}

	@Override
	protected FlowApprovalActivityGroupMessageTemplate populateFromAddRequest(HttpServletRequest req, User user, URIParser uriParser) throws Exception {

		FlowApprovalActivityGroupMessageTemplate template = super.populateFromAddRequest(req, user, uriParser);

		FlowApprovalActivityGroup activityGroup = (FlowApprovalActivityGroup) req.getAttribute("flowApprovalActivityGroup");

		template.setFlowApprovalActivityGroup(activityGroup);

		return template;
	}

	@Override
	protected void redirectToListMethod(HttpServletRequest req, HttpServletResponse res, FlowApprovalActivityGroupMessageTemplate template) throws Exception {

		Flow flow = (Flow) req.getAttribute("flow");

		res.sendRedirect(req.getContextPath() + callback.getFullAlias() + "/showflow/" + flow.getFlowID() + "#messagetemplates");
	}

	@Override
	protected void addBean(FlowApprovalActivityGroupMessageTemplate bean, HttpServletRequest req, User user, URIParser uriParser) throws Exception {

		bean.setFlowApprovalActivityGroup(callback.getActivityGroup(NumberUtils.toInt(uriParser.get(5))));
		super.addBean(bean, req, user, uriParser);
		
	}
	
	@Override
	protected ForegroundModuleResponse beanAdded(FlowApprovalActivityGroupMessageTemplate bean, HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Exception {

		res.sendRedirect(req.getContextPath() + req.getAttribute("extensionRequestURL") + "/showactivitygroup/" + bean.getFlowApprovalActivityGroup().getActivityGroupID());
		return null;
	}

	@Override
	protected ForegroundModuleResponse beanUpdated(FlowApprovalActivityGroupMessageTemplate bean, HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Exception {

		res.sendRedirect(req.getContextPath() + req.getAttribute("extensionRequestURL") + "/showactivitygroup/" + bean.getFlowApprovalActivityGroup().getActivityGroupID());
		return null;
	}
	
	@Override
	protected ForegroundModuleResponse beanDeleted(FlowApprovalActivityGroupMessageTemplate bean, HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Exception {

		res.sendRedirect(req.getContextPath() + req.getAttribute("extensionRequestURL") + "/showactivitygroup/" + bean.getFlowApprovalActivityGroup().getActivityGroupID());

		return null;
	}

	@Override
	public ForegroundModuleResponse list(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser, List<ValidationError> validationErrors) throws Exception {

		return callback.list((String) req.getAttribute("extensionRequestURL"), (Flow) req.getAttribute("flow"), req, res, user, uriParser, validationErrors);
	}


}