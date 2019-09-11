package com.nordicpeak.flowengine.cruds;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import se.unlogic.emailutils.framework.EmailUtils;
import se.unlogic.hierarchy.core.beans.Group;
import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.hierarchy.core.enums.CRUDAction;
import se.unlogic.hierarchy.core.enums.EventTarget;
import se.unlogic.hierarchy.core.events.CRUDEvent;
import se.unlogic.hierarchy.core.exceptions.AccessDeniedException;
import se.unlogic.hierarchy.core.exceptions.URINotFoundException;
import se.unlogic.hierarchy.core.interfaces.ForegroundModuleResponse;
import se.unlogic.hierarchy.core.utils.AccessUtils;
import se.unlogic.hierarchy.core.utils.AdvancedIntegerBasedCRUD;
import se.unlogic.standardutils.collections.CollectionUtils;
import se.unlogic.standardutils.dao.CRUDDAO;
import se.unlogic.standardutils.dao.querys.ArrayListQuery;
import se.unlogic.standardutils.numbers.NumberUtils;
import se.unlogic.standardutils.populators.DatePopulator;
import se.unlogic.standardutils.populators.IntegerPopulator;
import se.unlogic.standardutils.serialization.SerializationUtils;
import se.unlogic.standardutils.string.StringUtils;
import se.unlogic.standardutils.validation.ValidationError;
import se.unlogic.standardutils.validation.ValidationErrorType;
import se.unlogic.standardutils.validation.ValidationException;
import se.unlogic.standardutils.xml.XMLUtils;
import se.unlogic.webutils.http.URIParser;
import se.unlogic.webutils.populators.annotated.AnnotatedRequestPopulator;
import se.unlogic.webutils.populators.annotated.RequestMapping;
import se.unlogic.webutils.validation.ValidationUtils;

import com.nordicpeak.flowengine.FlowAdminModule;
import com.nordicpeak.flowengine.beans.Flow;
import com.nordicpeak.flowengine.beans.FlowFamily;
import com.nordicpeak.flowengine.beans.FlowFamilyManager;
import com.nordicpeak.flowengine.beans.FlowFamilyManagerGroup;
import com.nordicpeak.flowengine.utils.FlowFamilyUtils;
import com.nordicpeak.flowengine.validationerrors.ManagerGroupInUseValidationError;
import com.nordicpeak.flowengine.validationerrors.ManagerUserInUseValidationError;

public class FlowFamilyCRUD extends AdvancedIntegerBasedCRUD<FlowFamily, FlowAdminModule> {

	private static AnnotatedRequestPopulator<FlowFamily> FLOW_FAMILY_POULATOR = new AnnotatedRequestPopulator<FlowFamily>(FlowFamily.class);

	static {

		List<RequestMapping> requestMappings = new ArrayList<RequestMapping>(FLOW_FAMILY_POULATOR.getRequestMappings());

		for (RequestMapping requestMapping : requestMappings) {

			if(!requestMapping.getParamName().equals("group") && !requestMapping.getParamName().equals("user")) {

				FLOW_FAMILY_POULATOR.getRequestMappings().remove(requestMapping);
			}
		}
	}

	//@formatter:off
	private static final String ACTIVE_FLOWINSTANCE_MANAGERS_SQL = "SELECT DISTINCT userID FROM flowengine_flow_instance_managers"
			+ " WHERE flowInstanceID IN ("
			+ " SELECT ffi.flowInstanceID FROM flowengine_flow_instances AS ffi"
			+ " LEFT JOIN flowengine_flow_statuses AS ffs ON ffi.statusID = ffs.statusID"
			+ " WHERE ffi.flowID IN (SELECT flowID FROM flowengine_flows WHERE flowFamilyID = ? AND enabled = true) AND ffs.contentType != 'ARCHIVED')";
	
