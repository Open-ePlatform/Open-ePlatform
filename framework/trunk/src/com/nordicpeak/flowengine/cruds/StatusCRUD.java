package com.nordicpeak.flowengine.cruds;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import se.unlogic.hierarchy.core.beans.Group;
import se.unlogic.hierarchy.core.beans.SimpleForegroundModuleResponse;
import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.hierarchy.core.enums.CRUDAction;
import se.unlogic.hierarchy.core.enums.EventTarget;
import se.unlogic.hierarchy.core.events.CRUDEvent;
import se.unlogic.hierarchy.core.exceptions.AccessDeniedException;
import se.unlogic.hierarchy.core.exceptions.URINotFoundException;
import se.unlogic.hierarchy.core.interfaces.ForegroundModuleResponse;
import se.unlogic.hierarchy.core.interfaces.ViewFragment;
import se.unlogic.hierarchy.core.utils.AccessUtils;
import se.unlogic.hierarchy.core.utils.IntegerBasedCRUD;
import se.unlogic.hierarchy.core.utils.crud.FragmentLinkScriptFilter;
import se.unlogic.hierarchy.core.utils.crud.ModuleResponseFilter;
import se.unlogic.standardutils.collections.CollectionUtils;
import se.unlogic.standardutils.dao.CRUDDAO;
import se.unlogic.standardutils.dao.HighLevelQuery;
import se.unlogic.standardutils.dao.QueryParameterFactory;
import se.unlogic.standardutils.dao.TransactionHandler;
import se.unlogic.standardutils.dao.querys.ObjectQuery;
import se.unlogic.standardutils.populators.IntegerPopulator;
import se.unlogic.standardutils.validation.ValidationError;
import se.unlogic.standardutils.validation.ValidationException;
import se.unlogic.standardutils.xml.XMLUtils;
import se.unlogic.webutils.http.URIParser;
import se.unlogic.webutils.populators.annotated.AnnotatedRequestPopulator;

import com.nordicpeak.flowengine.FlowAdminModule;
import com.nordicpeak.flowengine.FlowInstanceAdminModule;
import com.nordicpeak.flowengine.beans.DefaultStatusMapping;
import com.nordicpeak.flowengine.beans.Flow;
import com.nordicpeak.flowengine.beans.FlowAction;
import com.nordicpeak.flowengine.beans.FlowFamily;
import com.nordicpeak.flowengine.beans.FlowType;
import com.nordicpeak.flowengine.beans.Status;
import com.nordicpeak.flowengine.interfaces.StatusFormExtensionProvider;
import com.nordicpeak.flowengine.utils.FlowFamilyUtils;
import com.nordicpeak.flowengine.validationerrors.UnauthorizedUserNotManagerValidationError;

public class StatusCRUD extends IntegerBasedCRUD<Status, FlowAdminModule> {

	private static final String FLOW_FLOWFAMILY_SQL = "SELECT flowFamilyID FROM flowengine_flows WHERE flowID = ?";
	private static final String STATUS_FLOWFAMILY_SQL = "SELECT flowFamilyID FROM flowengine_flows WHERE flowID = (SELECT flowID FROM flowengine_flow_statuses WHERE statusID = ?)";
	
	private QueryParameterFactory<FlowAction, String> flowActionIDParamFactory;
	private QueryParameterFactory<DefaultStatusMapping, String> defaultStatusMappingActionIDParamFactory;
	private QueryParameterFactory<DefaultStatusMapping, Flow> defaultStatusMappingFlowParamFactory;
	private QueryParameterFactory<DefaultStatusMapping, Status> defaultStatusMappingStatusParamFactory;
	
	private List<StatusFormExtensionProvider> statusFormExtensionProviders;
	private ModuleResponseFilter<Status> moduleResponseFilter;
	
	public StatusCRUD(CRUDDAO<Status, Integer> crudDAO, FlowAdminModule callback, List<StatusFormExtensionProvider> statusFormExtensionProviders) {
		
		super(crudDAO, new AnnotatedRequestPopulator<Status>(Status.class), "Status", "status", "", callback);
		
		flowActionIDParamFactory = callback.getDAOFactory().getFlowActionDAO().getParamFactory("actionID", String.class);
		defaultStatusMappingActionIDParamFactory = callback.getDAOFactory().getDefaultStatusMappingDAO().getParamFactory("actionID", String.class);
		defaultStatusMappingFlowParamFactory = callback.getDAOFactory().getDefaultStatusMappingDAO().getParamFactory("flow", Flow.class);
		defaultStatusMappingStatusParamFactory = callback.getDAOFactory().getDefaultStatusMappingDAO().getParamFactory("status", Status.class);
		
		moduleResponseFilter = new FragmentLinkScriptFilter<>();
		this.statusFormExtensionProviders = statusFormExtensionProviders;
	}
	
