package com.nordicpeak.flowengine.flowapprovalmodule;

import java.sql.SQLException;
import java.util.List;

import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.hierarchy.core.exceptions.AccessDeniedException;
import se.unlogic.hierarchy.core.utils.AccessUtils;
import se.unlogic.openhierarchy.foregroundmodules.siteprofile.interfaces.SiteProfile;

import com.nordicpeak.flowengine.FlowInstanceAdminModule;
import com.nordicpeak.flowengine.beans.Flow;
import com.nordicpeak.flowengine.flowapprovalmodule.beans.FlowApprovalActivity;
import com.nordicpeak.flowengine.flowapprovalmodule.beans.FlowApprovalActivityProgress;
import com.nordicpeak.flowengine.interfaces.FlowInstanceAccessController;
import com.nordicpeak.flowengine.interfaces.ImmutableFlowInstance;

public class FlowApprovalFlowInstanceAccessController implements FlowInstanceAccessController {

	private final FlowApprovalUserModule flowApprovalUserModule;

	public FlowApprovalFlowInstanceAccessController(FlowApprovalUserModule flowApprovalUserModule) {

		super();
		this.flowApprovalUserModule = flowApprovalUserModule;
	}

	@Override
	public void checkNewFlowInstanceAccess(Flow flow, User user, SiteProfile profile) throws AccessDeniedException {

		throw new AccessDeniedException("Cannot create new flow instances here");
	}

	@Override
	public void checkFlowInstanceAccess(ImmutableFlowInstance flowInstance, User user) throws AccessDeniedException {

		try {
			List<FlowApprovalActivityProgress> activityProgresses = flowApprovalUserModule.getActivities(user, flowInstance.getFlowInstanceID(), null, FlowApprovalActivityProgress.ACTIVITY_RELATION, FlowApprovalActivity.RESPONSIBLE_USERS_RELATION, FlowApprovalActivity.RESPONSIBLE_GROUPS_RELATION, FlowApprovalActivity.RESPONSIBLE_FALLBACK_RELATION);

			if (activityProgresses != null) {
				for (FlowApprovalActivityProgress activityProgress : activityProgresses) {

					if (activityProgress.getActivity().isShowFlowInstance() && AccessUtils.checkAccess(user, activityProgress)) {
						return;
					}
				}
			}

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

		try {
			FlowInstanceAdminModule.GENERAL_ACCESS_CONTROLLER.checkManagerAccess(flowInstance, user);

		} catch (AccessDeniedException e) {

			throw new AccessDeniedException("User does not have activity access to nor is a manager for " + flowInstance);
		}
	}

	@Override
	public boolean isMutable(ImmutableFlowInstance flowInstance, User user) {

		return false;
	}

}
