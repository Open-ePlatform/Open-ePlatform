package com.nordicpeak.flowengine.integration.callback;

import java.util.List;

import javax.jws.WebService;
import javax.xml.datatype.XMLGregorianCalendar;

import se.unlogic.hiearchy.foregroundmodules.jaxws.BaseWSModuleService;
import se.unlogic.hiearchy.foregroundmodules.jaxws.WSModuleCallback;
import se.unlogic.hiearchy.foregroundmodules.jaxws.WSModuleInstanceResolver;
import se.unlogic.standardutils.random.RandomUtils;

import com.nordicpeak.flowengine.integration.callback.exceptions.AccessDenied;
import com.nordicpeak.flowengine.integration.callback.exceptions.AccessDeniedException;
import com.nordicpeak.flowengine.integration.callback.exceptions.FlowInstanceNotFound;
import com.nordicpeak.flowengine.integration.callback.exceptions.FlowInstanceNotFoundException;
import com.nordicpeak.flowengine.integration.callback.exceptions.StatusNotFound;
import com.nordicpeak.flowengine.integration.callback.exceptions.StatusNotFoundException;

@WebService(endpointInterface="com.nordicpeak.flowengine.integration.callback.IntegrationCallback", name="IntegrationCallback",serviceName="IntegrationCallback")
@WSModuleInstanceResolver
public class DummyIntegrationCallback extends BaseWSModuleService implements IntegrationCallback {
	
	@Override
	public void init(WSModuleCallback callback) throws Exception {

		super.init(callback);
		
		log.info("Initialized");
	}

	@Override
	public void unload() throws Exception {

		super.unload();
		
		log.info("Unloaded");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int setStatus(Integer flowInstanceID, ExternalID externalID, Integer statusID, String statusAlias, Principal principal) throws AccessDeniedException, FlowInstanceNotFoundException, StatusNotFoundException {

		log.info("User " + callback.getUser() + " requested setStatus");
		
		checkFlowInstanceID(flowInstanceID);
		
		if(statusAlias.equalsIgnoreCase("NotFound")){
			
			throw new StatusNotFoundException("Status with alias " + statusAlias + " not found", new StatusNotFound());
		}
		
		return RandomUtils.getRandomInt(0, Integer.MAX_VALUE);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int addEvent(Integer flowInstanceID, ExternalID externalID, XMLGregorianCalendar date, String message, Principal principal) throws AccessDeniedException, FlowInstanceNotFoundException {

		log.info("User " + callback.getUser() + " requested addEvent");
		
		checkFlowInstanceID(flowInstanceID);
		
		return RandomUtils.getRandomInt(0, Integer.MAX_VALUE);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int addMessage(Integer flowInstanceID, ExternalID externalID, IntegrationMessage message, Principal principal) throws AccessDeniedException, FlowInstanceNotFoundException {

		log.info("User " + callback.getUser() + " requested addMessage");
		
		checkFlowInstanceID(flowInstanceID);
		
		return RandomUtils.getRandomInt(0, Integer.MAX_VALUE);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void confirmDelivery(int flowInstanceID, ExternalID externalID, boolean delivered, String logMessage) throws AccessDeniedException, FlowInstanceNotFoundException {

		log.info("User " + callback.getUser() + " requested confirmDelivery");

		checkFlowInstanceID(flowInstanceID);
		
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void deleteInstance(Integer flowInstanceID, ExternalID externalID, String logMessage) throws AccessDeniedException, FlowInstanceNotFoundException {

		log.info("User " + callback.getUser() + " requested deleteInstance");

		checkFlowInstanceID(flowInstanceID);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int setManagers(Integer flowInstanceID, ExternalID externalID, List<Principal> managers, List<PrincipalGroup> managerGroups) throws AccessDeniedException, FlowInstanceNotFoundException {

		log.info("User " + callback.getUser() + " requested setManagers");
		
		checkFlowInstanceID(flowInstanceID);
		
		return RandomUtils.getRandomInt(0, Integer.MAX_VALUE);
	}
	
	private void checkFlowInstanceID(Integer flowInstanceID) throws FlowInstanceNotFoundException, AccessDeniedException {

		if(flowInstanceID != null){
		
			if(flowInstanceID < 1){
				
				throw new FlowInstanceNotFoundException("Flow instance with flowInstanceID " + flowInstanceID, new FlowInstanceNotFound());
				
			}else if(flowInstanceID == 13){
				
				throw new AccessDeniedException("Access to flow instance " + flowInstanceID + " denied", new AccessDenied());
			}
		}
	}

	@Override
	public void setAttribute(Integer flowInstanceID, ExternalID externalID, String name, String value) throws AccessDeniedException, FlowInstanceNotFoundException {
		
		log.info("User " + callback.getUser() + " requested setAttribute");

		checkFlowInstanceID(flowInstanceID);
	}
}
