package com.nordicpeak.flowengine.accesscontrollers;

import javax.servlet.http.HttpSession;

import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.hierarchy.core.exceptions.AccessDeniedException;
import se.unlogic.webutils.http.SessionUtils;

import com.nordicpeak.flowengine.beans.Flow;
import com.nordicpeak.flowengine.interfaces.FlowInstanceAccessController;
import com.nordicpeak.flowengine.interfaces.ImmutableFlowInstance;


public class SessionAccessController implements FlowInstanceAccessController {

	private final HttpSession session;
	private final String tag;

	public SessionAccessController(HttpSession session, String tag) {

		this.session = session;
		this.tag = tag;
	}

	@Override
	public void checkNewFlowInstanceAccess(Flow flow, User user) throws AccessDeniedException {

		throw new AccessDeniedException("Operation not supported");
	}

	@Override
	public void checkFlowInstanceAccess(ImmutableFlowInstance flowInstance, User user) throws AccessDeniedException {

		try{
			if(session != null && session.getAttribute(SessionAccessController.class.getName() + "-" + tag + "-" + flowInstance.getFlowInstanceID()) != null){

				return;
			}
		}catch(IllegalStateException e){}

		throw new AccessDeniedException("Session based access check failed using tag: " + tag);
	}

	@Override
	public boolean isMutable(ImmutableFlowInstance flowInstance, User user) {

		return false;
	}

	public static void setSessionAttribute(Integer flowInstanceID, HttpSession session, String tag){

		SessionUtils.setAttribute(SessionAccessController.class.getName() + "-" + tag + "-" + flowInstanceID, true, session);		
	}	
}
