package com.nordicpeak.flowengine.beans;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

import se.unlogic.standardutils.collections.CollectionUtils;
import se.unlogic.standardutils.dao.annotations.DAOManaged;
import se.unlogic.standardutils.dao.annotations.Key;
import se.unlogic.standardutils.dao.annotations.ManyToOne;
import se.unlogic.standardutils.dao.annotations.OrderBy;
import se.unlogic.standardutils.dao.annotations.Table;
import se.unlogic.standardutils.date.DateUtils;
import se.unlogic.standardutils.json.JsonArray;
import se.unlogic.standardutils.json.JsonNode;
import se.unlogic.standardutils.json.JsonObject;
import se.unlogic.standardutils.time.TimeUtils;
import se.unlogic.standardutils.xml.GeneratedElementable;
import se.unlogic.standardutils.xml.XMLElement;

@Table(name = "flowengine_external_message_read_receipt_attachment_downloads")
@XMLElement
public class ExternalMessageReadReceiptAttachmentDownload extends GeneratedElementable implements Serializable {

	private static final long serialVersionUID = -8461833104836299216L;

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

		return "(attachmentFilename=" + attachmentFilename + ")";
	}
	
	public JsonNode toJson() {

		JsonObject jsonObject = new JsonObject(2);

		jsonObject.putField("attachmentFilename", attachmentFilename);
		jsonObject.putField("downloaded", DateUtils.DATE_TIME_FORMATTER.format(downloaded));

		return jsonObject;
	}

	public static JsonNode toJson(List<ExternalMessageReadReceiptAttachmentDownload> attachmentDownloads) {

		if (CollectionUtils.isEmpty(attachmentDownloads)) {

			return new JsonArray(0);
		}

		JsonArray jsonArray = new JsonArray(attachmentDownloads.size());

		attachmentDownloads.forEach(a -> jsonArray.addNode(a.toJson()));

		return jsonArray;
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
