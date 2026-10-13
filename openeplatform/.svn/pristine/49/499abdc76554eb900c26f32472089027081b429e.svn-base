package com.nordicpeak.flowengine.utils.flowfamilylist.bean;

import java.io.Serializable;

import se.unlogic.standardutils.beans.Named;
import se.unlogic.standardutils.xml.GeneratedElementable;
import se.unlogic.standardutils.xml.XMLElement;

import com.nordicpeak.flowengine.interfaces.ImmutableFlow;

@XMLElement(name = "FlowFamily")
public class FlowFamilyListItem extends GeneratedElementable implements Serializable, Named {

	private static final long serialVersionUID = 4980976980828189740L;

	@XMLElement(name = "FlowFamilyID")
	private Integer ID;

	@XMLElement(name = "FlowFamilyName")
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

		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		FlowFamilyListItem other = (FlowFamilyListItem) obj;
		if (ID == null) {
			if (other.ID != null) {
				return false;
			}
		} else if (!ID.equals(other.ID)) {
			return false;
		}
		if (name == null) {
			if (other.name != null) {
				return false;
			}
		} else if (!name.equals(other.name)) {
			return false;
		}
		return true;
	}

}
