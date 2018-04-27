package com.nordicpeak.flowengine.interfaces;

import com.nordicpeak.flowengine.beans.Contact;

public interface ContactQueryInstance extends QueryInstance {
	
	public Contact getContact();
	
	public boolean updatedPoster();
}
