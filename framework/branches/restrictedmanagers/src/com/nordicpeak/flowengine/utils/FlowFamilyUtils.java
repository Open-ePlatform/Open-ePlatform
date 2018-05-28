package com.nordicpeak.flowengine.utils;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import com.nordicpeak.flowengine.FlowInstanceAdminModule;
import com.nordicpeak.flowengine.beans.FlowFamily;
import com.nordicpeak.flowengine.beans.FlowFamilyManager;
import com.nordicpeak.flowengine.beans.FlowFamilyManagerGroup;
import com.nordicpeak.flowengine.comparators.GroupNameComparator;
import com.nordicpeak.flowengine.interfaces.ImmutableFlowFamily;

import se.unlogic.hierarchy.core.beans.Group;
import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.hierarchy.core.handlers.GroupHandler;
import se.unlogic.hierarchy.core.handlers.UserHandler;
import se.unlogic.hierarchy.foregroundmodules.usersessionadmin.UserNameComparator;
import se.unlogic.standardutils.collections.CollectionUtils;
import se.unlogic.standardutils.date.DateUtils;
import se.unlogic.standardutils.time.TimeUtils;
import se.unlogic.standardutils.validation.ValidationError;

public class FlowFamilyUtils {
	
	public static List<User> getAllowedManagers(ImmutableFlowFamily flowFamily, UserHandler userHandler) {
		
		HashSet<User> availableManagers = new HashSet<User>();
		
		if (flowFamily != null) {
			
			if (!CollectionUtils.isEmpty(flowFamily.getManagerUserIDs())) {
				
				List<User> users = userHandler.getUsers(flowFamily.getManagerUserIDs(), false, true);
				
				if (users != null) {
					
					availableManagers.addAll(users);
				}
			}
			
			if (!CollectionUtils.isEmpty(flowFamily.getManagerGroupIDs())) {
				
				List<User> users = userHandler.getUsersByGroups(flowFamily.getManagerGroupIDs(), true);
				
				if (users != null) {
					
					availableManagers.addAll(users);
				}
			}
		}
		
		if (!availableManagers.isEmpty()) {
			
			List<User> availableManagerList = new ArrayList<User>(availableManagers);
			Collections.sort(availableManagerList, UserNameComparator.getInstance());
			
			return availableManagerList;
		}
		
		return null;
	}
	
	public static List<Group> getAllowedManagerGroups(ImmutableFlowFamily flowFamily, GroupHandler groupHandler) {
		
		if (flowFamily != null && !CollectionUtils.isEmpty(flowFamily.getManagerGroups())) {
			
			List<Integer> groupIDs = new ArrayList<Integer>(flowFamily.getManagerGroups().size());
			
			for (FlowFamilyManagerGroup managerGroup : flowFamily.getManagerGroups()) {
				
				if (managerGroup.isRestricted()) {
					
					groupIDs.add(managerGroup.getGroupID());
				}
			}
			
			if (!groupIDs.isEmpty()) {
				
				List<Group> groups = groupHandler.getGroups(groupIDs, false);
				
				if (groups != null) {
					
					Collections.sort(groups, GroupNameComparator.getInstance());
					return groups;
				}
			}
		}
		
		return null;
	}
	
	public static List<User> filterSelectedManagers(List<User> allowedManagers, List<Integer> selectedUserIDs, List<ValidationError> validationErrors) {
		
		if (!CollectionUtils.isEmpty(selectedUserIDs)) {
			
			List<User> selectedManagers = new ArrayList<User>();
			
			if (allowedManagers != null) {
				for (User manager : allowedManagers) {
					for (Integer userID : selectedUserIDs) {
						
						if (manager.getUserID().equals(userID)) {
							
							selectedManagers.add(manager);
							break;
						}
					}
				}
			}
			
			if (validationErrors != null && CollectionUtils.getSize(selectedManagers) < selectedUserIDs.size()) {
				
				validationErrors.add(FlowInstanceAdminModule.ONE_OR_MORE_SELECTED_MANAGER_USERS_NOT_FOUND_VALIDATION_ERROR);
			}
			
			return selectedManagers;
			
		} else {
			
			return null;
		}
	}
	
	public static List<Group> filterSelectedManagerGroups(List<Group> allowedManagerGroups, List<Integer> selectedGroupIDs, List<ValidationError> validationErrors) {
		
		if (!CollectionUtils.isEmpty(selectedGroupIDs)) {
			
			List<Group> selectedManagerGroups = new ArrayList<Group>();
			
			if (allowedManagerGroups != null) {
				for (Group managerGroup : allowedManagerGroups) {
					for (Integer groupID : selectedGroupIDs) {
						
						if (managerGroup.getGroupID().equals(groupID)) {
							
							selectedManagerGroups.add(managerGroup);
							break;
						}
					}
				}
			}
			
			if (validationErrors != null && CollectionUtils.getSize(selectedManagerGroups) < selectedGroupIDs.size()) {
				
				validationErrors.add(FlowInstanceAdminModule.ONE_OR_MORE_SELECTED_MANAGER_GROUPS_NOT_FOUND_VALIDATION_ERROR);
			}
			
			return selectedManagerGroups;
			
		} else {
			
			return null;
		}
	}
	
