package com.nordicpeak.flowengine.interfaces;

import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.xml.transform.TransformerException;

import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.hierarchy.core.interfaces.ViewFragment;
import se.unlogic.standardutils.validation.ValidationException;
import se.unlogic.webutils.http.URIParser;

import com.nordicpeak.flowengine.beans.Flow;
import com.nordicpeak.flowengine.beans.Status;

public interface StatusFormExtensionProvider {

	public String getProviderID();

	public ViewFragment getViewFragment(Status status, Flow flow, HttpServletRequest req, User user, URIParser uriParser, ValidationException validationException) throws SQLException, TransformerException;

	public void validateRequest(HttpServletRequest req, User user, URIParser uriParser) throws ValidationException;

	public void processRequest(Status status, HttpServletRequest req, User user, URIParser uriParser) throws SQLException, ValidationException;

}
