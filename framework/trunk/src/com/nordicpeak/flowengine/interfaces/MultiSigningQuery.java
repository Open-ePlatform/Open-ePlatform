package com.nordicpeak.flowengine.interfaces;

import java.util.List;

import com.nordicpeak.flowengine.beans.SigningParty;


public interface MultiSigningQuery extends QueryInstance {

	List<? extends SigningParty> getSigningParties();
	
}
