package com.nordicpeak.flowengine.beans;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.hierarchy.core.handlers.SourceAttributeHandler;
import se.unlogic.hierarchy.core.interfaces.attributes.AttributeHandler;
import se.unlogic.hierarchy.core.interfaces.attributes.AttributeSource;
import se.unlogic.hierarchy.core.interfaces.attributes.MutableAttributeHandler;
import se.unlogic.standardutils.dao.annotations.DAOManaged;
import se.unlogic.standardutils.dao.annotations.Key;
import se.unlogic.standardutils.dao.annotations.ManyToOne;
import se.unlogic.standardutils.dao.annotations.OneToMany;
import se.unlogic.standardutils.dao.annotations.OrderBy;
import se.unlogic.standardutils.dao.annotations.Table;
import se.unlogic.standardutils.reflection.ReflectionUtils;
import se.unlogic.standardutils.xml.GeneratedElementable;
import se.unlogic.standardutils.xml.XMLElement;
import se.unlogic.standardutils.xml.XMLUtils;

import com.nordicpeak.flowengine.enums.EventType;
import com.nordicpeak.flowengine.interfaces.ImmutableFlowInstanceEvent;

@Table(name = "flowengine_flow_instance_events")
@XMLElement
public class FlowInstanceEvent extends GeneratedElementable implements Serializable , AttributeSource, ImmutableFlowInstanceEvent, Comparable<FlowInstanceEvent>{

	private static final long serialVersionUID = -2876177448272548003L;

	public static final Field FLOW_INSTANCE_RELATION = ReflectionUtils.getField(FlowInstanceEvent.class,"flowInstance");
	public static final Field ATTRIBUTES_RELATION = ReflectionUtils.getField(FlowInstanceEvent.class,"attributes");
	
	public static final Field POSTER_FIELD = ReflectionUtils.getField(FlowInstanceEvent.class,"poster");

	@Key
	@DAOManaged(autoGenerated = true)
	@XMLElement
	private Integer eventID;

	@DAOManaged(columnName = "flowInstanceID")
	@ManyToOne
	@XMLElement
	private FlowInstance flowInstance;

	@DAOManaged
	@XMLElement
	private EventType eventType;

	@DAOManaged
	@XMLElement
	private String status;

	@DAOManaged
	@XMLElement
	private String statusDescription;

	@DAOManaged
	@XMLElement
	private String details;

	@DAOManaged
	@OrderBy
	@XMLElement
	private Timestamp added;

	@XMLElement
	private String shortDate;

	@DAOManaged(dontUpdateIfNull = true)
	private User poster;

	@DAOManaged
	@OneToMany
	@XMLElement(fixCase=true)
	private List<FlowInstanceEventAttribute> attributes;

	private SourceAttributeHandler attributeHandler;

	@Override
	public Integer getEventID() {
		return eventID;
	}

	public void setEventID(Integer eventID) {
		this.eventID = eventID;
	}

	@Override
	public FlowInstance getFlowInstance() {
		return flowInstance;
	}

	public void setFlowInstance(FlowInstance flowInstance) {
		this.flowInstance = flowInstance;
	}

	@Override
	public EventType getEventType() {
		return eventType;
	}

	public void setEventType(EventType eventType) {
		this.eventType = eventType;
	}

	@Override
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Override
	public String getStatusDescription() {
		return statusDescription;
	}

	public void setStatusDescription(String statusDescription) {
		this.statusDescription = statusDescription;
	}

	@Override
	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}

	@Override
	public Timestamp getAdded() {
		return added;
	}

	public void setAdded(Timestamp added) {
		this.added = added;
	}

	@Override
	public String getShortDate() {
		return shortDate;
	}

	public void setShortDate(String shortDate) {
		this.shortDate = shortDate;
	}

	@Override
	public User getPoster() {
		return poster;
	}

	public void setPoster(User poster) {
		this.poster = poster;
	}

	@Override
	public List<FlowInstanceEventAttribute> getAttributes() {

		return attributes;
	}

	public void setAttributes(List<FlowInstanceEventAttribute> attributes) {

		this.attributes = attributes;
	}

	@Override
	public void addAttribute(String name, String value) {

		if(this.attributes == null){

			attributes = new ArrayList<FlowInstanceEventAttribute>();
		}

		attributes.add(new FlowInstanceEventAttribute(name, value));
	}

	@Override
	public synchronized MutableAttributeHandler getAttributeHandler() {

		if(attributeHandler == null){

			this.attributeHandler = new SourceAttributeHandler(this, 255, 65536);
		}

		return attributeHandler;
	}

	@Override
	public String toString() {

		return eventType + " (eventID: " + eventID + ")";
	}

	@Override
	public Element toXML(Document doc) {

		Element flowInstanceEventElement = super.toXML(doc);

		if(poster != null){

			Element userElement = poster.toXML(doc);

			AttributeHandler attributeHandler = poster.getAttributeHandler();

			if(attributeHandler != null && !attributeHandler.isEmpty()){

				userElement.appendChild(attributeHandler.toXML(doc));
			}

			Element posterElement = XMLUtils.appendNewElement(doc, flowInstanceEventElement, "poster");

			posterElement.appendChild(userElement);
		}

		return flowInstanceEventElement;
	}
	
	@Override
	public int compareTo(FlowInstanceEvent o) {

		Timestamp a1 = getAdded();
		Timestamp a2 = o.getAdded();

		if (a1 == null && a2 == null) {

			return 0;

		} else if (a1 != null && a2 == null) {

			return -1;

		} else if (a1 == null && a2 != null) {

			return 1;
		}

		return a1.compareTo(a2);
	}
}