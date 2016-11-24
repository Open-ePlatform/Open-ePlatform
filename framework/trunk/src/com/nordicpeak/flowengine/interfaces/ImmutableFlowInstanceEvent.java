package com.nordicpeak.flowengine.interfaces;

import java.sql.Timestamp;

import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.hierarchy.core.interfaces.AttributeHandler;
import se.unlogic.standardutils.xml.Elementable;

import com.nordicpeak.flowengine.enums.EventType;

public interface ImmutableFlowInstanceEvent extends Elementable {

	public Integer getEventID();

	public ImmutableFlowInstance getFlowInstance();

	public EventType getEventType();

	public String getStatus();

	public String getStatusDescription();

	public String getDetails();

	public Timestamp getAdded();

	public String getShortDate();

	public User getPoster();

	public AttributeHandler getAttributeHandler();

}