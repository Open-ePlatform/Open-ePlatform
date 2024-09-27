package com.nordicpeak.flowengine.listeners;

import java.io.File;
import java.util.List;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import se.unlogic.standardutils.base64.Base64;
import se.unlogic.standardutils.validation.ValidationError;
import se.unlogic.standardutils.xml.ElementableListener;
import se.unlogic.standardutils.xml.XMLUtils;

import com.nordicpeak.flowengine.FlowAdminModule;
import com.nordicpeak.flowengine.beans.FlowForm;
import com.nordicpeak.flowengine.validationerrors.FlowFormExportValidationError;

public class FlowFormExportElementableListener implements ElementableListener<FlowForm> {
	
	
	protected final Logger log = Logger.getLogger(getClass());
	
	protected final FlowAdminModule flowAdminModule;
	protected final List<ValidationError> validationErrors;
	
	public FlowFormExportElementableListener(FlowAdminModule flowAdminModule, List<ValidationError> validationErrors) {
		
		super();
		this.flowAdminModule = flowAdminModule;
		this.validationErrors = validationErrors;
	}
	
	@Override
	public void elementGenerated(Document doc, Element element, FlowForm flowForm) {
		
		try {
			if (flowForm.getExternalURL() == null) {
				
				File pdfFile = new File(flowAdminModule.getFlowFormFilePath(flowForm));
				
				if (pdfFile.exists()) {
					
					XMLUtils.appendNewCDATAElement(doc, element, "file", Base64.encodeFromFile(pdfFile));
					
				} else {
					
					log.error("Unable to find file " + pdfFile);
				}
			}
			
		} catch (Exception e) {
			
			log.error("Error exporting flow form " + flowForm, e);
			
			validationErrors.add(new FlowFormExportValidationError(flowForm));
		}
	}
}
