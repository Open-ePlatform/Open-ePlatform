package com.nordicpeak.flowengine.statistics;

import se.unlogic.standardutils.dao.annotations.DAOManaged;

public class IntegerEntry implements NumberEntry{

	@DAOManaged
	private Integer id;

	@DAOManaged
	private Integer value;

	public Integer getValue() {

		return value;
	}

	public void setValue(Integer value) {

		this.value = value;
	}

	public Integer getId() {

		return id;
	}

	public void setId(Integer id) {

		this.id = id;
	}
}
