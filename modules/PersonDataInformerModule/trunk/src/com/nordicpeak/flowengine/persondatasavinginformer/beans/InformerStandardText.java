package com.nordicpeak.flowengine.persondatasavinginformer.beans;

import java.io.Serializable;

import se.unlogic.standardutils.annotations.WebPopulate;
import se.unlogic.standardutils.dao.annotations.DAOManaged;
import se.unlogic.standardutils.dao.annotations.Key;
import se.unlogic.standardutils.dao.annotations.Table;
import se.unlogic.standardutils.xml.GeneratedElementable;
import se.unlogic.standardutils.xml.XMLElement;

@Table(name = "person_data_informer_standardtexts")
@XMLElement
public class InformerStandardText extends GeneratedElementable implements Serializable {

	private static final long serialVersionUID = -5742972070247487021L;

	@DAOManaged
	@Key
	@XMLElement
	private Integer textID;

	@DAOManaged
	@XMLElement
	private String name;

	@DAOManaged
	@WebPopulate(maxLength = 65535)
	@XMLElement
	private String value;

	@Override
	public String toString() {

		return name + " (ID: " + textID + ")";
	}

	public Integer getTextID() {

		return textID;
	}

	public void setTextID(Integer textID) {

		this.textID = textID;
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