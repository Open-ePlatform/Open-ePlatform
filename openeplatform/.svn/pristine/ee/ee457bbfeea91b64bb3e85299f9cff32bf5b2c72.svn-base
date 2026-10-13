package com.nordicpeak.flowengine.internalnotifications.interfaces;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import se.unlogic.hierarchy.core.beans.User;

public interface NotificationHandler {

	public void addNotification(int flowInstanceID, int userID, int sourceModuleID, String notificationType, Integer externalNotificationID, String title, Map<String,String> attributes) throws SQLException;

	public void deleteNotifications(int userID) throws SQLException;

	public void deleteNotifications(int sourceModuleID, int externalNotificationID, String notificationType) throws SQLException;
	
	public void deleteNotifications(int sourceModuleID, int flowInstanceID, User user, String notificationType) throws SQLException;

	public int getUnreadCount(int userID) throws SQLException;
	
	public Notification getNotification(int userID, int sourceModuleID, Integer externalNotificationID, String notificationType) throws SQLException;
	
	public List<? extends Notification> getNotificationsForUser(int userID, Integer count, Timestamp breakpoint, boolean showAll) throws SQLException;
	
	public List<? extends Notification> getNotificationsForModule(int sourceModuleID, Integer externalNotificationID, String notificationType, Boolean readStatus) throws SQLException;
	
	public void markAsSeen(List<? extends Notification> notifications) throws SQLException;
	
	public String getModuleURL(HttpServletRequest req);
	
	public void addNotificationSource(NotificationSource notificationSource);
	
	public boolean removeNotificationSource(NotificationSource notificationSource);
	
	public void sendNotificationToFlowInstanceOwners(NotificationSource notificationSource, Integer flowInstanceID, String title, User sendingUser, String type, Map<String, String> attributes) throws SQLException;
	
	public void sendNotificationToFlowInstanceManagers(NotificationSource notificationSource, Integer flowInstanceID, String title, User sendingUser, String type, Map<String, String> attributes) throws SQLException;

}
