package com.nordicpeak.flowengine.interfaces;

import java.io.File;
import java.sql.Timestamp;

import com.nordicpeak.flowengine.beans.FlowInstanceEvent;
import com.nordicpeak.flowengine.managers.FlowInstanceManager;



public interface XMLProvider {

	public File getXML(Integer flowInstanceID, Integer eventID);
	
	public void generateXML(ImmutableFlowInstance flowInstance, FlowInstanceManager flowInstanceManager, FlowInstanceEvent event, Timestamp lastSubmitted) throws Exception;
	
	public void generateXML(ImmutableFlowInstance flowInstance, FlowInstanceManager flowInstanceManager, FlowInstanceEvent event, Timestamp lastSubmitted, File outputFile) throws Exception;
}
