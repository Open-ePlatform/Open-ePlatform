package com.nordicpeak.flowengine.utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import se.unlogic.webutils.fileicons.FileIconHandler;

import com.nordicpeak.flowengine.interfaces.PDFResourceProvider;


public class FileIconResourceProvider implements PDFResourceProvider {

	@Override
	public InputStream getResource(String uri) throws IOException {

		URL url = FileIconHandler.getIconByFilename(uri);
		
		if(url != null){
			
			return url.openStream();
		}
		
		return null;
	}

}
