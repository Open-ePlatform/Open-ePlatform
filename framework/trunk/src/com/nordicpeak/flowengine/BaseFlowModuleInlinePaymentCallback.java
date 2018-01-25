package com.nordicpeak.flowengine;

import java.sql.SQLException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.openhierarchy.foregroundmodules.siteprofile.interfaces.SiteProfile;

import com.nordicpeak.flowengine.exceptions.flow.FlowDefaultStatusNotFound;
import com.nordicpeak.flowengine.exceptions.flowinstancemanager.FlowInstanceManagerClosedException;
import com.nordicpeak.flowengine.exceptions.queryinstance.UnableToSaveQueryInstanceException;
import com.nordicpeak.flowengine.interfaces.InlinePaymentCallback;
import com.nordicpeak.flowengine.managers.MutableFlowInstanceManager;


public class BaseFlowModuleInlinePaymentCallback implements InlinePaymentCallback {

	private final BaseFlowModule baseFlowModule;
	private final String actionID;
	private final SiteProfile siteProfile;
	private final User poster;
	
	public BaseFlowModuleInlinePaymentCallback(BaseFlowModule baseFlowModule, User poster, SiteProfile siteProfile, String actionID) {

		super();
		this.baseFlowModule = baseFlowModule;
		this.actionID = actionID;
		this.siteProfile = siteProfile;
		this.poster = poster;
	}

	@Override
	public void paymentComplete(MutableFlowInstanceManager instanceManager, User user, HttpServletRequest req, boolean addPaymentEvent, String details, Map<String,String> eventAttributes) throws FlowInstanceManagerClosedException, UnableToSaveQueryInstanceException, FlowDefaultStatusNotFound, SQLException {

		baseFlowModule.inlinePaymentComplete(instanceManager, req, user, poster, siteProfile, actionID, addPaymentEvent, details, eventAttributes);
	}

	@Override
	public String getPaymentSuccessURL(MutableFlowInstanceManager instanceManager, HttpServletRequest req) {

		return baseFlowModule.getPaymentSuccessURL(instanceManager, req);
	}

	@Override
	public String getPaymentFailURL(MutableFlowInstanceManager instanceManager, HttpServletRequest req) {

		return baseFlowModule.getPaymentFailURL(instanceManager, req);
	}
	
	@Override
	public String getPaymentURL(MutableFlowInstanceManager instanceManager, HttpServletRequest req) {

		return baseFlowModule.getSaveAndSubmitURL(instanceManager, req);
	}

	@Override
	public String getActionID() {

		return actionID;
	}

}
