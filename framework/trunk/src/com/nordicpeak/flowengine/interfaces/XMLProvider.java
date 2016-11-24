package com.nordicpeak.flowengine.interfaces;

import java.io.File;



public interface XMLProvider {

	public File getXML(Integer flowInstanceID, Integer eventID);
}