	@Override
	protected void checkAddAccess(User user, HttpServletRequest req, URIParser uriParser) throws AccessDeniedException, URINotFoundException, SQLException {
		
		Flow flow = callback.getRequestedFlow(req, user, uriParser);
		
		if (flow == null) {
			
			throw new URINotFoundException(uriParser);
		}
		
		checkFlowTypeAccess(user, flow.getFlowType());
		
		req.setAttribute("flow", flow);
	}
	
	@Override
	protected void checkUpdateAccess(Status bean, User user, HttpServletRequest req, URIParser uriParser) throws AccessDeniedException, URINotFoundException, SQLException {
		
		checkFlowTypeAccess(user, bean.getFlow().getFlowType());
	}
	
	@Override
	protected void checkDeleteAccess(Status bean, User user, HttpServletRequest req, URIParser uriParser) throws AccessDeniedException, URINotFoundException, SQLException {
		
		checkFlowTypeAccess(user, bean.getFlow().getFlowType());
		
		if (callback.getFlowInstanceCount(bean) > 0) {
			
			throw new AccessDeniedException("Unable to delete status " + bean + " since it has one or more flow instances connected to it.");
		}
	}
	
	private void checkFlowTypeAccess(User user, FlowType flowType) throws AccessDeniedException {
		
		if (!AccessUtils.checkAccess(user, flowType.getAdminAccessInterface()) && !AccessUtils.checkAccess(user, callback)) {
			
			throw new AccessDeniedException("User does not have access to flow type " + flowType);
		}
	}
	
	@Override
	protected Status populateFromAddRequest(HttpServletRequest req, User user, URIParser uriParser) throws ValidationException, Exception {

		List<ValidationError> validationErrors = new ArrayList<>();
		Status status = null;

		try {

			status = super.populateFromAddRequest(req, user, uriParser);

		} catch (ValidationException e) {

			validationErrors.addAll(e.getErrors());
		}

		return populate(status, req, user, uriParser, validationErrors);
	}

	@Override
	protected Status populateFromUpdateRequest(Status status, HttpServletRequest req, User user, URIParser uriParser) throws ValidationException, Exception {

		List<ValidationError> validationErrors = new ArrayList<>();

		try {

			status = super.populateFromUpdateRequest(status, req, user, uriParser);

		} catch (ValidationException e) {

			validationErrors.addAll(e.getErrors());
		}

		return populate(status, req, user, uriParser, validationErrors);
	}

	protected Status populate(Status status, HttpServletRequest req, User user, URIParser uriParser, List<ValidationError> validationErrors) throws ValidationException, Exception {

		if (extensionRequestsHasValidationErrors(req, user, uriParser)) {

			validationErrors.add(new ValidationError("ExtensionErrors"));
		}

		if (!validationErrors.isEmpty()) {

			throw new ValidationException(validationErrors);
		}

		return status;
	}

	private boolean extensionRequestsHasValidationErrors(HttpServletRequest req, User user, URIParser uriParser) {

		boolean hasErrors = false;

		if (!CollectionUtils.isEmpty(statusFormExtensionProviders)) {

			for (StatusFormExtensionProvider extensionProvider : statusFormExtensionProviders) {

				try {
					
					extensionProvider.validateRequest(req, user, uriParser);

				} catch (ValidationException e) {

					req.setAttribute(extensionProvider.getProviderID(), e);

					hasErrors = true;
				}
			}
		}

		return hasErrors;
	}
	
