package com.nordicpeak.flowengine.interfaces;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;

import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;
import javax.xml.transform.TransformerException;

import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.hierarchy.core.interfaces.SectionInterface;
import se.unlogic.hierarchy.core.interfaces.ViewFragment;
import se.unlogic.hierarchy.core.interfaces.modules.descriptors.ForegroundModuleDescriptor;


public interface FlowSubmitSurveyProvider {

	public ViewFragment getSurveyFormFragment(HttpServletRequest req, User user, ImmutableFlowInstance flowInstance) throws TransformerException, SQLException;

	public ViewFragment getShowFlowSurveysFragment(HttpServletRequest req, Integer flowID) throws TransformerException, SQLException;

	public Float getWeeklyAverage(Integer flowFamilyID, Timestamp startDate, Timestamp endDate) throws SQLException;

	Integer getFlowInstanceSurveyResult(int flowInstanceID, Connection connection) throws SQLException;

	void init(ForegroundModuleDescriptor moduleDescriptor, SectionInterface sectionInterface, DataSource dataSource) throws Exception;

}
