package com.nordicpeak.flowengine.queries.basemapquery;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Blob;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.standardutils.io.FileUtils;
import se.unlogic.standardutils.mime.MimeUtils;
import se.unlogic.standardutils.streams.StreamUtils;
import se.unlogic.webutils.http.HTTPUtils;
import se.unlogic.webutils.http.URIParser;

import com.nordicpeak.flowengine.interfaces.QueryRequestProcessor;

public class BaseMapImageRequestProcessor implements QueryRequestProcessor {

	protected final Logger log = Logger.getLogger(this.getClass());

	private final String filename;
	private final Blob mapImage;

	public BaseMapImageRequestProcessor(String filename, Blob mapImage) {

		super();
		this.filename = filename;
		this.mapImage = mapImage;
	}

	@Override
	public void processRequest(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Exception {

		InputStream in = null;
		OutputStream out = null;

		try {
			
			HTTPUtils.setContentLength(mapImage.length(), res);

			res.setContentType(MimeUtils.getMimeType(filename + ".png"));
			res.setHeader("Content-Disposition", "inline; filename=\"" + FileUtils.toValidHttpFilename(filename + ".png") + "\"");

			in = mapImage.getBinaryStream();

			out = res.getOutputStream();

			StreamUtils.transfer(in, out);

		} catch (RuntimeException e) {

			log.info("Error sending map image to user " + user);

		} catch (IOException e) {

			log.info("Error sending map image to user " + user);

		} finally {

			StreamUtils.closeStream(in);
			StreamUtils.closeStream(out);
		}

	}

	@Override
	public void close() throws Exception {

	}

}
