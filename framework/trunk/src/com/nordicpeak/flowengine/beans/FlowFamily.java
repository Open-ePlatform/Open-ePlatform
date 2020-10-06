package com.nordicpeak.flowengine.beans;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import se.unlogic.emailutils.populators.EmailPopulator;
import se.unlogic.hierarchy.core.beans.Group;
import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.hierarchy.core.handlers.GroupHandler;
import se.unlogic.hierarchy.core.handlers.UserHandler;
import se.unlogic.hierarchy.core.utils.UserUtils;
import se.unlogic.openhierarchy.foregroundmodules.siteprofile.interfaces.SiteProfile;
import se.unlogic.standardutils.annotations.NoDuplicates;
import se.unlogic.standardutils.annotations.RequiredIfSet;
import se.unlogic.standardutils.annotations.SplitOnLineBreak;
import se.unlogic.standardutils.annotations.Templated;
import se.unlogic.standardutils.annotations.WebPopulate;
import se.unlogic.standardutils.collections.CaseInsensitiveStringComparator;
import se.unlogic.standardutils.collections.CollectionUtils;
import se.unlogic.standardutils.dao.annotations.DAOManaged;
import se.unlogic.standardutils.dao.annotations.Key;
import se.unlogic.standardutils.dao.annotations.OneToMany;
import se.unlogic.standardutils.dao.annotations.SimplifiedRelation;
import se.unlogic.standardutils.dao.annotations.Table;
import se.unlogic.standardutils.date.DateUtils;
import se.unlogic.standardutils.object.ObjectUtils;
import se.unlogic.standardutils.populators.NonNegativeStringIntegerPopulator;
import se.unlogic.standardutils.populators.StringURLAliasPopulator;
import se.unlogic.standardutils.reflection.ReflectionUtils;
import se.unlogic.standardutils.string.StringTag;
import se.unlogic.standardutils.time.TimeUtils;
import se.unlogic.standardutils.xml.GeneratedElementable;
import se.unlogic.standardutils.xml.XMLElement;
import se.unlogic.standardutils.xml.XMLGeneratorDocument;
import se.unlogic.standardutils.xml.XMLUtils;

import com.nordicpeak.flowengine.annotations.TextTagReplace;
import com.nordicpeak.flowengine.comparators.FlowFamilyManagerComparator;
import com.nordicpeak.flowengine.comparators.FlowFamilyManagerGroupComparator;
import com.nordicpeak.flowengine.enums.ManagerAccess;
import com.nordicpeak.flowengine.enums.StatisticsMode;
import com.nordicpeak.flowengine.interfaces.ImmutableFlowFamily;
import com.nordicpeak.flowengine.utils.TextTagReplacer;

@Table(name = "flowengine_flow_families")
@XMLElement
public class FlowFamily extends GeneratedElementable implements Serializable, ImmutableFlowFamily {

	private static final long serialVersionUID = 6716050201654571775L;

	public static final Field FLOWS_RELATION = ReflectionUtils.getField(FlowFamily.class, "flows");
	public static final Field MANAGER_GROUPS_RELATION = ReflectionUtils.getField(FlowFamily.class, "managerGroups");
	public static final Field MANAGER_USERS_RELATION = ReflectionUtils.getField(FlowFamily.class, "managerUsers");
	public static final Field ALIASES_RELATION = ReflectionUtils.getField(FlowFamily.class, "aliases");
	public static final Field EVENTS_RELATION = ReflectionUtils.getField(FlowFamily.class, "events");
	public static final Field EXTERNAL_MESSAGE_TEMPLATES_RELATION = ReflectionUtils.getField(FlowFamily.class, "externalMessageTemplates");

	public static final Field AUTO_MANAGER_ASSIGNMENT_RULES_RELATION = ReflectionUtils.getField(FlowFamily.class, "autoManagerAssignmentRules");
	public static final Field AUTO_MANAGER_ASSIGNMENT_ALWAYS_USERS_RELATION = ReflectionUtils.getField(FlowFamily.class, "autoManagerAssignmentAlwaysUserIDs");
	public static final Field AUTO_MANAGER_ASSIGNMENT_ALWAYS_GROUPS_RELATION = ReflectionUtils.getField(FlowFamily.class, "autoManagerAssignmentAlwaysGroupIDs");
	public static final Field AUTO_MANAGER_ASSIGNMENT_NO_MATCH_USERS_RELATION = ReflectionUtils.getField(FlowFamily.class, "autoManagerAssignmentNoMatchUserIDs");
	public static final Field AUTO_MANAGER_ASSIGNMENT_NO_MATCH_GROUPS_RELATION = ReflectionUtils.getField(FlowFamily.class, "autoManagerAssignmentNoMatchGroupIDs");
	public static final Field AUTO_MANAGER_ASSIGNMENT_STATUS_RULES_RELATION = ReflectionUtils.getField(FlowFamily.class, "autoManagerAssignmentStatusRules");

