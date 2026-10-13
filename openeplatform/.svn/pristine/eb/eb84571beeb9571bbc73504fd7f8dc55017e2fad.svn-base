package com.nordicpeak.flowengine.utils;

import java.io.InputStream;
import java.sql.Blob;
import java.sql.SQLException;

import com.nordicpeak.flowengine.interfaces.PDFResourceProvider;


public class BlobResourceProvider implements PDFResourceProvider {

	private final Blob blob;
	
	public BlobResourceProvider(Blob blob) {

		super();
		this.blob = blob;
	}

	@Override
	public InputStream getResource(String uri) throws SQLException {

		return blob.getBinaryStream();
	}

}
