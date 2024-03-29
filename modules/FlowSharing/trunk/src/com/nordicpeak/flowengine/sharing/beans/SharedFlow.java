package com.nordicpeak.flowengine.sharing.beans;

import java.lang.reflect.Field;
import java.sql.Blob;
import java.sql.Timestamp;

import se.unlogic.standardutils.annotations.WebPopulate;
import se.unlogic.standardutils.dao.annotations.DAOManaged;
import se.unlogic.standardutils.dao.annotations.Key;
import se.unlogic.standardutils.dao.annotations.ManyToOne;
import se.unlogic.standardutils.dao.annotations.OrderBy;
import se.unlogic.standardutils.dao.annotations.Table;
import se.unlogic.standardutils.date.DateStringyfier;
import se.unlogic.standardutils.reflection.ReflectionUtils;
import se.unlogic.standardutils.xml.GeneratedElementable;
import se.unlogic.standardutils.xml.XMLElement;

@Table(name = "flowsharing_flows")
@XMLElement
public class SharedFlow extends GeneratedElementable {

	public static final Field SOURCE_FIELD = ReflectionUtils.getField(SharedFlow.class, "source");
	public static final Field XML_BLOB_FIELD = ReflectionUtils.getField(SharedFlow.class, "flowXML");

	@DAOManaged(autoGenerated = true)
	@Key
	@XMLElement
	private Integer sharedFlowID;

	@DAOManaged(columnName = "sourceID")
	@ManyToOne
	@XMLElement
	private Source source;

	@DAOManaged
	@WebPopulate(required = true)
	@XMLElement
	private Integer flowFamilyID;

	@DAOManaged
	@WebPopulate(required = true)
	@XMLElement
	private Integer flowID;

	@DAOManaged
	@OrderBy
	@WebPopulate(required = true)
	@XMLElement
	private Integer version;

	@DAOManaged
	@WebPopulate(required = true, maxLength = 255)
	@XMLElement
	private String name;

	@DAOManaged
	@WebPopulate(maxLength = 65535)
	@XMLElement
	private String comment;

	@DAOManaged
	@XMLElement(valueFormatter = DateStringyfier.class)
	private Timestamp added;

	@DAOManaged
	private transient Blob flowXML;

	@XMLElement
	private Integer familySize;

	public Integer getSharedFlowID() {

		return sharedFlowID;
	}

	public void setSharedFlowID(Integer sharedFlowID) {

		this.sharedFlowID = sharedFlowID;
	}

	public Source getSource() {

		return source;
	}

	public void setSource(Source source) {

		this.source = source;
	}

	public Integer getFlowFamilyID() {

		return flowFamilyID;
	}

	public void setFlowFamilyID(Integer flowFamilyID) {

		this.flowFamilyID = flowFamilyID;
	}

	public Integer getFlowID() {

		return flowID;
	}

	public void setFlowID(Integer flowID) {

		this.flowID = flowID;
	}

	public Integer getVersion() {

		return version;
	}

	public void setVersion(Integer version) {

		this.version = version;
	}

	public String getName() {

		return name;
	}

	public void setName(String name) {

		this.name = name;
	}

	public String getComment() {

		return comment;
	}

	public void setComment(String comment) {

		this.comment = comment;
	}

	public Timestamp getAdded() {

		return added;
	}

	public void setAdded(Timestamp added) {

		this.added = added;
	}

	public Blob getFlowXML() {

		return flowXML;
	}

	public void setFlowXML(Blob flowXML) {

		this.flowXML = flowXML;
	}

	public Integer getFamilySize() {

		return familySize;
	}

	public void setFamilySize(Integer familySize) {

		this.familySize = familySize;
	}

	@Override
	public String toString() {

		return name + " (ID=" + sharedFlowID + ", flowFamilyID=" + flowFamilyID + ", flowID=" + flowID + ", version=" + version + ")";
	}

}
