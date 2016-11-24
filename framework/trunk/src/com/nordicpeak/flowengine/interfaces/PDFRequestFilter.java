package com.nordicpeak.flowengine.interfaces;

import java.io.File;

import se.unlogic.openhierarchy.foregroundmodules.siteprofile.interfaces.SiteProfile;

public interface PDFRequestFilter {
	
	public File processPDFRequest(File inputPDF, SiteProfile siteProfile);

}
