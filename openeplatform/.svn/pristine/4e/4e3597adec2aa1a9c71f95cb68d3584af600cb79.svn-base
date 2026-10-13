package com.nordicpeak.flowengine.queries.childquery.beans;

import java.io.Serializable;

import se.unlogic.standardutils.dao.annotations.DAOManaged;
import se.unlogic.standardutils.dao.annotations.Key;
import se.unlogic.standardutils.dao.annotations.ManyToOne;
import se.unlogic.standardutils.dao.annotations.Table;
import se.unlogic.standardutils.xml.GeneratedElementable;
import se.unlogic.standardutils.xml.XMLElement;

@Table(name = "child_query_instance_attributes")
@XMLElement(name = "Attribute")
public class ChildAttribute extends GeneratedElementable implements Serializable {

	private static final long serialVersionUID = 7634780772195411123L;

	@Key
	@DAOManaged(columnName = "queryInstanceID")
	@ManyToOne
	private ChildQueryInstance queryInstance;

	@Key
	@XMLElement
	@DAOManaged
	private String name;

	@XMLElement
	@DAOManaged
	private String value;

	public ChildAttribute() {}

	public ChildAttribute(String name, String value) {
		super();
		this.name = name;
		this.value = value;
	}

	public ChildQueryInstance getQueryInstance() {
		return queryInstance;
	}

	public void setQueryInstance(ChildQueryInstance queryInstance) {
		this.queryInstance = queryInstance;
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

}
