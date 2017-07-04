package com.nordicpeak.flowengine.notifications;

import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import se.unlogic.emailutils.populators.EmailPopulator;
import se.unlogic.standardutils.annotations.PopulateOnlyIfSet;
import se.unlogic.standardutils.annotations.RequiredIfSet;
import se.unlogic.standardutils.annotations.SplitOnLineBreak;
import se.unlogic.standardutils.annotations.WebPopulate;
import se.unlogic.standardutils.dao.annotations.DAOManaged;
import se.unlogic.standardutils.dao.annotations.Key;
import se.unlogic.standardutils.dao.annotations.OneToMany;
import se.unlogic.standardutils.dao.annotations.SimplifiedRelation;
import se.unlogic.standardutils.dao.annotations.Table;
import se.unlogic.standardutils.xml.GeneratedElementable;
import se.unlogic.standardutils.xml.XMLElement;
import se.unlogic.standardutils.xml.XMLUtils;

@Table(name = "flow_familiy_notification_settings")
@XMLElement(name = "NotificationSettings")
public class FlowFamililyNotificationSettings extends GeneratedElementable {

	@DAOManaged
	@Key
	@XMLElement
	private Integer flowFamilyID;

	@DAOManaged
	@WebPopulate
	@XMLElement
	private boolean sendStatusChangedUserSMS;

	@DAOManaged
	@WebPopulate
	@XMLElement
	private boolean sendExternalMessageReceivedUserSMS;

	@DAOManaged
	@WebPopulate
	@XMLElement
	private boolean sendFlowInstanceSubmittedUserSMS;

	@DAOManaged
	@WebPopulate
	@XMLElement
	private boolean sendFlowInstanceArchivedUserSMS;

	@DAOManaged
	@WebPopulate
	@XMLElement
	private boolean sendStatusChangedUserEmail;

	@DAOManaged
	@WebPopulate
	@XMLElement
	private boolean sendExternalMessageReceivedUserEmail;

	@DAOManaged
	@WebPopulate
	@XMLElement
	private boolean sendFlowInstanceSubmittedUserEmail;

	@DAOManaged
	@WebPopulate
	@XMLElement
	private boolean sendFlowInstanceArchivedUserEmail;

	@DAOManaged
	@WebPopulate(maxLength = 255)
	@XMLElement
	private String flowInstanceSubmittedUserEmailSubject;

	@DAOManaged
	@WebPopulate(maxLength = 65536)
	@XMLElement
	private String flowInstanceSubmittedUserEmailMessage;

	@DAOManaged
	@WebPopulate
	@XMLElement
	private boolean flowInstanceSubmittedUserEmailAttachPDF;

	@DAOManaged
	@WebPopulate(maxLength = 65536)
	@XMLElement
	private String flowInstanceSubmittedNotLoggedInUserEmailMessage;

	@DAOManaged
	@WebPopulate(maxLength = 255)
	@XMLElement
	private String flowInstanceArchivedUserEmailSubject;

	@DAOManaged
	@WebPopulate(maxLength = 65536)
	@XMLElement
	private String flowInstanceArchivedUserEmailMessage;

	@DAOManaged
	@WebPopulate(maxLength = 65536)
	@XMLElement
	private String flowInstanceArchivedNotLoggedInUserEmailMessage;

	@DAOManaged
	@WebPopulate
	@XMLElement
	private boolean sendExternalMessageReceivedManagerEmail;

	@DAOManaged
	@WebPopulate
	@XMLElement
	private boolean sendFlowInstanceAssignedManagerEmail;

	@DAOManaged
	@WebPopulate
	@XMLElement
	private boolean sendStatusChangedManagerEmail;

	@DAOManaged
	@WebPopulate
	@XMLElement
	private boolean sendFlowInstanceSubmittedManagerEmail;

	@DAOManaged
	@WebPopulate
	@XMLElement
	private boolean sendFlowInstanceSubmittedGlobalEmail;

	@DAOManaged
	@WebPopulate
	@XMLElement
	private String flowInstanceSubmittedGlobalEmailSubject;

	@DAOManaged
	@WebPopulate
	@XMLElement
	private String flowInstanceSubmittedGlobalEmailMessage;

