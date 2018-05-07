package com.nordicpeak.flowengine.interfaces;

import java.io.Serializable;
import java.sql.Blob;
import java.util.List;

import se.unlogic.standardutils.xml.Elementable;


public interface ImmutableFlow extends Serializable, Elementable {

	public Integer getFlowID();

	public String getName();
	
	public boolean isEnabled();

	public boolean isPublished();

	public ImmutableFlowType getFlowType();

	public abstract boolean usesPreview();

	public abstract String getSubmittedMessage();

	public ImmutableStatus getDefaultState(String actionID);

	public ImmutableCategory getCategory();

	public List<? extends ImmutableStep> getSteps();

	public ImmutableFlowFamily getFlowFamily();

	public Integer getVersion();

	public List<String> getTags();

	public List<String> getChecks();

	public boolean requiresAuthentication();
	
	public boolean requiresSigning();
	
	public boolean showsSubmitSurvey();

	public abstract Blob getIcon();

	public abstract String getIconFileName();
	
	public boolean skipOverview();
	
	public boolean isPaymentSupportEnabled();
	
	public List<? extends ImmutableStatus> getStatuses();
	
	public boolean hidesManagerDetails();
}