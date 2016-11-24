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
	private final long signingChainID;
	
	public BaseFlowModuleSigningCallback(BaseFlowModule baseFlowModule, String actionID, EventType submitEventType, SiteProfile siteProfile, boolean addSubmitEvent) {

		super();
		this.baseFlowModule = baseFlowModule;
		this.actionID = actionID;
		this.submitEventType = submitEventType;
		this.siteProfile = siteProfile;
		this.addSubmitEvent = addSubmitEvent;
		
		signingChainID = System.currentTimeMillis();
	}

	@Override
	public SigningConfirmedResponse signingConfirmed(MutableFlowInstanceManager instanceManager, HttpServletRequest req, User user) throws FlowInstanceManagerClosedException, UnableToSaveQueryInstanceException, SQLException, FlowDefaultStatusNotFound {

		FlowInstanceEvent signingEvent = null;
		FlowInstanceEvent submitEvent = null;
		
		Map<String,String> eventAttributes = new HashMap<String, String>();
		eventAttributes.put(BaseFlowModule.SIGNING_CHAIN_ID_FLOW_INSTANCE_EVENT_ATTRIBUTE, Long.toString(signingChainID));
		
		if (addSubmitEvent) {
			
			signingEvent = baseFlowModule.save(instanceManager, user, req, actionID, EventType.SIGNED, eventAttributes);
			
			submitEvent = baseFlowModule.save(instanceManager, user, req, actionID, submitEventType, eventAttributes);
			
		} else {
			
			signingEvent = baseFlowModule.save(instanceManager, user, req, actionID, EventType.SIGNED, eventAttributes);
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
