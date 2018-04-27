package com.nordicpeak.flowengine.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import com.nordicpeak.flowengine.interfaces.PDFAttachment;

public class PDFFileAttachment implements PDFAttachment {

	private final File file;
	private final String name;
	private final String description;

	public PDFFileAttachment(File file, String description) {

		super();
		this.file = file;
		this.description = description;
		this.name = file.getName();

	}

	public PDFFileAttachment(File file, String name, String description) {

		super();
		this.file = file;
		this.description = description;
		this.name = name;

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

}
