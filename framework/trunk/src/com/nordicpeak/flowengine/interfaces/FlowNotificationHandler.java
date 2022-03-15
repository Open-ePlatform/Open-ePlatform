package com.nordicpeak.flowengine.interfaces;

import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;

import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.hierarchy.core.interfaces.ViewFragment;
import se.unlogic.standardutils.validation.ValidationException;
import se.unlogic.webutils.http.URIParser;

public interface FlowNotificationHandler {

	ViewFragment getCurrentSettingsView(ImmutableFlow flow, HttpServletRequest req, User user, URIParser uriParser) throws Exception;

	ViewFragment getUpdateSettingsView(ImmutableFlow flow, HttpServletRequest req, User user, URIParser uriParser, ValidationException validationException) throws Exception;

	void updateSettings(ImmutableFlow flow, HttpServletRequest req, User user, URIParser uriParser) throws ValidationException, Exception;

	String getInstallationBaseURL(Integer profileID);
	
	String getEmailSenderName(ImmutableFlowInstance flowInstance);

	String getEmailSenderAddress(ImmutableFlowInstance flowInstance);

	String getSMSSenderName(ImmutableFlowInstance flowInstance);

	boolean isAnyFlowInstanceExpiredNotificationsEnabled(ImmutableFlowFamily flowFamily) throws SQLException;

}
