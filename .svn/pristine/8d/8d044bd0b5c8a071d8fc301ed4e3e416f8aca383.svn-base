package com.nordicpeak.flowengine.interfaces;

import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.xml.transform.TransformerException;

import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.hierarchy.core.interfaces.ViewFragment;
import se.unlogic.webutils.http.URIParser;

import com.nordicpeak.flowengine.beans.ExternalMessage;
import com.nordicpeak.flowengine.beans.FlowInstance;

public interface ExternalMessageExtensionProvider {

	public ViewFragment getViewFragment(ExternalMessage message, FlowInstance flowInstance, HttpServletRequest req, User user, URIParser uriParser, boolean manager) throws SQLException, TransformerException;

}
