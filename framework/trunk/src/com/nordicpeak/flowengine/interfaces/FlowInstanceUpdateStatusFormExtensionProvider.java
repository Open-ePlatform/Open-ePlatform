package com.nordicpeak.flowengine.interfaces;

import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;

import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.hierarchy.core.interfaces.ViewFragment;
import se.unlogic.standardutils.dao.TransactionHandler;
import se.unlogic.standardutils.validation.ValidationException;
import se.unlogic.webutils.http.URIParser;

import com.nordicpeak.flowengine.beans.ExternalMessage;
import com.nordicpeak.flowengine.beans.Status;

public interface FlowInstanceUpdateStatusFormExtensionProvider {

	public ViewFragment getViewFragment(Status status, ExternalMessage message, HttpServletRequest req, User user, URIParser uriParser);

	public void processRequest(Status status, ExternalMessage message, User user, TransactionHandler transactionHandler) throws SQLException, ValidationException;
	
	public void processCommittedRequest(Status status, ExternalMessage message, User user) throws SQLException;

}
