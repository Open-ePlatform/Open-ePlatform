package com.nordicpeak.flowengine.notifications.beans;

import se.unlogic.hierarchy.core.interfaces.MutableAttribute;
import se.unlogic.standardutils.dao.annotations.DAOManaged;
import se.unlogic.standardutils.dao.annotations.Key;
import se.unlogic.standardutils.dao.annotations.ManyToOne;
import se.unlogic.standardutils.dao.annotations.Table;
import se.unlogic.standardutils.xml.GeneratedElementable;
import se.unlogic.standardutils.xml.XMLElement;

@Table(name = "flowengine_notification_attributes")
@XMLElement
public class StoredNotificationAttribute extends GeneratedElementable implements MutableAttribute {
	
	@DAOManaged
	@Key
	@XMLElement(fixCase = true)
	protected String name;
	
	@DAOManaged
	@XMLElement(fixCase = true)
	protected String value;
	
	@DAOManaged(columnName = "notificationID")
	@ManyToOne
	@XMLElement
	private StoredNotification storedNotification;
	
	public StoredNotificationAttribute() {
		
	}
	
	public StoredNotificationAttribute(String name, String value) {
		
		this.name = name;
		this.value = value;
	}
	
	@Override
	public String getName() {
		
		return name;
	}
	
	@Override
	public String getValue() {
		
		return value;
	}
	
	@Override
	public void setValue(String value) {
		
		this.value = value;
		
	}
	
	public StoredNotification getNotification() {
		
		return storedNotification;
	}
	
	public void setNotification(StoredNotification storedNotification) {
		
		this.storedNotification = storedNotification;
	}
	
}
