package com.nordicpeak.flowengine.beans;

import se.unlogic.standardutils.string.StringUtils;
import se.unlogic.standardutils.xml.GeneratedElementable;
import se.unlogic.standardutils.xml.XMLElement;

import com.nordicpeak.flowengine.enums.OperatingMessageType;
import com.nordicpeak.flowengine.interfaces.OperatingStatus;

@XMLElement(name = "OperatingMessage")
public class ExternalOperatingMessage extends GeneratedElementable implements OperatingStatus {
	
	
	@XMLElement
	private ExternalOperatingMessageSource source;
	
	@XMLElement
	private String message;
	
	@XMLElement
	private OperatingMessageType messageType;
	
	public ExternalOperatingMessage(ExternalOperatingMessageSource source, OperatingMessageType messageType, String message) {
		super();
		this.source = source;
		this.messageType = messageType;
		this.message = message;
	}
	
	@Override
	public String getMessage() {
		
		return message;
	}
	
	public void setMessage(String message) {
		
		this.message = message;
	}
	
	public OperatingMessageType getMessageType() {
		
		return messageType;
	}

	public void setMessageType(OperatingMessageType messageType) {

		this.messageType = messageType;
	}

	public ExternalOperatingMessageSource getSource() {
		return source;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((message == null) ? 0 : message.hashCode());
		result = prime * result + ((messageType == null) ? 0 : messageType.hashCode());
		result = prime * result + ((source == null) ? 0 : source.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ExternalOperatingMessage other = (ExternalOperatingMessage) obj;
		if (message == null) {
			if (other.message != null)
				return false;
		} else if (!message.equals(other.message))
			return false;
		if (messageType != other.messageType)
			return false;
		if (source == null) {
			if (other.source != null)
				return false;
		} else if (!source.equals(other.source))
			return false;
		return true;
	}
	
	@Override
	public boolean isDisabled() {
		
		return !source.isEnabled();
	}
	
	@Override
	public String toString() {
		
		return StringUtils.toLogFormat(message, 30) + " (Source: " + source.getName() + ")";
	}
}
