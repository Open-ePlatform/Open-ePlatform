package com.nordicpeak.flowengine.notifications;

import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.nordicpeak.flowengine.populators.EmailAttributeTagPopulator;

import se.unlogic.standardutils.annotations.NoDuplicates;
import se.unlogic.standardutils.annotations.PopulateOnlyIfSet;
import se.unlogic.standardutils.annotations.RequiredIfSet;
import se.unlogic.standardutils.annotations.SplitOnLineBreak;
import se.unlogic.standardutils.annotations.Templated;
import se.unlogic.standardutils.annotations.WebPopulate;
import se.unlogic.standardutils.collections.CaseInsensitiveStringComparator;
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
	@WebPopulate(maxLength = 255)
	@XMLElement
	private String statusChangedUserEmailSubject;

	@DAOManaged
	@WebPopulate(maxLength = 65536)
	@XMLElement
	private String statusChangedUserEmailMessage;

	@DAOManaged
	@WebPopulate
	@XMLElement
	private boolean sendExternalMessageReceivedUserEmail;

	@DAOManaged
	@WebPopulate(maxLength = 255)
	@XMLElement
	private String externalMessageReceivedUserEmailSubject;

	@DAOManaged
	@WebPopulate(maxLength = 65536)
	@XMLElement
	private String externalMessageReceivedUserEmailMessage;

	@DAOManaged
	@WebPopulate
	@XMLElement
	private boolean sendFlowInstanceSubmittedUserEmail;

	@DAOManaged
	@WebPopulate
	@XMLElement
	private boolean sendFlowInstanceArchivedUserEmail;

	@DAOManaged
	@Templated
	@WebPopulate(maxLength = 1024)
	@XMLElement
	private String statusChangedUserSMS;

	@DAOManaged
	@Templated
	@WebPopulate(maxLength = 1024)
	@XMLElement
	private String externalMessageReceivedUserSMS;

	@DAOManaged
	@Templated
	@WebPopulate(maxLength = 1024)
	@XMLElement
	private String flowInstanceSubmittedUserSMS;

	@DAOManaged
	@Templated
	@WebPopulate(maxLength = 1024)
	@XMLElement
	private String flowInstanceSubmittedNotLoggedInUserSMS;

	@DAOManaged
	@Templated
	@WebPopulate(maxLength = 1024)
	@XMLElement
	private String flowInstanceArchivedUserSMS;

	@DAOManaged
	@Templated
	@WebPopulate(maxLength = 1024)
	@XMLElement
	private String flowInstanceArchivedNotLoggedInUserSMS;

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
	@WebPopulate(maxLength = 255)
	@XMLElement
	private String externalMessageReceivedManagerSubject;

	@DAOManaged
	@WebPopulate(maxLength = 65536)
	@XMLElement
	private String externalMessageReceivedManagerMessage;

	@DAOManaged
	@WebPopulate
	@XMLElement
	private boolean sendInternalMessageAddedManagerEmail;

	@DAOManaged
	@WebPopulate(maxLength = 255)
	@Templated
	@XMLElement
	private String internalMessageAddedManagerEmailSubject;

	@DAOManaged
	@WebPopulate(maxLength = 65536)
	@Templated
	@XMLElement
	private String internalMessageAddedManagerEmailMessage;

	@DAOManaged
	@WebPopulate
	@XMLElement
	private boolean sendFlowInstanceAssignedManagerEmail;

	@DAOManaged
	@WebPopulate(maxLength = 255)
	@Templated
	@XMLElement
	private String flowInstanceAssignedManagerEmailSubject;

	@DAOManaged
	@WebPopulate(maxLength = 65536)
	@Templated
	@XMLElement
	private String flowInstanceAssignedManagerEmailMessage;

	@DAOManaged
	@WebPopulate
	@XMLElement
	private boolean sendStatusChangedManagerEmail;

	@DAOManaged
	@WebPopulate(maxLength = 255)
	@Templated
	@XMLElement
	private String statusChangedManagerEmailSubject;

	@DAOManaged
	@WebPopulate(maxLength = 65536)
	@Templated
	@XMLElement
	private String statusChangedManagerEmailMessage;

	@DAOManaged
	@WebPopulate
	@XMLElement
	private boolean sendFlowInstanceCompletionManagerEmail;

	@DAOManaged
	@WebPopulate(maxLength = 255)
	@Templated
	@XMLElement
	private String managerCompletionSubmittedEmailSubject;

	@DAOManaged
	@WebPopulate(maxLength = 65536)
	@Templated
	@XMLElement
	private String managerCompletionSubmittedEmailMessage;

	@DAOManaged
	@WebPopulate
	@XMLElement
	private boolean sendFlowInstanceSubmittedManagerEmail;

	@DAOManaged
	@WebPopulate(maxLength = 255)
	@Templated
	@XMLElement
	private String flowInstanceSubmittedManagerEmailSubject;

	@DAOManaged
	@WebPopulate(maxLength = 65536)
	@Templated
	@XMLElement
	private String flowInstanceSubmittedManagerEmailMessage;

	@DAOManaged
	@WebPopulate
	@XMLElement
	private boolean sendFlowInstanceExpiredManagerEmail;

	@DAOManaged
	@WebPopulate(maxLength = 255)
	@Templated
	@XMLElement
	private String flowInstanceExpiredManagerEmailSubject;

	@DAOManaged
	@WebPopulate(maxLength = 65536)
	@Templated
	@XMLElement
	private String flowInstanceExpiredManagerEmailMessage;

	@DAOManaged
	@WebPopulate
	@XMLElement
	private boolean sendExternalMessageReceivedGroupEmail;

	@DAOManaged
	@WebPopulate(maxLength = 255)
	@Templated
	@XMLElement
	private String externalMessageReceivedGroupEmailSubject;

	@DAOManaged
	@WebPopulate(maxLength = 65536)
	@Templated
	@XMLElement
	private String externalMessageReceivedGroupEmailMessage;

	@DAOManaged
	@WebPopulate
	@XMLElement
	private boolean sendFlowInstanceAssignedGroupEmail;

	@DAOManaged
	@WebPopulate(maxLength = 255)
	@Templated
	@XMLElement
	private String flowInstanceAssignedGroupEmailSubject;

	@DAOManaged
	@WebPopulate(maxLength = 65536)
	@Templated
	@XMLElement
	private String flowInstanceAssignedGroupEmailMessage;

	@DAOManaged
	@WebPopulate
	@XMLElement
	private boolean sendInternalMessageAddedGroupEmail;

	@DAOManaged
	@WebPopulate(maxLength = 255)
	@Templated
	@XMLElement
	private String internalMessageAddedGroupEmailSubject;

	@DAOManaged
	@WebPopulate(maxLength = 65536)
	@Templated
	@XMLElement
	private String internalMessageAddedGroupEmailMessage;

	@DAOManaged
	@WebPopulate
	@XMLElement
	private boolean sendStatusChangedManagerGroupEmail;

	@DAOManaged
	@WebPopulate(maxLength = 255)
	@Templated
	@XMLElement
	private String statusChangedManagerGroupEmailSubject;

	@DAOManaged
	@WebPopulate(maxLength = 65536)
	@Templated
	@XMLElement
	private String statusChangedManagerGroupEmailMessage;

	@DAOManaged
	@WebPopulate
	@XMLElement
	private boolean sendFlowInstanceSubmittedGlobalEmail;

	@DAOManaged
	@WebPopulate(maxLength = 255)
	@XMLElement
	private String flowInstanceSubmittedGlobalEmailSubject;

	@DAOManaged
	@WebPopulate(maxLength = 65536)
	@XMLElement
	private String flowInstanceSubmittedGlobalEmailMessage;

	@DAOManaged
	@OneToMany(autoAdd = true, autoGet = true, autoUpdate = true)
	@SimplifiedRelation(table = "flow_familiy_notification_setting_submitglobal", remoteValueColumnName = "email")
	@WebPopulate(maxLength = 255, populator = EmailAttributeTagPopulator.class)
	@RequiredIfSet(paramNames = "sendFlowInstanceSubmittedGlobalEmail")
	@SplitOnLineBreak
	@NoDuplicates(comparator = CaseInsensitiveStringComparator.class)
	@XMLElement(fixCase = true, childName = "address")
	private List<String> flowInstanceSubmittedGlobalEmailAddresses;

	@DAOManaged
	@WebPopulate
	@XMLElement
	private boolean flowInstanceSubmittedGlobalEmailAttachPDF;

	@DAOManaged
	@WebPopulate
	@XMLElement
	private boolean flowInstanceSubmittedGlobalEmailAttachXML;

	@DAOManaged
	@WebPopulate
	@PopulateOnlyIfSet(paramNames = "flowInstanceSubmittedGlobalEmailAttachPDF")
	@XMLElement
	private boolean flowInstanceSubmittedGlobalEmailAttachPDFAttachmentsSeparately;

	@DAOManaged
	@WebPopulate
	@XMLElement
	private boolean sendFlowInstanceAssignedGlobalEmail;

	@DAOManaged
	@WebPopulate(maxLength = 255)
	@Templated
	@XMLElement
	private String flowInstanceAssignedGlobalEmailSubject;

	@DAOManaged
	@WebPopulate(maxLength = 65536)
	@Templated
	@XMLElement
	private String flowInstanceAssignedGlobalEmailMessage;

	@DAOManaged
	@OneToMany(autoAdd = true, autoGet = true, autoUpdate = true)
	@SimplifiedRelation(table = "flow_familiy_notification_setting_assignedglobal", remoteValueColumnName = "email")
	@WebPopulate(maxLength = 255, populator = EmailAttributeTagPopulator.class)
	@RequiredIfSet(paramNames = "sendFlowInstanceAssignedGlobalEmail")
	@SplitOnLineBreak
	@NoDuplicates(comparator = CaseInsensitiveStringComparator.class)
	@XMLElement(fixCase = true, childName = "address")
	private List<String> flowInstanceAssignedGlobalEmailAddresses;

	@DAOManaged
	@WebPopulate
	@XMLElement
	private boolean sendFlowInstanceArchivedGlobalEmail;

	@DAOManaged
	@Templated
	@WebPopulate(maxLength = 255)
	@XMLElement
	private String flowInstanceArchivedGlobalEmailSubject;

	@DAOManaged
	@Templated
	@WebPopulate(maxLength = 65536)
	@XMLElement
	private String flowInstanceArchivedGlobalEmailMessage;

	@DAOManaged
	@Templated
	@OneToMany(autoAdd = true, autoGet = true, autoUpdate = true)
	@SimplifiedRelation(table = "flow_familiy_notification_setting_archivedglobal", remoteValueColumnName = "email")
	@WebPopulate(maxLength = 255, populator = EmailAttributeTagPopulator.class)
	@RequiredIfSet(paramNames = "sendFlowInstanceArchivedGlobalEmail")
	@SplitOnLineBreak
	@NoDuplicates(comparator = CaseInsensitiveStringComparator.class)
	@XMLElement(fixCase = true, childName = "address")
	private List<String> flowInstanceArchivedGlobalEmailAddresses;

	@DAOManaged
	@WebPopulate
	@XMLElement
	private boolean flowInstanceArchivedGlobalEmailAttachPDF;

	@DAOManaged
	@WebPopulate
	@PopulateOnlyIfSet(paramNames = "flowInstanceArchivedGlobalEmailAttachPDF")
	@XMLElement
	private boolean flowInstanceArchivedGlobalEmailAttachPDFAttachmentsSeparately;

	@DAOManaged
	@WebPopulate
	@XMLElement
	private boolean flowInstanceArchivedGlobalEmailAttachXML;

	@DAOManaged
	@WebPopulate
	@XMLElement
	private boolean sendExternalMessageReceivedGlobalEmail;

	@DAOManaged
	@OneToMany(autoAdd = true, autoGet = true, autoUpdate = true)
	@SimplifiedRelation(table = "flow_familiy_notification_setting_extmessageglobal", remoteValueColumnName = "email")
	@WebPopulate(maxLength = 255, populator = EmailAttributeTagPopulator.class)
	@RequiredIfSet(paramNames = "sendExternalMessageReceivedGlobalEmail")
	@SplitOnLineBreak
	@NoDuplicates(comparator = CaseInsensitiveStringComparator.class)
	@XMLElement(fixCase = true, childName = "address")
	private List<String> externalMessageReceivedGlobalEmailAddresses;

	@DAOManaged
	@WebPopulate
	@XMLElement
	private boolean sendInternalMessageReceivedGlobalEmail;

	@DAOManaged
	@OneToMany(autoAdd = true, autoGet = true, autoUpdate = true)
	@SimplifiedRelation(table = "flow_family_notification_setting_intmessageglobal", remoteValueColumnName = "email")
	@WebPopulate(maxLength = 255, populator = EmailAttributeTagPopulator.class)
	@RequiredIfSet(paramNames = "sendInternalMessageReceivedGlobalEmail")
	@SplitOnLineBreak
	@NoDuplicates(comparator = CaseInsensitiveStringComparator.class)
	@XMLElement(fixCase = true, childName = "address")
	private List<String> internalMessageReceivedGlobalEmailAddresses;

	@DAOManaged
	@WebPopulate
	@XMLElement
	private boolean sendManagerExpiredGlobalEmail;

	@DAOManaged
	@OneToMany(autoAdd = true, autoGet = true, autoUpdate = true)
	@SimplifiedRelation(table = "flow_familiy_notification_setting_managerexpiredglobal", remoteValueColumnName = "email")
	@WebPopulate(maxLength = 255, populator = EmailAttributeTagPopulator.class)
	@RequiredIfSet(paramNames = "sendManagerExpiredGlobalEmail")
	@SplitOnLineBreak
	@NoDuplicates(comparator = CaseInsensitiveStringComparator.class)
	@XMLElement(fixCase = true, childName = "address")
	private List<String> managerExpiredGlobalEmailAddresses;

	@DAOManaged
	@WebPopulate
	@XMLElement
	private boolean sendFlowInstanceExpiredGlobalEmail;

	@DAOManaged
	@Templated
	@WebPopulate(maxLength = 255)
	@XMLElement
	private String flowInstanceExpiredGlobalEmailSubject;

	@DAOManaged
	@Templated
	@WebPopulate(maxLength = 65536)
	@XMLElement
	private String flowInstanceExpiredGlobalEmailMessage;

	@DAOManaged
	@Templated
	@OneToMany(autoAdd = true, autoGet = true, autoUpdate = true)
	@SimplifiedRelation(table = "flow_familiy_notification_setting_expiredglobal", remoteValueColumnName = "email")
	@WebPopulate(maxLength = 255, populator = EmailAttributeTagPopulator.class)
	@RequiredIfSet(paramNames = "sendFlowInstanceExpiredGlobalEmail")
	@SplitOnLineBreak
	@NoDuplicates(comparator = CaseInsensitiveStringComparator.class)
	@XMLElement(fixCase = true, childName = "address")
	private List<String> flowInstanceExpiredGlobalEmailAddresses;

	@DAOManaged
	@Templated
	@WebPopulate(maxLength = 1024)
	@XMLElement
	private String flowInstanceMultiSignInitiatedUserSMS;

	@DAOManaged
	@Templated
	@WebPopulate(maxLength = 1024)
	@XMLElement
	private String flowInstanceMultiSignCanceledUserSMS;

	@DAOManaged
	@Templated
	@WebPopulate(maxLength = 1024)
	@XMLElement
	private String flowInstanceMultiSignCanceledOwnerSMS;

	@DAOManaged
	@Templated
	@WebPopulate(maxLength = 255)
	@XMLElement
	private String flowInstanceMultiSignInitiatedUserEmailSubject;

	@DAOManaged
	@Templated
	@WebPopulate(maxLength = 65536)
	@XMLElement
	private String flowInstanceMultiSignInitiatedUserEmailMessage;

	@DAOManaged
	@Templated
	@WebPopulate(maxLength = 255)
	@XMLElement
	private String flowInstanceMultiSignCanceledUserEmailSubject;

	@DAOManaged
	@Templated
	@WebPopulate(maxLength = 65536)
	@XMLElement
	private String flowInstanceMultiSignCanceledUserEmailMessage;

	@DAOManaged
	@Templated
	@WebPopulate(maxLength = 255)
	@XMLElement
	private String flowInstanceMultiSignCanceledOwnerEmailSubject;

	@DAOManaged
	@Templated
	@WebPopulate(maxLength = 65536)
	@XMLElement
	private String flowInstanceMultiSignCanceledOwnerEmailMessage;

	@DAOManaged
	@WebPopulate
	@XMLElement
	private boolean sendReadReceiptAddedManagerEmail;

	@DAOManaged
	@Templated
	@WebPopulate(maxLength = 255)
	@XMLElement
	private String readReceiptAddedManagerEmailSubject;

	@DAOManaged
	@Templated
	@WebPopulate(maxLength = 65536)
	@XMLElement
	private String readReceiptAddedManagerEmailMessage;

	@DAOManaged
	@WebPopulate
	@XMLElement
	private boolean sendReadReceiptAddedGroupEmail;

	@DAOManaged
	@Templated
	@WebPopulate(maxLength = 255)
	@XMLElement
	private String readReceiptAddedGroupEmailSubject;

	@DAOManaged
	@Templated
	@WebPopulate(maxLength = 65536)
	@XMLElement
	private String readReceiptAddedGroupEmailMessage;
	@DAOManaged
	@WebPopulate
	@XMLElement
	private boolean sendReadReceiptAddedGlobalEmail;

	@DAOManaged
	@Templated
	@WebPopulate(maxLength = 255)
	@XMLElement
	private String readReceiptAddedGlobalEmailSubject;

	@DAOManaged
	@Templated
	@WebPopulate(maxLength = 65536)
	@XMLElement
	private String readReceiptAddedGlobalEmailMessage;

	@DAOManaged
	@Templated
	@OneToMany(autoAdd = true, autoGet = true, autoUpdate = true)
	@SimplifiedRelation(table = "flow_familiy_notification_setting_readreceipt_addedglobal", remoteValueColumnName = "email")
	@WebPopulate(maxLength = 255, populator = EmailAttributeTagPopulator.class)
	@RequiredIfSet(paramNames = "sendReadReceiptAddedGlobalEmail")
	@SplitOnLineBreak
	@NoDuplicates(comparator = CaseInsensitiveStringComparator.class)
	@XMLElement(fixCase = true, childName = "address")
	private List<String> readReceiptAddedGlobalEmailAddresses;

	@DAOManaged
	@WebPopulate
	@XMLElement
	private boolean sendReadReceiptAttachmentDownloadedManagerEmail;

	@DAOManaged
	@Templated
	@WebPopulate(maxLength = 255)
	@XMLElement
	private String readReceiptAttachmentDownloadedManagerEmailSubject;

	@DAOManaged
	@Templated
	@WebPopulate(maxLength = 65536)
	@XMLElement
	private String readReceiptAttachmentDownloadedManagerEmailMessage;

	@DAOManaged
	@WebPopulate
	@XMLElement
	private boolean sendReadReceiptAttachmentDownloadedGroupEmail;

	@DAOManaged
	@Templated
	@WebPopulate(maxLength = 255)
	@XMLElement
	private String readReceiptAttachmentDownloadedGroupEmailSubject;

	@DAOManaged
	@Templated
	@WebPopulate(maxLength = 65536)
	@XMLElement
	private String readReceiptAttachmentDownloadedGroupEmailMessage;

	@DAOManaged
	@WebPopulate
	@XMLElement
	private boolean sendReadReceiptAttachmentDownloadedGlobalEmail;

	@DAOManaged
	@Templated
	@WebPopulate(maxLength = 255)
	@XMLElement
	private String readReceiptAttachmentDownloadedGlobalEmailSubject;

	@DAOManaged
	@Templated
	@WebPopulate(maxLength = 65536)
	@XMLElement
	private String readReceiptAttachmentDownloadedGlobalEmailMessage;

	@DAOManaged
	@Templated
	@OneToMany(autoAdd = true, autoGet = true, autoUpdate = true)
	@SimplifiedRelation(table = "flow_familiy_notification_setting_readreceipt_atch_dlglobal", remoteValueColumnName = "email")
	@WebPopulate(maxLength = 255, populator = EmailAttributeTagPopulator.class)
	@RequiredIfSet(paramNames = "sendReadReceiptAttachmentDownloadedGlobalEmail")
	@SplitOnLineBreak
	@NoDuplicates(comparator = CaseInsensitiveStringComparator.class)
	@XMLElement(fixCase = true, childName = "address")
	private List<String> readReceiptAttachmentDownloadedGlobalEmailAddresses;

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

	public String getStatusChangedManagerEmailSubject() {

		return statusChangedManagerEmailSubject;
	}

	public void setStatusChangedManagerEmailSubject(String statusChangedManagerEmailSubject) {

		this.statusChangedManagerEmailSubject = statusChangedManagerEmailSubject;
	}

	public String getStatusChangedManagerEmailMessage() {

		return statusChangedManagerEmailMessage;
	}

	public void setStatusChangedManagerEmailMessage(String statusChangedManagerEmailMessage) {

		this.statusChangedManagerEmailMessage = statusChangedManagerEmailMessage;
	}

	public boolean isSendFlowInstanceCompletionManagerEmail() {

		return sendFlowInstanceCompletionManagerEmail;
	}

	public void setSendFlowInstanceCompletionManagerEmail(boolean sendFlowInstanceCompletionManagerEmail) {

		this.sendFlowInstanceCompletionManagerEmail = sendFlowInstanceCompletionManagerEmail;
	}

	public String getManagerCompletionSubmittedEmailSubject() {

		return managerCompletionSubmittedEmailSubject;
	}

	public void setManagerCompletionSubmittedEmailSubject(String managerCompletionSubmittedEmailSubject) {

		this.managerCompletionSubmittedEmailSubject = managerCompletionSubmittedEmailSubject;
	}

	public String getManagerCompletionSubmittedEmailMessage() {

		return managerCompletionSubmittedEmailMessage;
	}

	public void setManagerCompletionSubmittedEmailMessage(String managerCompletionSubmittedEmailMessage) {

		this.managerCompletionSubmittedEmailMessage = managerCompletionSubmittedEmailMessage;
	}

	public boolean isSendFlowInstanceSubmittedManagerEmail() {

		return sendFlowInstanceSubmittedManagerEmail;
	}

	public void setSendFlowInstanceSubmittedManagerEmail(boolean sendFlowInstanceSubmittedManagerEmail) {

		this.sendFlowInstanceSubmittedManagerEmail = sendFlowInstanceSubmittedManagerEmail;
	}

	public String getFlowInstanceSubmittedManagerEmailSubject() {

		return flowInstanceSubmittedManagerEmailSubject;
	}

	public void setFlowInstanceSubmittedManagerEmailSubject(String flowInstanceSubmittedManagerEmailSubject) {

		this.flowInstanceSubmittedManagerEmailSubject = flowInstanceSubmittedManagerEmailSubject;
	}

	public String getFlowInstanceSubmittedManagerEmailMessage() {

		return flowInstanceSubmittedManagerEmailMessage;
	}

	public void setFlowInstanceSubmittedManagerEmailMessage(String flowInstanceSubmittedManagerEmailMessage) {

		this.flowInstanceSubmittedManagerEmailMessage = flowInstanceSubmittedManagerEmailMessage;
	}

	public boolean isSendFlowInstanceExpiredManagerEmail() {

		return sendFlowInstanceExpiredManagerEmail;
	}

	public void setSendFlowInstanceExpiredManagerEmail(boolean sendFlowInstanceExpiredManagerEmail) {

		this.sendFlowInstanceExpiredManagerEmail = sendFlowInstanceExpiredManagerEmail;
	}

	public String getFlowInstanceExpiredManagerEmailSubject() {

		return flowInstanceExpiredManagerEmailSubject;
	}

	public void setFlowInstanceExpiredManagerEmailSubject(String flowInstanceExpiredManagerEmailSubject) {

		this.flowInstanceExpiredManagerEmailSubject = flowInstanceExpiredManagerEmailSubject;
	}

	public String getFlowInstanceExpiredManagerEmailMessage() {

		return flowInstanceExpiredManagerEmailMessage;
	}

	public void setFlowInstanceExpiredManagerEmailMessage(String flowInstanceExpiredManagerEmailMessage) {

		this.flowInstanceExpiredManagerEmailMessage = flowInstanceExpiredManagerEmailMessage;
	}

	public boolean isSendFlowInstanceAssignedGroupEmail() {

		return sendFlowInstanceAssignedGroupEmail;
	}

	public void setSendFlowInstanceAssignedGroupEmail(boolean sendFlowInstanceAssignedGroupEmail) {

		this.sendFlowInstanceAssignedGroupEmail = sendFlowInstanceAssignedGroupEmail;
	}

	public String getFlowInstanceAssignedGroupEmailSubject() {

		return flowInstanceAssignedGroupEmailSubject;
	}

	public void setFlowInstanceAssignedGroupEmailSubject(String flowInstanceAssignedGroupEmailSubject) {

		this.flowInstanceAssignedGroupEmailSubject = flowInstanceAssignedGroupEmailSubject;
	}

	public String getFlowInstanceAssignedGroupEmailMessage() {

		return flowInstanceAssignedGroupEmailMessage;
	}

	public void setFlowInstanceAssignedGroupEmailMessage(String flowInstanceAssignedGroupEmailMessage) {

		this.flowInstanceAssignedGroupEmailMessage = flowInstanceAssignedGroupEmailMessage;
	}

	public boolean isSendExternalMessageReceivedGroupEmail() {

		return sendExternalMessageReceivedGroupEmail;
	}

	public void setSendExternalMessageReceivedGroupEmail(boolean sendExternalMessageReceivedGroupEmail) {

		this.sendExternalMessageReceivedGroupEmail = sendExternalMessageReceivedGroupEmail;
	}

	public String getExternalMessageReceivedGroupEmailSubject() {

		return externalMessageReceivedGroupEmailSubject;
	}

	public void setExternalMessageReceivedGroupEmailSubject(String externalMessageReceivedGroupEmailSubject) {

		this.externalMessageReceivedGroupEmailSubject = externalMessageReceivedGroupEmailSubject;
	}

	public String getExternalMessageReceivedGroupEmailMessage() {

		return externalMessageReceivedGroupEmailMessage;
	}

	public void setExternalMessageReceivedGroupEmailMessage(String externalMessageReceivedGroupEmailMessage) {

		this.externalMessageReceivedGroupEmailMessage = externalMessageReceivedGroupEmailMessage;
	}

	@Override
	public Element toXML(Document doc) {

		Element settingsElement = super.toXML(doc);

		if (sendStatusChangedUserSMS || sendExternalMessageReceivedUserSMS || sendFlowInstanceSubmittedUserSMS || sendFlowInstanceArchivedUserSMS || sendStatusChangedUserEmail || sendExternalMessageReceivedUserEmail || sendFlowInstanceSubmittedUserEmail || sendFlowInstanceArchivedUserEmail) {

			XMLUtils.appendNewElement(doc, settingsElement, "HasEnabledUserNotifications");
		}

		if (sendExternalMessageReceivedManagerEmail || sendFlowInstanceAssignedManagerEmail || sendStatusChangedManagerEmail || sendFlowInstanceSubmittedManagerEmail || sendInternalMessageAddedManagerEmail || sendFlowInstanceCompletionManagerEmail || sendFlowInstanceExpiredManagerEmail) {

			XMLUtils.appendNewElement(doc, settingsElement, "HasEnabledManagerNotifications");
		}

		if (sendExternalMessageReceivedGroupEmail || sendFlowInstanceAssignedGroupEmail || sendInternalMessageAddedGroupEmail || sendStatusChangedManagerGroupEmail) {

			XMLUtils.appendNewElement(doc, settingsElement, "HasEnabledGroupNotifications");
		}

		if (sendFlowInstanceSubmittedGlobalEmail || sendFlowInstanceAssignedGlobalEmail || sendExternalMessageReceivedGlobalEmail || sendInternalMessageReceivedGlobalEmail || sendManagerExpiredGlobalEmail || sendFlowInstanceArchivedGlobalEmail || sendFlowInstanceExpiredGlobalEmail) {

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

	public boolean isFlowInstanceSubmittedGlobalEmailAttachXML() {

		return flowInstanceSubmittedGlobalEmailAttachXML;
	}

	public void setFlowInstanceSubmittedGlobalEmailAttachXML(boolean flowInstanceSubmittedGlobalEmailAttachXML) {

		this.flowInstanceSubmittedGlobalEmailAttachXML = flowInstanceSubmittedGlobalEmailAttachXML;
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

	public boolean isSendFlowInstanceAssignedGlobalEmail() {

		return sendFlowInstanceAssignedGlobalEmail;
	}

	public void setSendFlowInstanceAssignedGlobalEmail(boolean sendFlowInstanceAssignedGlobalEmail) {

		this.sendFlowInstanceAssignedGlobalEmail = sendFlowInstanceAssignedGlobalEmail;
	}

	public String getFlowInstanceAssignedGlobalEmailSubject() {

		return flowInstanceAssignedGlobalEmailSubject;
	}

	public void setFlowInstanceAssignedGlobalEmailSubject(String flowInstanceAssignedGlobalEmailSubject) {

		this.flowInstanceAssignedGlobalEmailSubject = flowInstanceAssignedGlobalEmailSubject;
	}

	public String getFlowInstanceAssignedGlobalEmailMessage() {

		return flowInstanceAssignedGlobalEmailMessage;
	}

	public void setFlowInstanceAssignedGlobalEmailMessage(String flowInstanceAssignedGlobalEmailMessage) {

		this.flowInstanceAssignedGlobalEmailMessage = flowInstanceAssignedGlobalEmailMessage;
	}

	public List<String> getFlowInstanceAssignedGlobalEmailAddresses() {

		return flowInstanceAssignedGlobalEmailAddresses;
	}

	public void setFlowInstanceAssignedGlobalEmailAddresses(List<String> flowInstanceAssignedGlobalEmailAddresses) {

		this.flowInstanceAssignedGlobalEmailAddresses = flowInstanceAssignedGlobalEmailAddresses;
	}

	public boolean isSendFlowInstanceArchivedGlobalEmail() {

		return sendFlowInstanceArchivedGlobalEmail;
	}

	public void setSendFlowInstanceArchivedGlobalEmail(boolean sendFlowInstanceArchivedGlobalEmail) {

		this.sendFlowInstanceArchivedGlobalEmail = sendFlowInstanceArchivedGlobalEmail;
	}

	public String getFlowInstanceArchivedGlobalEmailSubject() {

		return flowInstanceArchivedGlobalEmailSubject;
	}

	public void setFlowInstanceArchivedGlobalEmailSubject(String flowInstanceArchivedGlobalEmailSubject) {

		this.flowInstanceArchivedGlobalEmailSubject = flowInstanceArchivedGlobalEmailSubject;
	}

	public String getFlowInstanceArchivedGlobalEmailMessage() {

		return flowInstanceArchivedGlobalEmailMessage;
	}

	public void setFlowInstanceArchivedGlobalEmailMessage(String flowInstanceArchivedGlobalEmailMessage) {

		this.flowInstanceArchivedGlobalEmailMessage = flowInstanceArchivedGlobalEmailMessage;
	}

	public List<String> getFlowInstanceArchivedGlobalEmailAddresses() {

		return flowInstanceArchivedGlobalEmailAddresses;
	}

	public void setFlowInstanceArchivedGlobalEmailAddresses(List<String> flowInstanceArchivedGlobalEmailAddresses) {

		this.flowInstanceArchivedGlobalEmailAddresses = flowInstanceArchivedGlobalEmailAddresses;
	}

	public boolean isFlowInstanceArchivedGlobalEmailAttachPDF() {

		return flowInstanceArchivedGlobalEmailAttachPDF;
	}

	public boolean isArchivedGlobalEmailPDFAttachmentSeparated() {

		return flowInstanceArchivedGlobalEmailAttachPDFAttachmentsSeparately;
	}

	public void setFlowInstanceArchivedGlobalEmailAttachPDFAttachmentsSeparately(boolean flowInstanceArchivedGlobalEmailAttachPDFAttachmentsSeparately) {

		this.flowInstanceArchivedGlobalEmailAttachPDFAttachmentsSeparately = flowInstanceArchivedGlobalEmailAttachPDFAttachmentsSeparately;
	}

	public void setFlowInstanceArchivedGlobalEmailAttachPDF(boolean flowInstanceArchivedGlobalEmailAttachPDF) {

		this.flowInstanceArchivedGlobalEmailAttachPDF = flowInstanceArchivedGlobalEmailAttachPDF;
	}

	public boolean isFlowInstanceArchivedGlobalEmailAttachXML() {

		return flowInstanceArchivedGlobalEmailAttachXML;
	}

	public void setFlowInstanceArchivedGlobalEmailAttachXML(boolean flowInstanceArchivedGlobalEmailAttachXML) {

		this.flowInstanceArchivedGlobalEmailAttachXML = flowInstanceArchivedGlobalEmailAttachXML;
	}

	public List<String> getExternalMessageReceivedGlobalEmailAddresses() {

		return externalMessageReceivedGlobalEmailAddresses;
	}

	public void setExternalMessageReceivedGlobalEmailAddresses(List<String> externalMessageReceivedGlobalEmailAddresses) {

		this.externalMessageReceivedGlobalEmailAddresses = externalMessageReceivedGlobalEmailAddresses;
	}

	public List<String> getInternalMessageReceivedGlobalEmailAddresses() {

		return internalMessageReceivedGlobalEmailAddresses;
	}

	public void setInternalMessageReceivedGlobalEmailAddresses(List<String> internalMessageReceivedGlobalEmailAddresses) {

		this.internalMessageReceivedGlobalEmailAddresses = internalMessageReceivedGlobalEmailAddresses;
	}

	public boolean isSendExternalMessageReceivedGlobalEmail() {

		return sendExternalMessageReceivedGlobalEmail;
	}

	public void setSendExternalMessageReceivedGlobalEmail(boolean sendExternalMessageReceivedGlobalEmail) {

		this.sendExternalMessageReceivedGlobalEmail = sendExternalMessageReceivedGlobalEmail;
	}

	public boolean isSendInternalMessageReceivedGlobalEmail() {

		return sendInternalMessageReceivedGlobalEmail;
	}

	public void setSendInternalMessageReceivedGlobalEmail(boolean sendInternalMessageReceivedGlobalEmail) {

		this.sendInternalMessageReceivedGlobalEmail = sendInternalMessageReceivedGlobalEmail;
	}

	public String getStatusChangedUserEmailSubject() {

		return statusChangedUserEmailSubject;
	}

	public void setStatusChangedUserEmailSubject(String statusChangedUserEmailSubject) {

		this.statusChangedUserEmailSubject = statusChangedUserEmailSubject;
	}

	public String getStatusChangedUserEmailMessage() {

		return statusChangedUserEmailMessage;
	}

	public void setStatusChangedUserEmailMessage(String statusChangedUserEmailMessage) {

		this.statusChangedUserEmailMessage = statusChangedUserEmailMessage;
	}

	public String getExternalMessageReceivedUserEmailSubject() {

		return externalMessageReceivedUserEmailSubject;
	}

	public void setExternalMessageReceivedUserEmailSubject(String externalMessageReceivedUserEmailSubject) {

		this.externalMessageReceivedUserEmailSubject = externalMessageReceivedUserEmailSubject;
	}

	public String getExternalMessageReceivedUserEmailMessage() {

		return externalMessageReceivedUserEmailMessage;
	}

	public void setExternalMessageReceivedUserEmailMessage(String externalMessageReceivedUserEmailMessage) {

		this.externalMessageReceivedUserEmailMessage = externalMessageReceivedUserEmailMessage;
	}

	public String getExternalMessageReceivedManagerSubject() {

		return externalMessageReceivedManagerSubject;
	}

	public void setExternalMessageReceivedManagerSubject(String externalMessageReceivedManagerSubject) {

		this.externalMessageReceivedManagerSubject = externalMessageReceivedManagerSubject;
	}

	public String getExternalMessageReceivedManagerMessage() {

		return externalMessageReceivedManagerMessage;
	}

	public void setExternalMessageReceivedManagerMessage(String externalMessageReceivedManagerMessage) {

		this.externalMessageReceivedManagerMessage = externalMessageReceivedManagerMessage;
	}

	public boolean isSendManagerExpiredGlobalEmail() {

		return sendManagerExpiredGlobalEmail;
	}

	public void setSendManagerExpiredGlobalEmail(boolean sendManagerExpiredGlobalEmail) {

		this.sendManagerExpiredGlobalEmail = sendManagerExpiredGlobalEmail;
	}

	public List<String> getManagerExpiredGlobalEmailAddresses() {

		return managerExpiredGlobalEmailAddresses;
	}

	public void setManagerExpiredGlobalEmailAddresses(List<String> managerExpiredGlobalEmailAddresses) {

		this.managerExpiredGlobalEmailAddresses = managerExpiredGlobalEmailAddresses;
	}

	public boolean isSendFlowInstanceExpiredGlobalEmail() {

		return sendFlowInstanceExpiredGlobalEmail;
	}

	public void setSendFlowInstanceExpiredGlobalEmail(boolean sendFlowInstanceExpiredGlobalEmail) {

		this.sendFlowInstanceExpiredGlobalEmail = sendFlowInstanceExpiredGlobalEmail;
	}

	public String getFlowInstanceExpiredGlobalEmailSubject() {

		return flowInstanceExpiredGlobalEmailSubject;
	}

	public void setFlowInstanceExpiredGlobalEmailSubject(String flowInstanceExpiredGlobalEmailSubject) {

		this.flowInstanceExpiredGlobalEmailSubject = flowInstanceExpiredGlobalEmailSubject;
	}

	public String getFlowInstanceExpiredGlobalEmailMessage() {

		return flowInstanceExpiredGlobalEmailMessage;
	}

	public void setFlowInstanceExpiredGlobalEmailMessage(String flowInstanceExpiredGlobalEmailMessage) {

		this.flowInstanceExpiredGlobalEmailMessage = flowInstanceExpiredGlobalEmailMessage;
	}

	public List<String> getFlowInstanceExpiredGlobalEmailAddresses() {

		return flowInstanceExpiredGlobalEmailAddresses;
	}

	public void setFlowInstanceExpiredGlobalEmailAddresses(List<String> flowInstanceExpiredGlobalEmailAddresses) {

		this.flowInstanceExpiredGlobalEmailAddresses = flowInstanceExpiredGlobalEmailAddresses;
	}

	public String getFlowInstanceSubmittedUserSMS() {

		return flowInstanceSubmittedUserSMS;
	}

	public void setFlowInstanceSubmittedUserSMS(String flowInstanceSubmittedUserSMS) {

		this.flowInstanceSubmittedUserSMS = flowInstanceSubmittedUserSMS;
	}

	public String getFlowInstanceSubmittedNotLoggedInUserSMS() {

		return flowInstanceSubmittedNotLoggedInUserSMS;
	}

	public void setFlowInstanceSubmittedNotLoggedInUserSMS(String flowInstanceSubmittedNotLoggedInUserSMS) {

		this.flowInstanceSubmittedNotLoggedInUserSMS = flowInstanceSubmittedNotLoggedInUserSMS;
	}

	public String getStatusChangedUserSMS() {

		return statusChangedUserSMS;
	}

	public void setStatusChangedUserSMS(String statusChangedUserSMS) {

		this.statusChangedUserSMS = statusChangedUserSMS;
	}

	public String getExternalMessageReceivedUserSMS() {

		return externalMessageReceivedUserSMS;
	}

	public void setExternalMessageReceivedUserSMS(String externalMessageReceivedUserSMS) {

		this.externalMessageReceivedUserSMS = externalMessageReceivedUserSMS;
	}

	public String getFlowInstanceArchivedUserSMS() {

		return flowInstanceArchivedUserSMS;
	}

	public void setFlowInstanceArchivedUserSMS(String flowInstanceArchivedUserSMS) {

		this.flowInstanceArchivedUserSMS = flowInstanceArchivedUserSMS;
	}

	public String getFlowInstanceArchivedNotLoggedInUserSMS() {

		return flowInstanceArchivedNotLoggedInUserSMS;
	}

	public void setFlowInstanceArchivedNotLoggedInUserSMS(String flowInstanceArchivedNotLoggedInUserSMS) {

		this.flowInstanceArchivedNotLoggedInUserSMS = flowInstanceArchivedNotLoggedInUserSMS;
	}

	public String getFlowInstanceAssignedManagerEmailSubject() {

		return flowInstanceAssignedManagerEmailSubject;
	}

	public void setFlowInstanceAssignedManagerEmailSubject(String flowInstanceAssignedManagerSubject) {

		this.flowInstanceAssignedManagerEmailSubject = flowInstanceAssignedManagerSubject;
	}

	public String getFlowInstanceAssignedManagerEmailMessage() {

		return flowInstanceAssignedManagerEmailMessage;
	}

	public void setFlowInstanceAssignedManagerEmailMessage(String flowInstanceAssignedManagerMessage) {

		this.flowInstanceAssignedManagerEmailMessage = flowInstanceAssignedManagerMessage;
	}

	public boolean isSendInternalMessageAddedManagerEmail() {

		return sendInternalMessageAddedManagerEmail;
	}

	public void setSendInternalMessageAddedManagerEmail(boolean sendInternalMessageAddedManagerEmail) {

		this.sendInternalMessageAddedManagerEmail = sendInternalMessageAddedManagerEmail;
	}

	public String getInternalMessageAddedManagerEmailSubject() {

		return internalMessageAddedManagerEmailSubject;
	}

	public void setInternalMessageAddedManagerEmailSubject(String internalMessageAddedManagerEmailSubject) {

		this.internalMessageAddedManagerEmailSubject = internalMessageAddedManagerEmailSubject;
	}

	public String getInternalMessageAddedManagerEmailMessage() {

		return internalMessageAddedManagerEmailMessage;
	}

	public void setInternalMessageAddedManagerEmailMessage(String internalMessageAddedManagerEmailMessage) {

		this.internalMessageAddedManagerEmailMessage = internalMessageAddedManagerEmailMessage;
	}

	public String getFlowInstanceMultiSignInitiatedUserSMS() {

		return flowInstanceMultiSignInitiatedUserSMS;
	}

	public void setFlowInstanceMultiSignInitiatedUserSMS(String flowInstanceMultiSignInitiatedUserSMS) {

		this.flowInstanceMultiSignInitiatedUserSMS = flowInstanceMultiSignInitiatedUserSMS;
	}

	public String getFlowInstanceMultiSignInitiatedUserEmailSubject() {

		return flowInstanceMultiSignInitiatedUserEmailSubject;
	}

	public void setFlowInstanceMultiSignInitiatedUserEmailSubject(String flowInstanceMultiSignInitiatedUserEmailSubject) {

		this.flowInstanceMultiSignInitiatedUserEmailSubject = flowInstanceMultiSignInitiatedUserEmailSubject;
	}

	public String getFlowInstanceMultiSignInitiatedUserEmailMessage() {

		return flowInstanceMultiSignInitiatedUserEmailMessage;
	}

	public void setFlowInstanceMultiSignInitiatedUserEmailMessage(String flowInstanceMultiSignInitiatedUserEmailMessage) {

		this.flowInstanceMultiSignInitiatedUserEmailMessage = flowInstanceMultiSignInitiatedUserEmailMessage;
	}

	public String getFlowInstanceMultiSignCanceledUserSMS() {

		return flowInstanceMultiSignCanceledUserSMS;
	}

	public void setFlowInstanceMultiSignCanceledUserSMS(String flowInstanceMultiSignCanceledUserSMS) {

		this.flowInstanceMultiSignCanceledUserSMS = flowInstanceMultiSignCanceledUserSMS;
	}

	public String getFlowInstanceMultiSignCanceledUserEmailSubject() {

		return flowInstanceMultiSignCanceledUserEmailSubject;
	}

	public void setFlowInstanceMultiSignCanceledUserEmailSubject(String flowInstanceMultiSignCanceledUserEmailSubject) {

		this.flowInstanceMultiSignCanceledUserEmailSubject = flowInstanceMultiSignCanceledUserEmailSubject;
	}

	public String getFlowInstanceMultiSignCanceledUserEmailMessage() {

		return flowInstanceMultiSignCanceledUserEmailMessage;
	}

	public void setFlowInstanceMultiSignCanceledUserEmailMessage(String flowInstanceMultiSignCanceledUserEmailMessage) {

		this.flowInstanceMultiSignCanceledUserEmailMessage = flowInstanceMultiSignCanceledUserEmailMessage;
	}

	public String getFlowInstanceMultiSignCanceledOwnerSMS() {

		return flowInstanceMultiSignCanceledOwnerSMS;
	}

	public void setFlowInstanceMultiSignCanceledOwnerSMS(String flowInstanceMultiSignCanceledOwnerSMS) {

		this.flowInstanceMultiSignCanceledOwnerSMS = flowInstanceMultiSignCanceledOwnerSMS;
	}

	public String getFlowInstanceMultiSignCanceledOwnerEmailSubject() {

		return flowInstanceMultiSignCanceledOwnerEmailSubject;
	}

	public void setFlowInstanceMultiSignCanceledOwnerEmailSubject(String flowInstanceMultiSignCanceledOwnerEmailSubject) {

		this.flowInstanceMultiSignCanceledOwnerEmailSubject = flowInstanceMultiSignCanceledOwnerEmailSubject;
	}

	public String getFlowInstanceMultiSignCanceledOwnerEmailMessage() {

		return flowInstanceMultiSignCanceledOwnerEmailMessage;
	}

	public void setFlowInstanceMultiSignCanceledOwnerEmailMessage(String flowInstanceMultiSignCanceledOwnerEmailMessage) {

		this.flowInstanceMultiSignCanceledOwnerEmailMessage = flowInstanceMultiSignCanceledOwnerEmailMessage;
	}

	public boolean isSendInternalMessageAddedGroupEmail() {

		return sendInternalMessageAddedGroupEmail;
	}

	public void setSendInternalMessageAddedGroupEmail(boolean sendInternalMessageAddedGroupEmail) {

		this.sendInternalMessageAddedGroupEmail = sendInternalMessageAddedGroupEmail;
	}

	public String getInternalMessageAddedGroupEmailSubject() {

		return internalMessageAddedGroupEmailSubject;
	}

	public void setInternalMessageAddedGroupEmailSubject(String internalMessageAddedGroupEmailSubject) {

		this.internalMessageAddedGroupEmailSubject = internalMessageAddedGroupEmailSubject;
	}

	public String getInternalMessageAddedGroupEmailMessage() {

		return internalMessageAddedGroupEmailMessage;
	}

	public void setInternalMessageAddedGroupEmailMessage(String internalMessageAddedGroupEmailMessage) {

		this.internalMessageAddedGroupEmailMessage = internalMessageAddedGroupEmailMessage;
	}

	public boolean isSendStatusChangedManagerGroupEmail() {

		return sendStatusChangedManagerGroupEmail;
	}

	public void setSendStatusChangedManagerGroupEmail(boolean sendStatusChangedManagerGroupEmail) {

		this.sendStatusChangedManagerGroupEmail = sendStatusChangedManagerGroupEmail;
	}

	public String getStatusChangedManagerGroupEmailSubject() {

		return statusChangedManagerGroupEmailSubject;
	}

	public void setStatusChangedManagerGroupEmailSubject(String statusChangedManagerGroupEmailSubject) {

		this.statusChangedManagerGroupEmailSubject = statusChangedManagerGroupEmailSubject;
	}

	public String getStatusChangedManagerGroupEmailMessage() {

		return statusChangedManagerGroupEmailMessage;
	}

	public void setStatusChangedManagerGroupEmailMessage(String statusChangedManagerGroupEmailMessage) {

		this.statusChangedManagerGroupEmailMessage = statusChangedManagerGroupEmailMessage;
	}

	public boolean isSendReadReceiptAddedManagerEmail() {

		return sendReadReceiptAddedManagerEmail;
	}

	public void setSendReadReceiptAddedManagerEmail(boolean sendReadReceiptAddedManagerEmail) {

		this.sendReadReceiptAddedManagerEmail = sendReadReceiptAddedManagerEmail;
	}

	public String getReadReceiptAddedManagerEmailSubject() {

		return readReceiptAddedManagerEmailSubject;
	}

	public void setReadReceiptAddedManagerEmailSubject(String readReceiptAddedManagerEmailSubject) {

		this.readReceiptAddedManagerEmailSubject = readReceiptAddedManagerEmailSubject;
	}

	public String getReadReceiptAddedManagerEmailMessage() {

		return readReceiptAddedManagerEmailMessage;
	}

	public void setReadReceiptAddedManagerEmailMessage(String readReceiptAddedManagerEmailMessage) {

		this.readReceiptAddedManagerEmailMessage = readReceiptAddedManagerEmailMessage;
	}

	public boolean isSendReadReceiptAddedGroupEmail() {

		return sendReadReceiptAddedGroupEmail;
	}

	public void setSendReadReceiptAddedGroupEmail(boolean sendReadReceiptAddedGroupEmail) {

		this.sendReadReceiptAddedGroupEmail = sendReadReceiptAddedGroupEmail;
	}

	public String getReadReceiptAddedGroupEmailSubject() {

		return readReceiptAddedGroupEmailSubject;
	}

	public void setReadReceiptAddedGroupEmailSubject(String readReceiptAddedGroupEmailSubject) {

		this.readReceiptAddedGroupEmailSubject = readReceiptAddedGroupEmailSubject;
	}

	public String getReadReceiptAddedGroupEmailMessage() {

		return readReceiptAddedGroupEmailMessage;
	}

	public void setReadReceiptAddedGroupEmailMessage(String readReceiptAddedGroupEmailMessage) {

		this.readReceiptAddedGroupEmailMessage = readReceiptAddedGroupEmailMessage;
	}

	public boolean isSendReadReceiptAddedGlobalEmail() {

		return sendReadReceiptAddedGlobalEmail;
	}

	public void setSendReadReceiptAddedGlobalEmail(boolean sendReadReceiptAddedGlobalEmail) {

		this.sendReadReceiptAddedGlobalEmail = sendReadReceiptAddedGlobalEmail;
	}

	public String getReadReceiptAddedGlobalEmailSubject() {

		return readReceiptAddedGlobalEmailSubject;
	}

	public void setReadReceiptAddedGlobalEmailSubject(String readReceiptAddedGlobalEmailSubject) {

		this.readReceiptAddedGlobalEmailSubject = readReceiptAddedGlobalEmailSubject;
	}

	public String getReadReceiptAddedGlobalEmailMessage() {

		return readReceiptAddedGlobalEmailMessage;
	}

	public void setReadReceiptAddedGlobalEmailMessage(String readReceiptAddedGlobalEmailMessage) {

		this.readReceiptAddedGlobalEmailMessage = readReceiptAddedGlobalEmailMessage;
	}

	public List<String> getReadReceiptAddedGlobalEmailAddresses() {

		return readReceiptAddedGlobalEmailAddresses;
	}

	public void setReadReceiptAddedGlobalEmailAddresses(List<String> readReceiptAddedGlobalEmailAddresses) {

		this.readReceiptAddedGlobalEmailAddresses = readReceiptAddedGlobalEmailAddresses;
	}

	public boolean isSendReadReceiptAttachmentDownloadedManagerEmail() {

		return sendReadReceiptAttachmentDownloadedManagerEmail;
	}

	public void setSendReadReceiptAttachmentDownloadedManagerEmail(boolean sendReadReceiptAttachmentDownloadedManagerEmail) {

		this.sendReadReceiptAttachmentDownloadedManagerEmail = sendReadReceiptAttachmentDownloadedManagerEmail;
	}

	public String getReadReceiptAttachmentDownloadedManagerEmailSubject() {

		return readReceiptAttachmentDownloadedManagerEmailSubject;
	}

	public void setReadReceiptAttachmentDownloadedManagerEmailSubject(String readReceiptAttachmentDownloadedManagerEmailSubject) {

		this.readReceiptAttachmentDownloadedManagerEmailSubject = readReceiptAttachmentDownloadedManagerEmailSubject;
	}

	public String getReadReceiptAttachmentDownloadedManagerEmailMessage() {

		return readReceiptAttachmentDownloadedManagerEmailMessage;
	}

	public void setReadReceiptAttachmentDownloadedManagerEmailMessage(String readReceiptAttachmentDownloadedManagerEmailMessage) {

		this.readReceiptAttachmentDownloadedManagerEmailMessage = readReceiptAttachmentDownloadedManagerEmailMessage;
	}

	public boolean isSendReadReceiptAttachmentDownloadedGroupEmail() {

		return sendReadReceiptAttachmentDownloadedGroupEmail;
	}

	public void setSendReadReceiptAttachmentDownloadedGroupEmail(boolean sendReadReceiptAttachmentDownloadedGroupEmail) {

		this.sendReadReceiptAttachmentDownloadedGroupEmail = sendReadReceiptAttachmentDownloadedGroupEmail;
	}

	public String getReadReceiptAttachmentDownloadedGroupEmailSubject() {

		return readReceiptAttachmentDownloadedGroupEmailSubject;
	}

	public void setReadReceiptAttachmentDownloadedGroupEmailSubject(String readReceiptAttachmentDownloadedGroupEmailSubject) {

		this.readReceiptAttachmentDownloadedGroupEmailSubject = readReceiptAttachmentDownloadedGroupEmailSubject;
	}

	public String getReadReceiptAttachmentDownloadedGroupEmailMessage() {

		return readReceiptAttachmentDownloadedGroupEmailMessage;
	}

	public void setReadReceiptAttachmentDownloadedGroupEmailMessage(String readReceiptAttachmentDownloadedGroupEmailMessage) {

		this.readReceiptAttachmentDownloadedGroupEmailMessage = readReceiptAttachmentDownloadedGroupEmailMessage;
	}

	public boolean isSendReadReceiptAttachmentDownloadedGlobalEmail() {

		return sendReadReceiptAttachmentDownloadedGlobalEmail;
	}

	public void setSendReadReceiptAttachmentDownloadedGlobalEmail(boolean sendReadReceiptAttachmentDownloadedGlobalEmail) {

		this.sendReadReceiptAttachmentDownloadedGlobalEmail = sendReadReceiptAttachmentDownloadedGlobalEmail;
	}

	public String getReadReceiptAttachmentDownloadedGlobalEmailSubject() {

		return readReceiptAttachmentDownloadedGlobalEmailSubject;
	}

	public void setReadReceiptAttachmentDownloadedGlobalEmailSubject(String readReceiptAttachmentDownloadedGlobalEmailSubject) {

		this.readReceiptAttachmentDownloadedGlobalEmailSubject = readReceiptAttachmentDownloadedGlobalEmailSubject;
	}

	public String getReadReceiptAttachmentDownloadedGlobalEmailMessage() {

		return readReceiptAttachmentDownloadedGlobalEmailMessage;
	}

	public void setReadReceiptAttachmentDownloadedGlobalEmailMessage(String readReceiptAttachmentDownloadedGlobalEmailMessage) {

		this.readReceiptAttachmentDownloadedGlobalEmailMessage = readReceiptAttachmentDownloadedGlobalEmailMessage;
	}

	public List<String> getReadReceiptAttachmentDownloadedGlobalEmailAddresses() {

		return readReceiptAttachmentDownloadedGlobalEmailAddresses;
	}

	public void setReadReceiptAttachmentDownloadedGlobalEmailAddresses(List<String> readReceiptAttachmentDownloadedGlobalEmailAddresses) {

		this.readReceiptAttachmentDownloadedGlobalEmailAddresses = readReceiptAttachmentDownloadedGlobalEmailAddresses;
	}

}
