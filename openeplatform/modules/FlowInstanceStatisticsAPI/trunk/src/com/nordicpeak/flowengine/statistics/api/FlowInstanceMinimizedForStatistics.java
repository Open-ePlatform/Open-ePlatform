package com.nordicpeak.flowengine.statistics.api;

import java.lang.reflect.Field;
import java.sql.Timestamp;

import se.unlogic.standardutils.dao.annotations.DAOManaged;
import se.unlogic.standardutils.dao.annotations.Key;
import se.unlogic.standardutils.dao.annotations.OrderBy;
import se.unlogic.standardutils.dao.annotations.Table;
import se.unlogic.standardutils.enums.Order;
import se.unlogic.standardutils.reflection.ReflectionUtils;

@Table(name = "flowengine_flow_instances")
public class FlowInstanceMinimizedForStatistics {

	public static final Field ID_FIELD = ReflectionUtils.getField(FlowInstanceMinimizedForStatistics.class, "flowInstanceID");

	@DAOManaged
	@Key
	private Integer flowInstanceID;

	@DAOManaged(dontUpdateIfNull = true)
	private Integer poster;

	@DAOManaged
	@OrderBy(order = Order.DESC)
	private Timestamp added;

	@DAOManaged
	private Timestamp firstSubmitted;

	@DAOManaged
	private Integer stepID;

	@DAOManaged
	private Integer flowID;

	public Integer getFlowInstanceID() {
		return flowInstanceID;
	}

	public Integer getPosterID() {
		return poster;
	}

	public Timestamp getAdded() {
		return added;
	}

	public Timestamp getFirstSubmitted() {
		return firstSubmitted;
	}

	public Integer getFlowID() {
		return flowID;
	}

	public Integer getStepID() {
		return stepID;
	}

}
