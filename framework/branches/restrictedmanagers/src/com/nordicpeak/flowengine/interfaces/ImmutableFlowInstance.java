package com.nordicpeak.flowengine.interfaces;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.hierarchy.core.interfaces.attributes.AttributeHandler;
import se.unlogic.standardutils.xml.Elementable;

public interface ImmutableFlowInstance extends Elementable, Serializable{

	public Integer getFlowInstanceID();
	
	public User getPoster();

	public List<User> getOwners();

	public Timestamp getAdded();

	public User getEditor();

	public Timestamp getUpdated();
	
	public Timestamp getFirstSubmitted();
	
	public Timestamp getLastStatusChange();

	public Integer getStepID();

	public ImmutableFlow getFlow();

	public ImmutableStatus getStatus();

	public boolean isFullyPopulated();

	public Integer getProfileID();

	public List<? extends ImmutableMessage> getInternalMessages();

	public List<? extends ImmutableMessage> getExternalMessages();

	public List<? extends ImmutableFlowInstanceEvent> getEvents();

	public List<User> getManagers();

	public AttributeHandler getAttributeHandler();
}