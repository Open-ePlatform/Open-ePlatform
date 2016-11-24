package com.nordicpeak.flowengine.beans;

import java.lang.reflect.Field;

import se.unlogic.standardutils.dao.annotations.DAOManaged;
import se.unlogic.standardutils.dao.annotations.ManyToOne;
import se.unlogic.standardutils.dao.annotations.Table;
import se.unlogic.standardutils.reflection.ReflectionUtils;
import se.unlogic.standardutils.xml.XMLElement;

@Table(name = "flowengine_internal_message_attachments")
@XMLElement
public class InternalMessageAttachment extends BaseAttachment {

	public static final Field DATA_FIELD = ReflectionUtils.getField(InternalMessageAttachment.class, "data");
	
	@DAOManaged(columnName = "messageID")
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
