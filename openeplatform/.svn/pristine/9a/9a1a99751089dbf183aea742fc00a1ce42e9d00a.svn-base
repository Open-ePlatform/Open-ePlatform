package com.nordicpeak.flowengine.persondatasavinginformer.query;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import se.unlogic.standardutils.xml.ElementableListener;
import se.unlogic.standardutils.xml.XMLUtils;

import com.nordicpeak.flowengine.persondatasavinginformer.beans.FlowFamilyInformerSetting;
import com.nordicpeak.flowengine.utils.JTidyUtils;

public class FlowFamilyInformerSettingTextsListener implements ElementableListener<FlowFamilyInformerSetting> {

	private final String encoding;
	
	public FlowFamilyInformerSettingTextsListener(String encoding) {

		super();
		
		this.encoding = encoding;
	}

	@Override
	public void elementGenerated(Document doc, Element element, FlowFamilyInformerSetting setting) {

		if (setting.getReason() != null) {
			XMLUtils.appendNewCDATAElement(doc, element, "reason", JTidyUtils.getXHTML(setting.getReason(), encoding));
		}
		
		if (setting.getExtraInformation() != null) {
			XMLUtils.appendNewCDATAElement(doc, element, "extraInformation", JTidyUtils.getXHTML(setting.getExtraInformation(), encoding));
		}
		
		if (setting.getComplaintDescription() != null) {
			XMLUtils.appendNewCDATAElement(doc, element, "complaintDescription", JTidyUtils.getXHTML(setting.getComplaintDescription(), encoding));
		}
		
		if (setting.getExtraInformationStorage() != null) {
			XMLUtils.appendNewCDATAElement(doc, element, "extraInformationStorage", JTidyUtils.getXHTML(setting.getExtraInformationStorage(), encoding));
		}
		
		if (setting.getConfirmationText() != null) {
			XMLUtils.appendNewCDATAElement(doc, element, "confirmationText", JTidyUtils.getXHTML(setting.getConfirmationText(), encoding));
		}
		
		if (setting.getDataRecipient() != null) {
			XMLUtils.appendNewCDATAElement(doc, element, "dataRecipient", JTidyUtils.getXHTML(setting.getDataRecipient(), encoding));
		}
	}
}