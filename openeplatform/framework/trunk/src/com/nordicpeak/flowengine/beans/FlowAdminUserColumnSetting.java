package com.nordicpeak.flowengine.beans;

import java.io.Serializable;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import se.unlogic.standardutils.dao.annotations.DAOManaged;
import se.unlogic.standardutils.dao.annotations.Key;
import se.unlogic.standardutils.dao.annotations.Table;
import se.unlogic.standardutils.xml.GeneratedElementable;
import se.unlogic.standardutils.xml.XMLElement;
import se.unlogic.standardutils.xml.XMLUtils;

import com.nordicpeak.flowengine.enums.FlowAdminColumn;

@Table(name = "flowengine_flowadmin_user_column_settings")
@XMLElement
public class FlowAdminUserColumnSetting extends GeneratedElementable implements Serializable {

	private static final long serialVersionUID = -3846960397800413428L;

	@DAOManaged
	@Key
	@XMLElement
	private Integer userID;

	@DAOManaged
	@XMLElement
	private Integer columnIndex;

	@DAOManaged
	@XMLElement
	private FlowAdminColumn columnName;

	@DAOManaged
	@XMLElement
	private boolean visible;

	public Integer getUserID() {

		return userID;
	}

	public void setUserID(Integer userID) {

		this.userID = userID;
	}

	public boolean isVisible() {

		return visible;
	}

	public void setVisible(boolean visible) {

		this.visible = visible;
	}

	public Integer getColumnIndex() {

		return columnIndex;
	}

	public void setColumnIndex(Integer columnIndex) {

		this.columnIndex = columnIndex;
	}

	public FlowAdminColumn getColumnName() {

		return columnName;
	}

	public void setColumn(FlowAdminColumn columnName) {

		this.columnName = columnName;
	}
	
	
	@Override
	public Element toXML(Document doc) {
		
		Element element = super.toXML(doc);
		XMLUtils.appendNewElement(doc,element, "column", columnName.getName());

		return element;
	}

	@Override
	public String toString() {

		return columnName + " (userID: " + userID + ", columnIndex: " + columnIndex + ", visible: " + visible + ")";
	}

}
