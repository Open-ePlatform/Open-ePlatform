package com.nordicpeak.flowengine.beans;

import java.util.List;

import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.standardutils.annotations.NoDuplicates;
import se.unlogic.standardutils.annotations.WebPopulate;
import se.unlogic.standardutils.dao.annotations.DAOManaged;
import se.unlogic.standardutils.dao.annotations.Key;
import se.unlogic.standardutils.dao.annotations.OneToMany;
import se.unlogic.standardutils.dao.annotations.SimplifiedRelation;
import se.unlogic.standardutils.dao.annotations.Table;
import se.unlogic.standardutils.xml.GeneratedElementable;
import se.unlogic.standardutils.xml.XMLElement;

@Table(name = "flowengine_operating_message_notifications")
@XMLElement
public class OperatingMessageNotificationSettings extends GeneratedElementable {

	@DAOManaged
	@Key
	private Integer dummyID = 1;

	@DAOManaged
	@OneToMany(autoGet = true, autoAdd = true, autoUpdate = true)
	@SimplifiedRelation(table = "flowengine_operating_message_notification_users", remoteValueColumnName = "userID")
	@NoDuplicates
	@WebPopulate(paramName = "user")
	@XMLElement(fixCase = true, childName = "userID")
	private List<Integer> notificationUserIDs;

	@XMLElement(fixCase = true)
	private List<User> notificationUsers;

	public List<Integer> getNotificationUserIDs() {
		return notificationUserIDs;
	}

	public void setNotificationUserIDs(List<Integer> notificationUserIDs) {
		this.notificationUserIDs = notificationUserIDs;
	}

	public void setNotificationUsers(List<User> notificationUsers) {
		this.notificationUsers = notificationUsers;
	}

	public List<User> getNotificationUsers() {
		return notificationUsers;
	}

}
