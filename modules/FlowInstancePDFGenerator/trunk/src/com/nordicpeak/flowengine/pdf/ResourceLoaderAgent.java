package com.nordicpeak.flowengine.pdf;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;

import org.apache.log4j.Logger;
import org.xhtmlrenderer.extend.FSImage;
import org.xhtmlrenderer.pdf.ITextFSImage;
import org.xhtmlrenderer.pdf.ITextOutputDevice;
import org.xhtmlrenderer.pdf.ITextUserAgent;
import org.xhtmlrenderer.resource.ImageResource;
import org.xhtmlrenderer.util.XRLog;

import com.lowagie.text.Image;
import com.nordicpeak.flowengine.beans.PDFQueryResponse;
import com.nordicpeak.flowengine.managers.PDFManagerResponse;

import se.unlogic.standardutils.io.FileUtils;
import se.unlogic.standardutils.numbers.NumberUtils;
import se.unlogic.standardutils.streams.StreamUtils;
import se.unlogic.standardutils.xml.ClassPathURIResolver;


public class ResourceLoaderAgent extends ITextUserAgent {

	private final Logger log = Logger.getLogger(this.getClass()); 
	
	private static final String QUERY_RESOURCE_PREFIX = "query://";
	private static final String FILE_RESOURCE_PREFIX = "file://";

	private final List<PDFManagerResponse> managerResponses;

	public ResourceLoaderAgent(ITextOutputDevice outputDevice, List<PDFManagerResponse> managerResponses) {

		super(outputDevice);
		this.managerResponses = managerResponses;
	}

	@Override
	protected InputStream resolveAndOpenStream(String uri) {

		if(uri != null){
		
			if(uri.startsWith(QUERY_RESOURCE_PREFIX) && uri.length() > (QUERY_RESOURCE_PREFIX.length() + 2)){

				int end = uri.indexOf("/", QUERY_RESOURCE_PREFIX.length());

				if(end != -1 && (end+1) < uri.length()){

					Integer queryID = NumberUtils.toInt(uri.substring(QUERY_RESOURCE_PREFIX.length(), end));

					if(queryID != null){

						for(PDFManagerResponse managerResponse : managerResponses){

							for(PDFQueryResponse queryResponse : managerResponse.getQueryResponses()){

								if(queryResponse.getQueryDescriptor().getQueryID().equals(queryID)){

									if(queryResponse.getPdfResourceProvider() != null){

										String remainingURI = uri.substring(end);
										
										try {
											return queryResponse.getPdfResourceProvider().getResource(remainingURI);
											
										} catch (Exception e) {

											log.error("Error getting resouce " + remainingURI + " from query " + queryResponse.getQueryDescriptor(), e);
										}
									}

									return null;
								}
							}
						}
					}
				}
				
			}else if(uri.startsWith(ClassPathURIResolver.PREFIX) && uri.length() > ClassPathURIResolver.PREFIX.length()){
				
				URL url = ClassPathURIResolver.getURL(uri);
				
				if(url != null){
					
					try {
						return url.openStream();
						
					} catch (IOException e) {
						
						log.error("Unable to open stream for uri " + uri,e);
					}
					
				}else{
					
					log.warn("Unable to find resource for uri " + uri);
				}
				
				return null;
				
			} else if (uri.startsWith(FILE_RESOURCE_PREFIX) && uri.length() > FILE_RESOURCE_PREFIX.length()) {

				File file = new File(uri.substring(FILE_RESOURCE_PREFIX.length()));

				if (FileUtils.isReadable(file)) {

					try {
						return new BufferedInputStream(new FileInputStream(file));

					} catch (IOException e) {

						log.error("Unable to open stream for uri " + uri, e);
					}

				} else {

					log.warn("Unable to find file for uri " + uri);
				}

				return null;
			}
		}
		

		InputStream is = super.resolveAndOpenStream(uri);

		if(is == null){
			
			log.warn("Unable to resolve uri: " + uri);
		}
		
		return is;
	}

	@Override
	public String resolveURI(String uri) {

		if(uri != null && uri.startsWith(ClassPathURIResolver.PREFIX) || uri.startsWith(QUERY_RESOURCE_PREFIX)){
			
			return uri;
		}
		
		return uri;
	}

	@SuppressWarnings("unchecked")
	@Override
	public ImageResource getImageResource(String uri) {

		ImageResource resource = null;
		uri = resolveURI(uri);
		resource = (ImageResource) _imageCache.get(uri);
		if (resource == null) {
			InputStream is = resolveAndOpenStream(uri);
			if (is != null) {
				try {
					Image image = Image.getInstance(StreamUtils.toByteArray(is));
					scaleToOutputResolution(image);
					resource = new ImageResource(uri, new ITextFSImage(image));
					_imageCache.put(uri, resource);
				} catch (Exception e) {
					XRLog.exception("Can't read image file; unexpected problem for URI '" + uri + "'", e);
				} finally {
					try {
						is.close();
					} catch (IOException e) {}
				}
			}
		}

		if (resource != null) {
			resource = new ImageResource(resource.getImageUri(), (FSImage) ((ITextFSImage) resource.getImage()).clone());
		} else {
			resource = new ImageResource(uri, null);
		}

		return resource;
	}

    protected void scaleToOutputResolution(Image image) {
        float factor = getSharedContext().getDotsPerPixel();
        image.scaleAbsolute(image.getPlainWidth() * factor, image.getPlainHeight() * factor);
    }	
	
	@Override
	public byte[] getBinaryResource(String arg0) {

		// TODO Auto-generated method stub
		return super.getBinaryResource(arg0);
	}
}
