package com.nordicpeak.flowengine.statistics;

import java.util.List;

import se.unlogic.standardutils.xml.GeneratedElementable;
import se.unlogic.standardutils.xml.XMLElement;

import com.nordicpeak.flowengine.beans.Step;

@XMLElement
public class FlowStatistics extends GeneratedElementable{

	@XMLElement
	private Integer flowID;

	@XMLElement
	private Integer version;

	private List<IntegerEntry> stepAbortCount;

	private List<IntegerEntry> stepUnsubmittedCount;

	private List<Step> steps;

	public List<IntegerEntry> getStepUnsubmittedCount() {

		return stepUnsubmittedCount;
	}

	public void setStepUnsubmittedCount(List<IntegerEntry> stepUnsubmittedCount) {

		this.stepUnsubmittedCount = stepUnsubmittedCount;
	}

	public Integer getVersion() {

		return version;
	}

	public void setVersion(Integer version) {

		this.version = version;
	}

	public List<IntegerEntry> getStepAbortCount() {

		return stepAbortCount;
	}

	public void setStepAbortCount(List<IntegerEntry> stepAbortCount) {

		this.stepAbortCount = stepAbortCount;
	}

	public Integer getFlowID() {

		return flowID;
	}

	public void setFlowID(Integer flowID) {

		this.flowID = flowID;
	}

	public List<Step> getSteps() {

		return steps;
	}

	public void setSteps(List<Step> steps) {

		this.steps = steps;
	}
}
