package com.nordicpeak.flowengine.utils;

import java.io.InputStream;

import com.nordicpeak.flowengine.interfaces.PDFAttachment;

public class PDFInputStreamAttachment implements PDFAttachment {

	private final InputStream inputStream;
	private final String name;
	private final String description;
	private final boolean inlineAttachment;

	public PDFInputStreamAttachment(InputStream inputStream, String name, String description) {
		this(inputStream, name, description, false);
	}

	public PDFInputStreamAttachment(InputStream inputStream, String name, String description, boolean inlineAttachment) {
		super();

		this.inputStream = inputStream;
		this.description = description;
		this.name = name;
		this.inlineAttachment = inlineAttachment;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getDescription() {
		return description;
	}

	@Override
	public InputStream getInputStream() throws Exception {
		return inputStream;
	}

	@Override
	public boolean isInlineAttachment() {
		return inlineAttachment;
	}

	@Override
	public boolean isAppendPageNumber() {
		return false;
	}
}
