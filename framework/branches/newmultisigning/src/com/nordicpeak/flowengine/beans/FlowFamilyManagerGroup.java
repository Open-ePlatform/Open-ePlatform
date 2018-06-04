package com.nordicpeak.flowengine.beans;

import java.io.Serializable;

import se.unlogic.hierarchy.core.beans.Group;
import se.unlogic.standardutils.dao.annotations.DAOManaged;
import se.unlogic.standardutils.dao.annotations.Key;
import se.unlogic.standardutils.dao.annotations.ManyToOne;
import se.unlogic.standardutils.dao.annotations.Table;
import se.unlogic.standardutils.xml.GeneratedElementable;
import se.unlogic.standardutils.xml.XMLElement;

@Table(name = "flowengine_flow_family_manager_groups")
@XMLElement
public class FlowFamilyManagerGroup extends GeneratedElementable implements Serializable {
	
	private static final long serialVersionUID = -3065392853394817211L;
	
	@DAOManaged(columnName = "flowFamilyID")
	@Key
	@ManyToOne
	private FlowFamily flowFamily;
	
	@DAOManaged
	@Key
	@XMLElement
	private Integer groupID;
	
	@DAOManaged
	@XMLElement
	private boolean restricted;
	
	/** Only for display */
	@XMLElement
	private Group group;
	
	public FlowFamilyManagerGroup() {}
	
	public FlowFamilyManagerGroup(Group group) {
		
		groupID = group.getGroupID();
	}
	
	public FlowFamily getFlowFamily() {
		return flowFamily;
	}
	
	public void setFlowFamily(FlowFamily flowFamily) {
		this.flowFamily = flowFamily;
	}
	
	public Integer getGroupID() {
		return groupID;
	}
	
	public void setGroupID(Integer groupID) {
		this.groupID = groupID;
	}
	
	/** Only for display */
	public void setGroup(Group group) {
		this.group = group;
	}
	
	/** Only for display */
	public Group getGroup() {
		return group;
	}
	
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	public boolean isRestricted() {
		return restricted;
	}
	
	public void setRestricted(boolean restricted) {
		this.restricted = restricted;
	}
	
}
