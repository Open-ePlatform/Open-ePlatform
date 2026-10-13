package com.nordicpeak.flowengine.interfaces;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.hierarchy.core.interfaces.ViewFragment;
import se.unlogic.webutils.http.URIParser;

import com.nordicpeak.flowengine.managers.FlowInstanceManager;


public interface FlowPaymentProvider {

	ViewFragment pay(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser, FlowInstanceManager instanceManager, FlowPaymentCallback callback) throws Exception;
	
}

