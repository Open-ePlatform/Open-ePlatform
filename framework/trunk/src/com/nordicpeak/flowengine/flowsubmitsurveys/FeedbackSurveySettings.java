package com.nordicpeak.flowengine.flowsubmitsurveys;

import java.sql.Timestamp;
import java.util.List;

import se.unlogic.emailutils.populators.EmailPopulator;
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

@Table(name = "feedback_flow_submit_survey_settings")
@XMLElement
public class FeedbackSurveySettings extends GeneratedElementable {
	
	@DAOManaged
	@Key
	@XMLElement
	private Integer flowFamilyID;
	
	@DAOManaged
	@XMLElement
	private Timestamp sendEmail;
	
	@DAOManaged
	@OneToMany(autoAdd = true, autoGet = true, autoUpdate = true)
	@SimplifiedRelation(table = "feedback_flow_submit_survey_settings_email", remoteValueColumnName = "email")
	@WebPopulate(maxLength = 255, populator = EmailPopulator.class)
	@RequiredIfSet(paramNames = "sendEmail")
	@SplitOnLineBreak
	@XMLElement(fixCase = true, childName = "address")
	private List<String> notificationEmailAddresses;


	
	public Integer getFlowFamilyID() {
	
		return flowFamilyID;
	}


	
	public void setFlowFamilyID(Integer flowFamilyID) {
	
		this.flowFamilyID = flowFamilyID;
	}


	
	public Timestamp isSendEmail() {
	
		return sendEmail;
	}


	
	public void setSendEmail(Timestamp sendEmail) {
	
		this.sendEmail = sendEmail;
	}


	
	public List<String> getNotificationEmailAddresses() {
	
		return notificationEmailAddresses;
	}


	
	public void setNotificationEmailAddresses(List<String> notificationEmailAddresses) {
	
		this.notificationEmailAddresses = notificationEmailAddresses;
	}
	
	
	
}
