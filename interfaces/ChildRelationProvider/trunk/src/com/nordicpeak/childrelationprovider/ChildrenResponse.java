package com.nordicpeak.childrelationprovider;

import java.util.Collections;
import java.util.Map;

import se.unlogic.standardutils.xml.GeneratedElementable;

public class ChildrenResponse extends GeneratedElementable {

	private Map<String, Child> children;

	private boolean childrenUnderSecrecy;

	private Integer childrenTotalCount;

	public ChildrenResponse(Map<String, Child> children, Integer childrenTotalCount, boolean childrenUnderSecrecy) {

		super();
		this.children = children;
		this.childrenUnderSecrecy = childrenUnderSecrecy;
		this.childrenTotalCount = childrenTotalCount;
	}

	public ChildrenResponse(boolean childrenUnderSecrecy) {

		super();

		this.childrenUnderSecrecy = childrenUnderSecrecy;
		children = Collections.emptyMap();
	}

	public Map<String, Child> getChildren() {

		return children;
	}

	public boolean hasChildrenUnderSecrecy() {

		return childrenUnderSecrecy;
	}

	public Integer getChildrenTotalCount() {

		return childrenTotalCount;
	}

	public void setChildrenTotalCount(Integer childrenTotalCount) {

		this.childrenTotalCount = childrenTotalCount;
	}

}
