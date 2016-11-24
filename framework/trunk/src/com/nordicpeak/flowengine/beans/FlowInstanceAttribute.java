package com.nordicpeak.flowengine.beans;

import se.unlogic.hierarchy.core.beans.SimpleAttribute;
import se.unlogic.standardutils.dao.annotations.DAOManaged;
import se.unlogic.standardutils.dao.annotations.Key;
import se.unlogic.standardutils.dao.annotations.ManyToOne;
import se.unlogic.standardutils.dao.annotations.Table;
import se.unlogic.standardutils.xml.XMLElement;

@Table(name = "flowengine_flow_instance_attributes")
@XMLElement(name = "Attribute")
public class FlowInstanceAttribute extends SimpleAttribute {

	private static final long serialVersionUID = 8352980246886449952L;

	@DAOManaged(columnName = "flowInstanceID")
	@Key
	@ManyToOne
	protected FlowInstance flowInstance;

	public FlowInstanceAttribute() {}


	public FlowInstanceAttribute(String name, String value) {

		super(name, value);
	}

	public FlowInstance getFlowInstance() {

		return flowInstance;
	}

	public void setFlowInstance(FlowInstance flowInstance) {

		this.flowInstance = flowInstance;
	}


}
