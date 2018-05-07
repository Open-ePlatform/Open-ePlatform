package com.nordicpeak.flowengine.flowsubmitsurveys;

import java.sql.Timestamp;

import se.unlogic.standardutils.annotations.WebPopulate;
import se.unlogic.standardutils.dao.annotations.DAOManaged;
import se.unlogic.standardutils.dao.annotations.Key;
import se.unlogic.standardutils.dao.annotations.Table;
import se.unlogic.standardutils.xml.GeneratedElementable;
import se.unlogic.standardutils.xml.XMLElement;

@Table(name = "feedback_flow_submit_surveys")
@XMLElement
public class FeedbackSurvey extends GeneratedElementable {

	@Key
	@DAOManaged
	@XMLElement
	private Integer flowID;

	@Key
	@DAOManaged
	@XMLElement
	private Integer flowInstanceID;

	@DAOManaged
	@XMLElement
	private Timestamp added;

	@DAOManaged
	@WebPopulate(required = true)
	@XMLElement
	private Answer answer;

	@DAOManaged
	@WebPopulate(maxLength = 65536)
	@XMLElement
	private String comment;

	public Integer getFlowID() {

		return flowID;
	}

	public void setFlowID(Integer flowID) {

		this.flowID = flowID;
	}

	public Integer getFlowInstanceID() {

		return flowInstanceID;
	}

	public void setFlowInstanceID(Integer flowInstanceID) {

		this.flowInstanceID = flowInstanceID;
	}

	public Timestamp getAdded() {

		return added;
	}

	public void setAdded(Timestamp added) {

		this.added = added;
	}

	public Answer getAnswer() {

		return answer;
	}

	public void setAnswer(Answer answer) {

		this.answer = answer;
	}

	public String getComment() {

		return comment;
	}

	public void setComment(String comment) {

		this.comment = comment;
	}

}