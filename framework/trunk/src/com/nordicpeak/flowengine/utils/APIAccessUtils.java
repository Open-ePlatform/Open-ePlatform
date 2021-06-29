package com.nordicpeak.flowengine.utils;

import java.sql.SQLException;

import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.hierarchy.core.exceptions.AccessDeniedException;

import com.nordicpeak.flowengine.beans.Flow;
import com.nordicpeak.flowengine.beans.FlowFamily;
import com.nordicpeak.flowengine.interfaces.APIAccessController;

public class APIAccessUtils {

	public static boolean hasAccess(APIAccessController apiAccessModule, Integer familyID, User user) throws SQLException {
		
		if (apiAccessModule != null) {
			
			return apiAccessModule.hasAccess(familyID, user);
		}
		
		return true;
	}

	public static boolean hasAccess(APIAccessController apiAccessModule, FlowFamily family, User user) throws SQLException {

		if (apiAccessModule != null) {
			
			return apiAccessModule.hasAccess(family, user);
		}
		
		return true;
	}

	public static boolean hasAccess(APIAccessController apiAccessModule, Flow flow, User user) throws SQLException {

		if (apiAccessModule != null) {
			
			return apiAccessModule.hasAccess(flow, user);
		}
		
		return true;
	}

	public static void accessCheck(APIAccessController apiAccessModule, Integer flowFamilyID, User user) throws SQLException, AccessDeniedException {

		if (apiAccessModule != null) {
			
			apiAccessModule.accessCheck(flowFamilyID, user);
		}
	}

	public static void accessCheck(APIAccessController apiAccessModule, FlowFamily family, User user) throws SQLException, AccessDeniedException {

		if (apiAccessModule != null) {
			
			apiAccessModule.accessCheck(family, user);
		}
	}

	public static void accessCheck(APIAccessController apiAccessModule, Flow flow, User user) throws SQLException, AccessDeniedException {

		if (apiAccessModule != null) {
			
			apiAccessModule.accessCheck(flow, user);
		}
	}
}
