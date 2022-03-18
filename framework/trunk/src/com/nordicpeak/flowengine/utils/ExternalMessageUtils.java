package com.nordicpeak.flowengine.utils;

import java.util.HashMap;
import java.util.Map;

import se.unlogic.standardutils.string.StringUtils;

import com.nordicpeak.flowengine.beans.ExternalMessage;

public class ExternalMessageUtils {

	private static final String EVENT_ATTRIBUTE_EXTERNAL_MESSAGE_ID = "externalMessageID";
	private static final String EVENT_ATTRIBUTE_EXTERNAL_MESSAGE = "externalMessageFragment";
	
	public static Map<String, String> getFlowInstanceEventAttributes(ExternalMessage externalMessage){
		
		if (externalMessage.isReadReceiptEnabled()) {
			
			return null;
		}
		
		Map<String, String> eventAttributes = new HashMap<String, String>(2);
		
		eventAttributes.put(EVENT_ATTRIBUTE_EXTERNAL_MESSAGE_ID, externalMessage.getMessageID().toString());
		eventAttributes.put(EVENT_ATTRIBUTE_EXTERNAL_MESSAGE, StringUtils.toLogFormat(externalMessage.getMessage(), 50));
		
		return eventAttributes;
	}
}
