package com.nordicpeak.flowengine.utils.flowfamilylist.bean;

import java.io.Serializable;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import se.unlogic.standardutils.beans.Named;
import se.unlogic.standardutils.xml.Elementable;
import se.unlogic.standardutils.xml.XMLUtils;

import com.nordicpeak.flowengine.interfaces.ImmutableFlow;

public class FlowFamilyListItem implements Serializable, Elementable, Named {

	private static final long serialVersionUID = 4980976980828189740L;

	public static final String NAME = "FlowFamilyList";
	public static final String FLOWFAMILY = "FlowFamily";

	public static final String FLOWFAMILYID = "FlowFamilyID";
	public static final String FLOWFAMILYNAME = "FlowFamilyName";

	private Integer ID;
	private String name;

	public FlowFamilyListItem(Integer ID, String name) {

		this.ID = ID;
		this.name = name;
	}

	public FlowFamilyListItem(ImmutableFlow flow) {

		this.ID = flow.getFlowFamily().getFlowFamilyID();
		this.name = flow.getName();
	}

	public Integer getID() {

		return ID;
	}

	@Override
	public String getName() {

		return this.name;
	}

	@Override
	public Element toXML(Document doc) {

		Element flowFamilyListElement = doc.createElement(FLOWFAMILY);

		if (this.getID() != null) {
			flowFamilyListElement.appendChild(XMLUtils.createCDATAElement(FLOWFAMILYID, this.getID().toString(), doc));
		}

		if (this.getName() != null) {
			flowFamilyListElement.appendChild(XMLUtils.createCDATAElement(FLOWFAMILYNAME, this.toString(), doc));
		}

		/*
		flowFamilyListElement.appendChild(XMLUtils.createCDATAElement("enabled", Boolean.toString(this.isEnabled()), doc));
		
		List<Element> additionalXML = this.getAdditionalXML(doc);
		
		if (!CollectionUtils.isEmpty(additionalXML)) {
		
			for (Element element : additionalXML) {
		
				flowFamilyListElement.appendChild(element);
			}
		}
		
		
		XMLUtils.appendNewElement(doc, flowFamilyListElement, "isMutable", this instanceof MutableGroup);
		*/

		return flowFamilyListElement;
	}

	@Override
	public String toString() {

		return name + " (ID: " + ID + ") ";
	}

	@Override
	public int hashCode() {

		final int prime = 31;
		int result = 1;
		result = prime * result + ((ID == null) ? 0 : ID.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {

		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FlowFamilyListItem other = (FlowFamilyListItem) obj;
		if (ID == null) {
			if (other.ID != null)
				return false;
		} else if (!ID.equals(other.ID))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

}
