package com.nordicpeak.flowengine.notifications.interfaces;

import java.sql.Timestamp;

import se.unlogic.hierarchy.core.interfaces.AttributeHandler;
import se.unlogic.standardutils.xml.XMLable;

public interface Notification extends XMLable {

	public Integer getNotificationID();

	public Integer getSourceModuleID();

	public Integer getFlowInstanceID();
	
	public Integer getUserID();

	public Timestamp getAdded();

	public Timestamp getSeen();
	
	public String getTitle();

	public String getNotificationType();

	public Integer getExternalNotificationID();

	public AttributeHandler getAttributeHandler();

}