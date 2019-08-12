package com.nordicpeak.flowengine.beans;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import se.unlogic.standardutils.dao.annotations.DAOManaged;
import se.unlogic.standardutils.dao.annotations.Key;
import se.unlogic.standardutils.dao.annotations.ManyToOne;
import se.unlogic.standardutils.dao.annotations.OrderBy;
import se.unlogic.standardutils.dao.annotations.Table;
import se.unlogic.standardutils.populators.StringPopulator;
import se.unlogic.standardutils.reflection.ReflectionUtils;
import se.unlogic.standardutils.validation.ValidationError;
import se.unlogic.standardutils.validation.ValidationException;
import se.unlogic.standardutils.xml.GeneratedElementable;
import se.unlogic.standardutils.xml.XMLElement;
import se.unlogic.standardutils.xml.XMLParser;
import se.unlogic.standardutils.xml.XMLParserPopulateable;
import se.unlogic.standardutils.xml.XMLValidationUtils;

@Table(name = "flowengine_flow_overview_attributes")
@XMLElement
public class FlowOverviewAttribute extends GeneratedElementable implements Serializable, XMLParserPopulateable {

	private static final long serialVersionUID = -818894046264852122L;

	public static final Field FLOW_RELATION = ReflectionUtils.getField(FlowOverviewAttribute.class, "flow");

	@DAOManaged(columnName = "flowID")
	@Key
	@ManyToOne
	@XMLElement
	private Flow flow;

	@DAOManaged
	@Key
	@XMLElement
	private String name;

	@DAOManaged
	@XMLElement
	protected String value;

	@DAOManaged
	@OrderBy
	@XMLElement
	private Integer sortIndex;

	public Flow getFlow() {
		return flow;
	}

	public void setFlow(Flow flow) {
		this.flow = flow;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Integer getSortIndex() {
		return sortIndex;
	}

	public void setSortIndex(Integer sortIndex) {
		this.sortIndex = sortIndex;
	}

	@Override
	public void populate(XMLParser xmlParser) throws ValidationException {

		List<ValidationError> errors = new ArrayList<ValidationError>();

		name = XMLValidationUtils.validateParameter("name", xmlParser, false, 1, 50, StringPopulator.getPopulator(), errors);
		value = XMLValidationUtils.validateParameter("value", xmlParser, false, 1, 1024, StringPopulator.getPopulator(), errors);
		sortIndex = xmlParser.getInteger("sortIndex");

		if (!errors.isEmpty()) {
			throw new ValidationException(errors);
		}
	}
}