	public static void validateSelectedManagers(List<User> allowedUsers, List<User> selectedUsers, List<Integer> userIDs, List<ValidationError> validationErrors) {
		
		if (!CollectionUtils.isEmpty(userIDs)) {
			
			if (selectedUsers == null || userIDs.size() != selectedUsers.size()) {
				
				validationErrors.add(FlowInstanceAdminModule.ONE_OR_MORE_SELECTED_MANAGER_USERS_NOT_FOUND_VALIDATION_ERROR);
			}
		}
		
		if (!CollectionUtils.isEmpty(selectedUsers)) {
			
			for (User user : selectedUsers) {
				
				boolean found = false;
				
				if (allowedUsers != null) {
					for (User allowedUser : allowedUsers) {
						
						if (allowedUser.equals(user)) {
							
							found = true;
							break;
						}
					}
				}
				
				if (!found) {
					validationErrors.add(new ValidationError("SelectedManagerUserNotFoundError", user.getFirstname() + " " + user.getLastname()));
				}
			}
		}
	}
	
	public static void validateSelectedManagerGroups(List<Group> allowedManagerGroups, List<Group> selectedGroups, List<Integer> groupIDs, List<ValidationError> validationErrors) {
		
		if (!CollectionUtils.isEmpty(groupIDs)) {
			
			if (selectedGroups == null || groupIDs.size() != selectedGroups.size()) {
				
				validationErrors.add(FlowInstanceAdminModule.ONE_OR_MORE_SELECTED_MANAGER_GROUPS_NOT_FOUND_VALIDATION_ERROR);
			}
		}
		
		if (!CollectionUtils.isEmpty(selectedGroups)) {
			
			for (Group group : selectedGroups) {
				
				boolean found = false;
				
				if (allowedManagerGroups != null) {
					for (Group allowedGroup : allowedManagerGroups) {
						
						if (allowedGroup.equals(group)) {
							
							found = true;
							break;
						}
					}
				}
				
				if (!found) {
					validationErrors.add(new ValidationError("SelectedManagerGroupNotFoundError", group.getName()));
				}
			}
		}
	}
	
	public static List<User> getActiveFullManagerUsers(FlowFamily flowFamily, UserHandler userHandler, GroupHandler groupHandler) {
		
		flowFamily.setManagerUsersAndGroups(userHandler, groupHandler);
		
		List<User> users = null;
		
		if (!CollectionUtils.isEmpty(flowFamily.getManagers())) {
			
			Timestamp startOfToday = DateUtils.setTimeToMidnight(TimeUtils.getCurrentTimestamp());
			
			for (FlowFamilyManager manager : flowFamily.getManagers()) {
				
				if (manager.isRestricted() || manager.getValidFromDate() != null && startOfToday.compareTo(manager.getValidFromDate()) < 0) {
					continue;
				}
				
				users = CollectionUtils.addAndInstantiateIfNeeded(users, manager.getUser());
			}
		}
		
		if (!CollectionUtils.isEmpty(flowFamily.getManagerGroups())) {
			
			for (FlowFamilyManagerGroup managerGroup : flowFamily.getManagerGroups()) {
				
				if (!managerGroup.isRestricted()) {
					
					users = CollectionUtils.addAndInstantiateIfNeeded(users, userHandler.getUsersByGroup(managerGroup.getGroupID(), false, true));
				}
			}
		}
		
		return users;
	}
	
	@SuppressWarnings("unchecked")
	public static boolean isAutoManagerRulesValid(FlowFamily flowFamily, UserHandler userHandler, GroupHandler groupHandler) {
		
		if (!CollectionUtils.isEmpty(flowFamily.getAutoManagerAssignmentRules()) && getActiveFullManagerUsers(flowFamily, userHandler, groupHandler) == null) {
			
			List<User> users = null;
			
			List<Integer> userIDs = CollectionUtils.combine(flowFamily.getAutoManagerAssignmentAlwaysUserIDs(), flowFamily.getAutoManagerAssignmentNoMatchUserIDs());
			List<Integer> groupIDs = CollectionUtils.combine(flowFamily.getAutoManagerAssignmentAlwaysGroupIDs(), flowFamily.getAutoManagerAssignmentNoMatchGroupIDs());
			
			if (userIDs != null) {
				users = userHandler.getUsers(userIDs, false, false);
			}
			
			if (groupIDs != null) {
				users = CollectionUtils.addAndInstantiateIfNeeded(users, userHandler.getUsersByGroups(groupIDs, false));
			}
			
			if (CollectionUtils.isEmpty(users)) {
				return false;
			}
		}
		
		return true;
	}
}
