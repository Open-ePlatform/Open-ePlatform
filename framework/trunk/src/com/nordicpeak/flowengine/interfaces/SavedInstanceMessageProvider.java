package com.nordicpeak.flowengine.interfaces;

import java.sql.SQLException;

public interface SavedInstanceMessageProvider {

	String getSavedInstanceMessage(Integer flowFamilyID, String statusName) throws SQLException;
}
