package com.nordicpeak.flowengine.interfaces;

import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.hierarchy.core.interfaces.ViewFragment;
import se.unlogic.standardutils.validation.ValidationException;
import se.unlogic.webutils.http.URIParser;

import com.nordicpeak.flowengine.beans.ExternalMessage;
import com.nordicpeak.flowengine.beans.Status;

public interface FlowInstanceUpdateStatusFormExtensionProvider {

	public ViewFragment getViewFragment(Status status, ExternalMessage message, HttpServletRequest req, User user, URIParser uriParser);

	public void processRequest(Status status, ExternalMessage message, HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws SQLException, ValidationException;

}
