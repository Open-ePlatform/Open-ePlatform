package com.nordicpeak.flowengine.utils;

import java.util.HashMap;
import java.util.Map;

import se.unlogic.standardutils.string.StringUtils;

import com.nordicpeak.flowengine.beans.ExternalMessage;

public class ExternalMessageUtils {

	private static final String EVENT_ATTRIBUTE_EXTERNAL_MESSAGE_ID = "externalMessageID";
	private static final String EVENT_ATTRIBUTE_EXTERNAL_MESSAGE = "externalMessageFragment";
	private static final String EVENT_ATTRIBUTE_EXTERNAL_MESSAGE_READ_BY_ALL_PARTS = "externalMessageReadByAllParts";
	
	public static Map<String, String> getFlowInstanceEventAttributes(ExternalMessage externalMessage, boolean addReadByAllAttribute){
		
		Map<String, String> eventAttributes = new HashMap<String, String>(2);
		
		eventAttributes.put(EVENT_ATTRIBUTE_EXTERNAL_MESSAGE_ID, externalMessage.getMessageID().toString());
		
		if (!externalMessage.isReadReceiptEnabled()) {

			eventAttributes.put(EVENT_ATTRIBUTE_EXTERNAL_MESSAGE, StringUtils.toLogFormat(externalMessage.getMessage(), 50));
			
		} else if(externalMessage.isReadReceiptEnabled() && addReadByAllAttribute) {
			
			eventAttributes.put(EVENT_ATTRIBUTE_EXTERNAL_MESSAGE_READ_BY_ALL_PARTS, EVENT_ATTRIBUTE_EXTERNAL_MESSAGE_READ_BY_ALL_PARTS);
		}
		
		return eventAttributes;
	}
}
