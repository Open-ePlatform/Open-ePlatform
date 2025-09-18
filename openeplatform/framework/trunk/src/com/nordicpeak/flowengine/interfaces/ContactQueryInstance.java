package com.nordicpeak.flowengine.interfaces;

import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.hierarchy.core.interfaces.attributes.MutableAttributeHandler;

import com.nordicpeak.flowengine.beans.Contact;

public interface ContactQueryInstance extends QueryInstance {
	
	public Contact getContact();
	
	public boolean updatedPoster();
	
	public void forcedRepopulate(User poster, MutableAttributeHandler flowInstanceAttributeHandler, QueryHandler queryHandler);
}
