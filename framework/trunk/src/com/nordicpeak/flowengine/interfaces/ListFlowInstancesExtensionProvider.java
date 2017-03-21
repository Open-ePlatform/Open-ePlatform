package com.nordicpeak.flowengine.interfaces;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.hierarchy.core.interfaces.ViewFragment;
import se.unlogic.hierarchy.core.utils.extensionlinks.ExtensionLink;
import se.unlogic.openhierarchy.foregroundmodules.siteprofile.interfaces.SiteProfileHandler;
import se.unlogic.webutils.http.URIParser;

import com.nordicpeak.flowengine.beans.FlowInstance;

public interface ListFlowInstancesExtensionProvider {

	public ExtensionLink getListFlowInstancesExtensionLink(FlowInstance flowInstance, HttpServletRequest req, URIParser uriParser, User user) throws Exception;
	public ViewFragment getListFlowInstancesViewFragment(List<FlowInstance> flowInstances, SiteProfileHandler profileHandler, HttpServletRequest req, URIParser uriParser, User user) throws Exception;
	public int getModuleID();
}
