package com.nordicpeak.flowengine.beans;

import se.unlogic.hierarchy.core.beans.SimpleAttribute;
import se.unlogic.standardutils.dao.annotations.DAOManaged;
import se.unlogic.standardutils.dao.annotations.Key;
import se.unlogic.standardutils.dao.annotations.ManyToOne;
import se.unlogic.standardutils.dao.annotations.Table;
import se.unlogic.standardutils.xml.XMLElement;

import com.nordicpeak.flowengine.interfaces.ImmutableFlowInstanceEvent;

@Table(name = "flowengine_flow_instance_event_attributes")
@XMLElement(name = "Attribute")
public class FlowInstanceEventAttribute extends SimpleAttribute {

	private static final long serialVersionUID = 8352980246886449952L;

	@DAOManaged(columnName = "eventID")
	@Key
	@ManyToOne
	protected FlowInstanceEvent event;

	public FlowInstanceEventAttribute() {}

	
	public FlowInstanceEventAttribute(String name, String value) {

		super(name, value);
	}

	public ImmutableFlowInstanceEvent getEvent() {

		return event;
	}

	public void setEvent(FlowInstanceEvent event) {

		this.event = event;
	}
}
