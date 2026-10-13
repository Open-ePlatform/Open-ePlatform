package com.nordicpeak.flowengine.interfaces;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.webutils.http.URIParser;


public interface QueryRequestProcessor {

	void processRequest(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Exception;
	
	void close() throws Exception;
}
