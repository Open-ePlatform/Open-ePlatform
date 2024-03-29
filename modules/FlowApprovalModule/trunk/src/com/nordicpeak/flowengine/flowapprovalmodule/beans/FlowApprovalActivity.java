package com.nordicpeak.flowengine.flowapprovalmodule.beans;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import se.unlogic.emailutils.populators.EmailPopulator;
import se.unlogic.emailutils.populators.LowerCaseEmailPopulator;
import se.unlogic.hierarchy.core.beans.Group;
import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.hierarchy.foregroundmodules.groupproviders.SimpleGroup;
import se.unlogic.hierarchy.foregroundmodules.userproviders.SimpleUser;
import se.unlogic.standardutils.annotations.NoDuplicates;
import se.unlogic.standardutils.annotations.RequiredIfSet;
import se.unlogic.standardutils.annotations.SplitOnLineBreak;
import se.unlogic.standardutils.annotations.WebPopulate;
import se.unlogic.standardutils.collections.CollectionUtils;
import se.unlogic.standardutils.dao.annotations.DAOManaged;
import se.unlogic.standardutils.dao.annotations.Key;
import se.unlogic.standardutils.dao.annotations.ManyToOne;
import se.unlogic.standardutils.dao.annotations.OneToMany;
import se.unlogic.standardutils.dao.annotations.OrderBy;
import se.unlogic.standardutils.dao.annotations.SimplifiedRelation;
import se.unlogic.standardutils.dao.annotations.Table;
import se.unlogic.standardutils.populators.EnumPopulator;
import se.unlogic.standardutils.populators.StringPopulator;
import se.unlogic.standardutils.reflection.ReflectionUtils;
import se.unlogic.standardutils.string.StringUtils;
import se.unlogic.standardutils.validation.ValidationError;
import se.unlogic.standardutils.validation.ValidationException;
import se.unlogic.standardutils.xml.GeneratedElementable;
import se.unlogic.standardutils.xml.XMLElement;
import se.unlogic.standardutils.xml.XMLParser;
import se.unlogic.standardutils.xml.XMLParserPopulateable;
import se.unlogic.standardutils.xml.XMLValidationUtils;

import com.nordicpeak.flowengine.flowapprovalmodule.enums.Comment;

@Table(name = "flowapproval_activities")
@XMLElement(name = "Activity")
public class FlowApprovalActivity extends GeneratedElementable implements XMLParserPopulateable {

	public static final Field ACTIVITY_GROUP_RELATION = ReflectionUtils.getField(FlowApprovalActivity.class, "activityGroup");
	public static final Field ACTIVITY_PROGRESSES_RELATION = ReflectionUtils.getField(FlowApprovalActivity.class, "activityProgresses");
	public static final Field RESPONSIBLE_USERS_RELATION = ReflectionUtils.getField(FlowApprovalActivity.class, "responsibleUsers");
	public static final Field RESPONSIBLE_GROUPS_RELATION = ReflectionUtils.getField(FlowApprovalActivity.class, "responsibleGroups");
	public static final Field ASSIGNABLE_USERS_RELATION = ReflectionUtils.getField(FlowApprovalActivity.class, "assignableUsers");
	public static final Field ASSIGNABLE_GROUPS_RELATION = ReflectionUtils.getField(FlowApprovalActivity.class, "assignableGroups");
	public static final Field RESPONSIBLE_FALLBACK_RELATION = ReflectionUtils.getField(FlowApprovalActivity.class, "responsibleFallbackUsers");
	
	@Key
	@DAOManaged(autoGenerated = true)
	@XMLElement
	private Integer activityID;

	@DAOManaged(columnName = "activityGroupID")
	@ManyToOne
	@XMLElement
	private FlowApprovalActivityGroup activityGroup;

	@DAOManaged
	@OrderBy
	@WebPopulate(maxLength = 255, required = true)
	@XMLElement
	private String name;

	@DAOManaged
	@WebPopulate(maxLength = 255)
	@XMLElement
	private String shortDescription;

	@DAOManaged
	@WebPopulate(maxLength = 65535)
	@XMLElement
	private String description;

	@DAOManaged
	@WebPopulate
	@XMLElement
	private boolean showFlowInstance;

