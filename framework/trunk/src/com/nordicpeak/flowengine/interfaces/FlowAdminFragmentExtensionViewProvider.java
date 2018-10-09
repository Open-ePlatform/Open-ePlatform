package com.nordicpeak.flowengine.interfaces;

import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;

import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.hierarchy.core.interfaces.Prioritized;
import se.unlogic.hierarchy.core.interfaces.ViewFragment;
import se.unlogic.webutils.http.URIParser;

import com.nordicpeak.flowengine.beans.Flow;

public interface FlowAdminFragmentExtensionViewProvider extends Prioritized {

	public ViewFragment getShowView(String extensionRequestURL, Flow flow, HttpServletRequest req, User user, URIParser uriParser) throws TransformerConfigurationException, TransformerException, SQLException;

	public String getExtensionViewTitle();
	public String getExtensionViewLinkName();

	// Return null for AdminModule to redirect out of this module
	public ViewFragment processRequest(String extensionRequestURL, Flow flow, HttpServletRequest req, User user, URIParser uriParser) throws Exception;

	public int getModuleID();
}