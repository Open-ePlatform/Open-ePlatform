package com.nordicpeak.flowengine.notifications;

import java.io.File;

import se.unlogic.emailutils.framework.SimpleEmail;
import se.unlogic.openhierarchy.foregroundmodules.siteprofile.interfaces.SiteProfile;

import com.nordicpeak.flowengine.beans.Contact;
import com.nordicpeak.flowengine.interfaces.ImmutableFlowInstance;

public interface NotificationEmailFilter {

	public void filterGlobalEmail(SiteProfile siteProfile, ImmutableFlowInstance flowInstance, Contact contact, File pdfFile, String generatedPDFFilename, boolean sendPDFAttachmentsSeparately, File xmlFile, String generateXMLFilename, SimpleEmail email) throws Exception;

}
