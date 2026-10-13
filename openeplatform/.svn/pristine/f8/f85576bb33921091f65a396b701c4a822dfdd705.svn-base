package com.nordicpeak.flowengine.events;

import java.io.Serializable;
import java.util.Collection;

import se.unlogic.hierarchy.core.beans.User;

import com.nordicpeak.flowengine.beans.SigningParty;
import com.nordicpeak.flowengine.interfaces.ImmutableFlowInstance;

public class MultiSigningPartySignedEvent implements Serializable {

private static final long serialVersionUID = -4738711597966694403L;
	
	private final ImmutableFlowInstance flowInstance;
	private final SigningParty signingParty;
	private final User user;
	private final Collection<SigningParty> signingParties;
	
	public MultiSigningPartySignedEvent(ImmutableFlowInstance flowInstance, SigningParty signingParty, User user, Collection<SigningParty> allSigningParties) {
		super();
		
		this.flowInstance = flowInstance;
		this.signingParty = signingParty;
		this.user = user;
		this.signingParties = allSigningParties;
	}
	
	public ImmutableFlowInstance getFlowInstance() {
		
		return flowInstance;
	}
	
	public SigningParty getSigningParty() {
		return signingParty;
	}
	
	public User getUser() {
		return user;
	}
	
	public Collection<SigningParty> getSigningParties() {
		return signingParties;
	}
}
