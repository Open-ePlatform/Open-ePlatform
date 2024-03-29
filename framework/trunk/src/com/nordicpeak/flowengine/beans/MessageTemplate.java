package com.nordicpeak.flowengine.beans;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import se.unlogic.standardutils.annotations.WebPopulate;
import se.unlogic.standardutils.dao.annotations.DAOManaged;
import se.unlogic.standardutils.dao.annotations.Key;
import se.unlogic.standardutils.dao.annotations.ManyToOne;
import se.unlogic.standardutils.dao.annotations.OrderBy;
import se.unlogic.standardutils.dao.annotations.Table;
import se.unlogic.standardutils.populators.EnumPopulator;
import se.unlogic.standardutils.populators.StringPopulator;
import se.unlogic.standardutils.reflection.ReflectionUtils;
import se.unlogic.standardutils.validation.ValidationError;
import se.unlogic.standardutils.validation.ValidationException;
import se.unlogic.standardutils.xml.GeneratedElementable;
import se.unlogic.standardutils.xml.XMLElement;
import se.unlogic.standardutils.xml.XMLParser;
import se.unlogic.standardutils.xml.XMLParserPopulateable;
import se.unlogic.standardutils.xml.XMLValidationUtils;

import com.nordicpeak.flowengine.enums.MessageTemplateType;

@Table(name = "flowengine_flow_family_message_templates")
@XMLElement
public class MessageTemplate extends GeneratedElementable implements Serializable, XMLParserPopulateable {

	private static final EnumPopulator<MessageTemplateType> TYPE_POPULATOR = new EnumPopulator<>(MessageTemplateType.class);

	public static final Field FLOW_FAMILY_RELATION = ReflectionUtils.getField(MessageTemplate.class, "flowFamily");

	private static final long serialVersionUID = 3268646669132872847L;

	@DAOManaged(autoGenerated = true)
	@Key
	@XMLElement
	private Integer templateID;

	@DAOManaged(columnName = "flowFamilyID")
	@ManyToOne
	@XMLElement
	private FlowFamily flowFamily;

	@DAOManaged
	@OrderBy
	@WebPopulate(maxLength = 255, required = true, paramName = "messageTemplateName")
	@XMLElement
	private String name;

	@DAOManaged
	@WebPopulate(maxLength = 65535, required = true, paramName = "messageTemplateMessage")
	@XMLElement
	private String message;

	@DAOManaged
	@WebPopulate(required = true, paramName = "messageTemplateType")
	@XMLElement
	private MessageTemplateType type;

	public Integer getTemplateID() {

		return templateID;
	}

	public void setTemplateID(Integer templateID) {

		this.templateID = templateID;
	}

	public FlowFamily getFlowFamily() {

		return flowFamily;
	}

	public void setFlowFamily(FlowFamily flowFamily) {

		this.flowFamily = flowFamily;
	}

	public String getName() {

		return name;
	}

	public void setName(String name) {

		this.name = name;
	}

	public String getMessage() {

		return message;
	}

	public void setMessage(String message) {

		this.message = message;
	}

	public MessageTemplateType getType() {

		return type;
	}

	public void setType(MessageTemplateType type) {

		this.type = type;
	}

	@Override
	public void populate(XMLParser xmlParser) throws ValidationException {

		List<ValidationError> errors = new ArrayList<>();

		name = XMLValidationUtils.validateParameter("name", xmlParser, true, 1, 255, StringPopulator.getPopulator(), errors);
		message = XMLValidationUtils.validateParameter("message", xmlParser, true, 1, 65535, StringPopulator.getPopulator(), errors);
		type = XMLValidationUtils.validateParameter("type", xmlParser, false, TYPE_POPULATOR, errors);
		
		if (type == null) {
			
			type = MessageTemplateType.EXTERNAL;
		}

		if (!errors.isEmpty()) {

			throw new ValidationException(errors);
		}
	}

	@Override
	public String toString() {

		return getClass().getSimpleName() + " (templateID=" + templateID + ", name=" + name + ")";
	}

}
