package com.nordicpeak.flowengine.savedmessagehandler;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.sql.DataSource;

import se.unlogic.hierarchy.core.comparators.PriorityComparator;
import se.unlogic.hierarchy.core.interfaces.SectionInterface;
import se.unlogic.hierarchy.core.interfaces.modules.descriptors.ForegroundModuleDescriptor;
import se.unlogic.hierarchy.foregroundmodules.AnnotatedForegroundModule;
import se.unlogic.standardutils.collections.CollectionUtils;

import com.nordicpeak.flowengine.interfaces.ImmutableFlowInstance;


public class SavedFlowInstanceMessageHandlerModule extends AnnotatedForegroundModule implements SavedFlowInstanceMessageHandler {

	private final CopyOnWriteArrayList<SavedFlowInstanceMessageProvider> providers = new CopyOnWriteArrayList<>();
	
	@Override
	public void init(ForegroundModuleDescriptor moduleDescriptor, SectionInterface sectionInterface, DataSource dataSource) throws Exception {

		super.init(moduleDescriptor, sectionInterface, dataSource);
		
		if (!systemInterface.getInstanceHandler().addInstance(SavedFlowInstanceMessageHandler.class, this)) {
			
			throw new RuntimeException("Unable to register module in global instance handler using key " + SavedFlowInstanceMessageHandler.class.getSimpleName() + ", another instance is already registered using this key.");
		}
	}

	@Override
	public void unload() throws Exception {

		systemInterface.getInstanceHandler().removeInstance(SavedFlowInstanceMessageHandler.class, this);
		
		super.unload();
	}	
	
	@Override
	public boolean addSavedFlowInstanceMessageProvider(SavedFlowInstanceMessageProvider provider) {

		boolean added = providers.add(provider);
		
		if(added) {
			
			providers.sort(PriorityComparator.ASC_COMPARATOR);
		}
		
		return added;
	}

	@Override
	public boolean removeSavedFlowInstanceMessageProvider(SavedFlowInstanceMessageProvider provider) {

		return providers.remove(provider);
	}

	@Override
	public List<SavedFlowInstanceMessageProvider> getProviders() {

		return Collections.unmodifiableList(providers);
	}
	
	@Override
	public List<String> getMessages(ImmutableFlowInstance flowInstance){
		
		List<String> messages = null;
		
		for(SavedFlowInstanceMessageProvider provider : providers) {
			
			String message;
			
			try {
				message = provider.getSavedInstanceMessage(flowInstance);
			
				messages = CollectionUtils.addAndInstantiateIfNeeded(messages, message);
				
			} catch (Exception e) {

				log.error("Error getting saved flow instance message from provider " + provider + " for flow family " + flowInstance.getFlow().getFlowFamily().getFlowFamilyID(), e);
			}
		}
		
		return messages;
	}
}
