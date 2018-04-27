package com.nordicpeak.flowengine.interfaces;

import java.sql.Timestamp;

import se.unlogic.hierarchy.core.beans.User;


public interface ImmutableReadReceipt {

	public ImmutableMessage getMessage();

	public User getUser();

	public Timestamp getRead();

}