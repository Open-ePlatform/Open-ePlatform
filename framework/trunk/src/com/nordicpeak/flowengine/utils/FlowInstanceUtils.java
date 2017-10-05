package com.nordicpeak.flowengine.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.hierarchy.core.interfaces.AttributeHandler;
import se.unlogic.hierarchy.core.interfaces.MutableAttributeHandler;
import se.unlogic.standardutils.collections.CollectionUtils;

import com.nordicpeak.flowengine.Constants;
import com.nordicpeak.flowengine.beans.Contact;
import com.nordicpeak.flowengine.beans.FlowInstance;
import com.nordicpeak.flowengine.beans.FlowInstanceEvent;
import com.nordicpeak.flowengine.enums.EventType;
import com.nordicpeak.flowengine.interfaces.ContactQueryInstance;
import com.nordicpeak.flowengine.interfaces.ImmutableFlowInstance;
import com.nordicpeak.flowengine.interfaces.ImmutableFlowInstanceEvent;
import com.nordicpeak.flowengine.managers.FlowInstanceManager;

public class FlowInstanceUtils {
	
	public static String getSubmitterName(ImmutableFlowInstance flowInstance, String anonymousUserName) {
		
		String submitterName = getSubmitterName(flowInstance);
		
		if (submitterName == null) {
			
			return anonymousUserName;
		}
		
		return submitterName;
	}
	
	public static String getSubmitterName(ImmutableFlowInstance flowInstance) {
		
		if (flowInstance.getAttributeHandler().isSet("organizationName")) {
			
			String orgName = flowInstance.getAttributeHandler().getString("organizationName");
			
			if (flowInstance.getPoster() != null) {
				
				return orgName + " (" + getNameFromUser(flowInstance.getPoster()) + ")";
				
			} else if (flowInstance.getAttributeHandler().isSet("firstname")) {
				
				return orgName + " (" + getNameFromAttributes(flowInstance.getAttributeHandler()) + ")";
			}
			
			return orgName;
			
		} else if (flowInstance.getPoster() != null) {
			
			return getNameFromUser(flowInstance.getPoster());
			
		} else if (flowInstance.getAttributeHandler().isSet("firstname")) {
			
			return getNameFromAttributes(flowInstance.getAttributeHandler());
		}
		
		return null;
	}
	
	private static String getNameFromUser(User user) {
		
		return user.getFirstname() + " " + user.getLastname();
	}
	
	private static String getNameFromAttributes(AttributeHandler attributeHandler) {
		
		String firstName = attributeHandler.getString("firstname");
		String lastname = attributeHandler.getString("lastname");
		
		return firstName + " " + lastname;
	}
	
	public static FlowInstanceEvent getLatestPDFEvent(FlowInstance flowInstance) {
		
		if (CollectionUtils.isEmpty(flowInstance.getEvents())) {
			
			return null;
		}
		
		for (int i = flowInstance.getEvents().size() - 1; i > -1; i--) {
			
			FlowInstanceEvent event = flowInstance.getEvents().get(i);
			
			if (Boolean.TRUE.equals(event.getAttributeHandler().getBoolean("pdf"))) {
				
				return event;
			}
		}
		
		return null;
	}
	
	public static ImmutableFlowInstanceEvent getLatestSubmitEvent(ImmutableFlowInstance flowInstance){
		
		if (CollectionUtils.isEmpty(flowInstance.getEvents())) {
			
			return null;
		}
		
		for (int i = flowInstance.getEvents().size() - 1; i > -1; i--) {
			
			ImmutableFlowInstanceEvent event = flowInstance.getEvents().get(i);
			
			if (event.getEventType() == EventType.SUBMITTED) {
				
				return event;
			}
		}
		
		return null;
	}
	
