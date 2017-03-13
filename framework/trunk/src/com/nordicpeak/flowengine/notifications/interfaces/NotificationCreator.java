package com.nordicpeak.flowengine.notifications.interfaces;

import se.unlogic.hierarchy.core.interfaces.ModuleDescriptor;

import com.nordicpeak.flowengine.beans.FlowInstance;
import com.nordicpeak.flowengine.notifications.beans.NotificationExtra;

public interface NotificationCreator {

	public NotificationExtra getNotificationExtra(Notification notification, FlowInstance flowInstance, String fullContextPath) throws Exception;
	
	public ModuleDescriptor getModuleDescriptor();
}