	@Override
	protected ForegroundModuleResponse beanAdded(Status bean, HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Exception {
		
		callback.addFlowFamilyEvent(callback.getEventStatusAddedMessage() + " \"" + bean.getName() + "\"", bean.getFlow(), user);

		processExtensionRequests(bean, req, user, uriParser);

		return beanEvent(bean, req, res, CRUDAction.ADD);
	}
	
	@Override
	protected ForegroundModuleResponse beanUpdated(Status bean, HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Exception {
		
		callback.addFlowFamilyEvent(callback.getEventStatusUpdatedMessage() + " \"" + bean.getName() + "\"", bean.getFlow(), user);
		
		processExtensionRequests(bean, req, user, uriParser);
		
		return beanEvent(bean, req, res, CRUDAction.UPDATE);
	}
	
	private void processExtensionRequests(Status status, HttpServletRequest req, User user, URIParser uriParser) throws Exception {
		
		if (!CollectionUtils.isEmpty(statusFormExtensionProviders)) {
			
			for (StatusFormExtensionProvider extensionProvider : statusFormExtensionProviders) {
				
				try {
					
					extensionProvider.processRequest(status, req, user, uriParser);

				} catch (Exception e) {
					
					log.error("Error processing request for extension provider " + extensionProvider + " for user " + user, e);
				}
			}
		}
	}
	
	@Override
	protected ForegroundModuleResponse beanDeleted(Status bean, HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Exception {
		
		callback.addFlowFamilyEvent(callback.getEventStatusDeletedMessage() + " \"" + bean.getName() + "\"", bean.getFlow(), user);
		
		return beanEvent(bean, req, res, CRUDAction.DELETE);
	}
	
	private ForegroundModuleResponse beanEvent(Status bean, HttpServletRequest req, HttpServletResponse res, CRUDAction action) throws IOException {
		
		callback.getEventHandler().sendEvent(Status.class, new CRUDEvent<Status>(action, bean), EventTarget.ALL);
		
		callback.redirectToMethod(req, res, "/showflow/" + bean.getFlow().getFlowID() + "#statuses");
		
		return null;
	}
	
	@Override
	protected void appendAddFormData(Document doc, Element addTypeElement, User user, HttpServletRequest req, URIParser uriParser) throws Exception {
		
		Flow flow = (Flow) req.getAttribute("flow");
		appendFormData(flow, null, doc, addTypeElement, user, req, uriParser);
	}
	
	@Override
	protected void appendUpdateFormData(Status bean, Document doc, Element updateTypeElement, User user, HttpServletRequest req, URIParser uriParser) throws Exception {
		
		appendFormData(bean.getFlow(), bean, doc, updateTypeElement, user, req, uriParser);
		
		if (bean.getManagerGroupIDs() != null) {
			
			XMLUtils.append(doc, updateTypeElement, "ManagerGroups", callback.getGroupHandler().getGroups(bean.getManagerGroupIDs(), false));
		}
		
		if (bean.getManagerUserIDs() != null) {
			
			XMLUtils.append(doc, updateTypeElement, "ManagerUsers", callback.getUserHandler().getUsers(bean.getManagerUserIDs(), false, true));
		}
	}
	
	protected void appendFormData(Flow flow, Status status, Document doc, Element typeElement, User user, HttpServletRequest req, URIParser uriParser) throws Exception {
		
		XMLUtils.append(doc, typeElement, "FlowActions", callback.getDAOFactory().getFlowActionDAO().getAll());
		
		FlowFamily flowFamily = getFlowFamily(flow);

		XMLUtils.append(doc, typeElement, "ExternalMessageTemplates", flowFamily.getExternalMessageTemplates());
		
		typeElement.appendChild(flow.toXML(doc));
		
		appendExtensionFormData(status, flow, doc, typeElement, user, req, uriParser);
	}
	
	private void appendExtensionFormData(Status status, Flow flow, Document doc, Element targetElement, User user, HttpServletRequest req, URIParser uriParser) throws Exception {
		
		if (!CollectionUtils.isEmpty(statusFormExtensionProviders)) {

			Element pluginFragments = XMLUtils.appendNewElement(doc, targetElement, "ViewFragmentExtension");
			
			for (StatusFormExtensionProvider extensionProvider : statusFormExtensionProviders) {
				
				try {
					
					ValidationException validationException = (ValidationException) req.getAttribute(extensionProvider.getProviderID());
					
					ViewFragment extensionSettings = extensionProvider.getViewFragment(status, flow, req, user, uriParser, validationException);
					
					if (extensionSettings != null) {
						
						pluginFragments.appendChild(extensionSettings.toXML(doc));
						FragmentLinkScriptFilter.addViewFragment(extensionSettings, req);
					}
					
				} catch (Exception e) {

					log.error("Error getting form view fragment for extension provider " + extensionProvider + " for user " + user, e);
				}
			}
		}
	}

	@Override
	protected SimpleForegroundModuleResponse createAddFormModuleResponse(Document doc, HttpServletRequest req, User user, URIParser uriParser) {

		SimpleForegroundModuleResponse moduleResponse = super.createAddFormModuleResponse(doc, req, user, uriParser);

		return moduleResponseFilter.filterAddFormModuleResponse(moduleResponse, doc, req, user, uriParser);
	}

	@Override
	protected SimpleForegroundModuleResponse createUpdateFormModuleResponse(Status status, Document doc, HttpServletRequest req, User user, URIParser uriParser) {

		SimpleForegroundModuleResponse moduleResponse = super.createUpdateFormModuleResponse(status, doc, req, user, uriParser);
		
		return moduleResponseFilter.filterUpdateFormModuleResponse(moduleResponse, status, doc, req, user, uriParser);
	}
	
	@Override
	protected void validateAddPopulation(Status bean, HttpServletRequest req, User user, URIParser uriParser) throws ValidationException, SQLException, Exception {
		
		bean.setFlow((Flow) req.getAttribute("flow"));
		bean.setSortIndex(Integer.MAX_VALUE);
		
		FlowFamily flowFamily = getFlowFamily(bean.getFlow());
		
		validatePopulation(bean, flowFamily, req, user, uriParser);
		
		setDefaultStatusMappings(bean, req);
	}
	
	@Override
	protected void validateUpdatePopulation(Status bean, HttpServletRequest req, User user, URIParser uriParser) throws ValidationException, SQLException, Exception {
		
		//TODO validate required action ID's
		
		FlowFamily flowFamily = getFlowFamily(bean);
		
		validatePopulation(bean, flowFamily, req, user, uriParser);
		
		setDefaultStatusMappings(bean, req);
	}
	
	protected void validatePopulation(Status bean, FlowFamily flowFamily, HttpServletRequest req, User user, URIParser uriParser) throws ValidationException, SQLException, Exception {

		List<ValidationError> errors = new ArrayList<ValidationError>();

		if (bean.isUseAccessCheck()) {

			List<Integer> selectedUserIDs = bean.getManagerUserIDs();
			List<Integer> selectedGroupIDs = bean.getManagerGroupIDs();

			List<User> allowedManagerUsers = FlowFamilyUtils.getAllowedManagerUsers(flowFamily, callback.getUserHandler());

			if (!CollectionUtils.isEmpty(selectedUserIDs)) {

				for (Integer userID : selectedUserIDs) {

					User selectedUser = callback.getUserHandler().getUser(userID, true, false);

					if (selectedUser == null) {

						errors.add(FlowInstanceAdminModule.ONE_OR_MORE_SELECTED_MANAGER_USERS_NOT_FOUND_VALIDATION_ERROR);
						continue;
					}

					if (allowedManagerUsers != null && allowedManagerUsers.contains(selectedUser)) {
						continue;
					}

					errors.add(new UnauthorizedUserNotManagerValidationError(selectedUser));
				}
			}

			if (!CollectionUtils.isEmpty(selectedGroupIDs)) {

				for (Integer groupID : selectedGroupIDs) {

					Group selectedGroup = callback.getGroupHandler().getGroup(groupID, false);

					if (selectedGroup == null) {

						errors.add(FlowInstanceAdminModule.ONE_OR_MORE_SELECTED_MANAGER_GROUPS_NOT_FOUND_VALIDATION_ERROR);
						continue;
					}
				}
			}

		} else {

			bean.setManagerUserIDs(null);
			bean.setManagerGroupIDs(null);
		}

		if (bean.isAddExternalMessage() && bean.isRequireSigning()) {

			errors.add(FlowAdminModule.EXTERNAL_MESSAGE_AND_REQUIRED_SIGNING_MUTUAL_EXCLUSIVE_VALIDATION_ERROR);
		}

		if (!errors.isEmpty()) {
			
			throw new ValidationException(errors);
		}
	}
	
	private FlowFamily getFlowFamily(Status status) throws SQLException {
		
		ObjectQuery<Integer> query = new ObjectQuery<Integer>(callback.getDataSource(), STATUS_FLOWFAMILY_SQL, IntegerPopulator.getPopulator());
		query.setInt(1, status.getStatusID());
		
		Integer flowFamilyID = query.executeQuery();
		
		return callback.getFlowFamily(flowFamilyID);
	}
	
	private FlowFamily getFlowFamily(Flow flow) throws SQLException {
		
		ObjectQuery<Integer> query = new ObjectQuery<Integer>(callback.getDataSource(), FLOW_FLOWFAMILY_SQL, IntegerPopulator.getPopulator());
		query.setInt(1, flow.getFlowID());
		
		Integer flowFamilyID = query.executeQuery();
		
		return callback.getFlowFamily(flowFamilyID);
	}
	
	private void setDefaultStatusMappings(Status bean, HttpServletRequest req) throws SQLException {
		
		String[] actions = req.getParameterValues("actionID");
		
		if (actions != null) {
			
			List<FlowAction> flowActions = getFlowActions(Arrays.asList(actions));
			
			if (flowActions != null) {
				
				ArrayList<DefaultStatusMapping> defaultStatusMappings = new ArrayList<DefaultStatusMapping>(flowActions.size());
				bean.setDefaultStatusMappings(defaultStatusMappings);
				
				for (FlowAction flowAction : flowActions) {
					
					defaultStatusMappings.add(new DefaultStatusMapping(flowAction.getActionID(), bean.getFlow(), bean));
				}
				
				return;
			}
		}
		
		bean.setDefaultStatusMappings(null);
	}
	
	private List<FlowAction> getFlowActions(List<String> actionIDs) throws SQLException {
		
		HighLevelQuery<FlowAction> query = new HighLevelQuery<FlowAction>();
		
		query.addParameter(flowActionIDParamFactory.getWhereInParameter(actionIDs));
		
		return callback.getDAOFactory().getFlowActionDAO().getAll(query);
	}
	
	@Override
	protected void addBean(Status bean, HttpServletRequest req, User user, URIParser uriParser) throws Exception {
		
		TransactionHandler transactionHandler = null;
		
		try {
			transactionHandler = callback.getDAOFactory().getTransactionHandler();
			
			crudDAO.add(bean, transactionHandler);
			
			if (bean.getDefaulStatusMappings() != null) {
				
				clearPreviousActionMappings(bean, transactionHandler);
				callback.getDAOFactory().getDefaultStatusMappingDAO().addAll(bean.getDefaulStatusMappings(), transactionHandler, null);
			}
			
			transactionHandler.commit();
			
		} finally {
			
			TransactionHandler.autoClose(transactionHandler);
		}
	}
	
	@Override
	protected void updateBean(Status bean, HttpServletRequest req, User user, URIParser uriParser) throws Exception {
		
		TransactionHandler transactionHandler = null;
		
		try {
			transactionHandler = callback.getDAOFactory().getTransactionHandler();
			
			crudDAO.update(bean, transactionHandler);
			
			clearStatusActionMappings(bean, transactionHandler);
			
			if (bean.getDefaulStatusMappings() != null) {
				
				clearPreviousActionMappings(bean, transactionHandler);
				callback.getDAOFactory().getDefaultStatusMappingDAO().addAll(bean.getDefaulStatusMappings(), transactionHandler, null);
			}
			
			transactionHandler.commit();
			
		} finally {
			
			TransactionHandler.autoClose(transactionHandler);
		}
	}
	
	private void clearPreviousActionMappings(Status bean, TransactionHandler transactionHandler) throws SQLException {
		
		ArrayList<String> actionIDs = new ArrayList<String>(bean.getDefaulStatusMappings().size());
		
		for (DefaultStatusMapping defaultStatusMapping : bean.getDefaulStatusMappings()) {
			
			actionIDs.add(defaultStatusMapping.getActionID());
		}
		
		HighLevelQuery<DefaultStatusMapping> query = new HighLevelQuery<DefaultStatusMapping>();
		
		query.addParameter(defaultStatusMappingFlowParamFactory.getParameter(bean.getFlow()));
		query.addParameter(defaultStatusMappingActionIDParamFactory.getWhereInParameter(actionIDs));
		
		callback.getDAOFactory().getDefaultStatusMappingDAO().delete(query, transactionHandler);
	}
	
	private void clearStatusActionMappings(Status bean, TransactionHandler transactionHandler) throws SQLException {
		
		HighLevelQuery<DefaultStatusMapping> query = new HighLevelQuery<DefaultStatusMapping>();
		
		query.addParameter(defaultStatusMappingStatusParamFactory.getParameter(bean));
		
		callback.getDAOFactory().getDefaultStatusMappingDAO().delete(query, transactionHandler);
	}
	
	@Override
	public ForegroundModuleResponse list(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser, List<ValidationError> validationErrors) throws Exception {
		
		return callback.list(req, res, user, uriParser, validationErrors);
	}
}
