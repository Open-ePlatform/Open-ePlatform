package com.nordicpeak.flowengine.queries.basemapquery;

import java.io.ByteArrayOutputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.SocketTimeoutException;
import java.net.URL;

import javax.sql.rowset.serial.SerialBlob;

import org.apache.log4j.Logger;

import se.unlogic.standardutils.io.CloseUtils;
import se.unlogic.webutils.http.HTTPUtils;

public class GenerateMapImageTask implements Runnable {

	protected final Logger log = Logger.getLogger(this.getClass());

	private String printConfig;

	private MapImageGenerator executor;
	
	private SerialBlob generatedMapImage;

	public GenerateMapImageTask(String printConfig, MapImageGenerator executor) {

		this.printConfig = printConfig;
		this.executor = executor;
	}

	@Override
	public void run() {

		ByteArrayOutputStream outputStream = null;

		try {

			StringReader reader = new StringReader(printConfig);

			StringWriter writer = new StringWriter();

			String url = executor.getPrintServiceAddress() + "/pdf/create.json";

			HTTPUtils.sendHTTPPostRequest(reader, new URL(url), writer, "UTF-8", executor.getMapFishConnectionTimeout(), executor.getMapFishReadTimeout());

			String mapImageURL = writer.toString();

			if (mapImageURL != null) {

				mapImageURL = mapImageURL.substring(11, mapImageURL.length() - 2);

				if (HTTPUtils.isValidURL(mapImageURL)) {

					log.info("Generatated map image: " + mapImageURL + " for queryInstance " + executor.getQueryInstance() + " for user " + executor.getUser());

					outputStream = new ByteArrayOutputStream();

					HTTPUtils.sendHTTPGetRequest(mapImageURL, null, outputStream);

					generatedMapImage = new SerialBlob(outputStream.toByteArray());

					return;
				}

			}

			log.error("Invalid response from print service when generating png for queryInstance " + executor.getQueryInstance() + " for user " + executor.getUser());

		} catch (SocketTimeoutException e) {

			log.log(executor.getFishTimeoutLogLevel().getLevel(), "Unable to generate png for queryInstance " + executor.getQueryInstance() + " for user " + executor.getUser(), e);

		} catch (Exception e) {

			log.log(executor.getFishErrorLogLevel().getLevel(), "Unable to generate png for queryInstance " + executor.getQueryInstance() + " for user " + executor.getUser(), e);

		} finally {

			CloseUtils.close(outputStream);

		}

		generatedMapImage = null;
	}

	public SerialBlob getGeneratedMapImage() {

		return generatedMapImage;
	}

}
