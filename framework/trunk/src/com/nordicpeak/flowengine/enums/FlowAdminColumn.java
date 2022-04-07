package com.nordicpeak.flowengine.enums;

public enum FlowAdminColumn {

	FLOW_NAME("flowName"),
	FLOW_TYPE("flowType"),
	CATEGORY("flowCategory"),
	VERSION_COUNT("versions"),
	SUBMITTED_INSTANCES("submittedInstances"),
	NOT_SUBMITTED_INSTANCES("notSubmittedInstances"),
	LAST_REVIEWED("flowFamilyLastReviewed"),
	FLOW_FAMILY_ID("flowFamilyID"),
	ORGANIZATION("organization");

	private String name;

	FlowAdminColumn(String name) {

		this.name = name;
	}

	public String getName() {

		return name;
	}

	public static FlowAdminColumn fromName(String name) {

		FlowAdminColumn[] enumTypes = FlowAdminColumn.values();

		for (FlowAdminColumn enumType : enumTypes) {

			if (enumType.name != null && enumType.name.equals(name)) {

				return enumType;
			}
		}

		return null;
	}
}
