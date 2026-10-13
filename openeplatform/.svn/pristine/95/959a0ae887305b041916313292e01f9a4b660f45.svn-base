package com.nordicpeak.flowengine.beans;

import java.lang.reflect.Field;

import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.standardutils.dao.annotations.DAOManaged;
import se.unlogic.standardutils.dao.annotations.Key;
import se.unlogic.standardutils.dao.annotations.ManyToOne;
import se.unlogic.standardutils.dao.annotations.Table;
import se.unlogic.standardutils.reflection.ReflectionUtils;
import se.unlogic.standardutils.xml.GeneratedElementable;
import se.unlogic.standardutils.xml.XMLElement;

@Table(name = "flowengine_flow_instance_bookmarks")
@XMLElement
public class UserBookmark extends GeneratedElementable {

	public static final Field FLOW_INSTANCE_RELATION = ReflectionUtils.getField(UserBookmark.class, "flowInstance");

	@DAOManaged(columnName="userID", dontUpdateIfNull = true)
	@Key
	@XMLElement
	private User user;

	@DAOManaged(columnName = "flowInstanceID")
	@Key
	@ManyToOne
	@XMLElement
	private FlowInstance flowInstance;

	public User getUser() {

		return user;
	}

	public void setUser(User user) {

		this.user = user;
	}

	public FlowInstance getFlowInstance() {

		return flowInstance;
	}

	public void setFlowInstance(FlowInstance flowInstance) {

		this.flowInstance = flowInstance;
	}

}
