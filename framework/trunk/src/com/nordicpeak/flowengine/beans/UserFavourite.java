package com.nordicpeak.flowengine.beans;

import java.io.Serializable;

import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.standardutils.dao.annotations.DAOManaged;
import se.unlogic.standardutils.dao.annotations.Key;
import se.unlogic.standardutils.dao.annotations.ManyToOne;
import se.unlogic.standardutils.dao.annotations.Table;
import se.unlogic.standardutils.json.JsonObject;
import se.unlogic.standardutils.xml.GeneratedElementable;
import se.unlogic.standardutils.xml.XMLElement;

import com.nordicpeak.flowengine.interfaces.ImmutableFlowFamily;
import com.nordicpeak.flowengine.interfaces.ImmutableUserFavourite;

@Table(name = "flowengine_flow_family_favourites")
@XMLElement
public class UserFavourite extends GeneratedElementable implements ImmutableUserFavourite, Serializable {

	private static final long serialVersionUID = 8386630568625924158L;

	@DAOManaged(columnName = "flowFamilyID")
	@Key
	@ManyToOne(autoGet = true)
	@XMLElement
	private FlowFamily flowFamily;

	@DAOManaged(columnName = "userID")
	@Key
	@XMLElement
	private User user;

	@XMLElement
	private String flowName;

	@XMLElement
	private boolean flowEnabled;

	public void setFlowFamily(FlowFamily flowFamily) {

		this.flowFamily = flowFamily;
	}

	@Override
	public ImmutableFlowFamily getFlowFamily() {

		return flowFamily;
	}

	@Override
	public User getUser() {

		return user;
	}

	public void setUser(User user) {

		this.user = user;
	}

	@Override
	public String getFlowName() {

		return flowName;
	}

	public void setFlowName(String flowName) {

		this.flowName = flowName;
	}

	@Override
	public boolean isFlowEnabled() {

		return flowEnabled;
	}

	public void setFlowEnabled(boolean flowEnabled) {

		this.flowEnabled = flowEnabled;
	}

	public JsonObject toJson() {

		JsonObject favourite = new JsonObject();

		favourite.putField("flowFamilyID", flowFamily.getFlowFamilyID().toString());

		if (flowName != null) {

			favourite.putField("flowName", flowName);
			favourite.putField("flowEnabled", flowEnabled + "");
		}

		if (user != null) {
			favourite.putField("userID", user.getUserID().toString());
		}

		return favourite;

	}

}
