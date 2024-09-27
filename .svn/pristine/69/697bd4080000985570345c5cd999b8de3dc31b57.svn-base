package com.nordicpeak.flowengine.events;

import java.io.Serializable;
import java.util.Collection;

import se.unlogic.hierarchy.core.beans.User;

import com.nordicpeak.flowengine.beans.SigningParty;
import com.nordicpeak.flowengine.interfaces.ImmutableFlowInstance;

public class MultiSigningCanceledEvent implements Serializable {

	private static final long serialVersionUID = -4738711597966694402L;

	private final ImmutableFlowInstance flowInstance;
	private final SigningParty cancellingSigningParty;
	private final Collection<SigningParty> signingParties;
	private final User user;

	public MultiSigningCanceledEvent(ImmutableFlowInstance flowInstance, Collection<SigningParty> allSigningParties, SigningParty cancellingSigningParty, User user) {

		super();
		this.flowInstance = flowInstance;
		this.cancellingSigningParty = cancellingSigningParty;
		this.user = user;
		this.signingParties = allSigningParties;
	}

	public ImmutableFlowInstance getFlowInstance() {

		return flowInstance;
	}

	/**
	 * Set if one of the signers cancelled. If null it was one of the owners who cancelled.
	 */
	public SigningParty getCancellingSigningParty() {

		return cancellingSigningParty;
	}

	/** The user who cancelled the signing, if logged in. */
	public User getUser() {

		return user;
	}

	public Collection<SigningParty> getSigningParties() {

		return signingParties;
	}

}
