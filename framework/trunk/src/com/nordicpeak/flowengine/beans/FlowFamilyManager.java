package com.nordicpeak.flowengine.beans;

import java.io.Serializable;
import java.sql.Date;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.standardutils.dao.annotations.DAOManaged;
import se.unlogic.standardutils.dao.annotations.Key;
import se.unlogic.standardutils.dao.annotations.ManyToOne;
import se.unlogic.standardutils.dao.annotations.Table;
import se.unlogic.standardutils.date.DateStringyfier;
import se.unlogic.standardutils.date.DateUtils;
import se.unlogic.standardutils.xml.GeneratedElementable;
import se.unlogic.standardutils.xml.XMLElement;
import se.unlogic.standardutils.xml.XMLUtils;

@Table(name = "flowengine_flow_family_manager_users")
@XMLElement
public class FlowFamilyManager extends GeneratedElementable implements Serializable {
	
	private static final long serialVersionUID = -3065392853394817211L;
	
	@DAOManaged(columnName = "flowFamilyID")
	@Key
	@ManyToOne
	private FlowFamily flowFamily;
	
	@DAOManaged
	@Key
	@XMLElement
	private Integer userID;
	
	@DAOManaged
	@XMLElement(valueFormatter = DateStringyfier.class)
	private Date validFromDate;
	
	@DAOManaged
	@XMLElement(valueFormatter = DateStringyfier.class)
	private Date validToDate;
	
	@DAOManaged
	@XMLElement
	private boolean restricted;
	
	/** Only for display */
	@XMLElement
	private User user;
	
	public FlowFamilyManager() {}
	
	public FlowFamilyManager(User user) {
		
		userID = user.getUserID();
	}
	
	public FlowFamily getFlowFamily() {
		return flowFamily;
	}
	
	public void setFlowFamily(FlowFamily flowFamily) {
		this.flowFamily = flowFamily;
	}
	
	public Integer getUserID() {
		return userID;
	}
	
	public void setUserID(Integer userID) {
		this.userID = userID;
	}
	
	/** Only for display */
	public void setUser(User user) {
		this.user = user;
	}
	
	/** Only for display */
	public User getUser() {
		return user;
	}

	public Date getValidFromDate() {
		return validFromDate;
	}
	
	public void setValidFromDate(Date validFromDate) {
		this.validFromDate = validFromDate;
	}
	
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	/** Inclusive */
	public Date getValidToDate() {
		return validToDate;
	}
	
	public void setValidToDate(Date validToDate) {
		this.validToDate = validToDate;
	}
	
	public boolean isActive() {
		
		Date startOfToday = DateUtils.setTimeToMidnight(new Date(System.currentTimeMillis()));
		
		if (validFromDate != null && validFromDate.compareTo(startOfToday) > 0) {
			return false;
		}
		
		if (validToDate != null && validToDate.compareTo(startOfToday) < 0) {
			return false;
		}
		
		return true;
	}
	
	public boolean isRestricted() {
		return restricted;
	}
	
	public void setRestricted(boolean restricted) {
		this.restricted = restricted;
	}
	
	@Override
	public Element toXML(Document doc) {

		Element element = super.toXML(doc);
		
		if (isActive()) {
			
			XMLUtils.appendNewElement(doc, element, "Active", true);
		}
		
		return element;
	}
	
	@Override
	public String toString() {
		
		if (user != null) {
			
			return user + " (restricted=" + restricted + ", validFromDate=" + validFromDate + ", validToDate=" + validToDate + ")";
			
		} else {
			
			return "userID " + userID + " (restricted=" + restricted + ", validFromDate=" + validFromDate + ", validToDate=" + validToDate + ")";
		}
	}
}
