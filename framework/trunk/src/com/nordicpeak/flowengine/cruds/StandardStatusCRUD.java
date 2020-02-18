package com.nordicpeak.flowengine.cruds;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

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
import se.unlogic.standardutils.dao.HighLevelQuery;
import se.unlogic.standardutils.dao.QueryParameterFactory;
import se.unlogic.standardutils.dao.TransactionHandler;
import se.unlogic.standardutils.dao.querys.ObjectQuery;
import se.unlogic.standardutils.populators.IntegerPopulator;
import se.unlogic.standardutils.validation.ValidationException;
import se.unlogic.standardutils.xml.XMLUtils;
import se.unlogic.webutils.http.URIParser;
import se.unlogic.webutils.populators.annotated.AnnotatedRequestPopulator;

import com.nordicpeak.flowengine.FlowAdminModule;
import com.nordicpeak.flowengine.beans.DefaultStandardStatusMapping;
import com.nordicpeak.flowengine.beans.FlowAction;
import com.nordicpeak.flowengine.beans.StandardStatus;
import com.nordicpeak.flowengine.beans.StandardStatusGroup;

public class StandardStatusCRUD extends IntegerBasedCRUD<StandardStatus, FlowAdminModule> {

	private QueryParameterFactory<FlowAction, String> flowActionIDParamFactory;
	private QueryParameterFactory<DefaultStandardStatusMapping, String> defaultStatusMappingActionIDParamFactory;
	private QueryParameterFactory<DefaultStandardStatusMapping, StandardStatus> defaultStatusMappingStatusParamFactory;
	
	private CRUDDAO<StandardStatusGroup, Integer> statusGroupDAO;

	public StandardStatusCRUD(CRUDDAO<StandardStatus, Integer> crudDAO, CRUDDAO<StandardStatusGroup, Integer> statusGroupDAO, FlowAdminModule callback) {

		super(crudDAO, new AnnotatedRequestPopulator<StandardStatus>(StandardStatus.class), "StandardStatus", "StandardStatuses", "standard status", "standard statuses", "/standardstatuses", callback);

		flowActionIDParamFactory = callback.getDAOFactory().getFlowActionDAO().getParamFactory("actionID", String.class);
		defaultStatusMappingActionIDParamFactory = callback.getDAOFactory().getDefaultStandardStatusMappingDAO().getParamFactory("actionID", String.class);
		defaultStatusMappingStatusParamFactory = callback.getDAOFactory().getDefaultStandardStatusMappingDAO().getParamFactory("status", StandardStatus.class);
		
		this.statusGroupDAO = statusGroupDAO;
	}

	@Override
	protected void checkAddAccess(User user, HttpServletRequest req, URIParser uriParser) throws AccessDeniedException, URINotFoundException, SQLException {

		checkAccess(user);
		
		Integer statusGroupID = uriParser.getInt(2);
		StandardStatusGroup statusGroup = null;

		if (statusGroupID != null) {
			
			statusGroup = statusGroupDAO.get(statusGroupID);
		}
		
		if (statusGroup == null) {
			throw new URINotFoundException(uriParser);
		}
		
		req.setAttribute("StandardStatusGroup", statusGroup);
	}

	@Override
	protected void checkUpdateAccess(StandardStatus standardStatus, User user, HttpServletRequest req, URIParser uriParser) throws AccessDeniedException, URINotFoundException, SQLException {

		checkAccess(user);
	}

	@Override
	protected void checkDeleteAccess(StandardStatus standardStatus, User user, HttpServletRequest req, URIParser uriParser) throws AccessDeniedException, URINotFoundException, SQLException {

		checkAccess(user);
	}

	private void checkAccess(User user) throws AccessDeniedException {

		if (!AccessUtils.checkAccess(user, callback)) {

			throw new AccessDeniedException("User does not have access to administrate standard statuses");
		}
	}

	@Override
	protected void appendAddFormData(Document doc, Element addTypeElement, User user, HttpServletRequest req, URIParser uriParser) throws Exception {

		XMLUtils.append(doc, addTypeElement, "FlowActions", callback.getDAOFactory().getFlowActionDAO().getAll());
	}

	@Override
	protected void appendUpdateFormData(StandardStatus standardStatus, Document doc, Element updateTypeElement, User user, HttpServletRequest req, URIParser uriParser) throws Exception {

		XMLUtils.append(doc, updateTypeElement, "FlowActions", callback.getDAOFactory().getFlowActionDAO().getAll());
	}
	
	@Override
	protected StandardStatus populateFromAddRequest(HttpServletRequest req, User user, URIParser uriParser) throws ValidationException, Exception {

		StandardStatus status = super.populateFromAddRequest(req, user, uriParser);

		status.setSortIndex(getCurrentMaxSortIndex() + 1);

		return status;
	}

	private int getCurrentMaxSortIndex() throws SQLException {

		ObjectQuery<Integer> query = new ObjectQuery<>(callback.getDataSource(), "SELECT MAX(sortIndex) FROM " + callback.getDAOFactory().getStandardStatusDAO().getTableName(), IntegerPopulator.getPopulator());

		Integer sortIndex = query.executeQuery();

		return sortIndex != null ? sortIndex : 0;
	}

	@Override
	protected void validateAddPopulation(StandardStatus standardStatus, HttpServletRequest req, User user, URIParser uriParser) throws ValidationException, SQLException, Exception {

		setDefaultStatusMappings(standardStatus, req);
		
		StandardStatusGroup statusGroup = (StandardStatusGroup) req.getAttribute("StandardStatusGroup");
		
		standardStatus.setStandardStatusGroup(statusGroup);
	}

