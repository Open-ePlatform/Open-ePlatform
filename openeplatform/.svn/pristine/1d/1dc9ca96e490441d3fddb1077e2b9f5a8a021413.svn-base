package com.nordicpeak.flowengine;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import se.unlogic.hierarchy.core.beans.SimpleForegroundModuleResponse;
import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.hierarchy.core.exceptions.AccessDeniedException;
import se.unlogic.hierarchy.core.exceptions.ModuleConfigurationException;
import se.unlogic.hierarchy.core.interfaces.ForegroundModuleResponse;
import se.unlogic.standardutils.validation.ValidationError;
import se.unlogic.standardutils.xml.XMLUtils;
import se.unlogic.webutils.http.URIParser;

import com.nordicpeak.flowengine.beans.Flow;
import com.nordicpeak.flowengine.beans.FlowInstance;
import com.nordicpeak.flowengine.beans.RequestMetadata;
import com.nordicpeak.flowengine.enums.EventType;
import com.nordicpeak.flowengine.exceptions.FlowEngineException;
import com.nordicpeak.flowengine.exceptions.flow.FlowDisabledException;
import com.nordicpeak.flowengine.exceptions.flowinstancemanager.FlowInstanceManagerClosedException;
import com.nordicpeak.flowengine.interfaces.FlowInstanceAccessController;
import com.nordicpeak.flowengine.managers.ImmutableFlowInstanceManager;
import com.nordicpeak.flowengine.managers.ManagerResponse;
import com.nordicpeak.flowengine.managers.MutableFlowInstanceManager;

public abstract class BaseFlowBrowserModule extends BaseFlowModule {

	public static final ValidationError FLOW_NOT_FOUND_VALIDATION_ERROR = new ValidationError("RequestedFlowNotFound");
	public static final ValidationError FLOW_NO_LONGER_AVAILABLE_VALIDATION_ERROR = new ValidationError("FlowNoLongerAvailable");
	public static final ValidationError FLOW_NO_LONGER_PUBLISHED_VALIDATION_ERROR = new ValidationError("FlowNoLongerPublished");

	public static final ValidationError FLOW_INSTANCE_NO_LONGER_AVAILABLE_VALIDATION_ERROR = new ValidationError("FlowInstanceNoLongerAvailable");

	public static final ValidationError ERROR_GETTING_FLOW_INSTANCE_MANAGER_VALIDATION_ERROR = new ValidationError("ErrorGettingFlowInstanceManager");

	public static final ValidationError FLOW_INSTANCE_ERROR_DATA_SAVED_VALIDATION_ERROR = new ValidationError("FlowInstanceErrorDataSaved");
	public static final ValidationError FLOW_INSTANCE_ERROR_DATA_NOT_SAVED_VALIDATION_ERROR = new ValidationError("FlowInstanceErrorDataNotSaved");

	protected ForegroundModuleResponse processFlowRequestException(MutableFlowInstanceManager instanceManager, HttpServletRequest req, HttpServletResponse res, User user, User poster, URIParser uriParser, RequestMetadata requestMetadata, Exception e) throws SQLException, ModuleConfigurationException {

		log.error("Error processing flow instance " + instanceManager + " belonging to user " + user + ". Trying to save flow instance...", e);

		if (user != null) {
			try {
				if (instanceManager.getFlowInstance().getStepID() != null) {

					instanceManager.saveInstance(this, user, poster, EventType.UPDATED, instanceManager.getFlowInstance().getLastStatusChange(), requestMetadata);

					rebindFlowInstance(req.getSession(), instanceManager);

					log.info("Saved flow instance " + instanceManager + " belonging to user " + user + " after previous exception.");

					return list(req, res, user, uriParser, Collections.singletonList(FLOW_INSTANCE_ERROR_DATA_SAVED_VALIDATION_ERROR));
				}

			} catch (FlowInstanceManagerClosedException e1) {

				removeMutableFlowInstanceManagerFromSession(instanceManager, req.getSession(false));

				log.error("Error saving flow instance " + instanceManager + " belonging to user " + user + " after previous exception.", e1);

			} catch (Exception e1) {

				log.error("Error saving flow instance " + instanceManager + " belonging to user " + user + " after previous exception.", e1);
			}
		}

		return list(req, res, user, uriParser, Collections.singletonList(FLOW_INSTANCE_ERROR_DATA_NOT_SAVED_VALIDATION_ERROR));
	}

