package com.nordicpeak.flowengine.interfaces;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.hierarchy.core.interfaces.ForegroundModuleResponse;
import se.unlogic.hierarchy.core.interfaces.events.EventHandler;
import se.unlogic.hierarchy.core.utils.AdvancedCRUDCallback;
import se.unlogic.standardutils.validation.ValidationError;
import se.unlogic.webutils.http.URIParser;

import com.nordicpeak.flowengine.beans.Flow;
import com.nordicpeak.flowengine.beans.FlowForm;

public interface FlowAdminCRUDCallback extends AdvancedCRUDCallback<User> {

	public String getFlowFormFilePath(FlowForm form);

	public String getFileMissing();

	public String getFlowFormFilestore();

	public boolean allowSkipOverviewForFlowForms();

	public boolean deleteFlowFormFile(FlowForm form);

	public void addFlowFamilyEvent(String message, ImmutableFlow flow, User user);

	public EventHandler getEventHandler();

	public String getEventFlowFormAddedMessage();

	public String getEventFlowFormUpdatedMessage();

	public String getEventFlowFormDeletedMessage();

	public Integer getMaxPDFFormFileSize();

	public void redirectToMethod(HttpServletRequest req, HttpServletResponse res, String alias) throws IOException;

	public ForegroundModuleResponse list(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser, List<ValidationError> validationErrors) throws Exception;

	public boolean hasFlowTypeAccess(User user, Flow flow);

	public List<String> getAllowedFlowFormFileExtensions();
}
