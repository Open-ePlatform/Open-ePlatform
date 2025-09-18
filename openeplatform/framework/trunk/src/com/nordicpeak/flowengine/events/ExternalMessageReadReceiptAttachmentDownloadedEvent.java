package com.nordicpeak.flowengine.events;

import java.io.Serializable;

import se.unlogic.openhierarchy.foregroundmodules.siteprofile.interfaces.SiteProfile;

import com.nordicpeak.flowengine.beans.ExternalMessageReadReceiptAttachmentDownload;
import com.nordicpeak.flowengine.beans.FlowInstance;

public class ExternalMessageReadReceiptAttachmentDownloadedEvent implements Serializable {

	private static final long serialVersionUID = 1873981649620240956L;

	private final FlowInstance flowInstance;
	private final SiteProfile siteProfile;
	private final ExternalMessageReadReceiptAttachmentDownload attachmentDownload;

	public ExternalMessageReadReceiptAttachmentDownloadedEvent(FlowInstance flowInstance, SiteProfile siteProfile, ExternalMessageReadReceiptAttachmentDownload attachmentDownload) {

		this.flowInstance = flowInstance;
		this.siteProfile = siteProfile;
		this.attachmentDownload = attachmentDownload;
	}

	public FlowInstance getFlowInstance() {

		return flowInstance;
	}

	public SiteProfile getSiteProfile() {

		return siteProfile;
	}

	public ExternalMessageReadReceiptAttachmentDownload getAttachmentDownload() {

		return attachmentDownload;
	}
}
