package com.nordicpeak.flowengine.managers;

import java.util.List;

import se.unlogic.standardutils.xml.GeneratedElementable;
import se.unlogic.standardutils.xml.XMLElement;

import com.nordicpeak.flowengine.beans.QueryResponse;

@XMLElement
public class ManagerResponse extends GeneratedElementable {

	@XMLElement
	private final int currentStepID;

	@XMLElement
	private final int currentStepIndex;

	@XMLElement(fixCase = true)
	private final List<QueryResponse> queryResponses;

	@XMLElement(fixCase = true)
	private final boolean validationErrors;

	@XMLElement
	private final boolean concurrentModificationLock;
	
	public ManagerResponse(int currentStepID, int currentStepIndex, List<QueryResponse> queryResponses, boolean hasValidationErrors, boolean concurrentModificationLock) {

		super();
		this.currentStepID = currentStepID;
		this.currentStepIndex = currentStepIndex;
		this.queryResponses = queryResponses;
		this.validationErrors = hasValidationErrors;
		this.concurrentModificationLock = concurrentModificationLock;
	}

	
	public boolean isConcurrentModificationLock() {
	
		return concurrentModificationLock;
	}

	public int getCurrentStepID() {

		return currentStepID;
	}

	public List<QueryResponse> getQueryResponses() {

		return queryResponses;
	}

	public boolean hasValidationErrors() {

		return validationErrors;
	}

	public int getCurrentStepIndex() {

		return currentStepIndex;
	}
}