	@DAOManaged
	@OneToMany(autoAdd = true, autoGet = true, autoUpdate = true)
	@SimplifiedRelation(table = "flow_familiy_notification_setting_submitglobal", remoteValueColumnName = "email")
	@WebPopulate(maxLength = 255, populator = EmailPopulator.class)
	@RequiredIfSet(paramNames = "sendFlowInstanceSubmittedGlobalEmail")
	@SplitOnLineBreak
	@XMLElement(fixCase=true, childName="address")
	private List<String> flowInstanceSubmittedGlobalEmailAddresses;

	@DAOManaged
	@WebPopulate
	@XMLElement
	private boolean flowInstanceSubmittedGlobalEmailAttachPDF;

	@DAOManaged
	@WebPopulate
	@PopulateOnlyIfSet(paramNames = "flowInstanceSubmittedGlobalEmailAttachPDF")
	@XMLElement
	private boolean flowInstanceSubmittedGlobalEmailAttachPDFAttachmentsSeparately;
	
	@DAOManaged
	@WebPopulate
	@XMLElement
	private boolean sendExternalMessageReceivedGlobalEmail;
	
	@DAOManaged
	@OneToMany(autoAdd = true, autoGet = true, autoUpdate = true)
	@SimplifiedRelation(table = "flow_familiy_notification_setting_extmessageglobal", remoteValueColumnName = "email")
	@WebPopulate(maxLength = 255, populator = EmailPopulator.class)
	@RequiredIfSet(paramNames = "sendExternalMessageReceivedGlobalEmail")
	@SplitOnLineBreak
	@XMLElement(fixCase=true, childName="address")
	private List<String> externalMessageReceivedGlobalEmailAddresses;

	public Integer getFlowFamilyID() {

		return flowFamilyID;
	}

	public void setFlowFamilyID(Integer flowFamilyID) {

		this.flowFamilyID = flowFamilyID;
	}

	public boolean isSendStatusChangedUserSMS() {

		return sendStatusChangedUserSMS;
	}

	public void setSendStatusChangedUserSMS(boolean sendStatusChangedUserSMS) {

		this.sendStatusChangedUserSMS = sendStatusChangedUserSMS;
	}

	public boolean isSendExternalMessageReceivedUserSMS() {

		return sendExternalMessageReceivedUserSMS;
	}

	public void setSendExternalMessageReceivedUserSMS(boolean sendExternalMessageReceivedUserSMS) {

		this.sendExternalMessageReceivedUserSMS = sendExternalMessageReceivedUserSMS;
	}

	public boolean isSendFlowInstanceSubmittedUserSMS() {

		return sendFlowInstanceSubmittedUserSMS;
	}

	public void setSendFlowInstanceSubmittedUserSMS(boolean sendFlowInstanceSubmittedUserSMS) {

		this.sendFlowInstanceSubmittedUserSMS = sendFlowInstanceSubmittedUserSMS;
	}

	public boolean isSendFlowInstanceArchivedUserSMS() {

		return sendFlowInstanceArchivedUserSMS;
	}

	public void setSendFlowInstanceArchivedUserSMS(boolean sendFlowInstanceArchivedUserSMS) {

		this.sendFlowInstanceArchivedUserSMS = sendFlowInstanceArchivedUserSMS;
	}

	public boolean isSendStatusChangedUserEmail() {

		return sendStatusChangedUserEmail;
	}

	public void setSendStatusChangedUserEmail(boolean sendStatusChangedUserEmail) {

		this.sendStatusChangedUserEmail = sendStatusChangedUserEmail;
	}

	public boolean isSendExternalMessageReceivedUserEmail() {

		return sendExternalMessageReceivedUserEmail;
	}

	public void setSendExternalMessageReceivedUserEmail(boolean sendExternalMessageReceivedUserEmail) {

		this.sendExternalMessageReceivedUserEmail = sendExternalMessageReceivedUserEmail;
	}

	public boolean isSendFlowInstanceSubmittedUserEmail() {

		return sendFlowInstanceSubmittedUserEmail;
	}

	public void setSendFlowInstanceSubmittedUserEmail(boolean sendFlowInstanceSubmittedUserEmail) {

		this.sendFlowInstanceSubmittedUserEmail = sendFlowInstanceSubmittedUserEmail;
	}

	public boolean isSendFlowInstanceArchivedUserEmail() {

		return sendFlowInstanceArchivedUserEmail;
	}

	public void setSendFlowInstanceArchivedUserEmail(boolean sendFlowInstanceArchivedUserEmail) {

		this.sendFlowInstanceArchivedUserEmail = sendFlowInstanceArchivedUserEmail;
	}

