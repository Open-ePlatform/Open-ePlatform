package com.nordicpeak.flowengine.beans;

import java.io.Serializable;

import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.standardutils.dao.annotations.DAOManaged;
import se.unlogic.standardutils.dao.annotations.Key;
import se.unlogic.standardutils.dao.annotations.Table;
import se.unlogic.standardutils.xml.GeneratedElementable;
import se.unlogic.standardutils.xml.XMLElement;

@Table(name = "flowengine_flows_user_settings")
@XMLElement
public class FlowAdminUserSettings extends GeneratedElementable implements Serializable {

	private static final long serialVersionUID = -3846960397800413428L;
	
	@DAOManaged(dontUpdateIfNull = true, columnName = "userID")
	@Key
	@XMLElement
	private User user;

	@DAOManaged
	@XMLElement
	private Integer columnOrder;
	
	@DAOManaged
	@XMLElement
	private String columnName;
	
	@DAOManaged
	@XMLElement
	private boolean visible;
	

	public User getUser() {

		return user;
	}

	public void setUser(User user) {

		this.user = user;
	}
	
	public Integer getColumnOrder() {
		
		return columnOrder;
	}

	
	public void setColumnOrder(Integer columnOrder) {
	
		this.columnOrder = columnOrder;
	}

	
	public String getColumnName() {
	
		return columnName;
	}

	
	public void setColumnName(String columnName) {
	
		this.columnName = columnName;
	}

	
	public boolean isVisible() {
	
		return visible;
	}

	
	public void setVisible(boolean visible) {
	
		this.visible = visible;
	}

	@Override
	public String toString() {

		return "FlowAdminUserSettings [user=" + user + ", columnOrder=" + columnOrder + ", columnName=" + columnName + ", visible=" + visible + "]";
	}
	
	
	

}
