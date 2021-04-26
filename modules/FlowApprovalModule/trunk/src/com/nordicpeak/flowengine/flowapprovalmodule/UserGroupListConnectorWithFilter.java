package com.nordicpeak.flowengine.flowapprovalmodule;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import se.unlogic.hierarchy.core.beans.Group;
import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.hierarchy.core.interfaces.ForegroundModuleResponse;
import se.unlogic.hierarchy.core.interfaces.SystemInterface;
import se.unlogic.hierarchy.core.utils.UserUtils;
import se.unlogic.hierarchy.core.utils.usergrouplist.UserGroupListConnector;
import se.unlogic.standardutils.collections.CollectionUtils;
import se.unlogic.standardutils.json.JsonObject;
import se.unlogic.webutils.http.URIParser;


public class UserGroupListConnectorWithFilter extends UserGroupListConnector {

	private final String titleAttribute;
	
	public UserGroupListConnectorWithFilter(SystemInterface systemInterface, String titleAttribute) {
		super(systemInterface);
		
		this.titleAttribute = titleAttribute;
	}

	public ForegroundModuleResponse getUsers(List<User> assignableUsers, List<Group> assignableGroups, HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Exception {
		
		String query = parseQuery(req, res);
		
		if (query == null) {
			
			sendEmptyJSONResponse(res);
			return null;
		}
		
		List<User> users = null;
		
		if (!CollectionUtils.isEmpty(assignableGroups)) {
			
			List<Integer> groupIDs = UserUtils.getGroupIDs(assignableGroups);
			users = CollectionUtils.addAndInstantiateIfNeeded(users, userHandler.searchUsers(query, false, true, getSearchResultLimit(), groupIDs, isOnlyEnabledUsers(), isRequireUsername()));
		}
		
		if (assignableUsers != null) {
			
			String terms[] = query.toLowerCase().split("[ ]+");
		
			List<User> matchingUsers = new ArrayList<User>(assignableUsers.size());
			
			for (User potentialUser : assignableUsers) {
				
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
				
				if (potentialUser.getEmail() != null) {
					fields.add(potentialUser.getEmail().toLowerCase());
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
			
			if (!matchingUsers.isEmpty()) {
				
				if (users == null) {
					users = matchingUsers;
					
				} else {
					@SuppressWarnings("unchecked")
					HashSet<User> usersSet = CollectionUtils.combineAsSet(users, matchingUsers);
					users = new ArrayList<>(usersSet);
				}
			}
		}
		
		log.info("User " + user + " searching for users using query " + query + ", found " + CollectionUtils.getSize(users) + " hits");
		
		if (users == null) {
			
			sendEmptyJSONResponse(res);
			return null;
		}
		
		sendJSONResponse(getUsersJsonArray(users), res);
		return null;
	}
	
	@Override
	protected JsonObject getUserJson(User user) {
		
		JsonObject instance = super.getUserJson(user);
		
		String username = null;
		
		if (user.getUsername() != null) {
			username = user.getUsername();
		}
		
		if (user.getAttributeHandler() != null && titleAttribute != null) {
			
			String title = user.getAttributeHandler().getString(titleAttribute);
			
			if (title != null) {
				
				if (username != null) {
					username = username + ", " + title;
					
				} else {
					username = title;
				}
			}
		}
		
		if (username != null) {
			instance.putField("Username", username);
		}
		
		return instance;
	}
}
