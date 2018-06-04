package com.nordicpeak.flowengine.interfaces;

import java.sql.SQLException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.hierarchy.core.exceptions.ModuleConfigurationException;
import se.unlogic.hierarchy.core.interfaces.ForegroundModuleResponse;
import se.unlogic.standardutils.validation.ValidationError;
import se.unlogic.webutils.http.URIParser;



public interface MessageCRUDCallback {

	int getRamThreshold();

	long getMaxRequestSize();

	long getMaxFileSize();

	String getTempDir();

	boolean isOperatingStatusDisabled(ImmutableFlow flow, boolean manager);

	ForegroundModuleResponse list(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser, List<ValidationError> errors) throws SQLException, ModuleConfigurationException;

}