	// Used when saving flow instance on submit and multipart signing complete
	public static void setContactAttributes(FlowInstanceManager flowInstanceManager, MutableAttributeHandler flowInstanceAttributeHandler) {
		
		List<ContactQueryInstance> contactQueryInstances = flowInstanceManager.getQueries(ContactQueryInstance.class);
		
		if (!CollectionUtils.isEmpty(contactQueryInstances)) {
			
			for (ContactQueryInstance contactQueryInstance : contactQueryInstances) {
				
				if (contactQueryInstance.getQueryInstanceDescriptor().isPopulated() && !contactQueryInstance.updatedPoster()) {
					
					Contact contact = contactQueryInstance.getContact();
					
					if (contact != null) {
						
						flowInstanceAttributeHandler.setAttribute("firstname", contact.getFirstname());
						flowInstanceAttributeHandler.setAttribute("lastname", contact.getLastname());
						flowInstanceAttributeHandler.setAttribute("address", contact.getAddress());
						flowInstanceAttributeHandler.setAttribute("zipCode", contact.getZipCode());
						flowInstanceAttributeHandler.setAttribute("postalAddress", contact.getPostalAddress());
						flowInstanceAttributeHandler.setAttribute("email", contact.getEmail());
						flowInstanceAttributeHandler.setAttribute("phone", contact.getPhone());
						flowInstanceAttributeHandler.setAttribute("mobilePhone", contact.getMobilePhone());
						flowInstanceAttributeHandler.setAttribute("citizenIdentifier", contact.getCitizenIdentifier());
						
						flowInstanceAttributeHandler.setAttribute("contactBySMS", Boolean.toString(contact.isContactBySMS()));
						
						flowInstanceAttributeHandler.setAttribute("organizationName", contact.getOrganizationName());
						flowInstanceAttributeHandler.setAttribute("organizationNumber", contact.getOrganizationNumber());
						
						return;
					}
				}
			}
			
			flowInstanceAttributeHandler.removeAttribute("firstname");
			flowInstanceAttributeHandler.removeAttribute("lastname");
			flowInstanceAttributeHandler.removeAttribute("address");
			flowInstanceAttributeHandler.removeAttribute("zipCode");
			flowInstanceAttributeHandler.removeAttribute("postalAddress");
			flowInstanceAttributeHandler.removeAttribute("email");
			flowInstanceAttributeHandler.removeAttribute("phone");
			flowInstanceAttributeHandler.removeAttribute("mobilePhone");
			flowInstanceAttributeHandler.removeAttribute("citizenIdentifier");
			
			flowInstanceAttributeHandler.removeAttribute("organizationName");
			flowInstanceAttributeHandler.removeAttribute("organizationNumber");
			
			flowInstanceAttributeHandler.removeAttribute("contactBySMS");
		}
	}
	
	public static void setDescriptionAttribute(MutableAttributeHandler attributeHandler) {
		
		boolean descriptionAttributeSet = attributeHandler.isSet(Constants.FLOW_INSTANCE_DESCRIPTION_ATTRIBUTE);
		
		if(!descriptionAttributeSet || (descriptionAttributeSet && attributeHandler.isSet(Constants.FLOW_INSTANCE_DESCRIPTION_GENERATED_ATTRIBUTE))){
		
			Map<String, String> attributeMap = attributeHandler.getAttributeMap();
			
			if (!CollectionUtils.isEmpty(attributeMap)) {
				
				ArrayList<String> descriptionAttributes = new ArrayList<String>(attributeMap.size());
				
				for (String attributeName : attributeMap.keySet()) {
					
					if (attributeName.startsWith(Constants.FLOW_INSTANCE_DESCRIPTION_ATTRIBUTE)) {
						
						descriptionAttributes.add(attributeName);
					}
				}
				
				if(!descriptionAttributes.isEmpty()){
					
					Collections.sort(descriptionAttributes);
					
					attributeHandler.setAttribute(Constants.FLOW_INSTANCE_DESCRIPTION_ATTRIBUTE, attributeHandler.getString(descriptionAttributes.get(descriptionAttributes.size()-1)));
					attributeHandler.setAttribute(Constants.FLOW_INSTANCE_DESCRIPTION_GENERATED_ATTRIBUTE, true);
					
					return;
				}
				
				attributeHandler.removeAttribute(Constants.FLOW_INSTANCE_DESCRIPTION_ATTRIBUTE);
				attributeHandler.removeAttribute(Constants.FLOW_INSTANCE_DESCRIPTION_GENERATED_ATTRIBUTE);
			}
		}
	}
}
