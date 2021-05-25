package com.nordicpeak.flowengine.interfaces;

import java.sql.SQLException;
import java.util.List;

public interface ReservationProvider {

	List<String> getReservedObjectIDs(List<String> objectIDs, Integer flowFamilyID, List<String> excludedReservationIDs) throws SQLException;

	String createReservation(String objectID, String reservationID, Integer flowFamilyID, Integer flowInstanceID, String flowInstanceManagerID, Integer reservationMinutes) throws SQLException;

	boolean deleteReservation(String reservationID, Integer flowFamilyID) throws Exception;

	int deleteReservations(List<String> reservationIDs, Integer flowFamilyID) throws Exception;

}
