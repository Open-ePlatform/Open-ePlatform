package com.nordicpeak.flowengine.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import com.nordicpeak.flowengine.interfaces.PDFAttachment;

public class PDFFileAttachment implements PDFAttachment {

	private final File file;
	private final String name;
	private final String description;
	private final boolean inlineAttachment;
	private final boolean appendPageNumber;

	public PDFFileAttachment(File file, String description) {

		super();
		this.file = file;
		this.description = description;
		this.name = file.getName();
		inlineAttachment = false;
		appendPageNumber = false;
	}

	public PDFFileAttachment(File file, String name, String description) {

		super();
		this.file = file;
		this.description = description;
		this.name = name;
		inlineAttachment = false;
		appendPageNumber = false;
	}

	public PDFFileAttachment(File file, String name, String description, boolean inlineAttachment, boolean appendPageNumber) {

		super();
		this.file = file;
		this.description = description;
		this.name = name;
		this.inlineAttachment = inlineAttachment;
		this.appendPageNumber = appendPageNumber;
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

		return new FileInputStream(file);
	}
	
	public File getFile() throws Exception {
		
		return file;
	}

	@Override
	public boolean isInlineAttachment() {
		return inlineAttachment;
	}

	@Override
	public boolean isAppendPageNumber() {
		return appendPageNumber;
	}

	@Override
	public String toString() {
		return getClass().getName() + " (name=" + name + ", file=" + file + ")";
	}

	
}
