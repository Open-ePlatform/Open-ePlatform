package com.nordicpeak.flowengine.utils;

import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.hierarchy.core.interfaces.AttributeHandler;
import se.unlogic.standardutils.collections.CollectionUtils;

import com.nordicpeak.flowengine.beans.FlowInstance;
import com.nordicpeak.flowengine.beans.FlowInstanceEvent;
import com.nordicpeak.flowengine.interfaces.ImmutableFlowInstance;

public class FlowInstanceUtils {

	public static String getSubmitterName(ImmutableFlowInstance flowInstance, String anonymousUserName) {

		String submitterName = getSubmitterName(flowInstance);

		if (submitterName == null) {

			return anonymousUserName;
		}

		return submitterName;
	}

	public static String getSubmitterName(ImmutableFlowInstance flowInstance) {

		if (flowInstance.getAttributeHandler().isSet("organizationName")) {

			String orgName = flowInstance.getAttributeHandler().getString("organizationName");

			if (flowInstance.getPoster() != null) {

				return orgName + " (" + getNameFromUser(flowInstance.getPoster()) + ")";

			} else if (flowInstance.getAttributeHandler().isSet("firstname")) {

				return orgName + " (" + getNameFromAttributes(flowInstance.getAttributeHandler()) + ")";
			}

			return orgName;

		} else if (flowInstance.getPoster() != null) {

			return getNameFromUser(flowInstance.getPoster());

		} else if (flowInstance.getAttributeHandler().isSet("firstname")) {

			return getNameFromAttributes(flowInstance.getAttributeHandler());
		}

		return null;
	}

	private static String getNameFromUser(User user) {

		return user.getFirstname() + " " + user.getLastname();
	}

	private static String getNameFromAttributes(AttributeHandler attributeHandler) {

		String firstName = attributeHandler.getString("firstname");
		String lastname = attributeHandler.getString("lastname");

		return firstName + " " + lastname;
	}
	
	public static FlowInstanceEvent getLatestPDFEvent(FlowInstance flowInstance) {

		if (CollectionUtils.isEmpty(flowInstance.getEvents())) {

			return null;
		}

		for (int i = flowInstance.getEvents().size() - 1; i > -1; i--) {

			FlowInstanceEvent event = flowInstance.getEvents().get(i);

			if (Boolean.TRUE.equals(event.getAttributeHandler().getBoolean("pdf"))) {

				return event;
			}
		}

		return null;
	}	

}
