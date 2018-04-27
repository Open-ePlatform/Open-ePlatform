package com.nordicpeak.flowengine.beans;

import java.sql.Timestamp;

import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.standardutils.dao.annotations.DAOManaged;
import se.unlogic.standardutils.dao.annotations.Key;
import se.unlogic.standardutils.dao.annotations.OrderBy;
import se.unlogic.standardutils.enums.Order;
import se.unlogic.standardutils.xml.GeneratedElementable;
import se.unlogic.standardutils.xml.XMLElement;

import com.nordicpeak.flowengine.interfaces.ImmutableReadReceipt;

public abstract class BaseMessageReadReceipt extends GeneratedElementable implements ImmutableReadReceipt {

	@DAOManaged(dontUpdateIfNull = true, columnName="userID")
	@Key
	@XMLElement
	protected User user;

	@DAOManaged
	@OrderBy(order = Order.DESC)
	@XMLElement
	protected Timestamp read;

	@Override
	public abstract BaseMessage getMessage();

	@Override
	public User getUser() {

		return user;
	}

	public void setUser(User user) {

		this.user = user;
	}

	@Override
	public Timestamp getRead() {

		return read;
	}

	public void setRead(Timestamp read) {

		this.read = read;
	}
}
