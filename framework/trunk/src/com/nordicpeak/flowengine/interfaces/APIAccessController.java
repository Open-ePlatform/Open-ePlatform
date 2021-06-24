package com.nordicpeak.flowengine.interfaces;

import java.sql.SQLException;

import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.hierarchy.core.exceptions.AccessDeniedException;

import com.nordicpeak.flowengine.beans.Flow;
import com.nordicpeak.flowengine.beans.FlowFamily;

public interface APIAccessController {

	public boolean hasAccess(Integer familyID, User user) throws SQLException;
	public boolean hasAccess(FlowFamily family, User user) throws SQLException;
	public boolean hasAccess(Flow flow, User user) throws SQLException;
		
	public void accessCheck(Integer flowFamilyID, User user) throws SQLException, AccessDeniedException;
	public void accessCheck(FlowFamily family, User user) throws SQLException, AccessDeniedException;
	public void accessCheck(Flow flow, User user) throws SQLException, AccessDeniedException;
}
