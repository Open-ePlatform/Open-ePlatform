package com.nordicpeak.flowengine.beans;

import java.sql.SQLException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.hierarchy.core.exceptions.ModuleConfigurationException;
import se.unlogic.hierarchy.core.interfaces.ForegroundModuleResponse;
import se.unlogic.standardutils.validation.ValidationError;
import se.unlogic.webutils.http.URIParser;

import com.nordicpeak.flowengine.UserFlowInstanceModule;
import com.nordicpeak.flowengine.interfaces.FlowProcessCallback;
import com.nordicpeak.flowengine.managers.FlowInstanceManager;
import com.nordicpeak.flowengine.managers.MutableFlowInstanceManager;

public class UserFlowInstanceBrowserProcessCallback implements FlowProcessCallback {

	private UserFlowInstanceModule userFlowInstanceModule;

	private String saveActionID;

	private String submitActionID;

	private String multiSigningActionID;

	private String paymentActionID;

	public UserFlowInstanceBrowserProcessCallback(String saveActionID, String submitActionID, String multiSigningActionID, String paymentActionID, UserFlowInstanceModule userFlowInstanceModule) {

		this.saveActionID = saveActionID;
		this.submitActionID = submitActionID;
		this.multiSigningActionID = multiSigningActionID;
		this.paymentActionID = paymentActionID;
		this.userFlowInstanceModule = userFlowInstanceModule;
	}

	@Override
	public ForegroundModuleResponse list(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser, List<ValidationError> validationErrors) throws ModuleConfigurationException, SQLException {

		return userFlowInstanceModule.list(req, res, user, uriParser, validationErrors);
	}

	@Override
	public String getSubmitActionID() {

		return submitActionID;
	}

	@Override
	public String getSaveActionID() {

		return saveActionID;
	}

	@Override
	public String getPaymentActionID() {

		return paymentActionID;
	}

	@Override
	public String getMultiSigningActionID() {

		return multiSigningActionID;
	}

	@Override
	public void appendFormData(Document doc, Element baseElement, MutableFlowInstanceManager instanceManager, HttpServletRequest req, User user) {}

	@Override
	public void appendShowFlowInstanceData(Document doc, Element baseElement, FlowInstanceManager instanceManager, HttpServletRequest req, User user) {}
}
