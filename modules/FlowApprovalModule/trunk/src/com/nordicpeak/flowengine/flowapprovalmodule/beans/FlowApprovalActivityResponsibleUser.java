package com.nordicpeak.flowengine.flowapprovalmodule.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.hierarchy.foregroundmodules.userproviders.SimpleUser;
import se.unlogic.standardutils.dao.annotations.DAOManaged;
import se.unlogic.standardutils.dao.annotations.Key;
import se.unlogic.standardutils.dao.annotations.ManyToOne;
import se.unlogic.standardutils.dao.annotations.Table;
import se.unlogic.standardutils.populators.StringPopulator;
import se.unlogic.standardutils.validation.ValidationError;
import se.unlogic.standardutils.validation.ValidationException;
import se.unlogic.standardutils.xml.GeneratedElementable;
import se.unlogic.standardutils.xml.XMLElement;
import se.unlogic.standardutils.xml.XMLParser;
import se.unlogic.standardutils.xml.XMLParserPopulateable;
import se.unlogic.standardutils.xml.XMLValidationUtils;

@Table(name = "flowapproval_activity_users")
@XMLElement(name = "ResponsibleUser")
public class FlowApprovalActivityResponsibleUser extends GeneratedElementable implements XMLParserPopulateable,Serializable {

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

	@Override
	public void populate(XMLParser xmlParser) throws ValidationException {

		List<ValidationError> errors = new ArrayList<>();
		
		
		this.fallback = xmlParser.getPrimitiveBoolean("fallback");
		String userName = XMLValidationUtils.validateParameter("user/username", xmlParser, false, 1, 255, StringPopulator.getPopulator(), errors);
		if(userName != null) {
		SimpleUser simpleUser = new SimpleUser();
		simpleUser.setUsername(userName);
		this.user = simpleUser;
		}
		
		if (!errors.isEmpty()) {

			throw new ValidationException(errors);
		}
		
	}
}