	public static final Field USE_LOGIN_HELP_LINK_FIELD = ReflectionUtils.getField(FlowFamily.class, "useLoginHelpLink");
	public static final Field LOGIN_HELP_LINK_NAME_FIELD = ReflectionUtils.getField(FlowFamily.class, "loginHelpLinkName");
	public static final Field LOGIN_HELP_LINK_URL_FIELD = ReflectionUtils.getField(FlowFamily.class, "loginHelpLinkURL");
	public static final Field CONTACT_NAME_FIELD = ReflectionUtils.getField(FlowFamily.class, "contactName");
	public static final Field CONTACT_EMAIL_FIELD = ReflectionUtils.getField(FlowFamily.class, "contactEmail");
	public static final Field CONTACT_PHONE_FIELD = ReflectionUtils.getField(FlowFamily.class, "contactPhone");
	public static final Field CONTACT_WEB_ADDRESS_FIELD = ReflectionUtils.getField(FlowFamily.class, "contactWebAddress");
	public static final Field OWNER_NAME_FIELD = ReflectionUtils.getField(FlowFamily.class, "ownerName");
	public static final Field OWNER_EMAIL_FIELD = ReflectionUtils.getField(FlowFamily.class, "ownerEmail");

	@DAOManaged(autoGenerated = true)
	@Key
	@StringTag
	@XMLElement
	private Integer flowFamilyID;

	@DAOManaged
	@XMLElement
	private Integer versionCount;

	@DAOManaged
	@WebPopulate(maxLength = 255)
	@XMLElement
	private String contactName;

	@DAOManaged
	@WebPopulate(maxLength = 255, populator = EmailPopulator.class)
	@XMLElement
	private String contactEmail;

	@DAOManaged
	@WebPopulate(maxLength = 255)
	@XMLElement
	private String contactPhone;

	@DAOManaged
	@WebPopulate(maxLength = 255)
	@XMLElement
	private String contactWebAddress;

	@DAOManaged
	@WebPopulate(maxLength = 255)
	@XMLElement
	private String ownerName;

	@DAOManaged
	@WebPopulate(maxLength = 255, populator = EmailPopulator.class)
	@XMLElement
	private String ownerEmail;

	@DAOManaged
	@WebPopulate
	@XMLElement
	private boolean useLoginHelpLink;

	@DAOManaged
	@TextTagReplace
	@Templated(fieldName = "defaultLoginHelpLinkName")
	@WebPopulate(maxLength = 50)
	@RequiredIfSet(paramNames = "useLoginHelpLink")
	@XMLElement
	private String loginHelpLinkName;

	@DAOManaged
	@TextTagReplace
	@Templated(fieldName = "defaultLoginHelpLinkURL")
	@WebPopulate(maxLength = 1024)
	@RequiredIfSet(paramNames = "useLoginHelpLink")
	@XMLElement
	private String loginHelpLinkURL;

	@DAOManaged
	@WebPopulate
	@XMLElement
	private StatisticsMode statisticsMode;

	@DAOManaged
	@WebPopulate(populator = NonNegativeStringIntegerPopulator.class)
	@XMLElement
	private Integer popularityBoost;

	@DAOManaged
	@OneToMany
	private List<FlowFamilyManagerGroup> managerGroups;

	private List<Integer> managerGroupIDs;

	@DAOManaged
	@OneToMany
	private List<FlowFamilyManager> managerUsers;

	private List<Integer> managerUserIDs;

	@DAOManaged
	@OneToMany
	@XMLElement
	private List<Flow> flows;

	private Integer flowInstanceCount;