	@DAOManaged
	@WebPopulate
	@XMLElement
	private boolean pdfDownloadActivation;

	@DAOManaged
	@WebPopulate
	@XMLElement
	private boolean requireSigning;

	@DAOManaged
	@WebPopulate
	@XMLElement
	private boolean requireComment;
	
	@DAOManaged
	@WebPopulate
	@RequiredIfSet(paramNames = "requireComment")
	@XMLElement
	private Comment whenToComment;
	
	@DAOManaged
	@WebPopulate(maxLength = 255)
	@XMLElement
	private String whenToCommentErrorMessage;

	@DAOManaged
	@OneToMany
	@SimplifiedRelation(table = "flowapproval_activity_users", remoteValueColumnName = "userID")
	@XMLElement(fixCase = true)
	private List<User> responsibleUsers;

	@DAOManaged
	@OneToMany
	@SimplifiedRelation(table = "flowapproval_activity_groups", remoteValueColumnName = "groupID")
	@XMLElement(fixCase = true)
	private List<Group> responsibleGroups;

	@DAOManaged
	@NoDuplicates
	@SplitOnLineBreak
	@WebPopulate(maxLength = 255)
	@OneToMany(autoGet = true, autoAdd = true, autoUpdate = true)
	@SimplifiedRelation(table = "flowapproval_activity_resp_user_attribute", remoteValueColumnName = "attributeName")
	@XMLElement(fixCase = true)
	private List<String> responsibleUserAttributeNames;
	
	@DAOManaged
	@NoDuplicates
	@SplitOnLineBreak
	@WebPopulate(maxLength = 255)
	@OneToMany(autoGet = true, autoAdd = true, autoUpdate = true)
	@SimplifiedRelation(table = "flowapproval_activity_resp_group_attribute", remoteValueColumnName = "attributeName")
	@XMLElement(fixCase = true)
	private List<String> responsibleGroupAttributeNames;

	@DAOManaged
	@WebPopulate
	@XMLElement
	private boolean allowManagersToAssignOwner;

	@DAOManaged
	@OneToMany
	@SimplifiedRelation(table = "flowapproval_activity_assignable_users", remoteValueColumnName = "userID")
	@XMLElement(fixCase = true)
	private List<User> assignableUsers;

	@DAOManaged
	@OneToMany
	@SimplifiedRelation(table = "flowapproval_activity_assignable_groups", remoteValueColumnName = "groupID")
	@XMLElement(fixCase = true)
	private List<Group> assignableGroups;
	
	@DAOManaged
	@OneToMany
	@SimplifiedRelation(table = "flowapproval_activity_fallback_users", remoteValueColumnName = "userID")
	@XMLElement(fixCase = true)
	private List<User> responsibleFallbackUsers;

	@DAOManaged
	@WebPopulate
	@XMLElement
	private boolean onlyUseGlobalNotifications;

	@DAOManaged
	@RequiredIfSet(paramNames = "onlyUseGlobalNotifications")
	@WebPopulate(maxLength = 255, populator = LowerCaseEmailPopulator.class)
	@XMLElement
	private String globalEmailAddress;

	@DAOManaged
	@WebPopulate(maxLength = 255)
	@XMLElement
	private String attributeName;

	@DAOManaged
	@WebPopulate
	@XMLElement
	private boolean invert;

	@DAOManaged
	@NoDuplicates
	@SplitOnLineBreak
	@RequiredIfSet(paramNames = "attributeName")
	@WebPopulate(maxLength = 1024)
	@OneToMany(autoGet = true, autoAdd = true, autoUpdate = true)
	@SimplifiedRelation(table = "flowapproval_activity_attribute_values", remoteValueColumnName = "value")
	@XMLElement(fixCase = true)
	private List<String> attributeValues;

	@DAOManaged
	@OneToMany
	@XMLElement(fixCase = true)
	private List<FlowApprovalActivityProgress> activityProgresses;
	
	@DAOManaged
	@WebPopulate
	@XMLElement
	private boolean active;

	public Integer getActivityID() {

		return activityID;
	}

	public void setActivityID(Integer activityID) {

		this.activityID = activityID;
	}

