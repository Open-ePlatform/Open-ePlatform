package com.nordicpeak.flowengine.interfaces;

import java.sql.SQLException;
import java.sql.Timestamp;

import javax.servlet.http.HttpServletRequest;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;

import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.hierarchy.core.interfaces.ViewFragment;


public interface FlowSubmitSurveyProvider {

	public ViewFragment getSurveyFormFragment(HttpServletRequest req, User user, ImmutableFlowInstance flowInstance) throws TransformerConfigurationException, TransformerException, SQLException;

	public ViewFragment getShowFlowSurveysFragment(Integer flowID) throws TransformerConfigurationException, TransformerException, SQLException;

	public Float getWeeklyAverage(Integer flowFamilyID, Timestamp startDate, Timestamp endDate) throws SQLException;

}
