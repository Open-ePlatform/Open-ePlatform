package com.nordicpeak.flowengine.interfaces;

import java.sql.SQLException;
import java.sql.Timestamp;

import javax.servlet.http.HttpServletRequest;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;

import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.hierarchy.core.interfaces.ViewFragment;

import com.nordicpeak.flowengine.managers.FlowInstanceManager;


public interface FlowSubmitSurveyProvider {

	public ViewFragment getSurveyFormFragment(HttpServletRequest req, User user, FlowInstanceManager instanceManager) throws TransformerConfigurationException, TransformerException, SQLException;

	public ViewFragment getShowFlowSurveysFragment(Integer flowID) throws TransformerConfigurationException, TransformerException, SQLException;

	public Float getWeeklyAverage(Integer flowFamilyID, Timestamp startDate, Timestamp endDate) throws SQLException;

}
