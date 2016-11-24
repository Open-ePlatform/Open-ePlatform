package com.nordicpeak.flowengine.interfaces;

import javax.servlet.http.HttpServletRequest;

import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.hierarchy.core.interfaces.ViewFragment;
import se.unlogic.standardutils.validation.ValidationException;
import se.unlogic.webutils.http.URIParser;


public interface FlowNotificationHandler {

	public ViewFragment getCurrentSettingsView(ImmutableFlow flow, HttpServletRequest req, User user, URIParser uriParser) throws Exception;

	public ViewFragment getUpdateSettingsView(ImmutableFlow flow, HttpServletRequest req, User user, URIParser uriParser, ValidationException validationException) throws Exception;

	public void updateSettings(ImmutableFlow flow, HttpServletRequest req, User user, URIParser uriParser) throws ValidationException, Exception;
}