	@DAOManaged
	@OneToMany
	@SimplifiedRelation(table = "flowengine_flow_family_aliases", remoteValueColumnName = "alias")
	@WebPopulate(paramName = "alias", maxLength = 50, populator = StringURLAliasPopulator.class)
	@NoDuplicates(comparator = CaseInsensitiveStringComparator.class)
	@SplitOnLineBreak
	@XMLElement(fixCase = true, childName = "Alias")
	private List<String> aliases;

	@DAOManaged
	@OneToMany
	@XMLElement
	private List<FlowFamilyEvent> events;

	@DAOManaged
	@WebPopulate(maxLength = 24)
	@XMLElement
	private String startButtonText;

	@DAOManaged
	@OneToMany
	@XMLElement(fixCase = true)
	private List<AutoManagerAssignmentRule> autoManagerAssignmentRules;

	@DAOManaged
	@OneToMany
	@SimplifiedRelation(table = "flowengine_flow_family_automanager_always_users", remoteValueColumnName = "userID")
	private List<Integer> autoManagerAssignmentAlwaysUserIDs;

	@DAOManaged
	@OneToMany
	@SimplifiedRelation(table = "flowengine_flow_family_automanager_always_groups", remoteValueColumnName = "groupID")
	private List<Integer> autoManagerAssignmentAlwaysGroupIDs;

	@DAOManaged
	@OneToMany
	@SimplifiedRelation(table = "flowengine_flow_family_automanager_nomatch_users", remoteValueColumnName = "userID")
	private List<Integer> autoManagerAssignmentNoMatchUserIDs;

	@DAOManaged
	@OneToMany
	@SimplifiedRelation(table = "flowengine_flow_family_automanager_nomatch_groups", remoteValueColumnName = "groupID")
	private List<Integer> autoManagerAssignmentNoMatchGroupIDs;

	@DAOManaged
	@OneToMany
	@XMLElement(fixCase = true)
	private List<AutoManagerAssignmentStatusRule> autoManagerAssignmentStatusRules;

	@DAOManaged
	@OneToMany
	@XMLElement(fixCase = true)
	private List<ExternalMessageTemplate> externalMessageTemplates;

	private boolean hasTextTags;

	@Override
	public Integer getFlowFamilyID() {

		return flowFamilyID;
	}

	public void setFlowFamilyID(Integer flowFamilyID) {

		this.flowFamilyID = flowFamilyID;
	}

	@Override
	public Integer getVersionCount() {

		return versionCount;
	}

	public void setVersionCount(Integer currentIncrement) {

		this.versionCount = currentIncrement;
	}

	public String getContactName() {

		return contactName;
	}

	public void setContactName(String contactName) {

		this.contactName = contactName;
	}

	public String getContactEmail() {

		return contactEmail;
	}

	public void setContactEmail(String contactEmail) {

		this.contactEmail = contactEmail;
	}

	public String getContactPhone() {

		return contactPhone;
	}

	public void setContactPhone(String contactPhone) {

		this.contactPhone = contactPhone;
	}

	public String getContactWebAddress() {

		return contactWebAddress;
	}

	public void setContactAddress(String contactWebAddress) {

		this.contactWebAddress = contactWebAddress;
	}

	public String getOwnerName() {

		return ownerName;
	}

	public void setOwnerName(String ownerName) {

		this.ownerName = ownerName;
	}

	public String getOwnerEmail() {

		return ownerEmail;
	}

	public void setOwnerEmail(String ownerEmail) {

		this.ownerEmail = ownerEmail;
	}

	@Override
	public List<Flow> getFlows() {

		return flows;
	}

	public void setFlows(List<Flow> flows) {

		this.flows = flows;
	}

	@Override
	public int hashCode() {

		final int prime = 31;
		int result = 1;
		result = prime * result + ((flowFamilyID == null) ? 0 : flowFamilyID.hashCode());
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
		
		FlowFamily other = (FlowFamily) obj;
		
		if (flowFamilyID == null) {
		
			if (other.flowFamilyID != null) {
				return false;
			}
			
		} else if (!flowFamilyID.equals(other.flowFamilyID)) {
			
			return false;
		}
		
		return true;
	}

	public Integer getFlowInstanceCount() {

		return flowInstanceCount;
	}

	public void setFlowInstanceCount(Integer flowInstanceCount) {

		this.flowInstanceCount = flowInstanceCount;
	}

	public static long getSerialversionuid() {

		return serialVersionUID;
	}

	@Override
	public List<FlowFamilyManager> getManagers() {

		return managerUsers;
	}

