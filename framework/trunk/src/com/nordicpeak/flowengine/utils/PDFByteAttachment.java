package com.nordicpeak.flowengine.utils;

public class PDFByteAttachment {

	private final String attachmentName;
	private final String filename;
	private final byte[] data;

	public PDFByteAttachment(String attachmentName, String filename, byte[] data) {
		super();

		this.attachmentName = attachmentName;
		this.filename = filename;
		this.data = data;
	}

	public String getAttachmentName() {

		return attachmentName;
	}

	public String getFilename() {

		return filename;
	}

	public byte[] getData() {

		return data;
	}
}
