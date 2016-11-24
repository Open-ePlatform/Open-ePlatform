package com.nordicpeak.flowengine.beans;

import java.lang.reflect.Field;

import se.unlogic.standardutils.dao.annotations.DAOManaged;
import se.unlogic.standardutils.dao.annotations.ManyToOne;
import se.unlogic.standardutils.dao.annotations.Table;
import se.unlogic.standardutils.reflection.ReflectionUtils;
import se.unlogic.standardutils.xml.XMLElement;

@Table(name = "flowengine_external_message_attachments")
@XMLElement
public class ExternalMessageAttachment extends BaseAttachment {

	public static final Field DATA_FIELD = ReflectionUtils.getField(ExternalMessageAttachment.class, "data");
	
	@DAOManaged(columnName = "messageID")
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
