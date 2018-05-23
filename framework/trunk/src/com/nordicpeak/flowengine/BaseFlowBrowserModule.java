package com.nordicpeak.flowengine;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.nordicpeak.flowengine.beans.Flow;
import com.nordicpeak.flowengine.beans.RequestMetadata;
import com.nordicpeak.flowengine.enums.EventType;
import com.nordicpeak.flowengine.exceptions.flowinstancemanager.FlowInstanceManagerClosedException;
import com.nordicpeak.flowengine.managers.MutableFlowInstanceManager;

import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.hierarchy.core.exceptions.ModuleConfigurationException;
import se.unlogic.hierarchy.core.interfaces.ForegroundModuleResponse;
import se.unlogic.standardutils.validation.ValidationError;
import se.unlogic.webutils.http.URIParser;


public abstract class BaseFlowBrowserModule extends BaseFlowModule {

	public static final ValidationError FLOW_NOT_FOUND_VALIDATION_ERROR = new ValidationError("RequestedFlowNotFound");
	public static final ValidationError FLOW_NO_LONGER_AVAILABLE_VALIDATION_ERROR = new ValidationError("FlowNoLongerAvailable");
	public static final ValidationError FLOW_NO_LONGER_PUBLISHED_VALIDATION_ERROR = new ValidationError("FlowNoLongerPublished");

	public static final ValidationError FLOW_INSTANCE_NO_LONGER_AVAILABLE_VALIDATION_ERROR = new ValidationError("FlowInstanceNoLongerAvailable");

	public static final ValidationError ERROR_GETTING_FLOW_INSTANCE_MANAGER_VALIDATION_ERROR = new ValidationError("ErrorGettingFlowInstanceManager");

	public static final ValidationError FLOW_INSTANCE_ERROR_DATA_SAVED_VALIDATION_ERROR = new ValidationError("FlowInstanceErrorDataSaved");
	public static final ValidationError FLOW_INSTANCE_ERROR_DATA_NOT_SAVED_VALIDATION_ERROR = new ValidationError("FlowInstanceErrorDataNotSaved");
	
	protected static final RequestMetadata DEFAULT_REQUEST_METADATA = new RequestMetadata(false);

	protected ForegroundModuleResponse processFlowRequestException(MutableFlowInstanceManager instanceManager, HttpServletRequest req, HttpServletResponse res, User user, User poster, URIParser uriParser, Exception e) throws SQLException, ModuleConfigurationException {

		log.error("Error processing flow instance " + instanceManager + " belonging to user " + user + ". Trying to save flow instance...", e);

		if(user != null){
			try {
				if(instanceManager.getFlowInstance().getStepID() != null){
				
					//TODO add flow instance event
					instanceManager.saveInstance(this, user, poster, EventType.UPDATED, instanceManager.getFlowInstance().getLastStatusChange());
					
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
	
}
