package com.nordicpeak.flowengine.internalnotifications.interfaces;

import se.unlogic.hierarchy.core.interfaces.modules.descriptors.ModuleDescriptor;

import com.nordicpeak.flowengine.beans.FlowInstance;
import com.nordicpeak.flowengine.internalnotifications.beans.NotificationMetadata;

public interface NotificationSource {

	public NotificationMetadata getNotificationMetadata(Notification notification, FlowInstance flowInstance, String fullContextPath) throws Exception;
	
	public ModuleDescriptor getModuleDescriptor();
}
