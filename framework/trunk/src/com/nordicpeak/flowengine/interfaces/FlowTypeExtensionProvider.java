package com.nordicpeak.flowengine.interfaces;

import javax.servlet.http.HttpServletRequest;

import com.nordicpeak.flowengine.beans.FlowType;

import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.hierarchy.core.interfaces.Prioritized;
import se.unlogic.hierarchy.core.interfaces.ViewFragment;
import se.unlogic.standardutils.validation.ValidationException;
import se.unlogic.webutils.http.URIParser;

public interface FlowTypeExtensionProvider extends Prioritized {

	public Integer getProviderID();

	public ViewFragment getShowFlowTypeFragment(FlowType flowType, HttpServletRequest req, User user, URIParser uriParser) throws Exception;

	public ViewFragment getUpdateFlowTypeFragment(FlowType flowType, HttpServletRequest req, User user, URIParser uriParser, ValidationException validationException) throws Exception;

	public void validateUpdateFlowType(FlowType flowType, HttpServletRequest req, User user, URIParser uriParser) throws ValidationException, Exception;

	public void updateFlowType(FlowType flowType, HttpServletRequest req, User user, URIParser uriParser) throws ValidationException, Exception;
}
