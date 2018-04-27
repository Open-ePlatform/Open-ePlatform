package com.nordicpeak.flowengine.interfaces;

import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;

import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.hierarchy.core.interfaces.Prioritized;
import se.unlogic.webutils.http.URIParser;

import com.nordicpeak.flowengine.beans.ExtensionView;
import com.nordicpeak.flowengine.beans.Flow;

public interface FlowBrowserExtensionViewProvider extends Prioritized {

	public ExtensionView getFlowOverviewExtensionView(Flow flow, HttpServletRequest req, User user, URIParser uriParser) throws TransformerConfigurationException, TransformerException, SQLException;

}