	public FlowApprovalActivityGroup getActivityGroup() {

		return activityGroup;
	}

	public void setActivityGroup(FlowApprovalActivityGroup activityGroup) {

		this.activityGroup = activityGroup;
	}

	public String getName() {

		return name;
	}

	public void setName(String name) {

		this.name = name;
	}

	public String getShortDescription() {

		return shortDescription;
	}

	public void setShortDescription(String shortDescription) {

		this.shortDescription = shortDescription;
	}

	public String getDescription() {

		return description;
	}

	public void setDescription(String description) {

		this.description = description;
	}

	public List<User> getResponsibleUsers() {

		return responsibleUsers;
	}

	public void setResponsibleUsers(List<User> responsibleUsers) {

		this.responsibleUsers = responsibleUsers;
	}

	public List<Group> getResponsibleGroups() {

		return responsibleGroups;
	}

	public void setResponsibleGroups(List<Group> responsibleGroups) {

		this.responsibleGroups = responsibleGroups;
	}

	public List<FlowApprovalActivityProgress> getActivityProgresses() {

		return activityProgresses;
	}

	public void setActivityProgresses(List<FlowApprovalActivityProgress> activityProgresses) {

		this.activityProgresses = activityProgresses;
	}

	public String getAttributeName() {

		return attributeName;
	}

	public void setAttributeName(String attributeName) {

		this.attributeName = attributeName;
	}

	public boolean isInverted() {

		return invert;
	}

	public void setInverted(boolean invert) {

		this.invert = invert;
	}

	public List<String> getAttributeValues() {

		return attributeValues;
	}

	public void setAttributeValues(List<String> attributeValues) {

		this.attributeValues = attributeValues;
	}

	public boolean isRequireSigning() {

		return requireSigning;
	}

	public void setRequireSigning(boolean requireSigning) {

		this.requireSigning = requireSigning;
	}

	public boolean isRequireComment() {

		return requireComment;
	}

	public void setRequireComment(boolean requireComment) {

		this.requireComment = requireComment;
	}
	
	public Comment getWhenToComment() {
	
		return whenToComment;
	}
	
	public void setWhenToComment(Comment whenToComment) {
	
		this.whenToComment = whenToComment;
	}
	
	public String getWhenToCommentErrorMessage() {
		
		return whenToCommentErrorMessage;
	}
	
	public void setWhenToCommentErrorMessage(String whenToCommentErrorMessage) {
	
		this.whenToCommentErrorMessage = whenToCommentErrorMessage;
	}

	public String getGlobalEmailAddress() {

		return globalEmailAddress;
	}

	public void setGlobalEmailAddress(String globalEmailAddress) {

		this.globalEmailAddress = globalEmailAddress;
	}

	public boolean isOnlyUseGlobalNotifications() {

		return onlyUseGlobalNotifications;
	}

	public void setOnlyUseGlobalNotifications(boolean onlyUseGlobalNotifications) {

		this.onlyUseGlobalNotifications = onlyUseGlobalNotifications;
	}

	public List<String> getResponsibleUserAttributeNames() {

		return responsibleUserAttributeNames;
	}

	public void setResponsibleUserAttributeName(List<String> responsibleUserAttributeNames) {

		this.responsibleUserAttributeNames = responsibleUserAttributeNames;
	}
	
	public List<String> getResponsibleGroupAttributeNames() {

		return responsibleGroupAttributeNames;
	}

	public void setResponsibleGroupAttributeName(List<String> responsibleGroupAttributeNames) {

		this.responsibleGroupAttributeNames = responsibleGroupAttributeNames;
	}

	public boolean isShowFlowInstance() {

		return showFlowInstance;
	}

	public void setShowFlowInstance(boolean showFlowInstance) {

		this.showFlowInstance = showFlowInstance;
	}

	public boolean isPDFDownloadActivation() {

		return pdfDownloadActivation;
	}

	public void setPDFDownloadActivation(boolean pdfDownloadActivation) {

		this.pdfDownloadActivation = pdfDownloadActivation;
	}

	public boolean isAllowManagersToAssignOwner() {

		return allowManagersToAssignOwner;
	}

