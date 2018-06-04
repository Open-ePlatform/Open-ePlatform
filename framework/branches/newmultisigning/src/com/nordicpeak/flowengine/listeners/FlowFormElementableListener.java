package com.nordicpeak.flowengine.listeners;

import java.io.File;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import se.unlogic.standardutils.io.BinarySizeFormater;
import se.unlogic.standardutils.string.StringUtils;
import se.unlogic.standardutils.xml.ElementableListener;
import se.unlogic.standardutils.xml.XMLUtils;

import com.nordicpeak.flowengine.FlowAdminModule;
import com.nordicpeak.flowengine.beans.FlowForm;

public class FlowFormElementableListener implements ElementableListener<FlowForm> {
	
	
	protected final Logger log = Logger.getLogger(getClass());
	
	protected final FlowAdminModule flowAdminModule;
	
	public FlowFormElementableListener(FlowAdminModule flowAdminModule) {
		
		super();
		this.flowAdminModule = flowAdminModule;
	}
	
	@Override
	public void elementGenerated(Document doc, Element element, FlowForm flowForm) {
		
		if (StringUtils.isEmpty(flowForm.getExternalURL())) {
			
			File file = new File(flowAdminModule.getFlowFormFilePath(flowForm));
			
			if (file.exists()) {
				
				XMLUtils.appendNewElement(doc, element, "formattedSize", BinarySizeFormater.getFormatedSize(file.length()));
				
			} else {
				
				log.warn("Unable to find PDF file for flow form " + flowForm + " at " + file);
				
				XMLUtils.appendNewElement(doc, element, "formattedSize", flowAdminModule.getFileMissing());
			}
		}
	}
}