	private static final String ACTIVE_FLOWINSTANCE_MANAGER_GROUPS_SQL = "SELECT DISTINCT groupID FROM flowengine_flow_instance_manager_groups"
			+ " WHERE flowInstanceID IN ("
			+ " SELECT ffi.flowInstanceID FROM flowengine_flow_instances AS ffi"
			+ " LEFT JOIN flowengine_flow_statuses AS ffs ON ffi.statusID = ffs.statusID"
			+ " WHERE ffi.flowID IN (SELECT flowID FROM flowengine_flows WHERE flowFamilyID = ? AND enabled = true) AND ffs.contentType != 'ARCHIVED')";
	//@formatter:on

	public FlowFamilyCRUD(CRUDDAO<FlowFamily, Integer> crudDAO, FlowAdminModule callback) {

		super(FlowFamily.class, crudDAO, FLOW_FAMILY_POULATOR, "FlowFamily", "flowfamily", "", callback);
	}

	@Override
	public ForegroundModuleResponse update(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Exception {

		if (uriParser.size() > 3 && NumberUtils.isInt(uriParser.get(3))) {

			Flow flow = callback.getCachedFlow(NumberUtils.toInt(uriParser.get(3)));

			if (flow != null) {

				req.setAttribute("flow", flow);

				return super.update(req, res, user, uriParser);
			}

		}

		throw new URINotFoundException(uriParser);
	}
	
	@Override
	protected void validateUpdatePopulation(FlowFamily flowFamily, HttpServletRequest req, User user, URIParser uriParser) throws ValidationException, SQLException, Exception {
		
		List<ValidationError> validationErrors = new ArrayList<ValidationError>();
		
		List<Integer> managerIDs = ValidationUtils.validateParameters("manager", req, false, IntegerPopulator.getPopulator(), validationErrors);
		
		if (managerIDs == null) {
			
			flowFamily.setManagerUsers(null);
			
		} else {
			
			List<FlowFamilyManager> managers = new ArrayList<FlowFamilyManager>(managerIDs.size());
			
			for (Integer managerID : managerIDs) {
				
				User managerUser = callback.getUserHandler().getUser(managerID, false, false);
			
				if (managerUser != null) {
					
					boolean restricted = "true".equals(req.getParameter("manager-restricted" + managerID));
					boolean allowUpdatingManagers = restricted && "true".equals(req.getParameter("manager-allowUpdatingManagers" + managerID));
					Date validFromDate = ValidationUtils.validateParameter("manager-validFromDate" + managerID, req, false, DatePopulator.getYearLimitedPopulator(), validationErrors);
					Date validToDate = ValidationUtils.validateParameter("manager-validToDate" + managerID, req, false, DatePopulator.getYearLimitedPopulator(), validationErrors);
					
					FlowFamilyManager manager = new FlowFamilyManager(managerUser);
					manager.setRestricted(restricted);
					manager.setAllowUpdatingManagers(allowUpdatingManagers);
					
					if (validFromDate != null) {
						
						manager.setValidFromDate(new java.sql.Date(validFromDate.getTime()));
					}
					
					if (validToDate != null) {
						
						manager.setValidToDate(new java.sql.Date(validToDate.getTime()));
					}
					
					managers.add(manager);
					
				} else {
					
					validationErrors.add(new ValidationError("manager", ValidationErrorType.InvalidFormat));
				}
			}
			
			flowFamily.setManagerUsers(managers);
		}
		
		List<Integer> managerGroupIDs = ValidationUtils.validateParameters("manager-group", req, false, IntegerPopulator.getPopulator(), validationErrors);
		
		if (managerGroupIDs == null) {
			
			flowFamily.setManagerGroups(null);
			
		} else {
			
			List<FlowFamilyManagerGroup> managerGroups = new ArrayList<>(managerGroupIDs.size());
			
			for (Integer groupID : managerGroupIDs) {

				Group group = callback.getGroupHandler().getGroup(groupID, false);
			
				if (group != null) {
					
					boolean restricted = "true".equals(req.getParameter("manager-group-restricted" + groupID));
					boolean allowUpdatingManagers = restricted && "true".equals(req.getParameter("manager-group-allowUpdatingManagers" + groupID));
					List<String> notificationEmailAddresses = StringUtils.splitOnLineBreak(req.getParameter("manager-group-notificationEmailAddresses" + groupID), true);

					FlowFamilyManagerGroup managerGroup = new FlowFamilyManagerGroup(group);
					managerGroup.setRestricted(restricted);
					managerGroup.setAllowUpdatingManagers(allowUpdatingManagers);

					if (!CollectionUtils.isEmpty(notificationEmailAddresses)) {

						for (String address : notificationEmailAddresses) {
							
							if (!EmailUtils.isValidEmailAddress(address)) {
								
								validationErrors.add(new ValidationError("manager-group-notificationEmailAddresses" + groupID, ValidationErrorType.InvalidFormat));
							}
						}
						
						managerGroup.setNotificationEmailAddresses(notificationEmailAddresses);
					}
					
					managerGroups.add(managerGroup);
					
				} else {
					
					validationErrors.add(new ValidationError("group", ValidationErrorType.InvalidFormat));
				}
			}
			
			flowFamily.setManagerGroups(managerGroups);
		}
		
		checkManagersInUse(flowFamily, managerIDs, managerGroupIDs, validationErrors);
		checkRestrictedManagerGroupsInUse(flowFamily, managerGroupIDs, validationErrors);
		
		if (!FlowFamilyUtils.isAutoManagerRulesValid(flowFamily, callback.getUserHandler())) {
			
			validationErrors.add(new ValidationError("FullManagerOrFallbackManagerRequired"));
		}
		
		if (!validationErrors.isEmpty()) {
			throw new ValidationException(validationErrors);
		}
	}
	
	public void checkManagersInUse(FlowFamily flowFamily, List<Integer> newManagerUserIDs, List<Integer> newManagerGroupIDs, List<ValidationError> validationErrors) throws SQLException, AccessDeniedException {
		
		List<Integer> currentFlowInstanceManagerUserIDs = getCurrentFlowInstanceManagerUserIDs(flowFamily);
		
		if (currentFlowInstanceManagerUserIDs != null) {
			
			FlowFamily unchangedFlowFamily = getBean(flowFamily.getFlowFamilyID());
			
			List<User> currentManagers = callback.getUserHandler().getUsers(currentFlowInstanceManagerUserIDs, true, false);
			
			if (currentManagers != null) {
				
				outer: for (User currentManager : currentManagers) {
					
					// User did not have access before, skip check
					if (unchangedFlowFamily.getManagerAccess(currentManager) == null) {
						continue;
					}
					
					if (newManagerUserIDs != null && newManagerUserIDs.contains(currentManager.getUserID())) {
						continue;
					}
					
					Collection<Group> managerGroups = currentManager.getGroups();
					
					if (newManagerGroupIDs != null && managerGroups != null) {
						
						for (Group group : managerGroups) {
							
							if (newManagerGroupIDs.contains(group.getGroupID())) {
								continue outer;
							}
						}
					}
					
					validationErrors.add(new ManagerUserInUseValidationError(currentManager));
				}
			}
		}
	}
	
	public void checkRestrictedManagerGroupsInUse(FlowFamily flowFamily, List<Integer> newManagerGroupIDs, List<ValidationError> validationErrors) throws SQLException, AccessDeniedException {
		
		List<Integer> currentFlowInstanceManagerGroupIDs = getCurrentFlowInstanceManagerGroupIDs(flowFamily);
		
		if (currentFlowInstanceManagerGroupIDs != null) {
			
			FlowFamily unchangedFlowFamily = getBean(flowFamily.getFlowFamilyID());
			
			List<Group> currentGroups = callback.getGroupHandler().getGroups(currentFlowInstanceManagerGroupIDs, false);
			
			if (currentGroups != null) {
				
				for (Group currentGroup : currentGroups) {
					
					// Group did not have access before, skip check
					if (unchangedFlowFamily.getManagerGroupIDs() == null || !unchangedFlowFamily.getManagerGroupIDs().contains(currentGroup.getGroupID())) {
						continue;
					}
					
					if (newManagerGroupIDs != null && newManagerGroupIDs.contains(currentGroup.getGroupID())) {
						continue;
					}
					
					validationErrors.add(new ManagerGroupInUseValidationError(currentGroup));
				}
			}
		}
	}

	@Override
	public FlowFamily getBean(Integer beanID) throws SQLException, AccessDeniedException {
		
		FlowFamily flowFamily = callback.getFlowFamily(beanID);
		
		if (flowFamily != null) {
			
			flowFamily = SerializationUtils.cloneSerializable(flowFamily);
		}
		
		return flowFamily;
	}

	@Override
	protected void appendUpdateFormData(FlowFamily flowFamily, Document doc, Element updateTypeElement, User user, HttpServletRequest req, URIParser uriParser) throws Exception {

		XMLUtils.append(doc, updateTypeElement, (Flow) req.getAttribute("flow"));
		
		flowFamily.setManagerUsersAndGroups(callback.getUserHandler(), callback.getGroupHandler());
		XMLUtils.append(doc, updateTypeElement, "ManagerUsers", flowFamily.getManagers());
		XMLUtils.append(doc, updateTypeElement, "ManagerGroups", flowFamily.getManagerGroups());
		
		if (callback.isShowManagerModalOnAdd()) {
			XMLUtils.appendNewElement(doc, updateTypeElement, "ShowManagerModalOnAdd");
		}
	}
	
	@Override
	protected void checkUpdateAccess(FlowFamily bean, User user, HttpServletRequest req, URIParser uriParser) throws AccessDeniedException, URINotFoundException, SQLException {
		
		Flow flow = (Flow) req.getAttribute("flow");
		
		if (!flow.getFlowFamily().equals(bean)) {
			
			throw new AccessDeniedException("Flow " + flow + " does not belong flow family " + bean);
		}
		
		checkAccess(user, flow);
	}

	@Override
	protected ForegroundModuleResponse beanUpdated(FlowFamily flowFamily, HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Exception {

		callback.getEventHandler().sendEvent(FlowFamily.class, new CRUDEvent<FlowFamily>(CRUDAction.UPDATE, flowFamily), EventTarget.ALL);
		
		callback.addFlowFamilyEvent(callback.getEventFlowFamilyUpdatedMessage(), flowFamily, user);

		Flow flow = (Flow) req.getAttribute("flow");

		callback.redirectToMethod(req, res, "/showflow/" + flow.getFlowID() + "#managers");

		return null;
	}

	private List<Integer> getCurrentFlowInstanceManagerUserIDs(FlowFamily flowFamily) throws SQLException {

		ArrayListQuery<Integer> query = new ArrayListQuery<Integer>(callback.getDataSource(), ACTIVE_FLOWINSTANCE_MANAGERS_SQL, IntegerPopulator.getPopulator());

		query.setInt(1, flowFamily.getFlowFamilyID());

		return query.executeQuery();
	}
	
	private List<Integer> getCurrentFlowInstanceManagerGroupIDs(FlowFamily flowFamily) throws SQLException {

		ArrayListQuery<Integer> query = new ArrayListQuery<Integer>(callback.getDataSource(), ACTIVE_FLOWINSTANCE_MANAGER_GROUPS_SQL, IntegerPopulator.getPopulator());

		query.setInt(1, flowFamily.getFlowFamilyID());

		return query.executeQuery();
	}

	private void checkAccess(User user, Flow bean) throws AccessDeniedException {

		if (!AccessUtils.checkAccess(user, bean.getFlowType().getAdminAccessInterface()) && !AccessUtils.checkAccess(user, callback)) {

			throw new AccessDeniedException("User does not have access to flow type " + bean.getFlowType());
		}
	}

}
