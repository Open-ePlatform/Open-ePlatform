package com.nordicpeak.flowengine.interfaces;

import java.sql.SQLException;
import java.util.List;

import se.unlogic.hierarchy.core.beans.User;

public interface ReservationProvider {

	List<String> getReservedObjectIDs(List<String> objectIDs, Integer flowFamilyID, List<String> excludedReservationIDs) throws SQLException;

	String createReservation(String objectID, String reservationID, Integer flowFamilyID, Integer flowInstanceID, String flowInstanceManagerID, Integer reservationMinutes, User poster) throws SQLException;
	
	boolean deleteReservation(String reservationID, Integer flowFamilyID, User poster) throws Exception;

	int deleteReservations(List<String> reservationIDs, Integer flowFamilyID, User poster) throws Exception;

}
