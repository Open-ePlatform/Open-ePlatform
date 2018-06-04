package com.nordicpeak.flowengine.events;

import java.io.Serializable;

import com.nordicpeak.flowengine.interfaces.ImmutableFlowInstance;
import com.nordicpeak.flowengine.interfaces.SigningSession;
import com.nordicpeak.flowengine.interfaces.SigningSessionParty;

import se.unlogic.hierarchy.core.beans.User;

public class MultiSigningCanceledEvent implements Serializable {
	
	private static final long serialVersionUID = -4738711597966694402L;
	
	private final ImmutableFlowInstance flowInstance;
	private final SigningSession signingSession;
	private final String fullContextPath;
	private final SigningSessionParty signingParty;
	private final User user;
	
	public MultiSigningCanceledEvent(ImmutableFlowInstance flowInstance, SigningSession signingSession, SigningSessionParty signingParty, User user, String fullContextPath) {
		
		super();
		this.flowInstance = flowInstance;
		this.signingSession = signingSession;
		this.fullContextPath = fullContextPath;
		this.signingParty = signingParty;
		this.user = user;
	}
	
	public ImmutableFlowInstance getFlowInstance() {
		
		return flowInstance;
	}
	
	/**
	 * Set if one of the signers cancelled.
	 * If null it was one of the owners who cancelled.
	 */
	public SigningSession getSigningSession() {
		return signingSession;
	}
	
	public String getFullContextPath() {
		return fullContextPath;
	}
	
	public SigningSessionParty getSigningParty() {
		return signingParty;
	}
	
	/**
	 * The user who cancelled the signing, if logged in.
	 */
	public User getUser() {
		return user;
	}
	
}
