package com.nordicpeak.flowengine.flowapprovalmodule;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.apache.log4j.Logger;
import org.xhtmlrenderer.extend.FSImage;
import org.xhtmlrenderer.pdf.ITextFSImage;
import org.xhtmlrenderer.pdf.ITextOutputDevice;
import org.xhtmlrenderer.pdf.ITextUserAgent;
import org.xhtmlrenderer.resource.ImageResource;
import org.xhtmlrenderer.util.XRLog;

import se.unlogic.standardutils.io.FileUtils;
import se.unlogic.standardutils.streams.StreamUtils;
import se.unlogic.standardutils.xml.ClassPathURIResolver;

import com.lowagie.text.Image;

public class ResourceLoaderAgent extends ITextUserAgent {

	private final Logger log = Logger.getLogger(this.getClass());

	private static final String FILE_RESOURCE_PREFIX = "file://";

	public ResourceLoaderAgent(ITextOutputDevice outputDevice) {

		super(outputDevice);
	}

	@Override
	protected InputStream resolveAndOpenStream(String uri) {

		if (uri != null) {

			if (uri.startsWith(ClassPathURIResolver.PREFIX) && uri.length() > ClassPathURIResolver.PREFIX.length()) {

				URL url = ClassPathURIResolver.getURL(uri);

				if (url != null) {

					try {
						return url.openStream();

					} catch (IOException e) {

						log.error("Unable to open stream for uri " + uri, e);
					}

				} else {

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

		if (is == null) {

			log.warn("Unable to resolve uri: " + uri);
		}

		return is;
	}

	@Override
	public String resolveURI(String uri) {

		if (uri != null && uri.startsWith(ClassPathURIResolver.PREFIX)) {

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

		return super.getBinaryResource(arg0);
	}
}
