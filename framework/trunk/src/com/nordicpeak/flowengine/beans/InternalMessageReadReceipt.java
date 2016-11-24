package com.nordicpeak.flowengine.beans;

import se.unlogic.standardutils.dao.annotations.DAOManaged;
import se.unlogic.standardutils.dao.annotations.Key;
import se.unlogic.standardutils.dao.annotations.ManyToOne;
import se.unlogic.standardutils.dao.annotations.Table;
import se.unlogic.standardutils.xml.XMLElement;

@Table(name = "flowengine_internal_message_read_receipts")
@XMLElement
public class InternalMessageReadReceipt extends BaseMessageReadReceipt {

	@DAOManaged(columnName = "messageID")
	@Key
	@ManyToOne
	@XMLElement
	private InternalMessage message;

	@Override
	public InternalMessage getMessage() {

		return message;
	}

	public void setMessage(InternalMessage message) {

		this.message = message;
	}

}
