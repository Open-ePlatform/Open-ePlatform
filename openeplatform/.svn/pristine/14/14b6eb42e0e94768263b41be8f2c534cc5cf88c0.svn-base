package com.nordicpeak.flowengine.beans;

import java.lang.reflect.Field;
import java.util.List;

import se.unlogic.standardutils.dao.annotations.DAOManaged;
import se.unlogic.standardutils.dao.annotations.ManyToOne;
import se.unlogic.standardutils.dao.annotations.OneToMany;
import se.unlogic.standardutils.dao.annotations.Table;
import se.unlogic.standardutils.reflection.ReflectionUtils;
import se.unlogic.standardutils.xml.XMLElement;

@Table(name = "flowengine_standard_statuses")
@XMLElement
public class StandardStatus extends BaseStatus {

	private static final long serialVersionUID = -9064856969314028632L;

	public static final Field STANDARD_STATUS_GROUP_RELATION = ReflectionUtils.getField(StandardStatus.class, "standardStatusGroup");
	public static final Field DEFAULT_STANDARD_STATUS_MAPPINGS_RELATION = ReflectionUtils.getField(StandardStatus.class, "defaultStandardStatusMappings");

	@DAOManaged(columnName = "statusGroupID")
	@ManyToOne
	@XMLElement(fixCase = true)
	private StandardStatusGroup standardStatusGroup;

	@DAOManaged
	@OneToMany
	@XMLElement(fixCase = true)
	private List<DefaultStandardStatusMapping> defaultStandardStatusMappings;

	public StandardStatusGroup getStandardStatusGroup() {

		return standardStatusGroup;
	}

	public void setStandardStatusGroup(StandardStatusGroup standardStatusGroup) {

		this.standardStatusGroup = standardStatusGroup;
	}

	public List<DefaultStandardStatusMapping> getDefaultStandardStatusMappings() {

		return defaultStandardStatusMappings;
	}

	public void setDefaultStandardStatusMappings(List<DefaultStandardStatusMapping> defaultStandardStatusMappings) {

		this.defaultStandardStatusMappings = defaultStandardStatusMappings;
	}

}
