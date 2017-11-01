package com.nordicpeak.flowengine.flowinstancerafflesummary.beans;

import java.util.List;

import se.unlogic.standardutils.collections.CollectionUtils;

import com.nordicpeak.flowengine.beans.FlowInstance;
import com.nordicpeak.flowengine.enums.ContentType;
import com.nordicpeak.flowengine.flowinstancesummary.beans.table.RowInformation;

public class RaffleRowInformation extends RowInformation {

	protected final RaffleFlow raffleFlow;
	protected final List<ContentType> autoExcludedStatusTypes;

	public RaffleRowInformation(RaffleFlow raffleFlow, List<ContentType> autoExcludedStatusTypes, FlowInstance flowInstance, int rowNumber, String flowBaseURL) {
		super(flowInstance, rowNumber, flowBaseURL);

		this.autoExcludedStatusTypes = autoExcludedStatusTypes;
		this.raffleFlow = raffleFlow;
	}

	@Override
	public boolean isMutable() {

		Integer statusID = getFlowInstance().getStatus().getStatusID();

		if (!CollectionUtils.isEmpty(autoExcludedStatusTypes) && autoExcludedStatusTypes.contains(flowInstance.getStatus().getContentType())) {

			return false;
		}

		if (!CollectionUtils.isEmpty(raffleFlow.getExcludedStatusIDs()) && raffleFlow.getExcludedStatusIDs().contains(statusID)) {

			return false;
		}

		if (statusID.equals(raffleFlow.getRaffledStatusID())) {

			return false;
		}

		return true;
	}

	public RaffleFlow getRaffleFlow() {

		return raffleFlow;
	}

}
