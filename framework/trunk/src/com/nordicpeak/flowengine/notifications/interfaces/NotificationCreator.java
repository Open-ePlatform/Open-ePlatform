package com.nordicpeak.flowengine.notifications.interfaces;

import se.unlogic.hierarchy.core.interfaces.ModuleDescriptor;

import com.nordicpeak.flowengine.beans.FlowInstance;
import com.nordicpeak.flowengine.notifications.beans.NotificationMetadata;

public interface NotificationCreator {

	public NotificationMetadata getNotificationExtra(Notification notification, FlowInstance flowInstance, String fullContextPath) throws Exception;
	
	public ModuleDescriptor getModuleDescriptor();
}
