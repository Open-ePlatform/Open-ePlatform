package com.nordicpeak.flowengine.beans;

import se.unlogic.hierarchy.core.interfaces.ViewFragment;
import se.unlogic.standardutils.beans.Named;
import se.unlogic.standardutils.xml.GeneratedElementable;
import se.unlogic.standardutils.xml.XMLElement;

@XMLElement
public class ExtensionView extends GeneratedElementable implements Named {
	
	@XMLElement
	private final String name;
	
	@XMLElement
	private final ViewFragment viewFragment;
	
	@XMLElement
	private final String slot;
	
	public ExtensionView(String name, ViewFragment viewFragment, String slot) {
		
		if (name == null) {
			
			throw new NullPointerException("name cannot be null");
		}
		
		if (viewFragment == null) {
			
			throw new NullPointerException("viewFragment cannot be null");
		}
		
		this.name = name;
		this.viewFragment = viewFragment;
		this.slot = slot;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((slot == null) ? 0 : slot.hashCode());
		result = prime * result + ((viewFragment == null) ? 0 : viewFragment.hashCode());
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
		ExtensionView other = (ExtensionView) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (slot == null) {
			if (other.slot != null)
				return false;
		} else if (!slot.equals(other.slot))
			return false;
		if (viewFragment == null) {
			if (other.viewFragment != null)
				return false;
		} else if (!viewFragment.equals(other.viewFragment))
			return false;
		return true;
	}
	
	@Override
	public String getName() {
		
		return name;
	}
	
	public ViewFragment getViewFragment() {
		return viewFragment;
	}
	
	public String getSlot() {
		return slot;
	}
	
}
