package com.nordicpeak.flowengine.queries.childquery.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import se.unlogic.standardutils.dao.annotations.DAOManaged;
import se.unlogic.standardutils.dao.annotations.Key;
import se.unlogic.standardutils.dao.annotations.ManyToOne;
import se.unlogic.standardutils.dao.annotations.Table;
import se.unlogic.standardutils.populators.EnumPopulator;
import se.unlogic.standardutils.populators.StringPopulator;
import se.unlogic.standardutils.validation.ValidationError;
import se.unlogic.standardutils.validation.ValidationException;
import se.unlogic.standardutils.xml.GeneratedElementable;
import se.unlogic.standardutils.xml.XMLElement;
import se.unlogic.standardutils.xml.XMLParser;
import se.unlogic.standardutils.xml.XMLParserPopulateable;
import se.unlogic.standardutils.xml.XMLValidationUtils;

import com.nordicpeak.flowengine.queries.childquery.enums.ChildAttributeDisplayMode;

@Table(name = "child_query_selected_attributes")
@XMLElement(name = "SelectedAttribute")
public class SelectedChildAttribute extends GeneratedElementable implements XMLParserPopulateable, Serializable {

	private static final long serialVersionUID = -5407922522706409574L;
	
	public static final EnumPopulator<ChildAttributeDisplayMode> DISPLAY_MODE_POPULATOR = new EnumPopulator<ChildAttributeDisplayMode>(ChildAttributeDisplayMode.class);

	@DAOManaged(columnName = "queryID")
	@Key
	@ManyToOne
	private ChildQuery query;

	@DAOManaged
	@Key
	@XMLElement
	private String name;

	@DAOManaged
	@XMLElement
	private ChildAttributeDisplayMode displayMode;

	public SelectedChildAttribute() {}

	public SelectedChildAttribute(String name, ChildAttributeDisplayMode displayMode) {
		super();
		this.name = name;
		this.displayMode = displayMode;
	}

	public ChildQuery getQuery() {
		return query;
	}

	public void setQuery(ChildQuery query) {
		this.query = query;
	}

	public String getName() {
		return name;
	}

	public ChildAttributeDisplayMode getDisplayMode() {
		return displayMode;
	}

	@Override
	public String toString() {

		return name + " (displayMode: " + displayMode + ")";
	}

	@Override
	public void populate(XMLParser xmlParser) throws ValidationException {

		List<ValidationError> errors = new ArrayList<ValidationError>();

		name = XMLValidationUtils.validateParameter("name", xmlParser, true, 1, 255, StringPopulator.getPopulator(), errors);
		displayMode = XMLValidationUtils.validateParameter("displayName", xmlParser, true, 1, 255, DISPLAY_MODE_POPULATOR, errors);

		if (!errors.isEmpty()) {

			throw new ValidationException(errors);
		}
	}
}
