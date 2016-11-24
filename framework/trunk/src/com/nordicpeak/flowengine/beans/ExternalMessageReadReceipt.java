package com.nordicpeak.flowengine.beans;

import se.unlogic.standardutils.dao.annotations.DAOManaged;
import se.unlogic.standardutils.dao.annotations.Key;
import se.unlogic.standardutils.dao.annotations.ManyToOne;
import se.unlogic.standardutils.dao.annotations.Table;
import se.unlogic.standardutils.xml.XMLElement;

@Table(name = "flowengine_external_message_read_receipts")
@XMLElement
public class ExternalMessageReadReceipt extends BaseMessageReadReceipt {

	@DAOManaged(columnName = "messageID")
	@Key
	@ManyToOne
	@XMLElement
	private ExternalMessage message;

	@Override
	public ExternalMessage getMessage() {

		return message;
	}

	public void setMessage(ExternalMessage message) {

		this.message = message;
	}

}
