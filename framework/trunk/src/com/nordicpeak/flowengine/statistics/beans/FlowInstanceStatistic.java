package com.nordicpeak.flowengine.statistics.beans;

import java.util.Date;

import se.unlogic.standardutils.xml.GeneratedElementable;
import se.unlogic.standardutils.xml.XMLElement;

@XMLElement
public class FlowInstanceStatistic extends GeneratedElementable implements Comparable<FlowInstanceStatistic> {

	@XMLElement
	private final int flowID;

	@XMLElement
	private final Date created;

	@XMLElement
	private String sex;

	@XMLElement
	private Integer age;

	@XMLElement
	private Integer abortedStep;

	@XMLElement
	private Integer savedStep;

	@XMLElement
	private Date submitted;

	@XMLElement
	private Integer surveyAnswer;

	public FlowInstanceStatistic(int flowID, Date created) {
		super();

		this.flowID = flowID;
		this.created = created;
	}

	public int getFlowID() {
		return flowID;
	}

	public Date getCreated() {
		return created;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public Integer getAbortedStep() {
		return abortedStep;
	}

	public void setAbortedStep(Integer abortedStep) {
		this.abortedStep = abortedStep;
	}

	public Integer getSavedStep() {
		return savedStep;
	}

	public void setSavedStep(Integer savedStep) {
		this.savedStep = savedStep;
	}

	public Date getSubmitted() {
		return submitted;
	}

	public void setSubmitted(Date submitted) {
		this.submitted = submitted;
	}

	public Integer getSurveyAnswer() {
		return surveyAnswer;
	}

	public void setSurveyAnswer(Integer surveyAnswer) {
		this.surveyAnswer = surveyAnswer;
	}

	@Override
	public int compareTo(FlowInstanceStatistic o) {
		return created.compareTo(o.getCreated());
	}

}
