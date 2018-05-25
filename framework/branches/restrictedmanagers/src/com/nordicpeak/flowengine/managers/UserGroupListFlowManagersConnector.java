package com.nordicpeak.flowengine.managers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.nordicpeak.flowengine.FlowAdminModule;
import com.nordicpeak.flowengine.FlowInstanceAdminModule;
import com.nordicpeak.flowengine.interfaces.ImmutableFlow;
import com.nordicpeak.flowengine.interfaces.ImmutableFlowFamily;

import se.unlogic.hierarchy.core.beans.Group;
import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.hierarchy.core.handlers.GroupHandler;
import se.unlogic.hierarchy.core.handlers.UserHandler;
import se.unlogic.hierarchy.core.interfaces.ForegroundModuleResponse;
import se.unlogic.hierarchy.core.interfaces.SystemInterface;
import se.unlogic.hierarchy.core.utils.usergrouplist.UserGroupListConnector;
import se.unlogic.standardutils.collections.CollectionUtils;
import se.unlogic.webutils.http.URIParser;

public class UserGroupListFlowManagersConnector extends UserGroupListConnector {
	
	
	protected final FlowAdminModule flowAdminModule;
	
	public UserGroupListFlowManagersConnector(SystemInterface systemInterface, FlowAdminModule flowAdminModule) {
		super(systemInterface);
		
		this.flowAdminModule = flowAdminModule;
	}
	
	public UserGroupListFlowManagersConnector(UserHandler userHandler, GroupHandler groupHandler, String encoding, FlowAdminModule flowAdminModule) {
		super(userHandler, groupHandler, encoding);
		
		this.flowAdminModule = flowAdminModule;
	}
	
	@Override
	public ForegroundModuleResponse getUsers(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Exception {
		
		Integer flowID;
		ImmutableFlow flow = null;
		
		if (uriParser.size() == 3 && (flowID = uriParser.getInt(2)) != null && (flow = flowAdminModule.getFlow(flowID)) != null) {
			
			ImmutableFlowFamily flowFamily = flow.getFlowFamily();
			
			List<User> managingUsers = getAllowedFlowManagers(flowFamily);
			
			if (CollectionUtils.isEmpty(managingUsers)) {
				
				sendEmptyJSONResponse(res);
				return null;
			}
			
			String query = parseQuery(req, res);
			
			if (query == null) {
				
				log.info("User " + user + " searching for manager users of flow " + flow + " using empty query, returning all " + managingUsers.size() + " managers");
				
				sendJSONResponse(getUsersJsonArray(managingUsers), res);
				return null;
			}
			
			List<User> matchingUsers = null;
			
			String terms[] = query.toLowerCase().split("[ ]+");
			matchingUsers = new ArrayList<User>(managingUsers.size());
			
			for (User potentialUser : managingUsers) {
				
				List<String> fields = new ArrayList<String>();
				
				if (potentialUser.getFirstname() != null) {
					fields.add(potentialUser.getFirstname().toLowerCase());
				}
				
				if (potentialUser.getLastname() != null) {
					fields.add(potentialUser.getLastname().toLowerCase());
				}
				
				if (potentialUser.getUsername() != null) {
					fields.add(potentialUser.getUsername().toLowerCase());
				}
				
				int matches = 0;
				
				for (String term : terms) {
					for (String field : fields) {
						
						if (field.contains(term)) {
							
							matches++;
							break;
						}
					}
				}
				
				if (matches == terms.length) {
					matchingUsers.add(potentialUser);
				}
			}
			
			log.info("User " + user + " searching for manager users of flow " + flow + " using query " + query + ", found " + CollectionUtils.getSize(matchingUsers) + " hits");
			
			if (CollectionUtils.isEmpty(matchingUsers)) {
				
				sendEmptyJSONResponse(res);
				return null;
			}
			
			sendJSONResponse(getUsersJsonArray(matchingUsers), res);
			return null;
			
		} else {
			
			sendEmptyJSONResponse(res);
			return null;
		}
	}
	
	@Override
	public ForegroundModuleResponse getGroups(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Exception {
		
		Integer flowID;
		ImmutableFlow flow = null;
		
		if (uriParser.size() == 3 && (flowID = uriParser.getInt(2)) != null && (flow = flowAdminModule.getFlow(flowID)) != null) {
			
			ImmutableFlowFamily flowFamily = flow.getFlowFamily();
			
			List<Group> managingGroups = FlowInstanceAdminModule.getAllowedManagerGroups(flowFamily, groupHandler);
			
			if (CollectionUtils.isEmpty(managingGroups)) {
				
				sendEmptyJSONResponse(res);
				return null;
			}
			
			String query = parseQuery(req, res);
			
			if (query == null) {
				
				log.info("User " + user + " searching for manager groups of flow " + flow + " using empty query, returning all " + managingGroups.size() + " groups");
				
				sendJSONResponse(getGroupsJsonArray(managingGroups), res);
				return null;
			}
			
			List<Group> matchingGroups = null;
			
			String terms[] = query.toLowerCase().split("[ ]+");
			matchingGroups = new ArrayList<Group>(managingGroups.size());
			
			for (Group potentialGroup : managingGroups) {
				
				List<String> fields = new ArrayList<String>();
				
				if (potentialGroup.getName() != null) {
					fields.add(potentialGroup.getName().toLowerCase());
				}
				
				int matches = 0;
				
				for (String term : terms) {
					for (String field : fields) {
						
						if (field.contains(term)) {
							
							matches++;
							break;
						}
					}
				}
				
				if (matches == terms.length) {
					matchingGroups.add(potentialGroup);
				}
			}
			
			log.info("User " + user + " searching for manager groups of flow " + flow + " using query " + query + ", found " + CollectionUtils.getSize(matchingGroups) + " hits");
			
			if (CollectionUtils.isEmpty(matchingGroups)) {
				
				sendEmptyJSONResponse(res);
				return null;
			}
			
			sendJSONResponse(getGroupsJsonArray(matchingGroups), res);
			return null;
			
		} else {
			
			sendEmptyJSONResponse(res);
			return null;
		}
	}
	
	protected List<User> getAllowedFlowManagers(ImmutableFlowFamily flowFamily) {
		
		List<User> managingUsers = new ArrayList<User>();
		
		Collection<Integer> managerIDs = flowFamily.getManagerUserIDs();
		List<Integer> managerGroupIDs = flowFamily.getManagerGroupIDs();
		
		if (!CollectionUtils.isEmpty(managerIDs)) {
			
			List<User> users = userHandler.getUsers(managerIDs, false, true);
			
			if (users != null) {
				managingUsers.addAll(users);
			}
		}
		
		if (!CollectionUtils.isEmpty(managerGroupIDs)) {
			
			List<User> users = userHandler.getUsersByGroups(managerGroupIDs, true);
			
			if (users != null) {
				managingUsers.addAll(users);
			}
		}
		
		return managingUsers;
	}
}