	public List<FlowFamilyManager> getActiveManagers() {

		if (CollectionUtils.isEmpty(managerUsers)) {
			return null;
		}

		Timestamp startOfToday = DateUtils.setTimeToMidnight(TimeUtils.getCurrentTimestamp());
		List<FlowFamilyManager> activeManagers = new ArrayList<FlowFamilyManager>(managerUsers.size());

		for (FlowFamilyManager manager : managerUsers) {

			if (manager.getValidFromDate() != null && startOfToday.compareTo(manager.getValidFromDate()) < 0) {
				continue;
			}

			activeManagers.add(manager);
		}

		return activeManagers;
	}

	@Override
	public List<FlowFamilyManagerGroup> getManagerGroups() {

		return managerGroups;
	}

	public void setManagerGroups(List<FlowFamilyManagerGroup> managerGroups) {

		this.managerGroups = managerGroups;
		managerGroupIDs = null;
	}

	public void setManagerUsers(List<FlowFamilyManager> managerUsers) {

		this.managerUsers = managerUsers;
		managerUserIDs = null;
	}

	@Override
	public List<Integer> getActiveManagerUserIDs() {

		if (managerUserIDs == null && !CollectionUtils.isEmpty(managerUsers)) {

			Timestamp startOfToday = DateUtils.setTimeToMidnight(TimeUtils.getCurrentTimestamp());
			List<Integer> temp = new ArrayList<Integer>(managerUsers.size());

			for (FlowFamilyManager manager : managerUsers) {

				if (manager.getValidFromDate() != null && startOfToday.compareTo(manager.getValidFromDate()) < 0) {
					continue;
				}

				temp.add(manager.getUserID());
			}

			managerUserIDs = temp;
		}

		return managerUserIDs;
	}

	@Override
	public List<Integer> getManagerGroupIDs() {

		if (managerGroupIDs == null && !CollectionUtils.isEmpty(managerGroups)) {

			List<Integer> temp = new ArrayList<Integer>(managerGroups.size());

			for (FlowFamilyManagerGroup managerGroup : managerGroups) {

				temp.add(managerGroup.getGroupID());
			}

			managerGroupIDs = temp;
		}

		return managerGroupIDs;
	}

	public void setManagerUsersAndGroups(UserHandler userHandler, GroupHandler groupHandler) {

		if (managerUsers != null && userHandler != null) {

			List<Integer> missingUserIDs = null;

			for (FlowFamilyManager manager : managerUsers) {

				if (manager.getUser() == null) {

					if (missingUserIDs == null) {
						missingUserIDs = new ArrayList<Integer>(managerUsers.size());
					}

					missingUserIDs.add(manager.getUserID());
				}
			}

			if (missingUserIDs != null) {

				Map<Integer, User> userMap = UserUtils.getUserIDMap(userHandler.getUsers(missingUserIDs, false, true));

				for (FlowFamilyManager manager : managerUsers) {

					if (manager.getUser() == null) {
						manager.setUser(userMap.get(manager.getUserID()));
					}
				}

				Collections.sort(getManagers(), FlowFamilyManagerComparator.getInstance());
			}
		}

		if (managerGroups != null && groupHandler != null) {

			List<Integer> missingGroupIDs = null;

			for (FlowFamilyManagerGroup managerGroup : managerGroups) {

				if (managerGroup.getGroup() == null) {

					if (missingGroupIDs == null) {
						missingGroupIDs = new ArrayList<Integer>(managerGroups.size());
					}

					missingGroupIDs.add(managerGroup.getGroupID());
				}
			}

			if (missingGroupIDs != null) {

				Map<Integer, Group> groupMap = UserUtils.getGroupIDMap(groupHandler.getGroups(missingGroupIDs, false));

				for (FlowFamilyManagerGroup managerGroup : managerGroups) {

					if (managerGroup.getGroup() == null) {
						managerGroup.setGroup(groupMap.get(managerGroup.getGroupID()));
					}
				}

				Collections.sort(managerGroups, FlowFamilyManagerGroupComparator.getComparator());
			}
		}
	}

