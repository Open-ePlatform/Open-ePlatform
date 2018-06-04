package com.nordicpeak.flowengine.interfaces;

import java.io.InputStream;


public interface PDFAttachment {

	public String getName();

	public String getDescription();

	public InputStream getInputStream() throws Exception;
}
