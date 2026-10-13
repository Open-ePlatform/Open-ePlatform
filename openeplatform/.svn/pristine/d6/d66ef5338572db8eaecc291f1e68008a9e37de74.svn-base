package com.nordicpeak.flowengine.cruds;

import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.hierarchy.core.enums.CRUDAction;
import se.unlogic.hierarchy.core.enums.EventTarget;
import se.unlogic.hierarchy.core.events.CRUDEvent;
import se.unlogic.hierarchy.core.exceptions.AccessDeniedException;
import se.unlogic.hierarchy.core.exceptions.URINotFoundException;
import se.unlogic.hierarchy.core.interfaces.ForegroundModuleResponse;
import se.unlogic.hierarchy.core.utils.AccessUtils;
import se.unlogic.hierarchy.core.utils.IntegerBasedCRUD;
import se.unlogic.standardutils.dao.CRUDDAO;
import se.unlogic.webutils.http.URIParser;
import se.unlogic.webutils.populators.annotated.AnnotatedRequestPopulator;

import com.nordicpeak.flowengine.FlowAdminModule;
import com.nordicpeak.flowengine.beans.StandardStatusGroup;

public class StandardStatusGroupCRUD extends IntegerBasedCRUD<StandardStatusGroup, FlowAdminModule> {

	public StandardStatusGroupCRUD(CRUDDAO<StandardStatusGroup, Integer> crudDAO, FlowAdminModule callback) {

		super(crudDAO, new AnnotatedRequestPopulator<StandardStatusGroup>(StandardStatusGroup.class), "StandardStatusGroup", "StandardStatusGroups", "standard status group", "standard status groups", "/standardstatuses", callback);

		setRequirePostForDelete(true);
	}

	@Override
	protected void checkAddAccess(User user, HttpServletRequest req, URIParser uriParser) throws AccessDeniedException, URINotFoundException, SQLException {

		checkAccess(user);
	}

	@Override
	protected void checkUpdateAccess(StandardStatusGroup statusGroup, User user, HttpServletRequest req, URIParser uriParser) throws AccessDeniedException, URINotFoundException, SQLException {

		checkAccess(user);
	}

	@Override
	protected void checkDeleteAccess(StandardStatusGroup statusGroup, User user, HttpServletRequest req, URIParser uriParser) throws AccessDeniedException, URINotFoundException, SQLException {

		if (statusGroup.getStatusGroupID() == 1) {
			throw new AccessDeniedException("May not remove standard status group 1");
		}
		
		checkAccess(user);
	}

	private void checkAccess(User user) throws AccessDeniedException {

		if (!AccessUtils.checkAccess(user, callback)) {

			throw new AccessDeniedException("User does not have access to administrate standard statuses");
		}
	}

	@Override
	protected ForegroundModuleResponse beanAdded(StandardStatusGroup statusGroup, HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Exception {

		beanEvent(statusGroup, req, res, CRUDAction.ADD);
		
		res.sendRedirect(req.getContextPath() + callback.getFullAlias() + "/showstandardstatusgroup/" + statusGroup.getStatusGroupID());
		return null;
	}

	@Override
	protected ForegroundModuleResponse beanUpdated(StandardStatusGroup statusGroup, HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Exception {

		beanEvent(statusGroup, req, res, CRUDAction.UPDATE);
		
		redirectToListMethod(req, res, statusGroup);
		return null;
	}

	@Override
	protected ForegroundModuleResponse beanDeleted(StandardStatusGroup statusGroup, HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Exception {

		beanEvent(statusGroup, req, res, CRUDAction.DELETE);
		
		redirectToListMethod(req, res, statusGroup);
		return null;
	}

	private void beanEvent(StandardStatusGroup statusGroup, HttpServletRequest req, HttpServletResponse res, CRUDAction action) throws Exception {

		callback.getEventHandler().sendEvent(StandardStatusGroup.class, new CRUDEvent<StandardStatusGroup>(action, statusGroup), EventTarget.ALL);
	}

}