	@Override
	public ManagerAccess getManagerAccess(User user) {

		ManagerAccess access = null;

		if (!CollectionUtils.isEmpty(managerUsers)) {

			Timestamp startOfToday = DateUtils.setTimeToMidnight(TimeUtils.getCurrentTimestamp());

			for (FlowFamilyManager manager : managerUsers) {

				if (manager.getUserID().equals(user.getUserID())) {

					if (manager.getValidFromDate() != null && startOfToday.compareTo(manager.getValidFromDate()) < 0) {
						break;
					}

					if (manager.isRestricted()) {
						access = ManagerAccess.RESTRICTED;

					} else {
						return ManagerAccess.FULL;
					}

					break;
				}
			}
		}

		if ((access == null || access == ManagerAccess.RESTRICTED) && !CollectionUtils.isEmpty(user.getGroups()) && !CollectionUtils.isEmpty(managerGroups)) {

			for (Group group : user.getGroups()) {

				if (group.isEnabled()) {

					for (FlowFamilyManagerGroup managerGroup : managerGroups) {

						if (managerGroup.getGroupID().equals(group.getGroupID())) {

							if (managerGroup.isRestricted()) {
								access = ManagerAccess.RESTRICTED;

							} else {
								return ManagerAccess.FULL;
							}

							break;
						}
					}
				}
			}
		}

		return access;
	}

	public ManagerAccess getManagerAccess(Group group) {

		ManagerAccess access = null;
		
		if (!CollectionUtils.isEmpty(managerGroups) && group.isEnabled()) {

			for (FlowFamilyManagerGroup managerGroup : managerGroups) {

				if (managerGroup.getGroupID().equals(group.getGroupID())) {

					if (managerGroup.isRestricted()) {
						access = ManagerAccess.RESTRICTED;

					} else {
						return ManagerAccess.FULL;
					}

					break;
				}
			}
		}
		
		return access;
	}
	
	@Override
	public boolean hasUpdateManagerAccess(User user) {

		if (!CollectionUtils.isEmpty(managerUsers)) {

			Timestamp startOfToday = DateUtils.setTimeToMidnight(TimeUtils.getCurrentTimestamp());

			for (FlowFamilyManager manager : managerUsers) {

				if (manager.getUserID().equals(user.getUserID())) {

					if (manager.getValidFromDate() != null && startOfToday.compareTo(manager.getValidFromDate()) < 0) {
						break;
					}

					if (!manager.isRestricted() || manager.isAllowUpdatingManagers()) {

						return true;
					}
				}
			}
		}

		if (!CollectionUtils.isEmpty(user.getGroups()) && !CollectionUtils.isEmpty(managerGroups)) {

			for (Group group : user.getGroups()) {

				if (group.isEnabled()) {

					for (FlowFamilyManagerGroup managerGroup : managerGroups) {

						if (managerGroup.getGroupID().equals(group.getGroupID())) {

							if (!managerGroup.isRestricted() || managerGroup.isAllowUpdatingManagers()) {

								return true;
							}
						}
					}
				}
			}
		}

		return false;
	}

	@Override
	public boolean checkManagerFullAccess(User user) {

		return getManagerAccess(user) == ManagerAccess.FULL;
	}

	@Override
	public boolean checkManagerRestrictedAccess(User user) {

		return getManagerAccess(user) == ManagerAccess.RESTRICTED;
	}

	@Override
	public String toString() {

		return "ID: " + flowFamilyID;
	}

	public StatisticsMode getStatisticsMode() {

		return statisticsMode;
	}

	public void setStatisticsMode(StatisticsMode statisticsMode) {

		this.statisticsMode = statisticsMode;
	}

	public List<String> getAliases() {

		return aliases;
	}

	public void setAliases(List<String> aliases) {

		this.aliases = aliases;
	}

	public List<FlowFamilyEvent> getEvents() {

		return events;
	}

	public void setEvents(List<FlowFamilyEvent> events) {

		this.events = events;
	}

	public Integer getPopularityBoost() {

		return popularityBoost;
	}

	public void setPopularityBoost(Integer popularityBoost) {

		this.popularityBoost = popularityBoost;
	}

	public String getStartButtonText() {

		return startButtonText;
	}

	public void setStartButtonText(String startButtonText) {

		this.startButtonText = startButtonText;
	}

	public boolean hasManagers() {

		return managerUsers != null || managerGroups != null;
	}

	public boolean hasTextTags() {

		return hasTextTags;
	}

	public void setHasTextTags(boolean hasTextTags) {

		this.hasTextTags = hasTextTags;
	}

	@Override
	public List<AutoManagerAssignmentRule> getAutoManagerAssignmentRules() {

		return autoManagerAssignmentRules;
	}

