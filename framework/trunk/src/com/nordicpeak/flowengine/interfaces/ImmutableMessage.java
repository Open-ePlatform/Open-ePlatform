package com.nordicpeak.flowengine.interfaces;

import java.sql.Timestamp;
import java.util.List;

import se.unlogic.hierarchy.core.beans.User;

public interface ImmutableMessage {

	public List<? extends ImmutableAttachment> getAttachments();

	public Integer getMessageID();

	public String getMessage();

	public User getPoster();

	public Timestamp getAdded();

	public User getEditor();

	public Timestamp getUpdated();

	public ImmutableFlowInstance getFlowInstance();
}