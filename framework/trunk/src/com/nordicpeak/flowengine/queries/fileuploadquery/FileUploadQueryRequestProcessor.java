package com.nordicpeak.flowengine.queries.fileuploadquery;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.standardutils.io.CloseUtils;
import se.unlogic.standardutils.io.FileUtils;
import se.unlogic.standardutils.mime.MimeUtils;
import se.unlogic.standardutils.streams.StreamUtils;
import se.unlogic.webutils.http.HTTPUtils;
import se.unlogic.webutils.http.URIParser;

import com.nordicpeak.flowengine.interfaces.QueryRequestProcessor;

public class FileUploadQueryRequestProcessor implements QueryRequestProcessor {

	protected final Logger log = Logger.getLogger(this.getClass());

	private final String originalName;
	private final File file;

	public FileUploadQueryRequestProcessor(String originalName, File file) {

		super();
		this.originalName = originalName;
		this.file = file;
	}

	@Override
	public void processRequest(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Exception {

		//TODO logging
		
		HTTPUtils.setContentLength(file.length(), res);
		res.setHeader("Content-Disposition", "attachment;filename=\"" + FileUtils.toValidHttpFilename(originalName) + "\"");

		String contentType = MimeUtils.getMimeType(originalName);

		if (contentType != null) {
			res.setContentType(contentType);
		} else {
			res.setContentType("application/x-unknown-mime-type");
		}

		InputStream inputStream = null;
		OutputStream outputStream = null;

		try {
			inputStream = new FileInputStream(file);
			outputStream = res.getOutputStream();

			StreamUtils.transfer(inputStream, outputStream);

		} catch (IOException e) {

			log.info("Error sending file " + file + " to user " + user);

		} finally {
			CloseUtils.close(inputStream);
			CloseUtils.close(outputStream);
		}
	}

	@Override
	public void close() throws Exception {

		if(!FileUtils.deleteFile(file) && file.exists()){
			
			log.warn("Unable to delete file " + file);
		}
	}
}