	public void setAutoManagerAssignmentRules(List<AutoManagerAssignmentRule> autoManagerAssignmentRules) {

		this.autoManagerAssignmentRules = autoManagerAssignmentRules;
	}

	@Override
	public List<Integer> getAutoManagerAssignmentAlwaysUserIDs() {

		return autoManagerAssignmentAlwaysUserIDs;
	}

	public void setAutoManagerAssignmentAlwaysUserIDs(List<Integer> autoManagerAssignmentAlwaysUserIDs) {

		this.autoManagerAssignmentAlwaysUserIDs = autoManagerAssignmentAlwaysUserIDs;
	}

	@Override
	public List<Integer> getAutoManagerAssignmentAlwaysGroupIDs() {

		return autoManagerAssignmentAlwaysGroupIDs;
	}

	public void setAutoManagerAssignmentAlwaysGroupIDs(List<Integer> autoManagerAssignmentAlwaysGroupIDs) {

		this.autoManagerAssignmentAlwaysGroupIDs = autoManagerAssignmentAlwaysGroupIDs;
	}

	@Override
	public List<Integer> getAutoManagerAssignmentNoMatchUserIDs() {

		return autoManagerAssignmentNoMatchUserIDs;
	}

	public void setAutoManagerAssignmentNoMatchUserIDs(List<Integer> autoManagerAssignmentNoMatchUserIDs) {

		this.autoManagerAssignmentNoMatchUserIDs = autoManagerAssignmentNoMatchUserIDs;
	}

	@Override
	public List<Integer> getAutoManagerAssignmentNoMatchGroupIDs() {

		return autoManagerAssignmentNoMatchGroupIDs;
	}

	public void setAutoManagerAssignmentNoMatchGroupIDs(List<Integer> autoManagerAssignmentNoMatchGroupIDs) {

		this.autoManagerAssignmentNoMatchGroupIDs = autoManagerAssignmentNoMatchGroupIDs;
	}

	@Override
	public List<AutoManagerAssignmentStatusRule> getAutoManagerAssignmentStatusRules() {

		return autoManagerAssignmentStatusRules;
	}

	public void setAutoManagerAssignmentStatusRules(List<AutoManagerAssignmentStatusRule> autoManagerAssignmentStatusRules) {

		this.autoManagerAssignmentStatusRules = autoManagerAssignmentStatusRules;
	}

	public boolean usesAutoManagerAssignment() {

		return !ObjectUtils.isNull(autoManagerAssignmentRules, autoManagerAssignmentAlwaysUserIDs, autoManagerAssignmentAlwaysGroupIDs, autoManagerAssignmentNoMatchUserIDs, autoManagerAssignmentNoMatchGroupIDs, autoManagerAssignmentStatusRules);
	}

	public List<ExternalMessageTemplate> getExternalMessageTemplates() {

		return externalMessageTemplates;
	}

	public void setExternalMessageTemplates(List<ExternalMessageTemplate> externalMessageTemplates) {

		this.externalMessageTemplates = externalMessageTemplates;
	}

	public Element toXML(Document doc, SiteProfile siteProfile) {

		XMLGeneratorDocument genDoc = new XMLGeneratorDocument(doc);
		genDoc.addIgnoredFields(LOGIN_HELP_LINK_NAME_FIELD, LOGIN_HELP_LINK_URL_FIELD);

		Element flowElement = super.toXML(genDoc);

		String loginHelpLinkName = this.loginHelpLinkName;
		String loginHelpLinkURL = this.loginHelpLinkURL;

		if (siteProfile != null && hasTextTags) {

			if (loginHelpLinkName != null) {

				loginHelpLinkName = TextTagReplacer.replaceTextTags(loginHelpLinkName, siteProfile.getSettingHandler());
			}

			if (loginHelpLinkURL != null) {

				loginHelpLinkURL = TextTagReplacer.replaceTextTags(loginHelpLinkURL, siteProfile.getSettingHandler());
			}
		}

		XMLUtils.appendNewElement(doc, flowElement, LOGIN_HELP_LINK_NAME_FIELD.getName(), loginHelpLinkName);
		XMLUtils.appendNewElement(doc, flowElement, LOGIN_HELP_LINK_URL_FIELD.getName(), loginHelpLinkURL);

		return flowElement;
	}

}
