package com.nordicpeak.flowengine.utils;

import java.io.InputStream;
import java.sql.Blob;

import com.nordicpeak.flowengine.interfaces.PDFAttachment;


public class BlobPDFAttachment implements PDFAttachment {

	private final Blob blob;
	private final String name;
	private final String description;
	
	public BlobPDFAttachment(Blob blob, String name, String description) {

		super();
		this.blob = blob;
		this.name = name;
		this.description = description;
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

		return blob.getBinaryStream();
	}

}
