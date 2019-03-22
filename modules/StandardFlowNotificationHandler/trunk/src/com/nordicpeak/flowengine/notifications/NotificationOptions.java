package com.nordicpeak.flowengine.notifications;

import java.io.File;

public class NotificationOptions {

	private File xmlFile;

	private String senderName;

	private String senderAddress;

	public NotificationOptions() {}
	
	public NotificationOptions(File xmlFile) {

		this.xmlFile = xmlFile;
	}

	public NotificationOptions(String senderName, String senderAddress) {

		this.senderName = senderName;
		this.senderAddress = senderAddress;
	}
	
	public File getXmlFile() {

		return xmlFile;
	}

	public void setXmlFile(File xmlFile) {

		this.xmlFile = xmlFile;
	}

	public String getSenderName() {

		return senderName;
	}

	public void setSenderName(String senderName) {

		this.senderName = senderName;
	}

	public String getSenderAddress() {

		return senderAddress;
	}

	public void setSenderAddress(String senderAddress) {

		this.senderAddress = senderAddress;
	}

}
