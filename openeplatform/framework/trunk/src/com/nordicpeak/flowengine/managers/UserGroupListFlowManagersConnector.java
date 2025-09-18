package com.nordicpeak.flowengine.managers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import se.unlogic.hierarchy.core.beans.Group;
import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.hierarchy.core.handlers.GroupHandler;
import se.unlogic.hierarchy.core.handlers.UserHandler;
import se.unlogic.hierarchy.core.interfaces.ForegroundModuleResponse;
import se.unlogic.hierarchy.core.interfaces.SystemInterface;
import se.unlogic.hierarchy.core.utils.usergrouplist.UserGroupListConnector;
import se.unlogic.standardutils.collections.CollectionUtils;
import se.unlogic.webutils.http.URIParser;

import com.nordicpeak.flowengine.FlowAdminModule;
import com.nordicpeak.flowengine.interfaces.ImmutableFlow;
import com.nordicpeak.flowengine.interfaces.ImmutableFlowFamily;
import com.nordicpeak.flowengine.utils.FlowFamilyUtils;

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
		
		if (uriParser.size() >= 3 && (flowID = uriParser.getInt(2)) != null && (flow = flowAdminModule.getFlow(flowID)) != null) {
			
			ImmutableFlowFamily flowFamily = flow.getFlowFamily();
			
			List<User> managingUsers = FlowFamilyUtils.getAllowedManagerUsers(flowFamily, userHandler);
			
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
			
			String terms[] = query.toLowerCase().split("[ ]+");
			List<User> matchingUsers = new ArrayList<User>(managingUsers.size());
			
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
			
			boolean checkHideGroupInAssignment = (Boolean) Optional.ofNullable(req.getAttribute("checkHideGroupInAssignment")).orElse(false);
			req.removeAttribute("checkHideGroupInAssignment");
			
			List<Group> managingGroups = FlowFamilyUtils.getAllowedManagerGroups(flowFamily, groupHandler, checkHideGroupInAssignment);
			
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
	
}
