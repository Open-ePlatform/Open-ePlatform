package com.nordicpeak.flowengine.statistics;

import se.unlogic.standardutils.dao.annotations.DAOManaged;

public class FloatEntry implements NumberEntry{

	@DAOManaged
	private Integer id;

	@DAOManaged
	private Float value;

	public Float getValue() {

		return value;
	}

	public void setValue(Float value) {

		this.value = value;
	}

	public Integer getId() {

		return id;
	}

	public void setId(Integer id) {

		this.id = id;
	}
}