	public String getFlowInstanceSubmittedUserEmailSubject() {

		return flowInstanceSubmittedUserEmailSubject;
	}

	public void setFlowInstanceSubmittedUserEmailSubject(String flowInstanceSubmittedUserEmailSubject) {

		this.flowInstanceSubmittedUserEmailSubject = flowInstanceSubmittedUserEmailSubject;
	}

	public String getFlowInstanceSubmittedUserEmailMessage() {

		return flowInstanceSubmittedUserEmailMessage;
	}

	public void setFlowInstanceSubmittedUserEmailMessage(String flowInstanceSubmittedUserEmailMessage) {

		this.flowInstanceSubmittedUserEmailMessage = flowInstanceSubmittedUserEmailMessage;
	}

	public boolean isFlowInstanceSubmittedUserEmailAttachPDF() {

		return flowInstanceSubmittedUserEmailAttachPDF;
	}

	public void setFlowInstanceSubmittedUserEmailAttachPDF(boolean flowInstanceSubmittedUserEmailAttachPDF) {

		this.flowInstanceSubmittedUserEmailAttachPDF = flowInstanceSubmittedUserEmailAttachPDF;
	}

	public String getFlowInstanceArchivedNotLoggedInUserEmailMessage() {

		return flowInstanceArchivedNotLoggedInUserEmailMessage;
	}

	public void setFlowInstanceArchivedNotLoggedInUserEmailMessage(String flowInstanceArchivedNotLoggedInUserEmailMessage) {

		this.flowInstanceArchivedNotLoggedInUserEmailMessage = flowInstanceArchivedNotLoggedInUserEmailMessage;
	}

	public String getFlowInstanceArchivedUserEmailSubject() {

		return flowInstanceArchivedUserEmailSubject;
	}

	public void setFlowInstanceArchivedUserEmailSubject(String flowInstanceArchivedUserEmailSubject) {

		this.flowInstanceArchivedUserEmailSubject = flowInstanceArchivedUserEmailSubject;
	}

	public String getFlowInstanceArchivedUserEmailMessage() {

		return flowInstanceArchivedUserEmailMessage;
	}

	public void setFlowInstanceArchivedUserEmailMessage(String flowInstanceArchivedUserEmailMessage) {

		this.flowInstanceArchivedUserEmailMessage = flowInstanceArchivedUserEmailMessage;
	}

	public String getFlowInstanceSubmittedNotLoggedInUserEmailMessage() {

		return flowInstanceSubmittedNotLoggedInUserEmailMessage;
	}

	public void setFlowInstanceSubmittedNotLoggedInUserEmailMessage(String flowInstanceSubmittedNotLoggedInUserEmailMessage) {

		this.flowInstanceSubmittedNotLoggedInUserEmailMessage = flowInstanceSubmittedNotLoggedInUserEmailMessage;
	}

	public boolean isSendExternalMessageReceivedManagerEmail() {

		return sendExternalMessageReceivedManagerEmail;
	}

	public void setSendExternalMessageReceivedManagerEmail(boolean sendExternalMessageReceivedManagerEmail) {

		this.sendExternalMessageReceivedManagerEmail = sendExternalMessageReceivedManagerEmail;
	}

	public boolean isSendFlowInstanceAssignedManagerEmail() {

		return sendFlowInstanceAssignedManagerEmail;
	}

	public void setSendFlowInstanceAssignedManagerEmail(boolean sendFlowInstanceAssignedManagerEmail) {

		this.sendFlowInstanceAssignedManagerEmail = sendFlowInstanceAssignedManagerEmail;
	}

	public boolean isSendStatusChangedManagerEmail() {

		return sendStatusChangedManagerEmail;
	}

	public void setSendStatusChangedManagerEmail(boolean sendStatusChangedManagerEmail) {

		this.sendStatusChangedManagerEmail = sendStatusChangedManagerEmail;
	}

	public boolean isSendFlowInstanceSubmittedManagerEmail() {

		return sendFlowInstanceSubmittedManagerEmail;
	}

	public void setSendFlowInstanceSubmittedManagerEmail(boolean sendFlowInstanceSubmittedManagerEmail) {

		this.sendFlowInstanceSubmittedManagerEmail = sendFlowInstanceSubmittedManagerEmail;
	}