	public void setAllowManagersToAssignOwner(boolean allowManagersToAssignOwner) {

		this.allowManagersToAssignOwner = allowManagersToAssignOwner;
	}

	public List<User> getAssignableUsers() {

		return assignableUsers;
	}

	public void setAssignableUsers(List<User> assignableUsers) {

		this.assignableUsers = assignableUsers;
	}

	public List<Group> getAssignableGroups() {

		return assignableGroups;
	}

	public void setAssignableGroups(List<Group> assignableGroups) {

		this.assignableGroups = assignableGroups;
	}

	public List<User> getResponsibleFallbackUsers() {

		return responsibleFallbackUsers;
	}

	public void setResponsibleFallbackUsers(List<User> responsibleFallbackUsers) {

		this.responsibleFallbackUsers = responsibleFallbackUsers;
	}

	public void clearUnknownUsersAndGroups() {

		setAssignableGroups(clearUnknownGroups(getAssignableGroups()));
		setResponsibleGroups(clearUnknownGroups(getResponsibleGroups()));
		setAssignableUsers(clearUnknownUsers(getAssignableUsers()));
		setResponsibleUsers(clearUnknownUsers(getResponsibleUsers()));
		setResponsibleFallbackUsers(clearUnknownUsers(getResponsibleFallbackUsers()));
	}
	
	public static List<Group> clearUnknownGroups(List<Group> list) {

		if (!CollectionUtils.isEmpty(list)) {
			list.removeIf(e -> (e.getGroupID() == null));
		}
		return list;
	}

	public static List<User> clearUnknownUsers(List<User> list) {

		if (!CollectionUtils.isEmpty(list)) {
			list.removeIf(e -> (e.getUserID() == null));
		}
		return list;
	}

	
	public boolean isActive() {
	
		return active;
	}

	
	public void setActive(boolean active) {
	
		this.active = active;
	}

	@Override
	public String toString() {

		return StringUtils.toLogFormat(name, 30) + " (activityID: " + activityID + ")";
	}

