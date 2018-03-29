package com.nordicpeak.flowengine.cruds;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

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
import se.unlogic.hierarchy.core.utils.UserUtils;
import se.unlogic.standardutils.dao.CRUDDAO;
import se.unlogic.standardutils.dao.querys.ArrayListQuery;
import se.unlogic.standardutils.numbers.NumberUtils;
import se.unlogic.standardutils.populators.DatePopulator;
import se.unlogic.standardutils.populators.IntegerPopulator;
import se.unlogic.standardutils.serialization.SerializationUtils;
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
import com.nordicpeak.flowengine.comparators.FlowFamilyManagerComparator;
import com.nordicpeak.flowengine.validationerrors.UnauthorizedManagerUserValidationError;

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

	private static final String ACTIVE_FLOWINSTANCE_MANAGERS_SQL = "SELECT DISTINCT userID FROM flowengine_flow_instance_managers WHERE flowInstanceID IN(" +
			"SELECT ffi.flowInstanceID FROM flowengine_flow_instances AS ffi LEFT JOIN flowengine_flow_statuses AS ffs ON ffi.statusID = ffs.statusID WHERE ffi.flowID IN(" +
			"SELECT flowID FROM flowengine_flows WHERE flowFamilyID = ? AND enabled = true) AND ffs.contentType != 'ARCHIVED')";

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
					
					Date validFromDate = ValidationUtils.validateParameter("manager-validFromDate" + managerID, req, false, DatePopulator.getYearLimitedPopulator(), validationErrors);
					Date validToDate = ValidationUtils.validateParameter("manager-validToDate" + managerID, req, false, DatePopulator.getYearLimitedPopulator(), validationErrors);
					
					FlowFamilyManager manager = new FlowFamilyManager(managerUser);

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
		
		List<Integer> currentFlowInstanceManagerUserIDs = getCurrentFlowInstanceManagerUserIDs(flowFamily);
		
		if (currentFlowInstanceManagerUserIDs != null) {
			
			FlowFamily unchangedFlowFamily = getBean(flowFamily.getFlowFamilyID());
			
			List<User> currentManagers = callback.getUserHandler().getUsers(currentFlowInstanceManagerUserIDs, true, false);
			
			if (currentManagers != null) {
				
				List<Integer> allowedUserIDs = managerIDs;
				List<Integer> allowedGroupIDs = flowFamily.getAllowedGroupIDs();
				
				outer: for (User currentManager : currentManagers) {
					
					//User did not have access before, skip check
					if (!AccessUtils.checkAccess(currentManager, unchangedFlowFamily)) {
						continue;
					}
					
					if (allowedUserIDs != null && allowedUserIDs.contains(currentManager.getUserID())) {
						continue;
					}
					
					Collection<Group> managerGroups = currentManager.getGroups();
					
					if (allowedGroupIDs != null && managerGroups != null) {
						
						for (Group group : managerGroups) {
							
							if (allowedGroupIDs.contains(group.getGroupID())) {
								continue outer;
							}
						}
					}
					
					validationErrors.add(new UnauthorizedManagerUserValidationError(currentManager));
				}
				
			}
		}
		
		if (!validationErrors.isEmpty()) {
			throw new ValidationException(validationErrors);
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

		//		This code is kept in case client side validation is to be implemented again
		//
		//		List<Integer> flowInstanceManagerUserIDs = getCurrentFlowInstanceManagerUserIDs(bean);
		//
		//		if (flowInstanceManagerUserIDs != null) {
		//
		//			XMLUtils.append(doc, updateTypeElement, "FlowInstanceManagerUsers", callback.getUserHandler().getUsers(flowInstanceManagerUserIDs, true, false));
		//
		//		}

		XMLUtils.append(doc, updateTypeElement, (Flow) req.getAttribute("flow"));
		
		if (flowFamily.getManagerGroupIDs() != null) {
			
			XMLUtils.append(doc, updateTypeElement, "ManagerGroups", callback.getGroupHandler().getGroups(flowFamily.getManagerGroupIDs(), false));
		}
		
		if (flowFamily.getAllowedUserIDs() != null) {

			List<Integer> userIDs = new ArrayList<Integer>();
			
			for (FlowFamilyManager manager : flowFamily.getManagerUsers()) {
				
				userIDs.add(manager.getUserID());
			}
			
			Map<Integer, User> userMap = UserUtils.getUserIDMap(callback.getUserHandler().getUsers(userIDs, false, true));
			
			for (FlowFamilyManager manager : flowFamily.getManagerUsers()) {
				
				manager.setUser(userMap.get(manager.getUserID()));
			}
			
			Collections.sort(flowFamily.getManagerUsers(), FlowFamilyManagerComparator.getComparator());
			XMLUtils.append(doc, updateTypeElement, "ManagerUsers", flowFamily.getManagerUsers());
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

	private void checkAccess(User user, Flow bean) throws AccessDeniedException {

		if (!AccessUtils.checkAccess(user, bean.getFlowType().getAdminAccessInterface()) && !AccessUtils.checkAccess(user, callback)) {

			throw new AccessDeniedException("User does not have access to flow type " + bean.getFlowType());
		}
	}

}
