package com.nordicpeak.flowengine.beans;

import java.sql.Timestamp;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.standardutils.annotations.WebPopulate;
import se.unlogic.standardutils.dao.annotations.DAOManaged;
import se.unlogic.standardutils.dao.annotations.Key;
import se.unlogic.standardutils.dao.annotations.OneToMany;
import se.unlogic.standardutils.dao.annotations.SimplifiedRelation;
import se.unlogic.standardutils.dao.annotations.Table;
import se.unlogic.standardutils.date.DateUtils;
import se.unlogic.standardutils.string.StringUtils;
import se.unlogic.standardutils.time.TimeUtils;
import se.unlogic.standardutils.xml.GeneratedElementable;
import se.unlogic.standardutils.xml.XMLElement;
import se.unlogic.standardutils.xml.XMLUtils;

import com.nordicpeak.flowengine.enums.OperatingMessageType;
import com.nordicpeak.flowengine.interfaces.OperatingStatus;

@Table(name = "flowengine_operating_messages")
@XMLElement
public class OperatingMessage extends GeneratedElementable implements OperatingStatus {

	@Key
	@DAOManaged(autoGenerated = true)
	@XMLElement
	private Integer messageID;

	@DAOManaged
	@WebPopulate(maxLength = 255, required = true)
	@XMLElement
	private String message;

	@DAOManaged
	private Timestamp startTime;

	@DAOManaged
	private Timestamp endTime;

	@DAOManaged
	@WebPopulate
	@XMLElement
	private OperatingMessageType messageType;
	
	@DAOManaged
	@WebPopulate(paramName = "flowFamilyID")
	@OneToMany(autoAdd = true, autoGet = true, autoUpdate = true)
	@SimplifiedRelation(table = "flowengine_operating_message_flowfamilies", remoteValueColumnName = "flowFamilyID")
	@XMLElement(childName = "flowFamilyID")
	private List<Integer> flowFamilyIDs;

	@DAOManaged
	@WebPopulate
	@XMLElement
	private boolean disableFlows;
	
	@DAOManaged
	@WebPopulate
	@XMLElement
	private boolean allowManagingOfInstances;
	
	@DAOManaged
	@WebPopulate
	@XMLElement
	private boolean allowUserHandlingOfSubmittedInstances;
	
	@DAOManaged
	@WebPopulate
	@XMLElement
	private boolean global;
	
	@DAOManaged
	@XMLElement
	private Timestamp posted;
	
	@DAOManaged
	@XMLElement
	private Timestamp updated;
	
	@DAOManaged(dontUpdateIfNull=true)
	@XMLElement(childName="poster")
	private User poster;
	
	@DAOManaged
	@XMLElement(childName="editor")
	private User editor;

	@DAOManaged
	@WebPopulate
	@XMLElement
	private boolean profileFilter;
	
	@DAOManaged
	@WebPopulate(paramName = "profileID")
	@OneToMany(autoAdd = true, autoGet = true, autoUpdate = true)
	@SimplifiedRelation(table = "flowengine_operating_message_profiles", remoteValueColumnName = "profileID")
	@XMLElement(childName = "profileID")
	private List<Integer> profileIDs;

	public Integer getMessageID() {

		return messageID;
	}

	public void setMessageID(Integer messageID) {

		this.messageID = messageID;
	}

	@Override
	public String getMessage() {

		return message;
	}

	public void setMessage(String message) {

		this.message = message;
	}

	public Timestamp getStartTime() {

		return startTime;
	}

	public void setStartTime(Timestamp startTime) {

		this.startTime = startTime;
	}

	public Timestamp getEndTime() {

		return endTime;
	}

	public void setEndTime(Timestamp endTime) {

		this.endTime = endTime;
	}

	public OperatingMessageType getMessageType() {
		
		return messageType;
	}

	public void setMessageType(OperatingMessageType messageType) {
	
		this.messageType = messageType;
	}

	public List<Integer> getFlowFamilyIDs() {

		return flowFamilyIDs;
	}

	public void setFlowFamilyIDs(List<Integer> flowFamilyIDs) {

		this.flowFamilyIDs = flowFamilyIDs;
	}

	public boolean isDisableFlows() {

		return disableFlows;
	}

	public void setDisableFlows(boolean disableFlows) {

		this.disableFlows = disableFlows;
	}

	public boolean isGlobal() {

		return global;
	}

	public void setGlobal(boolean global) {

		this.global = global;
	}

	
	public Timestamp getPosted() {
	
		return posted;
	}

	
	public void setPosted(Timestamp posted) {
	
		this.posted = posted;
	}

	
	public Timestamp getUpdated() {
	
		return updated;
	}

	
	public void setUpdated(Timestamp updated) {
	
		this.updated = updated;
	}

	
	public User getPoster() {
	
		return poster;
	}

	
	public void setPoster(User poster) {
	
		this.poster = poster;
	}

	
	public User getEditor() {
	
		return editor;
	}

	
	public void setEditor(User editor) {
	
		this.editor = editor;
	}

	public List<Integer> getProfileIDs() {
		
		return profileIDs;
	}
	
	public void setProfileIDs(List<Integer> profileIDs) {
	
		this.profileIDs = profileIDs;
	}
	
	@Override
	public int hashCode() {

		final int prime = 31;
		int result = 1;
		result = prime * result + ((messageID == null) ? 0 : messageID.hashCode());
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
		OperatingMessage other = (OperatingMessage) obj;
		if (messageID == null) {
			if (other.messageID != null) {
				return false;
			}
		} else if (!messageID.equals(other.messageID)) {
			return false;
		}
		return true;
	}

	@Override
	public boolean isDisabled() {

		return disableFlows;
	}

	
	public boolean allowsManagingOfInstances() {
	
		return allowManagingOfInstances;
	}

	
	public void setAllowManagingOfInstances(boolean allowManagingOfInstances) {

		this.allowManagingOfInstances = allowManagingOfInstances;
	}

	public boolean allowsUserHandlingOfSubmittedInstances() {
		return allowUserHandlingOfSubmittedInstances;
	}

	public void setAllowUserHandlingOfSubmittedInstances(boolean allowExistingInstances) {
		this.allowUserHandlingOfSubmittedInstances = allowExistingInstances;
	}

	@Override
	public String toString() {

		return StringUtils.toLogFormat(message, 30) + " (ID: " + messageID + ")";
	}
	
	@Override
	public Element toXML(Document doc) {

		Element element = super.toXML(doc);
		
		if (startTime != null && endTime != null) {

			XMLUtils.appendNewElement(doc, element, "startTime", TimeUtils.TIME_FORMATTER.format(startTime));
			XMLUtils.appendNewElement(doc, element, "endTime", TimeUtils.TIME_FORMATTER.format(endTime));

			XMLUtils.appendNewElement(doc, element, "startDate", DateUtils.DATE_FORMATTER.format(startTime));
			XMLUtils.appendNewElement(doc, element, "endDate", DateUtils.DATE_FORMATTER.format(endTime));

		}
		
		return element;
	}
}
