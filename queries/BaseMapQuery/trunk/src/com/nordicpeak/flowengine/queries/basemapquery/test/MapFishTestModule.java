package com.nordicpeak.flowengine.queries.basemapquery.test;

import it.sauronsoftware.cron4j.Scheduler;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.SocketTimeoutException;
import java.net.URL;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import se.unlogic.hierarchy.core.annotations.ModuleSetting;
import se.unlogic.hierarchy.core.annotations.TextAreaSettingDescriptor;
import se.unlogic.hierarchy.core.annotations.TextFieldSettingDescriptor;
import se.unlogic.hierarchy.core.annotations.WebPublic;
import se.unlogic.hierarchy.core.beans.SimpleForegroundModuleResponse;
import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.hierarchy.core.enums.SystemStatus;
import se.unlogic.hierarchy.core.interfaces.ForegroundModuleDescriptor;
import se.unlogic.hierarchy.core.interfaces.ForegroundModuleResponse;
import se.unlogic.hierarchy.core.interfaces.SectionInterface;
import se.unlogic.hierarchy.core.interfaces.SystemStartupListener;
import se.unlogic.hierarchy.foregroundmodules.AnnotatedForegroundModule;
import se.unlogic.standardutils.hash.HashAlgorithms;
import se.unlogic.standardutils.hash.HashUtils;
import se.unlogic.standardutils.io.CloseUtils;
import se.unlogic.standardutils.string.StringUtils;
import se.unlogic.standardutils.time.TimeUtils;
import se.unlogic.webutils.http.HTTPUtils;
import se.unlogic.webutils.http.URIParser;


public class MapFishTestModule extends AnnotatedForegroundModule implements Runnable, SystemStartupListener {

	@ModuleSetting
	@TextAreaSettingDescriptor(name = "Map image config", description = "The map image config to use when generating map image")
	protected String mapImageConfig;
	
	@ModuleSetting
	@TextFieldSettingDescriptor(name = "Expected hashed response (MD5)", description = "The expected hashed response using MD5", required = false)
	protected String expectedHashedResponse;
	
	@ModuleSetting
	@TextFieldSettingDescriptor(name="MapFish connection timeout", description="MapFish connection timeout")
	protected Integer mapFishConnectionTimeout = 5000;

	@ModuleSetting
	@TextFieldSettingDescriptor(name="MapFish read timeout", description="MapFish read timeout")	
	protected Integer mapFishReadTimeout = 10000;
	
	@ModuleSetting
	@TextFieldSettingDescriptor(name="MapFish server address",description="The address to the MapFish server", required=true)
	protected String mapFishServerAddress;
	
	@ModuleSetting
	@TextFieldSettingDescriptor(name = "MapFish request interval", description = "The interval between map fish requests", required = false)
	protected String intervalPattern = "* * * * *";
	
	private Scheduler taskScheduler;
	
	private boolean logAsError = true;
	
	@Override
	public void init(ForegroundModuleDescriptor moduleDescriptor, SectionInterface sectionInterface, DataSource dataSource) throws Exception {

		super.init(moduleDescriptor, sectionInterface, dataSource);

		if(systemInterface.getSystemStatus() == SystemStatus.STARTED){

			this.initTaskScheduler();

		}else{

			this.systemInterface.addStartupListener(this);
		}

	}

	@Override
	public void update(ForegroundModuleDescriptor moduleDescriptor, DataSource dataSource) throws Exception {

		super.update(moduleDescriptor, dataSource);

		this.stopTaskScheduler();

		this.initTaskScheduler();
	}
	
	@WebPublic(alias="gethash")
	public ForegroundModuleResponse generateExcpectedResponse(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Exception {

		byte [] mapImage = getMapImageFromMapFish();
		
		String response = new String(mapImage);
		
		String hash = HashUtils.hash(response, HashAlgorithms.SHA1);
		
		return new SimpleForegroundModuleResponse(hash, this.getDefaultBreadcrumb());
	
	}
	
	@Override
	public void run() {

		ByteArrayOutputStream outputStream = null;
		
		try {
			
			if(StringUtils.isEmpty(mapImageConfig)) {
				
				log.warn("No map image config is set, check module settings");
			
				return;
			}
			
			if(!HTTPUtils.isValidURL(mapFishServerAddress)) {
				
				log.warn("Invalid MapFish server address is set, check module settings");
			
				return;
			}
			
			long startTime = System.currentTimeMillis();
			
			byte [] mapImage = getMapImageFromMapFish();
			
			if(expectedHashedResponse != null) {
				
				String hash = HashUtils.hash(new String(mapImage), HashAlgorithms.SHA1);
				
				if(!hash.equals(expectedHashedResponse)) {
					
					log.error("Response from MapFish when generating map image using server " + mapFishServerAddress + " does not matching expected response");
					
					return;
				}
				
			}
			
			long finishedTime = System.currentTimeMillis();
			
			log.info("Successfully generated map image in " + TimeUtils.getSeconds(finishedTime - startTime) + " seconds");
			
		} catch (SocketTimeoutException e) {
			
			if(logAsError) {
			
				log.error("Socket timeout occured when generating map image, using server " + mapFishServerAddress + ". No further error messages will be sent until next successfully generated map image", e);
			
				logAsError = false;
			
				return;	
			} 
			
			log.warn("Socket timeout occured when generating map image, using server " + mapFishServerAddress + ".");
			
		} catch (Exception e) {
			
			if(logAsError) {
				
				log.error("Unable to generate map image, using server " + mapFishServerAddress + ". No further error messages will be sent until next successfully generated map image", e);
			
				logAsError = false;
			
				return;	
			} 
			
			log.warn("Unable to generate map image, using server " + mapFishServerAddress + ".");
			
		} finally {
			
			CloseUtils.close(outputStream);
			
		}
		
	}	
	
	protected byte[] getMapImageFromMapFish() throws IOException {
		
		ByteArrayOutputStream outputStream = null;
		
		StringReader reader = new StringReader(mapImageConfig);
		
		StringWriter writer = new StringWriter();
		
		HTTPUtils.sendHTTPPostRequest(reader, new URL(mapFishServerAddress + "/pdf/create.json"), writer, "UTF-8", mapFishConnectionTimeout, mapFishReadTimeout);
	
		String mapImageURL = writer.toString();

		if(mapImageURL != null) {
			
			mapImageURL = mapImageURL.substring(11, mapImageURL.length()-2);
			
			if(HTTPUtils.isValidURL(mapImageURL)) {

				log.info("Generatated map image: " + mapImageURL + ", using MapFish server " + mapFishServerAddress);
				
				outputStream = new ByteArrayOutputStream();
				
				HTTPUtils.sendHTTPGetRequest(mapImageURL, null, outputStream);
				
				logAsError = true;
				
				return outputStream.toByteArray();
				
			}
			
			throw new RuntimeException("Invalid map image URL in response from MapFish");
			
		} else {
			
			throw new RuntimeException("No map image URL in response from MapFish");
			
		}
		
	}
	
	@Override
	public void systemStarted() {

		this.initTaskScheduler();

	}

	private void initTaskScheduler(){
		
		this.taskScheduler = new Scheduler();
	
		this.taskScheduler.schedule(this.intervalPattern, this);
		
		this.taskScheduler.start();
		
	}
	
	private void stopTaskScheduler() {
		
		if (this.taskScheduler != null && this.taskScheduler.isStarted()) {
			this.taskScheduler.stop();
		}
		
	}
	
	@Override
	public void unload() throws Exception {
		
		stopTaskScheduler();
		
		super.unload();
	}

}