	@Override
	public Element toXML(Document doc) {

		Element settingsElement = super.toXML(doc);

		if (sendStatusChangedUserSMS || sendExternalMessageReceivedUserSMS || sendFlowInstanceSubmittedUserSMS || sendFlowInstanceArchivedUserSMS || sendStatusChangedUserEmail || sendExternalMessageReceivedUserEmail || sendFlowInstanceSubmittedUserEmail || sendFlowInstanceArchivedUserEmail) {

			XMLUtils.appendNewElement(doc, settingsElement, "HasEnabledUserNotifications");
		}

		if (sendExternalMessageReceivedManagerEmail || sendFlowInstanceAssignedManagerEmail || sendStatusChangedManagerEmail || sendFlowInstanceSubmittedManagerEmail) {

			XMLUtils.appendNewElement(doc, settingsElement, "HasEnabledManagerNotifications");
		}

		if (sendFlowInstanceSubmittedGlobalEmail || sendExternalMessageReceivedGlobalEmail) {

			XMLUtils.appendNewElement(doc, settingsElement, "HasEnabledGlobalNotifications");
		}

		return settingsElement;
	}

	public boolean isSendFlowInstanceSubmittedGlobalEmail() {

		return sendFlowInstanceSubmittedGlobalEmail;
	}

	public void setSendFlowInstanceSubmittedGlobalEmail(boolean sendFlowInstanceSubmittedGlobalEmail) {

		this.sendFlowInstanceSubmittedGlobalEmail = sendFlowInstanceSubmittedGlobalEmail;
	}

	public String getFlowInstanceSubmittedGlobalEmailSubject() {

		return flowInstanceSubmittedGlobalEmailSubject;
	}

	public void setFlowInstanceSubmittedGlobalEmailSubject(String flowInstanceSubmittedGlobalEmailSubject) {

		this.flowInstanceSubmittedGlobalEmailSubject = flowInstanceSubmittedGlobalEmailSubject;
	}

	public String getFlowInstanceSubmittedGlobalEmailMessage() {

		return flowInstanceSubmittedGlobalEmailMessage;
	}

	public void setFlowInstanceSubmittedGlobalEmailMessage(String flowInstanceSubmittedGlobalEmailMessage) {

		this.flowInstanceSubmittedGlobalEmailMessage = flowInstanceSubmittedGlobalEmailMessage;
	}

	public boolean isFlowInstanceSubmittedGlobalEmailAttachPDF() {

		return flowInstanceSubmittedGlobalEmailAttachPDF;
	}

	public void setFlowInstanceSubmittedGlobalEmailAttachPDF(boolean flowInstanceSubmittedGlobalEmailAttachPDF) {

		this.flowInstanceSubmittedGlobalEmailAttachPDF = flowInstanceSubmittedGlobalEmailAttachPDF;
	}

	public boolean isFlowInstanceSubmittedGlobalEmailAttachPDFAttachmentsSeparately() {

		return flowInstanceSubmittedGlobalEmailAttachPDFAttachmentsSeparately;
	}

	public void setFlowInstanceSubmittedGlobalEmailAttachPDFAttachmentsSeparately(boolean flowInstanceSubmittedUserEmailAttachPDFAttachmentsSeparately) {

		this.flowInstanceSubmittedGlobalEmailAttachPDFAttachmentsSeparately = flowInstanceSubmittedUserEmailAttachPDFAttachmentsSeparately;
	}

	public List<String> getFlowInstanceSubmittedGlobalEmailAddresses() {

		return flowInstanceSubmittedGlobalEmailAddresses;
	}

	public void setFlowInstanceSubmittedGlobalEmailAddresses(List<String> flowInstanceSubmittedGlobalEmailAddresses) {

		this.flowInstanceSubmittedGlobalEmailAddresses = flowInstanceSubmittedGlobalEmailAddresses;
	}

	
	public List<String> getExternalMessageReceivedGlobalEmailAddresses() {
	
		return externalMessageReceivedGlobalEmailAddresses;
	}

	
	public void setExternalMessageReceivedGlobalEmailAddresses(List<String> externalMessageReceivedGlobalEmailAddresses) {
	
		this.externalMessageReceivedGlobalEmailAddresses = externalMessageReceivedGlobalEmailAddresses;
	}

	
	public boolean isSendExternalMessageReceivedGlobalEmail() {
	
		return sendExternalMessageReceivedGlobalEmail;
	}

	
	public void setSendExternalMessageReceivedGlobalEmail(boolean sendExternalMessageReceivedGlobalEmail) {
	
		this.sendExternalMessageReceivedGlobalEmail = sendExternalMessageReceivedGlobalEmail;
	}
}
