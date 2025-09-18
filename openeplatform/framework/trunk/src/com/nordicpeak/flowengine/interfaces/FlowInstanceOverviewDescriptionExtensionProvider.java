package com.nordicpeak.flowengine.interfaces;

import javax.servlet.http.HttpServletRequest;

import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.hierarchy.core.interfaces.ViewFragment;
import se.unlogic.webutils.http.URIParser;

import com.nordicpeak.flowengine.beans.FlowInstance;

public interface FlowInstanceOverviewDescriptionExtensionProvider {

	public ViewFragment getFlowInstanceOverviewDescriptionViewFragment(FlowInstance flowInstance, HttpServletRequest req, URIParser uriParser, User user) throws Exception;
	
}
