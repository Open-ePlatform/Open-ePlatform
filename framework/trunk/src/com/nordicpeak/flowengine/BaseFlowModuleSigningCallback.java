package com.nordicpeak.flowengine;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.openhierarchy.foregroundmodules.siteprofile.interfaces.SiteProfile;

import com.nordicpeak.flowengine.beans.FlowInstanceEvent;
import com.nordicpeak.flowengine.enums.EventType;
import com.nordicpeak.flowengine.exceptions.flow.FlowDefaultStatusNotFound;
import com.nordicpeak.flowengine.exceptions.flowinstancemanager.FlowInstanceManagerClosedException;
import com.nordicpeak.flowengine.exceptions.queryinstance.UnableToSaveQueryInstanceException;
import com.nordicpeak.flowengine.interfaces.SigningCallback;
import com.nordicpeak.flowengine.managers.MutableFlowInstanceManager;


public class BaseFlowModuleSigningCallback implements SigningCallback {

	private final BaseFlowModule baseFlowModule;
	private final String actionID;
	private final EventType submitEventType;
	private final SiteProfile siteProfile;
	private final boolean addSubmitEvent;
	private final String signingSessionID;
	private User poster;
	
	public BaseFlowModuleSigningCallback(BaseFlowModule baseFlowModule, User poster, String actionID, EventType submitEventType, SiteProfile siteProfile, boolean addSubmitEvent) {

		super();
		this.baseFlowModule = baseFlowModule;
		this.actionID = actionID;
		this.submitEventType = submitEventType;
		this.siteProfile = siteProfile;
		this.addSubmitEvent = addSubmitEvent;
		this.poster = poster;
		
		signingSessionID = "single-" + Long.toString(System.currentTimeMillis());
	}

	@Override
	public SigningConfirmedResponse signingConfirmed(MutableFlowInstanceManager instanceManager, HttpServletRequest req, User user) throws FlowInstanceManagerClosedException, UnableToSaveQueryInstanceException, SQLException, FlowDefaultStatusNotFound {

		FlowInstanceEvent signingEvent = null;
		FlowInstanceEvent submitEvent = null;
		
		Map<String,String> signingEventAttributes = new HashMap<String, String>();
		signingEventAttributes.put(Constants.FLOW_INSTANCE_EVENT_SIGNING_SESSION, signingSessionID);
		signingEventAttributes.put(Constants.FLOW_INSTANCE_EVENT_SIGNING_SESSION_EVENT, Constants.FLOW_INSTANCE_EVENT_SIGNING_SESSION_EVENT_SIGNING_PDF);
		
		if (poster == null && user != null) {
			poster = user;
		}
		
		if (addSubmitEvent) {
			
			signingEvent = baseFlowModule.save(instanceManager, user, poster, req, actionID, EventType.SIGNED, signingEventAttributes);
			
			Map<String,String> submitEventAttributes = new HashMap<String, String>();
			submitEventAttributes.put(Constants.FLOW_INSTANCE_EVENT_SIGNING_SESSION, signingSessionID);
			submitEventAttributes.put(Constants.FLOW_INSTANCE_EVENT_SIGNING_SESSION_EVENT, Constants.FLOW_INSTANCE_EVENT_SIGNING_SESSION_EVENT_SIGNED_PDF);
			
			submitEvent = baseFlowModule.save(instanceManager, user, poster, req, actionID, submitEventType, submitEventAttributes);
			
		} else {
			
			signingEvent = baseFlowModule.save(instanceManager, user, poster, req, actionID, EventType.SIGNED, signingEventAttributes);
		}
		
		return new SigningConfirmedResponse(signingEvent, submitEvent);
	}

	@Override
	public void signingComplete(MutableFlowInstanceManager instanceManager, FlowInstanceEvent event, HttpServletRequest req) throws FlowInstanceManagerClosedException, UnableToSaveQueryInstanceException, FlowDefaultStatusNotFound, SQLException {

		baseFlowModule.signingComplete(instanceManager, event, req, siteProfile, actionID);
	}
	
	@Override
	public void abortSigning(MutableFlowInstanceManager instanceManager) {

		baseFlowModule.abortSigning(instanceManager);
	}

	@Override
	public String getSignFailURL(MutableFlowInstanceManager instanceManager, HttpServletRequest req) {

		return baseFlowModule.getSignFailURL(instanceManager, req);
	}

	@Override
	public String getSignSuccessURL(MutableFlowInstanceManager instanceManager, HttpServletRequest req) {

		return baseFlowModule.getSignSuccessURL(instanceManager, req);
	}

	@Override
	public String getSigningURL(MutableFlowInstanceManager instanceManager, HttpServletRequest req) {

		return baseFlowModule.getSaveAndSubmitURL(instanceManager, req);
	}

	@Override
	public String getActionID() {
		
		return actionID;
	}

	
	public EventType getEventType() {
	
		return submitEventType;
	}

	
	@Override
	public SiteProfile getSiteProfile() {
	
		return siteProfile;
	}
}