	public abstract ForegroundModuleResponse list(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser, List<ValidationError> validationErrors) throws ModuleConfigurationException, SQLException;

	protected boolean skipOverview(Flow flow, HttpServletRequest req, HttpServletResponse res, String redirectMethod) throws IOException {

		if (flow.skipOverview()) {

			if (flow.isInternal()) {

				redirectToMethod(req, res, "/" + redirectMethod + "/" + flow.getFlowID());

			} else {

				redirectToMethod(req, res, "/external/" + flow.getFlowID());
			}

			return true;
		}

		return false;
	}

	protected Element appendFlowInstancePreviewElement(Document doc, Element showFlowInstanceOverviewElement, FlowInstance flowInstance, HttpServletRequest req, User user, URIParser uriParser, SimpleForegroundModuleResponse moduleResponse, FlowInstanceAccessController accessController) {

		Element flowInstancePreviewElement = XMLUtils.append(doc, showFlowInstanceOverviewElement, flowInstance);

		ImmutableFlowInstanceManager instanceManager;

		try {

			//Get saved instance from DB or session
			instanceManager = getImmutableFlowInstanceManager(flowInstance.getFlowInstanceID(), accessController, user, true, req, uriParser, MANAGER_REQUEST_METADATA);

			if (instanceManager == null) {

				log.info("User " + user + " requested non-existing flow instance with ID " + flowInstance.getFlowInstanceID() + ", listing flows");

				XMLUtils.appendNewElement(doc, flowInstancePreviewElement, "PreviewError");

				return flowInstancePreviewElement;
			}

			String baseUpdateURL = getBaseUpdateURL(req, uriParser, user, instanceManager.getFlowInstance(), accessController);

			List<ManagerResponse> managerResponses = instanceManager.getFullShowHTML(req, user, this, true, baseUpdateURL, getImmutableQueryRequestBaseURL(req, instanceManager), MANAGER_REQUEST_METADATA);

			Element flowInstanceManagerElement = doc.createElement("ImmutableFlowInstanceManagerPreview");
			showFlowInstanceOverviewElement.appendChild(flowInstanceManagerElement);

			flowInstanceManagerElement.appendChild(instanceManager.getFlowInstance().toXML(doc));

			XMLUtils.append(doc, flowInstanceManagerElement, "ManagerResponses", managerResponses);

			appendLinksAndScripts(moduleResponse, managerResponses);

		} catch (FlowDisabledException e) {

			log.info("User " + user + " requested flow " + e.getFlow() + " which is not enabled.");

			XMLUtils.appendNewElement(doc, flowInstancePreviewElement, "PreviewError");

		} catch (FlowEngineException e) {

			log.error("Error generating preview of flowInstanceID " + flowInstance.getFlowInstanceID() + " for user " + user, e);

			XMLUtils.appendNewElement(doc, flowInstancePreviewElement, "PreviewError");

		} catch (AccessDeniedException e) {

			log.warn("Access denied. User " + user + " requested flow instance with ID " + flowInstance.getFlowInstanceID());

			XMLUtils.appendNewElement(doc, flowInstancePreviewElement, "PreviewError");

		} catch (SQLException e) {

			log.error("Access denied. User " + user + " requested flow instance with ID " + flowInstance.getFlowInstanceID() + ", " + e.getErrorCode() + ", " + e.getMessage());

			XMLUtils.appendNewElement(doc, flowInstancePreviewElement, "PreviewError");
		}

		return flowInstancePreviewElement;
	}

}
