package com.nordicpeak.flowengine.beans;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import se.unlogic.emailutils.populators.EmailPopulator;
import se.unlogic.hierarchy.core.interfaces.AccessInterface;
import se.unlogic.openhierarchy.foregroundmodules.siteprofile.interfaces.SiteProfile;
import se.unlogic.standardutils.annotations.NoDuplicates;
import se.unlogic.standardutils.annotations.RequiredIfSet;
import se.unlogic.standardutils.annotations.SplitOnLineBreak;
import se.unlogic.standardutils.annotations.Templated;
import se.unlogic.standardutils.annotations.WebPopulate;
import se.unlogic.standardutils.collections.CollectionUtils;
import se.unlogic.standardutils.dao.annotations.DAOManaged;
import se.unlogic.standardutils.dao.annotations.Key;
import se.unlogic.standardutils.dao.annotations.OneToMany;
import se.unlogic.standardutils.dao.annotations.SimplifiedRelation;
import se.unlogic.standardutils.dao.annotations.Table;
import se.unlogic.standardutils.date.DateUtils;
import se.unlogic.standardutils.populators.NonNegativeStringIntegerPopulator;
import se.unlogic.standardutils.populators.StringURLAliasPopulator;
import se.unlogic.standardutils.reflection.ReflectionUtils;
import se.unlogic.standardutils.time.TimeUtils;
import se.unlogic.standardutils.xml.GeneratedElementable;
import se.unlogic.standardutils.xml.XMLElement;
import se.unlogic.standardutils.xml.XMLGeneratorDocument;
import se.unlogic.standardutils.xml.XMLUtils;

import com.nordicpeak.flowengine.annotations.TextTagReplace;
import com.nordicpeak.flowengine.enums.StatisticsMode;
import com.nordicpeak.flowengine.interfaces.ImmutableFlowFamily;
import com.nordicpeak.flowengine.utils.TextTagReplacer;

@Table(name = "flowengine_flow_families")
@XMLElement
public class FlowFamily extends GeneratedElementable implements Serializable, ImmutableFlowFamily, AccessInterface {

	private static final long serialVersionUID = 6716050201654571775L;

	public static final Field FLOWS_RELATION = ReflectionUtils.getField(FlowFamily.class, "flows");
	public static final Field MANAGER_GROUPS_RELATION = ReflectionUtils.getField(FlowFamily.class, "managerGroups");
	public static final Field MANAGER_USERS_RELATION = ReflectionUtils.getField(FlowFamily.class, "managerUsers");
	public static final Field ALIASES_RELATION = ReflectionUtils.getField(FlowFamily.class, "aliases");
	public static final Field EVENTS_RELATION = ReflectionUtils.getField(FlowFamily.class, "events");
	
	public static final Field LOGIN_HELP_LINK_NAME_FIELD = ReflectionUtils.getField(FlowFamily.class, "loginHelpLinkName");
	public static final Field LOGIN_HELP_LINK_URL_FIELD = ReflectionUtils.getField(FlowFamily.class, "loginHelpLinkURL");

	@DAOManaged(autoGenerated = true)
	@Key
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
	@WebPopulate(maxLength = 255)
	@RequiredIfSet(paramNames = "useLoginHelpLink")
	@XMLElement
	private String loginHelpLinkURL;

	@DAOManaged
	@WebPopulate
	@XMLElement
	private StatisticsMode statisticsMode;

	@DAOManaged
	@WebPopulate(populator=NonNegativeStringIntegerPopulator.class)
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
	@NoDuplicates
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

	public List<FlowFamilyManager> getManagerUsers() {

		return managerUsers;
	}
	
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
	
	public List<Integer> getManagerUserIDs() {
		
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

	@Override
	public boolean allowsAdminAccess() {

		return false;
	}

	@Override
	public boolean allowsUserAccess() {

		return false;
	}

	@Override
	public boolean allowsAnonymousAccess() {

		return false;
	}

	@Override
	public List<Integer> getAllowedGroupIDs() {

		return getManagerGroupIDs();
	}

	@Override
	public List<Integer> getAllowedUserIDs() {

		return getManagerUserIDs();
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