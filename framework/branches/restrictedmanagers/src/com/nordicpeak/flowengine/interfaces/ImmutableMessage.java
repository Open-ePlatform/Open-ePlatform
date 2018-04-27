package com.nordicpeak.flowengine.interfaces;

import java.sql.Timestamp;
import java.util.List;

import se.unlogic.hierarchy.core.beans.User;

import com.nordicpeak.flowengine.beans.BaseMessageReadReceipt;

public interface ImmutableMessage {

	public List<? extends ImmutableAttachment> getAttachments();

	public List<? extends BaseMessageReadReceipt> getReadReceipts();

	public Integer getMessageID();

	public String getMessage();

	public User getPoster();

	public Timestamp getAdded();

	public User getEditor();

	public Timestamp getUpdated();

	public ImmutableFlowInstance getFlowInstance();
}