package com.nordicpeak.flowengine.beans;

import java.sql.Timestamp;

import se.unlogic.standardutils.dao.annotations.DAOManaged;
import se.unlogic.standardutils.dao.annotations.Key;
import se.unlogic.standardutils.dao.annotations.ManyToOne;
import se.unlogic.standardutils.dao.annotations.OrderBy;
import se.unlogic.standardutils.dao.annotations.Table;
import se.unlogic.standardutils.time.TimeUtils;
import se.unlogic.standardutils.xml.GeneratedElementable;
import se.unlogic.standardutils.xml.XMLElement;

@Table(name = "flowengine_external_message_read_receipt_attachment_downloads")
@XMLElement
public class ExternalMessageReadReceiptAttachmentDownload extends GeneratedElementable {

	@DAOManaged(columnName = "readReceiptID")
	@Key
	@ManyToOne
	@XMLElement
	private ExternalMessageReadReceipt readReceipt;

	@DAOManaged
	@XMLElement
	protected String attachmentFilename;

	@DAOManaged
	@OrderBy
	@XMLElement
	protected Timestamp downloaded;

	public ExternalMessageReadReceiptAttachmentDownload() {}

	public ExternalMessageReadReceiptAttachmentDownload(ExternalMessageReadReceipt readReceipt, String attachmentFilename) {

		this.readReceipt = readReceipt;
		this.attachmentFilename = attachmentFilename;
		this.downloaded = TimeUtils.getCurrentTimestamp();
	}

	@Override
	public String toString() {

		return "(readReceipt=" + readReceipt + ", attachmentFilename=" + attachmentFilename + ")";
	}

	public ExternalMessageReadReceipt getReadReceipt() {

		return readReceipt;
	}

	public void setReadReceipt(ExternalMessageReadReceipt readReceipt) {

		this.readReceipt = readReceipt;
	}

	public String getAttachmentFilename() {

		return attachmentFilename;
	}

	public void setAttachmentFilename(String attachmentFilename) {

		this.attachmentFilename = attachmentFilename;
	}
}