	@Override
	public int hashCode() {

		final int prime = 31;
		int result = 1;
		result = prime * result + (activityID == null ? 0 : activityID.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {

		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		FlowApprovalActivity other = (FlowApprovalActivity) obj;
		if (activityID == null) {
			if (other.activityID != null) {
				return false;
			}
		} else if (!activityID.equals(other.activityID)) {
			return false;
		}
		return true;
	}

	@Override
	public void populate(XMLParser xmlParser) throws ValidationException {

		List<ValidationError> errors = new ArrayList<>();

		this.name = XMLValidationUtils.validateParameter("name", xmlParser, true, 1, 255, StringPopulator.getPopulator(), errors);
		this.shortDescription = XMLValidationUtils.validateParameter("shortDescription", xmlParser, false, 1, 255, StringPopulator.getPopulator(), errors);
		this.description = XMLValidationUtils.validateParameter("description", xmlParser, false, 1, 65535, StringPopulator.getPopulator(), errors);
		this.showFlowInstance = xmlParser.getPrimitiveBoolean("showFlowInstance");
		this.pdfDownloadActivation = xmlParser.getPrimitiveBoolean("pdfDownloadActivation");
		this.requireSigning = xmlParser.getPrimitiveBoolean("requireSigning");
		this.requireComment = xmlParser.getPrimitiveBoolean("requireComment");
		this.whenToComment = XMLValidationUtils.validateParameter("whenToComment", xmlParser, false, new EnumPopulator<Comment>(Comment.class), errors);
		this.whenToCommentErrorMessage = XMLValidationUtils.validateParameter("whenToCommentErrorMessage", xmlParser, false, 1, 255, StringPopulator.getPopulator(), errors);
		this.allowManagersToAssignOwner = xmlParser.getPrimitiveBoolean("allowManagersToAssignOwner");
		this.onlyUseGlobalNotifications = xmlParser.getPrimitiveBoolean("onlyUseGlobalNotifications");
		this.invert = xmlParser.getPrimitiveBoolean("invert");
		this.active = xmlParser.getPrimitiveBoolean("active"); 

		this.attributeName = XMLValidationUtils.validateParameter("attributeName", xmlParser, false, 1, 255, StringPopulator.getPopulator(), errors);

		this.attributeValues = XMLValidationUtils.validateParameters("AttributeValues/value", xmlParser, false, 1, 1024, StringPopulator.getPopulator(), errors);

		this.globalEmailAddress = XMLValidationUtils.validateParameter("globalEmailAddress", xmlParser, false, 1, 255, EmailPopulator.getPopulator(), errors);

		this.responsibleUserAttributeNames = XMLValidationUtils.validateParameters("ResponsibleUserAttributeNames/value", xmlParser, false, 1, 255, StringPopulator.getPopulator(), errors);

		this.responsibleGroupAttributeNames = XMLValidationUtils.validateParameters("ResponsibleGroupAttributeNames/value", xmlParser, false, 1, 255, StringPopulator.getPopulator(), errors);
		
		List<String> responsibleUserNames = XMLValidationUtils.validateParameters("ResponsibleUsers/user", xmlParser, false, 1, 255, StringPopulator.getPopulator(), errors);

		if (!CollectionUtils.isEmpty(responsibleUserNames)) {
			List<User> responsibleUserTemp = new ArrayList<>();

			for (String userName : responsibleUserNames) {
				SimpleUser user = new SimpleUser();
				user.setUsername(userName);

				responsibleUserTemp.add(user);
			}

			if (!responsibleUserTemp.isEmpty()) {
				this.responsibleUsers = responsibleUserTemp;
			}
		}

		List<String> responsibleGroupNames = XMLValidationUtils.validateParameters("ResponsibleGroups/group", xmlParser, false, 1, 255, StringPopulator.getPopulator(), errors);

		if (!CollectionUtils.isEmpty(responsibleGroupNames)) {
			List<Group> responsibleGroupsTemp = new ArrayList<>();

			for (String groupName : responsibleGroupNames) {
				SimpleGroup group = new SimpleGroup();
				group.setName(groupName);

				responsibleGroupsTemp.add(group);
			}

			if (!responsibleGroupsTemp.isEmpty()) {
				this.responsibleGroups = responsibleGroupsTemp;
			}
		}

		List<String> assignableUserNames = XMLValidationUtils.validateParameters("AssignableUsers/user", xmlParser, false, 1, 255, StringPopulator.getPopulator(), errors);

		if (!CollectionUtils.isEmpty(assignableUserNames)) {
			List<User> assignableUserTemp = new ArrayList<>();

			for (String userName : assignableUserNames) {
				SimpleUser user = new SimpleUser();
				user.setUsername(userName);

				assignableUserTemp.add(user);
			}

			if (!assignableUserTemp.isEmpty()) {
				this.assignableUsers = assignableUserTemp;
			}
		}

		List<String> assignableGroupNames = XMLValidationUtils.validateParameters("AssignableGroups/group", xmlParser, false, 1, 255, StringPopulator.getPopulator(), errors);

		if (!CollectionUtils.isEmpty(assignableGroupNames)) {
			List<Group> assignableGroupsTemp = new ArrayList<>();

			for (String groupName : assignableGroupNames) {
				SimpleGroup group = new SimpleGroup();
				group.setName(groupName);

				assignableGroupsTemp.add(group);
			}

			if (!assignableGroupsTemp.isEmpty()) {
				this.assignableGroups = assignableGroupsTemp;
			}
		}
		
		List<String> responsibleFallbackUserNames = XMLValidationUtils.validateParameters("ResponsibleFallbackUsers/user", xmlParser, false, 1, 255, StringPopulator.getPopulator(), errors);

		if (!CollectionUtils.isEmpty(responsibleFallbackUserNames)) {
			List<User> responsibleFallbackUserTemp = new ArrayList<>();

			for (String userName : responsibleFallbackUserNames) {
				SimpleUser user = new SimpleUser();
				user.setUsername(userName);

				responsibleFallbackUserTemp.add(user);
			}

			if (!responsibleFallbackUserTemp.isEmpty()) {
				this.responsibleFallbackUsers = responsibleFallbackUserTemp;
			}
		}

		if (!errors.isEmpty()) {

			throw new ValidationException(errors);
		}

	}

}