	@Override
	protected void validateUpdatePopulation(StandardStatus standardStatus, HttpServletRequest req, User user, URIParser uriParser) throws ValidationException, SQLException, Exception {

		setDefaultStatusMappings(standardStatus, req);
	}

	private void setDefaultStatusMappings(StandardStatus standardStatus, HttpServletRequest req) throws SQLException {

		String[] actions = req.getParameterValues("actionID");

		if (actions != null) {

			List<FlowAction> flowActions = getFlowActions(Arrays.asList(actions));

			if (flowActions != null) {

				ArrayList<DefaultStandardStatusMapping> defaultStatusMappings = new ArrayList<DefaultStandardStatusMapping>(flowActions.size());
				standardStatus.setDefaultStandardStatusMappings(defaultStatusMappings);

				for (FlowAction flowAction : flowActions) {

					defaultStatusMappings.add(new DefaultStandardStatusMapping(flowAction.getActionID(), standardStatus));
				}

				return;
			}
		}

		standardStatus.setDefaultStandardStatusMappings(null);
	}

	private List<FlowAction> getFlowActions(List<String> actionIDs) throws SQLException {

		HighLevelQuery<FlowAction> query = new HighLevelQuery<FlowAction>();

		query.addParameter(flowActionIDParamFactory.getWhereInParameter(actionIDs));

		return callback.getDAOFactory().getFlowActionDAO().getAll(query);
	}

	@Override
	protected void addBean(StandardStatus standardStatus, HttpServletRequest req, User user, URIParser uriParser) throws Exception {

		TransactionHandler transactionHandler = null;

		try {
			transactionHandler = callback.getDAOFactory().getTransactionHandler();

			callback.getDAOFactory().getStandardStatusDAO().add(standardStatus, transactionHandler, null);

			if (standardStatus.getDefaultStandardStatusMappings() != null) {

				clearPreviousActionMappings(standardStatus, transactionHandler);
				callback.getDAOFactory().getDefaultStandardStatusMappingDAO().addAll(standardStatus.getDefaultStandardStatusMappings(), transactionHandler, null);
			}

			transactionHandler.commit();

		} finally {

			TransactionHandler.autoClose(transactionHandler);
		}
	}

	@Override
	protected void updateBean(StandardStatus standardStatus, HttpServletRequest req, User user, URIParser uriParser) throws Exception {

		TransactionHandler transactionHandler = null;

		try {
			transactionHandler = callback.getDAOFactory().getTransactionHandler();

			callback.getDAOFactory().getStandardStatusDAO().update(standardStatus, transactionHandler, null);

			clearStatusActionMappings(standardStatus, transactionHandler);

			if (standardStatus.getDefaultStandardStatusMappings() != null) {

				clearPreviousActionMappings(standardStatus, transactionHandler);
				callback.getDAOFactory().getDefaultStandardStatusMappingDAO().addAll(standardStatus.getDefaultStandardStatusMappings(), transactionHandler, null);
			}

			transactionHandler.commit();

		} finally {

			TransactionHandler.autoClose(transactionHandler);
		}
	}

	private void clearPreviousActionMappings(StandardStatus standardStatus, TransactionHandler transactionHandler) throws SQLException {

		ArrayList<String> actionIDs = new ArrayList<String>(standardStatus.getDefaultStandardStatusMappings().size());

		for (DefaultStandardStatusMapping defaultStatusMapping : standardStatus.getDefaultStandardStatusMappings()) {

			actionIDs.add(defaultStatusMapping.getActionID());
		}

		HighLevelQuery<DefaultStandardStatusMapping> query = new HighLevelQuery<DefaultStandardStatusMapping>();
		query.addParameter(defaultStatusMappingStatusParamFactory.getParameter(standardStatus));
		query.addParameter(defaultStatusMappingActionIDParamFactory.getWhereInParameter(actionIDs));

		callback.getDAOFactory().getDefaultStandardStatusMappingDAO().delete(query, transactionHandler);
	}

	private void clearStatusActionMappings(StandardStatus standardStatus, TransactionHandler transactionHandler) throws SQLException {

		HighLevelQuery<DefaultStandardStatusMapping> query = new HighLevelQuery<DefaultStandardStatusMapping>();
		query.addParameter(defaultStatusMappingStatusParamFactory.getParameter(standardStatus));

		callback.getDAOFactory().getDefaultStandardStatusMappingDAO().delete(query, transactionHandler);
	}
	
		@Override
	protected ForegroundModuleResponse beanAdded(StandardStatus standardStatus, HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Exception {

		return beanEvent(standardStatus, req, res, CRUDAction.ADD);
	}

	@Override
	protected ForegroundModuleResponse beanUpdated(StandardStatus standardStatus, HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Exception {

		return beanEvent(standardStatus, req, res, CRUDAction.UPDATE);
	}

	@Override
	protected ForegroundModuleResponse beanDeleted(StandardStatus standardStatus, HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Exception {

		return beanEvent(standardStatus, req, res, CRUDAction.DELETE);
	}

	private ForegroundModuleResponse beanEvent(StandardStatus standardStatus, HttpServletRequest req, HttpServletResponse res, CRUDAction action) throws Exception {

		callback.getEventHandler().sendEvent(StandardStatus.class, new CRUDEvent<StandardStatus>(action, standardStatus), EventTarget.ALL);

		redirectToListMethod(req, res, standardStatus);
		return null;
	}
	
	@Override
	protected void redirectToListMethod(HttpServletRequest req, HttpServletResponse res, StandardStatus standardStatus) throws Exception {

		res.sendRedirect(req.getContextPath() + callback.getFullAlias() + "/showstandardstatusgroup/" + standardStatus.getStandardStatusGroup().getStatusGroupID());
	}
}
