package com.nordicpeak.flowengine.flowapprovalmodule.beans;

import java.io.Serializable;

import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.standardutils.dao.annotations.DAOManaged;
import se.unlogic.standardutils.dao.annotations.Key;
import se.unlogic.standardutils.dao.annotations.ManyToOne;
import se.unlogic.standardutils.dao.annotations.Table;
import se.unlogic.standardutils.xml.GeneratedElementable;
import se.unlogic.standardutils.xml.XMLElement;

@Table(name = "flowapproval_activity_users")
@XMLElement(name = "ResponsibleUser")
public class FlowApprovalActivityResponsibleUser extends GeneratedElementable implements Serializable {

	private static final long serialVersionUID = -3065392853394817211L;

	@DAOManaged(columnName = "activityID")
	@Key
	@ManyToOne
	private FlowApprovalActivity activity;

	@DAOManaged(columnName = "userID")
	@Key
	@XMLElement
	private User user;

	@DAOManaged
	@XMLElement
	private boolean fallback;

	public FlowApprovalActivityResponsibleUser() {}

	public FlowApprovalActivityResponsibleUser(User user, boolean fallback) {

		this.user = user;
		this.fallback = fallback;
	}

	public FlowApprovalActivity getActivity() {
		return activity;
	}

	public void setFlowFamily(FlowApprovalActivity activity) {
		this.activity = activity;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public User getUser() {
		return user;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public boolean isFallback() {
		return fallback;
	}

	public void setFallback(boolean restricted) {
		this.fallback = restricted;
	}

	@Override
	public String toString() {

		return user + " (fallback=" + fallback + ")";
	}
}
