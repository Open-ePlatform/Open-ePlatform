package com.nordicpeak.flowengine.managers;

import java.util.List;

import se.unlogic.standardutils.xml.GeneratedElementable;
import se.unlogic.standardutils.xml.XMLElement;

import com.nordicpeak.flowengine.beans.PDFQueryResponse;

@XMLElement
public class PDFManagerResponse extends GeneratedElementable {

	@XMLElement
	private final int currentStepID;

	@XMLElement
	private final int currentStepIndex;

	@XMLElement(fixCase = true)
	private final List<PDFQueryResponse> queryResponses;

	public PDFManagerResponse(int currentStepID, int currentStepIndex, List<PDFQueryResponse> queryResponses) {

		super();
		this.currentStepID = currentStepID;
		this.currentStepIndex = currentStepIndex;
		this.queryResponses = queryResponses;
	}

	public int getCurrentStepID() {

		return currentStepID;
	}

	public List<PDFQueryResponse> getQueryResponses() {

		return queryResponses;
	}

	public int getCurrentStepIndex() {

		return currentStepIndex;
	}
}
