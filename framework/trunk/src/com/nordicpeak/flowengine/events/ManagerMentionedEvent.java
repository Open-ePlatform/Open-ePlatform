package com.nordicpeak.flowengine.events;

import java.io.Serializable;
import java.util.List;

import se.unlogic.hierarchy.core.beans.User;

import com.nordicpeak.flowengine.beans.FlowInstance;

public class ManagerMentionedEvent implements Serializable {

	private static final long serialVersionUID = -3105656362605519707L;

	private final FlowInstance flowInstance;
	private final List<User> mentionedUsers;
	private final User user;

	public ManagerMentionedEvent(FlowInstance flowInstance, List<User> mentionedUsers, User user) {

		super();
		this.flowInstance = flowInstance;
		this.mentionedUsers = mentionedUsers;
		this.user = user;
	}

	public static long getSerialversionuid() {

		return serialVersionUID;
	}

	public FlowInstance getFlowInstance() {

		return flowInstance;
	}

	public List<User> getMentionedUsers() {

		return mentionedUsers;
	}

	public User getUser() {

		return user;
	}
}
