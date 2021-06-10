package com.nordicpeak.flowengine.interfaces;

import java.io.File;
import java.util.List;
import java.util.Map;

import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.openhierarchy.foregroundmodules.siteprofile.interfaces.SiteProfile;

import com.nordicpeak.flowengine.beans.FlowInstanceEvent;
import com.nordicpeak.flowengine.managers.FlowInstanceManager;
import com.nordicpeak.flowengine.utils.PDFByteAttachment;

public interface PDFProvider {

	File getPDF(Integer flowInstanceID, Integer eventID);

	File getTemporaryPDF(FlowInstanceManager instanceManager);

	File createTemporaryPDF(FlowInstanceManager instanceManager, SiteProfile profile, User user) throws Exception;

	File createTemporaryPDF(FlowInstanceManager instanceManager, SiteProfile profile, User user, Map<String, String> extraElements) throws Exception;

	File createTemporaryPDF(FlowInstanceManager instanceManager, SiteProfile profile, User user, Map<String, String> extraElements, FlowInstanceEvent event) throws Exception;

	public boolean saveTemporaryPDF(FlowInstanceManager instanceManager, FlowInstanceEvent flowInstanceEvent) throws Exception;

	public boolean deleteTemporaryPDF(FlowInstanceManager instanceManager) throws Exception;

	public boolean hasTemporaryPDF(FlowInstanceManager instanceManager);

	public List<PDFByteAttachment> getPDFAttachments(File pdfFile, boolean getData) throws Exception;

	public byte[] removePDFAttachments(File pdfFile) throws Exception;

	public boolean deletePDF(Integer flowInstanceID, Integer eventID);

	public String getLogotype(SiteProfile siteProfile);

	@Deprecated
	public List<String> getIncludedFonts();
	
	@Deprecated
	public File getTempDir();
}
